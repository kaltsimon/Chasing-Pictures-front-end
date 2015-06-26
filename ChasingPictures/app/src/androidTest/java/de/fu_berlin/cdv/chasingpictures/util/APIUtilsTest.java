package de.fu_berlin.cdv.chasingpictures.util;

import android.app.Application;
import android.test.ApplicationTestCase;

import java.net.URI;

import de.fu_berlin.cdv.chasingpictures.R;

/**
 * @author Simon
 */
public class APIUtilsTest extends ApplicationTestCase<Application> {

    public APIUtilsTest() {
        super(Application.class);
    }

    @Override
    protected void setUp() throws Exception {
    }

    @Override
    protected void tearDown() throws Exception {

    }

    public void testCallSuccessful() throws Exception {

    }

    public void testGetURIforEndpoint() throws Exception {
        String base_uri = getContext().getString(R.string.api_url);

        URI expectedUriRegister = new URI(base_uri + getContext().getString(R.string.api_path_register));
        assertEquals("Registration URIs differ", expectedUriRegister.toString(), APIUtils.getURIforEndpoint(getContext(), R.string.api_path_register));

        URI expectedUriLogin = new URI(base_uri + getContext().getString(R.string.api_path_login));
        assertEquals("Login URIs differ", expectedUriLogin.toString(), APIUtils.getURIforEndpoint(getContext(), R.string.api_path_login));
    }
}