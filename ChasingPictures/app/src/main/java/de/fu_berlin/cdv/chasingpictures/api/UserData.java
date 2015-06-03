package de.fu_berlin.cdv.chasingpictures.api;

import android.os.Parcel;
import android.os.Parcelable;

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
}
