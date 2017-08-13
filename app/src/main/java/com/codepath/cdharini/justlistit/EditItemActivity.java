package com.codepath.cdharini.justlistit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class EditItemActivity extends AppCompatActivity {
    private static final String EXTRA_ITEM_POSISTION = "position";
    private static final String EXTRA_ITEM = "item";
    private static final int RESULT_CODE_OK = 200;

    private EditText etEditItem;
    private int mPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        mPosition = getIntent().getIntExtra(EXTRA_ITEM_POSISTION, 0);
        String item = getIntent().getStringExtra(EXTRA_ITEM);

        etEditItem = (EditText) findViewById(R.id.etToDoItem);
        etEditItem.setText(item);
        etEditItem.setSelection(etEditItem.getText().length());
        etEditItem.requestFocus();
    }

    public void onSaveEdit(View view) {
        String editedItem = etEditItem.getText().toString();
        Intent returnData = new Intent();
        returnData.putExtra(EXTRA_ITEM_POSISTION, mPosition);
        returnData.putExtra(EXTRA_ITEM, editedItem);
        setResult(RESULT_CODE_OK, returnData);
        finish();
    }
}
