package edu.calpoly.shaperecognition.shaperecognition;

import java.util.ArrayList;

import android.graphics.Point;
import android.util.Log;

public class ShapeRecognizer {
	
	
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
		ArrayList<Point> lines = shape.getShape();
		boolean seg1 = false, seg2 = false;
		double angle1 = 0, angle2 = 0, angle3 = 0, angle4 = 0;
		
		if(lines.size()/2 == 4){
			seg1 = isDistanceEqual(lines.get(0), lines.get(1), lines.get(4), lines.get(5));
			seg2 = isDistanceEqual(lines.get(2), lines.get(3), lines.get(6), lines.get(7));
			
			
			if(seg1 && seg2){
				
				angle1 = getDegrees(lines.get(0), lines.get(1), lines.get(3));
				angle2 = getDegrees(lines.get(1), lines.get(3), lines.get(5));
				angle3 = getDegrees(lines.get(4), lines.get(5), lines.get(7));
				angle4 = getDegrees(lines.get(6), lines.get(7), lines.get(1));
				
				if((Math.abs(angle1 - 90) < 20) && (Math.abs(angle2 - 90) < 20) &&
						(Math.abs(angle3 - 20) < 20) && (Math.abs(angle4 - 90) < 20)){
					
					return true;

				}			
			}
		}
		
		return false;
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
