package com.vosmann.runanas.data;

import java.util.LinkedList;
import java.util.List;

import android.location.Location;
import android.util.Log;

/**
 * A class encapsulating all important information about a run.
 * @author vosmann
 */
public class Run {
	private static final String TAG = "Run";
	
	private List<RunPoint> runPoints;
	private RunMetrics runMetrics;
	
	public Run(double mass) {
		this.runPoints = new LinkedList<RunPoint>();
		this.runMetrics = new RunMetrics(mass);
	}
	
	public void addRunPoint(Location newRunPointLocation) {
		if (newRunPointLocation == null) {
			Log.w(TAG, "addRunPoint(): Can't add a null runPointLocation!");
		}
		if (runPoints == null) {
			runPoints = new LinkedList<RunPoint>();
			Log.w(TAG,
					"addRunPoint(): runPoints list was null! Created new one.");
		}
		RunPoint lastRunPoint = runPoints.get(runPoints.size() - 1);
		RunPoint newRunPoint = new RunPoint(lastRunPoint, newRunPointLocation);
		runPoints.add(newRunPoint);
	}
	
	public void addRunPoints(List<Location> runPoints) {
		for(Location runPoint : runPoints) {
			addRunPoint(runPoint);
		}
	}
	
	private void gatherRunMetricsFromAllRunPoints() {
		
		
		Location firstRunPoint = runPoints.get(0);
		Location previousRunPoint = runPoints.get(runPoints.size() - 2);
		distance += newRunPoint.getdi
		duration = runPoint.getTime() - firstRunPoint.getTime();
		averageSpeed = (distance / duration) * 3600.0f; // [km/h].
		calculateEnergyExpenditure();
	}
	
	private void calculateEnergyExpenditure() {
		// This is an rough approximation of the energy rate while
		// running 10km/h.
		// This method will, of course, need revision.
		final double kjPerKgPerSecond = 0.0129138;
		energyExpenditure = mass * (duration / 1000) * kjPerKgPerSecond;
		bananas = energyExpenditure / BANANA_ENERGY;
	}

	/**
	 * The ordered list of GPS coordinates gathered during the run along with
	 * some corresponding information.
	 */
	public List<Location> getRunPoints() {
		return runPoints;
	}

		public String formatLastRunPoint() {
		String formattedLastRunPoint = null;
		Location lastRunPoint = runPoints.get(runPoints.size());
		if (lastRunPoint != null) {
			formattedLastRunPoint = String.format("%.6f, %.6f",
					lastRunPoint.getLatitude(), lastRunPoint.getLongitude());
		} else {
			formattedLastRunPoint = "Last run point not available";
		}
		return formattedLastRunPoint;
	}
}