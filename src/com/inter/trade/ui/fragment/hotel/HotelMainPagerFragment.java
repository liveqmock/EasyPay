package com.inter.trade.ui.fragment.hotel;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.inter.trade.R;
import com.inter.trade.ui.AgentQueryWheelDateActivity;
import com.inter.trade.ui.HotelSelectPriceActivity;
import com.inter.trade.ui.HotelSelectStarLevelActivity;
import com.inter.trade.ui.fragment.hotel.data.HotelGetCityData;
import com.inter.trade.ui.fragment.hotel.data.HotelKeywordData;
import com.inter.trade.ui.fragment.hotel.util.HotelUtils;
import com.inter.trade.ui.manager.IBaseFragment;
import com.inter.trade.ui.manager.UIManagerActivity;
import com.inter.trade.ui.manager.core.IMainHandlerManager;
import com.inter.trade.ui.manager.core.UIConstantDefault;
import com.inter.trade.util.PromptUtil;

/**
 * 酒店预订 首页
 * @author haifengli
 *
 */
public class HotelMainPagerFragment extends IBaseFragment implements OnClickListener{
	
	private static final String TAG = HotelMainPagerFragment.class.getName();
	
	private View rootView;
	
	private RelativeLayout rl_city_layout;
	private RelativeLayout rl_date_layout;
	private RelativeLayout rl_keyword_layout;
	private RelativeLayout rl_price_layout;
	private RelativeLayout rl_starlevel_layout;
	
	private Button btn_query;
	
	private TextView tv_city     ;
	private TextView tv_date     ;
	private TextView tv_date_out     ;
	private EditText et_daytotal     ;
	private ImageButton ibtn_daytotal_dec, ibtn_daytotal_inc;
	private TextView tv_keyword  ;
	private TextView tv_price    ;
	private TextView tv_starlevel;
	
	private String[] mPriceArray;
	private String[] mStarLevelArray;
	
	private Bundle data = null;
	
	//酒店预订 当前页面ID
	private int mModuleID = UIConstantDefault.UI_CONSTANT_HOTEL;
	
	/**
	 * 酒店预订 日期(入住)
	 */
	public String hotelDate = null;
	
	/**
	 * 酒店预订 日期(离店)
	 */
	public String hotelDateOut = null;
	
	/**
	 * 酒店预订 入住天数int
	 */
	public int days = 1;
	
	/**
	 * 酒店预订  入住天数String
	 */
	public String daytotal = null;
	
	//酒店预订 城市
	private String hotelCity = null;
	private HotelGetCityData hotelCityData = null;
	
	//酒店预订 价格
	private String hotelPrice = null;
	//酒店预订 价格ID
	private int priceId = 0;
	
	//酒店预订 星级 文字
	private String hotelStarLevel = null;
	//酒店预订 星级 ID
	private int starId = 0;
	
	//酒店预订 关键字
	private HotelKeywordData hotelKeyWord = null;
	
	
	public static HotelMainPagerFragment newInstance (Bundle data) {
		HotelMainPagerFragment fragment = new HotelMainPagerFragment();
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
		rootView = inflater.inflate(R.layout.hotel_main_layout, container,false);
		initViews(rootView);
		return rootView;
	}

	@Override
	protected void onAsyncLoadData() {
		
	}

