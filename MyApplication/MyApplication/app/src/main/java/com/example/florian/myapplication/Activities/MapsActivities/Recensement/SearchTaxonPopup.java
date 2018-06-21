package com.example.florian.myapplication.Activities.MapsActivities.Recensement;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.example.florian.myapplication.Database.LoadingDatabase.TaxUsrDAO;
import com.example.florian.myapplication.Database.LoadingDatabase.Taxon;
import com.example.florian.myapplication.R;
import com.example.florian.myapplication.Tools.Utils;

public class SearchTaxonPopup extends AppCompatActivity {

    public AutocompleteCustomArrayAdapter myLatinAdapter;
    public AutocompleteCustomArrayAdapter myFrAdapter;

    public CustomAutoCompleteView myAutoCompleteLatin;
    public CustomAutoCompleteView myAutoCompleteFr;

    protected String nomLatinInput;
    protected String nomFrInput;

    protected double lat;
    protected double lon;

    protected final String PLANTAE = "Plantae";
    protected final String AVES = "Aves";
    protected final String AMPHIBIA = "Amphibia";

    public TaxUsrDAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search_taxon_popup);

        dao = new TaxUsrDAO(this);
        dao.open();

        Intent intent = getIntent();
        lat = intent.getDoubleExtra("latitude",0.0);
        lon = intent.getDoubleExtra("longitude",0.0);

        loadAutoComplete();

        Button validerRecensement = (Button) findViewById(R.id.validerRecensement);

        validerRecensement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(myAutoCompleteFr.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(myAutoCompleteLatin.getWindowToken(), 0);
                try {
                    Intent intent = generateValidationIntent();
                    startActivity(intent);
                    finish();
                } catch(Exception e){
                    e.printStackTrace();
                    AlertDialog box;
                    AlertDialog.Builder builder = new AlertDialog.Builder(SearchTaxonPopup.this);
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

        intent.putExtra("nomfr",nomFrInput);
        intent.putExtra("nomlatin",nomLatinInput);
        intent.putExtra("heure", Utils.getTime());
        intent.putExtra("latitude",lat);
        intent.putExtra("longitude",lon);

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

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0,R.anim.exit_from_top);
    }
}
