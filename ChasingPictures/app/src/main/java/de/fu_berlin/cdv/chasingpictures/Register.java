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

    /*
    private static <T> T buildAndPostRestTemplate(int[] urlComponentIds, Resources resources, Class<T> resultClass) {
        StringBuilder url_sb = new StringBuilder(100);
        for (int componentId : urlComponentIds) {
            url_sb.append(resources.getString(componentId));
        }

        RestTemplate restTemplate = buildJSONRestTemplate();



        return restTemplate;
    }
    */
}
