/*
 * @Title:  BillTask.java
 * @Copyright:  XXX Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * @Description:  TODO<请描述此文件是做什么的>
 * @author:  MeiYi
 * @data:  2014年6月25日 下午2:20:23
 * @version:  V1.0
 */
package com.inter.trade.ui.fragment.coupon.task;

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
import com.inter.trade.ui.fragment.coupon.parser.CouponCreditPayParser;
import com.inter.trade.ui.fragment.gamerecharge.data.BillData;
import com.inter.trade.ui.fragment.waterelectricgas.data.ResponseStateListener;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PromptUtil;

/**
 * 商户收款 信用卡支付接口
 * @author  ChenGuangChi
 * @data:  2014年6月25日 下午2:20:23
 * @version:  V1.0
 */
public class CouponCreditPayTask extends AsyncTask<String, Void, Void> {
	
	private Context context;
	
	private ResponseStateListener listener;
	
	public CouponCreditPayTask(Context context, ResponseStateListener listener) {
		super();
		this.context = context;
		this.listener = listener;
	}

	ProtocolRsp mRsp;
	@Override
	protected Void doInBackground(String... params) {
		
			try {
				CommonData mData = new CommonData();
				mData.putValue("couponid", params[0]);
				mData.putValue("couponmoney", params[1]);//购买金额
				mData.putValue("paycardid", params[2]);//刷卡器ID
				mData.putValue("bkcardbank", params[3]);//银行名
				mData.putValue("bkCardno", params[4]);//银行卡号
				mData.putValue("bkcardman", params[5]);//执卡人
				mData.putValue("bkcardexpireMonth", params[6]);//月份
				mData.putValue("bkcardmanidcard", params[7]);//执卡人身份证
				mData.putValue("bankid", params[8]);//银行id
				mData.putValue("bkcardexpireYear", params[9]);//年
				mData.putValue("bkcardPhone", params[10]);//银行预留手机号码
				mData.putValue("bkcardcvv", params[11]);//cvv
				mData.putValue("paytype", params[12]);//业务类型
				mData.putValue("bankno", params[13]);//银行编号
				mData.putValue("bankname", params[14]);//银行名

				List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas(
						"ApiyibaoPayInfo", "couponPayReq", mData);
				CouponCreditPayParser authorRegParser = new CouponCreditPayParser();
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
