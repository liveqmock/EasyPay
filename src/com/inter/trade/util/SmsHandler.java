package com.inter.trade.util;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
/**
 * 
 * 自动填充手机验证码的相关类
 * @author  chenguangchi
 * @data:  2014年8月21日 上午9:23:55
 * @version:  V1.0
 */
public class SmsHandler {
	
	private static final int SMS_RECEIVED=66;
	
	private static final String TAG="sms";

	private Activity context;

	private ContentObserver contentObsesrver;
	
	private Handler han;
	
	public SmsHandler(Activity context,SMSReceiveListener listener) {
		super();
		this.context = context;
		han=new SmsAutoTextHandler(context, listener);
	}
	
	/***
	 * 注册手机验证码的监听
	 * 需要在使用自动填充短信之前调用此方法
	 */
	public  void registerSms() {
		contentObsesrver = new ContentObserver(han) {
			@Override
			public void onChange(boolean selfChange) {
				super.onChange(selfChange);
				han.sendEmptyMessage(SMS_RECEIVED);
			}
		};
		context.getContentResolver().registerContentObserver(
				Uri.parse("content://sms"), true, contentObsesrver);
	}
	
	/**
	 * 注销手机验证码的监听
	 * 需要在Activity 的onDestroy方法中调用此方法
	 */
	public void unregisterSms(){
		if(contentObsesrver!=null){
			context.getContentResolver().unregisterContentObserver(contentObsesrver);	
		}
	}
	
	public interface SMSReceiveListener{
		/***
		 * 
		 * 接收到短信验证码调用此方法
		 * @param code   短信验证码
		 * @throw
		 * @return void
		 */
		public void onReceived(String code);
	}
	
	
	
	static class SmsAutoTextHandler extends Handler{

		WeakReference<Activity> act;
		WeakReference<SMSReceiveListener> lis;
		
		public SmsAutoTextHandler(Activity activity,SMSReceiveListener listener) {
			super();
			act=new WeakReference<Activity>(activity);
			lis=new WeakReference<SmsHandler.SMSReceiveListener>(listener);
		}


		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(msg.what==SMS_RECEIVED){
				/** 短信的验证码 */
				String codestr = null;
				try {
					codestr = SmsUtil.getsmsyzm(act.get());
					if(lis.get()!=null){
						lis.get().onReceived(codestr);
					}
					
				} catch (Exception e) {
					Log.e(TAG, "验证码提取失败");
					e.printStackTrace();
				}
			}
		}
	}
}

