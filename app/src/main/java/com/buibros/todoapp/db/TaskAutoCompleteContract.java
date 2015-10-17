package com.buibros.todoapp.db;

import android.provider.BaseColumns;

/**
 * Created by Kevin on 10/13/2015.
 */
public class TaskAutoCompleteContract {
    public static final String DB_NAME = "com.buibros.todoapp.taskautocomplete";
    public static final int DB_VERSION = 2;
    public static final String TABLE = "autocompletetask";

    public class Columns implements BaseColumns
    {
        public static final String TASK_AUTCOMPLETE = "taskautocomplete";
        public static final String _ID = BaseColumns._ID;
    }
}
