package br.com.italoschramm.bibilinguo.model.rest;

import android.os.Parcel;
import android.os.Parcelable;

import java.lang.reflect.Type;
import java.util.List;

public class Question implements Parcelable {

    private long id;
    private String description;
    private String sound;
    private List<Answer> answer;
    private Answer rightAnswer;
    private TypeQuestion typeQuestion;
    private boolean active;

    protected Question(Parcel in) {
        id = in.readLong();
        description = in.readString();
        sound = in.readString();
        active = in.readByte() != 0;
        answer = in.createTypedArrayList(Answer.CREATOR);
        rightAnswer = (Answer) in.readParcelable(Answer.class.getClassLoader());
        typeQuestion = (TypeQuestion) in.readParcelable(TypeQuestion.class.getClassLoader());
    }

    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
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

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public List<Answer> getAnswer() {
        return answer;
    }

    public void setAnswers(List<Answer> answer) {
        this.answer = answer;
    }

    public void setAnswer(List<Answer> answer) {
        this.answer = answer;
    }

    public Answer getRightAnswer() {
        return rightAnswer;
    }

    public void setRightAnswer(Answer rightAnswer) {
        this.rightAnswer = rightAnswer;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public TypeQuestion getTypeQuestion() {
        return typeQuestion;
    }

    public void setTypeQuestion(TypeQuestion typeQuestion) {
        this.typeQuestion = typeQuestion;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(description);
        parcel.writeString(sound);
        parcel.writeByte((byte) (active ? 1 : 0));
        parcel.writeTypedList(answer);
        parcel.writeParcelable(rightAnswer, i);
        parcel.writeParcelable(typeQuestion, i);
    }
}
