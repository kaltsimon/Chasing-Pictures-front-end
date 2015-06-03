package de.fu_berlin.cdv.chasingpictures.api;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * @author Simon Kalt
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserData implements Parcelable {
    private Integer id;
    private String provider;
    private String uid;
    private String name;
    private String nickname;
    private String image;
    private String email;
    @JsonProperty("created_at")
    private Date createdAt;
    @JsonProperty("updated_at")
    private Date updatedAt;
    @JsonIgnore
    private String accessToken;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public UserData() { }

    public UserData(Parcel in) {
        readFromParcel(in);
    }

    private void readFromParcel(Parcel in) {
        setId(in.readInt());
        setProvider(in.readString());
        setUid(in.readString());
        setName(in.readString());
        setNickname(in.readString());
        setImage(in.readString());
        setEmail(in.readString());
        setCreatedAt(new Date(in.readLong()));
        setUpdatedAt(new Date(in.readLong()));
        setAccessToken(in.readString());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(provider);
        dest.writeString(uid);
        dest.writeString(name);
        dest.writeString(nickname);
        dest.writeString(image);
        dest.writeString(email);
        dest.writeLong(createdAt == null ? 0 : createdAt.getTime());
        dest.writeLong(updatedAt == null ? 0 : updatedAt.getTime());
        dest.writeString(accessToken);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator CREATOR =
            new Parcelable.Creator() {
                public UserData createFromParcel(Parcel in) {
                    return new UserData(in);
                }

                public UserData[] newArray(int size) {
                    return new UserData[size];
                }
            };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserData userData = (UserData) o;

        if (id != null ? !id.equals(userData.id) : userData.id != null) return false;
        if (provider != null ? !provider.equals(userData.provider) : userData.provider != null)
            return false;
        if (uid != null ? !uid.equals(userData.uid) : userData.uid != null) return false;
        if (name != null ? !name.equals(userData.name) : userData.name != null) return false;
        if (nickname != null ? !nickname.equals(userData.nickname) : userData.nickname != null)
            return false;
        if (image != null ? !image.equals(userData.image) : userData.image != null) return false;
        if (email != null ? !email.equals(userData.email) : userData.email != null) return false;
        if (createdAt != null ? !createdAt.equals(userData.createdAt) : userData.createdAt != null)
            return false;
        if (updatedAt != null ? !updatedAt.equals(userData.updatedAt) : userData.updatedAt != null)
            return false;
        return !(accessToken != null ? !accessToken.equals(userData.accessToken) : userData.accessToken != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (provider != null ? provider.hashCode() : 0);
        result = 31 * result + (uid != null ? uid.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (nickname != null ? nickname.hashCode() : 0);
        result = 31 * result + (image != null ? image.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
        result = 31 * result + (updatedAt != null ? updatedAt.hashCode() : 0);
        result = 31 * result + (accessToken != null ? accessToken.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UserData{" +
                "id=" + id +
                ", provider='" + provider + '\'' +
                ", uid='" + uid + '\'' +
                ", name='" + name + '\'' +
                ", nickname='" + nickname + '\'' +
                ", image='" + image + '\'' +
                ", email='" + email + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", accessToken='" + accessToken + '\'' +
                '}';
    }
}
