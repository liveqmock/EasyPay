package com.inter.trade;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.inter.protocol.ProtocolData;
import com.inter.protocol.ProtocolRsp;
import com.inter.protocol.util.ProtocolUtil;
import com.inter.trade.AsyncLoadWork.AsyncLoadWorkListener;
import com.inter.trade.data.CommonData;
import com.inter.trade.data.LoginStatus;
import com.inter.trade.data.ResponseData;
import com.inter.trade.log.Logger;
import com.inter.trade.net.HttpUtil;
import com.inter.trade.ui.MainActivity;
import com.inter.trade.ui.fragment.checking.GestureModifyPwdActivity;
import com.inter.trade.ui.fragment.checking.LockActivity;
import com.inter.trade.ui.fragment.checking.util.RegistParser;
import com.inter.trade.util.Constants;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PreferenceConfig;
import com.inter.trade.util.PromptUtil;

public class LoginTask extends AsyncTask<String, Integer, Boolean>{

	private ProtocolRsp mRsp;
	
	private Context mcontext ;
	
	/**
	 * 异步加载侦听
	 */
	private AsyncLoadWorkListener asyncWorkListener;
	
	public LoginTask (Context context, AsyncLoadWorkListener asyncLoadWorkListener) {
		mcontext = context;
		asyncWorkListener = asyncLoadWorkListener;
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
				//				PromptUtil.showToast(LockActivity.this, LockActivity.this.getString(R.string.net_error));
				//				gestureInputPwdNumInfo();
			}else{
				try {
					List<ProtocolData> mDatas = mRsp.mActions;
					parserResoponse(mDatas);
					if(LoginUtil.mLoginStatus.result.equals(ProtocolUtil.SUCCESS)){
						LoginUtil.isLogin=true;
						LoginUtil.mLoginStatus.login_name=PreferenceConfig.instance(mcontext).getString(Constants.USER_NAME, "");
						
						Log.i("my login au_token ", LoginUtil.mLoginStatus.mResponseData.getAu_token());
						
						//保存数据  "是否绑定过代理商" 到PreferenceConfig
						PreferenceConfig.instance(mcontext).putString(Constants.IS_BIND_AGENT, LoginUtil.mLoginStatus.relateAgent+"");
						
						//保存数据  "代理商id" 到PreferenceConfig
						PreferenceConfig.instance(mcontext).putString(Constants.AGENT_ID, LoginUtil.mLoginStatus.agentid+"");
						
						//保存数据  "代理商类型id" 到PreferenceConfig
						PreferenceConfig.instance(mcontext).putString(Constants.AGENT_TYPE_ID, LoginUtil.mLoginStatus.agenttypeid+"");
						
						asyncWorkListener.onSuccess(null, null);
					}

					//					if(LoginUtil.mLoginStatus.result.equals(ProtocolUtil.SUCCESS)){
					////						PromptUtil.showToast(LockActivity.this, LoginUtil.mLoginStatus.message);
					//						LoginUtil.isLogin=true;
					//						LoginUtil.mLoginStatus.login_name=PreferenceConfig.instance(LockActivity.this).getString(Constants.USER_NAME, "");
					//						Logger.d("login","登录接口"+LoginUtil.mLoginStatus.mResponseData.getReq_token());
					//						ProtocolUtil.printString("LoginFragment",
					//								 LoginUtil.mLoginStatus.mResponseData.getReq_token());
					//						
					//						//手势密码登录成功，保存手势密码到本地（用于登录失败，尝试手势密码登录）
					//						PreferenceConfig.instance(LockActivity.this).putString(Constants.USER_GESTURE_PWD, patternString);
					//						
					//						//登录成功进入主页面
					//						if(isLoadMain) {
					//							Intent intent = new Intent();
					//				    		intent.setClass(LockActivity.this, MainActivity.class);
					//				    		startActivity(intent);
					//						}
					//						
					//						//手势密码修改登录密码
					//						if(isGestureModifyPwd){
					//							Intent intent = new Intent();
					//							intent.putExtra(Constants.USER_NAME, PreferenceConfig.instance(LockActivity.this).getString(Constants.USER_NAME, ""));
					//				    		intent.setClass(LockActivity.this, GestureModifyPwdActivity.class);
					//				    		startActivity(intent);
					//						}
					//						
					//			    		gestureInputPwdNum = 5;
					////			    		if(timeoutListener != null) {//手势密码登录成功后，回调超时侦听器
					////			    			timeoutListener.onTimeout();
					////			    		}
					//						
					//						finish();
					////						FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
					////						fragmentTransaction.replace(R.id.func_container,FragmentFactory.createFragment(FragmentFactory.current_index));
					////						fragmentTransaction.commit();
					//						
					//					}else {
					////						PromptUtil.showToast(LockActivity.this, LoginUtil.mLoginStatus.message);
					//						gestureInputPwdNumInfo();
					//					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					//					PromptUtil.showToast(LockActivity.this, getString(R.string.req_error));
					//					gestureInputPwdNumInfo();
				}

			}
		} catch (Exception e) {
			// TODO: handle exception
			//			gestureInputPwdNumInfo();
		}


	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
//				PromptUtil.showDialog(mcontext, "正在登录...");
	}



	private List<ProtocolData> getRequestDatas(){
		CommonData data = new CommonData();

//		data.putValue("mobile", LoginUtil.mLoginStatus.login_name != null ? LoginUtil.mLoginStatus.login_name 
//				: PreferenceConfig.instance(mcontext).getString(Constants.USER_NAME, ""));//用户手机号
//		data.putValue("gesturepasswd", PreferenceConfig.instance(mcontext).getString(Constants.USER_GESTURE_PWD, ""));//手势密码
//		data.putValue("paypasswd", LoginUtil.mLoginStatus.login_pwd != null ? LoginUtil.mLoginStatus.login_pwd 
//				: PreferenceConfig.instance(mcontext).getString(Constants.USER_PASSWORD, ""));//登录密码
		
		data.putValue("mobile",  PreferenceConfig.instance(mcontext).getString(Constants.USER_NAME, ""));//用户手机号
		data.putValue("gesturepasswd", PreferenceConfig.instance(mcontext).getString(Constants.USER_GESTURE_PWD, ""));//手势密码
//		data.putValue("paypasswd", LoginUtil.mLoginStatus.login_pwd != null ? LoginUtil.mLoginStatus.login_pwd 
//				: PreferenceConfig.instance(mcontext).getString(Constants.USER_PASSWORD, ""));//登录密码

		List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas("ApiAuthorInfoV2", "login", data);


		return mDatas;
	}


	/**
	 * 解析响应体
	 * @param params
	 */
	private void parserResoponse(List<ProtocolData> params){
		ResponseData response = new ResponseData();
//		LoginUtil.mLoginStatus = new LoginStatus();
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

				/*
				 * 用户是否绑定过代理商，relateAgent，“0”否，“1”是
				 */
				List<ProtocolData> relateAgent = data.find("/relateAgent");
				if(relateAgent != null){
					LoginUtil.mLoginStatus.relateAgent = relateAgent.get(0).mValue;
				}

				/*
				 * 代理商类型，agenttypeid，“0”普通用户，“1”正式代理商， “2”虚拟代理商
				 */
				List<ProtocolData> agenttypeid = data.find("/agenttypeid");
				if(agenttypeid != null){
					LoginUtil.mLoginStatus.agenttypeid = agenttypeid.get(0).mValue;
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
