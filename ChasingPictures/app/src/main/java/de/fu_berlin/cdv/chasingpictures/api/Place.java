package de.fu_berlin.cdv.chasingpictures.api;

import android.location.Location;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * This class represents a place, a.k.a. the thing users have to search for.
 *
 * @author Simon Kalt
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Place implements Serializable {
    private int id;
    private String name;
    private double latitude;
    private double longitude;
    private String description;
    private List<Picture> pictures;
    private Double bearing;
    private Double distance;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Picture> getPictures() {
        return pictures;
    }

    public void setPictures(List<Picture> pictures) {
        this.pictures = pictures;
    }

    /**
     * If this place was requested from the API with a given location,
     * returns the bearing from that location to this place.
     */
    public Double getBearing() {
        return bearing;
    }

    /**
     * Set the bearing to this place. Value will be
     * automatically wrapped into the range [0, 360).
     */
    public void setBearing(double bearing) {
        while (bearing < 0.0f) {
            bearing += 360.0f;
        }
        while (bearing >= 360.0f) {
            bearing -= 360.0f;
        }
        this.bearing = bearing;
    }

    /**
     * If this place was requested from the API with a given location,
     * returns the distance from that location to this place.
     * <p/>
     * Since this is the distance to the location at request time,
     * consider using {@link #distanceTo(Location)}.
     */
    public Double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    /**
     * Get the first picture.
     *
     * @return The first picture associated with this place.
     * @deprecated Use {@link #getFirstPicture()} instead
     */
    @JsonIgnore
    @Deprecated
    public Picture getPicture() {
        return getFirstPicture();
    }

    /**
     * Get the first picture.
     *
     * @return The first picture associated with this place.
     */
    public Picture getFirstPicture() {
        return pictures == null || pictures.isEmpty() ? null : pictures.get(0);
    }

    /**
     * Sets the list of pictures associated with this place
     * to the single picture provided.
     *
     * @param picture The picture to set
     * @deprecated Use {@link #setPictures(List)} instead
     */
    @JsonIgnore
    @Deprecated
    public void setPicture(Picture picture) {
        this.pictures = Collections.singletonList(picture);
    }

    /**
     * Returns a {@link Location} describing latitude and longitude of this place.
     *
     * @return A {@link Location} object describing this place
     */
    public Location getLocation() {
        Location location = new Location("");
        location.setLatitude(latitude);
        location.setLongitude(longitude);

        return location;
    }

    /**
     * Calculates the distance of this place to another location.
     *
     * @param location (Own) location
     * @return Distance to the given location (in meters)
     */
    public float distanceTo(Location location) {
        return getLocation().distanceTo(location);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Place place = (Place) o;

        if (id != place.id) return false;
        if (Double.compare(place.latitude, latitude) != 0) return false;
        if (Double.compare(place.longitude, longitude) != 0) return false;
        if (name != null ? !name.equals(place.name) : place.name != null) return false;
        if (description != null ? !description.equals(place.description) : place.description != null)
            return false;
        if (pictures != null ? !pictures.equals(place.pictures) : place.pictures != null)
            return false;
        if (bearing != null ? !bearing.equals(place.bearing) : place.bearing != null) return false;
        return !(distance != null ? !distance.equals(place.distance) : place.distance != null);

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        temp = Double.doubleToLongBits(latitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(longitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (pictures != null ? pictures.hashCode() : 0);
        result = 31 * result + (bearing != null ? bearing.hashCode() : 0);
        result = 31 * result + (distance != null ? distance.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Place{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", description='" + description + '\'' +
                ", pictures=" + pictures +
                ", bearing=" + bearing +
                ", distance=" + distance +
                '}';
    }
}
