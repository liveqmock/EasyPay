package com.inter.trade.ui.fragment.agent.util;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.inter.protocol.ProtocolData;
import com.inter.protocol.ProtocolRsp;
import com.inter.protocol.body.NetParser;
import com.inter.protocol.util.ProtocolUtil;
import com.inter.trade.R;
import com.inter.trade.data.BaseData;
import com.inter.trade.data.CommonData;
import com.inter.trade.data.ResponseData;
import com.inter.trade.data.SunType;
import com.inter.trade.error.ErrorUtil;
import com.inter.trade.log.Logger;
import com.inter.trade.net.HttpUtil;
import com.inter.trade.util.Constants;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PreferenceConfig;
import com.inter.trade.util.PromptUtil;

/**
 * 代理商绑定，即激活码Task
 * @author Lihaifeng
 *
 */
public class AgentBindTask extends AsyncTask<String, Integer, Boolean>{
	private Context mContext;
	private ProtocolRsp mRsp;
	private String agentID;
	private String agentNo;
	private String agentArea;
	
	/**
	 * 3.0.0版本及以前，如未绑定代理商号，需要弹窗绑定，dialog不为空，
	 * 3.1.0版本以后，如未绑定代理商号，默认绑定总部“020001”，不再弹窗（仍需考虑网络问题，确保绑定成功），dialog为空
	 */
	DialogInterface dialog;
	
	public AgentBindTask(FragmentActivity context, String code, DialogInterface dialo){
		Logger.d("AgentBindTask", "task start");
		mContext=context;
		agentID=code;
		dialog=dialo;
	}
	
	@Override
	protected Boolean doInBackground(String... params) {
		// TODO Auto-generated method stub
		List<ProtocolData> mDatas = getRequestDatas();
		BindAgentParser authorRegParser = new BindAgentParser();
		mRsp = HttpUtil.doRequest(authorRegParser, mDatas);
		return null;
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
		// TODO Auto-generated method stub
		
		super.onPostExecute(result);
		try {
			PromptUtil.dissmiss();
			if(mRsp == null){
				if(dialog != null){
					PromptUtil.showToast(mContext, mContext.getString(R.string.net_error));
				}
			}else{
				try {
					List<ProtocolData> mDatas = mRsp.mActions;
					parserResoponse(mDatas);
					if(dialog != null){
						if(!ErrorUtil.create().errorDeal(LoginUtil.mLoginStatus,(Activity)mContext)){
							Logger.d("AgentBindTask", "task response failure");
							return;
						}
					}
					if(LoginUtil.mLoginStatus.result.equals(ProtocolUtil.SUCCESS)){
						if(dialog != null){
							PromptUtil.showToast(mContext, LoginUtil.mLoginStatus.message);
						}
						
						//手动更新为已绑定，后台接口更新后，自动更新绑定状态
						LoginUtil.mLoginStatus.relateAgent = "1";
						PreferenceConfig.instance(mContext).putString(Constants.IS_BIND_AGENT, "1");
						
						if(dialog != null) {
							try {
								
								Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
								field.setAccessible(true);
								field.set(dialog, true);
							} catch (Exception e) {
								e.printStackTrace();
							}
							dialog.dismiss();
						}
						Logger.d("AgentBindTask", "task response success");
						
						
//						(FragmentActivity)mContext.finish();
						
					}else {
						if(dialog != null){
							PromptUtil.showToast(mContext, LoginUtil.mLoginStatus.message);
						}
						Logger.d("AgentBindTask", "task response failure");
					}
				} catch (Exception e) {
					e.printStackTrace();
					if(dialog != null){
						PromptUtil.showToast(mContext, mContext.getString(R.string.req_error));
					}
				}
				
			}
		} catch (Exception e) {
		}
		
		
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if(dialog != null) {
			PromptUtil.showDialog(mContext, mContext.getResources().getString(R.string.loading));
		}
	}
	
	private List<ProtocolData> getRequestDatas(){
		CommonData data = new CommonData();
		
//		data.putValue("querytype", "");//查询类型
		data.putValue("agentno", agentID);//代理商代号
		
		List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas("ApiAgentInfo", "authorBindAgent", data);
		
		
		return mDatas;
	}
	
	
	/**
	 * 解析响应体
	 * @param params
	 */
	private void parserResoponse(List<ProtocolData> params){
		ResponseData response = new ResponseData();
		LoginUtil.mLoginStatus.mResponseData = response;
		for(ProtocolData data :params){
			if(data.mKey.equals(ProtocolUtil.msgheader)){
				ProtocolUtil.parserResponse(response, data);
				
			}else if(data.mKey.equals(ProtocolUtil.msgbody)){
				List<ProtocolData> result1 = data.find("/result");
				if(result1 != null){
					LoginUtil.mLoginStatus.result = result1.get(0).mValue;
				}
				
				
				List<ProtocolData> message = data.find("/message");
				if(message != null){
					LoginUtil.mLoginStatus.message = message.get(0).mValue;
				}
				
				List<ProtocolData> authorid = data.find("/authorid");
				if(authorid != null){
					LoginUtil.mLoginStatus.authorid = authorid.get(0).mValue;
				}
				
				List<ProtocolData> agentid = data.find("/agentid");
				if(agentid != null){
					LoginUtil.mLoginStatus.agentid = agentid.get(0).mValue;
				}
				
				List<ProtocolData> relateAgent = data.find("/relateAgent");
				if(relateAgent != null){
					LoginUtil.mLoginStatus.relateAgent = relateAgent.get(0).mValue;
				}
				
				List<ProtocolData> ispaypwd = data.find("/ispaypwd");
				if(ispaypwd != null){
					LoginUtil.mLoginStatus.ispaypwd = ispaypwd.get(0).mValue;
				}
				
				List<ProtocolData> gesturepasswd = data.find("/gesturepasswd");
				if(gesturepasswd != null){
					LoginUtil.mLoginStatus.gesturepasswd = gesturepasswd.get(0).mValue;
				}
			}
		}
	}
}