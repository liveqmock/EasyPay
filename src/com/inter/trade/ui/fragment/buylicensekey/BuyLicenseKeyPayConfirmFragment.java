package com.inter.trade.ui.fragment.buylicensekey;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.inter.protocol.ProtocolData;
import com.inter.protocol.ProtocolRsp;
import com.inter.protocol.util.ProtocolUtil;
import com.inter.trade.AsyncLoadWork.AsyncLoadWorkListener;
import com.inter.trade.R;
import com.inter.trade.SwipKeyTask;
import com.inter.trade.data.BaseData;
import com.inter.trade.data.CardData;
import com.inter.trade.data.CommonData;
import com.inter.trade.data.ResponseData;
import com.inter.trade.data.ResultData;
import com.inter.trade.error.ErrorUtil;
import com.inter.trade.net.HttpUtil;
import com.inter.trade.ui.BankListActivity;
import com.inter.trade.ui.DaikuanActivity;
import com.inter.trade.ui.PayApp;
import com.inter.trade.ui.BankRecordListActivity.TYPECLASS;
import com.inter.trade.ui.PayApp.BuySuccessListener;
import com.inter.trade.ui.PayApp.SwipListener;
import com.inter.trade.ui.QMoneyPayActivity;
import com.inter.trade.ui.creditcard.CommonEasyCreditcardPayActivity;
import com.inter.trade.ui.creditcard.CommonEasyCreditcardPayFragment;
import com.inter.trade.ui.creditcard.MyBankCardActivity;
import com.inter.trade.ui.creditcard.PayWaysHandler;
import com.inter.trade.ui.creditcard.SmsCodeDialog;
import com.inter.trade.ui.creditcard.SmsCodeDialog.SmsCodeSubmitListener;
import com.inter.trade.ui.creditcard.data.DefaultBankCardData;
import com.inter.trade.ui.creditcard.data.SmsCode;
import com.inter.trade.ui.creditcard.task.CaiSmsCodeTask;
import com.inter.trade.ui.creditcard.task.GetDefaultTask;
import com.inter.trade.ui.creditcard.task.PayCardCheckTask;
import com.inter.trade.ui.creditcard.task.SmsCodeTask;
import com.inter.trade.ui.creditcard.util.CreditCard;
import com.inter.trade.ui.fragment.BaseFragment;
import com.inter.trade.ui.fragment.buylicensekey.util.BuyLicenseKeyUtils;
import com.inter.trade.ui.fragment.coupon.task.BuySuccessTask;
import com.inter.trade.ui.fragment.hotel.HotelPaySuccessFragment;
import com.inter.trade.ui.fragment.hotel.data.HotelOrderData;
import com.inter.trade.ui.fragment.hotel.data.HotelRoomData;
import com.inter.trade.ui.fragment.hotel.task.HotelCreateOrderTask;
import com.inter.trade.ui.fragment.hotel.task.HotelPayOrderTask;
import com.inter.trade.ui.fragment.qmoney.QMoneyPaySuccessFragment;
import com.inter.trade.ui.fragment.qmoney.util.QMoneyCreditCardTask;
import com.inter.trade.ui.fragment.qmoney.util.QMoneyData;
import com.inter.trade.ui.fragment.qmoney.util.QMoneyNoParser;
import com.inter.trade.ui.fragment.returndaikuan.util.DaikuanData;
import com.inter.trade.ui.fragment.waterelectricgas.data.ResponseStateListener;
import com.inter.trade.util.BankCardUtil;
import com.inter.trade.util.Base64Pay;
import com.inter.trade.util.Constants;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.NumberFormatUtil;
import com.inter.trade.util.PreferenceConfig;
import com.inter.trade.util.PromptUtil;
import com.inter.trade.util.PromptUtil.NegativeListener;
import com.inter.trade.util.PromptUtil.PositiveListener;
import com.inter.trade.util.TaskUtil;
import com.inter.trade.util.UserInfoCheck;
import com.unionpay.uppayplugin.demo.UnionpayUtil;

