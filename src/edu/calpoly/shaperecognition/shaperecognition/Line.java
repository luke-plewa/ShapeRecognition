package edu.calpoly.shaperecognition.shaperecognition;

import java.util.ArrayList;

public class Line extends Shape {
	private ArrayList<Segment> segments;
	
	public Line(ArrayList<Segment> segments) {
		this.segments = segments;
	}
	
	public ArrayList<Segment> getSegments() {
		return segments;
	}
}
