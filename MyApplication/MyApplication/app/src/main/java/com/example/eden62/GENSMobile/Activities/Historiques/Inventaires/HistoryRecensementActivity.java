package com.example.eden62.GENSMobile.Activities.Historiques.Inventaires;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.example.eden62.GENSMobile.Activities.FormActivities.Faune.AmphibienActivity;
import com.example.eden62.GENSMobile.Activities.FormActivities.Faune.FauneActivity;
import com.example.eden62.GENSMobile.Activities.FormActivities.Faune.OiseauxActivity;
import com.example.eden62.GENSMobile.Activities.FormActivities.Flore.FloreActivity;
import com.example.eden62.GENSMobile.Activities.Historiques.HistoryActivity;
import com.example.eden62.GENSMobile.Activities.HttpActivity;
import com.example.eden62.GENSMobile.Database.CampagneDatabase.CampagneDAO;
import com.example.eden62.GENSMobile.Database.CampagneDatabase.Inventaire;
import com.example.eden62.GENSMobile.Database.LoadingDatabase.TaxUsrDAO;
import com.example.eden62.GENSMobile.R;
import com.example.eden62.GENSMobile.HistoryAdapters.InventaireAdapter;
import com.example.eden62.GENSMobile.Tools.MyHttpService;
import com.example.eden62.GENSMobile.Tools.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Request;
import okhttp3.Response;

public class HistoryRecensementActivity extends HistoryActivity<InventaireAdapter> {

    protected CampagneDAO campagneDao;
    protected TaxUsrDAO taxUsrDao;
    protected Button syncInvs;
    protected MyHttpService httpService;
    protected int idCampagne;
    protected ProgressDialog dial;
    protected SharedPreferences prefs;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        syncInvs = (Button) findViewById(R.id.createCampagne);
        httpService = new MyHttpService(this);

        prefs = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        idCampagne = prefs.getInt("idCampagne",0);

        if(getIntent().getBooleanExtra("createCampagne",false)){
            syncInvs.setVisibility(View.VISIBLE);
            deleteSelection.setVisibility(View.GONE);
        }else{
            syncInvs.setVisibility(View.GONE);
            deleteSelection.setVisibility(View.VISIBLE);
        }

