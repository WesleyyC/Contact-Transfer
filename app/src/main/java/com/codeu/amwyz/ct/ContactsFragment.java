package com.codeu.amwyz.ct;


import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.codeu.amwyz.ct.data.ContactContract;
import com.codeu.amwyz.ct.sync.CTSyncAdapter;

/**
 * Created by Youyou on 7/30/2015.
 */
// Implmented with a Loader and cursor adapter
public class ContactsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    private static final String LOG_TAG = "ContactFragment";//ContactsFragment.class.getSimpleName();

    // Initalize the listView and the cursor adpater
    private ContactAdapter mContactAdapter;
    private ListView mListView;
    private int mPosition = ListView.INVALID_POSITION;

    // ID for loader
    private static final int CONTACT_LOADER = 0;
    private static final String[] CONTACT_COLUMNS = {
            ContactContract.ContactEntry.TABLE_NAME + "." + ContactContract.ContactEntry._ID,
            ContactContract.ContactEntry.COLUMN_USER_PARSE_ID,
            ContactContract.ContactEntry.COLUMN_USER_REAL_NAME,
            ContactContract.ContactEntry.COLUMN_USER_PHONE,
            ContactContract.ContactEntry.COLUMN_USER_LINKEDIN,
            ContactContract.ContactEntry.COLUMN_USER_FACEBOOK,
            ContactContract.ContactEntry.COLUMN_USER_EMAIL
    };

    static final int COLUMN_CONTACT_ID = 0;
    static final int COLUMN_USER_PARSE_ID = 1;
    static final int COLUMN_USER_REAL_NAME = 2;
    static final int COLUMN_USER_PHONE = 3;
    static final int COLUMN_USER_LINKEDIN = 4;
    static final int COLUMN_USER_FACEBOOK = 5;
    static final int COLUMN_USER_EMAIL = 6;

    // place holder
    public ContactsFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        // create a cursor adapter

        mContactAdapter = new ContactAdapter(getActivity(),null,0);
        // inflate the fragment
        View rootView = inflater.inflate(R.layout.contacts_fragment, container, false);
        // bind the adapter to the list view
        mListView = (ListView) rootView.findViewById(R.id.listview_contacts);
        mListView.setAdapter(mContactAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(LOG_TAG, position + ", " + id);
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                if(cursor != null){
                    Intent intent = new Intent(getActivity(), DetailContacts.class)
                            .setData(ContactContract.ContactEntry.buildContactUri(id));
                    startActivity(intent);
                }
            }
        });
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // Init a loader
        getLoaderManager().initLoader(CONTACT_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    // A update function for update contacts
    public void updateContact() {
        CTSyncAdapter.syncImmediately(getActivity());
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Sort order:  Ascending, by name.
        String sortOrder = ContactContract.ContactEntry.COLUMN_USER_REAL_NAME + " ASC";
        // Get the content uri
        Uri contactUri = ContactContract.ContactEntry.CONTENT_URI;
        // bind the loader to the content provider
        return new CursorLoader(getActivity(),
                contactUri,
                CONTACT_COLUMNS,
                null,
                null,
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mContactAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mContactAdapter.swapCursor(null);
    }


}
