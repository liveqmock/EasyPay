package com.inter.trade.ui.fragment.agent.task;

import java.util.List;

import android.content.Context;
import android.os.AsyncTask;

import com.inter.protocol.ProtocolData;
import com.inter.protocol.ProtocolRsp;
import com.inter.protocol.util.ProtocolUtil;
import com.inter.trade.R;
import com.inter.trade.data.CommonData;
import com.inter.trade.data.ResponseData;
import com.inter.trade.net.HttpUtil;
import com.inter.trade.ui.fragment.agent.util.BindAgentParser;
import com.inter.trade.ui.fragment.waterelectricgas.data.ResponseStateListener;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PromptUtil;

/**
	 * 绑定代理商
	 * @author zhichao.huang
	 *
	 */
	public class BindAgentTask extends AsyncTask<String, Integer, Boolean>{
		private Context context;
		private ResponseStateListener listener;
		private ProtocolRsp mRsp;
		
		public BindAgentTask(Context context, ResponseStateListener listener) {
			super();
			this.context = context;
			this.listener = listener;
		}
		@Override
		protected Boolean doInBackground(String... params) {
			CommonData data = new CommonData();
			data.putValue("querytype", "");//查询类型
			data.putValue("agentno", params[0]);//代理商代号
			List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas("ApiAgentInfo", "authorBindAgent", data);
			BindAgentParser authorRegParser = new BindAgentParser();
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
				}else{
					try {
						List<ProtocolData> mDatas = mRsp.mActions;
						parserResoponse(mDatas);
						
						if(LoginUtil.mLoginStatus.result.equals(ProtocolUtil.SUCCESS)){
							PromptUtil.showToast(context, LoginUtil.mLoginStatus.message);
							if(listener!=null){
								listener.onSuccess("success", String.class);
							}
							
						}else {
							PromptUtil.showToast(context, LoginUtil.mLoginStatus.message);
						}
					} catch (Exception e) {
						e.printStackTrace();
						PromptUtil.showToast(context, context.getString(R.string.req_error));
					}
					
				}
			} catch (Exception e) {
			}
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			PromptUtil.showDialog(context, "正在提交...");
		}
	
	/**
	 * 解析响应体
	 * @param params
	 */
	private void parserResoponse(List<ProtocolData> params){
		ResponseData response = new ResponseData();
		LoginUtil.mLoginStatus.mResponseData = response;
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
				
				List<ProtocolData> authorid = data.find("/authorid");
				if(authorid != null){
					LoginUtil.mLoginStatus.authorid = authorid.get(0).mValue;
				}
				
				List<ProtocolData> agentid = data.find("/agentid");
				if(agentid != null){
					LoginUtil.mLoginStatus.agentid = agentid.get(0).mValue;
				}
				
				List<ProtocolData> ispaypwd = data.find("/ispaypwd");
				if(ispaypwd != null){
					LoginUtil.mLoginStatus.ispaypwd = ispaypwd.get(0).mValue;
				}
				
				List<ProtocolData> gesturepasswd = data.find("/gesturepasswd");
				if(gesturepasswd != null){
					LoginUtil.mLoginStatus.gesturepasswd = gesturepasswd.get(0).mValue;
				}
			}
		}
	}
}