        syncInvs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(adapter.getCheckedItemsStocker().isTwoDifferentsDay()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(HistoryRecensementActivity.this);
                    builder.setMessage("Une campagne ne peut être réalisée sur plusieurs jours. Veuillez sé" +
                            "lectionner uniquement des inventaires réalisés le même jour.");
                    builder.setPositiveButton(getString(R.string.accord), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    builder.create().show();
                }else{
                    //sync invs selectionnés
                    dial = ProgressDialog.show(HistoryRecensementActivity.this, "", "Synchronisation en cours...", true);
                    SendCampagneInfoTask task = new SendCampagneInfoTask(httpService.createSendInfoCampagneRequest(idCampagne,adapter.getCheckedItemsStocker().getCheckedItems().size()));
                    task.execute((Void)null);
                }

            }
        });
    }

    private class SendCampagneInfoTask extends AsyncTask<Void,Void,Boolean> {

        private final Request mRequete;
        private long _id;
        private String errMsg;

        SendCampagneInfoTask(Request requete) {
            mRequete = requete;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            try {
                if (!Utils.isConnected(HistoryRecensementActivity.this)) {
                    Snackbar.make(syncInvs, "Aucune connexion à internet.", Snackbar.LENGTH_LONG).show();
                    return false;
                }

                Response response = httpService.executeRequest(mRequete);
                if (!response.isSuccessful()) {
                    throw new IOException(response.toString());
                }

                final String body = response.body().string();

                JSONObject js = parseStringToJsonObject(body);
                return interpreteJson(js);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
                Snackbar.make(syncInvs, "Mauvaise forme de json",Snackbar.LENGTH_LONG).show();
            } return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {

            if (success) {
                final List<Inventaire> inventairesToSend = adapter.getCheckedItemsStocker().getCheckedItems();

                if(inventairesToSend.size() == 0) {
                    Snackbar.make(syncInvs, "Aucun inventaire à synchroniser", Snackbar.LENGTH_LONG).show();
                    return;
                }

                /*for(Inventaire inv : inventairesToSend){
                    SendDataTask task = new SendDataTask(httpService.createSendDataRequest(inv,idCampagne));
                    task.execute((Void) null);
                }*/
                idCampagne++;
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("idCampagne",idCampagne);
                editor.commit();
                dial.dismiss();
            } else {
                if(errMsg != null){
                    Snackbar.make(syncInvs, errMsg,Snackbar.LENGTH_SHORT).show();
                }else{
                    Snackbar.make(syncInvs,"Erreur sur l'inventaire " + _id,Snackbar.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        protected void onCancelled() { }

        private boolean interpreteJson(JSONObject json){
            int err = -1;
            _id = -1;
            boolean importStatus;
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
                if (!Utils.isConnected(HistoryRecensementActivity.this)) {
                    Snackbar.make(syncInvs, "Aucune connexion à internet.", Snackbar.LENGTH_LONG).show();
                    return false;
                }

                Response response = httpService.executeRequest(mRequete);
                if (!response.isSuccessful()) {
                    throw new IOException(response.toString());
                }

                final String body = response.body().string();

                JSONObject js = parseStringToJsonObject(body);
                return interpreteJson(js);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
                Snackbar.make(syncInvs, "Mauvaise forme de json",Snackbar.LENGTH_LONG).show();
            } return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {

            if (success) {
                Snackbar.make(syncInvs,"Synchronisation réussie",Snackbar.LENGTH_SHORT).show();
                campagneDao.deleteInventaire(_id);
            } else {
                if(errMsg != null){
                    Snackbar.make(syncInvs, errMsg,Snackbar.LENGTH_SHORT).show();
                }else{
                    Snackbar.make(syncInvs,"Erreur sur l'inventaire " + _id,Snackbar.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        protected void onCancelled() { }

        private boolean interpreteJson(JSONObject json){
            int err = -1;
            _id = -1;
            boolean importStatus;
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
    protected void setView() {
        setContentView(R.layout.activity_history_recensement);
    }

    @Override
    protected void openDatabases() {
        campagneDao = new CampagneDAO(this);
        taxUsrDao = new TaxUsrDAO(this);
        campagneDao.open();
        taxUsrDao.open();
    }

    /**
     * Lance le bon type de formulaire en fonction de l'inventaire
     * @param inv L'inventaire à consulter
     * @return L'intent du bon type de formulaire
     */
    protected Intent generateGoodIntent(Inventaire inv){
        Intent intent;
        if(usrInputIsPlantae(inv))
            intent = new Intent(this, FloreActivity.class);
        else if(usrInputIsAves(inv))
            intent = new Intent(this, OiseauxActivity.class);
        else if(usrInputIsAmphibia(inv))
            intent = new Intent(this, AmphibienActivity.class);
        else
            intent = new Intent(this, FauneActivity.class);
        intent.putExtra("selectedInv",inv);
        return intent;
    }

    /** Vérifie si l'espèce inséré par l'utilisateur est une plante
     * @return <code>True</code> si oui, <code>false</code> sinon
     */
    protected boolean usrInputIsPlantae(Inventaire inv){
        String regne = taxUsrDao.getRegne(new String[]{inv.getNomLatin(),inv.getNomFr()});
        return regne.equals(getString(R.string.plantae));
    }

    /**
     * Vérifie si l'espèce inséré par l'utilisateur est un oiseau
     * @return <code>True</code> si oui, <code>false</code> sinon
     */
    protected boolean usrInputIsAves(Inventaire inv){
        String classe = taxUsrDao.getClasse(new String[]{inv.getNomLatin(),inv.getNomFr()});
        return classe.equals(getString(R.string.aves));
    }

    /**
     * Vérifie si l'espèce inséré par l'utilisateur est un amphibien
     * @return <code>True</code> si oui, <code>false</code> sinon
     */
    protected boolean usrInputIsAmphibia(Inventaire inv){
        String classe = taxUsrDao.getClasse(new String[]{inv.getNomLatin(),inv.getNomFr()});
        return classe.equals(getString(R.string.amphibia));
    }

    @Override
    protected void actionOnItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        TextView nomsInvTxt = (TextView)view.findViewById(R.id.nomEspeceInv);
        TextView heureInvTxt = (TextView)view.findViewById(R.id.heureInventaire);

        String noms = nomsInvTxt.getText().toString();
        String[] splittedNoms;
        if(noms.contains(" - "))
            splittedNoms = noms.split(" - ");
        else {
            if(noms.contains(" sp."))
                noms = noms.replace(" sp.","");
            splittedNoms = new String[]{noms, ""};
        }
        String[] params = new String[] {splittedNoms[0],splittedNoms[1],heureInvTxt.getText().toString()};
        Inventaire selectedInventaire = campagneDao.getInventaireFromHistory(params);
        startActivity(generateGoodIntent(selectedInventaire));
    }

    @Override
    protected void setListViewHeader() {
        LayoutInflater inflater = getLayoutInflater();
        header = (ViewGroup)inflater.inflate(R.layout.row_item_inventaires,listItems,false);
    }

    @Override
    protected void setAdapter(){
        List<Inventaire> inventaires = campagneDao.getInventairesOfTheUsr(getCurrentUsrId());

        adapter = new InventaireAdapter(this,inventaires);
        listItems.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        campagneDao.close();
        taxUsrDao.close();
    }
}
