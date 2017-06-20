package com.example.john.geocalc;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Parcelable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.location.Location;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.example.john.geocalc.webservice.WeatherService;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import static com.example.john.geocalc.webservice.WeatherService.BROADCAST_WEATHER;

public class MainActivity extends AppCompatActivity {

    String distanceUnit = "Kilometers";
    String bearingUnit = "Degrees";
    String lat1;
    String lat2;
    String long1;
    String long2;
    boolean keyboard;
    public static int SETTINGS_RESULT = 1;
    public static int HISTORY_RESULT = 2;
    public static int LOCATION_SEARCH = 3;
    public static List<LocationLookup> allHistory;
    ImageView p1Icon;
    TextView p1Temp;
    TextView p1Summary;
    ImageView p2Icon;
    TextView p2Temp;
    TextView p2Summary;

    DatabaseReference topRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseDatabase dbRef = FirebaseDatabase.getInstance();
        topRef = dbRef.getReference("history");

        allHistory = new ArrayList<LocationLookup>();

        EditText latitude1 = (EditText) findViewById(R.id.latitude1);
        EditText latitude2 = (EditText) findViewById(R.id.latitude2);
        EditText longitude1 = (EditText) findViewById(R.id.longitude1);
        EditText longitude2 = (EditText) findViewById(R.id.longitude2);
        Button calculate = (Button) findViewById(R.id.calculate);
        Button clear = (Button) findViewById(R.id.clear);
        Button search = (Button) findViewById(R.id.search);
        TextView distanceValue = (TextView) findViewById(R.id.distanceText);
        TextView bearingValue = (TextView) findViewById(R.id.bearingText);
        p1Icon = (ImageView) findViewById(R.id.p1Icon);
        p1Temp = (TextView) findViewById(R.id.p1Temp);
        p1Summary = (TextView) findViewById(R.id.p1Summary);
        p2Icon = (ImageView) findViewById(R.id.p2Icon);
        p2Temp = (TextView) findViewById(R.id.p2Temp);
        p2Summary = (TextView) findViewById(R.id.p2Summary);

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
                WeatherService.startGetWeather(this, lat1, long1, "p1");
                WeatherService.startGetWeather(this, lat2, long2, "p2");
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
                WeatherService.startGetWeather(this, lat1, long1, "p1");
                WeatherService.startGetWeather(this, lat2, long2, "p2");

