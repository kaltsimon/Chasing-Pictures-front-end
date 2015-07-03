package de.fu_berlin.cdv.chasingpictures;

import android.app.Activity;
import android.content.Intent;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import de.fu_berlin.cdv.chasingpictures.api.LocationTask;
import de.fu_berlin.cdv.chasingpictures.api.Place;


public class Menu extends Activity {

    private static final int REQUEST_PICTURE_SELECTION = 0;
    private TextView mPlaceDistance;
    private ImageView mImageNext;
    private Location location;
    private Place place;

    private final LocationListener distanceCalculatorListener = new LocationHelper2.EasyLocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            updatePlaceDistance(location);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // find and assign view fields
        mImageNext = (ImageView) findViewById(R.id.imageNext);
        mPlaceDistance = (TextView) findViewById(R.id.menuPlaceDistance);

        // Filter the *next* image to grayscale
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);
        ColorMatrixColorFilter cf = new ColorMatrixColorFilter(matrix);
        mImageNext.setColorFilter(cf);

        location = new LocationHelper2(this).getLastKnownLocation();

        new LocationTask(this) {
            @Override
            protected void onPostExecute(@Nullable List<Place> places) {
                if (places == null || places.isEmpty())
                    return;

                place = places.get(0);
                // Check for null, just to be sure
                if (place == null) return;

                // TODO: Use place to set the *next* picture
                // TODO: Register a the listener to update the distance to that place
            }
        };
    }

    public void goToPictureSelection(View view) {
        Intent intent = new Intent(this, PictureSelectionActivity.class);
        startActivityForResult(intent, REQUEST_PICTURE_SELECTION);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_PICTURE_SELECTION:
                if (resultCode == RESULT_OK) {
                    // This means, that the user successfully finished a search
                    // TODO: Update the picture *last* to the finished search
                    // TODO: Set the picture *next* to another photo
                }
                break;
        }
    }

    private void updatePlaceDistance(Location location) {
        int round = Math.round(place.distanceTo(location));
        mPlaceDistance.setText(String.format("%d m", round));
    }
}
