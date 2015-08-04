package com.codeu.amwyz.ct;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.codeu.amwyz.ct.sync.CTSyncAdapter;

/**
 * Created by Youyou on 7/30/2015.
 */
public class ContactsActivity extends ActionBarActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts_main);

        ContactsFragment contactsFragment =  new ContactsFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.contacts_container,contactsFragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if(id == R.id.action_settings){
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }else if(id==R.id.action_refresh){
            CTSyncAdapter.syncImmediately(getApplicationContext());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
