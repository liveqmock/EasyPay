/*
 * @Title:  GameInfoTask.java
 * @Copyright:  XXX Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * @Description:  TODO<请描述此文件是做什么的>
 * @author:  MeiYi
 * @data:  2014年6月25日 上午11:53:40
 * @version:  V1.0
 */
package com.inter.trade.ui.fragment.salarypay.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.inter.protocol.ProtocolData;
import com.inter.protocol.ProtocolRsp;
import com.inter.protocol.util.ProtocolUtil;
import com.inter.trade.R;
import com.inter.trade.data.CommonData;
import com.inter.trade.data.ResponseData;
import com.inter.trade.error.ErrorUtil;
import com.inter.trade.net.HttpUtil;
import com.inter.trade.ui.fragment.gamerecharge.data.AreaData;
import com.inter.trade.ui.fragment.gamerecharge.data.GameInfoData;
import com.inter.trade.ui.fragment.salarypay.bean.ChannelData;
import com.inter.trade.ui.fragment.salarypay.parser.GetPayWaysParser;
import com.inter.trade.ui.fragment.waterelectricgas.data.ResponseStateListener;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PromptUtil;

/**
 * 获取游戏详细信息
 * @author  ChenGuangChi
 * @data:  2014年6月25日 上午11:53:40
 * @version:  V1.0
 */
public class GetPayWaysTask extends AsyncTask<String, Void, Void> {
	
	private Context context;
	private ResponseStateListener listener;
	
	public GetPayWaysTask(Context context, ResponseStateListener listener) {
		super();
		this.context = context;
		this.listener = listener;
	}

	ProtocolRsp mRsp;
	@Override
	protected Void doInBackground(String... params) {
		try {
			CommonData mData = new CommonData();
			mData.putValue("paytype", params[0]);
			
			List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas("ApiPaychannelInfo", 
					"AppmenupaychannelsList", mData);
			GetPayWaysParser myParser = new GetPayWaysParser();
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
				ArrayList<ChannelData> data = parserResponse(mDatas);
				
				if(!ErrorUtil.create().errorDeal(LoginUtil.mLoginStatus,(Activity)context)){
					return;
				}
				
				if(listener!=null){
					listener.onSuccess(data, ChannelData.class);
				}
				
			} catch (Exception e) {
				PromptUtil.showToast(context,context.getString(R.string.req_error));
				e.printStackTrace();
			}
		}
	}
	
	private ArrayList<ChannelData> parserResponse(List<ProtocolData> mDatas) {
		ArrayList<ChannelData> cList=new ArrayList<ChannelData>();
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
					ChannelData channle=new  ChannelData();
					if (child.mChildren != null && child.mChildren.size() > 0) {
						Set<String> keys = child.mChildren.keySet();
						for(String key:keys){
							List<ProtocolData> rs = child.mChildren.get(key);
							for(ProtocolData item: rs){
								if(item.mKey.equals("paychalid")){
									channle.paychalid=item.mValue;
								}else if(item.mKey.equals("paychalno")){
									channle.paychalno=item.mValue;
								}else if(item.mKey.equals("paychalname")){
									channle.paychalname=item.mValue;
								}else if(item.mKey.equals("paychalshowname")){
									channle.paychalshowname=item.mValue;
								}else if(item.mKey.equals("paychalmemo")){
									channle.paychalmemo=item.mValue;
								}else if(item.mKey.equals("paychalmaxmoney")){
									channle.paychalmaxmoney=item.mValue;
								}else if(item.mKey.equals("paychalparent")){
									channle.paychalparent=item.mValue;
								}
							}
							
						}
					}
					cList.add(channle);
				}
			}
		}
		
		return cList;
	}
}

