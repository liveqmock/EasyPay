/*
 * @Title:  GameInfoTask.java
 * @Copyright:  XXX Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * @Description:  TODO<请描述此文件是做什么的>
 * @author:  MeiYi
 * @data:  2014年6月25日 上午11:53:40
 * @version:  V1.0
 */
package com.inter.trade.ui.fragment.gamerecharge.task;

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
import com.inter.trade.ui.fragment.gamerecharge.parser.GameInfoParser;
import com.inter.trade.ui.fragment.waterelectricgas.data.ResponseStateListener;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PromptUtil;

/**
 * 获取游戏详细信息
 * @author  ChenGuangChi
 * @data:  2014年6月25日 上午11:53:40
 * @version:  V1.0
 */
public class GameInfoTask extends AsyncTask<String, Void, Void> {
	
	private Context context;
	private ResponseStateListener listener;
	
	public GameInfoTask(Context context, ResponseStateListener listener) {
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
			
			List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas("ApiGameRecharge", 
					"getGameDetail", mData);
			GameInfoParser myParser = new GameInfoParser();
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
				GameInfoData data = parserResponse(mDatas);
				if(listener!=null){
					listener.onSuccess(data, GameInfoData.class);
				}
				
				if(!ErrorUtil.create().errorDeal(LoginUtil.mLoginStatus,(Activity)context)){
					return;
				}
				
			} catch (Exception e) {
				PromptUtil.showToast(context,context.getString(R.string.req_error));
			}
		}
	}
	
	private GameInfoData parserResponse(List<ProtocolData> mDatas) {
		GameInfoData game=new GameInfoData();
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
				List<ProtocolData> gameName = data.find("/gameName");
				if (result != null) {
					game.setGameName(gameName.get(0).getmValue());
				}
				
				List<ProtocolData> aupic = data.find("/msgchild");
				ArrayList<AreaData> areaList=new ArrayList<AreaData>();
				if(aupic!=null)
				for(ProtocolData child:aupic){
					if (child.mChildren != null && child.mChildren.size() > 0) {
						Set<String> keys = child.mChildren.keySet();
						AreaData area=new AreaData();
						for(String key:keys){
							List<ProtocolData> rs = child.mChildren.get(key);
							for(ProtocolData item: rs){
								if(item.mKey.equals("area")){
									area.setName(item.mValue);
								}else if(item.mKey.equals("server")){
									String servers =item.mValue;
									area.setServerList(parserServer(servers));
								}
							}
						}
						areaList.add(area);
					}
				}
				game.setAreaList(areaList);
			}
		}
		
		return game;
	}
	
	/***
	 *  解析服务器字符串 
	 * @param servers
	 * @return
	 * @throw
	 * @return ArrayList<String>
	 */
	private ArrayList<String> parserServer(String servers){
		
		if(!TextUtils.isEmpty(servers)){
			ArrayList<String> list=new ArrayList<String>();
			String[] serverArr = servers.split("#");
			for(int i=0;i<serverArr.length;i++){
				list.add(serverArr[i]);
			}
			return list;
		}
		return null;
	}
}

