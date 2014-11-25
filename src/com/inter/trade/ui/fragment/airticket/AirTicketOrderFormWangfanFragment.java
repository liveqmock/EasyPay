package com.inter.trade.ui.fragment.airticket;

import java.io.Serializable;
import java.util.ArrayList;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

import com.inter.trade.R;
import com.inter.trade.adapter.AirTicketQueryLineAdapter;
import com.inter.trade.ui.fragment.BaseFragment;
import com.inter.trade.ui.fragment.airticket.adapter.OrderLinkmanAdapter;
import com.inter.trade.ui.fragment.airticket.adapter.OrderPassengerAdapter;
import com.inter.trade.ui.fragment.airticket.adapter.OrderPassengerAdapter.ListViewButtonListener;
import com.inter.trade.ui.fragment.airticket.util.ApiAirticketGetAirlineData;
import com.inter.trade.ui.fragment.airticket.util.PassengerData;
import com.inter.trade.ui.manager.IBaseFragment;
import com.inter.trade.ui.manager.UIManagerActivity;
import com.inter.trade.ui.manager.core.UIConstantDefault;
import com.inter.trade.ui.views.IListView;
import com.inter.trade.util.PromptUtil;

/**
 * 飞机票   往返订单  Fragment
 * @author zhichao.huang
 *
 */
