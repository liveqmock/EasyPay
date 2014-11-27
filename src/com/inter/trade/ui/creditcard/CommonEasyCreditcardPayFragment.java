/*
. * @Title:  CommonEasyCreditcardPayFragment.java
 * @Copyright:  XXX Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * @Description:  TODO<请描述此文件是做什么的>
 * @author:  MeiYi
 * @data:  2014年7月24日 上午11:07:42
 * @version:  V1.0
 */
package com.inter.trade.ui.creditcard;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.inter.trade.AsyncLoadWork;
import com.inter.trade.AsyncLoadWork.AsyncLoadWorkListener;
import com.inter.trade.R;
import com.inter.trade.data.AirTicketCreateOrderData;
import com.inter.trade.data.BankData;
import com.inter.trade.data.CommonData;
import com.inter.trade.db.DBHelper;
import com.inter.trade.ui.ConfirmActivity;
import com.inter.trade.ui.FunctionActivity;
import com.inter.trade.ui.GameRechargeSuccessActivity;
import com.inter.trade.ui.PayApp;
import com.inter.trade.ui.creditcard.SmsCodeDialog.SmsCodeSubmitListener;
import com.inter.trade.ui.creditcard.data.CreditCardData;
import com.inter.trade.ui.creditcard.data.SmsCode;
import com.inter.trade.ui.creditcard.task.AddBankCardTask;
import com.inter.trade.ui.creditcard.task.BankTask;
import com.inter.trade.ui.creditcard.task.CaiSmsCodeTask;
import com.inter.trade.ui.creditcard.task.SmsCodeTask;
import com.inter.trade.ui.creditcard.util.CreditCard;
import com.inter.trade.ui.creditcard.util.CreditcardInfoUtil;
import com.inter.trade.ui.fragment.AboutFragment;
import com.inter.trade.ui.fragment.BaseFragment;
import com.inter.trade.ui.fragment.FragmentFactory;
import com.inter.trade.ui.fragment.airticket.util.ApiAirticketCreateOrderParser;
import com.inter.trade.ui.fragment.airticket.util.ApiAirticketGetOrderHistoryData;
import com.inter.trade.ui.fragment.airticket.util.ApiAirticketGetOrderHistoryParser;
import com.inter.trade.ui.fragment.buyhtbcard.util.YiBaoOrderRequestParser;
import com.inter.trade.ui.fragment.buyswipcard.util.BuySwipCardOrderData;
import com.inter.trade.ui.fragment.coupon.BuySuccessFragment;
import com.inter.trade.ui.fragment.coupon.task.CouponCreditPayTask;
import com.inter.trade.ui.fragment.coupon.util.CouponData;
import com.inter.trade.ui.fragment.cridet.CridetCardFragment;
import com.inter.trade.ui.fragment.cridet.CridetSuccessFragment;
import com.inter.trade.ui.fragment.cridet.data.JournalData;
import com.inter.trade.ui.fragment.cridet.task.CreditCardPayTask;
import com.inter.trade.ui.fragment.gamerecharge.dialog.FavoriteCharacterDialogFragment;
import com.inter.trade.ui.fragment.gamerecharge.dialog.IFavoriteCharacterDialogListener;
import com.inter.trade.ui.fragment.hotel.HotelPaySuccessFragment;
import com.inter.trade.ui.fragment.hotel.task.HotelCreateOrderTask;
import com.inter.trade.ui.fragment.hotel.task.HotelPayOrderTask;
import com.inter.trade.ui.fragment.qmoney.QMoneyPaySuccessFragment;
import com.inter.trade.ui.fragment.qmoney.util.QMoneyCreditCardTask;
import com.inter.trade.ui.fragment.qmoney.util.QMoneyData;
import com.inter.trade.ui.fragment.salarypay.SalaryPaySuccessFragment;
import com.inter.trade.ui.fragment.salarypay.bean.CreditPayInfo;
import com.inter.trade.ui.fragment.salarypay.task.SalaryCreditPayTask;
import com.inter.trade.ui.fragment.telephone.TelephonePaySuccessFragment;
import com.inter.trade.ui.fragment.telephone.util.MobilePayTask;
import com.inter.trade.ui.fragment.telephone.util.MoblieRechangeData;
import com.inter.trade.ui.fragment.transfer.TransferSuccessFragment;
import com.inter.trade.ui.fragment.transfer.util.TransferCreditCardTask;
import com.inter.trade.ui.fragment.transfer.util.TransferData;
import com.inter.trade.ui.fragment.waterelectricgas.data.ResponseStateListener;
import com.inter.trade.ui.manager.core.IMainHandlerManager;
import com.inter.trade.ui.manager.core.UIConstantDefault;
import com.inter.trade.util.NumberFormatUtil;
import com.inter.trade.util.PromptUtil;
import com.inter.trade.util.TaskUtil;
import com.inter.trade.util.UserInfoCheck;

/**
 * 信用卡支付页面
 * 
 * @author ChenGuangChi
 * @data: 2014年7月24日 上午11:07:42
 * @version: V1.0
 */
