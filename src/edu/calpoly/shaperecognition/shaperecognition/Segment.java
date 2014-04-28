package edu.calpoly.shaperecognition.shaperecognition;

import android.graphics.Point;

public class Segment {

	Point a, b;
	
	public Segment(Point a, Point b) {
		this.a = a;
		this.b = b;
	}
	
	/*
	public double getDegrees(Point a, Point b, Point c) {
		//Vector v1
		int v1_x = (a.x - b.x);
		int v1_y = (a.y - b.y);
		//Vector v2
		int v2_x = c.x - b.x;
		int v2_y = c.y - b.y;
		
		Double a_scalar = Math.sqrt(Math.pow(v1_x,2) + Math.pow(v1_y,2));
		Double b_scalar = Math.sqrt(Math.pow(v2_x,2) + Math.pow(v2_y,2));
	
		//angle is in radians!!
		Double angle = Math.acos(((v1_x*v2_x)+(v1_y*v2_y)) / (a_scalar*b_scalar));
		return angle * (180/Math.PI);
	}
	*/
	/*
	public static double getAngle(Segment a, Segment b) {
		//Vector v1
		int v1_x = (a.x - b.x);
		int v1_y = (a.y - b.y);
		//Vector v2
		int v2_x = c.x - b.x;
		int v2_y = c.y - b.y;
		
		Double a_scalar = Math.sqrt(Math.pow(v1_x,2) + Math.pow(v1_y,2));
		Double b_scalar = Math.sqrt(Math.pow(v2_x,2) + Math.pow(v2_y,2));
	
		//angle is in radians!!
		Double angle = Math.acos(((v1_x*v2_x)+(v1_y*v2_y)) / (a_scalar*b_scalar));
		return angle * (180/Math.PI);
		
		return 0.0;
	}
	*/
	
	public double distanceBetween() {
		return Math.sqrt(Math.pow((b.x-a.x),2) + Math.pow((b.y-a.y), 2));
	}
	
}
