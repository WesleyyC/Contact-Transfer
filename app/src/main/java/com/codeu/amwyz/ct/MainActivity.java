package com.codeu.amwyz.ct;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {
    //SectionsPagerAdapter for settings profiles
    // log tag
    private final String LOG_TAG = MainActivity.class.getSimpleName();

    FragmentManager fragmentManager = getFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get the id info
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean idHasBeenGenerated = prefs.getBoolean(getString(R.string.id_generated_key), false);

        // if id hasn't been generated, create one and push to Parse server
        if(!idHasBeenGenerated){
            // create an empty parse object
            final ParseObject newObject = new ParseObject(getString(R.string.test_parse_class_key));
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
                        editor.putBoolean(getString(R.string.id_generated_key), true);
                        editor.putString(getString(R.string.user_id_key), objectId);
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

        //creating the buttons by attaching the fragments to the activity
        //todo: put in a method
        AddButtonFragment addButtonFragment = new AddButtonFragment();
        fragmentTransaction.add(R.id.top_main_fragment_container, addButtonFragment);

        ShareButtonFragment shareButtonFragment = new ShareButtonFragment();
        fragmentTransaction.add(R.id.top_main_fragment_container, shareButtonFragment);

        ContactsButtonFragment contactsButtonFragment = new ContactsButtonFragment();
        fragmentTransaction.add(R.id.bottom_main_fragment_container, contactsButtonFragment);

        fragmentTransaction.commit();
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

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.add_button:{
                break;
            }
            case R.id.share_button:{
                break;
            }
            case R.id.contacts_button:{
                Intent contactsIntent = new Intent(this, ContactsActivity.class);
                startActivity(contactsIntent);
                break;
            }
        }
    }
}
