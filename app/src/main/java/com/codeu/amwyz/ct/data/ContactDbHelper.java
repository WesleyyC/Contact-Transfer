package com.codeu.amwyz.ct.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.codeu.amwyz.ct.data.ContactContract.ContactEntry;

/**
 * Manages a local database for contact data.
 */
public class ContactDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "contact.db";

    public ContactDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Create a table to hold contact information
        final String SQL_CREATE_CONTACT_TABLE = "CREATE TABLE " + ContactEntry.TABLE_NAME + " (" +
                ContactEntry._ID + " INTEGER PRIMARY KEY," +
                ContactEntry.COLUMN_USER_PARSE_ID + " TEXT, " +
                ContactEntry.COLUMN_USER_REAL_NAME+ " TEXT," +
                ContactEntry.COLUMN_USER_PHONE+ " TEXT, " +
                ContactEntry.COLUMN_USER_LINKEDIN+ " TEXT, " +
                ContactEntry.COLUMN_USER_FACEBOOK+ " TEXT, " +
                ContactEntry.COLUMN_USER_EMAIL+ " TEXT" +
                " );";

        sqLiteDatabase.execSQL(SQL_CREATE_CONTACT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ContactEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
