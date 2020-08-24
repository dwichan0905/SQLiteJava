package id.dwichan.sqlitejava.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import id.dwichan.sqlitejava.databases.scores.ScoresContract;
import id.dwichan.sqlitejava.databases.students.StudentsContract;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String SQL_CREATE_TABLE_STUDENT = String.format(
            "CREATE TABLE %s (" +
                    "%s VARCHAR(10) NOT NULL PRIMARY KEY, " +
                    "%s TEXT NOT NULL, " +
                    "%s TEXT NOT NULL)",
            StudentsContract.TABLE_NAME,
            StudentsContract.StudentsColumn.NIM,
            StudentsContract.StudentsColumn.NAME,
            StudentsContract.StudentsColumn.EMAIL
    );
    private static final String SQL_CREATE_TABLE_SCORE = String.format(
            "CREATE TABLE %s (" +
                    "%s INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                    "%s VARCHAR(10) NOT NULL, " +
                    "%s TEXT NOT NULL, " +
                    "%s CHAR(1) NOT NULL," +
                    "UNIQUE (%s, %s)," +
                    "FOREIGN KEY (%s) REFERENCES %s(%s) ON UPDATE CASCADE ON DELETE CASCADE)",
            ScoresContract.TABLE_NAME,
            ScoresContract.ScoresColumn._ID,
            ScoresContract.ScoresColumn.NIM,
            ScoresContract.ScoresColumn.COURSES,
            ScoresContract.ScoresColumn.SCORE,
            ScoresContract.ScoresColumn.NIM, ScoresContract.ScoresColumn.COURSES, // UNIQUE
            // FOREIGN KEY
            ScoresContract.ScoresColumn.NIM,
            StudentsContract.TABLE_NAME, StudentsContract.StudentsColumn.NIM
    );
    public static String DATABASE_NAME = "dbStudentValues";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_STUDENT);
        db.execSQL(SQL_CREATE_TABLE_SCORE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ScoresContract.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + StudentsContract.TABLE_NAME);
        onCreate(db);
    }
}
