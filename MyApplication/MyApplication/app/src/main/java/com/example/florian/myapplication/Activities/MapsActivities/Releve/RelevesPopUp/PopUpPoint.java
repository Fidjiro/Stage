package com.example.florian.myapplication.Activities.MapsActivities.Releve.RelevesPopUp;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.florian.myapplication.Activities.MapsActivities.Releve.MainActivityRel;
import com.example.florian.myapplication.R;

import org.mapsforge.core.model.LatLong;

import static java.lang.Math.log;

public class PopUpPoint extends ReleveInfoPopup {

    protected TextView posWgs, posL93;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_pop_up_point);
    }

    @Override
    protected void finishPopUp() {
        this.finish();
    }

    @Override
    protected void initView() {
        super.initView();
        posWgs = (TextView) findViewById(R.id.posWgs);
        posL93 = (TextView) findViewById(R.id.posL93);
    }

    @Override
    protected void setViewsContent() {
        super.setViewsContent();
        double lat = Double.parseDouble(rel.getLatitudes());
        double lon = Double.parseDouble(rel.getLongitudes());
        XY xy = convertWgs84ToL93(new LatLong(lat,lon));

        posWgs.setText(lat + ";" + lon);
        posL93.setText(xy.x + ";" + xy.y);
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
