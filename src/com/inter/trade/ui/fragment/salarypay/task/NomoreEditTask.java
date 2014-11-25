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
import android.util.Log;

import com.inter.protocol.ProtocolData;
import com.inter.protocol.ProtocolRsp;
import com.inter.protocol.util.ProtocolUtil;
import com.inter.trade.R;
import com.inter.trade.data.CommonData;
import com.inter.trade.data.ResponseData;
import com.inter.trade.error.ErrorUtil;
import com.inter.trade.log.Logger;
import com.inter.trade.net.HttpUtil;
import com.inter.trade.ui.fragment.salarypay.parser.NomoreEditParser;
import com.inter.trade.ui.fragment.waterelectricgas.data.ResponseStateListener;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PromptUtil;

/**
 * 修改当前列表的工资的可编辑
 * 
 * @author ChenGuangChi
 * @data: 2014年6月25日 上午11:53:40
 * @version: V1.0
 */
public class NomoreEditTask extends AsyncTask<String, Void, Void> {

	private Context context;
	private ResponseStateListener listener;

	public NomoreEditTask(Context context,
			ResponseStateListener listener) {
		super();
		this.context = context;
		this.listener = listener;
	}

	ProtocolRsp mRsp;

	@Override
	protected Void doInBackground(String... params) {
		try {
			CommonData mData = new CommonData();
			mData.putValue("month", params[0]);
			mData.putValue("bossauthorid", params[1]);

			List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas(
					"ApiWageInfo", "Submit", mData);
			NomoreEditParser myParser = new NomoreEditParser();
			mRsp = HttpUtil.doRequest(myParser, mDatas);
		} catch (Exception e) {
			e.printStackTrace();
			mRsp = null;
		}
		return null;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		PromptUtil.showDialog(context,
				context.getResources().getString(R.string.loading));
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		PromptUtil.dissmiss();
		if (mRsp == null) {
			PromptUtil
					.showToast(context, context.getString(R.string.net_error));
		} else {
			try {
				List<ProtocolData> mDatas = mRsp.mActions;
				parserResponse(mDatas);

				if (!ErrorUtil.create().errorDeal(LoginUtil.mLoginStatus,
						(Activity) context)) {
					return;
				}

				if (listener != null) {
					listener.onSuccess(null, null);
				}

			} catch (Exception e) {
				PromptUtil.showToast(context,
						context.getString(R.string.req_error));
				Logger.i(Log.getStackTraceString(e));
			}
		}
	}

	private void parserResponse(List<ProtocolData> mDatas) {
		ResponseData response = new ResponseData();
		LoginUtil.mLoginStatus.mResponseData = response;
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
			}
		}
	}
}
