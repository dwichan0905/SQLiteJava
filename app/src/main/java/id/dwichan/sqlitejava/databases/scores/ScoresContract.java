package id.dwichan.sqlitejava.databases.scores;

import android.provider.BaseColumns;

public class ScoresContract {

    public final static String TABLE_NAME = "tbl_score"; // nama tabel

    public static final class ScoresColumn implements BaseColumns {

        public static String _ID = "id";
        public static String NIM = "nim"; // Nomor Induk Mahasiswa
        public static String COURSES = "courses"; // mata kuliah
        public static String SCORE = "score"; // nilai

    }

}
