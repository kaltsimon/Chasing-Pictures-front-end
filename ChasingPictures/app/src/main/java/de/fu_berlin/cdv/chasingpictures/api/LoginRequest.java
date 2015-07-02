package de.fu_berlin.cdv.chasingpictures.api;

import android.content.Context;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import de.fu_berlin.cdv.chasingpictures.R;
import de.fu_berlin.cdv.chasingpictures.security.Access;

/**
 * Login and registration requests.
 * @author Simon Kalt
 */
public class LoginRequest extends ApiRequest<LoginApiResult> {
    protected final Data data;

    protected LoginRequest(Context context, int endpointResID, Data data) {
        super(context, endpointResID);
        this.data = data;
    }

    public static LoginRequest makeRegistrationRequest(Context context, String name, String email, String password) {
        Data data = new Data(name, email, password);
        return new LoginRequest(context, R.string.api_path_register, data);
    }

    public static LoginRequest makeLoginRequest(Context context, String email, String password) {
        Data data = new Data(email, password);
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

    /**
     * Data class for login and registration requests.
     * Used for automatic serialization to JSON.
     * @author Simon Kalt
     */
    public static class Data {
        private String name;
        private String email;
        private String password;

        //region Constructors
        /**
         * This constructor is used for automatic construction from JSON.
         */
        public Data() {}

        public Data(String name, String email, String password) {
            this.name = name;
            this.email = email;
            this.password = password;
        }

        public Data(String email, String password) {
            this.email = email;
            this.password = password;
        }
        //endregion

        //region Getters & Setters
        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
        //endregion

        @Override
        public String toString() {
            return "Data{" +
                    "name='" + name + '\'' +
                    ", email='" + email + '\'' +
                    ", password='" + password + '\'' +
                    '}';
        }
    }
}
