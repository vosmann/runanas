package com.vosmann.runanas.model;

import android.location.Location;

/**
 * A RunPoint objects holds location information of a run point.
 * In this iteration it only contains a Location, but this can be extended at
 * any time.
 * @author vosmann
 */
public class RunPoint {
	private long runId; 
	private Location location;
	
	public RunPoint(Location location, long runId) {
//		UUID uuid = UUID.randomUUID();
//		this.runId = uuid.getMostSignificantBits(); // Unique enough.
		this.runId = runId;
		this.location = location;
	}

	public long getRunId() {
		return runId;
	}
	public Location getLocation() {
		return location;
	}
}

