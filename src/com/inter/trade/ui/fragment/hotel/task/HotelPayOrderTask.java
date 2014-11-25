/*
 * @Title:  BillTask.java
 * @Copyright:  XXX Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * @Description:  TODO<请描述此文件是做什么的>
 * @author:  MeiYi
 * @data:  2014年6月25日 下午2:20:23
 * @version:  V1.0
 */
package com.inter.trade.ui.fragment.hotel.task;

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
import com.inter.trade.ui.fragment.hotel.util.HotelCreateOrderParser;
import com.inter.trade.ui.fragment.hotel.util.HotelPayOrderParser;
import com.inter.trade.ui.fragment.waterelectricgas.data.ResponseStateListener;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PromptUtil;

/**
 * 酒店预订  信用卡支付接口
 * @author  haifengli
 * @data:  2014年10月17日 下午2:20:23
 * @version:  V1.0
 */
public class HotelPayOrderTask extends AsyncTask<String, Void, Void> {
	
	private Context context;
	
	private ResponseStateListener listener;
	private CommonData hotelData;
	
	public HotelPayOrderTask(Context context, ResponseStateListener listener, CommonData hotelData) {
		super();
		this.context = context;
		this.listener = listener;
		this.hotelData = hotelData;
	}

	ProtocolRsp mRsp;
	@Override
	protected Void doInBackground(String... params) {
		
			try {
				List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas(
						"ApiHotel", "PayOrder", hotelData);
				HotelPayOrderParser authorRegParser = new HotelPayOrderParser();
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
					
					List<ProtocolData> code = data.find("/needVerifyCode");
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
