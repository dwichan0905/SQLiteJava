package id.dwichan.sqlitejava.databases;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import id.dwichan.sqlitejava.databases.scores.ScoresContract;
import id.dwichan.sqlitejava.databases.students.StudentsContract;

public class AllDataHelper {

    private static DatabaseHelper databaseHelper;
    private static AllDataHelper INSTANCE;
    private static SQLiteDatabase database;

    public AllDataHelper(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public static AllDataHelper getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SQLiteOpenHelper.class) {
                if (INSTANCE == null) INSTANCE = new AllDataHelper(context);
            }
        }
        return INSTANCE;
    }

    public void openDatabase() throws SQLException {
        database = databaseHelper.getWritableDatabase();
        //database.execSQL("PRAGMA foreign_keys=ON");
    }

    public void closeDatabase() {
        databaseHelper.close();
        if (database.isOpen()) database.close();
    }

    public Cursor queryAllStudentScore() {
        return database.rawQuery(
                "SELECT a." + ScoresContract.ScoresColumn._ID + ", " +
                        "a." + ScoresContract.ScoresColumn.NIM + ", " +
                        "b." + StudentsContract.StudentsColumn.NAME + ", " +
                        "b." + StudentsContract.StudentsColumn.EMAIL + ", " +
                        "a." + ScoresContract.ScoresColumn.COURSES + ", " +
                        "a." + ScoresContract.ScoresColumn.SCORE + " " +
                        "FROM " + ScoresContract.TABLE_NAME + " a " +
                        "INNER JOIN " + StudentsContract.TABLE_NAME + " b " +
                        "ON a." + ScoresContract.ScoresColumn.NIM + " = b." + StudentsContract.StudentsColumn.NIM, null);
    }
}
