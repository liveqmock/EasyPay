package com.inter.trade.ui.fragment.salarypay;

import java.util.ArrayList;
import java.util.Calendar;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.inter.trade.AsyncLoadWork.AsyncLoadWorkListener;
import com.inter.trade.R;
import com.inter.trade.SwipKeyTask;
import com.inter.trade.data.BankData;
import com.inter.trade.data.CardData;
import com.inter.trade.db.DBHelper;
import com.inter.trade.db.SupportBank;
import com.inter.trade.log.Logger;
import com.inter.trade.ui.PayApp;
import com.inter.trade.ui.PayApp.SwipListener;
import com.inter.trade.ui.activity.SalaryPayMainActivity;
import com.inter.trade.ui.creditcard.SmsCodeDialog;
import com.inter.trade.ui.creditcard.SmsCodeDialog.SmsCodeSubmitListener;
import com.inter.trade.ui.creditcard.data.DefaultBankCardData;
import com.inter.trade.ui.creditcard.data.SmsCode;
import com.inter.trade.ui.creditcard.task.BankTask;
import com.inter.trade.ui.creditcard.task.CaiSmsCodeTask;
import com.inter.trade.ui.creditcard.util.CreditCard;
import com.inter.trade.ui.fragment.gamerecharge.data.BillData;
import com.inter.trade.ui.fragment.salarypay.bean.ChannelData;
import com.inter.trade.ui.fragment.salarypay.bean.CreditPayInfo;
import com.inter.trade.ui.fragment.salarypay.bean.DepositPayInfo;
import com.inter.trade.ui.fragment.salarypay.bean.PayInfo;
import com.inter.trade.ui.fragment.salarypay.bean.WageFeeInfo;
import com.inter.trade.ui.fragment.salarypay.task.GetBillTask;
import com.inter.trade.ui.fragment.salarypay.task.GetPayFeeTask;
import com.inter.trade.ui.fragment.salarypay.task.GetPayWaysTask;
import com.inter.trade.ui.fragment.salarypay.task.GetSalaryInfoTask;
import com.inter.trade.ui.fragment.salarypay.task.SalaryCreditPayTask;
import com.inter.trade.ui.fragment.waterelectricgas.data.ResponseStateListener;
import com.inter.trade.ui.manager.core.UIConstantDefault;
import com.inter.trade.util.BankCardUtil;
import com.inter.trade.util.DateUtil;
import com.inter.trade.util.PromptUtil;
import com.inter.trade.util.PromptUtil.NegativeListener;
import com.inter.trade.util.PromptUtil.PositiveListener;
import com.inter.trade.util.TaskUtil;
import com.unionpay.uppayplugin.demo.UnionpayUtil;

/**
 * 发工资的账单支付页面
 * 
 * @author chenguangchi
 * @data: 2014年9月2日 下午4:25:55
 * @version: V1.0
 */
