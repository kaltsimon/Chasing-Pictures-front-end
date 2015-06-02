package de.fu_berlin.cdv.chasingpictures;

import android.app.Activity;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import de.fu_berlin.cdv.chasingpictures.api.LoginRegistrationRequest;
import de.fu_berlin.cdv.chasingpictures.api.LoginResult;


public class LoginForm extends Activity {

    public static final String TAG = "LoginForm";

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


        LoginRegistrationRequest loginRequest = new LoginRegistrationRequest(emailString, passwordString);
        LoginRequestTask loginRequestTask = new LoginRequestTask();
        loginRequestTask.execute(loginRequest);
    }

    private class LoginRequestTask extends AsyncTask<LoginRegistrationRequest, Void, LoginResult> {

        @Override
        protected LoginResult doInBackground(LoginRegistrationRequest... params) {
            if (params.length != 0) {
                try {
                    final String url = getString(R.string.api_url) + getString(R.string.api_path_login);
                    RestTemplate restTemplate = new RestTemplate();

                    restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                    return restTemplate.postForObject(url, params[0], LoginResult.class);
                }
                catch (Exception e) {
                    Log.e(TAG, e.getMessage(), e);
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(LoginResult loginResult) {
            Log.d(TAG, "Received result from API:");
            Log.d(TAG, loginResult.toString());

            // DUMMY
            loginResult.setSuccessful(true);

            Toast notification;

            if (loginResult.isSuccessful()) {
                notification = Toast.makeText(getApplicationContext(), R.string.login_success, Toast.LENGTH_SHORT);
                notification.show();

                // Return to previous view
                setResult(RESULT_OK);
                finish();
            } else {
                notification = Toast.makeText(getApplicationContext(), R.string.login_fail, Toast.LENGTH_SHORT);
                notification.show();
            }

        }
    }
}
