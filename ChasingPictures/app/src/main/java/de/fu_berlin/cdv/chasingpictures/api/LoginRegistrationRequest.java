package de.fu_berlin.cdv.chasingpictures.api;

/**
 * Data class for login and registration requests.
 * Used for automatic serialization to JSON.
 * @author Simon Kalt
 */
public class LoginRegistrationRequest {
    private String name;
    private String email;
    private String password;

    /**
     * This constructor is used for automatic construction from JSON.
     */
    public LoginRegistrationRequest() {}

    public LoginRegistrationRequest(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public LoginRegistrationRequest(String email, String password) {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "LoginRegistrationRequest{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
