package de.fu_berlin.cdv.chasingpictures.api;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class into which errors from the API get deserialized.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiErrors {
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
            System.out.printf("Unrecognized error for (%s):%n%s", key, value);
        }
    }
}
