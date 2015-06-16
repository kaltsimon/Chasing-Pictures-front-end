package de.fu_berlin.cdv.chasingpictures.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import de.fu_berlin.cdv.chasingpictures.MainActivity;
import de.fu_berlin.cdv.chasingpictures.R;

public class SplashScreen extends Activity {

    private static final long SPLASH_DELAY_MILLIS = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Start the main activity after a short delay.
        new Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }
                },
                SPLASH_DELAY_MILLIS
        );
    }
}
