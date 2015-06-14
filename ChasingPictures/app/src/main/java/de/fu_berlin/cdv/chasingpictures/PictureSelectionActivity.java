package de.fu_berlin.cdv.chasingpictures;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;

import com.google.android.gms.location.LocationListener;

import org.springframework.http.ResponseEntity;

import java.util.List;

import de.fu_berlin.cdv.chasingpictures.PictureCard.OnFragmentInteractionListener;
import de.fu_berlin.cdv.chasingpictures.api.LocationRequest;
import de.fu_berlin.cdv.chasingpictures.api.Place;
import de.fu_berlin.cdv.chasingpictures.api.PlacesApiResult;


public class PictureSelectionActivity extends Activity implements OnFragmentInteractionListener {
    private static final String TAG = "PictureSelection";
    private Location mLastLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_selection);

        new PictureViewLocationHelper()
                .buildGoogleApiClient(getApplicationContext())
                .connect();
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
            ResponseEntity<PlacesApiResult> result = request.sendRequest();
            List<Place> places = result.getBody().getPlaces();

            for (Place place : places) {
                new PictureDownloader(getCacheDir()).execute(place.getPicture());
            }

            return places;
        }

        @Override
        protected void onPostExecute(List<Place> places) {
            // TODO: Only do this once! Once a second location update comes in, this crashes the activity
            // Add the picture view Fragment
            Fragment pictureCard = PictureCard.newInstance(mLastLocation, places.toArray(new Place[places.size()]));

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            fragmentTransaction.add(R.id.picture_selection_layout, pictureCard);
            fragmentTransaction.commit();
        }
    }

    private class PictureViewLocationHelper extends LocationHelper {
        @Override
        public void onConnected(Bundle connectionHint) {
            if (Debug.isDebuggerConnected())
                Log.i(TAG, "Connected to Google API services.");

            startLocationUpdates(
                    makeLocationRequest(
                            10000,
                            50000,
                            Accuracy.HIGH
                    ),
                    new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            mLastLocation = location;
                            new LocationTask().execute(location);
                        }
                    }
            );
        }
    }
}
