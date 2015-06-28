package de.fu_berlin.cdv.chasingpictures;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

import de.fu_berlin.cdv.chasingpictures.api.Place;


public class Maps extends Activity {

    public static final String EXTRA_PLACE = "de.fu_berlin.cdv.chasingpictures.EXTRA_PLACE";
    private static final String TAG = "MapActivity";
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
        UserLocationOverlay overlay = new UserLocationOverlay(provider, mv) {
            @Override
            public void onLocationChanged(Location location, GpsLocationProvider source) {
                super.onLocationChanged(location, source);
                ((TextView) findViewById(R.id.buttonDistance)).setText(String.valueOf(Math.round(place.distanceTo(location))));
            }
        };
        overlay.setTrackingMode(UserLocationOverlay.TrackingMode.FOLLOW);
        overlay.setRequiredZoom(18);
        overlay.enableMyLocation();
        mv.addOverlay(overlay);
        provider.startLocationProvider(overlay);

        Marker m = new Marker(mv, "Berlin", "Germany", berlin);


        m.setIcon(new Icon(this, Icon.Size.SMALL, "marker-stroked", "FF0000"));
        mv.addMarker(m);

        RelativeLayout layout = (RelativeLayout) findViewById(R.id.maps_layout);
        imageView = (ImageView) findViewById(R.id.imageSearch);
    }

    @Override
    protected void onStart() {
        super.onStart();
        String cachedFilePath = place.getPicture().getCachedFile().getPath();
        Bitmap bitmap = BitmapFactory.decodeFile(cachedFilePath);
        ImageView imageView = (ImageView) findViewById(R.id.imageSearch);
        imageView.setImageBitmap(bitmap);
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
        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
        final LayoutParams newParams = new LayoutParams(layoutParams);
        final int height = imageView.getHeight();

        if (imageViewVisible) {
            newParams.setMargins(0, 0, 0, -height);
        }
        else {
            newParams.setMargins(0, 0, 0, 0);
        }

        imageView.setLayoutParams(newParams);
        imageViewVisible = !imageViewVisible;
    }
}
