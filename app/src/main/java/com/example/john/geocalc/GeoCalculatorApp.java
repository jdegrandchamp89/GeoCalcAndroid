package com.example.john.geocalc;

import android.app.Application;
import net.danlew.android.joda.JodaTimeAndroid;

/**
 * Created by John on 6/1/2017.
 */

public class GeoCalculatorApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        JodaTimeAndroid.init(this);
    }

}
