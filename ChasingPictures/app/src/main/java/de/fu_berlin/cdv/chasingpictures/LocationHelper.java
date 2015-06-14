package de.fu_berlin.cdv.chasingpictures;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.HashSet;

/**
 * A class that makes access to Google's Location API easier.
 * @author Simon Kalt
 */
public abstract class LocationHelper implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "LocationHelper";

    public static final int HIGH_ACCURACY = LocationRequest.PRIORITY_HIGH_ACCURACY;

    public static final int STD_INTERVAL = 5000;
    public static final int STD_FASTEST_INTERVAL = 1000;
    public static final int STD_ACCURACY = HIGH_ACCURACY;

    protected GoogleApiClient mGoogleApiClient;
    protected HashSet<LocationListener> listeners;

    public synchronized LocationHelper buildGoogleApiClient(Context context) {
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        return this;
    }

    public LocationHelper connect() {
        mGoogleApiClient.connect();
        return this;
    }

    public GoogleApiClient getGoogleApiClient() {
        return mGoogleApiClient;
    }

    public Location getLastLocation() {
        return LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
    }

    public void startLocationUpdates(LocationRequest request, LocationListener listener) {
        addListener(listener);
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient,
                request,
                listener
        );
    }

    public void stopAllLocationUpdates() {
        for (LocationListener listener : listeners) {
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    mGoogleApiClient,
                    listener
            );
        }
        listeners.clear();
    }

    /**
     * Constructs a standard location request with the following parameters:
     * - Interval: {@link #STD_INTERVAL}
     * - Fastest Interval: {@link #STD_FASTEST_INTERVAL}
     * - Accuracy: {@link #STD_ACCURACY}
     */
    public static LocationRequest makeLocationRequest() {
        return makeLocationRequest(STD_INTERVAL, STD_FASTEST_INTERVAL, STD_ACCURACY);
    }

    /**
     * Constructs a location request with the supplied parameters.
     * @param interval
     * @param fastestInterval
     * @param accuracy
     */
    public static LocationRequest makeLocationRequest(int interval, int fastestInterval, int accuracy) {
        return new LocationRequest()
                .setInterval(interval)
                .setFastestInterval(fastestInterval)
                .setPriority(accuracy);
    }

    private void addListener(LocationListener listener) {
        if (listeners == null)
            listeners = new HashSet<>();

        listeners.add(listener);
    }

    private void removeListener(LocationListener listener) {
        if (listener != null)
            listeners.remove(listener);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "Connection to Google API services suspended.");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(TAG, "Connection to Google API services failed: " + connectionResult);
    }
}
