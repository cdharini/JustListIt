package com.codepath.cdharini.justlistit.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper with functions to access our database of todo items
 * Created by dharinic on 8/13/17.
 */
public class ToDoDatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = ToDoDatabaseHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "justlistit";
    private static final int DATABASE_VERSION = 1;

    private static final String KEY_ID = "_id";
    private static final String KEY_DUEDATE = "duedate";
    private static final String KEY_TODOITEM = "item";
    private static final String KEY_PRIORITY = "priority";
    private static final String TABLE_TODOITEMS = "todoItemsTable";


    private static ToDoDatabaseHelper sInstance;

    public static synchronized ToDoDatabaseHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new ToDoDatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    private ToDoDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TODOITEMS_TABLE = "CREATE TABLE " + TABLE_TODOITEMS +
                "(" +
                KEY_ID + " INTEGER PRIMARY KEY," +
                KEY_TODOITEM + " TEXT," +
                KEY_PRIORITY + " TEXT," +
                KEY_DUEDATE + " TEXT" +
                ")";

        db.execSQL(CREATE_TODOITEMS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODOITEMS);
            onCreate(db);
        }
    }

    /**
     * Adds a new entry to the database
     * @param item ToDoItem to be added
     */
    public void addItem(ToDoItem item) {
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_TODOITEM, item.getTodoItem());
            values.put(KEY_DUEDATE, item.getDate());
            values.put(KEY_PRIORITY, item.getPriority().toString());

            db.insertOrThrow(TABLE_TODOITEMS, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "Error while adding todo to database", e);
        } finally {
            db.endTransaction();
        }
    }

    /**
     * Deletes entry using database id
     * @param id Database primary key
     */
    public void deleteItem(long id) {
        SQLiteDatabase db = getWritableDatabase();
        String selection = KEY_ID + " = ?";
        db.beginTransaction();
        try {
            db.delete(TABLE_TODOITEMS, selection, new String[] { String.valueOf(id) });
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "Error when deleting todo", e);
        } finally {
            db.endTransaction();
        }
    }

    /**
     * Updates an item in the database
     * @param id Database primary key
     * @param editedItem updated Todo
     * @param priority updated priority
     * @param date updated due date
     */
    public void updateItem(long id, String editedItem, String priority, String date) {
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_TODOITEM, editedItem);
            values.put(KEY_PRIORITY, priority);
            values.put(KEY_DUEDATE, date);
            String selection = KEY_ID + " = ?";

            int count = db.update(
                    TABLE_TODOITEMS,
                    values,
                    selection,
                    new String[] { String.valueOf(id) });

            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while editing todo in database");
        } finally {
            db.endTransaction();
        }
    }

    /**
     * Retreives entry from DB
     * @param id Database primary key
     * @return ToDoItem
     */
    public ToDoItem getItem(long id) {
        SQLiteDatabase db = getWritableDatabase();
        String queryString = String.format("SELECT * FROM %s WHERE %s = ?",
                TABLE_TODOITEMS, KEY_ID);
        Cursor cursor = db.rawQuery(queryString, new String[]{String.valueOf(id)});
        if (cursor.getCount() == 0) {
            cursor.close();
            return null;
        }
        cursor.moveToFirst();
        ToDoItem item = parseItemFromCursor(cursor);
        cursor.close();
        return item;
    }

    /**
     * Get ToDoItem given a cursor
     * @param cursor
     * @return ToDoItem
     */
    public ToDoItem parseItemFromCursor(Cursor cursor) {
        String d = cursor.getString(cursor.getColumnIndex(KEY_DUEDATE));
        ToDoItem.Priority p = ToDoItem.Priority.valueOf((cursor.getString(cursor.getColumnIndex(KEY_PRIORITY))));
        String i = cursor.getString(cursor.getColumnIndex(KEY_TODOITEM));
        return new ToDoItem(i, d, p);
    }

    /**
     * Delets all rows in DB
     */
    public void deleteAllItems() {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(TABLE_TODOITEMS, null, null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "Error while trying to delete all posts and users", e);
        } finally {
            db.endTransaction();
        }
    }

    /**
     * Gets a Cursor to the items table
     * @return cursor
     */
    public Cursor getCursor() {
        SQLiteDatabase db = getWritableDatabase();
        String SELECT_ITEMS_QUERY = String.format("SELECT * FROM %s",  TABLE_TODOITEMS);

        Cursor todoCursor = db.rawQuery(SELECT_ITEMS_QUERY, null);
        return todoCursor;
    }

    /**
     * Gets all the items in the table
     * @return list of items in the table
     */
    public List<ToDoItem> getAllItems() {
        List<ToDoItem> items = new ArrayList<>();

        String SELECT_ITEMS_QUERY = String.format("SELECT * FROM %s",  TABLE_TODOITEMS);

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(SELECT_ITEMS_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    ToDoItem item = parseItemFromCursor(cursor);
                    items.add(item);
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error while trying to get posts from database", e);
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return items;
    }
}
