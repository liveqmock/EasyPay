package com.inter.trade.ui.fragment.buyhtbcard;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.inter.trade.AsyncLoadWork.AsyncLoadWorkListener;
import com.inter.trade.AsyncLoadWork;
import com.inter.trade.R;
import com.inter.trade.SwipKeyTask;
import com.inter.trade.data.CardData;
import com.inter.trade.data.CommonData;
import com.inter.trade.data.ResultData;
import com.inter.trade.ui.PayApp;
import com.inter.trade.ui.PayApp.BuySuccessListener;
import com.inter.trade.ui.TelephonePayActivity;
import com.inter.trade.ui.PayApp.SwipListener;
import com.inter.trade.ui.creditcard.MyBankCardActivity;
import com.inter.trade.ui.creditcard.PayWaysHandler;
import com.inter.trade.ui.creditcard.SmsCodeDialog;
import com.inter.trade.ui.creditcard.SmsCodeDialog.SmsCodeSubmitListener;
import com.inter.trade.ui.creditcard.data.DefaultBankCardData;
import com.inter.trade.ui.creditcard.data.SmsCode;
import com.inter.trade.ui.creditcard.task.CaiSmsCodeTask;
import com.inter.trade.ui.creditcard.task.GetDefaultTask;
import com.inter.trade.ui.creditcard.util.CreditCard;
import com.inter.trade.ui.fragment.airticket.util.ApiAirticketGetOrderHistoryData;
import com.inter.trade.ui.fragment.buyhtbcard.util.OrderRequestParser;
import com.inter.trade.ui.fragment.buyhtbcard.util.YiBaoOrderRequestParser;
import com.inter.trade.ui.fragment.coupon.task.BuySuccessTask;
import com.inter.trade.ui.fragment.waterelectricgas.data.ResponseStateListener;
import com.inter.trade.ui.manager.IBaseFragment;
import com.inter.trade.ui.manager.UIManagerActivity;
import com.inter.trade.ui.manager.core.UIConstantDefault;
import com.inter.trade.util.BankCardUtil;
import com.inter.trade.util.NumberFormatUtil;
import com.inter.trade.util.PromptUtil;
import com.inter.trade.util.UserInfoCheck;
import com.inter.trade.util.PromptUtil.NegativeListener;
import com.inter.trade.util.PromptUtil.PositiveListener;
import com.unionpay.uppayplugin.demo.UnionpayUtil;

/**
 *  清单结算 支付 Fragment
 * @author zhichao.huang
 *
 */
