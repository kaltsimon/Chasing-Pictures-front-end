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

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import de.fu_berlin.cdv.chasingpictures.api.ApiUtil;
import de.fu_berlin.cdv.chasingpictures.api.LoginApiResult;
import de.fu_berlin.cdv.chasingpictures.api.LoginRegistrationRequest;
import de.fu_berlin.cdv.chasingpictures.api.UserData;


public class LoginForm extends Activity {

    public static final String TAG = "LoginForm";
    private ApiUtil apiUtil;

    // Returned Data
    public static final String RETURN_USER_DATA = "return_user_data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_form);
        apiUtil = new ApiUtil(this);
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

    private class LoginRequestTask extends AsyncTask<LoginRegistrationRequest, Void, ResponseEntity<LoginApiResult>> {

        @Override
        protected ResponseEntity<LoginApiResult> doInBackground(LoginRegistrationRequest... params) {
            if (params.length != 0) {
                try {
                    RestTemplate restTemplate = ApiUtil.buildJsonRestTemplate();
                    return restTemplate.
                            exchange(apiUtil.getURIforEndpoint(R.string.api_path_login),
                                    HttpMethod.POST,
                                    new HttpEntity<>(params[0], null),
                                    LoginApiResult.class);
                }
                catch (Exception e) {
                    Log.e(TAG, e.getMessage(), e);
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(ResponseEntity<LoginApiResult> responseEntity) {
            String accessToken = apiUtil.getHeader(responseEntity, R.string.api_header_accessToken);

            if (responseEntity.getStatusCode() == HttpStatus.OK
                    && accessToken != null
                    && !accessToken.isEmpty()) {
                // Place access token in user data
                UserData userData = responseEntity.getBody().getData();
                userData.setAccessToken(accessToken);

                Intent resultData = new Intent();
                resultData.putExtra(RETURN_USER_DATA, userData);

                // Return to previous view
                setResult(RESULT_OK, resultData);
                finish();
            } else {
                Log.d(TAG, "Status: " + responseEntity.getStatusCode());
                Log.d(TAG, "Response Body: " + responseEntity.getBody());
                Toast.makeText(getApplicationContext(),
                        R.string.login_fail,
                        Toast.LENGTH_SHORT).show();
            }

        }
    }
}
