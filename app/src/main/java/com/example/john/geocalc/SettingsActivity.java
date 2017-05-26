package com.example.john.geocalc;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class SettingsActivity extends AppCompatActivity {

    String distanceString;
    String bearingString;
    String lat1;
    String lat2;
    String long1;
    String long2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Spinner distanceSpinner = (Spinner) findViewById(R.id.distanceUnit);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> distanceAdapter = ArrayAdapter.createFromResource(this,
                R.array.distance_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        distanceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        distanceSpinner.setAdapter(distanceAdapter);

        Spinner bearingSpinner = (Spinner) findViewById(R.id.bearingUnit);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> bearingAdapter = ArrayAdapter.createFromResource(this,
                R.array.bearing_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        bearingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        bearingSpinner.setAdapter(bearingAdapter);

        Intent payload = getIntent();
        if (payload.hasExtra("distanceUnit")) {
            distanceString = payload.getStringExtra("distanceUnit");
            if(distanceString.contentEquals("Kilometers")){
                distanceSpinner.setSelection(0);
            }
            else if(distanceString.contentEquals("Miles")){
                distanceSpinner.setSelection(1);
            }
        }

        if (payload.hasExtra("bearingUnit")) {
            bearingString = payload.getStringExtra("bearingUnit");
            if(bearingString.contentEquals("Degrees")){
                bearingSpinner.setSelection(0);
            }
            else if(bearingString.contentEquals("Mils")){
                bearingSpinner.setSelection(1);
            }
        }

        if (payload.hasExtra("lat1")) {
            lat1 = payload.getStringExtra("lat1");
        }
        if (payload.hasExtra("lat2")) {
            lat2 = payload.getStringExtra("lat2");
        }
        if (payload.hasExtra("long1")) {
            long1 = payload.getStringExtra("long1");
        }
        if (payload.hasExtra("long2")) {
            long2 = payload.getStringExtra("long2");
        }


        distanceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position,long id) {
                distanceString =(String)parent.getSelectedItem();

            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        bearingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position,long id) {
                bearingString =(String)parent.getSelectedItem();

            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        FloatingActionButton save = (FloatingActionButton) findViewById(R.id.savebutton);
        save.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
            intent.putExtra("distanceUnit", distanceString);
            intent.putExtra("bearingUnit", bearingString);
            intent.putExtra("lat1", lat1);
            intent.putExtra("lat2", lat2);
            intent.putExtra("long1", long1);
            intent.putExtra("long2", long2);
            setResult(MainActivity.SETTINGS_RESULT,intent);
            finish();
        });
    }

}