public class SalaryPayConfirmFragment extends SalaryPayBaseFragment implements
		OnClickListener, SwipListener, AsyncLoadWorkListener,
		ResponseStateListener, SmsCodeSubmitListener {

	private Button btnCommit;

	private ImageView swip_card;

	private EditText etCard;

	private SwipKeyTask mKeyTask;

	/**
	 * 支付工资的信息
	 */
	private PayInfo info = null;

	/**
	 * 通道信息
	 */
	private ChannelData channel;

	/**
	 * 当前的月份
	 */
	private String currentDate;
	/**
	 * 人数
	 */
	private int personNumber;
	/**
	 * 银行卡号
	 */
	private String bankno;

	/**
	 * 银联支付的有关信息
	 */
	private DepositPayInfo dInfo;

	/**
	 * 易宝支付的有关信息
	 */
	private CreditPayInfo cInfo;

	/**
	 * 此次支付的工资
	 */
	private Double money = 0.00;

	/**
	 * 总共需要支付的金额
	 */
	private double paymoney = 0;

	/**
	 * 支付的工资总额
	 */
	private String wageallmoney;

	/**
	 * 已支付的工资
	 */
	private Double paidmoney;
	private GetBillTask payTask;

	private SalaryCreditPayTask creditTask;

	/**
	 * 易宝支持的银行列表
	 */
	private ArrayList<SupportBank> bankList;
	
	private String wagelistid;

	private TextView tvPersonNumber, tvTotal, tvMaxSalary, tvFeePercent, tvFee,
			tvMoney;

	public static SalaryPayConfirmFragment getInstance(Bundle bundle) {
		SalaryPayConfirmFragment fragment = new SalaryPayConfirmFragment();
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		PayApp.mSwipListener = this;
		Bundle bundle = getArguments();
		if (bundle != null) {
			currentDate = bundle.getString("date");
			personNumber = bundle.getInt("personnumber");
			wagelistid=bundle.getString("wagelistid");
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		setTopTitle("付款");
		changeSwpieState(PayApp.isSwipIn);
	}

	@Override
	protected View onInitView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_salarypay_confirm, null);
		initView(view);
		return view;
	}

	private void initView(View view) {
		btnCommit = (Button) view.findViewById(R.id.btn_commit);
		etCard = (EditText) view.findViewById(R.id.card_edit);
		swip_card = (ImageView) view.findViewById(R.id.swip_card);
		tvPersonNumber = (TextView) view.findViewById(R.id.tv_person_number);
		tvTotal = (TextView) view.findViewById(R.id.tv_total);
		tvMaxSalary = (TextView) view.findViewById(R.id.tv_max_salary);
		tvFeePercent = (TextView) view.findViewById(R.id.tv_fee_percent);
		tvFee = (TextView) view.findViewById(R.id.tv_fee);
		tvMoney = (TextView) view.findViewById(R.id.tv_money);

		btnCommit.setOnClickListener(this);

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (db != null) {
			db.closeDB();
		}
	}

	private DBHelper db;

	@Override
	protected void onAsyncLoadData() {
		db = DBHelper.getInstance(getActivity());
		bankList = (ArrayList<SupportBank>) db.queryBanks();
		Logger.i(bankList.toString());
		if (bankList == null || bankList.size() == 0) {
			new BankTask(getActivity(), this, true).execute("");
		}
	}

	@Override
	public void onRefreshDatas() {
		if (currentDate == null) {
			Calendar c = Calendar.getInstance();
			currentDate = DateUtil.getYYYYMMFormatStr(c.getTime());
		}
		new GetSalaryInfoTask(getActivity(), this, false).execute(currentDate,wagelistid);
	}

	@Override
	public void onTimeout() {

	}

	long time = 0L;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_commit:
			// if(checkInput()){
			// gotoDeposit();
			// }
			if (checkInput()) {
				// addFragmentToStack(UIConstantDefault.UI_CONSTANT_SALARYPAY_SUCCESS,
				// 1,null);
				long currentTime = System.currentTimeMillis();
				if (currentTime - time < 1000
						|| TaskUtil.isTaskExcuting(payTask)
						|| TaskUtil.isTaskExcuting(creditTask)) {
					PromptUtil.showLongToast(getActivity(), getResources()
							.getString(R.string.warn_repate_commit));
					return;
				}
				time = System.currentTimeMillis();

				if (creditCard != null) {
					if (creditCard != null
							&& "creditcard".equals(creditCard
									.getBkcardcardtype())) {// 保存了这张卡而且是信用卡
						directToPay();
					} else if (creditCard != null
							&& "bankcard"
									.equals(creditCard.getBkcardcardtype())) {// 储蓄卡直接前往银联
						if (checkInput()) {
							gotoDeposit();
						}
					} else {
						defaultDeal();
					}
				} else {
					defaultDeal();
				}
			}

		default:
			break;
		}
	}

	/*** 默认处理 */
	private void defaultDeal() {
//		 String cardNumber = etCard.getText().toString();
//		 if (BankCardUtil.isCreditCard(cardNumber)
//		 &&BankCardUtil.isSupportYiBao(creditCard, bankList)) {
//		 PromptUtil.showTwoButtonDialog("温馨提示", "您当前使用卡号为信用卡,是否选择信用卡支付通道?",
//		 new PositiveListener() {
//		
//		 @Override
//		 public void onPositive() {// 易宝支付
//		 gotoCreditCard();
//		 }
//		 }, new NegativeListener() {
//		 @Override
//		 public void onNegative() {
//		 gotoDeposit();
//		 }
//		 }, getActivity());
//		 } else {// 银联支付
//		 gotoDeposit();
//		 }
		gotoCreditCard();
	}

	/*** 前往银联支付 */
	private void gotoDeposit() {
//		 collectDeposit();
//		 payTask = new GetBillTask(getActivity(), this);
//		 payTask.execute(dInfo);
		
		gotoCreditCard();
	}

	/*** 前往信用卡支付页面 */
	private void gotoCreditCard() {
		// Intent intent = new Intent(getActivity(),
		// CommonEasyCreditcardPayActivity.class);
		// intent.putExtra(CreditCard.PAY_KEY, CreditCard.SALARY);
		// intent.putExtra(CreditCard.PAYMONEY, paymoney+"");
		// intent.putExtra(CreditCard.CARDNO, bankno);
		// intent.putExtra("paycardid", PayApp.mKeyCode == null ? "" :
		// PayApp.mKeyCode);
		// intent.putExtra("wagelistid",info.wagelistid);
		// intent.putExtra("wagemoney", money+"");
		// intent.putExtra("wagemonth", currentDate+"");
		//
		// intent.putExtra("personnumber",personNumber+"");
		// intent.putExtra("wageallmoney",wageallmoney);
		// intent.putExtra("paidmoney",(paidmoney+money)+"");
		// startActivity(intent);

		Bundle b = new Bundle();
		b.putInt(CreditCard.PAY_KEY, CreditCard.SALARY);
		b.putString(CreditCard.PAYMONEY, paymoney + "");
		b.putString(CreditCard.CARDNO, bankno);
		b.putString("paycardid", PayApp.mKeyCode == null ? "" : PayApp.mKeyCode);
		b.putString("wagelistid", info.wagelistid);
		b.putString("wagemoney", money + "");
		b.putString("personnumber", personNumber + "");
		b.putString("wageallmoney", wageallmoney);
		b.putString("paidmoney", (paidmoney + money) + "");
		b.putString("wagemonth", currentDate + "");
		b.putSerializable("bank", creditCard);
		addFragmentToStack(UIConstantDefault.UI_CONSTANT_SALARY_CREDITPAY, 1, b);
	}

	/*** 直接前往易宝支付 */
	private void directToPay() {
		collectCredit();
		creditTask = new SalaryCreditPayTask(getActivity(), this);
		creditTask.execute(cInfo);
	}

	private boolean checkInput() {
		if (TextUtils.isEmpty(etCard.getText())) {
			PromptUtil.showLongToast(getActivity(), "亲,请刷卡!");
			return false;
		}
		bankno = etCard.getText() + "";
		if (money <= 0) {
			PromptUtil.showLongToast(getActivity(), "亲,本月工资已支付!");
			return false;
		}

		return true;
	}

	private void collectDeposit() {
		if (dInfo == null) {
			dInfo = new DepositPayInfo();
		}
		dInfo.fucardno = bankno;
		dInfo.paycardid = PayApp.mKeyCode == null ? "" : PayApp.mKeyCode;
		dInfo.fucardbank = "";
		dInfo.wagelistid = info.wagelistid;
		dInfo.wagemoney = money + "";
		dInfo.wagemonth = currentDate;
		dInfo.wagepaymoney = paymoney + "";

		// 收集成功页面需要的数据
		SalaryPayMainActivity.personNumber = personNumber + "";
		SalaryPayMainActivity.wageallmoney = wageallmoney;
		//SalaryPayMainActivity.paidmoney = (paidmoney + money) + "";
		SalaryPayMainActivity.wageMonth = currentDate;
	}

	private void collectCredit() {
		if (creditCard != null) {
			if (cInfo == null) {
				cInfo = new CreditPayInfo();
			}
			cInfo.bankid = creditCard.getBkcardbankid();
			cInfo.bkcardbank = creditCard.getBkcardbank();
			cInfo.bkcardcvv = creditCard.getBkcardcvv();
			cInfo.bkcardexpireMonth = creditCard.getBkcardyxmonth();
			cInfo.bkcardexpireYear = creditCard.getBkcardyxyear();
			cInfo.bkcardman = creditCard.getBkcardbankman();
			cInfo.bkcardmanidcard = creditCard.getBkcardidcard();
			cInfo.bkCardno = creditCard.getBkcardno();
			cInfo.bkcardPhone = creditCard.getBkcardbankphone();
			cInfo.paycardid = PayApp.mKeyCode == null ? "" : PayApp.mKeyCode;
			cInfo.wagelistid = info.wagelistid;
			cInfo.wagemoney = money + "";
			cInfo.wagemonth = currentDate;
			cInfo.wagepaymoney = paymoney + "";
		}
	}

	@Override
	public void recieveCard(CardData data) {
		etCard.setText(data.pan);
		bankno = data.pan;
	}

	@Override
	public void checkedCard(boolean flag) {
		if (isAdded()) {
			changeSwpieState(flag);
		}
	}

	private void changeSwpieState(boolean flag) {
		if (flag) {
			boolean s = PayApp.openReaderNow();
			swip_card.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.swip_enable));
		} else {
			swip_card.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.swip_card_bg));
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

			mKeyTask = new SwipKeyTask(getActivity(), PayApp.mKeyCode,
					PayApp.mKeyData, etCard.getText().toString(), "salarypay",
					this);
			mKeyTask.execute("");
			break;
		default:
			break;
		}
	}

	/***
	 * 刷卡获取的卡号信息
	 */
	private DefaultBankCardData creditCard;
	private String orderId;
	private SmsCode sms;

	@Override
	public void onSuccess(Object protocolDataList, Bundle bundle) {
		creditCard = (DefaultBankCardData) protocolDataList;
	}

	@Override
	public void onFailure(String error) {

	}

	@Override
	public void onSuccess(Object obj, Class cla) {

		if (ChannelData.class.equals(cla)) {// 获取费率信息
			ArrayList<ChannelData> cList = (ArrayList<ChannelData>) obj;
			for (ChannelData c : cList) {
				if ("易宝支付".equals(c.paychalname)) {
					channel = c;
					break;
				}
			}
			
			double maxmoney =Double.parseDouble(channel.paychalmaxmoney);//通道最大限额
			double feepercent =Double.parseDouble(info.feeperent.replace("%", ""))/100;//手续费率
			
			double wagemaxmoney =Math.rint(maxmoney/(1+feepercent)*100)/100;//通道最大支付的工资
			double needpaymoney=Math.rint(Double.parseDouble(info.needpaymoney)*100)/100;//剩余支付工资,不含手续费
			
			double thepaymoney = Math
					.rint(Math.min(needpaymoney, wagemaxmoney) * 100) / 100;
			
			new GetPayFeeTask(getActivity(), this).execute(channel.paychalid,"salarypay",thepaymoney+"");
			
		} else if (PayInfo.class.equals(cla)) {// 获取工资支付信息

			info = (PayInfo) obj;
			new GetPayWaysTask(getActivity(), this).execute("");

		} else if (BillData.class.equals(cla)) {// 银联获取订单
			BillData bill = (BillData) obj;
			UnionpayUtil.startUnionPlungin(bill.getBkntno(), getActivity());
			((SalaryPayMainActivity) getActivity()).mBankNo = bill.getBkntno();
		} else if (SmsCode.class.equals(cla)) {// 易宝直接支付
			sms = (SmsCode) obj;
			if (sms != null) {
				orderId = sms.getBkordernumber();
				if (sms.isNeed()) {// 需要验证码
					SmsCodeDialog dialog = new SmsCodeDialog();
					dialog.show(getActivity(), this);
				} else {// 不需要验证码,表示支付成功
					PromptUtil.showToast(getActivity(), sms.getMessage());
					goPaySunccess();
				}
			}
		} else if (BankData.class.equals(cla)) {//获取易宝支付
			ArrayList<BankData> list = (ArrayList<BankData>) obj;
			for (BankData data : list) {
				SupportBank bank = new SupportBank(null, data.bankid,
						data.bankno, data.bankname);
				bankList.add(bank);
			}
		} else if (WageFeeInfo.class.equals(cla)) {// 获取手续费
			
			WageFeeInfo feeinfo=(WageFeeInfo) obj;
			
			
			tvTotal.setText(info.wageallmoney + "元");
			tvPersonNumber.setText(personNumber + "人");
			tvFeePercent.setText(info.feeperent);
			tvMaxSalary.setText(feeinfo.money + "元");
			tvMoney.setText(feeinfo.paymoney + "元");
			tvFee.setText(feeinfo.payfee + "元");
			
			money=Double.parseDouble(feeinfo.money);
			paymoney=Double.parseDouble(feeinfo.paymoney);
			paidmoney=Double.parseDouble(info.wagepaymoney);

			Logger.i(info.toString());
		}
	}

	private void goPaySunccess() {
		Bundle bundle = new Bundle();
		bundle.putString("personnumber", personNumber + "");
		bundle.putString("wageallmoney", wageallmoney);
		bundle.putString("paidmoney", (paidmoney + money) + "");
		bundle.putString("wagemonth", currentDate);
		bundle.putString("wagelistid", wagelistid);
		addFragmentToStack(UIConstantDefault.UI_CONSTANT_SALARYPAY_SUCCESS, 1,
				bundle);
	}

	@Override
	public void onPositive(String code, SmsCodeDialog dialog) {
		String funName = "ApiWageInfo";
		String func = "ybwagepaySMSverify";
		new CaiSmsCodeTask(getActivity(), new ResponseStateListener() {

			@Override
			public void onSuccess(Object obj, Class cla) {
				PromptUtil.showToast(getActivity(), (String) obj);
				goPaySunccess();
			}

		}).execute(code, sms.getBkordernumber(), sms.getBkntno(),
				sms.getVerifytoken(), funName, func);
	}
}
