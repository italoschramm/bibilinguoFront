package br.com.italoschramm.bibilinguo.model.rest;

import android.os.Parcel;
import android.os.Parcelable;

public class TypeQuestion implements Parcelable {

    private long id;
    private String description;
    private boolean active;

    protected TypeQuestion(Parcel in) {
        id = in.readLong();
        description = in.readString();
        active = in.readByte() != 0;
    }

    public static final Creator<TypeQuestion> CREATOR = new Creator<TypeQuestion>() {
        @Override
        public TypeQuestion createFromParcel(Parcel in) {
            return new TypeQuestion(in);
        }

        @Override
        public TypeQuestion[] newArray(int size) {
            return new TypeQuestion[size];
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
        parcel.writeByte((byte) (active ? 1 : 0));
    }
}
