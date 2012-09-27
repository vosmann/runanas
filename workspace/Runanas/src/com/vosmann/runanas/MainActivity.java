package com.vosmann.runanas;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;

import com.vosmann.runanas.data.Run;
import com.vosmann.runt.R;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.ToggleButton;

public class MainActivity extends Activity {
	private static final String TAG = "MainActivity";
	private static final String LOG_LINE =
			"--------------------------------\n";
	private static final String LOG_PREFIX_LINE = "--- ";
	private static final String LOG_START = "Tracking start\n";
	private static final String LOG_END = "Tracking end\n";
	
	private TextView runLengthTextView;
	private Chronometer runDurationChronometer;
	private TextView avgSpeedTextView;
	private TextView lastLocationTextView;
	
	private LocationManager locationManager; 
	private LocationListener locationListener;
	
	private Run run;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
		// Get the references to the other widgets.
		runLengthTextView = (TextView) findViewById(R.id.run_length);
		runDurationChronometer = (Chronometer) findViewById(R.id.run_duration);
		avgSpeedTextView = (TextView) findViewById(R.id.run_avg_speed);
		lastLocationTextView = (TextView) findViewById(R.id.last_location);
		
		// Prepare the location acquiring.
		locationManager = (LocationManager)
				this.getSystemService(Context.LOCATION_SERVICE);
		locationListener = new LocationListener() {
			public void onLocationChanged(Location location) {
				handleNewLocation(location);
			}
			public void onStatusChanged(String provider,
					int status, Bundle extras) {}
			public void onProviderEnabled(String provider) {}
			public void onProviderDisabled(String provider) {}
		};
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    public void onToggleClicked(View view) {
    	ToggleButton toggleButton = (ToggleButton) view;
    	boolean on = toggleButton.isChecked();
    	if (on) {
    		startTracking();
    	} else {
    		stopTracking();
    	}
    }
    
    private void logPrimitively(String message) {
    	avgSpeedTextView.setText(avgSpeedTextView.getText() + message);
    	runLengthTextView.setText(runLengthTextView.getText() + message);
    }
    
    private void writeToFile(String content) {
    	String fileName = "runt-test-file.txt";
    	final String errorMessageDir =
    			"Couldn't get the external storage directory.";
    	String errorMessageCreateFile = "Couldn't create the file.";
    	String errorMessageWriteFile = "Couldn't write to the file.";
    	
    	File extStorageRootDir = null; 
    	File file = null; 
    	try {
    		extStorageRootDir = Environment.getExternalStorageDirectory();
    	} catch (Exception e) {
    		logPrimitively(errorMessageDir + ": " + e.getMessage());
    	}
    	
    	try {
    		file = new File(extStorageRootDir + "/" + fileName);
    	} catch (Exception e) {
    		// This is most probably redundant.
    		logPrimitively(errorMessageCreateFile);
    	}
    	try {
    		final boolean isAppend = true;
    		OutputStream os = new FileOutputStream(file, isAppend);
    		byte[] data = content.getBytes();
    		os.write(data);
    		os.close();
    	} catch (IOException e) {
    		logPrimitively(errorMessageWriteFile + ": " + e.getMessage());
    	}
    }
    
    private void logTrackingStart() {
    	Time now = new Time();
    	now.setToNow();
    	String message = LOG_PREFIX_LINE + LOG_LINE
    			+ LOG_PREFIX_LINE + LOG_START
    			+ LOG_PREFIX_LINE + now.format2445() + "\n"
    			+ LOG_PREFIX_LINE + LOG_LINE;
    	writeToFile(message);
    }
    private void logTrackingEnd() {
    	Time now = new Time();
    	now.setToNow();
    	String message = LOG_PREFIX_LINE + LOG_LINE
    			+ LOG_PREFIX_LINE + LOG_END
    			+ LOG_PREFIX_LINE + now.toString() + "\n"
    			+ LOG_PREFIX_LINE + LOG_LINE + "\n\n";
    	writeToFile(message);
    }
    private void logData(String content) {
    	Time now = new Time();
    	now.setToNow();
    	String message = LOG_PREFIX_LINE + now.toString() + "\n"
    			+ content + "\n";
    	writeToFile(message);
    }
    
    private void startTracking() {
    	run = new Run(78); // Use a spinner or something.
    	logTrackingStart();
    	updateTextViews(run);
    	// Start the Chronometer.
    	runDurationChronometer.setBase(SystemClock.elapsedRealtime());
    	runDurationChronometer.start();
    	// Start getting GPS locations.
    	startGettingLocations();
    }
    
    private void stopTracking() {
    	// Stop the Chronometer.
    	runDurationChronometer.stop();
    	// Stop getting GPS locations.
    	stopGettingLocations();
    	logTrackingEnd();
    }
    
    private void startGettingLocations() {
    	int minTimeBetweenUpdatesGps = 10000; // 10 seconds.
    	boolean gpsEnabled =
    			locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    	if (gpsEnabled) {
    		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
    				minTimeBetweenUpdatesGps, 0, locationListener);
    	} else {
			logPrimitively(getString(R.string.no_location_service));
			stopTracking();
		}
    }
    private void stopGettingLocations() {
		locationManager.removeUpdates(locationListener);
    }
    
    private void updateTextViews(Run run) {
    	runLengthTextView.setText(run.formatDistance());
    	avgSpeedTextView.setText(run.formatAverageSpeed());
    	lastLocationTextView.setText(run.formatLastRunPoint());
    }
    
    private void handleNewLocation(Location location) {
    	Log.d(TAG, "handleNewLocation(): Starting. New location: "
    			+ location.toString());
    	// "Store" it.
    	run.addRunPoint(location);
    	updateTextViews(run);
    	logData("Distance between: " + Float.toString(distance));
    }
}
