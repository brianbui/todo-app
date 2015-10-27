package com.buibros.todoapp.activities.TaskList;


import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;

import com.buibros.todoapp.R;
import com.buibros.todoapp.adapter.TaskAutocompleteAdapter;
import com.buibros.todoapp.db.TaskAutoCompleteContract;
import com.buibros.todoapp.db.TaskAutoCompleteDBHelper;

/**
 * Fragment to input a new task
 */
public class TaskInputFragment extends Fragment {
    private AutoCompleteTextView newTaskInputAutoCompleteTextView;
    private static AppCompatActivity activity;
 /*   newTaskInputtedListener mCallback;*/

    public TaskInputFragment() {
        // Required empty public constructor
    }

  /*  //Interface to communicate with TaskListActivity
    public interface newTaskInputtedListener
    {
        public void newTaskInputted(String newTask);
    }*/

    /*@Override
    public void onAttach(Context context) {
        super.onAttach(context);


        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (newTaskInputtedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }
*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment in order to call findviewbyid
        View newTaskFragmentView = inflater.inflate(R.layout.fragment_new_task, container, false);
        //instantiate the autocompleteview
        newTaskInputAutoCompleteTextView = (AutoCompleteTextView) newTaskFragmentView.findViewById(R.id.autoCompleteTaskInput);
        //attach a custom listadapter that implements filter so that the TaskAutocompletedatabase will be queried when the text is changed and a list of appropriate choices is shown
        newTaskInputAutoCompleteTextView.setAdapter(new TaskAutocompleteAdapter(getContext(), R.id.autocompleteDropdownTextView));
        //instantiate imagebutton
        ImageButton saveButton = (ImageButton) newTaskFragmentView.findViewById(R.id.save);
        //set onClicklistener for button
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Done button", "On click");
                //get text in autocompletetextview
                String [] taskToBeSaved = {newTaskInputAutoCompleteTextView.getText().toString()};
                //calls asynchtask to save value to taskautocompletedb
                new saveAutoCompleteTextView().execute(taskToBeSaved);
                //close fragment
                activity.getSupportFragmentManager().popBackStack();
                hide_keyboard(activity);
            }
        });
        ImageButton cancelButton = (ImageButton) newTaskFragmentView.findViewById(R.id.cancel_input);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //close fragment
                activity.getSupportFragmentManager().popBackStack();
                hide_keyboard(activity);

            }
        });
       /* newTaskInputAutoCompleteTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //if enter key pressed
                Log.d("Before onEditorAction", "");
                if (actionId == EditorInfo.IME_NULL)
                {
                   //saves input into content values;
                    mCallback.newTaskInputted(v.getText().toString());
                    Log.d("onEditorAction", v.getText().toString());
                }
                return false;
            }
        });*/
        Log.d("AfterOnEditorAction", "");
        return newTaskFragmentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        activity =(AppCompatActivity) getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        setHasOptionsMenu(true);
        if (actionBar!= null) {
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {

        getActivity().getMenuInflater().inflate(R.menu.menu_new_task_input, menu);
        menu.clear();
        super.onPrepareOptionsMenu(menu);
    }

    //newTaskInputAutoCompleteTextView not correctly showing keyboard despite having focus. This is the workaround
    @Override
    public void onResume() {
        //force focus on textview
        newTaskInputAutoCompleteTextView.requestFocus();
        //get inputmanager
        InputMethodManager mgr =      (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        //force the keyboard open
        mgr.showSoftInput(newTaskInputAutoCompleteTextView, InputMethodManager.SHOW_IMPLICIT);
        //Hide actionbar

        super.onResume();
    }
    private static class saveAutoCompleteTextView extends AsyncTask<String, Void, Void>
    {

        @Override
        protected Void doInBackground(String... params) {

            TaskAutoCompleteDBHelper helper = new TaskAutoCompleteDBHelper(activity);
            SQLiteDatabase sqLiteDatabase = helper.getWritableDatabase();
            String taskToSave = params[0];
            Log.d("Save Value", taskToSave);
            ContentValues values = new ContentValues();
            values.clear();

            values.put(TaskAutoCompleteContract.Columns.TASK_AUTCOMPLETE, taskToSave);

            sqLiteDatabase.insertWithOnConflict(TaskAutoCompleteContract.TABLE,
                    null,
                    values,
                    SQLiteDatabase.CONFLICT_IGNORE);

            return null;
        }
    }
    //Used to hide keyboard because the keyboard manager of Android IS GOD AWFUL
    public static void hide_keyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if(view == null) {
            view = new View(activity);
        }
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
