package com.example.eden62.GENSMobile.Activities.ProtocoleActivities.RNF;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.eden62.GENSMobile.Activities.ProtocoleActivities.FormMeteoActivity;
import com.example.eden62.GENSMobile.Adapters.RNFAdapter.TransectAdapter;
import com.example.eden62.GENSMobile.Database.SaisiesProtocoleDatabase.CampagneProtocolaire;
import com.example.eden62.GENSMobile.Database.SaisiesProtocoleDatabase.RNF.RNFSaisie;
import com.example.eden62.GENSMobile.Database.SaisiesProtocoleDatabase.CampagneProtocolaireDao;
import com.example.eden62.GENSMobile.Database.SaisiesProtocoleDatabase.RNF.RNFInventaire;
import com.example.eden62.GENSMobile.Database.SaisiesProtocoleDatabase.RNF.RNFInventories;
import com.example.eden62.GENSMobile.Database.SaisiesProtocoleDatabase.ProtocoleMeteo;
import com.example.eden62.GENSMobile.Database.SaisiesProtocoleDatabase.RNF.Transect;
import com.example.eden62.GENSMobile.R;
import com.example.eden62.GENSMobile.Tools.Utils;
import com.google.gson.Gson;

import java.util.List;

/**
 * Activité permettant de choisir un transect à remplir
 */
public class ChooseTransectActivity extends AppCompatActivity {

    protected ListView listTransect;
    protected int launchedTransectPos;
    protected List<Transect> transects;
    protected Button finRnf;

    protected int cptTransectDone = 0;
    protected boolean modification = false;
    protected CampagneProtocolaire<RNFSaisie> modifiedCampagne;
    protected RNFSaisie modifiedSaisie;

    protected ProtocoleMeteo meteo;
    protected String name, nomSite, heureDeb, heureFin;
    protected Gson gson = new Gson();

    private CampagneProtocolaireDao dao;

    public static final int RESULT_TRANSECT_DONE = 4;
    public static final int RESULT_NAME_RNF = 5;
    public static final int RESULT_FILL_METEO = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_choose_transect);

        dao = new CampagneProtocolaireDao(this);
        dao.open();

        listTransect = (ListView) findViewById(R.id.listTransect);
        finRnf = (Button) findViewById(R.id.finRnf);

        finRnf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(allTransectsAreDone()) {
                   if(modification){
                       modifiedCampagne.setSaisie(new Gson().toJson(modifiedSaisie));
                       dao.modifieCampagne(modifiedCampagne);
                       finish();
                   } else
                       startActivityForResult(new Intent(ChooseTransectActivity.this, NameRNFActivity.class), RESULT_NAME_RNF);
               }
               else
                   Toast.makeText(ChooseTransectActivity.this, "Tous les transects doivent être fait", Toast.LENGTH_SHORT).show();
            }
        });

        Intent callerIntent = getIntent();

        long campagneId = callerIntent.getLongExtra("campagneId",-1);
        if(campagneId != -1){
            modification = true;
            modifiedCampagne = dao.getCampagneById(campagneId);
            modifiedSaisie = modifiedCampagne.getSaisieFromJson(RNFSaisie.class);
            transects = modifiedSaisie.getTransects();
            cptTransectDone = transects.size();
            nomSite = modifiedCampagne.getNomSite();
        } else {
            transects = callerIntent.getParcelableArrayListExtra("transects");
            nomSite = callerIntent.getStringExtra("nomSite");
        }

        listTransect.setAdapter(new TransectAdapter(this,transects));

        listTransect.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                launchedTransectPos = position;
                Transect currTransect = transects.get(position);
                Intent intent = new Intent(ChooseTransectActivity.this, FillTransectActivity.class);
                intent.putExtra("transect",transects.get(position));
                if(currTransect.isDone())
                    intent.putExtra("modif", true);
                startActivityForResult(intent,RESULT_TRANSECT_DONE);
            }
        });

        meteo = callerIntent.getParcelableExtra("meteo");
    }

    /**
     * Crée une campagne RNF via les données remplies par l'utilisateur
     *
     * @return Une campagne RNF défini par l'utilisateur
     */
    protected CampagneProtocolaire<RNFSaisie> createCampagne(){
        String saisie = gson.toJson(new RNFSaisie(transects,meteo));
        return new CampagneProtocolaire<>(Utils.getCurrUsrId(this), name, Utils.getDate(), heureDeb, heureFin, getString(R.string.nomRNF), nomSite, saisie);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_TRANSECT_DONE){
            Transect modifiedTransect = transects.get(launchedTransectPos);
            // On récupère donc le RNFInventories stocké dans l'intent en arrayList et on crée un nouveau RNFInventories via cette arrayList
            RNFInventories recensedInventories =  new RNFInventories(data.<RNFInventaire>getParcelableArrayListExtra("recensedInvs"));

            modifiedTransect.setInventories(recensedInventories);
            modifiedTransect.setMinutes(data.getIntExtra("minutes",0));
            modifiedTransect.setSecondes(data.getIntExtra("secondes",0));
            modifiedTransect.setInfo(recensedInventories.toString());
            modifiedTransect.setDone(true);
            cptTransectDone++;

            listTransect.setAdapter(new TransectAdapter(this,transects));
        }

        if(resultCode == RESULT_NAME_RNF) {
            name = data.getStringExtra("nomRnf");
            heureDeb = data.getStringExtra("heureDebut");
            heureFin = data.getStringExtra("heureFin");
            startActivityForResult(new Intent(this,FormMeteoActivity.class),RESULT_FILL_METEO);
        }

        if(resultCode == RESULT_FILL_METEO){
            meteo = data.getParcelableExtra("meteo");
            CampagneProtocolaire<RNFSaisie> currCampagne = createCampagne();
            dao.insertCampagne(currCampagne);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.avertissementCancelRnf);
        builder.setTitle(getString(R.string.avertissement));
        builder.setCancelable(false);
        builder.setPositiveButton(getString(R.string.oui), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ChooseTransectActivity.super.onBackPressed();
            }
        });
        builder.setNegativeButton(getString(R.string.non), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                }
        });
        builder.create().show();
    }

    // Vérifie que tous les transects ont été fait
    private boolean allTransectsAreDone(){
        return cptTransectDone >= transects.size();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dao.close();
    }
}
