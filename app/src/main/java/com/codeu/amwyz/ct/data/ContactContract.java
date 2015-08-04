package com.codeu.amwyz.ct.data;

/**
 * Created by wesley on 7/30/15.
 */
import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Defines table and column names for the contact database.
 */
public class ContactContract {


    public static final String CONTENT_AUTHORITY = "com.codeu.amwyz.ct";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_CONTACT = "contact";

    /* Inner class that defines the table contents of the contact table */
    public static final class ContactEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_CONTACT).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CONTACT;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CONTACT;

        // Table name
        public static final String TABLE_NAME = "contact";

        // User ParseID
        public static final String COLUMN_USER_PARSE_ID = "user_parse_id";

        // Real Name
        public static final String COLUMN_USER_REAL_NAME = "user_real_name";

        // Phone Number
        public static final String COLUMN_USER_PHONE = "user_phone";

        // Linkedin Email
        public static final String COLUMN_USER_LINKEDIN = "user_linkedin";

        // Facebook ID
        public static final String COLUMN_USER_FACEBOOK = "user_facebook";

        // Email Address
        public static final String COLUMN_USER_EMAIL = "user_email";


        public static Uri buildContactUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static String getIDFromURI(Uri uri){
            return Long.toString(Long.parseLong(uri.getPathSegments().get(1)));
        }
    }


}
