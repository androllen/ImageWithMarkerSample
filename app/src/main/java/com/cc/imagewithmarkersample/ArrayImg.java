package com.cc.imagewithmarkersample;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
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
public class ArrayImg extends View {
    private static final String TAG = "ArrayImg";
    private ImageView mImageLt;
    public ImageView mImage;
    private ImageView mImageRb;
    private RelativeLayout relativeLayout;
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
    private int startX, startY;

    int x0, y0;

    public ArrayImg(Context context) {
        this(context, null);
    }

    public ArrayImg(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.ctl_cpt_img_item, null);  //继承的是 View

//        View view = LayoutInflater.from(getContext()).inflate(R.layout.ctl_cpt_img_item, this, true);//RelativeLayout

        mImageLt = (ImageView) view.findViewById(R.id.iv_lefttopid);
        mImageRb = (ImageView) view.findViewById(R.id.iv_rightbottomid);

        mImage = (ImageView) view.findViewById(R.id.iv_appicon);
        relativeLayout = (RelativeLayout) view.findViewById(R.id.rl_marker);

    }
    public static int getViewWidth(){
        return 150;
    }
    public static int getViewHeight(){
        return 150;
    }
    public void setWaterMark(@NonNull Bitmap bitmap) {
        mImage.setImageBitmap(bitmap);
        postInvalidate();
    }
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
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


}
