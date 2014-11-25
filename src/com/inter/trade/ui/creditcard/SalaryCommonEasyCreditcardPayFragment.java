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
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.inter.trade.ui.FunctionActivity;
import com.inter.trade.ui.activity.IFragment;
import com.inter.trade.ui.activity.SalaryPayMainActivity;
import com.inter.trade.ui.creditcard.SmsCodeDialog.SmsCodeSubmitListener;
import com.inter.trade.ui.creditcard.data.CreditCardData;
import com.inter.trade.ui.creditcard.data.DefaultBankCardData;
import com.inter.trade.ui.creditcard.data.SmsCode;
import com.inter.trade.ui.creditcard.task.AddBankCardTask;
import com.inter.trade.ui.creditcard.task.BankTask;
import com.inter.trade.ui.creditcard.task.CaiSmsCodeTask;
import com.inter.trade.ui.creditcard.task.SmsCodeTask;
import com.inter.trade.ui.creditcard.util.CreditCard;
import com.inter.trade.ui.creditcard.util.CreditcardInfoUtil;
import com.inter.trade.ui.fragment.AboutFragment;
import com.inter.trade.ui.fragment.FragmentFactory;
import com.inter.trade.ui.fragment.airticket.util.ApiAirticketGetOrderHistoryData;
import com.inter.trade.ui.fragment.airticket.util.ApiAirticketGetOrderHistoryParser;
import com.inter.trade.ui.fragment.gamerecharge.dialog.FavoriteCharacterDialogFragment;
import com.inter.trade.ui.fragment.gamerecharge.dialog.IFavoriteCharacterDialogListener;
import com.inter.trade.ui.fragment.salarypay.bean.CreditPayInfo;
import com.inter.trade.ui.fragment.salarypay.task.SalaryCreditPayTask;
import com.inter.trade.ui.fragment.waterelectricgas.data.ResponseStateListener;
import com.inter.trade.ui.manager.core.IMainHandlerManager;
import com.inter.trade.ui.manager.core.UIConstantDefault;
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
public class SalaryCommonEasyCreditcardPayFragment extends IFragment implements
		OnClickListener, IFavoriteCharacterDialogListener,
		ResponseStateListener, SmsCodeSubmitListener,
		OnCheckedChangeListener,OnFocusChangeListener{
	private EditText etCvv, etName, etID, etPhone,etMonth,etYear;
	private TextView tvPrice, tvCard,tvProtocol,tvBankList;
	private Button btnChoose, btnSubmit, btnBank;//, btnMonth, btnYear;
	private CheckBox cbSave,cbDefault;
	private LinearLayout cbDefaultLayout;
	private ScrollView scrollView;
	
	private String wagelistid;
	
	private ImageView ivPositive,ivNegative;//信用卡的正面和反面

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
	
	
	/**
	 * 获取的卡号信息
	 */
	private DefaultBankCardData creditCard;
	
	/**——————————————————————发工资有关数据end——————————————————————————————**/
	
	private 	Bundle b ;
	
	public static SalaryCommonEasyCreditcardPayFragment newInstance(Bundle b) {
		SalaryCommonEasyCreditcardPayFragment f = new SalaryCommonEasyCreditcardPayFragment();
		f.setArguments(b);
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}



	@Override
	protected View onInitView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.easypay_credit_card_pay_layout,
				null);
		initView(view);
		initData();
		setTopTitle("信用卡支付");
		return view;
	}

	@Override
	protected void onAsyncLoadData() {
			new BankTask(getActivity(), new ResponseStateListener() {
				
				@SuppressWarnings("unchecked")
				@Override
				public void onSuccess(Object obj, Class cla) {
					bankList=(ArrayList<BankData>) obj;
				}
			},true).execute("");
	}

	@Override
	public void onRefreshDatas() {
		
	}

	@Override
	public void onTimeout() {
		
	}

	@Override
	public void removeFragmentToStack() {
		((SalaryPayMainActivity) getActivity()).removeFragmentToStack();
	}

	@Override
	public void backHomeFragment() {
		((SalaryPayMainActivity) getActivity()).backHomeFragment();
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
		
		b= getArguments();
		if(b!=null){
			payKey = b.getInt(CreditCard.PAY_KEY, 0);

			bankno = b.getString(CreditCard.CARDNO);
			tvCard.setText(bankno);

			paymoney = b.getString(CreditCard.PAYMONEY);
			tvPrice.setText("￥" + paymoney);
			
			creditCard= (DefaultBankCardData) b.getSerializable("bank");
			if(creditCard!=null){
				isBank=true;
				String bankname = creditCard.getBkcardbank();
				btnBank.setText(bankname);
			}
		}
		
		cbDefaultLayout.setVisibility(View.GONE);//隐藏默认卡
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
		if(creditCard!=null){
			data.setBankId(creditCard.getBkcardbankid());
		}else{
			data.setBankId(bankList.get(bankIndex).bankid);
		}
		
		if(payKey==CreditCard.COUPON){
			if(btnBank.getHint() != null) {
				data.setBankId(btnBank.getHint().toString());
			}
		}
		
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
			case CreditCard.SALARY://发工资
				String pid = b.getString("paycardid");
				 wagelistid = b.getString("wagelistid");
				String wagemoney = b.getString("wagemoney");
				
				wagemonth = b.getString("wagemonth");
				personNumber = b.getString("personnumber");
				wageallmoney = b.getString("wageallmoney");
				paidmoney = b.getString("paidmoney");
				
				
				
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
		airTicketCreateOrderData.payinfoMap.put("cardCVV2No", data.getCvv2() !=null ? data.getCvv2():"");//三位有效码
		airTicketCreateOrderData.payinfoMap.put("cardHolder", data.getUsername() !=null ? data.getUsername() :"");//持卡人
		airTicketCreateOrderData.payinfoMap.put("cardHolderIdCardType", "1");//持卡人证件类型
		airTicketCreateOrderData.payinfoMap.put("cardHolderIdCardNumber", data.getId() !=null ? data.getId():"");//持卡人证件号
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
			func="ybwagepaySMSverify";
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
		switch (payKey) {
		case CreditCard.SALARY://发工资
			Bundle data=new Bundle();
			data.putString("personnumber",personNumber);
			data.putString("wageallmoney",wageallmoney);
			data.putString("paidmoney",paidmoney);
			data.putString("wagemonth",wagemonth);
			data.putString("wagelistid", wagelistid);
			removeFragmentToStack();
			addFragmentToStack(UIConstantDefault.UI_CONSTANT_SALARYPAY_SUCCESS, 1, data);
			break;
		default:
			break;
		}
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
				IMainHandlerManager.handlerUI(UIConstantDefault.UI_CONSTANT_AIR_TICKET_ORDER_PAY_SUCCESS, 1, orderDatas);
			}

			@Override
			public void onFailure(String error) {
			}
			
		}, false, true);
		
		asyncSmsPayWork.execute("ApiAirticket", "payWithCreditCard");
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
