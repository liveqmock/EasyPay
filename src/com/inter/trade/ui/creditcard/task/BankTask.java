package com.inter.trade.ui.creditcard.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import com.inter.protocol.ProtocolData;
import com.inter.protocol.ProtocolRsp;
import com.inter.protocol.body.ReadBankListParser;
import com.inter.protocol.util.ProtocolUtil;
import com.inter.trade.R;
import com.inter.trade.data.BankData;
import com.inter.trade.data.CommonData;
import com.inter.trade.data.ResponseData;
import com.inter.trade.error.ErrorUtil;
import com.inter.trade.net.HttpUtil;
import com.inter.trade.ui.PayApp;
import com.inter.trade.ui.fragment.waterelectricgas.data.ResponseStateListener;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PromptUtil;

public class BankTask extends AsyncTask<String, Integer, Boolean> {
		private ProtocolRsp mRsp;
		private Context context;
		private ResponseStateListener listener;
		/***是否易宝的支持列表*/
		private boolean isYibao;
		
		public BankTask(Context context, ResponseStateListener listener) {
			super();
			this.context = context;
			this.listener = listener;
			isYibao=false;
		}
		
		public BankTask(Context context, ResponseStateListener listener,boolean isYibao) {
			super();
			this.context = context;
			this.listener = listener;
			this.isYibao=isYibao;
		}

		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			// UPLOAD_URL =
			// LoginUtil.mLoginStatus.mUserInfo.mPicData.get(0).uploadurl;
			try {

				List<ProtocolData> mDatas = getRequestDatas("");
				ReadBankListParser authorRegParser = new ReadBankListParser();

				mRsp = HttpUtil.doRequest(authorRegParser, mDatas);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			PromptUtil.dissmiss();
			
			try {
				if (mRsp != null) {
					List<ProtocolData> datas = mRsp.mActions;
					ArrayList<BankData> banklist = parserResoponse(datas);
					
					if (!ErrorUtil.create().errorDeal(LoginUtil.mLoginStatus,
							(Activity)context)) {
						return;
					}
					if(listener!=null){
						listener.onSuccess(banklist, BankData.class);
					}

				} else {
					PromptUtil.showToast(context, "服务器繁忙,请稍后再试");
				}
			} catch (Exception e) {
				// TODO: handle exception
				PromptUtil.showToast(PayApp.pay, context.getString(R.string.net_error));
				e.printStackTrace();
			}
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			PromptUtil.showDialog(context, context.getResources()
					.getString(R.string.loading));
		}


	/**
	 * 解析响应体
	 * 
	 * @param params
	 */
	private ArrayList<BankData> parserResoponse(List<ProtocolData> params) {
		ResponseData response = new ResponseData();
		LoginUtil.mLoginStatus.mResponseData = response;
		ArrayList<BankData> mBankDatas =new ArrayList<BankData>();
		for (ProtocolData data : params) {
			if (data.mKey.equals(ProtocolUtil.msgheader)) {
				ProtocolUtil.parserResponse(response, data);

			} else if (data.mKey.equals(ProtocolUtil.msgbody)) {
				List<ProtocolData> result1 = data.find("/result");
				if (result1 != null) {
					LoginUtil.mLoginStatus.result = result1.get(0).mValue;
				}

				List<ProtocolData> message = data.find("/message");
				if (message != null) {
					LoginUtil.mLoginStatus.message = message.get(0).mValue;
				}

				List<ProtocolData> msgallcount = data.find("/msgallcount");//总共记录
				if (msgallcount != null) {
					
					
				}
				List<ProtocolData> msgdiscount = data.find("/msgdiscount");//累计记录
				if (msgdiscount != null) {
					
					
				}
				List<ProtocolData> aupic = data.find("/msgchild");
				if (aupic != null) {
					for (ProtocolData child : aupic) {
						BankData picData = new BankData();
						if (child.mChildren != null
								&& child.mChildren.size() > 0) {
							Set<String> keys = child.mChildren.keySet();
							for (String key : keys) {
								List<ProtocolData> rs = child.mChildren
										.get(key);
								for (ProtocolData item : rs) {
									if (item.mKey.equals("bankid")) {
										picData.bankid = item.mValue;

									} else if (item.mKey.equals("bankno")) {
										picData.bankno = item.mValue;

									} else if (item.mKey.equals("bankname")) {

										picData.bankname = item.mValue;
									}
								}
							}
						}

						mBankDatas.add(picData);
					}

				}
			}

		}
		return mBankDatas;
	}
		
		/**
		 * 请求修改身份信息
		 * 
		 * @return
		 */
		private List<ProtocolData> getRequestDatas(String where) {
			CommonData data = new CommonData();
			data.putValue("authorid", LoginUtil.mLoginStatus.authorid);
			data.putValue("activemobilesms", "0");
			data.putValue("msgstart", "0");
			data.putValue("msgdisplay", "100");
			data.putValue("querywhere", where);
			if(isYibao){
				data.putValue("banktype", "yibao");
			}
			List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas("ApiAppInfo",
					"readBankList", data);

			return mDatas;
		}
}

