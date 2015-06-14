package de.fu_berlin.cdv.chasingpictures;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;

import org.springframework.http.ResponseEntity;

import java.io.File;
import java.util.List;

import de.fu_berlin.cdv.chasingpictures.api.LocationRequest;
import de.fu_berlin.cdv.chasingpictures.api.Picture;
import de.fu_berlin.cdv.chasingpictures.api.Place;
import de.fu_berlin.cdv.chasingpictures.api.PlacesApiResult;


public class PictureSelectionActivity extends Activity {
    private static final String TAG = "PictureSelection";
    private Location mLastLocation;
    private ImageView mImageView;
    private SwipeDetector mSwipeDetector;
    private List<Place> places;
    private int currentPlace = 0;
    private LocationHelper mLocationHelper;
    private LocationListener placeFinderListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            mLastLocation = location;
            new LocationTask().execute(location);
        }
    };

    private LocationListener distanceCalculatorListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            mLastLocation = location;
            setPlaceInfoText(currentPlace);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_selection);

        mLocationHelper = new PictureViewLocationHelper()
                .buildGoogleApiClient(getApplicationContext())
                .connect();

        mSwipeDetector = new SwipeDetector();
        mImageView = (ImageView) findViewById(R.id.picture_card_image);
        mImageView.setOnTouchListener(mSwipeDetector);

        mImageView.setOnClickListener(new ClickListener());
    }

    private class LocationTask extends AsyncTask<Location, Object, List<Place>> {

        @Override
        protected List<Place> doInBackground(Location... params) {
            if (params.length == 0)
                return null;

            LocationRequest request = new LocationRequest(getApplicationContext(), params[0]);
            ResponseEntity<PlacesApiResult> result = request.sendRequest();
            List<Place> places = result.getBody().getPlaces();

            if (places != null && places.size() > 0) {
                // Since we have places now, deregister the listener
                mLocationHelper.stopLocationUpdates(placeFinderListener);
                // And register the distance calculator
                LocationServices.FusedLocationApi.requestLocationUpdates(
                        mLocationHelper.getGoogleApiClient(),
                        LocationHelper.makeLocationRequest(),
                        distanceCalculatorListener,
                        getMainLooper() // We need to do this on the Main looper, otherwise our application will crash
                );

                // Download the first picture and update the image view
                // as soon as we have it.
                new PictureDownloader(getCacheDir()) {
                    @Override
                    protected void onPostExecute(Void aVoid) {
                        updatePicture();
                        showDelayedPlaceInfo(currentPlace);
                    }
                }.execute();

                // Download the rest of the pictures
                Picture[] pictures = new Picture[places.size()];
                for (int i = 0; i < places.size(); i++) {
                    pictures[i] = places.get(i).getPicture();
                }

                new PictureDownloader(getCacheDir())
                        .execute(pictures);
            }

            return places;
        }

        @Override
        protected void onPostExecute(List<Place> resultPlaces) {
            if (resultPlaces != null)
                places = resultPlaces;
            updatePicture();
            showDelayedPlaceInfo(currentPlace);
        }
    }

    private class PictureViewLocationHelper extends LocationHelper {
        @Override
        public void onConnected(Bundle connectionHint) {
            if (Debug.isDebuggerConnected())
                Log.i(TAG, "Connected to Google API services.");

            startLocationUpdates(
                    makeLocationRequest(),
                    placeFinderListener
            );
        }
    }

    private void updatePicture() {
        if (checkAndFixIndex()) {
            File cachedFile = places.get(currentPlace).getPicture().getCachedFile();
            if (cachedFile != null) {
                Bitmap bitmap = BitmapFactory.decodeFile(cachedFile.getPath());
                mImageView.setImageBitmap(bitmap);
            }
        }
        else {
            Toast.makeText(
                    this,
                    "No places found nearby",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    private boolean checkAndFixIndex() {
        if (places == null || places.size() == 0)
            return false;

        // Fix the index, if necessary
        if (currentPlace < 0 || currentPlace >= places.size())
            currentPlace = 0;

        return true;
    }

    private void showDelayedPlaceInfo(final int placeNr) {
        if (checkAndFixIndex()) {
            ((TextView) findViewById(R.id.place_info)).setText("");
            new Handler().postDelayed(
                    new Runnable() {
                        @Override
                        public void run() {
                            setPlaceInfoText(placeNr);
                        }
                    },
                    1000
            );
        }
    }

    private void setPlaceInfoText(int placeNr) {
        String text = String.valueOf(Math.round(getDistanceToPlace(placeNr))) + "m";
        final TextView placeInfo = (TextView) findViewById(R.id.place_info);
        placeInfo.setText(text);
    }

    private float getDistanceToPlace(int placeNr) {
        return places.get(placeNr).distanceTo(mLastLocation);
    }

    private class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (mSwipeDetector.swipeDetected()) {
                showNextPlace(mSwipeDetector.getAction());
            }
        }
    }

    private void showNextPlace(SwipeDetector.Action direction) {
        switch (direction) {
            case LR:
                if (places != null) {
                    // Because the modulo operator in Java is shit.
                    if (currentPlace > 0)
                        currentPlace--;
                    else
                        currentPlace = places.size() - 1;
                }
                break;
            case RL:
                if (places != null)
                    currentPlace = (currentPlace + 1) % places.size();
                break;
            case TB:
            case BT:
            case None:
            default:
                break;
        }

        updatePicture();
        showDelayedPlaceInfo(currentPlace);
    }
}
