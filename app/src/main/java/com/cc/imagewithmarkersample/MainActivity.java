package com.cc.imagewithmarkersample;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends Activity {//implements View.OnFocusChangeListener{

    //取数组 add 一个控件
    //每个控件 传入参数 1.图片。2.标识。3.可见 拖动 旋转 缩放
    //

    private ArrayViewGroup mArrayViewGroup;
    private UITableView tableView;
    private ArrayView arrayView;
    private MyView myView;
    private MyView myView1;
    private ImgMarkertrContainer imgMarkertrContainer;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_main);



//        myView=(MyView)findViewById(R.id.myviewid);
//        Bitmap bmp = ReadBitmapById(this, R.drawable.flowers);
//        // 设置图片
//        myView.setImageBitmap(bmp);
//        myView.setOnFocusChangeListener(this);
//
//        myView1=(MyView)findViewById(R.id.myviewids);
//        Bitmap bmp1 = ReadBitmapById(this, R.drawable.hat_14);
//        // 设置图片
//        myView1.setImageBitmap(bmp1);
//        myView1.setOnFocusChangeListener(this);

//        mArrayViewGroup=(ArrayViewGroup)findViewById(R.id.vg_bg);
//        UUID uuid = UUID.randomUUID();
//        mArrayViewGroup.addBasicItem(R.drawable.flowers,uuid.toString());
//        mArrayViewGroup.addBasicItem(R.drawable.pic_h1_2,uuid.toString());
//
//        mArrayViewGroup.commit();


//        tableView = (UITableView) findViewById(R.id.tableView);
//
//        createList();
//
//        Log.d("MainActivity", "total items: " + tableView.getCount());
//
//        tableView.commit();

//        arrayView = (ArrayView) findViewById(R.id.arrayView);
//
//
//        arrayView.addBasicItem(R.drawable.flowers);
//        arrayView.addBasicItem(R.drawable.pic_h1_2);
//
//        Log.d("MainActivity", "total items: " + arrayView.getCount());

    }

    //    private void createList() {
//        CustomClickListener listener = new CustomClickListener();
//        tableView.setClickListener(listener);
//        tableView.addBasicItem(R.drawable.flowers, "Example 1", "Summary text 1");
//        tableView.addBasicItem(R.drawable.pic_h1_2, "Example 3", "Summary text 3");
//    }



//    @Override
//    public void onFocusChange(View v, boolean hasFocus) {
//        MyView view=(MyView)v;
//        if (myView == view){
//            if(hasFocus){
//
//            }
//        }
//    }
//    @Override
//    public boolean onTouch(View v, MotionEvent event) {
//
//        MyView view=(MyView)v;
//
//        return true;
//    }


}
