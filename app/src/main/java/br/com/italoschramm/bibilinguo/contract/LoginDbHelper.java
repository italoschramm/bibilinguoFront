package br.com.italoschramm.bibilinguo.contract;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.italoschramm.bibilinguo.model.rest.Login;

public class LoginDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "bibilinguo.db";

    public LoginDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(getData());
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public String getData(){
        String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + LoginContract.LoginEntry.TABLE_NAME + " (" +
                        LoginContract.LoginEntry._ID + " INTEGER PRIMARY KEY," +
                        LoginContract.LoginEntry.COLUMN_NAME_USERNAME + " TEXT," +
                        LoginContract.LoginEntry.COLUMN_NAME_PASSWORD + " TEXT)";
        return SQL_CREATE_ENTRIES;
    }

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + LoginContract.LoginEntry.TABLE_NAME;

    public void insertData(String login, String password){
        // Gets the data repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(LoginContract.LoginEntry.COLUMN_NAME_USERNAME, login);
        values.put(LoginContract.LoginEntry.COLUMN_NAME_PASSWORD, password);

        long newRowId = db.insert(LoginContract.LoginEntry.TABLE_NAME, null, values);
    }

    public void deleteData(){
        SQLiteDatabase db = this.getWritableDatabase();
        int deletedRows = db.delete(LoginContract.LoginEntry.TABLE_NAME, null, null);
    }

    public Map<String, String> existsData(){
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {
                BaseColumns._ID,
                LoginContract.LoginEntry.COLUMN_NAME_USERNAME,
                LoginContract.LoginEntry.COLUMN_NAME_PASSWORD
        };

        Cursor cursor = db.query(
                LoginContract.LoginEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );

        String login = "";
        String password = "";
        Map<String, String> loginPassword = new HashMap<String, String>();

        if(cursor.moveToNext()) {
            login = cursor.getString(
                    cursor.getColumnIndexOrThrow(LoginContract.LoginEntry.COLUMN_NAME_USERNAME));

            password = cursor.getString(
                    cursor.getColumnIndexOrThrow(LoginContract.LoginEntry.COLUMN_NAME_PASSWORD));
            loginPassword.put(login, password);
        }
        cursor.close();
        return loginPassword;
    }

}
