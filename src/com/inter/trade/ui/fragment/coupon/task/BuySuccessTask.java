package com.inter.trade.ui.fragment.coupon.task;

import java.util.List;

import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;

import com.inter.protocol.ProtocolData;
import com.inter.protocol.ProtocolRsp;
import com.inter.protocol.util.ProtocolUtil;
import com.inter.trade.R;
import com.inter.trade.data.ResponseData;
import com.inter.trade.data.ResultData;
import com.inter.trade.log.Logger;
import com.inter.trade.net.HttpUtil;
import com.inter.trade.ui.fragment.coupon.parser.BuySuccessParser;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PromptUtil;

public class BuySuccessTask extends AsyncTask<String, Integer, Boolean>{
	ProtocolRsp mRsp= null;
	FragmentActivity mActivity;
	private ResultData mData;
	private String mResultString;
	private String mApiName;
	private String mFunName;
	public BuySuccessTask(FragmentActivity temp,ResultData data,String apiName,String funName){
		mActivity = temp;
		mData = data;
		mApiName = apiName;
		mFunName = funName;
	}
	@Override
	protected Boolean doInBackground(String... arg0) {
		// TODO Auto-generated method stub
		try {
			List<ProtocolData> mDatas = createRequest(mData);
			
			BuySuccessParser versionParser = new BuySuccessParser();
			mRsp = HttpUtil.doRequest(versionParser, mDatas);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			Logger.e(e);
		}
		
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
				if(mResultString.equals("success")){
					Logger.d("request success");
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
		}
	}
	private List<ProtocolData> createRequest(ResultData data){
//		return ProtocolUtil.getRequestDatas("ApiCouponInfo", "couponSalePay", data);
		return ProtocolUtil.getRequestDatas(mApiName, mFunName, data);
	}
	
	private void parserResponse(List<ProtocolData> mDatas){
		ResponseData response = new ResponseData();
		LoginUtil.mLoginStatus.mResponseData = response;
		for(ProtocolData data :mDatas){
			if(data.mKey.equals(ProtocolUtil.msgheader)){
				ProtocolUtil.parserNotoken(response, data);
			}else if(data.mKey.equals(ProtocolUtil.msgbody)){
				
				List<ProtocolData> result = data.find("/result");
				if(result != null){
					mResultString = result.get(0).mValue;
				}
			}
		}
	}
}
