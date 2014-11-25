/*
 * @Title:  SmsCodeDialog.java
 * @Copyright:  XXX Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * @Description:  TODO<请描述此文件是做什么的>
 * @author:  MeiYi
 * @data:  2014年7月23日 下午1:36:17
 * @version:  V1.0
 */
package com.inter.trade.ui.fragment.salarypay;

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
public class WarmPromtDialog extends SimpleDialogFragment{
	private final static String TAG="esaypay";
	private ButtonListener listener;
	
	private EditText code;
	
	public void show(FragmentActivity activity,ButtonListener listener) {
		this.listener=listener;
		show(activity.getSupportFragmentManager(), TAG);
	}
	@Override
	protected Builder build(Builder builder) {
		builder.setTitle("提示");
		builder.setMessage("开始支付后本月工资信息将不能再更改，请确认工资清单是否已核对完毕？");
		builder.setPositiveButton("确定", new OnClickListener() {
			
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
		
	public interface ButtonListener{
		/**
		 * 点击确定按钮 
		 * @throw
		 * @return void
		 */
		void onPositive();
	}
}
