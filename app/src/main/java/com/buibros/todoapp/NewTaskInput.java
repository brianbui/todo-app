package com.buibros.todoapp;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Calendar;

public class NewTaskInput extends ActionBarActivity {
    static EditText taskNameInput;
    static EditText dueDateInput;
    static EditText locationInput;
    static Spinner prioritySpinner;
    static EditText durationInput;
    String[] priority = {"Low", "High"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task_input);
        taskNameInput = (EditText) findViewById(R.id.taskInput);
        dueDateInput = (EditText) findViewById(R.id.dateInput);
        locationInput = (EditText) findViewById(R.id.locationInput);
        prioritySpinner = (Spinner) findViewById(R.id.prioritySpinner);
        durationInput = (EditText) findViewById(R.id.durationInput);

        dueDateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerDialogFragment();
                Log.v("showDatePickerDialog", "Failed");
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });

        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(),
                                                R.layout.spinner_item,
                                                priority);
        prioritySpinner.setAdapter(adapter);
        prioritySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String input = (String) parent.getItemAtPosition(position);
                prioritySpinner.setPrompt(input);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_task_input, menu);
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
        if (id == R.id.action_done)
        {
            String task = taskNameInput.getText().toString();
            String date = dueDateInput.getText().toString();
            String location = locationInput.getText().toString();
            String priority = prioritySpinner.getPrompt().toString();
            String duration = durationInput.getText().toString();

            TaskDBHelper helper = new TaskDBHelper(this);
            SQLiteDatabase db = helper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.clear();

            values.put(TaskContract.Columns.TASK, task);
            values.put(TaskContract.Columns.DATE,date);
            values.put(TaskContract.Columns.LOCATION, location);
            values.put(TaskContract.Columns.PRIORITY, priority);
            values.put(TaskContract.Columns.DURATION, duration);

            db.insertWithOnConflict(TaskContract.TABLE,
                    null,
                    values,
                    SQLiteDatabase.CONFLICT_IGNORE);
            setResult(RESULT_OK, getIntent());
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    //Class used to create a dialog fragment to allow user to selct a date and subsequently to update the appropriate editview.
    public static class DatePickerDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener
    {
        public DatePickerDialogFragment()
        {}


        public Dialog onCreateDialog(Bundle savedInstanceState) {

            Calendar cal = Calendar.getInstance();
            return new DatePickerDialog(getActivity(),
                    this, cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        }

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            Log.v("OnDateSet", "d");
            dueDateInput.setText("" +year + "-"+ (monthOfYear + 1) + "-" + dayOfMonth);
        }
    }
}
