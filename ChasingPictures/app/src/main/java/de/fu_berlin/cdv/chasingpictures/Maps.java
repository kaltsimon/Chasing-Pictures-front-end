package de.fu_berlin.cdv.chasingpictures;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.mapbox.mapboxsdk.geometry.BoundingBox;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.overlay.GpsLocationProvider;
import com.mapbox.mapboxsdk.overlay.Icon;
import com.mapbox.mapboxsdk.overlay.Marker;
import com.mapbox.mapboxsdk.overlay.UserLocationOverlay;
import com.mapbox.mapboxsdk.tileprovider.tilesource.ITileLayer;
import com.mapbox.mapboxsdk.tileprovider.tilesource.MapboxTileLayer;
import com.mapbox.mapboxsdk.tileprovider.tilesource.WebSourceTileLayer;
import com.mapbox.mapboxsdk.views.MapView;

import java.io.File;

import de.fu_berlin.cdv.chasingpictures.api.PhotoUploadRequestTask;
import de.fu_berlin.cdv.chasingpictures.api.Place;
import de.fu_berlin.cdv.chasingpictures.camera.CameraActivity;


public class Maps extends Activity {

    public static final String EXTRA_PLACE = "de.fu_berlin.cdv.chasingpictures.EXTRA_PLACE";
    private static final String TAG = "MapActivity";
    private static final float SHOW_CAMERA_DISTANCE = 5; // Show camera when less than 5 meters away
    private final View.OnClickListener showHidePictureClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            pictureOverlay(v);
        }
    };
    private final View.OnClickListener cameraClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), CameraActivity.class);
            startActivityForResult(intent, MainActivity.REQUEST_TAKE_PICTURE);
        }
    };
    private com.mapbox.mapboxsdk.geometry.LatLng startingPoint = new LatLng(51f, 0f);
    private MapView mv;
    private String satellite = "brunosan.map-cyglrrfu";
    private String street = "examples.map-i87786ca";
    private String terrain = "examples.map-zgrqqx0w";
    private String mMap = "miriwie.130123ed";
    private String currentLayer = "";
    private LatLng berlin =  new LatLng(52.513578, 13.415124);
    private Place place;
    private boolean imageViewVisible;
    private ImageView imageView;
    private LinearLayout imageViewLayout;
    private Button distanceButton;
    private boolean cameraButtonShown;
    private Location mLastLocation;

    public static Intent createIntent(Context context, Place target) {
        Intent intent = new Intent(context, Maps.class);
        intent.putExtra(EXTRA_PLACE, target);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Try to load the place from the intent's extra data
        try {
            place = (Place) getIntent().getSerializableExtra(EXTRA_PLACE);
        } catch (Exception ex) {
            Log.e(TAG, "Place could not be retrieved from intent", ex);
            // TODO: Show error
            // TODO: finish();
        }

        setContentView(R.layout.activity_maps);

        mv =  (MapView) this.findViewById(R.id.mapview);

        // Set Default Map Type
        replaceMapView(mMap);
        currentLayer = "mMap";

        GpsLocationProvider provider = new GpsLocationProvider(this);
        distanceButton = ((Button) findViewById(R.id.buttonDistance));
        UserLocationOverlay overlay = new UserLocationOverlay(provider, mv) {
            @Override
            public void onLocationChanged(Location location, GpsLocationProvider source) {
                super.onLocationChanged(location, source);
                mLastLocation = location;
                float distanceToDestination = place.distanceTo(location);

                if (!cameraButtonShown)
                    distanceButton.setText(String.valueOf(Math.round(distanceToDestination)));

                if (distanceToDestination < SHOW_CAMERA_DISTANCE) {
                    showCameraButton();
                } else {
                    hideCameraButton();
                }
            }
        };
        overlay.setTrackingMode(UserLocationOverlay.TrackingMode.FOLLOW);
        overlay.setRequiredZoom(18);
        overlay.enableMyLocation();
        overlay.setDrawAccuracyEnabled(false);
        overlay.setPersonBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.user_location));
        overlay.setDirectionArrowBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.user_location_pointer));

        mv.addOverlay(overlay);
        provider.startLocationProvider(overlay);

        Marker m = new Marker(mv, "Berlin", "Germany", berlin);


        m.setIcon(new Icon(this, Icon.Size.SMALL, "marker-stroked", "FF0000"));
        mv.addMarker(m);

        RelativeLayout layout = (RelativeLayout) findViewById(R.id.maps_layout);
        imageViewLayout = (LinearLayout) findViewById(R.id.imageViewLayout);
        imageView = (ImageView) findViewById(R.id.imageSearch);
    }

    private void hideCameraButton() {
        if (cameraButtonShown) {
            Drawable background = getResources().getDrawable(R.drawable.map_distance_button);
            distanceButton.setBackgroundDrawable(background);
            distanceButton.setText(String.valueOf(Math.round(place.distanceTo(mLastLocation))));
            distanceButton.setOnClickListener(showHidePictureClickListener);
        }
        cameraButtonShown = false;
    }

    private void showCameraButton() {
        if (!cameraButtonShown) {
            distanceButton.setText("");
            Drawable background = getResources().getDrawable(R.drawable.camera_button);
            distanceButton.setBackgroundDrawable(background);
            distanceButton.setOnClickListener(cameraClickListener);
        }
        cameraButtonShown = true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case MainActivity.REQUEST_TAKE_PICTURE:
                if (resultCode == RESULT_OK) {
                    final File imageFile = (File) data.getSerializableExtra(CameraActivity.EXTRA_IMAGE_FILE);
                    if (imageFile != null && imageFile.exists() && imageFile.length() > 0) {
                        new PhotoUploadRequestTask(this, place, imageFile).execute();
                    }
                }
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        String cachedFilePath = place.getFirstPicture().getCachedFile().getPath();
        Bitmap bitmap = BitmapFactory.decodeFile(cachedFilePath);
        imageView.setImageBitmap(bitmap);

        // Calculate the height of the image by hand, since imageView.getHeight doesnt work yet
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        Point window = new Point();
        getWindowManager().getDefaultDisplay().getSize(window);

        double scale = window.x/(double) width;
        int actualHeight = (int) (height * scale);

        // Move the image out of sight
        imageViewLayout.setTranslationY(actualHeight);
    }

    protected void replaceMapView(String layer) {
        ITileLayer source;
        BoundingBox box;
        if (layer.equalsIgnoreCase("OpenStreetMap")) {
            source = new WebSourceTileLayer("openstreetmap",
                    "http://tile.openstreetmap.org/{z}/{x}/{y}.png").setName("OpenStreetMap")
                    .setAttribution("© OpenStreetMap Contributors")
                    .setMinimumZoomLevel(1)
                    .setMaximumZoomLevel(18);
        } else if (layer.equalsIgnoreCase("OpenSeaMap")) {
            source = new WebSourceTileLayer("openstreetmap",
                    "http://tile.openstreetmap.org/seamark/{z}/{x}/{y}.png").setName(
                    "OpenStreetMap")
                    .setAttribution("© OpenStreetMap Contributors")
                    .setMinimumZoomLevel(1)
                    .setMaximumZoomLevel(18);
        } else if (layer.equalsIgnoreCase("mapquest")) {
            source = new WebSourceTileLayer("mapquest",
                    "http://otile1.mqcdn.com/tiles/1.0.0/osm/{z}/{x}/{y}.png").setName(
                    "MapQuest Open Aerial")
                    .setAttribution(
                            "Tiles courtesy of MapQuest and OpenStreetMap contributors.")
                    .setMinimumZoomLevel(1)
                    .setMaximumZoomLevel(18);
        } else {
            source = new MapboxTileLayer(layer);
        }
        mv.setTileSource(source);
        box = source.getBoundingBox();
        mv.setScrollableAreaLimit(box);
        mv.setMinZoomLevel(mv.getTileProvider().getMinimumZoomLevel());
        mv.setMaxZoomLevel(mv.getTileProvider().getMaximumZoomLevel());
        mv.setCenter(berlin);
        mv.setZoom(10);
    }

    public void pictureOverlay(View view){
        int height = imageView.getHeight();
        height = imageViewVisible ? height : -height;

        ViewPropertyAnimator animator = imageViewLayout.animate();
        animator.translationYBy(height);

        imageViewVisible = !imageViewVisible;
    }
}
