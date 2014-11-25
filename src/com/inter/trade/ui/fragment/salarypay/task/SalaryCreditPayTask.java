/*
 * @Title:  BillTask.java
 * @Copyright:  XXX Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * @Description:  TODO<请描述此文件是做什么的>
 * @author:  MeiYi
 * @data:  2014年6月25日 下午2:20:23
 * @version:  V1.0
 */
package com.inter.trade.ui.fragment.salarypay.task;

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
import com.inter.trade.ui.fragment.salarypay.bean.CreditPayInfo;
import com.inter.trade.ui.fragment.salarypay.parser.CreditPayParser;
import com.inter.trade.ui.fragment.waterelectricgas.data.ResponseStateListener;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PromptUtil;

/**
 * 信用卡支付接口
 * @author  ChenGuangChi
 * @data:  2014年6月25日 下午2:20:23
 * @version:  V1.0
 */
public class SalaryCreditPayTask extends AsyncTask<CreditPayInfo, Void, Void> {
	
	private Context context;
	
	private ResponseStateListener listener;
	
	public SalaryCreditPayTask(Context context, ResponseStateListener listener) {
		super();
		this.context = context;
		this.listener = listener;
	}

	ProtocolRsp mRsp;
	@Override
	protected Void doInBackground(CreditPayInfo... params) {
		CreditPayInfo info=params[0];
			try {
				CommonData mData = new CommonData();
				
				mData.putValue("wagepaymoney", info.wagepaymoney);
				mData.putValue("wagemonth", info.wagemonth);
				mData.putValue("wagemoney", info.wagemoney);
				mData.putValue("paycardid", info.paycardid);
				mData.putValue("bkcardbank", info.bkcardbank);
				mData.putValue("bkCardno", info.bkCardno);
				mData.putValue("bkcardman", info.bkcardman);
				mData.putValue("bkcardexpireMonth", info.bkcardexpireMonth);
				mData.putValue("bkcardmanidcard", info.bkcardmanidcard);
				mData.putValue("bankid", info.bankid);
				mData.putValue("bkcardexpireYear", info.bkcardexpireYear);
				mData.putValue("bkcardPhone", info.bkcardPhone);
				mData.putValue("bkcardcvv", info.bkcardcvv);
				mData.putValue("wagelistid", info.wagelistid);

				List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas(
						"ApiWageInfo", "ybwagePayrq", mData);
				CreditPayParser authorRegParser = new CreditPayParser();
				mRsp = HttpUtil.doRequest(authorRegParser, mDatas);
			} catch (Exception e) {
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
					
					
					List<ProtocolData> code = data.find("/verifyCode");
					if(code!=null){
						if("1".equals(code.get(0).getmValue())){//是否需要验证码
							smscode.setNeed(true);
						}else{
							smscode.setNeed(false);
						}
					}
					List<ProtocolData> bkordernumber = data.find("/bkordernumber");
					if(bkordernumber!=null){
						smscode.setBkordernumber(bkordernumber.get(0).mValue);
					}
					List<ProtocolData> bkntno = data.find("/bkntno");
					if(bkntno!=null){
						smscode.setBkntno(bkntno.get(0).mValue);
					}
					List<ProtocolData> token = data.find("/verifytoken");
					if(token!=null){
						smscode.setVerifytoken(token.get(0).mValue);
					}
				}
			}
			return smscode;
		}
}
