package com.codeu.amwyz.ct;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.codeu.amwyz.ct.sync.CTSyncAdapter;

/**
 * Created by Youyou on 7/30/2015.
 */
public class ContactsActivity extends ActionBarActivity{

    private static final String LOG_TAG = ContactsActivity.class.getSimpleName();
    private static final String DETAIL_CONTACTS_FRAGMENT_TAG = "DCFTAG";

    private boolean mTwoPane;

    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts_main);

        if (findViewById(R.id.contacts_detail_container) != null) {
            //the detail container view should only be present on the large screen layout.
            mTwoPane = true;

            if(savedInstanceState == null){
                ContactsFragment contactsFragment =  new ContactsFragment();
                fragmentTransaction.add(R.id.contacts_container,contactsFragment, DETAIL_CONTACTS_FRAGMENT_TAG)
                        .commit();
            }
        }
        else{
            mTwoPane = false;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_contacts, menu);
        return true;
    }

    @Override
    // update the loader and sync data when we go back from the delete
    protected void onResume() {
        super.onResume();
        ContactsFragment df = (ContactsFragment)getSupportFragmentManager().findFragmentById(R.id.contacts_container);
        if ( null != df ) {
            df.updateContact();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if(id == R.id.action_settings){
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }else if(id==R.id.action_refresh){
            // every time we refresh, we perform a sync
            CTSyncAdapter.syncImmediately(getApplicationContext());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