	@Override
	public void onRefreshDatas() {
		Log.i(TAG, "onRefreshDatas");
		mModuleID = UIConstantDefault.UI_CONSTANT_HOTEL;
		((UIManagerActivity)getActivity()).setTopTitle("酒店预订");
		((UIManagerActivity)getActivity()).setRightButtonIconOnClickListener(View.VISIBLE , new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				addFragmentToStack(UIConstantDefault.UI_CONSTANT_HOTEL_ORDER_HISTORY_LIST, 1, data);
			}
		});
		
		//模拟后一个页面返回前一个页面 填充数据
		hotelCityData = ((UIManagerActivity)getActivity()).hotelCityData;
		if( hotelCityData != null ) {
			hotelCity = hotelCityData.getCityNameCh();
			if(hotelCity != null ){
				tv_city.setText(hotelCity);
			}
		}
		
		hotelDate = ((UIManagerActivity)getActivity()).hotelDate;
		if( hotelDate != null ) {
			tv_date.setText(hotelDate);
			showOutDate(hotelDate, days);
		}else{
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
			String date = df.format(new Date());//默认为当前系统时间
			if(date != null){
				hotelDate = date;
				((UIManagerActivity)getActivity()).hotelDate = date;
				tv_date.setText(hotelDate);
				showOutDate(hotelDate, days);
			}
		}
		
		hotelKeyWord = ((UIManagerActivity)getActivity()).hotelKeyWord;
		if( hotelKeyWord != null ) {
			hotelCity = hotelKeyWord.getCityNameCh();
			if(hotelCity != null ){
				tv_keyword.setText(hotelCity);
			}
		}
		
		hotelPrice = ((UIManagerActivity)getActivity()).hotelPrice;
		if( hotelPrice != null ) {
			tv_price.setText(hotelPrice);
		}
		
		hotelStarLevel = ((UIManagerActivity)getActivity()).hotelStarLevel;
		if( hotelStarLevel != null ) {
			tv_starlevel.setText(hotelStarLevel);
		}
		
	}

	private void initViews (View rootView) {
		rl_city_layout = (RelativeLayout)rootView.findViewById(R.id.rl_city_layout);
		rl_date_layout = (RelativeLayout)rootView.findViewById(R.id.rl_date_layout);
		rl_keyword_layout = (RelativeLayout)rootView.findViewById(R.id.rl_keyword_layout);
		rl_price_layout = (RelativeLayout)rootView.findViewById(R.id.rl_price_layout);
		rl_starlevel_layout = (RelativeLayout)rootView.findViewById(R.id.rl_starlevel_layout);
		btn_query = (Button)rootView.findViewById(R.id.btn_query);
		
		tv_city      = (TextView)rootView.findViewById(R.id.tv_city     );
		
		tv_date      = (TextView)rootView.findViewById(R.id.tv_date     );
		tv_date_out      = (TextView)rootView.findViewById(R.id.tv_date_out );
		et_daytotal      = (EditText)rootView.findViewById(R.id.et_daytotal );
		ibtn_daytotal_dec = (ImageButton)rootView.findViewById(R.id.ibtn_daytotal_dec);
		ibtn_daytotal_inc = (ImageButton)rootView.findViewById(R.id.ibtn_daytotal_inc);
		
		tv_keyword   = (TextView)rootView.findViewById(R.id.tv_keyword  );
		tv_price     = (TextView)rootView.findViewById(R.id.tv_price    );
		tv_starlevel = (TextView)rootView.findViewById(R.id.tv_starlevel);
		
		rl_city_layout.setOnClickListener(this);
		rl_date_layout.setOnClickListener(this);
		rl_keyword_layout.setOnClickListener(this);
		rl_price_layout.setOnClickListener(this);
		rl_starlevel_layout.setOnClickListener(this);
		btn_query.setOnClickListener(this);
		
		ibtn_daytotal_dec.setOnClickListener(this);
		ibtn_daytotal_inc.setOnClickListener(this);
		
		setEditTextChangedListener(et_daytotal);
	}
	
	@Override
	public void onClick(View v) throws NumberFormatException{
		Bundle dataBundle = new Bundle();
		switch (v.getId()) {
		case R.id.btn_query:
			if(checkToQuery()){
				mModuleID = UIConstantDefault.UI_CONSTANT_HOTEL_LIST;
				IMainHandlerManager.handlerUI(UIConstantDefault.UI_CONSTANT_HOTEL_LIST, 1, null);
			}
			break;
		case R.id.rl_price_layout:
			mModuleID = UIConstantDefault.UI_CONSTANT_HOTEL_PRICE;
			selectPrice();
			
			break;
		case R.id.rl_starlevel_layout:
			mModuleID = UIConstantDefault.UI_CONSTANT_HOTEL_STAR_LEVEL;
			selectStarLevel();
			
			break;
		case R.id.rl_date_layout:
			mModuleID = UIConstantDefault.UI_CONSTANT_HOTEL_DATE;
			/**
			 * 入住日期，hotelDateType：0
			 */
			dataBundle.putInt("hotelDateType", 0);
			IMainHandlerManager.handlerUI(UIConstantDefault.UI_CONSTANT_HOTEL_DATE, 1, dataBundle);
			
			break;
		case R.id.ibtn_daytotal_dec:
			daytotal = et_daytotal.getText()+"";
			days = Integer.parseInt(daytotal);
			if(days>1){
				days--;
				et_daytotal.setText(days+"");
//				showOutDate(hotelDate, days);
			}
			
			break;
		case R.id.ibtn_daytotal_inc:
			daytotal = et_daytotal.getText()+"";
			days = Integer.parseInt(daytotal);
			if(days<999){
				days++;
				et_daytotal.setText(days+"");
//				showOutDate(hotelDate, days);
			}
			
			break;
		case R.id.rl_city_layout:
			mModuleID = UIConstantDefault.UI_CONSTANT_HOTEL_CITY;
			IMainHandlerManager.handlerUI(UIConstantDefault.UI_CONSTANT_HOTEL_CITY, 1, null);
			
			break;
		case R.id.rl_keyword_layout:
			mModuleID = UIConstantDefault.UI_CONSTANT_HOTEL_KEYWORD;
			IMainHandlerManager.handlerUI(UIConstantDefault.UI_CONSTANT_HOTEL_KEYWORD, 1, null);
			
			break;
		default:
			break;
		}
		
	}
	
	private boolean checkToQuery()
	{
		hotelCityData = ((UIManagerActivity)getActivity()).hotelCityData;
		if( hotelCityData != null ){
			String city = tv_city.getText()+"";
			String cityId = hotelCityData.getCityId();
			if("".equals(city) || cityId==null || "".equals(cityId)){
				PromptUtil.showToast(getActivity(), "请选择入住城市");
				return false;
			}
		}else{
			PromptUtil.showToast(getActivity(), "请选择入住城市");
			return false;
		}
		return true;
	}
	
	private void setEditTextChangedListener(EditText et)
	{
		if(et != null){
			et.addTextChangedListener(new TextWatcher() {
				
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					// TODO Auto-generated method stub
					String dayStr = s.toString();
					if(dayStr == null){
						return;
					}
					int len = dayStr.length();
					if(len >= 1 && len<=3) {
						days =Integer.parseInt(dayStr);
						if(days >= 1){
							showOutDate(hotelDate, days);
						}else{
							et_daytotal.setText("1");//数量小于1，设置默认为1
						}
					}else if(len ==0) {//手动清除，设置默认为1
						et_daytotal.setText("1");
					}
				}
				
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,
						int after) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void afterTextChanged(Editable s) {
					// TODO Auto-generated method stub
					
				}
			});
		}
	}
	
	private void showOutDate(String dateStr, int days)
	{
		if(dateStr == null || days==0){
			return;
		}
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
		Date d = HotelUtils.strToDate(dateStr);
		Calendar cal = HotelUtils.dateToCalendar(d);
		cal.add(Calendar.DATE, days);
		Date date=cal.getTime();
		hotelDateOut = df.format(date);
		if(hotelDateOut != null){
			tv_date_out.setText(hotelDateOut+"离店");
			((UIManagerActivity)getActivity()).hotelDateOut = hotelDateOut;
		}
	}
	
	private void selectPrice()
	{
		Intent intent = new Intent();
		intent.setClass(getActivity(), HotelSelectPriceActivity.class);
		
		Bundle bundle = new Bundle();
		mPriceArray = new String[] {"不限", "￥150以下", "￥150-￥300", "￥301-￥450", "￥451-￥600", "￥601-￥800", "￥801-￥1000", "￥1000以上"};
		bundle.putStringArray("priceArray", mPriceArray);
		intent.putExtra("hotelPrice", bundle);
		startActivityForResult(intent, 5);
	}
	
	private void selectStarLevel()
	{
		Intent intent = new Intent();
		intent.setClass(getActivity(), HotelSelectStarLevelActivity.class);
		
		Bundle bundle = new Bundle();
		mStarLevelArray = new String[] {"不限", "快捷连锁", "二星级以下", "三星级/舒适", "四星级/高档", "五星级/豪华"};
		bundle.putStringArray("starLevelArray", mStarLevelArray);
		intent.putExtra("hotelStarLevel", bundle);
		startActivityForResult(intent, 6);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) { 
		 case 5:
			 if(resultCode == Activity.RESULT_OK )
			 {
				 hotelPrice = data.getStringExtra("price");
				 if( hotelPrice != null ) {
					 ((UIManagerActivity)getActivity()).hotelPrice=hotelPrice;
					 tv_price.setText(hotelPrice);
				 }
				 priceId = data.getIntExtra("priceId", -1);
				 if( priceId != -1 ) {
					 ((UIManagerActivity)getActivity()).priceId=priceId;
				 }
			 }
			 break;
		 case 6:
			 if(resultCode == Activity.RESULT_OK )
			 {
				 hotelStarLevel = data.getStringExtra("starLevel");
				 if( hotelStarLevel != null ) {
					 ((UIManagerActivity)getActivity()).hotelStarLevel=hotelStarLevel;
					 tv_starlevel.setText(hotelStarLevel);
				 }
				 starId = data.getIntExtra("starId",-1);
				 if( starId != -1 ) {
					 ((UIManagerActivity)getActivity()).starId=starId;
				 }
			 }
			 break;
		}
	}
	
	
	@Override
	public void onPause() {
		super.onPause();
		if(mModuleID != UIConstantDefault.UI_CONSTANT_HOTEL_STAR_LEVEL 
			&& mModuleID != UIConstantDefault.UI_CONSTANT_HOTEL_PRICE){
			((UIManagerActivity)getActivity()).setRightButtonIconOnClickListener(View.GONE ,null);
		}
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
	
	@Override
	public void onTimeout() {
		// TODO Auto-generated method stub
		
	}

}
