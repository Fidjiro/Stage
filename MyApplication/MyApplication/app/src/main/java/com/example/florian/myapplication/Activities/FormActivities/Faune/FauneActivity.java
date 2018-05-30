package com.example.florian.myapplication.Activities.FormActivities.Faune;

import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.florian.myapplication.Activities.FormActivities.FormActivity;
import com.example.florian.myapplication.Database.CampagneDatabase.Inventaire;
import com.example.florian.myapplication.R;

/**
 * Activité formulaire pour la faune qui n'est pas ni oiseaux ni amphibien
 */
public class FauneActivity extends FormActivity {

    protected RadioGroup rg;
    protected RadioButton rb;
    protected TextView maleTextView, femaleTextView;
    protected static final String STRING_TO_ADD = " : ";
    protected EditText nbMaleText, nbFemaleText;
    protected CheckBox maleCheckbox,femaleCheckbox;
    protected String obs;

    protected int nbMale;
    protected int nbFemale;
    /**
     * Défini une valeur pour signifier l'abscence de mâle et/ou femelle
     */
    protected static final int ABSENCE_GENRE = -1;

    @Override
    protected void setView() {
        setContentView(R.layout.activity_form_faune);
    }

    @Override
    protected Inventaire createPersonalInventaire() {
        return new Inventaire(ref_taxon, usrId, nomFrString, nomLatinString, typeTaxon, lat, lon, dat, heure, nb, obs, nbMale, nbFemale);
    }

