package com.inter.trade.ui.fragment.smsreceivepayment;

/*
 * @Title:  SmsCodeDialog.java
 * @Copyright:  XXX Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * @Description:  TODO<请描述此文件是做什么的>
 * @author:  MeiYi
 * @data:  2014年7月23日 下午1:36:17
 * @version:  V1.0
 */


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.inter.trade.R;
import com.inter.trade.ui.fragment.hotel.HotelPayConfirmFragment;
import com.inter.trade.util.PromptUtil;
import com.inter.trade.view.styleddialog.SimpleDialogFragment;

/**
 * 设置默认收款卡dialog
 * 
 * @author ChenGuangChi
 * @data: 2014年7月23日 下午1:36:17
 * @version: V1.0
 */
public class SmsToastDialogFragment extends SimpleDialogFragment {
	private final static String TAG = "esaypay";

	private TextView tv_note;

	public SmsToastDialogFragment(){
	}
	
	public static SmsToastDialogFragment newInstance(String message){
		SmsToastDialogFragment smsdf = new SmsToastDialogFragment(); 
        Bundle bundle = new Bundle(); 
        bundle.putString("alert-message", message); 
        smsdf.setArguments(bundle); 
        return smsdf; 
    }
	
	private String getMyMessage(){ 
        return getArguments().getString("alert-message"); 
    } 
    
	public void show(FragmentActivity activity) {
		show(activity.getSupportFragmentManager(), TAG);
	}

	@Override
	protected Builder build(Builder builder) {
		View view = View.inflate(getActivity(), R.layout.sms_toast_layout, null);
		tv_note = (TextView) view.findViewById(R.id.tv_note);
		tv_note.setText(getMyMessage());
		builder.setView(view);
		return builder;
	}

}
