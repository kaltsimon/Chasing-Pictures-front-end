package de.fu_berlin.cdv.chasingpictures;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mapbox.mapboxsdk.overlay.GpsLocationProvider;
import com.mapbox.mapboxsdk.views.MapView;

import java.io.File;
import java.util.List;

import de.fu_berlin.cdv.chasingpictures.api.LocationTask;
import de.fu_berlin.cdv.chasingpictures.api.Picture;
import de.fu_berlin.cdv.chasingpictures.api.Place;
import de.fu_berlin.cdv.chasingpictures.location.EasyLocationListener;
import de.fu_berlin.cdv.chasingpictures.location.LocationHelper;
import de.fu_berlin.cdv.chasingpictures.util.Utilities;

import static de.fu_berlin.cdv.chasingpictures.MapLayoutView.*;
import static de.fu_berlin.cdv.chasingpictures.location.LocationHelper.DEFAULT_MIN_DISTANCE;
import static de.fu_berlin.cdv.chasingpictures.location.LocationHelper.DEFAULT_MIN_TIME;


public class PictureSelectionActivity extends Activity {
    private static final String TAG = "PictureSelection";
    public static final String EXTRA_LOCATION = "de.fu_berlin.cdv.chasingpictures.EXTRA_LOCATION";
    private Location mLastLocation;
    private ImageView mImageView;
    private SwipeDetector mSwipeDetector;
    private List<Place> places;
    private int currentPlace = 0;
    private LocationHelper mLocationHelper;
    private ProgressBar mLocationProgressBar;
    private ProgressBar mImageProgressBar;
    private Button mChasePictureButton;

    private LocationListener placeFinderListener = new EasyLocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            mLastLocation = location;
            new MyLocationTask().execute(location);
        }
    };
    private LocationListener distanceCalculatorListener = new EasyLocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            mLastLocation = location;
            setPlaceInfoText(currentPlace);
        }
    };
    private TextView mPlaceDistance;
    private MapView mapView;

    public static Intent createIntent(Context context, Location location) {
        Intent intent = new Intent(context, PictureSelectionActivity.class);
        intent.putExtra(EXTRA_LOCATION, location);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_selection);
        mPlaceDistance = (TextView) findViewById(R.id.place_distance);
        mapView =  (MapView) this.findViewById(R.id.mapview);
        MapLayoutView mapLayoutView = new MapLayoutView(this, mapView, Maps.mMap, new MyUserLocationOverlay(mapView, this) {
            @Override
            public void onLocationChanged(Location location, GpsLocationProvider source) {
                super.onLocationChanged(location, source);

                // TODO: Show distance to place
            }
        });

        mapLayoutView.init().startTracking();

        mLocationHelper = new LocationHelper(this);

        Location lastLocation = mLocationHelper.getLastKnownLocation();
        if (lastLocation == null) {
            lastLocation = getIntent().getParcelableExtra(EXTRA_LOCATION);
        }
        if (lastLocation != null) {
            mLastLocation = lastLocation;
            new MyLocationTask(false).execute(lastLocation);
        }

        mLocationHelper.startLocationUpdates(placeFinderListener, DEFAULT_MIN_TIME, DEFAULT_MIN_DISTANCE);

        mSwipeDetector = new SwipeDetector();
        mImageView = (ImageView) findViewById(R.id.picture_card_image);
        mImageView.setColorFilter(Menu.GRAYSCALE_FILTER);
        mImageView.setOnTouchListener(mSwipeDetector);
        mImageView.setOnClickListener(new ClickListener());
        mChasePictureButton = (Button) findViewById(R.id.chasePictureButton);

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

    private class MyLocationTask extends LocationTask {

        private final boolean exitOnEmptyResult;

        public MyLocationTask(boolean exitOnEmptyResult) {
            super(getApplicationContext());
            this.exitOnEmptyResult = exitOnEmptyResult;
        }

        public MyLocationTask() {
            this(true);
        }

        @Override
        protected void onPostExecute(@Nullable List<Place> resultPlaces) {
            if (resultPlaces == null || resultPlaces.isEmpty()) {
                // TODO: Show better error and do not exit activity
                Utilities.showError(getApplicationContext(), R.string.error_location_no_places);
                if (exitOnEmptyResult) {
                    finish();
                }
                return;
            }

            places = resultPlaces;
            checkAndFixIndex();

            // Since we have places now, de-register the listener
            mLocationHelper.stopLocationUpdates(placeFinderListener);

            // And register the distance calculator
            mLocationHelper.startLocationUpdates(distanceCalculatorListener, DEFAULT_MIN_TIME, DEFAULT_MIN_DISTANCE);

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
                mChasePictureButton.setVisibility(View.VISIBLE);
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
            mPlaceDistance.setVisibility(View.INVISIBLE);
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
        String text = String.valueOf(Math.round(getDistanceToPlace(placeNr))) + " m";
        mPlaceDistance.setText(text);
        mPlaceDistance.setVisibility(View.VISIBLE);
    }

    private float getDistanceToPlace(int placeNr) {
        return places.get(placeNr).distanceTo(mLastLocation);
    }

    public void startSearch(View view) {
        if (checkAndFixIndex()) {
            Intent intent = Maps.createIntent(this, places.get(currentPlace));
            startActivity(intent);
        }
    }

    private class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (mSwipeDetector.swipeDetected()) {
                showNextPlace(mSwipeDetector.getAction());
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
