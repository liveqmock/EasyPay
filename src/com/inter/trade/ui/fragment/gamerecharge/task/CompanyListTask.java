/*
 * @Title:  CompanyListTask.java
 * @Copyright:  XXX Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * @Description:  TODO<请描述此文件是做什么的>
 * @author:  MeiYi
 * @data:  2014年6月26日 下午2:18:39
 * @version:  V1.0
 */
package com.inter.trade.ui.fragment.gamerecharge.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.inter.protocol.ProtocolData;
import com.inter.protocol.ProtocolRsp;
import com.inter.protocol.util.ProtocolUtil;
import com.inter.trade.R;
import com.inter.trade.data.CommonData;
import com.inter.trade.data.ResponseData;
import com.inter.trade.error.ErrorUtil;
import com.inter.trade.net.HttpUtil;
import com.inter.trade.ui.fragment.gamerecharge.data.CompanyListData;
import com.inter.trade.ui.fragment.gamerecharge.parser.CompanyListParser;
import com.inter.trade.ui.fragment.waterelectricgas.data.ResponseStateListener;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PromptUtil;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

/**
 * TODO<请描述这个类是干什么的>
 * @author  ChenGuangChi
 * @data:  2014年6月26日 下午2:18:39
 * @version:  V1.0
 */
public class CompanyListTask extends AsyncTask<Void, Void, Void> {
	private Context context;
	private ResponseStateListener listener;
	
	
	
	public CompanyListTask(Context context, ResponseStateListener listener) {
		super();
		this.context = context;
		this.listener = listener;
	}

	ProtocolRsp mRsp;
	@Override
	protected Void doInBackground(Void... params) {
		try {
			CommonData mData = new CommonData();
			List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas("ApiGameRecharge", 
					"getplatformList", mData);
			CompanyListParser myParser = new CompanyListParser();
			mRsp = HttpUtil.doRequest(myParser, mDatas);
			} catch (Exception e) {
				e.printStackTrace();
				mRsp =null;
			}
			return null;
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
				ArrayList<CompanyListData> netData = parserResponse(mDatas);
				if(listener!=null){
					listener.onSuccess(netData, CompanyListData.class);
				}
				
				if(!ErrorUtil.create().errorDeal(LoginUtil.mLoginStatus,(Activity)context)){
					return;
				}
				
			} catch (Exception e) {
				PromptUtil.showToast(context,context.getString(R.string.req_error));
			}
		}
	}


	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		PromptUtil.showDialog(context, context.getResources().getString(R.string.loading));
	}
	
	private ArrayList<CompanyListData> parserResponse(List<ProtocolData> mDatas) {
		ArrayList<CompanyListData> netData=new ArrayList<CompanyListData>();
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
				if (result != null) {
					LoginUtil.mLoginStatus.message = message.get(0).getmValue();
				}
				
				List<ProtocolData> aupic = data.find("/msgchild");
				if(aupic!=null)
				for(ProtocolData child:aupic){
					CompanyListData company=new CompanyListData();
					if (child.mChildren != null && child.mChildren.size() > 0) {
						Set<String> keys = child.mChildren.keySet();
						for(String key:keys){
							
							List<ProtocolData> rs = child.mChildren.get(key);
							for(ProtocolData item: rs){
								if(item.mKey.equals("platformId")){
									company.setCompanyId(item.mValue);
								}else if(item.mKey.equals("platformName")){
									company.setCompanyName(item.mValue);
								}
							}
							
						}
					}
					netData.add(company);
				}
			}
		}
		return netData;
	}
	
}
