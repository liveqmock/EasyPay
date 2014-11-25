package com.inter.trade.ui.fragment.airticket;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.inter.trade.AsyncLoadWork;
import com.inter.trade.AsyncLoadWork.AsyncLoadWorkListener;
import com.inter.trade.R;
import com.inter.trade.data.AirTicketCreateOrderData;
import com.inter.trade.ui.fragment.airticket.util.ApiAirticketCreateOrderParser;
import com.inter.trade.ui.fragment.airticket.util.ApiAirticketGetAirlineData;
import com.inter.trade.ui.fragment.airticket.util.PassengerData;
import com.inter.trade.ui.fragment.gamerecharge.dialog.FavoriteCharacterDialogFragment;
import com.inter.trade.ui.fragment.gamerecharge.dialog.IFavoriteCharacterDialogListener;
import com.inter.trade.ui.manager.IBaseFragment;
import com.inter.trade.ui.manager.UIManagerActivity;
import com.inter.trade.ui.manager.core.UIConstantDefault;
import com.inter.trade.util.PromptUtil;
import com.inter.trade.util.UserInfoCheck;
import com.inter.trade.view.popmenucompat.PopupMenuCompat;
import com.inter.trade.view.popmenucompat.PopupMenuCompat.OnMenuItemClickListener;

/**
 * 飞机票  信用卡支付 Fragment
 * @author zhichao.huang
 *
 */
