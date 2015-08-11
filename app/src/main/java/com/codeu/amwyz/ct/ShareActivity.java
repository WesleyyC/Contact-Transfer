package com.codeu.amwyz.ct;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

/**
 * Created by goodautumn on 8/3/2015.
 */
public class ShareActivity extends ActionBarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_main);

        Button sendNFCButton = (Button) findViewById(R.id.share_NFC_button);
        Button generateQRButton = (Button) findViewById(R.id.share_QR_button);

        sendNFCButton.setOnClickListener(onShareNFCButtonClick);
        generateQRButton.setOnClickListener(onShareQRButtonClick);

    }

    private View.OnClickListener onShareNFCButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            Intent addIntent = new Intent(getApplicationContext(), AddActivity.class);
//            startActivity(addIntent);
        }
    };
    private View.OnClickListener onShareQRButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent scanQrIntent = new Intent(getApplicationContext(), QrEncodeActivity.class);
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
