package de.fu_berlin.cdv.chasingpictures.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * @author Simon Kalt
 */
@JsonIgnoreProperties(value = {"data"}, ignoreUnknown = true)
public class PictureApiResult extends ApiResult<List<Picture>> {
    public List<Picture> getPictures() {
        return getData();
    }

    public void setPictures(List<Picture> pictures) {
        setData(pictures);
    }
}
