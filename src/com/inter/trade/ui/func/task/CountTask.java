/*
 * @Title:  CountTask.java
 * @Copyright:  XXX Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * @Description:  TODO<请描述此文件是做什么的>
 * @author:  MeiYi
 * @data:  2014年7月7日 上午9:35:57
 * @version:  V1.0
 */
package com.inter.trade.ui.func.task;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import com.inter.protocol.ProtocolData;
import com.inter.protocol.ProtocolRsp;
import com.inter.protocol.util.ProtocolUtil;
import com.inter.trade.R;
import com.inter.trade.data.CommonData;
import com.inter.trade.error.ErrorUtil;
import com.inter.trade.net.HttpUtil;
import com.inter.trade.ui.fragment.waterelectricgas.data.ResponseStateListener;
import com.inter.trade.ui.func.parser.CountParser;
import com.inter.trade.ui.func.parser.ModelParser;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PromptUtil;

/**
 * 统计功能使用次数
 * @author  ChenGuangChi
 * @data:  2014年7月7日 上午9:35:57
 * @version:  V1.0
 */
public class CountTask extends AsyncTask<String, Void, Void> {
	private Context context;
	private ResponseStateListener listener;
	ProtocolRsp mRsp;	
	
	public CountTask(Context context, ResponseStateListener listener) {
		super();
		this.context = context;
		this.listener = listener;
	}

	/**
	 * 重载方法
	 * @param params
	 * @return
	 */
	@Override
	protected Void doInBackground(String... params) {
		try {
			CommonData mData = new CommonData();
			mData.putValue("appmnuid", params[0]);
			mData.putValue("agentno", params[1]);
			
			List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas("ApiAppInfo", 
					"authorMenuCount", mData);
			CountParser myParser = new CountParser();
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
		//PromptUtil.showDialog(context, context.getResources().getString(R.string.loading));
	}

	@Override
	protected void onPostExecute(Void result) {
		//PromptUtil.dissmiss();
		if(mRsp==null){
			//PromptUtil.showToast(context, context.getString(R.string.net_error));
		}else{
			try {
				List<ProtocolData> mDatas = mRsp.mActions;
				
//				if(!ErrorUtil.create().errorDeal(LoginUtil.mLoginStatus,(Activity)context)){
//					return;
//				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
 
}
