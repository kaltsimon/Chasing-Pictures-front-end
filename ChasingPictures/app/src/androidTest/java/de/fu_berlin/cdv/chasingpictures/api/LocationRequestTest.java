package de.fu_berlin.cdv.chasingpictures.api;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.test.ApplicationTestCase;
import android.util.Log;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.fu_berlin.cdv.chasingpictures.R;
import de.fu_berlin.cdv.chasingpictures.security.Access;

/**
 * @author Simon
 */
public class LocationRequestTest extends ApplicationTestCase<Application> {

    private static final String TAG = "LocationRequestTest";

    public LocationRequestTest() {
        super(Application.class);
    }

    Resources res;
    RestTemplate restTemplate;
    String apiURL;
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
        res = getContext().getResources();
        apiURL = res.getString(R.string.api_url) + res.getString(R.string.api_path_location_request);
        restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        responseErrorHandler = new ResponseErrorHandler();
        restTemplate.setErrorHandler(responseErrorHandler);
        mLocationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public void tearDown() throws Exception {

    }

    public void testLocationRequest() throws Exception {
        // FIXME: WRONG ORDER SINCE API WANTS THEM THAT WAY!!!
        setMockLocation(MOCK_LOCATION_LON, MOCK_LOCATION_LAT);
        Location location = mLocationManager.getLastKnownLocation(MOCK_LOCATION_PROVIDER);

        HttpHeaders headers = new HttpHeaders();
        apiUtil.setAccessTokenHeader(headers);
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<PlacesApiResult> exchange = restTemplate.exchange(
                apiURL,
                HttpMethod.GET,
                httpEntity,
                PlacesApiResult.class,
                location.getLatitude(),
                location.getLongitude());

        assertEquals("Exchange not successful!", HttpStatus.OK, exchange.getStatusCode());
        List<Place> places = exchange.getBody().getPlaces();
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