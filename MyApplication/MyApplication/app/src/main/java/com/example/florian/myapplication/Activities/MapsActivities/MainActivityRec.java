package com.example.florian.myapplication.Activities.MapsActivities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.florian.myapplication.Activities.FormActivities.Faune.AmphibienActivity;
import com.example.florian.myapplication.Activities.FormActivities.Faune.FauneActivity;
import com.example.florian.myapplication.Activities.FormActivities.Faune.OiseauxActivity;
import com.example.florian.myapplication.Activities.FormActivities.Flore.FloreActivity;
import com.example.florian.myapplication.AutoComplete.ArrayAdapter.AutocompleteCustomArrayAdapter;
import com.example.florian.myapplication.AutoComplete.ArrayAdapter.AutocompleteCustomFrArrayAdapter;
import com.example.florian.myapplication.AutoComplete.ArrayAdapter.AutocompleteCustomLatinArrayAdapter;
import com.example.florian.myapplication.AutoComplete.AutoCompleteListeners.CustomAutoCompleteTextFrChangedListener;
import com.example.florian.myapplication.AutoComplete.AutoCompleteListeners.CustomAutoCompleteTextLatinChangedListener;
import com.example.florian.myapplication.AutoComplete.CustomAutoCompleteView;
import com.example.florian.myapplication.Database.LoadingDatabase.Taxon;
import com.example.florian.myapplication.R;

/**
 * Activité carte pour le recensement d'espèces
 */
public class MainActivityRec extends MainActivity {

    public AutocompleteCustomArrayAdapter myLatinAdapter;
    public AutocompleteCustomArrayAdapter myFrAdapter;

    public CustomAutoCompleteView myAutoCompleteLatin;
    public CustomAutoCompleteView myAutoCompleteFr;

    protected String nomLatinInput;
    protected String nomFrInput;

    protected final String PLANTAE = "Plantae";
    protected final String AVES = "Aves";
    protected final String AMPHIBIA = "Amphibia";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Button recenser = (Button) findViewById(R.id.recenser);
        Button validerRecensement = (Button) findViewById(R.id.validerRecensement);

        loadAutoComplete();

        recensementLayout = (LinearLayout) findViewById(R.id.recensementLayout);
        validerRecensement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(myAutoCompleteFr.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(myAutoCompleteLatin.getWindowToken(), 0);
                try {
                    Intent intent = generateValidationIntent();
                    recensementLayout.setVisibility(View.GONE);
                    startActivity(intent);
                } catch(Exception e){
                    e.printStackTrace();
                    AlertDialog box;
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivityRec.this);
                    builder.setTitle(getString(R.string.avertissement));
                    builder.setPositiveButton(getString(R.string.accord), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    builder.setMessage(getString(R.string.saisirTaxon));
                    box = builder.create();
                    box.show();
                }
            }
        });


        recenser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recensementLayout.setVisibility(View.VISIBLE);
                myAutoCompleteFr.setText("");
                myAutoCompleteLatin.setText("");
            }
        });
    }

    /**
     * Renvoie l'intent à lancer en utilisant generateGoodIntent et en lui ajoutant en extra la position de l'utilisateur
     * et les noms de l'espèce inséré par l'utilisateur
     *
     * @return L'intent à lancer
     */
    protected Intent generateValidationIntent(){
        setNamesfromUsrInput();
        Intent intent = generateGoodIntent();

        intent.putExtra("latitude",getUsrLatLong().getLatitude());
        intent.putExtra("longitude",getUsrLatLong().getLongitude());
        intent.putExtra("nomfr",nomFrInput);
        intent.putExtra("nomlatin",nomLatinInput);

        return intent;
    }

    /**
     * Génère le bon intent en fonction du type d'espèce insérer par l'utilisateur.
     *
     * @return Le bon intent correspondant
     */
    protected Intent generateGoodIntent(){
        if(usrInputIsPlantae()){
            return new Intent(this, FloreActivity.class);
        }else if(usrInputIsAves()){
            return new Intent(this, OiseauxActivity.class);
        }else if(usrInputIsAmphibia()){
            return new Intent(this, AmphibienActivity.class);
        } return new Intent(this, FauneActivity.class);
    }

    /**
     * Vérifie si l'espèce inséré par l'utilisateur est une plante
     * @return <code>True</code> si oui, <code>false</code> sinon
     */
    protected boolean usrInputIsPlantae(){
        String regne = dao.getRegne(new String[]{nomLatinInput,nomFrInput});
        return regne.equals(PLANTAE);
    }

    /**
     * Vérifie si l'espèce inséré par l'utilisateur est un oiseau
     * @return <code>True</code> si oui, <code>false</code> sinon
     */
    protected boolean usrInputIsAves(){
        String classe = dao.getClasse(new String[]{nomLatinInput,nomFrInput});
        return classe.equals(AVES);
    }

    /**
     * Vérifie si l'espèce inséré par l'utilisateur est un amphibien
     * @return <code>True</code> si oui, <code>false</code> sinon
     */
    protected boolean usrInputIsAmphibia(){
        String classe = dao.getClasse(new String[]{nomLatinInput,nomFrInput});
        return classe.equals(AMPHIBIA);
    }

    /**
     * Modifie les attribut nomLatinInput et nomFrInput en fonction de l'input de l'utilisateur
     */
    protected void setNamesfromUsrInput(){
        nomLatinInput = myAutoCompleteLatin.getText().toString();
        nomFrInput = myAutoCompleteFr.getText().toString();
    }

    @Override
    protected void setView() {
        setContentView(R.layout.activity_main_rec);
    }

    @Override
    protected void setRelocButton(View.OnClickListener listener) {
        Button reloc1 = (Button) findViewById(R.id.reloc1);
        reloc1.setOnClickListener(listener);
    }

    /**
     * Mets en place l'autocompletion
     */
    protected void loadAutoComplete(){
        try{
            // autocompletetextview is in activity_main.xml
            myAutoCompleteLatin = (CustomAutoCompleteView) findViewById(R.id.autoCompleteEspeceLatin);
            myAutoCompleteFr = (CustomAutoCompleteView) findViewById(R.id.autoCompleteEspeceFr);

            // add the listener so it will tries to suggest while the user types
            myAutoCompleteLatin.addTextChangedListener(new CustomAutoCompleteTextLatinChangedListener(this));
            myAutoCompleteFr.addTextChangedListener(new CustomAutoCompleteTextFrChangedListener(this));

            // ObjectItemData has no value at first
            Taxon[] ObjectItemDataLatin = new Taxon[0];
            Taxon[] ObjectItemDataFr = new Taxon[0];

            // set the custom ArrayAdapter
            myLatinAdapter = new AutocompleteCustomLatinArrayAdapter(this, R.layout.list_view_row, ObjectItemDataLatin);
            myAutoCompleteLatin.setAdapter(myLatinAdapter);

            myFrAdapter = new AutocompleteCustomFrArrayAdapter(this, R.layout.list_view_row, ObjectItemDataFr);
            myAutoCompleteFr.setAdapter(myFrAdapter);

        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void displayLayout(){
        ConstraintLayout layout = (ConstraintLayout)findViewById(R.id.missionLayout);
        layout.setVisibility(View.VISIBLE);
    }
}