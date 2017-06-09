package com.example.john.geocalc;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

import org.joda.time.DateTime;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LocationSearchActivity extends AppCompatActivity {

    public static int PLACE_AUTOCOMPLETE_REQUEST_CODE1 = 1;
    public static int PLACE_AUTOCOMPLETE_REQUEST_CODE2 = 2;
    @BindView(R.id.start_date) TextView startDateView;
    @BindView(R.id.location1) TextView location1;
    @BindView(R.id.location2) TextView location2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @OnClick(R.id.location1)
    public void locationPressed(){
        try{
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).build(this);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE1);
        }catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }catch (GooglePlayServicesRepairableException e){
            e.printStackTrace();
        }
    }

    @OnClick(R.id.location2)
    public void locationPressed2(){
        try{
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).build(this);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE2);
        }catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }catch (GooglePlayServicesRepairableException e){
            e.printStackTrace();
        }
    }

    @OnClick(R.id.start_date)
    public void datePressed(){
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE1) {
            if(resultCode == RESULT_OK){
                Place pl = PlaceAutocomplete.getPlace(this, data);
                location1.setText(pl.getAddress());
            }
        }else if(requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE2) {
            if(resultCode == RESULT_OK){
                Place pl = PlaceAutocomplete.getPlace(this, data);
                location2.setText(pl.getAddress());
            }
        }else{
            super.onActivityResult(requestCode,resultCode, data);
        }

    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user

        }
    }
}


