package com.cc.imagewithmarkersample;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Created by androllen on 15/10/29.
 */
@SuppressLint("NewApi")
public class ImgMarkertrContainer extends RelativeLayout {

    private Context mContext;
    private static final int CLICKRANGE = 5;
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

//        this.setOnTouchListener(this);


        StickerView stickerView = new StickerView(getContext());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.image);
        params.addRule(RelativeLayout.ALIGN_TOP, R.id.image);
        StickerView stickerView1 = new StickerView(getContext());
        Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.hat_14);
        stickerView1.setWaterMark(bitmap1);
        this.addView(stickerView1, params);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.flowers);
        stickerView.setWaterMark(bitmap);
        this.addView(stickerView, params);

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        boolean isTouch=false;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = (int) event.getX();
                startY = (int) event.getY();
                touchView = null;

                if (hasView(startX, startY)) {
                    startTouchViewLeft = touchView.getLeft();
                    startTouchViewTop = touchView.getTop();
                } else {
//                    addItem(startX, startY);
                }
                isTouch=true;
                break;
            case MotionEvent.ACTION_MOVE:
                //moveView((int) event.getX(),(int) event.getY());
                isTouch=false;

                break;
            case MotionEvent.ACTION_UP:
                break;
        }

        return super.dispatchTouchEvent(event);
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



    private void moveView(int x, int y) {
        if (touchView == null) return;
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.leftMargin = x - startX + startTouchViewLeft;
        params.topMargin = y - startY + startTouchViewTop;
        //限制子控件移动必须在视图范围内
        int left=touchView.getLeft();
        int right=touchView.getRight();
        if(params.leftMargin<0||(params.leftMargin+touchView.getWidth())>getWidth()){
            params.leftMargin = left;
        }
        if(params.topMargin<0||(params.topMargin+touchView.getHeight())>getHeight()){
            params.topMargin = right;
        }
        touchView.setLayoutParams(params);
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

}
