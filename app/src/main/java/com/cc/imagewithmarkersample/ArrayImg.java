package com.cc.imagewithmarkersample;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.Image;
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
public class ArrayImg extends RelativeLayout {

    private ImageView mImageLt;
    private ImageView mImage;
    private ImageView mImageRb;

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

    public ArrayImg(Context context) {
        this(context, null);
    }

    public ArrayImg(Context context, AttributeSet attrs) {
        super(context, attrs);
//        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View view = inflater.inflate(R.layout.ctl_cpt_img_item, this, true);  //继承的是 View

        View view=LayoutInflater.from(getContext()).inflate(R.layout.ctl_cpt_img_item,this,true);

        mImageLt = (ImageView) view.findViewById(R.id.iv_lefttopid);
        mImageRb = (ImageView) view.findViewById(R.id.iv_rightbottomid);

        mImage = (ImageView) view.findViewById(R.id.iv_appicon);

    }


    public void setImageBitmap(Bitmap bm) {
        mImage.setImageBitmap(bm);
    }

}
