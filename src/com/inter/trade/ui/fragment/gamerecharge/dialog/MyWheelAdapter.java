/*
 * @Title:  MyWheelAdapter.java
 * @Copyright:  XXX Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * @Description:  TODO<请描述此文件是做什么的>
 * @author:  MeiYi
 * @data:  2014年6月25日 上午9:16:03
 * @version:  V1.0
 */
package com.inter.trade.ui.fragment.gamerecharge.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.inter.trade.R.color;
import com.inter.trade.ui.views.wheelwidget.adapters.ArrayWheelAdapter;

/**
 * TODO<请描述这个类是干什么的>
 * @author  ChenGuangChi
 * @data:  2014年6月25日 上午9:16:03
 * @version:  V1.0
 */
public class MyWheelAdapter extends ArrayWheelAdapter<String> {
	
	private int currentItem;
	/** 
	 * <默认构造函数>
	 */
	public MyWheelAdapter(Context context, String[] items) {
		super(context, items);
	}

	@Override
	public View getItem(int index, View convertView, ViewGroup parent) {
		 if (index >= 0 && index < getItemsCount()) {
	            if (convertView == null) {
	                convertView = getView(itemResourceId, parent);
	            }
	            TextView textView = getTextView(convertView, itemTextResourceId);
	            if (textView != null) {
	                CharSequence text = getItemText(index);
	                if (text == null) {
	                    text = "";
	                }
	                textView.setText(text);
	               
	    
	                if (itemResourceId == TEXT_VIEW_ITEM_RESOURCE) {
	                    configureTextView(textView);
	                }
	                textView.setTextColor(color.game_gray);
	                /*if(currentItem==index){
	                	textView.setTextColor(color.game_blue);
	                }else{
	                	textView.setTextColor(color.game_blue);
	                }*/
	            }
	            return convertView;
	        }
	    	return null;
	}
	public void setCurrentItem(int item){
		currentItem=item;
	}
	
}
