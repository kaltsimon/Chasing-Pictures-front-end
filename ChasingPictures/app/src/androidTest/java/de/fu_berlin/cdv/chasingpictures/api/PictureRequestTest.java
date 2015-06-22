package de.fu_berlin.cdv.chasingpictures.api;

import android.test.AndroidTestCase;

import junit.framework.TestCase;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * @author Simon
 */
public class PictureRequestTest extends AndroidTestCase {
    @Override
    public void setUp() throws Exception {
        super.setUp();


    }

    public void testPictureRequest() throws Exception {
        Place p = new Place();
        p.setId(6);

        // Register user so we have a valid access token
        LoginRequestTest.registerUser(
                getContext(),
                "Tom",
                LoginRequestTest.getUniqueEmail(),
                "12345678"
        );

        PictureRequest request = new PictureRequest(getContext(), p);

        ResponseEntity<PictureApiResult> responseEntity = request.sendRequest();
        assertEquals("HTTP Status not OK", responseEntity.getStatusCode(), HttpStatus.OK);
        PictureApiResult body = responseEntity.getBody();
        assertNotNull("API response contains no data.", body);
        List<Picture> pictures = body.getData();
        assertFalse("Picture list is empty", pictures == null || pictures.isEmpty());

        // TODO: More testing
    }
}