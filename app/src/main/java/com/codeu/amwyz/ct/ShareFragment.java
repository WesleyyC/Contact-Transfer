package com.codeu.amwyz.ct;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.codeu.amwyz.ct.R;

/**
 * Created by Youyou on 8/3/2015.
 * contains the buttons to access the QR and NFC page
 */
public class ShareFragment extends Fragment {
    private FragmentActivity mContext;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.share_fragment, container, false);

        return view;
    }

    @Override
    public void onAttach(Activity activity){
        mContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }
}