public class PayMainFragment extends IBaseFragment 
implements OnClickListener, SwipListener, BuySuccessListener
{

	private static final String TAG = PayMainFragment.class.getName();

	private View rootView;

	private Button submitButton;

	/**
	 * 产品名称
	 */
	private TextView produreName;
	/**
	 * 数量
	 */
	private TextView produreNum;
	/**
	 * 单价
	 */
	private TextView produrePrice; 
	/**
	 * 支付总额
	 */
	private TextView produreTotal;

//	private LinearLayout fan_layout;

//	/**
//	 * 飞机票总价
//	 */
//	private int air_qu_total, air_fan_total;

	private Bundle data = null;


	/*** 处理支付方式的类 */
	private PayWaysHandler viewHandler;

	/*** 判断刷卡时是否为信用卡 */
	private boolean isCreditCardk = false;

	protected SwipKeyTask mKeyTask;

	private GetDefaultTask getDefaultTask;

	/***用来保存默认的银行卡*/
	private DefaultBankCardData creditCard;
	
	/**
	 * 后台获取预保存的卡(“刷卡”或者“选择我的银行卡”)
	 */
	private DefaultBankCardData defaultBankCardData;
	
	/**
	 * 订单提交异步
	 */
	private AsyncLoadWork<String> asyncLoadWork = null;
	
	/**
	 * 易宝订单提交异步
	 */
	private AsyncLoadWork<SmsCode> yibaoAsyncLoadWork = null;
	
	/**
	 * 订单短信支付提交异步
	 */
	private AsyncLoadWork<ApiAirticketGetOrderHistoryData> asyncSmsPayWork = null;
	
	private ArrayList<String> protocolList = null;
	
	public static PayMainFragment newInstance (Bundle data) {
		PayMainFragment fragment = new PayMainFragment();
		fragment.setArguments(data);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActivity().setTheme(R.style.DialogStyleLight);
		Log.i(TAG, "onCreate");
		Bundle bundle = getArguments();
		if(bundle != null) {
			data = bundle;
		}
		PayApp.mSwipListener = this;
	}

	@Override
	protected View onInitView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.htb_buycard_pay_layout, container,false);
		initViews(rootView);
		return rootView;
	}

	@Override
	protected void onAsyncLoadData() {
		//获取默认银行卡
		getDefaultTask = new GetDefaultTask(getActivity(), responseStateListener);
		getDefaultTask.execute("", "1");
	}

	@Override
	public void onRefreshDatas() {
		initSwipPic(PayApp.isSwipIn);
		((UIManagerActivity)getActivity()).setTopTitle("订单支付");
	}

	private void initSwipPic(boolean flag) {
		viewHandler.setCardImageVisibility(flag);
	}

	private void initViews (View rootView) {

		//订单信息
		produreName = (TextView)rootView.findViewById(R.id.produreName);
		produreNum = (TextView)rootView.findViewById(R.id.produreNum);
		produrePrice = (TextView)rootView.findViewById(R.id.produrePrice);
		produreTotal = (TextView)rootView.findViewById(R.id.produreTotal);
		
		
		//确定按钮
		submitButton = (Button)rootView.findViewById(R.id.submit_btn);
		submitButton.setOnClickListener(this);

		//付款
		viewHandler = new PayWaysHandler(getActivity(), this, null, null);
		viewHandler.initView(null, rootView);
		viewHandler.setDefaultPay(2);

		initDatas ();
	}

	private void initDatas () {
		if(data == null) return;
		
		produreName.setText(data.getString("produrename"));
		produreNum.setText(data.getString("ordernum"));
		produrePrice.setText(NumberFormatUtil.format2(data.getString("orderprice")));
		produreTotal.setText(data.getString("ordermoney"));
	}

	@Override
	public void onTimeout() {

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		PayApp.mSwipListener = null;
	}

	/** 銀行卡號 */
	private String cardNumber;
	/**
	 * 校验
	 * @return
	 */
	private boolean checkInput() {

		if (data == null) {
			PromptUtil.showToast(getActivity(), "请求数据不完整");
			return false;
		}

		if (!viewHandler.isSelected()) {
			PromptUtil.showToast(getActivity(), "请选择一种支付方式");
			return false;
		}
		cardNumber = "";
		switch (viewHandler.getCheckpay()) {
		case 1:
			cardNumber = creditCard.getBkcardno();
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

		if (!UserInfoCheck.checkBankCard(cardNumber)) {
			PromptUtil.showToast(getActivity(), "卡号格式不正确");
			return false;
		}


		return true;
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.submit_btn:
			if(checkInput()){

				if(viewHandler.getCheckpay() == 1) {//默认支付
					if(creditCard != null) {
						//进入默认支付
//						PromptUtil.showToast(getActivity(), "默认支付");
						gotoCreditCardPay(creditCard);
					}

				} else if (viewHandler.getCheckpay() == 2) {//信用卡支付

					//检查后台返回的银行卡信息有效性
					if(checkDefaultBankCardData()) {

						/*用后台获取的银行卡数据，和卡号输入框的数据比较（指两者的卡号进行比较），
						如果一样则判断是同一张卡，则用后台返回的银行卡做交易；如不同则按用户输入信息进行交易*/
						if(viewHandler.getCredit() != null && !viewHandler.getCredit().equals("") 
								&& defaultBankCardData.getBkcardno() != null && !defaultBankCardData.getBkcardno().equals("")
								&& viewHandler.getCredit().equals(defaultBankCardData.getBkcardno())) {

							if(defaultBankCardData.getBkcardcardtype() != null ) {
								if(defaultBankCardData.getBkcardcardtype().equals("creditcard")) {//信用卡
									//飞机票 订单支付
//									PromptUtil.showToast(getActivity(), "刷卡信用卡支付");
									gotoCreditCardPay(defaultBankCardData);
								} 
								else {//储蓄卡
//									PromptUtil.showToast(getActivity(), "暂不支持储蓄卡支付");
									gotoUnionPay(viewHandler.getCredit());
								}
							}

						} else {
//							PromptUtil.showToast(getActivity(), "用户输入卡号支付");
							checkBankCardType(viewHandler.getCredit());
						}

					} else {
//						PromptUtil.showToast(getActivity(), "输入卡号支付");
						checkBankCardType(viewHandler.getCredit());
					}
				} else { //其他储蓄卡
					checkBankCardType(viewHandler.getCredit());
				}

//				addFragmentToStack(UIConstantDefault.UI_CONSTANT_AIR_TICKET_CREDITCARD_PAY, 1, data);

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
	 * 当检测到用户手输卡号时，检查卡类型
	 * @param bankno
	 */
	private void checkBankCardType (final String bankno) {
		if(BankCardUtil.isCreditCard(bankno)){
			PromptUtil.showTwoButtonDialog("温馨提示", "您当前使用卡号为信用卡,是否选择信用卡支付通道?", new PositiveListener() {
				
				@Override
				public void onPositive() {//信用卡填写页面
					gotoCreditCardPagePay(bankno);
				}
			}, new NegativeListener() {
				
				@Override
				public void onNegative() {//银联支付
					gotoUnionPay (bankno);
				}
			}, getActivity());
			
			
		} else {
//			PromptUtil.showToast(getActivity(), "暂不支持储蓄卡支付");
			gotoUnionPay (bankno);
		}
	}
	
	/**
	 * 银联支付
	 */
	private void gotoUnionPay (String bankno) {
		OrderRequestParser authorRegParser = new OrderRequestParser();
		
		asyncLoadWork = new AsyncLoadWork<String>(getActivity(), authorRegParser, buildRequestBusinessData(), new AsyncLoadWorkListener() {

			@Override
			public void onSuccess(Object protocolDataList, Bundle bundle) {
				protocolList = (ArrayList<String>)protocolDataList;
				unionpayOrderNum = protocolList.get(0);
				
				if(TextUtils.isEmpty(unionpayOrderNum)){
					PromptUtil.showToast(getActivity(), "订单出错!");
					return;
				}
				UnionpayUtil.startUnionPlungin(unionpayOrderNum, getActivity());//跳到银联支付控件
			}

			@Override
			public void onFailure(String error) {
				
			}
			
		}, false, true);
		asyncLoadWork.execute("ApiExpresspayInfo", "payagentOrderRq");
	}
	
	/**
	 * 直接信用卡页面填写支付
	 * 
	 * @param bankno
	 */
	private void gotoCreditCardPagePay (String bankno) {
		data.putInt("pay_key", CreditCard.HTB_CARD_BUY);//支付业务类型
		data.putString("bankno", bankno);//卡号
		data.putString("paymonery", data.getString("ordermoney") != null ? data.getString("ordermoney") :"");//支付金额
		data.putSerializable("orderData",  buildRequestBusinessData());
		
		addFragmentToStack(UIConstantDefault.UI_CONSTANT_CREDITCARD_PAY, 1, data);
	}
	
	/**
	 * 直接信用卡支付
	 */
	private void gotoCreditCardPay(DefaultBankCardData defaultBankCardData ) {
		YiBaoOrderRequestParser authorRegParser = new YiBaoOrderRequestParser();

		yibaoAsyncLoadWork = new AsyncLoadWork<SmsCode>(getActivity(), authorRegParser, buildRequestAllDatas(defaultBankCardData), creditCardPayListener, false, true, true);
		yibaoAsyncLoadWork.execute("ApiExpresspayInfo", "ybagentorderPayrq");
	}
	
	/**
	 * 构造的整合请求数据（包括业务数据+信用卡信息）
	 * 
	 * @param defaultBankCardData
	 * @return
	 */
	private CommonData buildRequestAllDatas (DefaultBankCardData defaultBankCardData) {
		CommonData reqData = buildRequestBusinessData();
		return getDefaultBankCardData(reqData, defaultBankCardData);
		
	}
	
	/**
	 * 构建请求业务数据
	 * 
	 * @param defaultBankCardData
	 * @return
	 */
	private CommonData buildRequestBusinessData () {
		CommonData reqData = new CommonData();
		reqData.putValue("orderprodureid", data.getString("orderprodureid") != null ? data.getString("orderprodureid") :"");//购买产品id
		reqData.putValue("ordernum", data.getString("ordernum") != null ? data.getString("ordernum") : "");//购买数量
		reqData.putValue("orderprice", data.getString("orderprice") != null ? data.getString("orderprice") :"");//单个价格
		reqData.putValue("ordermoney", data.getString("ordermoney") != null ? data.getString("ordermoney") :"");//订单总额
		reqData.putValue("ordermemo", "");//订单备注
		reqData.putValue("paycardid", PayApp.mKeyCode != null ? PayApp.mKeyCode : "");//刷卡器ID
		
		//跳转到银联支付附加信息
		reqData.putValue("orderfucardno", viewHandler.getCredit() != null ? viewHandler.getCredit() :"");//支付银行卡号
		reqData.putValue("orderfucardbank", "");//发卡银行
		return reqData;
		
	}
	
	/**
	 * 构建统一的银行卡数据
	 * 
	 * @param defaultBankCardData
	 * @return
	 */
	private CommonData getDefaultBankCardData (CommonData data ,DefaultBankCardData defaultBankCardData) {
		data.putValue("bkcardbank", defaultBankCardData.getBkcardbank()!=null ? defaultBankCardData.getBkcardbank() :"");//付款银行名
		data.putValue("bkCardno", defaultBankCardData.getBkcardno()!=null ?defaultBankCardData.getBkcardno():"");//银行卡号
		data.putValue("bkcardman", defaultBankCardData.getBkcardbankman() !=null ? defaultBankCardData.getBkcardbankman() :"");//执卡人
		data.putValue("bkcardexpireMonth", defaultBankCardData.getBkcardyxmonth() !=null ? defaultBankCardData.getBkcardyxmonth() :"");//月份
		data.putValue("bkcardmanidcard", defaultBankCardData.getBkcardidcard() !=null ? defaultBankCardData.getBkcardidcard():"");//执卡人身份证
		data.putValue("bankid", defaultBankCardData.getBkcardbankid() !=null ? defaultBankCardData.getBkcardbankid():"");//银行id
		data.putValue("bkcardexpireYear", defaultBankCardData.getBkcardyxyear() !=null ? defaultBankCardData.getBkcardyxyear():"");//年
		data.putValue("bkcardPhone", defaultBankCardData.getBkcardbankphone() !=null ? defaultBankCardData.getBkcardbankphone():"");//银行预留手机号码
		data.putValue("bkcardcvv", defaultBankCardData.getBkcardcvv() !=null ? defaultBankCardData.getBkcardcvv():"");//cvv
		return data;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		/** 银行卡号 */
		String bankno = "";
		if (resultCode == 78) {// 选择银行
			defaultBankCardData = (DefaultBankCardData) data.getSerializableExtra("bankcard");
			// TODO 填充数据
			bankno = defaultBankCardData.getBkcardno();
			switch (viewHandler.getCheckpay()) {// 1 默认支付 2 信用卡支付 3储蓄卡支付
			case 2:
				viewHandler.setCredit(bankno);
				break;
			case 3:
				viewHandler.setDeposit(bankno);
				break;
			default:
				break;
			}

		} 

	}

	/*******************刷卡器回调***********start*********/
	@Override
	public void recieveCard(CardData data) {
		if ("2".equals(data.trackInfo)) {// 信用卡
			viewHandler.setDefaultPay(2);
			isCreditCardk = true;
		} else if ("23".equals(data.trackInfo)) {// 储蓄卡
			viewHandler.setDefaultPay(2);
			isCreditCardk = false;
		}
		switch (viewHandler.getCheckpay()) {
		case 2:// 信用卡支付
			viewHandler.setCredit(data.pan);
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
		boolean s = PayApp.openReaderNow();
		//		log("status : " + s);
		viewHandler.setCardImageVisibility(flag);
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
			mKeyTask = new SwipKeyTask(getActivity(),
					PayApp.mKeyCode, 
					PayApp.mKeyData, 
					viewHandler.getCredit(), 
					"airticket", asyncLoadWorkListener);
			mKeyTask.execute("");

			break;
		default:
			break;
		}
	}
	/*******************刷卡器回调***********end*********/

	/**
	 * 刷卡后检查是否有保存过卡的回调
	 */
	private AsyncLoadWorkListener asyncLoadWorkListener = new AsyncLoadWorkListener() {
		@Override
		public void onSuccess(Object protocolDataList, Bundle bundle) {
			if(protocolDataList != null) {
				defaultBankCardData = (DefaultBankCardData)protocolDataList;
			}
		}

		@Override
		public void onFailure(String error) {

		}
	};

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
		if(defaultBankCardData.getBkcardbankphone() == null || defaultBankCardData.getBkcardbankphone().equals("")) {
			return false;
		}
		if(defaultBankCardData.getBkcardbankman() == null || defaultBankCardData.getBkcardbankman().equals("")) {
			return false;
		}
		if(defaultBankCardData.getBkcardbankid() == null || defaultBankCardData.getBkcardbankid().equals("")) {
			return false;
		}

		if(defaultBankCardData.getBkcardcardtype() == null || defaultBankCardData.getBkcardcardtype().equals("")) {
			return false;
		}
		return true;
	}

	private String orderId;
	/**
	 * 获取默认银行卡
	 */
	private ResponseStateListener responseStateListener = new ResponseStateListener() {

		@Override
		public void onSuccess(Object obj, Class cla) {
			if (cla.equals(SmsCode.class)) {
				SmsCode sms = (SmsCode) obj;
				if (sms != null) {
					orderId = sms.getOrderId();
					HashMap<String, String> item = new HashMap<String, String>();
					item.put("交易请求号:", orderId);
					TelephonePayActivity.mCommonData.add(item);

					if (sms.isNeed()) {// 需要验证码
						SmsCodeDialog dialog = new SmsCodeDialog();
						dialog.show(getActivity(), smsCodeSubmitListener);
					} else {// 不需要验证码,表示支付成功
						PromptUtil.showToast(getActivity(), sms.getMessage());
						//						gotoPaySuccess();
					}
				}
			} else {
				creditCard = (DefaultBankCardData) obj;
				viewHandler.setDefaultBank(creditCard);
				viewHandler.setDefaultVisibility(View.VISIBLE);
				viewHandler.setDefaultPay(1);
			}

		}
	};
	
	/**
	 * 易宝支付短信SmsCode
	 */
	SmsCode mSmsCode = null;
	
	private SmsCodeDialog dialog;
	
	/**
	 * 信用卡支付-异宝订单交易
	 */
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
					dialog.show(getActivity(), smsCodeSubmitListener);
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
	
	/**
	 * 信用卡支付-短信认证
	 */
	private SmsCodeSubmitListener smsCodeSubmitListener = new SmsCodeSubmitListener() {

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
					"ApiExpresspayInfo", "ybagentorderSMSverify");
			
		}
	};

	@Override
	public void requestBuySuccess(final ResultData resultData, String msg) {
		resultData.putValue(ResultData.bkntno, unionpayOrderNum);
		new BuySuccessTask(getActivity(), resultData,"ApiExpresspayInfo","agentorderPayrqStatus").execute("");
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
                	gotoPaySunccess();
                }else{
//                		getActivity().finish();
                }
               
            }
        });
        builder.create().show();
		
	}
	
	/**
	 * 支付成功页面
	 */
	private void gotoPaySunccess() {
		addFragmentToStack(UIConstantDefault.UI_CONSTANT_BUY_HTBCARD_ORDER_PAY_SUCCESS, 1, data);
	}
	
	/**
	 * 银联交易流水号
	 */
	private String unionpayOrderNum;

	protected static final int SWIPING_START = 1;
	protected static final int SWIPING_FAIL = 2;
	protected static final int SWIPING_SUCCESS = 3;
	protected static final int CMD_KSN=4;//获取到刷卡器设备号
	//	
	protected static final String INSERTCARD="请插入刷卡器";
	protected static final String DECODING = "校验刷卡器中";
	protected static final String ReaderError = "银行卡号写入失败，多次尝试失败请重新插拔刷卡器";
	protected static final String ReaderSUCCESS = "银行卡号写入成功";

}
