package com.codeu.amwyz.ct;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codeu.amwyz.ct.data.ContactContract;

/**
 * Created by Youyou on 7/30/2015.
 */
public class DetailContactsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final String LOG_TAG = DetailContactsFragment.class.getSimpleName();
    static final String DETAIL_URI = "URI";

    private Uri mUri;

    // ID for loader
    private static final int DETAIL_LOADER = 0;
    private static final String[] DETAIL_COLUMNS = {
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

    private ImageView mIconView;
    private TextView mNameView;
    private TextView mParseIdView;
    private TextView mPhoneView;
    private TextView mEmailView;
    private TextView mFacebookView;
    private TextView mLinkedinView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        Bundle arguments = getArguments();
        if(arguments != null){
            mUri = arguments.getParcelable(DetailContactsFragment.DETAIL_URI);
            if(mUri == null) {
                Log.d(LOG_TAG, "mUri is null");
            }
        }

        //mContactAdapter = new ContactAdapter(getActivity(), null, 0);
        View rootView = inflater.inflate(R.layout.detail_fragment, container, false);

        mIconView = (ImageView) rootView.findViewById(R.id.contact_icon);
        mNameView = (TextView) rootView.findViewById(R.id.contact_real_name);
        mParseIdView = (TextView) rootView.findViewById(R.id.contact_parseId);
        mPhoneView = (TextView) rootView.findViewById(R.id.contact_phone);
        mEmailView = (TextView) rootView.findViewById(R.id.contact_email);
        mFacebookView = (TextView) rootView.findViewById(R.id.contact_facebook);
        mLinkedinView = (TextView) rootView.findViewById(R.id.contact_linkedin);
        Log.d(LOG_TAG, "onCreateView");
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        mUri = ContactContract.ContactEntry.CONTENT_URI;
        return new CursorLoader(
                getActivity(),
                mUri,
                DETAIL_COLUMNS,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(data != null && data.getCount()>0){
            data.moveToFirst();

            //default to the app icon
            mIconView.setImageResource(R.mipmap.ic_launcher);

            //read cursor to get name
            String name = data.getString(COLUMN_USER_REAL_NAME);
            mNameView.setText(name);
            Log.d(LOG_TAG, "onLoadFinished " + name);

            //read cursor to get the parse id
            String parseId = data.getString(COLUMN_USER_PARSE_ID);
            mParseIdView.setText(parseId);

            //read cursor to get the phone number
            String phone = data.getString(COLUMN_USER_PHONE);
            mPhoneView.setText(phone);
            Log.d(LOG_TAG, "onLoadFinished " + phone);

            //read the email from the cursor and update
            String email = data.getString(COLUMN_USER_EMAIL);
            mEmailView.setText(email);

            //read to get the facebook identification
            String facebook = data.getString(COLUMN_USER_FACEBOOK);
            mFacebookView.setText(facebook);

            //read to get the linkedIn identification
            String linkedin = data.getString(COLUMN_USER_LINKEDIN);
            mLinkedinView.setText(linkedin);

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        getLoaderManager().restartLoader(DETAIL_LOADER, null, this);
        Log.d(LOG_TAG, "onLoaderReset");
    }

    public void onDetailContactsClick(View v){
        switch(v.getId()){
            case R.id.export_button:
                break;
            case R.id.delete_button:
                break;
        }
    }
}
