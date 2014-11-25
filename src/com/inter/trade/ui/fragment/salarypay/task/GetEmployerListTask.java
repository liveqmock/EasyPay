/*
 * @Title:  GameInfoTask.java
 * @Copyright:  XXX Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * @Description:  TODO<请描述此文件是做什么的>
 * @author:  MeiYi
 * @data:  2014年6月25日 上午11:53:40
 * @version:  V1.0
 */
package com.inter.trade.ui.fragment.salarypay.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.inter.protocol.ProtocolData;
import com.inter.protocol.ProtocolRsp;
import com.inter.protocol.util.ProtocolUtil;
import com.inter.trade.R;
import com.inter.trade.data.CommonData;
import com.inter.trade.data.ResponseData;
import com.inter.trade.error.ErrorUtil;
import com.inter.trade.net.HttpUtil;
import com.inter.trade.ui.fragment.gamerecharge.data.AreaData;
import com.inter.trade.ui.fragment.gamerecharge.data.GameInfoData;
import com.inter.trade.ui.fragment.salarypay.bean.EmployerData;
import com.inter.trade.ui.fragment.salarypay.parser.GetEmployerListParser;
import com.inter.trade.ui.fragment.waterelectricgas.data.ResponseStateListener;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PromptUtil;

/**
 * 根据月份获取员工工资列表
 * @author  ChenGuangChi
 * @data:  2014年6月25日 上午11:53:40
 * @version:  V1.0
 */
public class GetEmployerListTask extends AsyncTask<String, Void, Void> {
	
	private Context context;
	private ResponseStateListener listener;
	
	public GetEmployerListTask(Context context, ResponseStateListener listener) {
		super();
		this.context = context;
		this.listener = listener;
	}

	ProtocolRsp mRsp;
	@Override
	protected Void doInBackground(String... params) {
		try {
			CommonData mData = new CommonData();
			mData.putValue("month", params[0]);
			mData.putValue("bossauthorid", params[1]);
			
			List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas("ApiWageInfo", 
					"GetSalaryList", mData);
			GetEmployerListParser myParser = new GetEmployerListParser();
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
		PromptUtil.showDialog(context, context.getResources().getString(R.string.loading));
	}
	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		PromptUtil.dissmiss();
		if(mRsp==null){
			PromptUtil.showToast(context, context.getString(R.string.net_error));
		}else{
			try {
				List<ProtocolData> mDatas = mRsp.mActions;
				ArrayList<EmployerData> data = parserResponse(mDatas);
				
				if(!ErrorUtil.create().errorDeal(LoginUtil.mLoginStatus,(Activity)context)){
					return;
				}
				
				if(listener!=null){
					listener.onSuccess(data, EmployerData.class);
				}
				
			} catch (Exception e) {
				PromptUtil.showToast(context,context.getString(R.string.req_error));
				e.printStackTrace();
			}
		}
	}
	
	private ArrayList<EmployerData> parserResponse(List<ProtocolData> mDatas) {
		ArrayList<EmployerData> list=new ArrayList<EmployerData>();
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
				if (result != null) {
					LoginUtil.mLoginStatus.message = message.get(0).getmValue();
				}
				
				List<ProtocolData> aupic = data.find("/msgchild");
				ArrayList<AreaData> areaList=new ArrayList<AreaData>();
				if(aupic!=null)
				for(ProtocolData child:aupic){
					if (child.mChildren != null && child.mChildren.size() > 0) {
						Set<String> keys = child.mChildren.keySet();
						EmployerData area=new EmployerData();
						for(String key:keys){
							List<ProtocolData> rs = child.mChildren.get(key);
							for(ProtocolData item: rs){
								if(item.mKey.equals("id")){
									area.id=item.mValue;
								}else if(item.mKey.equals("name")){
									area.name=item.mValue;
								}else if(item.mKey.equals("hasRegister")){
									area.hasRegister=item.mValue;
								}else if(item.mKey.equals("bankAccount")){
									area.bankAccount=item.mValue;
								}else if(item.mKey.equals("money")){
									area.money=item.mValue;
								}else if(item.mKey.equals("phone")){
									area.phone=item.mValue;
								}else if(item.mKey.equals("canEdit")){
									area.canEdit=item.mValue;
								}else if(item.mKey.equals("cwcanEdit")){
									area.cwcanEdit=item.mValue;
								}else if(item.mKey.equals("wagelistid")){
									area.wagelistid=item.mValue;
								}
							}
						}
						list.add(area);
					}
				}
			}
		}
		
		return list;
	}
}

