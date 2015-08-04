package com.codeu.amwyz.ct;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.codeu.amwyz.ct.data.ContactContract;
import com.codeu.amwyz.ct.sync.CTSyncAdapter;

/**
 * Created by Youyou on 7/30/2015.
 */
// Implmented with a Loader and cursor adapter
public class ContactsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    // Initalize the listView and the cursor adpater
    private ContactAdapter mContactsAdapter;
    ListView listView;
    // ID for loader
    private static final int CONTACT_LOADER = 0;

    // place holder
    public ContactsFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        // create a cursor adapter
        mContactsAdapter = new ContactAdapter(getActivity(),null,0);
        // inflate the fragment
        View rootView = inflater.inflate(R.layout.contacts_fragment, container, false);
        // bind the adapter to the list view
        listView = (ListView) rootView.findViewById(R.id.listview_contacts);
        listView.setAdapter(mContactsAdapter);

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
                null,
                null,
                null,
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mContactsAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mContactsAdapter.swapCursor(null);
    }

}
