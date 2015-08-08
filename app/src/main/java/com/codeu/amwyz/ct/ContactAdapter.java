package com.codeu.amwyz.ct;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
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
        // Currently only set the image, name, and import button.
        // need to update with dynamic buttons for facebook and linkedIn
        //public final ImageButton iconButton;
        public final TextView nameView;
        //public final Button exportButton;

        public ViewHolder(View view) {
            //todo: change
            //descriptionView = (TextView) view.findViewById(R.id.list_item_contacts_textview);
            //iconButton = (ImageButton) view.findViewById(R.id.list_item_icon_button);
            nameView = (TextView) view.findViewById(R.id.list_item_name_textview);
            //exportButton = (Button) view.findViewById(R.id.list_item_export_button);
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

        //viewHolder.iconView

        // construct the contact link
        String nameStr = cursor.getString(COLUMN_USER_REAL_NAME);
        // set the text
        viewHolder.nameView.setText(nameStr);
    }

}