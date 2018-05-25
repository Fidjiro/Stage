package com.example.florian.myapplication.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.florian.myapplication.Database.CampagneDatabase.CampagneDAO;
import com.example.florian.myapplication.Database.CampagneDatabase.Inventaire;
import com.example.florian.myapplication.R;

/**
 * Permet de tester si un inventaire à bien été inséré
 */
public class TestActivity extends AppCompatActivity {

    TextView ref_taxon,latitude,longitude,date,nombre,type_obs,usr_id,typeTaxon,nbMale,nbFemale,activite,statut,nidif,abondance,ponte;
    CampagneDAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        ref_taxon = (TextView) findViewById(R.id.ref_taxon);
        latitude = (TextView) findViewById(R.id.latitude);
        longitude = (TextView) findViewById(R.id.longitude);
        date = (TextView) findViewById(R.id.date);
        nombre = (TextView) findViewById(R.id.nombre);
        type_obs = (TextView) findViewById(R.id.type_obs);
        usr_id = (TextView) findViewById(R.id.usr_id);
        typeTaxon = (TextView) findViewById(R.id.typetaxon);
        nbMale = (TextView) findViewById(R.id.nbmale);
        nbFemale = (TextView) findViewById(R.id.nbfemale);
        activite = (TextView) findViewById(R.id.activite);
        statut = (TextView) findViewById(R.id.statut);
        nidif = (TextView) findViewById(R.id.nidif);
        ponte = (TextView) findViewById(R.id.presenceponte);
        abondance = (TextView) findViewById(R.id.indiceabondance);

        dao = new CampagneDAO(this);
        dao.open();
        Inventaire inv = dao.getInventaireOfTheUsr();

        ref_taxon.setText(ref_taxon.getText() + "" + inv.getRef_taxon());
        latitude.setText(latitude.getText() + "" + inv.getLatitude());
        longitude.setText(longitude.getText() + "" + inv.getLongitude());
        date.setText(date.getText() + inv.getDate());
        nombre.setText(nombre.getText() + "" + inv.getNombre());
        type_obs.setText(type_obs.getText() + inv.getType_obs());
        usr_id.setText(usr_id.getText() + "" + inv.getUser());
        typeTaxon.setText(typeTaxon.getText() + "" + inv.getTypeTaxon());
        nbMale.setText(nbMale.getText() + "" + inv.getNbMale());
        nbFemale.setText(nbFemale.getText() + "" + inv.getNbFemale());
        activite.setText(activite.getText() + "" + inv.getActivite());
        statut.setText(statut.getText() + "" + inv.getStatut());
        nidif.setText(nidif.getText() + "" + inv.getNidif());
        ponte.setText(ponte.getText() + "" + inv.getPresencePonte());
        abondance.setText(abondance.getText() + "" + inv.getIndiceAbondance());
    }
}
