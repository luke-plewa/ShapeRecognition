package edu.calpoly.shaperecognition.recognizer;

import java.util.ArrayList;

import android.graphics.Point;

public class Vector {

	private ArrayList<Point> points;
	
	public Vector() {
		points = new ArrayList<Point>();
	}
	
	public ArrayList<Point> getPoints() {
		return points;
	}
}
