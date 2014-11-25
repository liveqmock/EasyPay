package com.inter.trade.error;
import android.app.Activity;

import com.inter.protocol.util.ProtocolUtil;
import com.inter.trade.AsyncLoadWork.LoginTimeoutListener;
import com.inter.trade.data.LoginStatus;
import com.inter.trade.ui.PayApp;
import com.inter.trade.util.PromptUtil;

public class ErrorUtil {
	private static ErrorUtil mErrorUtil=null;
	
	public static ErrorUtil create(){
		if(mErrorUtil == null){
			mErrorUtil =new  ErrorUtil();
		}
		return mErrorUtil;
	}
	
	public boolean errorDeal(LoginStatus mStatus,final Activity activity){
		if(mStatus.mResponseData==null||mStatus.result==null||activity==null){
			return false;
		}
		String retType = mStatus.mResponseData.getRettype();
		String retCode = mStatus.mResponseData.getRetcode();
		String retMsg = mStatus.mResponseData.getRetmsg();
		String result=mStatus.result;
		String message=mStatus.message;
		
		if(retMsg==null || "".equals(retMsg)){
			retMsg = message;
		}
		if(retType.equals(ProtocolUtil.HEADER_SUCCESS) && retCode.equals(ProtocolUtil.HEADER_SUCCESS)){
//			PromptUtil.showToast(PayApp.pay, retMsg);
			if(result.equals(ProtocolUtil.SUCCESS)){
				return true;
			}else{
				return false;
			}
		}else if(retCode.equals(ProtocolUtil.REAL_ERROR)){
			PromptUtil.showRealFail(activity, retMsg);
			return false;
			
		}else if(retCode.equals(ProtocolUtil.ERROR_LOGINED)){//301 已登录
			PromptUtil.showLogoutTips(activity, "您的账号在其他设备已登录，请重新登录!");
			return false;
		}else{
			PromptUtil.showToast(PayApp.pay, retMsg);
			return false;
		}
	}
	
	/**
	 *  与errorDeal方法是一样的，只有处理200的时候不弹Toast
	 * @param mStatus
	 * @param activity
	 * @return
	 * @throw
	 * @return boolean
	 */
	public boolean dealError(LoginStatus mStatus,final Activity activity){
		if(mStatus.mResponseData==null||mStatus.result==null||activity==null){
			return false;
		}
		String retType = mStatus.mResponseData.getRettype();
		String retCode = mStatus.mResponseData.getRetcode();
		String retMsg = mStatus.mResponseData.getRetmsg();
		String result=mStatus.result;
		String message=mStatus.message;
		
		if(retMsg==null || "".equals(retMsg)){
			retMsg = message;
		}
		if(retType.equals(ProtocolUtil.HEADER_SUCCESS) && retCode.equals(ProtocolUtil.HEADER_SUCCESS)){
//			PromptUtil.showToast(PayApp.pay, retMsg);
			if(result.equals(ProtocolUtil.SUCCESS)){
				return true;
			}else{
				return false;
			}
		}else if(retCode.equals(ProtocolUtil.REAL_ERROR)){
			PromptUtil.showRealFail(activity, retMsg);
			return false;
			
		}else if(retCode.equals(ProtocolUtil.ERROR_LOGINED)){//301 已登录
			PromptUtil.showLogoutTips(activity, "您的账号在其他设备已登录，请重新登录!");
			return false;
		}else{
			//PromptUtil.showToast(PayApp.pay, retMsg);
			return false;
		}
	}
	
	/**
	 *  与errorDeal方法是一样的，只有处理200的时候不弹Toast,换成Dialog
	 * @param mStatus
	 * @param activity
	 * @return
	 * @throw
	 * @return boolean
	 */
	public boolean dealErrorWithDialog(LoginStatus mStatus,final Activity activity){
		if(mStatus.mResponseData==null||mStatus.result==null||activity==null){
			return false;
		}
		String retType = mStatus.mResponseData.getRettype();
		String retCode = mStatus.mResponseData.getRetcode();
		String retMsg = mStatus.mResponseData.getRetmsg();
		String result=mStatus.result;
		String message=mStatus.message;
		
		if(retMsg==null || "".equals(retMsg)){
			retMsg = message;
		}
		if(retType.equals(ProtocolUtil.HEADER_SUCCESS) && retCode.equals(ProtocolUtil.HEADER_SUCCESS)){
//			PromptUtil.showToast(PayApp.pay, retMsg);
			if(result.equals(ProtocolUtil.SUCCESS)){
				return true;
			}else{
				return false;
			}
		}else if(retCode.equals(ProtocolUtil.REAL_ERROR)){
			PromptUtil.showRealFail(activity, retMsg);
			return false;
			
		}else if(retCode.equals(ProtocolUtil.ERROR_LOGINED)){//301 已登录
			PromptUtil.showLogoutTips(activity, "您的账号在其他设备已登录，请重新登录!");
			return false;
		}else{
			PromptUtil.showNoticeDialog("提示", retMsg, activity);
			return false;
		}
	}
	
	/**
	 * 主要用于超时处理
	 * 
	 * @param mStatus
	 * @param activity
	 * @param timeoutListener 超时侦听
	 * @return
	 */
	public boolean errorDeal(LoginStatus mStatus,final Activity activity, LoginTimeoutListener timeoutListener){
		if(mStatus.mResponseData==null||mStatus.result==null||activity==null){
			return false;
		}
		String retType = mStatus.mResponseData.getRettype();
		String retCode = mStatus.mResponseData.getRetcode();
		String retMsg = mStatus.mResponseData.getRetmsg();
		String result=mStatus.result;
		String message=mStatus.message;
		
		if(retMsg==null || "".equals(retMsg)){
			retMsg = message;
		}
		if(retType.equals(ProtocolUtil.HEADER_SUCCESS) && retCode.equals(ProtocolUtil.HEADER_SUCCESS)){
//			PromptUtil.showToast(PayApp.pay, retMsg);
			if(result.equals(ProtocolUtil.SUCCESS)){
				return true;
			}else{
				return false;
			}
		}else if(retCode.equals(ProtocolUtil.REAL_ERROR)){
			PromptUtil.showTimeoutRealFail(activity, retMsg, timeoutListener);
			return false;
			
		}else if(retCode.equals(ProtocolUtil.ERROR_LOGINED)){//301 已登录
			PromptUtil.showLogoutTips(activity, "您的账号在其他设备已登录，请重新登录!");
			return false;
		}else{
			PromptUtil.showToast(PayApp.pay, retMsg);
			return false;
		}
	}
	
}
