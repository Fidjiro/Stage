package com.example.eden62.GENSMobile.Activities.ProtocoleActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eden62.GENSMobile.Activities.ProtocoleActivities.RNF.ChooseTransectActivity;
import com.example.eden62.GENSMobile.Database.SaisiesProtocoleDatabase.CampagneProtocolaire;
import com.example.eden62.GENSMobile.Database.SaisiesProtocoleDatabase.CampagneProtocolaireDao;
import com.example.eden62.GENSMobile.Database.SaisiesProtocoleDatabase.ProtocoleMeteo;
import com.example.eden62.GENSMobile.Database.SaisiesProtocoleDatabase.RNF.RNFSaisie;
import com.example.eden62.GENSMobile.Database.SaisiesProtocoleDatabase.Saisie;
import com.example.eden62.GENSMobile.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Activité formulaire permettant de renseigner une météo
 */
public class FormMeteoActivity extends AppCompatActivity {

    protected Spinner visibilite,precipitation,nebulosite,directionVent,vitesseVent;
    protected EditText temperatureAir;
    protected Button valider;

    protected Intent callerIntent;
    protected boolean modification = false;
    protected CampagneProtocolaireDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_form_meteo);

        visibilite = (Spinner) findViewById(R.id.visibilite);
        precipitation = (Spinner) findViewById(R.id.precipitation);
        nebulosite = (Spinner) findViewById(R.id.nebulosite);
        directionVent = (Spinner) findViewById(R.id.directionVent);
        vitesseVent = (Spinner) findViewById(R.id.vitesseVent);
        temperatureAir = (EditText) findViewById(R.id.temperature);
        valider = (Button) findViewById(R.id.validerMeteo);

        dao = new CampagneProtocolaireDao(this);
        dao.open();
        callerIntent = getIntent();

        ProtocoleMeteo modifiedMeteo = callerIntent.getParcelableExtra("modifiedMeteo");
        if(modifiedMeteo != null) {
            setFieldsFromModifiedMeteo(modifiedMeteo);
            modification = true;
        }

        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(meteoNotValidable())
                    actionWhenMeteoNotValidable();
                else {
                    ProtocoleMeteo meteo = createMeteoFromFields();
                    if(modification){
                        CampagneProtocolaire c = dao.getCampagneById(callerIntent.getLongExtra("campagneId",0));
                        // On doit réutilisiser getGoodClass car sinon Gson n'arrive pas à parser correctement le Json si
                        // l'on met Saisie.class
                        Saisie s = c.getSaisieFromJson(getGoodClass(c));
                        s.setMeteo(meteo);
                        c.setSaisie(new Gson().toJson(s));
                        dao.modifieCampagne(c);
                    }else {
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("meteo", meteo);
                        setResult(ChooseTransectActivity.RESULT_FILL_METEO, resultIntent);
                    }
                    finish();
                }
            }
        });

    }

    /**
     * Récupère le bon type de classe de saisie pour cette campagne via le nom du protocole que cette-ci suit
     *
     * @param c La campagne protocolaire
     * @return La classe saisie correspondante au protocole de cette campagne
     */
    private Class getGoodClass(CampagneProtocolaire c){
        if(c.getNomProto().equals(getString(R.string.nomRNF)))
            return RNFSaisie.class;
        // On ajoutera des if si d'autre protocoles sont implémentés
        return null;
    }

    // Affecte les champs via la météo fourni via Intent
    private void setFieldsFromModifiedMeteo(ProtocoleMeteo modifiedMeteo) {
        setSpinnerSelection(visibilite,R.array.chooseVisibilite,modifiedMeteo.getVisibilite());
        setSpinnerSelection(precipitation,R.array.choosePrecipitation,modifiedMeteo.getPrecipitation());
        setSpinnerSelection(nebulosite,R.array.chooseNebulosite,modifiedMeteo.getNebulosite());
        setSpinnerSelection(directionVent,R.array.chooseDirectionVent,modifiedMeteo.getDirectionVent());
        setSpinnerSelection(vitesseVent,R.array.chooseVitesseVent,modifiedMeteo.getVitesseVent());
        temperatureAir.setText(modifiedMeteo.getTemperatureAir() + "");
    }

    // Mets la string object dans la liste de string d'identifiant id en sélection du spinner
    private void setSpinnerSelection(Spinner spinner, int id, String object){
        String[] stringArray = getResources().getStringArray(id);
        ArrayList<String> stringList = new ArrayList<>(Arrays.asList(stringArray));
        spinner.setSelection(stringList.indexOf(object));
    }

    // Renvoie true si l'un des champs nebulosités, temperature ou vitesse du vent n'a pas été renseigné
    private boolean meteoNotValidable() {
        return isNotANebulosite() || isNotATemperature() || isNotAVitesseVent();
    }

    // Renvoie true si le champ nebulosité n'a pas été renseigné
    private boolean isNotANebulosite(){
        String selectedNebulosite = (String) nebulosite.getSelectedItem();

        return selectedNebulosite.isEmpty();
    }

    // Renvoie true si le champ temperature n'a pas été renseigné
    private boolean isNotATemperature(){
        String temperatureInput = temperatureAir.getText().toString();

        return temperatureInput.isEmpty();
    }

    // Renvoie true si le champ vitesse du vent n'a pas été renseigné
    private boolean isNotAVitesseVent(){
        String selectedVitesseVent = (String) vitesseVent.getSelectedItem();

        return selectedVitesseVent.isEmpty();
    }

    // Affiche un message en fonction du champ non renseigné
    private void actionWhenMeteoNotValidable(){
        if(isNotANebulosite()){
            ((TextView)nebulosite.getSelectedView()).setError("");
            Toast.makeText(this,"Veuillez choisir un type de nébulosité",Toast.LENGTH_SHORT).show();
        }
        if(isNotATemperature())
            temperatureAir.setError("Veuillez insérer une température");
        if(isNotAVitesseVent()){
            ((TextView)vitesseVent.getSelectedView()).setError("");
            Toast.makeText(this, "Veuillez choisir une vitese de vent",Toast.LENGTH_SHORT).show();
        }
    }

    // Créé l'objet météo via les champs du formulaire
    private ProtocoleMeteo createMeteoFromFields(){
        String visibiliteInput = (String) visibilite.getSelectedItem();
        String precipitationInput = (String) precipitation.getSelectedItem();
        String nebulositeInput = (String) nebulosite.getSelectedItem();
        float temperatureInput = Float.parseFloat(temperatureAir.getText().toString());
        String directionVentInput = (String) directionVent.getSelectedItem();
        String vitesseVentInput = (String) vitesseVent.getSelectedItem();

        return new ProtocoleMeteo(visibiliteInput, precipitationInput, nebulositeInput, temperatureInput, directionVentInput, vitesseVentInput);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dao.close();
    }
}
