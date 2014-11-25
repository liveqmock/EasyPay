package com.inter.trade.ui.fragment.qmoney;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.inter.trade.data.ArriveData;
import com.inter.trade.data.CommonData;
import com.inter.trade.data.ResponseData;
import com.inter.trade.data.TaskData;
import com.inter.trade.error.ErrorUtil;
import com.inter.trade.net.HttpUtil;
import com.inter.trade.ui.BankListActivity;
import com.inter.trade.ui.PayApp;
import com.inter.trade.ui.QMoneyPayActivity;
import com.inter.trade.ui.QMoneyPayRecordActivity;
//import com.inter.trade.ui.TelephonePayActivity;
import com.inter.trade.ui.fragment.BaseFragment;
import com.inter.trade.ui.fragment.cridet.CridetCardFragment;
import com.inter.trade.ui.fragment.cridet.data.JournalData;
import com.inter.trade.ui.fragment.cridet.task.CreditCardfeeTask;
import com.inter.trade.ui.fragment.cridet.task.CreditCardfeeTask.FeeListener;
import com.inter.trade.ui.fragment.order.util.OrderData;
import com.inter.trade.ui.fragment.qmoney.util.QMoneyData;
import com.inter.trade.ui.fragment.qmoney.util.QMoneyDenominationData;
import com.inter.trade.ui.fragment.qmoney.util.QMoneyDenominationParser;
import com.inter.trade.ui.fragment.returndaikuan.util.DaikuanfeeParser;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.NumberFormatUtil;
import com.inter.trade.util.PromptUtil;
//import com.inter.trade.ui.TelphonePayRecordActivity;

/**
 * Q币充值
 * @author Lihaifeng
 *
 */
public class QMoneyPayMainFragment extends BaseFragment implements OnClickListener,/**SwipListener*/ FeeListener{
	
	private Button cridet_back_btn;
	private ImageView swip_card;
	private TextView swip_prompt;
	private EditText card_edit;
	private LinearLayout bank_layout;
	private TextView bank_name;
	private TextView acount;
	
	private LinearLayout query_layout;
	private EditText query_input;
	private Button send;
	private EditText telephone_edit;//QQ号码输入框
	private EditText qmoney_edit;//Q币数额输入框
	private LinearLayout chongzhi10, chongzhi20, chongzhi30, chongzhi50, 
	chongzhi100, chongzhi200, chongzhi300, chongzhi400, chongzhi500;//充值10元Layout
	private TextView chongzhi10_button, chongzhi20_button, chongzhi30_button,
	chongzhi50_button, chongzhi100_button, chongzhi200_button, chongzhi300_button, chongzhi400_button, chongzhi500_button;//充值10元按钮
	
	
	//QQ号码
	private int qqNumber = 0;
	//充值的金额
	private int money = 0;
	//实际支付的金额
	private double payMoney;
	//优惠百分比
	private double persent=1;
	
	private TextView payMoneyTextView;
	
	
	
	private OrderData mOrderData;
	
	private CommonData mData = new CommonData();
	
	/**
	 * 交易流水号 订单号
	 */
	private String mBkntno;
	
	public CommonData mfeeData=new CommonData();
	
	/**
	 * 充值面额集合
	 */
	private ArrayList<QMoneyDenominationData> mList = new ArrayList<QMoneyDenominationData>();
	
	public QMoneyPayMainFragment()
	{
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		LoginUtil.detection(getActivity());
//		initReader();
//		PayApp.mSwipListener = this;
		
	}
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		LoginUtil.detection(getActivity());
		View view = inflater.inflate(R.layout.qmoney_new_layout, container,false);
		initView(view);
		
		setTitle("Q币充值");
		setBackVisible();
		setRightVisible(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				PromptUtil.showToast(getActivity(), "该功能正在开发...");
				Intent intent = new Intent();
				intent.setClass(getActivity(), QMoneyPayRecordActivity.class);
				getActivity().startActivityForResult(intent, 200);
			}
		}, "历史记录");
