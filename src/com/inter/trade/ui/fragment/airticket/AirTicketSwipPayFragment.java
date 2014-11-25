package com.inter.trade.ui.fragment.airticket;

import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.inter.trade.R;
import com.inter.trade.SwipKeyTask;
import com.inter.trade.data.AirTicketCreateOrderData;
import com.inter.trade.data.CardData;
import com.inter.trade.data.ResponseData;
import com.inter.trade.data.ResultData;
import com.inter.trade.error.ErrorUtil;
import com.inter.trade.net.HttpUtil;
import com.inter.trade.ui.BankListActivity;
import com.inter.trade.ui.PayApp;
import com.inter.trade.ui.PayApp.BuySuccessListener;
import com.inter.trade.ui.PayApp.SwipListener;
import com.inter.trade.ui.TelephonePayActivity;
import com.inter.trade.ui.fragment.BaseFragment;
import com.inter.trade.ui.fragment.airticket.util.ApiAirticketCreateOrderParser;
import com.inter.trade.ui.fragment.coupon.task.BuySuccessTask;
import com.inter.trade.ui.fragment.telephone.util.MoblieRechangeData;
import com.inter.trade.ui.fragment.telephone.util.MoblieRechangeNoParser;
import com.inter.trade.ui.manager.UIManagerActivity;
import com.inter.trade.util.Base64Pay;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.NumberFormatUtil;
import com.inter.trade.util.PromptUtil;
import com.inter.trade.util.UserInfoCheck;
import com.unionpay.uppayplugin.demo.UnionpayUtil;

/**
 * 刷卡支付页面
 * @author zhichao.huang
 *
 */
public class AirTicketSwipPayFragment extends BaseFragment implements OnClickListener,SwipListener,BuySuccessListener{
	private Button cridet_back_btn;
	private ImageView swip_card;
	private TextView swip_prompt;
	private EditText card_edit;
	private LinearLayout bank_layout;
	private TextView bank_name;
	private EditText open_name_edit;
	private EditText open_phone_edit;
	private TextView acount;
	
	private String mBkntno;
	private String mMessage ="";
	private String mResult =""; 
	private static double count =0;
	private static String mCouponId;
	private boolean isSelectedBank=false;
	private LinearLayout coupon_layout;
	
	private BuyTask mBuyTask;
	
	private Bundle bundle;
	
	public static AirTicketSwipPayFragment create(double value,String couponId){
		count = value;
		mCouponId = couponId;
		return new AirTicketSwipPayFragment();
	}
	
