package edu.calpoly.shaperecognition.shaperecognition;

public class Ellipse extends Shape {
	
	private double cx, cy, radius;
	
	public Ellipse(double center_x, double center_y, double radius_x) {
		this.cx = center_x;
		this.cy = center_y;
		this.radius = radius_x;
	}
	
	public double getCX() {
		return cx;
	}
	
	public double getCY() {
		return cy;
	}
	
	public double getRadius() {
		return radius;
	}
}
