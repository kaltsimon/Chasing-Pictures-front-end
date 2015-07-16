package de.fu_berlin.cdv.chasingpictures;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mapbox.mapboxsdk.views.MapView;

import java.io.File;
import java.util.List;

import de.fu_berlin.cdv.chasingpictures.api.LocationTask;
import de.fu_berlin.cdv.chasingpictures.api.Picture;
import de.fu_berlin.cdv.chasingpictures.api.Place;
import de.fu_berlin.cdv.chasingpictures.location.EasyLocationListener;
import de.fu_berlin.cdv.chasingpictures.location.LocationHelper;
import de.fu_berlin.cdv.chasingpictures.utilities.UtilitiesPackage;

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
    private LinearLayout mPlaceDistanceView;
    private int windowWidth;
    private int mPlaceDistanceViewHeight;

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
        mapView =  (MapView) findViewById(R.id.mapview);
        mPlaceDistanceView = (LinearLayout) findViewById(R.id.distanceView);
        mImageView = (ImageView) findViewById(R.id.picture_card_image);
        mImageView.setColorFilter(Menu.GRAYSCALE_FILTER);
        mChasePictureButton = (Button) findViewById(R.id.chasePictureButton);
        mLocationProgressBar = (ProgressBar) findViewById(R.id.locationProgressBar);
        mImageProgressBar = (ProgressBar) findViewById(R.id.imageProgressBar);

        // Swipe listener
        mSwipeDetector = new SwipeDetector();
        mImageView.setOnTouchListener(mSwipeDetector);
        mImageView.setOnClickListener(new ClickListener());

        // Location listener
        mLocationHelper = new LocationHelper(this);

        // Initialize map view
        MapLayoutView mapLayoutView = new MapLayoutView(this, mapView, Maps.mMap, true);
        mapLayoutView.init().startTracking();


        Location lastLocation = mLocationHelper.getLastKnownLocation();
        if (lastLocation == null) {
            lastLocation = getIntent().getParcelableExtra(EXTRA_LOCATION);
            if (lastLocation != null) {
                mLastLocation = lastLocation;
                new MyLocationTask(false).execute(lastLocation);
            }
        }

        mLocationHelper.startLocationUpdates(placeFinderListener, DEFAULT_MIN_TIME, DEFAULT_MIN_DISTANCE);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        animateShowButtons();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Menu.MAPS_REQUEST_SEARCH:
                if (resultCode == RESULT_OK) {
                    setResult(RESULT_OK);
                    finish();
                }
                break;
        }
    }

    private class MyPictureDownloader extends PictureDownloader {
        public MyPictureDownloader() {
            super(getCacheDir(), true);
        }

        @Override
        protected void handleProgressUpdate(@NonNull Progress progress) {
            if (progress.getState() == currentPlace) {
                File cachedFile = places.get(currentPlace).getFirstPicture().getCachedFile();
                if (cachedFile != null) {
                    updatePicture(true);
                }
            }
        }

        @Override
        protected void handleException(@Nullable Throwable exception) {
            super.handleException(exception);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            updatePicture(false);
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
                UtilitiesPackage.showError(getApplicationContext(), R.string.error_location_no_places);
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


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasFocus) {
            Point windowSize = new Point();
            getWindowManager().getDefaultDisplay().getSize(windowSize);
            windowWidth = windowSize.x;
            mPlaceDistanceViewHeight = mPlaceDistanceView.getHeight();
            hideButtons();
            mPlaceDistanceView.setVisibility(View.VISIBLE);
            mChasePictureButton.setVisibility(View.VISIBLE);
        }
    }

    private void hideButtons() {
        // Somehow does not work, i.e. it isn't animated and setAlpha(1) does not work...
//        mPlaceDistanceView.animate().alpha(0);
//        mChasePictureButton.animate().alpha(0);

        mPlaceDistanceView.setTranslationY(-mPlaceDistanceViewHeight);
        mChasePictureButton.setTranslationX(windowWidth);
    }

    private void animateShowButtons() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mChasePictureButton.animate().translationX(0);
                mPlaceDistanceView.animate().translationY(0);
            }
        }, 500);
    }

    private void updatePicture(boolean isNewPicture) {

        if (checkAndFixIndex()) {
            File cachedFile = places.get(currentPlace).getFirstPicture().getCachedFile();
            setPlaceInfoText(currentPlace);
            if (cachedFile != null) {
                Bitmap bitmap = BitmapFactory.decodeFile(cachedFile.getPath());
                if (isNewPicture) {
                    hideButtons();

                    updateImageBitmap(bitmap);

                    animateShowButtons();
                } else {
                    updateImageBitmap(bitmap);
                    if (mChasePictureButton.getVisibility() != View.VISIBLE)
                        mChasePictureButton.setVisibility(View.VISIBLE);
                    if (mPlaceDistanceView.getVisibility() != View.VISIBLE)
                        mPlaceDistanceView.setVisibility(View.VISIBLE);
                }
            }
        }
        else {
            Log.e(TAG, "No places available");
        }
    }

    private void updateImageBitmap(Bitmap bitmap) {
        mImageProgressBar.setVisibility(View.GONE);
        mImageView.setImageBitmap(bitmap);
        mImageView.setVisibility(View.VISIBLE);
    }

    private boolean checkAndFixIndex() {
        if (places == null || places.isEmpty())
            return false;

        // Fix the index, if necessary
        if (currentPlace < 0 || currentPlace >= places.size())
            currentPlace = 0;

        return true;
    }

    private void showDelayedPlaceInfo(final int placeNr, boolean hideFirst) {
        if (checkAndFixIndex()) {
            if (hideFirst)
                mPlaceDistanceView.setVisibility(View.GONE);

            setPlaceInfoText(placeNr);

            if (mPlaceDistanceView.getVisibility() == View.GONE) {
                mPlaceDistanceView.setVisibility(View.VISIBLE);
            }
        }
    }

    private void setPlaceInfoText(int placeNr) {
        String text = String.valueOf(Math.round(getDistanceToPlace(placeNr)));
        mPlaceDistance.setText(text);
    }

    private float getDistanceToPlace(int placeNr) {
        return places.get(placeNr).distanceTo(mLastLocation);
    }

    public void startSearch(View view) {
        if (checkAndFixIndex()) {
            Intent intent = Maps.createIntent(this, places.get(currentPlace));
            startActivityForResult(intent, Menu.MAPS_REQUEST_SEARCH);
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

        updatePicture(true);
        return true;
    }
}
