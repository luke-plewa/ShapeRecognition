package edu.calpoly.shaperecognition.shaperecognition;

import java.util.ArrayList;

import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

public class ShapeRecognizer {
	
	private static final double LENGTH_TOLERANCE = 0.2;
	private static final double DEG_TOLERANCE = 20;
	private static final double RIGHT_ANGLE = 90;
	private static final double TRIANGLE_SUM = 180;
	private static final double TRI_TOLERANCE = 20;
	private static final double ELLIPSE_TOLERANCE = 1.3;
	private static final double ELLIPSE_LOWER_TOLERANCE = 0.7;
	private final static boolean DEBUG = false;
	
	public static Shape recognizeShape(Vector shape){
		
		if(isSquare(shape)){
			if (DEBUG) Log.d("Shape", "We have a square!");
			return makeRectangle(shape);
		}else if(isTriangle(shape)){
			return makeTriangle(shape);
		}else if(isCircle(shape)){
			if (DEBUG) Log.d("Shape", "We have a circle!");
			return makeEllipse(shape);
		}
		
		return null;
	}
	
	public static Ellipse makeEllipse(Vector shape) {
		if (DEBUG) Log.d("Shape", "Maybe it's a circle");
		ArrayList<Point> points = shape.getPoints();
		double max_x = 0, max_y = 0, min_x = Double.MAX_VALUE, min_y = Double.MAX_VALUE;
		
		for (int i = 0; i < points.size(); i++) {
			Point p = points.get(i);
			if (p.x < min_x) {
				min_x = p.x;
			}
			if (p.x > max_x) {
				max_x = p.x;
			}
			if (p.y < min_y) {
				min_y = p.y;
			}
			if (p.y > max_y) {
				max_y = p.y;
			}
		}
		
		double center_x = (max_x + min_x) / 2;
		double center_y = (max_y + min_y) / 2;
		double radius_x = Math.abs(max_x - min_x) / 2;
		double radius_y = Math.abs(max_y - min_y) / 2;
		
		if (DEBUG) Log.d("Shape", max_x + " " + min_x + " " + max_y + " " + min_y);
		if (DEBUG) Log.d("Shape", center_x + " " + center_y + " " + radius_x + " " + radius_y);
		
		return new Ellipse(center_x, center_y, radius_x, radius_y);
	}
	
	public static Triangle makeTriangle(Vector shape) {
		ArrayList<Segment> segments = shape.getSegments();
		
		while (canBeSquished(segments, 3)) {
			segments = adjustSegments(segments);
		}
		
		for (int i = 0; i < segments.size(); i++) {
			int offset = i + 1;
			if (i + 1 == segments.size()) {
				offset = 0;
			}
			
			Point cur = new Point(segments.get(i).end.x, segments.get(i).end.y);
			Point next = new Point(segments.get(offset).start.x, segments.get(offset).start.y);
			Point midpoint = new Point((cur.x + next.x) / 2, (cur.y + next.y) / 2);
			
			segments.get(i).end.x = midpoint.x;
			segments.get(i).end.y = midpoint.y;
			segments.get(offset).start.x = midpoint.x;
			segments.get(offset).start.y = midpoint.y;
		}
		
		Triangle tri = new Triangle(segments);
		
		return tri;
	}
	
