package com.groupbwt.draw.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;


public class DrawCanvas extends SurfaceView implements SurfaceHolder.Callback  {

    private Path mPath;

    private Thread thread = null;

    private SurfaceHolder mSurfaceHolder;

    private DrawThread mDrawThread;

    private float mX, mY, mLastX, mLastY;

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private boolean mDrawing;


    public DrawCanvas(Context context) {
        super(context);
        init();
    }

    public DrawCanvas(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DrawCanvas(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mDrawThread = new DrawThread(getHolder());
        mDrawThread.setRunning(true);
        mDrawThread.start();
        //this.setBackgroundColor(Color.WHITE);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        mDrawThread.setRunning(false);
        while (retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {}
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int type = event.getAction();

        if(type == MotionEvent.ACTION_DOWN) {
            mPath = new Path();
            mPath.moveTo(event.getX(), event.getY());

            mDrawing = true;
        }

        if(type == MotionEvent.ACTION_MOVE) {
            mPath.lineTo(event.getX(), event.getY());
        }

        if (type == MotionEvent.ACTION_UP) {
            mDrawing = false;
        }

        return true;
    }

        private void init() {
        getHolder().addCallback(this);

        setFocusable(true); // make sure we get key events

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(10);
        mPaint.setColor(Color.RED);
    }

    private class DrawThread extends Thread {

        private final SurfaceHolder mThreadSurfaceHolder;

        private boolean myThreadRun = false;

        DrawThread(SurfaceHolder surfaceHolder) {
            mThreadSurfaceHolder = surfaceHolder;
        }

        void setRunning(boolean b) {
            myThreadRun = b;
        }

        @Override
        public void run() {
            while (myThreadRun) {
                Canvas c = null;
                if(mDrawing) {
                    try {
                        c = mThreadSurfaceHolder.lockCanvas(null);
                        synchronized (mThreadSurfaceHolder) {
                            c.drawPath(mPath, mPaint);
                        }
                    } finally {
                        // do this in a finally so that if an exception is thrown
                        // during the above, we don't leave the Surface in an
                        // inconsistent state
                        if (c != null) {
                            mThreadSurfaceHolder.unlockCanvasAndPost(c);
                        }
                    }
                }

            }
        }
    }

}
