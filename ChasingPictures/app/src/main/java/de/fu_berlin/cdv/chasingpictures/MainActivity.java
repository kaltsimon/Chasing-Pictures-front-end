package de.fu_berlin.cdv.chasingpictures;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;


public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.intro);
        mediaPlayer.start(); // no need to call prepare(); create() does that for you
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