	public static Rectangle makeRectangle(Vector shape) {
		ArrayList<Segment> segments = shape.getSegments();
		
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

		Rectangle r = new Rectangle(left, top, right, bottom);

		if (DEBUG) Log.d("Shape", "points: " + left + top + right + bottom);	
		
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
	
	private static double longest(ArrayList<Segment> segments) {
		double longest = 0;
		
		for (int i = 0; i < segments.size(); i++) {
			if (segments.get(i).getLength() > longest) { 
				longest = segments.get(i).getLength();
			}
		}
		
		return longest;
	}
	
	private static boolean canBeSquished(ArrayList<Segment> segments, int size) {
		boolean small_segment = false;
		double longest_length = longest(segments);
		
		for (int i = 0; i < segments.size(); i++) {
			if (segments.get(i).getLength() < LENGTH_TOLERANCE * longest_length) { 
				small_segment = true;
			}
		}
		
		return segments.size() > size && small_segment;
	}

	private static boolean isSquare(Vector shape){
		if (DEBUG) Log.d("Shape", "Maybe it's a square");
		ArrayList<Segment> segments = shape.getSegments();
		double angle1 = 0, angle2 = 0, angle3 = 0, angle4 = 0;
	
		while (canBeSquished(segments, 4)) {
			segments = adjustSegments(segments);
		}
		
		if (segments.size() == 4) {
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
		
		if (DEBUG) Log.d("Shape", "It's not a square");
		return false;
	}
	
	private static ArrayList<Segment> adjustSegments(ArrayList<Segment> segments) {
		double longest_length = longest(segments);

		for (int i = 0; i < segments.size(); i++) {
			Segment curr = segments.get(i);
			if (curr.getLength() < LENGTH_TOLERANCE * longest_length) {
				Point midpoint = new Point();
				midpoint.x = (curr.start.x + curr.end.x ) / 2;
				midpoint.y = (curr.start.y + curr.end.y ) / 2;
				segments.remove(i);
				
				int before = 0, after = 0;
				if (i == 0) {
					before = segments.size() - 1;
					after = 0;
				} else if (i >= segments.size() - 1) {
					before = segments.size() - 1;
					after = 0;
				} else {
					before = i;
					after = i + 1;
				}
				
				if (DEBUG) Log.d("Shape", before + " " + after);
				if (DEBUG) Log.d("Shape", "size " + segments.size());
				
				segments.get(before).end.x = midpoint.x;
				segments.get(before).end.y = midpoint.y;
				segments.get(after).start.x = midpoint.x;
				segments.get(after).start.y = midpoint.y;
			}
			
			if (DEBUG) Log.d("Shape", "Got here " + i);
		}
		
		if (DEBUG) Log.d("Shape", "Got here 3");
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
		//if (DEBUG) Log.d("Shape", "isDistanceEqual");
		double seg1 = distanceBetween(a, b);
		double seg2 = distanceBetween(c, d);
		
		if (Math.abs(seg1-seg2) < (.5 * Math.max(seg1, seg2))) {
			return true;
		}
		
		return false;
	}
	
	
	private static boolean isTriangle(Vector shape){
		if (DEBUG) Log.d("Shape", "Maybe it's a triangle");
		ArrayList<Segment> segments = shape.getSegments();
		double angle1 = 0, angle2 = 0, angle3 = 0;
	
		if (DEBUG) Log.d("Shape", "Start squishing " + segments.size());
		while (canBeSquished(segments, 3)) {
			segments = adjustSegments(segments);
		}
		if (DEBUG) Log.d("Shape", "End squishing");
		
		if (segments.size() == 3) {
			angle1 = TRIANGLE_SUM - segments.get(0).getAngle(segments.get(1));
			angle2 = TRIANGLE_SUM - segments.get(1).getAngle(segments.get(2));
			angle3 = TRIANGLE_SUM - segments.get(2).getAngle(segments.get(0));
		}
		
		if (DEBUG) Log.d("Shape", "Tri: " + angle1 + " " + angle2 + " " + angle3);
		
		if(Math.abs(angle1 + angle2 + angle3 - TRIANGLE_SUM) < TRI_TOLERANCE){
			if (DEBUG) Log.d("Shape", "It's a triangle");
			return true;
		}
		
		if (DEBUG) Log.d("Shape", "It's not a triangle");
		return false;
	}
	
	private static boolean isCircle(Vector shape){
		if (DEBUG) Log.d("Shape", "Maybe it's a circle");
		ArrayList<Segment> segments = shape.getSegments();
		ArrayList<Point> points = shape.getPoints();
		boolean isCircle = true;
		double max_x = 0, max_y = 0, min_x = 0, min_y = 0;
		
		for (int i = 0; i < points.size(); i++) {
			Point p = points.get(i);
			if (p.x < min_x) {
				min_x = p.x;
			}
			if (p.x > max_x) {
				max_x = p.x;
			}
			if (p.y < min_y) {
				min_y = p.y;
			}
			if (p.y > max_y) {
				max_y = p.y;
			}
		}
		
		double center_x = (max_x + min_x) / 2;
		double center_y = (max_y + min_y) / 2;
		double radius_x = Math.abs(max_x - min_x);
		double radius_y = Math.abs(max_y - min_y);
		
		for (int i = 0; i < points.size(); i++) {
			Point p = points.get(i);
			double lhs = Math.pow((p.x - center_x), 2) / Math.pow(radius_x, 2);
			lhs += Math.pow((p.y - center_y), 2) / Math.pow(radius_y, 2);
			if (lhs > ELLIPSE_TOLERANCE || lhs < ELLIPSE_LOWER_TOLERANCE) {
				isCircle = false;
			}
		}
		
		if (DEBUG) Log.d("Shape", "Maybe it's a circle end");
		return isCircle;
	}
}
