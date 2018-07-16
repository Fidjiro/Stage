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

import com.example.eden62.GENSMobile.R;

import org.mapsforge.core.model.LatLong;

import java.util.ArrayList;
import java.util.List;

public class PointsTakerService extends Service {

    private static final String TAG = "BOOMBOOMTESTGPS";
    private static final int ONGOING_NOTIFICATION_ID = 1;
    private final IBinder mBinder = new PointsTakerBinder();
    private LocationManager mLocationManager = null;
    private List<LatLong> takenPoints = new ArrayList<>();
    private static final int LOCATION_INTERVAL = 2000;
    private static final String PROVIDER = LocationManager.GPS_PROVIDER;
    protected MyLocationListener locationListener = new MyLocationListener();

    public class PointsTakerBinder extends Binder {
        PointsTakerService getService(){
            return PointsTakerService.this;
        }
    }

    protected class MyLocationListener implements LocationListener {

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) { }

        @Override
        public void onProviderEnabled(String provider) { }

        @Override
        public void onProviderDisabled(String provider) { }

        @Override
        public void onLocationChanged(Location location) {
            takenPoints.add(getLocationPoint(location));
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
        Log.e(TAG,"Onstartcommand");
        super.onStartCommand(intent, flags, startId);
        startForeground(ONGOING_NOTIFICATION_ID,getNotification());
        return START_STICKY;
    }

    private Notification getNotification(){
        Notification.Builder notificationBuilder =
                new Notification.Builder(this)
                        .setContentTitle(getText(R.string.notificationTitle))
                        .setContentText(getText(R.string.notificationMessage))
                        .setSmallIcon(R.drawable.logo_eden)
                        .setTicker(getText(R.string.notificationTicker));
        return notificationBuilder.build();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
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
        Log.e(TAG,"Ondestroy");
        super.onDestroy();
        if(mLocationManager != null){
            try{
                mLocationManager.removeUpdates(locationListener);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void initializeLocationManager() {
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        }
    }

    public List<LatLong> getTakenPoints(){
        stopSelf();
        return takenPoints;
    }
}
