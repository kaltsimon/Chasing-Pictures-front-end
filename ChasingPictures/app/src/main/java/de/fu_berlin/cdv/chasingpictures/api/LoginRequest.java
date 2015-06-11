package de.fu_berlin.cdv.chasingpictures.api;

import android.content.Context;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import de.fu_berlin.cdv.chasingpictures.R;

/**
 * Login and registration requests.
 * @author Simon Kalt
 */
public class LoginRequest<T> extends ApiRequest<T> {
    protected final LoginRegistrationRequest data;
    protected final Class<T> type;

    protected LoginRequest(Context context, int endpointResID, LoginRegistrationRequest data, Class<T> type) {
        super(context, endpointResID);
        this.data = data;
        this.type = type;
    }

    public static LoginRequest<RegistrationApiResult> makeRegistrationRequest(Context context, String name, String email, String password) {
        LoginRegistrationRequest data = new LoginRegistrationRequest(name, email, password);
        return new LoginRequest<>(context, R.string.api_path_register, data, RegistrationApiResult.class);
    }

    public static LoginRequest<LoginApiResult> makeLoginRequest(Context context, String email, String password) {
        LoginRegistrationRequest data = new LoginRegistrationRequest(email, password);
        return new LoginRequest<>(context, R.string.api_path_login, data, LoginApiResult.class);
    }

    @Override
    public ResponseEntity<T> send() {
        return restTemplate.exchange(
                apiUri,
                HttpMethod.POST,
                new HttpEntity<>(data, null),
                type
        );
    }
}
