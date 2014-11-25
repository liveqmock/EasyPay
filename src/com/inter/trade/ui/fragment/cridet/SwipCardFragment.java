package com.inter.trade.ui.fragment.cridet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import com.inter.trade.AsyncLoadWork;
import com.inter.trade.AsyncLoadWork.AsyncLoadWorkListener;
import com.inter.trade.R;
import com.inter.trade.SwipKeyTask;
import com.inter.trade.data.CardData;
import com.inter.trade.data.CommonData;
import com.inter.trade.data.ResponseData;
import com.inter.trade.error.ErrorUtil;
import com.inter.trade.net.HttpUtil;
import com.inter.trade.ui.BankListActivity;
import com.inter.trade.ui.ConfirmActivity;
import com.inter.trade.ui.CridetPayActivity;
import com.inter.trade.ui.PayApp;
import com.inter.trade.ui.PayApp.SwipListener;
import com.inter.trade.ui.creditcard.CommonEasyCreditcardPayActivity;
import com.inter.trade.ui.creditcard.SmsCodeDialog;
import com.inter.trade.ui.creditcard.SmsCodeDialog.SmsCodeSubmitListener;
import com.inter.trade.ui.creditcard.data.DefaultBankCardData;
import com.inter.trade.ui.creditcard.data.SmsCode;
import com.inter.trade.ui.creditcard.task.CaiSmsCodeTask;
import com.inter.trade.ui.creditcard.util.CreditCard;
import com.inter.trade.ui.factory.ConfrimFactory;
import com.inter.trade.ui.fragment.BaseFragment;
import com.inter.trade.ui.fragment.FragmentFactory;
import com.inter.trade.ui.fragment.cridet.data.JournalData;
import com.inter.trade.ui.fragment.cridet.parser.CridetNoParser;
import com.inter.trade.ui.fragment.cridet.task.CreditCardFeeParser;
import com.inter.trade.ui.fragment.cridet.task.CreditCardPayParser;
import com.inter.trade.ui.fragment.cridet.task.CreditCardfeeData2;
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

/**
 * 刷卡
 * @author apple
 *
 */

public class SwipCardFragment extends BaseFragment implements OnClickListener,SwipListener, SmsCodeSubmitListener{
	
	private Button cridet_back_btn;
	private ImageView swip_card;
	private TextView swip_prompt;
	private EditText card_edit;
	private LinearLayout bank_layout;
	private TextView bank_name;
	private EditText open_name_edit;
	private EditText open_phone_edit;
	private TextView acount;
	private TextView repay_money;//还款金额
	private String repay_money_str;//还款金额
	
	private LinearLayout coupon_layout;
	
	private String mBkntno;
	private String mMessage ="";
	private String mResult =""; 
	private static String feemoney;
	private static String allmoney;
	private static String paymoney;
	private boolean isSelectedBank=false;
	
	private BuyTask mBuyTask;
	private CridetPayActivity mActivity;
	
	CreditCardfeeData2 fee = null;
	
	/**
	 * 标记是否可以选中银行
	 */
	private boolean isCanSelectBank=true;
	
	public SwipCardFragment()
	{
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		LoginUtil.detection(getActivity());
//		initReader();
		PayApp.mSwipListener = this;
		if(getActivity() instanceof CridetPayActivity){
			 mActivity = (CridetPayActivity)getActivity();
		}
		
		
	}
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.coupon_cridet_layout, container,false);
		initView(view);
		
		setTitle("付款");
		setBackVisible();
