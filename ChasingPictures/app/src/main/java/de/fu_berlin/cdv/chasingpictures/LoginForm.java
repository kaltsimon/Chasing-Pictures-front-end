package de.fu_berlin.cdv.chasingpictures;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import de.fu_berlin.cdv.chasingpictures.api.LoginApiResult;
import de.fu_berlin.cdv.chasingpictures.api.LoginRequest;
import de.fu_berlin.cdv.chasingpictures.api.UserData;
import de.fu_berlin.cdv.chasingpictures.security.Access;


public class LoginForm extends Activity {

    public static final String TAG = "LoginForm";

    // Returned Data
    public static final String RETURN_USER_DATA = "return_user_data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_form);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login_form, menu);
        return true;
    }

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

        // TODO: salt & hash password?!

        LoginRequest loginRequest = LoginRequest.makeLoginRequest(this, emailString, passwordString);
        LoginRequestTask loginRequestTask = new LoginRequestTask();
        //noinspection unchecked
        loginRequestTask.execute(loginRequest);
    }

    private class LoginRequestTask extends AsyncTask<LoginRequest, Void, ResponseEntity<LoginApiResult>> {

        @Override
        protected ResponseEntity<LoginApiResult> doInBackground(LoginRequest... params) {
            return params.length > 0 ? params[0].send() : null;
        }

        @Override
        protected void onPostExecute(ResponseEntity<LoginApiResult> responseEntity) {
            Access.setAccess(getApplicationContext(), responseEntity);

            if (responseEntity != null
                    && responseEntity.getStatusCode() == HttpStatus.OK
                    && Access.hasAccess(getApplicationContext())) {
                UserData userData = responseEntity.getBody().getData();

                // TODO: save user data in storage, e.g. SQLite DB
                Intent resultData = new Intent();
                resultData.putExtra(RETURN_USER_DATA, userData);

                // Return to previous view
                setResult(RESULT_OK, resultData);
                finish();
            } else {
                if (responseEntity != null) {
                    Log.d(TAG, "Status: " + responseEntity.getStatusCode());
                    Log.d(TAG, "Response Body: " + responseEntity.getBody());
                }
                Toast.makeText(
                        getApplicationContext(),
                        R.string.login_fail,
                        Toast.LENGTH_SHORT
                ).show();
            }

        }
    }
}
