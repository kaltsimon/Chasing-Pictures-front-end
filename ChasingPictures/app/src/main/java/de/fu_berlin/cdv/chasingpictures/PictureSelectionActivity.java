package de.fu_berlin.cdv.chasingpictures;

import android.app.Activity;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.springframework.http.ResponseEntity;

import java.lang.reflect.Method;
import java.util.List;

import de.fu_berlin.cdv.chasingpictures.api.LocationRequest;
import de.fu_berlin.cdv.chasingpictures.api.Place;
import de.fu_berlin.cdv.chasingpictures.api.PlacesApiResult;

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

        // This currently does not work!
        // buildGoogleApiClient();

        // Instead we manually get the mock location and send it.
        Location lastKnownLocation = mLocationManager.getLastKnownLocation(MOCK_LOCATION_PROVIDER);
        Log.d(TAG, "Last location: " + lastKnownLocation);

        if (lastKnownLocation != null) {
            new LocationTask().execute(lastKnownLocation);
        } else {
            Toast.makeText(this, "Could not read location...", Toast.LENGTH_SHORT).show();
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    private class LocationTask extends AsyncTask<Location, Object, List<Place>> {

        @Override
        protected List<Place> doInBackground(Location... params) {
            if (params.length == 0)
                return null;

            LocationRequest request = new LocationRequest(getApplicationContext(), params[0]);
            ResponseEntity<PlacesApiResult> result = request.send();
            return result.getBody().getPlaces();
        }

        @Override
        protected void onPostExecute(List<Place> places) {
            for (Place place : places) {
                new PictureDownloader(getCacheDir()).execute(place.getPicture());
            }
        }
    }

    //region Google API callbacks
    @Override
    public void onConnected(Bundle connectionHint) {
        Log.d(TAG, "Connected to Google API services.");

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        new LocationTask().execute(mLastLocation);
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
