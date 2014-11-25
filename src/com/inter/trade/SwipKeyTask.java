package com.inter.trade;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;

import com.inter.protocol.ProtocolData;
import com.inter.protocol.ProtocolRsp;
import com.inter.protocol.body.KeyParser;
import com.inter.protocol.util.ProtocolUtil;
import com.inter.trade.AsyncLoadWork.AsyncLoadWorkListener;
import com.inter.trade.data.CommonData;
import com.inter.trade.data.KeyData;
import com.inter.trade.data.LoginStatus;
import com.inter.trade.data.ResponseData;
import com.inter.trade.net.HttpUtil;
import com.inter.trade.ui.creditcard.data.DefaultBankCardData;
import com.inter.trade.ui.fragment.waterelectricgas.data.ResponseStateListener;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PromptUtil;

/**
 * 验证刷卡器
 * @author apple
 *
 */
public class SwipKeyTask extends AsyncTask<String, Integer, Boolean>{
	ProtocolRsp mRsp= null;
	FragmentActivity mActivity;
	private String mResult;
	private String mMessage;
	public boolean isUseable=false;
	public String mKey;
	private KeyData mKeyData;
	private  Handler mHandler;
	
	private String mbkcardno;
	private String mpaytype;
	AsyncLoadWorkListener mlistener;
	
	/**
	 * 限制银行
	 */
	private String readmode;
	
