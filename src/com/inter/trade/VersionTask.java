package com.inter.trade;

import java.util.List;

import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;

import com.inter.protocol.ProtocolData;
import com.inter.protocol.ProtocolRsp;
import com.inter.protocol.body.VersionParser;
import com.inter.protocol.util.ProtocolUtil;
import com.inter.trade.data.CommonData;
import com.inter.trade.data.LoginStatus;
import com.inter.trade.data.ResponseData;
import com.inter.trade.data.VersionData;
import com.inter.trade.error.ErrorUtil;
import com.inter.trade.log.Logger;
import com.inter.trade.net.HttpUtil;
import com.inter.trade.util.Constants;
import com.inter.trade.util.PreferenceConfig;
import com.inter.trade.util.PromptUtil;
import com.inter.trade.util.VersionUtil;

/**
 * 版本更新接口
 * @author apple
 *
 */
public class VersionTask extends AsyncTask<String, Integer, Boolean>{
	ProtocolRsp mRsp= null;
	FragmentActivity mActivity;
	private VersionData codeData;
	private LoginStatus mLoginStatus=new LoginStatus();
	private boolean isBackground=false;
	/**
	 * 是否reqtolen为“”；
	 */
	private boolean reqtokennull;
	
	public VersionTask(FragmentActivity temp,boolean background,boolean reqtokennull){
		mActivity = temp;
		isBackground = background;
		this.reqtokennull=reqtokennull;
	}
	
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		if(!isBackground){
			PromptUtil.showDialog(mActivity, "正在检测最新版本，请稍后...");
			PromptUtil.dialog.setCancelable(false);
		}
		
	}

	@Override
	protected Boolean doInBackground(String... arg0) {
		// TODO Auto-generated method stub
		CommonData data = new CommonData();
		data.putValue("apptype", "1");
		data.putValue("appversion",  VersionUtil.getVersionName(mActivity));
		List<ProtocolData> mDatas =null;
		if(reqtokennull){
			mDatas = ProtocolUtil.getNullReqTokenRequestDatas("ApiAppInfo", "checkAppVersion", data);
		}else{
			mDatas = ProtocolUtil.getRequestDatas("ApiAppInfo", "checkAppVersion", data);
		}
		
		
		
		VersionParser versionParser = new VersionParser();
		
		mRsp = HttpUtil.doRequest(versionParser, mDatas);
		return null;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		PromptUtil.dissmiss();
		if(mRsp==null){
			if(!isBackground){
				PromptUtil.showToast(mActivity, mActivity.getString(R.string.net_error));
			}
			
		}else{
			try {
				List<ProtocolData> mDatas = mRsp.mActions;
				parserResponse(mDatas);
				Logger.d("login","版本："+mLoginStatus.mResponseData.getReq_token());
				if(!isBackground)
				if(!ErrorUtil.create().errorDeal(mLoginStatus,mActivity)){
					return;
				}
				
				if(codeData.result.equals(ProtocolUtil.SUCCESS)){
					PreferenceConfig.instance(mActivity).putString(Constants.VERSION_KEY, codeData.appnewversion);
					PreferenceConfig.instance(mActivity).putString(Constants.VERSION_MSUT_UPDATE, codeData.appisnew);
					if(null != codeData.appdownurl && !"".equals(codeData.appdownurl)){
						PreferenceConfig.instance(mActivity).putString(Constants.VERSION_UPDATE_URL, codeData.appdownurl);
					}
					if(codeData.appisnew.equals("1")){
						String message = "1,版本号："+codeData.appnewversion;
						message +="\n2,更新内容："+codeData.appnewcontent;
						PreferenceConfig.instance(mActivity).putString(Constants.VERSION_UPDATE_MESSAGE, message);
						VersionUtil.showDialog(mActivity,message, codeData.appdownurl);
					}else {
						if(!isBackground)
						PromptUtil.showToast(mActivity, codeData.message);
					}
				}else {
					if(!isBackground)
					PromptUtil.showToast(mActivity, codeData.message);
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				if(!isBackground)
				PromptUtil.showToast(mActivity,mActivity.getResources().getString(R.string.req_error));
			}
			
		}
	}
	

	
	private void parserResponse(List<ProtocolData> mDatas){
		codeData = new VersionData();
		ResponseData response = new ResponseData();
		codeData.mData = response;
		mLoginStatus.mResponseData = response;
		for(ProtocolData data :mDatas){
			if(data.mKey.equals(ProtocolUtil.msgheader)){
				ProtocolUtil.parserResponse(response, data);
				
				/*
				 * 更新动态码到 LoginUtil.mLoginStatus.mResponseData.au_token
				 */
//				LoginUtil.mLoginStatus.mResponseData.setAu_token(mLoginStatus.mResponseData.getAu_token());

			}else if(data.mKey.equals(ProtocolUtil.msgbody)){
				
				List<ProtocolData> result = data.find("/result");
				if(result != null){
					codeData.result = result.get(0).mValue;
					mLoginStatus.result = result.get(0).mValue;
				}
				
				List<ProtocolData> message = data.find("/message");
				if(message != null){
					codeData.message = message.get(0).mValue;
					mLoginStatus.message = message.get(0).mValue;
				}
				
				List<ProtocolData> apptype = data.find("/apptype");
				if(apptype != null){
					codeData.apptype = apptype.get(0).mValue;
				}
				
				List<ProtocolData> appisnew = data.find("/appisnew");
				Logger.d("VersionTask", "codeData.appisnew"+appisnew);
				if(appisnew != null){
					codeData.appisnew = appisnew.get(0).mValue;
					Logger.d("VersionTask", "codeData.appisnew"+codeData.appisnew);
				}
				List<ProtocolData> appreversion = data.find("/appnewversion");
				if(appreversion != null){
					codeData.appnewversion = appreversion.get(0).mValue;
				}
				
				List<ProtocolData> clearoldinfo = data.find("/clearoldinfo");
				if(clearoldinfo != null){
					codeData.clearoldinfo = clearoldinfo.get(0).mValue;
				}
				
				List<ProtocolData> appdownurl = data.find("/appdownurl");
				if(appdownurl != null){
					codeData.appdownurl = appdownurl.get(0).mValue;
				}
				
				List<ProtocolData> appnewcontent = data.find("/appnewcontent");
				if(appnewcontent != null){
					codeData.appnewcontent = appnewcontent.get(0).mValue;
				}
				
				List<ProtocolData> appstrupdate = data.find("/appstrupdate");
				if(appstrupdate != null){
					codeData.appstrupdate = appstrupdate.get(0).mValue;
				}
			}
		}
	}
}
