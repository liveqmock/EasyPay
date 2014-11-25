/*
 * @Title:  SimpleMessageDialog.java
 * @Copyright:  XXX Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * @Description:  TODO<请描述此文件是做什么的>
 * @author:  MeiYi
 * @data:  2014年6月26日 上午9:06:59
 * @version:  V1.0
 */
package com.inter.trade.ui.fragment.gamerecharge.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.inter.trade.R;

/**
 * TODO<请描述这个类是干什么的>
 * @author  ChenGuangChi
 * @data:  2014年6月26日 上午9:06:59
 * @version:  V1.0
 */
public class SimpleMessageDialog extends DialogFragment {
	private TextView tvTitle;
	private TextView tvMessage;
	
	private Bundle bundle;
	private String title;
	private String message;
	
	public SimpleMessageDialog() {}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		bundle=getArguments();
		title=bundle.getString("title");
		message=bundle.getString("message");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
			View view = inflater.inflate(R.layout.dialog_simple_message, container);
			tvTitle =(TextView) view.findViewById(R.id.sdl__title);
			tvMessage =(TextView) view.findViewById(R.id.sdl__message);
			Button btn=(Button) view.findViewById(R.id.sdl__btn);
			btn.setText("确定");
			btn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					dismiss();
				}
			});
			tvTitle.setText(title);
			tvMessage.setText(message);
		return view;
	}
	public static void show(FragmentManager fm,String title,String message){
		Bundle b=new Bundle();
		b.putString("title", title);
		b.putString("message", message);
		SimpleMessageDialog fragment = new SimpleMessageDialog();
		fragment.setArguments(b);
		FragmentTransaction ft = fm.beginTransaction();
		ft.replace(R.id.func_container, fragment);
		ft.commitAllowingStateLoss();
	}
		
	
}
