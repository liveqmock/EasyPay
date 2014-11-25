/*
 * @Title:  SmsCodeDialog.java
 * @Copyright:  XXX Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * @Description:  TODO<请描述此文件是做什么的>
 * @author:  MeiYi
 * @data:  2014年7月23日 下午1:36:17
 * @version:  V1.0
 */
package com.inter.trade.view.dialog;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.inter.trade.R;
import com.inter.trade.view.styleddialog.SimpleDialogFragment;

/**
 * 手机号码校验的dialog
 * 
 * @author ChenGuangChi
 * @data: 2014年7月23日 下午1:36:17
 * @version: V1.0
 */
public class MobileDialog extends SimpleDialogFragment {
	private final static String TAG = "esaypay";
	private SmsCodeSubmitListener listener;

	private EditText code;
	
	
	public  static MobileDialog getInstance(String moblie){
			MobileDialog dialog=new MobileDialog();
			Bundle b=new Bundle();
			b.putString("moblie", moblie);
			dialog.setArguments(b);
			return dialog;
	}
	
	public void show(FragmentActivity activity, SmsCodeSubmitListener listener) {
		this.listener = listener;
		show(activity.getSupportFragmentManager(), TAG);
	}

	@Override
	protected Builder build(Builder builder) {
		Bundle b=getArguments();
		String mobile="";
		if(b!=null){
			mobile = b.getString("moblie");
		}
		
		builder.setNegativeButton("取消", new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		builder.setPositiveButton("好", new OnClickListener() {
			
			@Override
			public void onClick(View v) {
					dismiss();
					if(listener!=null){
						listener.onPositive();
					}
			}
		});
		
		View view = View.inflate(getActivity(), R.layout.dialog_message_mobile, null);
		TextView mesg=(TextView) view.findViewById(R.id.sdl__message);
		mesg.setText(mobile.substring(0, 3)+" "+mobile.substring(3, 7)+" "+mobile.substring(7, 11));
		builder.setView(view);
		return builder;
	}

	public interface SmsCodeSubmitListener {
		void onPositive();
	}

}
