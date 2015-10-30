package com.cc.imagewithmarkersample;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * Created by androllen on 15/10/29.
 */
@SuppressLint("NewApi")
public class ImgMarkertrContainer extends RelativeLayout {

    private Context mContext;
    int startX = 0;
    int startY = 0;
    int startTouchViewLeft = 0;
    int startTouchViewTop = 0;
    private View touchView;
    public ImgMarkertrContainer(Context context) {
        super(context);
    }

    public ImgMarkertrContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    private void init() {

        addItem();
        addItem();

    }

    public enum Status {ACTION_MOVE, ACTION_SCALE, ACTION_ROTATE, ACTION_DELETE}

    public Status mStatus;

    public void setStatus(Status status, MotionEvent event) {
        switch (status) {
            case ACTION_MOVE:
                moveView((int) event.getX(), (int) event.getY());
                break;
            case ACTION_ROTATE:
                float rotate = rotation(event.getX(),event.getY(),event);
                Matrix matrix=new Matrix();
                matrix.postRotate(rotate, startTouchViewLeft, startTouchViewTop);
                ImgMarker imgMarker=(ImgMarker)touchView;
                imgMarker.setImageMatrix(matrix);
                break;
            case ACTION_SCALE:
                break;
            case ACTION_DELETE:
                break;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                int sX = (int) event.getX();
                int sY = (int) event.getY();
                touchView = null;

                if (hasView(sX, sY)) {
                    startTouchViewLeft = touchView.getLeft();
                    startTouchViewTop = touchView.getTop();
                    mStatus = Status.ACTION_MOVE;
                }else{
                    addItem(sX,sY);
                }

                if (isInController(sX, sY)) {
                    mStatus = Status.ACTION_ROTATE;
                }
                startX=sX;
                startY=sY;

//                if (isInDelete(x, y)) {
//                    mInDelete = true;
//                    break;
//                }
//
//                if (isInReversalHorizontal(x, y)) {
//                    mInReversalHorizontal = true;
//                    break;
//                }
//
//                if (isInReversalVertical(x, y)) {
//                    mInReversalVertical = true;
//                    break;
//                }
                break;
            case MotionEvent.ACTION_MOVE:

                setStatus(mStatus,event);

                break;
            case MotionEvent.ACTION_UP:
//                if (isInDelete(x, y) && mInDelete) {
//                    doDeleteSticker();
//                }
//                if (isInReversalHorizontal(x, y) && mInReversalHorizontal) {
//                    doReversalHorizontal();
//                }
//                if (isInReversalVertical(x, y) && mInReversalVertical) {
//                    doReversalVertical();
//                }
                break;
            case MotionEvent.ACTION_CANCEL:
                startX = 0;
                startY = 0;
                break;
        }

        return super.dispatchTouchEvent(event);
    }

    private boolean hasView(int x, int y) {
        //循环获取子view，判断xy是否在子view上，即判断是否按住了子view
        for (int index = 0; index < this.getChildCount(); index++) {
            View view = this.getChildAt(index);
            int left = view.getLeft();
            int top = view.getTop();
            int right = view.getRight();
            int bottom = view.getBottom();
            Rect rect = new Rect(left, top, right, bottom);
            boolean contains = rect.contains(x, y);
            //如果是与子view重叠则返回真,表示已经有了view不需要添加新view了
            if (contains) {
                touchView = view;
                touchView.bringToFront();
                return false;
            }
        }
        touchView = null;
        return false;
    }

    private void moveView(int x, int y) {
        if (touchView == null) return;
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.leftMargin = x - startX + startTouchViewLeft;
        params.topMargin = y - startY + startTouchViewTop;
        //限制子控件移动必须在视图范围内
        int left = touchView.getLeft();
        int right = touchView.getRight();
        if (params.leftMargin < 0 || (params.leftMargin + touchView.getWidth()) > getWidth()) {
            params.leftMargin = left;
        }
        if (params.topMargin < 0 || (params.topMargin + touchView.getHeight()) > getHeight()) {
            params.topMargin = right;
        }
        touchView.setLayoutParams(params);
    }

