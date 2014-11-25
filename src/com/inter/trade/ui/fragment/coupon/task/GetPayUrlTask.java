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
import com.inter.trade.ui.fragment.coupon.parser.GetPayUrlParser;
import com.inter.trade.ui.fragment.coupon.util.UrlData;
import com.inter.trade.ui.fragment.waterelectricgas.data.ResponseStateListener;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PromptUtil;

/**
 * 商户收款   获取填写交易信息的url
 * @author  ChenGuangChi
 * @data:  2014年6月25日 下午2:20:23
 * @version:  V1.0
 */
public class GetPayUrlTask extends AsyncTask<String, Void, Void> {
	
	private Context context;
	
	private ResponseStateListener listener;
	
	public GetPayUrlTask(Context context, ResponseStateListener listener) {
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
				mData.putValue("paycardid", "4");//刷卡器ID

				List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas(
						"ApiExpresspayInfo", "couponReq", mData);
				GetPayUrlParser authorRegParser = new GetPayUrlParser();
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
					UrlData sms = parserResponse(mDatas);
					if (!ErrorUtil.create().errorDeal(LoginUtil.mLoginStatus,
							(Activity)context)) {
						
						return;
					}
					if(listener!=null){
						listener.onSuccess(sms, UrlData.class);
					}

				} catch (Exception e) {
					PromptUtil.showToast(context,
							context.getString(R.string.req_error));
					e.printStackTrace();
				}
			}
		}

		private UrlData parserResponse(List<ProtocolData> mDatas) {
			ResponseData response = new ResponseData();
			UrlData url=new UrlData();
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
					}
					
					List<ProtocolData> urlData = data.find("/requrl");
					if(urlData!=null){
						url.setUrl(urlData.get(0).mValue);
					}
					List<ProtocolData> bkntno = data.find("/bkordernumber");
					if(bkntno!=null){
						url.setOrderNo(bkntno.get(0).mValue);
					}
				}
			}
			return url;
		}
}
