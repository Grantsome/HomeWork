package com.grantsome.zhihudaily.Fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;

/**
 * Created by tom on 2017/2/16.
 */

public class BackgroundFragment extends View{

    public static final int STATE_NOT_STARTED = 0;

    public static final int STATE_FILL_STARTED = 1;

    public static final int STATE_FINISHED = 2;

    private static final Interpolator INTERPOLATOR =new AccelerateInterpolator();

    private static final int FILL_TIMe = 600;

    private int state = STATE_NOT_STARTED;

    private Paint fillPaint;

    private int currentRadius;

    ObjectAnimator backgroundAnimator;

    private int startLoactionX;

    private int startLocationY;

    private OnStateChangeListener onStateChangeListener;

    public BackgroundFragment(Context context) {
        super(context);
        init();
    }

    public BackgroundFragment(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BackgroundFragment(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BackgroundFragment(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init(){
        fillPaint = new Paint();
        fillPaint.setStyle(Paint.Style.FILL);
        fillPaint.setColor(Color.WHITE);
    }

    public void startFillPaintColor(int color){
        fillPaint.setColor(color);
    }

    public void startFromLocation(int[] tapLocationOnScreen){
        changeState(STATE_FILL_STARTED);
        startLoactionX = tapLocationOnScreen[0];
        startLocationY = tapLocationOnScreen[1];
        backgroundAnimator = ObjectAnimator.ofInt(this,"currentRadius",0,getWidth()+getHeight()).setDuration(FILL_TIMe);
        backgroundAnimator.setInterpolator(INTERPOLATOR);
        backgroundAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                changeState(STATE_FINISHED);
            }
        });
        backgroundAnimator.start();
    }

    public void setToFinishedFrame(){
        changeState(STATE_FINISHED);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas){
        if(state == STATE_FINISHED){
            canvas.drawRect(0,0,getWidth(),getHeight(),fillPaint);
        } else {
            canvas.drawCircle(startLoactionX,startLocationY,currentRadius,fillPaint);
        }
    }

    private void changeState(int state){
        if(state == STATE_FINISHED){
        }

        this.state = state;
        if(onStateChangeListener != null){
            onStateChangeListener.onStateChange(state);
        }
    }

    public void setOnStateChangeListener(OnStateChangeListener onStateChangeListener){
        this.onStateChangeListener = onStateChangeListener;
    }

    public void setCurrentRadius(int radius){
        this.currentRadius = radius;
        invalidate();
    }

    public static interface OnStateChangeListener{
        void onStateChange(int state);
    }

}
