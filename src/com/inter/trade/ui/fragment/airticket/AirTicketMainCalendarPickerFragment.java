package com.inter.trade.ui.fragment.airticket;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;

import com.inter.trade.R;
import com.inter.trade.ui.fragment.BaseFragment;
import com.inter.trade.ui.manager.UIManagerActivity;
import com.inter.trade.ui.views.calendar.CalendarPickerView;
import com.inter.trade.util.DateUtil;
import com.inter.trade.util.PromptUtil;

/**
 * 飞机票 日期选择 Fragment
 * @author zhichao.huang
 *
 */
public class AirTicketMainCalendarPickerFragment extends BaseFragment implements OnClickListener{
	
	private static final String TAG = AirTicketMainCalendarPickerFragment.class.getName();
	
	private CalendarPickerView calendarPickerView;
	
	private Bundle data = null;
	
	private String date_model;
	
	public static AirTicketMainCalendarPickerFragment newInstance (Bundle data) {
		AirTicketMainCalendarPickerFragment fragment = new AirTicketMainCalendarPickerFragment();
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
			date_model = data.getString("date_model");
		}
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.i(TAG, "onCreateView");
		((UIManagerActivity)getActivity()).setTopTitle("日期选择");
		calendarPickerView = (CalendarPickerView)inflater.inflate(R.layout.calendar_picker_layout, container,false);
		initViews(calendarPickerView);
		return calendarPickerView;
	}
	
	private void initViews (CalendarPickerView calendarPickerView) {
		
		final Calendar nextYear = Calendar.getInstance();
		nextYear.add(Calendar.YEAR, 1);

		final Calendar lastYear = Calendar.getInstance();
		lastYear.add(Calendar.YEAR, -1);
		
		calendarPickerView.init(new Date(), nextYear.getTime()) //
		.withSelectedDate(new Date());
		calendarPickerView.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
			
			@Override
			public void onDateUnselected(Date date) {
			}
			
			@Override
			public void onDateSelected(Date date) {
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
				
				if(((UIManagerActivity)getActivity()).danOrFan == 0) {
					((UIManagerActivity)getActivity()).airTicketDate = sdf.format(date);
				}else if(((UIManagerActivity)getActivity()).danOrFan == 1){
					if(date_model.equals("start")) {
						
						//去程日期不能大于返程日期
						if(((UIManagerActivity)getActivity()).airTicketFanDate != null) {
							
							if(DateUtil.compareDate(((UIManagerActivity)getActivity()).airTicketFanDate, date)) {
								PromptUtil.showToast(getActivity(), "去程日期不能在返程日期之后");
							} else {
								((UIManagerActivity)getActivity()).airTicketStartDate = sdf.format(date);
							}
							
						} else {
							((UIManagerActivity)getActivity()).airTicketStartDate = sdf.format(date);
						}
						
					}else if(date_model.equals("fan")) {
						
						if(((UIManagerActivity)getActivity()).airTicketStartDate != null) {
							
							if(!DateUtil.compareDate(((UIManagerActivity)getActivity()).airTicketStartDate, date)) {
								if(((UIManagerActivity)getActivity()).airTicketStartDate.equals(sdf.format(date)) ) {
									((UIManagerActivity)getActivity()).airTicketFanDate = sdf.format(date);
								} else{
									PromptUtil.showToast(getActivity(), "返程日期不能在出发日期之前");
								}
								
							} else {
								((UIManagerActivity)getActivity()).airTicketFanDate = sdf.format(date);
							}
							
						} else {
							((UIManagerActivity)getActivity()).airTicketFanDate = sdf.format(date);
						}
					}
				}
				
				((UIManagerActivity)getActivity()).removeFragmentToStack();
			}
		});
		
	}
	
	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		default:
			break;
		}
		
	}

}
