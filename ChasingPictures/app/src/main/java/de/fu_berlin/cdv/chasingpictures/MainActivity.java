package de.fu_berlin.cdv.chasingpictures;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import de.fu_berlin.cdv.chasingpictures.api.Picture;
import de.fu_berlin.cdv.chasingpictures.api.Place;
import de.fu_berlin.cdv.chasingpictures.camera.CameraActivity;
import de.fu_berlin.cdv.chasingpictures.security.Access;


public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";
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
        if (requestCode == LoginPage.LOGIN
                && resultCode == RESULT_OK
                && Access.hasAccess(this)) {
            Toast.makeText(this, R.string.login_success, Toast.LENGTH_SHORT).show();
            findViewById(R.id.show_login_page_button).setEnabled(false);
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
        startActivity(intent);
    }

}
