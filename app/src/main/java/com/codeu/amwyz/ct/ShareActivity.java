package com.codeu.amwyz.ct;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.nio.charset.Charset;
import java.util.logging.Handler;

/**
 * Created by goodautumn on 8/3/2015.
 */
public class ShareActivity extends ActionBarActivity implements NfcAdapter.CreateNdefMessageCallback
        //, NfcAdapter.OnNdefPushCompleteCallback
{
    private String LOG_TAG = ShareActivity.class.getSimpleName();
    private NfcAdapter mNfcAdapter;
    SharedPreferences prefs;
    private Activity mActivity;
    NfcAdapter.CreateNdefMessageCallback callback;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_main);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        mActivity = this;
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        Button sendNFCButton = (Button) findViewById(R.id.share_NFC_button);
        Button generateQRButton = (Button) findViewById(R.id.share_QR_button);

        sendNFCButton.setOnClickListener(onShareNFCButtonClick);
        generateQRButton.setOnClickListener(onShareQRButtonClick);
//
//        mNfcAdapter.setOnNdefPushCompleteCallback(new NfcAdapter.OnNdefPushCompleteCallback() {
//            @Override
//            public void onNdefPushComplete(NfcEvent nfcEvent) {
//                Log.e(LOG_TAG, "push successful");
//            }
//        }, mActivity);

        callback = this;
    }

    private View.OnClickListener onShareNFCButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Send the contact info
            String userParseId = prefs.getString("user_id",null);
            //Create an Ndef record with a text mime type that contains the user parseId
            //as well as an AARecord to force our CT app to open on the receiving phone
           /* NdefMessage userInfo = new NdefMessage(new NdefRecord[] { NdefRecord.createMime("text*//*",userParseId.getBytes())
                  ,NdefRecord.createApplicationRecord("com.codeu.amwyz.ct")
            });*/
            mNfcAdapter.setNdefPushMessageCallback(callback, mActivity);
            //mNfcAdapter.enableForegroundNdefPush(mActivity, userInfo);
            //mNfcAdapter.setNdefPushMessage(userJSON,mActivity);

            Log.e(LOG_TAG, "ndef push started");
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

    @Override
    public NdefMessage createNdefMessage(NfcEvent nfcEvent) {
        //Send the contact info
        String userParseId = prefs.getString("user_id",null);
        //Create an Ndef record with a text mime type that contains the user parseId
        //as well as an AARecord to force our CT app to open on the receiving phone
        NdefMessage userInfo = new NdefMessage(new NdefRecord[] { NdefRecord.createMime("application/CT",userParseId.getBytes(Charset.forName("US-ASCII")))
              // ,NdefRecord.createApplicationRecord("com.codeu.amwyz.ct")
        });
        return userInfo;
    }

   /* @Override
    public void onNdefPushComplete(NfcEvent nfcEvent) {
        Log.e(LOG_TAG, "NFC push complete");
    }*/
}

