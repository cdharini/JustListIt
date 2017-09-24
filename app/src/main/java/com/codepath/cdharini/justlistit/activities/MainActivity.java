package com.codepath.cdharini.justlistit.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.codepath.cdharini.justlistit.adapters.JustListItCursorAdapter;
import com.codepath.cdharini.justlistit.R;
import com.codepath.cdharini.justlistit.model.ToDoDatabaseHelper;

public class MainActivity extends AppCompatActivity {

    private ListView mListView;

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String EXTRA_ITEM_ID = "dbId";
    private static final int REQUEST_CODE = 10;

    ToDoDatabaseHelper dBHelper;
    private JustListItCursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = (ListView) findViewById(R.id.lvToDoItems);
        setupListViewListener();

        dBHelper = ToDoDatabaseHelper.getInstance(MainActivity.this);
        mCursorAdapter = new JustListItCursorAdapter(MainActivity.this, dBHelper.getCursorSortByPriority());
        mListView.setAdapter(mCursorAdapter);
    }

    public void onAddItem(View view) {
        Intent addActivityIntent = new Intent(MainActivity.this, AddItemActivity.class);
        startActivityForResult(addActivityIntent, REQUEST_CODE);
    }

    private void setupListViewListener() {
        /*
         * Delete item on Long click with a confirmation dialog
         */
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> adapter, View v, int pos, long id) {
                final long dbId = id;
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setTitle(R.string.delete);
                alert.setMessage(R.string.deleteConfirmation);
                alert.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        removeItem(dbId);
                        dialog.dismiss();
                    }
                });

                alert.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                alert.show();
                return true;
            }
        });
        /*
         * Open edit activity on click
         */
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapter, View v, int pos, long id) {
                Intent editActivityIntent = new Intent(MainActivity.this, EditItemActivity.class);
                editActivityIntent.putExtra(EXTRA_ITEM_ID, id);
                startActivityForResult(editActivityIntent, REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCursorAdapter.changeCursor(dBHelper.getCursorSortByPriority());
    }

    void removeItem(long id) {
        dBHelper.deleteItem(id);
        mCursorAdapter.changeCursor(dBHelper.getCursorSortByPriority());
    }
}
