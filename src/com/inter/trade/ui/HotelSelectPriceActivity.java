package com.inter.trade.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.inter.trade.R;
import com.inter.trade.ui.views.wheelwidget.WheelView;
import com.inter.trade.ui.views.wheelwidget.adapters.ArrayWheelAdapter;

public class HotelSelectPriceActivity extends Activity implements OnClickListener{
//	private UIManagerActivity uimActivity
	private int mPrice;
	private String priceArrays[];
	private TextView cancel_tv;
	private TextView ok_tv;
	
	private WheelView price;
	Bundle bundle;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.hotel_price_layout);
		
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
		
//		calendar = Calendar.getInstance();

		cancel_tv = (TextView) findViewById(R.id.cancel_tv);
		ok_tv = (TextView) findViewById(R.id.ok_tv);
		cancel_tv.setOnClickListener(this);
		ok_tv.setOnClickListener(this);
		
		price = (WheelView) findViewById(R.id.price);

		price.setWheelBackground(R.drawable.wheel_bg_white);// 设置背景的颜色
		price.setShadowColor(0xFFFFFF, 0xFFFFFF, 0xFFFFFF);// 设置上下边缘的颜色值
//		price.setCyclic(true);// 设置是否循环
		
		
		bundle = getIntent().getBundleExtra("hotelPrice");
		if(bundle != null){
			priceArrays = bundle.getStringArray("priceArray");
		}
		
		if(priceArrays != null){
//			//默认选择中间项
//			int curPrice = (int)(priceArrays.length/2);
			
			//默认选择首项
			int curPrice = 0;
			price.setViewAdapter(new DateArrayAdapter(this, priceArrays, curPrice));
			price.setCurrentItem(curPrice);
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
			getPrice();
			break;
		}
	}
	
	public void getPrice(){
		mPrice = price.getCurrentItem();
		
		Intent intent = new Intent();
		if(priceArrays != null && mPrice<priceArrays.length){
			intent.putExtra("price", priceArrays[mPrice]);
			intent.putExtra("priceId", mPrice);
		}
		setResult(RESULT_OK, intent);
		finish();
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
//			if (currentItem == currentValue) {
//				view.setTextColor(0xFF0000F0);
//			}
			view.setTypeface(Typeface.SANS_SERIF);
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			currentItem = index;
			return super.getItem(index, cachedView, parent);
		}
	}
}
