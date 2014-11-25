/*
 * @Title:  BillTask.java
 * @Copyright:  XXX Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * @Description:  TODO<请描述此文件是做什么的>
 * @author:  MeiYi
 * @data:  2014年6月25日 下午2:20:23
 * @version:  V1.0
 */
package com.inter.trade.ui.creditcard.task;

import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;

import com.inter.protocol.ProtocolData;
import com.inter.protocol.ProtocolRsp;
import com.inter.protocol.util.ProtocolUtil;
import com.inter.trade.R;
import com.inter.trade.ResponseMoreStateListener;
import com.inter.trade.data.CommonData;
import com.inter.trade.data.ResponseData;
import com.inter.trade.error.ErrorUtil;
import com.inter.trade.net.HttpUtil;
import com.inter.trade.ui.creditcard.data.SmsCode;
import com.inter.trade.ui.creditcard.parser.SmsCodeParser;
import com.inter.trade.ui.fragment.gamerecharge.data.BillData;
import com.inter.trade.ui.fragment.gamerecharge.parser.GetBillParser;
import com.inter.trade.ui.fragment.waterelectricgas.data.ResponseStateListener;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PromptUtil;

/**
 * 手机充值 信用卡短信验证码接口
 * @author  ChenGuangChi
 * @data:  2014年6月25日 下午2:20:23
 * @version:  V1.0
 */
public class SmsCodeTask extends AsyncTask<String, Void, Void> {
	
	private Context context;
	
	private ResponseStateListener listener;
	
	private String[] params;
	
	public SmsCodeTask(Context context, ResponseStateListener listener) {
		super();
		this.context = context;
		this.listener = listener;
	}

	ProtocolRsp mRsp;
	@Override
	protected Void doInBackground(String... params) {
			this.params=params;
			try {
				CommonData mData = new CommonData();
				mData.putValue("orderId", params[0]);
				mData.putValue("verifyCode", params[1]);

				List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas(
						params[2], params[3], mData);
				SmsCodeParser authorRegParser = new SmsCodeParser();
				mRsp = HttpUtil.doRequest(authorRegParser, mDatas);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				mRsp = null;
			}

			return null;
	}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			PromptUtil.showDialog(context, context.getResources()
					.getString(R.string.loading));
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			PromptUtil.dissmiss();
			if (mRsp == null) {
//				PromptUtil.showToast(context,
//				context.getString(R.string.net_error));
				showAgainLoadDialog();
			} else {
				try {
					List<ProtocolData> mDatas = mRsp.mActions;
					String message = parserResponse(mDatas);
					if (!ErrorUtil.create().dealErrorWithDialog(LoginUtil.mLoginStatus,
							(Activity)context)) {
						return;
					}
					if(listener!=null){
						listener.onSuccess(message, String.class);
					}

				} catch (Exception e) {
					PromptUtil.showToast(context,
							context.getString(R.string.req_error));
					e.printStackTrace();
				}
			}
		}

		private String parserResponse(List<ProtocolData> mDatas) {
			ResponseData response = new ResponseData();
			LoginUtil.mLoginStatus.mResponseData = response;
			for (ProtocolData data : mDatas) {
				if (data.mKey.equals(ProtocolUtil.msgheader)) {
					ProtocolUtil.parserResponse(response, data);

				} else if (data.mKey.equals(ProtocolUtil.msgbody)) {

					List<ProtocolData> result = data.find("/result");
					if (result != null) {
						LoginUtil.mLoginStatus.result = result.get(0)
								.getmValue();
					}

					List<ProtocolData> message = data.find("/message");
					if (message != null) {
						LoginUtil.mLoginStatus.message=message.get(0).getmValue();
						return message.get(0).getmValue();
					}
				}
			}
			return "";
		}
		
		private void showAgainLoadDialog () {
			new AlertDialog.Builder(context).setTitle("网络异常").setMessage("网络可能出现异常是否重试?")
			.setPositiveButton("重试", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					  new SmsCodeTask(context, listener).execute(params);
				}
			}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			}).setCancelable(false).show();
		}
}
