package com.inter.trade.ui.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.inter.protocol.ProtocolData;
import com.inter.protocol.ProtocolRsp;
import com.inter.protocol.util.ProtocolUtil;
import com.inter.trade.R;
import com.inter.trade.data.CommonData;
import com.inter.trade.data.ResponseData;
import com.inter.trade.error.ErrorUtil;
import com.inter.trade.net.HttpUtil;
import com.inter.trade.ui.ReadPwdProtectionActivity;
import com.inter.trade.ui.fragment.checking.pwdsafety.PwdSafetyReplyActivity;
import com.inter.trade.ui.fragment.checking.pwdsafety.PwdSafetyReplyCheckFragment;
import com.inter.trade.ui.fragment.checking.util.PwdSafetyValidateUserData;
import com.inter.trade.ui.fragment.checking.util.PwdSafetyValidateUserParser;
import com.inter.trade.util.Constants;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PreferenceConfig;
import com.inter.trade.util.PromptUtil;


/**
 * 读取指定账号的密保问题和答案
 * @author Lihaifeng
 *
 */
public class ReadPwdProtectionTask extends AsyncTask<String, Integer, Boolean>{
	public static ArrayList<PwdSafetyValidateUserData> mList = new ArrayList<PwdSafetyValidateUserData>();
	public static String phonenumber;
	private FragmentActivity activity;
//	private PwdSafetyTask mReadPwdProtectionTask;
	ProtocolRsp mRsp;
	public ReadPwdProtectionTask (FragmentActivity context, String phone){
		activity = context;
		phonenumber = phone;
	}

	@Override
	protected Boolean doInBackground(String... params) {
		// TODO Auto-generated method stub
		try {
//			String phonenumber=PreferenceConfig.instance(activity).getString(Constants.USER_NAME, "");
			CommonData mData = new CommonData();
			mData.putValue("phonenumber", phonenumber);
			
			List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas("ApiSafeGuard", 
					"validateUser", mData);
			PwdSafetyValidateUserParser pwdSafetyValidateUserParser = new PwdSafetyValidateUserParser();
			mRsp = HttpUtil.doRequest(pwdSafetyValidateUserParser, mDatas);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			mRsp =null;
		}
		
		return null;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		
		PromptUtil.dissmiss();
		if(mRsp==null){
			PromptUtil.showToast(activity, activity.getString(R.string.net_error));
		}else{
			try {
				mList.clear();
				List<ProtocolData> mDatas = mRsp.mActions;
				parserResponse(mDatas);
				
				if(!ErrorUtil.create().errorDeal(LoginUtil.mLoginStatus,activity)){
					if(LoginUtil.mLoginStatus.mResponseData.getRettype().equals(ProtocolUtil.HEADER_SUCCESS) 
						&& LoginUtil.mLoginStatus.mResponseData.getRetcode().equals(ProtocolUtil.HEADER_SUCCESS)){
						PromptUtil.showToast(activity, LoginUtil.mLoginStatus.message);
					}
					return;
				}
				
				if(LoginUtil.mLoginStatus.result.equals(ProtocolUtil.SUCCESS)){
			
					Intent pwdIntent = new Intent();
					pwdIntent.setClass(activity, ReadPwdProtectionActivity.class);
					activity.startActivity(pwdIntent);
				}
			} catch (Exception e) {
				PromptUtil.showToast(activity, activity.getString(R.string.req_error));
			}
		
		}
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		PromptUtil.showDialog(activity, activity.getResources().getString(R.string.loading));
	}

	private void parserResponse(List<ProtocolData> mDatas) {
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
				List<ProtocolData> authorid = data.find("/authorid");
				if(authorid != null){
					LoginUtil.mLoginStatus.authorid = authorid.get(0).mValue;
				}
				
				List<ProtocolData> aupic = data.find("/msgchild");
				if(aupic!=null)
				for(ProtocolData child:aupic){
					PwdSafetyValidateUserData picData = new PwdSafetyValidateUserData();
					if (child.mChildren != null && child.mChildren.size() > 0) {
						Set<String> keys = child.mChildren.keySet();
						for(String key:keys){
							List<ProtocolData> rs = child.mChildren.get(key);
							for(ProtocolData item: rs){
								if(item.mKey.equals("que")){
									picData.que  = item.mValue;
									
								}else if(item.mKey.equals("answer")){
									picData.answer  = item.mValue;
									
								}
							}
						}
					}
					
					mList.add(picData);
				}
			}
		}
	}
}