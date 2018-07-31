package com.example.eden62.GENSMobile.Activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.eden62.GENSMobile.Activities.MapsActivities.Recensement.MainActivityRec;
import com.example.eden62.GENSMobile.Activities.MapsActivities.Releve.MainActivityRel;
import com.example.eden62.GENSMobile.R;
import com.example.eden62.GENSMobile.Tools.LoadingMapDialog;

/**
 * Activité regroupant les boutons menant au différentes action possible de l'application
 */
public class HomeActivity extends AppCompatActivity {

    public final static int RESULT_CLOSE_ALL = 2;
    public static LoadingMapDialog lmd;
    LinearLayout buttonList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        lmd = new LoadingMapDialog(this);

        buttonList = (LinearLayout) findViewById(R.id.buttonList);

        Button obsPonctButton = (Button) findViewById(R.id.obs_ponc_button);
        obsPonctButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgress(true);
                startActivity(new Intent(HomeActivity.this, MainActivityRec.class));
            }
        });

        Button protocoleButton = (Button) findViewById(R.id.protocole_button);
        protocoleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgress(true);
                startActivity(new Intent(HomeActivity.this, MainActivityRec.class));
            }
        });

        Button releveBouton = (Button) findViewById(R.id.releve_button);
        releveBouton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgress(true);
                startActivity(new Intent(HomeActivity.this, MainActivityRel.class));
            }
        });

        Button syncButton = (Button) findViewById(R.id.sync_button);
        syncButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                synchroniserMission();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        buttonList.setVisibility(View.VISIBLE);
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    protected void showProgress(final boolean show) {
        buttonList.setVisibility(show ? View.INVISIBLE : View.VISIBLE);
        lmd.show(show);
    }

    /**
     * Renvoi sur la page de synchronisation de mission
     */
    protected void synchroniserMission() {
        Intent intent = new Intent(this, HttpActivity.class);
        startActivityForResult(intent, RESULT_CLOSE_ALL);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_CLOSE_ALL)
            finish();
        super.onActivityResult(requestCode, resultCode, data);
    }
}
