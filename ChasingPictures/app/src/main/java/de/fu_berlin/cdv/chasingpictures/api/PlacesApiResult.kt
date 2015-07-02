package de.fu_berlin.cdv.chasingpictures.api

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * Class to receive results from searching for places.
 *
 * @author Simon Kalt
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlacesApiResult : ApiResult<List<Place>>() {
    public fun getPlaces(): List<Place> {
        return data
    }

    public fun setPlaces(places: List<Place>) {
        data = places
    }
}
