package com.inter.trade.ui.fragment;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

public class MyFramlayout extends FrameLayout{
	public boolean flag=false;
	
	public MyFramlayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public MyFramlayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public MyFramlayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
//		for(int i =0;i<getChildCount();i++){
//			View child = getChildAt(i);
//			if(child instanceof ViewPager){
//				Log.d("SlidingMenuView", "MyLinearLayout ViewPager");
//				return !child.dispatchTouchEvent(event);
//			}
//		}
//		if(event.getAction() == MotionEvent.ACTION_DOWN && flag){
//			flag = false;
//			return flag;
//		}
		
		return super.onTouchEvent(event);
	}
}
