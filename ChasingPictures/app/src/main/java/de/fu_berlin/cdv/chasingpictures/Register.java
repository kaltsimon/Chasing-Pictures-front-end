package de.fu_berlin.cdv.chasingpictures;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import de.fu_berlin.cdv.chasingpictures.api.ApiErrors;
import de.fu_berlin.cdv.chasingpictures.api.LoginApiResult;
import de.fu_berlin.cdv.chasingpictures.api.LoginRequest;
import de.fu_berlin.cdv.chasingpictures.security.Access;


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
        } else if (password.length() < 8) { // TODO: Put MIN_PASS_LENGTH into field
            password.setError("Password is too short");
        }

        // TODO: salt & hash password?!

        LoginRequest registrationRequest = LoginRequest.makeRegistrationRequest(this, usernameString, emailString, passwordString);
        RegistrationRequestTask requestTask = new RegistrationRequestTask();
        //noinspection unchecked
        requestTask.execute(registrationRequest);
    }

    private class RegistrationRequestTask extends AsyncTask<LoginRequest, Void, ResponseEntity<LoginApiResult>> {

        @Override
        protected ResponseEntity<LoginApiResult> doInBackground(LoginRequest... params) {
            return params.length > 0 ? params[0].send() : null;
        }

        @Override
        protected void onPostExecute(ResponseEntity<LoginApiResult> responseEntity) {
            LoginApiResult apiResult = responseEntity.getBody();
            Access.setAccess(getApplicationContext(), responseEntity);

            if (responseEntity.getStatusCode() == HttpStatus.OK
                    && Access.hasAccess(getApplicationContext())) {

                setResult(RESULT_OK);
                finish();
            } else {
                final ApiErrors errors = (ApiErrors) apiResult.getErrors();
                if (!errors.getErrorMessages().isEmpty()) {
                    for (Map.Entry<String, List<String>> entry : errors.getErrorMessages().entrySet()) {
                        String key = entry.getKey();
                        if (key.equals(getString(R.string.api_error_email))) {
                            ((EditText) findViewById(R.id.LoginEmailAddress)).setError(entry.getValue().get(0));
                        } else if (key.equals(getString(R.string.api_error_password))) {
                            ((EditText) findViewById(R.id.LoginPassword)).setError(entry.getValue().get(0));
                        } else if (key.equals(getString(R.string.api_error_username))) {
                            ((EditText) findViewById(R.id.LoginUsername)).setError(entry.getValue().get(0));
                        }
                    }
                }
                Toast.makeText(
                        getApplicationContext(),
                        R.string.registration_fail,
                        Toast.LENGTH_SHORT
                ).show();
            }
        }
    }
}
