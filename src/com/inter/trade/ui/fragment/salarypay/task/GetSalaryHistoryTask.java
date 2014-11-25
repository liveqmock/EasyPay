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

import com.inter.protocol.ProtocolData;
import com.inter.protocol.ProtocolRsp;
import com.inter.protocol.util.ProtocolUtil;
import com.inter.trade.R;
import com.inter.trade.data.CommonData;
import com.inter.trade.data.ResponseData;
import com.inter.trade.error.ErrorUtil;
import com.inter.trade.net.HttpUtil;
import com.inter.trade.ui.fragment.salarypay.bean.SalaryData;
import com.inter.trade.ui.fragment.salarypay.bean.Stuff;
import com.inter.trade.ui.fragment.salarypay.parser.GetSalaryHistoryParser;
import com.inter.trade.ui.fragment.waterelectricgas.data.ResponseStateListener;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PromptUtil;

/**
 * 读取支付工资的历史记录
 * @author  ChenGuangChi
 * @data:  2014年6月25日 上午11:53:40
 * @version:  V1.0
 */
public class GetSalaryHistoryTask extends AsyncTask<String, Void, Void> {
	
	private Context context;
	private ResponseStateListener listener;
	
	public GetSalaryHistoryTask(Context context, ResponseStateListener listener) {
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
			mData.putValue("querywhere",params[1]);
			mData.putValue("bossauthorid",params[2]);
			
			List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas("ApiWageInfo", 
					"readwagelists", mData);
			GetSalaryHistoryParser myParser = new GetSalaryHistoryParser();
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
				ArrayList<SalaryData> data = parserResponse(mDatas);
				
				if(!ErrorUtil.create().errorDeal(LoginUtil.mLoginStatus,(Activity)context)){
					return;
				}
				if(listener!=null){
					listener.onSuccess(data, SalaryData.class);
				}
				
			} catch (Exception e) {
				PromptUtil.showToast(context,context.getString(R.string.req_error));
				e.printStackTrace();
			}
		}
	}
	
	private ArrayList<SalaryData> parserResponse(List<ProtocolData> mDatas) {
		ArrayList<SalaryData> salaryList=new ArrayList<SalaryData>();
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
					SalaryData salary=new SalaryData();
					if (child.mChildren != null && child.mChildren.size() > 0) {
						Set<String> keys = child.mChildren.keySet();
						for(String key:keys){
							List<ProtocolData> rs = child.mChildren.get(key);
							for(ProtocolData item: rs){
								if(item.mKey.equals("wagestanum")){
									salary.wagestanum=item.mValue;
								}else if(item.mKey.equals("wageallmoney")){
									salary.wageallmoney=item.mValue;
								}else if(item.mKey.equals("wagemonth")){
									salary.wagemonth=item.mValue;
								}else if(item.mKey.equals("wagepaymoney")){
									salary.wagepaymoney=item.mValue;
								}else if(item.mKey.equals("ilist")){
									List<ProtocolData> childs = item.find("/msgchild");
									if(childs!=null){
										ArrayList<Stuff> stuffList=new ArrayList<Stuff>();
										for(ProtocolData subchild:childs){
											Stuff stuff=new Stuff();
											if(subchild.mChildren!=null && child.mChildren.size()>0){
												Set<String> k=subchild.mChildren.keySet();
												for(String str:k){
													List<ProtocolData> subrs = subchild.mChildren.get(str);
													for(ProtocolData sub:subrs){
														if(sub.mKey.equals("mobile")){
															stuff.mobile=sub.mValue;
														}else if(sub.mKey.equals("staname")){
															stuff.staname=sub.mValue;
														}else if(sub.mKey.equals("wagemoney")){
															stuff.wagemoney=sub.mValue;
														}else if(sub.mKey.equals("bkcardno")){
															stuff.bkcardno=sub.mValue;
														}else if(sub.mKey.equals("bkcardbank")){
															stuff.bkcardbank=sub.mValue;
														}
													}
													
												}
											}
											stuffList.add(stuff);
										}
										salary.stuffList=stuffList;
									}
								}
							}
						}
					}
					salaryList.add(salary);
				}
			}
		}
		
		return salaryList;
	}
}

