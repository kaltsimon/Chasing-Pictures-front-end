package de.fu_berlin.cdv.chasingpictures;

import android.app.Activity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;

import de.fu_berlin.cdv.chasingpictures.api.LoginRequest;
import de.fu_berlin.cdv.chasingpictures.api.LoginRequestTask;
import de.fu_berlin.cdv.chasingpictures.security.Access;


public class LoginForm extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_form);
    }

    /**
     * Perform the actual log in, if the input is acceptable.
     */
    public void doLogin(View view) {
        // Retrieve text fields
        EditText email = (EditText) findViewById(R.id.LoginEmailAddress);
        EditText password = (EditText) findViewById(R.id.LoginPassword);

        // Retrieve contents
        String emailString = email.getText().toString();
        String passwordString = password.getText().toString();

        // Check if E-Mail address is valid
        if (!Patterns.EMAIL_ADDRESS.matcher(emailString).matches()) {
            email.setError(getString(R.string.invalid_email));
            return;
        }

        // Check if password is not empty
        if (passwordString.isEmpty()) {
            password.setError(getString(R.string.empty_password));
            return;
        }

        passwordString = Access.saltAndHash(this, passwordString);

        LoginRequest loginRequest = LoginRequest.makeLoginRequest(this, emailString, passwordString);
        LoginRequestTask loginRequestTask = LoginRequestTask.makeLoginTask(this);
        loginRequestTask.execute(loginRequest);
    }
}
