package com.inter.trade.ui.fragment.agent.task;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import com.inter.protocol.ProtocolData;
import com.inter.protocol.ProtocolRsp;
import com.inter.protocol.util.ProtocolUtil;
import com.inter.trade.R;
import com.inter.trade.data.CommonData;
import com.inter.trade.data.ResponseData;
import com.inter.trade.error.ErrorUtil;
import com.inter.trade.net.HttpUtil;
import com.inter.trade.ui.fragment.agent.util.AgentData;
import com.inter.trade.ui.fragment.agent.util.AgentParser;
import com.inter.trade.ui.fragment.waterelectricgas.data.ResponseStateListener;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PromptUtil;
/**
 * 读取代理商相关的信息 
 * @author  chenguangchi
 * @data:  2014年10月31日 上午11:58:28
 * @version:  V1.0
 */
public class ReadAgentInfoTask extends AsyncTask<String, Integer, Boolean> {
	ProtocolRsp mRsp;

	private Context context;

	private ResponseStateListener listener;

	public ReadAgentInfoTask(Context context, ResponseStateListener listener) {
		super();
		this.context = context;
		this.listener = listener;
	}

	@Override
	protected Boolean doInBackground(String... params) {
		try {
			CommonData data = new CommonData();
			List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas(
					"ApiAgentInfo", "readagentinfo", data);
			AgentParser authorRegParser = new AgentParser();
			mRsp = HttpUtil.doRequest(authorRegParser, mDatas);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			mRsp = null;
		}

		return null;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);

		PromptUtil.dissmiss();
		if (mRsp == null) {
			PromptUtil
					.showToast(context, context.getString(R.string.net_error));
		} else {
			try {
				List<ProtocolData> mDatas = mRsp.mActions;
				AgentData agentData = parserResoponse(mDatas);
				if (!ErrorUtil.create().errorDeal(LoginUtil.mLoginStatus,
						(Activity) context)) {
					return;
				}

				if (listener != null) {
					listener.onSuccess(agentData, AgentData.class);
				}

			} catch (Exception e) {
				PromptUtil.showToast(context,
						context.getString(R.string.req_error));
				e.printStackTrace();
			}

		}
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		PromptUtil.showDialog(context,
				context.getResources().getString(R.string.loading));
		// PromptUtil.showDialog(getActivity(), "代理商");
	}

	private AgentData parserResoponse(List<ProtocolData> params) {
		AgentData agentData = new AgentData();
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

				List<ProtocolData> todayfenrun = data.find("/todayfenrun");
				if (todayfenrun != null) {
					agentData.todayfenrun = todayfenrun.get(0).mValue;
				}
				List<ProtocolData> areafenrun = data.find("/areafenrun");
				if (areafenrun != null) {
					agentData.areafenrun = areafenrun.get(0).mValue;
				}
				List<ProtocolData> salepaycardnum = data
						.find("/salepaycardnum");
				if (salepaycardnum != null) {
					agentData.salepaycardnum = salepaycardnum.get(0).mValue;
				}
				List<ProtocolData> areapaycardnum = data
						.find("/areapaycardnum");
				if (areapaycardnum != null) {
					agentData.areapaycardnum = areapaycardnum.get(0).mValue;
				}
				List<ProtocolData> areaauthornum = data.find("/areaauthornum");
				if (areaauthornum != null) {
					agentData.areaauthornum = areaauthornum.get(0).mValue;
				}
			}
		}
		return agentData;
	}

}