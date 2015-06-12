package de.fu_berlin.cdv.chasingpictures.api;

import android.util.Log;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class into which errors from the API get deserialized.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiErrors {
    private static final String TAG = "ApiErrors";
    private List<String> errors;
    private Map<String, List<String>> errorMessages = new HashMap<>();

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    @JsonIgnore
    public Map<String, List<String>> getErrorMessages() {
        return errorMessages;
    }

    @JsonAnySetter
    public void addErrorMessage(String key, Object value) {
        try {
            List<String> errors = (List<String>) value;
            errorMessages.put(key, errors);
        } catch (Exception ex) {
            Log.d(TAG, String.format("Unrecognized error for (%s):%n%s", key, value), ex);
        }
    }
}
