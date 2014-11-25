package com.inter.trade.ui;

import java.util.Calendar;

import com.inter.trade.R;
import com.inter.trade.ui.fragment.qmoney.util.QMoneyData;
import com.inter.trade.ui.views.wheelwidget.OnWheelChangedListener;
import com.inter.trade.ui.views.wheelwidget.WheelView;
import com.inter.trade.ui.views.wheelwidget.adapters.ArrayWheelAdapter;
import com.inter.trade.ui.views.wheelwidget.adapters.NumericWheelAdapter;
import com.inter.trade.util.PromptUtil;

//import kankan.wheel.demo.extended.R;
//import kankan.wheel.widget.OnWheelChangedListener;
//import kankan.wheel.widget.WheelView;
//import kankan.wheel.widget.adapters.ArrayWheelAdapter;
//import kankan.wheel.widget.adapters.NumericWheelAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

public class AgentQueryWheelDateActivity extends Activity implements OnClickListener{
	private int mYear;
	private int mMonth;
	private int mDay;
	private TextView cancel_tv;
	private TextView ok_tv;
	
	private WheelView year;
	private WheelView month;
	private WheelView day;
	private String mDateType;
	Calendar calendar;
	Bundle bundle;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.agent_query_wheel_date_layout);
		
//		WindowManager wm = getWindowManager();
//		Display defaultDisplay = wm.getDefaultDisplay();
//		int height = defaultDisplay.getHeight();//为获取屏幕高    
//		int width = defaultDisplay.getWidth();//为获取屏幕宽
//		Window win = getWindow();
//		LayoutParams params = new LayoutParams();
//		params.x = 0;
//		params.y = height/2;
//		params.width=width;
//		params.height=height/2;
//		win.setAttributes(params);
		
		//弹出一个窗口，让背后的窗口变暗一点
//		WindowManager.LayoutParams lp = getWindow().getAttributes();
//        //When FLAG_DIM_BEHIND is set, this is the amount of dimming to apply. Range is from 1.0 for completely opaque to 0.0 for no dim. 
//		lp.dimAmount = 0.4f;
		
		WindowManager wm = getWindowManager();
		Display defaultDisplay = wm.getDefaultDisplay();
		int height = defaultDisplay.getHeight();//为获取屏幕高    
		int width = defaultDisplay.getWidth();//为获取屏幕宽
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.dimAmount = 0.4f;//弹出一个窗口，让背后的窗口变暗一点
		lp.x = 0;
		lp.y = height/2;
		lp.width=width;
		lp.height=height/2;
		
		getWindow().setAttributes(lp);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND, WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		
		calendar = Calendar.getInstance();

		cancel_tv = (TextView) findViewById(R.id.cancel_tv);
		ok_tv = (TextView) findViewById(R.id.ok_tv);
		cancel_tv.setOnClickListener(this);
		ok_tv.setOnClickListener(this);
		
		month = (WheelView) findViewById(R.id.month);
		year = (WheelView) findViewById(R.id.year);
		day = (WheelView) findViewById(R.id.day);

		month.setWheelBackground(R.drawable.wheel_bg_white);// 设置背景的颜色
		month.setShadowColor(0xFFFFFF, 0xFFFFFF, 0xFFFFFF);// 设置上下边缘的颜色值
		month.setCyclic(true);// 设置是否循环
		
		year.setWheelBackground(R.drawable.wheel_bg_white);// 设置背景的颜色
		year.setShadowColor(0xFFFFFF, 0xFFFFFF, 0xFFFFFF);// 设置上下边缘的颜色值
		year.setCyclic(true);// 设置是否循环
		
		day.setWheelBackground(R.drawable.wheel_bg_white);// 设置背景的颜色
		day.setShadowColor(0xFFFFFF, 0xFFFFFF, 0xFFFFFF);// 设置上下边缘的颜色值
		day.setCyclic(true);// 设置是否循环
		
		
		OnWheelChangedListener listener = new OnWheelChangedListener() {
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				updateDays(year, month, day);
			}
		};

		// month
		int curMonth = calendar.get(Calendar.MONTH);
//		String months[] = new String[] {"January", "February", "March", "April", "May",
//				"June", "July", "August", "September", "October", "November", "December"};
		String months[] = new String[] {"1月", "2月", "3月", "4月", "5月",
				"6月", "7月", "8月", "9月", "10月", "11月", "12月"};
		month.setViewAdapter(new DateArrayAdapter(this, months, curMonth));
		month.setCurrentItem(curMonth);
		month.addChangingListener(listener);

		// year
		int curYear = calendar.get(Calendar.YEAR);
