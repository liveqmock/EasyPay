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
import com.inter.trade.ui.creditcard.data.DefaultBankCardData;
import com.inter.trade.ui.creditcard.parser.PayCardCheckParser;
import com.inter.trade.ui.fragment.waterelectricgas.data.ResponseStateListener;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PromptUtil;

/**
 * 根据银行卡号获取是否有历史记录
 * @author  ChenGuangChi
 * @data:  2014年6月25日 下午2:20:23
 * @version:  V1.0
 */
public class PayCardCheckTask extends AsyncTask<String, Void, Void> {
	
	private Context context;
	
	private ResponseStateListener listener;
	
	/***返回错误信息时，是否显示Toast*/
	private boolean isShowToast=true;
	
	public PayCardCheckTask(Context context, ResponseStateListener listener,boolean isShowToast) {
		super();
		this.context = context;
		this.listener = listener;
		this.isShowToast=isShowToast;
	}

	ProtocolRsp mRsp;
	@Override
	protected Void doInBackground(String... params) {
		
			try {
				CommonData mData = new CommonData();
				mData.putValue("paycardkey", params[0]);//刷卡器ID
				mData.putValue("bkcardno", params[1]);//银行卡号
				mData.putValue("paytype", params[2]);//交易类型

				List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas(
						"ApiAuthorInfo", "payCardCheck", mData);
				PayCardCheckParser authorRegParser = new PayCardCheckParser();
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
					DefaultBankCardData card = parserResponse(mDatas);
					
					//不成功，即没有该银行卡记录，传回空值null给回调函数onSuccess(),
					//防止下面dealError() 处理retcode==200 等等,没有执行onSuccess()
					if(!(LoginUtil.mLoginStatus.result.equals(ProtocolUtil.SUCCESS))){
						if(listener!=null){
							listener.onSuccess(null, DefaultBankCardData.class);
						}
					}
					
					if(isShowToast){//弹吐司
						if (!ErrorUtil.create().errorDeal(LoginUtil.mLoginStatus,
								(Activity)context)) {
							return;
						}
					}else{//不弹吐司
						if (!ErrorUtil.create().dealError(LoginUtil.mLoginStatus,
								(Activity)context)) {
							return;
						}
					}
					
					if(LoginUtil.mLoginStatus.result.equals(ProtocolUtil.SUCCESS)){
						if(listener!=null){
							listener.onSuccess(card, DefaultBankCardData.class);
						}
					}

				} catch (Exception e) {
					PromptUtil.showToast(context,
							context.getString(R.string.req_error));
					e.printStackTrace();
				}
			}
		}

		private DefaultBankCardData parserResponse(List<ProtocolData> mDatas) {
			ResponseData response = new ResponseData();
			LoginUtil.mLoginStatus.mResponseData = response;
			DefaultBankCardData card=new DefaultBankCardData();
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
					}
					List<ProtocolData> bkcardno = data.find("/bkcardno");
					if (bkcardno != null) {
						card.setBkcardno(bkcardno.get(0).mValue);
					}
					List<ProtocolData> bkcardman = data.find("/bkcardman");
					if (bkcardman != null) {
						card.setBkcardbankman(bkcardman.get(0).mValue);
					}
					List<ProtocolData> bkcardphone = data.find("/bkcardphone");
					if (bkcardphone != null) {
						card.setBkcardbankphone(bkcardphone.get(0).mValue);
					}
					List<ProtocolData> bkcardbankid = data.find("/bkcardbankid");
					if (bkcardbankid != null) {
						card.setBkcardbankid(bkcardbankid.get(0).mValue);
					}
					List<ProtocolData> bkcardbankname = data.find("/bkcardbankname");
					if (bkcardbankname != null) {
						card.setBkcardbank(bkcardbankname.get(0).mValue);
					}
					List<ProtocolData> bkcardyxmonth = data.find("/bkcardyxmonth");
					if (bkcardyxmonth != null) {
						card.setBkcardyxmonth(bkcardyxmonth.get(0).mValue);
					}
					List<ProtocolData> bkcardyxyear = data.find("/bkcardyxyear");
					if (bkcardyxyear != null) {
						card.setBkcardyxyear(bkcardyxyear.get(0).mValue);
					}
					List<ProtocolData> bkcardcvv = data.find("/bkcardcvv");
					if (bkcardcvv != null) {
						card.setBkcardcvv(bkcardcvv.get(0).mValue);
					}
					List<ProtocolData> bkcardidcard = data.find("/bkcardidcard");
					if (bkcardidcard != null) {
						card.setBkcardidcard(bkcardidcard.get(0).mValue);
					}
					List<ProtocolData> bkcardtype = data.find("/bkcardtype");
					if (bkcardtype != null) {
						card.setBkcardcardtype(bkcardtype.get(0).mValue);
					}
				}
			}
			return card;
		}
}
