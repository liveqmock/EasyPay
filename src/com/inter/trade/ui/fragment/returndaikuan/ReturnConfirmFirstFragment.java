package com.inter.trade.ui.fragment.returndaikuan;

import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
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
import com.inter.trade.data.CardData;
import com.inter.trade.data.ResponseData;
import com.inter.trade.error.ErrorUtil;
import com.inter.trade.net.HttpUtil;
import com.inter.trade.ui.BankListActivity;
import com.inter.trade.ui.DaikuanActivity;
import com.inter.trade.ui.PayApp;
import com.inter.trade.ui.PayApp.SwipListener;
import com.inter.trade.ui.fragment.BaseFragment;
import com.inter.trade.ui.fragment.cridet.data.JournalData;
import com.inter.trade.ui.fragment.returndaikuan.util.DaikuanData;
import com.inter.trade.ui.fragment.returndaikuan.util.DaikuanNoParser;
import com.inter.trade.util.Base64Pay;
import com.inter.trade.util.Constants;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.NumberFormatUtil;
import com.inter.trade.util.PromptUtil;
import com.inter.trade.util.UserInfoCheck;

public class ReturnConfirmFirstFragment extends BaseFragment implements OnClickListener,SwipListener{
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
	private DaikuanActivity mActivity;
	
	public static ReturnConfirmFirstFragment create(double value,String couponId){
		count = value;
		mCouponId = couponId;
		return new ReturnConfirmFirstFragment();
	}
	
	public ReturnConfirmFirstFragment()
	{
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
		
	}
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		LoginUtil.detection(getActivity());
		View view = inflater.inflate(R.layout.daikuan_swip_layout, container,false);
		initView(view);
		
		setTitle("确认还贷");
		setBackVisible();
//		openReader();
		
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
		
		coupon_layout.setVisibility(View.GONE);
		
		cridet_back_btn.setOnClickListener(this);
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
			mKeyTask = new SwipKeyTask(getActivity(), PayApp.mKeyCode, PayApp.mKeyData);
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
		case R.id.cridet_back_btn:
			if(checkInput()){
//				showChuxuka();
				mBuyTask = new BuyTask();
				mBuyTask.execute("");
//				FragmentTransaction transaction = getFragmentManager().beginTransaction();
//				transaction.replace(R.id.func_container, new ReturnConfirmFragment());
//				transaction.commit();
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
		if(!isSelectedBank){
			PromptUtil.showToast(getActivity(), "请选择银行");
			return false;
		}
		
		
		String cardNumber = card_edit.getText().toString();
		if(null == cardNumber || "".equals(cardNumber)){
			PromptUtil.showToast(getActivity(), "请刷卡获取卡号");
			return false;
		}
		
		if(!UserInfoCheck.checkBandCid(cardNumber)){
			PromptUtil.showToast(getActivity(), "卡号格式不正确");
			return false;
		}
		DaikuanActivity.mDaikuanData.sunMap.put(DaikuanData.fucardno, cardNumber);
		
		
		String openName = open_name_edit.getText().toString();
		if(null == openName || "".equals(openName)){
			PromptUtil.showToast(getActivity(), "请输入开户名");
			return false;
		}
		if(!UserInfoCheck.checkName(openName)){
			PromptUtil.showToast(getActivity(), "姓名格式不正确");
			return false;
		}
		
		DaikuanActivity.mDaikuanData.sunMap.put(DaikuanData.fucardman, openName);
		String openPhone = open_phone_edit.getText().toString();
		if(null == openPhone || "".equals(openPhone)){
			PromptUtil.showToast(getActivity(), "请输入电话号码");
			return false;
		}
		if(!UserInfoCheck.checkMobilePhone(openPhone)){
			PromptUtil.showToast(getActivity(), "手机号码格式不正确");
			return false;
		}
		DaikuanActivity.mDaikuanData.sunMap.put(DaikuanData.fucardmobile, openPhone);
		
//		isSwipIn = true;
		if(PayApp.isSwipIn && PayApp.mKeyData.mType==1){
			PromptUtil.showToast(getActivity(), PayApp.mKeyData.message);
			return false;
		}
		
		if(!PayApp.isSwipIn){
			if(null == PayApp.mKeyCode || "".equals(PayApp.mKeyCode)){
				PromptUtil.showToast(getActivity(), "请刷卡");
			}else{
				PromptUtil.showToast(getActivity(), "请插入刷卡器");
			}
			return false;
		}
		DaikuanActivity.mDaikuanData.sunMap.put(DaikuanData.paycardid, PayApp.mKeyCode);
		DaikuanActivity.mDaikuanData.sunMap.put(JournalData.merReserved, 
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
//		endCallStateService();
//		mobileReader.close();
//		closeReader();
		PayApp.mSwipListener= null;
		if(mBuyTask != null){
			mBuyTask.cancel(true);
		}
//		cancelTimer();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
//		mobileReader.close();
//		endCallStateService();
		log("onpause endCallStateService");
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
//		mobileReader.open();
//		openReaderNow();
		setTitle("确认还贷");
		initSwipPic(PayApp.isSwipIn);
//		startCallStateService();
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
//		mobileReader.close();
//		endCallStateService();
		log("onStop endCallStateService");
	}

	
	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
//		mobileReader.close();
//		endCallStateService();
		log("onDetach endCallStateService");
	}

