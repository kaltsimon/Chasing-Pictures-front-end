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
        RegistrationRequestTask requestTask = new RegistrationRequestTask();
        requestTask.execute(registrationRequest);
    }

    private class RegistrationRequestTask extends AsyncTask<LoginRegistrationRequest, Void, LoginResult> {

        @Override
        protected LoginResult doInBackground(LoginRegistrationRequest... params) {
            if (params.length != 0) {
                try {
                    Resources res = getResources();
                    final String url = res.getString(R.string.api_main) + res.getString(R.string.api_register);
                    RestTemplate restTemplate = buildJSONRestTemplate();
                    return restTemplate.postForObject(url, params[0], LoginResult.class);
                }
                catch (Exception e) {
                    Log.e(TAG, e.getMessage(), e);
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(LoginResult registrationResult) {
            // DUMMY
            registrationResult.setSuccessful(true);

            Toast notification;

            if (registrationResult.isSuccessful()) {
                notification = Toast.makeText(getApplicationContext(), R.string.registration_success, Toast.LENGTH_SHORT);
                notification.show();

                // Return to previous view
                setResult(RESULT_OK);
                finish();
            } else {
                notification = Toast.makeText(getApplicationContext(), R.string.registration_fail, Toast.LENGTH_SHORT);
                notification.show();
            }
        }
    }

    /**
     * Builds a basic JSON rest template for sending requests.
     * @return A RestTemplate with a {@see org.springframework.http.converter.json.Jackson2HttpMessageConverter} attached.
     */
    private static RestTemplate buildJSONRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        return restTemplate;
    }
}
