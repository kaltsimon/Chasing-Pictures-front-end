package de.fu_berlin.cdv.chasingpictures.api;

import android.content.Context;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import de.fu_berlin.cdv.chasingpictures.R;

/**
 * Login and registration requests.
 *
 * @author Simon Kalt
 */
public class LoginRequest extends ApiRequest<LoginApiResult> {
    protected final LoginRequestData data;

    protected LoginRequest(Context context, int endpointResID, LoginRequestData data) {
        super(context, endpointResID);
        this.data = data;
    }

    public static LoginRequest makeRegistrationRequest(Context context, String name, String email, String password) {
        LoginRequestData data = new LoginRequestData(name, email, password);
        return new LoginRequest(context, R.string.api_path_register, data);
    }

    public static LoginRequest makeLoginRequest(Context context, String email, String password) {
        LoginRequestData data = new LoginRequestData(email, password);
        return new LoginRequest(context, R.string.api_path_login, data);
    }

    @Override
    public void beforeSending() {
        // This prevents currently stored access information to be sent.
    }

    @Override
    public ResponseEntity<LoginApiResult> send() {
        return restTemplate.exchange(
                apiUri,
                HttpMethod.POST,
                new HttpEntity<>(data),
                LoginApiResult.class
        );
    }
}
