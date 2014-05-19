package edu.calpoly.shaperecognition.shaperecognition;

import android.graphics.RectF;

public class Ellipse extends Shape {
	
	private double cx, cy, radius_x, radius_y;
	private RectF rectF;
	
	public Ellipse(double center_x, double center_y, double radius_x, double radius_y) {
		this.cx = center_x;
		this.cy = center_y;
		this.radius_x = radius_x;
		this.radius_y = radius_y;
		rectF = new RectF((float) (center_x - radius_x), (float) (center_y - radius_y),
				(float) (center_x + radius_x), (float) (center_y + radius_y));
	}
	
	public double getCX() {
		return cx;
	}
	
	public double getCY() {
		return cy;
	}
	
	public double getRadius() {
		return radius_x;
	}
	
	public RectF getRectF() {
		return rectF;
	}
}