    /**
     * Extension de la méthode initFields mère pour adapter au layout faune
     */
    @Override
    protected void initFields() {
        super.initFields();
        typeTaxon = 0;
        rg = (RadioGroup) findViewById(R.id.typeObs);
        rb = (RadioButton)rg.getChildAt(1);
        maleTextView = (TextView) findViewById(R.id.maleText);
        femaleTextView = (TextView) findViewById(R.id.femaleText);
        nbMaleText = (EditText) findViewById(R.id.nbMale);
        nbFemaleText = (EditText) findViewById(R.id.nbFemale);
        maleCheckbox = (CheckBox) findViewById(R.id.maleCheckbox);
        femaleCheckbox = (CheckBox) findViewById(R.id.femaleCheckbox);

        nbMaleText.setOnFocusChangeListener(new MyFocusChangeListener());
        nbFemaleText.setOnFocusChangeListener(new MyFocusChangeListener());
        nombre.setOnFocusChangeListener(new MyFocusChangeListener());

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                rb.setError(null);
            }
        });

        maleCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                CharSequence maleText = maleTextView.getText();
                if(compoundButton.isChecked()){
                    nbMaleText.setVisibility(View.VISIBLE);
                    maleTextView.setText(maleText + STRING_TO_ADD);
                }else{
                    nbMaleText.setVisibility(View.INVISIBLE);
                    nbMaleText.setText("");
                    maleTextView.setText(maleText.subSequence(0,maleText.length() - STRING_TO_ADD.length()));
                }
            }
        });

        femaleCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                CharSequence femaleText = femaleTextView.getText();
                if(compoundButton.isChecked()){
                    nbFemaleText.setVisibility(View.VISIBLE);
                    femaleTextView.setText(femaleText + STRING_TO_ADD);
                }else{
                    nbFemaleText.setVisibility(View.INVISIBLE);
                    nbFemaleText.setText("");
                    femaleTextView.setText(femaleText.subSequence(0,femaleText.length() - STRING_TO_ADD.length()));
                }
            }
        });
    }

    /**
     * Vérifie si un bouton d'observation (vu ou entendu) est coché
     *
     * @return <code>true</code> si un bouton est coché, <code>false</code> sinon
     */
    protected boolean isCheckedObsButton(){
        return rg.getCheckedRadioButtonId() != -1;
    }

    @Override
    protected boolean formIsValidable() {
        return coherantNumberGenre() && isCheckedObsButton() && super.formIsValidable();
    }

    /**
     * Vérifie que le dénombrement soit cohérent avec le nombre de mâle/femmelle.
     * Si le champ dénombrement est vide, il est autorisé de dénombrer le nombre de mâle/femelle uniquement. On peut
     * connaître le nombre de mâle par exemple sans savoir exactement le nombre de femelle et donc le dénombrement. Si le
     * champ dénombrement n'est pas vide, le nombre de mâle/femelle doit être inférieur ou égal à celui-ci
     *
     * @return <code>True</code> si le dénombrement est cohérant, <code>false</code> sinon
     */
    protected boolean coherantNumberGenre(){
        Integer tot = getNbgenre();
        return nombre.getText().toString().isEmpty() || tot <= getDenombrement();
    }

    /**
     * Récupère le nombre total de mâle et femelle
     *
     * @return L'entier correspondant au nombre de mâle/femelle
     */
    protected int getNbgenre(){
        boolean isNbMaleTextEmpty = nbMaleText.getText().toString().isEmpty();
        boolean isNbFemaleTextEmpty = nbFemaleText.getText().toString().isEmpty();
        if(!isNbMaleTextEmpty){
            if(!isNbFemaleTextEmpty)
                return getNbFemale() + getNbMale();
            return getNbMale();
        }
        if(!isNbFemaleTextEmpty)
            return getNbFemale();
        return 0;
    }

    /**
     * Vérifie si la case mâle est cochée
     *
     * @return <code>True</code> si la case est cochée, <code>false</code> sinon
     */
    protected boolean isMaleChecked(){
        return maleCheckbox.isChecked();
    }

    /**
     * Vérifie si la case femelle est cochée
     *
     * @return <code>True</code> si la case est cochée, <code>false</code> sinon
     */
    protected boolean isFemaleChecked(){
        return femaleCheckbox.isChecked();
    }

    /**
     * Récupère le dénombrement du genre correspondant au paramètre lorsque la case de celui-ci est coché
     *
     * @param genreEditText Le champ du genre à récupérer
     * @return Le dénombrement de ce genre
     */
    protected int getNbGenreWhenChecked(EditText genreEditText){
        int res = 0;
        try{
            res = Integer.parseInt(genreEditText.getText().toString());
        }catch(Exception e){
            e.printStackTrace();
        }
        return res;
    }

    /**
     * Récupère le nombre de mâle lorsque la case de celui-ci est coché
     *
     * @return Le nombre de mâle
     * @see FauneActivity#getNbMale()
     */
    protected int getNbMaleWhenChecked(){
        return getNbGenreWhenChecked(nbMaleText);
    }

    /**
     * Récupère le nombre de mâle
     *
     * @return Le nombre de mâle
     */
    protected int getNbMale(){
        return isMaleChecked() ? getNbMaleWhenChecked() : ABSENCE_GENRE;
    }

    /**
     * Récupère le nombre de femelle lorsque la case de celui-ci est coché
     *
     * @return Le nombre de femelle
     * @see FauneActivity#getNbFemale()
     */
    protected int getNbFemaleWhenChecked(){
        return getNbGenreWhenChecked(nbFemaleText);
    }

    /**
     * Récupère le nombre de femelle
     *
     * @return Le nombre de femelle
     */
    protected int getNbFemale(){
        return isFemaleChecked() ? getNbFemaleWhenChecked() : ABSENCE_GENRE;
    }

    @Override
    protected void setValuesFromUsrInput() {
        super.setValuesFromUsrInput();

        RadioButton checkedButton = (RadioButton) findViewById(rg.getCheckedRadioButtonId());
        obs = checkedButton.getText().toString();
        nbMale = getNbMale();
        nbFemale = getNbFemale();
    }

    @Override
    protected void actionWhenFormNotValidable() {
        super.actionWhenFormNotValidable();

        if(!isCheckedObsButton()) {
            rb.setError(getString(R.string.error_radio_button));
            rb.requestFocus();
        }
        if(!coherantNumberGenre()){
            nombre.setError(getString(R.string.incoherantNbGenre));
            nombre.requestFocus();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        nombre.setError(null);
    }
}
