package com.haraj.mersal.reactivelocation;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class LocationService extends Service implements LocationListener {

    private static LocationService service;

    public static final String BROADCAST_LOCATION = "broadcastLocation";

    public static final String BROADCAST_TYPE_LOCATION_UPDATE = "locationUpdate";
    public static final String BROADCAST_TYPE_GPS_DISABLED = "gpsDisabled";
    public static final String BROADCAST_TYPE_GPS_ENABLED = "gpsEnabled";

    private LocationEngine locationEngine;

    public static LocationService getInstance() {
        return service;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        locationEngine = new LocationEngine(this);
        service = this;
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return locationEngine.handleStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        locationEngine.handleOnDestroy();
        service = null;
        super.onDestroy();
    }

    @Override
    public void onLocationChanged(Location location) {
        locationEngine.handleOnLocationChanged(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        /**
         * Do nothing
         */
    }

    @Override
    public void onProviderEnabled(String provider) {
        locationEngine.handleOnProviderEnabled(provider);
    }

    @Override
    public void onProviderDisabled(String provider) {
        locationEngine.handleOnProviderDisabled(provider);
    }

    public Location getLocation() {
        return locationEngine.getLocation();
    }
}
