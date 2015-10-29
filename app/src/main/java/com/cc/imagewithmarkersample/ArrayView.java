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
    private static final String TAG = "ArrayView";
    private int mIndexController = 0;
    private LayoutInflater mInflater;
    private FrameLayout mMainContainer;
    private RelativeLayout mListContainer;
    private List<IListItem> mItemList;
    private List<ArrayImg> myViewList;
    private Context mContext;
    private ClickListener mClickListener;
    private int startX, startY;
    public ArrayView(Context context) {
        super(context);
    }

    public ArrayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext=context;
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

        return myView;
    }
    public void addBasicItem(int drawable){

        View view=mInflater.inflate(R.layout.ctl_cpt_img_item,null);

        CustomClickListener listener = new CustomClickListener();
        RelativeLayout layout =(RelativeLayout)view.findViewById(R.id.dragctrl);
        layout.setOnTouchListener(listener);



        ImageView imageView=(ImageView) view.findViewById(R.id.iv_appicon);
//        Bitmap bmp1 = Utils.ReadBitmapById(getContext(), drawable);
        imageView.setImageResource(drawable);
//        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

        //fl_marker
        CustomImageViewClickListener listener1=new CustomImageViewClickListener();


//        FrameLayout frameLayout=(FrameLayout)view.findViewById(R.id.fl_marker);
//        frameLayout.setOnTouchListener(listener1);

        ImageView imageView_tb=(ImageView) view.findViewById(R.id.iv_rightbottomid);
        imageView_tb.setOnTouchListener(listener1);

        RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
        lp2.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        lp2.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        mListContainer.addView(view,lp2);

    }
    public void addBasicItems(int drawable) {

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
            Log.d(TAG, "onTouch ");

            int x = (int) event.getX();
            int y = (int) event.getY();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:// 手指按下时
                    startX = x;
                    startY = y;
                    break;
                case MotionEvent.ACTION_MOVE:

                    int l = v.getLeft();
                    int r = v.getRight();
                    int t = v.getTop();
                    int b = v.getBottom();

                    int Bitmap_X = x - startX;
                    int Bitmap_Y = y - startY;

                    int nl = l + Bitmap_X;
                    int nr = r + Bitmap_X;
                    int nt = t + Bitmap_Y;
                    int nb = b + Bitmap_Y;

                    v.layout(nl, nt, nr, nb);
                    break;
            }
            return true;
        }
    }

    private class CustomImageViewClickListener implements OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
//            View view = mListContainer.findFocus();
//            ImageView frameLayou=(ImageView)v;
//            if (frameLayou==view){
//
//            }
            Log.d(TAG, "onTouch ");

            int x = (int) event.getX();
            int y = (int) event.getY();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:// 手指按下时
                    startX = x;
                    startY = y;
                    break;
                case MotionEvent.ACTION_MOVE:

                    int l = v.getLeft();
                    int r = v.getRight();
                    int t = v.getTop();
                    int b = v.getBottom();

                    int Bitmap_X = x - startX;
                    int Bitmap_Y = y - startY;

                    int nl = l + Bitmap_X;
                    int nr = r + Bitmap_X;
                    int nt = t + Bitmap_Y;
                    int nb = b + Bitmap_Y;


                    v.layout(nl, nt, nr, nb);
                    break;
            }
            return true;
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
