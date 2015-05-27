package de.fu_berlin.cdv.chasingpictures;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import org.springframework.http.HttpMethod;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

import de.fu_berlin.cdv.chasingpictures.api.LoginRequest;
import de.fu_berlin.cdv.chasingpictures.api.LoginResult;


public class LoginForm extends AppCompatActivity {

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you spe/cify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void doLogin(View view) {
        // TODO: log in user AKA send request to backend

        String email = ((EditText) findViewById(R.id.LoginEmailAddress)).getText().toString();
        String password = ((EditText) findViewById(R.id.LoginEmailAddress)).getText().toString();
        // TODO: salt & hash password?!


        LoginRequest loginRequest = new LoginRequest(email, password);
        LoginRequestTask loginRequestTask = new LoginRequestTask();
        loginRequestTask.execute(loginRequest);
    }

    private class LoginRequestTask extends AsyncTask<LoginRequest, Void, LoginResult> {

        @Override
        protected LoginResult doInBackground(LoginRequest... params) {
            if (params.length != 0) {
                try {
                    Resources res = getResources();
                    final String url = res.getString(R.string.api_main) + res.getString(R.string.api_login);
                    RestTemplate restTemplate = new RestTemplate();
                    // Currently results in crash!
                    /*
                    restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                    return restTemplate.postForObject(url, params[0], LoginResult.class);
                    */

                    return new LoginResult(true, "Dummy!");
                } catch (HttpClientErrorException e) {

                    Log.e(TAG, e.getResponseBodyAsString(), e);
                }
                catch (Exception e) {
                    Log.e(TAG, e.getMessage(), e);
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(LoginResult loginResult) {
            if (loginResult.isSuccessful()) {
                Log.d(TAG, "Login successful, returning...");
                // TODO: Show "login successful" message (or do it in the parent view?)
                setResult(RESULT_OK);
                finish();
            } else {
                Log.e(TAG, loginResult.getMessage());
                // TODO: Show message about login failure
                Log.d(TAG, "Login failed...");
            }
        }
    }
}
