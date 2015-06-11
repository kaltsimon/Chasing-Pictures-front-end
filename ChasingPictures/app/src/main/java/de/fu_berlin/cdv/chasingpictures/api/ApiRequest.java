package de.fu_berlin.cdv.chasingpictures.api;

import android.content.Context;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * Abstract class for API requests.
 * @author Simon Kalt
 */
public abstract class ApiRequest<ResponseType> {
    protected final ApiUtil apiUtil;
    protected final String apiUri;
    protected final RestTemplate restTemplate;
    protected final Context context;

    protected ApiRequest(Context context, int endpointResID) {
        this.context = context;
        this.apiUtil = new ApiUtil(context);
        this.apiUri = apiUtil.getURIforEndpoint(endpointResID);
        this.restTemplate = ApiUtil.buildJsonRestTemplate();
    }

    public abstract ResponseEntity<ResponseType> send();
}
