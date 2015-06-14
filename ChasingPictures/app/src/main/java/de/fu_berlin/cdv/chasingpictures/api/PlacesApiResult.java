package de.fu_berlin.cdv.chasingpictures.api;

import java.util.List;

/**
 * Class to receive results from searching for places.
 * @author Simon Kalt
 */
public class PlacesApiResult extends ApiResult<List<Place>> {
    public List<Place> getPlaces() {
        return getData();
    }

    public void setPlaces(List<Place> places) {
        setData(places);
    }
}
