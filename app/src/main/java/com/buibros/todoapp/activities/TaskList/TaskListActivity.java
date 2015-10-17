package com.buibros.todoapp.activities.TaskList;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.buibros.todoapp.R;
import com.buibros.todoapp.activities.PomodoroTimerActivity;

public class TaskListActivity extends AppCompatActivity/* implements TaskInputFragment.newTaskInputtedListener */ {
    private ContentValues newTaskValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.taskListActivityRelativeLayout, new TaskListFragment());
        fragmentTransaction.commit();
        newTaskValue = new ContentValues();

        ;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_task_list, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        else if (id == R.id.action_timer) {
            Intent timerIntent = new Intent(this, PomodoroTimerActivity.class);
            startActivity(timerIntent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


   /* @Override
    public void newTaskInputted(String newTask) {
        newTaskValue.clear();
        newTaskValue.put(TaskContract.Columns.TASK, newTask);
    }*/
}
