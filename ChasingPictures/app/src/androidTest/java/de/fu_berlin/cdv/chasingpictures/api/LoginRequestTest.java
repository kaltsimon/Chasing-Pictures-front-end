package de.fu_berlin.cdv.chasingpictures.api;

import android.app.Application;
import android.content.Context;
import android.test.ApplicationTestCase;
import android.util.Base64;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.security.SecureRandom;

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