//		openReader();
		/**
		 * 获取话费充值面额选择
		mBuyTask = new BuyTask();
		mBuyTask.execute("");
		*/
		return view;
	}
	
	private void initView(View view){
		cridet_back_btn = (Button)view.findViewById(R.id.cridet_back_btn);
		swip_card = (ImageView)view.findViewById(R.id.swip_card);
		swip_prompt = (TextView)view.findViewById(R.id.swip_prompt);
		card_edit = (EditText)view.findViewById(R.id.card_edit);
		bank_layout = (LinearLayout)view.findViewById(R.id.bank_layout);
		bank_name = (TextView)view.findViewById(R.id.bank_name);
		acount = (TextView)view.findViewById(R.id.acount);
		
		//addressbook = (Button)view.findViewById(R.id.addressbook);
		telephone_edit = (EditText)view.findViewById(R.id.telephone_edit);
		qmoney_edit = (EditText)view.findViewById(R.id.qmoney_edit);
		
		chongzhi10 = (LinearLayout)view.findViewById(R.id.chongzhi10);
		chongzhi10_button = (TextView)view.findViewById(R.id.chongzhi10_button);
		chongzhi20 = (LinearLayout)view.findViewById(R.id.chongzhi20);
		chongzhi20_button = (TextView)view.findViewById(R.id.chongzhi20_button);
		chongzhi30 = (LinearLayout)view.findViewById(R.id.chongzhi30);
		chongzhi30_button = (TextView)view.findViewById(R.id.chongzhi30_button);
		chongzhi50 = (LinearLayout)view.findViewById(R.id.chongzhi50);
		chongzhi50_button = (TextView)view.findViewById(R.id.chongzhi50_button);
		chongzhi100 = (LinearLayout)view.findViewById(R.id.chongzhi100);
		chongzhi100_button = (TextView)view.findViewById(R.id.chongzhi100_button);
		chongzhi200 = (LinearLayout)view.findViewById(R.id.chongzhi200);
		chongzhi200_button = (TextView)view.findViewById(R.id.chongzhi200_button);
		chongzhi300 = (LinearLayout)view.findViewById(R.id.chongzhi300);
		chongzhi300_button = (TextView)view.findViewById(R.id.chongzhi300_button);
		chongzhi400 = (LinearLayout)view.findViewById(R.id.chongzhi400);
		chongzhi400_button = (TextView)view.findViewById(R.id.chongzhi400_button);
		chongzhi500 = (LinearLayout)view.findViewById(R.id.chongzhi500);
		chongzhi500_button = (TextView)view.findViewById(R.id.chongzhi500_button);
		
		payMoneyTextView = (TextView)view.findViewById(R.id.payMoney);
		
		
		query_layout = (LinearLayout)view.findViewById(R.id.query_layout);
		query_input = (EditText)view.findViewById(R.id.query_input);
		send = (Button)view.findViewById(R.id.send);
		
		
		send.setOnClickListener(this);
		cridet_back_btn.setOnClickListener(this);
		bank_layout.setOnClickListener(this);
		//addressbook.setOnClickListener(this);
		chongzhi10.setOnClickListener(this);
		chongzhi10_button.setOnClickListener(this);
		chongzhi20.setOnClickListener(this);
		chongzhi20_button.setOnClickListener(this);
		chongzhi30.setOnClickListener(this);
		chongzhi30_button.setOnClickListener(this);
		chongzhi50.setOnClickListener(this);
		chongzhi50_button.setOnClickListener(this);
		chongzhi100.setOnClickListener(this);
		chongzhi100_button.setOnClickListener(this);
		chongzhi200.setOnClickListener(this);
		chongzhi200_button.setOnClickListener(this);
		
		chongzhi300.setOnClickListener(this);
		chongzhi300_button.setOnClickListener(this);
		chongzhi400.setOnClickListener(this);
		chongzhi400_button.setOnClickListener(this);
		chongzhi500.setOnClickListener(this);
		chongzhi500_button.setOnClickListener(this);
		
		telephone_edit.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				Log.i("Hello", "onTextChanged:" + s + "-" + start + "-" + count + "-" + before); 
				//Integer.parseInt(s.toString());
				/*
				boolean isMobile;
				if(start == 10){//当用户输入号码11位时，触发该动作
					isMobile = isMobileNum(s.toString());
					if(isMobile){
						//查询手机归属地
						//mobileQueryAttribution(s.toString());
						defaultMobileMoney();
					}else{
						mobileAttribution.setVisibility(View.GONE);
						PromptUtil.showToast(getActivity(), "请输入正确的QQ号码");
					}
				}
				*/
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				
			}
		});
		
		qmoney_edit.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				Log.i("Hello", "onTextChanged:" + s + "-" + start + "-" + count + "-" + before); 
				/*
				boolean isMobile;
				if(start == 10){//当用户输入号码11位时，触发该动作
					isMobile = isMobileNum(s.toString());
					if(isMobile){
						//查询
						//mobileQueryAttribution(s.toString());
						defaultMobileMoney();
					}else{
						mobileAttribution.setVisibility(View.GONE);
						PromptUtil.showToast(getActivity(), "请输入正确的Q币数额");
					}
				}
				*/
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				
			}
		});
		
	}
	
	


	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.cridet_back_btn:
			if(checkInput()){
				invokeTelephonePayConfirm ();
			}
			break;
		case R.id.bank_layout:
			showBankList();
			break;
		case R.id.send:
