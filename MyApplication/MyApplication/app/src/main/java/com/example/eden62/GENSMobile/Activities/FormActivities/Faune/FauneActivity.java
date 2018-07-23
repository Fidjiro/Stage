package com.example.eden62.GENSMobile.Activities.FormActivities.Faune;

import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.eden62.GENSMobile.Activities.FormActivities.FormActivity;
import com.example.eden62.GENSMobile.Database.CampagneDatabase.Inventaire;
import com.example.eden62.GENSMobile.R;
import com.example.eden62.GENSMobile.Tools.Utils;

/**
 * Activité formulaire pour la faune qui n'est pas ni oiseaux ni amphibien
 */
public class FauneActivity extends FormActivity {

    protected RadioGroup rg;
    protected RadioButton buttonEntendu,buttonVu;
    protected TextView maleTextView, femaleTextView;
    protected EditText nbMaleText, nbFemaleText;
    protected CheckBox maleCheckbox,femaleCheckbox;

    protected static final String STRING_TO_ADD = " : ";
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
        return new Inventaire(ref_taxon, Utils.getVerCode(this), nv_taxon, usrId, nomFrString, nomLatinString, typeTaxon, lat, lon, dat, heure, nb, obs, remarquesTxt, nbMale, nbFemale);
    }

    @Override
    protected void changeFieldsStates(boolean enabled) {
        super.changeFieldsStates(enabled);
        buttonVu.setEnabled(enabled);
        buttonEntendu.setEnabled(enabled);
        nbFemaleText.setEnabled(enabled);
        nbMaleText.setEnabled(enabled);
        maleCheckbox.setEnabled(enabled);
        femaleCheckbox.setEnabled(enabled);
    }

    @Override
    protected void setFieldsFromConsultedInv() {
        super.setFieldsFromConsultedInv();

        if(consultedInv.getType_obs().equals("Vu"))
            buttonVu.setChecked(true);
        else
            buttonEntendu.setChecked(true);

        int invNbMale = consultedInv.getNbMale();
        int invNbFemale = consultedInv.getNbFemale();
        setGenreFields(invNbMale,maleCheckbox,nbMaleText);
        setGenreFields(invNbFemale,femaleCheckbox,nbFemaleText);
    }

    @Override
    protected void modifConsultedInventaire() {
        super.modifConsultedInventaire();

        consultedInv.setType_obs(obs);
        consultedInv.setNbFemale(nbFemale);
        consultedInv.setNbMale(nbMale);
    }

    /**
     * Change le champ genre correspondant via les données de l'inventaire consulté.
     * Méthode valable pour le champ mâle et femelle
     *
     * @param nbGenre Décompte du genre
     * @param checkBox La checkBox correspondante au genre
     * @param editText L'editText correspondante au genre
     */
    protected void setGenreFields(int nbGenre, CheckBox checkBox, EditText editText){
        if(nbGenre > -1){
            checkBox.setChecked(true);
            if(nbGenre > 0)
                editText.setText(nbGenre + "");
        }
    }

    /**
     * Extension de la méthode initFields mère pour adapter au layout faune
     */
    @Override
    protected void initFields() {
        super.initFields();
        typeTaxon = 0;
        rg = (RadioGroup) findViewById(R.id.typeObs);
        buttonEntendu = (RadioButton) findViewById(R.id.buttonEntendu);
        buttonVu = (RadioButton) findViewById(R.id.buttonVu);
        maleTextView = (TextView) findViewById(R.id.maleText);
        femaleTextView = (TextView) findViewById(R.id.femaleText);
        nbMaleText = (EditText) findViewById(R.id.nbMale);
        nbFemaleText = (EditText) findViewById(R.id.nbFemale);
        maleCheckbox = (CheckBox) findViewById(R.id.maleCheckbox);
        femaleCheckbox = (CheckBox) findViewById(R.id.femaleCheckbox);

        nbMaleText.setOnFocusChangeListener(new MyFocusChangeListener());
        nbMaleText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.requestFocus();
            }
        });
        nbFemaleText.setOnFocusChangeListener(new MyFocusChangeListener());
        nbFemaleText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.requestFocus();
            }
        });
        nombre.setOnFocusChangeListener(new MyFocusChangeListener());

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                buttonEntendu.setError(null);
            }
        });

        maleCheckbox.setOnCheckedChangeListener(new MyGenreOnCheckedChangeListener(maleTextView,nbMaleText));

        femaleCheckbox.setOnCheckedChangeListener(new MyGenreOnCheckedChangeListener(femaleTextView,nbFemaleText));
    }

    /**
     * Listener utilisé pour les Checkboxes de genre
     */
    private class MyGenreOnCheckedChangeListener implements CompoundButton.OnCheckedChangeListener {

        TextView genreTv;
        EditText nbGenreEdt;

        public MyGenreOnCheckedChangeListener(TextView genreTv, EditText nbGenreEdt) {
            this.genreTv = genreTv;
            this.nbGenreEdt = nbGenreEdt;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            genreCheckboxOnCheckedChangeAction(isChecked, genreTv, nbGenreEdt);
        }

        // Change l'affichage des champs genre, si on coche la checkbox, on ajoute ":" au label et on affiche l'editText,
        // inversement si on décoche la checkbox
        private void genreCheckboxOnCheckedChangeAction(boolean checked, TextView genreTv, EditText nbGenreEdt) {
            if (checked) {
                addStringToAddToGenreLabel(genreTv);
                showNbGenreField(nbGenreEdt);
            } else {
                removeStringToAddToGenreLabel(genreTv);
                hideNbGenreField(nbGenreEdt);
            }
        }

        protected void showNbGenreField(EditText nbGenreEdt) {
            nbGenreEdt.setVisibility(View.VISIBLE);
        }

        protected void hideNbGenreField(EditText nbGenreEdt) {
            nbGenreEdt.setVisibility(View.INVISIBLE);
            nbGenreEdt.setText("");
        }

        protected void addStringToAddToGenreLabel(TextView genreTv) {
            genreTv.setText(genreTv.getText() + STRING_TO_ADD);
        }

        protected void removeStringToAddToGenreLabel(TextView genreTv) {
            CharSequence genreText = genreTv.getText();
            genreTv.setText(genreText.subSequence(0, genreText.length() - STRING_TO_ADD.length()));
        }
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
        String nombreText = nombre.getText().toString();
        return nombreText.isEmpty() || getNbgenre() <= getDenombrement();
    }

    /**
     * Récupère le nombre total de mâle et femelle
     *
     * @return L'entier correspondant au nombre de mâle/femelle
     */
    protected int getNbgenre(){
        return getNbFemaleWhenChecked() + getNbMaleWhenChecked();
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
            buttonEntendu.setError(getString(R.string.error_radio_button));
            buttonEntendu.requestFocus();
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
