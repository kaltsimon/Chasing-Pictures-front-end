package de.fu_berlin.cdv.chasingpictures.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ImageView;

import de.fu_berlin.cdv.chasingpictures.MainActivity;
import de.fu_berlin.cdv.chasingpictures.R;

public class SplashScreen extends Activity {

    private static final long ANIMATION_DURATION = 3202;
    private AnimationDrawable draw;
    private final Thread splashThread = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                synchronized (splashThread) {
                    splashThread.wait(ANIMATION_DURATION);
                }
            } catch (InterruptedException ignored) {}

            finish();

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        ImageView imageView = (ImageView) findViewById(R.id.splashScreenImageView);
        draw = (AnimationDrawable) imageView.getBackground();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            draw.start();
            splashThread.start();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            synchronized(splashThread) {
                splashThread.notifyAll();
            }
            return true;
        }
        return false;
    }
}
