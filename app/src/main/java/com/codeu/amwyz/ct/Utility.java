package com.codeu.amwyz.ct;

import android.content.ContentValues;

import com.codeu.amwyz.ct.data.ContactContract;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by wesley on 8/3/15.
 */
public class Utility {

    // contacts_list_setup
    public static final String[] TEST_CONTACT_ARRAY={"Xhgsv7u7pW","kexbIzRtjc","sma0eYdYTL","E8OetEbIbi","haWykmSo9s"};
    public static final List<String> TEST_CONTACT_LIST= Arrays.asList(TEST_CONTACT_ARRAY);
    public static final Set<String> TEST_CONTACT_SET = new HashSet<>(TEST_CONTACT_LIST);

    public static ContentValues createContactValues(String user_parse_id, String user_real_name, String user_phone, String user_email, String user_facebook, String user_linkedin) {
        // Create a new map of values, where column names are the keys
        ContentValues testValues = new ContentValues();
        testValues.put(ContactContract.ContactEntry.COLUMN_USER_PARSE_ID, user_parse_id);
        testValues.put(ContactContract.ContactEntry.COLUMN_USER_REAL_NAME, user_real_name);
        testValues.put(ContactContract.ContactEntry.COLUMN_USER_PHONE,user_phone);
        testValues.put(ContactContract.ContactEntry.COLUMN_USER_EMAIL,user_email);
        testValues.put(ContactContract.ContactEntry.COLUMN_USER_FACEBOOK,user_facebook);
        testValues.put(ContactContract.ContactEntry.COLUMN_USER_LINKEDIN,user_linkedin);
        return testValues;
    }
}
