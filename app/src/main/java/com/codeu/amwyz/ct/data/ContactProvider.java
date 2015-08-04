package com.codeu.amwyz.ct.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class ContactProvider extends ContentProvider {

    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private ContactDbHelper mOpenHelper;

    static final int CONTACT = 100;
    static final int CONTACT_WITH_ID = 101;

    //contact.parse_id = ?
    private static final String sContactIDSelection =
            ContactContract.ContactEntry.TABLE_NAME+
                    "." + ContactContract.ContactEntry._ID + " = ? ";

    //contact.contact_id = ?
    public static final String sParseIDSelection =
            ContactContract.ContactEntry.TABLE_NAME+
                    "." + ContactContract.ContactEntry.COLUMN_USER_PARSE_ID + " = ? ";

    // get contact table row by id
    private Cursor getContactByID(Uri uri, String[] projection, String sortOrder) {
        // build URI
        String contact_id = ContactContract.ContactEntry.getIDFromURI(uri);
        // Build Selection
        String[] selectionArgs = new String[]{contact_id};
        String selection = sContactIDSelection;
        // get the cursor
        return mOpenHelper.getReadableDatabase().query(
                ContactContract.ContactEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    // build up the UriMatcher
    static UriMatcher buildUriMatcher() {
        // Path
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = ContactContract.CONTENT_AUTHORITY;
        // /contact
        matcher.addURI(authority, ContactContract.PATH_CONTACT, CONTACT);
        // /contact/id
        matcher.addURI(authority, ContactContract.PATH_CONTACT+ "/#", CONTACT_WITH_ID);

        return matcher;
    }


    @Override
    public boolean onCreate() {
        mOpenHelper = new ContactDbHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {

        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case CONTACT_WITH_ID:
                return ContactContract.ContactEntry.CONTENT_ITEM_TYPE;
            case CONTACT:
                return ContactContract.ContactEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            // "contact/#"
            case CONTACT_WITH_ID: {
                retCursor = getContactByID(uri, projection, sortOrder);
                break;
            }
            // "contact"
            case CONTACT: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        ContactContract.ContactEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case CONTACT: {
                long _id = db.insert(ContactContract.ContactEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = ContactContract.ContactEntry.buildContactUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted
        if ( null == selection ) selection = "1";
        switch (match) {
            case CONTACT:
                rowsDeleted = db.delete(
                        ContactContract.ContactEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }


    @Override
    public int update(
            Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case CONTACT:
                rowsUpdated = db.update(ContactContract.ContactEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case CONTACT:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(ContactContract.ContactEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }
}