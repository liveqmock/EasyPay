package com.inter.trade.ui.fragment.airticket;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.inter.trade.AsyncLoadWork.AsyncLoadWorkListener;
import com.inter.trade.AsyncLoadWork;
import com.inter.trade.R;
import com.inter.trade.SwipKeyTask;
import com.inter.trade.data.AirTicketCreateOrderData;
import com.inter.trade.data.CardData;
import com.inter.trade.data.CommonData;
import com.inter.trade.ui.BankListActivity;
import com.inter.trade.ui.DaikuanActivity;
import com.inter.trade.ui.PayApp;
import com.inter.trade.ui.TelephonePayActivity;
import com.inter.trade.ui.PayApp.SwipListener;
import com.inter.trade.ui.creditcard.MyBankCardActivity;
import com.inter.trade.ui.creditcard.PayWaysHandler;
import com.inter.trade.ui.creditcard.SmsCodeDialog;
import com.inter.trade.ui.creditcard.SmsCodeDialog.SmsCodeSubmitListener;
import com.inter.trade.ui.creditcard.data.DefaultBankCardData;
import com.inter.trade.ui.creditcard.data.SmsCode;
import com.inter.trade.ui.creditcard.task.GetDefaultTask;
import com.inter.trade.ui.creditcard.util.CreditCard;
import com.inter.trade.ui.creditcard.util.CreditcardInfoUtil;
import com.inter.trade.ui.fragment.airticket.util.ApiAirticketCreateOrderParser;
import com.inter.trade.ui.fragment.airticket.util.ApiAirticketGetAirlineData;
import com.inter.trade.ui.fragment.airticket.util.ApiAirticketGetOrderHistoryData;
import com.inter.trade.ui.fragment.airticket.util.ApiAirticketGetOrderHistoryParser;
import com.inter.trade.ui.fragment.airticket.util.PassengerData;
import com.inter.trade.ui.fragment.returndaikuan.util.DaikuanData;
import com.inter.trade.ui.fragment.telephone.util.MoblieRechangeData;
import com.inter.trade.ui.fragment.waterelectricgas.data.ResponseStateListener;
import com.inter.trade.ui.manager.IBaseFragment;
import com.inter.trade.ui.manager.UIManagerActivity;
import com.inter.trade.ui.manager.core.UIConstantDefault;
import com.inter.trade.util.BankCardUtil;
import com.inter.trade.util.Base64Pay;
import com.inter.trade.util.Constants;
import com.inter.trade.util.PromptUtil;
import com.inter.trade.util.UserInfoCheck;

/**
 * 飞机票  清单结算 Fragment
 * @author zhichao.huang
 *
 */
