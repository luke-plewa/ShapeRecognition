package edu.calpoly.shaperecognition.shaperecognition;

import java.util.ArrayList;

import android.graphics.Point;
import android.util.Log;

public class ShapeRecognizer {
	
	private static final double LENGTH_TOLERANCE = 0.2;
	
	public static Vector recognizeShape(Vector shape){
		
		Vector ans = new Vector();
		
		if(isSquare(shape)){
			Log.d("Shape", "We have a square!");
		}else if(isTriangle()){
			
		}else if(isCircle()){
			
		}
		
		return ans;
	}

	private static boolean isSquare(Vector shape){
		Log.d("Shape", "Maybe it's a square");
		ArrayList<Segment> segments = shape.getShape();
		double angle1 = 0, angle2 = 0, angle3 = 0, angle4 = 0;
	
		if (segments.size() > 4) {
			segments = adjustSegments(segments);
		}
		
		if (segments.size() >= 4) {
			angle1 = segments.get(0).getAngle(segments.get(1));
			angle2 = segments.get(1).getAngle(segments.get(2));
			angle3 = segments.get(2).getAngle(segments.get(3));
			angle4 = segments.get(3).getAngle(segments.get(0));
		}
		
		if((Math.abs(angle1 - 90) < 20) && (Math.abs(angle2 - 90) < 20) &&
				(Math.abs(angle3 - 20) < 20) && (Math.abs(angle4 - 90) < 20)){
			return true;
		}
		
		return false;
	}
	
	private static ArrayList<Segment> adjustSegments(ArrayList<Segment> segments) {
		double avg_length = 0;
		for (int i = 0; i < segments.size(); i++) {
			Log.d("Shape", Double.toString(segments.get(i).getLength()));
			avg_length += segments.get(i).getLength();
		}
		avg_length /= segments.size();

		for (int i = 0; i < segments.size(); i++) {
			Segment curr = segments.get(i);
			if (curr.getLength() < LENGTH_TOLERANCE * avg_length) {
				Point midpoint = new Point();
				midpoint.x = (curr.start.x + curr.end.x ) / 2;
				midpoint.y = (curr.start.y + curr.end.y ) / 2;
				segments.remove(i);
				
				int before = 0, after = 0;
				if (i == 0) {
					before = segments.size() - 1;
					after = 0;
				} else if (i == segments.size() - 1) {
					before = 0;
					after = i;
				} else {
					before = i;
					after = i + 1;
				}
				
				Log.d("Shape", before + " " + after);
				
				segments.get(before).end.x = midpoint.x;
				segments.get(before).end.y = midpoint.y;
				segments.get(after).start.x = midpoint.x;
				segments.get(after).start.y = midpoint.y;
			}
		}
		return segments;
	}
	
	private static double getDegrees(Point a, Point b, Point c) {
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
	
	private static double distanceBetween(Point a, Point b) {
		return Math.sqrt(Math.pow((b.x-a.x),2) + Math.pow((b.y-a.y), 2));
	}
	
	private static boolean isDistanceEqual(Point a, Point b, Point c, Point d){
		//Log.d("Shape", "isDistanceEqual");
		double seg1 = distanceBetween(a, b);
		double seg2 = distanceBetween(c, d);
		
		if (Math.abs(seg1-seg2) < (.5 * Math.max(seg1, seg2))) {
			return true;
		}
		
		return false;
	}
	
	
	private static boolean isTriangle(){
		
		return false;
	}
	
	private static boolean isCircle(){
		
		return false;
	}
}
