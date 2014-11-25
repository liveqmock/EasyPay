package com.inter.trade.ui.fragment.hotel;

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

/**
 * 酒店预订 日期选择 Fragment
 * @author haifengli
 *
 */
public class HotelSelectDateFragment extends BaseFragment implements OnClickListener{
	
	private static final String TAG = HotelSelectDateFragment.class.getName();
	
	private CalendarPickerView calendarPickerView;
	
	private Bundle data = null;
	
	/**
	 * 0:入住日期; 
	 * 1:离店日期;
	 */
	private int date_type;
	
	public static HotelSelectDateFragment newInstance (Bundle data) {
		HotelSelectDateFragment fragment = new HotelSelectDateFragment();
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
			date_type = data.getInt("hotelDateType");
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
				
				if(date_type==0){
					((UIManagerActivity)getActivity()).hotelDate = sdf.format(date);
				}else{
					((UIManagerActivity)getActivity()).hotelDateOut = sdf.format(date);
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
