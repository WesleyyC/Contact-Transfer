package com.codeu.amwyz.ct;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

/**
 * Created by goodautumn on 8/3/2015.
 */
public class ShareActivity extends ActionBarActivity {
    String LOG_TAG = ShareActivity.class.getSimpleName();
    FragmentManager fragmentManager = getFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    NFCButtonFragment nfcButtonFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_main);


        QrScanButtonFragment qrScanButtonFragment = new QrScanButtonFragment();
        nfcButtonFragment = new NFCButtonFragment();
        fragmentTransaction.add(R.id.bottom_share_fragment_container, qrScanButtonFragment);
        fragmentTransaction.add(R.id.top_share_fragment_container, nfcButtonFragment);
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

            case R.id.qr_scan_button:{
                Intent qrScanIntent = new Intent(this, QrScanActivity.class);
                startActivity(qrScanIntent);
                break;
            }
            case R.id.nfc_button: {
                Log.e(LOG_TAG, "nfc button pushed");
                nfcButtonFragment.sendContactInfo(view);
            }
        }
    }
}
