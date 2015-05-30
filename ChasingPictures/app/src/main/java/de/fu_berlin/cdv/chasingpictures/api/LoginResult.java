package de.fu_berlin.cdv.chasingpictures.api;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by Simon on 27.05.15.
 */
public class LoginResult {
    private String name;
    private String email;
    private int id;

    // ignore, since we don't receive this info
    @JsonIgnore
    private boolean successful;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    @Override
    public String toString() {
        return "LoginResult{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", id=" + id +
                ", successful=" + successful +
                '}';
    }
}
