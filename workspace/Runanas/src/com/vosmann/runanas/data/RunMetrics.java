package com.vosmann.runanas.data;

/**
 * The RunMetrics class encapsulates information about a segment of a run. 
 * Usually each RunPoint object has a corresponding RunMetrics object.
 * Of course, every Run object also has a RunMetrics object describing the 
 * entire run.
 * 
 * @author vosmann
 */
public class RunMetrics {
	//  Medium-sized banana.
	private static final double BANANA_ENERGY = 440.0; // In [kJ].
	
	private double distance; 
	private long duration;
	private double averageSpeed;
	private double mass;
	private double energyExpenditure;
	private double bananas;
	
	public RunMetrics(double mass) {
		this.distance = 0.0;
		this.duration = 0l;
		this.averageSpeed = 0.0;
		this.mass = mass;
		this.energyExpenditure = 0.0;
		this.bananas = 0.0;
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
		return averageSpeed;
	}
	public void setAverageSpeed(double averageSpeed) {
		this.averageSpeed = averageSpeed;
	}
	/** Expressed in kilograms [kg]. */
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
	/** Energy expenditure expressed in bananas. */
	public double getBananas() {
		return bananas;
	}
	public void setBananas(double bananas) {
		this.bananas = bananas;
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
		return String.format("%.2 km/h", averageSpeed);
	}
	public String formatMass() {
		return String.format("%.1f", mass);
	}
	public String formatEnergyExpenditure() {
		return String.format("%.3f kJ", energyExpenditure);
	}
	public String formatBananas() {
		return String.format("%.1f", bananas);
	}
}
