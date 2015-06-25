package de.fu_berlin.cdv.chasingpictures;

import android.app.Activity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;

import de.fu_berlin.cdv.chasingpictures.api.LoginRequest;
import de.fu_berlin.cdv.chasingpictures.api.LoginRequestTask;
import de.fu_berlin.cdv.chasingpictures.security.Access;


public class Register extends Activity {

    private static final String TAG = "RegisterForm";
    private final int MIN_PASSWORD_LENGTH = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void doRegister(View view) {
        // Retrieve text fields
        EditText email = (EditText) findViewById(R.id.LoginEmailAddress);
        EditText username = (EditText) findViewById(R.id.LoginUsername);
        EditText password = (EditText) findViewById(R.id.LoginPassword);

        // Retrieve contents
        String emailString = email.getText().toString();
        String usernameString = username.getText().toString();
        String passwordString = password.getText().toString();

        // Check if username is valid
        if (usernameString.isEmpty()) {
            username.setError(getString(R.string.register_invalid_username));
            return;
        }

        // Check if E-Mail address is valid
        if (!Patterns.EMAIL_ADDRESS.matcher(emailString).matches()) {
            email.setError(getString(R.string.invalid_email));
            return;
        }

        // Ensure that password is not empty
        if (passwordString.isEmpty()) {
            password.setError(getString(R.string.empty_password));
            return;
        } else if (password.length() < MIN_PASSWORD_LENGTH) {
            password.setError(getString(R.string.register_error_password_too_short));
            return;
        }

        passwordString = Access.saltAndHash(this, passwordString);

        LoginRequest registrationRequest = LoginRequest.makeRegistrationRequest(this, usernameString, emailString, passwordString);
        LoginRequestTask requestTask = LoginRequestTask.makeRegisterTask(this);
        requestTask.execute(registrationRequest);
    }
}
