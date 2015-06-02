package de.fu_berlin.cdv.chasingpictures.api;

import android.content.Context;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import de.fu_berlin.cdv.chasingpictures.R;

/**
 * @author Simon Kalt
 */
public class Util {
    private final Context context;

    public Util(Context context) {
        this.context = context;
    }

    /**
     * Builds a basic JSON rest template for sending requests.
     * @return A RestTemplate with a {@link org.springframework.http.converter.json.MappingJackson2HttpMessageConverter} attached.
     */
    public static RestTemplate buildJsonRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        return restTemplate;
    }

    /**
     * Checks whether the call to the API was successful.
     * @param result Response received from the API.
     * @return {@code} true if the call was successful
     */
    public boolean callSuccessful(ApiResult result) {
        return result.getStatus().equals(context.getString(R.string.api_status_success));
    }
}
