/*
 * @Title:  GameInfoTask.java
 * @Copyright:  XXX Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * @Description:  TODO<请描述此文件是做什么的>
 * @author:  MeiYi
 * @data:  2014年6月25日 上午11:53:40
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
import com.inter.trade.ui.fragment.salarypay.bean.FinancialStuff;
import com.inter.trade.ui.fragment.salarypay.parser.GetBindFinancialParser;
import com.inter.trade.ui.fragment.waterelectricgas.data.ResponseStateListener;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PromptUtil;

/**
 * 根据财务人员的账号来获取绑定的财务人员的信息
 * @author  ChenGuangChi
 * @data:  2014年6月25日 上午11:53:40
 * @version:  V1.0
 */
public class GetBindFinancialTask extends AsyncTask<String, Void, Void> {
	
	private Context context;
	private ResponseStateListener listener;
	
	public GetBindFinancialTask(Context context, ResponseStateListener listener) {
		super();
		this.context = context;
		this.listener = listener;
	}

	ProtocolRsp mRsp;
	@Override
	protected Void doInBackground(String... params) {
		try {
			CommonData mData = new CommonData();
			mData.putValue("authorusername", params[0]);
			
			List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas("ApiWageInfo", 
					"readAuthorInfo", mData);
			GetBindFinancialParser myParser = new GetBindFinancialParser();
			mRsp = HttpUtil.doRequest(myParser, mDatas);
			} catch (Exception e) {
				e.printStackTrace();
				mRsp =null;
			}
			return null;
	}
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		PromptUtil.showDialog(context, context.getResources().getString(R.string.loading));
	}
	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		PromptUtil.dissmiss();
		if(mRsp==null){
			PromptUtil.showToast(context, context.getString(R.string.net_error));
		}else{
			try {
				List<ProtocolData> mDatas = mRsp.mActions;
				FinancialStuff data = parserResponse(mDatas);
				
				if(!ErrorUtil.create().errorDeal(LoginUtil.mLoginStatus,(Activity)context)){
					return;
				}
				
				if(listener!=null){
					listener.onSuccess(data, FinancialStuff.class);
				}
				
			} catch (Exception e) {
				PromptUtil.showToast(context,context.getString(R.string.req_error));
			}
		}
	}
	
	private FinancialStuff parserResponse(List<ProtocolData> mDatas) {
		FinancialStuff stuff=new FinancialStuff();
		ResponseData response = new ResponseData();
		LoginUtil.mLoginStatus.mResponseData =response;
		for (ProtocolData data : mDatas) {
			if (data.mKey.equals(ProtocolUtil.msgheader)) {
				ProtocolUtil.parserResponse(response, data);

			} else if (data.mKey.equals(ProtocolUtil.msgbody)) {

				List<ProtocolData> result = data.find("/result");
				if (result != null) {
					LoginUtil.mLoginStatus.result = result.get(0).getmValue();
				}
				
				List<ProtocolData> message = data.find("/message");
				if (message != null) {
					LoginUtil.mLoginStatus.message = message.get(0).getmValue();
				}
				List<ProtocolData> cwtruename = data.find("/cwtruename");
				if (cwtruename != null) {
					stuff.cwtruename=cwtruename.get(0).mValue;
				}
				List<ProtocolData> cwtrueidcard = data.find("/cwtrueidcard");
				if (cwtrueidcard != null) {
					stuff.cwtrueidcard=cwtrueidcard.get(0).mValue;
				}
				List<ProtocolData> cwemail = data.find("/cwemail");
				if (cwemail != null) {
					stuff.cwemail=cwemail.get(0).mValue;
				}
				List<ProtocolData> cwmobile = data.find("/cwmobile");
				if (cwmobile != null) {
					stuff.cwmobile=cwmobile.get(0).mValue;
				}
				
			}
		}
		
		return stuff;
	}
}

