package com.inter.trade;

import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;

import com.inter.protocol.ProtocolData;
import com.inter.protocol.ProtocolParser;
import com.inter.protocol.ProtocolRsp;
import com.inter.protocol.body.SmsCodeParser;
import com.inter.protocol.util.ProtocolUtil;
import com.inter.trade.data.CommonData;
import com.inter.trade.data.ResponseData;
import com.inter.trade.data.SmsCodeData;
import com.inter.trade.error.ErrorUtil;
import com.inter.trade.log.Logger;
import com.inter.trade.net.HttpUtil;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PromptUtil;

/**
 * 获取手机号码随机短信效验码。
 * @author apple
 *
 */
public class SendSmsTask extends AsyncTask<String, Integer, Boolean>{
	ProtocolRsp mRsp= null;
	FragmentActivity mActivity;
	String mPhone ="";
	private SmsCodeData codeData;
	public SendSmsTask(FragmentActivity temp,String phone){
		mActivity = temp;
		mPhone = phone;
	}
	@Override
	protected Boolean doInBackground(String... arg0) {
		// TODO Auto-generated method stub
		CommonData data = new CommonData();
		data.putValue(ProtocolUtil.smsmobile, mPhone);
		List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas("ApiSendSms", 
				"getSmsCode", data);
		
		mRsp = HttpUtil.doRequest(new SmsCodeParser(), mDatas);
		return null;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		PromptUtil.dissmiss();
		if(mRsp==null){
			PromptUtil.showToast(mActivity, mActivity.getResources().getString(R.string.net_error));
		}else{
			try {
				List<ProtocolData> mDatas = mRsp.mActions;
				parserResponse(mDatas);
				
				if(!ErrorUtil.create().errorDeal(LoginUtil.mLoginStatus,mActivity)){
					return;
				}
				if(codeData.result.equals(ProtocolUtil.SUCCESS)){
				}else {
					PromptUtil.showToast(mActivity, codeData.message);
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				PromptUtil.showToast(mActivity,mActivity.getResources().getString(R.string.req_error));
			}
			
		}
	}
	
	
	private void parserResponse(List<ProtocolData> mDatas){
		codeData = new SmsCodeData();
		ResponseData response = new ResponseData();
		codeData.mResponseData = response;
		LoginUtil.mLoginStatus.mResponseData = response;
		
		for(ProtocolData data :mDatas){
			if(data.mKey.equals(ProtocolUtil.msgheader)){
				ProtocolUtil.parserResponse(response, data);
//				List<ProtocolData> req_seq = data.find("/req_seq");
//				if(req_seq!=null){
//					response.setReq_seq(req_seq.get(0).mValue);
//				}
//				
//				
//				List<ProtocolData> ope_seq = data.find("/ope_seq");
//				if(ope_seq!=null){
//					response.setOpe_seq(ope_seq.get(0).mValue);
//				}
//			
//				
//				List<ProtocolData> rettype = data.find("/retinfo/rettype");
//				if(rettype!=null){
//					response.setRettype(rettype.get(0).mValue);
//				}
//				
//				List<ProtocolData> retcode = data.find("/retinfo/retcode");
//				if(retcode!=null){
//					response.setRetcode(retcode.get(0).mValue);
//				}
//			
//				
//				List<ProtocolData> retmsg = data.find("/retinfo/retmsg");
//				if(retmsg!=null){
//					response.setRetmsg(retmsg.get(0).mValue);
//				}
//				
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
	}
}
