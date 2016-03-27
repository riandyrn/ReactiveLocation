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
public class MainPresenter implements LocationListener{

    private MainActivity activity;
    private LocationManager locationManager;
    private AlertDialog gpsDialog;

    private boolean isAttemptUpdate = false;

    public MainPresenter(MainActivity activity) {
        this.activity = activity;
        locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
    }

    public void startListeningLocationUpdates() {
        isAttemptUpdate = true;
        if(locationManager.isProviderEnabled(locationManager.GPS_PROVIDER)) {
            activity.setTextView("Start listening updates...");
            locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 0, 0, this);
        } else {
            showGPSDialog();
        }
    }

    public void stopListeningLocationUpdates() {
        activity.setTextView("Not listening to location update...");
        locationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        activity.setTextView(location.toString());
        Log.d(MainPresenter.class.getSimpleName(), location.toString());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        if(gpsDialog != null) {
            gpsDialog.dismiss();
            gpsDialog = null;
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        showGPSDialog();
    }

    private void showGPSDialog() {
        gpsDialog = createGPSDialog();
        gpsDialog.setCancelable(false);
        gpsDialog.show();
    }

    private AlertDialog createGPSDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Enable GPS")
                .setMessage("Please enable your GPS")
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
            if(locationManager.isProviderEnabled(locationManager.GPS_PROVIDER)) {
                if(isAttemptUpdate) {
                    startListeningLocationUpdates();
                    isAttemptUpdate = false;
                }
            } else {
                showGPSDialog();
            }
        }
    }
}
