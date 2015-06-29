package de.fu_berlin.cdv.chasingpictures;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import de.fu_berlin.cdv.chasingpictures.util.Utilities;


public class PictureSelectionActivity extends Activity {
    private static final String TAG = "PictureSelection";
    private Location mLastLocation;
    private ImageView mImageView;
    private SwipeDetector mSwipeDetector;
    private List<Place> places;
    private int currentPlace = 0;
    private LocationHelper mLocationHelper;
    private ProgressBar mLocationProgressBar;
    private ProgressBar mImageProgressBar;

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

    private class PictureViewLocationHelper extends LocationHelper {
        @Override
        public void onConnected(Bundle connectionHint) {
            Log.d(TAG, "Connected to Google API services.");

            // TODO: Find sensible values for location updates, i.e. when do we want to search for new places
            startLocationUpdates(
                    makeLocationRequest(),
                    placeFinderListener
            );
        }
    }

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

        mLocationProgressBar = (ProgressBar) findViewById(R.id.locationProgressBar);
        mImageProgressBar = (ProgressBar) findViewById(R.id.imageProgressBar);
    }

    private class MyPictureDownloader extends PictureDownloader {
        public MyPictureDownloader() {
            super(getCacheDir(), true);
        }

        @Override
        protected void handleProgressUpdate(@NonNull Progress progress) {
            if (progress.getState() == currentPlace) {
                updatePicture();
                showDelayedPlaceInfo(currentPlace);
            }
        }

        @Override
        protected void handleException(@Nullable Throwable exception) {
            super.handleException(exception);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            updatePicture();
            showDelayedPlaceInfo(currentPlace);
        }
    }

    private class LocationTask extends AsyncTask<Location, Object, List<Place>> {

        @Override
        protected List<Place> doInBackground(Location... params) {
            if (params.length == 0 || params[0] == null)
                return null;

            LocationRequest request = new LocationRequest(getApplicationContext(), params[0]);
            ResponseEntity<PlacesApiResult> result = request.sendRequest();
            PlacesApiResult body = result == null ? null : result.getBody();
            return body == null ? null : body.getPlaces();
        }

        @Override
        protected void onPostExecute(List<Place> resultPlaces) {
            if (resultPlaces == null || resultPlaces.isEmpty()) {
                Utilities.showError(getApplicationContext(), "No places found nearby");
                finish();
                return;
            }

            places = resultPlaces;
            checkAndFixIndex();

            // Since we have places now, de-register the listener
            mLocationHelper.stopLocationUpdates(placeFinderListener);

            // And register the distance calculator
            mLocationHelper.startLocationUpdates(LocationHelper.makeLocationRequest(), distanceCalculatorListener);

            // Hide the location progress bar
            mLocationProgressBar.setVisibility(View.GONE);
            mImageProgressBar.setVisibility(View.VISIBLE);

            // Collect the pictures
            Picture[] pictures = new Picture[places.size()];
            for (int i = 0; i < places.size(); i++) {
                pictures[i] = places.get(i).getFirstPicture();
            }

            // Download the pictures
            new MyPictureDownloader().execute(pictures);
        }
    }


    private void updatePicture() {
        if (checkAndFixIndex()) {
            File cachedFile = places.get(currentPlace).getFirstPicture().getCachedFile();
            if (cachedFile != null) {
                Bitmap bitmap = BitmapFactory.decodeFile(cachedFile.getPath());
                mImageView.setImageBitmap(bitmap);
                mImageProgressBar.setVisibility(View.GONE);
                mImageView.setVisibility(View.VISIBLE);
            }
        }
        else {
            Log.e(TAG, "No places available");
        }
    }

    private boolean checkAndFixIndex() {
        if (places == null || places.isEmpty())
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
            boolean swiped = false;
            if (mSwipeDetector.swipeDetected()) {
                swiped = showNextPlace(mSwipeDetector.getAction());
            }
            if (!swiped && checkAndFixIndex()) {
                Intent intent = Maps.createIntent(getApplicationContext(), places.get(currentPlace));
                startActivity(intent);
            }
        }
    }

    private boolean showNextPlace(SwipeDetector.Action direction) {
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
                return false;
        }

        updatePicture();
        showDelayedPlaceInfo(currentPlace);
        return true;
    }
}
