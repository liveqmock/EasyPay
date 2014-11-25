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
 * 短信收款默认卡dialog
 * 
 * @author ChenGuangChi
 * @data: 2014年7月23日 下午1:36:17
 * @version: V1.0
 */
public class MySweetDialog extends SimpleDialogFragment {
	private final static String TAG = "esaypay";
	private MyAppListener listener;

	
	public void show(FragmentActivity activity, MyAppListener listener) {
		this.listener = listener;
		show(activity.getSupportFragmentManager(), TAG);
	}

	@Override
	protected Builder build(Builder builder) {
		builder.setMessage("　　为了您收款资金及时到账，请您首先设置收款账户信息。该账户将作为您的默认收款账户，如果需要，您可以通过“我的银行卡”菜单进行账户的新增、变更或删除。");
		builder.setNegativeButton("选择已有账户", new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dismiss();
				if(listener!=null){
					listener.onNegative();
				}
			}
		});
		builder.setPositiveButton("新增收款银行", new OnClickListener() {
			
			@Override
			public void onClick(View v) {
					dismiss();
					if(listener!=null){
						listener.onPositive();
					}
			}
		});
		return builder;
	}

	public interface MyAppListener {
		void onNegative();
		void onPositive();
	}

}
