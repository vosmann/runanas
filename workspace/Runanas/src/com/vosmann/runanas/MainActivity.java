package com.vosmann.runanas;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.vosmann.runanas.model.Run;
import com.vosmann.runanas.model.RunPoint;
import com.vosmann.runanas.persistence.RunStorage;

/**
 * Implements the basic functionality of the Runanas run tracking app.
 * TODO:
 * + finish domain model
 * - fix all loose ends so the app can be built, deployed to phone and tested
 * - implement storing into a local DB on the phone:
 *   - the only classes that will need to have their own tables seem to be
 *     RunPoint and RunResult
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
	public static final String RUNT_LOG_FILE_NAME = "runt-test-file.txt";
	
	// User interface.
	private TextView statusTextView;
	private TextView runDistanceTextView;
	private Chronometer runDurationChronometer;
	private TextView avgSpeedTextView;
	private TextView lastLocationTextView;
	
	// Tracking domain necessities.
	private LocationManager locationManager; 
	private LocationListener locationListener;
	
	private static final int WEIGHT = 78; // Use a spinner or something.
	private static final int MIN_UPDATE_TIME = 1000; // 1 second.
	private static final int MIN_UPDATE_DISTANCE = 5;
	
	private boolean runStarted;
	private Run run;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
		// Get references to the activity's widgets.
        statusTextView = (TextView) findViewById(R.id.status);
		runDistanceTextView = (TextView) findViewById(R.id.run_length);
		runDurationChronometer = (Chronometer) findViewById(R.id.run_duration);
		avgSpeedTextView = (TextView) findViewById(R.id.run_avg_speed);
		lastLocationTextView = (TextView) findViewById(R.id.last_location);
		
		// Prepare location acquiring.
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
    
    private void startTracking() {
    	run = new Run(WEIGHT);
    	updateTextViews(run);
    	// Start getting GPS locations.
    	displayStatus(getString(R.string.acquiring_first_location));
    	startGettingLocations();
    }
    
    private void stopTracking() {
    	RunStorage.storeRunResult(run.getRunResult(), this);
    	// Stop the Chronometer.
    	runDurationChronometer.stop();
    	// Stop getting GPS locations.
    	stopGettingLocations();
    	displayStatus(getString(R.string.tracking_stopped));
    }
    
    private void startGettingLocations() {
    	boolean gpsEnabled =
    			locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    	if (gpsEnabled) {
    		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
    				MIN_UPDATE_TIME, MIN_UPDATE_DISTANCE, locationListener);
    	} else {
			displayStatus(getString(R.string.no_location_service));
			stopTracking();
		}
    }
    private void stopGettingLocations() {
		locationManager.removeUpdates(locationListener);
    }
    
    private void updateTextViews(Run run) {
    	runDistanceTextView.setText(run.formatDistance());
    	avgSpeedTextView.setText(run.formatAverageSpeed());
    	lastLocationTextView.setText(run.formatLastRunPoint());
    }
    
    private void handleNewLocation(Location location) {
    	Log.d(TAG, "handleNewLocation(): Starting. New location: "
    			+ location.toString());
    	if (!runStarted){
	    	// Start the Chronometer.
	    	// runDurationChronometer.setBase(SystemClock.elapsedRealtime()*/);
	    	displayStatus(getString(R.string.first_location_acquired));
	    	runDurationChronometer.start();
    	}
    	// "Store" it.
    	RunPoint runPoint = run.addRunPoint(location);
    	updateTextViews(run);
    	RunStorage.storeRunPoint(runPoint, this);
    }
    
    private void displayStatus(String message) {
    	statusTextView.setText(message);
    }
}
