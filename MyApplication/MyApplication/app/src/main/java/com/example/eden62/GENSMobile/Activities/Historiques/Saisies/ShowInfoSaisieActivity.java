package com.example.eden62.GENSMobile.Activities.Historiques.Saisies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.eden62.GENSMobile.Activities.ProtocoleActivities.FormMeteoActivity;
import com.example.eden62.GENSMobile.Activities.ProtocoleActivities.RNF.ChooseTransectActivity;
import com.example.eden62.GENSMobile.Database.CampagneDatabase.CampagneDAO;
import com.example.eden62.GENSMobile.Database.SaisiesProtocoleDatabase.CampagneProtocolaire;
import com.example.eden62.GENSMobile.Database.SaisiesProtocoleDatabase.CampagneProtocolaireDao;
import com.example.eden62.GENSMobile.Database.SaisiesProtocoleDatabase.ProtocoleMeteo;
import com.example.eden62.GENSMobile.Database.SaisiesProtocoleDatabase.RNF.RNFSaisie;
import com.example.eden62.GENSMobile.Database.SaisiesProtocoleDatabase.Saisie;
import com.example.eden62.GENSMobile.R;

/**
 * Pop up d'informations sur une campagne protocolaire
 */
public class ShowInfoSaisieActivity extends AppCompatActivity {

    protected TextView nomProto, nomSite, date, synthese, nomCampagne, heureDebut, heureFin;
    protected Button modifInfoCampagne, modifMeteo, modifInvs;
    protected ImageButton deleteCampagne;

    protected long campagneId;
    protected Class currSaisieClass;
    protected CampagneProtocolaire campagneProtocolaire;
    protected CampagneProtocolaireDao dao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_show_info_saisie);

        dao = new CampagneProtocolaireDao(this);
        dao.open();

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.8),(int)(height*.8));

        campagneId = getIntent().getLongExtra("campagneId",0);

        nomCampagne = (TextView) findViewById(R.id.nomCampagne);
        nomProto = (TextView)findViewById(R.id.nomProto);
        nomSite = (TextView)findViewById(R.id.nomSite);
        date = (TextView)findViewById(R.id.date);
        heureDebut = (TextView) findViewById(R.id.heureDebut);
        heureFin = (TextView)findViewById(R.id.heureFin);
        synthese = (TextView)findViewById(R.id.syntheseSaisie);
        modifInfoCampagne = (Button)findViewById(R.id.modifInfo);
        modifMeteo = (Button)findViewById(R.id.modifMeteo);
        modifInvs = (Button)findViewById(R.id.modifSaisie);
        deleteCampagne = (ImageButton)findViewById(R.id.deleteCampagne);

        deleteCampagne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dao.delete(campagneProtocolaire);
                finish();
            }
        });

        modifMeteo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowInfoSaisieActivity.this, FormMeteoActivity.class);
                intent.putExtra("modifiedMeteo",campagneProtocolaire.getSaisieFromJson(currSaisieClass).getMeteo());
                intent.putExtra("campagneId",campagneId);
                intent.putExtra("saisieClass",currSaisieClass);
                startActivity(intent);
            }
        });

        modifInfoCampagne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowInfoSaisieActivity.this, ModifInfoCampagneActivity.class);
                intent.putExtra("campagneId",campagneId);
                startActivity(intent);
            }
        });

        modifInvs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Saisie s = campagneProtocolaire.getSaisieFromJson(currSaisieClass);
                Intent intent = new Intent(ShowInfoSaisieActivity.this, s.getFillingActivity());
                intent.putExtra("campagneId",campagneId);
                startActivity(intent);
            }
        });
    }

    /**
     * Récupère le bon type de classe de saisie pour cette campagne via le nom du protocole que cette-ci suit
     *
     * @param c La campagne protocolaire
     * @return La classe saisie correspondante au protocole de cette campagne
     */
    private Class getGoodClass(CampagneProtocolaire c){
        if(c.getNomProto().equals(getString(R.string.nomRNF)))
            return RNFSaisie.class;
        // On ajoutera des if si d'autre protocoles sont implémentés
        return null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        campagneProtocolaire = dao.getCampagneById(campagneId);
        currSaisieClass = getGoodClass(campagneProtocolaire);

        nomCampagne.setText(campagneProtocolaire.getName());
        nomProto.setText(campagneProtocolaire.getNomProto());
        nomSite.setText(campagneProtocolaire.getNomSite());
        date.setText(campagneProtocolaire.getDate());
        heureDebut.setText(campagneProtocolaire.getHeureDebut());
        heureFin.setText(campagneProtocolaire.getHeureFin());
        synthese.setText(campagneProtocolaire.getSaisieFromJson(currSaisieClass).getSynthese());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dao.close();
    }
}
