package com.inter.trade.ui.fragment.waterelectricgas;

import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.inter.protocol.ProtocolData;
import com.inter.protocol.ProtocolRsp;
import com.inter.protocol.util.ProtocolUtil;
import com.inter.trade.AsyncLoadWork.AsyncLoadWorkListener;
import com.inter.trade.R;
import com.inter.trade.SwipKeyTask;
import com.inter.trade.data.CardData;
import com.inter.trade.data.CommonData;
import com.inter.trade.data.ResponseData;
import com.inter.trade.error.ErrorUtil;
import com.inter.trade.net.HttpUtil;
import com.inter.trade.ui.BankListActivity;
import com.inter.trade.ui.DaikuanActivity;
import com.inter.trade.ui.PayApp;
import com.inter.trade.ui.PayApp.SwipListener;
//import com.inter.trade.ui.WaterElectricGasPayActivity;
import com.inter.trade.ui.WaterElectricGasPayActivity;
import com.inter.trade.ui.fragment.BaseFragment;
import com.inter.trade.ui.fragment.cridet.data.JournalData;
//import com.inter.trade.ui.fragment.qmoney.util.WaterElectricGasPayData;
import com.inter.trade.ui.fragment.returndaikuan.util.DaikuanData;
import com.inter.trade.ui.fragment.returndaikuan.util.DaikuanNoParser;
import com.inter.trade.ui.fragment.waterelectricgas.util.WaterElectricGasBillData;
import com.inter.trade.ui.fragment.waterelectricgas.util.WaterElectricGasBillParser;
import com.inter.trade.ui.fragment.waterelectricgas.util.WaterElectricGasPayData;
import com.inter.trade.ui.fragment.waterelectricgas.util.WaterElectricGasPayParser;
import com.inter.trade.util.Base64Pay;
import com.inter.trade.util.Constants;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.NumberFormatUtil;
import com.inter.trade.util.PromptUtil;
import com.inter.trade.util.UserInfoCheck;
import com.unionpay.uppayplugin.demo.UnionpayUtil;

/**
 * 水电煤缴费支付，确认Fragment
 * @author Lihaifeng
 *
 */

@SuppressLint("ValidFragment")
public class WaterElectricGasPayConfirmFragment extends BaseFragment implements OnClickListener,SwipListener{
	private Button toPay_btn;
	private ImageView swip_card;
	private TextView swip_prompt;
	private EditText card_edit;
	private LinearLayout bank_layout;
	private TextView bank_name;
	private EditText open_name_edit;
	private EditText open_phone_edit;
	private TextView acount;
	
	private TextView company_tv;
	private TextView client_tv;
	private TextView late_fees_tv;
	private TextView pay_money_tv;
	private TextView client_user_tv;
	
	private WaterElectricGasBillData mBillData = new WaterElectricGasBillData();
	private String mBkntno;
	private String mMessage ="";
	private String mResult =""; 
	private static double count =0;
	private static String mCouponId;
	private boolean isSelectedBank=false;
	private LinearLayout coupon_layout;
	
//	private BillTask mBillTask;
	private BuyTask mBuyTask;
	private DaikuanActivity mActivity;
	
	private Bundle bundle;
	private int mPayType=0;
	
	public static WaterElectricGasPayConfirmFragment create(double value,String couponId){
		count = value;
		mCouponId = couponId;
		return new WaterElectricGasPayConfirmFragment();
	}
	
	public WaterElectricGasPayConfirmFragment()
	{
	}
	
	public WaterElectricGasPayConfirmFragment(Bundle b) {
		this.bundle = b;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
//		initReader();
		PayApp.mSwipListener = this;
		if(getActivity() instanceof DaikuanActivity){
			 mActivity = (DaikuanActivity)getActivity();
		}
		
//		//获取账单信息
//		mBillTask = new BillTask();
//		mBillTask.execute("");
		
	}
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		LoginUtil.detection(getActivity());
		View view = inflater.inflate(R.layout.water_electric_gas_pay_swip_layout, container,false);
		initView(view);
		
//		setTitle("缴费账单");
		mPayType = WaterElectricGasMainFragment.mPayType;
		String title="";
		switch(mPayType){
		case 1:
			title = "水费";
			break;
		case 2:
			title = "电费";
			break;
		case 3:
			title = "燃气费";
			break;
		default:
			title = "水费";
		}
		setTitle(title+"账单");
		setBackVisible();
		