	public static AirTicketSwipPayFragment newInstance (Bundle data) {
		AirTicketSwipPayFragment fragment = new AirTicketSwipPayFragment();
		fragment.setArguments(data);
		return fragment;
	}
	
//	public AirTicketSwipPayFragment()
//	{
//	}
//	
//	public AirTicketSwipPayFragment(Bundle b) {
//		this.bundle = b;
//	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		PayApp.mSwipListener = this;
		Bundle data = getArguments();
		if(data != null) {
			bundle = data;
		}
	}
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		LoginUtil.detection(getActivity());
		
		((UIManagerActivity)getActivity()).setTopTitle("刷卡购票");
		
		View view = inflater.inflate(R.layout.telephone_swip_layout, container,false);
		initView(view);
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
		cridet_back_btn = (Button)view.findViewById(R.id.cridet_back_btn);
		swip_card = (ImageView)view.findViewById(R.id.swip_card);
		swip_prompt = (TextView)view.findViewById(R.id.swip_prompt);
		card_edit = (EditText)view.findViewById(R.id.card_edit);
		bank_layout = (LinearLayout)view.findViewById(R.id.bank_layout);
		bank_name = (TextView)view.findViewById(R.id.bank_name);
		acount = (TextView)view.findViewById(R.id.acount);
		open_phone_edit = (EditText)view.findViewById(R.id.open_phone_edit);
		open_name_edit = (EditText)view.findViewById(R.id.open_name_edit);
		coupon_layout =  (LinearLayout)view.findViewById(R.id.coupon_layout);
		
		if(bundle != null) {
			((TextView)view.findViewById(R.id.mobile_number)).setText(bundle.getString(MoblieRechangeData.MRD_RECHAMOBILE));
			((TextView)view.findViewById(R.id.mobile_rechamobileprov)).setText(bundle.getString(MoblieRechangeData.MRD_RECHAMOBILEPROV));
			((TextView)view.findViewById(R.id.mobile_rechamoney)).setText(bundle.getString(MoblieRechangeData.MRD_RECHAMONEY)+"元");
			((TextView)view.findViewById(R.id.mobile_rechapaymoney)).setText(bundle.getString(MoblieRechangeData.MRD_RECHAPAYMONEY)+"元");
		}
		
		coupon_layout.setVisibility(View.GONE);
		
		cridet_back_btn.setOnClickListener(this);
		bank_layout.setOnClickListener(this);
		
	}
	@Override
	public void recieveCard(CardData data) {
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
		switch (status) {
		case SWIPING_START:
			PromptUtil.showToast(getActivity(), message);
			break;
		case SWIPING_FAIL:
			PromptUtil.showToast(getActivity(), message);
					break;
		case SWIPING_SUCCESS:
			PromptUtil.showToast(getActivity(), message);
			mKeyTask = new SwipKeyTask(getActivity(), PayApp.mKeyCode, PayApp.mKeyData);
			mKeyTask.execute("");
			
			break;
		default:
			break;
		}
		
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.cridet_back_btn:
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
		if(null == cardNumber || "".equals(cardNumber)){
			PromptUtil.showToast(getActivity(), "请刷卡或输入卡号");
			return false;
		}
		
		if(!UserInfoCheck.checkBankCard(cardNumber)){
			PromptUtil.showToast(getActivity(), "卡号格式不正确");
			return false;
		}
		TelephonePayActivity.moblieRechangeData.sunMap.put(MoblieRechangeData.MRD_RECHABKCARDNO, cardNumber);
		
//		if(bundle == null) {
//			PromptUtil.showToast(getActivity(), "操作异常");
//			return false;
//		}
		
		TelephonePayActivity.moblieRechangeData.sunMap.put(MoblieRechangeData.MRD_RECHAMONEY, "50");
		TelephonePayActivity.moblieRechangeData.sunMap.put(MoblieRechangeData.MRD_RECHAPAYMONEY, "30");
		TelephonePayActivity.moblieRechangeData.sunMap.put(MoblieRechangeData.MRD_RECHAMOBILE, "13866666666");
		TelephonePayActivity.moblieRechangeData.sunMap.put(MoblieRechangeData.MRD_RECHAMOBILEPROV, "中国赣州");
		//支付类型id ，默认为1
		TelephonePayActivity.moblieRechangeData.sunMap.put(MoblieRechangeData.MRD_RECHAPAYTYPEID, 1+"");
		
//		Log.i("mobile", bundle.getString(MoblieRechangeData.MRD_RECHAMONEY)+ "-"+bundle.getString(MoblieRechangeData.MRD_RECHAPAYMONEY)+"-"+
//		bundle.getString(MoblieRechangeData.MRD_RECHAMOBILE)+"-"+ bundle.getString(MoblieRechangeData.MRD_RECHAMOBILEPROV));
		
		
//		String openName = open_name_edit.getText().toString();
//		if(null == openName || "".equals(openName)){
//			PromptUtil.showToast(getActivity(), "请输入开户名");
//			return false;
//		}
//		if(!UserInfoCheck.checkName(openName)){
//			PromptUtil.showToast(getActivity(), "姓名格式不正确");
//			return false;
//		}
//		
//		DaikuanActivity.mDaikuanData.sunMap.put(DaikuanData.fucardman, openName);
//		String openPhone = open_phone_edit.getText().toString();
//		if(null == openPhone || "".equals(openPhone)){
//			PromptUtil.showToast(getActivity(), "请输入电话号码");
//			return false;
//		}
//		if(!UserInfoCheck.checkMobilePhone(openPhone)){
//			PromptUtil.showToast(getActivity(), "手机号码格式不正确");
//			return false;
//		}
//		DaikuanActivity.mDaikuanData.sunMap.put(DaikuanData.fucardmobile, openPhone);
		
//		isSwipIn = true;
		if(PayApp.isSwipIn && PayApp.mKeyData.mType==1){
			PromptUtil.showToast(getActivity(), PayApp.mKeyData.message);
			return false;
		}
		
//		if(!PayApp.isSwipIn){
//			if(null == PayApp.mKeyCode || "".equals(PayApp.mKeyCode)){
//				PromptUtil.showToast(getActivity(), "请刷卡");
//			}else{
//				PromptUtil.showToast(getActivity(), "请插入刷卡器");
//			}
//			return false;
//		}
		TelephonePayActivity.moblieRechangeData.sunMap.put(MoblieRechangeData.MRD_PAYCARDID, PayApp.mKeyCode != null ? PayApp.mKeyCode : "");
		TelephonePayActivity.moblieRechangeData.sunMap.put(MoblieRechangeData.merReserved, 
				Base64Pay.encode(PayApp.merReserved.toString().getBytes()));
		return true;
	}
	
	