public class AirTicketOrderFormWangfanFragment extends IBaseFragment 
implements OnClickListener{

	private static final String TAG = AirTicketOrderFormWangfanFragment.class.getName();

	private View rootView;

	private Button submitButton;

	private Bundle data = null;

	/**
	 * 登机人列表，联系人列表
	 */
	private IListView djr_list, linkman_list;

	//去
	private TextView start_city, end_city,
	start_date, start_time, start_airport, 
	end_date, end_time, end_airport;
	
	//返
	private TextView fan_start_city, fan_end_city,
	fan_start_date, fan_start_time, fan_start_airport, 
	fan_end_date, fan_end_time, fan_end_airport;
	
	//返程，航空公司，航班号，舱位等级
	private TextView fan_qucheng, fan_aviation_company, fan_flight, fan_air_class;

	private TextView add_djr;//添加登记人
	private TextView add_linkman;//添加聯繫人

	//去程，航空公司，航班号，舱位等级
	private TextView qucheng, aviation_company, flight, air_class;

	//票价，基建，燃油
	private TextView price, jijian, fuel;

	/**
	 * 乘机人列表
	 */
	private ArrayList<PassengerData> passengerList= null;

	/**
	 * 联系人列表
	 */
	private ArrayList<PassengerData> linkmanList= null;
	
	ApiAirticketGetAirlineData airlineData = null;
	
	OrderPassengerAdapter orderPassengerAdapter;
	OrderLinkmanAdapter orderLinkmanAdapter;
	

	public static AirTicketOrderFormWangfanFragment newInstance (Bundle data) {
		AirTicketOrderFormWangfanFragment fragment = new AirTicketOrderFormWangfanFragment();
		fragment.setArguments(data);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "onCreate");
		Bundle bundle = getArguments();
		if(bundle != null) {
			data = bundle;
		}
	}

	@Override
	protected View onInitView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.airticket_order_wangfan_layout, container,false);
		initViews(rootView);
		return rootView;
	}

	@Override
	protected void onAsyncLoadData() {

	}
	
	@Override
	public void onTimeout() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRefreshDatas() {
		((UIManagerActivity)getActivity()).setTopTitle("机票订单");

		passengerList = ((UIManagerActivity)getActivity()).selectedPassengerList;
		linkmanList = ((UIManagerActivity)getActivity()).selectedLinkmanList;

		if(passengerList != null) {
			orderPassengerAdapter = new OrderPassengerAdapter(getActivity(), passengerList, new ListViewButtonListener() {
				
				@Override
				public void clickAtPosition(int position) {
					if (passengerList.contains(passengerList.get(position))) {
						passengerList.remove(passengerList.get(position));
					}
					
					djr_list.setAdapter(orderPassengerAdapter);
				}
			});
			
			djr_list.setAdapter(orderPassengerAdapter);
		}
		
		if(linkmanList != null) {
			orderLinkmanAdapter = new OrderLinkmanAdapter(getActivity(), linkmanList, new ListViewButtonListener() {
				
				@Override
				public void clickAtPosition(int position) {
					if (linkmanList.contains(linkmanList.get(position))) {
						linkmanList.remove(linkmanList.get(position));
					}
					
					linkman_list.setAdapter(orderLinkmanAdapter);
				}
			});
			linkman_list.setAdapter(orderLinkmanAdapter);
		}

	}

	private void initViews (View rootView) {
		submitButton = (Button)rootView.findViewById(R.id.submit);
		djr_list = (IListView)rootView.findViewById(R.id.djr_list);
		linkman_list = (IListView)rootView.findViewById(R.id.linkman_list);

		start_city = (TextView)rootView.findViewById(R.id.start_city);
		end_city = (TextView)rootView.findViewById(R.id.end_city);

		start_date = (TextView)rootView.findViewById(R.id.start_date);
		start_time = (TextView)rootView.findViewById(R.id.start_time);
		start_airport = (TextView)rootView.findViewById(R.id.start_airport);

		end_date = (TextView)rootView.findViewById(R.id.end_date);
		end_time = (TextView)rootView.findViewById(R.id.end_time);
		end_airport = (TextView)rootView.findViewById(R.id.end_airport);

		qucheng = (TextView)rootView.findViewById(R.id.qucheng);
		aviation_company = (TextView)rootView.findViewById(R.id.aviation_company);
		flight = (TextView)rootView.findViewById(R.id.flight);
		air_class = (TextView)rootView.findViewById(R.id.air_class);

		price = (TextView)rootView.findViewById(R.id.price);
		jijian = (TextView)rootView.findViewById(R.id.jijian);
		fuel = (TextView)rootView.findViewById(R.id.fuel);

		add_djr = (TextView)rootView.findViewById(R.id.add_djr);
		add_linkman=(TextView) rootView.findViewById(R.id.add_linkman);
		
		rootView.findViewById(R.id.ticket_info).setOnClickListener(this);

		submitButton.setOnClickListener(this);

		add_djr.setOnClickListener(this);
		add_linkman.setOnClickListener(this);

		initFanViews(rootView) ;
		
		initDatas();
	}
	
	/**
	 * 初始化 返程views
	 * 
	 * @param rootView
	 */
	private void initFanViews (View rootView) {
		
		fan_start_date = (TextView)rootView.findViewById(R.id.fan_start_date);
		fan_start_time = (TextView)rootView.findViewById(R.id.fan_start_time);
		fan_start_airport = (TextView)rootView.findViewById(R.id.fan_start_airport);

		fan_end_date = (TextView)rootView.findViewById(R.id.fan_end_date);
		fan_end_time = (TextView)rootView.findViewById(R.id.fan_end_time);
		fan_end_airport = (TextView)rootView.findViewById(R.id.fan_end_airport);

		fan_qucheng = (TextView)rootView.findViewById(R.id.fan_qucheng);
		fan_aviation_company = (TextView)rootView.findViewById(R.id.fan_aviation_company);
		fan_flight = (TextView)rootView.findViewById(R.id.fan_flight);
		fan_air_class = (TextView)rootView.findViewById(R.id.fan_air_class);
		
		initFanDatas();
	}
	
	private ApiAirticketGetAirlineData airticketGetAirlineData;//去程数据
	private ApiAirticketGetAirlineData airticketFanchengAirlineData;//返程数据
	private ApiAirticketGetAirlineData airticketFanchengAirlineDetailData;//返程数据(详情信息)
	/**
	 * 初始化返程数据
	 */
	private void initFanDatas() {
		if(data == null ) return;
		
		airticketGetAirlineData = (ApiAirticketGetAirlineData)data.getSerializable("AirlineData");
		if(airticketGetAirlineData != null) {
			airticketFanchengAirlineData = airticketGetAirlineData.airticketFanchengAirlineData;
			
			
			
			String startTime = null, endTime = null, startDate= null, endDate = null;
			if(airticketFanchengAirlineData.takeOffTime != null && !airticketFanchengAirlineData.takeOffTime.equals("")) {
				startDate = airticketFanchengAirlineData.takeOffTime.substring(0, airticketFanchengAirlineData.takeOffTime.indexOf("T"));
				startTime = airticketFanchengAirlineData.takeOffTime.substring(airticketFanchengAirlineData.takeOffTime.indexOf("T")+1, airticketFanchengAirlineData.takeOffTime.lastIndexOf(":"));
			}

			if(airticketFanchengAirlineData.arriveTime != null && !airticketFanchengAirlineData.arriveTime.equals("")) {
				endDate = airticketFanchengAirlineData.arriveTime.substring(0, airticketFanchengAirlineData.arriveTime.indexOf("T"));
				endTime = airticketFanchengAirlineData.arriveTime.substring(airticketFanchengAirlineData.arriveTime.indexOf("T")+1, airticketFanchengAirlineData.arriveTime.lastIndexOf(":"));
			}

			fan_start_date.setText(startDate);
			fan_end_date.setText(endDate);

			fan_start_time.setText(startTime);
			fan_end_time.setText(endTime);

			fan_start_airport.setText(airticketFanchengAirlineData.dPortName);
			fan_end_airport.setText(airticketFanchengAirlineData.aPortName);
			
			
			fan_aviation_company.setText(airticketFanchengAirlineData.airLineName);
			fan_flight.setText(airticketFanchengAirlineData.flight);
			
			
			Serializable serializable = data.getSerializable("AirlineDetailFan");
			if(serializable != null && serializable instanceof ApiAirticketGetAirlineData) {
				airticketFanchengAirlineDetailData = (ApiAirticketGetAirlineData)serializable;
			}
			
			fan_air_class.setText(airticketFanchengAirlineDetailData.aClass);
			
			
		}
	}

	private void initDatas() {
		if(data == null ) return;

		start_city.setText(data.getString("departCityNameCh"));
		end_city.setText(data.getString("arriveCityNameCh"));


		String startTime = null, endTime = null, startDate= null, endDate = null;
		if(data.getString("departTime") != null && !data.getString("departTime").equals("")) {
			startDate = data.getString("departTime").substring(0, data.getString("departTime").indexOf("T"));
			startTime = data.getString("departTime").substring(data.getString("departTime").indexOf("T")+1, data.getString("departTime").lastIndexOf(":"));
		}

		if(data.getString("arriveTime") != null && !data.getString("arriveTime").equals("")) {
			endDate = data.getString("arriveTime").substring(0, data.getString("arriveTime").indexOf("T"));
			endTime = data.getString("arriveTime").substring(data.getString("arriveTime").indexOf("T")+1, data.getString("arriveTime").lastIndexOf(":"));
		}

		start_date.setText(startDate);
		end_date.setText(endDate);

		start_time.setText(startTime);
		end_time.setText(endTime);

		start_airport.setText(data.getString("dPortName"));
		end_airport.setText(data.getString("aPortName"));

//		if(data.getString("searchType").equals("S")) {
//			qucheng.setText("去程");
//		} else if(data.getString("searchType").equals("D")) {
//			qucheng.setText("返程");
//		}
		
		
		aviation_company.setText(data.getString("airLineName"));
		flight.setText(data.getString("flight"));

		
		Serializable serializable = data.getSerializable("AirlineDetail");
		if(serializable != null && serializable instanceof ApiAirticketGetAirlineData) {
			airlineData = (ApiAirticketGetAirlineData)serializable;
		}

		if(airlineData != null) {
			air_class.setText(airlineData.aClass);
			price.setText("￥"+airlineData.price);
			jijian.setText("￥"+airlineData.tax);
			fuel.setText("￥"+airlineData.oilFee);
		}


	}

	/**
	 * 检查用户信息
	 * 
	 * @return
	 */
	private boolean checkInfos() {
		//检查是否选择了联系人，登机人
		if(passengerList == null || passengerList.size() <= 0) {
			PromptUtil.showToast(getActivity(), "请选择登机人");
			return false;
		}
		if(linkmanList == null || linkmanList.size() <= 0) {
			PromptUtil.showToast(getActivity(), "请选择联系人");
			return false;
		}
		data.putInt("people_num", passengerList.size());//保存乘机人数，用于计算机票总价
		return true;
	}
	
	private ScrollView scrollView;
	private PopupWindow popupWindow;
	private TextView rerNote, endNote, refNote;//退改签信息
	private void showPopupWindow(String rerNoteInfo, String endNoteInfo, String refNoteInfo) {
		scrollView = (ScrollView) LayoutInflater.from(getActivity()).inflate(
				R.layout.airticket_repolicy_layout, null);
		rerNote = (TextView)scrollView.findViewById(R.id.rerNote);
		rerNote.setText(rerNoteInfo);
		
		endNote = (TextView)scrollView.findViewById(R.id.endNote);
		endNote.setText(endNoteInfo);
		
		refNote = (TextView)scrollView.findViewById(R.id.refNote);
		refNote.setText(refNoteInfo);

		popupWindow = new PopupWindow(getActivity());
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setWidth(getActivity().getWindowManager().getDefaultDisplay().getWidth());
		popupWindow.setHeight(getActivity().getWindowManager().getDefaultDisplay().getHeight());
		popupWindow.setOutsideTouchable(true);
		popupWindow.setFocusable(true);
		popupWindow.setContentView(scrollView);

		popupWindow.setAnimationStyle(R.style.popuStyle);
		popupWindow.showAtLocation(rootView, Gravity.LEFT
				| Gravity.BOTTOM , 0, 0);//需要指定Gravity，默认情况是center.
	}
	

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.submit:
			if(checkInfos() && data != null) {
				addFragmentToStack(UIConstantDefault.UI_CONSTANT_AIR_TICKET_CLEARINGL, 1, data);
			}
			break;

		case R.id.add_djr:
			addFragmentToStack(UIConstantDefault.UI_CONSTANT_AIR_TICKET_SELECT_PASSENGER, 1, null);
			break;

		case R.id.add_linkman:
			addFragmentToStack(UIConstantDefault.UI_CONSTANT_AIR_TICKET_SELECT_CONTACT, 1, null);
			break;
			
		case R.id.ticket_info://退改签信息
			if(airlineData != null){
				showPopupWindow(airlineData.rerNote, airlineData.endNote, airlineData.refNote );
			}
			else{
				showPopupWindow("暂无退改签信息！","暂无退改签信息！","暂无退改签信息！");
			}
			break;
		default:
			break;
		}

	}

}
