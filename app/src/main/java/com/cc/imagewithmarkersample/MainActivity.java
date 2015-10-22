package com.cc.imagewithmarkersample;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.UUID;

public class MainActivity extends Activity implements View.OnFocusChangeListener,View.OnTouchListener {

    //取数组 add 一个控件
    //每个控件 传入参数 1.图片。2.标识。3.可见 拖动 旋转 缩放
    //

    private ArrayViewGroup mArrayViewGroup;

    private MyView myView;
    private MyView myView1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_main);


        myView=(MyView)findViewById(R.id.myviewid);
        Bitmap bmp = ReadBitmapById(this, R.drawable.flowers);
        // 设置图片
        myView.setImageBitmap(bmp);
        myView.setOnFocusChangeListener(this);
        myView.setOnTouchListener(this);

        myView1=(MyView)findViewById(R.id.myviewids);
        Bitmap bmp1 = ReadBitmapById(this, R.drawable.hat_14);
        // 设置图片
        myView1.setImageBitmap(bmp1);
        myView1.setOnTouchListener(this);
        myView1.setOnFocusChangeListener(this);
//        mArrayViewGroup=(ArrayViewGroup)findViewById(R.id.vg_bg);
//        UUID uuid = UUID.randomUUID();
////        mArrayViewGroup.addBasicItem(R.drawable.flowers,"");
//        mArrayViewGroup.addBasicItem(R.drawable.pic_h1_2,uuid.toString());
//
//        mArrayViewGroup.commit();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        MyView view=(MyView)v;
        if (myView == view){
            if(hasFocus){

            }
        }
    }
//    @Override
//    public boolean onTouch(View v, MotionEvent event) {
//
//        MyView view=(MyView)v;
//
//        if(myView.isFocused()){
//            myView.setFocusable(true);
//        }
//
//        return true;
//    }

    /***
     * 根据资源文件获取Bitmap
     *
     * @param context
     * @param drawableId
     * @return
     */
    public static Bitmap ReadBitmapById(Context context, int drawableId) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        options.inInputShareable = true;
        options.inPurgeable = true;
        InputStream stream = context.getResources().openRawResource(drawableId);
        Bitmap bitmap = BitmapFactory.decodeStream(stream, null, options);
        return getBitmap(bitmap, 100, 100);
    }

    /***
     * 等比例压缩图片
     *
     * @param bitmap
     * @param screenWidth
     * @param screenHight
     * @return
     */
    public static Bitmap getBitmap(Bitmap bitmap, int screenWidth,
                                   int screenHight) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Log.e("jj", "图片宽度" + w + ",screenWidth=" + screenWidth);
        Matrix matrix = new Matrix();
        float scale = (float) screenWidth / w;
        float scale2 = (float) screenHight / h;

        // scale = scale < scale2 ? scale : scale2;

        // 保证图片不变形.
        matrix.postScale(scale, scale);
        // w,h是原图的属性.
        return Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
    }

}
