package edu.calpoly.shaperecongition.drawingcanvas;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import edu.calpoly.shaperecognition.shaperecognition.Ellipse;
import edu.calpoly.shaperecognition.shaperecognition.Line;
import edu.calpoly.shaperecognition.shaperecognition.Rectangle;
import edu.calpoly.shaperecognition.shaperecognition.Segment;
import edu.calpoly.shaperecognition.shaperecognition.Shape;
import edu.calpoly.shaperecognition.shaperecognition.Triangle;
import edu.calpoly.shaperecognition.shaperecognition.Vector;

public class DrawingPanel extends View {
	private ArrayList<Path> _graphics = new ArrayList<Path>();
	private ArrayList<Vector> _vectors = new ArrayList<Vector>();
	private Paint mPaint;
	private Paint pointPaint;
	private Paint rectPaint;

	private Path path;
	private Vector vector;

	public DrawingPanel(Context context) {
		super(context);

		mPaint = new Paint();
		mPaint.setDither(true);
		mPaint.setColor(Color.WHITE);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeJoin(Paint.Join.ROUND);
		mPaint.setStrokeCap(Paint.Cap.ROUND);
		mPaint.setStrokeWidth(5);

		pointPaint = new Paint();
		pointPaint.setDither(true);
		pointPaint.setColor(Color.RED);
		pointPaint.setStyle(Paint.Style.STROKE);
		pointPaint.setStrokeJoin(Paint.Join.ROUND);
		pointPaint.setStrokeCap(Paint.Cap.ROUND);
		pointPaint.setStrokeWidth(5);
		pointPaint.setAntiAlias(true);

		rectPaint = new Paint();
		rectPaint.setDither(true);
		rectPaint.setColor(Color.GREEN);
		rectPaint.setStyle(Paint.Style.STROKE);
		rectPaint.setStrokeJoin(Paint.Join.ROUND);
		rectPaint.setStrokeCap(Paint.Cap.ROUND);
		rectPaint.setStrokeWidth(5);
		rectPaint.setAntiAlias(true);
	}

	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			path = new Path();
			vector = new Vector();
			Log.d("Called", "Called!");
			path.moveTo(event.getX(), event.getY());
			path.lineTo(event.getX(), event.getY());
			vector.addPoint(new Point((int) event.getX(), (int) event.getY()));
		} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
			path.lineTo(event.getX(), event.getY());
			vector.addPoint(new Point((int) event.getX(), (int) event.getY()));
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			path.lineTo(event.getX(), event.getY());
			vector.addPoint(new Point((int) event.getX(), (int) event.getY()));
			_graphics.add(path);
			_vectors.add(vector);
			vector = null;
			path = null;
		}
		invalidate();

		return true;
	}

	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		canvas.drawColor(Color.BLACK);

		if (path != null) {
			canvas.drawPath(path, mPaint);
		}
		/*
		 * for (Path path : _graphics) { //canvas.drawPoint(graphic.x,
		 * graphic.y, mPaint); canvas.drawPath(path, mPaint); }
		 */

		// Delete after debugging
		for (Vector v : _vectors) {
			Shape shape = v.getShape();
			if (shape instanceof Rectangle) {
				Rectangle r = (Rectangle) shape;
				canvas.drawRect(r.getRect(), rectPaint);
			} else if (shape instanceof Triangle) {
				Triangle tri = (Triangle) shape;
				ArrayList<Segment> lines = tri.getSegments();
				for (int i = 0; i < lines.size(); i++) {
					Segment curr = lines.get(i);
					canvas.drawLine(curr.start.x, curr.start.y, curr.end.x,
							curr.end.y, rectPaint);
				}
			} else if (shape instanceof Ellipse) {
				Ellipse ellipse = (Ellipse) shape;
				canvas.drawOval(ellipse.getRectF(), rectPaint);
			} else if (shape instanceof Line) {
				Line line = (Line) shape;
				Segment s = line.getSegments().get(0);
				canvas.drawLine(s.start.x, s.start.y, s.end.x, s.end.y, rectPaint);
			}

		}
	}
}
