package edu.calpoly.shaperecognition.shaperecognition;

import java.util.ArrayList;

import android.graphics.Point;
import android.util.Log;

public class Vector {
	
	private static final String TAG = "VECTOR";
	private static final double MIN_DISTANCE = 3.0; // TODO
	private static final int SCALAR_TOLERANCE = 100; // TODO
	
	private ArrayList<Point> points;
	
	public Vector() {
		points = new ArrayList<Point>();
	}
	
	public Vector(ArrayList<Point> points) {
		this.points = points;
	}
	
	public void addPoint(Point point) {
		Log.d(TAG, "Point: " + point.x + " " + point.y);
		if (points.size() == 0 || distanceBetween(point, points.get(points.size()-1)) > MIN_DISTANCE) {
			points.add(point);
			Log.d(TAG, "Added Point: " + point.x + " " + point.y);
		}
	}
	
	private double distanceBetween(Point a, Point b) {
		return Math.sqrt(Math.pow((b.x-a.x),2) + Math.pow((b.y-a.y), 2));
	}
	
	public ArrayList<Point> getPoints() {
		return points;
	}
	
	public void processVector() {
		for (int i = 0; i < points.size() && i + 2 < points.size(); i++) {
			Point a = points.get(i);
			Point b = points.get(i+1);
			Point c = points.get(i+2);

		/*	Double ab = distanceBetween(a, b);
			Double ac = distanceBetween(a, c);
			Double bc = distanceBetween(b, c);
			
			Double ab2 = Math.pow(ab, 2); //ab squared
			Double ac2 = Math.pow(ac, 2); //ac squared
			Double bc2 = Math.pow(bc, 2); //bc squared
						
			Double angle = Math.acos((ab2 + bc2 - ac2)/(2 * ab2 * bc2));*/
			
			int v1_x = b.x - a.x;
			int v1_y = b.y - a.y;
			int v2_x = c.x - b.x;
			int v2_y = c.y - b.y;
			
			Double a_scalar = Math.sqrt(Math.pow(v1_x,2) + Math.pow(v1_y,2));
			Double b_scalar = Math.sqrt(Math.pow(v2_x,2) + Math.pow(v2_y,2));
		
			Double angle;
			
			if((a_scalar*b_scalar) > SCALAR_TOLERANCE){
				angle = Math.acos(((v1_x*v2_x)+(v1_y*v2_y)) / (a_scalar*b_scalar));
			}else{
				angle = Math.PI;
			}
			
			Log.d(TAG, "scalar: " + a_scalar + " " + b_scalar);
			Log.d(TAG, "angle: " + angle);
			
			Double degrees = angle * (180/Math.PI);
			Log.d(TAG, "Degrees: " + degrees);
		}
	}
}
