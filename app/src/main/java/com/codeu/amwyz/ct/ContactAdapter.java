package com.codeu.amwyz.ct;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class ContactAdapter extends CursorAdapter {

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
        // currently we only have a description text view
        public final TextView descriptionView;
        public ViewHolder(View view) {
            descriptionView = (TextView) view.findViewById(R.id.list_item_contacts_textview);
        }
    }

    public ContactAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Choose the layout type
        int layoutId = R.layout.list_item_contacts;
        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // get a view holder
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        // construct the contact link
        String contactStr = cursor.getString(COLUMN_USER_REAL_NAME)+"-"+cursor.getString(COLUMN_USER_PHONE)+"-"+cursor.getString(COLUMN_USER_EMAIL);
        // set the text
        viewHolder.descriptionView.setText(contactStr);

    }

}