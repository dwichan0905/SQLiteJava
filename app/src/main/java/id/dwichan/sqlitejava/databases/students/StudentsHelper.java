package id.dwichan.sqlitejava.databases.students;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import id.dwichan.sqlitejava.databases.DatabaseHelper;

public class StudentsHelper {
    private static final String DATABASE_TABLE = StudentsContract.TABLE_NAME;
    private static DatabaseHelper databaseHelper;
    private static StudentsHelper INSTANCE;
    private static SQLiteDatabase database;

    private StudentsHelper(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public static StudentsHelper getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SQLiteOpenHelper.class) {
                if (INSTANCE == null) INSTANCE = new StudentsHelper(context);
            }
        }
        return INSTANCE;
    }

    public void openDatabase() throws SQLException {
        database = databaseHelper.getWritableDatabase();
        database.execSQL("PRAGMA foreign_keys=ON");
    }

    public void closeDatabase() {
        databaseHelper.close();
        if (database.isOpen()) database.close();
    }

    public Cursor queryAll() {
        return database.query(DATABASE_TABLE,
                null,
                null,
                null,
                null,
                null,
                null);
    }

    public long insertData(ContentValues values) {
        return database.insert(DATABASE_TABLE, null, values);
    }

    public int update(String whereNim, ContentValues modifiedValues) {
        return database.update(DATABASE_TABLE,
                modifiedValues,
                StudentsContract.StudentsColumn.NIM + " = ?",
                new String[]{whereNim});
    }

    public int deleteByNim(String nim) {
        return database.delete(DATABASE_TABLE,
                StudentsContract.StudentsColumn.NIM + " = ?",
                new String[]{nim});
    }
}