//	private boolean checkInput(){
//		
//		String cardNumber = card_edit.getText().toString();
//		if(null == cardNumber || "".equals(cardNumber)){
//			PromptUtil.showToast(getActivity(), "请刷卡或输入卡号");
//			return false;
//		}
//		
//		if(!UserInfoCheck.checkBankCard(cardNumber)){
//			PromptUtil.showToast(getActivity(), "卡号格式不正确");
//			return false;
//		}
//		TelephonePayActivity.moblieRechangeData.sunMap.put(MoblieRechangeData.MRD_RECHABKCARDNO, cardNumber);
//		
//		if(bundle == null) {
//			PromptUtil.showToast(getActivity(), "操作异常");
//			return false;
//		}
//		
//		TelephonePayActivity.moblieRechangeData.sunMap.put(MoblieRechangeData.MRD_RECHAMONEY, bundle.getString(MoblieRechangeData.MRD_RECHAMONEY));
//		TelephonePayActivity.moblieRechangeData.sunMap.put(MoblieRechangeData.MRD_RECHAPAYMONEY, bundle.getString(MoblieRechangeData.MRD_RECHAPAYMONEY));
//		TelephonePayActivity.moblieRechangeData.sunMap.put(MoblieRechangeData.MRD_RECHAMOBILE, bundle.getString(MoblieRechangeData.MRD_RECHAMOBILE));
//		TelephonePayActivity.moblieRechangeData.sunMap.put(MoblieRechangeData.MRD_RECHAMOBILEPROV, bundle.getString(MoblieRechangeData.MRD_RECHAMOBILEPROV));
//		//支付类型id ，默认为1
//		TelephonePayActivity.moblieRechangeData.sunMap.put(MoblieRechangeData.MRD_RECHAPAYTYPEID, 1+"");
//		
//		Log.i("mobile", bundle.getString(MoblieRechangeData.MRD_RECHAMONEY)+ "-"+bundle.getString(MoblieRechangeData.MRD_RECHAPAYMONEY)+"-"+
//		bundle.getString(MoblieRechangeData.MRD_RECHAMOBILE)+"-"+ bundle.getString(MoblieRechangeData.MRD_RECHAMOBILEPROV));
//		
//		
////		String openName = open_name_edit.getText().toString();
////		if(null == openName || "".equals(openName)){
////			PromptUtil.showToast(getActivity(), "请输入开户名");
////			return false;
////		}
////		if(!UserInfoCheck.checkName(openName)){
////			PromptUtil.showToast(getActivity(), "姓名格式不正确");
////			return false;
////		}
////		
////		DaikuanActivity.mDaikuanData.sunMap.put(DaikuanData.fucardman, openName);
////		String openPhone = open_phone_edit.getText().toString();
////		if(null == openPhone || "".equals(openPhone)){
////			PromptUtil.showToast(getActivity(), "请输入电话号码");
////			return false;
////		}
////		if(!UserInfoCheck.checkMobilePhone(openPhone)){
////			PromptUtil.showToast(getActivity(), "手机号码格式不正确");
////			return false;
////		}
////		DaikuanActivity.mDaikuanData.sunMap.put(DaikuanData.fucardmobile, openPhone);
//		
////		isSwipIn = true;
//		if(PayApp.isSwipIn && PayApp.mKeyData.mType==1){
//			PromptUtil.showToast(getActivity(), PayApp.mKeyData.message);
//			return false;
//		}
//		
////		if(!PayApp.isSwipIn){
////			if(null == PayApp.mKeyCode || "".equals(PayApp.mKeyCode)){
////				PromptUtil.showToast(getActivity(), "请刷卡");
////			}else{
////				PromptUtil.showToast(getActivity(), "请插入刷卡器");
////			}
////			return false;
////		}
//		TelephonePayActivity.moblieRechangeData.sunMap.put(MoblieRechangeData.MRD_PAYCARDID, PayApp.mKeyCode != null ? PayApp.mKeyCode : "");
//		TelephonePayActivity.moblieRechangeData.sunMap.put(MoblieRechangeData.merReserved, 
//				Base64Pay.encode(PayApp.merReserved.toString().getBytes()));
//		return true;
//	}
//	@Override
//	public void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
//		payResult(data);
//		 if (data == null) {
//	            return;
//	     }
//		 String bankid = data.getStringExtra(BankListActivity.BANK_ID);
//		 String bankname = data.getStringExtra(BankListActivity.BANK_NAME);
//		 if(null != bankname &&!"".equals(bankname)){
//			 isSelectedBank=true;
//			 DaikuanActivity.mDaikuanData.sunMap.put(DaikuanData.fucardbank, bankname);
//		 }
//		 bank_name.setText(bankname);
//		 
//		 //银联支付结果
//		 
//	}
//	
//	private void payResult(Intent data){
//		/*
//         * 支付控件返回字符串:success、fail、cancel
//         *      分别代表支付成功，支付失败，支付取消
//         */
//		 if (data == null) {
//	            return;
//	     }
//		String msg ="";
//        String str = data.getExtras().getString("pay_result");
//        if(null == str){
//        		return;
//        }
//        if (str.equalsIgnoreCase("success")) {
//            msg = "支付成功！";
//        } else if (str.equalsIgnoreCase("fail")) {
//            msg = "支付失败！";
//        } else if (str.equalsIgnoreCase("cancel")) {
//            msg = "用户取消了支付";
//        }
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setTitle("支付结果通知");
//        builder.setMessage(msg);
//        builder.setInverseBackgroundForced(true);
//        //builder.setCustomTitle();
//        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//        builder.create().show();
//	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		PayApp.mSwipListener= null;
		if(mBuyTask != null){
			mBuyTask.cancel(true);
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		log("onpause endCallStateService");
	}

	@Override
	public void onResume() {
		super.onResume();
		((UIManagerActivity)getActivity()).setTopTitle("刷卡购票");
		initSwipPic(PayApp.isSwipIn);
	}

	@Override
	public void onStop() {
		super.onStop();
		log("onStop endCallStateService");
	}

	
	@Override
	public void onDetach() {
		super.onDetach();
		log("onDetach endCallStateService");
	}

	private void showChuxuka(){
		UnionpayUtil.startUnionPlungin(mBkntno, getActivity());
	}
	
	class BuyTask extends AsyncTask<String, Integer, Boolean>{
		ProtocolRsp mRsp;
		@Override
		protected Boolean doInBackground(String... params) {
			try {
				
				AirTicketCreateOrderData airTicketCreateOrderData = new AirTicketCreateOrderData();
				airTicketCreateOrderData.ticketMap.put("amout", "100");
				airTicketCreateOrderData.ticketMap.put("departCityId", "bj");
				airTicketCreateOrderData.ticketMap.put("arriveCityId", "sh");
				airTicketCreateOrderData.ticketMap.put("departPortCode", "xxx");
				airTicketCreateOrderData.ticketMap.put("arrivePortCode", "yyy");
				airTicketCreateOrderData.ticketMap.put("airlineCode", "code");
				airTicketCreateOrderData.ticketMap.put("flight", "kb1024");
				airTicketCreateOrderData.ticketMap.put("class", "Y");
				airTicketCreateOrderData.ticketMap.put("takeOffTime", "2014-2-2");
				airTicketCreateOrderData.ticketMap.put("arriveTime", "2014-2-3");
				airTicketCreateOrderData.ticketMap.put("rate", "0.7");
				airTicketCreateOrderData.ticketMap.put("price", "1240");
				airTicketCreateOrderData.ticketMap.put("tax", "30");
				airTicketCreateOrderData.ticketMap.put("oilFee", "80");
				
				for(int i = 0; i < 2; i ++) {
					HashMap<String, String> passenger = new HashMap<String, String>();
					passenger.put("passengerId", "ADU");
					passenger.put("name", "justone");
					passenger.put("birthDay", "1992-01-01");
					passenger.put("passportTypeId", "1");
					passenger.put("passportNo", "123456789019890603");
					passenger.put("gender", "man");
					passenger.put("telephone", "13812345678");
					
					airTicketCreateOrderData.passengerList.add(passenger);
				}
				
				List<ProtocolData> mDatas = ProtocolUtil.getCustomRequestDatas("ApiAirticket", 
						"createOrder", airTicketCreateOrderData);
				
				ApiAirticketCreateOrderParser authorRegParser = new ApiAirticketCreateOrderParser();
				mRsp = HttpUtil.doRequest(authorRegParser, mDatas);
			} catch (Exception e) {
				e.printStackTrace();
				mRsp =null;
			}
			
			return null;
		}

		@Override
		protected void onPostExecute(Boolean result) {
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
						TelephonePayActivity.mCommonData.clear();
						UIManagerActivity.mBankNo = mBkntno;
							
							HashMap<String, String> item5 = new HashMap<String, String>();
							item5.put("充值号码:", TelephonePayActivity.moblieRechangeData.getValue(MoblieRechangeData.MRD_RECHAMOBILE));
							TelephonePayActivity.mCommonData.add(item5);
							
							HashMap<String, String> item8 = new HashMap<String, String>();
							item8.put("号码归属:", TelephonePayActivity.moblieRechangeData.getValue(MoblieRechangeData.MRD_RECHAMOBILEPROV));
							TelephonePayActivity.mCommonData.add(item8);
							
							HashMap<String, String> item6 = new HashMap<String, String>();
							item6.put("充值金额:", NumberFormatUtil.format2(TelephonePayActivity.moblieRechangeData.getValue(MoblieRechangeData.MRD_RECHAMONEY)));
							TelephonePayActivity.mCommonData.add(item6);
							
							HashMap<String, String> item7 = new HashMap<String, String>();
							item7.put("实际支付金额:", NumberFormatUtil.format2(TelephonePayActivity.moblieRechangeData.getValue(MoblieRechangeData.MRD_RECHAPAYMONEY)));
							TelephonePayActivity.mCommonData.add(item7);
							
							HashMap<String, String> item3 = new HashMap<String, String>();
							item3.put("刷卡卡号:", TelephonePayActivity.moblieRechangeData.getValue(MoblieRechangeData.MRD_RECHABKCARDNO));
							TelephonePayActivity.mCommonData.add(item3);
							
							HashMap<String, String> item = new HashMap<String, String>();
							item.put("交易请求号:", mBkntno);
							TelephonePayActivity.mCommonData.add(item);
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
				
				List<ProtocolData> bkntno = data.find("/bkntno");
				if(bkntno != null){
					mBkntno = bkntno.get(0).mValue;
				}
			}
		}
	}
	
	@Override
	public void requestBuySuccess(final ResultData resultData, final String msg) {
		new BuySuccessTask(getActivity(), resultData,"ApiMoblieRechangeInfo","checkRechaMoneyStatus").execute("");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("支付结果通知");
        builder.setMessage(msg);
        builder.setInverseBackgroundForced(true);
        builder.setCancelable(false);
        //builder.setCustomTitle();
        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (resultData.getValue(ResultData.result).equalsIgnoreCase("success")) {
//	                	 FragmentTransaction transaction = TelephonePayActivity.this.getSupportFragmentManager().beginTransaction();
//	 	        		transaction.replace(R.id.func_container,TelephonePaySuccessFragment.createFragment(mCommonData));
//	 	        		transaction.commit();
                }else{
//                		getActivity().finish();
                }
               
            }
        });
        builder.create().show();
	}
	
	
}
