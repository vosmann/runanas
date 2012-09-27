package com.vosmann.runanas.data;

import android.location.Location;

/**
 * A RunPoint objects holds location information of a run point. It additionally
 * contains some metrics about run segment started with the previous RunPoint
 * and leading up to this one.
 * 
 * @author vosmann
 */
public class RunPoint {
	private Location location;
	private RunMetrics runMetrics;
	
	public RunPoint(Location location) {
		this.location = location;
		this.runMetrics = new RunMetrics(mass);
	}
}

