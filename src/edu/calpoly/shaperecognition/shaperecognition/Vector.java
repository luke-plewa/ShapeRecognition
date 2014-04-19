package edu.calpoly.shaperecognition.shaperecognition;

import java.util.ArrayList;

import android.graphics.Point;
import android.util.Log;

public class Vector {
	
	private static final String TAG = "VECTOR";
	private static final double MIN_DISTANCE = 3.0; // TODO
	private static final double SCALAR_TOLERANCE = 10.0; // TODO
	private static final double STRAIGHT_LINE = 180.0;
	private static final double RIGHT_ANGLE = 90.0;
	
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
			Double degrees = angle * (180/Math.PI);

			Log.d(TAG, "angle: " + angle);

			//angle between two parallel vectors:
			//if facing the same direction -> 0 degrees
			//if facing opposite directions -> 180 degrees
			if(Math.abs(degrees - STRAIGHT_LINE) < SCALAR_TOLERANCE){
				degrees = STRAIGHT_LINE;
				Log.d(TAG, "Straight Line Drawn");
				
			}else if(Math.abs(degrees - RIGHT_ANGLE) < SCALAR_TOLERANCE){
				degrees = RIGHT_ANGLE;
				Log.d(TAG, "Right Angle Drawn");
			}
			Log.d(TAG, "Degrees: " + degrees);

			
		}
	}
}
