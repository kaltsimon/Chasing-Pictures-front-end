package de.fu_berlin.cdv.chasingpictures.api;

import android.app.Application;
import android.test.ApplicationTestCase;

import org.springframework.http.HttpHeaders;

import java.net.URI;

import de.fu_berlin.cdv.chasingpictures.R;
import de.fu_berlin.cdv.chasingpictures.security.Access;

/**
 * @author Simon
 */
public class ApiUtilTest extends ApplicationTestCase<Application> {

    private ApiUtil apiUtil;

    public ApiUtilTest() {
        super(Application.class);
    }

    @Override
    protected void setUp() throws Exception {
        apiUtil = new ApiUtil(getContext());
    }

    @Override
    protected void tearDown() throws Exception {

    }

    public void testCallSuccessful() throws Exception {

    }

    public void testGetURIforEndpoint() throws Exception {
        String base_uri = getContext().getString(R.string.api_url);

        URI expectedUriRegister = new URI(base_uri + getContext().getString(R.string.api_path_register));
        assertEquals("Registration URIs differ", expectedUriRegister, apiUtil.getURIforEndpoint(R.string.api_path_register));

        URI expectedUriLogin = new URI(base_uri + getContext().getString(R.string.api_path_login));
        assertEquals("Login URIs differ", expectedUriLogin, apiUtil.getURIforEndpoint(R.string.api_path_login));
    }

    public void testSetHeader() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        String accessToken = "238fz09238f0239r";
        apiUtil.setHeader(headers, R.string.api_header_accessToken, accessToken);
        assertEquals(
                "Stored header does not match input.",
                accessToken,
                headers.get(getContext().getString(R.string.api_header_accessToken)).get(0)
        );
    }

    public void testSetAccessTokenHeader() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        apiUtil.setAccessTokenHeader(headers);
        assertEquals(
                "Stored header does not match input.",
                Access.getAccessToken(getContext()),
                headers.get(getContext().getString(R.string.api_header_accessToken)).get(0)
        );
    }
}