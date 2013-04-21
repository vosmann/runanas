package com.vosmann.runanas.model;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import android.location.Location;
import android.util.Log;

/**
 * A class encapsulating all important information about a run.
 * @author vosmann
 */
public class Run {
	private static final String TAG = "Run";
	
	private long id;
	private List<RunPoint> runPoints;
	private RunResult runResult;
	
	public Run(double mass) {
		UUID uuid = UUID.randomUUID();
		this.id = uuid.getMostSignificantBits(); // Unique enough?
		this.runPoints = new LinkedList<RunPoint>();
		this.runResult = new RunResult(id, mass);
	}
	public Run(long id, List<RunPoint> runPoints, RunResult runResult) {
		super();
		this.id = id;
		this.runPoints = runPoints;
		this.runResult = runResult;
	}

	public long getId() {
		return id;
	}
	/**
	 * The ordered list of GPS coordinates gathered during the run along with
	 * some corresponding information.
	 */
	public List<RunPoint> getRunPoints() {
		return runPoints;
	}
	public RunResult getRunResult() {
		return runResult;
	}
	
	public void addRunPoint(Location location) {
		RunPoint runPoint = null;
		if (location == null) {
			Log.w(TAG, "addRunPoint(Location ): Can't add a null Location!");
		} else {
			runPoint = new RunPoint(this.id, location);
		}
		addRunPoint(runPoint);
	}
	
	private void addRunPoint(RunPoint runPoint) {
		if (runPoint == null) {
			String errMsg = "addRunPoint(RunPoint): Can't add a null RunPoint!";
			Log.w(TAG, errMsg);
			throw new NullPointerException(errMsg);
		}
		if (runPoints == null) {
			runPoints = new LinkedList<RunPoint>();
			Log.i(TAG,
					"addRunPoint(): runPoints list was null! Created new one.");
		}
		RunPoint lastPoint = runPoints.get(runPoints.size() - 1);
		runPoints.add(runPoint);
		try {
			double segmentDistance = runPoint.getLocation().distanceTo(
					lastPoint.getLocation());
			long segmentDuration = runPoint.getLocation().getTime()
					- lastPoint.getLocation().getTime();
			runResult.update(segmentDistance, segmentDuration);
		} catch (Exception e) {
			Log.w(TAG, "Couldn't update RunResult. Segment faulty.");
		}
	}
	
//	public void addRunPoints(List<Location> runPoints) {
//		for(Location runPoint : runPoints) {
//			addRunPoint(runPoint);
//		}
//	}
	
	public String formatLastRunPoint() {
		String formattedLastRunPoint = null;
		RunPoint lastRunPoint = runPoints.get(runPoints.size());
		if (lastRunPoint != null) {
			Location lastLocation = lastRunPoint.getLocation();
			formattedLastRunPoint = String.format("%.6f, %.6f",
					lastLocation.getLatitude(), lastLocation.getLongitude());
		} else {
			formattedLastRunPoint = "Last run point not available";
		}
		return formattedLastRunPoint;
	}
}
