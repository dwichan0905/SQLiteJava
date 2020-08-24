package id.dwichan.sqlitejava.databases.students;

import android.database.Cursor;

import java.util.ArrayList;

import id.dwichan.sqlitejava.models.Student;

public class StudentsMappingHelper {

    public static ArrayList<Student> mapCursorToArrayList(Cursor cursor) {
        ArrayList<Student> students = new ArrayList<>();

        while (cursor.moveToNext()) {
            String nim = cursor.getString(cursor.getColumnIndexOrThrow(StudentsContract.StudentsColumn.NIM));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(StudentsContract.StudentsColumn.NAME));
            String email = cursor.getString(cursor.getColumnIndexOrThrow(StudentsContract.StudentsColumn.EMAIL));
            students.add(new Student(nim, name, email));
        }

        return students;
    }

}
