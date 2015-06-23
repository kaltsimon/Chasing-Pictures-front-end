package de.fu_berlin.cdv.chasingpictures.api;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import android.location.Location;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * This class represents a place, a.k.a. the thing users have to search for.
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

    @JsonIgnore
    public Picture getPicture() {
        return pictures.get(0);
    }

    @JsonIgnore
    public void setPicture(Picture picture) {
        this.pictures = Collections.singletonList(picture);
    }

    public Location getLocation() {
        Location location = new Location("");
        location.setLatitude(latitude);
        location.setLongitude(longitude);

        return location;
    }

    public float distanceTo(Location location) {
       return getLocation().distanceTo(location);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Place place = (Place) o;

        if (Double.compare(place.latitude, latitude) != 0) return false;
        if (Double.compare(place.longitude, longitude) != 0) return false;
        if (name != null ? !name.equals(place.name) : place.name != null) return false;
        if (description != null ? !description.equals(place.description) : place.description != null)
            return false;
        return !(pictures != null ? !pictures.equals(place.pictures) : place.pictures != null);

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = name != null ? name.hashCode() : 0;
        temp = Double.doubleToLongBits(latitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(longitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (pictures != null ? pictures.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Place{" +
                "name='" + name + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", description='" + description + '\'' +
                ", pictures=" + pictures +
                '}';
    }
}
