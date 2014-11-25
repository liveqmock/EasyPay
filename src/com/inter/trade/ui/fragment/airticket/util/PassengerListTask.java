/*
 * @Title:  GameListTask.java
 * @Copyright:  XXX Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * @Description:  TODO<请描述此文件是做什么的>
 * @author:  MeiYi
 * @data:  2014年6月26日 下午2:10:17
 * @version:  V1.0
 */
package com.inter.trade.ui.fragment.airticket.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import com.inter.protocol.ProtocolData;
import com.inter.protocol.ProtocolRsp;
import com.inter.protocol.body.NetParser;
import com.inter.protocol.util.ProtocolUtil;
import com.inter.trade.R;
import com.inter.trade.data.CommonData;
import com.inter.trade.data.ResponseData;
import com.inter.trade.data.SunType;
import com.inter.trade.error.ErrorUtil;
import com.inter.trade.net.HttpUtil;
import com.inter.trade.ui.fragment.waterelectricgas.data.ResponseStateListener;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PromptUtil;

/**
 * 飞机票 获取联系人或者乘机人
 * @author zhichao.huang
 *
 */
public class PassengerListTask extends AsyncTask<String, String, String> {

	private Context context;
	private ResponseStateListener listener;
	
	/**
	 * 数据解析
	 */
	private NetParser parser;
	
	/**
	 * 数据模型
	 */
	private SunType sunTypeData;
	
	/**
	 * 是后台运行
	 */
	private boolean isBackground = false;

	public PassengerListTask(Context context, ResponseStateListener listener) {
		super();
		this.context = context;
		this.listener = listener;
	}
	
	public PassengerListTask(Context context, NetParser netParser, SunType data, ResponseStateListener listener) {
		this(context, listener);
		parser = netParser;
		sunTypeData = data;
	}
	
	/**
	 * 
	 * @param context
	 * @param netParser
	 * @param data
	 * @param isbackground 是否后台运行
	 * @param listener
	 */
	public PassengerListTask(Context context, NetParser netParser, SunType data, boolean isbackground, ResponseStateListener listener) {
		this(context, netParser, data, listener);
		isBackground = isbackground;
	}

	ProtocolRsp mRsp;

	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		try {
			CommonData mData = new CommonData();
			mData.putValue("type", params[0]);
			List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas(
					"ApiAirticket", "getPassenger", mData);
			PassengerParser myParser=new PassengerParser();
			mRsp = HttpUtil.doRequest(myParser, mDatas);
			
		} catch (Exception e) {
			e.printStackTrace();
			mRsp = null;
		}
		return null;
	}
	
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		PromptUtil.dissmiss();
		if(mRsp==null){
			PromptUtil.showToast(context, context.getString(R.string.net_error));
		}else{
			try {
				List<ProtocolData> mDatas = mRsp.mActions;
				ArrayList<PassengerData> parserResponse = parserResponse(mDatas);
				if(listener!=null){
					listener.onSuccess(parserResponse, PassengerData.class);
				}
				if(!ErrorUtil.create().errorDeal(LoginUtil.mLoginStatus,(Activity)context)){
					return;
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				PromptUtil.showToast(context,context.getString(R.string.req_error));
			}
		}
	}


	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		if(isBackground) return;
		PromptUtil.showDialog(context, context.getResources().getString(R.string.loading));
	}
	private ArrayList<PassengerData> parserResponse(List<ProtocolData> mDatas) {
		ArrayList<PassengerData> netData=new ArrayList<PassengerData>();
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
				
				List<ProtocolData> aupic = data.find("/msgchild");
				netData=new ArrayList<PassengerData>();
				if(aupic!=null)
				for(ProtocolData child:aupic){
					PassengerData adata=new PassengerData();
					if (child.mChildren != null && child.mChildren.size() > 0) {
						Set<String> keys = child.mChildren.keySet();
						for(String key:keys){
							List<ProtocolData> rs = child.mChildren.get(key);
							for(ProtocolData item: rs){
								if(item.mKey.equals("name")){
									adata.setName(item.mValue);
								}else if(item.mKey.equals("cardType")){
									adata.setIdtype(item.mValue);
								}else if(item.mKey.equals("cardId")){
									adata.setPassportNo(item.mValue);
								}else if(item.mKey.equals("phoneNumber")){
									adata.setPhoneNumber(item.mValue);
								}else if(item.mKey.equals("id")){
									adata.setId(item.mValue);
								}else if(item.mKey.equals("passengerType")){
									adata.setPassengerType(item.mValue);
								}
							}
							
						}
					}
					netData.add(adata);
				}
			}
		}
		return netData;
	}

}
