package com.example.eden62.GENSMobile.Activities;

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

/**
 * Activité regroupant les boutons menant au différentes action possible de l'application
 */
public class HomeActivity extends AppCompatActivity {

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button obsPonctButton = (Button) findViewById(R.id.obs_ponc_button);
        obsPonctButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgress(true);
                HomeActivity.this.executeLaunchActivity(false);
            }
        });

        Button protocoleButton = (Button) findViewById(R.id.protocole_button);
        protocoleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgress(true);
                HomeActivity.this.executeLaunchActivity(false);
            }
        });

        Button releveBouton = (Button) findViewById(R.id.releve_button);
        releveBouton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgress(true);
                HomeActivity.this.executeLaunchActivity(true);
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

    /**
     * Lance l'activité correspondant à isReleveActivity
     *
     * @param isReleveActivity Si il vaut true, on lance la {@link MainActivityRel}, sinon {@link MainActivityRec}
     */
    protected void executeLaunchActivity(boolean isReleveActivity){
        final ProgressTask relActivityLaunch = new ProgressTask(isReleveActivity);
        relActivityLaunch.execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        showProgress(false);
    }

    /**
     * Génère l'intent en fonction du paramètre isReleveActivity
     *
     * @param isReleveActivity Si il vaut true, on lance la {@link MainActivityRel}, sinon {@link MainActivityRec}
     * @return Le bon intent
     */
    public Intent generateIntent (boolean isReleveActivity){
        Intent intent;
        if(isReleveActivity)
            intent = new Intent(HomeActivity.this,MainActivityRel.class);
        else
            intent = new Intent(HomeActivity.this,MainActivityRec.class);
        return intent;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    protected void showProgress(final boolean show) {

        LinearLayout buttonList = (LinearLayout) findViewById(R.id.buttonList);
        TextView mProgressView = (TextView) findViewById(R.id.action_progress);

        mProgressView.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
        buttonList.setVisibility(show ? View.INVISIBLE : View.VISIBLE);
    }

    /**
     * Renvoi sur la page de synchronisation de mission
     */
    protected void synchroniserMission(){
        Intent intent = new Intent(this,HttpActivity.class);
        startActivity(intent);
    }

    /**
     * Lance l'activité correspondant au bouton pressé par l'utilisateur et affiche un message de chargement
     */
    public class ProgressTask extends AsyncTask<Void, Void, Boolean> {

        private final boolean isRelActivity;

        public ProgressTask(boolean isRelActivity) {
            this.isRelActivity = isRelActivity;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            intent = generateIntent(isRelActivity);
            startActivity(intent);
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) { }

        @Override
        protected void onCancelled() {
            showProgress(false);
        }
    }
}
