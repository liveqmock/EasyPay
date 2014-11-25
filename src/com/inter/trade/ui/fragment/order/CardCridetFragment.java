package com.inter.trade.ui.fragment.order;

import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.inter.protocol.ProtocolData;
import com.inter.protocol.ProtocolRsp;
import com.inter.protocol.P.file_download.req;
import com.inter.protocol.util.ProtocolUtil;
import com.inter.trade.R;
import com.inter.trade.data.CommonData;
import com.inter.trade.data.ResponseData;
import com.inter.trade.error.ErrorUtil;
import com.inter.trade.net.HttpUtil;
import com.inter.trade.ui.PayActivity;
import com.inter.trade.ui.PayApp;
import com.inter.trade.ui.fragment.BaseFragment;
import com.inter.trade.ui.fragment.order.util.OrderCridetParser;
import com.inter.trade.ui.fragment.order.util.OrderData;
import com.inter.trade.util.Base64Pay;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PromptUtil;
import com.unionpay.uppayplugin.demo.UnionpayUtil;

/**
 * 支付订单获取银行卡星级评价
 * @author apple
 *
 */

public class CardCridetFragment extends BaseFragment{
	private static String bankno;
	private static String mBankName;
	private static OrderData mData;
	private static String mCardNo;
	private CommonData requestData = new CommonData();
	
	private TextView cridet_tv;
	private CridetTask mCridetTask;
	private NoTask mNoTask;
	private String mBkntno;//流水号
	private PayActivity mActivity;
	
	public static CardCridetFragment create(OrderData data,String no,String cardNo,String bankname){
		CardCridetFragment f = new CardCridetFragment();
		bankno = no;
		mData = data;
		mCardNo = cardNo;
		mBankName= bankname;
		return f;
	}
	public CardCridetFragment(){
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitle("个人信用");
		setBackVisible();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		LoginUtil.detection(getActivity());
		View view = inflater.inflate(R.layout.order_cridet_layout, container,false);
		Button next = (Button)view.findViewById(R.id.swip_confirm_btn);
		next.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//				FragmentTransaction transaction = getFragmentManager().beginTransaction();
//				transaction.replace(R.id.func_container, new SubmitFragment());
//				transaction.commit();
				mNoTask = new NoTask();
				mNoTask.execute("");
			}
		});
		initView(view);
		initData();
		return view;
	}
	private void initView(View view){
		cridet_tv = (TextView)view.findViewById(R.id.cridet_tv);
	}
	
	private void initData(){
		mCridetTask = new CridetTask();
		mCridetTask.execute("");
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(mCridetTask!=null){
			mCridetTask.cancel(true);
			mCridetTask=null;
		}
		
		if(mNoTask!=null){
			mNoTask.cancel(true);
			mNoTask=null;
		}
	}
	

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setTitle("个人信用");
//		openReaderNow();
	}


	class CridetTask extends AsyncTask<String, Integer, Boolean>{
		ProtocolRsp mRsp;
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				requestData.putValue("authorid",LoginUtil.mLoginStatus.authorid);
				requestData.putValue("orderid", mData.orderid);
				requestData.putValue("orderno", mData.orderno);
				requestData.putValue("paymoney", mData.ordermoney);
				requestData.putValue("bankcardno", mCardNo);
				requestData.putValue("bankname", mBankName);
				List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas("ApiOrderInfo", 
						"orderPayBankCardStar", requestData);
				OrderCridetParser authorRegParser = new OrderCridetParser();
				mRsp = HttpUtil.doRequest(authorRegParser, mDatas);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				mRsp =null;
			}
			
			return null;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			PromptUtil.dissmiss();
			if(mRsp==null){
				PromptUtil.showToast(getActivity(), getString(R.string.net_error));
			}else{
				try {
					List<ProtocolData> mDatas = mRsp.mActions;
					parserResoponse(mDatas);
				
				} catch (Exception e) {
					// TODO: handle exception
					PromptUtil.showToast(getActivity(),getString(R.string.req_error));
				}
			
			}
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			PromptUtil.showDialog(getActivity(), getActivity().getResources().getString(R.string.loading));
		}
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
					LoginUtil.mLoginStatus.result = result1.get(0).mValue;
				}
				
				List<ProtocolData> message = data.find("/message");
				if(message != null){
					LoginUtil.mLoginStatus.message = message.get(0).mValue;
				}
				
				List<ProtocolData> bankcardstar = data.find("/bankcardstar");
				if(bankcardstar != null){
					String bankPrompt = bankcardstar.get(0).mValue;
					cridet_tv.setText(bankPrompt);
				}
			}
		}
	}
	
	
	class NoTask extends AsyncTask<String, Integer, Boolean>{
		ProtocolRsp mRsp;
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				requestData.putValue("authorid",LoginUtil.mLoginStatus.authorid);
				requestData.putValue("orderid", mData.orderid);
				requestData.putValue("orderno", mData.orderno);
				requestData.putValue("paymoney", mData.ordermoney);
				requestData.putValue("bankcardno", mCardNo);
				requestData.putValue("bankname", mBankName);
				requestData.putValue("paycardid", PayApp.mKeyCode);
				requestData.putValue("merReserved", Base64Pay.encode(PayApp.merReserved.toString().getBytes()));
				List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas("ApiOrderInfo", 
						"orderPayReq", requestData);
				OrderCridetParser authorRegParser = new OrderCridetParser();
				mRsp = HttpUtil.doRequest(authorRegParser, mDatas);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				mRsp =null;
			}
			
			return null;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			PromptUtil.dissmiss();
			if(mRsp==null){
				PromptUtil.showToast(getActivity(), getString(R.string.net_error));
			}else{
				try {
					List<ProtocolData> mDatas = mRsp.mActions;
					parserNoResoponse(mDatas);
					
					if(!ErrorUtil.create().errorDeal(LoginUtil.mLoginStatus,getActivity())){
						return;
					}
//					android.util.Log.d("tgy","-------------------------1");
					if(LoginUtil.mLoginStatus.result.equals(ProtocolUtil.SUCCESS)){
//						android.util.Log.d("tgy","-------------------------2");
//						if(getActivity() instanceof PayActivity){
//							android.util.Log.d("tgy","-------------------------21");
//							mActivity.mCommonData.clear();
//							mActivity = (PayActivity)getActivity();
//							android.util.Log.d("tgy","-------------------------22");
//							HashMap<String, String> item = new HashMap<String, String>();
//							item.put("订单编号：",mData.orderno);
//							mActivity.mCommonData.add(item);
//							android.util.Log.d("tgy","-------------------------23");
//							HashMap<String, String> item1 = new HashMap<String, String>();
//							item1.put("支付金额：",mData.ordermoney);
//							mActivity.mCommonData.add(item1);
//							android.util.Log.d("tgy","-------------------------24");
//						}
//						android.util.Log.d("tgy","-------------------------3");
						UnionpayUtil.startUnionPlungin(mBkntno, getActivity());
//						android.util.Log.d("tgy","-------------------------4");
					}
				} catch (Exception e) {
					android.util.Log.d("tgy","-------------------------6");
					android.util.Log.d("tgy",e.toString());
					// TODO: handle exception
					PromptUtil.showToast(getActivity(),getString(R.string.req_error));
				}
			
			}
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			PromptUtil.showDialog(getActivity(), getActivity().getResources().getString(R.string.loading));
		}
	}
	private void parserNoResoponse(List<ProtocolData> params){
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
				
				List<ProtocolData> bkntno = data.find("/bkntno");
				if(bkntno != null){
					mBkntno = bkntno.get(0).mValue;
				}
			}
		}
	}
}
