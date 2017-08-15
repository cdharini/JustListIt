package com.codepath.cdharini.justlistit;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by dharinic on 8/13/17.
 */

public class JustListItCursorAdapter extends CursorAdapter{

    private static final String KEY_TODOITEM = "item";

    private static final String KEY_PRIORITY = "priority";
        public JustListItCursorAdapter(Context context, Cursor cursor) {
            super(context, cursor, 0);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return LayoutInflater.from(context).inflate(R.layout.item_todo, parent, false);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            // Find fields to populate in inflated template
            TextView tvBody = (TextView) view.findViewById(R.id.tvItem);
            TextView tvPriority = (TextView) view.findViewById(R.id.tvPriority);
            // Extract properties from cursor
            String body = cursor.getString(cursor.getColumnIndexOrThrow(KEY_TODOITEM));
            String priority = cursor.getString(cursor.getColumnIndexOrThrow(KEY_PRIORITY));
            // Populate fields with extracted properties
            tvBody.setText(body);
            tvPriority.setText(priority);
            tvPriority.setTextColor(ContextCompat.getColor(context, getPriorityColor(priority)));
        }

        private int getPriorityColor(String p) {
            int color = Color.BLACK;
            switch(p) {
                case "HIGH":
                    color = R.color.red;
                    break;
                case "MEDIUM":
                    color = R.color.orange;
                    break;
                case "LOW":
                    color = R.color.green;
                    break;
            }
            return color;
        }
    }

