package de.fu_berlin.cdv.chasingpictures.api;

import android.content.Context;
import android.support.annotation.StringRes;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

import de.fu_berlin.cdv.chasingpictures.R;
import de.fu_berlin.cdv.chasingpictures.security.Access;

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
        this.apiUri = getURIforEndpoint(context, endpointResID);
        this.restTemplate = buildJsonRestTemplate();
        this.headers = new HttpHeaders();
    }

    /**
     * Builds a basic JSON rest template for sending requests.
     *
     * @return A RestTemplate with a {@link org.springframework.http.converter.json.MappingJackson2HttpMessageConverter} attached.
     */
    public static RestTemplate buildJsonRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        restTemplate.setErrorHandler(new DefaultAPIResponseErrorHandler());
        return restTemplate;
    }

    /**
     * Retrieves the API URI for the specified endpoint.
     *
     * @param context The current context
     * @param endpointResID A resource id pointing to an R.strings.api_path_* value
     * @return The URI to send your request to
     */
    public static String getURIforEndpoint(Context context, @StringRes int endpointResID) {
        return context.getString(R.string.api_url) + context.getString(endpointResID);
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

    /**
     * This default error handler ignores the 403 error code,
     * which is not always a fatal error.
     */
    public static class DefaultAPIResponseErrorHandler extends DefaultResponseErrorHandler {
        public void handleError(ClientHttpResponse response) throws IOException {
            final HttpStatus statusCode = response.getStatusCode();
            if (statusCode == HttpStatus.FORBIDDEN || statusCode == HttpStatus.UNAUTHORIZED) {
                // Request was denied,
                // do nothing and return.
                return;
            }
            super.handleError(response);
        }
    }
}
