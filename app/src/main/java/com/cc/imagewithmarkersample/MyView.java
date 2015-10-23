package com.cc.imagewithmarkersample;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import java.io.InputStream;

/**
 * Created by androllen on 15/10/16.
 */
public class MyView extends View {
    //    private String mtext;
    private int msrclt, msrcrt, msrcrb;
    private static final String SrcLT = "SrcLT";
    private static final String SrcRT = "SrcRT";
    private static final String SrcRB = "SrcRB";
    private static final int Canvas_W = 150, Canvas_H = 150;
    private static final int Rect_W = 100, Rect_H = 100;
    private Bitmap bitmap;
    private int bitmap_W, bitmap_H;
    private int LT_X = 0, LT_Y = 0;
    private int RT_X = 100, RT_Y = 0;
    private int RB_X = 100, RB_Y = 100;
    private int Bitmap_X = 25, Bitmap_Y = 25;
    private int Rect_X = 25, Rect_Y = 25;
    int x0, y0;

    public MyView(Context context) {
        super(context);
    }

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
//        int textId = attrs.getAttributeResourceValue(null, "Text",0);
        int srcLeftTopId = attrs.getAttributeResourceValue(null, SrcLT, 0);
        int srcRightTopId = attrs.getAttributeResourceValue(null, SrcRT, 0);
        int srcRightBottomId = attrs.getAttributeResourceValue(null, SrcRB, 0);
//        mtext = context.getResources().getText(textId).toString();
        msrclt = srcLeftTopId;
        msrcrt = srcRightTopId;
        msrcrb = srcRightBottomId;

    }

    public void setImageBitmap(Bitmap bm) {
        /** 获取图片宽高 **/
        bitmap_W = bm.getWidth();
        bitmap_H = bm.getHeight();

        Bitmap_X = (Canvas_W - bitmap_W) / 2;
        Bitmap_Y = (Canvas_H - bitmap_H) / 2;

        if (bitmap != bm) {
            bitmap = bm;
        }

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.GRAY);

        canvas.drawBitmap(bitmap, Bitmap_X, Bitmap_Y, paint);

        paint.setAlpha(100);
        canvas.drawRect(new Rect(Rect_X, Rect_Y, Rect_W + Rect_X, Rect_H + Rect_Y), paint);


        paint.setAlpha(255);
        onDrawLt(canvas, paint);
        onDrawRt(canvas, paint);
        onDrawRb(canvas, paint);


//        canvas.drawText(mtext, bw / 2, 30, paint);
    }

    private void onDrawLt(Canvas canvas, Paint paint) {
        InputStream is = getResources().openRawResource(msrclt);
        Bitmap mBitmap = BitmapFactory.decodeStream(is);
        int bh = mBitmap.getHeight();
        int bw = mBitmap.getWidth();
        canvas.drawBitmap(mBitmap, LT_X, LT_Y, paint);

    }

    private void onDrawRt(Canvas canvas, Paint paint) {
        InputStream is = getResources().openRawResource(msrcrt);
        Bitmap mBitmap = BitmapFactory.decodeStream(is);
        int bh = mBitmap.getHeight();
        int bw = mBitmap.getWidth();
        canvas.drawBitmap(mBitmap, RT_X, RT_Y, paint);
    }

    private void onDrawRb(Canvas canvas, Paint paint) {
        InputStream is = getResources().openRawResource(msrcrb);
        Bitmap mBitmap = BitmapFactory.decodeStream(is);
        int bh = mBitmap.getHeight();
        int bw = mBitmap.getWidth();
        canvas.drawBitmap(mBitmap, RB_X, RB_Y, paint);
    }

    public static Bitmap rotate(Bitmap b, int degrees) {
        if (degrees != 0 && b != null) {
            Matrix m = new Matrix();
            m.setRotate(degrees,
                    (float) b.getWidth() / 2, (float) b.getHeight() / 2);
            try {
                Bitmap b2 = Bitmap.createBitmap(
                        b, 0, 0, b.getWidth(), b.getHeight(), m, true);
                if (b != b2) {
                    b.recycle();  //Android开发网再次提示Bitmap操作完应该显示的释放
                    b = b2;
                }
            } catch (OutOfMemoryError ex) {
                // 建议大家如何出现了内存不足异常，最好return 原始的bitmap对象。.
            }
        }
        return b;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(!this.isClickable())
            return super.onTouchEvent(event);

        int action = event.getAction();
        boolean isTouch=false;
        /*
         * (x,y)点为发生事件时的点，它的坐标值为相对于该控件左上角的距离
         */
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                x0 = x;
                y0 = y;
                isTouch=true;
                break;
            case MotionEvent.ACTION_MOVE:
                Bitmap_X += x - x0;
                Bitmap_Y += y - y0;

                LT_X += x - x0;
                LT_Y += y - y0;

                RT_X += x - x0;
                RT_Y += y - y0;

                RB_X += x - x0;
                RB_Y += y - y0;

                Rect_X += x - x0;
                Rect_Y += y - y0;

                x0 = x;
                y0 = y;
                isTouch=true;
                Log.i("move", "(" + x0 + "," + y0 + ")");
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                break;
        }

        return isTouch;
    }
}