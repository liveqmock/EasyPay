/*
 * @Title:  GameInfoTask.java
 * @Copyright:  XXX Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * @Description:  TODO<请描述此文件是做什么的>
 * @author:  MeiYi
 * @data:  2014年6月25日 上午11:53:40
 * @version:  V1.0
 */
package com.inter.trade.ui.fragment.salaryget.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.TextureView;
import android.widget.TextView;

import com.inter.protocol.ProtocolData;
import com.inter.protocol.ProtocolRsp;
import com.inter.protocol.util.ProtocolUtil;
import com.inter.trade.R;
import com.inter.trade.data.CommonData;
import com.inter.trade.data.ResponseData;
import com.inter.trade.error.ErrorUtil;
import com.inter.trade.net.HttpUtil;
import com.inter.trade.ui.fragment.salaryget.bean.SalaryGet;
import com.inter.trade.ui.fragment.salaryget.parser.GetPayGetListParser;
import com.inter.trade.ui.fragment.waterelectricgas.data.ResponseStateListener;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PromptUtil;

/**
 * 获取工资签收列表
 * @author  ChenGuangChi
 * @data:  2014年6月25日 上午11:53:40
 * @version:  V1.0
 */
public class GetPayGetListTask extends AsyncTask<String, Void, Void> {
	
	private Context context;
	private ResponseStateListener listener;
	
	public GetPayGetListTask(Context context, ResponseStateListener listener) {
		super();
		this.context = context;
		this.listener = listener;
	}

	ProtocolRsp mRsp;
	@Override
	protected Void doInBackground(String... params) {
		try {
			CommonData mData = new CommonData();
			mData.putValue("querytype", params[0]);
			mData.putValue("querywhere", params[1]);
			
			List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas("ApiWageInfo", 
					"readauthorwagelists", mData);
			GetPayGetListParser myParser = new GetPayGetListParser();
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
				ArrayList<SalaryGet> data = parserResponse(mDatas);
				
				if(!ErrorUtil.create().errorDeal(LoginUtil.mLoginStatus,(Activity)context)){
					return;
				}
				if(listener!=null){
					listener.onSuccess(data, SalaryGet.class);
				}
				
			} catch (Exception e) {
				PromptUtil.showToast(context,context.getString(R.string.req_error));
				Log.getStackTraceString(e);
			}
		}
	}
	
	private ArrayList<SalaryGet> parserResponse(List<ProtocolData> mDatas) {
		ArrayList<SalaryGet> list=new ArrayList<SalaryGet>();
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
				
				if(aupic!=null)
				for(ProtocolData child:aupic){
					SalaryGet salary=new SalaryGet();
					if (child.mChildren != null && child.mChildren.size() > 0) {
						Set<String> keys = child.mChildren.keySet();
						for(String key:keys){
							List<ProtocolData> rs = child.mChildren.get(key);
							for(ProtocolData item: rs){
								if(item.mKey.equals("wagemoney")){
									salary.wagemoney=item.mValue;
								}else if(item.mKey.equals("wagemonth")){
									salary.wagemonth=item.mValue;
								}else if(item.mKey.equals("isqianshou")){
									salary.isqianshou=item.mValue;
								}else if(item.mKey.equals("wageid")){
									salary.wageid=item.mValue;
								}
							}
						}
					}
					list.add(salary);
				}
				
			}
		}
		
		return list;
	}
}