//			sendSms();
			break;
		case R.id.addressbook://通讯录
		    addressBook();
			break;
		
		default:
			break;
		}
	}
	
	/**
	 * 调度手机通讯录
	 */
	private void addressBook() {
		Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
		startActivityForResult(intent, 2);
	}
	
	/**
	 * 调度到话费充值确认界面，也就是刷卡支付页面
	 */
	private void invokeTelephonePayConfirm () {
		Intent intent = new Intent();
		intent.setClass(getActivity(), QMoneyPayActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString(QMoneyData.MRD_RECHAMOBILE,telephone_edit.getText().toString());
		//bundle.putString(QMoneyData.MRD_RECHAMOBILEPROV, mobileAttributionStr);
		bundle.putString(QMoneyData.MRD_RECHAMONEY, money+"");
		bundle.putString(QMoneyData.MRD_RECHAPAYMONEY, payMoney+"");
		intent.putExtra("MoblieRechange", bundle);
		startActivityForResult(intent, 3);
	}
	
	private void showBankList()
	{
		Intent intent = new Intent();
		intent.putExtra("type", "1");
		intent.setClass(getActivity(), BankListActivity.class);
		startActivityForResult(intent, 1);
	}
	
	//QQ号码设置为5到15位纯数字，QQ号首位可以是0，但首2位不能都是0
	private boolean isMobileNum(String mobile) {
		int len = mobile.length();
		if(len<5 || len>15){
			return false;
		}
		String second=mobile.substring(0,2);
		if("00".equals(second)){
			return false;
		}else{
			return true;
		}
			
		/* 手机号码
		 Pattern p = Pattern
	                .compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
	        Matcher m = p.matcher(mobile);
	        return m.matches();
	    */
	}
	
	private boolean checkInput(){
		
		String telnumber = telephone_edit.getText().toString();
		String qmoney = qmoney_edit.getText().toString();
		//money=(double)Integer.parseInt(qmoney);
		//payMoney=money*persent;
		
		if(null == telnumber || "".equals(telnumber) //|| telephone_edit.length() != 11
				|| !isMobileNum(telnumber)){
			PromptUtil.showToast(getActivity(), "请输入正确的QQ号码");
			return false;
		}
		
		if(null == qmoney || "".equals(qmoney)) {
			PromptUtil.showToast(getActivity(), "请输入正确的充值金额");
			return false;
		}
		
		money=Integer.parseInt(qmoney);
		
		if(money>5000) {
			PromptUtil.showToast(getActivity(), "单笔充值最大为5000");
			return false;
		}
		
		payMoney=money*persent;
		if( money <= 0.0 || payMoney <= 0.0) {
			PromptUtil.showToast(getActivity(), "请输入正确的充值金额");
			return false;
		}
		
		CridetCardFragment.mJournalData.putValue(JournalData.paycardid, PayApp.mKeyCode);
		CridetCardFragment.mJournalData.putValue(JournalData.merReserved, PayApp.merReserved.toString());
		return true;
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (data == null) {
			return;
		}
		 
		 //银联支付结果
		 
		 switch (requestCode) { 
		 case 2: 
			 if (data == null) { 
				 return; 
			 } 
			 String phoneNumber = null; 
			 Uri contactData = data.getData(); 
			 if (contactData == null) { 
				 return ; 
			 } 
			 Cursor cursor = getActivity().managedQuery(contactData, null, null, null, null); 
			 if (cursor.moveToFirst()) { 
				 //	                  String name = cursor.getString(cursor 
						 //	                          .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)); 
				 String hasPhone = cursor 
						 .getString(cursor 
								 .getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)); 
				 String id = cursor.getString(cursor 
						 .getColumnIndex(ContactsContract.Contacts._ID)); 
				 if (hasPhone.equalsIgnoreCase("1")) { 
					 hasPhone = "true"; 
				 } else { 
					 hasPhone = "false"; 
				 } 
				 if (Boolean.parseBoolean(hasPhone)) { 
					 Cursor phones = getActivity().getContentResolver().query( 
							 ContactsContract.CommonDataKinds.Phone.CONTENT_URI, 
							 null, 
							 ContactsContract.CommonDataKinds.Phone.CONTACT_ID 
							 + " = " + id, null, null); 
					 while (phones.moveToNext()) { 
						 phoneNumber = phones 
								 .getString(phones 
										 .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)); 
					 } 
					 phones.close(); 
				 } 
			 } 
			 
			 //模拟带连接符“-”的手机号码，如：138-8888-8888
//			 String tempNum = "0138-8888-88-88";
			 String tempNum = phoneNumber;
			 String[] phoneNumbers = tempNum.split("-");
			 
			 String tempPhoneNumber = "";
			 for(int i = 0; i < phoneNumbers.length ; i ++){
				 tempPhoneNumber +=  phoneNumbers[i];
			 }
			 if(tempPhoneNumber != null && !tempPhoneNumber.equals("")){
				 if(tempPhoneNumber.length() != 11){
					 String num = tempPhoneNumber.substring(0, 1);
					 String tempNumber = "";
					 if( num.equals("0") ){
						 tempNumber =  tempPhoneNumber.substring(1);
						 if(isMobileNum(tempNumber)){
							 telephone_edit.setText(tempNumber);
							 mobileQueryAttribution(tempNumber);
						 }else{
							 PromptUtil.showToast(getActivity(), "号码格式不正确");
						 }
					 }
				 }else{
					 if(isMobileNum(tempPhoneNumber)){
						 telephone_edit.setText(tempPhoneNumber);
						 mobileQueryAttribution(tempPhoneNumber);
					 }else{
						 PromptUtil.showToast(getActivity(), "号码格式不正确");
					 }
				 }
			 }else{
				 if(tempNum != null && !tempNum.equals("") && tempNum.length() == 11 && isMobileNum(tempNum)){
					 if(isMobileNum(tempNum)){
						 telephone_edit.setText(tempNum);
						 mobileQueryAttribution(tempNum);
					 }else{
						 PromptUtil.showToast(getActivity(), "号码格式不正确");
					 }
					 
				 }else{
					 if(tempNum == null || tempNum.equals("")){
						 PromptUtil.showToast(getActivity(), "号码格式不正确");
						 return;
					 }
					 if(tempNum.length() > 11){
						 String num = tempNum.substring(0, 1);
						 if( num.equals("0") ){
							 if(isMobileNum(tempNum.substring(1))){
								 telephone_edit.setText(tempNum.substring(1));
								 mobileQueryAttribution(tempNum.substring(1));
							 }else{
								 PromptUtil.showToast(getActivity(), "号码格式不正确");
							 }
						 }
					 }else{
						 PromptUtil.showToast(getActivity(), "号码格式不正确");
					 }
				 }
				
			 }
//			 telephone_edit.setText(phoneNumber);
//			 mobileQueryAttribution(phoneNumber);
			 break; 
		 } 


		 
	}
	/**
	 * 手机归属地查询
	 * @param mobileNumber 手机号码
	 */
	public void mobileQueryAttribution (final String mobileNumber){
		/*
		new Thread(new Runnable() {
			MobileAttribution attribution;
			@Override
			public void run() {
				MobileQueryAttributionHandler handle = new MobileQueryAttributionHandler();
				InputStream is = SunHttpApi.requestHttp("http://life.tenpay.com/cgi-bin/mobile/MobileQueryAttribution.cgi?chgmobile=" + mobileNumber);
				if(is !=null) {
					attribution = handle.parser(is,"GBK");
					getActivity().runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							
							if(attribution.getProvince() == null || attribution.getSupplier() == null) {
								mobileAttribution.setVisibility(View.GONE);
								return;
							}
							
							mobileAttribution.setVisibility(View.VISIBLE);
							mobileAttributionStr = attribution.getProvince()+attribution.getSupplier();
							mobileAttribution.setText("号码归属地:"+attribution.getProvince()+attribution.getSupplier());
							defaultMobileMoney();
						}
					});
				}else{
					getActivity().runOnUiThread(new Runnable() {

						@Override
						public void run() {
							mobileAttribution.setVisibility(View.GONE);
						}
					});
				}
				
			}
		}).start();
		*/
	}
	
	public static String inputStream2String(InputStream is){
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int i=-1;
			while((i=is.read())!=-1){
				baos.write(i);
			}
			return baos.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	private TaskData initTaskData(){
		TaskData taskData = new TaskData();
		mfeeData.sunMap.put("money", money+"");
		taskData.mCommonData = mfeeData;
		taskData.apiName="ApiPayinfo";
//		if(mType.equals(TransferType.USUAL_TRANSER)){
			taskData.funcName="getTransferPayfee";
//		}else{
//			taskData.funcName="getSupTransferPayfee";
//		}
		
		taskData.mNetParser=new DaikuanfeeParser();
		return taskData;
	}
	
    public void setFee(String fee){
//    	payMoney = Integer.parseInt(fee) + money;
//    	payMoney = money;
		payMoneyTextView.setText("实际支付金额: "+NumberFormatUtil.format2(String.valueOf(payMoney))+"元");
		payMoneyTextView.setTextColor(android.graphics.Color.RED);
//		isSetFee =true;
//			int cost = Integer.parseInt(fee);
//			int money = Integer.parseInt(paymoney);
//			DaikuanActivity.mDaikuanData.sunMap.put(DaikuanData.money, (cost+money)+"");
//			Log.d("DaikuanActivity", DaikuanActivity.mDaikuanData.getValue(DaikuanData.money));
//		DaikuanActivity.mDaikuanData.sunMap.put(DaikuanData.payfee, fee);
	}
	
	@Override
	public void result(int state, String fee, ArrayList<ArriveData> datas) {
		PromptUtil.dissmiss();
		if(state ==0){
			setFee(fee);
//			isFeeSuccess =true;
//			CommonActivity.mTransferData.putValue(TransferData.payfee	, fee.replace("元", ""));
//			cost_container.removeAllViews();
//			mViews.clear();
//			for(ArriveData data : datas){
//				ArriveView view = new ArriveView(getActivity(), data,this);
//				cost_container.addView(view.mView);
//				mViews.add(view);
//			}
//			if(isNext){
//				isNext = false;
//				Intent intent = new Intent();
//				intent.setClass(getActivity(), CommonActivity.class);
//				startActivity(intent);
//			}
		}else{
//			isFeeSuccess =false;
			PromptUtil.showToast(getActivity(), "获取手续费失败");
		}
		
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
//		endCallStateService();
//		mobileReader.close();
		PayApp.mSwipListener= null;
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
//		mobileReader.open(false);
		setTitle("Q币充值");
//		initSwipPic(PayApp.isSwipIn);
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

	class BuyTask extends AsyncTask<String, Integer, Boolean>{
		ProtocolRsp mRsp;
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
//				mData.putValue("bankid", mBankIdString);
//				mData.putValue("bankcardno", mCardNo);
//				mData.putValue("bankname", mBankName);
				List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas("ApiQQRechangeInfo", 
						"readRechaMoneyinfo", mData);
				QMoneyDenominationParser rechangeDenominationParser = new QMoneyDenominationParser();
				mRsp = HttpUtil.doRequest(rechangeDenominationParser, mDatas);
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
					parserResponse(mDatas);
					
					if(!ErrorUtil.create().errorDeal(LoginUtil.mLoginStatus,getActivity())){
						return;
					}
					
					/*
					if(LoginUtil.mLoginStatus.result.equals(ProtocolUtil.SUCCESS)){
//						query_layout.setVisibility(View.VISIBLE);
//						query_input.setText(mMessage);
						
						for(int i = 0; i < mList.size(); i ++){
							if(i == 0){
								chongzhi10.setVisibility(View.VISIBLE);
								chongzhi10_button.setText(mList.get(i).rechamoney+"元");
								chongzhi10_button.setHint(mList.get(i).rechapaymoney);
							}else if(i == 1){
								chongzhi20.setVisibility(View.VISIBLE);
								chongzhi20_button.setText(mList.get(i).rechamoney+"元");
								chongzhi20_button.setHint(mList.get(i).rechapaymoney);
							}else if(i == 2){
								chongzhi30.setVisibility(View.VISIBLE);
								chongzhi30_button.setText(mList.get(i).rechamoney+"元");
								chongzhi30_button.setHint(mList.get(i).rechapaymoney);
							}else if(i == 3){
								chongzhi50.setVisibility(View.VISIBLE);
								chongzhi50_button.setText(mList.get(i).rechamoney+"元");
								chongzhi50_button.setHint(mList.get(i).rechapaymoney);
								
								chongzhi100.setVisibility(View.INVISIBLE);
								chongzhi200.setVisibility(View.INVISIBLE);
							}else if(i == 4){
								chongzhi100.setVisibility(View.VISIBLE);
								chongzhi100_button.setText(mList.get(i).rechamoney+"元");
								chongzhi100_button.setHint(mList.get(i).rechapaymoney);
							}else if(i == 5){
								chongzhi200.setVisibility(View.VISIBLE);
								chongzhi200_button.setText(mList.get(i).rechamoney+"元");
								chongzhi200_button.setHint(mList.get(i).rechapaymoney);
							}else if(i == 6){
								chongzhi300.setVisibility(View.VISIBLE);
								chongzhi300_button.setText(mList.get(i).rechamoney+"元");
								chongzhi300_button.setHint(mList.get(i).rechapaymoney);
								
								chongzhi400.setVisibility(View.INVISIBLE);
								chongzhi500.setVisibility(View.INVISIBLE);
							}else if(i == 7){
								chongzhi400.setVisibility(View.VISIBLE);
								chongzhi400_button.setText(mList.get(i).rechamoney+"元");
								chongzhi400_button.setHint(mList.get(i).rechapaymoney);
							}else if(i == 8){
								chongzhi500.setVisibility(View.VISIBLE);
								chongzhi500_button.setText(mList.get(i).rechamoney+"元");
								chongzhi500_button.setHint(mList.get(i).rechapaymoney);
							}
							
						}
						
						if(PhoneInfoUtil.getNativePhoneNumber(getActivity()) != null) {
							String telnumber = null;
							telnumber = PhoneInfoUtil.getNativePhoneNumber(getActivity());
							
							if(telnumber != null && isMobileNum(telnumber)) {
								telephone_edit.setText(telnumber);
								//查询
								mobileQueryAttribution(telnumber);
								defaultMobileMoney();
							}
						}
						
					}
					*/
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
	
	
	
	
	private void parserResponse(List<ProtocolData> mDatas) {
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
//				List<ProtocolData> msgallcount = data.find("/msgallcount");
//				if(msgallcount != null){
//					mTotalCount = Integer.parseInt(msgallcount.get(0).mValue.trim());
//				}
//				List<ProtocolData> msgdiscount = data.find("/msgdiscount");
//				if(msgdiscount != null){
//					mLoadedCount  = Integer.parseInt(msgdiscount.get(0).mValue.trim());
//				}
				
				List<ProtocolData> aupic = data.find("/msgchild");
				if(aupic!=null)
				for(ProtocolData child:aupic){
					QMoneyDenominationData picData = new QMoneyDenominationData();
					if (child.mChildren != null && child.mChildren.size() > 0) {
						Set<String> keys = child.mChildren.keySet();
						for(String key:keys){
							List<ProtocolData> rs = child.mChildren.get(key);
							for(ProtocolData item: rs){
								if(item.mKey.equals("rechaMoneyid")){
									picData.rechaMoneyid  = item.mValue;
									
								}else if(item.mKey.equals("rechamoney")){
									picData.rechamoney  = item.mValue;
									
								}else if(item.mKey.equals("rechapaymoney")){
									
									picData.rechapaymoney  = item.mValue;
									
								}else if(item.mKey.equals("rechamemo")){
									
									picData.rechamemo  = item.mValue;
									
								}else if(item.mKey.equals("rechaisdefault")){
									
									picData.rechaisdefault  = item.mValue;
									
								}
								/*
								else if(item.mKey.equals("rechapersent")){
									
									picData.rechapersent  = item.mValue;//默认金额优惠百分比
									
								}
								*/
								/**else if(item.mKey.equals("feemoney")){
									
									picData.feemoney  = item.mValue;
								}else if(item.mKey.equals("state")){
									
									picData.state  = item.mValue;
								}else if(item.mKey.equals("allmoney")){
									
									picData.allmoney  = item.mValue;
								}else if(item.mKey.equals("huancardbank")){
									
									picData.huancardbank  = item.mValue;
									
								}else if(item.mKey.equals("fucardbank")){
									
									picData.fucardbank  = item.mValue;
								}*/
							}
						}
					}
					
					mList.add(picData);
				}
			}
		}
	}

	
}
