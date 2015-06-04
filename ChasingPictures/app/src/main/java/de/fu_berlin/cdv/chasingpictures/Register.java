package de.fu_berlin.cdv.chasingpictures;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import de.fu_berlin.cdv.chasingpictures.api.ApiErrors;
import de.fu_berlin.cdv.chasingpictures.api.LoginRegistrationRequest;
import de.fu_berlin.cdv.chasingpictures.api.RegistrationApiResult;
import de.fu_berlin.cdv.chasingpictures.api.ApiUtil;
import de.fu_berlin.cdv.chasingpictures.security.KeyStore;
import de.fu_berlin.cdv.chasingpictures.security.SecurePreferences;


public class Register extends Activity {

    private static final String TAG = "RegisterForm";
    private ApiUtil apiUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        apiUtil = new ApiUtil(this);
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
        }

        // TODO: salt & hash password?!

        LoginRegistrationRequest registrationRequest = new LoginRegistrationRequest(usernameString, emailString, passwordString);
        RegistrationRequestTask requestTask = new RegistrationRequestTask();
        requestTask.execute(registrationRequest);
    }

    private class RegistrationRequestTask extends AsyncTask<LoginRegistrationRequest, Void, ResponseEntity<RegistrationApiResult>> {

        @Override
        protected ResponseEntity<RegistrationApiResult> doInBackground(LoginRegistrationRequest... params) {
            if (params.length != 0) {
                try {
                    RestTemplate restTemplate = ApiUtil.buildJsonRestTemplate();
                    return restTemplate.
                            exchange(apiUtil.getURIforEndpoint(R.string.api_path_register),
                                    HttpMethod.POST, new HttpEntity<>(params[0], null),
                                    RegistrationApiResult.class);
                }
                catch (Exception e) {
                    Log.e(TAG, e.getMessage(), e);
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(ResponseEntity<RegistrationApiResult> responseEntity) {
            RegistrationApiResult apiResult = responseEntity.getBody();
            String accessToken = apiUtil.getHeader(responseEntity, R.string.api_header_accessToken);

            if (responseEntity.getStatusCode() == HttpStatus.OK
                    && apiUtil.callSuccessful(apiResult)
                    && accessToken != null
                    && !accessToken.isEmpty()) {

                SecurePreferences.getInstance(getApplicationContext(), R.string.security_preferences_ID);

                setResult(RESULT_OK);
                finish();
            } else {
                final ApiErrors errors = apiResult.getErrors();
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
                Toast notification = Toast.makeText(getApplicationContext(), R.string.registration_fail, Toast.LENGTH_SHORT);
                notification.show();
            }
        }
    }
}
