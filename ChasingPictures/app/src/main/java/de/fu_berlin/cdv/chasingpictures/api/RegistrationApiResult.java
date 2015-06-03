package de.fu_berlin.cdv.chasingpictures.api;

/**
 * @author Simon Kalt
 */
public class RegistrationApiResult extends ApiResult {
    private UserData data;

    public UserData getData() {
        return data;
    }

    public void setData(UserData data) {
        this.data = data;
    }
}
