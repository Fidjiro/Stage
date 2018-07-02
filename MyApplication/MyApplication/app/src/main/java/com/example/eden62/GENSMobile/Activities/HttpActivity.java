package com.example.eden62.GENSMobile.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.eden62.GENSMobile.Activities.Historiques.Inventaires.HistoryRecensementActivity;
import com.example.eden62.GENSMobile.Activities.Historiques.Releves.HistoryReleveActivity;
import com.example.eden62.GENSMobile.Database.CampagneDatabase.CampagneDAO;
import com.example.eden62.GENSMobile.Database.CampagneDatabase.Inventaire;
import com.example.eden62.GENSMobile.Database.ReleveDatabase.HistoryDao;
import com.example.eden62.GENSMobile.R;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * IN PROGRESS
 */
public class HttpActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout psswLayout,nbInvLayout, nbRelLayout;
    Button launchSync,validPssw;
    TextView txtJson, nbInvToSyncTxt, nbRelToSyncTxt;
    EditText psswText;
    private Snackbar snackbar;

    int nbInvToSync, nbRelToSync;

    private CookieJar cookieJar;
    private OkHttpClient client;
    private String username;
    private long usrId;
    static final String URL_CONNEXION = "http://vps122669.ovh.net:8080/connexion.php";
    static final String URL_ADD_DATA = "http://vps122669.ovh.net:8080/addData.php";

    private CampagneDAO campagneDao;
    private HistoryDao releveDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http);

        campagneDao = new CampagneDAO(this);
        releveDao = new HistoryDao(this);
        campagneDao.open();
        releveDao.open();


        cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(this));
        client = new OkHttpClient.Builder().cookieJar(cookieJar).build();

        makeView();

        SharedPreferences loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        username = loginPreferences.getString("username","");
        usrId = loginPreferences.getLong("usrId" , 0);
        setTxtNbDatas();

        snackbar = Snackbar.make(txtJson, "Requête en cours d'exécution",
                Snackbar.LENGTH_INDEFINITE);
    }

    private void makeView() {
        launchSync = (Button) findViewById(R.id.btnHit);
        txtJson = (TextView) findViewById(R.id.tvJsonItem);
        validPssw = (Button) findViewById(R.id.validPssw);
        psswText = (EditText) findViewById(R.id.password);
        psswLayout = (LinearLayout) findViewById(R.id.passwordLayout);
        nbInvLayout = (LinearLayout) findViewById(R.id.invToSyncLayout);
        nbRelLayout = (LinearLayout) findViewById(R.id.relToSyncLayout);
        validPssw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isConnected()) {
                    Snackbar.make(txtJson, "Aucune connexion à internet.", Snackbar.LENGTH_LONG).show();
                    return;
                }
                String mdp = psswText.getText().toString();
                Log.w("Mdp: ", mdp);
                RequestBody requestBody = new FormBody.Builder().add("log",username).add("mdp",mdp).build();
                final Request request = new Request.Builder().url(URL_CONNEXION).post(requestBody).build();
                AttemptLoginTask task = new AttemptLoginTask(request);
                task.execute((Void)null);
            }
        });
        launchSync.setOnClickListener(this);
        nbInvToSyncTxt = (TextView) findViewById(R.id.nbInvToSync);
        nbRelToSyncTxt = (TextView) findViewById(R.id.nbRelToSync);
        nbInvLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HttpActivity.this, HistoryRecensementActivity.class);
                startActivity(intent);
            }
        });
        nbRelLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HttpActivity.this, HistoryReleveActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getNbDataToSync(){
        nbInvToSync = campagneDao.getNbInventairesOfTheUsr(usrId);
        nbRelToSync = releveDao.getNbReleveOfTheUsr(usrId);
    }

    protected void setTxtNbDatas(){
        getNbDataToSync();
        nbInvToSyncTxt.setText(nbInvToSync + " " + getString(R.string.invToSync));
        nbRelToSyncTxt.setText(nbRelToSync + " " + getString(R.string.relToSync));
    }

    @Override
    public void onClick(final View view) {
        if (!isConnected()) {
            Snackbar.make(view, "Aucune connexion à internet.", Snackbar.LENGTH_LONG).show();
            return;
        }
        snackbar.show();

        RequestBody requestBody;
        List<Inventaire> inventairesToSend = campagneDao.getInventaireOfTheUsr(usrId);

        if(inventairesToSend.size() == 0)
            Snackbar.make(txtJson, "Aucun inventaire à synchroniser", Snackbar.LENGTH_LONG).show();

        for(Inventaire inv : inventairesToSend){
            requestBody = createRequestBodyToSend(inv);
            final Request request = new Request.Builder().url(URL_ADD_DATA).post(requestBody).build();
            SendDataTask task = new SendDataTask(request);
            task.execute((Void) null);
        }

    }

    private class SendDataTask extends AsyncTask<Void,Void,Boolean>{

        private final Request mRequete;
        private long _id;
        private String errMsg;

        SendDataTask(Request requete) {
            mRequete = requete;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            snackbar.show();

            try {
                Response response = client.newCall(mRequete).execute();
                if (!response.isSuccessful()) {
                    throw new IOException(response.toString());
                }

                final String body = response.body().string();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        txtJson.setText(body);
                        snackbar.dismiss();
                    }
                });

                JSONObject js = parseStringToJsonObject(body);
                return interpreteJson(js);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
                Snackbar.make(txtJson, "Mauvaise forme de json",Snackbar.LENGTH_LONG).show();
            } return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {

            if (success) {
                Snackbar.make(txtJson,"Synchronisation réussie",Snackbar.LENGTH_SHORT).show();
                campagneDao.deleteInventaire(_id);
            } else {
                if(errMsg != null){
                    Snackbar.make(txtJson, errMsg,Snackbar.LENGTH_SHORT).show();
                }else{
                    Snackbar.make(txtJson,"Erreur sur l'inventaire " + _id,Snackbar.LENGTH_SHORT).show();
                }
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setTxtNbDatas();
                }
            });
        }

        @Override
        protected void onCancelled() {
            //showProgress(false);
        }

        private boolean interpreteJson(JSONObject json){
            int err = -1;
            _id = -1;
            boolean importStatus = false;
            try{
                err = json.getInt("err");
                _id = json.getLong("_id");
                importStatus = json.getBoolean("import");
            } catch (JSONException e) {
                e.printStackTrace();
                errMsg = "Mauvais parsage de JSON";
                return false;
            }
            return importStatus;
        }
    }

    private class AttemptLoginTask extends AsyncTask<Void,Void,Boolean>{

        private final Request mRequete;

        AttemptLoginTask(Request requete) {
            mRequete = requete;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            snackbar.show();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    psswText.setError(null);
                }
            });

            try {
                Response response = client.newCall(mRequete).execute();
                if (!response.isSuccessful()) {
                    throw new IOException(response.toString());
                }

                final String body = response.body().string();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        txtJson.setText(body);
                        snackbar.dismiss();
                    }
                });

                JSONObject js = parseStringToJsonObject(body);
                return interpreteJson(js);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
                Snackbar.make(txtJson, "Mauvaise forme de json",Snackbar.LENGTH_LONG).show();
            } return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            //showProgress(false);

            if (success) {
                Snackbar.make(txtJson,"Connexion réussie",Snackbar.LENGTH_SHORT).show();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        psswLayout.setVisibility(View.GONE);
                        setTxtNbDatas();
                        nbInvLayout.setVisibility(View.VISIBLE);
                        nbRelLayout.setVisibility(View.VISIBLE);
                        launchSync.setVisibility(View.VISIBLE);
                        final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                    }
                });
            } else {
                psswText.setError(getString(R.string.error_incorrect_login));
                psswText.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            //showProgress(false);
        }

        protected boolean interpreteJson(JSONObject json){
            int err = -1;
            int con = -1;
            String titre = "";
            String msg = "";
            try {
                err = json.getInt("err");
                con = json.getInt("con");
                titre = json.getString("titre");
                msg = json.getString("msg");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(err == 0){
                if(con == 0){
                    Log.w(titre,msg);
                    return false;
                }else if(con == 1){
                    Log.w("Connexion","Connexion réussi");
                    return true;
                }else {
                    Log.w("Déconnexion", "Se déconnecte");
                    return false;
                }
            }return false;
        }
    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    protected JSONObject parseStringToJsonObject(String s) throws JSONException {
        return new JSONObject(s);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        campagneDao.close();
    }

    private RequestBody createRequestBodyToSend(Inventaire inv){

        RequestBody requestBody = new FormBody.Builder().add("_id",inv.get_id() + "").
                add("ref_taxon",inv.getRef_taxon() + "").
                add("ref_user",inv.getUser() + "").
                add("typeTaxon",inv.getTypeTaxon() + "").
                add("latitude",inv.getLatitude() + "").
                add("longitude",inv.getLongitude() + "").
                add("date",inv.getDate()).
                add("nombre",inv.getNombre() + "").
                add("type_obs",inv.getType_obs()).
                add("nbMale",inv.getNbMale() + "").
                add("nbFemale",inv.getNbFemale() + "").
                add("presencePonte",inv.getPresencePonte()).
                add("activite",inv.getActivite()).
                add("statut",inv.getStatut()).
                add("nidif",inv.getNidif()).
                add("indice_abondance",inv.getIndiceAbondance() + "").
                build();
        return requestBody;
    }

    @Override
    protected void onResume() {
        super.onResume();
        setTxtNbDatas();
    }
}