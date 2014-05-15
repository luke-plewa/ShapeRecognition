package edu.calpoly.shaperecognition.shaperecognition;

import java.util.ArrayList;

public class Triangle extends Shape {
	
	private ArrayList<Segment> segments;
	
	public Triangle(ArrayList<Segment> segments) {
		this.segments = segments;
	}
	
	public ArrayList<Segment> getSegments() {
		return segments;
	}

}
