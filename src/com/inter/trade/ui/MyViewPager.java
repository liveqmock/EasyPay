package com.inter.trade.ui;

import com.inter.trade.log.Logger;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class MyViewPager extends ViewPager {
	
	private float downX, downY;
	
	public MyViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public MyViewPager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return super.onInterceptTouchEvent(arg0);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		Logger.d("onTouchEvent", ""+event.getAction());
		
		if (event.getAction() == MotionEvent.ACTION_DOWN){
			downX = event.getRawX();
			downY = event.getRawY();
		} else if (event.getAction() == MotionEvent.ACTION_MOVE){
			float detalX = downX - event.getRawX();
			//向右滑动
			if(detalX<0){
				int i = getCurrentItem();
				if(i== 0){
					return false;
				}else {
					return super.onTouchEvent(event);
				}
			}
//			float detalX = Math.abs(downX - event.getRawX());
//			float detalY = Math.abs(downY - event.getRawY());
//			if (detalX > detalY && getParent() != null){
//				getParent().requestDisallowInterceptTouchEvent(true);
//			}
		}
		return super.onTouchEvent(event);
		
	}
	
}
