package de.fu_berlin.cdv.chasingpictures;

import android.app.Activity;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.lang.reflect.Method;

import static com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import static com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;


public class PictureSelectionActivity extends Activity implements ConnectionCallbacks, OnConnectionFailedListener {
    private static final String TAG = "PictureSelection";

    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private LocationManager mLocationManager;

    //region MockLocation
    private static final String MOCK_LOCATION_PROVIDER = "MockLocationProvider";
    private static final double MOCK_LOCATION_LAT = 52.517072;
    private static final double MOCK_LOCATION_LON = 13.388873;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_selection);

        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        setMockLocation(MOCK_LOCATION_LAT, MOCK_LOCATION_LON);
        Log.d(TAG, "Last location: " + mLocationManager.getLastKnownLocation(MOCK_LOCATION_PROVIDER));

        buildGoogleApiClient();
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    //region Google API callbacks
    @Override
    public void onConnected(Bundle connectionHint) {
        Log.d(TAG, "Connected to Google API services.");

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        Log.d(TAG, mLastLocation.toString());
        ((TextView) findViewById(R.id.textLatitude)).setText(String.valueOf(mLastLocation.getLatitude()));
        ((TextView) findViewById(R.id.textLongitude)).setText(String.valueOf(mLastLocation.getLongitude()));
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "Connection to Google API services suspended.");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(TAG, "Connection to Google API services failed: " + connectionResult);
    }
    //endregion

    //region MockLocation
    public void setMockLocation(double latitude, double longitude) {
        if (mLocationManager.getProvider(MOCK_LOCATION_PROVIDER) != null) {
            mLocationManager.removeTestProvider(MOCK_LOCATION_PROVIDER);
        }

        mLocationManager.addTestProvider(
                MOCK_LOCATION_PROVIDER,
                false, // requires network
                false, // requires satellite
                false, // requires cell
                false, // has monetary cost
                false, // supports altitude
                false, // supports speed
                false, // supports bearing
                Criteria.POWER_LOW,
                Criteria.ACCURACY_FINE
        );

        Location mockLocation = new Location(MOCK_LOCATION_PROVIDER);
        mockLocation.setLatitude(latitude);
        mockLocation.setLongitude(longitude);
        mockLocation.setAccuracy(100);
        mockLocation.setTime(System.currentTimeMillis());

        //region Complete Mock Location
        Method locationJellyBeanFixMethod = null;
        try {
            locationJellyBeanFixMethod = Location.class.getMethod("makeComplete");
            if (locationJellyBeanFixMethod != null) {
                locationJellyBeanFixMethod.invoke(mockLocation);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //endregion

        mLocationManager.setTestProviderEnabled(MOCK_LOCATION_PROVIDER, true);

        mLocationManager.setTestProviderStatus(
                MOCK_LOCATION_PROVIDER,
                LocationProvider.AVAILABLE,
                null,
                System.currentTimeMillis()
        );

        mLocationManager.setTestProviderLocation(
                MOCK_LOCATION_PROVIDER,
                mockLocation
        );
    }
    //endregion
}
