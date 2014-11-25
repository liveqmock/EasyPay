package com.inter.trade.ui.fragment.coupon;

import java.util.HashMap;
import java.util.List;

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
import com.inter.trade.data.CardData;
import com.inter.trade.data.ResponseData;
import com.inter.trade.error.ErrorUtil;
import com.inter.trade.net.HttpUtil;
import com.inter.trade.ui.BankListActivity;
import com.inter.trade.ui.CounponActivity;
import com.inter.trade.ui.PayApp;
import com.inter.trade.ui.PayApp.SwipListener;
import com.inter.trade.ui.creditcard.CommonEasyCreditcardPayActivity;
import com.inter.trade.ui.creditcard.SmsCodeDialog;
import com.inter.trade.ui.creditcard.SmsCodeDialog.SmsCodeSubmitListener;
import com.inter.trade.ui.creditcard.data.DefaultBankCardData;
import com.inter.trade.ui.creditcard.data.SmsCode;
import com.inter.trade.ui.creditcard.task.CaiSmsCodeTask;
import com.inter.trade.ui.creditcard.util.CreditCard;
import com.inter.trade.ui.fragment.BaseFragment;
import com.inter.trade.ui.fragment.coupon.parser.CouponBuyParser;
import com.inter.trade.ui.fragment.coupon.task.CouponCreditPayTask;
import com.inter.trade.ui.fragment.coupon.util.CouponData;
import com.inter.trade.ui.fragment.cridet.data.JournalData;
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

