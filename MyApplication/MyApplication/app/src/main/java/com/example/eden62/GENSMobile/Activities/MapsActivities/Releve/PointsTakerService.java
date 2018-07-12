package com.example.eden62.GENSMobile.Activities.MapsActivities.Releve;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import org.mapsforge.core.model.LatLong;

import java.util.ArrayList;
import java.util.List;

public class PointsTakerService extends Service {

    private static final String TAG = "BOOMBOOMTESTGPS";
    private final IBinder mBinder = new PointsTakerBinder();
    private LocationManager mLocationManager = null;
    private List<LatLong> takenPoints = new ArrayList<>();
    private static final int LOCATION_INTERVAL = 2000;
    private static final String PROVIDER = LocationManager.GPS_PROVIDER;
    protected MyLocationListener locationListener = new MyLocationListener(PROVIDER);

    public class PointsTakerBinder extends Binder {
        PointsTakerService getService(){
            return PointsTakerService.this;
        }
    }

    protected class MyLocationListener implements LocationListener {

        public MyLocationListener(String provider){
            Log.e(TAG, "LocationListener " + provider);
            //takenPoints.add(getLocationPoint(new Location(provider)));
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {Log.e(TAG, "onStatusChanged: " + provider + ",status : " + status); }

        @Override
        public void onProviderEnabled(String provider) { Log.e(TAG, "onProviderEnabled: " + provider);}

        @Override
        public void onProviderDisabled(String provider) {
            Log.e(TAG, "onProviderDisabled: " + provider);
        }

        @Override
        public void onLocationChanged(Location location) {
            takenPoints.add(getLocationPoint(location));
            Log.e(TAG, "onLocationChanged: " + location + ",listSize: " + takenPoints.size());
        }

        /**
         * Récupère une {@link Location} en {@link LatLong}
         *
         * @param loc La location à transformer
         * @return La {@link Location} tranformée en {@link LatLong}
         */
        public LatLong getLocationPoint(Location loc){
            return new LatLong(loc.getLatitude(),loc.getLongitude());
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.e(TAG,"Binding");
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate");
        initializeLocationManager();
        try {
            mLocationManager.requestLocationUpdates(PROVIDER, LOCATION_INTERVAL, 0, locationListener);
        } catch (java.lang.SecurityException ex) {
            ex.printStackTrace();
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy");
        if(mLocationManager != null){
            try{
                mLocationManager.removeUpdates(locationListener);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        }
    }

    public List<LatLong> getTakenPoints(){
        Log.e(TAG, "Uses og getTakenPoints, listSize: " + takenPoints.size());
        stopSelf();
        return takenPoints;
    }
}
