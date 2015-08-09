package com.codeu.amwyz.ct;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;

import com.codeu.amwyz.ct.data.ContactContract;
import com.codeu.amwyz.ct.data.ContactProvider;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by wesley on 8/3/15.
 */
public class Utility {

    private static final String LOG_TAG = Utility.class.getSimpleName();

    // contacts_list_setup
    public static final String[] TEST_CONTACT_ARRAY={"Xhgsv7u7pW","kexbIzRtjc","sma0eYdYTL","E8OetEbIbi","haWykmSo9s"};
    public static final List<String> TEST_CONTACT_LIST= Arrays.asList(TEST_CONTACT_ARRAY);
    public static final Set<String> TEST_CONTACT_SET = new HashSet<>(TEST_CONTACT_LIST);

    // build up a content value for database using the following parameter
    public static ContentValues createContactValues(String user_parse_id, String user_real_name, String user_phone, String user_email, String user_facebook, String user_linkedin) {
        // Create a new map of values, where column names are the keys
        ContentValues testValues = new ContentValues();
        testValues.put(ContactContract.ContactEntry.COLUMN_USER_PARSE_ID, user_parse_id);
        testValues.put(ContactContract.ContactEntry.COLUMN_USER_REAL_NAME, user_real_name);
        testValues.put(ContactContract.ContactEntry.COLUMN_USER_PHONE, user_phone);
        testValues.put(ContactContract.ContactEntry.COLUMN_USER_EMAIL,user_email);
        testValues.put(ContactContract.ContactEntry.COLUMN_USER_FACEBOOK,user_facebook);
        testValues.put(ContactContract.ContactEntry.COLUMN_USER_LINKEDIN, user_linkedin);
        return testValues;
    }

    // add a contact to the user's contact list locally, which will then update with the ParseServer
    // just give context and contact's ParseId
    public static void addContacts(final Context context, final String parseId){
        // Remove from server
        // get the default preference list
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        // get current user's contact list
        ParseQuery<ParseObject> query = ParseQuery.getQuery(context.getString(R.string.test_parse_class_key));
        String objectId = prefs.getString(context.getString(R.string.user_id_key), "");
        query.getInBackground(objectId, new GetCallback<ParseObject>() {
            public void done(ParseObject user_profile, ParseException e) {
                if (e == null) {
                    // convert the json contact list to String set
                    JSONArray contactsJSONList = user_profile.getJSONArray(context.getString(R.string.user_contacts_key));
                    if (contactsJSONList != null) {
                        user_profile.put(context.getString(R.string.user_contacts_key), contactsJSONList.put(parseId));
                        user_profile.saveInBackground();
                    }
                }
            }
        });
    }

    public static void QRAdd(Context context, String parseId){
        addContacts(context, parseId);
    }

    public static void NFCAdd(Context context, String user_parse_id, String user_real_name, String user_phone, String user_email, String user_facebook, String user_linkedin){
        addContacts(context, user_parse_id);
        ContentValues newValue = createContactValues(user_parse_id,user_real_name,user_phone,user_email,user_facebook,user_linkedin);
        context.getContentResolver().insert(ContactContract.ContactEntry.CONTENT_URI, newValue);
    }

    public static void facebookIntent(Context context, String facebookId){
        String facebookURL = "http://www.facebook.com/"+facebookId;
        Intent i = new Intent(Intent.ACTION_VIEW,
                Uri.parse(facebookURL));
        context.startActivity(i);
    }

    public static void removeContactAndSync(final Context context, final String parseId){
        // Remove from local database
        String[] selectionArgs = {parseId};
        context.getContentResolver().delete(ContactContract.ContactEntry.CONTENT_URI, ContactProvider.sParseIDSelection, selectionArgs);

        // Remove from server
        // get the default preference list
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        // get current user's contact list
        ParseQuery<ParseObject> query = ParseQuery.getQuery(context.getString(R.string.test_parse_class_key));
        String objectId = prefs.getString(context.getString(R.string.user_id_key), "");
        query.getInBackground(objectId, new GetCallback<ParseObject>() {
            public void done(ParseObject user_profile, ParseException e) {
                if (e == null) {
                    // convert the json contact list to String set
                    JSONArray contactsJSONList = user_profile.getJSONArray(context.getString(R.string.user_contacts_key));
                    Set<String> contactsSet = new HashSet<String>();
                    if (contactsJSONList != null) {
                        for (int i = 0; i < contactsJSONList.length(); i++) {
                            try {
                                contactsSet.add(contactsJSONList.getString(i));
                            } catch (JSONException JSONe) {
                                Log.d(LOG_TAG, "Problem getting contact list set: " + JSONe.toString());
                            }
                        }
                        contactsSet.remove(parseId);
                        user_profile.put(context.getString(R.string.user_contacts_key), new JSONArray(contactsSet));
                        user_profile.saveInBackground();
                    }
                }
            }
        });
    }
}
