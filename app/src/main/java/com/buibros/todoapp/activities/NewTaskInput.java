package com.buibros.todoapp.activities;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.buibros.todoapp.R;
import com.buibros.todoapp.adapter.PlaceAutocompleteAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.Calendar;

public class NewTaskInput extends ActionBarActivity implements GoogleApiClient.OnConnectionFailedListener {
    //All of the text fields that are being inputted into initialized here.
    private EditText taskNameInput;
    private static EditText dueDateInput;
    private AutoCompleteTextView locationInput;
    private Spinner prioritySpinner;
    private EditText durationInput;
    private final String[] PRIORITY = {"Low", "High"};

    //For google location
    protected GoogleApiClient mGoogleApiClient;
    private PlaceAutocompleteAdapter mAdapter;
    //Bounds that surrounds the united states
    private LatLngBounds currentBounds = new LatLngBounds(new LatLng(25.364064, -124.629880), new LatLng(48.997188, -66.866327));
    //adapter that responds to pressing a button on the autocomplete
    private AdapterView.OnItemClickListener locationInputClickListener =
            new AdapterView.OnItemClickListener()
            {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //retrieves the autocompleteprediction based on location pressed
                    final AutocompletePrediction item = mAdapter.getItem(position);
                    //stores id for use with getPlaceById
                    final String placeId  = item.getPlaceId();
                    //primaryText = place name
                    final CharSequence primaryText = item.getPrimaryText(null);
                    PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                            .getPlaceById(mGoogleApiClient, placeId);
                    placeResult.setResultCallback(mUpdatePlaceDetailCallback);

                }
            };
    //shows first result
    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailCallback = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess())
            {
                //if request failed
                places.release();
                return;
            }
            final Place place = places.get(0);
            //formats details of place and display
            locationInput.setText(place.getAddress());
            places.release();

        }
    };

    private static Spanned formatPlaceDetails(Resources res, CharSequence name, String id,
                                              CharSequence address, CharSequence phoneNumber, Uri websiteUri) {
        return Html.fromHtml(res.getString(R.string.place_details, name,id,address,phoneNumber,websiteUri));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task_input);
        //All EditTexts saved
        taskNameInput = (EditText) findViewById(R.id.taskInput);
        dueDateInput = (EditText) findViewById(R.id.dateInput);
        locationInput = (AutoCompleteTextView) findViewById(R.id.locationInput);
        prioritySpinner = (Spinner) findViewById(R.id.prioritySpinner);
        durationInput = (EditText) findViewById(R.id.durationInput);

  /*      //Click listener that starts the DatePickerDialogFragment when clicked.
        dueDateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerDialogFragment();
                Log.v("showDatePickerDialog", "Failed");
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });*/
/*

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                                                R.layout.spinner_item,
                                                PRIORITY);
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
*/

        //Implementation of Google location
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .enableAutoManage(this, 0, this)
                .addApi(Places.GEO_DATA_API)
              //  .addApi(LocationServices.API)
                .build();

        locationInput.setOnItemClickListener(locationInputClickListener);
        mAdapter = new PlaceAutocompleteAdapter(this, mGoogleApiClient, currentBounds,null);
        locationInput.setAdapter(mAdapter);
    }
    @Override
    protected void onStart()
    {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop()
    {
        mGoogleApiClient.disconnect();
        super.onStop();
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
        //if done is selected the various edittext's values are saved into taskDB
       /* if (id == R.id.action_done)
        {
            //gets all of the values stored in the edittexts
            String task = taskNameInput.getText().toString();
            String date = dueDateInput.getText().toString();
            String location = locationInput.getText().toString();
            String priority = prioritySpinner.getPrompt().toString();
            String duration = durationInput.getText().toString();

            //boilerplate code to access taskdb
            TaskDBHelper helper = new TaskDBHelper(this);
            SQLiteDatabase db = helper.getWritableDatabase();
            //container for values to be stored in db
            ContentValues values = new ContentValues();
            values.clear();
            //stores values in key value pairs
            values.put(TaskContract.Columns.TASK, task);
            values.put(TaskContract.Columns.DATE,date);
            values.put(TaskContract.Columns.LOCATION, location);
            values.put(TaskContract.Columns.PRIORITY, priority);
            values.put(TaskContract.Columns.DURATION, duration);

            //stores the contentvalues in db
            db.insertWithOnConflict(TaskContract.TABLE,
                    null,
                    values,
                    SQLiteDatabase.CONFLICT_IGNORE);
            //sets the result to okay for startActivityForResult's onActivityResult
            setResult(RESULT_OK, getIntent());
            //closes activity
            finish();
        }*/

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        Log.e("GoogleApi", "onConnectionFailed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());

        Toast.makeText(this,
                "Could not connect to Google API Client: Error " + connectionResult.getErrorCode(),
                Toast.LENGTH_SHORT).show();
    }

    //Class used to create a dialog fragment to allow user to selct a date and subsequently to update the appropriate editview.
    public static class DatePickerDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener
    {
        //required so that the dialog fragment will not crash when rotated
        public DatePickerDialogFragment()
        {}

        //fills the dialog fragment with a datepickerdialog
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            Calendar cal = Calendar.getInstance();
            return new DatePickerDialog(getActivity(),
                    this, cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        }
        //stores the result of datepickerdialog into the date input field
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            Log.v("OnDateSet", "d");
            dueDateInput.setText("" +year + "-"+ (monthOfYear + 1) + "-" + dayOfMonth);
        }
    }

}
