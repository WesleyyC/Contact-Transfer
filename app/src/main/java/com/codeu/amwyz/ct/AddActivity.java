package com.codeu.amwyz.ct;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

/**
 * Created by goodautumn on 8/3/2015.
 */
public class AddActivity extends ActionBarActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_main);


        Button receiveNFCButton = (Button) findViewById(R.id.add_NFC_button);
        Button scanQRButton = (Button) findViewById(R.id.add_QR_button);

        receiveNFCButton.setOnClickListener(onAddNFCButtonClick);
        scanQRButton.setOnClickListener(onAddQRButtonClick);
    }
    private View.OnClickListener onAddNFCButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            Intent addIntent = new Intent(getApplicationContext(), AddActivity.class);
//            startActivity(addIntent);
        }
    };
    private View.OnClickListener onAddQRButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent scanQrIntent = new Intent(getApplicationContext(), QrScanActivity.class);
            startActivity(scanQrIntent);
        }
    };

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

}
