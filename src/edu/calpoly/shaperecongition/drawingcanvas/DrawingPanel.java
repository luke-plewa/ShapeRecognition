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
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import edu.calpoly.shaperecognition.shaperecognition.Vector;

public class DrawingPanel extends SurfaceView implements SurfaceHolder.Callback 
{
    private ArrayList<Path> _graphics = new ArrayList<Path>();
    private ArrayList<Vector> _vectors = new ArrayList<Vector>();
    private Path curPath;
    private Paint mPaint;
    private Paint pointPaint;

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
        pointPaint.setColor(Color.BLUE);
        pointPaint.setStyle(Paint.Style.STROKE);
        pointPaint.setStrokeJoin(Paint.Join.ROUND);
        pointPaint.setStrokeCap(Paint.Cap.ROUND);
        pointPaint.setStrokeWidth(5);
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
                _graphics.add(path);
                _vectors.add(vector);
                vector = null;
            }

            return true;
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
    	
        for (Path path : _graphics) {
            //canvas.drawPoint(graphic.x, graphic.y, mPaint);
            canvas.drawPath(path, mPaint);
        }
//        if (path != null) {
//        	canvas.drawPath(path, mPaint);
//        }
        
//        if (!_vectors.isEmpty()) {
//        	Vector last = _vectors.get(_vectors.size()-1);
//        	
//        	ArrayList<Point> last_points = last.getPoints();
//        	for (int i = 0; i < last_points.size(); i++) {
//        		if (i+1 < last_points.size()) {
//        		canvas.drawLine(last_points.get(i).x, 
//        						last_points.get(i).y, 
//        						last_points.get(i+1).x,
//        						last_points.get(i+1).y,
//        						pointPaint);
//        		}
//        	}
//        }
//        
        Log.d("Called", "Size: " + _vectors.size());
        
        //Delete after debugging
        for (Vector v : _vectors) {
        	ArrayList<Point> lines = v.getShape();

		        for (int i = 0; i + 1 < lines.size(); i += 2) {
		        	Log.d("Lines", "Index: " + i);
			        	canvas.drawLine(lines.get(i).x,
								lines.get(i).y, 
								lines.get(i+1).x,
								lines.get(i+1).y,
								pointPaint);
		        }
		        
//		        Log.d("Lines", "Lines size of vector: "+ lines.size()); 
//		    	Log.d("Lines", "Vector null? : " + (vector == null));
//		    	Log.d("Lines", "Lines null? : " + (lines == null));
//		       if (lines.get(i) == null) 
//		        	Log.d("Lines", "Lines.get(" + i + ") null? : " + (lines.get(i) == null));
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
