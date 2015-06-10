package de.fu_berlin.cdv.chasingpictures;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.lang.reflect.Method;

/**
 * @author Simon Kalt
 */
public abstract class LocationHelper implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "LocationHelper";
    protected final Context mContext;
    protected GoogleApiClient mGoogleApiClient;

    private static final String MOCK_LOCATION_PROVIDER = "MockLocationProvider";
    public LocationHelper(Context context) {
        this.mContext = context;
    }

    public synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    public void connect() {
        mGoogleApiClient.connect();
    }

    /**
     * Call only after Call only after connecting!
     * M
     * @param mockLocation
     */
    public void useMockLocation(Location mockLocation) {
        LocationServices.FusedLocationApi.setMockMode(mGoogleApiClient, true);
        LocationServices.FusedLocationApi.setMockLocation(mGoogleApiClient, mockLocation);
    }

    /**
     * Call only after connecting!
     * @param latitude
     * @param longitude
     */
    public void useMockLocation(double latitude, double longitude) {
        Location mockLocation = new Location(MOCK_LOCATION_PROVIDER);
        mockLocation.setLatitude(latitude);
        mockLocation.setLongitude(longitude);
        mockLocation.setAccuracy(100);
        mockLocation.setTime(System.currentTimeMillis());

        //region Complete Mock Location
        Method locationJellyBeanFixMethod;
        try {
            locationJellyBeanFixMethod = Location.class.getMethod("makeComplete");
            if (locationJellyBeanFixMethod != null) {
                locationJellyBeanFixMethod.invoke(mockLocation);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //endregion

        useMockLocation(mockLocation);
    }

    public GoogleApiClient getGoogleApiClient() {
        return mGoogleApiClient;
    }

    public Location getLastLocation() {
        return LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
    }

    /**
     * Be aware that this call blocks until a location is available.
     * @return
     */
    public Location waitForLastLocation() {
        Location lastLocation = getLastLocation();
        while (lastLocation == null) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Log.e(TAG, Log.getStackTraceString(e));
            }
            lastLocation = getLastLocation();
        }

        return lastLocation;
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
