package com.codeu.amwyz.ct;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.codeu.amwyz.ct.sync.CTSyncAdapter;

/**
 * Created by Youyou on 7/30/2015.
 */
public class ContactsActivity extends ActionBarActivity{

    private static final String LOG_TAG = ContactsActivity.class.getSimpleName();


    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts_main);

        // go to the Contact Fragment
        // Fragment class is v4
        ContactsFragment contactsFragment =  new ContactsFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.contacts_container,contactsFragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(LOG_TAG,"In Resume");
        ContactsFragment df = (ContactsFragment)getSupportFragmentManager().findFragmentById(R.id.contacts_container);
        if ( null != df ) {
            df.updateContact();
        }
    }

//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        Log.d(LOG_TAG, "In Restart");
//        ContactsFragment df = (ContactsFragment)getSupportFragmentManager().findFragmentById(R.id.contacts_container);
//        if ( null != df ) {
//            df.updateContact();
//        }
//    }

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
