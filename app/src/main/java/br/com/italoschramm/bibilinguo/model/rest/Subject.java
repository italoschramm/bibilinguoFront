package br.com.italoschramm.bibilinguo.model.rest;

import android.os.Parcel;
import android.os.Parcelable;

public class Subject implements Parcelable {

    private long id;
    private String description;
    private String image;
    private boolean active;

    protected Subject(Parcel in) {
        id = in.readLong();
        description = in.readString();
        image = in.readString();
        active = in.readByte() != 0;
    }

    public static final Creator<Subject> CREATOR = new Creator<Subject>() {
        @Override
        public Subject createFromParcel(Parcel in) {
            return new Subject(in);
        }

        @Override
        public Subject[] newArray(int size) {
            return new Subject[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(description);
        parcel.writeString(image);
        parcel.writeByte((byte) (active ? 1 : 0));
    }
}
