package com.codeu.amwyz.ct;

/**
 * Created by wesley on 7/24/15.
 */

import android.app.Application;
import android.content.pm.PackageManager;
import android.os.Build;
import android.widget.Toast;

import com.parse.Parse;

public class App extends Application {

    @Override public void onCreate() {
        super.onCreate();
        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "XqIZtjIyDFCKbKQhhvc3esRE48SOai6XAPODZq16", "YUJDQfuYKsSOmvPdOKj19oEL4lQVpxFeEOSuv47k");


    }



}