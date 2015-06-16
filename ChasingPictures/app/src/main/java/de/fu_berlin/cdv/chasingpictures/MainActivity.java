package de.fu_berlin.cdv.chasingpictures;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

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
        Intent intent = new Intent(this, Maps.class);
        startActivity(intent);

    }

    public void showPictureSelectionPage(View view) {
        Intent intent = new Intent(this, PictureSelectionActivity.class);
        startActivity(intent);
    }

    public void showCamera(View view){
        Intent intent = new Intent(this, Camera.class);
        startActivity(intent);
    }

}
