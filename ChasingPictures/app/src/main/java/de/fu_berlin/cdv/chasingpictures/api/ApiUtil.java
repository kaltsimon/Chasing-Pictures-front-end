package de.fu_berlin.cdv.chasingpictures.api;

import android.content.Context;

import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import de.fu_berlin.cdv.chasingpictures.R;

/**
 * @author Simon Kalt
 */
public class ApiUtil {
    private final Context context;

    public ApiUtil(Context context) {
        this.context = context;
    }

    /**
     * Builds a basic JSON rest template for sending requests.
     * @return A RestTemplate with a {@link org.springframework.http.converter.json.MappingJackson2HttpMessageConverter} attached.
     */
    public static RestTemplate buildJsonRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler());
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

    /**
     * Retrieves the API URI for the specified endpoint.
     * @param endpointId An android resource id pointing to an R.strings.api_path_* value
     * @return The URI to send your request to
     */
    public URI getURIforEndpoint(int endpointId) {
        try {
            URI apiUrl = new URI(context.getString(R.string.api_url));
            return apiUrl.resolve(context.getString(endpointId));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Returns all headers for the header field named by the given resource ID.
     * @param responseEntity The request response
     * @param resourceId The resource ID for the header field
     * @return A list of strings containing the values for this header field
     */
    public List<String> getHeaders(ResponseEntity<?> responseEntity, int resourceId) {
        return responseEntity.getHeaders().get(context.getString(resourceId));
    }

    /**
     * Returns <i>the first</i> header for the header field named by the given resource ID.
     * @param responseEntity The request response
     * @param resourceId The resource ID for the header field
     * @return A strings containing the value of this header field
     */
    public String getHeader(ResponseEntity<?> responseEntity, int resourceId) {
        List<String> headers = getHeaders(responseEntity, resourceId);
        return headers.isEmpty() ? null : headers.get(0);
    }

    /**
     * This default error handler, ignores the 403 error code,
     * which is not always a fatal error.
     */
    public static class DefaultAPIResponseErrorHandler extends DefaultResponseErrorHandler {
        public void handleError(ClientHttpResponse response) throws IOException {
            if (response.getStatusCode().value() == 403) {
                // Registration was denied,
                // do nothing and return.
                return;
            }
            super.handleError(response);
        }
    }
}
