package de.fu_berlin.cdv.chasingpictures.location;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

/**
 * Class for easy {@link LocationListener} implementation.
 * All methods except {@link #onLocationChanged(Location)} are implemented as stubs.
 */
public abstract class EasyLocationListener implements LocationListener {

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