//		openReader();
		getCreditCardMoneyPayfee ();
		return view;
	}
	
	/**
	 * 获取手续费
	 */
	private void getCreditCardMoneyPayfee () {
		
		CreditCardFeeParser netParser = new CreditCardFeeParser();
		
		CommonData data = new CommonData();
		data.putValue("bankid", "");
		data.putValue("money", CridetCardFragment.mJournalData.getValue(JournalData.paymoney));//支付金额
		
		getFeeAsyncLoadWork = new AsyncLoadWork<CreditCardfeeData2>(getActivity(), netParser, data, getCreditCardMoneyPayfeeListener, false, true);
		getFeeAsyncLoadWork.execute("ApiPayinfo", "getcreditCardMoneyPayfee");
		
	}
	
	 private AsyncLoadWorkListener getCreditCardMoneyPayfeeListener = new AsyncLoadWorkListener() {

		@Override
		public void onSuccess(Object protocolDataList, Bundle bundle) {
			if (protocolDataList == null) return;
			ArrayList<CreditCardfeeData2> tempList = (ArrayList<CreditCardfeeData2>)protocolDataList;
			if(tempList.size() > 0) {
				fee = tempList.get(0);
			}
			if(fee != null) {
				//更新UI
				repay_money.setText(NumberFormatUtil.format2(fee.allmoney)+"元");
				repay_money_str = fee.allmoney;
			}
			
			Log.i("get fee:", tempList.size()+"");
			
		}

		@Override
		public void onFailure(String error) {
			// TODO Auto-generated method stub
			Log.i("get fee:", "onFailure");
			if(getActivity() != null) {
				getActivity().finish();
			}
		}
		 
	};
	
//	/**
//	 * 刷卡后查询该银行卡是否存储过，如果存储过则自动填充剩余数据
//	 * 如果有存储数据且是信用卡，用户提交的时候自动去提交（不跳到信用卡填写页面）
//	 */
//	private void queryBankCard (String cardNum) {
//		if(cardNum == null || cardNum.equals("")) return;
//		
//		new PayCardCheckTask(getActivity(), new ResponseStateListener() {
//			
//			@Override
//			public void onSuccess(Object obj, Class cla) {
//				
//				if(obj != null) {
//					defaultBankCardData = (DefaultBankCardData)obj;
//				}
//				if(defaultBankCardData != null) {
//					//自动填充付款信息
//					
//				}
//				
//			}
//		}, true).execute(PayApp.mKeyCode != null ? PayApp.mKeyCode : "", cardNum, "creditcard");
//	}
	
	
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
		coupon_layout = (LinearLayout)view.findViewById(R.id.coupon_layout);
		repay_money = (TextView)view.findViewById(R.id.repay_money);
		
		coupon_layout.setVisibility(View.GONE);
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
		
