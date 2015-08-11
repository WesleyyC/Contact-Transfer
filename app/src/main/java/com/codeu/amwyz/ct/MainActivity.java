package com.codeu.amwyz.ct;


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.codeu.amwyz.ct.sync.CTSyncAdapter;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import org.json.JSONArray;


public class MainActivity extends ActionBarActivity {
    // log tag
    private final String LOG_TAG = MainActivity.class.getSimpleName();
    private NfcAdapter mNfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Debug.waitForDebugger();
        Log.e(LOG_TAG, "onCreate called");
        setContentView(R.layout.activity_main);
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (savedInstanceState == null){
            // get the id info
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            boolean idHasBeenGenerated = prefs.getBoolean(getString(R.string.id_generated_key), false);
            checkNFCCapabilities();
            // if id hasn't been generated, create one and push to Parse server
            if (!prefs.contains(getString(R.string.user_id_key))) {
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
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString(getString(R.string.user_id_key), objectId);
                            editor.putStringSet(getString(R.string.user_contacts_key), Utility.TEST_CONTACT_SET);
                            editor.commit();

                            // log
                            Log.v(LOG_TAG, "New User ID: " + objectId);
                        } else {
                            // Failure log
                            Log.d(LOG_TAG, "Couldn't initialize new object");
                        }
                    }
                });
            } else {
                // User Already Have an Info
                Log.v(LOG_TAG, "Already have ID info");
            }

            Button addButton = (Button) findViewById(R.id.main_add_button);
            Button shareButton = (Button) findViewById(R.id.main_share_button);
            Button contactsButton = (Button) findViewById(R.id.main_contact_button);

            addButton.setOnClickListener(onAddButtonClick);
            shareButton.setOnClickListener(onShareButtonClick);
            contactsButton.setOnClickListener(onContactButtonClick);

        }

        CTSyncAdapter.initializeSyncAdapter(this);
    }

    private View.OnClickListener onAddButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent addIntent = new Intent(getApplicationContext(), AddActivity.class);
            startActivity(addIntent);
        }
    };
    private View.OnClickListener onShareButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent shareIntent = new Intent(getApplicationContext(), ShareActivity.class);
            startActivity(shareIntent);
        }
    };
    private View.OnClickListener onContactButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent contactIntent = new Intent(getApplicationContext(), ContactsActivity.class);
            startActivity(contactIntent);
        }
    };


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
        }else if (id == R.id.action_photo) {
            startActivity(new Intent(this, com.codeu.amwyz.ct.PhotoActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void checkNFCCapabilities() {
        PackageManager pm = this.getPackageManager();
        if (!pm.hasSystemFeature(PackageManager.FEATURE_NFC)) {
            // NFC is not available on the device.
            Toast.makeText(this, "The device does not has NFC hardware.",
                    Toast.LENGTH_SHORT);
        }
        // Check whether device is running Android 4.1 or higher
        else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            // Android Beam feature is not supported.
            Toast.makeText(this, "Android Beam is not supported.",
                    Toast.LENGTH_SHORT).show();
        }
        else {
            // NFC and Android Beam file transfer is supported.
            Toast.makeText(this, "Android Beam is supported on your device.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    /*@Override
    protected void onStart() {
        super.onStart();
        Log.e(LOG_TAG, "onStart called");
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.e(LOG_TAG, "on resume called");
        *//*mNfcAdapter.enableForegroundDispatch();*//*
//        mResumed = true;
        // Sticky notes received from Android
        Log.e(LOG_TAG, getIntent().getAction().toString());
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            Log.e(LOG_TAG, "ndef discovered");
            Toast.makeText(this, "NDEF MESSAGE DISCOVERED", Toast.LENGTH_LONG).show();
            NdefMessage[] messages = getNdefMessages(getIntent());
            byte[] payload = messages[0].getRecords()[0].getPayload();
//            setNoteBody(new String(payload));
            //add the contact by getting the parseId from the Ndef message
            Log.e(LOG_TAG,"payload is: "+  payload.toString());
            Utility.addContacts(this,payload.toString());
            //setIntent(new Intent()); // Consume this intent.
        }
//        enableNdefExchangeMode();
    }

    //if the onResume method ends up not actually adding the contact,
    //might need to add the contact from here...?
    @Override
    protected void onNewIntent(Intent intent) {
        // NDEF exchange mode
//        Log.e(LOG_TAG,"on new intent called");
//        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
//            NdefMessage[] msgs = getNdefMessages(intent);
//            //promptForContent(msgs[0]);
//            Log.e(LOG_TAG,"ndef discovered in onNewIntent");
//            Utility.addContacts(this,msgs[0].getRecords()[0].getPayload().toString());
//        }
        setIntent(intent);
    }

    public NdefMessage[] getNdefMessages(Intent intent) {
        NdefMessage[] messages;
        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(mNfcAdapter.EXTRA_NDEF_MESSAGES);
        if (rawMsgs != null) {
            messages = new NdefMessage[rawMsgs.length];
            for (int i = 0; i < rawMsgs.length; i++) {
                messages[i] = (NdefMessage) rawMsgs[i];
            }
            return messages;
        }
        else if (rawMsgs == null){
            Toast.makeText(getApplicationContext(), "No NDEF Message Read", Toast.LENGTH_LONG).show();
        }
        return null;
    }*/
}
