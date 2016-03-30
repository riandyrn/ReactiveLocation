package com.haraj.mersal.reactivelocation;

import android.location.Location;

/**
 * Created by riandyrn on 3/30/16.
 */
public interface LocationServiceListener {

    void onLocationUpdated(Location location);

    void onServiceExecuted(boolean isRunning);
}
