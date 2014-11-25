/*
 * @Title:  SmsCodeDialog.java
 * @Copyright:  XXX Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * @Description:  TODO<请描述此文件是做什么的>
 * @author:  MeiYi
 * @data:  2014年7月23日 下午1:36:17
 * @version:  V1.0
 */
package com.inter.trade.ui.fragment.salaryget.util;

import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;

import com.inter.trade.view.styleddialog.SimpleDialogFragment;


/**
 * 绑定成功提示的dialog
 * @author  ChenGuangChi
 * @data:  2014年7月23日 下午1:36:17
 * @version:  V1.0
 */
public class BindSuccessTipsDialog extends SimpleDialogFragment{
	private final static String TAG="esaypay";
	
	private SalaryGetSuccessListener listener;
	
	public void show(FragmentActivity activity, SalaryGetSuccessListener listener) {
		show(activity.getSupportFragmentManager(), TAG);
		this.listener=listener;
	}
	@Override
	protected Builder build(Builder builder) {
		builder.setTitle("提示");
		builder.setMessage("签收成功，工资将发放到您绑定的落地银行卡，请查收核实。");
		builder.setPositiveButton("确定", new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(listener!=null){
					listener.onPositiveButton();
				}
				dismiss();
				
			}
		});
		
		return builder;
	}
	
	 public interface  SalaryGetSuccessListener{
		 public void onPositiveButton();
	 }
}
