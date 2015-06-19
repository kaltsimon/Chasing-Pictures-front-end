package de.fu_berlin.cdv.chasingpictures.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Class to receive results from searching for places.
 * @author Simon Kalt
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlacesApiResult extends ApiResult<List<Place>> {
    public List<Place> getPlaces() {
        return getData();
    }

    public void setPlaces(List<Place> places) {
        setData(places);
    }
}
