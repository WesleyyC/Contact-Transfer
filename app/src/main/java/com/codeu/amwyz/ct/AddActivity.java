package com.codeu.amwyz.ct;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.content.IntentFilter;
import android.widget.Toast;
import android.widget.Button;

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


Button receiveNFCButton = (Button) findViewById(R.id.add_NFC_button);
        Button scanQRButton = (Button) findViewById(R.id.add_QR_button);

        receiveNFCButton.setOnClickListener(onAddNFCButtonClick);
        scanQRButton.setOnClickListener(onAddQRButtonClick);

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        mNfcPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
                getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TOP), 0);


        IntentFilter nfcIntent = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        nfcIntent.addDataScheme("text/plain");
        mNdefExchangeFilters = new IntentFilter[] { nfcIntent };

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


    
    @Override
    protected void onResume() {
        super.onResume();
//        mResumed = true;
        // Sticky notes received from Android
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            NdefMessage[] messages = getNdefMessages(getIntent());
            byte[] payload = messages[0].getRecords()[0].getPayload();
//            setNoteBody(new String(payload));
            //add the contact by getting the parseId from the Ndef message
            Utility.addContacts(this,payload.toString());
            setIntent(new Intent()); // Consume this intent.
        }
//        enableNdefExchangeMode();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        mResumed = false;
        mNfcAdapter.disableForegroundNdefPush(this);
    }

    //if the onResume method ends up not actually adding the contact,
    //might need to add the contact from here...?
//    @Override
//    protected void onNewIntent(Intent intent) {
//        // NDEF exchange mode
//        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
//            NdefMessage[] msgs = getNdefMessages(intent);
//            //promptForContent(msgs[0]);
////            Utility.addContacts(this, );
//        }
//    }

    public NdefMessage[] getNdefMessages(Intent intent) {
        NdefMessage[] messages;
        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(mNfcAdapter.EXTRA_NDEF_MESSAGES);
        if (rawMsgs != null) {
            messages = new NdefMessage[rawMsgs.length];
            for (int i = 0; i < rawMsgs.length; i++) {
                messages[i] = (NdefMessage) rawMsgs[i];
            }
            return messages;
        }
        else if (rawMsgs == null){
            Toast.makeText(getApplicationContext(), "No NDEF Message Read", Toast.LENGTH_LONG).show();
        }
        return null;
    }
}
