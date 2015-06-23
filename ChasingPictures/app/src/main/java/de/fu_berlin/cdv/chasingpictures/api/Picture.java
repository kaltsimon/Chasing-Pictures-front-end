package de.fu_berlin.cdv.chasingpictures.api;

import android.util.Log;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.File;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Date;

/**
 * This class represents a picture which is associated with a place.
 * @author Simon Kalt
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Picture implements Serializable {
    private long id;
    private Date time;
    private String url; // TODO: Deserialize as URL?
    @JsonProperty("place_id")
    private long placeId;
    @JsonProperty("created_at")
    private Date createdAt;
    @JsonProperty("updated_at")
    private Date updatedAt;
    @JsonIgnore
    private File cachedFile;

    // TODO: Split up these properties into a separate object
    private String file_file_name;
    private String file_content_type;
    private Integer file_file_size;
    private Date file_updated_at;
    private Integer user_id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getUrl() {
        return url;
    }

    public URL getASCIIUrl() {

        try {
            URI uri = new URL(url).toURI();
            return new URL(uri.toASCIIString());
        } catch (MalformedURLException | URISyntaxException e) {
            Log.e("Picture", Log.getStackTraceString(e));
        }

        return null;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getPlaceId() {
        return placeId;
    }

    public void setPlaceId(long placeId) {
        this.placeId = placeId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public File getCachedFile() {
        return cachedFile;
    }

    public void setCachedFile(File cachedFile) {
        this.cachedFile = cachedFile;
    }

    public String getFile_file_name() {
        return file_file_name;
    }

    public void setFile_file_name(String file_file_name) {
        this.file_file_name = file_file_name;
    }

    public String getFile_content_type() {
        return file_content_type;
    }

    public void setFile_content_type(String file_content_type) {
        this.file_content_type = file_content_type;
    }

    public Integer getFile_file_size() {
        return file_file_size;
    }

    public void setFile_file_size(Integer file_file_size) {
        this.file_file_size = file_file_size;
    }

    public Date getFile_updated_at() {
        return file_updated_at;
    }

    public void setFile_updated_at(Date file_updated_at) {
        this.file_updated_at = file_updated_at;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Picture picture = (Picture) o;

        if (id != picture.id) return false;
        if (placeId != picture.placeId) return false;
        if (time != null ? !time.equals(picture.time) : picture.time != null) return false;
        if (url != null ? !url.equals(picture.url) : picture.url != null) return false;
        if (createdAt != null ? !createdAt.equals(picture.createdAt) : picture.createdAt != null)
            return false;
        return !(updatedAt != null ? !updatedAt.equals(picture.updatedAt) : picture.updatedAt != null);

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (time != null ? time.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (int) (placeId ^ (placeId >>> 32));
        result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
        result = 31 * result + (updatedAt != null ? updatedAt.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Picture{" +
                "id=" + id +
                ", time=" + time +
                ", url='" + url + '\'' +
                ", placeId=" + placeId +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
