package de.fu_berlin.cdv.chasingpictures.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Arrays;
import java.util.List;

/**
 * @author Simon Kalt
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginApiResult extends ApiResult {
    private UserData data;

    //region Getters & Setters
    public UserData getData() {
        return data;
    }

    public void setData(UserData data) {
        this.data = data;
    }
    //endregion
}
