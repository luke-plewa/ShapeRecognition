package edu.calpoly.shaperecognition.shaperecognition;

import java.util.ArrayList;

import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

public class Vector {
	
	private static final String TAG = "VECTOR";
	private static final double MIN_DISTANCE = 10.0; // TODO
	private static final double SCALAR_TOLERANCE = 15.0; // TODO
	private static final double STRAIGHT_LINE = 180.0;
	private static final double RIGHT_ANGLE = 90.0;
	
	private ArrayList<Point> points;
	private ArrayList<Segment> segments;
	
	public Vector() {
		points = new ArrayList<Point>();
		segments = new ArrayList<Segment>();
	}
	
	public Vector(ArrayList<Point> points) {
		this.points = points;
		segments = new ArrayList<Segment>();
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
	
	//angle between two parallel vectors:
	//if facing the same direction -> 0 degrees
	//if facing opposite directions -> 180 degrees
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
	
	public void processVector() {
		Point start_point = null, end_point = null;
		
		for (int i = 0; i + 2 < points.size(); i++) {
			Point a = points.get(i);
			Point b = points.get(i+1);
			Point c = points.get(i+2);
			
			double degrees = getDegrees(a, b, c);

			if(Math.abs(degrees - STRAIGHT_LINE) < SCALAR_TOLERANCE){
				degrees = STRAIGHT_LINE;
				if (start_point == null) {
					start_point = a;
				} else {
					end_point = c;
				}
			}else if(start_point != null && end_point != null){
				segments.add(new Segment(start_point, end_point));
				start_point = null;
				end_point = null;
			}
			
			Log.d(TAG, "Degrees: " + degrees);	
		}
		if(end_point != null && !segments.contains(end_point)){
			segments.add(new Segment(start_point, end_point));
			start_point = null;
			end_point = null;
		}
		
		Log.d("Shape", "segments size" + segments.size());
		
		if (segments.size() == 0) {
			segments.add(new Segment(new Point(), new Point()));
		}
		
	}
	
	public Shape getShape() {
		if (segments.isEmpty()) {
			processVector();
			if (!segments.isEmpty()){
				return ShapeRecognizer.recognizeShape(this);
			}
		}
		return null;
	}
	
	public ArrayList<Segment> getSegments() {
		if (segments.isEmpty()) {
			processVector();
			if (!segments.isEmpty()){
				ShapeRecognizer.recognizeShape(this);
			}
		}
		return segments;
	}
	
	public Rectangle getRect() {
		if (segments.isEmpty()) {
			processVector();
			if (!segments.isEmpty()){
				Log.d("Shape", "RECOGNIZIZIZIZNG");	
				return (Rectangle) ShapeRecognizer.recognizeShape(this);
			}
		}
		return null;
	}
}