	public SwipKeyTask(FragmentActivity temp,String key,KeyData keyData){
		mActivity = temp;
		mKey = key;
		this.mKeyData = keyData;
		mHandler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				if(msg.what==0){
					dialog.dismiss();
					dialog=null;
				}
			}
			
		};
	}
	
	/**
	 * 
	 * @param temp
	 * @param key
	 * @param keyData
	 * @param bkcardno 银行卡号
	 * @param paytype 交易类型
	 * @param listener 异步监听
	 */
	public SwipKeyTask(FragmentActivity temp,String key,KeyData keyData, String bkcardno, String paytype, AsyncLoadWorkListener listener){
		mActivity = temp;
		mKey = key;
		this.mKeyData = keyData;
		
		mbkcardno = bkcardno;
		mpaytype = paytype;
		mlistener = listener;
		
		mHandler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				if(msg.what==0){
					dialog.dismiss();
					dialog=null;
				}
			}
			
		};
	}
	
	/**
	 * 
	 * @param temp
	 * @param key
	 * @param keyData
	 * @param bkcardno 银行卡号
	 * @param paytype 交易类型
	 * @param listener 异步监听
	 */
	public SwipKeyTask(FragmentActivity temp,String key,KeyData keyData, String bkcardno, String paytype, AsyncLoadWorkListener listener, String mreadmode){
		mActivity = temp;
		mKey = key;
		this.mKeyData = keyData;
		
		mbkcardno = bkcardno;
		mpaytype = paytype;
		mlistener = listener;
		readmode = mreadmode;
		
		mHandler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				if(msg.what==0){
					dialog.dismiss();
					dialog=null;
				}
			}
			
		};
	}
	
	@Override
	protected Boolean doInBackground(String... arg0) {
		// TODO Auto-generated method stub
		List<ProtocolData> mDatas = new ArrayList<ProtocolData>();
		CommonData data = new CommonData();
		data.sunMap.put("paycardkey", mKey);
		data.sunMap.put("authorid", LoginUtil.mLoginStatus.authorid);
		
		if(mbkcardno != null && !mbkcardno.equals("")){//银行卡号
			data.sunMap.put("bkcardno", mbkcardno);
		}
		if(mpaytype != null && !mpaytype.equals("")){//交易类型
			data.sunMap.put("paytype", mpaytype);
		}
		
		//限制银行
		if(readmode != null && !readmode.equals("")) {
			data.sunMap.put("readmode", readmode);
		}
		
		mDatas = ProtocolUtil.getRequestDatas("ApiAuthorInfo",
								"payCardCheck", data);
		
		
		KeyParser versionParser = new KeyParser();
		mRsp = HttpUtil.doRequest(versionParser, mDatas);
		try {
			Thread.sleep(1000);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}
	
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
//		PromptUtil.showDialog(mActivity, "正在验证刷卡器...");
		showDialog(mActivity, "正在验证刷卡器...");
	}
	@Override
	protected void onPostExecute(Boolean result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
//		PromptUtil.dissmiss();
		mHandler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				mHandler.sendEmptyMessage(0);
			}
		}, 2000);
		if(mRsp==null){
			mKeyData.mType=2;
			mKeyData.message = "刷卡器验证失败，请检查网络";
			showQuit(mActivity, "刷卡器验证失败，请检查网络");
//			PromptUtil.showToast(mActivity, "刷卡器验证失败，请检查网络");
		}else{
			try {
				List<ProtocolData> mDatas = mRsp.mActions;
				DefaultBankCardData card = parserResponse(mDatas);
				
				if(!errorDeal(LoginUtil.mLoginStatus,mActivity)){
					mKeyData.mType=1;
					mKeyData.message = LoginUtil.mLoginStatus.mResponseData.getRetmsg();
					if(mlistener != null) {
						mlistener.onFailure("error");
					}
					return;
				}else {
					mKeyData.mType=0;
					mKeyData.message = LoginUtil.mLoginStatus.mResponseData.getRetmsg();
				}
				
				if(LoginUtil.mLoginStatus.result.equals(ProtocolUtil.SUCCESS)){
					if(mlistener != null) {
						mlistener.onSuccess(card, null);
					}
				} else{
					if(mlistener != null) {
						mlistener.onFailure("error");
					}
				}
				
				
				
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
//				PromptUtil.showToast(mActivity,mActivity.getResources().getString(R.string.net_error));
				mKeyData.mType=2;
				mKeyData.message = "刷卡器验证失败，请检查网络";
				showQuit(mActivity, "刷卡器验证失败，请检查网络");
				if(mlistener != null) {
					mlistener.onFailure("error");
				}
			}
		}
	}
	
	public  static ProgressDialog dialog;
	public  static void  showDialog(final Activity context,String text) {
		if(dialog != null
				){
			dialog.setMessage(text);
		}else{
			dialog = new ProgressDialog(context);
			dialog.setCancelable(false);
//			dialog.setButton("取消", new OnClickListener() {
//				
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//					// TODO Auto-generated method stub
//					dialog.dismiss();
//					context.finish();
//					dialog=null;
//				}
//			});
			dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			if(text!= null && !"".equals(text)){
				dialog.setMessage(text);
			}
		}
		
		dialog.show();
	}
	
	private DefaultBankCardData parserResponse(List<ProtocolData> mDatas){
		ResponseData response = new ResponseData();
		LoginUtil.mLoginStatus.mResponseData=response;
		DefaultBankCardData card=new DefaultBankCardData();
		for(ProtocolData data :mDatas){
			if(data.mKey.equals(ProtocolUtil.msgheader)){
				ProtocolUtil.parserResponse(response, data);
				
			}else if(data.mKey.equals(ProtocolUtil.msgbody)){
				
				List<ProtocolData> result = data.find("/result");
				if(result != null){
					LoginUtil.mLoginStatus.result = result.get(0).mValue;
				}
				
				List<ProtocolData> message = data.find("/apptype");
				if(message != null){
					LoginUtil.mLoginStatus.message = message.get(0).mValue;
				}
				
				List<ProtocolData> bkcardno = data.find("/bkcardno");
				if (bkcardno != null) {
					card.setBkcardno(bkcardno.get(0).mValue);
				}
				List<ProtocolData> bkcardman = data.find("/bkcardman");
				if (bkcardman != null) {
					card.setBkcardbankman(bkcardman.get(0).mValue);
				}
				List<ProtocolData> bkcardphone = data.find("/bkcardphone");
				if (bkcardphone != null) {
					card.setBkcardbankphone(bkcardphone.get(0).mValue);
				}
				List<ProtocolData> bkcardbankid = data.find("/bkcardbankid");
				if (bkcardbankid != null) {
					card.setBkcardbankid(bkcardbankid.get(0).mValue);
				}
				List<ProtocolData> bkcardbankname = data.find("/bkcardbankname");
				if (bkcardbankname != null) {
					card.setBkcardbank(bkcardbankname.get(0).mValue);
				}
				List<ProtocolData> bkcardyxmonth = data.find("/bkcardyxmonth");
				if (bkcardyxmonth != null) {
					card.setBkcardyxmonth(bkcardyxmonth.get(0).mValue);
				}
				List<ProtocolData> bkcardyxyear = data.find("/bkcardyxyear");
				if (bkcardyxyear != null) {
					card.setBkcardyxyear(bkcardyxyear.get(0).mValue);
				}
				List<ProtocolData> bkcardcvv = data.find("/bkcardcvv");
				if (bkcardcvv != null) {
					card.setBkcardcvv(bkcardcvv.get(0).mValue);
				}
				List<ProtocolData> bkcardidcard = data.find("/bkcardidcard");
				if (bkcardidcard != null) {
					card.setBkcardidcard(bkcardidcard.get(0).mValue);
				}
				List<ProtocolData> bkcardtype = data.find("/bkcardtype");
				if (bkcardtype != null) {
					card.setBkcardcardtype(bkcardtype.get(0).mValue);
				}
				List<ProtocolData> bkcardbankno = data.find("/bkcardbankno");
				if (bkcardbankno != null) {
					card.setBkcardbankcode(bkcardbankno.get(0).mValue);
				}
			}
		}
		return card;
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
			
			if(result.equals(ProtocolUtil.SUCCESS)){
//				showQuit(mActivity, retMsg);
				dialog.setMessage(retMsg);
				return true;
			}else{
				dialog.setMessage(retMsg);
//				showQuit(mActivity, retMsg);
				return false;
			}
		}else if(retCode.equals(ProtocolUtil.REAL_ERROR)){
			PromptUtil.showRealFail(activity, retMsg);
			return false;
			
		}else{
//			PromptUtil.showToast(PayApp.pay, retMsg);
//			showQuit(mActivity, retMsg);
			dialog.setMessage(retMsg);
			return false;
		}
	}
	public static void showQuit(Context context,String message){
		new AlertDialog.Builder(context).setMessage(message)
		.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				arg0.dismiss();
			}
		})
		.show();
	}
}
