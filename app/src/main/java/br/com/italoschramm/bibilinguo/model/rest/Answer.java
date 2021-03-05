package br.com.italoschramm.bibilinguo.model.rest;

import android.os.Parcel;
import android.os.Parcelable;

public class Answer implements Parcelable {

    private long id;
    private String description;
    private String image;
    private String sound;
    private Question question;
    private boolean active;

    protected Answer(Parcel in) {
        id = in.readLong();
        description = in.readString();
        image = in.readString();
        sound = in.readString();
        question = in.readParcelable(Question.class.getClassLoader());
        active = in.readByte() != 0;
    }

    public static final Creator<Answer> CREATOR = new Creator<Answer>() {
        @Override
        public Answer createFromParcel(Parcel in) {
            return new Answer(in);
        }

        @Override
        public Answer[] newArray(int size) {
            return new Answer[size];
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

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
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
        parcel.writeString(sound);
        parcel.writeParcelable(question, i);
        parcel.writeByte((byte) (active ? 1 : 0));
    }
}
