package de.fu_berlin.cdv.chasingpictures.api;

import android.app.Application;
import android.content.Context;
import android.test.ApplicationTestCase;
import android.util.Base64;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.security.SecureRandom;
import java.util.List;

import de.fu_berlin.cdv.chasingpictures.security.Access;

/**
 * @author Simon
 */
public class LoginRequestTest extends ApplicationTestCase<Application> {
    private static final String NAME = "Tom";
    private static final String PASSWORD = "12345678";

    public LoginRequestTest() {
        super(Application.class);
    }

    public void testMakeRegistrationRequest() throws Exception {
        Context context = getContext();
        ResponseEntity<LoginApiResult> responseEntity;
        responseEntity = registerUser(context, NAME, getUniqueEmail(), PASSWORD);
        checkAccess(context, responseEntity);
    }

    public void testResponseHeaders() throws Exception {
        LoginRequest request;
        request = LoginRequest.makeRegistrationRequest(
                getContext(), NAME, getUniqueEmail(), PASSWORD);
        ResponseEntity<LoginApiResult> response = request.send();

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        HttpHeaders headers = response.getHeaders();
        assertNotNull(headers);

        // Check that all the access headers are there
        for (Access.Headers header : Access.Headers.values()) {
            List<String> values = headers.get(header.field);
            assertNotNull(values);
            assertFalse("Header field " + header.field + " is missing", values.isEmpty());
            String value = values.get(0);
            assertNotNull("Header value for " + header.field + " is null", value);
            assertFalse("Header value for " + header.field + " is empty", value.isEmpty());
        }

        // We can assume this exists now
        Long expiry = Long.valueOf(headers.get(Access.Headers.EXPIRY.field).get(0));
        assertTrue("Access-Token is expired", System.currentTimeMillis() / 1000 < expiry);
    }

    public void testBlankEmailError() throws Exception {
        // Perform request
        LoginApiResult apiResult = registerUser(getContext(), NAME, "", PASSWORD).getBody();

        assertEquals("API return status is wrong.", "error", apiResult.getStatus());
        assertEquals("Email error message is wrong.", "can't be blank", ((ApiErrors) apiResult.getErrors()).getErrorMessages().get("email").get(0));
    }

    public void testBlankPasswordError() throws Exception {
        // Perform request
        LoginApiResult apiResult = registerUser(getContext(), NAME, getUniqueEmail(), "").getBody();

        assertEquals("API return status is wrong.", "error", apiResult.getStatus());
        assertEquals("Password error message is wrong.", "can't be blank", ((ApiErrors) apiResult.getErrors()).getErrorMessages().get("password").get(0));
    }

    public void testMakeLoginRequest() throws Exception {
        // Set up
        Context context = getContext();
        String email = getUniqueEmail();
        ResponseEntity<LoginApiResult> registerResult = registerUser(context, NAME, email, PASSWORD);
        assertEquals("Registration not successful.", registerResult.getStatusCode(), HttpStatus.OK);

        LoginRequest request;
        request = LoginRequest.makeLoginRequest(context, email, PASSWORD);
        ResponseEntity<LoginApiResult> responseEntity = request.send();
        checkAccess(context, responseEntity);
    }

    public void testInvalidLoginRequest() throws Exception {
        // Set up
        Context context = getContext();
        String email = getUniqueEmail();

        LoginRequest request;
        request = LoginRequest.makeLoginRequest(context, email, PASSWORD);
        ResponseEntity<LoginApiResult> responseEntity = request.send();

        List<String> errors = (List<String>) responseEntity.getBody().getErrors();
        assertNotNull("No errors available.", errors);
        assertFalse("No errors available.", errors.isEmpty());
        assertNotNull("Error is empty.", errors.get(0));
        assertEquals("Error message does not match.", "Invalid login credentials. Please try again.", errors.get(0));
    }

    private void checkAccess(Context context, ResponseEntity<?> responseEntity) {
        Access.setAccess(context, responseEntity);
        assertTrue("No access!", Access.hasAccess(context));
        Access.revokeAccess(context);
        assertFalse("Access where it shouldn't be!", Access.hasAccess(context));
    }

    public static ResponseEntity<LoginApiResult> registerUser(Context context, String name, String email, String password) {
        LoginRequest request;
        request = LoginRequest.makeRegistrationRequest(context, name, email, password);
        ResponseEntity<LoginApiResult> responseEntity = request.send();
        Access.setAccess(context, responseEntity);
        return responseEntity;
    }

    public static String getUniqueEmail() {
        SecureRandom r = new SecureRandom();
        byte[] x = new byte[10];
        r.nextBytes(x);
        String rand = Base64.encodeToString(x, Base64.DEFAULT);
        rand = rand.substring(0, rand.length() - 1);

        return String.valueOf(System.nanoTime()) + rand + "@cdv.de";
    }
}