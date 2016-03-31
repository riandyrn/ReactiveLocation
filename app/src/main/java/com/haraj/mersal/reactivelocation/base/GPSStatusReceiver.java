package com.haraj.mersal.reactivelocation.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;

/**
 * Created by riandyrn on 3/30/16.
 */
public class GPSStatusReceiver extends BroadcastReceiver {

    /**
     * This receiver will show alert dialog
     * if GPS is disabled
     */

    private BaseActivity activity;
    private AlertDialog gpsDialog;

    public GPSStatusReceiver(BaseActivity activity) {
        this.activity = activity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        handleBroadcast(context, intent);
    }

    private void handleBroadcast(Context context, Intent intent) {

        boolean isEnabled = intent.getExtras().getBoolean("isEnabled", false);
        if(!isEnabled) {
            showGPSDialog();
        } else {
            dismissGPSDialog();
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
                .setMessage("Please enable your GPS")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        activity.startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 10);
                    }
                });

        return builder.create();
    }

    public void handleActivityResult(int requestCode, int resultCode, Intent data, ActivityResultGPSEnabledCallback activityResultGpsEnabledCallback) {
        if(requestCode == 10) {
            if(isGPSEnabled()) {
                dismissGPSDialog();
                activityResultGpsEnabledCallback.onResultGPSEnabled();
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
