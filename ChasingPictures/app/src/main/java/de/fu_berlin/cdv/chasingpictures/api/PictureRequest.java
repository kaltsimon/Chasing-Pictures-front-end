package de.fu_berlin.cdv.chasingpictures.api;

import android.content.Context;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import de.fu_berlin.cdv.chasingpictures.R;

/**
 * @author Simon Kalt
 */
public class PictureRequest extends ApiRequest<PlacesApiResult> {
    private final Place place;

    protected PictureRequest(Context context, Place place) {
        super(context, R.string.api_path_picture);
        this.place = place;
    }

    @Override
    protected ResponseEntity<PlacesApiResult> send() {
        Map<String, Integer> queryParameters = new HashMap<>(2);
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
