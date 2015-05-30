package de.fu_berlin.cdv.chasingpictures;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;


public class LoginPage extends Activity {

    public static final int LOGIN = 1;
    public static final int REGISTER = 2;
    public static final String TAG = "LoginPage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login_page, menu);
        return true;
    }

    public void showLoginForm(View view) {
        Log.d(TAG, "Showing log in form...");
        Intent intent = new Intent(this, LoginForm.class);
        startActivityForResult(intent, LOGIN);
    }

    public void showRegisterForm(View view) {
        Intent intent = new Intent(this, Register.class);
        startActivityForResult(intent, REGISTER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            // TODO: Depending on wether the user logged in or registered, do something with that information...
            switch (requestCode) {
                case LOGIN:
                    Log.d(TAG, "Logged in successfully!");
                    break;
                case REGISTER:
                    Log.d(TAG, "Registered successfully!");
                    break;
            }

            // TODO: Continue to logged-in status
        } else {
            Log.d(TAG, "Log in/Register result not OK!");
            // Do nothing
        }
    }
}
