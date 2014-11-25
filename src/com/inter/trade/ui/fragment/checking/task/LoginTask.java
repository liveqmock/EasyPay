package com.inter.trade.ui.fragment.checking.task;

import java.util.List;

import android.content.Context;
import android.os.AsyncTask;

import com.inter.protocol.ProtocolData;
import com.inter.protocol.ProtocolRsp;
import com.inter.protocol.util.ProtocolUtil;
import com.inter.trade.R;
import com.inter.trade.data.CommonData;
import com.inter.trade.data.ResponseData;
import com.inter.trade.log.Logger;
import com.inter.trade.net.HttpUtil;
import com.inter.trade.ui.MainActivity;
import com.inter.trade.ui.fragment.checking.bean.LoginInfo;
import com.inter.trade.ui.fragment.checking.util.RegistParser;
import com.inter.trade.ui.fragment.waterelectricgas.data.ResponseStateListener;
import com.inter.trade.util.Constants;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PreferenceConfig;
import com.inter.trade.util.PromptUtil;

public class LoginTask extends AsyncTask<String, Integer, Boolean>{
		private ProtocolRsp mRsp;
		
		private Context context;
		
		private ResponseStateListener listener;
		
		public LoginTask(Context context, ResponseStateListener listener) {
			super();
			this.context = context;
			this.listener = listener;
		}

		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			List<ProtocolData> mDatas = getRequestDatas();
			RegistParser authorRegParser = new RegistParser();
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
						LoginInfo info = parserResoponse(mDatas);
						
						if(LoginUtil.mLoginStatus.result.equals(ProtocolUtil.SUCCESS)){
							PromptUtil.showToast(context, LoginUtil.mLoginStatus.message);
							LoginUtil.isLogin=true;
							
							//保存数据到PreferenceConfig
							PreferenceConfig.instance(context).putString(Constants.USER_NAME, LoginUtil.mLoginStatus.login_name);
							PreferenceConfig.instance(context).putString(Constants.USER_PASSWORD, LoginUtil.mLoginStatus.login_pwd);
							PreferenceConfig.instance(context).putString(Constants.USER_AUTHORID, LoginUtil.mLoginStatus.authorid);
							
							//保存数据  "是否绑定过代理商" 到PreferenceConfig
							PreferenceConfig.instance(context).putString(Constants.IS_BIND_AGENT, LoginUtil.mLoginStatus.relateAgent+"");
							
							//保存数据  "代理商id" 到PreferenceConfig
							PreferenceConfig.instance(context).putString(Constants.AGENT_ID, LoginUtil.mLoginStatus.agentid+"");
							
							//保存数据  "代理商类型id" 到PreferenceConfig
							PreferenceConfig.instance(context).putString(Constants.AGENT_TYPE_ID, LoginUtil.mLoginStatus.agenttypeid+"");
							
							//保存上一次登陆的用户名
							PreferenceConfig.instance(context).putString(Constants.LAST_LOGIN_USERNAME, LoginUtil.mLoginStatus.login_name);
							
							Logger.d("login","登录接口"+LoginUtil.mLoginStatus.mResponseData.getReq_token());
							ProtocolUtil.printString("LoginFragment",
									 LoginUtil.mLoginStatus.mResponseData.getReq_token());
							
							MainActivity.switchLogin = true;
							
							
							if(listener!=null){
								listener.onSuccess(info, LoginInfo.class);
							}
							
//							new CheckUserStateTask().execute("");
							
				    		
//				    		context.setResult(Constants.ACTIVITY_FINISH);
				    		
//							FragmentTransaction fragmentTransaction = context.getSupportFragmentManager().beginTransaction();
//							fragmentTransaction.replace(R.id.func_container,FragmentFactory.createFragment(FragmentFactory.current_index));
//							fragmentTransaction.commit();
						}else{
							PromptUtil.showToast(context, LoginUtil.mLoginStatus.message);
						}
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
						PromptUtil.showToast(context, context.getString(R.string.req_error));
					}
					
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		
			
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			PromptUtil.showDialog(context, "正在登录...");
		}
		private List<ProtocolData> getRequestDatas(){
			CommonData data = new CommonData();
			
			data.putValue("mobile", LoginUtil.mLoginStatus.login_name);//用户手机号
			data.putValue("gesturepasswd", "");//手势密码
			data.putValue("paypasswd", LoginUtil.mLoginStatus.login_pwd);//登录密码
			
			List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas("ApiAuthorInfoV2", "login", data);
			
			
			return mDatas;
		}
		
		  /**
		 * 解析响应体
		 * @param params
		 */
		private LoginInfo parserResoponse(List<ProtocolData> params){
			LoginInfo info=new LoginInfo();
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
						info.authorid = authorid.get(0).mValue;
					}
					
					List<ProtocolData> agentid = data.find("/agentid");
					if(agentid != null){
						LoginUtil.mLoginStatus.agentid = agentid.get(0).mValue;
						info.agentid = agentid.get(0).mValue;
					}
					
					/*
					 * 用户是否绑定过代理商，relateAgent，“0”否，“1”是
					 */
					List<ProtocolData> relateAgent = data.find("/relateAgent");
					if(relateAgent != null){
						LoginUtil.mLoginStatus.relateAgent = relateAgent.get(0).mValue;
						info.relateAgent = relateAgent.get(0).mValue;
					}
					
					/*
					 * 代理商类型，agenttypeid，“0”普通用户，“1”正式代理商， “2”虚拟代理商
					 */
					List<ProtocolData> agenttypeid = data.find("/agenttypeid");
					if(agenttypeid != null){
						LoginUtil.mLoginStatus.agenttypeid = agenttypeid.get(0).mValue;
						info.agenttypeid = agenttypeid.get(0).mValue;
					}
					
					List<ProtocolData> ispaypwd = data.find("/ispaypwd");
					if(ispaypwd != null){
						LoginUtil.mLoginStatus.ispaypwd = ispaypwd.get(0).mValue;
						info.ispaypwd = ispaypwd.get(0).mValue;
					}
					
					List<ProtocolData> gesturepasswd = data.find("/gesturepasswd");
					if(gesturepasswd != null){
						LoginUtil.mLoginStatus.gesturepasswd = gesturepasswd.get(0).mValue;
						info.gesturepasswd = gesturepasswd.get(0).mValue;
					}
				}
			}
			return info;
		}
	}