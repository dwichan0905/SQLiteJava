package id.dwichan.sqlitejava.databases.scores;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import id.dwichan.sqlitejava.databases.DatabaseHelper;

public class ScoresHelper {

    private final static String DATABASE_TABLE = ScoresContract.TABLE_NAME;
    private static DatabaseHelper databaseHelper;
    private static ScoresHelper INSTANCE;
    private static SQLiteDatabase database;

    public ScoresHelper(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public static ScoresHelper getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SQLiteOpenHelper.class) {
                if (INSTANCE == null) INSTANCE = new ScoresHelper(context);
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

    public Cursor queryAllByNim(String nim) {
        return database.query(DATABASE_TABLE,
                null,
                ScoresContract.ScoresColumn.NIM + " = ?",
                new String[]{nim},
                null,
                null,
                null);
    }

    public long insertData(ContentValues values) {
        return database.insert(DATABASE_TABLE, null, values);
    }

    public int update(String whereId, ContentValues modifiedValues) {
        return database.update(DATABASE_TABLE,
                modifiedValues,
                ScoresContract.ScoresColumn._ID + " = ?",
                new String[]{whereId});
    }

    public int deleteById(String id) {
        return database.delete(DATABASE_TABLE,
                ScoresContract.ScoresColumn._ID + " = ?",
                new String[]{id});
    }
}
