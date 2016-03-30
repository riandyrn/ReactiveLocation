package com.haraj.mersal.reactivelocation;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.support.v4.content.LocalBroadcastManager;

/**
 * Created by riandyrn on 3/30/16.
 */
public class LocationServiceReceiver extends BroadcastReceiver {

    private BaseActivity activity;
    private LocationServiceListener locationServiceListener;

    public LocationServiceReceiver(BaseActivity activity, LocationServiceListener locationServiceListener) {
        this.activity = activity;
        this.locationServiceListener = locationServiceListener;
    }

    public void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();

        intentFilter.addAction(LocationService.BROADCAST_LOCATION);
        intentFilter.addAction(LocationService.BROADCAST_SERVICE_STATUS);

        LocalBroadcastManager.getInstance(activity).registerReceiver(this, intentFilter);
    }

    public void unregisterReceiver() {
        LocalBroadcastManager.getInstance(activity).unregisterReceiver(this);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();

        switch (action) {
            case LocationService.BROADCAST_LOCATION:
                Location location = LocationService.getInstance().getLocation();
                locationServiceListener.onLocationUpdated(location);

                break;
            case LocationService.BROADCAST_SERVICE_STATUS:
                boolean isRunning = intent.getBooleanExtra("isRunning", false);
                locationServiceListener.onServiceExecuted(isRunning);

                break;
        }
    }
}
