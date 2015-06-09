package de.fu_berlin.cdv.chasingpictures.api;

import java.util.List;

/**
 * Class to receive results from searching for places.
 * @author Simon Kalt
 */
public class PlacesApiResult extends ApiResult {
    private List<Place> places;

    public List<Place> getPlaces() {
        return places;
    }

    public void setPlaces(List<Place> places) {
        this.places = places;
    }

    // TODO: check if error handling is correct.
}
