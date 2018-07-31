package com.example.eden62.GENSMobile.Activities.Historiques.Inventaires;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.eden62.GENSMobile.Activities.SyncInvActivity;
import com.example.eden62.GENSMobile.Database.CampagneDatabase.CampagneDAO;
import com.example.eden62.GENSMobile.Database.CampagneDatabase.Inventaire;
import com.example.eden62.GENSMobile.Database.LoadingDatabase.TaxUsrDAO;
import com.example.eden62.GENSMobile.R;
import com.example.eden62.GENSMobile.HistoryAdapters.InventaireAdapter;
import com.example.eden62.GENSMobile.Tools.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Historique des inventaires non synchronisés ou hors sites de l'utilisateur
 */
public class HistoryRecensementActivity extends HistoryActivity<InventaireAdapter> {

    protected CampagneDAO campagneDao;
    protected TaxUsrDAO taxUsrDao;

    protected Button syncInvs;
    protected SharedPreferences prefs;
    protected TextView txtJson;

    protected String mdp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        displayGoodButton(intent.getBooleanExtra("createCampagne",false));
        mdp = intent.getStringExtra("mdp");
    }

    private void displayGoodButton(boolean createCampagne){
        syncInvs.setVisibility(createCampagne ? View.VISIBLE : View.GONE);
        deleteSelection.setVisibility(createCampagne ? View.GONE : View.VISIBLE);
    }

    @Override
    protected void initFields() {
        super.initFields();

        txtJson = (TextView) findViewById(R.id.txtJson);
        syncInvs = (Button) findViewById(R.id.createCampagne);

        prefs = getSharedPreferences("loginPrefs", MODE_PRIVATE);

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
                    final ArrayList<Inventaire> inventairesToSend = getCheckedInventairesToSend();
                    int listSize = inventairesToSend.size();
                    boolean noInvToSend = listSize == 0;

                    if(noInvToSend) {
                        Snackbar.make(syncInvs, "Aucun inventaire à synchroniser", Snackbar.LENGTH_LONG).show();
                        return;
                    }

                    Intent intent = new Intent(HistoryRecensementActivity.this,SyncInvActivity.class);
                    intent.putParcelableArrayListExtra("inventairesToSend",inventairesToSend);
                    intent.putExtra("mdp",mdp);
                    startActivityForResult(intent,HttpActivity.END_OF_SYNC);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == HttpActivity.END_OF_SYNC)
            setAdapter();
        super.onActivityResult(requestCode, resultCode, data);
    }

    private ArrayList<Inventaire> getCheckedInventairesToSend(){
        List<Inventaire> tmp = adapter.getCheckedItemsStocker().getCheckedItems();
        ArrayList<Inventaire> res = new ArrayList<>();
        for (Inventaire inv : tmp){
            if(inv.isToSync())
                res.add(inv);
        }
        return res;
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

    @Override
    protected void closeDatabases() {
        campagneDao.close();
        taxUsrDao.close();
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
        List<Inventaire> inventaires = campagneDao.getInventairesOfTheUsr(Utils.getCurrUsrId(HistoryRecensementActivity.this));

        adapter = new InventaireAdapter(this,inventaires);
        listItems.setAdapter(adapter);
    }
}
