package com.example.huchen.musicplayer.View;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Handler;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.example.huchen.musicplayer.R;

/**
 * Created by 54571 on 2017/6/10.
 */

public class MyView extends AppCompatImageView {

    int mHeight = 0;
    int mWidth = 0;
    Bitmap bitmap = null;
    int radius = 0;
    Matrix m = new Matrix();
    int degrees = 0;

    Handler mHandler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            m.postRotate(degrees++, radius / 2, radius / 2);
            invalidate();
            mHandler.postDelayed(runnable, 100);
        }
    };

    public MyView(Context context) {
        super(context);
        initView();
    }
    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }
    public void initView() {
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.hehe);
        mHandler.post(runnable);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //测量整个View的宽和高
        mWidth = w(widthMeasureSpec);
        mHeight = h(heightMeasureSpec);
        setMeasuredDimension(mWidth, mHeight);
    }
    private int w(int widthMeasureSpec) {
        int Mode = MeasureSpec.getMode(widthMeasureSpec);
        int Size = MeasureSpec.getSize(widthMeasureSpec);
        if (Mode == MeasureSpec.EXACTLY) {
            mWidth = Size;
        } else {
            int value = getPaddingLeft() + getPaddingRight() + bitmap.getWidth();
            if (Mode == MeasureSpec.AT_MOST) {
                mWidth = Math.min(value, Size);
            }
        }
        return mWidth;
    }
    private int h(int heightMeasureSpec) {
        int Mode = MeasureSpec.getMode(heightMeasureSpec);
        int Size = MeasureSpec.getSize(heightMeasureSpec);
        if (Mode == MeasureSpec.EXACTLY) {
            mHeight = Size;
        } else {
            int value = getPaddingTop() + getPaddingBottom() + bitmap.getHeight();
            if (Mode == MeasureSpec.AT_MOST) {
                mHeight = Math.min(value, Size);
            }
        }
        return mHeight;
    }
    public void stop(){
        mHandler.removeCallbacks(runnable);
    }
    public void start(){
        mHandler.post(runnable);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.concat(m);
        radius = Math.min(mWidth, mHeight);
        bitmap = Bitmap.createScaledBitmap(bitmap, radius, radius, false);
        canvas.drawBitmap(panit(bitmap, radius), 0, 0, null);
        m.reset();
    }
    private Bitmap panit(Bitmap source, int radius) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        Bitmap target = Bitmap.createBitmap(radius, radius, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(target);
        canvas.drawCircle(radius / 2, radius / 2, radius / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(source, 0, 0, paint);
        return target;
    }
}