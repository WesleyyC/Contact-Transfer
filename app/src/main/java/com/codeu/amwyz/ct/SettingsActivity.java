
package com.codeu.amwyz.ct;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;


public class SettingsActivity extends PreferenceActivity
        implements Preference.OnPreferenceChangeListener {

    // Creating Facebook CallbackManager Value
    public static CallbackManager callbackmanager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add 'general' preferences, defined in the XML file
        addPreferencesFromResource(R.xml.pref_general);

        //include facebook info as null

        // For all preferences, attach an OnPreferenceChangeListener so the UI summary can be
        // updated when the preference changes.
        bindPreferenceSummaryToValue(findPreference(getString(R.string.user_real_name_key)));
        bindPreferenceSummaryToValue(findPreference(getString(R.string.user_phone_key)));
        bindPreferenceSummaryToValue(findPreference(getString(R.string.user_email_key)));
//        bindPreferenceSummaryToValue(findPreference(getString(R.string.user_facebook_key_provided)));
        bindPreferenceSummaryToValue(findPreference(getString(R.string.user_linkedin_key)));
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Toolbar bar;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            LinearLayout root = (LinearLayout) findViewById(android.R.id.list).getParent().getParent().getParent();
            bar = (Toolbar) LayoutInflater.from(this).inflate(R.layout.setting_toolbar, root, false);
            root.addView(bar, 0); // insert at top
        } else {
            ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
            ListView content = (ListView) root.getChildAt(0);

            root.removeAllViews();

            bar = (Toolbar) LayoutInflater.from(this).inflate(R.layout.setting_toolbar, root, false);


            int height;
            TypedValue tv = new TypedValue();
            if (getTheme().resolveAttribute(R.attr.actionBarSize, tv, true)) {
                height = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
            } else {
                height = bar.getHeight();
            }

            content.setPadding(0, height, 0, 0);

            root.addView(content);
            root.addView(bar);
        }

        bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * Attaches a listener so the summary is always updated with the preference value.
     * Also fires the listener once, to initialize the summary (so it shows up before the value
     * is changed.)
     */
    private void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(this);

        // Trigger the listener immediately with the preference's
        // current value.
        if(preference.getKey().equals(getString(R.string.user_facebook_key_provided))) {
            onPreferenceChange(preference,
                    PreferenceManager
                            .getDefaultSharedPreferences(preference.getContext())
                            .getBoolean(preference.getKey(), false));
        }
        else{
            onPreferenceChange(preference,
                    PreferenceManager
                            .getDefaultSharedPreferences(preference.getContext())
                            .getString(preference.getKey(), ""));
            Log.d("Settings", preference.getKey());
        }
    }

    @Override
    public boolean onPreferenceChange(final Preference preference, Object value) {
        // get the updated value
       Log.d("Setting", preference.getKey());
        Log.d("Setting",value.toString());
        if(preference.getKey().equals(getString(R.string.user_facebook_key_provided))) {
            // get objectID

            final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor = prefs.edit();
            String objectId = prefs.getString(getString(R.string.user_id_key), "");

            if(value.toString().equals("true") &&  prefs.getString(getString(R.string.facebook_user_id),null) == null){
                onFblogin();
               // Log.d("ID", prefs.getString(getString(R.string.facebook_user_id), null));
            }
            else if(value.toString().equals("false")){
                editor.putString(getString(R.string.facebook_user_id),null).commit();
                ParseQuery<ParseObject> query = ParseQuery.getQuery(getString(R.string.test_parse_class_key));
                query.getInBackground(objectId, new GetCallback<ParseObject>() {
                    public void done(ParseObject user_profile, ParseException e) {
                        if (e == null) {
                            String facebookId = prefs.getString(getString(R.string.facebook_user_id), "");
                            Log.d("facebookId", facebookId);
                            if (facebookId == null) {
                                user_profile.put(getString(R.string.facebook_user_id), JSONObject.NULL);
                                user_profile.saveInBackground();
                            } else {
                                user_profile.put(getString(R.string.facebook_user_id), facebookId);
                                user_profile.saveInBackground();
                            }
                        }
                    }
                });
                //prof
            }


            // Retrieve the object by id and update
          /*  ParseQuery<ParseObject> query = ParseQuery.getQuery(getString(R.string.test_parse_class_key));
            query.getInBackground(objectId, new GetCallback<ParseObject>() {
                public void done(ParseObject user_profile, ParseException e) {
                    if (e == null) {
                        String facebookId = prefs.getString(getString(R.string.facebook_user_id),"");
                        Log.d("facebookId",facebookId);
                        if(facebookId == null)
                        {
                            user_profile.put(getString(R.string.facebook_user_id), JSONObject.NULL);
                            user_profile.saveInBackground();
                        }
                        else {
                            user_profile.put(getString(R.string.facebook_user_id), facebookId);
                            user_profile.saveInBackground();
                        }
                    }
                }
            });*/
            // change the file
            preference.setSummary("");
        }else {

            final String stringValue = value.toString();
            // get objectID
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            String objectId = prefs.getString(getString(R.string.user_id_key), "");

            // Retrieve the object by id and update
            ParseQuery<ParseObject> query = ParseQuery.getQuery(getString(R.string.test_parse_class_key));
            query.getInBackground(objectId, new GetCallback<ParseObject>() {
                public void done(ParseObject user_profile, ParseException e) {
                    if (e == null) {

                        user_profile.put(preference.getKey(), stringValue);
                        user_profile.saveInBackground();
                    }
                }
            });
            // change the file
            preference.setSummary(stringValue);
        }

        return true;
    }

    /*public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("user_facebook",null);
        if(checked){
            editor.putString("user_facebook",onFblogin());
        }
        else{
            editor.putString("user_facebook",null);
        }

        editor.commit();
    }*/

    private String onFblogin()
    {
        callbackmanager = CallbackManager.Factory.create();
        String str_id = ""; //id to be returned
        // Set permissions
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "user_photos", "public_profile"));

        LoginManager.getInstance().registerCallback(callbackmanager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {

                        System.out.println("Success");
                        GraphRequest.newMeRequest(
                                loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject json, GraphResponse response) {
                                        if (response.getError() != null) {
                                            // handle error
                                            System.out.println("ERROR");
                                        } else {
                                            System.out.println("Success");
                                            try {

                                                String jsonresult = String.valueOf(json);
                                                System.out.println("JSON Result" + jsonresult);

                                                String facebookId = json.getString("id");
                                                final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                                Log.d("Prefs",prefs.getString(getString(R.string.facebook_user_id),""));
                                                prefs.edit().putString(getString(R.string.facebook_user_id),facebookId).commit();
                                                Log.d("Prefs",prefs.getString(getString(R.string.facebook_user_id),""));
                                                //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                                //SharedPreferences.Editor editor = prefs.edit();
                                                //editor.putString(getString(R.string.facebook_user_id),str_id).commit();
                                                Log.d("Setting", facebookId);

                                                String objectId = prefs.getString(getString(R.string.user_id_key), "");
                                                ParseQuery<ParseObject> query = ParseQuery.getQuery(getString(R.string.test_parse_class_key));
                                                query.getInBackground(objectId, new GetCallback<ParseObject>() {
                                                    public void done(ParseObject user_profile, ParseException e) {
                                                        if (e == null) {
                                                            String facebookId = prefs.getString(getString(R.string.facebook_user_id), "");
                                                            Log.d("facebookId", facebookId);
                                                            if (facebookId == null) {
                                                                user_profile.put(getString(R.string.facebook_user_id), JSONObject.NULL);
                                                                user_profile.saveInBackground();
                                                            } else {
                                                                user_profile.put(getString(R.string.facebook_user_id), facebookId);
                                                                user_profile.saveInBackground();
                                                            }
                                                        }
                                                    }
                                                });
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }

                                }).executeAsync();

                    }

                    @Override
                    public void onCancel() {
                        Log.d("Cancel", "On cancel");
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.d("Error", error.toString());
                    }
                });



        return str_id;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackmanager.onActivityResult(requestCode, resultCode, data);
    }



}