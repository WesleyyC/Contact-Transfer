
package com.codeu.amwyz.ct;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;


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
        bindPreferenceSummaryToValue(findPreference(getString(R.string.user_facebook_key)));
        bindPreferenceSummaryToValue(findPreference(getString(R.string.user_linkedin_key)));
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
}