package de.fu_berlin.cdv.chasingpictures.api;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * @author Simon Kalt
 */
public abstract class ApiResult<Data> {
    private String status;
    private Object errors;
    protected Data data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getErrors() {
        return errors;
    }

    @JsonDeserialize(using = ApiErrorDeserializer.class)
    public void setErrors(Object errors) {
        this.errors = errors;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}
