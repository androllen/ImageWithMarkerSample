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
public class ImgMarker extends ImageView{

    private static final String TAG = "imgmarker";
    private ImageView mImageLt;
    private ImageView mImage;
    private ImageView mImageRb;

    private Context context;

    public static int getViewWidth() {
        return ViewWidth;
    }

    public static int getViewHeight() {
        return ViewHeight;
    }

    private static final int ViewWidth = 150;
    private static final int ViewHeight = 150;

    public ImgMarker(Context context) {
        this(context,null);

    }

    public ImgMarker(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initViews();
    }

    public void setWaterMark(@NonNull Bitmap bitmap) {
        mImage.setImageBitmap(bitmap);
    }

    /** 初始化视图 **/
    protected void initViews(){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.ctl_cpt_img_item, null);  //继承的是 View

        mImageLt = (ImageView) view.findViewById(R.id.iv_lefttopid);
        mImageRb = (ImageView) view.findViewById(R.id.iv_rightbottomid);

        mImage = (ImageView) view.findViewById(R.id.iv_appicon);

        setScaleType(ImageView.ScaleType.MATRIX);
    }


}
