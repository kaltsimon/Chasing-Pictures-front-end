package de.fu_berlin.cdv.chasingpictures.api;

/**
 * Created by Simon on 27.05.15.
 */
public class LoginRequest {
    private String email;
    private String password;


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
}
