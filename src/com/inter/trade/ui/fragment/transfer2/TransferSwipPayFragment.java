package com.inter.trade.ui.fragment.transfer2;

import java.io.Serializable;
import java.util.ArrayList;
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
import com.inter.trade.AsyncLoadWork.AsyncLoadWorkListener;
import com.inter.trade.R;
import com.inter.trade.SwipKeyTask;
import com.inter.trade.data.BankData;
import com.inter.trade.data.BankRecordData;
import com.inter.trade.data.CardData;
import com.inter.trade.data.CommonData;
import com.inter.trade.data.ResponseData;
import com.inter.trade.data.ResultData;
import com.inter.trade.error.ErrorUtil;
import com.inter.trade.net.HttpUtil;
import com.inter.trade.ui.BankListActivity;
import com.inter.trade.ui.BankRecordListActivity.TYPECLASS;
import com.inter.trade.ui.CommonActivity;
import com.inter.trade.ui.PayApp;
import com.inter.trade.ui.PayApp.BuySuccessListener;
import com.inter.trade.ui.PayApp.SwipListener;
import com.inter.trade.ui.creditcard.CommonEasyCreditcardPayFragment;
import com.inter.trade.ui.creditcard.SmsCodeDialog;
import com.inter.trade.ui.creditcard.SmsCodeDialog.SmsCodeSubmitListener;
import com.inter.trade.ui.creditcard.data.DefaultBankCardData;
import com.inter.trade.ui.creditcard.data.SmsCode;
import com.inter.trade.ui.creditcard.task.BankTask;
import com.inter.trade.ui.creditcard.task.SmsCodeTask;
import com.inter.trade.ui.creditcard.util.CreditCard;
import com.inter.trade.ui.creditcard.util.CreditcardInfoUtil;
import com.inter.trade.ui.fragment.BaseFragment;
import com.inter.trade.ui.fragment.coupon.task.BuySuccessTask;
import com.inter.trade.ui.fragment.cridet.data.JournalData;
import com.inter.trade.ui.fragment.transfer.TransferSuccessFragment;
import com.inter.trade.ui.fragment.transfer.util.TransferCreditCardTask;
import com.inter.trade.ui.fragment.transfer.util.TransferData;
import com.inter.trade.ui.fragment.transfer.util.TransferNoParser;
import com.inter.trade.ui.fragment.waterelectricgas.data.ResponseStateListener;
import com.inter.trade.util.BankCardUtil;
import com.inter.trade.util.Base64Pay;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.NumberFormatUtil;
import com.inter.trade.util.PromptUtil;
import com.inter.trade.util.PromptUtil.NegativeListener;
import com.inter.trade.util.PromptUtil.PositiveListener;
import com.inter.trade.util.TaskUtil;
import com.inter.trade.util.UserInfoCheck;
import com.unionpay.uppayplugin.demo.UnionpayUtil;

/**
 * 付款卡信息
 * @author zhichao.huang
 *
 */
