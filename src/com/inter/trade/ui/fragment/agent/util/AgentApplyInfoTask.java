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
import com.inter.trade.ui.fragment.gamerecharge.data.BillData;
import com.inter.trade.ui.fragment.waterelectricgas.data.ResponseStateListener;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PromptUtil;

/**
 * 代理商在线申请，获取申请基本信息Task
 * @author Lihaifeng
 *
 */
public class AgentApplyInfoTask extends AsyncTask<String, Integer, Boolean>{
	//true dialog 已打开; false,dialog 未打开
	private boolean mIsDialogOpen=false;
	
	//true dialog 要关闭; false,dialog 不需关闭
	private boolean mDialogClose=false;
	
	Context mContext;
	ResponseStateListener  listener;
	ProtocolRsp mRsp;
	String mApiName;
	String mApiFunc;
	SunType mdataReq;
	String mtagRep;
	ArrayAdapter<String> madapter;
	List<AgentApplyInfoData> mList;
	List<String> typeList;
	
	public AgentApplyInfoTask(Context context, ResponseStateListener  listener0, List<AgentApplyInfoData> list, List<String> spTypeList, ArrayAdapter<String> adapterProv, String ApiName, String ApiFunc, SunType dataReq, String tagRep){
		mContext=context;
		listener = listener0;
		mList=list;
		typeList=spTypeList;
		madapter=adapterProv;
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
			List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas(mApiName, 
					mApiFunc, mdataReq);
			AgentApplyInfoParser authorRegParser = new AgentApplyInfoParser();
			mRsp = HttpUtil.doRequest(authorRegParser, mDatas);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			mRsp =null;
			PromptUtil.dissmiss();
			mIsDialogOpen=false;
			mDialogClose=false;
		}
		return null;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if(mIsDialogOpen && mDialogClose){
			PromptUtil.dissmiss();
		}
		if(mRsp==null){
			PromptUtil.showToast(mContext, mContext.getString(R.string.net_error));
			PromptUtil.dissmiss();
//			mAutoUpdate=false;
			mIsDialogOpen=false;
			mDialogClose=false;
		}else{
			try {
				mList.clear();
				typeList.clear();
				List<ProtocolData> mDatas = mRsp.mActions;
				parserResoponse(mDatas, mtagRep);
				
				if(!ErrorUtil.create().errorDeal(LoginUtil.mLoginStatus,(Activity)mContext)){
//					mAutoUpdate=false;
					mIsDialogOpen=false;
					mDialogClose=false;
					return;
				}
				if(LoginUtil.mLoginStatus.result.equals(ProtocolUtil.SUCCESS)){
					madapter.notifyDataSetChanged();
					if(listener!=null){
						listener.onSuccess(null, null);
					}
				}
			} catch (Exception e) {
				PromptUtil.showToast(mContext,mContext.getString(R.string.req_error));
				PromptUtil.dissmiss();
//				mAutoUpdate=false;
				mIsDialogOpen=false;
				mDialogClose=false;
			}
		
		}
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		if(!mIsDialogOpen){
			PromptUtil.showDialog(mContext, mContext.getResources().getString(R.string.loading));
			mIsDialogOpen=true;
			mDialogClose=true;
		}
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
				
//				List<ProtocolData> agentprotocol = data.find("/agentprotocol");
//				if(message != null){
//					agentprotocol = agentprotocol.get(0).mValue;
//				}
				
				List<ProtocolData> aupic = data.find("/msgchild");
				if(aupic==null){
					return;
				}
				for(ProtocolData child:aupic){
					AgentApplyInfoData infoData = new AgentApplyInfoData();
					infoData.infoDataList = new ArrayList<BaseData>();
//					String prov = "";
					if (child.mChildren != null && child.mChildren.size() > 0) {
						Set<String> keys = child.mChildren.keySet();
						for(String key:keys){
							List<ProtocolData> rs = child.mChildren.get(key);
							for(ProtocolData item: rs){
								if(item.mKey.equals("custypeid")){
									infoData.custypeid  = item.mValue;
								}else if(item.mKey.equals("custypename")){
									infoData.custypename  = item.mValue;
									typeList.add(item.mValue);
								}else if(item.mKey.equals("msgfile")){
//									prov  = item.mValue;
									List<ProtocolData> aupic2 = item.find("/msgchild");
									if(aupic2==null){
										return;
									}
									for(ProtocolData child2:aupic2){
										BaseData infoDat =  new BaseData();
//										String prov = "";
										if (child2.mChildren != null && child2.mChildren.size() > 0) {
											Set<String> keys2 = child2.mChildren.keySet();
											for(String key2:keys2){
												List<ProtocolData> rs2 = child2.mChildren.get(key2);
												for(ProtocolData item2: rs2){
													if(item2.mKey.equals("pictypeid")){
														infoDat.putValue("pictypeid", item2.mValue);
													}else if(item2.mKey.equals("pictypename")){
														infoDat.putValue("pictypename", item2.mValue);
													}else if(item2.mKey.equals("pictypeno")){
														infoDat.putValue("pictypeno", item2.mValue);
													}else if(item2.mKey.equals("picuploadurl")){
														infoDat.putValue("picuploadurl", item2.mValue);
													}
												}
											}
										}
										infoData.infoDataList.add(infoDat);
									}
								}	
							}
						}
					}
					
					mList.add(infoData);
				}
				
			}
		}
	}
}