package com.vosmann.runanas;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;

import com.vosmann.runanas.R;
import com.vosmann.runanas.model.Run;

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

/**
 * Implements the basic functionality of the Runanas run tracking app.
 * TODO:
 * + finish domain model
 * - fix all loose ends so the app can be built, deployed to phone and tested
 * - implement storing into a local DB on the phone:
 *   - the only classes that will need to have their own tables seem to be
 *     RunPoint and RunMetrics
 *   - they only need to be linked by a common Run ID. 
 * - implement chronometer restarting once first GPS location is received
 *   - add label/pop-up that says "acquiring first location"
 *   - add label/pop-up that says "GO!" once location is acquired
 * - make phone vibrate on each location update, later maybe add sound upon
 *   updates
 * - include a settings tab and store settings into a Settings class
 * - make a run map tab (with these multiple map options - subtabs?)
 *   - draw a blind map with only the contour of the run path - maybe 8bit art!!
 *   - generate links to googlemaps using the gathered locations (this way, the
 *     app is still private (doesn't require network connectivity)
 *   - integrate a googlemap in to the app itself
 * 
 * @author vosmann
 */
public class MainActivity extends Activity {
	private static final String TAG = "MainActivity";
	// TODO: Get rid of these silly logging const strings.
	private static final String LOG_LINE =
			"--------------------------------\n";
	private static final String LOG_PREFIX_LINE = "--- ";
	private static final String LOG_START = "Tracking start\n";
	private static final String LOG_END = "Tracking end\n";
	
	// Interface.
	private TextView runLengthTextView;
	private Chronometer runDurationChronometer;
	private TextView avgSpeedTextView;
	private TextView lastLocationTextView;
	
	// Domain model.
	private LocationManager locationManager; 
	private LocationListener locationListener;
	// TODO incorporate these into a Settings class.
	private static final int WEIGHT = 78;
	private static final int MIN_UPDATE_TIME = 1000; // 1 second.
	private static final int MIN_UPDATE_DISTANCE = 5; // 1 second.
	
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
    	run = new Run(WEIGHT); // Use a spinner or something.
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
    	boolean gpsEnabled =
    			locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    	if (gpsEnabled) {
    		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
    				MIN_UPDATE_TIME, MIN_UPDATE_DISTANCE, locationListener);
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