                // remember the calculation.
//                HistoryContent.HistoryItem item = new HistoryContent.HistoryItem(lat1.toString(), long1.toString(), lat2.toString(), long2.toString(), DateTime.now());
//                HistoryContent.addItem(item);
                LocationLookup entry = new LocationLookup();
                entry.setOrigLat(Double.parseDouble(lat1));
                entry.setOrigLng(Double.parseDouble(long1));
                entry.setDestLat(Double.parseDouble(lat2));
                entry.setDestLng(Double.parseDouble(long2));
                DateTimeFormatter fmt = ISODateTimeFormat.dateTime();
                entry.setTimestamp(fmt.print(DateTime.now()));
                topRef.push().setValue(entry);
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
            setWeatherViews(View.INVISIBLE);
        });

        search.setOnClickListener(v -> {
            Intent locationSearch = new Intent(MainActivity.this, LocationSearchActivity.class);
            startActivityForResult(locationSearch, LOCATION_SEARCH);
        });

    }

    private void hideKeyboard() {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            //this.getSystemService(Context.INPUT_METHOD_SERVICE);
            InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
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
        else if (resultCode == HISTORY_RESULT) {
            String[] vals = data.getStringArrayExtra("item");
            this.lat1 = vals[0];
            this.long1 = vals[1];
            this.lat2 = vals[2];
            this.long2 = vals[3];
            //this.updateScreen(); // code that updates the calcs.
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

                EditText latitude1 = (EditText) findViewById(R.id.latitude1);
                EditText latitude2 = (EditText) findViewById(R.id.latitude2);
                EditText longitude1 = (EditText) findViewById(R.id.longitude1);
                EditText longitude2 = (EditText) findViewById(R.id.longitude2);
                latitude1.setText(lat1);
                latitude2.setText(lat2);
                longitude1.setText(long1);
                longitude2.setText(long2);

                TextView distanceValue = (TextView) findViewById(R.id.distanceText);
                TextView bearingValue = (TextView) findViewById(R.id.bearingText);
                distanceValue.setText("Distance: " + String.format("%.2f%n", distance2) + distanceUnit);
                bearingValue.setText("Bearing: " + String.format("%.2f%n", bearing2) + bearingUnit);

                WeatherService.startGetWeather(this, lat1, long1, "p1");
                WeatherService.startGetWeather(this, lat2, long2, "p2");
            }
        }else if(resultCode == LOCATION_SEARCH){
            if (data != null && data.hasExtra("LOCATIONS")) {
                Parcelable par = data.getParcelableExtra("LOCATIONS");
                LocationLookup l = Parcels.unwrap(par);
                topRef.push().setValue(l);
                this.lat1 = Double.toString(l.origLat);
                this.long1 = Double.toString(l.origLng);
                this.lat2 = Double.toString(l.destLat);
                this.long2 = Double.toString(l.destLng);

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

                    EditText latitude1 = (EditText) findViewById(R.id.latitude1);
                    EditText latitude2 = (EditText) findViewById(R.id.latitude2);
                    EditText longitude1 = (EditText) findViewById(R.id.longitude1);
                    EditText longitude2 = (EditText) findViewById(R.id.longitude2);
                    latitude1.setText(lat1);
                    latitude2.setText(lat2);
                    longitude1.setText(long1);
                    longitude2.setText(long2);

                    TextView distanceValue = (TextView) findViewById(R.id.distanceText);
                    TextView bearingValue = (TextView) findViewById(R.id.bearingText);
                    distanceValue.setText("Distance: " + String.format("%.2f%n", distance2) + distanceUnit);
                    bearingValue.setText("Bearing: " + String.format("%.2f%n", bearing2) + bearingUnit);
                    WeatherService.startGetWeather(this, lat1, long1, "p1");
                    WeatherService.startGetWeather(this, lat2, long2, "p2");
                }
            }
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
        }else if(item.getItemId() == R.id.action_history) {
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            startActivityForResult(intent, HISTORY_RESULT );

            return true;
        }
        return false;
    }

    @Override
    public void onResume(){
        super.onResume();
        allHistory.clear();
        topRef = FirebaseDatabase.getInstance().getReference("history");
        topRef.addChildEventListener (chEvListener);
        IntentFilter weatherFilter = new IntentFilter(BROADCAST_WEATHER);
        LocalBroadcastManager.getInstance(this).registerReceiver(weatherReceiver, weatherFilter);
        setWeatherViews(View.INVISIBLE);
//topRef.addValueEventListener(valEvListener);
    }

    @Override
    public void onPause(){
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(weatherReceiver);
        topRef.removeEventListener(chEvListener);
    }

    private ChildEventListener chEvListener = new ChildEventListener() {

        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            LocationLookup entry = (LocationLookup)
                    dataSnapshot.getValue(LocationLookup.class);
            entry._key = dataSnapshot.getKey();
            DateTimeFormatter fmt = ISODateTimeFormat.dateTime();
            entry.setTimestamp(fmt.print(DateTime.now()));
            allHistory.add(entry);
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            LocationLookup entry = (LocationLookup)
                    dataSnapshot.getValue(LocationLookup.class);

            List<LocationLookup> newHistory = new ArrayList<LocationLookup>();

            for (LocationLookup t : allHistory) {
                if (!t._key.equals(dataSnapshot.getKey())) {
                    newHistory.add(t);
                }
            }

            allHistory = newHistory;
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    private void setWeatherViews(int visible) {

        p1Icon.setVisibility(visible);
        p2Icon.setVisibility(visible);
        p1Summary.setVisibility(visible);
        p2Summary.setVisibility(visible);
        p1Temp.setVisibility(visible);
        p2Temp.setVisibility(visible);

    }

    private BroadcastReceiver weatherReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            double temp = bundle.getDouble("TEMPERATURE");
            String summary = bundle.getString("SUMMARY");
            String icon = bundle.getString("ICON").replaceAll("-", "_");
            String key = bundle.getString("KEY");
            int resID = getResources().getIdentifier(icon , "drawable", getPackageName());
            setWeatherViews(View.VISIBLE);
            if (key.equals("p1")) {
                p1Summary.setText(summary);
                p1Temp.setText(Double.toString(temp));
                p1Icon.setImageResource(resID);
                p1Icon.setVisibility(View.INVISIBLE);
            } else {
                p2Summary.setText(summary);
                p2Temp.setText(Double.toString(temp));
                p2Icon.setImageResource(resID);
            }

        }

    };

}