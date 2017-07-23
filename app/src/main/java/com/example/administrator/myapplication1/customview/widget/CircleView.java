package com.example.administrator.myapplication1.customview.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Scroller;

/**
 * Created by Administrator on 2017/7/18/018.
 */

public class CircleView extends View {

    private Paint mPaint;

    public CircleView(Context context) {
        this(context, null);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //实心圆
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        canvas.drawCircle(100, 100, 80, mPaint);
        //空心圆
        mPaint.setStrokeWidth(8);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        canvas.drawCircle(300, 100, 80, mPaint);
        //path
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        Path path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
        path.addCircle(500, 100, 80, Path.Direction.CCW);
        path.addCircle(500, 100, 50, Path.Direction.CCW);
        //饼图
        RectF rectF1 = new RectF(100, 300, 300, 500);
        path.addArc(rectF1, 0, 80);//不连接圆心
        canvas.drawPath(path, mPaint);
        RectF rectF2 = new RectF(100, 600, 300, 800);
        canvas.drawArc(rectF2, 0, 80, false, mPaint);//不连接圆心
        RectF rectF3 = new RectF(100, 900, 300, 1100);
        canvas.drawArc(rectF3, 0, 80, true, mPaint);//连接圆心
    }

    Scroller scroller = new Scroller(getContext());

    public void smoothScrollTo() {
        int scrollX = getScrollX();
//        int delta = destX - scrollX;
        // 1000ms 内滑向destX，效果就是慢慢滑动
        scroller.startScroll(scrollX, 0, -300, -200, 3000);
    }

    @Override
    public void computeScroll() {
        if(scroller.computeScrollOffset()){
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            postInvalidate();
        }
    }
}
