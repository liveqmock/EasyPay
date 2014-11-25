package com.inter.trade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.inter.protocol.ProtocolData;
import com.inter.protocol.ProtocolRsp;
import com.inter.protocol.body.NetParser;
import com.inter.protocol.util.ProtocolUtil;
import com.inter.trade.data.SunType;
import com.inter.trade.error.ErrorUtil;
import com.inter.trade.net.HttpUtil;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PromptUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * 统一的异步加载工作
 * 
 * @author zhichao.huang
 *
 * @param <T> 该泛型应该对应NetParser的泛型，主要用于在NetParser的public ArrayList<T> parserResponseDatas(List<ProtocolData> mDatas, Bundle bundle)
 * 获取对应的结果数据
 */
public class AsyncLoadWork<T> extends AsyncTask<String, Integer, ProtocolRsp> implements IAsyncLoadWorkInterface<T>{
	
	private ProtocolRsp mRsp = null;
	
	/**
	 * API接口名,对应doInBackground(String... params)的params[0]参数
	 */
	private String apiName;
	
	/**
	 * API函数名（方法），对应doInBackground(String... params)的params[1]参数
	 */
	private String apiFunc;
	
	/**
	 * 数据解析
	 */
	private NetParser<T> parser;
	
	/**
	 * 数据模型
	 */
	private SunType sunTypeData;
	
	/**
	 * 异步加载侦听
	 */
	private AsyncLoadWorkListener asyncWorkListener;
	
	private Activity mContext;
	
	/**
	 * 加载的时候是否显示加载框，默认false 
	 */
	private boolean _isBackgroud = false;
	
	/**
	 * 当请求失败后是否弹框提示用户再次加载 默认false（不重新加载）
	 */
	private boolean _isAgainLoad = false;
	
	/**
	 * 请求响应的错误提示是否用Dialog提示；true:Dialog; false:toast 默认
	 */
	private boolean errorWithDialog = false;
	
	/**
	 * 用于存储附带信息，如（下拉加载）加载更多的字段，或数据的总条数
	 */
	private Bundle bundle = null;
	
	/**
	 * 请求响应的错误200错误是否用toast提示  true:提示，false 不提示
	 */
	private boolean isShow200Toast =true;
	
	/**
	 * 
	 * @param netParser 数据解析
	 * @param data 数据模型
	 * @param asyncLoadWorkListener 数据监听
	 */
	public AsyncLoadWork (NetParser<T> netParser, SunType data, 
			AsyncLoadWorkListener asyncLoadWorkListener) {
		parser = netParser;
		sunTypeData = data;
		asyncWorkListener = asyncLoadWorkListener;
		
		bundle = new Bundle();
	}
	
	/**
	 * 
	 * @param context Context
	 * @param netParser 数据解析
	 * @param data 数据模型
	 * @param asyncLoadWorkListener 数据监听
	 */
	public AsyncLoadWork (Activity context, NetParser<T> netParser, SunType data, 
			AsyncLoadWorkListener asyncLoadWorkListener) {
		this(netParser, data, asyncLoadWorkListener);
		mContext = context;
	}
	
	/**
	 * 
	 * @param context Context
	 * @param netParser 数据解析
	 * @param data 数据模型
	 * @param asyncLoadWorkListener 数据监听
	 * @param isBackgroud 加载的时候是否显示加载框，默认false （显示加载框）
	 * @param isAgainLoad 当请求失败后是否弹框提示用户再次加载 默认false（不重新加载）
	 */
	public AsyncLoadWork (Activity context, NetParser<T> netParser, SunType data, 
			AsyncLoadWorkListener asyncLoadWorkListener, boolean isBackgroud, boolean isAgainLoad) {
		this(context, netParser, data, asyncLoadWorkListener);
		_isBackgroud = isBackgroud;
		_isAgainLoad = isAgainLoad;
	}
	
	/**
	 * 
	 * @param context Context
	 * @param netParser 数据解析
	 * @param data 数据模型
	 * @param asyncLoadWorkListener 异步数据监听
	 * @param isBackgroud 加载的时候是否显示加载框，默认false （显示加载框）
	 * @param isAgainLoad 当请求失败后是否弹框提示用户再次加载 默认false（不重新加载）
	 * @param dealErrorWithDialog 请求响应的错误提示是否用Dialog提示；true:Dialog; false:toast 默认
	 */
	public AsyncLoadWork (Activity context, NetParser<T> netParser, SunType data, 
			AsyncLoadWorkListener asyncLoadWorkListener, boolean isBackgroud, boolean isAgainLoad, boolean dealErrorWithDialog) {
		this(context, netParser, data, asyncLoadWorkListener, isBackgroud, isAgainLoad);
		errorWithDialog = dealErrorWithDialog;
	}
	
	
	/**
	 * 
	 * @param context Context
	 * @param netParser 数据解析
	 * @param data 数据模型
	 * @param asyncLoadWorkListener 异步数据监听
	 * @param isBackgroud 加载的时候是否显示加载框，默认false （显示加载框）
	 * @param isAgainLoad 当请求失败后是否弹框提示用户再次加载 默认false（不重新加载）
	 * @param dealErrorWithDialog 请求响应的错误提示是否用Dialog提示；true:Dialog; false:toast 默认
	 * @param isShow200Toast 请求响应的错误200错误是否用toast提示  true:提示，false 不提示
	 */
	public AsyncLoadWork (Activity context, NetParser<T> netParser, SunType data, 
			AsyncLoadWorkListener asyncLoadWorkListener, boolean isBackgroud, boolean isAgainLoad, boolean dealErrorWithDialog,boolean isShow200Toast) {
		this(context, netParser, data, asyncLoadWorkListener, isBackgroud, isAgainLoad,dealErrorWithDialog);
		this.isShow200Toast=isShow200Toast;
	}

