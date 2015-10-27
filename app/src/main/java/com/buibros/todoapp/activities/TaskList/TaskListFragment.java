package com.buibros.todoapp.activities.TaskList;


import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.buibros.todoapp.R;
import com.buibros.todoapp.db.TaskContract;
import com.buibros.todoapp.db.TaskDBHelper;

/**
 * The task list fragment to show all available tasks
 */
public class TaskListFragment extends Fragment {
    private final String LOG_TAG = this.toString();
    private ListView taskList;
    private FloatingActionButton newTaskFloatingActionButton;


    public TaskListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // instantiate variables
        final View view = inflater.inflate(R.layout.fragment_task_list,container,false);
        newTaskFloatingActionButton = (FloatingActionButton) view.findViewById(R.id.fab_new_input);
        taskList = (ListView) view.findViewById(android.R.id.list);
        Log.d(LOG_TAG, "Before FAB");
        //set onclicklistener for FAB
        newTaskFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LOG_TAG, "FAB OCL");
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.taskListActivityRelativeLayout, new TaskInputFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        //Toolbar added as action bar after activity is created
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) getActivity().findViewById(R.id.taskListToolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        //UpdateUI can only be used after the Activity has been created.
        new UpdateUI().execute(getActivity());
        super.onActivityCreated(savedInstanceState);
    }

    //When button is pressed this method deletes from db
    public void onDoneButtonClick(View view) {
        View v = (View) view.getParent();
        TextView taskTextView = (TextView) v.findViewById(R.id.taskTextView);
        String task = taskTextView.getText().toString();

        String sql = String.format("DELETE FROM %s WHERE %s = '%s'",
                TaskContract.TABLE,
                TaskContract.Columns.TASK,
                task);
        TaskDBHelper helper = new TaskDBHelper(getActivity());
        SQLiteDatabase sqlDB = helper.getWritableDatabase();
        sqlDB.execSQL(sql);
        new UpdateUI().execute(getActivity());
    }
    //Database calls preformed on a separate thread
    private static class UpdateUI extends AsyncTask<Activity, Void, Void>
    {
        private Activity activity;
        private Cursor cursor;
        @Override
        protected Void doInBackground(Activity... params) {
            activity = params[0];
            TaskDBHelper helper = new TaskDBHelper(activity);
            SQLiteDatabase sqlDB = helper.getReadableDatabase();
            cursor = sqlDB.query(TaskContract.TABLE,
                    new String[]{TaskContract.Columns._ID, TaskContract.Columns.TASK, TaskContract.Columns.DATE},
                    null, null, null, null, TaskContract.Columns.DATE);
            return null;
        }

        @Override
        protected void onPostExecute(Void blank) {
            ListAdapter listAdapter = new SimpleCursorAdapter(
                    activity,
                    R.layout.list_item_task,
                    cursor,
            new String[]{TaskContract.Columns.TASK, TaskContract.Columns.DATE},
                    new int[]{R.id.taskTextView, R.id.dateTextView},
                    0);
            super.onPostExecute(null);
        }
    }

}
