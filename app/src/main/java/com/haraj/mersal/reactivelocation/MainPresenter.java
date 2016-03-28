package com.haraj.mersal.reactivelocation;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.util.Log;

/**
 * Created by riandyrn on 3/27/16.
 */
public class MainPresenter {

    private MainActivity activity;
    private AlertDialog gpsDialog;

    private boolean isUpdateAttempted = false;

    public MainPresenter(MainActivity activity) {
        this.activity = activity;
    }

    public void startListeningLocationUpdates() {
        if(isGPSEnabled()) {
            activity.setTextView("Listening location updates...");
            activity.startService(new Intent(activity, LocationService.class));
        } else {
            isUpdateAttempted = true;
            showGPSDialog();
        }
    }

    public void stopListeningLocationUpdates() {
        activity.setTextView("Not listening location updates...");
        activity.stopService(new Intent(activity, LocationService.class));
    }

    public void handleBroadcast(Context context, Intent intent) {
        String type = intent.getExtras().getString("broadcastType", "");
        Log.d(this.getClass().getSimpleName(), "handleBroadcast: " + type);

        switch (type){
            case LocationService.BROADCAST_TYPE_LOCATION_UPDATE:
                Location location = LocationService.getInstance().getLocation();
                activity.setTextView(location.toString());
                break;
            case LocationService.BROADCAST_TYPE_GPS_DISABLED:
                showGPSDialog();
                break;
            case LocationService.BROADCAST_TYPE_GPS_ENABLED:
                dismissGPSDialog();
                break;
        }
    }

    private void dismissGPSDialog() {
        if(gpsDialog != null) {
            gpsDialog.dismiss();
            gpsDialog = null;
        }
    }

    private void showGPSDialog() {
        dismissGPSDialog();
        gpsDialog = createGPSDialog();
        gpsDialog.setCancelable(false);
        gpsDialog.show();
    }

    private AlertDialog createGPSDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Enable GPS")
                .setMessage("Please enabled your GPS")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        activity.startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 10);
                    }
                });

        return builder.create();
    }

    public void handleActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 10) {
            if(isGPSEnabled()) {
                dismissGPSDialog();

                if(isUpdateAttempted){
                    startListeningLocationUpdates();
                    isUpdateAttempted = false;
                }
            } else {
                showGPSDialog();
            }
        }
    }

    private boolean isGPSEnabled() {
        LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(locationManager.GPS_PROVIDER);
    }
}
