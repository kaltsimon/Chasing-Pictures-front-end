package de.fu_berlin.cdv.chasingpictures;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import de.fu_berlin.cdv.chasingpictures.api.LoginRegistrationRequest;


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
        Toast errorMsg;

        String email = ((EditText) findViewById(R.id.LoginEmailAddress)).getText().toString();
        String confirmEmail = ((EditText) findViewById(R.id.LoginRepeatEmailAddress)).getText().toString();
        String password = ((EditText) findViewById(R.id.LoginPassword)).getText().toString();

        // Check if E-Mail address is valid
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            errorMsg = Toast.makeText(this, R.string.invalid_email, Toast.LENGTH_SHORT);
            errorMsg.show();
            return;
        }

        if (!email.equals(confirmEmail)) {
            errorMsg = Toast.makeText(this, R.string.email_does_not_match, Toast.LENGTH_SHORT);
            errorMsg.show();
            return;
        }

        // Ensure that password is not empty
        if (password.isEmpty()) {
            errorMsg = Toast.makeText(this, R.string.empty_password, Toast.LENGTH_SHORT);
            errorMsg.show();
            return;
        }

        // TODO: salt & hash password?!

        LoginRegistrationRequest registrationRequest = new LoginRegistrationRequest(email, password);
        // TODO: send request
    }
}
