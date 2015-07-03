package de.fu_berlin.cdv.chasingpictures;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * This is largely copied from Mapbox' {@link com.mapbox.mapboxsdk.overlay.GpsLocationProvider GpsLocationProvider}.
 *
 * @author Simon Kalt
 */
public class LocationHelper2 {
    /**
     * The default minimum time interval for location updates (5 seconds).
     */
    public static final int DEFAULT_MIN_TIME = 5000;
    /**
     * The default minimum distance for location updates (5 meters).
     */
    public static final int DEFAULT_MIN_DISTANCE = 5;
    private final Map<LocationListener, ListenerInfo> listeners = new HashMap<>();
    private LocationManager mLocationManager;

    /**
     * Constructs a new location helper.
     *
     * @param context The current context
     */
    public LocationHelper2(@NonNull Context context) {
        mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    /**
     * Returns the last known location with the highest available accuracy.
     * <p/>
     * TODO: Only update in specified interval?
     */
    @Nullable
    public Location getLastKnownLocation() {
        return getLocation(false);
    }

    private Location getLocation(boolean registerListeners) {
        Location location = null;

        for (final String provider : mLocationManager.getProviders(true)) {
            if (LocationManager.GPS_PROVIDER.equals(provider)
                    || LocationManager.PASSIVE_PROVIDER.equals(provider)
                    || LocationManager.NETWORK_PROVIDER.equals(provider)) {
                Location lastKnownLocation = mLocationManager.getLastKnownLocation(provider);

                if (location == null)
                    location = lastKnownLocation;
                else if (lastKnownLocation != null && lastKnownLocation.getAccuracy() > location.getAccuracy())
                    location = lastKnownLocation;

                if (registerListeners) {
                    for (Map.Entry<LocationListener, ListenerInfo> entry : listeners.entrySet()) {
                        ListenerInfo info = entry.getValue();
                        LocationListener listener = entry.getKey();

                        if (location != null)
                            listener.onLocationChanged(location);
                        mLocationManager.removeUpdates(listener);
                        mLocationManager.requestLocationUpdates(provider, info.minTime, info.minDistance, listener);
                    }
                }
            }
        }

        return location;
    }

    //region Starting and stopping location updates

    /**
     * Start location updates for the given listener.
     *
     * @param newListener
     * @param minTime
     * @param minDistance
     * @return
     */
    public LocationHelper2 startLocationUpdates(@NonNull LocationListener newListener, long minTime, float minDistance) {
        addListener(newListener, minTime, minDistance);
        getLocation(true);
        return this;
    }

    /**
     * Stop location updates on all registered listeners.
     */
    public void stopAllLocationUpdates() {
        for (Map.Entry<LocationListener, ListenerInfo> entry : listeners.entrySet()) {
            mLocationManager.removeUpdates(entry.getKey());
        }

        listeners.clear();
    }

    /**
     * Stop location updates on the given listener.
     *
     * @param listener Listener to stop location updates for
     */
    public void stopLocationUpdates(LocationListener listener) {
        mLocationManager.removeUpdates(listener);
        listeners.remove(listener);
    }
    //endregion

    //region Internals

    /**
     * Add a listener to the local reference of listeners.
     *
     * @param listener Listener to add
     */
    private void addListener(LocationListener listener, long minTime, float minDistance) {
        listeners.put(listener, new ListenerInfo(minTime, minDistance));
    }

    /**
     * Remove a listener from the local reference of listeners.
     *
     * @param listener Listener to removeg
     */
    private void removeListener(LocationListener listener) {
        if (listener != null)
            listeners.remove(listener);
    }

    /**
     * Info about location listeners.
     */
    private static class ListenerInfo {
        public final long minTime;
        public final float minDistance;

        private ListenerInfo(long minTime, float minDistance) {
            this.minTime = minTime;
            this.minDistance = minDistance;
        }
    }
    //endregion

    /**
     * Class for easy {@link LocationListener} implementation.
     * All methods except {@link #onLocationChanged(Location)} are implemented as stubs.
     */
    public static abstract class EasyLocationListener implements LocationListener {

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
}
