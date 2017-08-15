package com.codepath.cdharini.justlistit.model;

/**
 * Model of the ToDoItem in our database
 * Created by dharinic on 8/13/17.
 */
public class ToDoItem {
    private String mTodoItem;
    private String mDate;
    private Priority mPriority;

    public enum Priority {
        HIGH("HIGH"),
        MEDIUM("MEDIUM"),
        LOW("LOW");

        private String mPriority;

        Priority(final String priority) {
            mPriority = priority;
        }

        @Override
        public String toString() {
            return mPriority;
        }
    }

    public Priority getPriority() {
        return mPriority;
    }

    public String getTodoItem() {
        return mTodoItem;
    }

    public String getDate() {
        return mDate;
    }

    public ToDoItem(String item, String date, Priority priority) {
        mTodoItem = item;
        mDate = date;
        mPriority = priority;
    }

}
