package com.example.florian.myapplication.Activities.MapsActivities.Releve;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.florian.myapplication.Activities.MapsActivities.Releve.RelevesPopUp.PopUpPoint;
import com.example.florian.myapplication.Database.ReleveDatabase.HistoryDao;
import com.example.florian.myapplication.Database.ReleveDatabase.Releve;
import com.example.florian.myapplication.R;

import org.mapsforge.core.model.LatLong;

import static java.lang.Math.log;

public class NameRelevePopup extends AppCompatActivity {

    protected TextView lineLengthText,perimeterText, polygonAreaText, positionWGSText,positionL93Text;
    protected Button validNom;
    protected EditText nomReleve;

    protected Releve releveToAdd;

    protected HistoryDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_releve);

        dao = new HistoryDao(this);
        dao.open();

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.75),(int)(height*.33));

        nomReleve = (EditText) findViewById(R.id.nomRel);
        validNom = (Button) findViewById(R.id.validerNom);
        lineLengthText = (TextView) findViewById(R.id.lineLength);
        perimeterText = (TextView) findViewById(R.id.perimeter);
        polygonAreaText = (TextView) findViewById(R.id.polygonArea);
        positionWGSText = (TextView) findViewById(R.id.positionWGS);
        positionL93Text = (TextView) findViewById(R.id.positionL93);

        Intent intent = getIntent();
        releveToAdd = intent.getParcelableExtra("releveToAdd");

        displayGoodReleveInfos();

        validNom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!nomReleve.getText().toString().isEmpty()) {
                    releveToAdd.setNom(nomReleve.getText().toString());
                    dao.insertInventaire(releveToAdd);
                    final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(nomReleve.getWindowToken(), 0);
                    NameRelevePopup.this.finish();
                }else
                    createAvertissementDialog().show();
            }
        });
    }

    private void displayGoodReleveInfos() {
        String type = releveToAdd.getType();
        if(type.equals(getString(R.string.point))) {
            double lat = Double.parseDouble(releveToAdd.getLatitudes());
            double lon = Double.parseDouble(releveToAdd.getLongitudes());
            XY xy = convertWgs84ToL93(new LatLong(lat, lon));

            positionL93Text.setVisibility(View.VISIBLE);
            positionWGSText.setVisibility(View.VISIBLE);
            positionWGSText.setText(getString(R.string.positionWGS) + " " + lat + " ; " + lon);
            positionL93Text.setText(getString(R.string.positionL93) + " " + xy.x + " ; " + xy.y);
        } else if(type.equals(getString(R.string.line))){
            lineLengthText.setVisibility(View.VISIBLE);
            lineLengthText.setText(getString(R.string.longueur) + releveToAdd.getLength());
        } else{
            polygonAreaText.setVisibility(View.VISIBLE);
            perimeterText.setVisibility(View.VISIBLE);
            perimeterText.setText(getString(R.string.perimetre) + releveToAdd.getPerimeter());
            polygonAreaText.setText(getString(R.string.polygonArea) + releveToAdd.getArea());
        }
    }

    protected Dialog createAvertissementDialog(){
        AlertDialog box;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.nommezReleve));
        builder.setTitle(getString(R.string.avertissement));
        builder.setCancelable(false);
        builder.setPositiveButton(getString(R.string.accord), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        box = builder.create();
        return box;
    }

    private double atanh(double x){
        return (log(1+x) - log(1-x))/2;
    }

    private XY convertWgs84ToL93(LatLong latLong){

        double latitude = latLong.getLatitude();
        double longitude = latLong.getLongitude();

// définition des constantes
        double c= 11754255.426096; //constante de la projection
        double e= 0.0818191910428158; //première exentricité de l'ellipsoïde
        double n= 0.725607765053267; //exposant de la projection
        double xs= 700000; //coordonnées en projection du pole
        double ys= 12655612.049876; //coordonnées en projection du pole

// pré-calculs
        double lat_rad= latitude/180*Math.PI; //latitude en rad
        double lat_iso= atanh(Math.sin(lat_rad))-e*atanh(e*Math.sin(lat_rad)); //latitude isométrique

//calcul
        double x= ((c*Math.exp(-n*(lat_iso)))*Math.sin(n*(longitude-3)/180*Math.PI)+xs);
        double y= (ys-(c*Math.exp(-n*(lat_iso)))*Math.cos(n*(longitude-3)/180*Math.PI));
        return new XY(x,y);
    }
    private class XY{
        public double x;
        public double y;

        public XY(){
            x = 0;
            y = 0;
        }

        public XY(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }
}
