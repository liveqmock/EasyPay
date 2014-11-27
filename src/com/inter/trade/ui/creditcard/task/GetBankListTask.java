package com.inter.trade.ui.creditcard.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;

import com.inter.protocol.ProtocolData;
import com.inter.protocol.ProtocolRsp;
import com.inter.protocol.util.ProtocolUtil;
import com.inter.trade.R;
import com.inter.trade.data.CommonData;
import com.inter.trade.data.ResponseData;
import com.inter.trade.error.ErrorUtil;
import com.inter.trade.log.Logger;
import com.inter.trade.net.HttpUtil;
import com.inter.trade.parser.MyBankCardListParser;
import com.inter.trade.ui.creditcard.data.DefaultBankCardData;
import com.inter.trade.ui.fragment.order.util.OrderData;
import com.inter.trade.ui.fragment.waterelectricgas.data.ResponseStateListener;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PromptUtil;

public class GetBankListTask extends AsyncTask<String, Integer, Boolean> {
		ProtocolRsp mRsp = null;
		FragmentActivity mActivity;
		private String mResultString;
		private Context context;
		private ResponseStateListener listener;
		
		public GetBankListTask(Context context, ResponseStateListener listener) {
			super();
			this.context = context;
			this.listener = listener;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			PromptUtil.showDialog(context, context.getResources().getString(R.string.loading));
		}

		@Override
		protected Boolean doInBackground(String... arg0) {
			try {
				CommonData data = new CommonData();
				
				List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas("ApiAuthorKuaibkcardInfo", "readKuaibkcardLists",
						data);
				MyBankCardListParser recordParser = new MyBankCardListParser();
				mRsp = HttpUtil.doRequest(recordParser, mDatas);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				Logger.e(e);
			}
			return null;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			PromptUtil.dissmiss();
			try {
			if (mRsp == null) {
				PromptUtil.showToast(mActivity, mActivity.getString(R.string.net_error));
			} else {
				
					List<ProtocolData> mDatas = mRsp.mActions;
					ArrayList<DefaultBankCardData> data = parserResponse(mDatas);
					
					/**
					 * Retcode !=200
					 */
					if(!ProtocolUtil.ERROR_USER_DEFINE.equals(LoginUtil.mLoginStatus.mResponseData.getRetcode())){
						if(!ErrorUtil.create().errorDeal(LoginUtil.mLoginStatus,(Activity)context)){
							return;
						}
					}
					if(listener!=null){
						/**
						 * Retcode ==200,返回失败，data=null
						 */
						if(ProtocolUtil.ERROR_USER_DEFINE.equals(LoginUtil.mLoginStatus.mResponseData.getRetcode())){
							data=null;
						}
						listener.onSuccess(data, DefaultBankCardData.class);
					}

			}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}

	private ArrayList<DefaultBankCardData> parserResponse(List<ProtocolData> mDatas) {
		ResponseData response = new ResponseData();
		LoginUtil.mLoginStatus.mResponseData = response;
		ArrayList<DefaultBankCardData> mList=new  ArrayList<DefaultBankCardData>();
		for (ProtocolData data : mDatas) {
			if (data.mKey.equals(ProtocolUtil.msgheader)) {
				ProtocolUtil.parserResponse(response, data);

			} else if (data.mKey.equals(ProtocolUtil.msgbody)) {

				List<ProtocolData> result = data.find("/result");
				if (result != null) {
					LoginUtil.mLoginStatus.result = result.get(0).mValue;
				}
				
				List<ProtocolData> message = data.find("/message");
				if (message != null) {
					LoginUtil.mLoginStatus.message = result.get(0).mValue;
				}
				
				List<ProtocolData> msgallcount = data.find("/msgallcount");
				List<ProtocolData> aupic = data.find("/msgchild");
				if(aupic==null){
					return null;
				}
				for (ProtocolData child : aupic) {
					DefaultBankCardData orderData = new DefaultBankCardData();
					if (child.mChildren != null && child.mChildren.size() > 0) {
						Set<String> keys = child.mChildren.keySet();
						for (String key : keys) {
							List<ProtocolData> rs = child.mChildren.get(key);
							for (ProtocolData item : rs) {
								if (item.mKey.equals("bkcardid")) {
									orderData.setBkcardid(item.mValue);

								} else if (item.mKey.equals("bkcardno")) {
									orderData.setBkcardno(item.mValue);

								} else if (item.mKey.equals("bkcardbankid")) {
									orderData.setBkcardbankid(item.mValue);

								} else if (item.mKey.equals("bkcardbank")) {
									orderData.setBkcardbank(item.mValue);

								} else if (item.mKey.equals("bkcardbanklogo")) {
									orderData.setBkcardbanklogo(item.mValue);
									
								} else if (item.mKey.equals("banklogo")) {
									orderData.setBanklogo(item.mValue);

								} else if (item.mKey.equals("bkcardbankman")) {
									orderData.setBkcardbankman(item.mValue);

								} else if (item.mKey.equals("bkcardbankphone")) {
									orderData.setBkcardbankphone(item.mValue);

								} else if (item.mKey.equals("bkcardyxmonth")) {
									orderData.setBkcardyxmonth(item.mValue);

								} else if (item.mKey.equals("bkcardyxyear")) {
									orderData.setBkcardyxyear(item.mValue);

								} else if (item.mKey.equals("bkcardcvv")) {
									orderData.setBkcardcvv(item.mValue);

								} else if (item.mKey.equals("bkcardidcard")) {
									orderData.setBkcardidcard(item.mValue);

								} else if (item.mKey.equals("bkcardisdefault")) {
									orderData.setBkcardisdefault(item.mValue);
									
								} else if (item.mKey.equals("bkcardfudefault")) {
									orderData.setBkcardfudefault(item.mValue);
									
								} else if (item.mKey.equals("bkcardshoudefault")) {
									orderData.setBkcardshoudefault(item.mValue);

								} else if (item.mKey.equals("bkcardcardtype")) {
									orderData.setBkcardcardtype(item.mValue);

								} else if (item.mKey.equals("bkcardbankcode")) {
									orderData.setBkcardbankcode(item.mValue);

								} else if (item.mKey.equals("ctripbankctt")) {
									orderData.setCtripbankctt(item.mValue);

								}
							}

						}
					}
					mList.add(orderData);
				}
				
			}
		}
		return mList;
	}
	
}