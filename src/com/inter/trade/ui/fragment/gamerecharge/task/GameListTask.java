/*
 * @Title:  GameListTask.java
 * @Copyright:  XXX Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * @Description:  TODO<请描述此文件是做什么的>
 * @author:  MeiYi
 * @data:  2014年6月26日 下午2:10:17
 * @version:  V1.0
 */
package com.inter.trade.ui.fragment.gamerecharge.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import com.inter.protocol.ProtocolData;
import com.inter.protocol.ProtocolRsp;
import com.inter.protocol.util.ProtocolUtil;
import com.inter.trade.R;
import com.inter.trade.adapter.GameRechargeCompanyAdapter;
import com.inter.trade.data.CommonData;
import com.inter.trade.data.ResponseData;
import com.inter.trade.error.ErrorUtil;
import com.inter.trade.net.HttpUtil;
import com.inter.trade.ui.fragment.gamerecharge.data.GameListData;
import com.inter.trade.ui.fragment.gamerecharge.parser.GameListParser;
import com.inter.trade.ui.fragment.waterelectricgas.data.ResponseStateListener;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PromptUtil;

/**
 * TODO<请描述这个类是干什么的>
 * 
 * @author ChenGuangChi
 * @data: 2014年6月26日 下午2:10:17
 * @version: V1.0
 */
public class GameListTask extends AsyncTask<String, String, String> {

	private Context context;
	private ResponseStateListener listener;

	public GameListTask(Context context, ResponseStateListener listener) {
		super();
		this.context = context;
		this.listener = listener;
	}

	ProtocolRsp mRsp;

	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		try {
			CommonData mData = new CommonData();
			List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas(
					"ApiGameRecharge", "getGameList", mData);
			GameListParser myParser = new GameListParser();
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
				ArrayList<GameListData> parserResponse = parserResponse(mDatas);
				listener.onSuccess(parserResponse, GameListData.class);
				
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
	private ArrayList<GameListData> parserResponse(List<ProtocolData> mDatas) {
		ArrayList<GameListData> netData=new ArrayList<GameListData>();
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
				netData=new ArrayList<GameListData>();
				if(aupic!=null)
				for(ProtocolData child:aupic){
					GameListData game=new GameListData();
					if (child.mChildren != null && child.mChildren.size() > 0) {
						Set<String> keys = child.mChildren.keySet();
						for(String key:keys){
							List<ProtocolData> rs = child.mChildren.get(key);
							for(ProtocolData item: rs){
								if(item.mKey.equals("gameId")){
									game.setGameId(item.mValue);
								}else if(item.mKey.equals("gameName")){
									game.setGameName(item.mValue);
								}
							}
							
						}
					}
					netData.add(game);
//					mList.add(picData);
				}
			}
		}
		return netData;
	}

}
