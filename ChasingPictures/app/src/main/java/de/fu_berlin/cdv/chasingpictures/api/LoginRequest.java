package de.fu_berlin.cdv.chasingpictures.api;

/**
 * Created by Simon on 27.05.15.
 */
public class LoginRequest {
    private String email;
    private String password;

    /**
     * This constructor is used for autmatic construction
     * from JSON.
     */
    public LoginRequest() {

    }

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

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

    @Override
    public String toString() {
        return "LoginRequest{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
