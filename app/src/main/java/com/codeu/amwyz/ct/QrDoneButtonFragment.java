package com.codeu.amwyz.ct;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by goodautumn on 8/4/2015.
 */
public class QrDoneButtonFragment extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.qr_done_button_fragment, container, false);

        Button qrDoneButton = (Button) view.findViewById(R.id.qr_done_button);
        qrDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnToHomeIntent = new Intent(getActivity(), MainActivity.class);
                startActivity(returnToHomeIntent);
            }
        });
        return view;
    }
}
