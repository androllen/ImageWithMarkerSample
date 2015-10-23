package com.cc.imagewithmarkersample;

import android.content.Context;
import android.graphics.Rect;
import android.text.method.MovementMethod;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by androllen on 2015/9/29.
 */
public class ArrayViewGroup extends FrameLayout implements View.OnTouchListener ,View.OnFocusChangeListener{

    private LayoutInflater mInflater;
    private FrameLayout mMainContainer;
    private RelativeLayout mImgContainer;
    private List<IListItem> mItemList;
    private int mIndexController = 0;

    private DragImageView mDragImageView;

    /** 移动的偏移量  */
    private int mMoveDeltX = 0;
    /** 最大移动距离   */
    private int mMoveLength;
    /** 判断是否在拖动 */
    private boolean mIsScrolled =false;
    /** 第一次按下的有效区域 */
    private float mLastX = 0;
    /** 绘制的目标区域大小  */
    private Rect mDest = null;
    /** 当前点位置 */
    private float mCurrentX = 0;

    private float start_x, start_y, current_x, current_y;// 触摸位置

    public ArrayViewGroup(Context context) {
        this(context, null);
    }

    public ArrayViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        mItemList = new ArrayList<IListItem>();

        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMainContainer = (FrameLayout) mInflater.inflate(R.layout.ctl_cpt_btn_list_container, null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.FILL_PARENT);
        addView(mMainContainer, params);
        mImgContainer = (RelativeLayout) findViewById(R.id.buttonsContainer);
        mMoveLength=480;
    }

    /** */
    private void onTouchDown(MotionEvent event){
        current_x=event.getRawX();
        current_y=event.getRawY();

        start_x=event.getX();
        start_y=event.getY();
    }

    /** */
    private void onTouchMove(MotionEvent event){
        mCurrentX = event.getX();
        mMoveDeltX = (int) (mCurrentX - mLastX);
        if(mMoveDeltX > 10){
            //设置了10这个误差距离，可以更好的实现点击效果
            mIsScrolled = true;
        }
//        // 如果开关开着向左滑动，或者开关关着向右滑动（这时候是不需要处理的）
//        if ((mSwitchOn && mMoveDeltX < 0) || (!mSwitchOn && mMoveDeltX > 0)) {
//            mFlag = true;
//            mMoveDeltX = 0;
//        }

        if (Math.abs(mMoveDeltX) > mMoveLength) {
            mMoveDeltX = mMoveDeltX > 0 ? mMoveLength : -mMoveLength;
        }
        invalidate();
    }

    private void onTouchUp(MotionEvent event){
//        mSwitchThumb = mSwitchThumbNormal;
//        //如果没有滑动过，就看作一次点击事件
//        if(!mIsScrolled){
//            mMoveDeltX = mSwitchOn ? mMoveLength : -mMoveLength;
//            mSwitchOn = !mSwitchOn;
//            if (switchListener != null) {
//                switchListener.onSwitchChange(this, mSwitchOn);
//            }
//            invalidate();
//            mMoveDeltX = 0;
//            break;
//        }
//        mIsScrolled = false;
//        if (Math.abs(mMoveDeltX) > 0 && Math.abs(mMoveDeltX) < mMoveLength / 2) {
//            mMoveDeltX = 0;
//            invalidate();
//        } else if (Math.abs(mMoveDeltX) > mMoveLength / 2
//                && Math.abs(mMoveDeltX) <= mMoveLength) {
//            mMoveDeltX = mMoveDeltX > 0 ? mMoveLength : -mMoveLength;
//            mSwitchOn = !mSwitchOn;
//            if (switchListener != null) {
//                switchListener.onSwitchChange(this, mSwitchOn);
//            }
//            invalidate();
//            mMoveDeltX = 0;
//        } else if (mMoveDeltX == 0 && mFlag) {
//            // 这时候得到的是不需要进行处理的，因为已经move过了
//            mMoveDeltX = 0;
//            mFlag = false;
//        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                onTouchDown(event);
                break;
            case MotionEvent.ACTION_MOVE:
                onTouchMove(event);
                break;
            case MotionEvent.ACTION_UP:
                onTouchUp(event);
            default:
                break;
        }
        return true;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

    }

    public void addBasicItem(int drawable, String title) {
        mItemList.add(new ImgItem(drawable, title));
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }

    public void commit() {
        mIndexController = 0;
        for (IListItem obj : mItemList) {
            View tempItemView;
            tempItemView = mInflater.inflate(R.layout.ctl_cpt_img_item, null);
            setupItem(tempItemView, obj, mIndexController);
            mImgContainer.addView(tempItemView);

            mIndexController++;
        }
    }

    private void setupItem(View view, IListItem item, int index) {
        if (item instanceof ImgItem) {
            ImgItem tempItem = (ImgItem) item;
            setupImgItem(view, tempItem, index);
        }
    }

    private void setupImgItem(View view, ImgItem item, int index) {
        if (item.getDrawable() > -1) {
            ((ImageView) view.findViewById(R.id.iv_appicon)).setBackgroundResource(item.getDrawable());
        }

        view.setOnFocusChangeListener(this);
    }

    /**
     * 计算控件的大小
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measureWidth = measureWidth(widthMeasureSpec);
        int measureHeight = measureHeight(heightMeasureSpec);
        // 计算自定义的ViewGroup中所有子控件的大小
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        // 设置自定义的控件MyViewGroup的大小
        setMeasuredDimension(measureWidth, measureHeight);
    }

    private int measureWidth(int pWidthMeasureSpec) {
        int result = 0;
        int widthMode = MeasureSpec.getMode(pWidthMeasureSpec);// 得到模式
        int widthSize = MeasureSpec.getSize(pWidthMeasureSpec);// 得到尺寸

        switch (widthMode) {
            /**
             * mode共有三种情况，取值分别为MeasureSpec.UNSPECIFIED, MeasureSpec.EXACTLY,
             * MeasureSpec.AT_MOST。
             *
             *
             * MeasureSpec.EXACTLY是精确尺寸，
             * 当我们将控件的layout_width或layout_height指定为具体数值时如andorid
             * :layout_width=50dip，或者为FILL_PARENT是，都是控件大小已经确定的情况，都是精确尺寸。
             *
             *
             * MeasureSpec.AT_MOST是最大尺寸，
             * 当控件的layout_width或layout_height指定为WRAP_CONTENT时
             * ，控件大小一般随着控件的子空间或内容进行变化，此时控件尺寸只要不超过父控件允许的最大尺寸即可
             * 。因此，此时的mode是AT_MOST，size给出了父控件允许的最大尺寸。
             *
             *
             * MeasureSpec.UNSPECIFIED是未指定尺寸，这种情况不多，一般都是父控件是AdapterView，
             * 通过measure方法传入的模式。
             */
            case MeasureSpec.AT_MOST:
            case MeasureSpec.EXACTLY:
                result = widthSize;
                break;
        }
        return result;
    }

    private int measureHeight(int pHeightMeasureSpec) {
        int result = 0;

        int heightMode = MeasureSpec.getMode(pHeightMeasureSpec);
        int heightSize = MeasureSpec.getSize(pHeightMeasureSpec);

        switch (heightMode) {
            case MeasureSpec.AT_MOST:
            case MeasureSpec.EXACTLY:
                result = heightSize;
                break;
        }
        return result;
    }

    /**
     * 覆写onLayout，其目的是为了指定视图的显示位置，方法执行的前后顺序是在onMeasure之后，因为视图肯定是只有知道大小的情况下，
     * 才能确定怎么摆放
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        // 记录总高度
        int mTotalHeight = 0;
        // 遍历所有子视图
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);

            // 获取在onMeasure中计算的视图尺寸
            int measureHeight = childView.getMeasuredHeight();
            int measuredWidth = childView.getMeasuredWidth();

            childView.layout(l, mTotalHeight, measuredWidth, mTotalHeight
                    + measureHeight);

            mTotalHeight += measureHeight;

        }
    }


}
