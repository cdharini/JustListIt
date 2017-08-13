package com.codepath.cdharini.justlistit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> mItems;
    private ArrayAdapter<String> mArrayAdapter;
    private ListView mListView;

    private static final String SAVED_ITEMS_FILE = "itemsFile.txt";
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String EXTRA_ITEM_POSISTION = "position";
    private static final String EXTRA_ITEM = "item";
    private static final int EDIT_REQUEST_CODE = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        readItemsFromFile();
        mArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mItems);

        mListView = (ListView) findViewById(R.id.lvToDoItems);
        mListView.setAdapter(mArrayAdapter);
        setupListViewListener();
    }

    public void onAddItem(View view) {
        EditText etAddItem = (EditText) findViewById(R.id.etAddItem);
        String newItem = etAddItem.getText().toString();
        mArrayAdapter.add(newItem);
        writeItemsToFile();
        etAddItem.setText("");
    }

    private void setupListViewListener() {
        /*
         * Delete item on Long click
         */
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> adapter, View v, int pos, long id) {
                mItems.remove(pos);
                mArrayAdapter.notifyDataSetChanged();
                writeItemsToFile();
                return true;
            }
        });
        /*
         * Open edit activity on click
         */
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapter, View v, int pos, long id) {
                Intent editActivityIntent = new Intent(MainActivity.this, EditItemActivity.class);
                editActivityIntent.putExtra(EXTRA_ITEM_POSISTION, pos);
                editActivityIntent.putExtra(EXTRA_ITEM, mItems.get(pos));
                startActivityForResult(editActivityIntent, EDIT_REQUEST_CODE);
            }
        });
    }

    private void readItemsFromFile() {
        File filesDir = getFilesDir();
        File itemsFile = new File(filesDir, SAVED_ITEMS_FILE);
        try {
            mItems = new ArrayList<String>(FileUtils.readLines(itemsFile));
        } catch (IOException e) {
            Log.i(TAG, "IOException when reading items from file!", e);
            mItems = new ArrayList<String>();
        }
    }

    private void writeItemsToFile() {
        File filesDir = getFilesDir();
        File itemsFile = new File(filesDir, SAVED_ITEMS_FILE);
        try {
            FileUtils.writeLines(itemsFile, mItems);
        } catch (IOException e) {
            Log.e(TAG, "IOException when reading items from file!", e);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //save the data to the adapter and file
        String editedItem = data.getStringExtra(EXTRA_ITEM);
        int pos = data.getIntExtra(EXTRA_ITEM_POSISTION, -1);
        mItems.set(pos, editedItem);
        mArrayAdapter.notifyDataSetChanged();
        writeItemsToFile();
    }
}
