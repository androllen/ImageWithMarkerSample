package com.cc.imagewithmarkersample;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


/**
 * Created by sam on 14-8-14.
 */
public class ImgMarker extends View {
    private static final String TAG = "ImgMarker";
    private float mScaleSize;

    public static final float MAX_SCALE_SIZE = 3.2f;
    public static final float MIN_SCALE_SIZE = 0.0f;


    private float[] mOriginPoints;
    private float[] mPoints;
    private RectF mOriginContentRect;
    private RectF mContentRect;
    private RectF mViewRect;


    private Bitmap mBitmap;
    private Bitmap mRotateBitmap, mDeleteBitmap, mScaleBitmap;
    private Matrix mMatrix;
    private Paint mPaint, mBorderPaint;
    private float mRotateWidth, mRotateHeight, mDeleteWidth, mDeleteHeight, mScaleWidth, mScaleHeight;
    private boolean mInController, mInMove;
    private boolean mInReversalHorizontal, mInReversalVertical;
    private int StartX, StartY;
    private boolean mDrawController = true;
    //private boolean mCanTouch;
    private float mStickerScaleSize = 1.0f;

    private OnStickerDeleteListener mOnStickerDeleteListener;

    public ImgMarker(Context context) {
        this(context, null);
    }

    public ImgMarker(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImgMarker(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setFilterBitmap(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(4.0f);
        mPaint.setColor(Color.WHITE);

        mBorderPaint = new Paint(mPaint);
        mBorderPaint.setColor(Color.parseColor("#B2ffffff"));
        mBorderPaint.setShadowLayer(DisplayUtil.dip2px(getContext(), 2.0f), 0, 0, Color.parseColor("#33000000"));

        mScaleBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_sticker_control);
        mScaleWidth = mScaleBitmap.getWidth();
        mScaleHeight = mScaleBitmap.getHeight();

        mDeleteBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_sticker_delete);
        mDeleteWidth = mDeleteBitmap.getWidth();
        mDeleteHeight = mDeleteBitmap.getHeight();

        mRotateBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_sticker_delete);
        mRotateWidth = mRotateBitmap.getWidth();
        mRotateHeight = mRotateBitmap.getHeight();

    }

    /**
     * mOriginPoints
     * 0-------1
     * |       |
     * |   4   |
     * |       |
     * 3-------2
     * 0-(0,0)
     * 1-(px, 0)
     * 2-(px,py)
     * 3-(0,py)
     * 4-(px / 2, py / 2)
     * <p/>
     * mOriginContentRect
     * 0--------
     * |       |
     * |       |
     * |       |
     * --------1
     * 0-(0,0)
     * 1-(px, py)
     */
    public void setWaterMark(@NonNull Bitmap bitmap) {
        mBitmap = bitmap;
        mStickerScaleSize = 1.0f;


        setFocusable(true);
        try {


            float px = mBitmap.getWidth();
            float py = mBitmap.getHeight();


            //mOriginPoints = new float[]{px, py, px + bitmap.getWidth(), py, bitmap.getWidth() + px, bitmap.getHeight() + py, px, py + bitmap.getHeight()};
            mOriginPoints = new float[]{0, 0, px, 0, px, py, 0, py, px / 2, py / 2};
            mOriginContentRect = new RectF(0, 0, px, py);
            mPoints = new float[10];
            mContentRect = new RectF();

            mMatrix = new Matrix();
            float transtLeft = ((float) DisplayUtil.getDisplayWidthPixels(getContext()) - mBitmap.getWidth()) / 2;
            float transtTop = ((float) DisplayUtil.getDisplayWidthPixels(getContext()) - mBitmap.getHeight()) / 2;

            mMatrix.postTranslate(transtLeft, transtTop);

        } catch (Exception e) {
            e.printStackTrace();
        }
        postInvalidate();

    }

    public enum Status {NOACTION, ACTION_MOVE, ACTION_SCALE, ACTION_ROTATE, ACTION_DELETE, ACTION_REVERSALHORIZONTAL, ACTION_INREVERSALVERTICAL}

    public Status mStatus;

    public Matrix getMarkMatrix() {
        return mMatrix;
    }

    @Override
    public void setFocusable(boolean focusable) {
        super.setFocusable(focusable);
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mBitmap == null || mMatrix == null) {
            return;
        }

        mMatrix.mapPoints(mPoints, mOriginPoints);

        mMatrix.mapRect(mContentRect, mOriginContentRect);
        canvas.drawBitmap(mBitmap, mMatrix, mPaint);
        if (mDrawController && isFocusable()) {
            canvas.drawLine(mPoints[0], mPoints[1], mPoints[2], mPoints[3], mBorderPaint);
            canvas.drawLine(mPoints[2], mPoints[3], mPoints[4], mPoints[5], mBorderPaint);
            canvas.drawLine(mPoints[4], mPoints[5], mPoints[6], mPoints[7], mBorderPaint);
            canvas.drawLine(mPoints[6], mPoints[7], mPoints[0], mPoints[1], mBorderPaint);
            canvas.drawBitmap(mDeleteBitmap, mPoints[0] - mDeleteWidth / 2, mPoints[1] - mDeleteHeight / 2, mBorderPaint);
            canvas.drawBitmap(mRotateBitmap, mPoints[2] - mRotateWidth / 2, mPoints[3] - mRotateHeight / 2, mBorderPaint);
            canvas.drawBitmap(mScaleBitmap, mPoints[4] - mScaleWidth / 2, mPoints[5] - mScaleHeight / 2, mBorderPaint);
        }
    }

    public Bitmap getBitmap() {
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        mDrawController = false;
        draw(canvas);
        mDrawController = true;
        canvas.save();
        return bitmap;
    }

    public void setShowDrawController(boolean show) {
        mDrawController = show;
    }

    private boolean isInRotater(float x, float y) {
        int position = 2;

        float rx = mPoints[position];
        float ry = mPoints[position + 1];
        RectF rectF = new RectF(rx - mRotateWidth / 2,
                ry - mRotateHeight / 2,
                rx + mRotateWidth / 2,
                ry + mRotateHeight / 2);
        if (rectF.contains(x, y)) {
            return true;
        }

        return false;
    }

    private boolean isInScaler(float x, float y) {

        int position = 4;

        float rx = mPoints[position];
        float ry = mPoints[position + 1];
        RectF rectF = new RectF(rx - mScaleWidth / 2,
                ry - mScaleHeight / 2,
                rx + mScaleWidth / 2,
                ry + mScaleHeight / 2);
        if (rectF.contains(x, y)) {
            return true;
        }

        return false;
    }

    private boolean isInDelete(float x, float y) {
        int position = 0;
        float rx = mPoints[position];
        float ry = mPoints[position + 1];
        RectF rectF = new RectF(rx - mDeleteWidth / 2,
                ry - mDeleteHeight / 2,
                rx + mDeleteWidth / 2,
                ry + mDeleteHeight / 2);
        if (rectF.contains(x, y)) {
            return true;
        }

        return false;

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (!isFocusable()) {
            return super.dispatchTouchEvent(event);
        }
        if (mViewRect == null) {
            mViewRect = new RectF(0f, 0f, getMeasuredWidth(), getMeasuredHeight());
        }
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                StartX = x;
                StartY = y;

                if (isInRotater(x, y)) {
                    mStatus = Status.ACTION_ROTATE;
                    break;
                }

                if (isInDelete(x, y)) {
                    mStatus = Status.ACTION_DELETE;
                    break;
                }

                if (isInScaler(x, y)) {
                    mStatus = Status.ACTION_SCALE;
                    break;
                }

                if (mContentRect.contains(x, y)) {
                    mStatus = Status.ACTION_MOVE;
                    break;

                }

            case MotionEvent.ACTION_CANCEL:
                StartX = 0;
                StartY = 0;
                break;
            case MotionEvent.ACTION_MOVE:
                setStatus(mStatus, event);
                break;
            case MotionEvent.ACTION_UP:
                setActionUp(mStatus, event);
                break;

        }
        return true;
    }

    private void setActionUp(Status status, MotionEvent event) {
        if (status == null)
            return;
        switch (status) {
            case ACTION_DELETE:
                if (isInDelete(StartX, StartY)) {
                    doDeleteSticker();
                }
                break;
            default:
                break;
        }
    }

    public void setStatus(Status status, MotionEvent event) {
        if (status == null)
            return;
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (status) {
            case ACTION_MOVE:
                float cX = x - StartX;
                float cY = y - StartY;
                if (FloatMath.sqrt(cX * cX + cY * cY) > 2.0f && canStickerMove(cX, cY)) {
                    mMatrix.postTranslate(cX, cY);
                    postInvalidate();

                    StartX = x;
                    StartY = y;
                }

                break;
            case ACTION_ROTATE:
                mMatrix.postRotate(rotation(event), mPoints[0], mPoints[1]);
                postInvalidate();

                StartX = x;
                StartY = y;
                break;
            case ACTION_SCALE:
                Log.d(TAG, "setStatus scale");
                float nowLenght = caculateLength(mPoints[0], mPoints[1]);
                float touchLenght = caculateLength(x, y);

                if (FloatMath.sqrt((nowLenght - touchLenght) * (nowLenght - touchLenght)) > 0.0f) {
                    float scale = touchLenght / nowLenght;
                    float nowsc = mStickerScaleSize * scale;
                    if (nowsc >= MIN_SCALE_SIZE && nowsc <= MAX_SCALE_SIZE) {
                        mMatrix.postScale(scale, scale);
                        mStickerScaleSize = nowsc;
                    }
                }
                break;
            case ACTION_DELETE:
                break;
            default:
                break;
        }
    }

    private void doDeleteSticker() {
        setWaterMark(null);
        if (mOnStickerDeleteListener != null) {
            mOnStickerDeleteListener.onDelete();
        }
    }

    private boolean canStickerMove(float cx, float cy) {
        float px = cx + mPoints[8];
        float py = cy + mPoints[9];
        if (mViewRect.contains(px, py)) {
            return true;
        } else {
            return false;
        }
    }

    private float caculateLength(float x, float y) {
        float ex = x - mPoints[8];
        float ey = y - mPoints[9];
        return FloatMath.sqrt(ex * ex + ey * ey);
    }

    private float rotation(MotionEvent event) {
        float originDegree = calculateDegree(StartX, StartY);
        float nowDegree = calculateDegree(event.getX(), event.getY());
        return nowDegree - originDegree;
    }

    private float calculateDegree(float x, float y) {
        double delta_x = x - mPoints[0];
        double delta_y = y - mPoints[1];
        double radians = Math.atan2(delta_y, delta_x);
        return (float) Math.toDegrees(radians);
    }

    public void setOnStickerDeleteListener(OnStickerDeleteListener listener) {
        mOnStickerDeleteListener = listener;
    }
}