public class CommonEasyCreditcardPayFragment extends BaseFragment implements
		OnClickListener, IFavoriteCharacterDialogListener,
		ResponseStateListener, SmsCodeSubmitListener,
		OnCheckedChangeListener,OnFocusChangeListener{
	private EditText etCvv, etName, etID, etPhone,etMonth,etYear;
	private TextView tvPrice, tvCard,tvProtocol,tvBankList;
	private Button btnChoose, btnSubmit, btnBank;//, btnMonth, btnYear;
	private CheckBox cbSave,cbDefault;
	private LinearLayout cbDefaultLayout;
	private ScrollView scrollView;
	
	private ImageView ivPositive,ivNegative;//信用卡的正面和反面

	private static Intent intent;
	private Context context;

	/**
	 * 支付的业务代号
	 */
	private int payKey;

	/**
	 * 银行卡号
	 */
	private String bankno;

	/**
	 * 支付金额
	 */
	private String paymoney;
	/**
	 * 是否选中月份
	 */
	private boolean isMonth = false;
	/**
	 * 是否选中年份
	 */
	private Boolean isYear = false;
	/**
	 * 是否选中证件类型
	 */
	private Boolean isId = true;
	/**
	 * 是否选中银行
	 */
	private boolean isBank = false;

	/**
	 * 支付成功页面显示的数据
	 */
	public static ArrayList<HashMap<String, String>> mCommonData = new ArrayList<HashMap<String, String>>();
	
	/***
	 * 游戏充值支付成功需要的数据
	 */
	private Bundle  bundle;
	/**
	 * 数据库帮助
	 */
	private DBHelper helper;
	
	/**
	 * 银行列表数据
	 */
	private ArrayList<BankData> bankList;
	
	/**
	 * 选中的银行的position
	 * @param in
	 * @return
	 */
	private int bankIndex;
	
	private AsyncTask payTask;
	
	
	
	/**——————————————————————发工资有关数据start——————————————————————————————**/
	/**
	 * 发工资人数
	 */
	private String personNumber;
	/**
	 * 发工资的总额
	 */
	private String wageallmoney;
	/**
	 * 已支付的总额
	 */
	private String paidmoney; 
	
	/**
	 * 工资月份
	 */
	private String wagemonth; 
	
	/**——————————————————————发工资有关数据end——————————————————————————————**/
	
	

	public static CommonEasyCreditcardPayFragment newInstance(Intent in) {
		intent = in;
		return new CommonEasyCreditcardPayFragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActivity().setTheme(R.style.DialogStyleLight);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.easypay_credit_card_pay_layout,
				null);
		initView(view);
		setBackVisible();
		setTitle("信用卡支付");
		initData();
		
		if(payKey==CreditCard.AIRTICKET){//机票，则获取携程支持的银行
			new BankTask(getActivity(), new ResponseStateListener() {
				
				@SuppressWarnings("unchecked")
				@Override
				public void onSuccess(Object obj, Class cla) {
					bankList=(ArrayList<BankData>) obj;
				}
			},"ctripctt").execute("");
			
		} else {
			new BankTask(getActivity(), new ResponseStateListener() {
				
				@SuppressWarnings("unchecked")
				@Override
				public void onSuccess(Object obj, Class cla) {
					bankList=(ArrayList<BankData>) obj;
				}
			},true).execute("");
		}
		
		
		
		return view;
	}


	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		context = activity;
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if(helper!=null){
			helper.closeDB();
		}
	}

	private void initData() {
		payKey = intent.getIntExtra(CreditCard.PAY_KEY, 0);

		bankno = intent.getStringExtra(CreditCard.CARDNO);
		tvCard.setText(bankno);

		paymoney = intent.getStringExtra(CreditCard.PAYMONEY);
		tvPrice.setText("￥" + paymoney);
		
//		helper = DBHelper.getInstance(getActivity());
//		CreditCardRecord queryCredit = helper.queryCredit(LoginUtil.mLoginStatus.login_name, bankno);
//		if(queryCredit!=null){
//			etName.setText(queryCredit.getName());
//			etPhone.setText(queryCredit.getPhone());
//			etID.setText(queryCredit.getIdcard());
//		}
		
		if(payKey==CreditCard.TRANSFER){//转账,填充开户人和开户手机,隐藏保存为默认
			String man = intent.getStringExtra(TransferData.fucardman);
			String p = intent.getStringExtra(TransferData.fucardmobile);
			String fucardbank = intent.getStringExtra(TransferData.fucardbank);
			etName.setText(man);
			etPhone.setText(p);
			if(fucardbank != null && !fucardbank.equals("")) {
				btnBank.setText(fucardbank);
				btnBank.setEnabled(false);
				isBank = true;
			}
			
			cbDefaultLayout.setVisibility(View.GONE);//隐藏默认卡
			
		} else if(payKey==CreditCard.REPAY_CREDITCARD) {//自动填充部分信息(开户人和开户手机,开户银行)
			String cc_fucardman = intent.getStringExtra(JournalData.fucardman);
			String cc_fucardmobile = intent.getStringExtra(JournalData.fucardmobile);
			String cc_fucardbank = intent.getStringExtra(JournalData.fucardbank);
			
			etName.setText(cc_fucardman);
			etPhone.setText(cc_fucardmobile);
			btnBank.setText(cc_fucardbank);
			isBank = true;
			
			cbDefaultLayout.setVisibility(View.GONE);//隐藏默认卡
		}else if(payKey==CreditCard.COUPON){
			String cman = intent.getStringExtra(CouponData.CREDITCARDMAN);
			String cphone = intent.getStringExtra(CouponData.CREDITCARDPHONE);
			String fucardbank = intent.getStringExtra(JournalData.fucardbank);
			String fucardbankid = intent.getStringExtra(JournalData.fucardbankid);
			
			etName.setText(cman);
			etPhone.setText(cphone);
			
			if(fucardbank != null && !fucardbank.equals("")) {
				btnBank.setText(fucardbank);
				btnBank.setHint(fucardbankid);
				btnBank.setEnabled(false);
				isBank = true;
			}
			
			cbDefaultLayout.setVisibility(View.GONE);//隐藏默认卡
		}else if(payKey==CreditCard.SALARY){
			cbDefaultLayout.setVisibility(View.GONE);//隐藏默认卡
		}
	}

	private void initView(View view) {
		etCvv = (EditText) view.findViewById(R.id.et_cvv);
		etName = (EditText) view.findViewById(R.id.et_name);
		etID = (EditText) view.findViewById(R.id.et_id);
		etPhone = (EditText) view.findViewById(R.id.et_phone);
		tvPrice = (TextView) view.findViewById(R.id.tv_price);
		tvCard = (TextView) view.findViewById(R.id.tv_card);
		btnChoose = (Button) view.findViewById(R.id.btn_idtype);
		btnSubmit = (Button) view.findViewById(R.id.submit_btn);
		btnBank = (Button) view.findViewById(R.id.btn_bank);
//		btnMonth = (Button) view.findViewById(R.id.btn_month);
//		btnYear = (Button) view.findViewById(R.id.btn_year);
		tvProtocol=(TextView) view.findViewById(R.id.tv_protocol);
		cbSave=(CheckBox) view.findViewById(R.id.cb_save);cbSave.setChecked(true);//保存银行卡信息，默认勾上
		cbDefault=(CheckBox) view.findViewById(R.id.cb_default);
		tvBankList=(TextView) view.findViewById(R.id.tv_banklist);
		cbDefaultLayout=(LinearLayout) view.findViewById(R.id.cb_default_layout);
		etMonth=(EditText) view.findViewById(R.id.et_month);
		etYear=(EditText) view.findViewById(R.id.et_year);
		ivPositive=(ImageView) view.findViewById(R.id.iv_bankcard_positive);
		ivNegative=(ImageView) view.findViewById(R.id.iv_bankcard_negative);
		scrollView=(ScrollView) view.findViewById(R.id.sv_main);

		btnChoose.setOnClickListener(this);
		btnSubmit.setOnClickListener(this);
		btnBank.setOnClickListener(this);
//		btnMonth.setOnClickListener(this);
//		btnYear.setOnClickListener(this);
		tvProtocol.setOnClickListener(this);
		cbSave.setOnCheckedChangeListener(this);
		cbDefault.setOnCheckedChangeListener(this);
		tvBankList.setOnClickListener(this);
		
		etMonth.setOnFocusChangeListener(this);
		etYear.setOnFocusChangeListener(this);
		etCvv.setOnFocusChangeListener(this);
	}

	long time = 0L;
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_idtype:// 选择证件类型
			isId = true;
			choosetype = 4;
			FavoriteCharacterDialogFragment.show(this, getActivity(), "选择证件类型",
					new String[] { "身份证" });// ,"护照","军官证","港澳居民往来内地通行证","台湾居民来往大陆通行证","其他"});
			break;
		case R.id.btn_bank:// 选择银行
			isBank = true;
			choosetype = 3;
			if(bankList!=null){
				FavoriteCharacterDialogFragment.show(this, getActivity(), "选择所属银行",
						CreditcardInfoUtil.getBankNameList(bankList));
			}
			// ,"护照","军官证","港澳居民往来内地通行证","台湾居民来往大陆通行证","其他"});
			break;
		case R.id.btn_month:// 月份有效期
			isMonth = true;
			choosetype = 1;
			FavoriteCharacterDialogFragment.show(this, getActivity(), "选择月份",
					getResources().getStringArray(R.array.months));
			break;
		case R.id.btn_year:// 年份有效期
			isYear = true;
			choosetype = 2;
			FavoriteCharacterDialogFragment.show(this, getActivity(), "选择年份",
					getResources().getStringArray(R.array.years));
			break;

		case R.id.submit_btn:// 提交按钮
			if (checkInput()) {
				long currentTime = System.currentTimeMillis();
				if (currentTime - time < 1000||TaskUtil.isTaskExcuting(payTask)) {//
					PromptUtil.showToast(getActivity(), getResources().getString(R.string.warn_repate_commit));
					return;
				}
				time = System.currentTimeMillis();
				gotoPay();
			}
			break;
			
		case R.id.tv_protocol://协议
			AboutFragment.mProtocolType="6";
			showProtocol();
			break;
		case R.id.tv_banklist://银行列表
			AboutFragment.mProtocolType="7";
			showProtocol();
			break;

		default:
			break;
		}
	}
	
	private void showProtocol(){
		Intent intent = new Intent();
		intent.setClass(getActivity(), FunctionActivity.class);
		intent.putExtra(FragmentFactory.INDEX_KEY, FragmentFactory.PROTOCOL_LIST_INDEX);
		getActivity().startActivity(intent);
	}

	/**
	 * 添加银行卡
	 * @throw
	 * @return void
	 */
	private void addBankCard() {
		String isdefault="1";
		if(cbDefault.isChecked()){
			isdefault="1";
		}else{
			isdefault="0";
		}
		CreditCardData data = collectionInfo();
		if(cbSave.isChecked()){
			new AddBankCardTask(getActivity(), null).execute(data.getBankId()
					,data.getBankName()
					,data.getBankno()
					,data.getUsername()
					,data.getPhone()
					,data.getMonth()
					,data.getYear()
					,data.getCvv2()
					,data.getId()
					,"creditcard"
					,isdefault);
		}
	}

	private boolean checkInput() {
		if (!isBank) {
			PromptUtil.showToast(getActivity(), "亲,请您选择银行!");
			return false;
		}
		else if (btnBank.getText().toString().startsWith("请")) {
			PromptUtil.showToast(getActivity(), "亲,请您选择银行!");
			return false;
		}
//		else if (!isMonth) {
//			PromptUtil.showToast(getActivity(), "亲,请您选择月份!");
//			return false;
//		} else if (!isYear) {
//			PromptUtil.showToast(getActivity(), "亲,请您选择年份!");
//			return false;
//		}
		else if (TextUtils.isEmpty(etMonth.getText())) {
			PromptUtil.showToast(getActivity(), "亲,请您填写月份!");
			return false;
		}
		else if (Integer.parseInt(etMonth.getText()+"")>12 || Integer.parseInt(etMonth.getText()+"")==0) {
			PromptUtil.showToast(getActivity(), "亲,请您填写有效的月份!");
			return false;
		}
		else if (TextUtils.isEmpty(etYear.getText())) {
			PromptUtil.showToast(getActivity(), "亲,请您填写年份!");
			return false;
		}
		else if (TextUtils.isEmpty(etCvv.getText())) {
			PromptUtil.showToast(getActivity(), "亲,请您输入安全码!");
			return false;
		} else if (TextUtils.isEmpty(etPhone.getText())) {
			PromptUtil.showToast(getActivity(), "亲,请您输入预留手机号!");
			return false;
		} else if (!UserInfoCheck.checkMobilePhone(etPhone.getText()+"")) {
			PromptUtil.showToast(getActivity(), "亲,请您输入正确的手机号!");
			return false;
		}else if (TextUtils.isEmpty(etName.getText())) {
			PromptUtil.showToast(getActivity(), "亲,请您输入持卡人姓名!");
			return false;
		} else if (!isId) {
			PromptUtil.showToast(getActivity(), "亲,请您选择证件类型!");
			return false;
		} else if (TextUtils.isEmpty(etID.getText())) {
			PromptUtil.showToast(getActivity(), "亲,请您输入身份证号!");
			return false;
		}
		// TODO 验证各种信息
		return true;
	}

	private CreditCardData collectionInfo() {
		CreditCardData data = new CreditCardData();
		data.setPaymoney(paymoney);
		data.setBankno(bankno);
		data.setCvv2(etCvv.getText() + "");
		data.setPhone(etPhone.getText() + "");
		data.setIdtype(CreditcardInfoUtil.transferStringToId(btnChoose
				.getText() + ""));
		data.setId(etID.getText() + "");
		data.setBank(bankList.get(bankIndex).bankno);//
		data.setBankName(btnBank.getText()+"");
		data.setMonth(CreditcardInfoUtil.transferStringToMonth(etMonth
				.getText() + ""));
		data.setYear(CreditcardInfoUtil.transferStringToYear(etYear.getText()
				+ ""));
		data.setUsername(etName.getText() + "");
		data.setBankId(bankList.get(bankIndex).bankid);
		
		if(payKey==CreditCard.COUPON){
			if(btnBank.getHint() != null) {
				data.setBankId(btnBank.getHint().toString());
			}
		}
		
		data.setCtripbankctt(bankList.get(bankIndex).ctripbankctt != null ? bankList.get(bankIndex).ctripbankctt : "");//携程ctt
		
		return data;
	}

	/**
	 * 前往支付
	 * 
	 * @throw
	 * @return void
	 */
	private void gotoPay() {
			CreditCardData data = collectionInfo();
//			if(helper!=null){
//				helper.insertCreditCard(new CreditCardRecord(null, LoginUtil.mLoginStatus.login_name, data.getBankno(), data.getUsername(), data.getId(), data.getPhone()));
//			}
			switch (payKey) {
			case CreditCard.MOBILE:// 话费充值
				String rechargemobile = intent
						.getStringExtra(MoblieRechangeData.MRD_RECHAMOBILE);// 充值手机号码
				String address = intent
						.getStringExtra(MoblieRechangeData.MRD_RECHAMOBILEPROV);
				String money = intent
						.getStringExtra(MoblieRechangeData.MRD_RECHAMONEY);
				
				mCommonData.clear();
				
				
				HashMap<String, String> item39 = new HashMap<String, String>();
				item39.put("付款账号:", bankno);
				mCommonData.add(item39);
				
				HashMap<String, String> item37 = new HashMap<String, String>();
				item37.put("付款银行:", data.getBankName());
				mCommonData.add(item37);
				
				HashMap<String, String> item38 = new HashMap<String, String>();
				item38.put("付款人:", data.getUsername());
				mCommonData.add(item38);
				
				HashMap<String, String> item1 = new HashMap<String, String>();
				item1.put("充值号码:", rechargemobile);
				mCommonData.add(item1);

//				HashMap<String, String> item2 = new HashMap<String, String>();
//				item2.put("号码归属:", address);
//				mCommonData.add(item2);

				HashMap<String, String> item3 = new HashMap<String, String>();
				item3.put("充值面额:", money+"元");
				mCommonData.add(item3);
				
				HashMap<String, String> item40 = new HashMap<String, String>();
				item40.put("支付金额:", paymoney+"元");
				mCommonData.add(item40);

				//collectCommonData(item1);

				payTask =new MobilePayTask(context, this).execute(money, paymoney,
						rechargemobile, bankno, data.getBank(), data.getId(),
						data.getPhone(), data.getUsername(), data.getYear(),
						data.getMonth(), data.getCvv2(), address,PayApp.mKeyCode==null?"":PayApp.mKeyCode,data.getBankName());
				break;
			case CreditCard.BUYSWIPCRAD:// 购买刷卡器
				String productname = intent
						.getStringExtra(BuySwipCardOrderData.PRODURENAME);
				String orderprice = intent
						.getStringExtra(BuySwipCardOrderData.ORDERPRICE);
				String ordernum = intent
						.getStringExtra(BuySwipCardOrderData.ORDERNUM);
				String yunmoney = intent
						.getStringExtra(BuySwipCardOrderData.YUNMONEY);
				String ordershman = intent
						.getStringExtra(BuySwipCardOrderData.ORDERSHMAN);
				String ordershphone = intent
						.getStringExtra(BuySwipCardOrderData.ORDERSHPHONE);
				String orderaddress = intent
						.getStringExtra(BuySwipCardOrderData.OREDERSHADDRESS);
				
				mCommonData.clear();
				HashMap<String, String> item6 = new HashMap<String, String>();
				item6.put("产品名称:", productname);
				mCommonData.add(item6);
				HashMap<String, String> item8 = new HashMap<String, String>();
				item8.put("单个价格:", orderprice);
				mCommonData.add(item8);
				HashMap<String, String> item9 = new HashMap<String, String>();
				item9.put("购买数量:", ordernum);
				mCommonData.add(item9);
				HashMap<String, String> item10 = new HashMap<String, String>();
				item10.put("运费金额:", yunmoney);
				mCommonData.add(item10);
				HashMap<String, String> item11= new HashMap<String, String>();
				item11.put("收货人:", ordershman);
				mCommonData.add(item11);
				HashMap<String, String> item12 = new HashMap<String, String>();
				item12.put("收货人电话:", ordershphone);
				mCommonData.add(item12);
				HashMap<String, String> item7 = new HashMap<String, String>();
				item7.put("收货地址:", orderaddress);
				mCommonData.add(item7);
				collectionInfo();
				
				break;

			case CreditCard.QQ:// Q币充值
				String qq = intent.getStringExtra(QMoneyData.MRD_RECHAMOBILE);
				String qqrechamoney = intent.getStringExtra(QMoneyData.MRD_RECHAMONEY);
				
				String qqpaycardid = intent.getStringExtra(QMoneyData.MRD_PAYCARDID);
				String qqpaytypeid = intent.getStringExtra(QMoneyData.MRD_RECHAPAYTYPEID);
				String qqreserved = intent.getStringExtra(QMoneyData.merReserved);
				
				mCommonData.clear();
				
				HashMap<String, String> item5 = new HashMap<String, String>();
				item5.put("付款卡号:", bankno);
				mCommonData.add(item5);
				
				HashMap<String, String> item35 = new HashMap<String, String>();
				item35.put("付款银行:", data.getBankName());
				mCommonData.add(item35);
				
				HashMap<String, String> item36 = new HashMap<String, String>();
				item36.put("付款人:", data.getUsername());
				mCommonData.add(item36);
				
				HashMap<String, String> item13 = new HashMap<String, String>();
				item13.put("充值QQ:", qq);
				mCommonData.add(item13);
				
				HashMap<String, String> item14 = new HashMap<String, String>();
				item14.put("充值金额:", qqrechamoney+"元");
				mCommonData.add(item14);
				
				HashMap<String, String> item4 = new HashMap<String, String>();
				item4.put("支付金额:", paymoney+"元");
				mCommonData.add(item4);
				
				
//				collectCommonData(item14);
				
//				HashMap<String, String> item15 = new HashMap<String, String>();
//				if(smsCai != null){
//					item15.put("交易请求号:", smsCai.getBkordernumber()+"");
//					QMoneyPayActivity.mCommonData.add(item15);
//				}
				
				CommonData qqData = new CommonData();
				qqData.putValue("payMoney", paymoney+"");
				qqData.putValue("rechargeMoney", qqrechamoney+"");
				qqData.putValue("RechargeQQ", qq+"");
				qqData.putValue("bkCardno", bankno);
				qqData.putValue("bkcardman", data.getUsername());
				qqData.putValue("bkcardexpireMonth", data.getMonth());
				qqData.putValue("bkcardmanidcard", data.getId());
				qqData.putValue("bankid", data.getBankId()+"");
				qqData.putValue("bankno", data.getBank()+"");
				qqData.putValue("bankname", data.getBankName()+"");
				qqData.putValue("bkcardexpireYear", data.getYear());
				qqData.putValue("bkcardPhone", data.getPhone());
				qqData.putValue("bkcardcvv", data.getCvv2());
				qqData.putValue("paytype", "qqrecharge");
				qqData.putValue("paycardid", qqpaycardid+"");
				payTask =new QMoneyCreditCardTask(getActivity(), this, qqData).execute();
				break;
				
			case CreditCard.HOTEL:// 酒店预订
				String hotel_paycardid = intent.getStringExtra("paycardid");
				String hotel_hotelCode = intent.getStringExtra("hotelCode");
				String hotel_roomCode = intent.getStringExtra("roomCode");
				String hotel_priceCode = intent.getStringExtra("priceCode");
				String hotel_startDate = intent.getStringExtra("startDate");
				String hotel_endDate = intent.getStringExtra("endDate");
				String hotel_roomCount = intent.getStringExtra("roomCount");
				String hotel_payMoney = intent.getStringExtra("payMoney");
				String hotel_phone = intent.getStringExtra("phone");
				String hotel_name = intent.getStringExtra("name");//need modify
				
				String hotel_price = intent.getStringExtra("price");//need modify
				String hotel_daytotal = intent.getStringExtra("daytotal");//need modify
				
				mCommonData.clear();
				
				HashMap<String, String> hotel_bankno = new HashMap<String, String>();
				hotel_bankno.put("付款账号:", bankno);
				mCommonData.add(hotel_bankno);
				
				HashMap<String, String> hotel_BankName = new HashMap<String, String>();
				hotel_BankName.put("付款银行:", data.getBankName());
				mCommonData.add(hotel_BankName);
				
				HashMap<String, String> hotel_UserName = new HashMap<String, String>();
				hotel_UserName.put("付款人:", data.getUsername());
				mCommonData.add(hotel_UserName);
				
				HashMap<String, String> hotel_price_HM = new HashMap<String, String>();
				hotel_price_HM.put("房间单价:", "￥"+hotel_price);
				mCommonData.add(hotel_price_HM);

				HashMap<String, String> hotel_daytotal_HM = new HashMap<String, String>();
				hotel_daytotal_HM.put("入住天数:", hotel_daytotal);
				mCommonData.add(hotel_daytotal_HM);
				
				HashMap<String, String> hotel_roomCount_HM = new HashMap<String, String>();
				hotel_roomCount_HM.put("房间数量:", hotel_roomCount);
				mCommonData.add(hotel_roomCount_HM);
				
				HashMap<String, String> hotel_payMoney_HM = new HashMap<String, String>();
				hotel_payMoney_HM.put("支付总额:", "￥"+hotel_payMoney);
				mCommonData.add(hotel_payMoney_HM);
				
//				collectCommonData(item14);
				
//				HashMap<String, String> item15 = new HashMap<String, String>();
//				if(smsCai != null){
//					item15.put("交易请求号:", smsCai.getBkordernumber()+"");
//					QMoneyPayActivity.mCommonData.add(item15);
//				}
				
				CommonData hotelData = new CommonData();
				hotelData.putValue("hotelCode", hotel_hotelCode);
				hotelData.putValue("roomCode", hotel_roomCode);
				hotelData.putValue("priceCode", hotel_priceCode);
				hotelData.putValue("startDate", hotel_startDate);
				hotelData.putValue("endDate", hotel_endDate);
				hotelData.putValue("roomCount", hotel_roomCount);
				hotelData.putValue("payMoney", hotel_payMoney);
				hotelData.putValue("phone", hotel_phone);
				hotelData.putValue("name", hotel_name);//need modify
				
				hotelData.putValue("bkCardno", bankno);
				hotelData.putValue("bkcardman", data.getUsername());
				hotelData.putValue("bkcardexpireMonth", data.getMonth());
				hotelData.putValue("bkcardmanidcard", data.getId());
				hotelData.putValue("bankid", data.getBankId()+"");
				hotelData.putValue("bankno", data.getBank()+"");
				hotelData.putValue("bankname", data.getBankName()+"");
				hotelData.putValue("bkcardexpireYear", data.getYear());
				hotelData.putValue("bkcardPhone", data.getPhone());
				hotelData.putValue("bkcardcvv", data.getCvv2());
				hotelData.putValue("paytype", "hotel");//need modify
				hotelData.putValue("paycardid", hotel_paycardid+"");
				/**
				 * 生成订单
				 */
				payTask = new HotelCreateOrderTask(getActivity(), mHotelCreateOrderListener, hotelData).execute();
				break;
				
			case CreditCard.TRANSFER:// 转账汇款
				String shoucardno = intent.getStringExtra(TransferData.shoucardno);
//				付款卡号
				String fucardno = intent.getStringExtra(TransferData.fucardno);
//				转账金额
				String pmoney = intent.getStringExtra(TransferData.paymoney);
//				实际支付金额
				String totalmoney = intent.getStringExtra(TransferData.money);
				String payfee = intent.getStringExtra(TransferData.payfee);
				
				String shoucardbank = intent.getStringExtra(TransferData.shoucardbank);
				String shoucardman = intent.getStringExtra(TransferData.shoucardman);
				String shoucardmobile = intent.getStringExtra(TransferData.shoucardmobile);
				
				
				String paycardid = intent.getStringExtra(TransferData.paycardid);
				
				String transferType = intent.getStringExtra("type_transfer");
				
				String payType = intent.getStringExtra("payType");
				String arriveid=intent.getStringExtra(TransferData.arriveid);
				String fucardbank=intent.getStringExtra(TransferData.fucardbank);
				String fucardman=intent.getStringExtra(TransferData.fucardman);
				
				
				mCommonData.clear();
				HashMap<String, String> item21 = new HashMap<String, String>();
				item21.put("收款卡号", shoucardno);
				mCommonData.add(item21);
				
				HashMap<String, String> item120 = new HashMap<String, String>();
				item120.put("收款银行",shoucardbank
						);
				mCommonData.add(item120);
				
				HashMap<String, String> item121 = new HashMap<String, String>();
				item121.put("收款人",shoucardman
						);
				mCommonData.add(item121);
				
				HashMap<String, String> item22 = new HashMap<String, String>();
				item22.put("付款卡号", bankno);
				mCommonData.add(item22);
				
				HashMap<String, String> item122 = new HashMap<String, String>();
				item122.put("付款银行",fucardbank
						);
				mCommonData.add(item122);
				
				HashMap<String, String> item123 = new HashMap<String, String>();
				item123.put("付款人",fucardman
						);
				mCommonData.add(item123);
				
				HashMap<String, String> item23 = new HashMap<String, String>();
				item23.put("转账金额", NumberFormatUtil.format2(pmoney)+"元");
				mCommonData.add(item23);
				
				HashMap<String, String> item25 = new HashMap<String, String>();
				item25.put("手续费", NumberFormatUtil.format2(payfee)+"元");
				mCommonData.add(item25);
				
				HashMap<String, String> item24 = new HashMap<String, String>();
				item24.put("支付金额", NumberFormatUtil.format2(paymoney)+"元");
				mCommonData.add(item24);
				
				payTask =new TransferCreditCardTask(context, this).execute(
						bankno, data.getBank(), data.getId(),
						data.getPhone(), data.getUsername(), data.getYear(),
						data.getMonth(), data.getCvv2(), paycardid,
						pmoney, paymoney, shoucardno, shoucardbank, shoucardman, shoucardmobile,transferType
						,payType,arriveid,data.getBankName());
				break;
			
			case CreditCard.GAME://游戏充值
//				String gameId = intent.getStringExtra(Game.GAMEID);
//				String gameName = intent.getStringExtra(Game.GAMENAME);
//				String area = intent.getStringExtra(Game.GAMEAREA);
//				String server = intent.getStringExtra(Game.GAMESERVER);
//				String quantity = intent.getStringExtra(Game.GAMEQUANTITY);
//				String usercount = intent.getStringExtra(Game.GAMEUSERCOUNT);
//				String payCardId = intent.getStringExtra(Game.GAMEPAYCARDID);
//				String cost = intent.getStringExtra(Game.GAMECOST);
//				
//				bundle=new Bundle();
//				bundle.putString("gamename",gameName);
//				bundle.putString("bankno",bankno);
//				bundle.putString("money",paymoney);
//				
//				new GamePayTask(context, this).execute(gameId,
//													  gameName,
//													  area,
//													  server,
//													  quantity,
//													  paymoney,
//													  cost,
//													  usercount,
//													  payCardId,
//													  data.getBankno(),
//													  data.getBank(),
//													  data.getId(),
//													  data.getPhone(),
//													  data.getUsername(),
//													  data.getYear(),
//													  data.getMonth(),
//													  data.getCvv2());
//				
				break;
			case CreditCard.REPAY_CREDITCARD:// 还信用卡
				//还款信息
				String paytype = intent.getStringExtra(JournalData.paytype);
				String current = intent.getStringExtra(JournalData.current);
				String paymoney = intent.getStringExtra(JournalData.paymoney);
				String cc_paycardid = intent.getStringExtra(JournalData.paycardid);
				String merReserved = intent.getStringExtra(JournalData.merReserved);
				
				String cc_allmoney = intent.getStringExtra(JournalData.allmoney);//所有金额
				String cc_feemoney = intent.getStringExtra(JournalData.feemoney);//手续费
				
				//收款信息
				String cc_shoucardno = intent.getStringExtra(JournalData.shoucardno);
				String cc_shoucardman = intent.getStringExtra(JournalData.shoucardman);
				String cc_shoucardmobile = intent.getStringExtra(JournalData.shoucardmobile);
				String cc_shoucardbank = intent.getStringExtra(JournalData.shoucardbank);
				
				//付款信息
				String cc_fucardno = intent.getStringExtra(JournalData.fucardno);
				String cc_fucardman = intent.getStringExtra(JournalData.fucardman);
				String cc_fucardmobile = intent.getStringExtra(JournalData.fucardmobile);
				String cc_fucardbank = intent.getStringExtra(JournalData.fucardbank);
				String fucardbankid = intent.getStringExtra(JournalData.fucardbankid);
				
				
				mCommonData.clear();
				HashMap<String, String> item26 = new HashMap<String, String>();
				item26.put("收款卡号:", cc_shoucardno);
				mCommonData.add(item26);
				
				HashMap<String, String> item221 = new HashMap<String, String>();
				item221.put("收款人姓名",cc_shoucardman);
				mCommonData.add(item221);
				
				HashMap<String, String> item222= new HashMap<String, String>();
				item222.put("收款银行", cc_shoucardbank);
				mCommonData.add(item222);
				
				HashMap<String, String> item27 = new HashMap<String, String>();
				item27.put("付款卡号:", cc_fucardno);
				mCommonData.add(item27);
				
				HashMap<String, String> item223 = new HashMap<String, String>();
				item223.put("付款人姓名", cc_fucardman);
				mCommonData.add(item223);
				
				HashMap<String, String> item224= new HashMap<String, String>();
				item224.put("付款银行", cc_fucardbank);
				mCommonData.add(item224);
				
				HashMap<String, String> item28 = new HashMap<String, String>();
				item28.put("还款金额:", paymoney+"元");
				mCommonData.add(item28);
				
				HashMap<String, String> itemfeemoney = new HashMap<String, String>();
				itemfeemoney.put("手续费:", cc_feemoney+"元");
				mCommonData.add(itemfeemoney);
				
				HashMap<String, String> itemallmoney = new HashMap<String, String>();
				itemallmoney.put("支付金额:", cc_allmoney+"元");
				mCommonData.add(itemallmoney);
				

				
				payTask =new CreditCardPayTask(context, this).execute(
						data.getBankName(),
						bankno, 
						data.getUsername(),
						data.getMonth(), 
						data.getId(),
						
						//所属银行ID
						fucardbankid != null && !fucardbankid.equals("") ? fucardbankid : data.getBank(), 
						
						data.getYear(),
						data.getPhone(), 
						data.getCvv2(), 
						
						"creditcard",//支付类型信用卡
						paymoney,
						cc_allmoney,
						cc_feemoney,
						
						cc_shoucardno,
						cc_shoucardmobile,
						cc_shoucardman,
						cc_shoucardbank,
						current,
						cc_paycardid,
						merReserved);
				
//				new CreditCardPayTask(context, this).execute(
//						bankno, data.getBank(), data.getId(),
//						data.getPhone(), data.getUsername(), data.getYear(),
//						data.getMonth(), data.getCvv2(), paytype,
//						current, paymoney, cc_paycardid, merReserved, cc_shoucardno, cc_shoucardman,cc_shoucardmobile
//						,cc_shoucardbank,data.getBankName());
				break;
			case CreditCard.COUPON://抵用劵或者商户收款
				String cpayCardId = intent.getStringExtra(CouponData.PAYCARDID);
				String couponId = intent.getStringExtra(CouponData.COUPONID);
				
				mCommonData.clear();
				
				HashMap<String, String> item30 = new HashMap<String, String>();
				item30.put("收款金额:", this.paymoney+"元");
				mCommonData.add(item30);
				
				HashMap<String, String> item31 = new HashMap<String, String>();
				item31.put("付款卡号:", this.bankno);
				mCommonData.add(item31);
				
				HashMap<String, String> item32 = new HashMap<String, String>();
				item32.put("支付金额:", this.paymoney+"元");
				mCommonData.add(item32);
				
				payTask =new CouponCreditPayTask(getActivity(), this).execute(
						couponId,
						this.paymoney,
						cpayCardId,
						/**data.getBankName()*/btnBank.getText().toString()!= null ? btnBank.getText().toString():"",
						this.bankno,
						data.getUsername(),
						data.getMonth(),
						data.getId(),
						/**data.getBankId()*/btnBank.getHint().toString()!= null ? btnBank.getHint().toString():"",
						data.getYear(),
						data.getPhone(),
						data.getCvv2(),
						"coupon",
						data.getBank() != null ? data.getBank() :"",
						/**data.getBankName()*/btnBank.getText().toString()!= null ? btnBank.getText().toString():""
						);
				
				
				break;
				
			case CreditCard.AIRTICKET://飞机票
				 AirTicketCreateOrderData airTicketCreateOrderData = null;
				if(intent != null) {
					orderDatas = intent.getExtras();
					airTicketCreateOrderData = (AirTicketCreateOrderData)orderDatas.getSerializable("orderData");
				}
				if(airTicketCreateOrderData != null) {
					//创建机票订单解析
					ApiAirticketCreateOrderParser authorRegParser = new ApiAirticketCreateOrderParser();

					asyncLoadWork = new AsyncLoadWork<String>(getActivity(), authorRegParser, buildAirRequestData(airTicketCreateOrderData, data), orderAsyncWorkListener, false, true);
					asyncLoadWork.execute("ApiAirticket", "createOrder");
				}
				
				break;
			case CreditCard.HTB_CARD_BUY://购买汇通卡
				CommonData commonData = null;
				if(intent != null) {
					orderDatas = intent.getExtras();
					commonData = (CommonData)orderDatas.getSerializable("orderData");
				}
				
				if(commonData != null) {
					YiBaoOrderRequestParser authorRegParser = new YiBaoOrderRequestParser();
					
					new AsyncLoadWork<SmsCode>(getActivity(), authorRegParser, getDefaultBankCardData(commonData, data), creditCardPayListener, 
							false, true, true).execute("ApiExpresspayInfo", "ybagentorderPayrq");
				}
				break;
			case CreditCard.SALARY://发工资
				String pid = intent.getStringExtra("paycardid");
				String wagelistid = intent.getStringExtra("wagelistid");
				String wagemoney = intent.getStringExtra("wagemoney");
				
				wagemonth = intent.getStringExtra("wagemonth");
				personNumber = intent.getStringExtra("personnumber");
				wageallmoney = intent.getStringExtra("wageallmoney");
				paidmoney = intent.getStringExtra("paidmoney");
				
				
				
				CreditPayInfo info=new CreditPayInfo();
				info.bankid=data.getBankId();
				info.bkcardbank=data.getBankName();
				info.bkcardcvv=data.getCvv2();
				info.bkcardexpireMonth=data.getMonth();
				info.bkcardexpireYear=data.getYear();
				info.bkcardman=data.getUsername();
				info.bkcardmanidcard=data.getId();
				info.bkCardno=this.bankno;
				info.bkcardPhone=data.getPhone();
				info.paycardid=pid;
				info.wagelistid=wagelistid;
				info.wagemoney=wagemoney;
				info.wagemonth=wagemonth;
				info.wagepaymoney=this.paymoney;
				
				
				payTask=new SalaryCreditPayTask(getActivity(), this).execute(info);
				
				break;
			default:
				break;
			}


	}
	
	/**
	 * 构建统一的银行卡数据
	 * 
	 * @param defaultBankCardData
	 * @return
	 */
	private CommonData getDefaultBankCardData (CommonData data ,CreditCardData cardData) {
		data.putValue("bkcardbank", cardData.getBankName()!=null ? cardData.getBankName() :"");//付款银行名
		data.putValue("bkCardno", bankno!=null ?bankno:"");//银行卡号
		data.putValue("bkcardman", cardData.getUsername() !=null ? cardData.getUsername() :"");//执卡人
		data.putValue("bkcardexpireMonth", cardData.getMonth() !=null ? cardData.getMonth() :"");//月份
		data.putValue("bkcardmanidcard", cardData.getId() !=null ? cardData.getId():"");//执卡人身份证
		data.putValue("bankid", cardData.getBankId() !=null ? cardData.getBankId():"");//银行id
		data.putValue("bkcardexpireYear", cardData.getYear() !=null ? cardData.getYear():"");//年
		data.putValue("bkcardPhone", cardData.getPhone() !=null ? cardData.getPhone():"");//银行预留手机号码
		data.putValue("bkcardcvv", cardData.getCvv2() !=null ? cardData.getCvv2():"");//cvv
		return data;
	}
	
	/**
	 * 信用卡支付-异宝订单交易
	 */
	private AsyncLoadWorkListener creditCardPayListener = new AsyncLoadWorkListener() {

		@Override
		public void onSuccess(Object protocolDataList, Bundle bundle) {
			if (protocolDataList == null) return;
			
			ArrayList<SmsCode> tempList = (ArrayList<SmsCode>)protocolDataList;
			if(tempList.size() > 0) {
				smsCai = tempList.get(0);
			}
			
			if (smsCai != null) {
				orderId = smsCai.getOrderId();
				if (smsCai.isNeed()) {// 需要验证码
					dialog=new SmsCodeDialog();
					dialog.show(getActivity(), smsCodeSubmitListener);
				} else {// 不需要验证码,表示支付成功
					PromptUtil.showToast(getActivity(), smsCai.getMessage());
					gotoPaySunccess();
				}
			}
		}

		@Override
		public void onFailure(String error) {
			
		}
		
	};
	
	/**
	 * 信用卡 短信认证
	 */
	private SmsCodeSubmitListener smsCodeSubmitListener = new SmsCodeSubmitListener() {
		
		@Override
		public void onPositive(String code, SmsCodeDialog dialog) {
			String funName="";
			String func="";
			switch (payKey) {
			case CreditCard.HTB_CARD_BUY://购买汇通卡
				funName="ApiExpresspayInfo";
				func="ybagentorderSMSverify";
				break;
			default:
				break;
			}
			
			switch (payKey) {
			case CreditCard.HTB_CARD_BUY://购买汇通卡
				new CaiSmsCodeTask(getActivity(), new ResponseStateListener() {
					
					@Override
					public void onSuccess(Object obj, Class cla) {
						gotoPaySunccess();
					}
				}).execute(code,
						   smsCai.getBkordernumber(),
						   smsCai.getBkntno(),
						   smsCai.getVerifytoken(),
						   funName, func);
				break;
				
				
				
			default:
				break;
			}
			
			
		}
	};
	
	/**
	 * 新的 支付成功页面跳转
	 */
	private void gotoPaySunccess() {
		addBankCard();
		switch (payKey) {
		case CreditCard.HTB_CARD_BUY://购买汇通卡
			IMainHandlerManager.handlerUI(UIConstantDefault.UI_CONSTANT_BUY_HTBCARD_ORDER_PAY_SUCCESS, 1, orderDatas);
			break;

		default:
			break;
		}
		
	}
	
	/**
	 * 构建飞机票订单的请求数据+付款银行卡信息
	 * @param airTicketCreateOrderData 订单信息
	 * @param data 信用卡数据
	 * @return
	 */
	private AirTicketCreateOrderData buildAirRequestData(AirTicketCreateOrderData airTicketCreateOrderData, CreditCardData data) {
		//支付信息
		airTicketCreateOrderData.payinfoMap.put("cardNumber", this.bankno !=null ? this.bankno:"");//卡号
		airTicketCreateOrderData.payinfoMap.put("bkcardexpireYear", (data.getYear() !=null ? data.getYear():""));//有效年份
		airTicketCreateOrderData.payinfoMap.put("bkcardexpireMonth", data.getMonth() !=null ? data.getMonth() :"");//有效月份
		airTicketCreateOrderData.payinfoMap.put("validity", data.getYear() + CreditcardInfoUtil.transferTwoMonth(data.getMonth()));//有效年份月份
		airTicketCreateOrderData.payinfoMap.put("cardCVV2No", data.getCvv2() !=null ? data.getCvv2():"");//三位有效码
		airTicketCreateOrderData.payinfoMap.put("cardHolder", data.getUsername() !=null ? data.getUsername() :"");//持卡人
		airTicketCreateOrderData.payinfoMap.put("cardHolderIdCardType", "1");//持卡人证件类型
		airTicketCreateOrderData.payinfoMap.put("cardHolderIdCardNumber", data.getId() !=null ? data.getId():"");//持卡人证件号
		airTicketCreateOrderData.payinfoMap.put("phoneNumber", data.getPhone() !=null ? data.getPhone():"");//持卡人手机号
		
		airTicketCreateOrderData.payinfoMap.put("bankCct", data.getCtripbankctt() !=null ? data.getCtripbankctt():"");//携程CTT
		return airTicketCreateOrderData;
	}
	
	
	
	/**收集相同的信息(实际付钱和)**/
	private void collectCommonData(HashMap<String, String> item1) {
		HashMap<String, String> item4 = new HashMap<String, String>();
		item4.put("支付金额:", paymoney);
		mCommonData.add(item4);
		
		HashMap<String, String> item5 = new HashMap<String, String>();
		item5.put("付款卡号:", bankno);
		mCommonData.add(item5);
	}

	
	private String orderId;
	
	private SmsCode smsCai; 

	@Override
	public void onPositive(String code,final SmsCodeDialog dialog) {
		// PromptUtil.showToast(this, code);
		String funName="";
		String func="";
		switch (payKey) {
		case CreditCard.MOBILE:// 话费充值
			funName="ApiMobileRechargeInfoV2";
			func="PayWithVerifyCode";
			break;
		case CreditCard.TRANSFER:// 转账
			funName="ApiTransferMoney";
			func="PayWithVerifyCode";
			break;
		case CreditCard.QQ:// QQ充值
			funName="ApiyibaoPayInfo";
			func="qqrechargeSMSverify";
			break;
		case CreditCard.GAME://游戏充值
			funName="ApiGameRecharge";
			func="PayWithVerifyCode";
			break;
		case CreditCard.REPAY_CREDITCARD://还信用卡，待替换接口名
			funName="ApiyibaoPayInfo";
			func="creditCardMoneySMSverify";
			break;
		case CreditCard.COUPON://抵用劵
			funName="ApiyibaoPayInfo";
			func="couponPaySMSverify";
			break;
//		case CreditCard.HTB_CARD_BUY://购买汇通卡
//			funName="ApiExpresspayInfo";
//			func="ybagentorderSMSverify";
//			break;
		case CreditCard.SALARY://发工资
			funName="ApiWageInfo";
			func="ybwagePayrq";
			break;
		default:
			break;
		}
		
		switch (payKey) {
		case CreditCard.MOBILE:
		case CreditCard.TRANSFER:	
			
			new SmsCodeTask(getActivity(), new ResponseStateListener() {

				@Override
				public void onSuccess(Object obj, Class cla) {
					//PromptUtil.showToast(getActivity(), (String) obj);
//					dialog.dismiss();
					gotoPaySuccess();
				}
			}).execute(orderId, code, funName, func);
			break;
		case CreditCard.COUPON://抵用劵
		case CreditCard.QQ://q币充值
		case CreditCard.REPAY_CREDITCARD://信用卡还款
		case CreditCard.SALARY://发工资
//		case CreditCard.HTB_CARD_BUY://购买汇通卡
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
			break;
			
			
			
		default:
			break;
		}
	}
	
	private SmsCodeDialog dialog;
	
	@Override
	public void onSuccess(Object obj, Class cla) {
		switch (payKey) {
		case CreditCard.MOBILE:// 话费充值
		case CreditCard.TRANSFER:// 转账
			//gotoPaySuccess();
			SmsCode sms = (SmsCode) obj;
			if (sms != null) {
				orderId = sms.getOrderId();
				if (sms.isNeed()) {// 需要验证码
					dialog=new SmsCodeDialog();
					dialog.show(getActivity(), this);
				} else {// 不需要验证码,表示支付成功
					PromptUtil.showToast(getActivity(), sms.getMessage());
					gotoPaySuccess();
				}
			}

			break;
		case CreditCard.COUPON://抵用劵
		case CreditCard.QQ://
		case CreditCard.REPAY_CREDITCARD:
		case CreditCard.SALARY://发工资
			
			smsCai=(SmsCode) obj;
			if(smsCai!=null){
				orderId = smsCai.getBkordernumber();
				if(smsCai.isNeed()){//需要验证码
					dialog=new SmsCodeDialog();
					dialog.show(getActivity(), this);
				}else{//不需要验证码,支付成功
					PromptUtil.showToast(getActivity(), smsCai.getMessage());
					gotoPaySuccess();
				}
			}
			
			break;
		default:
			break;
		}
	}

	// 启动付款成功页面
	private void gotoPaySuccess() {
		addBankCard();
		FragmentTransaction ft = getFragmentManager().beginTransaction();
				switch (payKey) {
		case CreditCard.MOBILE:// 话费充值
			ft.replace(R.id.func_container, TelephonePaySuccessFragment.createFragment(mCommonData,intent.getBooleanExtra(MoblieRechangeData.ISLAUCHGUIDE, false)));
			break;
		case CreditCard.BUYSWIPCRAD:// 购买刷卡器
			ft.replace(R.id.func_container, BuySuccessFragment.createFragment(mCommonData));
			break;
		case CreditCard.QQ:// Q币充值
			ft.replace(R.id.func_container, QMoneyPaySuccessFragment.createFragment(mCommonData));
			break;
		case CreditCard.HOTEL:// 酒店预订
			ft.replace(R.id.func_container, HotelPaySuccessFragment.createFragment(mCommonData));
			ft.addToBackStack(null);//test
			break;
		case CreditCard.TRANSFER:// 转账
			ft.replace(R.id.func_container, TransferSuccessFragment.createFragment(mCommonData));
			ft.addToBackStack(null);
			break;
		case CreditCard.GAME://游戏
			Intent in = new Intent(getActivity(),
					GameRechargeSuccessActivity.class);
			in.putExtra("data", bundle);
			startActivity(in);
			break;
		case CreditCard.REPAY_CREDITCARD:// 还信用卡
			ft.replace(R.id.func_container, CridetSuccessFragment.createFragment(mCommonData));
			break;
		case CreditCard.COUPON://抵用劵
//			HashMap<String, String> item29 = new HashMap<String, String>();
//			item29.put("交易请求号:", this.orderId);
//			mCommonData.add(item29);
			ft.replace(R.id.func_container, BuySuccessFragment.createFragment(mCommonData));
			break;
		case CreditCard.SALARY://发工资
			Bundle data=new Bundle();
			data.putString("personnumber",personNumber);
			data.putString("wageallmoney",wageallmoney);
			data.putString("paidmoney",paidmoney);
			data.putString("wagemonth",wagemonth);
//			IMainHandlerManager.handlerUI(UIConstantDefault.UI_CONSTANT_SALARYPAY_SUCCESS, 1, data);
			ft.replace(R.id.func_container, SalaryPaySuccessFragment.getInstance(data));
			break;
		default:
			break;
		}
		ft.commit();
	}
	private int choosetype = 1;// 1是月份 2是年份 3是银行 4 是证件类型
	@Override
	public void onListItemSelected(String value, int number) {
		// PromptUtil.showToast(this,
		// CreditcardInfoUtil.transferStringToId(value));
		switch (choosetype) {
//		case 1:
//			btnMonth.setText(value);
//			break;
//		case 2:
//			btnYear.setText(value);
//			break;
		case 3:
			btnBank.setText(value);
			bankIndex=number;
			break;
		case 4:
			btnChoose.setText(value);
			break;

		default:
			break;
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()) {
		case R.id.cb_save://选择保存
			if(!isChecked){
				cbDefault.setChecked(false);
			}
			break;
		case R.id.cb_default://选择默认
			if(isChecked){
				cbSave.setChecked(true);
			}
			break;

		default:
			break;
		}
	}
	
	ArrayList<String> protocolList = null;
	/**
	 * 订单异步提交监听
	 */
	private AsyncLoadWorkListener orderAsyncWorkListener = new AsyncLoadWorkListener() {

		@Override
		public void onSuccess(Object protocolDataList, Bundle bundle) {
			protocolList = (ArrayList<String>)protocolDataList;
			orderNum = protocolList.get(0);
			
			if(TextUtils.isEmpty(orderNum)){
				PromptUtil.showToast(getActivity(), "订单出错!");
				return;
			}
			gotoSmsPayDialog(orderNum);
//			if(orderDatas != null) {
//				orderDatas.putString("orderId", protocolList.get(0));
//			}
////			addFragmentToStack(UIConstantDefault.UI_CONSTANT_AIR_TICKET_ORDER_PAY_SUCCESS, 1, orderDatas);
//			IMainHandlerManager.handlerUI(UIConstantDefault.UI_CONSTANT_AIR_TICKET_ORDER_PAY_SUCCESS, 1, orderDatas);
		}

		@Override
		public void onFailure(String error) {
			
		}
		
	};
	
	/**
	 * 短信支付验证Dialog
	 * 
	 * @param orderID 订单编号
	 */
	private void gotoSmsPayDialog (String orderID) {
		SmsCodeDialog dialog = new SmsCodeDialog();
		dialog.show(getActivity(), new SmsCodeSubmitListener() {

			@Override
			public void onPositive(String code, SmsCodeDialog dialog) {
				
				gotoSmsPay(code);
			}
		});
	}
	
	/**
	 * 订单短信支付
	 */
	private void gotoSmsPay(String smsCode) {
		ApiAirticketGetOrderHistoryParser netParser = new ApiAirticketGetOrderHistoryParser();
		CommonData requsetData = new CommonData();
		requsetData.putValue("orderId",orderNum);
		requsetData.putValue("verifyCode",smsCode);
		asyncSmsPayWork = new AsyncLoadWork<ApiAirticketGetOrderHistoryData>(getActivity(), netParser, requsetData, new AsyncLoadWorkListener() {

			@Override
			public void onSuccess(Object protocolDataList, Bundle bundle) {
				orderDatas.putString("orderId", orderNum);
				String message = bundle.getString("message");
				showSuccessDialog(message);
//				IMainHandlerManager.handlerUI(UIConstantDefault.UI_CONSTANT_AIR_TICKET_ORDER_PAY_SUCCESS, 1, orderDatas);
			}

			@Override
			public void onFailure(String error) {
			}
			
		}, false, true, true);
		
		asyncSmsPayWork.execute("ApiAirticket", "payWithCreditCard");
	}
	
	private void showSuccessDialog(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("提示").setMessage(message);
		builder.setPositiveButton("确定",new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				IMainHandlerManager.handlerUI(UIConstantDefault.UI_CONSTANT_AIR_TICKET_ORDER_PAY_SUCCESS, 1, orderDatas);
			}
		});
		builder.show();
	}
	
	/**
	 * 订单编号
	 */
	private String orderNum = null;
	
	/**
	 * 机票、酒店，订单信息
	 */
	Bundle orderDatas = null;
	
	/**
	 * 订单提交异步
	 */
	private AsyncLoadWork<String> asyncLoadWork = null;
	
	/**
	 * 订单短信支付提交异步
	 */
	private AsyncLoadWork<ApiAirticketGetOrderHistoryData> asyncSmsPayWork = null;

	
	/**
	 * 酒店预订 生成订单 监听
	 */
	private ResponseStateListener mHotelCreateOrderListener=new ResponseStateListener() {
		
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
						new HotelPayOrderTask(getActivity(), mHotelPayOrderListener, hotelData).execute();
					}
				}
			}
		}
	};
	
	/**
	 * 酒店预订 信用卡支付订单 监听
	 */
	private ResponseStateListener mHotelPayOrderListener=new ResponseStateListener() {
		
		@Override
		public void onSuccess(Object obj, Class cla) {
			// TODO Auto-generated method stub
			if(cla.equals(SmsCode.class)){
				SmsCode sms=(SmsCode) obj;
				if(sms!=null){
					if(sms.isNeed()){//需要验证码
						dialog=new SmsCodeDialog();
						dialog.show(getActivity(), mHotelSmsCodeSubmitListener);
					}else{//不需要验证码,支付成功
						PromptUtil.showToast(getActivity(), sms.getMessage());
						gotoPaySuccess();
					}
				}
			}
		}
	};
	
	/**
	 * 酒店预订 验证码支付订单 监听
	 */
	private SmsCodeSubmitListener mHotelSmsCodeSubmitListener = new SmsCodeSubmitListener() {
		
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
	
	
	@Override
	public void onFocusChange(View v, boolean hasFocus) {//填写年月，显示正面，填写有效期，显示反面
		if(hasFocus){
			switch (v.getId()) {
			case R.id.et_month:
			case R.id.et_year:
				ivPositive.setVisibility(View.VISIBLE);
				//scrollView.scrollBy(0, 250);
				break;
			case R.id.et_cvv:
				ivNegative.setVisibility(View.VISIBLE);
				ivPositive.setVisibility(View.GONE);
				//scrollView.scrollBy(0, 250);
				break;
			default:
				break;
			}
		}else{
			if(v.getId()==R.id.et_cvv){
				ivNegative.setVisibility(View.GONE);
			}
		}
		
	}
}
