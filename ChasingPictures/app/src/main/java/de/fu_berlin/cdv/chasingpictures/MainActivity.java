package de.fu_berlin.cdv.chasingpictures;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.fu_berlin.cdv.chasingpictures.activity.Slideshow;
import de.fu_berlin.cdv.chasingpictures.api.PhotoUploadRequest;
import de.fu_berlin.cdv.chasingpictures.api.Picture;
import de.fu_berlin.cdv.chasingpictures.api.PictureRequest;
import de.fu_berlin.cdv.chasingpictures.api.Place;
import de.fu_berlin.cdv.chasingpictures.camera.CameraActivity;
import de.fu_berlin.cdv.chasingpictures.security.Access;


public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";
    public static final int REQUEST_TAKE_PICTURE = 123411235;
    private boolean triedLogin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.intro);
//        mediaPlayer.start(); // no need to call prepare(); create() does that for you
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Check if we are logged in, but only once
        if (!triedLogin && !Access.hasAccess(this)) {
            triedLogin = true;
            Intent intent = new Intent(this, LoginPage.class);
            startActivityForResult(intent, LoginPage.LOGIN);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case LoginPage.LOGIN:
                if (resultCode == RESULT_OK && Access.hasAccess(this)) {
                    Toast.makeText(this, R.string.login_success, Toast.LENGTH_SHORT).show();
                    findViewById(R.id.show_login_page_button).setEnabled(false);
                }
                break;
            case REQUEST_TAKE_PICTURE:
                if (resultCode == RESULT_OK) {
                    final File imageFile = (File) data.getSerializableExtra(CameraActivity.EXTRA_IMAGE_FILE);
                    Place place = (Place) data.getSerializableExtra(Maps.EXTRA_PLACE);

                    // Check if the file exists
                    if (imageFile != null && imageFile.exists()) {
                        // FIXME: This is only for testing purposes, usually we should only arrive here with a valid place!
                        // TODO: Check in outer if-condition
                        if (place == null) {
                            place = new Place();
                            place.setId(6);
                            place.setPictures(new ArrayList<Picture>());
                        }

                        final Place finalPlace = place;
                        new AsyncTask<Place, Void, ResponseEntity<Picture>>() {

                            @Override
                            protected ResponseEntity<Picture> doInBackground(Place... params) {
                                PhotoUploadRequest request = new PhotoUploadRequest(getApplicationContext(), params[0], imageFile);
                                return request.sendRequest();
                            }

                            @Override
                            protected void onPostExecute(ResponseEntity<Picture> response) {
                                if (response != null && response.getStatusCode() == HttpStatus.OK) {
                                    // TODO: Give Slideshow a single place and make it send a request for the pictures
                                    Picture uploaded = response.getBody();
                                    uploaded.setCachedFile(imageFile);
                                    finalPlace.getPictures().add(uploaded);

                                    // Start the slideshow-activity with the
                                    Intent intent = Slideshow.createIntent(
                                            getApplicationContext(),
                                            finalPlace.getPictures()
                                    );

                                    // Currently crashes since the downloader tries to
                                    // download the newly taken photo for which we don't have a URL
                                    // startActivity(intent);
                                } else {
                                    Toast.makeText(
                                            getApplicationContext(),
                                            "Error uploading photo",
                                            Toast.LENGTH_SHORT
                                    ).show();
                                }
                            }
                        }.execute(place);
                    } else {
                        Toast.makeText(
                                this,
                                "Received no photo",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }
                break;
        }
    }

    public void logOut(View view) {
        Access.revokeAccess(this);
        if (!Access.hasAccess(this)) {
            Toast.makeText(this, getString(R.string.logout_success), Toast.LENGTH_SHORT).show();
            findViewById(R.id.show_login_page_button).setEnabled(true);
        }
    }

    public void showLoginPage(View view) {
        Intent intent = new Intent(this, LoginPage.class);
        startActivity(intent);
    }

    public void toNext(View view){
        //region Save picture
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.rathaus);
        File tempFile = null;
        try {
            tempFile = File.createTempFile("rathaus", "png", getCacheDir());
            FileOutputStream out = new FileOutputStream(tempFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.close();
        } catch (IOException e) {
            Log.e(TAG, "Image file could not be written.", e);
        }

        Picture picture = new Picture();
        picture.setTime(new Date());
        picture.setCachedFile(tempFile);
        //endregion

        Place dummy = new Place();
        dummy.setPicture(picture);
        dummy.setLatitude(52.5170716);
        dummy.setLongitude(13.3888716);
        Intent intent = Maps.createIntent(this, dummy);
        startActivity(intent);
    }

    public void showPictureSelectionPage(View view) {
        Intent intent = new Intent(this, PictureSelectionActivity.class);
        startActivity(intent);
    }

    public void showCamera(View view){
        Intent intent = new Intent(this, CameraActivity.class);
        startActivityForResult(intent, REQUEST_TAKE_PICTURE);
    }

    public void showSlideshow(View view) {
        final AsyncTask<PictureRequest, Void, List<Picture>> task = new AsyncTask<PictureRequest, Void, List<Picture>>() {
            @Override
            protected List<Picture> doInBackground(PictureRequest... params) {
                return params[0].sendRequest().getBody().getPlaces().get(0).getPictures();
            }

            @Override
            protected void onPostExecute(List<Picture> pictures) {
                Intent intent = Slideshow.createIntent(getApplicationContext(), pictures);
                startActivity(intent);
            }
        };

        Place place = new Place();
        place.setId(6);
        task.execute(new PictureRequest(this, place));
    }
}
