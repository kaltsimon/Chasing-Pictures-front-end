package de.fu_berlin.cdv.chasingpictures.api;

/**
 * @author Simon Kalt
 */
public class RegistrationApiResult extends ApiResult {
    private RegistrationResultData data;

    public RegistrationResultData getData() {
        return data;
    }

    public void setData(RegistrationResultData data) {
        this.data = data;
    }
}
