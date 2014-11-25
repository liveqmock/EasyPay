package com.inter.trade.ui.fragment;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

public class MyLinearLayout extends LinearLayout{

	public MyLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public MyLinearLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		for(int i =0;i<getChildCount();i++){
			View child = getChildAt(i);
			if(child instanceof ViewPager){
				Log.d("SlidingMenuView", "MyLinearLayout ViewPager");
				return !child.dispatchTouchEvent(event);
			}
		}
		
		return super.onTouchEvent(event);
	}
	
}
