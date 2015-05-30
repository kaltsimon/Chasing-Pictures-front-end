package de.fu_berlin.cdv.chasingpictures;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;


public class Register extends Activity {

    private static final String TAG = "RegisterForm";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
        return true;
    }

    public void doRegister(View view) {
        // TODO: register user AKA send request to backend
        boolean registerResult = true;

        if (registerResult) {
            Log.d(TAG, "Registration successful, returning...");
            setResult(RESULT_OK);
            finish();
        } else {
            // TODO: Show message about login failure
            Log.d(TAG, "Registration failed...");
        }
    }
}
