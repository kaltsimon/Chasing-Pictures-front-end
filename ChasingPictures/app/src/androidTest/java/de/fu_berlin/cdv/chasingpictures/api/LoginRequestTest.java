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
        ResponseEntity<RegistrationApiResult> responseEntity;
        responseEntity = registerUser(context, NAME, getUniqueEmail(), PASSWORD);
        checkAccess(context, responseEntity);
    }

    public void testResponseHeaders() throws Exception {
        LoginRequest<RegistrationApiResult> request;
        request = LoginRequest.makeRegistrationRequest(
                getContext(), NAME, getUniqueEmail(), PASSWORD);
        ResponseEntity<RegistrationApiResult> response = request.send();

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
        RegistrationApiResult apiResult = registerUser(getContext(), NAME, "", PASSWORD).getBody();

        assertEquals("API return status is wrong.", "error", apiResult.getStatus());
        assertEquals("Email error message is wrong.", "can't be blank", apiResult.getErrors().getErrorMessages().get("email").get(0));
    }

    public void testBlankPasswordError() throws Exception {
        // Perform request
        RegistrationApiResult apiResult = registerUser(getContext(), NAME, getUniqueEmail(), "").getBody();

        assertEquals("API return status is wrong.", "error", apiResult.getStatus());
        assertEquals("Password error message is wrong.", "can't be blank", apiResult.getErrors().getErrorMessages().get("password").get(0));
    }

    public void testMakeLoginRequest() throws Exception {
        // Set up
        Context context = getContext();
        String email = getUniqueEmail();
        ResponseEntity<RegistrationApiResult> registerResult = registerUser(context, NAME, email, PASSWORD);
        assertEquals("Registration not successful.", registerResult.getStatusCode(), HttpStatus.OK);

        LoginRequest<LoginApiResult> request;
        request = LoginRequest.makeLoginRequest(context, email, PASSWORD);
        ResponseEntity<LoginApiResult> responseEntity = request.send();
        checkAccess(context, responseEntity);
    }

    private void checkAccess(Context context, ResponseEntity<?> responseEntity) {
        Access.setAccess(context, responseEntity);
        assertTrue("No access!", Access.hasAccess(context));
        Access.revokeAccess(context);
        assertFalse("Access where it shouldn't be!", Access.hasAccess(context));
    }

    public static ResponseEntity<RegistrationApiResult> registerUser(Context context, String name, String email, String password) {
        LoginRequest<RegistrationApiResult> request;
        request = LoginRequest.makeRegistrationRequest(context, name, email, password);
        ResponseEntity<RegistrationApiResult> responseEntity = request.send();
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