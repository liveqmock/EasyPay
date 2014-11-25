/*
 * @Title:  BaseFragmentActivity.java
 * @Copyright:  XXX Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * @Description:  TODO<请描述此文件是做什么的>
 * @author:  MeiYi
 * @data:  2014年7月30日 上午10:02:00
 * @version:  V1.0
 */
package com.inter.trade.qrsacncode;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import com.inter.trade.R;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * TODO<请描述这个类是干什么的>
 * @author  ChenGuangChi
 * @data:  2014年7月30日 上午10:02:00
 * @version:  V1.0
 */
public class BaseFragmentActivity extends FragmentActivity {
	protected Button menu;
	
	/**
	 * 设置标题
	 * 
	 * @param titleString
	 */
	protected void setTitle(String titleString) {
		TextView title = (TextView) findViewById(R.id.title_name);
		if (title == null) {
			return;
		}
		title.setText(titleString);
	}

	/**
	 * 设置返回事件
	 */
	protected void setBackVisible() {
		
		Button back = (Button) findViewById(R.id.title_back_btn);

		if (back == null) {
			return;
		}
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// FragmentManager manager =
				// getActivity().getSupportFragmentManager();
				// int len = manager.getBackStackEntryCount();
				// if(len>0){
				// manager.popBackStack();
				// }else{
				finish();
				// }
			}
		});

		menu = (Button) findViewById(R.id.title_menu_btn);
		menu.setVisibility(View.GONE);
		Button right = (Button) findViewById(R.id.title_right_btn);
		right.setVisibility(View.GONE);
	}

	/**
	 * 设置右侧按钮可见，及事件
	 * 
	 * @param onClickListener
	 * @param title
	 */
	protected void setRightVisible(OnClickListener onClickListener, String title) {

		Button back = (Button)findViewById(R.id.title_back_btn);

		if (back == null) {
			return;
		}
		Button right = (Button)findViewById(R.id.title_right_btn);
		right.setVisibility(View.VISIBLE);
		if (title != null && !title.equals("")) {
			right.setText(title);
		}
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		right.setOnClickListener(onClickListener);
		menu = (Button)findViewById(R.id.title_menu_btn);
		menu.setVisibility(View.GONE);
	}

	protected String getTitle(int id) {
		
		return getResources().getString(id);
	}

	protected void setMenuVisible() {
		
		Button back = (Button) findViewById(R.id.title_back_btn);

		if (back == null) {
			return;
		}
		back.setVisibility(View.GONE);
		menu = (Button) findViewById(R.id.title_menu_btn);
		menu.setVisibility(View.GONE);
	}
}