public class AirTicketMainClearingFragment extends IBaseFragment 
implements OnClickListener, SwipListener
{

	private static final String TAG = AirTicketMainClearingFragment.class.getName();

	private View rootView;

	private Button submitButton;

	/**
	 * 去
	 */
	private TextView price, capital, fuel_charge, insure, people_num, total;

	/**
	 * 返
	 */
	private TextView fan_price, fan_capital, fan_fuel_charge, fan_people_num, fan_total, all_total;

	private LinearLayout fan_layout;

	/**
	 * 飞机票总价
	 */
	private int air_qu_total, air_fan_total;

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
	
	/**********************机票所属信息**********************/
	
	/**
	 * 订单提交异步
	 */
	private AsyncLoadWork<String> asyncLoadWork = null;
	
	/**
	 * 订单短信支付提交异步
	 */
	private AsyncLoadWork<ApiAirticketGetOrderHistoryData> asyncSmsPayWork = null;
	
	/**
	 * 乘机人列表
	 */
	private ArrayList<PassengerData> passengerList= null;

	/**
	 * 联系人列表
	 */
	private ArrayList<PassengerData> linkmanList= null;

	public static AirTicketMainClearingFragment newInstance (Bundle data) {
		AirTicketMainClearingFragment fragment = new AirTicketMainClearingFragment();
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
		
		passengerList = ((UIManagerActivity)getActivity()).selectedPassengerList;
		linkmanList = ((UIManagerActivity)getActivity()).selectedLinkmanList;
	}

	@Override
	protected View onInitView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.airticket_clearing_layout, container,false);
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
		((UIManagerActivity)getActivity()).setTopTitle("机票账单");
		
		passengerList = ((UIManagerActivity)getActivity()).selectedPassengerList;
		linkmanList = ((UIManagerActivity)getActivity()).selectedLinkmanList;
	}

	private void initSwipPic(boolean flag) {
		viewHandler.setCardImageVisibility(flag);
	}

	private void initViews (View rootView) {

		//去
		price = (TextView)rootView.findViewById(R.id.price);
		capital = (TextView)rootView.findViewById(R.id.capital);
		fuel_charge = (TextView)rootView.findViewById(R.id.fuel_charge);
		insure = (TextView)rootView.findViewById(R.id.insure);
		people_num = (TextView)rootView.findViewById(R.id.people_num);
		total = (TextView)rootView.findViewById(R.id.total);

		//返
		fan_layout = (LinearLayout)rootView.findViewById(R.id.fan_layout);
		fan_price = (TextView)rootView.findViewById(R.id.fan_price);
		fan_capital = (TextView)rootView.findViewById(R.id.fan_capital);
		fan_fuel_charge = (TextView)rootView.findViewById(R.id.fan_fuel_charge);
		//		insure = (TextView)rootView.findViewById(R.id.insure);
		fan_people_num = (TextView)rootView.findViewById(R.id.fan_people_num);
		fan_total = (TextView)rootView.findViewById(R.id.fan_total);

		all_total = (TextView)rootView.findViewById(R.id.all_total);

		submitButton = (Button)rootView.findViewById(R.id.submit_btn);
		submitButton.setOnClickListener(this);

		//付款
		viewHandler = new PayWaysHandler(getActivity(), this, null, null);
		viewHandler.initView(null, rootView);
		viewHandler.setDefaultPay(2);

		initDatas ();
	}

	//去程
	ApiAirticketGetAirlineData airlineData = null;
	//返程
	ApiAirticketGetAirlineData airlineFanData = null;
	
	private void initDatas () {
		if(data == null) return;
		int people = 0;

//		//去程
//		ApiAirticketGetAirlineData airlineData = null;
		Serializable serializable = data.getSerializable("AirlineDetail");
		if(serializable != null && serializable instanceof ApiAirticketGetAirlineData) {
			airlineData = (ApiAirticketGetAirlineData)serializable;
		}

		if(airlineData != null) {
			//以下计算都得跟据登机人来计算
			price.setText("￥"+airlineData.price);
			capital.setText("￥"+airlineData.tax);
			fuel_charge.setText("￥"+airlineData.oilFee);

			people = data.getInt("people_num");
			people_num.setText(people+"");

//			air_qu_total = (Integer.parseInt(airlineData.price) + Integer.parseInt(airlineData.tax) + Integer.parseInt(airlineData.oilFee))*people;
			air_qu_total = computeQuchengTotalPrice ();//去程订单金额
			data.putString("qu_amout", air_qu_total +"");//去程订单金额
			total.setText("￥"+air_qu_total);
		}

//		//返程
//		ApiAirticketGetAirlineData airlineFanData = null;
		Serializable serializable2 = data.getSerializable("AirlineDetailFan");
		if(serializable2 != null && serializable2 instanceof ApiAirticketGetAirlineData) {
			airlineFanData = (ApiAirticketGetAirlineData)serializable2;
		}

		if(airlineFanData != null) {

			fan_layout.setVisibility(View.VISIBLE);

			fan_price.setText("￥"+airlineFanData.price);
			fan_capital.setText("￥"+airlineFanData.tax);
			fan_fuel_charge.setText("￥"+airlineFanData.oilFee);

			people = data.getInt("people_num");
			fan_people_num.setText(people+"");

//			air_fan_total = (Integer.parseInt(airlineFanData.price) + Integer.parseInt(airlineFanData.tax) + Integer.parseInt(airlineFanData.oilFee))*people;
			air_fan_total = computeFanchengTotalPrice();//返程订单金额
			data.putString("fan_amout", air_fan_total +"");//返程订单金额
			fan_total.setText("￥"+air_fan_total);

			all_total.setText("￥"+(air_qu_total + air_fan_total));//往返金额合计

		}


	}
	
	int quPrice = 0;//去程总机票价
	int quTax = 0;//去程总税
	int quOilFee = 0;//去程总燃油费
	
	int fanPrice = 0;//返程总机票价
	int fanTax = 0;//返程总税
	int fanOilFee = 0;//返程总燃油费
	
	/**
	 * 计算去程机票总价
	 */
	private int computeQuchengTotalPrice () {
		int airTotalPrice = 0;
		if(passengerList != null && passengerList.size()> 0) {
			
			for(PassengerData passengerData : passengerList) {
				//乘客类型：默认值1代表成人，2代表儿童，3代表婴儿
				String passengerType = passengerData.getPassengerType();
				if(passengerType.equals("1")) {//成人
					airTotalPrice += (Integer.parseInt(airlineData.price) + Integer.parseInt(airlineData.tax) + Integer.parseInt(airlineData.oilFee));
					quPrice += Integer.parseInt(airlineData.price);
					quTax += Integer.parseInt(airlineData.tax);
					quOilFee += Integer.parseInt(airlineData.oilFee);
					
				} else if(passengerType.equals("2")) {//儿童
					airTotalPrice += (Integer.parseInt(airlineData.standardPriceForChild) + Integer.parseInt(airlineData.taxForChild) + Integer.parseInt(airlineData.oilFeeForChild));
					quPrice += Integer.parseInt(airlineData.standardPriceForChild);
					quTax += Integer.parseInt(airlineData.taxForChild);
					quOilFee += Integer.parseInt(airlineData.oilFeeForChild);
					
				} else if(passengerType.equals("3")) {//婴儿
					airTotalPrice += (Integer.parseInt(airlineData.standardPriceForBaby) + Integer.parseInt(airlineData.taxForBaby) + Integer.parseInt(airlineData.oilFeeForBaby));
					quPrice += Integer.parseInt(airlineData.standardPriceForBaby);
					quTax += Integer.parseInt(airlineData.taxForBaby);
					quOilFee += Integer.parseInt(airlineData.oilFeeForBaby);
				} else {
					airTotalPrice += (Integer.parseInt(airlineData.price) + Integer.parseInt(airlineData.tax) + Integer.parseInt(airlineData.oilFee));
					quPrice += Integer.parseInt(airlineData.price);
					quTax += Integer.parseInt(airlineData.tax);
					quOilFee += Integer.parseInt(airlineData.oilFee);
				}
			}
			
			setQuAirInfo ();
			
		}
		return airTotalPrice;
	}
	
	/**
	 * 设置去程机票价格信息
	 */
	private void setQuAirInfo () {
		price.setText("￥"+quPrice);
		capital.setText("￥"+quTax);
		fuel_charge.setText("￥"+quOilFee);
	}
	
	/**
	 * 设置返程机票价格信息
	 */
	private void setFanAirInfo () {
		fan_price.setText("￥"+fanPrice);
		fan_capital.setText("￥"+fanTax);
		fan_fuel_charge.setText("￥"+fanOilFee);
	}
	
	/**
	 * 计算返程机票总价
	 */
	private int computeFanchengTotalPrice () {
		int airTotalPrice = 0;
		if(passengerList != null && passengerList.size()> 0) {
			
			for(PassengerData passengerData : passengerList) {
				//乘客类型：默认值1代表成人，2代表儿童，3代表婴儿
				String passengerType = passengerData.getPassengerType();
				if(passengerType.equals("1")) {//成人
					airTotalPrice += (Integer.parseInt(airlineFanData.price) + Integer.parseInt(airlineFanData.tax) + Integer.parseInt(airlineFanData.oilFee));
					fanPrice += Integer.parseInt(airlineFanData.price);
					fanTax += Integer.parseInt(airlineFanData.tax);
					fanOilFee += Integer.parseInt(airlineFanData.oilFee);
					
				} else if(passengerType.equals("2")) {//儿童
					airTotalPrice += (Integer.parseInt(airlineFanData.standardPriceForChild) + Integer.parseInt(airlineFanData.taxForChild) + Integer.parseInt(airlineFanData.oilFeeForChild));
					fanPrice += Integer.parseInt(airlineFanData.standardPriceForChild);
					fanTax += Integer.parseInt(airlineFanData.taxForChild);
					fanOilFee += Integer.parseInt(airlineFanData.oilFeeForChild);
					
				} else if(passengerType.equals("3")) {//婴儿
					airTotalPrice += (Integer.parseInt(airlineFanData.standardPriceForBaby) + Integer.parseInt(airlineFanData.taxForBaby) + Integer.parseInt(airlineFanData.oilFeeForBaby));
					fanPrice += Integer.parseInt(airlineFanData.standardPriceForBaby);
					fanTax += Integer.parseInt(airlineFanData.taxForBaby);
					fanOilFee += Integer.parseInt(airlineFanData.oilFeeForBaby);
					
				} else {
					airTotalPrice += (Integer.parseInt(airlineFanData.price) + Integer.parseInt(airlineFanData.tax) + Integer.parseInt(airlineFanData.oilFee));
					fanPrice += Integer.parseInt(airlineFanData.price);
					fanTax += Integer.parseInt(airlineFanData.tax);
					fanOilFee += Integer.parseInt(airlineFanData.oilFee);
				}
			}
			
			setFanAirInfo ();
			
		}
		return airTotalPrice;
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
						gotoPay(creditCard);
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
									gotoPay(defaultBankCardData);
								} 
								else {//储蓄卡
									PromptUtil.showToast(getActivity(), "暂不支持储蓄卡支付");
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
	private void checkBankCardType (String bankno) {
		if(BankCardUtil.isCreditCard(bankno)){
			data.putInt("pay_key", CreditCard.AIRTICKET);//支付业务类型
			data.putString("bankno", bankno);//卡号
			data.putString("paymonery", (air_qu_total + air_fan_total)+"");//支付金额
			data.putSerializable("orderData",  getNoCardOrderData ());
			addFragmentToStack(UIConstantDefault.UI_CONSTANT_CREDITCARD_PAY, 1, data);
		} else {
			PromptUtil.showToast(getActivity(), "暂不支持储蓄卡支付");
		}
	}
	
	/**
	 * 直接支付
	 */
	private void gotoPay(DefaultBankCardData defaultBankCardData ) {
		runAsyncTask (defaultBankCardData);
	}
	
	/**
	 * 订单提交异步
	 */
	private void runAsyncTask (DefaultBankCardData defaultBankCardData) {
		//创建机票订单解析
		ApiAirticketCreateOrderParser authorRegParser = new ApiAirticketCreateOrderParser();

		asyncLoadWork = new AsyncLoadWork<String>(getActivity(), authorRegParser, buildRequestData(defaultBankCardData), orderAsyncWorkListener, false, true);
		asyncLoadWork.execute("ApiAirticket", "createOrder");
	}
	
	/**
	 * 构建机票订单请求数据
	 */
	private AirTicketCreateOrderData buildRequestData(DefaultBankCardData defaultBankCardData) {
		
		AirTicketCreateOrderData airTicketCreateOrderData = getNoCardOrderData ();
		
		if(defaultBankCardData != null) {
			//支付信息
			airTicketCreateOrderData.payinfoMap.put("cardNumber", defaultBankCardData.getBkcardno()!=null ?defaultBankCardData.getBkcardno():"");//卡号
			airTicketCreateOrderData.payinfoMap.put("bkcardexpireYear", (defaultBankCardData.getBkcardyxyear() !=null ? defaultBankCardData.getBkcardyxyear():""));//有效年份
			airTicketCreateOrderData.payinfoMap.put("bkcardexpireMonth", defaultBankCardData.getBkcardyxmonth() !=null ? CreditcardInfoUtil.transferTwoMonth(defaultBankCardData.getBkcardyxmonth()) :"");//有效月份
			airTicketCreateOrderData.payinfoMap.put("validity", defaultBankCardData.getBkcardyxyear()+CreditcardInfoUtil.transferTwoMonth(defaultBankCardData.getBkcardyxmonth()));//有效月份年份
			airTicketCreateOrderData.payinfoMap.put("cardCVV2No", defaultBankCardData.getBkcardcvv() !=null ? defaultBankCardData.getBkcardcvv():"");//三位有效码
			airTicketCreateOrderData.payinfoMap.put("cardHolder", defaultBankCardData.getBkcardbankman() !=null ? defaultBankCardData.getBkcardbankman() :"");//持卡人
			airTicketCreateOrderData.payinfoMap.put("cardHolderIdCardType", "1");//持卡人证件类型
			airTicketCreateOrderData.payinfoMap.put("cardHolderIdCardNumber", defaultBankCardData.getBkcardidcard() !=null ? defaultBankCardData.getBkcardidcard():"");//持卡人证件号
			airTicketCreateOrderData.payinfoMap.put("phoneNumber", defaultBankCardData.getBkcardbankphone() !=null ? defaultBankCardData.getBkcardbankphone():"");//持卡人手机号
			airTicketCreateOrderData.payinfoMap.put("bankCct", defaultBankCardData.getCtripbankctt() !=null ? defaultBankCardData.getCtripbankctt():"");//携程CTT
		}

		return airTicketCreateOrderData;
	}
	
	/**
	 * 获取没有银行卡信息的订单数据
	 * 
	 * @return
	 */
	private AirTicketCreateOrderData getNoCardOrderData () {
		
		AirTicketCreateOrderData airTicketCreateOrderData = new AirTicketCreateOrderData();
		
		airTicketCreateOrderData.putValue("amount", (air_qu_total + air_fan_total)+"");//总金额

		//机票信息
		ApiAirticketGetAirlineData airlineData = null;//去程航班信息,如果内字段airticketFanchengAirlineData 不为空，说明存在返程信息
		ApiAirticketGetAirlineData airlineFanData = null;//返程航班信息

		ApiAirticketGetAirlineData airlineDetail = null;//去程详情信息
		ApiAirticketGetAirlineData airlineDetailFan = null;//返程详情信息

		airlineData = (ApiAirticketGetAirlineData)data.getSerializable("AirlineData");
		airlineFanData = airlineData.airticketFanchengAirlineData;

		airlineDetail = (ApiAirticketGetAirlineData)data.getSerializable("AirlineDetail");

		if(airlineData != null) {

			if(airlineFanData != null) {//是往返机票

				airlineDetailFan = (ApiAirticketGetAirlineData)data.getSerializable("AirlineDetailFan");

				//票ID，用逗号隔开
				String ticketID = "";
				
				/**
				 * 去程信息
				 */
				HashMap<String, String> quTicket = new HashMap<String, String>();
				if (airlineDetail != null) {
//					quTicket.put("ticketId", airlineDetail.id != null ? airlineDetail.id :"");//航班ID，用于生成订单
					ticketID += airlineDetail.id +",";
				}
				
				
//				quTicket.put("amout", data.getString("qu_amout") != null ? data.getString("qu_amout") :"");//去程-订单金额
//				quTicket.put("departCityId", data.getString("departCityId") != null ? data.getString("departCityId") :"");//出发城市ID
//				quTicket.put("arriveCityId", data.getString("arriveCityId") != null ? data.getString("arriveCityId") :"");//到达城市ID
//				quTicket.put("departPortCode", airlineData.dPortCode != null ? airlineData.dPortCode : "");//出发机场三字码
//				quTicket.put("arrivePortCode", airlineData.aPortCode != null ? airlineData.aPortCode : "");//到达机场三字码
//				quTicket.put("airlineCode", airlineData.airLineCode != null ? airlineData.airLineCode : "");//航空公司代码
//				quTicket.put("flight", airlineData.flight != null ? airlineData.flight : "");//航班号
//
//				if (airlineDetail != null) {
//					quTicket.put("class", airlineDetail.aClass != null ? airlineDetail.aClass :"");//舱位等级,Y 经济舱，C 公务舱，F 头等舱
//					quTicket.put("takeOffTime", airlineData.takeOffTime != null ? airlineData.takeOffTime :"");//起飞时间,时间格式：yyyy-MM-ddThh:mm:ss；比如2013-05-20T07:55:00
//					quTicket.put("arriveTime", airlineData.arriveTime != null ? airlineData.arriveTime :"");//到达时间 同上
//					quTicket.put("rate", "");//航班折扣率
//					quTicket.put("price", airlineDetail.price != null ? airlineDetail.price :"");//航班票价
//					quTicket.put("tax", airlineDetail.tax != null ? airlineDetail.tax :"");//税
//					quTicket.put("oilFee", airlineDetail.oilFee != null ? airlineDetail.oilFee :"");//燃油费
//				}
//				airTicketCreateOrderData.ticketListMap.add(quTicket);


				/**
				 * 返程信息
				 */
				
//				HashMap<String, String> fanTicket = new HashMap<String, String>();
				if (airlineDetailFan != null) {
//					fanTicket.put("ticketId", airlineDetailFan.id != null ? airlineDetailFan.id :"");//航班ID，用于生成订单
					ticketID += airlineDetailFan.id ;
				}
				
//				fanTicket.put("amout", data.getString("fan_amout") != null ? data.getString("fan_amout") :"");//返程-订单金额
//				fanTicket.put("departCityId", data.getString("arriveCityId") != null ? data.getString("arriveCityId") :"");//出发城市ID,返程的出发城市是去程的到达城市
//				fanTicket.put("arriveCityId", data.getString("departCityId") != null ? data.getString("departCityId") :"");//到达城市ID，如上反之
//				fanTicket.put("departPortCode", airlineFanData.dPortCode != null ? airlineFanData.dPortCode : "");//出发机场三字码
//				fanTicket.put("arrivePortCode", airlineFanData.aPortCode != null ? airlineFanData.aPortCode : "");//到达机场三字码
//				fanTicket.put("airlineCode", airlineFanData.airLineCode != null ? airlineFanData.airLineCode : "");//航空公司代码
//				fanTicket.put("flight", airlineFanData.flight != null ? airlineFanData.flight : "");//航班号
//
//				if (airlineDetailFan != null) {
//					fanTicket.put("class", airlineDetailFan.aClass != null ? airlineDetailFan.aClass :"");//舱位等级,Y 经济舱，C 公务舱，F 头等舱
//					fanTicket.put("takeOffTime", airlineFanData.takeOffTime != null ? airlineFanData.takeOffTime :"");//起飞时间,时间格式：yyyy-MM-ddThh:mm:ss；比如2013-05-20T07:55:00
//					fanTicket.put("arriveTime", airlineFanData.arriveTime != null ? airlineFanData.arriveTime :"");//到达时间 同上
//					fanTicket.put("rate", "");//航班折扣率
//					fanTicket.put("price", airlineDetailFan.price != null ? airlineDetailFan.price :"");//航班票价
//					fanTicket.put("tax", airlineDetailFan.tax != null ? airlineDetailFan.tax :"");//税
//					fanTicket.put("oilFee", airlineDetailFan.oilFee != null ? airlineDetailFan.oilFee :"");//燃油费
//				}
				
				quTicket.put("ticketId", ticketID);//航班ID，用于生成订单
				airTicketCreateOrderData.ticketListMap.add(quTicket);


			} else { //单程

				/**
				 * 去程信息
				 */
				HashMap<String, String> quTicket = new HashMap<String, String>();
				if (airlineDetail != null) {
					quTicket.put("ticketId", airlineDetail.id != null ? airlineDetail.id :"");//航班ID，用于生成订单
				}
				
//				quTicket.put("amout", data.getString("qu_amout") != null ? data.getString("qu_amout") :"");//去程-订单金额
//				quTicket.put("departCityId", data.getString("departCityId") != null ? data.getString("departCityId") :"");//出发城市ID
//				quTicket.put("arriveCityId", data.getString("arriveCityId") != null ? data.getString("arriveCityId") :"");//到达城市ID
//				quTicket.put("departPortCode", airlineData.dPortCode != null ? airlineData.dPortCode : "");//出发机场三字码
//				quTicket.put("arrivePortCode", airlineData.aPortCode != null ? airlineData.aPortCode : "");//到达机场三字码
//				quTicket.put("airlineCode", airlineData.airLineCode != null ? airlineData.airLineCode : "");//航空公司代码
//				quTicket.put("flight", airlineData.flight != null ? airlineData.flight : "");//航班号
//
//				if (airlineDetail != null) {
//					quTicket.put("class", airlineDetail.aClass != null ? airlineDetail.aClass :"");//舱位等级,Y 经济舱，C 公务舱，F 头等舱
//					quTicket.put("takeOffTime", airlineData.takeOffTime != null ? airlineData.takeOffTime :"");//起飞时间,时间格式：yyyy-MM-ddThh:mm:ss；比如2013-05-20T07:55:00
//					quTicket.put("arriveTime", airlineData.arriveTime != null ? airlineData.arriveTime :"");//到达时间 同上
//					quTicket.put("rate", "");//航班折扣率
//					quTicket.put("price", airlineDetail.price != null ? airlineDetail.price :"");//航班票价
//					quTicket.put("tax", airlineDetail.tax != null ? airlineDetail.tax :"");//税
//					quTicket.put("oilFee", airlineDetail.oilFee != null ? airlineDetail.oilFee :"");//燃油费
//				}
				airTicketCreateOrderData.ticketListMap.add(quTicket);

			}

		}

		//乘机人信息
		if(passengerList != null && passengerList.size()> 0) {
			
			HashMap<String, String> passenger = new HashMap<String, String>();
			String passengerID = "";
			//乘机人信息
			for(int i = 0; i < passengerList.size(); i ++) {
				
				PassengerData passengerData = passengerList.get(i);
//
//				passenger.put("passengerId", passengerData.getId() != null ? passengerData.getId() :"");//乘机人ID，对应后台数据库的ID
				
				//乘机人ID，对应后台数据库的ID
				passengerID += passengerData.getId() + ",";
				
//				passenger.put("passengerId", passengerData.getPassengerType() != null ? passengerData.getPassengerType() :"1");//乘机人类型,ADU（1：成人），CHI:（2：儿童）， BAB:（3：婴儿）
//				passenger.put("name", passengerData.getName() != null ? passengerData.getName() :"");//乘机人姓名
//				passenger.put("birthDay", passengerData.getBirthDay()!= null ? passengerData.getBirthDay() :"");//乘机人出生日期,形如：1984-01-01
//				passenger.put("passportTypeId", passengerData.getIdtype()!= null ? passengerData.getIdtype() :"");//证件类型ID,1身份证，2护照，4军人证，7回乡证，8台胞证，10港澳通行证，11国际海员证，20外国人永久居留证，25户口簿，27出生证明，99其它
//				passenger.put("passportNo", passengerData.getPassportNo()!= null ? passengerData.getPassportNo() :"");//证件号码,
//				passenger.put("gender", "");//乘机人性别
//				passenger.put("telephone", passengerData.getPhoneNumber()!= null ? passengerData.getPhoneNumber() :"");//乘机人联系电话

//				airTicketCreateOrderData.passengerList.add(passenger);
			}
			
			passenger.put("passengerId", passengerID.substring(0, passengerID.length()-1));
			airTicketCreateOrderData.passengerList.add(passenger);
			
			
			
//			//乘机人信息
//			for(int i = 0; i < passengerList.size(); i ++) {
//				HashMap<String, String> passenger = new HashMap<String, String>();
//				PassengerData passengerData = passengerList.get(i);
//
//				passenger.put("passengerId", passengerData.getId() != null ? passengerData.getId() :"");//乘机人ID，对应后台数据库的ID
//				
////				passenger.put("passengerId", passengerData.getPassengerType() != null ? passengerData.getPassengerType() :"1");//乘机人类型,ADU（1：成人），CHI:（2：儿童）， BAB:（3：婴儿）
////				passenger.put("name", passengerData.getName() != null ? passengerData.getName() :"");//乘机人姓名
////				passenger.put("birthDay", passengerData.getBirthDay()!= null ? passengerData.getBirthDay() :"");//乘机人出生日期,形如：1984-01-01
////				passenger.put("passportTypeId", passengerData.getIdtype()!= null ? passengerData.getIdtype() :"");//证件类型ID,1身份证，2护照，4军人证，7回乡证，8台胞证，10港澳通行证，11国际海员证，20外国人永久居留证，25户口簿，27出生证明，99其它
////				passenger.put("passportNo", passengerData.getPassportNo()!= null ? passengerData.getPassportNo() :"");//证件号码,
////				passenger.put("gender", "");//乘机人性别
////				passenger.put("telephone", passengerData.getPhoneNumber()!= null ? passengerData.getPhoneNumber() :"");//乘机人联系电话
//
//				airTicketCreateOrderData.passengerList.add(passenger);
//			}

			//联系人信息
			if(linkmanList != null && linkmanList.size() > 0) {
				
				HashMap<String, String> contacter = new HashMap<String, String>();
				String contacterID = "";
				
				for(int i = 0; i < linkmanList.size(); i ++) {
//					HashMap<String, String> contacter = new HashMap<String, String>();
					PassengerData passengerData = linkmanList.get(i);

//					contacter.put("contacterId", passengerData.getId() != null ? passengerData.getId() :"");//联系人ID，对应后台数据库的ID
					contacterID += passengerData.getId() + "," ;
//					passenger.put("passengerId", "");//乘机人类型,ADU（成人），CHI（儿童）， BAB（婴儿）
//					passenger.put("name", passengerData.getName() != null ? passengerData.getName() :"");//乘机人姓名
//					passenger.put("birthDay", "");//乘机人出生日期,形如：1984-01-01
//					passenger.put("passportTypeId", "");//证件类型ID,1身份证，2护照，4军人证，7回乡证，8台胞证，10港澳通行证，11国际海员证，20外国人永久居留证，25户口簿，27出生证明，99其它
//					passenger.put("passportNo", "");//证件号码,
//					passenger.put("gender", "");//乘机人性别
//					passenger.put("telephone", passengerData.getPhoneNumber()!= null ? passengerData.getPhoneNumber() :"");//乘机人联系电话

//					airTicketCreateOrderData.contacterList.add(contacter);
				}
				contacter.put("contacterId", contacterID.substring(0, contacterID.length()-1));//联系人ID，对应后台数据库的ID
				airTicketCreateOrderData.contacterList.add(contacter);
			}
		}
		return airTicketCreateOrderData;
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
					"airplane", asyncLoadWorkListener);
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
//			if(defaultBankCardData != null) {
//				//自动填充付款信息
//				if(checkDefaultBankCardData()) {
//					//					autoFullDatas();
//				}
//
//			}
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
	
	private SmsCodeSubmitListener smsCodeSubmitListener = new SmsCodeSubmitListener() {

		@Override
		public void onPositive(String code, SmsCodeDialog dialog) {
			
		}
	};

	ArrayList<String> protocolList = null;
	/**
	 * 订单编号
	 */
	private String orderNum = null;
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
				data.putString("orderId", orderNum);
				String message = bundle.getString("message");
				showSuccessDialog(message);
//				addFragmentToStack(UIConstantDefault.UI_CONSTANT_AIR_TICKET_ORDER_PAY_SUCCESS, 1, data);
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
				addFragmentToStack(UIConstantDefault.UI_CONSTANT_AIR_TICKET_ORDER_PAY_SUCCESS, 1, data);
			}
		});
		builder.show();
	}
	
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