public class AirTicketMainCreditCardFragment extends IBaseFragment 
implements OnClickListener, OnMenuItemClickListener, AsyncLoadWorkListener
,IFavoriteCharacterDialogListener{

	private static final String TAG = AirTicketMainCreditCardFragment.class.getName();

	private View rootView;

	private Button submitButton;

	/**
	 * 卡号
	 */
	private EditText card;
	/**
	 * 有限期
	 */
	private EditText deadline; 
	/**
	 * 验证码
	 */
	private EditText auth_code; 
	/**
	 * 持卡人姓名
	 */
	private EditText card_name;
	/**
	 * 证件类型
	 */
	private TextView papers_type_text;
	/**
	 * 证件号码
	 */
	private EditText papers_number;
	
	/**
	 * 有限期 月、年
	 */
	private Button btnMonth, btnYear;

	RelativeLayout papers_type;

	/**
	 * 卡号
	 */
	private String card_str;

	/**
	 * 有限期
	 */
	private String deadline_str;
	/**
	 * 验证码
	 */
	private String auth_code_str;
	/**
	 * 持卡人姓名
	 */
	private String card_name_str;
	/**
	 * 证件类型
	 */
	private String papers_type_text_str;
	/**
	 * 证件号码
	 */
	private String papers_number_str;

	private Bundle data = null;

	/**
	 * 乘机人列表
	 */
	private ArrayList<PassengerData> passengerList= null;

	/**
	 * 联系人列表
	 */
	private ArrayList<PassengerData> linkmanList= null;

	private AsyncLoadWork<String> asyncLoadWork = null;

	public static AirTicketMainCreditCardFragment newInstance (Bundle data) {
		AirTicketMainCreditCardFragment fragment = new AirTicketMainCreditCardFragment();
		fragment.setArguments(data);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "onCreate");
		getActivity().setTheme(R.style.DialogStyleLight);
		Bundle bundle = getArguments();
		if(bundle != null) {
			data = bundle;
		}
	}

	@Override
	protected View onInitView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.airticket_credit_card_pay_layout, container,false);
		initViews(rootView);
		return rootView;
	}

	@Override
	protected void onAsyncLoadData() {

	}

	@Override
	public void onRefreshDatas() {
		((UIManagerActivity)getActivity()).setTopTitle("信用卡支付");

		passengerList = ((UIManagerActivity)getActivity()).selectedPassengerList;
		linkmanList = ((UIManagerActivity)getActivity()).selectedLinkmanList;
	}

	private void initViews (View rootView) {

		card = (EditText)rootView.findViewById(R.id.card);
		deadline = (EditText)rootView.findViewById(R.id.deadline);
		auth_code = (EditText)rootView.findViewById(R.id.auth_code);
		card_name = (EditText)rootView.findViewById(R.id.card_name);
		papers_type_text = (TextView)rootView.findViewById(R.id.papers_type_text);
		papers_number = (EditText)rootView.findViewById(R.id.papers_number);
		
		btnMonth = (Button) rootView.findViewById(R.id.btn_month);
		btnYear = (Button) rootView.findViewById(R.id.btn_year);

		papers_type = (RelativeLayout)rootView.findViewById(R.id.papers_type);
		papers_type.setOnClickListener(this);


		submitButton = (Button)rootView.findViewById(R.id.submit_btn);
		submitButton.setOnClickListener(this);
		
		btnMonth.setOnClickListener(this);
		btnYear.setOnClickListener(this);

	}

	private boolean checkInfos() {

		card_str = card.getText().toString();
		if(card_str == null || card_str.equals("")) {
			PromptUtil.showToast(getActivity(), "请输入银行卡卡号");
			return false;
		}
		if(!UserInfoCheck.checkBankCard(card_str)){
			PromptUtil.showToast(getActivity(), "您输入的银行卡卡号有误");
			return false;
		}

//		deadline_str = deadline.getText().toString();
//		if(deadline_str == null || deadline_str.equals("")) {
//			PromptUtil.showToast(getActivity(), "请输入有限期");
//			return false;
//		}
		String month = btnMonth.getText().toString();
		String year = btnYear.getText().toString();
		
		if(month.equals("月")) {
			PromptUtil.showToast(getActivity(), "请选择月份");
			return false;
		}
		
		if(year.equals("年")) {
			PromptUtil.showToast(getActivity(), "请选择年份");
			return false;
		}
		deadline_str = month+"-"+year;

		auth_code_str = auth_code.getText().toString();
		if(auth_code_str == null || auth_code_str.equals("") ||auth_code_str.length() !=3) {
			PromptUtil.showToast(getActivity(), "请输入卡号后3位数");
			return false;
		}

		card_name_str = card_name.getText().toString();
		if(card_name_str == null || card_name_str.equals("")) {
			PromptUtil.showToast(getActivity(), "请输入持卡人姓名");
			return false;
		}

		papers_type_text_str = papers_type_text.getText().toString();
		if(papers_type_text_str == null || papers_type_text_str.equals("")) {
			PromptUtil.showToast(getActivity(), "请选择证件类型");
			return false;
		}

		papers_number_str = papers_number.getText().toString();
		if(papers_number_str == null || papers_number_str.equals("")) {
			PromptUtil.showToast(getActivity(), "请选择证件号码");
			return false;
		}

		//检查是否选择了联系人，登机人
		if(passengerList == null || passengerList.size() <= 0) {
			PromptUtil.showToast(getActivity(), "请选择乘机人");
			return false;
		}
		if(linkmanList == null || linkmanList.size() <= 0) {
			PromptUtil.showToast(getActivity(), "请选择联系人");
			return false;
		}

		return true;
	}

	/**
	 * 构建请求数据
	 */
	private AirTicketCreateOrderData buildRequestData() {
//		ApiAirticketGetAirlineData airticketGetAirlineData = null;

		AirTicketCreateOrderData airTicketCreateOrderData = new AirTicketCreateOrderData();
		
		
		
//		airTicketCreateOrderData.ticketMap.put("amout", data.getString("amout") != null ? data.getString("amout") :"");//订单金额
//		airTicketCreateOrderData.ticketMap.put("departCityId", data.getString("departCityId") != null ? data.getString("departCityId") :"");//出发城市ID
//		airTicketCreateOrderData.ticketMap.put("arriveCityId", data.getString("arriveCityId") != null ? data.getString("arriveCityId") :"");//到达城市ID
//		airTicketCreateOrderData.ticketMap.put("departPortCode", "xxx");//出发机场三字码
//		airTicketCreateOrderData.ticketMap.put("arrivePortCode", "yyy");//到达机场三字码
//		airTicketCreateOrderData.ticketMap.put("airlineCode", data.getString("airLineCode")!= null ? data.getString("airLineCode") :"");//航空公司代码
//		airTicketCreateOrderData.ticketMap.put("flight", data.getString("flight")!= null ? data.getString("flight") :"");//航班号
//
//		airticketGetAirlineData = ((ApiAirticketGetAirlineData)data.getSerializable("AirlineDetail"));
//
//		airTicketCreateOrderData.ticketMap.put("class", airticketGetAirlineData.aClass != null ? airticketGetAirlineData.aClass :"");//舱位等级,Y 经济舱，C 公务舱，F 头等舱
//		airTicketCreateOrderData.ticketMap.put("takeOffTime", data.getString("departTime") != null ? data.getString("departTime") :"");//起飞时间,时间格式：yyyy-MM-ddThh:mm:ss；比如2013-05-20T07:55:00
//		airTicketCreateOrderData.ticketMap.put("arriveTime", data.getString("returnTime") != null ? data.getString("returnTime") :"");//到达时间 同上
//		airTicketCreateOrderData.ticketMap.put("rate", "");//航班折扣率
//		airTicketCreateOrderData.ticketMap.put("price", airticketGetAirlineData.price != null ? airticketGetAirlineData.price :"");//航班票价
//		airTicketCreateOrderData.ticketMap.put("tax", airticketGetAirlineData.tax != null ? airticketGetAirlineData.tax :"");//税
//		airTicketCreateOrderData.ticketMap.put("oilFee", airticketGetAirlineData.oilFee != null ? airticketGetAirlineData.oilFee :"");//燃油费
		
		
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
				
				/**
				 * 去程信息
				 */
				HashMap<String, String> quTicket = new HashMap<String, String>();
				quTicket.put("amout", data.getString("qu_amout") != null ? data.getString("qu_amout") :"");//去程-订单金额
				quTicket.put("departCityId", data.getString("departCityId") != null ? data.getString("departCityId") :"");//出发城市ID
				quTicket.put("arriveCityId", data.getString("arriveCityId") != null ? data.getString("arriveCityId") :"");//到达城市ID
				quTicket.put("departPortCode", airlineData.dPortCode != null ? airlineData.dPortCode : "");//出发机场三字码
				quTicket.put("arrivePortCode", airlineData.aPortCode != null ? airlineData.aPortCode : "");//到达机场三字码
				quTicket.put("airlineCode", airlineData.airLineCode != null ? airlineData.airLineCode : "");//航空公司代码
				quTicket.put("flight", airlineData.flight != null ? airlineData.flight : "");//航班号
				
				if (airlineDetail != null) {
					quTicket.put("class", airlineDetail.aClass != null ? airlineDetail.aClass :"");//舱位等级,Y 经济舱，C 公务舱，F 头等舱
					quTicket.put("takeOffTime", airlineData.takeOffTime != null ? airlineData.takeOffTime :"");//起飞时间,时间格式：yyyy-MM-ddThh:mm:ss；比如2013-05-20T07:55:00
					quTicket.put("arriveTime", airlineData.arriveTime != null ? airlineData.arriveTime :"");//到达时间 同上
					quTicket.put("rate", "");//航班折扣率
					quTicket.put("price", airlineDetail.price != null ? airlineDetail.price :"");//航班票价
					quTicket.put("tax", airlineDetail.tax != null ? airlineDetail.tax :"");//税
					quTicket.put("oilFee", airlineDetail.oilFee != null ? airlineDetail.oilFee :"");//燃油费
				}
				airTicketCreateOrderData.ticketListMap.add(quTicket);
				
				
				/**
				 * 返程信息
				 */
				HashMap<String, String> fanTicket = new HashMap<String, String>();
				fanTicket.put("amout", data.getString("fan_amout") != null ? data.getString("fan_amout") :"");//返程-订单金额
				fanTicket.put("departCityId", data.getString("arriveCityId") != null ? data.getString("arriveCityId") :"");//出发城市ID,返程的出发城市是去程的到达城市
				fanTicket.put("arriveCityId", data.getString("departCityId") != null ? data.getString("departCityId") :"");//到达城市ID，如上反之
				fanTicket.put("departPortCode", airlineFanData.dPortCode != null ? airlineFanData.dPortCode : "");//出发机场三字码
				fanTicket.put("arrivePortCode", airlineFanData.aPortCode != null ? airlineFanData.aPortCode : "");//到达机场三字码
				fanTicket.put("airlineCode", airlineFanData.airLineCode != null ? airlineFanData.airLineCode : "");//航空公司代码
				fanTicket.put("flight", airlineFanData.flight != null ? airlineFanData.flight : "");//航班号
				
				if (airlineDetailFan != null) {
					fanTicket.put("class", airlineDetailFan.aClass != null ? airlineDetailFan.aClass :"");//舱位等级,Y 经济舱，C 公务舱，F 头等舱
					fanTicket.put("takeOffTime", airlineFanData.takeOffTime != null ? airlineFanData.takeOffTime :"");//起飞时间,时间格式：yyyy-MM-ddThh:mm:ss；比如2013-05-20T07:55:00
					fanTicket.put("arriveTime", airlineFanData.arriveTime != null ? airlineFanData.arriveTime :"");//到达时间 同上
					fanTicket.put("rate", "");//航班折扣率
					fanTicket.put("price", airlineDetailFan.price != null ? airlineDetailFan.price :"");//航班票价
					fanTicket.put("tax", airlineDetailFan.tax != null ? airlineDetailFan.tax :"");//税
					fanTicket.put("oilFee", airlineDetailFan.oilFee != null ? airlineDetailFan.oilFee :"");//燃油费
				}
				airTicketCreateOrderData.ticketListMap.add(fanTicket);
				
				
			} else { //单程
				
				/**
				 * 去程信息
				 */
				HashMap<String, String> quTicket = new HashMap<String, String>();
				quTicket.put("amout", data.getString("qu_amout") != null ? data.getString("qu_amout") :"");//去程-订单金额
				quTicket.put("departCityId", data.getString("departCityId") != null ? data.getString("departCityId") :"");//出发城市ID
				quTicket.put("arriveCityId", data.getString("arriveCityId") != null ? data.getString("arriveCityId") :"");//到达城市ID
				quTicket.put("departPortCode", airlineData.dPortCode != null ? airlineData.dPortCode : "");//出发机场三字码
				quTicket.put("arrivePortCode", airlineData.aPortCode != null ? airlineData.aPortCode : "");//到达机场三字码
				quTicket.put("airlineCode", airlineData.airLineCode != null ? airlineData.airLineCode : "");//航空公司代码
				quTicket.put("flight", airlineData.flight != null ? airlineData.flight : "");//航班号
				
				if (airlineDetail != null) {
					quTicket.put("class", airlineDetail.aClass != null ? airlineDetail.aClass :"");//舱位等级,Y 经济舱，C 公务舱，F 头等舱
					quTicket.put("takeOffTime", airlineData.takeOffTime != null ? airlineData.takeOffTime :"");//起飞时间,时间格式：yyyy-MM-ddThh:mm:ss；比如2013-05-20T07:55:00
					quTicket.put("arriveTime", airlineData.arriveTime != null ? airlineData.arriveTime :"");//到达时间 同上
					quTicket.put("rate", "");//航班折扣率
					quTicket.put("price", airlineDetail.price != null ? airlineDetail.price :"");//航班票价
					quTicket.put("tax", airlineDetail.tax != null ? airlineDetail.tax :"");//税
					quTicket.put("oilFee", airlineDetail.oilFee != null ? airlineDetail.oilFee :"");//燃油费
				}
				airTicketCreateOrderData.ticketListMap.add(quTicket);
				
			}
			
		}
		
		//乘机人信息
		if(passengerList != null && passengerList.size()> 0) {
			for(int i = 0; i < passengerList.size(); i ++) {
				HashMap<String, String> passenger = new HashMap<String, String>();
				PassengerData passengerData = passengerList.get(i);

				passenger.put("passengerId", "ADU");//乘机人类型,ADU（成人），CHI（儿童）， BAB（婴儿）
				passenger.put("name", passengerData.getName() != null ? passengerData.getName() :"");//乘机人姓名
				passenger.put("birthDay", passengerData.getBirthDay()!= null ? passengerData.getBirthDay() :"");//乘机人出生日期,形如：1984-01-01
				passenger.put("passportTypeId", passengerData.getIdtype()!= null ? passengerData.getIdtype() :"");//证件类型ID,1身份证，2护照，4军人证，7回乡证，8台胞证，10港澳通行证，11国际海员证，20外国人永久居留证，25户口簿，27出生证明，99其它
				passenger.put("passportNo", passengerData.getPassportNo()!= null ? passengerData.getPassportNo() :"");//证件号码,
				passenger.put("gender", "");//乘机人性别
				passenger.put("telephone", passengerData.getPhoneNumber()!= null ? passengerData.getPhoneNumber() :"");//乘机人联系电话

				airTicketCreateOrderData.passengerList.add(passenger);
			}

			//联系人信息
			if(linkmanList != null && linkmanList.size() > 0) {
				for(int i = 0; i < linkmanList.size(); i ++) {
					HashMap<String, String> passenger = new HashMap<String, String>();
					PassengerData passengerData = linkmanList.get(i);

					passenger.put("passengerId", "");//乘机人类型,ADU（成人），CHI（儿童）， BAB（婴儿）
					passenger.put("name", passengerData.getName() != null ? passengerData.getName() :"");//乘机人姓名
					passenger.put("birthDay", "");//乘机人出生日期,形如：1984-01-01
					passenger.put("passportTypeId", "");//证件类型ID,1身份证，2护照，4军人证，7回乡证，8台胞证，10港澳通行证，11国际海员证，20外国人永久居留证，25户口簿，27出生证明，99其它
					passenger.put("passportNo", "");//证件号码,
					passenger.put("gender", "");//乘机人性别
					passenger.put("telephone", passengerData.getPhoneNumber()!= null ? passengerData.getPhoneNumber() :"");//乘机人联系电话

					airTicketCreateOrderData.passengerList.add(passenger);
				}
			}
		}

		//支付信息
		airTicketCreateOrderData.payinfoMap.put("cardNumber", card_str);//卡号
		airTicketCreateOrderData.payinfoMap.put("cardValidity", deadline_str);//有效日期
		airTicketCreateOrderData.payinfoMap.put("cardCVV2No", auth_code_str);//三位有效码
		airTicketCreateOrderData.payinfoMap.put("cardHolder", card_name_str);//持卡人
		airTicketCreateOrderData.payinfoMap.put("cardHolderIdCardType", papers_type_text_str);//持卡人证件类型
		airTicketCreateOrderData.payinfoMap.put("cardHolderIdCardNumber", papers_number_str);//持卡人证件号

		return airTicketCreateOrderData;
	}

	/**
	 * 请求
	 */
	private void runAsyncTask () {
		//创建机票订单解析
		ApiAirticketCreateOrderParser authorRegParser = new ApiAirticketCreateOrderParser();

		asyncLoadWork = new AsyncLoadWork<String>(getActivity(), authorRegParser, buildRequestData(), this, false, true);
		asyncLoadWork.execute("ApiAirticket", "createOrder");
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.submit_btn:
			if(data != null && checkInfos()) {
				runAsyncTask ();
			}

			//			((UIManagerActivity)getActivity()).testData = "15812345678";
			//			((UIManagerActivity)getActivity()).removeFragmentToStack();
			break;
		case R.id.papers_type_text:
		case R.id.papers_type:
			PopupMenuCompat popup = PopupMenuCompat.newInstance(getActivity(),
					v);
			popup.inflate(R.menu.tpye_ids);
			popup.setOnMenuItemClickListener(this);
			popup.show();
			break;
			
		case R.id.btn_month:// 月份有效期
			choosetype = 1;
			FavoriteCharacterDialogFragment.show(this, getActivity(), "选择月份",
					getResources().getStringArray(R.array.months));
			break;
		case R.id.btn_year:// 年份有效期
			choosetype = 2;
			FavoriteCharacterDialogFragment.show(this, getActivity(), "选择年份",
					getResources().getStringArray(R.array.years));
			break;
			
		default:
			break;
		}

	}

	ArrayList<String> protocolList = null;

	@Override
	public void onSuccess(Object protocolDataList, Bundle bundle) {
		protocolList = (ArrayList<String>)protocolDataList;
		data.putString("orderId", protocolList.get(0));
//		backHomeFragment();
		addFragmentToStack(UIConstantDefault.UI_CONSTANT_AIR_TICKET_ORDER_PAY_SUCCESS, 1, data);
	}

	@Override
	public void onFailure(String error) {

	}

	@Override
	public boolean onMenuItemClick(MenuItem item) {
		papers_type_text.setText(item.getTitle().toString());
		return false;
	}
	

	@Override
	public void onListItemSelected(String value, int number) {
		switch (choosetype) {
		case 1:
			btnMonth.setText(value);
			break;
		case 2:
			btnYear.setText(value);
			break;

		default:
			break;
		}		
	}

	private int choosetype = 1;// 1是月份 2是年份 3是银行 4 是证件类型
	
	@Override
	public void onTimeout() {
		// TODO Auto-generated method stub
		
	}
}
