/*
 * @Title:  BillTask.java
 * @Copyright:  XXX Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * @Description:  TODO<请描述此文件是做什么的>
 * @author:  MeiYi
 * @data:  2014年6月25日 下午2:20:23
 * @version:  V1.0
 */
package com.inter.trade.ui.fragment.gamerecharge.task;

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
import com.inter.trade.ui.fragment.gamerecharge.data.BillData;
import com.inter.trade.ui.fragment.gamerecharge.parser.GetBillParser;
import com.inter.trade.ui.fragment.waterelectricgas.data.ResponseStateListener;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PromptUtil;

/**
 * TODO<请描述这个类是干什么的>
 * @author  ChenGuangChi
 * @data:  2014年6月25日 下午2:20:23
 * @version:  V1.0
 */
public class BillTask extends AsyncTask<String, Void, Void> {
	
	private Context context;
	
	private ResponseStateListener listener;
	
	public BillTask(Context context, ResponseStateListener listener) {
		super();
		this.context = context;
		this.listener = listener;
	}

	ProtocolRsp mRsp;
	@Override
	protected Void doInBackground(String... params) {
		
			try {
				CommonData mData = new CommonData();
				mData.putValue("gameId", params[0]);
				mData.putValue("gameName", params[1]);
				mData.putValue("area", params[2]);
				mData.putValue("server", params[3]);
				mData.putValue("quantity", params[4]);
				mData.putValue("price", params[5]);
				mData.putValue("userCount", params[6]);
				mData.putValue("paycardid", params[7]);
				mData.putValue("rechabkcardno", params[8]);
				mData.putValue("cost", params[9]);

				List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas(
						"ApiGameRecharge", "createOrder", mData);
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
					if(listener!=null){
						listener.onSuccess(bill, BillData.class);
					}
					if (!ErrorUtil.create().errorDeal(LoginUtil.mLoginStatus,
							(Activity)context)) {
						return;
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
					
					List<ProtocolData> totalPrice = data.find("/totalPrice");
					if(totalPrice!=null){
						bill.setTotalPrice(totalPrice.get(0).getmValue());
					}
					
//					List<ProtocolData> orderid = data.find("/orderid");
//					if(totalPrice!=null){
//						bill.setOrderid(orderid.get(0).getmValue());
//					}
				}
			}
			return bill;
		}
}
