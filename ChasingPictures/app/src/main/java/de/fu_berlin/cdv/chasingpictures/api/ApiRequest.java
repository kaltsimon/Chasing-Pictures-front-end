package de.fu_berlin.cdv.chasingpictures.api;

import android.content.Context;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import de.fu_berlin.cdv.chasingpictures.security.Access;
import de.fu_berlin.cdv.chasingpictures.util.APIUtils;

/**
 * Abstract class for API requests.
 * @author Simon Kalt
 */
public abstract class ApiRequest<ResponseType> {
    protected final String apiUri;
    protected final RestTemplate restTemplate;
    protected final Context context;
    protected HttpHeaders headers;

    protected ApiRequest(Context context, int endpointResID) {
        this.context = context;
        this.apiUri = APIUtils.getURIforEndpoint(context, endpointResID);
        this.restTemplate = APIUtils.buildJsonRestTemplate();
        this.headers = new HttpHeaders();
    }

    /**
     * Sends this request to the API.
     * @return A {@link ResponseEntity} with the result of the call.
     */
    protected abstract ResponseEntity<ResponseType> send();

    /**
     * This is executed before executing a request.
     * By default it loads the stored access info into the HTTP headers.
     */
    protected void beforeSending() {
        // Store access info in HTTP headers
        Access.getAccess(context, headers);
    }

    /**
     * This is executed after executing a request.
     * By default it stores the received access info in the application.
     */
    protected void afterSending(ResponseEntity<ResponseType> responseEntity) {
        Access.setAccess(context, responseEntity);
    }

    /**
     * Sends this request to the API.
     * @return A {@link ResponseEntity} with the result of the call.
     */
    public final ResponseEntity<ResponseType> sendRequest() {
        beforeSending();
        ResponseEntity<ResponseType> response = send();
        afterSending(response);
        return response;
    }
}
