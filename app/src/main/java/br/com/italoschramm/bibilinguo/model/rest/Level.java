package br.com.italoschramm.bibilinguo.model.rest;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Level implements Parcelable {

    private long id;
    private String description;
    private List<Subject> subject;
    private boolean active;

    protected Level(Parcel in) {
        id = in.readLong();
        description = in.readString();
        subject = in.createTypedArrayList(Subject.CREATOR);
        active = in.readByte() != 0;
    }

    public static final Creator<Level> CREATOR = new Creator<Level>() {
        @Override
        public Level createFromParcel(Parcel in) {
            return new Level(in);
        }

        @Override
        public Level[] newArray(int size) {
            return new Level[size];
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

    public List<Subject> getSubject() {
        return subject;
    }

    public void setSubject(List<Subject> subject) {
        this.subject = subject;
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
        parcel.writeTypedList(subject);
        parcel.writeByte((byte) (active ? 1 : 0));
    }
}
