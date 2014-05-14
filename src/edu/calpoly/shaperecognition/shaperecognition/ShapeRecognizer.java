package edu.calpoly.shaperecognition.shaperecognition;

import java.util.ArrayList;

import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

public class ShapeRecognizer {
	
	private static final double LENGTH_TOLERANCE = 0.2;
	private static final double DEG_TOLERANCE = 20;
	private static final double RIGHT_ANGLE = 90;
	
	public static Rect recognizeShape(Vector shape){
		
		if(isSquare(shape)){
			Log.d("Shape", "We have a square!");
			return makeRectangle(shape);
		}else if(isTriangle()){
			
		}else if(isCircle()){
			
		}
		
		return null;
	}
	
	public static Rect makeRectangle(Vector shape) {
		ArrayList<Segment> segments = shape.getShape();
		
		while (canBeSquished(segments, 4)) {
			segments = adjustSegments(segments);
		}
		
		Point center = new Point();
		center.x += segments.get(0).start.x;
		center.y += segments.get(0).start.y;
		center.x += segments.get(2).start.x;
		center.y += segments.get(2).start.y;
		center.x /= 2;
		center.y /= 2;
		
		Segment right_segment = new Segment();
		Segment left_segment = new Segment();
		Segment top_segment = new Segment();
		Segment bottom_segment = new Segment();
		
		for (int i = 0; i < segments.size(); i++) {
			Point start = segments.get(i).start;
			Point end = segments.get(i).end;
			if (start.x > center.x && end.x > center.x) {
				right_segment = segments.get(i);
			} else if (start.x < center.x && end.x < center.x) {
				left_segment = segments.get(i);
			} else if (start.y < center.y && end.y < center.y) {
				top_segment = segments.get(i);
			} else if (start.y > center.y && end.y > center.y) {
				bottom_segment = segments.get(i);
			}
		}
		
		double length_1 = top_segment.getLength() + bottom_segment.getLength();
		length_1 /= 2;
		double length_2 = right_segment.getLength() + left_segment.getLength();
		length_2 /= 2;

		int left = (int) (center.x - length_1 / 2);
		int top = (int) (center.y - length_2 / 2);
		int bottom = (int) (center.y + length_2 / 2);
		int right = (int) (center.x + length_1 / 2);

		Rect r = new Rect(left, top, right, bottom);

		Log.d("Shape", "points: " + left + top + right + bottom);	
		
		return r;
	}
	
	private static double avgLength(ArrayList<Segment> segments) { 
		double avg_length = 0;
		
		for (int i = 0; i < segments.size(); i++) {
			avg_length += segments.get(i).getLength();
		}
		avg_length /= segments.size();
		
		return avg_length;
	}
	
	private static boolean canBeSquished(ArrayList<Segment> segments, int size) {
		boolean small_segment = false;
		double avg_length = avgLength(segments);
		
		for (int i = 0; i < segments.size(); i++) {
			if (segments.get(i).getLength() < LENGTH_TOLERANCE * avg_length) { 
				small_segment = true;
			}
		}
		
		return segments.size() > size && small_segment;
	}

	private static boolean isSquare(Vector shape){
		Log.d("Shape", "Maybe it's a square");
		ArrayList<Segment> segments = shape.getShape();
		double angle1 = 0, angle2 = 0, angle3 = 0, angle4 = 0;
	
		while (canBeSquished(segments, 4)) {
			segments = adjustSegments(segments);
		}
		
		if (segments.size() >= 4) {
			angle1 = segments.get(0).getAngle(segments.get(1));
			angle2 = segments.get(1).getAngle(segments.get(2));
			angle3 = segments.get(2).getAngle(segments.get(3));
			angle4 = segments.get(3).getAngle(segments.get(0));
		}
		
		if((Math.abs(angle1 - RIGHT_ANGLE) < DEG_TOLERANCE)
				&& (Math.abs(angle2 - RIGHT_ANGLE) < DEG_TOLERANCE) &&
				(Math.abs(angle3 - RIGHT_ANGLE) < DEG_TOLERANCE)
				&& (Math.abs(angle4 - RIGHT_ANGLE) < DEG_TOLERANCE)){
			return true;
		}
		
		return false;
	}
	
	private static ArrayList<Segment> adjustSegments(ArrayList<Segment> segments) {
		double avg_length = avgLength(segments);

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
				Log.d("Shape", "size " + segments.size());
				
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
