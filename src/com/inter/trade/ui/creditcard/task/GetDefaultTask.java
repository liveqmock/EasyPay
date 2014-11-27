/*
 * @Title:  BillTask.java
 * @Copyright:  XXX Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * @Description:  TODO<请描述此文件是做什么的>
 * @author:  MeiYi
 * @data:  2014年6月25日 下午2:20:23
 * @version:  V1.0
 */
package com.inter.trade.ui.creditcard.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
import com.inter.trade.ui.creditcard.data.SmsCode;
import com.inter.trade.ui.creditcard.parser.GetDefaultParser;
import com.inter.trade.ui.creditcard.parser.SmsCodeParser;
import com.inter.trade.ui.fragment.gamerecharge.data.AreaData;
import com.inter.trade.ui.fragment.gamerecharge.data.BillData;
import com.inter.trade.ui.fragment.gamerecharge.parser.GetBillParser;
import com.inter.trade.ui.fragment.waterelectricgas.data.ResponseStateListener;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PromptUtil;

/**
 * 获取默认的信用卡号
 * @author  ChenGuangChi
 * @data:  2014年6月25日 下午2:20:23
 * @version:  V1.0
 */
public class GetDefaultTask extends AsyncTask<String, Void, Void> {
	
	private Context context;
	
	private ResponseStateListener listener;
	
	public GetDefaultTask(Context context, ResponseStateListener listener) {
		super();
		this.context = context;
		this.listener = listener;
	}

	ProtocolRsp mRsp;
	@Override
	protected Void doInBackground(String... params) {
		
			try {
				CommonData mData = new CommonData();
				mData.putValue("bkcardid", params[0]);
				mData.putValue("bkcardisdefault", params[1]);

				List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas(
						"ApiPaychannelInfo", "readKuaipaybkcardInfo", mData);
				GetDefaultParser authorRegParser = new GetDefaultParser();
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
					DefaultBankCardData bankcard= parserResponse(mDatas);
					if (!ErrorUtil.create().dealError(LoginUtil.mLoginStatus,
							(Activity)context)) {
						return;
					}
					if(listener!=null){
						listener.onSuccess(bankcard, DefaultBankCardData.class);
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
			DefaultBankCardData bankCard=new DefaultBankCardData();
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
					
					List<ProtocolData> paychalname = data.find("/paychalname");
					if (paychalname != null) {
						bankCard.setPaychalname(paychalname.get(0).getmValue());
					}
					
					List<ProtocolData> aupic = data.find("/msgchild");
					if(aupic!=null)
					for(ProtocolData child:aupic){
						if (child.mChildren != null && child.mChildren.size() > 0) {
							Set<String> keys = child.mChildren.keySet();
							for(String key:keys){
								List<ProtocolData> rs = child.mChildren.get(key);
								for(ProtocolData item: rs){
									if(item.mKey.equals("bkcardid")){
										bankCard.setBkcardid(item.mValue);
									}else if(item.mKey.equals("bkcardno")){
										bankCard.setBkcardno(item.mValue);
									}else if(item.mKey.equals("bkcardbankid")){
										bankCard.setBkcardbankid(item.mValue);
									}else if(item.mKey.equals("bkcardbank")){
										bankCard.setBkcardbank(item.mValue);
									}else if(item.mKey.equals("bkcardbanklogo")){
										bankCard.setBkcardbanklogo(item.mValue);
									}else if(item.mKey.equals("bkcardbankman")){
										bankCard.setBkcardbankman(item.mValue);
									}else if(item.mKey.equals("bkcardbankphone")){
										bankCard.setBkcardbankphone(item.mValue);
									}else if(item.mKey.equals("bkcardyxmonth")){
										bankCard.setBkcardyxmonth(item.mValue);
									}else if(item.mKey.equals("bkcardyxyear")){
										bankCard.setBkcardyxyear(item.mValue);
									}else if(item.mKey.equals("bkcardcvv")){
										bankCard.setBkcardcvv(item.mValue);
									}else if(item.mKey.equals("bkcardidcard")){
										bankCard.setBkcardidcard(item.mValue);
									}else if(item.mKey.equals("bkcardisdefault")){
										bankCard.setBkcardisdefault(item.mValue);
									}else if(item.mKey.equals("bkcardcardtype")){
										bankCard.setBkcardcardtype(item.mValue);
									}else if(item.mKey.equals("bkcardbankcode")){
										bankCard.setBkcardbankcode(item.mValue);
									}else if(item.mKey.equals("ctripbankctt")){
										bankCard.setCtripbankctt(item.mValue);
									}
								}
							}
						}
					}
				}
			}
			return bankCard;
		}
}
