package com.haraj.mersal.reactivelocation;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

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
        stopListeningLocationUpdates();

        if(locationManager.isProviderEnabled(locationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 2 * 1000, 0, locationService);
        } else {
            sendBroadcast(LocationService.BROADCAST_TYPE_GPS_DISABLED);
        }
    }

    private void stopListeningLocationUpdates() {
        locationManager.removeUpdates(locationService);
    }

    public int handleStartCommand(Intent intent, int flags, int startId) {
        startListeningLocationUpdates();
        return locationService.START_REDELIVER_INTENT;
    }

    public void handleOnDestroy() {
        stopListeningLocationUpdates();
    }

    public void handleOnLocationChanged(Location location) {
        Log.d(this.getClass().getSimpleName(), "location: " + location.toString());
        currentBestLocation = location;
        sendBroadcast(LocationService.BROADCAST_TYPE_LOCATION_UPDATE);
    }

    public void handleOnProviderDisabled(String provider) {
        if(!isOnForeground())
            showGPSNotification();

        sendBroadcast(LocationService.BROADCAST_TYPE_GPS_DISABLED);
    }

    public void handleOnProviderEnabled(String provider) {
        startListeningLocationUpdates();
        sendBroadcast(LocationService.BROADCAST_TYPE_GPS_ENABLED);
    }

    private void sendBroadcast(String typePayload) {
        Intent intent = new Intent(LocationService.BROADCAST_LOCATION);
        intent.putExtra("broadcastType", typePayload);

        LocalBroadcastManager.getInstance(locationService).sendBroadcast(intent);
    }

    private void showGPSNotification() {
        NotificationManager notificationManager = (NotificationManager) locationService.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(10, createGPSNotification());
    }

    public void dismissGPSNotification() {
        NotificationManager notificationManager = (NotificationManager) locationService.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(10);
    }

    private Notification createGPSNotification() {

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(locationService)
                .setContentTitle("App Require Attention")
                .setContentText("Please tap to open the app")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setSound(soundUri)
                .setOngoing(true);

        //Intent notifyIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        Intent notifyIntent = new Intent(locationService, MainActivity.class);

        //notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent notifyPendingIntent = PendingIntent.getActivity(locationService, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(notifyPendingIntent);

        return builder.build();
    }

    public boolean isOnForeground() {
        return PreferenceProvider.isOnForeground(locationService);
    }

}
