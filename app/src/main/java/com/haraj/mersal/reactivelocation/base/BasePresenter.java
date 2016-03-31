package com.haraj.mersal.reactivelocation.base;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import com.haraj.mersal.reactivelocation.location.LocationService;
import com.haraj.mersal.reactivelocation.tools.PreferenceProvider;

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
        gpsStatusReceiver.handleActivityResult(requestCode, resultCode, data, activityResultGpsEnabledCallback);
    }

    public void showGPSNotificationIfNeeded() {
        if(LocationService.getInstance() != null) {
            LocationService.getInstance().showGPSNotification();
        }
    }

    public void dismissGPSNotificationIfAny() {
        if(LocationService.getInstance() != null) {
            LocationService.getInstance().dismissGPSNotification();
        }
    }
}