	private void showChuxuka(){
		
		Intent intent = new Intent();
		intent.putExtra("TN", mBkntno);
		intent.setClass(getActivity(), DaikuanActivity.class);
		getActivity().startActivityForResult(intent, Constants.ACTIVITY_FINISH);
//		UnionpayUtil.startUnionPlungin(mBkntno, getActivity());
	}
	
	class BuyTask extends AsyncTask<String, Integer, Boolean>{
		ProtocolRsp mRsp;
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas("ApiPayinfo", 
						"RepayMoneyRq", DaikuanActivity.mDaikuanData);
				DaikuanNoParser authorRegParser = new DaikuanNoParser();
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
							DaikuanActivity.mCommonData.clear();
							DaikuanActivity.mBankNo = mBkntno;
							HashMap<String, String> item = new HashMap<String, String>();
							item.put("交易请求号", mBkntno);
							DaikuanActivity.mCommonData.add(item);
							
							HashMap<String, String> item1 = new HashMap<String, String>();
							item1.put("还贷银行",
									DaikuanActivity.mDaikuanData.getValue(DaikuanData.shoucardbank)
									);
							DaikuanActivity.mCommonData.add(item1);
							
							HashMap<String, String> item2 = new HashMap<String, String>();
							item2.put("收款银行卡", DaikuanActivity.mDaikuanData.getValue(DaikuanData.shoucardno));
							DaikuanActivity.mCommonData.add(item2);
							
							HashMap<String, String> item3 = new HashMap<String, String>();
							item3.put("付款借记卡", DaikuanActivity.mDaikuanData.getValue(DaikuanData.fucardno));
							DaikuanActivity.mCommonData.add(item3);
							
							HashMap<String, String> item4 = new HashMap<String, String>();
							item4.put("还贷金额", NumberFormatUtil.format2(DaikuanActivity.mDaikuanData.getValue(DaikuanData.paymoney)));
							DaikuanActivity.mCommonData.add(item4);
							
							HashMap<String, String> item5 = new HashMap<String, String>();
							item5.put("手续费", NumberFormatUtil.format2(DaikuanActivity.mDaikuanData.getValue(DaikuanData.payfee)));
							DaikuanActivity.mCommonData.add(item5);
							
							HashMap<String, String> item6 = new HashMap<String, String>();
							item6.put("刷卡金额", NumberFormatUtil.format2(DaikuanActivity.mDaikuanData.getValue(DaikuanData.money)));
							DaikuanActivity.mCommonData.add(item6);
						showChuxuka();
					}else {
						PromptUtil.showToast(getActivity(), LoginUtil.mLoginStatus.message);
					}
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
				
				List<ProtocolData> bkntno = data.find("/bkntno");
				if(bkntno != null){
					mBkntno = bkntno.get(0).mValue;
				}
			}
		}
	}
	
	
}
