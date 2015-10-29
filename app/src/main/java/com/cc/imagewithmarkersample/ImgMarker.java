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
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Created by androllen on 15/10/29.
 */
public class ImgMarker extends RelativeLayout implements OnClickListener{

    private static final String TAG = "imgmarker";
    private ImageView mImageLt;
    private ImageView mImage;
    private ImageView mImageRb;

    private Context context;
    private View loTag;
    private InputMethodManager imm;
    private static final int ViewWidth = 150;
    private static final int ViewHeight = 150;


    private float mScaleSize;

    public static final float MAX_SCALE_SIZE = 3.2f;
    public static final float MIN_SCALE_SIZE = 0.6f;


    private float[] mOriginPoints;
    private float[] mPoints;
    private RectF mOriginContentRect;
    private RectF mContentRect;
    private RectF mViewRect;

    private float mLastPointX, mLastPointY;

    private Bitmap mBitmap;
    private Bitmap mControllerBitmap, mDeleteBitmap;
    private Bitmap mReversalHorBitmap,mReversalVerBitmap;//水平反转和垂直反转bitmap
    private Matrix mMatrix;
    private Paint mPaint, mBorderPaint;
    private float mControllerWidth, mControllerHeight, mDeleteWidth, mDeleteHeight;
    private float mReversalHorWidth,mReversalHorHeight,mReversalVerWidth,mReversalVerHeight;
    private boolean mInController, mInMove;
    private boolean mInReversalHorizontal,mInReversalVertical;
    private int startX, startY;
    private boolean mDrawController = true;
    //private boolean mCanTouch;
    private float mStickerScaleSize = 1.0f;
    private boolean mInDelete = false;
    private OnStickerDeleteListener mOnStickerDeleteListener;
    private Matrix matrix ;
    /**
     * 图片操作类型
     */
    public static final int OPER_DEFAULT = -1;      //默认
    public static final int OPER_TRANSLATE = 0;     //移动
    public static final int OPER_SCALE = 1;         //缩放
    public static final int OPER_ROTATE = 2;        //旋转
    public static final int OPER_SELECTED = 3;      //选择



    public ImgMarker(Context context) {
        super(context);
        this.context = context;
        initViews();
    }

    private void doDeleteSticker(View v) {
        setWaterMark(null);
        if (mOnStickerDeleteListener != null) {
            mOnStickerDeleteListener.onDelete(v);
        }
    }

    @Override
    public void onClick(View v) {
        doDeleteSticker(v);
    }

    public void setOnStickerDeleteListener(OnStickerDeleteListener listener) {
        mOnStickerDeleteListener = listener;
    }
    /** 初始化视图 **/
    protected void initViews(){
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setFilterBitmap(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(4.0f);
        mPaint.setColor(Color.WHITE);

        mBorderPaint = new Paint(mPaint);
        mBorderPaint.setColor(Color.parseColor("#B2ffffff"));
        mBorderPaint.setShadowLayer(DisplayUtil.dip2px(getContext(), 2.0f), 0, 0, Color.parseColor("#33000000"));

        mControllerBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_sticker_control);
        mControllerWidth = mControllerBitmap.getWidth();
        mControllerHeight = mControllerBitmap.getHeight();

        mDeleteBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_sticker_delete);
        mDeleteWidth = mDeleteBitmap.getWidth();
        mDeleteHeight = mDeleteBitmap.getHeight();

        mReversalHorBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.ic_sticker_reversal_horizontal);
        mReversalHorWidth = mReversalHorBitmap.getWidth();
        mReversalHorHeight = mReversalHorBitmap.getHeight();

        mReversalVerBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.ic_sticker_reversal_vertical);
        mReversalVerWidth = mReversalVerBitmap.getWidth();
        mReversalVerHeight = mReversalVerBitmap.getHeight();

        matrix = new Matrix();
    }

    public Matrix getMarkMatrix() {
        return mMatrix;
    }
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }
    @Override
    public void setFocusable(boolean focusable) {
        super.setFocusable(focusable);
        postInvalidate();
    }

    public void setWaterMark(@NonNull Bitmap bitmap) {
        mBitmap = bitmap;
        mStickerScaleSize = 1.0f;

        mImage.setImageBitmap(bitmap);

        setFocusable(true);
        try {


            float px = mBitmap.getWidth();
            float py = mBitmap.getHeight();


            //mOriginPoints = new float[]{px, py, px + bitmap.getWidth(), py, bitmap.getWidth() + px, bitmap.getHeight() + py, px, py + bitmap.getHeight()};
            mOriginPoints = new float[]{0, 0, px, 0, px, py, 0, py, px / 2, py / 2};
            mOriginContentRect = new RectF(0, 0, px, py);
            mPoints = new float[10];
            mContentRect = new RectF();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public boolean isInController(float x, float y) {
        int position = 4;
        //while (position < 8) {
        float rx = mPoints[position];
        float ry = mPoints[position + 1];
        RectF rectF = new RectF(rx - mControllerWidth / 2,
                ry - mControllerHeight / 2,
                rx + mControllerWidth / 2,
                ry + mControllerHeight / 2);
        if (rectF.contains(x, y)) {
            return true;
        }
        //   position += 2;
        //}
        return false;

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
            canvas.drawBitmap(mControllerBitmap, mPoints[4] - mControllerWidth / 2, mPoints[5] - mControllerHeight / 2, mBorderPaint);
            canvas.drawBitmap(mDeleteBitmap, mPoints[0] - mDeleteWidth / 2, mPoints[1] - mDeleteHeight / 2, mBorderPaint);
            canvas.drawBitmap(mReversalHorBitmap,mPoints[2]-mReversalHorWidth/2,mPoints[3]-mReversalVerHeight/2,mBorderPaint);
            canvas.drawBitmap(mReversalVerBitmap,mPoints[6]-mReversalVerWidth/2,mPoints[7]-mReversalVerHeight/2,mBorderPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getAction();
/*
         * (x,y)点为发生事件时的点，它的坐标值为相对于该控件左上角的距离
         */

        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (action) {
            case MotionEvent.ACTION_DOWN:

                startX = x;
                startY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                int l = this.getLeft();
                int r = this.getRight();
                int t = this.getTop();
                int b = this.getBottom();


                int Bitmap_X = x - startX;
                int Bitmap_Y = y - startY;

                int nl = l + Bitmap_X;
                int nr = r + Bitmap_X;
                int nt = t + Bitmap_Y;
                int nb = b + Bitmap_Y;

                this.layout(nl, nt, nr, nb);
                break;
            case MotionEvent.ACTION_UP:
                break;
        }

        return true;

    }

    public static int getViewWidth(){
        return ViewWidth;
    }
    public static int getViewHeight(){
        return ViewHeight;
    }
}
