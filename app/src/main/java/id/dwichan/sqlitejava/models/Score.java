package id.dwichan.sqlitejava.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Score implements Parcelable {

    public static final Creator<Score> CREATOR = new Creator<Score>() {
        @Override
        public Score createFromParcel(Parcel in) {
            return new Score(in);
        }

        @Override
        public Score[] newArray(int size) {
            return new Score[size];
        }
    };
    private String id;
    private String nim;
    private String courseName;
    private String score;

    public Score() {
    }

    public Score(String id, String nim, String course, String score) {
        this.id = id;
        this.nim = nim;
        this.courseName = course;
        this.score = score;
    }

    protected Score(Parcel in) {
        id = in.readString();
        nim = in.readString();
        courseName = in.readString();
        score = in.readString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNim() {
        return nim;
    }

    public void setNim(String nim) {
        this.nim = nim;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getScore() {
        return score;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(nim);
        dest.writeString(courseName);
        dest.writeString(score);
    }
}
