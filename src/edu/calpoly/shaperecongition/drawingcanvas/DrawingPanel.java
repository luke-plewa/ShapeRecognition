package edu.calpoly.shaperecongition.drawingcanvas;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import edu.calpoly.shaperecognition.shaperecognition.Rectangle;
import edu.calpoly.shaperecognition.shaperecognition.Segment;
import edu.calpoly.shaperecognition.shaperecognition.Shape;
import edu.calpoly.shaperecognition.shaperecognition.Triangle;
import edu.calpoly.shaperecognition.shaperecognition.Vector;

public class DrawingPanel extends SurfaceView implements SurfaceHolder.Callback 
{
    private ArrayList<Path> _graphics = new ArrayList<Path>();
    private ArrayList<Vector> _vectors = new ArrayList<Vector>();
    private Path curPath;
    private Paint mPaint;
    private Paint pointPaint;
    private Paint rectPaint;

    private DrawingThread _thread;
    private Path path;
    private Vector vector;

    public DrawingPanel(Context context) 
    {
        super(context);
        getHolder().addCallback(this);
        _thread = new DrawingThread(getHolder(), this);
        
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
        
        rectPaint = new Paint();
        rectPaint.setDither(true);
        rectPaint.setColor(Color.GREEN);
        rectPaint.setStyle(Paint.Style.STROKE);
        rectPaint.setStrokeJoin(Paint.Join.ROUND);
        rectPaint.setStrokeCap(Paint.Cap.ROUND);
        rectPaint.setStrokeWidth(5);
    }


    public boolean onTouchEvent(MotionEvent event) 
    {
        synchronized (_thread.getSurfaceHolder()) {
            if(event.getAction() == MotionEvent.ACTION_DOWN){
                path = new Path();
                vector = new Vector();
                Log.d("Called", "Called!");
                path.moveTo(event.getX(), event.getY());
                path.lineTo(event.getX(), event.getY());
                vector.addPoint(new Point((int)event.getX(), (int)event.getY()));
            }else if(event.getAction() == MotionEvent.ACTION_MOVE){
                path.lineTo(event.getX(), event.getY());
                vector.addPoint(new Point((int)event.getX(), (int)event.getY()));
            }else if(event.getAction() == MotionEvent.ACTION_UP){
                path.lineTo(event.getX(), event.getY());
                vector.addPoint(new Point((int)event.getX(), (int)event.getY()));
                _graphics.add(path);
                _vectors.add(vector);
                vector = null;
                path = null;
            }

            return true;
        }
    }

    
    @Override
    public void onDraw(Canvas canvas) {
    	
    	if (path != null) {
    		canvas.drawPath(path, mPaint);
    	}
        /*for (Path path : _graphics) {
            //canvas.drawPoint(graphic.x, graphic.y, mPaint);
            canvas.drawPath(path, mPaint);
        }*/

        //Log.d("Called", "Size: " + _vectors.size());
        
        //Delete after debugging
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
    	        	canvas.drawLine(curr.start.x,
    	        			curr.start.y,
    	        			curr.end.x,
    	        			curr.end.y,
    	        			rectPaint);
    	        }
        	}
        	/*ArrayList<Segment> lines = shape.getSegments();
	        for (int i = 0; i < lines.size(); i++) {
	        	Segment curr = lines.get(i);
	        	canvas.drawLine(curr.start.x,
	        			curr.start.y,
	        			curr.end.x,
	        			curr.end.y,
	        			pointPaint);
	        }*/
	        /*Rect r = v.getRect();
	        if (r != null) {
	        	canvas.drawRect(r, rectPaint);
	        }*/
        }
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        // TODO Auto-generated method stub

    }

    public void surfaceCreated(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        _thread.setRunning(true);
        _thread.start();
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        boolean retry = true;
        _thread.setRunning(false);
        while (retry) {
            try {
                _thread.join();
                retry = false;
            } catch (InterruptedException e) {
                // we will try it again and again...
            }
        }
    }
}
