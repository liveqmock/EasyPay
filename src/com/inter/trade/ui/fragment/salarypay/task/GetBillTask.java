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
import com.inter.trade.ui.fragment.gamerecharge.data.BillData;
import com.inter.trade.ui.fragment.salarypay.bean.DepositPayInfo;
import com.inter.trade.ui.fragment.salarypay.parser.GetBillParser;
import com.inter.trade.ui.fragment.waterelectricgas.data.ResponseStateListener;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PromptUtil;

/**
 * 获取银联订单
 * @author  ChenGuangChi
 * @data:  2014年6月25日 下午2:20:23
 * @version:  V1.0
 */
public class GetBillTask extends AsyncTask<DepositPayInfo, Void, Void> {
	
	private Context context;
	
	private ResponseStateListener listener;
	
	public GetBillTask(Context context, ResponseStateListener listener) {
		super();
		this.context = context;
		this.listener = listener;
	}

	ProtocolRsp mRsp;
	@Override
	protected Void doInBackground(DepositPayInfo... params) {
		DepositPayInfo info=params[0];
			try {
				CommonData mData = new CommonData();
				mData.putValue("wagepaymoney", info.wagepaymoney);
				mData.putValue("wagemonth", info.wagemonth);
				mData.putValue("wagemoney", info.wagemoney);
				mData.putValue("fucardno", info.fucardno);
				mData.putValue("fucardbank", info.fucardbank);
				mData.putValue("wagelistid", info.wagelistid);
				mData.putValue("paycardid", info.paycardid);

				List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas(
						"ApiWageInfo", "paywageRq", mData);
				GetBillParser authorRegParser = new GetBillParser();
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
					BillData bill = parserResponse(mDatas);
					if (!ErrorUtil.create().errorDeal(LoginUtil.mLoginStatus,
							(Activity)context)) {
						return;
					}
					
					if(listener!=null){
						listener.onSuccess(bill, BillData.class);
					}

				} catch (Exception e) {
					PromptUtil.showToast(context,
							context.getString(R.string.req_error));
					e.printStackTrace();
				}
			}
		}

		private BillData parserResponse(List<ProtocolData> mDatas) {
			BillData bill=new BillData();
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
					if (result != null) {
						LoginUtil.mLoginStatus.message = message.get(0)
								.getmValue();
					}
					
					List<ProtocolData> bkntno = data.find("/bkntno");
					if(bkntno!=null){
						bill.setBkntno(bkntno.get(0).getmValue());
					}
				}
			}
			return bill;
		}
}
