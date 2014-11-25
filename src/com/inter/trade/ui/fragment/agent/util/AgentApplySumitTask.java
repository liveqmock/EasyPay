package com.inter.trade.ui.fragment.agent.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.widget.ArrayAdapter;

import com.inter.protocol.ProtocolData;
import com.inter.protocol.ProtocolRsp;
import com.inter.protocol.body.NetParser;
import com.inter.protocol.util.ProtocolUtil;
import com.inter.trade.R;
import com.inter.trade.data.BaseData;
import com.inter.trade.data.ResponseData;
import com.inter.trade.data.SunType;
import com.inter.trade.error.ErrorUtil;
import com.inter.trade.net.HttpUtil;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PromptUtil;

/**
 * 代理商在线申请，提交信息Task
 * @author Lihaifeng
 *
 */
public class AgentApplySumitTask extends AsyncTask<String, Integer, Boolean>{
	Context mContext;
	ProtocolRsp mRsp;
	String mApiName;
	String mApiFunc;
	SunType mdataReq;
	String mtagRep;
//	ArrayAdapter<String> madapter;
	AgentApplyInfoData mApplyData;
//	List<String> typeList;
	public AgentApplySumitTask(Context context, AgentApplyInfoData applyData, String ApiName, String ApiFunc, SunType dataReq, String tagRep){
		mContext=context;
		mApplyData=applyData;
//		typeList=spTypeList;
//		madapter=adapterProv;
		mApiName=ApiName;
		mApiFunc=ApiFunc;
		mdataReq=dataReq;
		mtagRep=tagRep;
	}
	
	@Override
	protected Boolean doInBackground(String... params) {
		// TODO Auto-generated method stub
		try {
			
//			CommonData data = new CommonData();
//			data.putValue("address",agentData.address);
//			data.putValue("phone",agentData.phone);
//			data.putValue("name",agentData.name);
			List<ProtocolData> mDatas = ProtocolUtil.getAgentApplyRequestDatas(mApiName, 
					mApiFunc, mdataReq, mApplyData);
			AgentApplySumitParser authorRegParser = new AgentApplySumitParser();
			mRsp = HttpUtil.doRequest(authorRegParser, mDatas);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			mRsp =null;
			PromptUtil.dissmiss();
		}
		return null;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		PromptUtil.dissmiss();
		if(mRsp==null){
			PromptUtil.showToast(mContext, mContext.getString(R.string.net_error));
		}else{
			try {
				List<ProtocolData> mDatas = mRsp.mActions;
				parserResoponse(mDatas, mtagRep);
				
				if(!ErrorUtil.create().errorDeal(LoginUtil.mLoginStatus,(Activity)mContext)){
					return;
				}
				if(LoginUtil.mLoginStatus.result.equals(ProtocolUtil.SUCCESS)){
					PromptUtil.showToast(mContext,LoginUtil.mLoginStatus.message);
					 FragmentManager manager = ((FragmentActivity)mContext).getSupportFragmentManager();
					 int len = manager.getBackStackEntryCount();
					 if(len>0){
						 manager.popBackStack(); 
					 }else{
						 ((FragmentActivity)mContext).finish();
					 }
				}
			} catch (Exception e) {
				PromptUtil.showToast(mContext,mContext.getString(R.string.req_error));
			}
		
		}
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
			PromptUtil.showDialog(mContext, mContext.getResources().getString(R.string.loading));
	}
	private void parserResoponse(List<ProtocolData> params, String tagRep){
		ResponseData response = new ResponseData();
		LoginUtil.mLoginStatus.mResponseData = response;
		for(ProtocolData data :params){
			if(data.mKey.equals(ProtocolUtil.msgheader)){
				ProtocolUtil.parserResponse(response, data);
			}else if(data.mKey.equals(ProtocolUtil.msgbody)){
				
				List<ProtocolData> result1 = data.find("/result");
				if(result1 != null){
//					mResult = result1.get(0).mValue;
					LoginUtil.mLoginStatus.result = result1.get(0).mValue;
				}
				
				List<ProtocolData> message = data.find("/message");
				if(message != null){
//					mMessage = message.get(0).mValue;
					LoginUtil.mLoginStatus.message = message.get(0).mValue;
				}
				
			}
		}
	}
}