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
public class ContactsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

//    //using an array adapter as a place holder
//    private ArrayAdapter<String> mContactsAdapter;

    private ContactAdapter mContactsAdapter;
    ListView listView;

    private static final int CONTACT_LOADER = 0;

    public ContactsFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
//        //Dummy Data
//        String[] data = {
//                "Alice - ###-###-####, alice@gmail.com",
//                "Bob - ###-###-####, bob@gmail.com",
//                "Carol - ###-###-####, carol@gmail.com"
//        };
//        List<String> contactsData = new ArrayList<String>(Arrays.asList(data));
//
//        mContactsAdapter = new ArrayAdapter<String>(
//                getActivity(),
//                R.layout.list_item_contacts,
//                R.id.list_item_contacts_textview,
//                contactsData
//        );
//
        mContactsAdapter = new ContactAdapter(getActivity(),null,0);


        View rootView = inflater.inflate(R.layout.contacts_fragment, container, false);

        listView = (ListView) rootView.findViewById(R.id.listview_contacts);
        listView.setAdapter(mContactsAdapter);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(CONTACT_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    public void updateContact() {
        CTSyncAdapter.syncImmediately(getActivity());
    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // This is called when a new Loader needs to be created.  This
        // fragment only uses one loader, so we don't care about checking the id.

        // To only show current and future dates, filter the query to return weather only for
        // dates after or including today.

        // Sort order:  Ascending, by date.
        String sortOrder = ContactContract.ContactEntry.COLUMN_USER_REAL_NAME + " ASC";

        Uri contactUri = ContactContract.ContactEntry.CONTENT_URI;

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
