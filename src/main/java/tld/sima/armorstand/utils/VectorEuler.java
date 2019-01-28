package tld.sima.armorstand.utils;

import org.bukkit.Location;

public class VectorEuler {
	
	double radius;
	double theta;
	
	public VectorEuler(Location loc) {
		double radiussquared = Math.pow(loc.getX(), 2) + Math.pow(loc.getZ(), 2);
		radius = Math.sqrt(radiussquared);
		theta = Math.atan2(loc.getZ(), loc.getX());
	}
	
	public void addRadian(double theta) {
		this.theta += theta;
	}
	
	public void addDegrees(double theta) {
		double radian = theta * Math.PI/180.0;
		this.theta += radian;
	}
	
	public double getX() {
		return radius*Math.cos(theta);
	}
	
	public double getZ() {
		return radius*Math.sin(theta);
	}
}
