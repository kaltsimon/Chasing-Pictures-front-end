package de.fu_berlin.cdv.chasingpictures.api;


import android.app.Application;
import android.content.res.Resources;
import android.test.ApplicationTestCase;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import de.fu_berlin.cdv.chasingpictures.R;


/**
 * @author Simon Kalt
 */
public class RegistrationTest extends ApplicationTestCase<Application> {

    Resources res;
    RestTemplate restTemplate;
    String apiURL;
    ResponseErrorHandler responseErrorHandler;

    public RegistrationTest() {
        super(Application.class);
    }

    @Override
    public void setUp() throws Exception {
        res = mContext.getResources();
        apiURL = res.getString(R.string.api_main) + res.getString(R.string.api_register);
        restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        responseErrorHandler = new ResponseErrorHandler();
        restTemplate.setErrorHandler(responseErrorHandler);
    }

    @Override
    public void tearDown() throws Exception {

    }

    public void testResponseHeaders() throws Exception {
        String expectedEmail;
        HttpHeaders responseHeaders, requestHeaders;
        ResponseEntity<RegistrationApiResult> exchange;
        List<String> accessToken;
        HttpEntity<LoginRegistrationRequest> requestEntity;

        expectedEmail = getUniqueEmail();
        LoginRegistrationRequest request = new LoginRegistrationRequest("Tom", expectedEmail, "12345678");
        responseErrorHandler.setExpectedStatusCode(200);

        requestHeaders = new HttpHeaders();
        requestHeaders.set("Content-type", MediaType.APPLICATION_JSON_VALUE);
        requestEntity = new HttpEntity<>(request, requestHeaders);

        exchange = restTemplate.exchange(apiURL, HttpMethod.POST, requestEntity, RegistrationApiResult.class);
        responseHeaders = exchange.getHeaders();
        assertNotNull(responseHeaders);

        String[] expectedHeaders = {"Access-Token", "Uid"};

        for (String header : expectedHeaders) {
            assertFalse(responseHeaders.get(header).isEmpty());
        }

    }

    public void testGetStatus() throws Exception {
        final String expectedEmail = getUniqueEmail();
        RegistrationApiResult apiResult = postRequest("Tom", expectedEmail, "12345678", 200);
        assertEquals("Status does not match.", "success", apiResult.getStatus());
    }

    public void testGetData() throws Exception {
        // Perform request
        final String expectedEmail = "a@b.de";
        final String expectedName = "Tom";
        RegistrationApiResult apiResult = postRequest(expectedName, expectedEmail, "12345678", -1);
        assertEquals("Provider does not match.", "email", apiResult.getData().getProvider());
        assertEquals("Email does not match.", expectedEmail, apiResult.getData().getEmail());
        assertEquals("Name does not match.", expectedName, apiResult.getData().getName());
    }

    public void testBlankEmailError() throws Exception {
        // Perform request
        RegistrationApiResult apiResult = postRequest("Hello", "", "12345", 403);

        // Check results
        assertFalse(apiResult.isSuccessful());
        assertEquals("API return status is wrong.", "error", apiResult.getStatus());
        assertEquals("Email error message is wrong.", "can't be blank", apiResult.getErrors().getErrorMessages().get("email").get(0));
    }

    public void testBlankPasswordError() throws Exception {
        // Perform request
        RegistrationApiResult apiResult = postRequest("Hello", "a@b.de", "", 403);

        // Check results
        assertFalse(apiResult.isSuccessful());
        assertEquals("API return status is wrong.", "error", apiResult.getStatus());
        assertEquals("Email error message is wrong.", "can't be blank", apiResult.getErrors().getErrorMessages().get("password").get(0));
    }

    /**
     * Constructs an API POST request with the given parameters
     * @param name
     * @param email
     * @param password
     * @param expectedStatusCode pass a HTTP status code you want to check against, or -1 if you don't want to check
     * @return
     */
    public RegistrationApiResult postRequest(String name, String email, String password, final int expectedStatusCode) {
        LoginRegistrationRequest request = new LoginRegistrationRequest(name, email, password);
        responseErrorHandler.setExpectedStatusCode(expectedStatusCode);
        return restTemplate.postForObject(apiURL, request, RegistrationApiResult.class);
    }


    public String getUniqueEmail() {
        return String.valueOf(new Date().getTime()) + "@cdv.de";
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