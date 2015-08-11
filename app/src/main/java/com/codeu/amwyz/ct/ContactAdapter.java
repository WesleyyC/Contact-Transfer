package com.codeu.amwyz.ct;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.provider.ContactsContract;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.codeu.amwyz.ct.data.ContactContract;


public class ContactAdapter extends CursorAdapter {
    private final String LOG_TAG = "ContactAdaper";
    private Context mContext;

    //private View viewLayout;
    // these indices for cursor indexes
    private static final int COLUMN_USER_ID = 0;
    private static final int COLUMN_USER_PARSE_ID = 1;
    private static final int COLUMN_USER_REAL_NAME = 2;
    private static final int COLUMN_USER_PHONE = 3;
    private static final int COLUMN_USER_LINKEDIN = 4;
    private static final int COLUMN_USER_FACEBOOK = 5;
    private static final int COLUMN_USER_EMAIL = 6;

    // view holder for getting access to the layout view
    public static class ViewHolder {
        // Currently only set the image, name, and import button.
        // need to update with dynamic buttons for facebook and linkedIn
        public final ImageView iconButton;
        public final TextView nameView;
        public final Button facebookButton;
        public final Button linkedinButton;
        public final Button exportButton;
        public final ListView listView;

        public ViewHolder(View view) {
            iconButton = (ImageView) view.findViewById(R.id.list_item_icon_button);
            nameView = (TextView) view.findViewById(R.id.list_item_name_textview);
            facebookButton = (Button) view.findViewById(R.id.list_item_fb_button);
            linkedinButton = (Button) view.findViewById(R.id.list_item_li_button);
            exportButton = (Button) view.findViewById(R.id.list_item_export_button);
            listView = (ListView) view.findViewById(R.id.listview_contacts);
        }
    }

    public ContactAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        this.mContext = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Choose the layout type
        int layoutId = R.layout.list_item_contacts;
        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        viewHolder.iconButton.setOnClickListener(onIconClickListener);
        viewHolder.nameView.setOnClickListener(onNameClickListener);
        viewHolder.exportButton.setOnClickListener(onExportClickListener);

        view.setTag(viewHolder);

        return view;
    }

    private View.OnClickListener onIconClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ListView listView = (ListView) v.getParent().getParent().getParent();
            final int position = listView.getPositionForView((View)v.getParent());
            Log.d(LOG_TAG, "Icon clicked, row" + position);

            Cursor cursor = (Cursor) listView.getItemAtPosition(position);
            if(cursor != null ){
                Intent intent = new Intent(mContext, DetailContacts.class)
                        .setData(ContactContract.ContactEntry.buildContactUri(
                                cursor.getLong(cursor.getColumnIndex("_id")))); //cursor id
                mContext.startActivity(intent);
            }
        }
    };

    private View.OnClickListener onNameClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ListView listView = (ListView) v.getParent().getParent();
            final int position = listView.getPositionForView((View)v.getParent());
            Log.d(LOG_TAG, "Name clicked, row" + position);

            Cursor cursor = (Cursor) listView.getItemAtPosition(position);
            if(cursor != null){
                Intent intent = new Intent(mContext, DetailContacts.class)
                        .setData(ContactContract.ContactEntry.buildContactUri(
                                cursor.getLong(cursor.getColumnIndex("_id")))); //cursor id
                mContext.startActivity(intent);
            }
        }
    };

    private View.OnClickListener onExportClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ListView listView = (ListView) v.getParent().getParent().getParent();
            final int position = listView.getPositionForView((View)v.getParent());
            Cursor cursor = (Cursor) listView.getItemAtPosition(position);
            Log.d(LOG_TAG, "Export clicked, row" + position);

            Intent contactIntent = new Intent(ContactsContract.Intents.Insert.ACTION);
            //set the MIME type to match the Contacts Provider
            contactIntent.setType(ContactsContract.RawContacts.CONTENT_TYPE);

            //inserts new data into the intent
            contactIntent.putExtra(ContactsContract.Intents.Insert.NAME, cursor.getString(COLUMN_USER_REAL_NAME)) //name
                    .putExtra(ContactsContract.Intents.Insert.EMAIL, cursor.getString(COLUMN_USER_EMAIL)) //email
                    .putExtra(ContactsContract.Intents.Insert.EMAIL_TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK) //email type defaulting to work
                    .putExtra(ContactsContract.Intents.Insert.PHONE, cursor.getString(COLUMN_USER_PHONE))
                    .putExtra(ContactsContract.Intents.Insert.PHONE_TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);//defaulting to mobile, as an extra could be changed
            mContext.startActivity(contactIntent);
        }
    };

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // get a view holder
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        String parseID = cursor.getString(COLUMN_USER_PARSE_ID).substring(2);
        Bitmap userBitmap = Utility.getProfilePicture(context, parseID);
        if(userBitmap==null){
            viewHolder.iconButton.setImageResource(R.mipmap.ic_launcher);
        }else{
            viewHolder.iconButton.setImageBitmap(userBitmap);
        }

        // construct the contact link
        String nameStr = cursor.getString(COLUMN_USER_REAL_NAME);
        // set the text
        viewHolder.nameView.setText(nameStr);

        if(cursor.getString(COLUMN_USER_FACEBOOK) == null){
            //The contact does not have a facebook
            viewHolder.facebookButton.setEnabled(false);
            //the other option is View.INVISIBLE which leaves a space for where the
            //button should have been
        }

        if(cursor.getString(COLUMN_USER_LINKEDIN) == null){
            //contact does not have a linked in
            viewHolder.linkedinButton.setEnabled(false);
        }

    }

}