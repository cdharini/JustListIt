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

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Activity user is taken to when they want to add an item
 * Returns back to MainActivity once finished
 */
public class AddItemActivity extends AppCompatActivity {

    private static final int RESULT_CODE_OK = 200;
    private EditText etEditItem;
    private Spinner mSpinner;
    private DatePicker mDatePicker;
    ToDoDatabaseHelper dBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        dBHelper = ToDoDatabaseHelper.getInstance(AddItemActivity.this);

        etEditItem = (EditText) findViewById(R.id.etToDoItem);
        etEditItem.requestFocus();

        mSpinner = (Spinner) findViewById(R.id.spPriority);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.priority_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);

        mDatePicker = (DatePicker) findViewById(R.id.dpDueDate);
    }

    public void onSave(View view) {
        String item = etEditItem.getText().toString();
        String priority = mSpinner.getSelectedItem().toString();
        String date = getDateStringFromPicker();
        ToDoItem newItem = new ToDoItem(item, date, ToDoItem.Priority.valueOf(priority));
        Intent returnData = new Intent();
        dBHelper.addItem(newItem);
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
