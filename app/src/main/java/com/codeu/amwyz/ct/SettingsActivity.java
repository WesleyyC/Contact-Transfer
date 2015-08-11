
package com.codeu.amwyz.ct;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.facebook.AccessToken;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Arrays;


public class SettingsActivity extends PreferenceActivity
        implements Preference.OnPreferenceChangeListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add 'general' preferences, defined in the XML file
        addPreferencesFromResource(R.xml.pref_general);

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
        onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    @Override
    public boolean onPreferenceChange(final Preference preference, Object value) {
        // get the updated value
        final String stringValue = value.toString();
        // get objectID

        if (preference.getKey().equals(R.string.user_facebook_key_provided)) {
            CheckBoxPreference pref = (CheckBoxPreference) preference;
            SharedPreferences sharedPreferences = this.getPreferences(MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            if (pref.isChecked()) {
                editor.putString("facebook_user_id",facebookLogin());
                Utility.facebookIntent(this, facebookLogin());
            }
            else{
                if(sharedPreferences.contains("facebook_user_id"))
                    editor.remove("facebook_user_id");
            }
        }
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
        return true;
    }

    private String facebookLogin() {
        LoginManager loginManager = LoginManager.getInstance();
        if(!isLoggedIn()){
            loginManager.logInWithReadPermissions(
                    this,
                    Arrays.asList("public_profile"));

            Profile profile = Profile.getCurrentProfile();
            return profile.getId();
        }
        else{
            return null;
        }
    }

    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }



}