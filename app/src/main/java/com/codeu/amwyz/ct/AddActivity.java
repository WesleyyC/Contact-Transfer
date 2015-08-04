package com.codeu.amwyz.ct;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

/**
 * Created by goodautumn on 8/3/2015.
 */
public class AddActivity extends ActionBarActivity{
    FragmentManager fragmentManager = getFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_main);


        QrEncodeButtonFragment qrEncodeButtonFragment = new QrEncodeButtonFragment();
        fragmentTransaction.add(R.id.bottom_add_fragment_container, qrEncodeButtonFragment);
        fragmentTransaction.commit();
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
        }

        return super.onOptionsItemSelected(item);
    }


    public void onClick(View view) {
        switch(view.getId()){

            case R.id.qr_encode_button:{
                Intent qrEncodeIntent = new Intent(this, QrEncodeActivity.class);
                startActivity(qrEncodeIntent);
                break;
            }
        }
    }
}