		return view;
	}
	private void initSwipPic(boolean flag){
		if(flag){
			swip_prompt.setText(getString(R.string.has_checked_swip));
			swip_card.setBackgroundDrawable(getResources().getDrawable(R.drawable.swip_enable));
		}else{
			swip_prompt.setText(getString(R.string.cridet_insert));
			swip_card.setBackgroundDrawable(getResources().getDrawable(R.drawable.swip_card_bg));
		}
	}
	private void initView(View view){
		toPay_btn = (Button)view.findViewById(R.id.toPay_btn);
		swip_card = (ImageView)view.findViewById(R.id.swip_card);
		swip_prompt = (TextView)view.findViewById(R.id.swip_prompt);
		card_edit = (EditText)view.findViewById(R.id.card_edit);
		bank_layout = (LinearLayout)view.findViewById(R.id.bank_layout);
		bank_name = (TextView)view.findViewById(R.id.bank_name);
//		acount = (TextView)view.findViewById(R.id.acount);
		open_phone_edit = (EditText)view.findViewById(R.id.open_phone_edit);
		open_name_edit = (EditText)view.findViewById(R.id.open_name_edit);
//		coupon_layout =  (LinearLayout)view.findViewById(R.id.coupon_layout);
		
		company_tv=(TextView)view.findViewById(R.id.company_tv);
		client_tv=(TextView)view.findViewById(R.id.client_tv);
		late_fees_tv=(TextView)view.findViewById(R.id.late_fees_tv);
		pay_money_tv=(TextView)view.findViewById(R.id.pay_money_tv);
		client_user_tv=(TextView)view.findViewById(R.id.client_user_tv);
		
		company_tv.setText(bundle.getString("company"));
		client_tv.setText(bundle.getString("client"));
		late_fees_tv.setText(bundle.getString("factBills"));
		pay_money_tv.setText(bundle.getString("totalBill"));
		client_user_tv.setText(bundle.getString("username"));
//		orderid.setText(bundle.getString("orderid"));
//		companyId.setText(bundle.getString("companyId"));
		
		toPay_btn.setOnClickListener(this);
		bank_layout.setOnClickListener(this);
		
	}
	@Override
	public void recieveCard(CardData data) {
		// TODO Auto-generated method stub
		card_edit.setText(data.pan);
	}

	@Override
	public void checkedCard(boolean flag) {
		// TODO Auto-generated method stub
		if(flag){
			boolean s = PayApp.openReaderNow();
//			startTimer();
			log("status : "+s);
			swip_prompt.setText(getString(R.string.has_checked_swip));
			swip_card.setBackgroundDrawable(getResources().getDrawable(R.drawable.swip_enable));
		}else{
			swip_prompt.setText(getString(R.string.cridet_insert));
			swip_card.setBackgroundDrawable(getResources().getDrawable(R.drawable.swip_card_bg));
		}
	}
	

	@Override
	public void progress(int status, String message) {
		// TODO Auto-generated method stub
		switch (status) {
		case SWIPING_START:
			PromptUtil.showToast(getActivity(), message);
			break;
		case SWIPING_FAIL:
			PromptUtil.showToast(getActivity(), message);
					break;
		case SWIPING_SUCCESS:
			PromptUtil.showToast(getActivity(), message);
			mKeyTask = new SwipKeyTask(getActivity(), PayApp.mKeyCode, PayApp.mKeyData,
					card_edit.getText().toString(), "family",new AsyncLoadWorkListener() {

						@Override
						public void onSuccess(Object protocolDataList,
								Bundle bundle) {
							// TODO Auto-generated method stub
							
						}

						@Override
						public void onFailure(String error) {
							// TODO Auto-generated method stub
							
						}
					});
			mKeyTask.execute("");
			
			break;
		default:
			break;
		}
		
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.toPay_btn:
			if(checkInput()){
				mBuyTask = new BuyTask();
				mBuyTask.execute("");
			}
			break;
		case R.id.bank_layout:
			showBankList();
			break;

		default:
			break;
		}
	}
	
	private void showBankList()
	{
		Intent intent = new Intent();
		intent.setClass(getActivity(), BankListActivity.class);
		startActivityForResult(intent, 1);
	}
	
	private boolean checkInput(){
		String cardNumber = card_edit.getText().toString();
		
//		cardNumber="655412542125211245";
//		PayApp.mKeyCode="3542154";
		
		if(null == cardNumber || "".equals(cardNumber)){
			PromptUtil.showToast(getActivity(), "请刷卡或输入卡号");
			return false;
		}
		
		if(!UserInfoCheck.checkBankCard(cardNumber)){
			PromptUtil.showToast(getActivity(), "卡号格式不正确");
			return false;
		}
		WaterElectricGasPayActivity.payData.sunMap.put(WaterElectricGasPayData.MRD_RECHABKCARDNO, cardNumber);
		WaterElectricGasPayActivity.payData.sunMap.put(WaterElectricGasPayData.MRD_RECHAPAYTYPEID, bundle.getString("orderid"));
		
		if(PayApp.isSwipIn && PayApp.mKeyData.mType==1){
			PromptUtil.showToast(getActivity(), PayApp.mKeyData.message);
			return false;
		}
//		
//		if(!PayApp.isSwipIn){
//			if(null == PayApp.mKeyCode || "".equals(PayApp.mKeyCode)){
//				PromptUtil.showToast(getActivity(), "请刷卡");
//			}else{
//				PromptUtil.showToast(getActivity(), "请插入刷卡器");
//			}
//			return false;
//		}
		WaterElectricGasPayActivity.payData.sunMap.put(WaterElectricGasPayData.MRD_PAYCARDID, PayApp.mKeyCode != null ? PayApp.mKeyCode : "");
		WaterElectricGasPayActivity.payData.sunMap.put(WaterElectricGasPayData.merReserved, 
				Base64Pay.encode(PayApp.merReserved.toString().getBytes()));
		return true;
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		payResult(data);
		 if (data == null) {
	            return;
	     }
		 String bankid = data.getStringExtra(BankListActivity.BANK_ID);
		 String bankname = data.getStringExtra(BankListActivity.BANK_NAME);
		 if(null != bankname &&!"".equals(bankname)){
			 isSelectedBank=true;
			 DaikuanActivity.mDaikuanData.sunMap.put(DaikuanData.fucardbank, bankname);
		 }
		 bank_name.setText(bankname);
		 
		 //银联支付结果
		 
	}
	
	private void payResult(Intent data){
		/*
         * 支付控件返回字符串:success、fail、cancel
         *      分别代表支付成功，支付失败，支付取消
         */
		 if (data == null) {
	            return;
	     }
		String msg ="";
        String str = data.getExtras().getString("pay_result");
        if(null == str){
        		return;
        }
        if (str.equalsIgnoreCase("success")) {
            msg = "支付成功！";
        } else if (str.equalsIgnoreCase("fail")) {
            msg = "支付失败！";
        } else if (str.equalsIgnoreCase("cancel")) {
            msg = "用户取消了支付";
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("支付结果通知");
        builder.setMessage(msg);
        builder.setInverseBackgroundForced(true);
        //builder.setCustomTitle();
        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		PayApp.mSwipListener= null;
		if(mBuyTask != null){
			mBuyTask.cancel(true);
		}
//		if(mBillTask != null){
//			mBillTask.cancel(true);
//		}
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		log("onpause endCallStateService");
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
//		setTitle("缴费账单");
		String title="";
		switch(mPayType){
		case 1:
			title = "水费";
			break;
		case 2:
			title = "电费";
			break;
		case 3:
			title = "燃气费";
			break;
		default:
			title = "水费";
		}
		setTitle(title+"账单");
		initSwipPic(PayApp.isSwipIn);
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		log("onStop endCallStateService");
	}

	
	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
		log("onDetach endCallStateService");
	}

	private void showChuxuka(){
//		FragmentTransaction transaction = getFragmentManager().beginTransaction();
//		transaction.replace(R.id.func_container, TransferConfirmFragment.create(mBkntno));
//		transaction.commit();
//		Intent intent = new Intent();
//		intent.putExtra("TN", mBkntno);
//		intent.setClass(getActivity(), UnionpayActivity.class);
//		getActivity().startActivity(intent);
		UnionpayUtil.startUnionPlungin(mBkntno, getActivity());
	}
	
	class BuyTask extends AsyncTask<String, Integer, Boolean>{
		ProtocolRsp mRsp;
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas("ApiUtility", 
						"submitOrder", WaterElectricGasPayActivity.payData);
				WaterElectricGasPayParser authorRegParser = new WaterElectricGasPayParser();
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
				PromptUtil.showToast(getActivity(), getActivity().getString(R.string.net_error));
			}else{
				try {
					List<ProtocolData> mDatas = mRsp.mActions;
					parserResoponse(mDatas);
					if(!ErrorUtil.create().errorDeal(LoginUtil.mLoginStatus,getActivity())){
						return;
					}
					if(LoginUtil.mLoginStatus.result.equals(ProtocolUtil.SUCCESS)){
						WaterElectricGasPayActivity.mCommonData.clear();
						WaterElectricGasPayActivity.mBankNo = mBkntno;
							
							HashMap<String, String> item4 = new HashMap<String, String>();
							item4.put("订单编号:", bundle.getString("orderid"));
							WaterElectricGasPayActivity.mCommonData.add(item4);
							
							HashMap<String, String> item6 = new HashMap<String, String>();
							item6.put("账单金额:", NumberFormatUtil.format2(bundle.getString("factBills"))+"元");
							WaterElectricGasPayActivity.mCommonData.add(item6);
							
							HashMap<String, String> item7 = new HashMap<String, String>();
							item7.put("支付金额:", NumberFormatUtil.format2(bundle.getString("totalBill"))+"元");
							WaterElectricGasPayActivity.mCommonData.add(item7);
							
							HashMap<String, String> item8 = new HashMap<String, String>();
							item8.put("用户编号:", bundle.getString("client"));
							WaterElectricGasPayActivity.mCommonData.add(item8);
							
							HashMap<String, String> item9 = new HashMap<String, String>();
							item9.put("用户姓名:", bundle.getString("username"));
							WaterElectricGasPayActivity.mCommonData.add(item9);
							
							HashMap<String, String> item2 = new HashMap<String, String>();
							item2.put("收费单位:", bundle.getString("company"));
							WaterElectricGasPayActivity.mCommonData.add(item2);
							
							HashMap<String, String> item3 = new HashMap<String, String>();
							item3.put("刷卡卡号:", WaterElectricGasPayActivity.payData.getValue(WaterElectricGasPayData.MRD_RECHABKCARDNO));
							WaterElectricGasPayActivity.mCommonData.add(item3);
							
							HashMap<String, String> item = new HashMap<String, String>();
							item.put("交易请求号:", mBkntno);
							WaterElectricGasPayActivity.mCommonData.add(item);
						showChuxuka();
					}else {
						PromptUtil.showToast(getActivity(), LoginUtil.mLoginStatus.message);
					}
				} catch (Exception e) {
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
				
//				List<ProtocolData> bkntno = data.find("/bkntno");
//				if(bkntno != null){
//					mBkntno = bkntno.get(0).mValue;
//				}
				
				List<ProtocolData> bkntno = data.find("/bkntno");
				if(bkntno != null){
					mBkntno = bkntno.get(0).mValue;
				}
			}
		}
	}
	
	
	class BillTask extends AsyncTask<String, Integer, Boolean>{
		ProtocolRsp mRsp;
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				CommonData data = new CommonData();
				data.putValue("account", bundle.getString("client"));
				data.putValue("proId", bundle.getString("companyId"));
				
				List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas("ApiUtility", 
						"createOrder", data);
				WaterElectricGasBillParser authorRegParser = new WaterElectricGasBillParser();
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
				PromptUtil.showToast(getActivity(), getActivity().getString(R.string.net_error));
			}else{
				try {
					List<ProtocolData> mDatas = mRsp.mActions;
					parserResoponseBill(mDatas);
					if(!ErrorUtil.create().errorDeal(LoginUtil.mLoginStatus,getActivity())){
						return;
					}
					
//					late_fees_tv.setText(mBillData.factBills);
//					pay_money_tv.setText(mBillData.totalBill);
//					client_user_tv.setText(mBillData.username);
					
//					if(LoginUtil.mLoginStatus.result.equals(ProtocolUtil.SUCCESS)){
//					}else {
//						PromptUtil.showToast(getActivity(), LoginUtil.mLoginStatus.message);
//					}
				} catch (Exception e) {
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
	private void parserResoponseBill(List<ProtocolData> params){
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
				
				List<ProtocolData> orderid = data.find("/orderid");
				if(orderid != null){
					mBillData.orderid = orderid.get(0).mValue;
				}
				List<ProtocolData> username = data.find("/username");
				if(username != null){
					mBillData.username = username.get(0).mValue;
				}
				List<ProtocolData> factBills = data.find("/factBills");
				if(factBills != null){
					mBillData.factBills = factBills.get(0).mValue;
				}
				List<ProtocolData> totalBill = data.find("/totalBill");
				if(totalBill != null){
					mBillData.totalBill = totalBill.get(0).mValue;
				}
			}
		}
	}
	
}
