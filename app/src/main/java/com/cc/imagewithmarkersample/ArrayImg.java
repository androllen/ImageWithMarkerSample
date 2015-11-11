package com.cc.imagewithmarkersample;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.Image;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.InputStream;
import java.util.List;

/**
 * Created by androllen on 15/10/16.
 */
public class ArrayImg extends ImageView {
    private static final String TAG = "ArrayImg";

    private int Canvas_W = 160, Canvas_H = 160, mCanvasW = 0, mCanvasH = 0;
    private int Rect_W = 100, Rect_H = 100;
    private Bitmap bitmap;
    private float scale=1.0f,Angle = 0.0f;
    private int bitmap_W, bitmap_H;
    private int LT_X = 0, LT_Y = 0;
    private int RT_X = 100, RT_Y = 0;
    private int RB_X = 100, RB_Y = 100;
    private int Bitmap_X = 25, Bitmap_Y = 25;
    private int Rect_X = 25, Rect_Y = 25;
    private int x0, y0;
    private int StartX = 0, StartY = 0;
    private Bitmap mRotateBitmap, mDeleteBitmap, mScaleBitmap;
    private Paint mPaint;
    private Matrix mMatrix;
    private int mRotateWidth, mRotateHeight, mDeleteWidth, mDeleteHeight, mScaleWidth, mScaleHeight;

    public enum Status {ACTION_NO, ACTION_MOVE, ACTION_SCALE, ACTION_ROTATE, ACTION_DELETE}

    public Status mStatus;
    private Context mContext;

    public ArrayImg(Context context) {
        this(context, null);
    }

    public ArrayImg(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mScaleBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_sticker_control);
        mScaleWidth = mScaleBitmap.getWidth();
        mScaleHeight = mScaleBitmap.getHeight();

        mDeleteBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_sticker_delete);
        mDeleteWidth = mDeleteBitmap.getWidth();
        mDeleteHeight = mDeleteBitmap.getHeight();

        mRotateBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_sticker_delete);
        mRotateWidth = mRotateBitmap.getWidth();
        mRotateHeight = mRotateBitmap.getHeight();
        mPaint = new Paint();
        mMatrix = new Matrix();
        mStatus = Status.ACTION_MOVE;
    }

    public void setWaterMark(@NonNull Bitmap bm) {
        bitmap_W = bm.getWidth();
        bitmap_H = bm.getHeight();

//        Canvas_W = Rect_W + mRotateWidth / 2 + mDeleteWidth / 2;
//        Canvas_H = Rect_H + mScaleHeight / 2 + mDeleteHeight / 2;


        float transtLeft = ((float)DisplayUtil.getDisplayWidthPixels(getContext()) - bitmap_W) / 2;
        float transtTop = ((float)DisplayUtil.getDisplayWidthPixels(getContext()) - bitmap_H) / 2;
        Bitmap_X = (Rect_W - bitmap_W) / 2;
        Bitmap_Y = (Rect_H - bitmap_H) / 2;

        mMatrix.postTranslate(Bitmap_X, Bitmap_Y);

        if (bitmap != bm) {
            bitmap = bm;
        }
        postInvalidate();
    }
    private float rotation(MotionEvent event) {
        float  originDegree = calculateDegree(x0, y0);
        float nowDegree = calculateDegree(event.getX(), event.getY());
        return nowDegree - originDegree;
    }
    private float calculateDegree(float x, float y) {
        double delta_x = x - LT_X;
        double delta_y = y - LT_Y;
        double radians = Math.atan2(delta_y, delta_x);
        return (float) Math.toDegrees(radians);
    }

    private double GetAngle(Point origin, Point start, Point end)
    {
        double cosfi = 0, fi = 0, norm = 0;
        double dsx = start.x - origin.x;
        double dsy = start.y - origin.y;
        double dex = end.x - origin.x;
        double dey = end.y - origin.y;

        cosfi = dsx * dex + dsy * dey;
        norm = (dsx * dsx + dsy * dsy) * (dex * dex + dey * dey);
        if (norm == 0) return 0; // origin和end坐标一样
        cosfi /= Math.sqrt(norm);

        if (cosfi >= 1.0) return 0;
        if (cosfi <= -1.0) return 180;
        fi = Math.acos(cosfi);

        double angle = 180 * fi / Math.PI;

        if (dey > 0)
        {
            return angle;
        }
        return -angle;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        switch (mStatus) {
            case ACTION_NO:
            case ACTION_MOVE:
                canvas.drawBitmap(bitmap, mMatrix, mPaint);
                break;
            case ACTION_SCALE:
                mMatrix.reset();
                mMatrix.postScale(scale, scale);
                Bitmap mBitQQ2 = Bitmap.createBitmap(bitmap, 0, 0, bitmap_W,bitmap_H, mMatrix, true);
                canvas.drawBitmap(mBitQQ2, Bitmap_X, Bitmap_Y, mPaint);
                if (mBitQQ2 != null && !mBitQQ2.isRecycled())
                {
                    mBitQQ2.recycle();
                    mBitQQ2 = null;
                }
                break;
            case ACTION_ROTATE:
                canvas.drawBitmap(bitmap, mMatrix, mPaint);
                break;
            default:
                break;
        }

        mPaint.setColor(Color.GRAY);
        mPaint.setAlpha(100);
        canvas.drawRect(new Rect(Rect_X, Rect_Y, Rect_W + Rect_X, Rect_H + Rect_Y), mPaint);

        mPaint.setAlpha(255);
        onDrawLt(canvas, mPaint);
        onDrawRt(canvas, mPaint);
        onDrawRb(canvas, mPaint);
    }

    private void onDrawLt(Canvas canvas, Paint paint) {
        canvas.drawBitmap(mDeleteBitmap, LT_X, LT_Y, paint);
    }

    private void onDrawRt(Canvas canvas, Paint paint) {
        canvas.drawBitmap(mRotateBitmap, RT_X, RT_Y, paint);
    }

    private void onDrawRb(Canvas canvas, Paint paint) {
        canvas.drawBitmap(mScaleBitmap, RB_X, RB_Y, paint);
    }

    private boolean isInDelete(int x, int y) {
        int rx = Bitmap_X;
        int ry = Rect_Y;
        Rect rectF = new Rect(rx - mDeleteWidth / 2,
                ry - mDeleteHeight / 2,
                rx + mDeleteWidth / 2,
                ry + mDeleteHeight / 2);
        if (rectF.contains(x, y)) {
            return true;
        }

        return false;

    }

    private boolean isInRotater(int x, int y) {
        int rx = Rect_W + Rect_X;
        int ry = Rect_Y;
        Rect rectF = new Rect(rx - mRotateWidth / 2,
                ry - mRotateHeight / 2,
                rx + mRotateWidth / 2,
                ry + mRotateHeight / 2);
        if (rectF.contains(x, y)) {
            return true;
        }

        return false;
    }

    private boolean isInScaler(int x, int y) {

        int rx = Rect_W + Rect_X;
        int ry = Rect_H + Rect_Y;
        Rect rectF = new Rect(rx - mScaleWidth / 2,
                ry - mScaleHeight / 2,
                rx + mScaleWidth / 2,
                ry + mScaleHeight / 2);
        if (rectF.contains(x, y)) {
            return true;
        }

        return false;
    }

    private boolean isInMover(int x, int y) {
        Rect rectF = new Rect(Rect_X, Rect_Y, Rect_X + Rect_W, Rect_Y + Rect_H);
        if (rectF.contains(x, y)) {
            return true;
        }

        return false;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        int action = event.getAction();
        boolean isTouch = false;
        /*
         * (x,y)点为发生事件时的点，它的坐标值为相对于该控件左上角的距离
         */
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (isInDelete(x, y)) {
                    mStatus = Status.ACTION_DELETE;
                } else if (isInRotater(x, y)) {
                    mStatus = Status.ACTION_ROTATE;
                } else if (isInScaler(x, y)) {
                    mStatus = Status.ACTION_SCALE;
                } else if (isInMover(x, y)) {
                    mStatus = Status.ACTION_MOVE;
                } else {
                    mStatus = Status.ACTION_NO;
                }

                x0 = x;
                y0 = y;
                isTouch = true;
                break;
            case MotionEvent.ACTION_MOVE:
                switch (mStatus) {
                    case ACTION_MOVE:
                        LT_X += x - x0;
                        LT_Y += y - y0;

                        RT_X += x - x0;
                        RT_Y += y - y0;

                        RB_X += x - x0;
                        RB_Y += y - y0;

                        Rect_X += x - x0;
                        Rect_Y += y - y0;

                        int cX = x - x0;
                        int cY = y - y0;
                        x0 = x;
                        y0 = y;
                        mMatrix.postTranslate(cX,cY);
                        isTouch = true;
                        Log.e("move", "(" + x0 + "," + y0 + ")");
                        invalidate();
                        break;
                    case ACTION_DELETE:
                        break;
                    case ACTION_ROTATE:
                        LT_X += x - x0;
                        LT_Y += y - y0;

                        RT_X += x - x0;
                        RT_Y += y - y0;

                        RB_X += x - x0;
                        RB_Y += y - y0;

                        Rect_X += x - x0;
                        Rect_Y += y - y0;

                        float angle=(float)GetAngle(new Point(LT_X,LT_Y),new Point(x,y),new Point(RT_X,RT_Y));
                        mMatrix.postRotate(rotation(event),LT_X,LT_Y);
                        Log.e("angle", "(" + angle + "," + ")");
                        x0 = x;
                        y0 = y;
                        invalidate();
                        break;
                    case ACTION_SCALE:
                        RB_X += x - x0;
                        RB_Y += y - y0;

                        Rect_W += x - x0;
                        Rect_H += y - y0;

                        RT_X += x - x0;

                        bitmap_W = bitmap.getWidth();
                        bitmap_H = bitmap.getHeight();

                        Bitmap_X = (Rect_W - bitmap_W) / 2;
                        Bitmap_Y = (Rect_H - bitmap_H) / 2;


                        scale = (float) Rect_W / bitmap_W;
                        float scale2 = (float) Rect_H / bitmap_H;
                        Log.e(TAG, "图片宽度=" + (Rect_W-30) + ",screenWidth=" + Rect_W);
                        Log.e(TAG, "图片高度=" + (Rect_H-30) + ",screenHeight=" + Rect_H);
                        scale = scale < scale2 ? scale : scale2;
                        Log.e(TAG, "图片缩放1=" + scale+"缩放2="+scale2);
//                      // h>>1  same as (...)/2

                        //Rect rect=new Rect(LT_X,LT_Y,RB_X,RB_Y);
//                        // w,h是原图的属性.
//                        Bitmap bmp = Bitmap.createBitmap(bitmap, w, h, Rect_W, Rect_H);
//                        if (bitmap != null && !bitmap.equals(bmp) && !bitmap.isRecycled())
//                        {
//                            bitmap.recycle();
//                            bitmap = null;
//                        }
//                        bitmap=bmp;






//                        mCanvasW = Rect_W + mRotateWidth / 2 + mDeleteWidth / 2;
//                        mCanvasH = Rect_H + mScaleHeight / 2 + mDeleteHeight / 2;


                        x0 = x;
                        y0 = y;
                        invalidate();
                        break;
                    default:
                        break;
                }

                break;
            case MotionEvent.ACTION_UP:
                break;
        }

        return isTouch;

    }

}