public class CouponBuyFragment extends BaseFragment implements OnClickListener,
		SwipListener, ResponseStateListener, SmsCodeSubmitListener,AsyncLoadWorkListener {
	private Button cridet_back_btn;
	private CouponData mCouponData;
	private ImageView swip_card;
	private TextView swip_prompt;
	private EditText card_edit;
	private LinearLayout bank_layout;
	private TextView bank_name;
	private EditText open_name_edit;
	private EditText open_phone_edit;
	private TextView acount;

	private String mBkntno;
	private static double count = 0;
	private static String mCouponId;
	private boolean isSelectedBank = false;

	private BuyTask mBuyTask;
	private CounponActivity mActivity;
	
	
	private CouponCreditPayTask payTask;
	
	/**
	 * 标记是否可以选中银行
	 */
	private boolean isCanSelectBank=true;

	public static CouponBuyFragment create(double value, String couponId) {
		count = value;
		mCouponId = couponId;
		return new CouponBuyFragment();
	}

	public CouponBuyFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		LoginUtil.detection(getActivity());
		// initReader();
		PayApp.mSwipListener = this;
		initData();
		if (getActivity() instanceof CounponActivity) {
			mActivity = (CounponActivity) getActivity();
		}
		// SwipKeyTask.showDialog(getActivity(), DECODING);

	}

	private void initData() {
		mCouponData = new CouponData();
		mCouponData.sunMap.put("authorid", LoginUtil.mLoginStatus.authorid);
		mCouponData.sunMap.put(mCouponData.couponid, mCouponId);
		mCouponData.sunMap.put(mCouponData.couponmoney, count + "");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.layout_coupon_fragment, container,
				false);
		initView(view);

		setTitle("付款");
		setBackVisible();
		// openReader();

		return view;
	}

	private void initView(View view) {
		cridet_back_btn = (Button) view.findViewById(R.id.cridet_back_btn);
		swip_card = (ImageView) view.findViewById(R.id.swip_card);
		swip_prompt = (TextView) view.findViewById(R.id.swip_prompt);
		card_edit = (EditText) view.findViewById(R.id.card_edit);
		bank_layout = (LinearLayout) view.findViewById(R.id.bank_layout);
		bank_name = (TextView) view.findViewById(R.id.bank_name);
		acount = (TextView) view.findViewById(R.id.acount);
		open_phone_edit = (EditText) view.findViewById(R.id.open_phone_edit);
		open_name_edit = (EditText) view.findViewById(R.id.open_name_edit);

		cridet_back_btn.setOnClickListener(this);
		bank_layout.setOnClickListener(this);

		acount.setText(NumberFormatUtil.format2(count + "") + "元");
	}

	@Override
	public void recieveCard(CardData data) {
		// TODO Auto-generated method stub
		card_edit.setText(data.pan);
	}

	@Override
	public void checkedCard(boolean flag) {
		// TODO Auto-generated method stub
		if (flag) {
			boolean s = PayApp.openReaderNow();
			log("status : " + s);
			// startTimer();
			swip_prompt.setText(getString(R.string.has_checked_swip));
			swip_card.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.swip_enable));
		} else {
			// SwipKeyTask.showDialog(getActivity(), ReaderError);
			swip_prompt.setText(getString(R.string.cridet_insert));
			swip_card.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.swip_card_bg));
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
			mKeyTask = new SwipKeyTask(getActivity(),
									   PayApp.mKeyCode, 
									   PayApp.mKeyData, 
									   card_edit.getText().toString(), 
									   "coupon", this);
			mKeyTask.execute("");
			break;
		// case CMD_KSN:
		// mKeyTask = new SwipKeyTask(getActivity(), mKeyCode);
		// mKeyTask.execute("");
		// break;
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
				PromptUtil.showLongToast(getActivity(), getResources().getString(R.string.warn_repate_commit));
				return;
			}
			time = System.currentTimeMillis();
			
			if(creditCard!=null){
				if (creditCard != null
						&& "creditcard".equals(creditCard.getBkcardcardtype())) {// 保存了这张卡而且是信用卡
					directToPay();
				} else if (creditCard != null
						&& "bankcard".equals(creditCard.getBkcardcardtype())) {// 储蓄卡直接前往银联
					if(checkInput()){
						gotoDeposit();
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
	
	/***默认处理*/
	private void defaultDeal() {
		if (checkInput()) {
			String cardNumber = card_edit.getText().toString();
			if (BankCardUtil.isCreditCard(cardNumber)) {
				PromptUtil.showTwoButtonDialog("温馨提示",
						"您当前使用卡号为信用卡,是否选择信用卡支付通道?", new PositiveListener() {

							@Override
							public void onPositive() {// 易宝支付
								gotoCreditCard();
							}
						}, new NegativeListener() {
							@Override
							public void onNegative() {
								gotoDeposit();
							}
						}, getActivity());
			} else {// 银联支付
				gotoDeposit();
			}
		}
	}

	/*** 前往银联支付 */
	private void gotoDeposit() {
	
		mBuyTask = new BuyTask();
		mBuyTask.execute("");
	}

	/*** 前往信用卡支付页面 */
	private void gotoCreditCard() {
		Intent intent = new Intent(getActivity(),
				CommonEasyCreditcardPayActivity.class);
		intent.putExtra(CreditCard.PAY_KEY, CreditCard.COUPON);
		intent.putExtra(CreditCard.PAYMONEY,
				mCouponData.sunMap.get(mCouponData.couponmoney));
		intent.putExtra(CreditCard.CARDNO,
				mCouponData.sunMap.get(mCouponData.creditcardno));
		intent.putExtra(CouponData.CREDITCARDMAN,
				mCouponData.sunMap.get(mCouponData.creditcardman));
		intent.putExtra(CouponData.CREDITCARDPHONE,
				mCouponData.sunMap.get(mCouponData.creditcardphone));
		intent.putExtra(CouponData.PAYCARDID,
				mCouponData.sunMap.get(mCouponData.paycardid));
		intent.putExtra(CouponData.COUPONID, mCouponId);
		
		intent.putExtra(JournalData.fucardbank, bank_name.getText().toString() != null ? bank_name.getText().toString():"");//付款银行
		intent.putExtra(JournalData.fucardbankid, bank_name.getHint().toString() != null ? bank_name.getHint().toString() :"");//付款银行ID
		startActivityForResult(intent, 16);
	}

	/*** 直接前往信用卡支付 **/
	private void directToPay() {
		isSelectedBank=true;
		checkInput();
		mActivity.mCommonData.clear();

		HashMap<String, String> item1 = new HashMap<String, String>();
		item1.put("收款金额", mCouponData.sunMap.get(mCouponData.couponmoney) + "元");
		mActivity.mCommonData.add(item1);

		HashMap<String, String> item2 = new HashMap<String, String>();
		item2.put("付款卡号", mCouponData.sunMap.get(mCouponData.creditcardno));
		mActivity.mCommonData.add(item2);

		HashMap<String, String> item3 = new HashMap<String, String>();
		item3.put(
				"支付金额",
				NumberFormatUtil.format2(mCouponData.sunMap
						.get(mCouponData.couponmoney)) + "元");
		mActivity.mCommonData.add(item3);
		
		payTask =new CouponCreditPayTask(getActivity(), this);
		payTask.execute(
				mCouponId,
				mCouponData.sunMap.get(mCouponData.couponmoney),
				mCouponData.sunMap.get(mCouponData.paycardid),
				creditCard.getBkcardbank(),
				creditCard.getBkcardno(),
				creditCard.getBkcardbankman(),
				creditCard.getBkcardyxmonth(),
				creditCard.getBkcardidcard(),
				creditCard.getBkcardbankid(),
				creditCard.getBkcardyxyear(),
				creditCard.getBkcardbankphone(),
				creditCard.getBkcardcvv(),
				"coupon",
				"",
				creditCard.getBkcardbank()
				);

	}

	private void showBankList() {
		Intent intent = new Intent();
		intent.setClass(getActivity(), BankListActivity.class);
		startActivityForResult(intent, 1);
	}

	private boolean checkInput() {

		if (!isSelectedBank) {
			PromptUtil.showToast(getActivity(), "请选择银行");
			return false;
		}

		String cardNumber = card_edit.getText().toString();
		if (null == cardNumber || "".equals(cardNumber)) {
			PromptUtil.showToast(getActivity(), "请刷卡");
			return false;
		}

		if (!UserInfoCheck.checkBankCard(cardNumber)) {
			PromptUtil.showToast(getActivity(), "卡号格式不正确");
			return false;
		}
		mCouponData.sunMap.put(mCouponData.creditcardno, cardNumber);

		String openName = open_name_edit.getText().toString();
		if (null == openName || "".equals(openName)) {
			PromptUtil.showToast(getActivity(), "请输入开户名");
			return false;
		}
		if (!UserInfoCheck.checkName(openName)) {
			PromptUtil.showToast(getActivity(), "姓名格式不正确");
			return false;
		}

		mCouponData.sunMap.put(mCouponData.creditcardman, openName);
		String openPhone = open_phone_edit.getText().toString();
		if (null == openPhone || "".equals(openPhone)) {
			PromptUtil.showToast(getActivity(), "请输入电话号码");
			return false;
		}
		if (!UserInfoCheck.checkMobilePhone(openPhone)) {
			PromptUtil.showToast(getActivity(), "手机号码格式不正确");
			return false;
		}
		mCouponData.sunMap.put(mCouponData.creditcardphone, openPhone);

		if (PayApp.isSwipIn && PayApp.mKeyData.mType == 1) {
			PromptUtil.showToast(getActivity(), PayApp.mKeyData.message);
			return false;
		}

		// isSwipIn = true;
		// if(!PayApp.isSwipIn){
		// if(null == PayApp.mKeyCode || "".equals(PayApp.mKeyCode)){
		// PromptUtil.showToast(getActivity(), "请刷卡");
		// }else{
		// PromptUtil.showToast(getActivity(), "请插入刷卡器");
		// }
		// return false;
		// }
		mCouponData.sunMap.put(mCouponData.paycardid,
				PayApp.mKeyCode == null ? "" : PayApp.mKeyCode);
		mCouponData.sunMap.put(CouponData.merReserved,
				Base64Pay.encode(PayApp.merReserved.toString().getBytes()));
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
		if (null != bankname && !"".equals(bankname)) {
			isSelectedBank = true;
			mCouponData.sunMap.put(mCouponData.creditbank, bankname);
		}
		bank_name.setText(bankname);
		bank_name.setHint(bankid);//银行ID

		// 银联支付结果

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		// endCallStateService();
		// mobileReader.close();
		// mobileReader.releaseRecorderDevice();
		// closeReader();
		PayApp.mSwipListener = null;
		if (mBuyTask != null) {
			mBuyTask.cancel(true);
		}

		if (mKeyTask != null) {
			mKeyTask.cancel(true);
		}
		// cancelTimer();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// mobileReader.close();
		// endCallStateService();
		log("onpause endCallStateService");
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// mobileReader.open(false);
		// if(isSwipIn){
		// openReaderNow();
		// }
		//
		// startCallStateService();
		setTitle("付款");
		initSwipPic(PayApp.isSwipIn);
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		// mobileReader.close();
		// endCallStateService();
		log("onStop endCallStateService");
	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
		// mobileReader.close();
		// endCallStateService();
		log("onDetach endCallStateService");
	}

	private void showChuxuka() {
		UnionpayUtil.startUnionPlungin(mBkntno, getActivity());
	}

	private void initSwipPic(boolean flag) {
		if (flag) {
			swip_prompt.setText(getString(R.string.has_checked_swip));
			swip_card.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.swip_enable));
		} else {
			swip_prompt.setText(getString(R.string.cridet_insert));
			swip_card.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.swip_card_bg));
		}
	}

	class BuyTask extends AsyncTask<String, Integer, Boolean> {
		ProtocolRsp mRsp;

		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas(
						"ApiCouponInfo", "couponSale", mCouponData);
				CouponBuyParser authorRegParser = new CouponBuyParser();
				mRsp = HttpUtil.doRequest(authorRegParser, mDatas);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				mRsp = null;
			}

			return null;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			PromptUtil.dissmiss();
			if (mRsp == null) {
				PromptUtil.showToast(getActivity(),
						getString(R.string.net_error));
			} else {
				try {
					List<ProtocolData> mDatas = mRsp.mActions;
					parserResoponse(mDatas);
					if (!ErrorUtil.create().dealErrorWithDialog(LoginUtil.mLoginStatus,
							getActivity())) {
						return;
					}

					if (LoginUtil.mLoginStatus.result
							.equals(ProtocolUtil.SUCCESS)) {
						if (getActivity() instanceof CounponActivity) {
							mActivity.mCommonData.clear();

							((CounponActivity) getActivity()).mBankNo = mBkntno;
//							HashMap<String, String> item = new HashMap<String, String>();
//							item.put("交易请求号", mBkntno);
//							mActivity.mCommonData.add(item);

							HashMap<String, String> item1 = new HashMap<String, String>();
							item1.put(
									"收款金额",
									mCouponData.sunMap
											.get(mCouponData.couponmoney) + "元");
							mActivity.mCommonData.add(item1);

							HashMap<String, String> item2 = new HashMap<String, String>();
							item2.put("付款卡号", mCouponData.sunMap
									.get(mCouponData.creditcardno));
							mActivity.mCommonData.add(item2);

							HashMap<String, String> item3 = new HashMap<String, String>();
							item3.put(
									"支付金额",
									NumberFormatUtil.format2(mCouponData.sunMap
											.get(mCouponData.couponmoney))
											+ "元");
							mActivity.mCommonData.add(item3);
						}
						showChuxuka();
					} else {
						PromptUtil.showToast(getActivity(),
								LoginUtil.mLoginStatus.message);
					}
				} catch (Exception e) {
					// TODO: handle exception
					PromptUtil.showToast(getActivity(),
							getString(R.string.req_error));
				}

			}
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			PromptUtil.showDialog(getActivity(), getActivity().getResources()
					.getString(R.string.loading));
		}
	}

	private void parserResoponse(List<ProtocolData> params) {
		ResponseData response = new ResponseData();
		LoginUtil.mLoginStatus.mResponseData = response;
		for (ProtocolData data : params) {
			if (data.mKey.equals(ProtocolUtil.msgheader)) {
				ProtocolUtil.parserResponse(response, data);
			} else if (data.mKey.equals(ProtocolUtil.msgbody)) {

				List<ProtocolData> result1 = data.find("/result");
				if (result1 != null) {
					LoginUtil.mLoginStatus.result = result1.get(0).mValue;
				}

				List<ProtocolData> message = data.find("/message");
				if (message != null) {
					LoginUtil.mLoginStatus.message = message.get(0).mValue;
				}

				List<ProtocolData> bkntno = data.find("/bkntno");
				if (bkntno != null) {
					mBkntno = bkntno.get(0).mValue;
				}
			}
		}
	}

	private DefaultBankCardData creditCard;
	private String orderId;
	private SmsCode sms;

	@Override
	public void onSuccess(Object obj, Class cla) {
		if (cla.equals(SmsCode.class)) {
			sms = (SmsCode) obj;
			if (sms != null) {
				orderId = sms.getBkordernumber();
//				HashMap<String, String> item = new HashMap<String, String>();
//				item.put("交易请求号", orderId);
//				mActivity.mCommonData.add(item);
				if (sms.isNeed()) {// 需要验证码
					SmsCodeDialog dialog = new SmsCodeDialog();
					dialog.show(getActivity(), this);
					hideSoftInput();
				} else {// 不需要验证码,表示支付成功
					PromptUtil.showToast(getActivity(), sms.getMessage());
					goPaySunccess();
				}
			}
		} 
	}

	/*** 支付成功 */
	private void goPaySunccess() {
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.replace(R.id.func_container,
				BuySuccessFragment.createFragment(mActivity.mCommonData));
		ft.commit();
	}

	@Override
	public void onPositive(String code, SmsCodeDialog dialog) {
		String funName = "ApiyibaoPayInfo";
		String func = "couponPaySMSverify";
		new CaiSmsCodeTask(getActivity(), new ResponseStateListener() {

			@Override
			public void onSuccess(Object obj, Class cla) {
				PromptUtil.showToast(getActivity(), (String) obj);
				goPaySunccess();
			}

		}).execute(code,
				   sms.getBkordernumber(),
				   sms.getBkntno(),
				   sms.getVerifytoken(),
				   funName, func);
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
				if(creditCard.getBkcardbankid() != null) {
					bank_name.setHint(creditCard.getBkcardbankid());//银行ID
				}else{
					bank_name.setHint("");//银行ID
				}
				isCanSelectBank=false;
				isSelectedBank=true;
			}else{
				bank_name.setText("");
				isCanSelectBank=true;
				isSelectedBank=false;
			}
		}
	}

	@Override
	public void onFailure(String error) {
		
	}

}