public class TransferSwipPayFragment extends BaseFragment implements OnClickListener,SwipListener,
ResponseStateListener,SmsCodeSubmitListener,AsyncLoadWorkListener,  BuySuccessListener{
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
//	private CommonActivity mActivity;
	private String mType=TYPECLASS.tfmg;
	
	private TransferCreditCardTask payTask;
	
	/**
	 * 标记是否可以选中银行
	 */
	private boolean isCanSelectBank=true;
	
	private Bundle bundleData = null;
	
	/**
	 * 收款人银行卡信息
	 */
	private BankRecordData bankRecordData = null;
	
	public ArrayList<HashMap<String, String>> mCommonData = new ArrayList<HashMap<String,String>>();
	
	public static TransferSwipPayFragment newInstance (Bundle data) {
		TransferSwipPayFragment fragment = new TransferSwipPayFragment();
		fragment.setArguments(data);
		return fragment;
	}
	
//	public static TransferSwipPayFragment create(double value,String couponId){
//		count = value;
//		mCouponId = couponId;
//		return new TransferSwipPayFragment();
//	}
	
	public TransferSwipPayFragment()
	{
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getActivity().setTheme(R.style.DialogStyleLight);
		LoginUtil.detection(getActivity());
//		initReader();
		PayApp.mSwipListener = this;
		initData();
//		if(getActivity() instanceof CommonActivity){
//			 mActivity = (CommonActivity)getActivity();
//			 mType = mActivity.mType;
//		}
		
		Bundle bundle = getArguments();
		if(bundle != null) {
			bundleData = bundle;
			mType = bundleData.getString("paytype");
			Serializable serializable = bundleData.getSerializable("bankShoukuanRecordData");
			if(serializable != null) {
				bankRecordData = (BankRecordData)serializable;
			}
		}
		
	}
	
	private void initData(){
		loadYibaoBanks();
	}
	
	private View rootView = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
//		LoginUtil.detection(getActivity());
		
		if(rootView == null) {
			rootView = inflater.inflate(R.layout.transfer_swip_layout, container,false);
			initView(rootView);
		}
		ViewGroup parent = (ViewGroup) rootView.getParent();  
        if (parent != null) {  
            parent.removeView(rootView);  
        }  
		
//		View view = inflater.inflate(R.layout.transfer_swip_layout, container,false);
//		initView(view);
		
		setTitle("付款");
//		setBackVisible();
//		openReader();
		
		return rootView;
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
		
		cridet_back_btn.setOnClickListener(this);
		bank_layout.setOnClickListener(this);
		
	}
	
	/***判断刷的卡是否为信用卡*/
	private boolean isCreditCard=false;
	
	@Override
	public void recieveCard(CardData data) {
		// TODO Auto-generated method stub
		if("2".equals(data.trackInfo)){//信用卡
			isCreditCard=true;
		}else if("23".equals(data.trackInfo)){//储蓄卡
			isCreditCard=false;
		}
		card_edit.setText(data.pan);
		/*new PayCardCheckTask(getActivity(), this, false).execute(
				PayApp.mKeyCode==null ?"":PayApp.mKeyCode,
						data.pan,
						paytype);*/
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
			
			String paytype="";
			if(TYPECLASS.tfmg.equals(mType)){
				paytype="tfmg";
			}else{
				paytype="suptfmg";
			}
			
			mKeyTask = new SwipKeyTask(getActivity(),
					   PayApp.mKeyCode, 
					   PayApp.mKeyData, 
					   card_edit.getText().toString(), 
					   paytype, this);
			mKeyTask.execute("");
			break;
		default:
			break;
		}
		
	}

	long time = 0L;
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.cridet_back_btn:
			
			long currentTime = System.currentTimeMillis();
			if (currentTime - time < 1000 || TaskUtil.isTaskExcuting(payTask)||TaskUtil.isTaskExcuting(mBuyTask)) {
				PromptUtil.showToast(getActivity(), getResources().getString(R.string.warn_repate_commit));
				return;
			}
			time = System.currentTimeMillis();
			if(creditCard!=null){
				if(creditCard!=null && "creditcard".equals(creditCard.getBkcardcardtype())){//保存了这张卡而且是信用卡
					directToPay();
				}else if(creditCard!=null && "bankcard".equals(creditCard.getBkcardcardtype())){//储蓄卡直接前往银联
					if(checkInput()){
						gotoUnipay();
					}
				}else{
					defaultDeal();
				}
			}else{
				defaultDeal();
			}
			
			break;
		case R.id.bank_layout:
			if(isCanSelectBank){
				showBankList();
			}
			break;

		default:
			break;
		}
	}
	
	/**默认处理*/
	private void defaultDeal() {
		if(checkInput()){
			String card=card_edit.getText().toString();
			if(BankCardUtil.isCreditCard(card)){//信用卡支付
				
				if(CreditcardInfoUtil.isYibaoBank(bankList, bank_name.getText().toString())){//是易宝支持的银行，弹框让用户选择
//					goCreditCard();
					
					PromptUtil.showTwoButtonDialog("温馨提示", "您当前使用卡号为信用卡,是否选择信用卡支付通道?", new PositiveListener() {
					
					@Override
					public void onPositive() {//信用卡填写页面
						goCreditCard();
					}
					}, new NegativeListener() {
					
					@Override
					public void onNegative() {//银联支付
						gotoUnipay();
					}
				}, getActivity());
					
				} else {//易宝不支持的银行，直接跳到银联
					gotoUnipay();
				}
				
			}else{//银联支付
				gotoUnipay();
			}
		}
	}
	
	/***前往银联支付*/
	private void gotoUnipay() {
		mBuyTask = new BuyTask();
		mBuyTask.execute("");
	}
	
	/***前往信用卡支付填写页面*/
	private void goCreditCard() {
//		Intent intent=new Intent(getActivity(),CommonEasyCreditcardPayActivity.class);
//		intent.putExtra(CreditCard.PAY_KEY, CreditCard.TRANSFER);
//		intent.putExtra(CreditCard.CARDNO, CommonActivity.mTransferData.getValue(TransferData.fucardno));
//		intent.putExtra(CreditCard.PAYMONEY, CommonActivity.mTransferData.getValue(TransferData.money));
//		intent.putExtra(TransferData.money, CommonActivity.mTransferData.getValue(TransferData.money));
//		intent.putExtra(TransferData.fucardno, CommonActivity.mTransferData.getValue(TransferData.fucardno));
//		intent.putExtra(TransferData.shoucardno, CommonActivity.mTransferData.getValue(TransferData.shoucardno));
//		intent.putExtra(TransferData.paymoney, CommonActivity.mTransferData.getValue(TransferData.paymoney));
//		intent.putExtra(TransferData.payfee, CommonActivity.mTransferData.getValue(TransferData.payfee));
//		
//		intent.putExtra(TransferData.shoucardbank, CommonActivity.mTransferData.getValue(TransferData.shoucardbank));
//		intent.putExtra(TransferData.shoucardman, CommonActivity.mTransferData.getValue(TransferData.shoucardman));
//		intent.putExtra(TransferData.shoucardmobile, CommonActivity.mTransferData.getValue(TransferData.shoucardmobile));
//		
//		intent.putExtra(TransferData.paycardid, CommonActivity.mTransferData.getValue(TransferData.paycardid));
//		intent.putExtra(TransferData.fucardman, CommonActivity.mTransferData.getValue(TransferData.fucardman));
//		intent.putExtra(TransferData.fucardmobile, CommonActivity.mTransferData.getValue(TransferData.fucardmobile));
//		intent.putExtra(TransferData.fucardbank, CommonActivity.mTransferData.getValue(TransferData.fucardbank));//付款银行
//		intent.putExtra(TransferData.arriveid, CommonActivity.mTransferData.getValue(TransferData.arriveid));
//		if(TYPECLASS.tfmg.equals(mType)){
//			intent.putExtra("type_transfer", 0+"");//普通转账
//			intent.putExtra("payType","tfmg");
//		}else{
//			intent.putExtra("type_transfer", 1+"");//超级转账
//			intent.putExtra("payType", "suptfmg");
//		}
//		
//		startActivity(intent);
		
		
		Intent intent=new Intent();
		intent.putExtra(CreditCard.PAY_KEY, CreditCard.TRANSFER);
		intent.putExtra(CreditCard.CARDNO, card_edit.getText().toString());
		intent.putExtra(CreditCard.PAYMONEY, bundleData.getString("money") != null ? bundleData.getString("money") : "");
		intent.putExtra(TransferData.money, bundleData.getString("money") != null ? bundleData.getString("money") : "");
		intent.putExtra(TransferData.fucardno, card_edit.getText().toString());
		intent.putExtra(TransferData.shoucardno, bankRecordData.shoucardno);
		intent.putExtra(TransferData.paymoney, bundleData.getString("paymoney") != null ? bundleData.getString("paymoney") : "");
		intent.putExtra(TransferData.payfee, bundleData.getString("payfee") != null ? bundleData.getString("payfee") : "");
		
		intent.putExtra(TransferData.shoucardbank, bankRecordData.shoucardbank);
		intent.putExtra(TransferData.shoucardman, bankRecordData.shoucardman);
		intent.putExtra(TransferData.shoucardmobile, bankRecordData.shoucardmobile != null ? bankRecordData.shoucardmobile :"");
		
		intent.putExtra(TransferData.paycardid, PayApp.mKeyCode==null ?"":PayApp.mKeyCode);
		intent.putExtra(TransferData.fucardman, open_name_edit.getText().toString());
		intent.putExtra(TransferData.fucardmobile, open_phone_edit.getText().toString());
		intent.putExtra(TransferData.fucardbank, bank_name.getText().toString());//付款银行
		intent.putExtra(TransferData.arriveid, bundleData.getString("arriveid") != null ? bundleData.getString("arriveid") : "");
		if(TYPECLASS.tfmg.equals(mType)){
			intent.putExtra("type_transfer", 0+"");//普通转账
			intent.putExtra("payType","tfmg");
		}else{
			intent.putExtra("type_transfer", 1+"");//超级转账
			intent.putExtra("payType", "suptfmg");
		}
		
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.replace(R.id.func_container, CommonEasyCreditcardPayFragment.newInstance(intent));
		ft.addToBackStack(null);
		ft.commit();
		
//		IMainHandlerManager.handlerUI(UIConstantDefault.UI_CONSTANT_CREDITCARD_PAY, 1, bundleData);
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
			PromptUtil.showToast(getActivity(), "请刷卡");
			return false;
		}
		
		if(!UserInfoCheck.checkBankCard(cardNumber)){
			PromptUtil.showToast(getActivity(), "卡号格式不正确");
			return false;
		}
		CommonActivity.mTransferData.putValue(TransferData.fucardno, cardNumber);
		
		
		String openName = open_name_edit.getText().toString();
		if(null == openName || "".equals(openName)){
			PromptUtil.showToast(getActivity(), "请输入开户名");
			return false;
		}
		
		if(!UserInfoCheck.checkName(openName)){
			PromptUtil.showToast(getActivity(), "姓名格式不正确");
			return false;
		}
		CommonActivity.mTransferData.putValue(TransferData.fucardman, openName);
		String openPhone = open_phone_edit.getText().toString();
		if(null == openPhone || "".equals(openPhone)){
			PromptUtil.showToast(getActivity(), "请输入电话号码");
			return false;
		}
		
		if(!UserInfoCheck.checkMobilePhone(openPhone)){
			PromptUtil.showToast(getActivity(), "手机号码格式不正确");
			return false;
		}
		CommonActivity.mTransferData.putValue(TransferData.fucardmobile, openPhone);
		
		if(PayApp.isSwipIn && PayApp.mKeyData.mType==1){
			PromptUtil.showToast(getActivity(), PayApp.mKeyData.message);
			return false;
		}
		
		
		
		
