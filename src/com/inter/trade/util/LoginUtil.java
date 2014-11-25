package com.inter.trade.util;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.inter.trade.R;
import com.inter.trade.data.LoginStatus;
import com.inter.trade.data.UserInfo;
import com.inter.trade.ui.FunctionActivity;
import com.inter.trade.ui.fragment.FragmentFactory;
import com.inter.trade.ui.fragment.LoginFragment;

public class LoginUtil {
	public static boolean isLogin = false;
	public static boolean isAgentLogin = false;//代理商登录，暂时测试用
	public static LoginStatus mLoginStatus=new LoginStatus();
	public static  UserInfo mUserInfo = new UserInfo();
	
	public static boolean checkLogin(){
		boolean flag = true;
		if(mLoginStatus != null){
			if(mLoginStatus.mResponseData == null){
				flag = false;
			}
		}else {
			flag = false;
		}
		return flag;
	}
	
	public static void startLogin(Context context){
		Intent intent = new Intent();
		intent.setClass(context, FunctionActivity.class);
		intent.putExtra(FragmentFactory.INDEX_KEY, FragmentFactory.LOGIN_FRAGMENT_INDEX);
		context.startActivity(intent);
	}
	
	public static void detection(Activity context){
		try {
			if(isLogin !=false){
				if(!checkLogin()){
//					PromptUtil.showToast(context, "请重新登录");
////					startLogin(context);
//					context.finish();
//					PromptUtil.showRealFail(context, "登录失效，请重新登录");
					PromptUtil.showLogin2(context);
				}
			}else {
				PromptUtil.showRealFail(context, "网络异常，请重新登录");
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		
	}
	public static void showLogin(FragmentManager fragmentManager){
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.replace(R.id.func_container,new LoginFragment());
		fragmentTransaction.commit();
	}
}
