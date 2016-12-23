package com.groupbwt.draw.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.groupbwt.draw.R;

import java.util.ArrayList;
import java.util.List;


public class DrawCanvas extends SurfaceView  {


    private Path mCurrentPath;

    private Paint mCurrentPaint;

    private Paint mBitmapPaint;

    private float mX, mY;

    private static final float TOLERANCE = 5;

    private float mStrokeWidth = 10f;

    private int mStrokeColor = Color.BLUE;

    private Bitmap mBitmap;

    private Canvas mCanvas;

    private List<Stroke> mStrokes = new ArrayList<>();

    private boolean mDrawing = false;

    public DrawCanvas(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs,  R.styleable.DrawCanvas, 0, 0);
        mStrokeColor = a.getColor(R.styleable.DrawCanvas_stroke_color, Color.BLUE);
        mStrokeWidth = a.getFloat(R.styleable.DrawCanvas_stroke_width, 10f);
        a.recycle();
        init();
    }

    public DrawCanvas(Context c) {
        super(c);
        init();
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startTouch(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                moveTouch(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                upTouch();
                invalidate();
                break;
        }
        return true;
    }

    public float getStrokeWidth() {
        return mStrokeWidth;
    }

    public void setStrokeWidth(float strokeWidth) {
        mStrokeWidth = strokeWidth;
        initPaint();
    }

    public int getStrokeColor() {
        return mStrokeColor;
    }

    public void setStrokeColor(int strokeColor) {
        mStrokeColor = strokeColor;
        initPaint();
    }

    public void clearCanvas() {
        if(mCurrentPath != null) {
            mCurrentPath.reset();
            mStrokes.clear();
            mBitmap = Bitmap.createBitmap(mCanvas.getWidth(), mCanvas.getHeight(), Bitmap.Config.ARGB_8888);
            mCanvas = new Canvas(mBitmap);
            invalidate();
        }

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }

    // override onDraw
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(mCurrentPath != null) {

            canvas.drawBitmap( mBitmap, 0, 0, mBitmapPaint);
            canvas.drawPath( mCurrentPath,  mCurrentPaint);
        }

        /*if(mDrawing && mCurrentPath != null) {
            canvas.drawPath(mCurrentPath, mCurrentPaint);
        } else {
            for(Stroke stroke: mStrokes){
                canvas.drawPath(stroke.getPath(), stroke.getPaint());
            }
        }*/
    }

    private void init () {
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);
        initPaint();
    }

    private void initPaint () {
        mCurrentPaint = new Paint();
        mCurrentPaint.setAntiAlias(true);
        mCurrentPaint.setColor(Color.RED);
        mCurrentPaint.setDither(true);
        mCurrentPaint.setColor(mStrokeColor);
        mCurrentPaint.setStyle(Paint.Style.STROKE);
        mCurrentPaint.setStrokeJoin(Paint.Join.ROUND);
        mCurrentPaint.setStrokeCap(Paint.Cap.ROUND);
        mCurrentPaint.setStrokeWidth(mStrokeWidth);
    }



    // when ACTION_DOWN start touch according to the x,y values
    private void startTouch(float x, float y) {
        mDrawing = true;
        mCurrentPath = new Path();
        mCurrentPath.moveTo(x, y);
        mX = x;
        mY = y;
        mCurrentPath.addCircle(mX,mY, mStrokeWidth/10, Path.Direction.CCW);
    }

    // when ACTION_MOVE move touch according to the x,y values
    private void moveTouch(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOLERANCE || dy >= TOLERANCE) {
            mCurrentPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
        }
    }

    // when ACTION_UP stop touch
    private void upTouch() {
        mCurrentPath.lineTo(mX, mY);
        mCurrentPath.addCircle(mX,mY, mStrokeWidth/10, Path.Direction.CW);
        mStrokes.add(new Stroke(mCurrentPaint, mCurrentPath));
        mDrawing = false;
        mCanvas.drawPath(mCurrentPath,  mCurrentPaint);

    }


    private class Stroke {

        private Paint mPaint;

        private Path mPath;

        Stroke(Paint paint, Path path) {
            mPaint = paint;
            mPath = path;
        }

        public Paint getPaint() {
            return mPaint;
        }

        public Path getPath() {
            return mPath;
        }
    }

    //override the onTouchEvent


}
