package com.codeu.amwyz.ct;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by goodautumn on 8/3/2015.
 */
public class QrScanButtonFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState){
            View view = inflater.inflate(R.layout.qr_scan_button_fragment, container, false);

            Button qrScanButton = (Button) view.findViewById(R.id.qr_scan_button);
            qrScanButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent qrScanIntent = new Intent(getActivity(), QrScanActivity.class);
                    startActivity(qrScanIntent);
                }
            });
            return view;
        }

}
