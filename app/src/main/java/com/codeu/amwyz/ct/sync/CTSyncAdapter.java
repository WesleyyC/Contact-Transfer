package com.codeu.amwyz.ct.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.codeu.amwyz.ct.R;
import com.codeu.amwyz.ct.Utility;
import com.codeu.amwyz.ct.data.ContactContract;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CTSyncAdapter extends AbstractThreadedSyncAdapter {
    public final String LOG_TAG = CTSyncAdapter.class.getSimpleName();
    // Interval at which to sync with the weather, in seconds.
    // 60 seconds (1 minute) * 60 = 1 hours
    public static final int SYNC_INTERVAL = 60 * 60;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL/3;



    public CTSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.d(LOG_TAG, "Starting sync");
        // get the default preference list
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        // get current user's contact list
        ParseQuery<ParseObject> query = ParseQuery.getQuery(getContext().getString(R.string.test_parse_class_key));
        String objectId = prefs.getString(getContext().getString(R.string.user_id_key), "");
        query.getInBackground(objectId, new GetCallback<ParseObject>() {
            public void done(ParseObject user_profile, ParseException e) {
                if (e == null) {
                    // convert the json contact list to String set
                    JSONArray contactsJSONList = user_profile.getJSONArray(getContext().getString(R.string.user_contacts_key));
                    Set<String> contactsSet = new HashSet<String>();
                    if(contactsJSONList != null) {
                        for (int i = 0; i < contactsJSONList.length(); i++) {
                            try {
                                contactsSet.add(contactsJSONList.getString(i));
                            }catch(JSONException JSONe){
                                Log.d(LOG_TAG, "Problem getting contact list set: " + JSONe.toString());
                            }
                        }
                        // use the string set to make query for contact list
                        ParseQuery<ParseObject> query = ParseQuery.getQuery(getContext().getString(R.string.test_parse_class_key));
                        query = query.whereContainedIn("objectId",contactsSet);
                        query.findInBackground(new FindCallback<ParseObject>() {
                            public void done(List<ParseObject> objects, ParseException e) {
                                if (e == null) {
                                    // before inserting, drop the whole table
                                    getContext().getContentResolver().delete(ContactContract.ContactEntry.CONTENT_URI,null,null);
                                    // insert each ParseObject
                                    for(ParseObject object:objects){
                                        // Create content value with the following method in Utility
                                        ContentValues newValue = Utility.createContactValues(object.getObjectId(), object.getString(getContext().getString(R.string.user_real_name_key)),
                                                object.getString(getContext().getString(R.string.user_phone_key)), object.getString(getContext().getString(R.string.user_email_key)),
                                                object.getString(getContext().getString(R.string.facebook_user_id)), object.getString(getContext().getString(R.string.user_linkedin_key)));
                                        // Insert
                                        Log.d(LOG_TAG,newValue.getAsString(ContactContract.ContactEntry.COLUMN_USER_PARSE_ID));
                                        getContext().getContentResolver().insert(ContactContract.ContactEntry.CONTENT_URI,newValue);
                                    }
                                } else {
                                    Log.d(LOG_TAG,"Parse fetching fail:" + e.toString());
                                }
                            }
                        });
                    }
                }
            }
        });
    }


    /**
     * Helper method to schedule the sync adapter periodic execution
     */
    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // we can enable inexact timers in our periodic sync
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(syncInterval, flexTime).
                    setSyncAdapter(account, authority).
                    setExtras(new Bundle()).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account,
                    authority, new Bundle(), syncInterval);
        }
    }

    /**
     * Helper method to have the sync adapter sync immediately
     * @param context The context used to access the account service
     */
    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority), bundle);
    }

    /**
     * Helper method to get the fake account to be used with SyncAdapter, or make a new one
     * if the fake account doesn't exist yet.  If we make a new account, we call the
     * onAccountCreated method so we can initialize things.
     *
     * @param context The context used to access the account service
     * @return a fake account.
     */
    public static Account getSyncAccount(Context context) {
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Create the account type and default account
        Account newAccount = new Account(
                context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

        // If the password doesn't exist, the account doesn't exist
        if ( null == accountManager.getPassword(newAccount) ) {

        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }


            onAccountCreated(newAccount, context);
        }
        return newAccount;
    }

    private static void onAccountCreated(Account newAccount, Context context) {

        CTSyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);

        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);

        syncImmediately(context);
    }

    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
    }
}