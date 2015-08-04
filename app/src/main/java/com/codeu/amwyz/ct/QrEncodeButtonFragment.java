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
public class QrEncodeButtonFragment extends Fragment{
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.qr_encode_button_fragment, container, false);

        Button qrEncodeButton = (Button) view.findViewById(R.id.qr_encode_button);
        qrEncodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent qrEncodeIntent = new Intent(getActivity(), QrEncodeActivity.class);
                startActivity(qrEncodeIntent);
            }
        });
        return view;
    }
}
