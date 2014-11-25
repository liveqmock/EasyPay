/*
 * @Title:  SmsCodeDialog.java
 * @Copyright:  XXX Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * @Description:  TODO<请描述此文件是做什么的>
 * @author:  MeiYi
 * @data:  2014年7月23日 下午1:36:17
 * @version:  V1.0
 */
package com.inter.trade.ui.creditcard;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.inter.trade.R;
import com.inter.trade.util.PromptUtil;
import com.inter.trade.view.styleddialog.SimpleDialogFragment;

/**
 * 短信验证码的输入dialog
 * 
 * @author ChenGuangChi
 * @data: 2014年7月23日 下午1:36:17
 * @version: V1.0
 */
public class SmsCodeDialog extends SimpleDialogFragment {
	private final static String TAG = "esaypay";
	private SmsCodeSubmitListener listener;

	private EditText code;

	private Button btnCommit;

	public void show(FragmentActivity activity, SmsCodeSubmitListener listener) {
		this.listener = listener;
		show(activity.getSupportFragmentManager(), TAG);
		InputMethodManager imm = (InputMethodManager) activity
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(code, InputMethodManager.RESULT_SHOWN);
		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
				InputMethodManager.HIDE_IMPLICIT_ONLY);
	}

	@Override
	protected Builder build(Builder builder) {
		View view = View.inflate(getActivity(), R.layout.dialog_smscode, null);
		code = (EditText) view.findViewById(R.id.et_smscode);
		btnCommit = (Button) view.findViewById(R.id.btn_commit);
		btnCommit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (listener != null) {
					String smscode = code.getText() + "";
					if (TextUtils.isEmpty(smscode)) {
						PromptUtil.showToast(getActivity(), "亲,请输入手机验证码!");
						return;
					}

					listener.onPositive(smscode, SmsCodeDialog.this);
				}
				dismiss();
			}
		});
		// builder.setTitle("填写接收到的手机验证码");
		builder.setView(view);
		return builder;
	}

	public interface SmsCodeSubmitListener {
		void onPositive(String code, SmsCodeDialog dialog);
	}

	/**
	 * 填充短信
	 * 
	 * @param sms
	 * @throw
	 * @return void
	 */
	public void setSms(String sms) {
		code.setText(sms);
	}
}
