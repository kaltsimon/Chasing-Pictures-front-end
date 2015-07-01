package de.fu_berlin.cdv.chasingpictures.api

import android.location.Location
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.io.Serializable

/**
 * This class represents a place, a.k.a. the thing users have to search for.
 *
 * @author Simon Kalt
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public data class Place(
        var id: Int = 0,
        var name: String? = null,
        var latitude: Double = 0.0,
        var longitude: Double = 0.0,
        var description: String? = null,
        var pictures: List<Picture>? = null,
        var bearing: Double = 0.0,
        var distance: Double = 0.0
) : Serializable {

    /**
     * Get the first picture.
     *
     * @return The first picture associated with this place.
     */
    @JsonIgnore
    fun getFirstPicture(): Picture? = if (pictures?.isEmpty() ?: true) null else pictures?.get(0)

    /**
     * Returns a {@link Location} describing latitude and longitude of this place.
     */
    @JsonIgnore
    fun getLocation(): Location {
        val location = Location("");
        location.setLatitude(latitude)
        location.setLongitude(longitude)

        return location
    }

    /**
     * Calculates the distance of this place to another location.
     *
     * @param location (Own) location
     * @return Distance to the given location (in meters)
     */
    fun distanceTo(location: Location): Float = getLocation().distanceTo(location)

    /**
     * Sets the list of pictures associated with this place
     * to the single picture provided.
     *
     * @param picture The picture to set
     * @deprecated Use {@link #setPictures(List)} instead
     * *
     */
    JsonIgnore
    deprecated("Use {@link #setPictures(List)} instead", ReplaceWith("setPictures(listOf(picture))"))
    public fun setPicture(picture: Picture) {
        this.pictures = listOf(picture)
    }

    /**
     * Get the first picture.
     *
     * @return The first picture associated with this place.
     * @deprecated Use {@link #getFirstPicture()} instead
     */
    @JsonIgnore
    deprecated("Use {@link #getFirstPicture()} instead", replaceWith = ReplaceWith("getFirstPicture()"))
    fun getPicture(): Picture? {
        return getFirstPicture();
    }
}