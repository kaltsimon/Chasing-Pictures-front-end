package de.fu_berlin.cdv.chasingpictures.api;

import android.content.Context;
import android.support.annotation.NonNull;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import de.fu_berlin.cdv.chasingpictures.R;

/**
 * API request for retrieving all available pictures for a given place.
 *
 * @author Simon Kalt
 */
public class PictureRequest extends ApiRequest<PlacesApiResult> {
    private final Place place;

    /**
     * Create a new request.
     *
     * @param context The current context
     * @param place   The place for which to get the pictures
     */
    public PictureRequest(Context context, @NonNull Place place) {
        super(context, R.string.api_path_picture);
        this.place = place;
    }

    @Override
    protected ResponseEntity<PlacesApiResult> send() {
        Map<String, Integer> queryParameters = new HashMap<>(1);
        queryParameters.put("placeId", place.getId());

        return restTemplate.exchange(
                apiUri,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                PlacesApiResult.class,
                queryParameters
        );
    }
}
