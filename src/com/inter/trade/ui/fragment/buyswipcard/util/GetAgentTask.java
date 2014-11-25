/*
 * @Title:  GetAgentTask.java
 * @Copyright:  XXX Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * @Description:  TODO<请描述此文件是做什么的>
 * @author:  MeiYi
 * @data:  2014年8月4日 下午4:57:47
 * @version:  V1.0
 */
package com.inter.trade.ui.fragment.buyswipcard.util;

import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;

import com.inter.protocol.ProtocolData;
import com.inter.protocol.ProtocolRsp;
import com.inter.protocol.util.ProtocolUtil;
import com.inter.trade.R;
import com.inter.trade.data.CommonData;
import com.inter.trade.data.ResponseData;
import com.inter.trade.net.HttpUtil;
import com.inter.trade.ui.fragment.agent.util.GetAgentParser;
import com.inter.trade.ui.fragment.waterelectricgas.data.ResponseStateListener;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PromptUtil;

/**
 * 获取绑定代理商信息
 * 
 * @author ChenGuangChi
 * @data: 2014年8月4日 下午4:57:47
 * @version: V1.0
 */
public class GetAgentTask extends AsyncTask<String, Integer, Boolean> {
	private ProtocolRsp mRsp;
	private Context context;
	private ResponseStateListener listener;

	public GetAgentTask(Context context, ResponseStateListener listener) {
		super();
		this.context = context;
		this.listener = listener;
	}

	@Override
	protected Boolean doInBackground(String... params) {
		// TODO Auto-generated method stub
		List<ProtocolData> mDatas = getAreaRequestDatas();
		GetAgentParser authorRegParser = new GetAgentParser();
		mRsp = HttpUtil.doRequest(authorRegParser, mDatas);
		return null;
	}

	@Override
	protected void onPostExecute(Boolean result) {

		super.onPostExecute(result);
		try {
			PromptUtil.dissmiss();
			if (mRsp == null) {
				PromptUtil.showToast(context,
						context.getString(R.string.net_error));
			} else {
				try {
					List<ProtocolData> mDatas = mRsp.mActions;
					String agentno = parserResoponseArea(mDatas);

					if (LoginUtil.mLoginStatus.result
							.equals(ProtocolUtil.SUCCESS)) {
						if(listener!=null){
							listener.onSuccess(agentno, String.class);
						}
					} else {
						PromptUtil.showToast(context,
								LoginUtil.mLoginStatus.message);
					}
				} catch (Exception e) {
					e.printStackTrace();
					PromptUtil.showToast(context,
							context.getString(R.string.req_error));
				}

			}
		} catch (Exception e) {
		}

	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		PromptUtil.showDialog(context,
				context.getResources().getString(R.string.loading));
	}

	private List<ProtocolData> getAreaRequestDatas() {
		CommonData data = new CommonData();
		List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas(
				"ApiAgentInfo", "readauthorareaagent", data);
		return mDatas;
	}

	/**
	 * 解析响应体
	 * 
	 * @param params
	 */
	private String parserResoponseArea(List<ProtocolData> params) {
		ResponseData response = new ResponseData();
		LoginUtil.mLoginStatus.mResponseData = response;
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

				List<ProtocolData> agentno = data.find("/agentno");
				if (agentno != null) {
					return agentno.get(0).mValue;
				}
			}
		}
		return "";
	}
}
