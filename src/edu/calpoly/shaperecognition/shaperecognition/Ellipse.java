package edu.calpoly.shaperecognition.shaperecognition;

public class Ellipse extends Shape {
	
	float cx, cy, radius;
	
	public Ellipse(float cx, float cy, float radius) {
		this.cx = cx;
		this.cy = cy;
		this.radius = radius;
	}
}
