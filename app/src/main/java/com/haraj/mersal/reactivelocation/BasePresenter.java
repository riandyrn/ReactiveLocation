package com.haraj.mersal.reactivelocation;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.support.v4.content.LocalBroadcastManager;

/**
 * Created by riandyrn on 3/30/16.
 */
public class BasePresenter {

    private BaseActivity activity;
    private GPSStatusReceiver gpsStatusReceiver;

    public BasePresenter(BaseActivity activity) {
        this.activity = activity;
        gpsStatusReceiver = new GPSStatusReceiver(activity);
    }

    public void registerReceiver() {
        LocalBroadcastManager
                .getInstance(activity)
                .registerReceiver(gpsStatusReceiver, new IntentFilter(LocationService.BROADCAST_GPS_STATUS));
    }

    public void unregisterReceiver() {
        LocalBroadcastManager
                .getInstance(activity)
                .unregisterReceiver(gpsStatusReceiver);
    }

    public void setIsOnForeground(boolean isOnForeground) {
        PreferenceProvider.setIsOnForeground(activity, isOnForeground);
    }

    public void handleActivityResult(int requestCode, int resultCode, Intent data, ActivityResultGPSEnabledCallback activityResultGpsEnabledCallback) {
        if(requestCode == 10) {
            if(isGPSEnabled()) {
                gpsStatusReceiver.dismissGPSDialog();
                activityResultGpsEnabledCallback.onResultGPSEnabled();
            } else {
                gpsStatusReceiver.showGPSDialog();
            }
        }
    }

    private boolean isGPSEnabled() {
        LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(locationManager.GPS_PROVIDER);
    }
}
