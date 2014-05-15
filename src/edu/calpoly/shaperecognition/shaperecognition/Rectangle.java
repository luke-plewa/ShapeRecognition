package edu.calpoly.shaperecognition.shaperecognition;

import android.graphics.Rect;

public class Rectangle extends Shape {
	
	private Rect r;
	
	public Rectangle(int left, int top, int right, int bottom) {
		// TODO Auto-generated constructor stub
		r = new Rect(left, top, right, bottom);
	}

	public Rect getRect() {
		return r;
	}
}
