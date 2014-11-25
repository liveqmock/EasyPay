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
import com.inter.trade.ui.creditcard.parser.AddBankCardParser;
import com.inter.trade.ui.fragment.waterelectricgas.data.ResponseStateListener;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PromptUtil;

/**
 * 添加银行卡
 * @author  ChenGuangChi
 * @data:  2014年6月25日 下午2:20:23
 * @version:  V1.0
 */
public class AddBankCardTask2 extends AsyncTask<String, Void, Void> {
	
	private Context context;
	
	private ResponseStateListener listener;
	
	public AddBankCardTask2(Context context, ResponseStateListener listener) {
		super();
		this.context = context;
		this.listener = listener;
	}

	ProtocolRsp mRsp;
	@Override
	protected Void doInBackground(String... params) {
		
			try {
				CommonData mData = new CommonData();
				mData.putValue("bkcardbankid", params[0]);
				mData.putValue("bkcardbank",  params[1]);
				mData.putValue("bkcardno",  params[2]);
				mData.putValue("bkcardbankman",  params[3]);
				mData.putValue("bkcardbankphone", params[4]);
				mData.putValue("bkcardyxmonth",  params[5]);
				mData.putValue("bkcardyxyear",  params[6]);
				mData.putValue("bkcardcvv",  params[7]);
				mData.putValue("bkcardidcard", params[8]);
				mData.putValue("bkcardcardtype",  params[9]);
				mData.putValue("bkcardisdefault",  params[10]);
				
				mData.putValue("bkcardfudefault",  params[11]);
				mData.putValue("bkcardshoudefault",  params[12]);

				List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas(
						"ApiAuthorKuaibkcardInfo", "AddKuaibkcard", mData);
				AddBankCardParser authorRegParser = new AddBankCardParser();
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
					parserResponse(mDatas);
					if (!ErrorUtil.create().errorDeal(LoginUtil.mLoginStatus,
							(Activity)context)) {
						return;
					}
					if(listener!=null){
						listener.onSuccess("保存成功", DefaultBankCardData.class);
					}

				} catch (Exception e) {
					PromptUtil.showToast(context,
							context.getString(R.string.req_error));
					e.printStackTrace();
				}
			}
		}

		private void parserResponse(List<ProtocolData> mDatas) {
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
					}
				}
			}
		}
}
