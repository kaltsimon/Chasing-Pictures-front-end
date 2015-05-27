package de.fu_berlin.cdv.chasingpictures.api;

/**
 * Created by Simon on 27.05.15.
 */
public class LoginResult {
    private boolean successful;
    private String message;

    public LoginResult(boolean successful, String message) {
        this.successful = successful;
        this.message = message;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "LoginResult{" +
                "successful=" + successful +
                ", message='" + message + '\'' +
                '}';
    }
}
