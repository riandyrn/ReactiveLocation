package com.haraj.mersal.reactivelocation.location;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.haraj.mersal.reactivelocation.MainActivity;
import com.haraj.mersal.reactivelocation.R;
import com.haraj.mersal.reactivelocation.tools.PreferenceProvider;

/**
 * Created by riandyrn on 3/28/16.
 */
public class LocationEngine {

    private LocationService locationService;
    private LocationManager locationManager;

    private Location currentBestLocation;

    public Location getLocation() {
        return currentBestLocation;
    }

    public LocationEngine(LocationService locationService) {
        this.locationService = locationService;
        locationManager = (LocationManager) locationService.getSystemService(Context.LOCATION_SERVICE);
        currentBestLocation = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
    }

    private void startListeningLocationUpdates() {
        stopListeningLocationUpdates(); // make sure only one location listener is active

        if(locationManager.isProviderEnabled(locationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 2 * 1000, 0, locationService);
        } else {
            sendGPSStatusBroadcast(false);
        }
    }

    private void stopListeningLocationUpdates() {
        locationManager.removeUpdates(locationService);
    }

    public int handleStartCommand(Intent intent, int flags, int startId) {
        if(isGPSEnabled()) {
            startListeningLocationUpdates();
            sendServiceStatusBroadcast(true);
        } else {
            sendGPSStatusBroadcast(false);
            sendServiceStatusBroadcast(false);
            locationService.stopSelf();
        }

        return locationService.START_REDELIVER_INTENT;
    }

    public void handleOnDestroy() {
        stopListeningLocationUpdates();
    }

    public void handleOnLocationChanged(Location location) {
        Log.d(this.getClass().getSimpleName(), "location: " + location.toString());
        currentBestLocation = location;
        sendLocationBroadcast();
    }

    public void handleOnProviderDisabled(String provider) {
        if(!isOnForeground())
            showGPSNotification();

        sendGPSStatusBroadcast(false);
    }

    public void handleOnProviderEnabled(String provider) {
        dismissGPSNotification();
        //startListeningLocationUpdates();
        sendGPSStatusBroadcast(true);
    }

    private void sendGPSStatusBroadcast(boolean isEnabled) {
        Intent intent = new Intent(LocationService.BROADCAST_GPS_STATUS);
        intent.putExtra("isEnabled", isEnabled);

        LocalBroadcastManager.getInstance(locationService).sendBroadcast(intent);
    }

    private void sendServiceStatusBroadcast(boolean isRunning) {
        Intent intent = new Intent(LocationService.BROADCAST_SERVICE_STATUS);
        intent.putExtra("isRunning", isRunning);

        LocalBroadcastManager.getInstance(locationService).sendBroadcast(intent);
    }

    private void sendLocationBroadcast() {
        Intent intent = new Intent(LocationService.BROADCAST_LOCATION);
        LocalBroadcastManager.getInstance(locationService).sendBroadcast(intent);
    }

    public void showGPSNotification() {
        if(!isGPSEnabled()) {
            NotificationManager notificationManager = (NotificationManager) locationService.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(10, createGPSNotification());
        }
    }

    public void dismissGPSNotification() {
        NotificationManager notificationManager = (NotificationManager) locationService.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(10);
    }

    private Notification createGPSNotification() {

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Intent notifyIntent = new Intent(locationService, MainActivity.class);
        PendingIntent notifyPendingIntent = PendingIntent.getActivity(locationService, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(locationService)
                .setContentTitle("Enable GPS")
                .setContentText("Tap for further details")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setSound(soundUri)
                .setOngoing(true)
                .setContentIntent(notifyPendingIntent);

        return builder.build();
    }

    public boolean isOnForeground() {
        return PreferenceProvider.isOnForeground(locationService);
    }

    private boolean isGPSEnabled() {
        LocationManager locationManager = (LocationManager) locationService.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(locationManager.GPS_PROVIDER);
    }
}
