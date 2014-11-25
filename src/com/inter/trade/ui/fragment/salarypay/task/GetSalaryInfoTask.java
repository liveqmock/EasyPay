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
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import com.inter.protocol.ProtocolData;
import com.inter.protocol.ProtocolRsp;
import com.inter.protocol.util.ProtocolUtil;
import com.inter.trade.R;
import com.inter.trade.ResponseMoreStateListener;
import com.inter.trade.ResponseWithTimeoutStateListener;
import com.inter.trade.data.CommonData;
import com.inter.trade.data.ResponseData;
import com.inter.trade.error.ErrorUtil;
import com.inter.trade.net.HttpUtil;
import com.inter.trade.ui.fragment.salarypay.bean.ChannelData;
import com.inter.trade.ui.fragment.salarypay.bean.PayInfo;
import com.inter.trade.ui.fragment.salarypay.parser.GetSalaryInfoParser;
import com.inter.trade.ui.fragment.waterelectricgas.data.ResponseStateListener;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PromptUtil;

/**
 * 读取当月的工资情况，进行支付
 * @author  ChenGuangChi
 * @data:  2014年6月25日 上午11:53:40
 * @version:  V1.0
 */
public class GetSalaryInfoTask extends AsyncTask<String, Void, Void> {
	
	private Context context;
	private ResponseStateListener listener;
	private boolean isDelay;
	
	public GetSalaryInfoTask(Context context, ResponseStateListener listener,boolean isDelay) {
		super();
		this.context = context;
		this.listener = listener;
		this.isDelay=isDelay;
	}

	ProtocolRsp mRsp;
	@Override
	protected Void doInBackground(String... params) {
		try {
			if(isDelay){
				Thread.sleep(3000);
			}
			CommonData mData = new CommonData();
			mData.putValue("querytype", "month");
			mData.putValue("querywhere", params[0]);
			mData.putValue("wagelistid", params[1]);
			
			List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas("ApiWageInfo", 
					"paymonthwage", mData);
			GetSalaryInfoParser myParser = new GetSalaryInfoParser();
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
			//PromptUtil.showToast(context, context.getString(R.string.net_error));
			if(listener instanceof ResponseWithTimeoutStateListener){
				((ResponseWithTimeoutStateListener) listener).onFailure(null, null);
			}
		}else{
			try {
				List<ProtocolData> mDatas = mRsp.mActions;
				PayInfo data = parserResponse(mDatas);
				
				if(!ErrorUtil.create().dealError(LoginUtil.mLoginStatus,(Activity)context)){
						if(listener instanceof ResponseWithTimeoutStateListener){
							((ResponseWithTimeoutStateListener) listener).onFailure(null, null);
						}
					return;
				}
				
				if(listener!=null){
					listener.onSuccess(data, PayInfo.class);
				}
				
			} catch (Exception e) {
				//PromptUtil.showToast(context,context.getString(R.string.req_error));
				if(listener instanceof ResponseWithTimeoutStateListener){
					((ResponseWithTimeoutStateListener) listener).onTimeOut(null, null);
				}
				e.printStackTrace();
			}
		}
	}
	
	private PayInfo parserResponse(List<ProtocolData> mDatas) {
		PayInfo info=new PayInfo();
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
				List<ProtocolData> minfee = data.find("/minfee");
				if (minfee != null) {
					info.minfee=minfee.get(0).getmValue();
				}
				
				List<ProtocolData> maxfee = data.find("/maxfee");
				if (maxfee != null) {
					info.maxfee=maxfee.get(0).getmValue();
				}
				
				List<ProtocolData> feeperent = data.find("/feeperent");
				if (feeperent != null) {
					info.feeperent=feeperent.get(0).getmValue();
				}
				
				
				List<ProtocolData> aupic = data.find("/msgchild");
				if(aupic!=null)
				for(ProtocolData child:aupic){
					if (child.mChildren != null && child.mChildren.size() > 0) {
						Set<String> keys = child.mChildren.keySet();
						for(String key:keys){
							List<ProtocolData> rs = child.mChildren.get(key);
							for(ProtocolData item: rs){
								if(item.mKey.equals("wageallmoney")){
									info.wageallmoney=item.mValue;
								}else if(item.mKey.equals("wagemonth")){
									info.wagemonth=item.mValue;
								}else if(item.mKey.equals("wagepaymoney")){
									info.wagepaymoney=item.mValue;
								}else if(item.mKey.equals("needpaymoney")){
									info.needpaymoney=item.mValue;
								}else if(item.mKey.equals("payfee")){
									info.payfee=item.mValue;
								}else if(item.mKey.equals("wagelistid")){
									info.wagelistid=item.mValue;
								}
							}
							
						}
					}
				}
				
			}
		}
		
		return info;
	}
}

