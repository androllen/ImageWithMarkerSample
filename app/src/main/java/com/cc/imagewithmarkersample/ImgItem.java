package com.cc.imagewithmarkersample;

/**
 * Created by androllen on 2015/9/29.
 */
public class ImgItem implements IListItem {

    private boolean mClickable=true;
    private String mTitle;
    private int mDrawable = -1;
    @Override
    public boolean isClickable() {
        return false;
    }

    @Override
    public void setClickable(boolean clickable) {
        mClickable=clickable;
    }
    public int getDrawable() {
        return mDrawable;
    }
    public ImgItem(int _drawable,String _title) {
        this.mTitle = _title;
        mDrawable=_drawable;
    }
    public String getTitle() {
        return mTitle;
    }
}
