package de.fu_berlin.cdv.chasingpictures.api;

import android.content.Context;
import android.location.Location;
import android.support.annotation.NonNull;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import de.fu_berlin.cdv.chasingpictures.R;
import de.fu_berlin.cdv.chasingpictures.security.Access;

/**
 * API request to find places near a given location.
 * TODO: Rename this class to something like PlaceRequest?
 *
 * @author Simon Kalt
 */
public class LocationRequest extends ApiRequest<PlacesApiResult> {
    @NonNull
    private final Location location;

    /**
     * Create a new request.
     *
     * @param context The current context
     * @param location The location from which to search for places
     */
    public LocationRequest(Context context, @NonNull Location location) {
        super(context, R.string.api_path_location_request);
        this.location = location;
    }

    @Override
    protected ResponseEntity<PlacesApiResult> send() {
        Map<String, Double> queryParameters = new HashMap<>(2);
        queryParameters.put("latitude", location.getLatitude());
        queryParameters.put("longitude", location.getLongitude());

        return restTemplate.exchange(
                apiUri,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                PlacesApiResult.class,
                queryParameters
        );
    }

    RestTemplate getRestTemplate() {
        return restTemplate;
    }
}
