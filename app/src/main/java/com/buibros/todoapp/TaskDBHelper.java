package com.buibros.todoapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Kevin on 9/17/2015.
 */
public class TaskDBHelper  extends SQLiteOpenHelper{

    public TaskDBHelper(Context context)
    {
        super(context, TaskContract.DB_NAME, null, TaskContract.DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqlDB) {
        String sqlQuery = String.format("CREATE TABLE %s (" +
                        "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "%s TEXT," +
                        "%s DATE" +
                        ")",
                    TaskContract.TABLE,
                    TaskContract.Columns.TASK,
                    TaskContract.Columns.DATE);

        Log.v("TaskDBHelper", "Query to form table"+sqlQuery);
        sqlDB.execSQL(sqlQuery);
        Log.v("TaskDBHelper", "Works?");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqlDB, int oldVersion, int newVersion) {
        Log.v("On Upgrade", "Works?");
        sqlDB.execSQL("DROP TABLE IF EXISTS" + TaskContract.TABLE);
        onCreate(sqlDB);
    }
}
