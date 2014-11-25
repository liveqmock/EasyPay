package com.inter.trade;

import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.inter.protocol.ProtocolData;
import com.inter.protocol.ProtocolRsp;
import com.inter.protocol.util.ProtocolUtil;
import com.inter.trade.AsyncLoadWork.AsyncLoadWorkListener;
import com.inter.trade.data.CommonData;
import com.inter.trade.data.NoticeData;
import com.inter.trade.data.ResponseData;
import com.inter.trade.error.ErrorUtil;
import com.inter.trade.log.Logger;
import com.inter.trade.net.HttpUtil;
import com.inter.trade.parser.NoticeParser;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PreferenceConfig;
import com.inter.trade.util.PromptUtil;

public class NoticeNoTask extends AsyncTask<String, Integer, Boolean>{
	Context mContext;
	ProtocolRsp mRsp;
	String mApiName;
	String mApiFunc;
//	SunType mdataReq;
//	String mtagRep;
	List<NoticeData> mList;
	AsyncLoadWorkListener mListener;
	public NoticeNoTask(Context context, List<NoticeData> list, String apiName, String apiFunc, AsyncLoadWorkListener listener){
		Logger.d("NoticeNoTask", "task start");
		mContext=context;
		mList=list;
		mApiName = apiName;
		mApiFunc = apiFunc;
		mListener = listener;
	}
	
	@Override
	protected Boolean doInBackground(String... params) {
		// TODO Auto-generated method stub
		try {
			
			CommonData data = new CommonData();
//			data.putValue("address",agentData.address);
//			data.putValue("phone",agentData.phone);
//			data.putValue("name",agentData.name);
			List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas(mApiName, 
					mApiFunc, data);
			NoticeParser authorRegParser = new NoticeParser();
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
//			PromptUtil.showToast(mContext, mContext.getString(R.string.net_error));
		}else{
			try {
				mList.clear();
				List<ProtocolData> mDatas = mRsp.mActions;
				parserResoponse(mDatas);
				
				if(!ErrorUtil.create().errorDeal(LoginUtil.mLoginStatus,(Activity)mContext)){
					Logger.d("NoticeNoTask", "task response failure");
					return;
				}
				if(LoginUtil.mLoginStatus.result.equals(ProtocolUtil.SUCCESS)){
//					PromptUtil.showToast(mContext,LoginUtil.mLoginStatus.message);
					if(mListener != null){
						Logger.d("NoticeNoTask", "task response success");
						mListener.onSuccess(mList, null);
					}
				}
			} catch (Exception e) {
//				PromptUtil.showToast(mContext,mContext.getString(R.string.req_error));
			}
		
		}
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
//			PromptUtil.showDialog(mContext, mContext.getResources().getString(R.string.loading));
	}
	private void parserResoponse(List<ProtocolData> params){
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
				
				List<ProtocolData> aupic = data.find("/msgchild");
				if(aupic==null){
					return;
				}
				for(ProtocolData child:aupic){
					NoticeData noticedata = new NoticeData();
					if (child.mChildren != null && child.mChildren.size() > 0) {
						Set<String> keys = child.mChildren.keySet();
						for(String key:keys){
							List<ProtocolData> rs = child.mChildren.get(key);
							for(ProtocolData item: rs){
								if(item.mKey.equals("noticeid")){
									noticedata.setNoticeid(item.mValue);
								}else if(item.mKey.equals("noticeno")){
									noticedata.setNoticeno(item.mValue);
								}else if(item.mKey.equals("noticecontent")){
									noticedata.setNoticecontent(item.mValue);
								}else if(item.mKey.equals("noticetitle")){
									noticedata.setNoticetitle(item.mValue);
								}else if(item.mKey.equals("noticedate")){
									noticedata.setNoticedate(item.mValue);
								}	
									
							}
						}
					}
					
					mList.add(noticedata);
				}
			}
		}
	}
}
