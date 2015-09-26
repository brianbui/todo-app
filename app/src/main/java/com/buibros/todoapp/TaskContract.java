package com.buibros.todoapp;

import android.provider.BaseColumns;

/**
 * Created by Kevin on 9/17/2015.
 */
public class TaskContract {
    public static final String DB_NAME = "com.buibros.todoapp.tasks";
    public static final int DB_VERSION = 3;
    public static final String TABLE = "tasks";

    public class Columns implements BaseColumns
    {
        public static final String TASK = "task";
        public static final String _ID = BaseColumns._ID;
        public static final String DATE = "date";
        public static final String LOCATION = "location";
        public static final String PRIORITY = "priority";
        public static final String DURATION = "duration";
    }
}
