package com.example.john.geocalc;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.renderscript.Double2;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.location.Location;
import android.widget.TextView;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends AppCompatActivity {

    String distanceUnit = "Kilometers";
    String bearingUnit = "Degrees";
    String lat1;
    String lat2;
    String long1;
    String long2;
    boolean keyboard;
    public static int SETTINGS_RESULT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        EditText latitude1 = (EditText) findViewById(R.id.latitude1);
        EditText latitude2 = (EditText) findViewById(R.id.latitude2);
        EditText longitude1 = (EditText) findViewById(R.id.longitude1);
        EditText longitude2 = (EditText) findViewById(R.id.longitude2);
        Button calculate = (Button) findViewById(R.id.calculate);
        Button clear = (Button) findViewById(R.id.clear);
        TextView distanceValue = (TextView) findViewById(R.id.distanceText);
        TextView bearingValue = (TextView) findViewById(R.id.bearingText);

        Intent payload = getIntent();
        if (payload.hasExtra("distanceUnit")) {
            distanceUnit = payload.getStringExtra("distanceUnit");
        }
        if (payload.hasExtra("bearingUnit")) {
            bearingUnit = payload.getStringExtra("bearingUnit");
        }
        if (payload.hasExtra("lat1")) {
            lat1 = payload.getStringExtra("lat1");
            latitude1.setText(lat1);
        }
        if (payload.hasExtra("lat2")) {
            lat2 = payload.getStringExtra("lat2");
            latitude2.setText(lat2);
        }
        if (payload.hasExtra("long1")) {
            long1 = payload.getStringExtra("long1");
            longitude1.setText(long1);
        }
        if (payload.hasExtra("long2")) {
            long2 = payload.getStringExtra("long2");
            longitude2.setText(long2);

            // if these values already exist use them to make calculation
            if (lat1.length() != 0 && lat2.length() != 0 && long1.length() != 0 && long2.length() != 0) {
                Location location1 = new Location("");
                location1.setLatitude(Double.parseDouble(lat1));
                location1.setLongitude(Double.parseDouble(long1));

                Location location2 = new Location("");
                location2.setLatitude(Double.parseDouble(lat2));
                location2.setLongitude(Double.parseDouble(long2));

                Float distance2 = location1.distanceTo(location2);
                distance2 *= Float.parseFloat("0.001");
                if (distanceUnit.contentEquals("Miles")) {
                    distance2 *= Float.parseFloat("0.621371");
                }

                Float bearing2 = location1.bearingTo(location2);
                if (bearingUnit.contentEquals("Mils")) {
                    bearing2 *= Float.parseFloat("17.777777777778");
                }

                distanceValue.setText("Distance: " + String.format("%.2f%n", distance2) + distanceUnit);
                bearingValue.setText("Bearing: " + String.format("%.2f%n", bearing2) + bearingUnit);
            }

        }

        calculate.setOnClickListener(v -> {
            lat1 = latitude1.getText().toString();
            lat2 = latitude2.getText().toString();
            long1 = longitude1.getText().toString();
            long2 = longitude2.getText().toString();

            if (lat1.length() != 0 && lat2.length() != 0 && long1.length() != 0 && long2.length() != 0) {
                Location loc1 = new Location("");
                loc1.setLatitude(Double.parseDouble(lat1));
                loc1.setLongitude(Double.parseDouble(long1));

                Location loc2 = new Location("");
                loc2.setLatitude(Double.parseDouble(lat2));
                loc2.setLongitude(Double.parseDouble(long2));

                Float distance = loc1.distanceTo(loc2);
                distance *= Float.parseFloat("0.001");
                if (distanceUnit.contentEquals("Miles")) {
                    distance *= Float.parseFloat("0.621371");
                }

                Float bearing = loc1.bearingTo(loc2);
                if (bearingUnit.contentEquals("Mils")) {
                    bearing *= Float.parseFloat("17.777777777778");
                }

                distanceValue.setText("Distance: " + String.format("%.2f%n", distance) + distanceUnit);
                bearingValue.setText("Bearing: " + String.format("%.2f%n", bearing) + bearingUnit);
            }

            hideKeyboard();


        });

        clear.setOnClickListener(v -> {
            latitude1.setText(null);
            latitude2.setText(null);
            longitude1.setText(null);
            longitude2.setText(null);;
            distanceValue.setText("Distance:");
            bearingValue.setText("Bearing:");

            hideKeyboard();
        });

    }

    private void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == SETTINGS_RESULT) {
            this.bearingUnit = data.getStringExtra("bearingUnits");
            this.distanceUnit = data.getStringExtra("distanceUnits");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (item.getItemId() == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            intent.putExtra("distanceUnit", distanceUnit);
            intent.putExtra("bearingUnit", bearingUnit);
            intent.putExtra("lat1", lat1);
            intent.putExtra("lat2", lat2);
            intent.putExtra("long1", long1);
            intent.putExtra("long2", long2);

            startActivityForResult(intent, SETTINGS_RESULT);
            //finish();
            return true;
        }
        return false;
    }

}