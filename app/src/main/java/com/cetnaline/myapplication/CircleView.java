package com.cetnaline.myapplication;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

public class CircleView extends View {
    public CircleView(Context context) {
        super(context);
        init(null);
    }

    public CircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public CircleView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs){
        mPaint = new Paint();
        TypedArray a = getContext().obtainStyledAttributes(attrs,R.styleable.CircleView);
        mPaint.setColor(a.getColor(R.styleable.CircleView_circle_color,getResources().getColor(android.R.color.holo_red_light,null)));
        a.recycle();
    }

    private Paint mPaint;
    private int width,height,radius;

//    @Override
//    protected void onAttachedToWindow() {
//        super.onAttachedToWindow();
//
//        width = getMeasuredWidth();
//        height = getMeasuredHeight();
//        radius = Math.min(width, height) / 2;
//    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(200, 200);
        } else if (widthMeasureSpec == MeasureSpec.AT_MOST) {
            setMeasuredDimension(200, heightSpecSize);
        } else if (heightMeasureSpec == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSpecSize, 200);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        width = getWidth()-getPaddingLeft()-getPaddingRight();
        height = getHeight()-getPaddingTop()-getPaddingBottom();
        radius = Math.min(width, height) / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(width/2+getPaddingLeft(),height/2+getPaddingTop(), radius, mPaint);
    }
}





















