package id.dwichan.sqlitejava.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Student implements Parcelable {

    public static final Creator<Student> CREATOR = new Creator<Student>() {
        @Override
        public Student createFromParcel(Parcel in) {
            return new Student(in);
        }

        @Override
        public Student[] newArray(int size) {
            return new Student[size];
        }
    };
    private String nim;
    private String name;
    private String email;

    public Student(String nim, String name, String email) {
        this.nim = nim;
        this.name = name;
        this.email = email;
    }

    protected Student(Parcel in) {
        nim = in.readString();
        name = in.readString();
        email = in.readString();
    }

    public String getNim() {
        return nim;
    }

    public void setNim(String nim) {
        this.nim = nim;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nim);
        dest.writeString(name);
        dest.writeString(email);
    }
}
