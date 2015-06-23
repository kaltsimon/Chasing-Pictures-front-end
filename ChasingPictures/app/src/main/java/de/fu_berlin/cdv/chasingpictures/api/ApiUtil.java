package de.fu_berlin.cdv.chasingpictures.api;

import android.content.Context;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
import de.fu_berlin.cdv.chasingpictures.security.Access;

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
        restTemplate.setErrorHandler(new DefaultAPIResponseErrorHandler());
        return restTemplate;
    }

    /**
     * Retrieves the API URI for the specified endpoint.
     * @param endpointId An android resource id pointing to an R.strings.api_path_* value
     * @return The URI to send your request to
     */
    public String getURIforEndpoint(int endpointId) {
        return context.getString(R.string.api_url) + context.getString(endpointId);
    }

    /**
     * Returns <i>the first</i> header for the header field named by the given String.
     * @param responseEntity The request response
     * @param key The resource ID for the header field
     * @return A list of strings containing the values for this header field
     */
    public static String getHeader(ResponseEntity<?> responseEntity, String key) {
        List<String> headers = getHeaders(responseEntity, key);
        return headers == null || headers.isEmpty() ? null : headers.get(0);
    }

    public static List<String> getHeaders(ResponseEntity<?> responseEntity, String key) {
        return responseEntity.getHeaders().get(key);
    }

    /**
     * This default error handler, ignores the 403 error code,
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
