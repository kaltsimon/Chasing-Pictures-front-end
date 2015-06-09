package de.fu_berlin.cdv.chasingpictures.api;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author Simon Kalt
 */
public abstract class ApiResult {
    private String status;
    private ApiErrors errors;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ApiErrors getErrors() {
        return errors;
    }

    public void setErrors(ApiErrors errors) {
        this.errors = errors;
    }

}
