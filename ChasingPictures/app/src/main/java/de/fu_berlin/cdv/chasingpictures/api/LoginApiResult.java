package de.fu_berlin.cdv.chasingpictures.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Arrays;
import java.util.List;

/**
 * @author Simon Kalt
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginApiResult {
    private List<String> errors;
    private UserData data;

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public UserData getData() {
        return data;
    }

    public void setData(UserData data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "LoginApiResult{" +
                "errors=" + Arrays.toString(errors.toArray()) +
                '}';
    }
}