	/**
	 *  接口名 apiName > params[0]
	 *  函数名 apiFunc > params[1]
	 */
	@Override
	protected ProtocolRsp doInBackground(String... params) {
		try {
			apiName = params[0];
			apiFunc = params[1];
			if( checkRequestParam () ) {
				mRsp = HttpUtil.doRequest(parser, ProtocolUtil.getCustomRequestDatas(apiName, apiFunc, sunTypeData));
			} else {
				PromptUtil.showToast(mContext, "请求参数出现异常");
				asyncWorkListener.onFailure("request param error");
			}
		} catch (Exception e) {
			e.printStackTrace();
			mRsp =null;
			return mRsp;
		}
		
		return mRsp;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if(mContext == null) return;
		if(!_isBackgroud)
			PromptUtil.showDialog(mContext, mContext.getResources().getString(R.string.loading));
	}

	@Override
	protected void onPostExecute(ProtocolRsp result) {
		super.onPostExecute(result);
		PromptUtil.dissmiss();
		ArrayList<T> responseDatas = null;
		if(result != null) {
			responseDatas = parser.parserResponseDatas(result.mActions, bundle);
			
//			LoginUtil.mLoginStatus.mResponseData.setRetcode("300");
//			if(!ErrorUtil.create().errorDeal(LoginUtil.mLoginStatus, mContext, 
//					loginTimeoutListener != null ? loginTimeoutListener : defaultLoginTimeoutListener)) {
//				asyncWorkListener.onFailure("error");
//				return;
//			}
			if(errorWithDialog) {
				if(!ErrorUtil.create().dealErrorWithDialog(LoginUtil.mLoginStatus,mContext)){
					asyncWorkListener.onFailure("error");
					return;
				}
				
			} else {
				
				if(isShow200Toast){
					if(!ErrorUtil.create().errorDeal(LoginUtil.mLoginStatus,mContext)){
						asyncWorkListener.onFailure("error");
						return;
					}
				}else{
					if(!ErrorUtil.create().dealError(LoginUtil.mLoginStatus,mContext)){
						asyncWorkListener.onFailure("error");
						return;
					}
				}
			}
			
			if(LoginUtil.mLoginStatus.result.equals(ProtocolUtil.SUCCESS)){
				asyncWorkListener.onSuccess(responseDatas, bundle);
			} else{
				PromptUtil.showToast(mContext, LoginUtil.mLoginStatus.message);
			}
			
		}else{
			if(!_isAgainLoad)
				PromptUtil.showToast(mContext, mContext.getString(R.string.net_error));
			asyncWorkListener.onFailure("result is null");
			if(_isAgainLoad) {
				showAgainLoadDialog ();
			}
		}
		
	}
	
	/**
	 * 校验请求参数
	 * 
	 * @return
	 */
	private boolean checkRequestParam () {
		if(apiName == null || apiName.equals("") 
				|| apiFunc == null || apiFunc.equals("")) {
			return false;
		}
		if(parser == null || sunTypeData == null) return false;
		
		return true;
	}
	
	private void showAgainLoadDialog () {
		new AlertDialog.Builder(mContext).setTitle("网络异常").setMessage("网络可能出现异常是否重试?")
		.setPositiveButton("重试", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				agenRunAsyncLoadWork ();
			}
		}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		}).setCancelable(false).show();
	}
	
	/**
	 * 再次执行异步加载操作
	 */
	private AsyncLoadWork<T> asyncLoadWork = null;
	private void agenRunAsyncLoadWork () {
		if(checkRequestParam ()) {
			if(!this.isCancelled()) {
				this.cancel(true);
			}
			asyncLoadWork = new AsyncLoadWork<T>(mContext, parser, sunTypeData, asyncWorkListener, _isBackgroud, _isAgainLoad, errorWithDialog);
			asyncLoadWork.execute(apiName, apiFunc);
		}
		
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
	}

//	@Override
//	protected void onCancelled(Boolean result) {
//		super.onCancelled(result);
//	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
	}
	
	/** 
     * 网络是否可用 
     *  
     * @param context 
     * @return 
     */  
    public static boolean isNetworkAvailable(Context context) {  
        ConnectivityManager mgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);  
        NetworkInfo[] info = mgr.getAllNetworkInfo();  
        if (info != null) {  
            for (int i = 0; i < info.length; i++) {  
                if (info[i].getState() == NetworkInfo.State.CONNECTED) {  
                    return true;  
                }  
            }  
        }  
        return false;  
    }  
	
	/**
	 * 默认的登录超时侦听
	 */
	private LoginTimeoutListener defaultLoginTimeoutListener = new LoginTimeoutListener() {
		
		/**
		 * serialVersionUID
		 */
		private static final long serialVersionUID = 7775473006495660392L;

		@Override
		public void onTimeout() {
			agenRunAsyncLoadWork ();
		}

	};
	
	/**
	 * 该接口用于监听加载数据是否成功，作一个回调
	 * 
	 * @author zhichao.huang
	 *
	 */
	public interface AsyncLoadWorkListener {
		
		/**
		 * 数据加载成功
		 * 
		 * @param protocolDataList 加载的数据
		 * @param bundle 用于存储附带信息，如（下拉加载）加载更多的字段，或数据的总条数
		 */
		void onSuccess(Object protocolDataList, Bundle bundle);
		
		/**
		 * 数据加载失败
		 */
		void onFailure(String error);
		
	}
	
	/**
	 * 登录超时侦听
	 * 
	 * @author zhichao.huang
	 *
	 */
	public interface LoginTimeoutListener extends Serializable {
		
		/**
		 * 超时
		 */
		void onTimeout ();
		
	}
	

}
