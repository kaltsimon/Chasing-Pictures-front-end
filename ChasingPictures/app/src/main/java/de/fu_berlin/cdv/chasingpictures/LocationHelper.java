package de.fu_berlin.cdv.chasingpictures;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.HashSet;

/**
 * A class that makes access to Google's Location API easier.
 * Usage:
 *      Subclass this class and implement the method {@link com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks#onConnected(Bundle)}.
 *      There you can request location updates.
 *
 *      Then create an instance and connect like this:
 *      <code>
 *          new MyLocationHelper()
 *               .buildGoogleApiClient(getApplicationContext())
 *               .connect();
 *      </code>
 * @author Simon Kalt
 */
public abstract class LocationHelper implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "LocationHelper";

    public enum Accuracy {
        HIGH(LocationRequest.PRIORITY_HIGH_ACCURACY),
        BALANCED_POWER(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY),
        LOW_POWER(LocationRequest.PRIORITY_LOW_POWER),
        NO_POWER(LocationRequest.PRIORITY_NO_POWER);

        public final int value;

        Accuracy(int value) {
            this.value = value;
        }
    }

    public static final int STD_INTERVAL = 5000;
    public static final int STD_FASTEST_INTERVAL = 1000;

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
        if (mGoogleApiClient != null)
            mGoogleApiClient.connect();
        return this;
    }

    public GoogleApiClient getGoogleApiClient() {
        return mGoogleApiClient;
    }

    public Location getLastLocation() {
        return LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
    }

    //region Starting and stopping location updates
    public void startLocationUpdates(LocationRequest request, LocationListener listener) {
        addListener(listener);
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient,
                request,
                listener
        );
    }

    public void stopLocationUpdates(LocationListener listener) {
        if (listeners != null)
            listeners.remove(listener);

        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient,
                listener
        );
    }

    public void stopAllLocationUpdates() {
        if (listeners != null) {
            for (LocationListener listener : listeners) {
                LocationServices.FusedLocationApi.removeLocationUpdates(
                        mGoogleApiClient,
                        listener
                );
            }
            listeners.clear();
        }
    }
    //endregion

    //region Constructing location requests
    /**
     * Constructs a standard location request with the following parameters:
     * - Interval: {@link #STD_INTERVAL}
     * - Fastest Interval: {@link #STD_FASTEST_INTERVAL}
     * - Accuracy: {@link de.fu_berlin.cdv.chasingpictures.LocationHelper.Accuracy#HIGH}
     */
    public static LocationRequest makeLocationRequest() {
        return makeLocationRequest(STD_INTERVAL, STD_FASTEST_INTERVAL, Accuracy.HIGH);
    }

    /**
     * Constructs a location request with the supplied parameters.
     * @param interval Interval (in milliseconds) to receive updates in
     * @param fastestInterval Fastest interval in which you want to handle updates
     * @param accuracy The accuracy with which to receive updates
     */
    public static LocationRequest makeLocationRequest(int interval, int fastestInterval, Accuracy accuracy) {
        return new LocationRequest()
                .setInterval(interval)
                .setFastestInterval(fastestInterval)
                .setPriority(accuracy.value);
    }
    //endregion

    //region Internals
    private void addListener(LocationListener listener) {
        if (listeners == null)
            listeners = new HashSet<>();

        listeners.add(listener);
    }

    private void removeListener(LocationListener listener) {
        if (listener != null)
            listeners.remove(listener);
    }
    //endregion

    //region Overrides
    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "Connection to Google API services suspended.");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(TAG, "Connection to Google API services failed: " + connectionResult);
    }
    //endregion
}
