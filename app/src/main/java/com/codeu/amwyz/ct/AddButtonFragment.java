package com.codeu.amwyz.ct;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Youyou on 8/1/2015.
 * Clicking on the add button will hade the share button
 */
public class AddButtonFragment extends Fragment {
    private FragmentActivity mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.add_button_fragment, container, false);

        Button addButton = (Button) view.findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = mContext.getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                ShareFragment shareFragment = new ShareFragment();
                fragmentTransaction.replace(R.id.bottom_main_fragment_container, shareFragment);
                fragmentTransaction.hide(fragmentManager.findFragmentById(R.id.top_right_main_fragment_container));
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        return view;
    }

    @Override
    public void onAttach(Activity activity){
        mContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }
}
