package com.inter.trade.ui.fragment;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.GridView;

public class MyGridView extends GridView{

	public MyGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public MyGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public MyGridView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	float downX=0;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		
//		if (event.getAction() == MotionEvent.ACTION_DOWN){
//			downX = event.getX();
//		} else if (event.getAction() == MotionEvent.ACTION_MOVE){
//			float detalX = downX - event.getX();
//			if(detalX<0 && Math.abs(detalX)>20){
//				Log.d("SlidingMenuView", "MyGridView false"+detalX);
//					return false;
//			}
//		}
		return super.onTouchEvent(event);
	}
	
}
