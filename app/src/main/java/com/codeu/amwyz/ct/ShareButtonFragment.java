package com.codeu.amwyz.ct;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Youyou on 8/1/2015.
 */
public class ShareButtonFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.share_button_fragment, container, false);
        Button shareButton = (Button) view.findViewById(R.id.share_button);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(getActivity(), ShareActivity.class);
                startActivity(shareIntent);
            }
        });
        return view;
    }
}
