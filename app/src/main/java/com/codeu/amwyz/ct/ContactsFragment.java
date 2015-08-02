package com.codeu.amwyz.ct;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Youyou on 7/30/2015.
 */
public class ContactsFragment extends Fragment{

    //using an array adapter as a place holder
    private ArrayAdapter<String> mContactsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        //Dummy Data
        String[] data = {
                "Alice - ###-###-####, alice@gmail.com",
                "Bob - ###-###-####, bob@gmail.com",
                "Carol - ###-###-####, carol@gmail.com"
        };
        List<String> contactsData = new ArrayList<String>(Arrays.asList(data));

        mContactsAdapter = new ArrayAdapter<String>(
                getActivity(),
                R.layout.list_item_contacts,
                R.id.list_item_contacts_textview,
                contactsData
        );

        View view = inflater.inflate(R.layout.contacts_fragment, container, false);

        ListView listView = (ListView) view.findViewById(R.id.listview_contacts);
        listView.setAdapter(mContactsAdapter);

        return view;
    }
}