//		year.setViewAdapter(new DateNumericAdapter(this, curYear, curYear + 10, 0));
		year.setViewAdapter(new DateYearAdapter(this, curYear-10, curYear + 10, 10));
		year.setCurrentItem(10);
		year.addChangingListener(listener);

		//day
		updateDays(year, month, day);
		day.setCurrentItem(calendar.get(Calendar.DAY_OF_MONTH) - 1);
		
		bundle = getIntent().getBundleExtra("wheelDate");
		mDateType = bundle.getString("dateType");
		if(mDateType.equals("month")){
			day.setVisibility(View.GONE);
		}else if(mDateType.equals("year")){
			day.setVisibility(View.GONE);
			month.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.cancel_tv://取消
			finish();
			break;
		case R.id.ok_tv://确定
			checkDate();
			break;
		}
	}
	
	public void checkDate(){
		mYear  = calendar.get(Calendar.YEAR)-10 + year.getCurrentItem();
		mMonth = month.getCurrentItem()+1;
		mDay   = day.getCurrentItem()+1;
		int curYear = calendar.get(Calendar.YEAR);
		int curMonth = calendar.get(Calendar.MONTH)+1;
		int curDay = calendar.get(Calendar.DAY_OF_MONTH);
		
		if(mYear>curYear){
			PromptUtil.showToast(this, "年份超前，请重选");
			return;
		}
		if(mYear==curYear && mMonth>curMonth){
			PromptUtil.showToast(this, "月份超前，请重选");
			return;
		}
		if(mYear==curYear && mMonth==curMonth && mDay>curDay){
			PromptUtil.showToast(this, "日期超前，请重选");
			return;
		}
		Intent intent = new Intent();
		intent.putExtra("year", mYear);
		intent.putExtra("month", mMonth);
		intent.putExtra("day", mDay);
		setResult(RESULT_OK, intent);
		finish();
	}
	
	
	/**
	 * Updates day wheel. Sets max days according to selected month and year
	 */
	void updateDays(WheelView year, WheelView month, WheelView day) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR)-10 + year.getCurrentItem());
		calendar.set(Calendar.MONTH, month.getCurrentItem());

		int maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		day.setViewAdapter(new DateNumericAdapter(this, 1, maxDays, calendar.get(Calendar.DAY_OF_MONTH) - 1));
		int curDay = Math.min(maxDays, day.getCurrentItem() + 1);
		day.setCurrentItem(curDay - 1, true);
	}

	/**
	 * Adapter for numeric wheels. Highlights the current value.
	 */
	private class DateNumericAdapter extends NumericWheelAdapter {
		// Index of current item
		int currentItem;
		// Index of item to be highlighted
		int currentValue;

		/**
		 * Constructor
		 */
		public DateNumericAdapter(Context context, int minValue, int maxValue, int current) {
			super(context, minValue, maxValue);
			this.currentValue = current;
			setTextSize(16);
		}

		@Override
		protected void configureTextView(TextView view) {
			super.configureTextView(view);
			if (currentItem == currentValue) {
				view.setTextColor(0xFF0000F0);
			}
			view.setTypeface(Typeface.SANS_SERIF);
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			currentItem = index;
			return super.getItem(index, cachedView, parent);
		}
	}
	
	/**
	 * Adapter for year"年" wheels. Highlights the current value.
	 */
	private class DateYearAdapter extends DateNumericAdapter {
		public DateYearAdapter(Context context, int minValue, int maxValue, int current) {
			super(context, minValue, maxValue, current);
		}
		
		 @Override
		    public CharSequence getItemText(int index) {
			 return super.getItemText(index)+"年";
		 }      
	}

	/**
	 * Adapter for string based wheel. Highlights the current value.
	 */
	private class DateArrayAdapter extends ArrayWheelAdapter<String> {
		// Index of current item
		int currentItem;
		// Index of item to be highlighted
		int currentValue;

		/**
		 * Constructor
		 */
		public DateArrayAdapter(Context context, String[] items, int current) {
			super(context, items);
			this.currentValue = current;
			setTextSize(16);
		}

		@Override
		protected void configureTextView(TextView view) {
			super.configureTextView(view);
			if (currentItem == currentValue) {
				view.setTextColor(0xFF0000F0);
			}
			view.setTypeface(Typeface.SANS_SERIF);
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			currentItem = index;
			return super.getItem(index, cachedView, parent);
		}
	}
}
