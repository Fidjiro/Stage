package com.example.eden62.GENSMobile.Activities.FormActivities.Faune;

import android.widget.CheckBox;

import com.example.eden62.GENSMobile.Database.CampagneDatabase.Inventaire;
import com.example.eden62.GENSMobile.R;
import com.example.eden62.GENSMobile.Tools.Utils;

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
        return new Inventaire(ref_taxon, Utils.getVerCode(this), nv_taxon, usrId, nomFrString, nomLatinString, typeTaxon, lat, lon, dat, heure, nb, obs, remarquesTxt, nbMale, nbFemale,presencePonteValue);
    }

    @Override
    protected void changeFieldsStates(boolean enabled) {
        super.changeFieldsStates(enabled);
        presencePonte.setEnabled(enabled);
    }

    @Override
    protected void setFieldsFromConsultedInv() {
        super.setFieldsFromConsultedInv();
        if(consultedInv.getPresencePonte().equals("true"))
            presencePonte.setChecked(true);
    }

    @Override
    protected void modifConsultedInventaire() {
        super.modifConsultedInventaire();

        consultedInv.setPresencePonte(presencePonteValue);
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
