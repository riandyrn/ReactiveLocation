package com.haraj.mersal.reactivelocation;

import android.content.Intent;
import android.location.Location;

/**
 * Created by riandyrn on 3/27/16.
 */
public class MainPresenter implements LocationServiceListener {

    private MainActivity activity;
    private LocationServiceReceiver locationServiceReceiver;

    private boolean isUpdateAttempted = false;

    public MainPresenter(MainActivity activity) {
        this.activity = activity;
        locationServiceReceiver = new LocationServiceReceiver(activity, this);
    }

    public void startListeningLocationUpdates() {
        if(!isUpdateAttempted)
            isUpdateAttempted = true;

        locationServiceReceiver.registerReceiver();
        activity.startService(new Intent(activity, LocationService.class));
    }

    public void stopListeningLocationUpdates() {
        activity.setTextView("Not listening location updates...");
        activity.stopService(new Intent(activity, LocationService.class));
        locationServiceReceiver.unregisterReceiver();
    }

    public void handleOnResultGPSEnabled() {
        if(isUpdateAttempted) {
            startListeningLocationUpdates();
            isUpdateAttempted = false;
        }
    }

    @Override
    public void onLocationUpdated(Location location) {
        activity.setTextView(location.toString());
    }

    @Override
    public void onServiceExecuted(boolean isRunning) {
        if(isRunning) {
            activity.setTextView("Listening to location updates...");
        } else {
            stopListeningLocationUpdates();
        }
    }
}
