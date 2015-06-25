package de.fu_berlin.cdv.chasingpictures;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

    public void showLoginForm(View view) {
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
            setResult(RESULT_OK);
            finish();
        } else {
            Log.d(TAG, "Log in/Register result not OK!");
            // Do nothing
        }
    }
}
