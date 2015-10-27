package com.cc.imagewithmarkersample;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by androllen on 15/10/20.
 */
public class ArrayView extends RelativeLayout {

    private int mIndexController = 0;
    private LayoutInflater mInflater;
    private FrameLayout mMainContainer;
    private RelativeLayout mListContainer;
    private List<IListItem> mItemList;
    private List<ArrayImg> myViewList;

    private ClickListener mClickListener;

    public ArrayView(Context context) {
        super(context);
    }

    public ArrayView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mItemList = new ArrayList<IListItem>();
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMainContainer = (FrameLayout)  mInflater.inflate(R.layout.ctl_cpt_btn_list_container, null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
        addView(mMainContainer, params);
        mListContainer = (RelativeLayout) mMainContainer.findViewById(R.id.buttonsContainer);

    }

    private ArrayImg addArrayImg(int drawable){
        ArrayImg myView =new ArrayImg(getContext());
        Bitmap bmp1 = Utils.ReadBitmapById(getContext(), drawable);
        myView.mImage.setImageBitmap(bmp1);

//        View tempItemView;
//        tempItemView = mInflater.inflate(R.layout.ctl_cpt_img_item, this,true);
//
//        ArrayImg myView=(ArrayView)tempItemView;
//
//        if (item.getDrawable() > -1) {
//            ((ImageView) view.findViewById(R.id.iv_appicon)).setBackgroundResource(item.getDrawable());
//        }
        return myView;
    }

    public void addBasicItem(int drawable) {

        ArrayImg myView = addArrayImg(drawable);
        CustomClickListener listener = new CustomClickListener();
        myView.setOnTouchListener(listener);
//        myView.setFocusable(true);
        myView.setFocusableInTouchMode(true);

        mListContainer.addView(myView);

    }

    private class CustomClickListener implements OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            ArrayImg img=(ArrayImg)v;
            return false;
        }
    }

    /**
     *
     * @return
     */
    public int getCount() {
        return mItemList.size();
    }

    /**
     *
     */
    public void clear() {
        mItemList.clear();
        mListContainer.removeAllViews();
    }

    /**
     *
     * @param listener
     */
    public void setClickListener(ClickListener listener) {
        this.mClickListener = listener;
    }

    /**
     *
     */
    public void removeClickListener() {
        this.mClickListener = null;
    }

}