    private boolean isInController(int x, int y) {
        int rx = touchView.getRight();
        int ry = touchView.getBottom();

        Rect rectF = new Rect(rx - 25, ry - 25, rx + 25,ry + 25);
        if (rectF.contains(x, y)) {
            return true;
        }

        return false;

    }

    private float rotation(float x, float y, MotionEvent event) {
        float originDegree = calculateDegree(startX, startY);
        float nowDegree = calculateDegree(x,y);
        return nowDegree - originDegree;
    }

    private float calculateDegree(float x, float y) {
        double delta_x = x - startTouchViewLeft;
        double delta_y = y - startTouchViewTop;
        double radians = Math.atan2(delta_y, delta_x);
        return (float) Math.toDegrees(radians);
    }

//    private boolean isInDelete(float x, float y) {
//        int position = 0;
//        //while (position < 8) {
//        float rx = mPoints[position];
//        float ry = mPoints[position + 1];
//        RectF rectF = new RectF(rx - mDeleteWidth / 2,
//                ry - mDeleteHeight / 2,
//                rx + mDeleteWidth / 2,
//                ry + mDeleteHeight / 2);
//        if (rectF.contains(x, y)) {
//            return true;
//        }
//        //   position += 2;
//        //}
//        return false;
//
//    }
//
//    //判断点击区域是否在水平反转按钮区域内
//    private boolean isInReversalHorizontal(float x, float y) {
//        int position = 2;
//        float rx = mPoints[position];
//        float ry = mPoints[position + 1];
//
//        RectF rectF = new RectF(rx - mReversalHorWidth / 2, ry - mReversalHorHeight / 2, rx + mReversalHorWidth / 2, ry + mReversalHorHeight / 2);
//        if (rectF.contains(x, y))
//            return true;
//
//        return false;
//
//    }
//
//    //判断点击区域是否在垂直反转按钮区域内
//    private boolean isInReversalVertical(float x, float y) {
//        int position = 6;
//        float rx = mPoints[position];
//        float ry = mPoints[position + 1];
//
//        RectF rectF = new RectF(rx - mReversalVerWidth / 2, ry - mReversalVerHeight / 2, rx + mReversalVerWidth / 2, ry + mReversalVerHeight / 2);
//        if (rectF.contains(x, y))
//            return true;
//        return false;
//    }

//    private void doDeleteSticker() {
//        setWaterMark(null);
//        if (mOnStickerDeleteListener != null) {
//            mOnStickerDeleteListener.onDelete();
//        }
//    }
//
//    //图片水平反转
//    private void doReversalHorizontal() {
//        float[] floats = new float[]{-1f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 1f};
//        Matrix tmpMatrix = new Matrix();
//        tmpMatrix.setValues(floats);
//        mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(),
//                mBitmap.getHeight(), tmpMatrix, true);
//        invalidate();
//        mInReversalHorizontal = false;
//    }
//
//    //图片垂直反转
//    private void doReversalVertical() {
//        float[] floats = new float[]{1f, 0f, 0f, 0f, -1f, 0f, 0f, 0f, 1f};
//        Matrix tmpMatrix = new Matrix();
//        tmpMatrix.setValues(floats);
//        mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(),
//                mBitmap.getHeight(), tmpMatrix, true);
//        invalidate();
//        mInReversalVertical = false;
//    }
//
//
//    private float caculateLength(float x, float y) {
//        float ex = x - mPoints[8];
//        float ey = y - mPoints[9];
//        return FloatMath.sqrt(ex * ex + ey * ey);
//    }

    private void addItem(){
        addItem(0,0);
    }

    private void addItem(int x, int y) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        int screenWidth=getWidth();
        int imgWidth=ImgMarker.getViewWidth();
        ImgMarker view = null;
        if (x + imgWidth > screenWidth) {
            params.leftMargin = screenWidth - ImgMarker.getViewWidth();
            view = new ImgMarker(getContext());
        } else {
            params.leftMargin = x;
            view = new ImgMarker(getContext());
        }

        params.topMargin = y;

        if(params.topMargin<0){
            params.topMargin =0;
        }
        else if((params.topMargin+ImgMarker.getViewHeight())>getHeight()){
            params.topMargin = getHeight() - ImgMarker.getViewHeight();
        }


        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.flowers);
        view.setWaterMark(bitmap);

        this.addView(view, params);

    }
}
