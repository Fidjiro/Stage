package com.example.eden62.GENSMobile.Activities.ProtocoleActivities.RNF;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eden62.GENSMobile.Database.RNFDatabase.RNFMeteo;
import com.example.eden62.GENSMobile.R;
import com.google.gson.Gson;

public class FormMeteoActivity extends AppCompatActivity {

    protected Spinner visibilite,precipitation,nebulosite,directionVent,vitesseVent;
    protected EditText temperatureAir;
    protected Button valider;

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

        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(meteoNotValidable())
                    actionWhenMeteoNotValidable();
                else {
                    RNFMeteo meteo = createMeteoFromFields();
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("meteo",meteo);
                    setResult(ChooseTransectActivity.RESULT_FILL_METEO, resultIntent);
                    finish();
                }
            }
        });
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
    private RNFMeteo createMeteoFromFields(){
        String visibiliteInput = (String) visibilite.getSelectedItem();
        String precipitationInput = (String) precipitation.getSelectedItem();
        String nebulositeInput = (String) nebulosite.getSelectedItem();
        float temperatureInput = Float.parseFloat(temperatureAir.getText().toString());
        String directionVentInput = (String) directionVent.getSelectedItem();
        String vitesseVentInput = (String) vitesseVent.getSelectedItem();

        return new RNFMeteo(visibiliteInput, precipitationInput, nebulositeInput, temperatureInput, directionVentInput, vitesseVentInput);
    }
}