//		queryBankCard (data.pan);
	}

	@Override
	public void checkedCard(boolean flag) {
		// TODO Auto-generated method stub
		if(flag){
			boolean s = PayApp.openReaderNow();
			log("status : "+s);
			swip_prompt.setText(getString(R.string.has_checked_swip));
			swip_card.setBackgroundDrawable(getResources().getDrawable(R.drawable.swip_enable));
		}else{
			swip_prompt.setText(getString(R.string.cridet_insert));
			swip_card.setBackgroundDrawable(getResources().getDrawable(R.drawable.swip_card_bg));
		}
	}
	
	private void showDialog(){
		 AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	        builder.setTitle("还款手续费");
	        builder.setMessage("本次还款手续费：2元，点击确认完成还款操作");
	        builder.setInverseBackgroundForced(true);
	        builder.setCancelable(false);
	        //builder.setCustomTitle();
	        builder.setNegativeButton("确认", new DialogInterface.OnClickListener() {
	            @Override
	            public void onClick(DialogInterface dialog, int which) {
	                dialog.dismiss();
	                mBuyTask = new BuyTask();
					mBuyTask.execute("");
//	                FragmentTransaction transaction = getFragmentManager().beginTransaction();
//					transaction.replace(R.id.func_container, new CridetConfirmFragment());
//					transaction.commit();
	            }
	        });
	        builder.setPositiveButton("返回", new DialogInterface.OnClickListener() {
	            @Override
	            public void onClick(DialogInterface dialog, int which) {
	                dialog.dismiss();
	               
	            }
	        });
	        builder.create().show();
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
			
			mKeyTask = new SwipKeyTask(getActivity(), PayApp.mKeyCode, PayApp.mKeyData, card_edit.getText().toString(), "creditcard", new AsyncLoadWorkListener() {
				@Override
				public void onSuccess(Object protocolDataList, Bundle bundle) {
					if(protocolDataList != null) {
						defaultBankCardData = (DefaultBankCardData)protocolDataList;
					}
					if(defaultBankCardData != null) {
						//自动填充付款信息
//						if(checkDefaultBankCardData()) {
//							autoFullDatas();
//						} else{
//							cleanUiInfo();
//						}
						fillCarInfo(defaultBankCardData);
					} else {
						cleanUiInfo();
					}
//					
				}
				@Override
				public void onFailure(String error) {
				}
			});
			mKeyTask.execute("");
			break;
		default:
			break;
		}
		
	}
	
	/**
	 * 刷卡后填充卡信息
	 */
	private void fillCarInfo(DefaultBankCardData creditCard) {
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
			
			if(creditCard.getBkcardbankid()!=null){
			    CridetCardFragment.mJournalData.putValue(JournalData.fucardbankid, creditCard.getBkcardbankid());
			}
			
			if(creditCard.getBkcardbank()!=null){
				bank_name.setText(creditCard.getBkcardbank());
				CridetCardFragment.mJournalData.putValue(JournalData.fucardbank, creditCard.getBkcardbank());
//				isCanSelectBank=false;
				isSelectedBank=true;
			}else{
				bank_name.setText("点击选择");
//				isCanSelectBank=true;
				isSelectedBank=false;
			}
			
			
		}
		
	}
	
	/**
	 * 检查返回的银行卡信息有效性
	 * @param defaultBankCardData
	 * @return
	 */
	public boolean checkDefaultBankCardData () {
		if(defaultBankCardData == null) return false;
		
		if(defaultBankCardData.getBkcardbank() == null || defaultBankCardData.getBkcardbank().equals("")) {
			return false;
			
		}
		if(defaultBankCardData.getBkcardbankid() == null || defaultBankCardData.getBkcardbankid().equals("")) {
			return false;
			
		}
		
		if(defaultBankCardData.getBkcardbankphone() == null || defaultBankCardData.getBkcardbankphone().equals("")) {
			return false;
			
		}
		if(defaultBankCardData.getBkcardbankman() == null || defaultBankCardData.getBkcardbankman().equals("")) {
			return false;
			
		}
		if(defaultBankCardData.getBkcardcardtype() == null || defaultBankCardData.getBkcardcardtype().equals("")) {
			return false;
			
		}
		
		return true;
	}
	
	/**
	 * 清除UI数据
	 */
	private void cleanUiInfo() {
		bank_name.setText("点击选择");
		open_name_edit.setText("");
		open_phone_edit.setText("");
		isSelectedBank=false;
		open_name_edit.setEnabled(true);
		open_phone_edit.setEnabled(true);
		isCanSelectBank=true;
	}
	
	/**
	 * 自动UI页面填充数据
	 */
	private void autoFullDatas() {
		getActivity().runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				if(defaultBankCardData != null) {
					bank_name.setText(defaultBankCardData.getBkcardbank());
					open_name_edit.setText(defaultBankCardData.getBkcardbankman());
					open_phone_edit.setText(defaultBankCardData.getBkcardbankphone());
					isSelectedBank=true;
					
					open_name_edit.setEnabled(false);
					open_phone_edit.setEnabled(false);
					isCanSelectBank=false;
				}else {
					bank_name.setText("");
					open_name_edit.setText("");
					open_phone_edit.setText("");
					isSelectedBank=false;
					open_name_edit.setEnabled(true);
					open_phone_edit.setEnabled(true);
					isCanSelectBank=true;
				}
			}
		});
		
	}
	long time=0L;
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.cridet_back_btn:
			
			
			if(checkInput()){
				//1秒内，禁止双击两次提交
				long currentTime = System.currentTimeMillis();
				if (currentTime - time < 1000 || TaskUtil.isTaskExcuting(asyncLoadWork)||TaskUtil.isTaskExcuting(mBuyTask)) {
					PromptUtil.showLongToast(getActivity(), getResources().getString(R.string.warn_repate_commit));
					return;
				}
				time = System.currentTimeMillis();
				
//				if(isCreditCard){//信用卡支付
//					gotoCreditCard();
//				}else{//银联支付
//					//showChuxuka();
//					mBuyTask = new BuyTask();
//					mBuyTask.execute("");
//					//showDialog();
//				}
				
				//检查后台返回的银行卡信息有效性
				if(checkDefaultBankCardData()) {
					
					if( defaultBankCardData != null) {
//						if(defaultBankCardData.getBkcardcardtype() != null 
//								&& defaultBankCardData.getBkcardcardtype().equals("creditcard")) {//信用卡
//							
//							gotoCreditCardPay();
//							
//						} 
//						else if(defaultBankCardData.getBkcardcardtype() != null 
//								&& defaultBankCardData.getBkcardcardtype().equals("authorbkcard")) {//储蓄卡
//							
//							gotoDeposit();
//							
//						}
						
						if(defaultBankCardData.getBkcardcardtype() != null ) {
							if(defaultBankCardData.getBkcardcardtype().equals("creditcard")) {//信用卡
								
								gotoCreditCardPay();
								
							} 
							else {//储蓄卡
								
								gotoDeposit();
								
							}
						}
						
					}
				} else{
					String bankno = card_edit.getText().toString();//付款卡号
					if(BankCardUtil.isCreditCard(bankno)){//信用卡
						
						PromptUtil.showTwoButtonDialog("温馨提示", "您当前使用卡号为信用卡,是否选择信用卡支付通道?", new PositiveListener() {
							
							@Override
							public void onPositive() {//信用卡填写页面
								gotoCreditCard();
							}
						}, new NegativeListener() {
							
							@Override
							public void onNegative() {//银联支付
								gotoDeposit();
							}
						}, getActivity());
					}else{
						gotoDeposit();
					}
				}
				
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
	
	
	/**
	 * 订单id
	 */
	private String orderId;
	
	private SmsCodeDialog dialog;
	
	/**
	 * 统一的异步加载
	 */
	private AsyncLoadWork<SmsCode> asyncLoadWork = null;
	
	private AsyncLoadWork<CreditCardfeeData2> getFeeAsyncLoadWork = null;
	
	/**
	 * 易宝支付短信SmsCode
	 */
	SmsCode mSmsCode = null;
	
	DefaultBankCardData defaultBankCardData;
	
	/**
	 * 不弹新页面，直接信用卡支付， 等待短信验证
	 */
	private void gotoCreditCardPay() {
		
		if(fee == null) {
			PromptUtil.showToast(getActivity(), "手续费获取失败");
			return;
		}
		
		ConfirmActivity.mCommonData.clear();
		HashMap<String, String> item26 = new HashMap<String, String>();
		item26.put("收款卡号:", CridetCardFragment.mJournalData.getValue(JournalData.shoucardno));
		ConfirmActivity.mCommonData.add(item26);
		
		HashMap<String, String> item21 = new HashMap<String, String>();
		item21.put("收款人姓名", CridetCardFragment.mJournalData.getValue(JournalData.shoucardman));
		ConfirmActivity.mCommonData.add(item21);
		
		HashMap<String, String> item22= new HashMap<String, String>();
		item22.put("收款银行", CridetCardFragment.mJournalData.getValue(JournalData.shoucardbank));
		ConfirmActivity.mCommonData.add(item22);
		
		HashMap<String, String> item27 = new HashMap<String, String>();
		item27.put("付款卡号:", CridetCardFragment.mJournalData.getValue(JournalData.fucardno));
		ConfirmActivity.mCommonData.add(item27);
		
		HashMap<String, String> item23 = new HashMap<String, String>();
		item23.put("付款人姓名", CridetCardFragment.mJournalData.getValue(JournalData.fucardman));
		ConfirmActivity.mCommonData.add(item23);
		
		HashMap<String, String> item24= new HashMap<String, String>();
		item24.put("付款银行", CridetCardFragment.mJournalData.getValue(JournalData.fucardbank));
		ConfirmActivity.mCommonData.add(item24);
		
		HashMap<String, String> item28 = new HashMap<String, String>();
		item28.put("还款金额:", CridetCardFragment.mJournalData.getValue(JournalData.paymoney)+"元");
		ConfirmActivity.mCommonData.add(item28);
		
		HashMap<String, String> itemfeemoney = new HashMap<String, String>();
		itemfeemoney.put("手续费:", fee.feemoney+"元");
		ConfirmActivity.mCommonData.add(itemfeemoney);
		
		HashMap<String, String> itemallmoney = new HashMap<String, String>();
		itemallmoney.put("支付金额:", fee.allmoney+"元");
		ConfirmActivity.mCommonData.add(itemallmoney);
		
		
		
		CreditCardPayParser netParser = new CreditCardPayParser();
		
		CommonData data = new CommonData();
		
		data.putValue("paytype", "creditcard");//交易类型
		data.putValue("paymoney", CridetCardFragment.mJournalData.getValue(JournalData.paymoney));//交易金额
		data.putValue("allmoney", fee.allmoney);//刷卡金额
		data.putValue("feemoney", fee.feemoney);//手续费
		
		data.putValue("shoucardno", CridetCardFragment.mJournalData.getValue(JournalData.shoucardno));//收款卡号
		data.putValue("shoucardmobile", CridetCardFragment.mJournalData.getValue(JournalData.shoucardmobile));//收款人手机
		data.putValue("shoucardman", CridetCardFragment.mJournalData.getValue(JournalData.shoucardman));//收款人姓名
		data.putValue("shoucardbank", CridetCardFragment.mJournalData.getValue(JournalData.shoucardbank));//收款银行
		
		data.putValue("current", CridetCardFragment.mJournalData.getValue(JournalData.current));//币种
		data.putValue("paycardid", CridetCardFragment.mJournalData.getValue(JournalData.paycardid));//刷卡器key
		data.putValue("merReserved", CridetCardFragment.mJournalData.getValue(JournalData.merReserved));//银行卡信息保留域
		
		
		
		//从后台获取到保存过的卡进行自动填充
		data.putValue("bkcardbank", defaultBankCardData.getBkcardbank()!=null ? defaultBankCardData.getBkcardbank() :"");//付款银行名
		data.putValue("bkCardno", defaultBankCardData.getBkcardno()!=null ?defaultBankCardData.getBkcardno():"");//银行卡号
		data.putValue("bkcardman", defaultBankCardData.getBkcardbankman() !=null ? defaultBankCardData.getBkcardbankman() :"");//执卡人
		data.putValue("bkcardexpireMonth", defaultBankCardData.getBkcardyxmonth() !=null ? defaultBankCardData.getBkcardyxmonth() :"");//月份
		data.putValue("bkcardmanidcard", defaultBankCardData.getBkcardidcard() !=null ? defaultBankCardData.getBkcardidcard():"");//执卡人身份证
		data.putValue("bankid", defaultBankCardData.getBkcardbankid() !=null ? defaultBankCardData.getBkcardbankid():"");//银行id
		data.putValue("bkcardexpireYear", defaultBankCardData.getBkcardyxyear() !=null ? defaultBankCardData.getBkcardyxyear():"");//年
		data.putValue("bkcardPhone", defaultBankCardData.getBkcardbankphone() !=null ? defaultBankCardData.getBkcardbankphone():"");//银行预留手机号码
		data.putValue("bkcardcvv", defaultBankCardData.getBkcardcvv() !=null ? defaultBankCardData.getBkcardcvv():"");//cvv
		
		asyncLoadWork = new AsyncLoadWork<SmsCode>(getActivity(), netParser, data, creditCardPayListener, false, true);
		asyncLoadWork.execute("ApiyibaoPayInfo", "creditCardMoneyRq");
	}
	
	private AsyncLoadWorkListener creditCardPayListener = new AsyncLoadWorkListener() {

		@Override
		public void onSuccess(Object protocolDataList, Bundle bundle) {
			if (protocolDataList == null) return;
			
			ArrayList<SmsCode> tempList = (ArrayList<SmsCode>)protocolDataList;
			if(tempList.size() > 0) {
				mSmsCode = tempList.get(0);
			}
			
			if (mSmsCode != null) {
				orderId = mSmsCode.getOrderId();
				if (mSmsCode.isNeed()) {// 需要验证码
					dialog=new SmsCodeDialog();
					dialog.show(getActivity(), SwipCardFragment.this);
				} else {// 不需要验证码,表示支付成功
					PromptUtil.showToast(getActivity(), mSmsCode.getMessage());
					gotoPaySunccess();
				}
			}
		}

		@Override
		public void onFailure(String error) {
			
		}
		
	};
	
	@Override
	public void onPositive(String code, SmsCodeDialog dialog) {

		new CaiSmsCodeTask(getActivity(), new ResponseStateListener() {

			@Override
			public void onSuccess(Object obj, Class cla) {
				gotoPaySunccess();
			}
		}).execute(code,
				mSmsCode.getBkordernumber(),
				mSmsCode.getBkntno(),
				mSmsCode.getVerifytoken(),
				"ApiyibaoPayInfo", "creditCardMoneySMSverify");
		//隐藏键盘
		hideSoftInput();
	}
	
	/**
	 * 前往银联支付
	 * 
	 * @throw
	 * @return void
	 */
	private void gotoDeposit() {
		mBuyTask = new BuyTask();
		mBuyTask.execute("");
	}
	
	/**
	 * 信用卡支付
	 */
	private void gotoCreditCard() {
		if(fee == null) {
			PromptUtil.showToast(getActivity(), "手续费获取失败");
			return;
		}
		Intent intent=new Intent(getActivity(),CommonEasyCreditcardPayActivity.class);
		//信用卡默认信息
		intent.putExtra(CreditCard.PAY_KEY, CreditCard.REPAY_CREDITCARD);
		intent.putExtra(CreditCard.CARDNO, CridetCardFragment.mJournalData.getValue(JournalData.fucardno));//付款卡号
		intent.putExtra(CreditCard.PAYMONEY, fee.allmoney);
		
		//支付信息
		intent.putExtra(JournalData.paytype, CridetCardFragment.mJournalData.getValue(JournalData.paytype));
		intent.putExtra(JournalData.current, CridetCardFragment.mJournalData.getValue(JournalData.current));
		intent.putExtra(JournalData.paymoney, CridetCardFragment.mJournalData.getValue(JournalData.paymoney));
		intent.putExtra(JournalData.paycardid, CridetCardFragment.mJournalData.getValue(JournalData.paycardid));
		intent.putExtra(JournalData.merReserved, CridetCardFragment.mJournalData.getValue(JournalData.merReserved));
		
		//收款信息
		intent.putExtra(JournalData.shoucardno, CridetCardFragment.mJournalData.getValue(JournalData.shoucardno));
		intent.putExtra(JournalData.shoucardman, CridetCardFragment.mJournalData.getValue(JournalData.shoucardman));
		intent.putExtra(JournalData.shoucardmobile, CridetCardFragment.mJournalData.getValue(JournalData.shoucardmobile));
		intent.putExtra(JournalData.shoucardbank, CridetCardFragment.mJournalData.getValue(JournalData.shoucardbank));
		
		//付款信息
		intent.putExtra(JournalData.fucardno, CridetCardFragment.mJournalData.getValue(JournalData.fucardno));
		intent.putExtra(JournalData.fucardman, CridetCardFragment.mJournalData.getValue(JournalData.fucardman));
		intent.putExtra(JournalData.fucardmobile, CridetCardFragment.mJournalData.getValue(JournalData.fucardmobile));
		intent.putExtra(JournalData.fucardbank, CridetCardFragment.mJournalData.getValue(JournalData.fucardbank));
		intent.putExtra(JournalData.fucardbankid, CridetCardFragment.mJournalData.getValue(JournalData.fucardbankid));
		
		if(fee != null) {//手续费
			intent.putExtra(JournalData.allmoney, fee.allmoney);
			intent.putExtra(JournalData.feemoney, fee.feemoney);
		}
		
		startActivityForResult(intent, 16);
	}
	
	private void gotoPaySunccess() {
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.replace(R.id.func_container, CridetSuccessFragment.createFragment(ConfirmActivity.mCommonData));
		ft.commit();
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
		
		CridetCardFragment.mJournalData.putValue(JournalData.fucardno, cardNumber);
		
		String openName = open_name_edit.getText().toString();
		if(null == openName || "".equals(openName)){
			PromptUtil.showToast(getActivity(), "请输入开户名");
			return false;
		}
		
		if(!UserInfoCheck.checkName(openName)){
			PromptUtil.showToast(getActivity(), "姓名格式不正确");
			return false;
		}
		
		CridetCardFragment.mJournalData.putValue(JournalData.fucardman, openName);
		String openPhone = open_phone_edit.getText().toString();
		if(null == openPhone || "".equals(openPhone)){
			PromptUtil.showToast(getActivity(), "请输入电话号码");
			return false;
		}
		
		if(!UserInfoCheck.checkMobilePhone(openPhone)){
			PromptUtil.showToast(getActivity(), "手机号码格式不正确");
			return false;
		}
		CridetCardFragment.mJournalData.putValue(JournalData.fucardmobile, openPhone);
		
		
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
		CridetCardFragment.mJournalData.putValue(JournalData.paycardid, PayApp.mKeyCode != null ? PayApp.mKeyCode : "");
		CridetCardFragment.mJournalData.sunMap.put(JournalData.merReserved, Base64Pay.encode(PayApp.merReserved.toString().getBytes()));
		
		return true;
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
			 bank_name.setText(bankname);
			 CridetCardFragment.mJournalData.putValue(JournalData.fucardbank, bankname);
		 }
		 if(null != bankid &&!"".equals(bankid)){
			 CridetCardFragment.mJournalData.putValue(JournalData.fucardbankid, bankid);
			 Log.i("fucardbankid:", bankid);
		 }else{
			 CridetCardFragment.mJournalData.putValue(JournalData.fucardbankid, "");
			 Log.i("fucardbankid2:", bankid);
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
	private void initSwipPic(boolean flag){
		if(flag){
			swip_prompt.setText(getString(R.string.has_checked_swip));
			swip_card.setBackgroundDrawable(getResources().getDrawable(R.drawable.swip_enable));
		}else{
			swip_prompt.setText(getString(R.string.cridet_insert));
			swip_card.setBackgroundDrawable(getResources().getDrawable(R.drawable.swip_card_bg));
		}
	}
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
//		mobileReader.close();
//		closeReader();
//		endCallStateService();
		log("onpause endCallStateService");
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setTitle("付款");
		initSwipPic(PayApp.isSwipIn);
//		mobileReader.open(false);
//		openReaderNow();
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
//		FragmentTransaction transaction = getFragmentManager().beginTransaction();
//		transaction.replace(R.id.func_container, CridetConfirmFragment.create(
//				feemoney, allmoney, mBkntno));
//		transaction.commit();
		Intent intent = new Intent();
		intent.putExtra(FragmentFactory.INDEX_KEY, ConfrimFactory.CridetConfirmFragment_index);
		Bundle bundle = new Bundle();
		bundle.putString("costString", feemoney);
		bundle.putString("allString", allmoney);
		bundle.putString("no", mBkntno);
		intent.putExtra("bundle", bundle);
		intent.setClass(getActivity(), ConfirmActivity.class);
		getActivity().startActivityForResult(intent, 1);
//		UnionpayUtil.startUnionPlungin(mBkntno, getActivity());
	}
	
	class BuyTask extends AsyncTask<String, Integer, Boolean>{
		ProtocolRsp mRsp;
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas("ApiPayinfo", 
						"creditCardMoneyRq", CridetCardFragment.mJournalData);
				CridetNoParser authorRegParser = new CridetNoParser();
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
							ConfirmActivity.mCommonData.clear();
//							HashMap<String, String> item = new HashMap<String, String>();
							ConfirmActivity.mBankNo = mBkntno;
//							item.put("交易请求号", mBkntno);
//							ConfirmActivity.mCommonData.add(item);
							
							HashMap<String, String> item1 = new HashMap<String, String>();
							item1.put("收款卡号", CridetCardFragment.mJournalData.getValue(JournalData.shoucardno));
							ConfirmActivity.mCommonData.add(item1);
							
							HashMap<String, String> item21 = new HashMap<String, String>();
							item21.put("收款人姓名", CridetCardFragment.mJournalData.getValue(JournalData.shoucardman));
							ConfirmActivity.mCommonData.add(item21);
							
							HashMap<String, String> item22= new HashMap<String, String>();
							item22.put("收款银行", CridetCardFragment.mJournalData.getValue(JournalData.shoucardbank));
							ConfirmActivity.mCommonData.add(item22);
							
							HashMap<String, String> item2 = new HashMap<String, String>();
							item2.put("付款卡号", CridetCardFragment.mJournalData.getValue(JournalData.fucardno));
							ConfirmActivity.mCommonData.add(item2);
							
							HashMap<String, String> item23 = new HashMap<String, String>();
							item23.put("付款人姓名", CridetCardFragment.mJournalData.getValue(JournalData.fucardman));
							ConfirmActivity.mCommonData.add(item23);
							
							HashMap<String, String> item24= new HashMap<String, String>();
							item24.put("付款银行", CridetCardFragment.mJournalData.getValue(JournalData.fucardbank));
							ConfirmActivity.mCommonData.add(item24);
							
							HashMap<String, String> item3 = new HashMap<String, String>();
							item3.put("还款金额", NumberFormatUtil.format2(CridetCardFragment.mJournalData.getValue(JournalData.paymoney))+"元");
							ConfirmActivity.mCommonData.add(item3);
							
							HashMap<String, String> item4 = new HashMap<String, String>();
							String temp = "0";
							if(null != feemoney&&!"".equals(feemoney)){
								temp = feemoney;
							}
							item4.put("手续费", NumberFormatUtil.format2(temp)+"元");
							ConfirmActivity.mCommonData.add(item4);
							
							HashMap<String, String> item5 = new HashMap<String, String>();
							String temp1 = CridetCardFragment.mJournalData.getValue(JournalData.paymoney);
							if(null != allmoney&&!"".equals(allmoney)){
								temp1 = allmoney;
							}else{
								temp1=(Float.parseFloat(temp1)+Float.parseFloat(temp))+"";
								allmoney = temp1;
							}
							item5.put("支付金额", NumberFormatUtil.format2(temp1)+"元");
							ConfirmActivity.mCommonData.add(item5);
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
				ProtocolUtil.parserResponse(response,data);
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
				
				List<ProtocolData> paymoney1 = data.find("/paymoney");
				if(paymoney1 != null){
					paymoney = paymoney1.get(0).mValue;
				}
				
				List<ProtocolData> feemoney1 = data.find("/feemoney");
				if(feemoney1 != null){
					feemoney = feemoney1.get(0).mValue;
				}
				
				List<ProtocolData> allmoney1 = data.find("/allmoney");
				if(allmoney1 != null){
					allmoney = allmoney1.get(0).mValue;
				}
			}
		}
	}
	
}
