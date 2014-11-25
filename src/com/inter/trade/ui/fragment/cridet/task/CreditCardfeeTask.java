package com.inter.trade.ui.fragment.cridet.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;

import com.inter.protocol.ProtocolData;
import com.inter.protocol.ProtocolParser;
import com.inter.protocol.ProtocolRsp;
import com.inter.protocol.util.ProtocolUtil;
import com.inter.trade.R;
import com.inter.trade.data.ArriveData;
import com.inter.trade.data.ResponseData;
import com.inter.trade.data.TaskData;
import com.inter.trade.error.ErrorUtil;
import com.inter.trade.log.Logger;
import com.inter.trade.net.HttpUtil;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PromptUtil;

/**
 * 获取交易的手续费和其他信息
 * @author apple
 *
 */
public class CreditCardfeeTask extends AsyncTask<String, Integer, Boolean>{
	ProtocolRsp mRsp= null;
	FragmentActivity mActivity;
	private TaskData mTaskData;
	private String mResult;
	private String mMessage;
	public String mPayfee;
	private FeeListener mFeeListener;
	private ArrayList<ArriveData> mArriveDatas=new ArrayList<ArriveData>();
	
	
	public CreditCardfeeTask(FragmentActivity temp,TaskData taskData){
		mActivity = temp;
		mTaskData = taskData;
	}
	@Override
	protected Boolean doInBackground(String... arg0) {
		// TODO Auto-generated method stub
		List<ProtocolData> mDatas = getResponseDatas();
		
		ProtocolParser.instance().setParser(mTaskData.mNetParser);
		String content = ProtocolParser.instance().aToXml(mDatas);
		
		Logger.d("HttpApi", "request\n"+content);
		mRsp = HttpUtil.getRequest(content, mTaskData.mNetParser);
		return null;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		PromptUtil.dissmiss();
		if(mRsp==null){
			PromptUtil.showToast(mActivity,mActivity.getString(R.string.net_error));
			if(mFeeListener != null){
				mFeeListener.result(1, mMessage,mArriveDatas);
			}
		}else{
			try {
				List<ProtocolData> mDatas = mRsp.mActions;
				parserResponse(mDatas);
				
				if(!ErrorUtil.create().errorDeal(LoginUtil.mLoginStatus,mActivity)){
					if(mFeeListener != null){
						mFeeListener.result(1, mMessage,mArriveDatas);
					}
					return;
				}
				if(mResult.equals(ProtocolUtil.SUCCESS)){
					if(mFeeListener != null){
						mFeeListener.result(0, mPayfee,mArriveDatas);
					}
				}else {
//					PromptUtil.showToast(mActivity,mMessage);
					if(mFeeListener != null){
						mFeeListener.result(1, mMessage,mArriveDatas);
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
//				PromptUtil.showToast(mActivity,mActivity.getResources().getString(R.string.net_error));
			}
			
		}
	}
	public void setFeeListener(FeeListener feeListener){
		this.mFeeListener = feeListener;
	}
	private List<ProtocolData> getResponseDatas(){
		if(!mTaskData.mCommonData.sunMap.containsKey("arriveid")){
			mTaskData.mCommonData.putValue("arriveid", "");
		}
		return ProtocolUtil.getRequestDatas(mTaskData.apiName, mTaskData.funcName, mTaskData.mCommonData);
	}
	
	private void parserResponse(List<ProtocolData> mDatas){
		ResponseData response = new ResponseData();
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
////				}
				
			}else if(data.mKey.equals(ProtocolUtil.msgbody)){
				List<ProtocolData> result1 = data.find("/result");
				if(result1 != null){
					mResult = result1.get(0).mValue;
					LoginUtil.mLoginStatus.result = mResult;
				}
				
				
				List<ProtocolData> message = data.find("/message");
				if(message != null){
					mMessage = message.get(0).mValue;
					LoginUtil.mLoginStatus.message = mMessage;
				}
				
				List<ProtocolData> payfee = data.find("/payfee");
				if(payfee != null){
					mPayfee = payfee.get(0).mValue;
				}
				
				List<ProtocolData> aupic = data.find("/msgchild");
				if(aupic==null){
					return;
				}
				for(ProtocolData child:aupic){
					ArriveData picData = new ArriveData();
					if (child.mChildren != null && child.mChildren.size() > 0) {
						Set<String> keys = child.mChildren.keySet();
						for(String key:keys){
							List<ProtocolData> rs = child.mChildren.get(key);
							for(ProtocolData item: rs){
								if(item.mKey.equals("arriveid")){
									picData.arriveid  = item.mValue;
									
								}else if(item.mKey.equals("arrivetime")){
									picData.arrivetime  = item.mValue;
									
								}else if(item.mKey.equals("activearriveid")){
									
									picData.activearriveid  = item.mValue;
								}else if(item.mKey.equals("activememo")){
									
									picData.activememo  = item.mValue;
								}
							}
						}
					}
					
					mArriveDatas.add(picData);
				}
				
			}
		}
	}
	public interface FeeListener{
		/**
		 * 
		 * @param state 1:获取成功
		 * 				2：获取失败
		 * @param fee
		 */
		public void result(int state,String fee,ArrayList<ArriveData> datas);
	}
}
