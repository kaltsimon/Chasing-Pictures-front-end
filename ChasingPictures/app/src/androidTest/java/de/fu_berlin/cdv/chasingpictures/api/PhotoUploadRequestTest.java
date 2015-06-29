package de.fu_berlin.cdv.chasingpictures.api;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.test.AndroidTestCase;
import android.util.Log;

import org.springframework.http.ResponseEntity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import de.fu_berlin.cdv.chasingpictures.R;

/**
 * @author Simon Kalt
 */
public class PhotoUploadRequestTest extends AndroidTestCase {

    private static final String TAG = "PhotoUploadRequestTest";

    public void setUp() throws Exception {
        super.setUp();
    }

    public void testPhotoUpload() throws Exception {
        // Register user so we have a valid access token
        LoginRequestTest.registerUser(
                getContext(),
                "Tom",
                LoginRequestTest.getUniqueEmail(),
                "12345678"
        );

        Place place = new Place();
        place.setId(6);

        //region Save picture
        Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.upload_image);
        File tempFile = null;
        try {
            tempFile = File.createTempFile("UPLOAD_TEST", ".jpg", getContext().getCacheDir());
            FileOutputStream out = new FileOutputStream(tempFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();
        } catch (IOException e) {
            Log.e(TAG, "Image file could not be written.", e);
        }
        //endregion

        assertNotNull("Temp file is null!", tempFile);
        PhotoUploadRequest request = new PhotoUploadRequest(getContext(), place, tempFile);
        ResponseEntity<Picture> pictureResponseEntity = request.sendRequest();
        assertNotNull("No response entity received", pictureResponseEntity);
        Picture picture = pictureResponseEntity.getBody();
        assertNotNull("Response entity has empty body", picture);
    }
}