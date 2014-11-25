/*
 * @Title:  GameListTask.java
 * @Copyright:  XXX Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * @Description:  TODO<请描述此文件是做什么的>
 * @author:  MeiYi
 * @data:  2014年6月26日 下午2:10:17
 * @version:  V1.0
 */
package com.inter.trade.ui.fragment.hotel.task;

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
import com.inter.trade.data.ResponseData;
import com.inter.trade.data.SunType;
import com.inter.trade.error.ErrorUtil;
import com.inter.trade.net.HttpUtil;
import com.inter.trade.ui.fragment.hotel.data.HotelKeywordData;
import com.inter.trade.ui.fragment.waterelectricgas.data.ResponseStateListener;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PromptUtil;

/**
 * 酒店预订  获取关键字
 * @author haifengli
 *
 */
public class HotelKeywordTask extends AsyncTask<String, String, String> {

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

	public HotelKeywordTask(Context context, ResponseStateListener listener) {
		super();
		this.context = context;
		this.listener = listener;
	}
	
	public HotelKeywordTask(Context context, NetParser netParser, SunType data, ResponseStateListener listener) {
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
	public HotelKeywordTask(Context context, NetParser netParser, SunType data, boolean isbackground, ResponseStateListener listener) {
		this(context, netParser, data, listener);
		isBackground = isbackground;
	}

	ProtocolRsp mRsp;

	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		try {
//			CommonData mData = new CommonData();
//			mData.putValue("firstLetter", "");
//			mData.putValue("cityName", "");
//			List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas(
//					"ApiAirticket", "getCity", mData);
//			ApiAirticketGetCityParser myParser = new ApiAirticketGetCityParser();
//			mRsp = HttpUtil.doRequest(myParser, mDatas);
			
			if(params[0] != null && !params[0].equals("") && params[1] != null && !params[1].equals("")){
				mRsp = HttpUtil.doRequest(parser, ProtocolUtil.getRequestDatas(params[0], params[1], sunTypeData));
			}
			
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
				ArrayList<HotelKeywordData> parserResponse = parserResponse(mDatas);
				listener.onSuccess(parserResponse, HotelKeywordData.class);
				
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
		if(isBackground) return;
		PromptUtil.showDialog(context, context.getResources().getString(R.string.loading));
	}
	private ArrayList<HotelKeywordData> parserResponse(List<ProtocolData> mDatas) {
		ArrayList<HotelKeywordData> netData=new ArrayList<HotelKeywordData>();
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
				netData=new ArrayList<HotelKeywordData>();
				if(aupic!=null)
				for(ProtocolData child:aupic){
					HotelKeywordData adata=new HotelKeywordData();
					if (child.mChildren != null && child.mChildren.size() > 0) {
						Set<String> keys = child.mChildren.keySet();
						for(String key:keys){
							List<ProtocolData> rs = child.mChildren.get(key);
							for(ProtocolData item: rs){
								if(item.mKey.equals("districtId")){
									adata.setCityId(item.mValue);
								}else if(item.mKey.equals("districtName")){
									adata.setCityNameCh(item.mValue);
								}else if(item.mKey.equals("zoneId")){
									adata.setCityId(item.mValue);
								}else if(item.mKey.equals("zoneName")){
									adata.setCityNameCh(item.mValue);
								}else if(item.mKey.equals("brandId")){
									adata.setCityId(item.mValue);
								}else if(item.mKey.equals("brandName")){
									adata.setCityNameCh(item.mValue);
								}else if(item.mKey.equals("themeId")){
									adata.setCityId(item.mValue);
								}else if(item.mKey.equals("themeName")){
									adata.setCityNameCh(item.mValue);
								}else if(item.mKey.equals("locationId")){
									adata.setCityId(item.mValue);
								}else if(item.mKey.equals("locationName")){
									adata.setCityNameCh(item.mValue);
								}
//								if(item.mKey.equals("districtId")){
//									adata.setDistrictId(item.mValue);
//								}else if(item.mKey.equals("districtName")){
//									adata.setDistrictName(item.mValue);
//								}else if(item.mKey.equals("zoneId")){
//									adata.setZoneId(item.mValue);
//								}else if(item.mKey.equals("zoneName")){
//									adata.setZoneName(item.mValue);
//								}else if(item.mKey.equals("brandId")){
//									adata.setBrandId(item.mValue);
//								}else if(item.mKey.equals("brandName")){
//									adata.setBrandName(item.mValue);
//								}else if(item.mKey.equals("themeId")){
//									adata.setThemeId(item.mValue);
//								}else if(item.mKey.equals("themeName")){
//									adata.setThemeName(item.mValue);
//								}
							}
							
						}
					}
					netData.add(adata);
//					mList.add(picData);
				}
			}
		}
		return netData;
	}

}
