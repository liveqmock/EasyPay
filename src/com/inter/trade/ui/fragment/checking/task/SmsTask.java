package com.inter.trade.ui.fragment.checking.task;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import com.inter.protocol.ProtocolData;
import com.inter.protocol.ProtocolRsp;
import com.inter.protocol.body.SmsCodeParser;
import com.inter.protocol.util.ProtocolUtil;
import com.inter.trade.R;
import com.inter.trade.ResponseMoreStateListener;
import com.inter.trade.data.CommonData;
import com.inter.trade.data.LoginStatus;
import com.inter.trade.data.ResponseData;
import com.inter.trade.data.SmsCodeData;
import com.inter.trade.error.ErrorUtil;
import com.inter.trade.net.HttpUtil;
import com.inter.trade.ui.fragment.waterelectricgas.data.ResponseStateListener;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PromptUtil;

/**
 *  获取短信验证码
 * @author  chenguangchi
 * @data:  2014年10月27日 上午10:01:33
 * @version:  V1.0
 */
public class SmsTask extends AsyncTask<String, Integer, String>{
		ProtocolRsp mRsp= null;
		
		private Context context ;
		
		private ResponseStateListener listener;  
		
		public SmsTask(Context context, ResponseStateListener listener) {
			super();
			this.context = context;
			this.listener = listener;
		}

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			CommonData  data  = new CommonData();
			data.putValue(ProtocolUtil.smsmobile, arg0[0]);
			List<ProtocolData> mDatas = ProtocolUtil.getNullReqTokenRequestDatas("ApiAuthorReg", "getSmsCode", data);
			
			
			mRsp = HttpUtil.doRequest( new SmsCodeParser(), mDatas);
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			PromptUtil.dissmiss();
			if(mRsp==null){
				PromptUtil.showToast(context,context.getString(R.string.net_error) );
			}else{
				try {
					List<ProtocolData> mDatas = mRsp.mActions;
					SmsCodeData smsCodedata = parserResponse(mDatas);
					
					if(!ErrorUtil.create().errorDeal(LoginUtil.mLoginStatus,(Activity)context)){
						if(listener instanceof ResponseMoreStateListener){
							((ResponseMoreStateListener) listener).onFailure(null, String.class);
						}
						return;
					}
					
					if(listener!=null){
						listener.onSuccess(smsCodedata, SmsCodeData.class);
					}
					
					
//					if(codeData.result.equals(ProtocolUtil.SUCCESS)){
//						getCodeSuccess();
//						isSmsInvilidate=false;
//					}else {
//						PromptUtil.showToast(context, codeData.message);
//					}
				} catch (Exception e) {
					e.printStackTrace();
					PromptUtil.showToast(context,context.getString(R.string.req_error));
				}
				
			}
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			PromptUtil.showDialog(context, "请稍候...");
		}
		

	
	private SmsCodeData parserResponse(List<ProtocolData> mDatas){
		SmsCodeData codeData = new SmsCodeData();
		ResponseData response = new ResponseData();
		codeData.mResponseData = response;
		LoginUtil.mLoginStatus.mResponseData = response;
		for(ProtocolData data :mDatas){
			if(data.mKey.equals(ProtocolUtil.msgheader)){
				
				ProtocolUtil.parserResponse(response, data);
				
			}else if(data.mKey.equals(ProtocolUtil.msgbody)){
				List<ProtocolData> result1 = data.find("/result");
				if(result1 != null){
					codeData.result = result1.get(0).mValue;
					LoginUtil.mLoginStatus.result = result1.get(0).mValue;
				}
				
				
				List<ProtocolData> message = data.find("/message");
				if(message != null){
					codeData.message = message.get(0).mValue;
					LoginUtil.mLoginStatus.message = message.get(0).mValue;
				}
				
				
				List<ProtocolData> smsmobile = data.find("/smsmobile");
				if(smsmobile != null){
					codeData.smsmobile = smsmobile.get(0).mValue;
				}
				
				
				List<ProtocolData> smscode = data.find("/smscode");
				if(smscode != null){
					codeData.smscode = smscode.get(0).mValue;
				}
				
			}
		}
		return codeData;
	}
}
