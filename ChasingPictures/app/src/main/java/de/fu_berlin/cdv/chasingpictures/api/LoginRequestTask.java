package de.fu_berlin.cdv.chasingpictures.api;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.annotation.StringRes;
import android.util.Log;
import android.widget.EditText;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import de.fu_berlin.cdv.chasingpictures.R;
import de.fu_berlin.cdv.chasingpictures.security.Access;
import de.fu_berlin.cdv.chasingpictures.util.Utilities;

/**
 * Task to send a login/registration request to the back end.
 *
 * @author Simon Kalt
 */
public class LoginRequestTask extends AsyncTask<LoginRequest, Void, ResponseEntity<LoginApiResult>> {

    private static final String TAG = "LoginRequestTask";
    private final Activity activity;
    private final Mode mode;

    public LoginRequestTask(Activity activity, Mode mode) {
        this.activity = activity;
        this.mode = mode;
    }

    public static LoginRequestTask makeLoginTask(Activity activity) {
        return new LoginRequestTask(activity, Mode.LOGIN);
    }

    public static LoginRequestTask makeRegisterTask(Activity activity) {
        return new LoginRequestTask(activity, Mode.REGISTER);
    }

    @Override
    protected ResponseEntity<LoginApiResult> doInBackground(LoginRequest... params) {
        return params.length > 0 && params[0] != null ? params[0].sendRequest() : null;
    }

    @Override
    protected void onPostExecute(ResponseEntity<LoginApiResult> responseEntity) {
        if (responseEntity != null
                && responseEntity.getStatusCode() == HttpStatus.OK
                && Access.hasAccess(activity.getApplicationContext())) {

            // TODO: save user data in storage, e.g. SQLite DB
            UserData userData = responseEntity.getBody().getData();

            activity.setResult(Activity.RESULT_OK);
            activity.finish();
        } else {
            // Handle errors
            @StringRes int errorResID = R.string.login_fail;

            // We currently have two different error formats for login and registration
            if (responseEntity != null) {
                Log.e(TAG, "Status: " + responseEntity.getStatusCode());
                Log.e(TAG, "Response Body: " + responseEntity.getBody());
            }

            switch (mode) {
                case REGISTER:
                    errorResID = R.string.registration_fail;
                    if (responseEntity != null) {
                        ApiErrors errors = (ApiErrors) responseEntity.getBody().getErrors();

                        // Check the error messages and display them in the corresponding fields
                        String emailErrorKey = activity.getString(R.string.api_error_email);
                        List<String> emailErrors = errors.getErrorMessages().get(emailErrorKey);

                        if (emailErrors != null && !emailErrors.isEmpty()) {
                            EditText emailTextField = (EditText) activity.findViewById(R.id.LoginEmailAddress);
                            emailTextField.setError(emailErrors.get(0));
                        }
                    }
                    break;
                case LOGIN:
                    errorResID = R.string.login_fail;
                    break;
            }

            Utilities.showError(activity, errorResID);
        }
    }

    public enum Mode {
        REGISTER, LOGIN
    }
}
