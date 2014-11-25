package com.inter.trade.ui.fragment.agent.task;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;

import com.inter.protocol.ProtocolData;
import com.inter.protocol.ProtocolRsp;
import com.inter.protocol.util.ProtocolUtil;
import com.inter.trade.AsyncLoadWork.AsyncLoadWorkListener;
import com.inter.trade.R;
import com.inter.trade.data.CommonData;
import com.inter.trade.data.ResponseData;
import com.inter.trade.error.ErrorUtil;
import com.inter.trade.net.HttpUtil;
import com.inter.trade.ui.fragment.agent.util.GetAgentParser;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PromptUtil;

/**
	 * 获取代理商信息，即获取激活码
	 * @author zhichao.huang
	 *
	 */
	public class GetAgentTask extends AsyncTask<String, Integer, Boolean>{
		
		private Context context;
		private AsyncLoadWorkListener listener;
		private ProtocolRsp mRsp;
		
		public GetAgentTask(Context context, AsyncLoadWorkListener listener) {
			super();
			this.context = context;
			this.listener = listener;
		}

		@Override
		protected Boolean doInBackground(String... params) {
			CommonData data = new CommonData();
			List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas("ApiAgentInfo", "readauthorareaagent", data);
			GetAgentParser authorRegParser = new GetAgentParser();
			mRsp = HttpUtil.doRequest(authorRegParser, mDatas);
			return null;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			
			super.onPostExecute(result);
			try {
				PromptUtil.dissmiss();
				if(mRsp == null){
					PromptUtil.showToast(context, context.getString(R.string.net_error));
					if(listener!=null){
						listener.onFailure("error");
					}
				}else{
					try {
						List<ProtocolData> mDatas = mRsp.mActions;
						AgentInfo info = parserResoponseArea(mDatas);
						if(!ErrorUtil.create().errorDeal(LoginUtil.mLoginStatus,(Activity)context)){
							if(listener!=null){
								listener.onFailure("error");
							}
							return;
						}
						if(LoginUtil.mLoginStatus.result.equals(ProtocolUtil.SUCCESS)){
//							成功不弹出提示
//							PromptUtil.showToast(getActivity(), LoginUtil.mLoginStatus.message);
							if(listener!=null){
								listener.onSuccess(info, null);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
						PromptUtil.showToast(context, context.getString(R.string.req_error));
						if(listener!=null){
							listener.onFailure("error");
						}
					}
					
				}
			} catch (Exception e) {
				e.printStackTrace();
				if(listener!=null){
					listener.onFailure("error");
				}
			}
		
			
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			PromptUtil.showDialog(context, context.getResources().getString(R.string.loading));
		}
    
    /**
	 * 解析响应体
	 * @param params
	 */
	private AgentInfo parserResoponseArea(List<ProtocolData> params){
		ResponseData response = new ResponseData();
		LoginUtil.mLoginStatus.mResponseData = response;
		AgentInfo info=new AgentInfo();
		for(ProtocolData data :params){
			if(data.mKey.equals(ProtocolUtil.msgheader)){
				ProtocolUtil.parserResponse(response, data);
				
			}else if(data.mKey.equals(ProtocolUtil.msgbody)){
				List<ProtocolData> result1 = data.find("/result");
				if(result1 != null){
					LoginUtil.mLoginStatus.result = result1.get(0).mValue;
				}
				
				
				List<ProtocolData> message = data.find("/message");
				if(message != null){
					LoginUtil.mLoginStatus.message = message.get(0).mValue;
				}
				
				List<ProtocolData> agentno = data.find("/agentno");
				if(agentno != null){
					info.setAgentNo(agentno.get(0).mValue);
				}
				
				List<ProtocolData> agentarea = data.find("/agentarea");
				if(agentarea != null){
					info.setAgentArea(agentarea.get(0).mValue);
				}
			}
		}
		return info;
	}
	/**
	 * 绑定信息 
	 * @author  chenguangchi
	 * @data:  2014年8月29日 上午11:11:29
	 * @version:  V1.0
	 */
	public static class AgentInfo{
		/***代理商代号*/
		private String agentNo;
		
		/**代理商区域*/
		private String agentArea;

		public String getAgentNo() {
			return agentNo;
		}

		public void setAgentNo(String agentNo) {
			this.agentNo = agentNo;
		}

		public String getAgentArea() {
			return agentArea;
		}

		public void setAgentArea(String agentArea) {
			this.agentArea = agentArea;
		}
	}
}