package com.example.android.careemapp.SQL;

import android.provider.BaseColumns;

/**
 * Created by asher.ansari on 1/29/2018.
 */

public class Table {
    public static class USER_TABLE {
        public static final String TABLE_NAME = "user";
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_UID = "uid";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_MOBILE_ID = "instanceID";
        public static final String COLUMN_USER_RIGHT = "rights";
    }
}
