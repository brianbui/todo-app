package com.buibros.todoapp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Kevin on 10/13/2015.
 */
public class TaskAutoCompleteDBHelper extends SQLiteOpenHelper {
    public TaskAutoCompleteDBHelper(Context context)
    {
        super(context, TaskAutoCompleteContract.DB_NAME, null, TaskAutoCompleteContract.DB_VERSION);
    }
//creates table
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlQuery = String.format("CREATE TABLE %s (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "%s TEXT)",
                TaskAutoCompleteContract.TABLE,
                TaskAutoCompleteContract.Columns.TASK_AUTCOMPLETE);
        db.execSQL(sqlQuery);
    }
//deletes table when database is upgraded
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TaskAutoCompleteContract.TABLE);
        onCreate(db);
    }

}
