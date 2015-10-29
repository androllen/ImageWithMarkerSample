package com.cc.imagewithmarkersample;

import android.content.Context;
import android.graphics.Bitmap;

import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
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

    private boolean mDrawController = true;
    //private boolean mCanTouch;
    private float mStickerScaleSize = 1.0f;
    private boolean mInDelete = false;
    private OnStickerDeleteListener mOnStickerDeleteListener;


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
        LayoutInflater.from(context).inflate(R.layout.ctl_cpt_img_item, this, true);

        mImageLt = (ImageView)findViewById(R.id.iv_lefttopid);
        mImageRb = (ImageView)findViewById(R.id.iv_rightbottomid);
        mImageLt.setOnClickListener(this);
        mImage = (ImageView)findViewById(R.id.iv_appicon);
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
//        setFocusable(true);
//        try {
//
//
//            float px = mBitmap.getWidth();
//            float py = mBitmap.getHeight();
//
//
//            //mOriginPoints = new float[]{px, py, px + bitmap.getWidth(), py, bitmap.getWidth() + px, bitmap.getHeight() + py, px, py + bitmap.getHeight()};
//            mOriginPoints = new float[]{0, 0, px, 0, px, py, 0, py, px / 2, py / 2};
//            mOriginContentRect = new RectF(0, 0, px, py);
//            mPoints = new float[10];
//            mContentRect = new RectF();
//
//            mMatrix = new Matrix();
//            float transtLeft = ((float)DisplayUtil.getDisplayWidthPixels(getContext()) - mBitmap.getWidth()) / 2;
//            float transtTop = ((float)DisplayUtil.getDisplayWidthPixels(getContext()) - mBitmap.getHeight()) / 2;
//
//            mMatrix.postTranslate(transtLeft, transtTop);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        postInvalidate();

    }


    public static int getViewWidth(){
        return ViewWidth;
    }
    public static int getViewHeight(){
        return ViewHeight;
    }
}
