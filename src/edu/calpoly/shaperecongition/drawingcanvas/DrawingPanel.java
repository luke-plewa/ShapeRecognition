package edu.calpoly.shaperecongition.drawingcanvas;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class DrawingPanel extends SurfaceView implements SurfaceHolder.Callback 
{
    private ArrayList<Path> _graphics = new ArrayList<Path>();
    private Path curPath;
    private Paint mPaint;

    private DrawingThread _thread;
    private Path path;

    public DrawingPanel(Context context) 
    {
        super(context);
        getHolder().addCallback(this);
        _thread = new DrawingThread(getHolder(), this);
        
        mPaint = new Paint();
        mPaint.setDither(true);
        mPaint.setColor(0xffa020f0);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(5);
    }


    public boolean onTouchEvent(MotionEvent event) 
    {
        synchronized (_thread.getSurfaceHolder()) {
            if(event.getAction() == MotionEvent.ACTION_DOWN){
                path = new Path();
                path.moveTo(event.getX(), event.getY());
                path.lineTo(event.getX(), event.getY());
            }else if(event.getAction() == MotionEvent.ACTION_MOVE){
                path.lineTo(event.getX(), event.getY());
            }else if(event.getAction() == MotionEvent.ACTION_UP){
                path.lineTo(event.getX(), event.getY());
                _graphics.add(path);
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
        if (path != null) {
        	canvas.drawPath(path, mPaint);
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
