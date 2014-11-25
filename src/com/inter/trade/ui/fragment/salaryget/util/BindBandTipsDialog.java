/*
 * @Title:  SmsCodeDialog.java
 * @Copyright:  XXX Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * @Description:  TODO<请描述此文件是做什么的>
 * @author:  MeiYi
 * @data:  2014年7月23日 下午1:36:17
 * @version:  V1.0
 */
package com.inter.trade.ui.fragment.salaryget.util;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.inter.trade.view.styleddialog.SimpleDialogFragment;


/**
 * 绑定银行卡提示的dialog
 * @author  ChenGuangChi
 * @data:  2014年7月23日 下午1:36:17
 * @version:  V1.0
 */
public class BindBandTipsDialog extends SimpleDialogFragment{
	private final static String TAG="esaypay";
	private BindBankTipListener listener;
	
	private EditText code;

	public static BindBandTipsDialog getInstance(String message){
		BindBandTipsDialog f=new BindBandTipsDialog();
		Bundle bundle =new Bundle();
		bundle.putString("message", message);
		f.setArguments(bundle);
		return f;
	}
	
	public void show(FragmentActivity activity,BindBankTipListener listener) {
		this.listener=listener;
		show(activity.getSupportFragmentManager(), TAG);
	}
	@Override
	protected Builder build(Builder builder) {
		Bundle msg = getArguments();
		if(msg!=null){
			String message = msg.getString("message");
			builder.setMessage(message);
		}
		builder.setTitle("提示");
		
		//builder.setMessage("签收失败，您还没有绑定工资落地银行卡，请先进行绑定。");
		builder.setPositiveButton("马上绑定", new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(listener!=null){
					listener.onPositive();
				}
				dismiss();
			}
		});
		
		builder.setNegativeButton("取消", new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		
		return builder;
	}
		
	public interface BindBankTipListener{
		void onPositive();
	}
	
	/**
	 * 填充短信 
	 * @param sms
	 * @throw
	 * @return void
	 */
	public void setSms(String sms){
		code.setText(sms);
	}
}
