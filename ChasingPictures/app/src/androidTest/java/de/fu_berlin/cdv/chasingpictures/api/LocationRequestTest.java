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
    //region MockLocation
    private static final String MOCK_LOCATION_PROVIDER = "MockLocationProvider";
    private static final double MOCK_LOCATION_LAT = 52.517072;
    private static final double MOCK_LOCATION_LON = 13.388873;
    //endregion

    @Override
    public void setUp() throws Exception {
        responseErrorHandler = new ResponseErrorHandler();
    }

    @Override
    public void tearDown() throws Exception {

    }

    public void testLocationRequest() throws Exception {
        // Register first
        LoginRequestTest.registerUser(
                getContext(),
                "Tom",
                LoginRequestTest.getUniqueEmail(),
                "12345678"
        );

        Location location = new Location(MOCK_LOCATION_PROVIDER);
        location.setLatitude(MOCK_LOCATION_LAT);
        location.setLongitude(MOCK_LOCATION_LON);

        LocationRequest locationRequest = new LocationRequest(getContext(), location);
        responseErrorHandler.setExpectedStatusCode(200);
        locationRequest.getRestTemplate().setErrorHandler(responseErrorHandler);

        ResponseEntity<PlacesApiResult> result = locationRequest.sendRequest();

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
}
