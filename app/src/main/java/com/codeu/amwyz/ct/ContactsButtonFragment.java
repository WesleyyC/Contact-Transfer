package com.codeu.amwyz.ct;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Youyou on 8/2/2015.
 */
public class ContactsButtonFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.contacts_button_fragment, container, false);

        Button contactsButton = (Button) view.findViewById(R.id.contacts_button);
        contactsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent contactsIntent = new Intent(getActivity(), ContactsActivity.class);
                startActivity(contactsIntent);
            }
        });
        return view;
    }
}
