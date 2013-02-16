package com.vosmann.runanas.model;

import java.util.List;

import android.util.Log;

/**
 * The RunMetrics class encapsulates information about a segment of a run. 
 * Usually each RunPoint object has a corresponding RunMetrics object.
 * Of course, every Run object also has a RunMetrics object describing the 
 * entire run.
 * 
 * @author vosmann
 */
public class RunMetrics {
	private static final String TAG = "RunMetrics";
	
	private double distance; 
	private long duration;
	private double avgSpeed;
	private double maxSpeed;
	private double minSpeed;
	private double mass;
	private double energyExpenditure;
	
	public RunMetrics(double mass) {
		this.distance = 0.0;
		this.duration = 0l;
		this.avgSpeed = 0.0;
		this.maxSpeed = 0.0;
		this.minSpeed = 0.0;
		this.mass = mass;
		this.energyExpenditure = 0.0;
	}
	
	public RunMetrics(double mass, List<RunPoint> runPoints) {
		this(mass);
		int nrPoints = runPoints.size();
		if (nrPoints >= 2) {
			for (int i = 1; i < nrPoints; ++i) {
				double segmentDistance = 0.0;
				long segmentDuration = 0;
				update(segmentDistance, segmentDuration);
			}
		} else {
			Log.e(TAG, "Less than two RunPoints given to constructor.");
		}
	}
	
	/** The distance ran. Expressed in meters. */
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
			this.distance = distance;
	}
	/** Expressed in milliseconds. */
	public long getDuration() {
		return duration;
	}
	public void setDuration(long duration) {
		this.duration = duration;
	}
	/** Expressed in kilometers per hour. */
	public double getAverageSpeed() {
		return avgSpeed;
	}
	public void setAverageSpeed(double averageSpeed) {
		this.avgSpeed = averageSpeed;
	}
	/** Expressed in kilometers per hour. */
	public double getFastestSpeed() {
		return maxSpeed;
	}
	public void setFastestSpeed(double fastestSpeed) {
		this.maxSpeed = fastestSpeed;
	}
	/** Expressed in kilometers per hour. */
	public double getSlowestSpeed() {
		return minSpeed;
	}
	public void setSlowestSpeed(double slowestSpeed) {
		this.minSpeed = slowestSpeed;
	}
	/**
	 * Gets the mass of the runner.
	 * @return Mass expressed in kilograms [kg].
	 */
	public double getMass() {
		return mass;
	}
	public void setMass(double mass) {
		this.mass = mass;
	}
	/** Expressed in joules [J]. */
	public double getEnergyExpenditure() {
		return energyExpenditure;
	}
	public void setEnergyExpenditure(double energyExpenditure) {
		this.energyExpenditure = energyExpenditure;
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
			calculateEnergyExpenditure();
		}
	}
	
	/**
	 * This is an rough approximation of the energy rate while running 10km/h.
	 * This method will, of course, need revision.
	 */
	private void calculateEnergyExpenditure() {
		final double kjPerKgPerSecond = 0.0129138;
		energyExpenditure = mass * (duration / 1000) * kjPerKgPerSecond;
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
	public double getEnergyExpenditureIn(Nutrient nutrient) {
		double energyExpenditureIn = 0.0;
		energyExpenditureIn = energyExpenditure / nutrient.getEnergy(); 
		return energyExpenditureIn;
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
	public String formatEnergyExpenditure() {
		return String.format("%.3f kJ", energyExpenditure);
	}
}
