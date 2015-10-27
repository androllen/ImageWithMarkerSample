package com.cc.imagewithmarkersample;


import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class UITableView extends LinearLayout {
    private static final String TAG = "UITableView";
    private int mIndexController = 0;
    private LayoutInflater mInflater;
    private FrameLayout mMainContainer;
    private RelativeLayout mListContainer;
    private List<IListItem> mItemList;
    private ClickListener mClickListener;

    public UITableView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mItemList = new ArrayList<IListItem>();
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMainContainer = (FrameLayout) mInflater.inflate(R.layout.ctl_cpt_btn_list_container, null);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
        addView(mMainContainer, params);
        mListContainer = (RelativeLayout) mMainContainer.findViewById(R.id.buttonsContainer);
    }

    /**
     * @param title
     */
    public void addBasicItem(String title) {
        mItemList.add(new BasicItem(title));
    }

    /**
     * @param title
     * @param summary
     */
    public void addBasicItem(String title, String summary) {
        mItemList.add(new BasicItem(title, summary));
    }

    /**
     * @param title
     * @param summary
     * @param color
     */
    public void addBasicItem(String title, String summary, int color) {
        mItemList.add(new BasicItem(title, summary, color));
    }

    /**
     * @param drawable
     * @param title
     * @param summary
     */
    public void addBasicItem(int drawable, String title, String summary) {
        mItemList.add(new BasicItem(drawable, title, summary));
    }

    /**
     * @param drawable
     * @param title
     * @param summary
     */
    public void addBasicItem(int drawable, String title, String summary, int color) {
        mItemList.add(new BasicItem(drawable, title, summary, color));
    }

    /**
     * @param item
     */
    public void addBasicItem(BasicItem item) {
        mItemList.add(item);
    }

    /**
     * @param itemView
     */
    public void addViewItem(ViewItem itemView) {
        mItemList.add(itemView);
    }

    public void commit() {
        mIndexController = 0;

        if (mItemList.size() > 1) {
            //when the list has more than one item
            for (IListItem obj : mItemList) {
                View tempItemView;
                tempItemView = mInflater.inflate(R.layout.ctl_cpt_img_item, null);
                setupItem(tempItemView, obj, mIndexController);
                tempItemView.setClickable(obj.isClickable());
                mListContainer.addView(tempItemView);
                mIndexController++;
            }
        } else if (mItemList.size() == 1) {
            //when the list has only one item
            View tempItemView = mInflater.inflate(R.layout.list_item_single, null);
            IListItem obj = mItemList.get(0);
            setupItem(tempItemView, obj, mIndexController);
            tempItemView.setClickable(obj.isClickable());
            mListContainer.addView(tempItemView);
        }
    }

    private void setupItem(View view, IListItem item, int index) {
        if (item instanceof BasicItem) {
            BasicItem tempItem = (BasicItem) item;
            setupBasicItem(view, tempItem, mIndexController);
        } else if (item instanceof ViewItem) {
            ViewItem tempItem = (ViewItem) item;
            setupViewItem(view, tempItem, mIndexController);
        }
    }

    private int startX, startY;

    /**
     * @param view
     * @param item
     * @param index
     */
    private void setupBasicItem(View view, BasicItem item, int index) {
        if (item.getDrawable() > -1) {
            ((ImageView) view.findViewById(R.id.iv_appicon)).setBackgroundResource(item.getDrawable());
        }
        view.setTag(index);
        if (item.isClickable()) {
            view.setOnTouchListener(new OnTouchListener() {
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
//							invalidate();
                            break;
                    }
                    return true;
                }
            });
            view.setOnFocusChangeListener(new OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    Log.d(TAG, "onFocusChange ");
                }
            });
            view.setOnKeyListener(new OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    Log.d(TAG, "onKey ");
                    return false;
                }
            });
            view.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View view) {
                    Log.d(TAG, "onClick ");
//					if (mClickListener != null)
//						mClickListener.onClick((Integer) view.getTag());
                }

            });
        } else {
            ((ImageView) view.findViewById(R.id.chevron)).setVisibility(View.GONE);
        }
    }

    /**
     * @param view
     * @param itemView
     * @param index
     */
    private void setupViewItem(View view, ViewItem itemView, int index) {
        if (itemView.getView() != null) {
            LinearLayout itemContainer = (LinearLayout) view.findViewById(R.id.itemContainer);
            itemContainer.removeAllViews();
            //itemContainer.removeAllViewsInLayout();
            itemContainer.addView(itemView.getView());

            if (itemView.isClickable()) {
                itemContainer.setTag(index);
                itemContainer.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mClickListener != null)
                            mClickListener.onClick((Integer) view.getTag());
                    }
                });
            }
        }
    }

    public interface ClickListener {
        void onClick(int index);
    }

    /**
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
