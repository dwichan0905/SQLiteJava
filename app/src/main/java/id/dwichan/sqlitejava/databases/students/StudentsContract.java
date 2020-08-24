package id.dwichan.sqlitejava.databases.students;

import android.provider.BaseColumns;

public class StudentsContract {

    public final static String TABLE_NAME = "tbl_students";

    public static final class StudentsColumn implements BaseColumns {
        public static String NIM = "nim";
        public static String NAME = "name";
        public static String EMAIL = "email";
    }

}
