package com.vosmann.runanas.model;

public enum Nutrient {
	
	//  Medium-sized banana.
	BANANA(440.0);
	// Should add candy bar, sandwich, croissant, beer, etc.

	private double energy;
	
	private Nutrient(double energy) {
		this.energy = energy;
	}
	
	/**
	 * The energy of the nutrient expressed in kilojoules [kJ].
	 * @return
	 */
	public double getEnergy() {
		return energy;
	}
}
