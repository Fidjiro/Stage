package com.example.florian.myapplication.Activities.FormActivities.Faune;

import android.widget.CheckBox;

import com.example.florian.myapplication.Database.CampagneDatabase.Inventaire;
import com.example.florian.myapplication.R;

/**
 * Activité formulaire pour les amphibiens
 */
public class AmphibienActivity extends FauneActivity {

    protected CheckBox presencePonte;

    protected String presencePonteValue;

    @Override
    protected void setView() {
        setContentView(R.layout.activity_form_amphib);
    }

    @Override
    protected Inventaire createPersonalInventaire() {
        return new Inventaire(ref_taxon, usrId, typeTaxon, lat, lon, dat, nb, obs, nbMale, nbFemale,presencePonteValue);
    }

    /**
     * Extension de la méthode initFields mère pour adapter au layout amphibien
     */
    @Override
    protected void initFields() {
        super.initFields();
        typeTaxon = 3;
        presencePonte = (CheckBox) findViewById(R.id.ponte);
    }

    @Override
    protected void setValuesFromUsrInput() {
        super.setValuesFromUsrInput();
        presencePonteValue = presencePonte.isChecked()+"";
    }
}
