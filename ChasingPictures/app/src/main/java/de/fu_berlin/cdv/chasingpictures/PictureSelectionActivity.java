package de.fu_berlin.cdv.chasingpictures;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.springframework.http.ResponseEntity;

import java.util.List;

import de.fu_berlin.cdv.chasingpictures.PictureCard.OnFragmentInteractionListener;
import de.fu_berlin.cdv.chasingpictures.api.LocationRequest;
import de.fu_berlin.cdv.chasingpictures.api.Place;
import de.fu_berlin.cdv.chasingpictures.api.PlacesApiResult;


public class PictureSelectionActivity extends Activity implements OnFragmentInteractionListener {
    private static final String TAG = "PictureSelection";

    private Location mLastLocation;

    //region MockLocation
    private static final double MOCK_LOCATION_LAT = 52.517072;
    private static final double MOCK_LOCATION_LON = 13.388873;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_selection);

        LocationHelper locationHelper = new PictureViewLocationHelper(this);
        locationHelper.buildGoogleApiClient();
        locationHelper.connect();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private class LocationTask extends AsyncTask<Location, Object, List<Place>> {

        @Override
        protected List<Place> doInBackground(Location... params) {
            if (params.length == 0)
                return null;

            LocationRequest request = new LocationRequest(getApplicationContext(), params[0]);
            ResponseEntity<PlacesApiResult> result = request.send();
            List<Place> places = result.getBody().getPlaces();

            for (Place place : places) {
                new PictureDownloader(getCacheDir()).execute(place.getPicture());
            }

            return places;
        }

        @Override
        protected void onPostExecute(List<Place> places) {
            // Add the picture view Fragment
            Fragment pictureCard = PictureCard.newInstance(places.toArray(new Place[places.size()]));

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            fragmentTransaction.add(R.id.picture_selection_layout, pictureCard);
            fragmentTransaction.commit();
        }
    }

    private class PictureViewLocationHelper extends LocationHelper {
        public PictureViewLocationHelper(Context context) {
            super(context);
        }

        @Override
        public void onConnected(Bundle connectionHint) {
            Log.d(TAG, "Connected to Google API services.");
            useMockLocation(MOCK_LOCATION_LAT, MOCK_LOCATION_LON);
            mLastLocation = waitForLastLocation();
            new LocationTask().execute(mLastLocation);
        }
    }
}
