package com.example.eden62.GENSMobile.Activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.eden62.GENSMobile.Database.CampagneDatabase.CampagneDAO;
import com.example.eden62.GENSMobile.Database.CampagneDatabase.Inventaire;
import com.example.eden62.GENSMobile.R;
import com.example.eden62.GENSMobile.Tools.AttemptLoginTask;
import com.example.eden62.GENSMobile.Tools.MyHttpService;
import com.example.eden62.GENSMobile.Tools.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;
import okhttp3.Response;

public class SyncInvActivity extends AppCompatActivity {

    private TextView txtJson;
    private MyHttpService httpService;
    private ProgressDialog dial;

    private ArrayList<Inventaire> inventairesToSend;
    private static int cpt, currTotalInv, nbErr;
    protected int idCampagne;

    protected SharedPreferences prefs;

    protected CampagneDAO campagneDao;

    protected String mdp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_inv);

        campagneDao = new CampagneDAO(this);
        campagneDao.open();

        dial = ProgressDialog.show(this, "", "Synchronisation en cours...", true);
        txtJson = (TextView) findViewById(R.id.tvJsonItem);
        httpService = new MyHttpService(this);
        prefs = getSharedPreferences("loginPrefs", MODE_PRIVATE);

        idCampagne = prefs.getInt("idCampagne",0);

        Intent intent = getIntent();
        inventairesToSend = intent.getParcelableArrayListExtra("inventairesToSend");
        mdp = intent.getStringExtra("mdp");
        currTotalInv = inventairesToSend.size();
        new SyncInvLoginTask(httpService.createConnectionRequest(prefs.getString("username",""),mdp),httpService).execute((Void)null);
    }

    private class SyncInvLoginTask extends com.example.eden62.GENSMobile.Tools.AttemptLoginTask{
        public SyncInvLoginTask(Request requete, MyHttpService httpService) {
            super(requete, httpService);
        }

        @Override
        protected void makeWrongJsonSnackbar() {
            Snackbar.make(txtJson, "Mauvaise forme de json",Snackbar.LENGTH_LONG).show();
        }

        @Override
        protected void updateTxtJson(final String body) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    txtJson.setText(body);
                }
            });
        }

        @Override
        protected void actionOnPostExecute(Boolean success) {
            if (success) {
                Snackbar.make(txtJson,"Connexion réussie",Snackbar.LENGTH_SHORT).show();
                new SendCampagneInfoTask(httpService.createSendInfoCampagneRequest(idCampagne,currTotalInv),inventairesToSend).execute((Void)null);
            }else{
                Snackbar.make(txtJson, "Connexion échouée",Snackbar.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    /**
     * Tâche qui envoi les infos sur la campagne créée par l'utilisateur
     */
    private class SendCampagneInfoTask extends AsyncTask<Void,Void,Boolean> {

        private final Request mRequete;
        private long _id;
        private String errMsg;
        private List<Inventaire> inventairesToSend;

        SendCampagneInfoTask(Request requete, List<Inventaire> invs) {
            mRequete = requete;
            inventairesToSend = invs;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            try {
                if (!Utils.isConnected(SyncInvActivity.this)) {
                    Snackbar.make(txtJson, "Aucune connexion à internet.", Snackbar.LENGTH_LONG).show();
                    return false;
                }

                Response response = httpService.executeRequest(mRequete);
                if (!response.isSuccessful()) {
                    throw new IOException(response.toString());
                }

                final String body = response.body().string();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        txtJson.setText(body);
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
                cpt = 0;
                nbErr = 0;
                for(Inventaire inv : inventairesToSend){
                    SendDataTask task = new SendDataTask(httpService.createSendDataRequest(inv,idCampagne));
                    task.execute((Void) null);
                }
                idCampagne++;
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("idCampagne",idCampagne);
                editor.commit();
            } else {
                if(errMsg != null){
                    Snackbar.make(txtJson, errMsg,Snackbar.LENGTH_SHORT).show();
                }else{
                    Snackbar.make(txtJson,"Erreur sur l'inventaire " + _id,Snackbar.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        protected void onCancelled() { }

        private boolean interpreteJson(JSONObject json){
            int err;
            _id = -1;
            boolean importStatus;
            try{
                err = json.getInt("err");
                importStatus = json.getBoolean("import");
                if(err == 1){
                    errMsg = json.getString("msg");
                    return false;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                errMsg = "Mauvais parsage de JSON";
                return false;
            }
            return importStatus;
        }

    }

    /**
     * Tâche qui permet d'envoyer un inventaire au serveur
     */
    private class SendDataTask extends AsyncTask<Void,Void,Boolean> {

        private final Request mRequete;
        private long _id;
        private String errMsg;

        SendDataTask(Request requete) {
            mRequete = requete;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            try {
                if (!Utils.isConnected(SyncInvActivity.this)) {
                    Snackbar.make(txtJson, "Aucune connexion à internet.", Snackbar.LENGTH_LONG).show();
                    return false;
                }

                Response response = httpService.executeRequest(mRequete);
                if (!response.isSuccessful()) {
                    throw new IOException(response.toString());
                }

                final String body = response.body().string();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        txtJson.setText(body);
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
            cpt++;
            if(cpt == currTotalInv ){
                dial.dismiss();
                if (nbErr > 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SyncInvActivity.this);
                    builder.setMessage(nbErr + goodFormatForWordInventaire() + "à plus de 100m hors des limites de sites");
                    builder.setPositiveButton(getString(R.string.accord), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            setResult(HttpActivity.END_OF_SYNC);
                            finish();
                        }
                    });
                    builder.create().show();
                }else {
                    setResult(HttpActivity.END_OF_SYNC);
                    finish();
                }
            }
        }

        private String goodFormatForWordInventaire(){
            if(nbErr > 1)
                return " inventaires ";
            return " inventaire ";
        }

        @Override
        protected void onCancelled() { }

        private boolean interpreteJson(JSONObject json){
            int err;
            _id = -1;
            boolean importStatus;
            try{
                err = json.getInt("err");
                _id = json.getLong("_id");
                importStatus = json.getBoolean("import");
                if(err == 1){
                    nbErr ++;
                    errMsg = json.getString("msg");
                    Inventaire inv = campagneDao.getInventaire(_id);
                    inv.setErr(1);
                    campagneDao.modifInventaire(inv);
                    return false;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                errMsg = "Mauvais parsage de JSON";
                return false;
            }
            return importStatus;
        }
    }

    /**
     * Transforme une chaîne de caractère en un objet JSON
     *
     * @param s La String à transformer
     * @return L'objet JSON correspondant à la String
     * @throws JSONException En cas d'echec de parsage
     */
    protected JSONObject parseStringToJsonObject(String s) throws JSONException {
        return new JSONObject(s);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        campagneDao.close();
    }
}
