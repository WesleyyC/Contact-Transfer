package com.codeu.amwyz.ct;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by t-mavac on 8/4/2015.
 */
public class NFCButtonFragment extends Fragment {
    private String LOG_TAG = NFCButtonFragment.class.getSimpleName();
    private NfcAdapter mNfcAdapter;
    SharedPreferences prefs;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.nfc_button_fragment, container, false);
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this.getActivity());
        Button shareButton = (Button) view.findViewById(R.id.button);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendContactInfo(v);
            }
        });
        return view;

    }

    public void sendContactInfo(View view) {
        // Check whether NFC is enabled on device
        if(!mNfcAdapter.isEnabled()){
            // NFC is disabled, show the settings UI
            // to enable NFC
            Toast.makeText(this.getActivity(), "Please enable NFC.",
                    Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Settings.ACTION_NFC_SETTINGS));
        }
        // Check whether Android Beam feature is enabled on device
        else if (!mNfcAdapter.isNdefPushEnabled()) {
            // Android Beam is disabled, show the settings UI
            // to enable Android Beam
            Toast.makeText(this.getActivity(), "Please enable Android Beam.",
                    Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Settings.ACTION_NFCSHARING_SETTINGS));
        } else {
            // NFC and Android Beam both are enabled

            String userParseId = prefs.getString("user_id",null);
            //Create an Ndef record with a text mime type that contains the user parseId
            //as well as an AARecord to force our CT app to open on the receiving phone
            NdefMessage userJSON = new NdefMessage(new NdefRecord[] { NdefRecord.createMime("text/*",userParseId.getBytes()),
                    NdefRecord.createApplicationRecord("com.codeu.amwyz.ct")});

            mNfcAdapter.setNdefPushMessage(userJSON, this.getActivity());
            Log.e(LOG_TAG, "ndef pushed");
            // Create a new file using the specified directory and name
//            try {
//                File fileToTransfer = new File("/sdcard/testFile.txt");
//                fileToTransfer.createNewFile();
//                fileToTransfer.setReadable(true, false);
//                FileOutputStream fOut = new FileOutputStream(fileToTransfer);
//                OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
//                myOutWriter.append("test");
//                myOutWriter.close();
//                fOut.close();
//                mNfcAdapter.setBeamPushUris(
//                        new Uri[]{Uri.fromFile(fileToTransfer)}, this.getActivity());
//            } catch (Exception e) {
//                Log.e("ERRR", "Could not create file", e);
//            }

        }
    }
}
