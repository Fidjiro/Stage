package com.example.eden62.GENSMobile.Activities.MapsActivities.Recensement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.eden62.GENSMobile.Activities.Historiques.Inventaires.HistoryRecensementActivity;
import com.example.eden62.GENSMobile.Activities.HomeActivity;
import com.example.eden62.GENSMobile.Activities.MapsActivities.MainActivity;
import com.example.eden62.GENSMobile.R;
import com.example.eden62.GENSMobile.Tools.LoadingMapDialog;

/**
 * Activité carte pour le recensement d'espèces
 */
public class MainActivityRec extends MainActivity {

    protected Button mesRecensement;

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void initFields() {
        super.initFields();

        Button recenser = (Button) findViewById(R.id.recenser);

        mesRecensement = (Button) findViewById(R.id.mesCampagnes);

        mesRecensement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivityRec.this,HistoryRecensementActivity.class);
                intent.putExtra("createCampagne",false);
                startActivity(intent);
            }
        });

        recenser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivityRec.this,SearchTaxonPopup.class);
                intent.putExtra("latitude",getUsrLatLong().getLatitude());
                intent.putExtra("longitude",getUsrLatLong().getLongitude());
                startActivity(intent);
                overridePendingTransition(R.anim.enter_from_top,0);
            }
        });

        HomeActivity.lmd.show(false);
    }

    @Override
    protected void setView() {
        setContentView(R.layout.activity_main_rec);
    }
}