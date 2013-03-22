package com.vosmann.runanas.model;

import android.util.Log;

/**
 * The RunResult class encapsulates information about a segment of a run. 
 * Usually each RunPoint object has a corresponding RunResult object.
 * Of course, every Run object also has a RunResult object describing the 
 * entire run.
 * 
 * @author vosmann
 */
public class RunResult {
	private static final String TAG = "RunResult";
	
	private long runId; 
	private long time; 
	private double distance; 
	private long duration;
	private double avgSpeed;
	private double maxSpeed;
	private double minSpeed;
	private double mass;
	private double energy;
	
	public RunResult(long runId, double mass) {
		this.runId = runId;
		this.time = 0l;
		this.distance = 0.0;
		this.duration = 0l;
		this.avgSpeed = 0.0;
		this.maxSpeed = 0.0;
		this.minSpeed = 0.0;
		this.mass = mass;
		this.energy = 0.0;
	}
	
	// Factory instead...?
	public RunResult(long runId, long time, double distance, long duration,
			double avgSpeed, double maxSpeed, double minSpeed, double mass,
			double energy) {
		this.runId = runId;
		this.time = time;
		this.distance = distance;
		this.duration = duration;
		this.avgSpeed = avgSpeed;
		this.maxSpeed = maxSpeed;
		this.minSpeed = minSpeed;
		this.mass = mass;
		this.energy = energy;
	}
	
//	public RunResult(long runId, double mass, List<RunPoint> runPoints) {
//		this(runId, mass);
//		int nrPoints = runPoints.size();
//		if (nrPoints >= 2) {
//			for (int i = 1; i < nrPoints; ++i) {
//				// TODO
//				double segmentDistance = 0.0;
//				long segmentDuration = 0;
//				update(segmentDistance, segmentDuration);
//			}
//		} else {
//			Log.e(TAG, "Less than two RunPoints given to constructor.");
//		}
//	}

	/** The ID of the Run this RunResult belongs to. */
	public long getRunId() {
		return runId;
	}
	/** The time this result were finished (end-of-run). */
	public long getTime() {
		return time;
	}
	/** The distance ran. Expressed in meters. */
	public double getDistance() {
		return distance;
	}
	/** Expressed in milliseconds. */
	public long getDuration() {
		return duration;
	}
	/** Expressed in kilometers per hour. */
	public double getAvgSpeed() {
		return avgSpeed;
	}
	/** Expressed in kilometers per hour. */
	public double getMaxSpeed() {
		return maxSpeed;
	}
	/** Expressed in kilometers per hour. */
	public double getMinSpeed() {
		return minSpeed;
	}
	/**
	 * Gets the mass of the runner.
	 * @return Mass expressed in kilograms [kg].
	 */
	public double getMass() {
		return mass;
	}
	/** Expressed in joules [J]. */
	public double getEnergy() {
		return energy;
	}

	
	public void update(double segmentDistance, long segmentDuration) {
		if (segmentDistance < 1.0 || segmentDuration == 0) {
			Log.w(TAG, "Segment too short to update.");
		} else {
			distance += segmentDistance;
			duration += segmentDuration;
			avgSpeed = (distance / duration) * 3600.0f; // [km/h].
			double segmentSpeed = (segmentDistance / segmentDuration) * 3600.0f;
			if (segmentSpeed > maxSpeed) {
				maxSpeed = segmentSpeed;
			}
			if (segmentSpeed < minSpeed) {
				minSpeed = segmentSpeed;
			}
			calculateEnergy();
		}
	}
	
	/**
	 * This is an rough approximation of the energy rate while running 10km/h.
	 * This method will, of course, need revision.
	 */
	private void calculateEnergy() {
		final double kjPerKgPerSecond = 0.0129138;
		energy = mass * (duration / 1000) * kjPerKgPerSecond;
	}
	
	/**
	 * Gets the energy expenditure expressed specifically in a type of
	 * nutrient (food or drink).
	 * @param nutrient
	 * 		Type of the nutrient, e.g. "banana".
	 * @return
	 * 		Double value representing the number of, for example, bananas
	 * 		burned.
	 */
	public double getEnergyIn(Nutrient nutrient) {
		double energyIn = 0.0;
		energyIn = energy / nutrient.getEnergy(); 
		return energyIn;
	}
	
	public String formatDistance() {
		return String.format("%.2f m", distance);
	}
	public String formatDuration() {
		long durationSecs = duration / 1000L;
		long durationMins = durationSecs / 60L;
		long durationHours = durationMins / 60L;
		return String.format("%d:%d:%d",
				durationHours, durationMins, durationSecs);
	}
	public String formatAverageSpeed() {
		return String.format("%.2 km/h", avgSpeed);
	}
	public String formatMass() {
		return String.format("%.1f", mass);
	}
	public String formatEnergy() {
		return String.format("%.3f kJ", energy);
	}
}
