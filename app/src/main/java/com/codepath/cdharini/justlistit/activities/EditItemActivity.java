package com.codepath.cdharini.justlistit.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.codepath.cdharini.justlistit.R;
import com.codepath.cdharini.justlistit.model.ToDoDatabaseHelper;
import com.codepath.cdharini.justlistit.model.ToDoItem;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Activity user is taken to when they select an existing ToDoItem
 * allowing them to edit the item
 */
public class EditItemActivity extends AppCompatActivity {
    private static final String EXTRA_ITEM_ID = "dbId";
    private static final int RESULT_CODE_OK = 200;

    private EditText etEditItem;
    private long mDbId;
    private Spinner mSpinner;
    private DatePicker mDatePicker;
    ToDoDatabaseHelper dBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        dBHelper = ToDoDatabaseHelper.getInstance(EditItemActivity.this);
        mDbId = getIntent().getLongExtra(EXTRA_ITEM_ID, 0);
        ToDoItem item = dBHelper.getItem(mDbId);

        //populate edit text
        etEditItem = (EditText) findViewById(R.id.etToDoItem);
        etEditItem.setText(item.getTodoItem());
        etEditItem.setSelection(etEditItem.getText().length());
        etEditItem.requestFocus();

        //populate spinner
        mSpinner = (Spinner) findViewById(R.id.spPriority);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.priority_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);
        mSpinner.setSelection(adapter.getPosition(item.getPriority().toString()));

        //populate date picker
        mDatePicker = (DatePicker) findViewById(R.id.dpDueDate);
        String curDate = item.getDate();
        SimpleDateFormat formatter =  new SimpleDateFormat("MM-dd-yyyy");
        Date d = formatter.parse(curDate, new ParsePosition(0));
        if (d != null) {
            mDatePicker.updateDate(d.getYear(), d.getMonth() - 1, d.getDate());
        }
    }

    public void onSaveEdit(View view) {
        String editedItem = etEditItem.getText().toString();
        String priority = mSpinner.getSelectedItem().toString();
        String date = getDateStringFromPicker();
        Intent returnData = new Intent();
        dBHelper.updateItem(mDbId, editedItem, priority, date);

        returnData.putExtra(EXTRA_ITEM_ID, mDbId);
        setResult(RESULT_CODE_OK, returnData);
        finish();
    }

    public String getDateStringFromPicker() {
        int date = mDatePicker.getDayOfMonth();
        int month = mDatePicker.getMonth() + 1;
        int year = mDatePicker.getYear();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("MM-dd-yyyy");
        Date d = new Date(year, month, date);
        String strDate = dateFormatter.format(d);
        return strDate;
    }
}
