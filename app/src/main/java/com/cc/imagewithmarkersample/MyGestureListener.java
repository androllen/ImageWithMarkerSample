package com.cc.imagewithmarkersample;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * Created by androllen on 2015/9/28.
 */
public class MyGestureListener extends GestureDetector.SimpleOnGestureListener{
    private Context mContext;
    public MyGestureListener(Context context){
        mContext=context;
    }


    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return super.onScroll(e1, e2, distanceX, distanceY);
    }


}
