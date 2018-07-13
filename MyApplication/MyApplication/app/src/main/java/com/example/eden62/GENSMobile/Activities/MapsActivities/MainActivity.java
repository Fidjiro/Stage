package com.example.eden62.GENSMobile.Activities.MapsActivities;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import com.example.eden62.GENSMobile.Activities.HomeActivity;
import com.example.eden62.GENSMobile.Activities.MapsActivities.Recensement.MainActivityRec;
import com.example.eden62.GENSMobile.Activities.MapsActivities.Releve.MainActivityRel;
import com.example.eden62.GENSMobile.Database.LoadingDatabase.TaxUsrDAO;
import com.example.eden62.GENSMobile.R;
import com.example.eden62.GENSMobile.Tools.MyMapView;

import org.mapsforge.core.model.LatLong;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.android.view.MapView;
import org.mapsforge.map.view.InputListener;

/**
 * Activité Map de base regroupant les outils commun de {@link MainActivityRec} et {@link MainActivityRel}
 */
public abstract class MainActivity extends AppCompatActivity{

    public TaxUsrDAO dao;

    protected static final int BOITE_AVERTISSEMENT = 1;
    protected static final int BOITE_GPS_MANQUANT = 2;
    protected MyLocationListener locationListener = new MyLocationListener();

    public MyMapView myMap;
    protected LocationManager locationManager;
    protected LatLong usrPosition;

    protected boolean firstLaunch = true;
    protected boolean freeLance = false;
    protected static final int REQUEST_ACCESS_FINE_LOCATION = 987;

    protected String provider;


    protected ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AndroidGraphicFactory.createInstance(getApplication());

        setView();

        progressDialog = ProgressDialog.show(this, "",
                "En attente du signal GPS...", true);
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                startActivity(new Intent(MainActivity.this, HomeActivity.class));
            }
        });

        dao = new TaxUsrDAO(this);
        dao.open();
        myMap = new MyMapView((MapView)findViewById(R.id.mapViewOSM));

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provider = LocationManager.GPS_PROVIDER;

        myMap.addOnInputListener(new InputListener() {
            @Override
            public void onMoveEvent() {
                freeLance = true;
            }

            @Override
            public void onZoomEvent() {

            }
        });

        setRelocButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                centerOnTheUsr();
                freeLance = false;
            }
        });

    }

    /**
     * Permet d'obtenir un layout personalisé pour chaque activité
     */
    protected abstract void setView();

    /**
     * Ajoute au bouton relocalisation de l'activité l'évenement listener
     *
     * @param listener L'évènement à ajouter au bouton
     */
    protected abstract void setRelocButton(View.OnClickListener listener);

    @Override
    public void onResume(){
        super.onResume();
        getUsrLocation();
    }

    @Override
    public void onPause(){
        super.onPause();
        stopLocationUpdates();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dao.close();
        myMap.exit();
    }

    /**
     * Crée un dialog différent en fonction de l'identifiant
     *
     * @param identifiant BOITE_AVERTISSEMENT BOITE_FIN_RELEVE BOITE_GPS_MANQUANT
     * @return Le dialog correspondant à l'identifiant
     */
    public Dialog createDialog(int identifiant) {
        AlertDialog box;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        switch (identifiant) {

            case BOITE_AVERTISSEMENT:
                builder.setMessage(R.string.avertissement_message);
                builder.setTitle(getString(R.string.avertissement));
                builder.setPositiveButton(getString(R.string.accord), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        requestGPSPermission();
                    }
                });
                break;
            case BOITE_GPS_MANQUANT:
                builder.setMessage(R.string.gps_requis_message);
                builder.setTitle(getString(R.string.avertissement));
                builder.setCancelable(false);
                builder.setPositiveButton(getString(R.string.oui), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS) ;
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton(getString(R.string.non), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
                break;
        }
        box = builder.create();
        return box;
    }

    /**
     * Demande la permission à l'utilisateur d'utiliser le gps
     */
    protected void requestGPSPermission() {
        if (phoneSdkNeedsRequest())
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_ACCESS_FINE_LOCATION);
    }

    /**
     * Récupère dynamiquement la position de l'utilisateur
     */
    protected void getUsrLocation() {
        if (phoneSdkNeedsRequest()) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION))
                    createDialog(BOITE_AVERTISSEMENT).show();
                else
                    requestGPSPermission();
                return;
            }
        }
        if(!locationManager.isProviderEnabled(provider)){
            createDialog(BOITE_GPS_MANQUANT).show();
        }else
            locationManager.requestLocationUpdates(provider, 1000, 10, locationListener);
    }

    protected class MyLocationListener implements LocationListener {

        public MyLocationListener(){
            super();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) { }

        @Override
        public void onProviderEnabled(String provider) { }

        @Override
        public void onProviderDisabled(String provider) {
            createDialog(BOITE_GPS_MANQUANT).show();
        }

        @Override
        public void onLocationChanged(Location location) {
            progressDialog.dismiss();
            usrPosition = getLocationPoint(location);
            setMyMarkerOnUsr();
            if(!freeLance){
                centerOnTheUsr();
            }
            if(firstLaunch) {
                firstLaunch = false;
                displayLayout();
            }
        }
    }

    /**
     * Affiche le layout en bas de la page
     */
    protected abstract void displayLayout();

    /**
     * Récupère une {@link Location} en {@link LatLong}
     *
     * @param loc La location à transformer
     * @return La {@link Location} tranformée en {@link LatLong}
     */
    public LatLong getLocationPoint(Location loc){
        return new LatLong(loc.getLatitude(),loc.getLongitude());
    }

    /**
     * Centre la carte sur l'utilisateur
     */
    public void centerOnTheUsr(){
        myMap.centerOn(usrPosition);
    }

    /**
     * Positionne le marqueur sur l'utilisateur
     */
    public void setMyMarkerOnUsr(){
        myMap.setMyMarkerOn(usrPosition);
    }

    /**
     * Vérifie si le téléphone à besoin de demander une permission
     *
     * @return <code>True</code> si oui, <code>false</code> sinon
     */
    protected boolean phoneSdkNeedsRequest() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    /**
     * Récupère la {@link LatLong} de l'utilisateur
     *
     * @return La {@link LatLong} de l'utilisateur
     */
    protected LatLong getUsrLatLong(){
        return new LatLong(usrPosition.getLatitude(),usrPosition.getLongitude());
    }

    /**
     * Arrête la demande de localisation de l'utilisateur
     */
    protected void stopLocationUpdates() {
        locationManager.removeUpdates(locationListener);
    }
}
