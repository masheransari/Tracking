package com.example.android.careemapp.SQL;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.careemapp.ModelClass.UserModel;

/**
 * Created by asher.ansari on 1/29/2018.
 */

public class DB_Helper extends SQLiteOpenHelper {
    private static String DB_NAME = "careem.db";
    private static int DB_VERSION = 1;

    public DB_Helper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    private String userTable = "CREATE TABLE IF NOT EXISTS " + Table.USER_TABLE.TABLE_NAME + " (" +
            Table.USER_TABLE._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            Table.USER_TABLE.COLUMN_NAME + " TEXT NOT NULL," +
            Table.USER_TABLE.COLUMN_EMAIL + " TEXT NOT NULL," +
            Table.USER_TABLE.COLUMN_MOBILE_ID + " TEXT NOT NULL," +
            Table.USER_TABLE.COLUMN_UID + " TEXT NOT NULL," +
            Table.USER_TABLE.COLUMN_USER_RIGHT + " INTEGER DEFAULT 0);";

    public void insertUser(UserModel userModel) {
        ContentValues values = new ContentValues();
        values.put(Table.USER_TABLE.COLUMN_EMAIL, userModel.getEmail());
        values.put(Table.USER_TABLE.COLUMN_NAME, userModel.getName());
        values.put(Table.USER_TABLE.COLUMN_MOBILE_ID, userModel.getMobileId());
        values.put(Table.USER_TABLE.COLUMN_UID, userModel.getUid());
        values.put(Table.USER_TABLE.COLUMN_USER_RIGHT, Integer.valueOf(userModel.getUserRight()));
        SQLiteDatabase database = getWritableDatabase();
        database.insert(Table.USER_TABLE.TABLE_NAME, null, values);
        database.close();
    }

    public void updateMobileId(String mobileId, String uid) {
        String where = Table.USER_TABLE.COLUMN_UID + "='" + uid + "'";
        ContentValues values = new ContentValues();
        values.put(Table.USER_TABLE.COLUMN_MOBILE_ID, mobileId);
        getWritableDatabase().update(Table.USER_TABLE.TABLE_NAME, values, where, null);
    }

    public UserModel getUser() {
        Cursor cursor = getReadableDatabase().query(Table.USER_TABLE.TABLE_NAME, null, null, null, null, null, null);
        if (cursor.getCount() == 1) {
            while (cursor.moveToNext()) {
                return new UserModel(cursor.getString(cursor.getColumnIndex(Table.USER_TABLE.COLUMN_UID)),
                        cursor.getString(cursor.getColumnIndex(Table.USER_TABLE.COLUMN_NAME)),
                        cursor.getString(cursor.getColumnIndex(Table.USER_TABLE.COLUMN_EMAIL)),
                        null,
                        "" + cursor.getInt(cursor.getColumnIndex(Table.USER_TABLE.COLUMN_USER_RIGHT)),
                        cursor.getString(cursor.getColumnIndex(Table.USER_TABLE.COLUMN_MOBILE_ID))
                );
            }
        }
        deleteUSerTable();
        return null;
    }

    public void deleteUSerTable() {
        getWritableDatabase().delete(Table.USER_TABLE.TABLE_NAME, null, null);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(userTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
