package com.codeu.amwyz.ct;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

/**
 * Created by t-mavac on 8/4/2015.
 */
public class NFCButtonFragment extends Fragment {
    private NfcAdapter nfcAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.nfc_button_fragment, container, false);
        Button shareButton = (Button) view.findViewById(R.id.button);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testSendFile(v);
            }
        });
        return view;

    }

    public void testSendFile(View view) {
        nfcAdapter = NfcAdapter.getDefaultAdapter(this.getActivity());

        // Check whether NFC is enabled on device
        if(!nfcAdapter.isEnabled()){
            // NFC is disabled, show the settings UI
            // to enable NFC
            Toast.makeText(this.getActivity(), "Please enable NFC.",
                    Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Settings.ACTION_NFC_SETTINGS));
        }
        // Check whether Android Beam feature is enabled on device
        else if (!nfcAdapter.isNdefPushEnabled()) {
            // Android Beam is disabled, show the settings UI
            // to enable Android Beam
            Toast.makeText(this.getActivity(), "Please enable Android Beam.",
                    Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Settings.ACTION_NFCSHARING_SETTINGS));
        } else {
            // NFC and Android Beam both are enabled

            // File to be transferred
            //for current testing purposes this is just the icon for the app
            String fileName = getResources().getResourceName(R.mipmap.ic_launcher);

            // Create a new file using the specified directory and name
            try {
                File fileToTransfer = new File("/sdcard/testFile.txt");
                fileToTransfer.createNewFile();
                fileToTransfer.setReadable(true, false);
                FileOutputStream fOut = new FileOutputStream(fileToTransfer);
                OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
                myOutWriter.append("test");
                myOutWriter.close();
                fOut.close();
                nfcAdapter.setBeamPushUris(
                        new Uri[]{Uri.fromFile(fileToTransfer)}, this.getActivity());
            } catch (Exception e) {
                Log.e("ERRR", "Could not create file", e);
            }

        }
    }
}
