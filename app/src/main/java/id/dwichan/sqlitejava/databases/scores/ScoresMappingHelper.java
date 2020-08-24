package id.dwichan.sqlitejava.databases.scores;

import android.database.Cursor;

import java.util.ArrayList;

import id.dwichan.sqlitejava.models.Score;

public class ScoresMappingHelper {

    public static ArrayList<Score> mapCursorToArrayList(Cursor cursor) {
        ArrayList<Score> scores = new ArrayList<>();

        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndexOrThrow(ScoresContract.ScoresColumn._ID));
            String nim = cursor.getString(cursor.getColumnIndexOrThrow(ScoresContract.ScoresColumn.NIM));
            String course = cursor.getString(cursor.getColumnIndexOrThrow(ScoresContract.ScoresColumn.COURSES));
            String score = cursor.getString(cursor.getColumnIndexOrThrow(ScoresContract.ScoresColumn.SCORE));
            scores.add(new Score(id, nim, course, score));
        }

        return scores;
    }

}
