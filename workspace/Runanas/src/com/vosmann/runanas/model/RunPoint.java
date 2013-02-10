package com.vosmann.runanas.model;

import java.util.UUID;

import android.location.Location;

/**
 * A RunPoint objects holds location information of a run point.
 * In this iteration it only contains a Location, but this can be extended at
 * any time.
 * @author vosmann
 */
public class RunPoint {
	private long id; 
	private Location location;
	
	public RunPoint(Location location) {
		UUID uuid = UUID.randomUUID();
		this.id = uuid.getMostSignificantBits(); // Unique enough.
		this.location = location;
	}

	public long getId() {
		return id;
	}
	public Location getLocation() {
		return location;
	}
}

