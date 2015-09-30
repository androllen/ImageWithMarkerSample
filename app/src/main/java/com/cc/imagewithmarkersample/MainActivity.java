package com.cc.imagewithmarkersample;

import android.app.Activity;
import android.os.Bundle;

import java.util.UUID;

public class MainActivity extends Activity {

    //取数组 add 一个控件
    //每个控件 传入参数 1.图片。2.标识。3.可见 拖动 旋转 缩放
    //

    private ArrayViewGroup mArrayViewGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_main);

        mArrayViewGroup=(ArrayViewGroup)findViewById(R.id.vg_bg);
        UUID uuid = UUID.randomUUID();
//        mArrayViewGroup.addBasicItem(R.drawable.flowers,"");
        mArrayViewGroup.addBasicItem(R.drawable.pic_h1_2,uuid.toString());

        mArrayViewGroup.commit();
    }




}
