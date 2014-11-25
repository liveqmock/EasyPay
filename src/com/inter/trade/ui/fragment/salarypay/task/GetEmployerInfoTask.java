/*
 * @Title:  GameInfoTask.java
 * @Copyright:  XXX Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * @Description:  TODO<请描述此文件是做什么的>
 * @author:  MeiYi
 * @data:  2014年6月25日 上午11:53:40
 * @version:  V1.0
 */
package com.inter.trade.ui.fragment.salarypay.task;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;

import com.inter.protocol.ProtocolData;
import com.inter.protocol.ProtocolRsp;
import com.inter.protocol.util.ProtocolUtil;
import com.inter.trade.R;
import com.inter.trade.data.CommonData;
import com.inter.trade.data.ResponseData;
import com.inter.trade.error.ErrorUtil;
import com.inter.trade.net.HttpUtil;
import com.inter.trade.ui.fragment.salarypay.bean.EmployerData;
import com.inter.trade.ui.fragment.salarypay.parser.GetEmployerInfoParser;
import com.inter.trade.ui.fragment.waterelectricgas.data.ResponseStateListener;
import com.inter.trade.ui.func.task.ModleTask;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PromptUtil;

/**
 * 根据财务人员的账号来获取绑定的财务人员的信息
 * @author  ChenGuangChi
 * @data:  2014年6月25日 上午11:53:40
 * @version:  V1.0
 */
public class GetEmployerInfoTask extends AsyncTask<String, Void, Void> {
	
	private Context context;
	private ResponseStateListener listener;
	
	private String[] params;
	
	public GetEmployerInfoTask(Context context, ResponseStateListener listener) {
		super();
		this.context = context;
		this.listener = listener;
	}

	ProtocolRsp mRsp;
	@Override
	protected Void doInBackground(String... params) {
		this.params=params;
		try {
			CommonData mData = new CommonData();
			mData.putValue("phone", params[0]);
			
			List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas("ApiWageInfo", 
					"GetStaffInfo", mData);
			GetEmployerInfoParser myParser = new GetEmployerInfoParser();
			mRsp = HttpUtil.doRequest(myParser, mDatas);
			} catch (Exception e) {
				e.printStackTrace();
				mRsp =null;
			}
			return null;
	}
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		//PromptUtil.showDialog(context, context.getResources().getString(R.string.loading));
	}
	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		//PromptUtil.dissmiss();
		if(mRsp==null){
			//PromptUtil.showToast(context, context.getString(R.string.net_error));
			showAgainLoadDialog();
		}else{
			try {
				List<ProtocolData> mDatas = mRsp.mActions;
				EmployerData data = parserResponse(mDatas);
				
				if(!ErrorUtil.create().errorDeal(LoginUtil.mLoginStatus,(Activity)context)){
					return;
				}
				
				if(listener!=null){
					listener.onSuccess(data, EmployerData.class);
				}
				
			} catch (Exception e) {
				PromptUtil.showToast(context,context.getString(R.string.req_error));
			}
		}
	}
	
	private EmployerData parserResponse(List<ProtocolData> mDatas) {
		EmployerData employer=new EmployerData();
		ResponseData response = new ResponseData();
		LoginUtil.mLoginStatus.mResponseData =response;
		for (ProtocolData data : mDatas) {
			if (data.mKey.equals(ProtocolUtil.msgheader)) {
				ProtocolUtil.parserResponse(response, data);

			} else if (data.mKey.equals(ProtocolUtil.msgbody)) {

				List<ProtocolData> result = data.find("/result");
				if (result != null) {
					LoginUtil.mLoginStatus.result = result.get(0).getmValue();
				}
				
				List<ProtocolData> message = data.find("/message");
				if (message != null) {
					LoginUtil.mLoginStatus.message = message.get(0).getmValue();
				}
				List<ProtocolData> cwtruename = data.find("/name");
				if (cwtruename != null) {
					employer.name=cwtruename.get(0).mValue;
				}
				List<ProtocolData> cwtrueidcard = data.find("/hasRegister");
				if (cwtrueidcard != null) {
					employer.hasRegister=cwtrueidcard.get(0).mValue;
				}
				List<ProtocolData> cwemail = data.find("/bankAccount");
				if (cwemail != null) {
					employer.bankAccount=cwemail.get(0).mValue;
				}
				
			}
		}
		
		return employer;
	}
	
	private void showAgainLoadDialog () {
		new AlertDialog.Builder(context).setTitle("网络异常").setMessage("网络可能出现异常是否重试?")
		.setPositiveButton("重试", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				new GetEmployerInfoTask(context, listener).execute(params);
			}
		}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		}).setCancelable(false).show();
	}
}

