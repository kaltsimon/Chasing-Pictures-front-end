package de.fu_berlin.cdv.chasingpictures.api;

import android.app.Application;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.test.ApplicationTestCase;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author Simon
 */
public class LocationRequestTest extends ApplicationTestCase<Application> {

    private static final String TAG = "LocationRequestTest";

    public LocationRequestTest() {
        super(Application.class);
    }

    ResponseErrorHandler responseErrorHandler;
    ApiUtil apiUtil;
    LocationManager mLocationManager;
    //region MockLocation
    private static final String MOCK_LOCATION_PROVIDER = "MockLocationProvider";
    private static final double MOCK_LOCATION_LAT = 52.517072;
    private static final double MOCK_LOCATION_LON = 13.388873;
    //endregion

    @Override
    public void setUp() throws Exception {
        apiUtil = new ApiUtil(getContext());
        responseErrorHandler = new ResponseErrorHandler();
        mLocationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public void tearDown() throws Exception {

    }

    public void testLocationRequest() throws Exception {
        setMockLocation(MOCK_LOCATION_LAT, MOCK_LOCATION_LON);
        Location location = mLocationManager.getLastKnownLocation(MOCK_LOCATION_PROVIDER);

        LocationRequest locationRequest = new LocationRequest(getContext(), location);
        responseErrorHandler.setExpectedStatusCode(200);
        locationRequest.getRestTemplate().setErrorHandler(responseErrorHandler);

        ResponseEntity<PlacesApiResult> result = locationRequest.send();

        List<Place> places = result.getBody().getPlaces();
        assertNotNull(places);

        // This only works if the mock location is set properly
        assertFalse(places.isEmpty());
    }

    private class ResponseErrorHandler extends DefaultResponseErrorHandler {
        private int expectedStatusCode = -1;

        public int getExpectedStatusCode() {
            return expectedStatusCode;
        }

        public void setExpectedStatusCode(int expectedStatusCode) {
            this.expectedStatusCode = expectedStatusCode;
        }

        @Override
        public void handleError(ClientHttpResponse response) throws IOException {
            HttpStatus statusCode = response.getStatusCode();
            if (expectedStatusCode != -1) // only check if we want to
                assertEquals("Invalid status code.", expectedStatusCode, statusCode.value());
        }
    }

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