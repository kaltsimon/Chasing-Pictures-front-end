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
 * A class that makes access to Google's Location API easier.<br>
 * Usage:
 * <ol>
 * <li>Subclass this class and implement the method {@link com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks#onConnected(Bundle)}.</li>
 * <li>Request location updates in that callback method.</li>
 * <li>Create an instance and connect like this:
 * <pre><code>
 * new MyLocationHelper()
 *      .buildGoogleApiClient(getApplicationContext())
 *      .connect();
 * </code></pre></li>
 * </ol>
 *
 * @author Simon Kalt
 */
public abstract class LocationHelper implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "LocationHelper";

    /**
     * Different values for the accuracy of a location request.
     * See the <a href="https://developers.google.com/android/reference/com/google/android/gms/location/LocationRequest">Google Documentation</a>
     * for details.
     */
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

    /**
     * The standard value for the interval in which to receive location updates (5 seconds).
     */
    public static final int STD_INTERVAL = 5000;

    /**
     * The standard value for the smallest interval in which to receive location updates (1 second).
     */
    public static final int STD_FASTEST_INTERVAL = 1000;

    protected GoogleApiClient mGoogleApiClient;
    protected HashSet<LocationListener> listeners;

    /**
     * Build the Google API client.
     *
     * @param context The current context.
     * @return The object itself for method-chaining.
     */
    public synchronized LocationHelper buildGoogleApiClient(Context context) {
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        return this;
    }

    /**
     * Connect to the Google API client.
     * Be sure to call {@link #buildGoogleApiClient(Context)} first,
     * otherwise this method does nothing.
     *
     * @return The object itself for method-chaining.
     */
    public LocationHelper connect() {
        if (mGoogleApiClient != null)
            mGoogleApiClient.connect();
        return this;
    }

    /**
     * Returns the Google API client, if available.
     */
    public GoogleApiClient getGoogleApiClient() {
        return mGoogleApiClient;
    }

    /**
     * Returns the last known location known to the {@link LocationServices#FusedLocationApi}.
     */
    public Location getLastLocation() {
        return LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
    }

    //region Starting and stopping location updates

    /**
     * Start location updates.
     *
     * @param request  A location request, for example created with {@link #makeLocationRequest()}.
     * @param listener A location listener to call, when an update comes in.
     */
    public void startLocationUpdates(LocationRequest request, LocationListener listener) {
        addListener(listener);
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient,
                request,
                listener
        );
    }

    /**
     * Stop location updates for the given listener.
     *
     * @param listener Listener to stop updates for.
     */
    public void stopLocationUpdates(LocationListener listener) {
        if (listeners != null)
            listeners.remove(listener);

        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient,
                listener
        );
    }

    /**
     * Stop location updates on all registered listeners.
     */
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
     * <ul>
     * <li>Interval to receive updates in (ms): {@link #STD_INTERVAL}</li>
     * <li>Fastest interval to receive updates in (ms): {@link #STD_FASTEST_INTERVAL}</li>
     * <li>Accuracy: {@link de.fu_berlin.cdv.chasingpictures.LocationHelper.Accuracy#HIGH}</li>
     * </ul>
     */
    public static LocationRequest makeLocationRequest() {
        return makeLocationRequest(STD_INTERVAL, STD_FASTEST_INTERVAL, Accuracy.HIGH);
    }

    /**
     * Constructs a location request with the supplied parameters.
     *
     * @param interval        Interval (in milliseconds) to receive updates in
     * @param fastestInterval Fastest interval in which you want to handle updates
     * @param accuracy        The accuracy with which to receive updates
     */
    public static LocationRequest makeLocationRequest(int interval, int fastestInterval, Accuracy accuracy) {
        return new LocationRequest()
                .setInterval(interval)
                .setFastestInterval(fastestInterval)
                .setPriority(accuracy.value);
    }
    //endregion

    //region Internals

    /**
     * Add a listener to the local reference of listeners.
     *
     * @param listener Listener to add
     */
    private void addListener(LocationListener listener) {
        if (listeners == null)
            listeners = new HashSet<>();

        listeners.add(listener);
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
