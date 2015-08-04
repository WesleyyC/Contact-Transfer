package com.codeu.amwyz.ct;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import org.json.JSONArray;


public class MainActivity extends ActionBarActivity {

    // log tag
    private final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get the id info
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        // if id hasn't been generated, create one and push to Parse server
        if(!prefs.contains(getString(R.string.user_id_key))){
            // create an empty parse object
            final ParseObject newObject = new ParseObject(getString(R.string.test_parse_class_key));
            // enter test contact list
            JSONArray mJSONArray = new JSONArray(Utility.TEST_CONTACT_LIST);
            newObject.put(getString(R.string.user_contacts_key), mJSONArray);
            // push to parse server
            newObject.saveInBackground(new SaveCallback() {
                public void done(ParseException e) {
                    if (e == null) {
                        // Success!
                        // get the object id as the user id
                        String objectId = newObject.getObjectId();
                        // get DEFAULT SHARE PREFERENCES
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        // update the preference with new boolean and id
                        Editor editor = prefs.edit();
                        editor.putString(getString(R.string.user_id_key), objectId);
                        editor.putStringSet(getString(R.string.user_contacts_key),Utility.TEST_CONTACT_SET);
                        editor.commit();

                        // log
                        Log.v(LOG_TAG, "New User ID: " + objectId);
                    } else {
                        // Failure log
                        Log.d(LOG_TAG, "Couldn't initialize new object");
                    }
                }
            });
        }else{
            // User Already Have an Info
            Log.v(LOG_TAG, "Already have ID info");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, com.codeu.amwyz.ct.SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
