package edu.calpoly.shaperecognition.shaperecognition;

import android.graphics.Point;

public class Segment {

	public Point start, end;
	
	public Segment(Point a, Point b) {
		this.start = a;
		this.end = b;
	}

	public double getAngle(Segment other) {
		//Vector v1
		int v1_x = (start.x - end.x);
		int v1_y = (start.y - end.y);
		//Vector v2
		int v2_x = other.start.x - other.end.x;
		int v2_y = other.start.y - other.end.y;
		
		Double a_scalar = Math.sqrt(Math.pow(v1_x,2) + Math.pow(v1_y,2));
		Double b_scalar = Math.sqrt(Math.pow(v2_x,2) + Math.pow(v2_y,2));
	
		//angle is in radians!!
		Double angle = Math.acos(((v1_x*v2_x)+(v1_y*v2_y)) / (a_scalar*b_scalar));
		return angle * (180/Math.PI);
	}
	
	public double getLength() {
		return Math.sqrt(Math.pow((end.x-start.x),2) + Math.pow((end.y-start.y), 2));
	}
	
}
