package com.buibros.todoapp.adapter;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.buibros.todoapp.R;
import com.buibros.todoapp.db.TaskAutoCompleteContract;
import com.buibros.todoapp.db.TaskAutoCompleteDBHelper;

import java.util.ArrayList;

/**
 * An adapter to retrieve autocomplete results from the autocomplete database
 */
public class TaskAutocompleteAdapter extends ArrayAdapter<String> implements Filterable {
    private ArrayList<String> resultList;
    private Context context;
    private int viewInt;

    public TaskAutocompleteAdapter(Context context, int resource) {
        super(context, resource);
        this.context = context;
        viewInt = resource;
    }

    @Override
    public int getCount() {
        return resultList.size();
    }

    @Override
    public String getItem(int index) {
        return resultList.get(index);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    // Retrieve the autocomplete results.
                    resultList = autocomplete(constraint.toString());

                    // Assign the data to the FilterResults
                    filterResults.values = resultList;
                    filterResults.count = resultList.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, Filter.FilterResults results) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged();
                }
                else {
                    notifyDataSetInvalidated();
                }
            }};
    }
    //Get result from database. This is called within filter so it is automatically off the ui thread
    private ArrayList<String> autocomplete(String constraint)
    {
        //create database helper
        TaskAutoCompleteDBHelper helper = new TaskAutoCompleteDBHelper(getContext());
        //create database
        SQLiteDatabase database = helper.getReadableDatabase();
        //get the cursor from database where the result is like the constraint
        Cursor cursor = database.rawQuery("SELECT taskautocomplete FROM autocompletetask WHERE taskautocomplete LIKE '%" + constraint + "%'", null);
        Log.d("cursor count", "" + cursor.getCount());
        //instantiate the list
        resultList = new ArrayList<String>();
        //get the column id for the auto complete column
        int columnIndex = cursor.getColumnIndex(TaskAutoCompleteContract.Columns.TASK_AUTCOMPLETE);
        //iterate through cursor and add result to resultlist
        while (cursor.moveToNext())
        {
            resultList.add(cursor.getString(columnIndex));
            Log.d("cursor move to next", cursor.getString(columnIndex));
        }
        return resultList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView ==null)
        {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.autocomplete_dropdown, parent, false);
        }
        TextView taskTextView = (TextView) convertView.findViewById(R.id.autocompleteDropdownTextView);
        taskTextView.setText(resultList.get(position));

        return convertView;
    }
}
