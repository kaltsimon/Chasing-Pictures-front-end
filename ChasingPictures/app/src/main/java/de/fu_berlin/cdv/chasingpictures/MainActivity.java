package de.fu_berlin.cdv.chasingpictures;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import de.fu_berlin.cdv.chasingpictures.activity.Slideshow;
import de.fu_berlin.cdv.chasingpictures.api.Place;
import de.fu_berlin.cdv.chasingpictures.security.Access;
import de.fu_berlin.cdv.chasingpictures.util.Utilities;


public class MainActivity extends Activity {
    public static final int REQUEST_LOGIN_REGISTER = 1;
    public static final int REQUEST_TAKE_PICTURE = 2;
    private static final String TAG = "MainActivity";
    private boolean triedLogin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Check if we are logged in, but only once
        if (!triedLogin && !Access.hasAccess(this)) {
            triedLogin = true;
            Intent intent = new Intent(this, LoginPage.class);
            startActivity(intent);
        }
    }

    public void logOut(View view) {
        Access.revokeAccess(this);
        if (!Access.hasAccess(this)) {
            Utilities.showToast(this, R.string.logout_success);
        }
    }

    public void showPictureSelectionPage(View view) {
        Intent intent = new Intent(this, PictureSelectionActivity.class);
        startActivity(intent);
    }

    public void showSlideshow(View view) {
        Place place = new Place();
        place.setId(6);
        Intent intent = Slideshow.createIntent(this, place);
        startActivity(intent);
    }
}