@SuppressLint("ValidFragment")
public class BuyLicenseKeyPayConfirmFragment extends BaseFragment implements OnClickListener,SwipListener, 
	ResponseStateListener,SmsCodeSubmitListener,TextWatcher,OnFocusChangeListener,BuySuccessListener{
	
	/**
	 * 购买授权码支付页面，复用公用布局layout_pay_ways.xml里【打勾  银行卡支付，选择银行卡】 那一行布局rl_creditcard不显示，只走银联通道
	 */
	private RelativeLayout rl_creditcard;
	
	/**
	 * 屏蔽【温馨提醒：同一信用卡当天请勿频繁交易.】
	 */
	private TextView tv_creditcard_tips;
	
	private TextView tv_authorid_phone;
	private Button cridet_back_btn;
//	private ImageView swip_card;
//	private TextView swip_prompt;
//	private EditText card_edit;
//	private LinearLayout bank_layout;
//	private TextView bank_name;
//	private EditText open_name_edit;
//	private EditText open_phone_edit;
//	private TextView acount;
	
	private String mBkntno;
	private String mMessage ="";
	private String mResult =""; 
	
	/**
	 * 酒店 只支持信用卡 到携程付款，isCreditCardPayOnly=true; 故凡是走银联的都转为走信用卡
	 */
	private final boolean isCreditCardPayOnly = false;
	
	private boolean isSelectedBank=false;
//	private LinearLayout coupon_layout;
	
	private BuyTask mBuyTask;
//	private DaikuanActivity mActivity;
	
//	private Bundle bundle;
	private HotelOrderData mHotelOrderData;
	public ArrayList<HashMap<String, String>> mCommonData = new ArrayList<HashMap<String,String>>();
	
	
	/*** 处理支付方式的类 */
	private PayWaysHandler viewHandler;

	private PayCardCheckTask getHistoryTask;
	
	private GetDefaultTask getDefaultTask;

	private DefaultBankCardData creditCard;
	
	private DefaultBankCardData historyCard;

	/** 银行卡号 */
	private String bankno;

	/** 銀行卡號 */
	private String cardNumber;
	
	/**记录信用卡是否来自选择银行卡*/
	private boolean isChooseBank=false;
	
	/**该信用卡是否消费过（后台有记录），true 是，false否*/
	private boolean hasPayHistory=false;
	
	/**判断执行异步任务的类型，0获取默认支付卡信息，1获取该信用卡是否消费过（后台有记录）*/
	private int taskId=0;
	
	private QMoneyCreditCardTask  payTask;
	
	public static BuyLicenseKeyPayConfirmFragment create(double value,String couponId){
		return new BuyLicenseKeyPayConfirmFragment();
	}
	
	public BuyLicenseKeyPayConfirmFragment()
	{
	}
	
	public BuyLicenseKeyPayConfirmFragment(Bundle b) {
//		this.bundle = b;
	}
	
	public static BuyLicenseKeyPayConfirmFragment newInstance (Bundle data) {
		BuyLicenseKeyPayConfirmFragment fragment = new BuyLicenseKeyPayConfirmFragment();
		fragment.setArguments(data);
		return fragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		Bundle data = getArguments();
		if(data != null) {
			mHotelOrderData = (HotelOrderData) data.getSerializable("hotelOrder");
		}
		
		PayApp.mSwipListener = this;
		
	}
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		LoginUtil.detection(getActivity());
		View view = inflater.inflate(R.layout.buy_licensekey_swip_layout, container,false);
		initView(view);
		
		setTitle("购买授权码");
		setBackVisibleForFragment();
		
		/**
		 * 以下暂时屏蔽
		 */
//		if (savedInstanceState != null) {
//			creditCard = (DefaultBankCardData) savedInstanceState
//					.getSerializable("data");
//		}
//		if (creditCard == null) {
//			taskId=0;
//			getDefaultTask = new GetDefaultTask(getActivity(), this);
//			getDefaultTask.execute("", "1");
//		}
		return view;
	}
	
	private void initSwipPic(boolean flag) {
		viewHandler.setCardImageVisibility(flag);
	}
	
	private void initView(View view){
		cridet_back_btn = (Button)view.findViewById(R.id.cridet_back_btn);
		tv_authorid_phone = (TextView)view.findViewById(R.id.tv_authorid_phone);
		
		/**
		 * 显示：授权用户，即本机登录用户
		 */
		tv_authorid_phone.setText(PreferenceConfig.instance(getActivity()).getString(Constants.USER_NAME, ""));
		cridet_back_btn.setOnClickListener(this);
		
		viewHandler = new PayWaysHandler(getActivity(),this,this,this);
		viewHandler.initView(null, view);
		viewHandler.setDefaultPay(2);
		
		/**
		 * 公用布局layout_pay_ways.xml里【打勾  银行卡支付，选择银行卡】那一行不显示
		 */
		rl_creditcard = (RelativeLayout) view.findViewById(R.id.rl_creditcard);
		rl_creditcard.setVisibility(View.GONE);
		
		/**
		 * 屏蔽【温馨提醒：同一信用卡当天请勿频繁交易.】
		 */
		tv_creditcard_tips = (TextView) view.findViewById(R.id.tv_creditcard_tips);
		tv_creditcard_tips.setVisibility(View.GONE);
		
		
	}
	
	/***判断刷卡时是否为信用卡*/
	private boolean isCreditCardk=false;
	/***判断是否为刷卡得到的卡号*/
	private boolean isSwipCard=false;

	@Override
	public void recieveCard(CardData data) {
		// card_edit.setText(data.pan);
//		Log.i("CardData", PayApp.merReserved.toString());
		isChooseBank=false;
		isSwipCard=true;
//		if("2".equals(data.trackInfo)){//信用卡
//			viewHandler.setDefaultPay(2);
//			isCreditCardk=true;
//		}else if("23".equals(data.trackInfo)){//储蓄卡
//			viewHandler.setDefaultPay(2);
//			isCreditCardk=false;
//		}
		
		viewHandler.setDefaultPay(2);
		switch (viewHandler.getCheckpay()) {
		case 2:// 信用卡支付
			viewHandler.setCredit(data.pan);
			isChooseBank=false;
			break;
		case 3:// 储蓄卡支付
			viewHandler.setDeposit(data.pan);
			break;

		default:
			break;
		}
	}
	
	@Override
	public void checkedCard(boolean flag) {
		// TODO Auto-generated method stub
		boolean s = PayApp.openReaderNow();
		log("status : " + s);
		viewHandler.setCardImageVisibility(flag);
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
//			mKeyTask = new SwipKeyTask(getActivity(), PayApp.mKeyCode, PayApp.mKeyData);
//			mKeyTask.execute("");
			
			mKeyTask = new SwipKeyTask(getActivity(), PayApp.mKeyCode+"", PayApp.mKeyData, viewHandler.getCredit()+"", "hotel", new AsyncLoadWorkListener() {
				@Override
				public void onSuccess(Object protocolDataList, Bundle bundle) {
					if(protocolDataList != null) {
						historyCard = (DefaultBankCardData)protocolDataList;
						if(historyCard != null){
							String type=historyCard.getBkcardcardtype();
							if(type==null || "".equals(type)){
								historyCard = null;
							}
						}
//						if(historyCard != null){
//							directCreditPay(historyCard);
//						}
//						else{
//							showPayWays();
//						}
					}
//					if(historyCard != null) {
//						//自动填充付款信息
//					}
				}
				@Override
				public void onFailure(String error) {
					historyCard = null;
//					showPayWays();
				}
			});
			mKeyTask.execute("");
			break;
		default:
			break;
		}
		
	}

	long time=0L;
	@Override
	public void onClick(View arg0) {
		String bankno;
		switch (arg0.getId()) {
		case R.id.cridet_back_btn:
			
			if (checkInput()) {
				//1秒内，禁止双击两次提交
				long currentTime = System.currentTimeMillis();
				if (currentTime - time < 1000 || TaskUtil.isTaskExcuting(payTask)||TaskUtil.isTaskExcuting(mBuyTask)) {
					PromptUtil.showLongToast(getActivity(), getResources().getString(R.string.warn_repate_commit));
					return;
				}
				time = System.currentTimeMillis();
				
				switch (viewHandler.getCheckpay()) {//1 默认 2 信用卡 3储蓄卡
				case 1://信用卡直接支付
					directCreditPay(creditCard);
					break;
				case 2:
					//点击选择了银行卡号a，或刷卡获取了卡号b，但最后手动输入修改过卡号c，要对输入的号码c作判断，c==a 或 c==b
//					bankno=viewHandler.getCredit();
//					if(bank != null){
//						String bkcardno=bank.getBkcardno();
//						if(bankno.equals(bkcardno)){
//							isChooseBank=true;
//						}
//					}
//					if(historyCard != null){
//						isChooseBank=false;
//					}
					
					if(isChooseBank){//选择银行
						if("creditcard".equals(bank.getBkcardcardtype())){//信用卡直接支付
							directCreditPay(bank);
						}else if("bankcard".equals(bank.getBkcardcardtype())){//银联支付
							gotoDeposit();
						}else{
							gotoDeposit();
						}
					}else{//进入银行卡信息填写
//						if(isSwipCard){//刷卡
//							if(isCreditCardk){//信用卡填写页面
//								gotoCreditpay();
//							}else{//银联支付
//								gotoDeposit();
//							}
//						}else
						
//						if(!isChooseBank){
//							testPayCard();//del
//							return;
//						}
						
						{//手动输入
							bankno=viewHandler.getCredit();
							if(BankCardUtil.isCreditCard(bankno)){//初步判断为16位，当信用卡
								if(historyCard != null){
//									directCreditPay(historyCard);
									String cardType=historyCard.getBkcardcardtype();
									if(cardType == null || "".equalsIgnoreCase(cardType)){
										directCreditPay(historyCard);//没有返回类型，则当作信用卡
									}else{
										if(cardType != null && "creditcard".equalsIgnoreCase(cardType)){
											directCreditPay(historyCard);//信用卡
										}else{
											gotoDeposit();//储蓄卡
										}
									}
								}
								else{
									showPayWays();//信用卡，储蓄卡 选择框
								}
							}else{
								gotoDeposit();
							}
						}
					}
					break;
				case 3:
					gotoDeposit();
					break;
				default:
					break;
				}
			}
			break;
		case R.id.btn_choose_one:// 信用卡 选择我的银行卡
			Intent in1 = new Intent(getActivity(), MyBankCardActivity.class);
			in1.putExtra("type", 1);
			startActivityForResult(in1, 77);
			break;
		case R.id.btn_choose_two:// 储蓄卡 选择我的银行卡
			Intent in2 = new Intent(getActivity(), MyBankCardActivity.class);
			in2.putExtra("type", 2);
			startActivityForResult(in2, 78);
			break;
		default:
			break;
		}
	
		
	}
	
	/**
	 * 直接使用易宝通道支付 
	 * @throw
	 * @return void
	 */
	private void directCreditPay(DefaultBankCardData bank) {
		if(bank!=null){
			
			mCommonData.clear();
			
			HashMap<String, String> item3 = new HashMap<String, String>();
			item3.put("付款账号:", cardNumber);
			mCommonData.add(item3);
			
			HashMap<String, String> item37 = new HashMap<String, String>();
			item37.put("付款银行:", bank.getBkcardbank());
			mCommonData.add(item37);
			
			HashMap<String, String> item38 = new HashMap<String, String>();
			item38.put("付款人:", bank.getBkcardbankman());
			mCommonData.add(item38);
			
			
			HashMap<String, String> item5 = new HashMap<String, String>();
			item5.put("房间单价:", "￥"+mHotelOrderData.price);
			mCommonData.add(item5);

			HashMap<String, String> item6 = new HashMap<String, String>();
			item6.put("入住天数:", mHotelOrderData.dayCount);
			mCommonData.add(item6);
			
			HashMap<String, String> item7 = new HashMap<String, String>();
			item7.put("房间数量:", mHotelOrderData.roomCount);
			mCommonData.add(item7);
			
			HashMap<String, String> item8 = new HashMap<String, String>();
			item8.put("支付总额:", "￥"+mHotelOrderData.payMoney);
			mCommonData.add(item8);
			
//			HashMap<String, String> item = new HashMap<String, String>();
//			item.put("交易请求号:", orderId);
//			if(smsCai != null){
//				item.put("交易请求号:", smsCai.getBkordernumber()+"");
//				mCommonData.add(item);
//			}
			
			CommonData hotelData = new CommonData();
			
			hotelData.putValue("hotelCode", mHotelOrderData.hotelCode);
			hotelData.putValue("roomCode", mHotelOrderData.roomCode);
			hotelData.putValue("priceCode", mHotelOrderData.priceCode);
			hotelData.putValue("startDate", mHotelOrderData.startDate);
			hotelData.putValue("endDate", mHotelOrderData.endDate);
			hotelData.putValue("roomCount", mHotelOrderData.roomCount);
			hotelData.putValue("payMoney", mHotelOrderData.payMoney);
			hotelData.putValue("phone", mHotelOrderData.phone);
			hotelData.putValue("name", mHotelOrderData.names[0]);//need modify
			
			hotelData.putValue("bkCardno", bank.getBkcardno());
			hotelData.putValue("bkcardman", bank.getBkcardbankman());
			hotelData.putValue("bkcardexpireMonth", bank.getBkcardyxmonth());
			hotelData.putValue("bkcardmanidcard", bank.getBkcardidcard());
			hotelData.putValue("bankid", bank.getBkcardbankid()+"");
			hotelData.putValue("bankno", bank.getBkcardbankcode()+"");
			hotelData.putValue("bankname", bank.getBkcardbank()+"");
			hotelData.putValue("bkcardexpireYear", bank.getBkcardyxyear());
			hotelData.putValue("bkcardPhone", bank.getBkcardbankphone());
			hotelData.putValue("bkcardcvv", bank.getBkcardcvv());
			hotelData.putValue("paytype", "hotel");//need modify
			hotelData.putValue("paycardid", PayApp.mKeyCode != null ? PayApp.mKeyCode : "");
			
			/**
			 * 生成订单
			 */
			new HotelCreateOrderTask(getActivity(), mCreateOrderListener, hotelData).execute();
		}
	}

	/**
	 * 生成订单 监听
	 */
	private ResponseStateListener mCreateOrderListener=new ResponseStateListener() {
		
		@Override
		public void onSuccess(Object obj, Class cla) {
			// TODO Auto-generated method stub
			if(cla.equals(SmsCode.class)){
				SmsCode sms=(SmsCode) obj;
				if(sms!=null){
					orderId=sms.getOrderId();
					if(orderId != null){//已生成订单
						CommonData hotelData = new CommonData();
						hotelData.putValue("orderId", orderId);
						hotelData.putValue("payInfo", "");
						/**
						 * 信用卡支付订单
						 */
						new HotelPayOrderTask(getActivity(), mPayOrderListener, hotelData).execute();
					}
				}
			}
		}
	};
	
	/**
	 * 信用卡支付订单 监听
	 */
	private ResponseStateListener mPayOrderListener=new ResponseStateListener() {
		
		@Override
		public void onSuccess(Object obj, Class cla) {
			// TODO Auto-generated method stub
			if(cla.equals(SmsCode.class)){
				SmsCode sms=(SmsCode) obj;
				if(sms!=null){
					if(sms.isNeed()){//需要验证码
						dialog=new SmsCodeDialog();
						dialog.show(getActivity(), mSmsCodeSubmitListener);
					}else{//不需要验证码,支付成功
						PromptUtil.showToast(getActivity(), sms.getMessage());
						gotoPaySuccess();
					}
				}
			}
		}
	};
	
	/**
	 * 验证码支付订单 监听
	 */
	private SmsCodeSubmitListener mSmsCodeSubmitListener = new SmsCodeSubmitListener() {
		
		@Override
		public void onPositive(String code, SmsCodeDialog dialog) {
			// TODO Auto-generated method stub
			String funName="ApiHotel";
			String func="PayWithVerifyCode";
			
			new SmsCodeTask(getActivity(), new ResponseStateListener() {

				@Override
				public void onSuccess(Object obj, Class cla) {
					PromptUtil.showToast(getActivity(), (String) obj);
					// dialog.dismiss();
					gotoPaySuccess();
				}

			}).execute(orderId, code, funName, func);
		}
	};
	
	/**
	 * 前往银联支付
	 * 
	 * @throw
	 * @return void
	 */
	private void gotoDeposit() {
		if(isCreditCardPayOnly){
			gotoCreditpay();
		}else{
			testF();
			
//			mBuyTask = new BuyTask();
//			mBuyTask.execute("");
//			Log.d("BuyTask", "前往银联支付");
		}
	}

	/**
	 * 前往信用卡支付
	 * 
	 * @throw
	 * @return void
	 */
	private void gotoCreditpay() {
//		Intent intent=new Intent(getActivity(),CommonEasyCreditcardPayActivity.class);
		Intent intent=new Intent();
		intent.putExtra(CreditCard.PAY_KEY, CreditCard.HOTEL);
		intent.putExtra(CreditCard.CARDNO, cardNumber);
		intent.putExtra(CreditCard.PAYMONEY, mHotelOrderData.payMoney);
		intent.putExtra("paycardid", PayApp.mKeyCode != null ? PayApp.mKeyCode : "");
		
		intent.putExtra("hotelCode", mHotelOrderData.hotelCode);
		intent.putExtra("roomCode", mHotelOrderData.roomCode);
		intent.putExtra("priceCode", mHotelOrderData.priceCode);
		intent.putExtra("startDate", mHotelOrderData.startDate);
		intent.putExtra("endDate", mHotelOrderData.endDate);
		intent.putExtra("roomCount", mHotelOrderData.roomCount);
		intent.putExtra("payMoney", mHotelOrderData.payMoney);
		intent.putExtra("phone", mHotelOrderData.phone);
		intent.putExtra("name", mHotelOrderData.names[0]);//need modify
		
		intent.putExtra("price", mHotelOrderData.price);
		intent.putExtra("daytotal", mHotelOrderData.dayCount);
		
//		startActivityForResult(intent, 0);
		
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.replace(R.id.func_container, CommonEasyCreditcardPayFragment.newInstance(intent));
		ft.addToBackStack(null);
		ft.commit();
		
	}
	
	/**
	 * 测试代码
	 */
	private boolean testF(){
		/**
		 * 购买授权码，支付成功，结果页面
		 */
		String licenseKey="1610015483";
		Bundle bundle = new Bundle();
		bundle.putString("licenseKey", licenseKey);
		BuyLicenseKeyUtils.switchFragment(getFragmentManager().beginTransaction(),
				BuyLicenseKeySuccessFragment.newInstance(bundle), 1);
		return true;
	}
	
	private boolean checkInput(){
//		if(testF()) return false;
		/**
		 * 
		 */
		
		if (!viewHandler.isSelected()) {
			PromptUtil.showToast(getActivity(), "请选择一种支付方式");
			return false;
		}
		cardNumber = "";
		switch (viewHandler.getCheckpay()) {
		case 1:
			cardNumber=creditCard.getBkcardno();
			break;
		case 2:
			cardNumber = viewHandler.getCredit();
			if (null == cardNumber || "".equals(cardNumber)) {
				PromptUtil.showToast(getActivity(), "请刷卡或输入卡号");
				return false;
			}
			break;
		case 3:
			cardNumber = viewHandler.getDeposit();
			if (null == cardNumber || "".equals(cardNumber)) {
				PromptUtil.showToast(getActivity(), "请刷卡或输入卡号");
				return false;
			}
			break;

		default:
			break;
		}		
		
//		String cardNumber = card_edit.getText().toString();
//		if(null == cardNumber || "".equals(cardNumber)){
//			PromptUtil.showToast(getActivity(), "请刷卡或输入卡号");
//			return false;
//		}
		
		if(!UserInfoCheck.checkBankCard(cardNumber)){
			PromptUtil.showToast(getActivity(), "卡号格式不正确");
			return false;
		}
//		QMoneyPayActivity.moblieRechangeData.sunMap.put(QMoneyData.MRD_RECHABKCARDNO, cardNumber);
		
//		if (mHotelOrderData == null) {
//			PromptUtil.showToast(getActivity(), "操作异常");
//			return false;
//		}
		
//		QMoneyPayActivity.moblieRechangeData.sunMap.put(QMoneyData.MRD_RECHAMONEY, bundle.getString(QMoneyData.MRD_RECHAMONEY));
//		QMoneyPayActivity.moblieRechangeData.sunMap.put(QMoneyData.MRD_RECHAPAYMONEY, bundle.getString(QMoneyData.MRD_RECHAPAYMONEY));
//		QMoneyPayActivity.moblieRechangeData.sunMap.put(QMoneyData.MRD_RECHAMOBILE, bundle.getString(QMoneyData.MRD_RECHAMOBILE));
//		//支付类型id ，默认为1
//		QMoneyPayActivity.moblieRechangeData.sunMap.put(QMoneyData.MRD_RECHAPAYTYPEID, 1+"");
		
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
//		QMoneyPayActivity.moblieRechangeData.sunMap.put(QMoneyData.MRD_PAYCARDID, PayApp.mKeyCode != null ? PayApp.mKeyCode : "");
//		QMoneyPayActivity.moblieRechangeData.sunMap.put(QMoneyData.merReserved, 
//				Base64Pay.encode(PayApp.merReserved.toString().getBytes()));
		return true;
	}
	
	/***选择银行返回的数据*/
	private DefaultBankCardData bank;
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		if (resultCode == 78) {// 选择银行
			bank = (DefaultBankCardData) data
					.getSerializableExtra("bankcard");
			// TODO 填充数据
			bankno = bank.getBkcardno();
			switch (viewHandler.getCheckpay()) {// 1 默认支付 2 信用卡支付 3储蓄卡支付
			case 2:
				viewHandler.setCredit(bankno);
				isChooseBank=true;
				break;
			case 3:
				viewHandler.setDeposit(bankno);
				break;
			default:
				break;
			}

		} else {// 银联支付结果
//			payResult(data);
//			 if (data == null) {
//		            return;
//		     }
//			 String bankid = data.getStringExtra(BankListActivity.BANK_ID);
//			 String bankname = data.getStringExtra(BankListActivity.BANK_NAME);
//			 if(null != bankname &&!"".equals(bankname)){
//				 isSelectedBank=true;
//				 DaikuanActivity.mDaikuanData.sunMap.put(DaikuanData.fucardbank, bankname);
//			 }
//			 bank_name.setText(bankname);
//			 
//			 //银联支付结果
		}
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
		setTitle("购买授权码");
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

	/**
	 * 调用银联支付 控件，自启动新的Activity
	 */
	private void showChuxuka(){
		UnionpayUtil.startUnionPlungin(mBkntno, getActivity());
	}
	
	
	/**
	 * 银联通道：储蓄卡，（酒店业务：只用携程：信用卡；不用银联）
	 * @author Administrator
	 *
	 */
	class BuyTask extends AsyncTask<String, Integer, Boolean>{
		ProtocolRsp mRsp;
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				BaseData reqData = new BaseData();
				reqData.putValue("paycardid", "");
				
				List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas("ApiBuyOderInfo ", 
						"payOrderRq", reqData);
				QMoneyNoParser authorRegParser = new QMoneyNoParser();
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
						mCommonData.clear();
//						QMoneyPayActivity.mBankNo = mBkntno;
							
							HashMap<String, String> item3 = new HashMap<String, String>();
							item3.put("付款账号:", cardNumber);
							mCommonData.add(item3);
							
							HashMap<String, String> item5 = new HashMap<String, String>();
							item5.put("房间单价:", "￥"+mHotelOrderData.price);
							mCommonData.add(item5);

							HashMap<String, String> item6 = new HashMap<String, String>();
							item6.put("入住天数:", mHotelOrderData.dayCount);
							mCommonData.add(item6);
							
							HashMap<String, String> item7 = new HashMap<String, String>();
							item7.put("房间数量:", mHotelOrderData.roomCount);
							mCommonData.add(item7);
							
							HashMap<String, String> item8 = new HashMap<String, String>();
							item8.put("支付总额:", "￥"+mHotelOrderData.payMoney);
							mCommonData.add(item8);
							
//							HashMap<String, String> item = new HashMap<String, String>();
//							item.put("交易请求号:", mBkntno);
//							mCommonData.add(item);
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
				
				List<ProtocolData> bkntno = data.find("/bkntno");
				if(bkntno != null){
					mBkntno = bkntno.get(0).mValue;
				}
			}
		}
	}
	
	private static String orderId;
	private SmsCodeDialog dialog;
	
	@Override
	public void onSuccess(Object obj, Class cla) {
		
		if(cla.equals(SmsCode.class)){
			smsCai=(SmsCode) obj;
			if(smsCai!=null){
				if(smsCai.isNeed()){//需要验证码
					dialog=new SmsCodeDialog();
					dialog.show(getActivity(), this);
				}else{//不需要验证码,支付成功
					PromptUtil.showToast(getActivity(), smsCai.getMessage());
					gotoPaySuccess();
				}
			}
		}else{
			if(taskId==0){
				creditCard = (DefaultBankCardData) obj;
				viewHandler.setDefaultBank(creditCard);
				viewHandler.setDefaultVisibility(View.VISIBLE);
				viewHandler.setDefaultPay(1);
				viewHandler.setBankTip();
			}else{
				historyCard = (DefaultBankCardData) obj;
				if(historyCard != null){
					directCreditPay(historyCard);
				}else{
					showPayWays();
				}
			}
		}
	}

	public void showPayWays() {
		/**
		 * 银联支付,购买授权码暂只走银联通道
		 */
		if(!isCreditCardPayOnly){
			gotoDeposit();
			return;
		}
		
		
		if(isCreditCardPayOnly){
			gotoCreditpay();
		}else{
			PromptUtil.showTwoButtonDialog("温馨提示", "您当前使用卡号为信用卡,是否选择信用卡支付通道?", new PositiveListener() {
				
				@Override
				public void onPositive() {//信用卡填写页面
					gotoCreditpay();
				}
			}, new NegativeListener() {
				
				@Override
				public void onNegative() {//银联支付
					gotoDeposit();
				}
			}, getActivity());
		}
	}
	
	private SmsCode smsCai;
	@Override
	public void onPositive(String code, SmsCodeDialog dialog) {
		String funName="ApiHotel";
		String func="PayWithVerifyCode";
		
		new CaiSmsCodeTask(getActivity(), new ResponseStateListener() {
			
			@Override
			public void onSuccess(Object obj, Class cla) {
				gotoPaySuccess();
			}
		}).execute(code,
				   smsCai.getBkordernumber(),
				   smsCai.getBkntno(),
				   smsCai.getVerifytoken(),
				   funName, func);
	}
	
	/**
	 * （信用卡）支付成功，跳转到成功页面
	 */
	private void gotoPaySuccess() {
		FragmentTransaction transaction =getActivity().getSupportFragmentManager().beginTransaction();
  		transaction.replace(R.id.func_container, HotelPaySuccessFragment.createFragment(mCommonData));
  		transaction.addToBackStack(null);
  		transaction.commit();
	}

	/**
	 * 银联储蓄卡支付成功UIManagerActivity::onActivityResult，跳转页面（酒店业务用不到，只是备用）
	 */
	@Override
	public void requestBuySuccess(final ResultData resultData, String msg) {
		resultData.putValue(ResultData.bkntno, mBkntno);
		
		/**
		 * 银联储蓄卡支付
		 */
//		new BuySuccessTask(getActivity(), resultData,"ApiQQRechangeInfo","checkRechaMoneyStatus").execute("");
		
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
                	gotoPaySuccess();
                }else{
//                	getActivity().finish();
//                	gotoPaySuccess();
                }
               
            }
        });
        builder.create().show();
	}
	
	
	//TextWatcher的实现方法
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		
	}
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		
	}
	@Override
	public void afterTextChanged(Editable s) {
		
	}

	@Override
	public void onFocusChange(View arg0, boolean isFocus) {
		if(isFocus){
			isSwipCard=false;
			isChooseBank=false;
		}
	}
	
}
