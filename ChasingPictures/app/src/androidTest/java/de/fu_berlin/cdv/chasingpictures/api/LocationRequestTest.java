package de.fu_berlin.cdv.chasingpictures.api;

import android.app.Application;
import android.content.res.Resources;
import android.test.ApplicationTestCase;

import junit.framework.TestCase;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

import de.fu_berlin.cdv.chasingpictures.R;

/**
 * @author Simon
 */
public class LocationRequestTest extends ApplicationTestCase<Application> {

    public LocationRequestTest() {
        super(Application.class);
    }

    Resources res;
    RestTemplate restTemplate;
    String apiURL;
    ResponseErrorHandler responseErrorHandler;
    ApiUtil apiUtil;

    @Override
    public void setUp() throws Exception {
        apiUtil = new ApiUtil(getContext());
        res = getContext().getResources();
        apiURL = res.getString(R.string.api_url) + res.getString(R.string.api_path_register);
        restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        responseErrorHandler = new ResponseErrorHandler();
        restTemplate.setErrorHandler(responseErrorHandler);
    }

    @Override
    public void tearDown() throws Exception {

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
}