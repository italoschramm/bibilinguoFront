package br.com.italoschramm.bibilinguo.model;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

    private long id;

    private String name;

    private String email;

    private boolean active;

    private String imageProfile;

    public User(){

    }

    protected User(Parcel in) {
        id = in.readLong();
        name = in.readString();
        email = in.readString();
        active = in.readByte() != 0;
        imageProfile = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getImageProfile() {
        return imageProfile;
    }

    public void setImageProfile(String imageProfile) {
        this.imageProfile = imageProfile;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(name);
        parcel.writeString(email);
        parcel.writeByte((byte) (active ? 1 : 0));
        parcel.writeString(imageProfile);
    }
}
