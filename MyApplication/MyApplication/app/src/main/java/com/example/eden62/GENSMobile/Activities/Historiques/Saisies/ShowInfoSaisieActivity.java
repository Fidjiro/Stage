package com.example.eden62.GENSMobile.Activities.Historiques.Saisies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.EditText;
import android.widget.TextView;

import com.example.eden62.GENSMobile.Database.SaisiesProtocoleDatabase.CampagneProtocolaire;
import com.example.eden62.GENSMobile.Database.SaisiesProtocoleDatabase.RNF.RNFSaisie;
import com.example.eden62.GENSMobile.Database.SaisiesProtocoleDatabase.Saisie;
import com.example.eden62.GENSMobile.R;

public class ShowInfoSaisieActivity extends AppCompatActivity {

    protected CampagneProtocolaire campagneProtocolaire;
    protected TextView nomProto, nomSite, date, synthese, nomCampagne, heureDebut, heureFin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_show_info_saisie);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.8),(int)(height*.8));

        campagneProtocolaire = getIntent().getParcelableExtra("campagne");

        nomCampagne = (TextView) findViewById(R.id.nomCampagne);
        nomProto = (TextView)findViewById(R.id.nomProto);
        nomSite = (TextView)findViewById(R.id.nomSite);
        date = (TextView)findViewById(R.id.date);
        heureDebut = (TextView) findViewById(R.id.heureDebut);
        heureFin = (TextView)findViewById(R.id.heureFin);
        synthese = (TextView)findViewById(R.id.syntheseSaisie);

        nomCampagne.setText(campagneProtocolaire.getName());
        nomProto.setText(campagneProtocolaire.getNomProto());
        nomSite.setText(campagneProtocolaire.getNomSite());
        date.setText(campagneProtocolaire.getDate());
        heureDebut.setText(campagneProtocolaire.getHeureDebut());
        heureFin.setText(campagneProtocolaire.getHeureFin());
       synthese.setText(campagneProtocolaire.getSaisieFromJson(getGoodClass(campagneProtocolaire)).getSynthese());
    }

    private Class getGoodClass(CampagneProtocolaire c){
        if(c.getNomProto().equals(getString(R.string.nomRNF)))
            return RNFSaisie.class;
        // On ajoutera des if si d'autre protocoles sont implémentés
        return null;
    }
}
