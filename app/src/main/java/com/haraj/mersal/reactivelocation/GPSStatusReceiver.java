package com.haraj.mersal.reactivelocation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

    public void dismissGPSDialog() {
        if(gpsDialog != null) {
            gpsDialog.dismiss();
            gpsDialog = null;
        }
    }

    public void showGPSDialog() {
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
}
