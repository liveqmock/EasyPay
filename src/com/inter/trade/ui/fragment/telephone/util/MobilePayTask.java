/*
 * @Title:  BillTask.java
 * @Copyright:  XXX Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * @Description:  TODO<请描述此文件是做什么的>
 * @author:  MeiYi
 * @data:  2014年6月25日 下午2:20:23
 * @version:  V1.0
 */
package com.inter.trade.ui.fragment.telephone.util;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import com.inter.protocol.ProtocolData;
import com.inter.protocol.ProtocolRsp;
import com.inter.protocol.util.ProtocolUtil;
import com.inter.trade.R;
import com.inter.trade.data.CommonData;
import com.inter.trade.data.ResponseData;
import com.inter.trade.error.ErrorUtil;
import com.inter.trade.net.HttpUtil;
import com.inter.trade.ui.creditcard.data.SmsCode;
import com.inter.trade.ui.fragment.gamerecharge.data.BillData;
import com.inter.trade.ui.fragment.waterelectricgas.data.ResponseStateListener;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PromptUtil;

/**
 * 手机充值 信用卡支付接口
 * @author  ChenGuangChi
 * @data:  2014年6月25日 下午2:20:23
 * @version:  V1.0
 */
public class MobilePayTask extends AsyncTask<String, Void, Void> {
	
	private Context context;
	
	private ResponseStateListener listener;
	
	public MobilePayTask(Context context, ResponseStateListener listener) {
		super();
		this.context = context;
		this.listener = listener;
	}

	ProtocolRsp mRsp;
	@Override
	protected Void doInBackground(String... params) {
		
			try {
				CommonData mData = new CommonData();
				mData.putValue("rechargeMoney", params[0]);
				mData.putValue("payMoney", params[1]);
				mData.putValue("rechargePhone", params[2]);
				mData.putValue("bankCardId", params[3]);
				mData.putValue("bankId", params[4]);
				mData.putValue("manCardId", params[5]);
				mData.putValue("payPhone", params[6]);
				mData.putValue("manName", params[7]);
				mData.putValue("expireYear", params[8]);
				mData.putValue("expireMonth", params[9]);
				mData.putValue("cvv", params[10]);
				mData.putValue("mobileProvince", params[11]);
				mData.putValue("paycardid", params[12]);
				//mData.putValue("bankname", params[13]);

				List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas(
						"ApiMobileRechargeInfoV2", "PayWithCreditCard", mData);
				MobilePayParser authorRegParser = new MobilePayParser();
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
				PromptUtil.showToast(context,
				context.getString(R.string.net_error));
			} else {
				try {
					List<ProtocolData> mDatas = mRsp.mActions;
					SmsCode sms = parserResponse(mDatas);
					if (!ErrorUtil.create().dealErrorWithDialog(LoginUtil.mLoginStatus,
							(Activity)context)) {
						
						return;
					}
					if(listener!=null){
						listener.onSuccess(sms, SmsCode.class);
					}

				} catch (Exception e) {
					PromptUtil.showToast(context,
							context.getString(R.string.req_error));
					e.printStackTrace();
				}
			}
		}

		private SmsCode parserResponse(List<ProtocolData> mDatas) {
			ResponseData response = new ResponseData();
			SmsCode smscode=new SmsCode();
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
						LoginUtil.mLoginStatus.message = message.get(0)
								.getmValue();
						smscode.setMessage(message.get(0).getmValue());
					}
					
					List<ProtocolData> orderId = data.find("/orderId");
					if (orderId != null) {
						smscode.setOrderId(orderId.get(0).getmValue());
					}
					
					List<ProtocolData> code = data.find("/verifyCode");
					if(code!=null){
						if("1".equals(code.get(0).getmValue())){//是否需要验证码
							smscode.setNeed(true);
						}else{
							smscode.setNeed(false);
						}
					}
				}
			}
			return smscode;
		}
}
