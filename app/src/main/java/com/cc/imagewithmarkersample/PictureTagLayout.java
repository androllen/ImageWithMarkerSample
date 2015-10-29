package com.cc.imagewithmarkersample;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.RelativeLayout;

import com.cc.imagewithmarkersample.PictureTagView.Status;
import com.cc.imagewithmarkersample.PictureTagView.Direction;

@SuppressLint("NewApi")
public class PictureTagLayout extends RelativeLayout implements OnTouchListener {
	private static final int CLICKRANGE = 5;
	int startX = 0;
	int startY = 0;
	int startTouchViewLeft = 0;
	int startTouchViewTop = 0;
	private View touchView,clickView;
	public PictureTagLayout(Context context) {
		super(context, null);
	}
	public PictureTagLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	private void init(){
		this.setOnTouchListener(this);
	}
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			touchView = null;
			if(clickView!=null){
				((PictureTagView)clickView).setStatus(Status.Normal);
				clickView = null;
			}
			startX = (int) event.getX();
			startY = (int) event.getY();
			if(hasView(startX,startY)){
				startTouchViewLeft = touchView.getLeft();
				startTouchViewTop = touchView.getTop();
			}
			else{				
				addItem(startX,startY);
			}
			break;
		case MotionEvent.ACTION_MOVE:
			moveView((int) event.getX(),
					(int) event.getY());
			break;
		case MotionEvent.ACTION_UP:
			int endX = (int) event.getX();
			int endY = (int) event.getY();

			if(touchView!=null&& Math.abs(endX - startX)<CLICKRANGE&& Math.abs(endY - startY)<CLICKRANGE){

				((PictureTagView)touchView).setStatus(Status.Edit);
				clickView = touchView;
			}
			touchView = null;
			break;
		}
		return true;
	}


	private void addItem(int x,int y){
		View view = null;
		RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		if(x>getWidth()*0.5){
			params.leftMargin = x - PictureTagView.getViewWidth();
			view = new PictureTagView(getContext(),Direction.Right);
		}
		else{
			params.leftMargin = x;
			view = new PictureTagView(getContext(),Direction.Left);
		}

		params.topMargin = y;

		if(params.topMargin<0){
			params.topMargin =0;
		}
		else if((params.topMargin+PictureTagView.getViewHeight())>getHeight()){
			params.topMargin = getHeight() - PictureTagView.getViewHeight();
		}


		this.addView(view, params);
	}
	private void moveView(int x,int y){
		if(touchView == null) return;
		RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.leftMargin = x - startX + startTouchViewLeft;
		params.topMargin = y - startY + startTouchViewTop;
		if(params.leftMargin<0||(params.leftMargin+touchView.getWidth())>getWidth())params.leftMargin = touchView.getLeft();
		if(params.topMargin<0||(params.topMargin+touchView.getHeight())>getHeight())params.topMargin = touchView.getTop();
		touchView.setLayoutParams(params);
	}
	private boolean hasView(int x,int y){
		for(int index = 0; index < this.getChildCount(); index ++){
			View view = (View)this.getChildAt(index);
			int left = view.getLeft();
			int top = view.getTop();
			int right = view.getRight();
			int bottom = view.getBottom();
			Rect rect = new Rect(left, top, right, bottom);
			boolean contains = rect.contains(x, y);
			if(contains){
				touchView = view;
				touchView.bringToFront();
				return true;
			}
		}
		touchView = null;
		return false;
	}
}
