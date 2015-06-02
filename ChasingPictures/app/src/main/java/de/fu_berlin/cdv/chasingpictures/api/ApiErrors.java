package de.fu_berlin.cdv.chasingpictures.api;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class into which errors from the API get deserialized.
 */
public class ApiErrors {
    @JsonProperty("full_messages")
    private List<String> fullMessages;

    private Map<String, List<String>> errorMessages = new HashMap<>();

    public List<String> getFullMessages() {
        return fullMessages;
    }

    public void setFullMessages(List<String> fullMessages) {
        this.fullMessages = fullMessages;
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
