package id.dwichan.sqlitejava.databases;

import android.database.Cursor;

import java.util.ArrayList;

import id.dwichan.sqlitejava.databases.scores.ScoresContract;
import id.dwichan.sqlitejava.databases.students.StudentsContract;
import id.dwichan.sqlitejava.models.Data;

public class AllDataMappingHelper {

    public static ArrayList<Data> mapCursorToArrayList(Cursor cursor) {
        ArrayList<Data> dataArrayList = new ArrayList<>();

        while (cursor.moveToNext()) {
            String nim = cursor.getString(cursor.getColumnIndexOrThrow(ScoresContract.ScoresColumn.NIM));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(StudentsContract.StudentsColumn.NAME));
            String email = cursor.getString(cursor.getColumnIndexOrThrow(StudentsContract.StudentsColumn.EMAIL));
            String courses = cursor.getString(cursor.getColumnIndexOrThrow(ScoresContract.ScoresColumn.COURSES));
            String scores = cursor.getString(cursor.getColumnIndexOrThrow(ScoresContract.ScoresColumn.SCORE));

            Data data = new Data();
            data.setNim(nim);
            data.setName(name);
            data.setEmail(email);
            data.setCourses(courses);
            data.setScores(scores);

            dataArrayList.add(data);
        }

        return dataArrayList;
    }

}
