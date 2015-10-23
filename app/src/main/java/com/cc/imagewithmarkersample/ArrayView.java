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

    private LayoutInflater mInflater;
    private FrameLayout mMainContainer;
    private RelativeLayout mImgContainer;
    private List<IListItem> mItemList;
    private int mIndexController = 0;

    public ArrayView(Context context) {
        super(context);
    }

    public ArrayView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mItemList = new ArrayList<IListItem>();

        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMainContainer = (FrameLayout) mInflater.inflate(R.layout.ctl_cpt_btn_list_container, null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.FILL_PARENT);
        addView(mMainContainer, params);
        mImgContainer = (RelativeLayout) findViewById(R.id.buttonsContainer);

    }

    public void addBasicItem(int drawable, String title) {
        mItemList.add(new ImgItem(drawable, title));
    }

    public void commit() {
        mIndexController = 0;
        for (IListItem obj : mItemList) {
            View tempItemView;
            tempItemView = mInflater.inflate(R.layout.ctl_cpt_img_item, null);
            setupItem(tempItemView, obj, mIndexController);
            tempItemView.setClickable(obj.isClickable());
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
        view.setTag(index);
        if (item.isClickable()) {
            view.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View view) {

                }

            });
        }
    }

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
