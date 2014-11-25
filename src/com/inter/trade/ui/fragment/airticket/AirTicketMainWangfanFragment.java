package com.inter.trade.ui.fragment.airticket;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.inter.trade.R;
import com.inter.trade.ui.fragment.airticket.util.ApiAirticketGetCityData;
import com.inter.trade.ui.manager.IBaseFragment;
import com.inter.trade.ui.manager.UIManagerActivity;
import com.inter.trade.ui.manager.core.IMainHandlerManager;
import com.inter.trade.ui.manager.core.UIConstantDefault;
import com.inter.trade.util.PromptUtil;

/**
 * 飞机票-往返Fragment
 * @author zhichao.huang
 *
 */
public class AirTicketMainWangfanFragment extends IBaseFragment implements OnClickListener{
	
	private static final String TAG = AirTicketMainWangfanFragment.class.getName();
	
	private View rootView;
	
	/**
	 * 提交按钮
	 */
	private Button submit;
	
	private EditText dateEditText, dateFanEditText, start_address, end_address;
	
	/**
	 * 出发/返程日期
	 */
	private String airTicketStartDate, airTicketFanDate;
	
	private ApiAirticketGetCityData airticketGetCityData, airticketEndCityData;
	
	private Bundle data = null;
	
	public static AirTicketMainWangfanFragment newInstance (Bundle data) {
		AirTicketMainWangfanFragment fragment = new AirTicketMainWangfanFragment();
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
		airTicketStartDate = null;
	}
	
	@Override
	protected View onInitView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.i(TAG, "onInitView");
		rootView = inflater.inflate(R.layout.airticket_main_wangfan_layout, container,false);
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
		airTicketStartDate = ((UIManagerActivity)getActivity()).airTicketStartDate;
		airTicketFanDate = ((UIManagerActivity)getActivity()).airTicketFanDate;
		
		airticketGetCityData = ((UIManagerActivity)getActivity()).wangfan_start_data;
		airticketEndCityData = ((UIManagerActivity)getActivity()).wangfan_end_data;
		
		//模拟后一个页面返回前一个页面 填充数据
		if( airTicketStartDate != null ) {
			dateEditText.setText(airTicketStartDate);
		}

		if(airTicketFanDate != null){
			dateFanEditText.setText(airTicketFanDate);
		}

		if(airticketGetCityData != null) {
			start_address.setText(airticketGetCityData.getCityNameCh());
		}
		if(airticketEndCityData != null) {
			end_address.setText(airticketEndCityData.getCityNameCh());
		}

	}

	private void initViews (View rootView) {
		dateEditText = (EditText)rootView.findViewById(R.id.calendar_picker_date);
		dateFanEditText = (EditText)rootView.findViewById(R.id.calendar_picker_fan_date);
		submit = (Button)rootView.findViewById(R.id.submit);
		
		start_address = (EditText)rootView.findViewById(R.id.start_address);
		end_address = (EditText)rootView.findViewById(R.id.end_address);
//		
		submit.setOnClickListener(this);
		dateEditText.setOnClickListener(this);
		dateFanEditText.setOnClickListener(this);
		
		start_address.setOnClickListener(this);
		end_address.setOnClickListener(this);
		
		rootView.findViewById(R.id.calendar_picker_layout).setOnClickListener(this);
		rootView.findViewById(R.id.calendar_picker_fan_layout).setOnClickListener(this);
		
		rootView.findViewById(R.id.start_city_layout).setOnClickListener(this);
		rootView.findViewById(R.id.end_city_layout).setOnClickListener(this);
		
		
	}
	
	/**
	 * 检查用户输入信息的真实性
	 */
	private boolean checkInfos () {
		
		if(start_address.getText().toString().equals("")) {
			PromptUtil.showToast(getActivity(), "请选择出发城市");
			return false;
		}
		if(end_address.getText().toString().equals("")) {
			PromptUtil.showToast(getActivity(), "请选择到达城市");
			return false;
		}
		
		if(dateEditText.getText().toString().equals("")) {
			PromptUtil.showToast(getActivity(), "请选择出发日期");
			return false;
		}
		if(dateFanEditText.getText().toString().equals("")) {
			PromptUtil.showToast(getActivity(), "请选择返程日期");
			return false;
		}
		
		return true;
	}
	
	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.calendar_picker_date:
		case R.id.calendar_picker_layout:
			((UIManagerActivity)getActivity()).danOrFan = 1;
			Bundle b = new Bundle();
			b.putString("date_model", "start");
			IMainHandlerManager.handlerUI(UIConstantDefault.UI_CONSTANT_AIR_TICKET_CALENDAR_PICKER, 1, b);
			break;
		case R.id.calendar_picker_fan_date:
		case R.id.calendar_picker_fan_layout:
			((UIManagerActivity)getActivity()).danOrFan = 1;
			Bundle b2 = new Bundle();
			b2.putString("date_model", "fan");
			IMainHandlerManager.handlerUI(UIConstantDefault.UI_CONSTANT_AIR_TICKET_CALENDAR_PICKER, 1, b2);
			break;
		case R.id.start_city_layout:
		case R.id.start_address:
			((UIManagerActivity)getActivity()).danOrFan = 1;
			Bundle startCityBundle = new Bundle();
			startCityBundle.putString("city", "start");
			IMainHandlerManager.handlerUI(UIConstantDefault.UI_CONSTANT_AIR_TICKET_CITY_SELECTE, 1, startCityBundle);
			break;
		case R.id.end_city_layout:
		case R.id.end_address:
			((UIManagerActivity)getActivity()).danOrFan = 1;
			Bundle endCityBundle = new Bundle();
			endCityBundle.putString("city", "end");
			IMainHandlerManager.handlerUI(UIConstantDefault.UI_CONSTANT_AIR_TICKET_CITY_SELECTE, 1, endCityBundle);
			break;
		case R.id.submit:
			if( !checkInfos() ) break;
			((UIManagerActivity)getActivity()).danOrFan = 1;
			
			Bundle queryBundle = new Bundle();
			queryBundle.putString("departCityCode", airticketGetCityData.getCityCode());
			queryBundle.putString("arriveCityCode", airticketEndCityData.getCityCode());
			queryBundle.putString("departDate", airTicketStartDate);
			queryBundle.putString("returnDate", airTicketFanDate);
			queryBundle.putString("searchType", "D");
			
			queryBundle.putString("departCityNameCh", airticketGetCityData.getCityNameCh());
			queryBundle.putString("arriveCityNameCh", airticketEndCityData.getCityNameCh());
			
			queryBundle.putString("departCityId", airticketGetCityData.getCityId());//出发城市ID
			queryBundle.putString("arriveCityId", airticketEndCityData.getCityId());//到达城市ID
			
			IMainHandlerManager.handlerUI(UIConstantDefault.UI_CONSTANT_AIR_TICKET_QUERY, 1, queryBundle);
			break;
		default:
			break;
		}
		
	}

}