//		isSwipIn = true;
//		if(!PayApp.isSwipIn){
//			if(null == PayApp.mKeyCode || "".equals(PayApp.mKeyCode)){
//				PromptUtil.showToast(getActivity(), "请刷卡");
//			}else{
//				PromptUtil.showToast(getActivity(), "请插入刷卡器");
//			}
//			return false;
//		}
		CommonActivity.mTransferData.putValue(TransferData.paycardid, PayApp.mKeyCode==null ?"":PayApp.mKeyCode);
		CommonActivity.mTransferData.putValue(JournalData.merReserved, Base64Pay.encode(PayApp.merReserved.toString().getBytes()));
		
		return true;
	}
	
	/**
	 * 构建请求数据
	 */
	public CommonData getRequestData() {
		if(bundleData == null) {
			return null;
		}
		CommonData data = new CommonData();
		//业务信息
		data.putValue("paymoney", bundleData.getString("paymoney") != null ? bundleData.getString("paymoney") : "");
		data.putValue("payfee", bundleData.getString("payfee") != null ? bundleData.getString("payfee") : "");
		data.putValue("money", bundleData.getString("money") != null ? bundleData.getString("money") : "");
		data.putValue("current", bundleData.getString("current") != null ? bundleData.getString("current") : "");
		data.putValue("arriveid", bundleData.getString("arriveid") != null ? bundleData.getString("arriveid") : "");
		data.putValue(JournalData.merReserved, Base64Pay.encode(PayApp.merReserved.toString().getBytes()));
		data.putValue(TransferData.paycardid, PayApp.mKeyCode==null ?"":PayApp.mKeyCode);
		
		//收款卡信息
		data.putValue("shoucardman", bankRecordData.shoucardman);
		data.putValue("shoucardno", bankRecordData.shoucardno);
		data.putValue("shoucardbank", bankRecordData.shoucardbank);
		
		//付款卡信息
		data.putValue("fucardno", card_edit.getText().toString());
		data.putValue("fucardbank", bank_name.getText().toString());
		data.putValue("fucardman", open_name_edit.getText().toString());
		data.putValue("fucardmobile", open_phone_edit.getText().toString());
		
		return data;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		 if (data == null) {
	            return;
	     }
		 String bankid = data.getStringExtra(BankListActivity.BANK_ID);
		 String bankname = data.getStringExtra(BankListActivity.BANK_NAME);
		 if(null != bankname &&!"".equals(bankname)){
			 isSelectedBank=true;
			 CommonActivity.mTransferData.putValue(TransferData.fucardbank, bankname);
			 bank_name.setText(bankname);
		 }
		 
		 //银联支付结果
		 
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
		
		if(mKeyTask!=null){
			mKeyTask.cancel(true);
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
//		startCallStateService();
		initSwipPic(PayApp.isSwipIn);
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
		UnionpayUtil.startUnionPlungin(mBkntno, getActivity());
	}
	
	class BuyTask extends AsyncTask<String, Integer, Boolean>{
		ProtocolRsp mRsp;
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				List<ProtocolData> mDatas=null;
				if(TYPECLASS.tfmg.equals(mType)){
					mDatas = ProtocolUtil.getRequestDatas("ApiPayinfo", 
								"transferMoneyRq", /**CommonActivity.mTransferData*/getRequestData());
				}else{
					mDatas = ProtocolUtil.getRequestDatas("ApiPayinfo", 
							"SuptransferMoneyRq", /**CommonActivity.mTransferData*/getRequestData());
				}
				
				TransferNoParser authorRegParser = new TransferNoParser();
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
				PromptUtil.showToast(getActivity(),getString(R.string.net_error));
			}else{
				try {
					List<ProtocolData> mDatas = mRsp.mActions;
					parserResoponse(mDatas);
					
					
					if(!ErrorUtil.create().dealErrorWithDialog(LoginUtil.mLoginStatus,getActivity())){
						return;
					}
				
					if(LoginUtil.mLoginStatus.result.equals(ProtocolUtil.SUCCESS)){
//						if(getActivity() instanceof CommonActivity){
							mCommonData.clear();
							
//							((CommonActivity)getActivity()).mBankNo = mBkntno;
							
//							HashMap<String, String> item = new HashMap<String, String>();
//							item.put("交易请求号", mBkntno);
//							mCommonData.add(item);
							
							HashMap<String, String> item1 = new HashMap<String, String>();
							item1.put("收款卡号",bankRecordData.shoucardno
//									CommonActivity.mTransferData.getValue(TransferData.shoucardno)
									);
							mCommonData.add(item1);
							
							HashMap<String, String> item20 = new HashMap<String, String>();
							item20.put("收款银行",bankRecordData.shoucardbank
									);
							mCommonData.add(item20);
							
							HashMap<String, String> item21 = new HashMap<String, String>();
							item21.put("收款人",bankRecordData.shoucardman
									);
							mCommonData.add(item21);
							
							HashMap<String, String> item2 = new HashMap<String, String>();
							item2.put("付款卡号", card_edit.getText().toString());
							mCommonData.add(item2);
							
							HashMap<String, String> item22 = new HashMap<String, String>();
							item22.put("付款银行",bank_name.getText().toString()
									);
							mCommonData.add(item22);
							
							HashMap<String, String> item23 = new HashMap<String, String>();
							item23.put("付款人",open_name_edit.getText().toString()
									);
							mCommonData.add(item23);
							
							
							HashMap<String, String> item4 = new HashMap<String, String>();
							item4.put("转账金额", bundleData.getString("paymoney") != null ? NumberFormatUtil.format2(bundleData.getString("paymoney"))+"元" : "");
							mCommonData.add(item4);
							
							HashMap<String, String> item5 = new HashMap<String, String>();
							item5.put("手续费", bundleData.getString("payfee") != null ? NumberFormatUtil.format2(bundleData.getString("payfee"))+"元" : "");
							mCommonData.add(item5);
							
							HashMap<String, String> item6 = new HashMap<String, String>();
							item6.put("支付金额", bundleData.getString("money") != null ? NumberFormatUtil.format2(bundleData.getString("money"))+"元" : "");
							mCommonData.add(item6);
						showChuxuka();
//						}
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

	private DefaultBankCardData creditCard;
	private String orderId;
	@Override
	public void onSuccess(Object obj, Class cla) {
		if (cla.equals(SmsCode.class)) {
			SmsCode sms = (SmsCode) obj;
			if (sms != null) {
				orderId = sms.getOrderId();
//				HashMap<String, String> item = new HashMap<String, String>();
//				item.put("交易请求号", orderId);
//				mCommonData.add(item);
				if (sms.isNeed()) {// 需要验证码
					SmsCodeDialog dialog = new SmsCodeDialog();
					dialog.show(getActivity(), this);
				} else {// 不需要验证码,表示支付成功
					PromptUtil.showToast(getActivity(), sms.getMessage());
					goPaySunccess();
				}
			}
		}
	}
	
	

	/***直接使用信用卡支付*/
	private void directToPay() {
		isSelectedBank=true;
		checkInput();
		mCommonData.clear();
//		((CommonActivity)getActivity()).mBankNo = mBkntno;
		
		HashMap<String, String> item1 = new HashMap<String, String>();
		item1.put("收款卡号",
				bankRecordData.shoucardno
				);
		mCommonData.add(item1);
		
		HashMap<String, String> item20 = new HashMap<String, String>();
		item20.put("收款银行",bankRecordData.shoucardbank
				);
		mCommonData.add(item20);
		
		HashMap<String, String> item21 = new HashMap<String, String>();
		item21.put("收款人",bankRecordData.shoucardman
				);
		mCommonData.add(item21);
		
		HashMap<String, String> item2 = new HashMap<String, String>();
		item2.put("付款卡号", card_edit.getText().toString());
		mCommonData.add(item2);
		
		HashMap<String, String> item22 = new HashMap<String, String>();
		item22.put("付款银行",bank_name.getText().toString()
				);
		mCommonData.add(item22);
		
		HashMap<String, String> item23 = new HashMap<String, String>();
		item23.put("付款人",open_name_edit.getText().toString()
				);
		mCommonData.add(item23);
		
		HashMap<String, String> item4 = new HashMap<String, String>();
		item4.put("转账金额", bundleData.getString("paymoney") != null ? NumberFormatUtil.format2(bundleData.getString("paymoney"))+"元" : "");
		mCommonData.add(item4);
		
		HashMap<String, String> item5 = new HashMap<String, String>();
		item5.put("手续费", bundleData.getString("payfee") != null ? NumberFormatUtil.format2(bundleData.getString("payfee"))+"元" : "");
		mCommonData.add(item5);
		
		HashMap<String, String> item6 = new HashMap<String, String>();
		item6.put("支付金额", bundleData.getString("money") != null ? NumberFormatUtil.format2(bundleData.getString("money"))+"元" : "");
		mCommonData.add(item6);
		
		String transferType="0";
		String payType="tfmg";
		
		if(TYPECLASS.tfmg.equals(mType)){
			transferType="0";//普通转账
			payType="tfmg";
		}else{
			transferType="1";//超级转账
			payType="suptfmg";
		}
		
		payTask=new TransferCreditCardTask(getActivity(), this);
		payTask.execute(
				creditCard.getBkcardno(), 
				creditCard.getBkcardbankcode(), 
				creditCard.getBkcardidcard(),
				creditCard.getBkcardbankphone(), 
				creditCard.getBkcardbankman(),
				creditCard.getBkcardyxyear(),
				creditCard.getBkcardyxmonth(), 
				creditCard.getBkcardcvv(), 
				PayApp.mKeyCode==null ?"":PayApp.mKeyCode,
				bundleData.getString("paymoney") != null ? bundleData.getString("paymoney") : "", 
				bundleData.getString("money") != null ? bundleData.getString("money") : "", 
				bankRecordData.shoucardno, 
				bankRecordData.shoucardbank, 
				bankRecordData.shoucardman, 
				bankRecordData.shoucardmobile != null?bankRecordData.shoucardmobile:"",
				transferType,
				payType,
				bundleData.getString("arriveid") != null ? bundleData.getString("arriveid") : "",
				creditCard.getBkcardbank()
				);
		
	}

	@Override
	public void onPositive(String code, SmsCodeDialog dialog) {
		String funName = "ApiTransferMoney";
		String func = "PayWithVerifyCode";
		new SmsCodeTask(getActivity(), new ResponseStateListener() {

			@Override
			public void onSuccess(Object obj, Class cla) {
				PromptUtil.showToast(getActivity(), (String) obj);
				// dialog.dismiss();
				goPaySunccess();
			}
			
			

		}).execute(orderId, code, funName, func);
	}
	
	private void goPaySunccess() {
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.replace(R.id.func_container, TransferSuccessFragment.createFragment(mCommonData));
		ft.addToBackStack(null);
		ft.commit();
	}

	@Override
	public void onSuccess(Object protocolDataList, Bundle bundle) {
		creditCard = (DefaultBankCardData) protocolDataList;
		if(creditCard!=null){
			if(creditCard.getBkcardbankman()!=null){
				open_name_edit.setText(creditCard.getBkcardbankman());
				open_name_edit.setEnabled(false);
			}else{
				open_name_edit.setText("");
				open_name_edit.setEnabled(true);
			}
			if(creditCard.getBkcardbankphone()!=null){
				open_phone_edit.setText(creditCard.getBkcardbankphone());
				open_phone_edit.setEnabled(false);
			}else{
				open_phone_edit.setText("");
				open_phone_edit.setEnabled(true);
			}
			if(creditCard.getBkcardbank()!=null){
				bank_name.setText(creditCard.getBkcardbank());
				isCanSelectBank=false;
				isSelectedBank=true;
			}else{
				bank_name.setText("点击选择");
				isCanSelectBank=true;
				isSelectedBank=false;
			}
		}
	}

	@Override
	public void onFailure(String error) {
		
	}

	@Override
	public void requestBuySuccess(final ResultData resultData, String msg) {
		resultData.putValue(ResultData.bkntno, mBkntno);
		
		if(TYPECLASS.tfmg.equals(mType)){
			new BuySuccessTask(getActivity(), resultData,"ApiPayinfo","insertTransferMoney").execute("");
		}else {
			new BuySuccessTask(getActivity(), resultData,"ApiPayinfo","insertSupTransferMoney").execute("");
		}
		
//		new BuySuccessTask(getActivity(), resultData,"ApiExpresspayInfo","agentorderPayrqStatus").execute("");
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
                	goPaySunccess();
                }else{
//                		getActivity().finish();
//                	goPaySunccess();
                }
               
            }
        });
        builder.create().show();
		
	}
	
	/**
	 * 初始化加载易宝所支持的银行列表
	 */
	private void loadYibaoBanks() {
		new BankTask(getActivity(), new ResponseStateListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(Object obj, Class cla) {
				bankList=(ArrayList<BankData>) obj;
			}
		},true).execute("");
	}
	
	/**
	 * 易宝银行列表数据
	 */
	private ArrayList<BankData> bankList = null;
	
}
