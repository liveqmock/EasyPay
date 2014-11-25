package com.inter.trade.ui.fragment.hotel;


import java.io.ObjectOutputStream.PutField;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.inter.trade.R;
import com.inter.trade.ui.fragment.hotel.data.HotelListData;
import com.inter.trade.ui.fragment.hotel.data.HotelOrderData;
import com.inter.trade.ui.fragment.hotel.data.HotelRoomData;
import com.inter.trade.ui.fragment.hotel.util.HotelUtils;
import com.inter.trade.ui.manager.IBaseFragment;
import com.inter.trade.ui.manager.UIManagerActivity;
import com.inter.trade.ui.manager.core.IMainHandlerManager;
import com.inter.trade.ui.manager.core.UIConstantDefault;
import com.inter.trade.util.PromptUtil;
import com.inter.trade.util.UserInfoCheck;

/**
 * 酒店预订 订单填写
 * @author haifengli
 *
 */
public class HotelOrderFragment extends IBaseFragment implements OnClickListener{
	
	private static final String TAG = HotelOrderFragment.class.getName();
	
	private View rootView;
	
	private RelativeLayout rl_date_layout;
	private RelativeLayout rl_date_out_layout;
	private RelativeLayout rl_person_layout;
	private RelativeLayout rl_phone_layout;
	private RelativeLayout rl_receipt_layout;
	
	private ImageButton ibtn_roomtotal_dec, ibtn_roomtotal_inc;
	private EditText et_roomtotal, et_person, et_phone;
	private Button btn_toPay;
	
	private TextView tv_date_out     ;
	private TextView tv_date     ;
	private TextView tv_totalprice  ;
	
	private Bundle data = null;
	private HotelRoomData mHotelRoomData = null;
	
	//酒店预订 当前页面ID
	private int mModuleID = UIConstantDefault.UI_CONSTANT_HOTEL;
	
	/**
	 * 酒店预订 订单数据（汇总）
	 */
	private HotelOrderData mHotelOrderData = null;
	
	/**
	 * 酒店预订 房费总额
	 */
	private int totalprice = 0;
	
	/**
	 * 酒店预订 日期
	 */
	private String hotelDate = null;
	
	/**
	 * 酒店预订 日期(离店)
	 */
	public String hotelDateOut = null;
	
	/**
	 * 酒店预订 入住天数
	 */
	public int daytotal = 1;
	
	/**
	 * 酒店预订 房间数int
	 */
	public int rooms = 1;
	
	/**
	 * 酒店预订 房间数String
	 */
	public String roomtotal = null;
	
	/**
	 * 酒店预订 房间单价
	 */
	public int price = 0;
	
	
	public static HotelOrderFragment newInstance (Bundle data) {
		HotelOrderFragment fragment = new HotelOrderFragment();
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
			mHotelRoomData = (HotelRoomData) data.getSerializable("hotelOrder");
			String  priceString = mHotelRoomData.price;
			if(priceString != null){
				price=Integer.parseInt(priceString);
			}
		}
	}
	
	@Override
	protected View onInitView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.hotel_order_form_layout, container,false);
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
		((UIManagerActivity)getActivity()).setTopTitle("订单填写");
		
		/**
		 * 模拟后一个页面返回前一个页面 填充数据
		 */
		
		/**
		 * 入住日期
		 */
		hotelDate = ((UIManagerActivity)getActivity()).hotelDate;
		if( hotelDate != null ) {
			tv_date.setText(hotelDate);
			tv_date.setTextColor(getActivity().getResources().getColor(R.color.hotel_textcolor_gray));
		}
		
		/**
		 * 离店日期
		 */
		hotelDateOut = ((UIManagerActivity)getActivity()).hotelDateOut;
		if( hotelDateOut != null ) {
			tv_date_out.setText(hotelDateOut);
			tv_date_out.setTextColor(getActivity().getResources().getColor(R.color.hotel_textcolor_gray));
		}
		
		/**
		 * 计算 入住天数
		 */
		if(hotelDate != null && hotelDateOut != null){
			int flag = hotelDate.compareTo(hotelDateOut);
			if(flag > 0){
				daytotal=0; //入住日期 晚于 离店日期（属于选择异常，应重选日期）
				tv_date.setTextColor(getActivity().getResources().getColor(R.color.red));
				tv_date_out.setTextColor(getActivity().getResources().getColor(R.color.red));
				PromptUtil.showToast(getActivity(), "入住天数异常");
			}else if(flag == 0){
				daytotal=1; //入住日期 等于 离店日期（即同一天，按一天算）
			}else{ //入住日期 早于 离店日期（一般的正常情况）
				daytotal = countDays(hotelDate, hotelDateOut);
			}
		}
		
		/**
		 * 计算 房间数
		 */
		roomtotal = et_roomtotal.getText()+"";
		rooms = Integer.parseInt(roomtotal);
		
		/**
		 * 显示 房间数
		 */
		if( et_roomtotal != null ) {
			et_roomtotal.setText(roomtotal+"");
		}
		
		/**
		 * 房费总额 = 入住天数 * 房间数 * 单价
		 */
		showTotalPrice();
	}

	private void initViews (View rootView) {
		rl_date_layout = (RelativeLayout)rootView.findViewById(R.id.rl_date_layout);
		rl_date_out_layout = (RelativeLayout)rootView.findViewById(R.id.rl_date_out_layout);
		rl_person_layout = (RelativeLayout)rootView.findViewById(R.id.rl_person_layout);
		rl_phone_layout = (RelativeLayout)rootView.findViewById(R.id.rl_phone_layout);
		rl_receipt_layout = (RelativeLayout)rootView.findViewById(R.id.rl_receipt_layout);
		btn_toPay = (Button)rootView.findViewById(R.id.btn_toPay);
		
		et_person = (EditText)rootView.findViewById(R.id.et_person);
		et_phone = (EditText)rootView.findViewById(R.id.et_phone);
		
		et_roomtotal = (EditText)rootView.findViewById(R.id.et_roomtotal);
		ibtn_roomtotal_dec = (ImageButton)rootView.findViewById(R.id.ibtn_roomtotal_dec);
		ibtn_roomtotal_inc = (ImageButton)rootView.findViewById(R.id.ibtn_roomtotal_inc);
		
		ibtn_roomtotal_dec.setOnClickListener(this);
		ibtn_roomtotal_inc.setOnClickListener(this);
		
		rl_date_layout.setOnClickListener(this);
		rl_date_out_layout.setOnClickListener(this);
		rl_receipt_layout.setOnClickListener(this);
		btn_toPay.setOnClickListener(this);
		
		tv_totalprice  = (TextView)rootView.findViewById(R.id.tv_totalprice     );
		tv_date      = (TextView)rootView.findViewById(R.id.tv_date     );
		tv_date_out  = (TextView)rootView.findViewById(R.id.tv_date_out );
		
		setEditTextChangedListener(et_roomtotal);
	}
	
	@Override
	public void onClick(View v) throws NumberFormatException{
		Bundle dataBundle = new Bundle();
		switch (v.getId()) {
		case R.id.btn_toPay:
			if(checkToPay()){
				mModuleID = UIConstantDefault.UI_CONSTANT_HOTEL_PAY_CONFIRM;
				dataBundle.putSerializable("hotelOrder", mHotelOrderData);
				IMainHandlerManager.handlerUI(UIConstantDefault.UI_CONSTANT_HOTEL_PAY_CONFIRM, 1, dataBundle);
			}
			
			break;
		case R.id.rl_date_layout:
			mModuleID = UIConstantDefault.UI_CONSTANT_HOTEL_DATE;
			dataBundle.putInt("hotelDateType", 0);
			IMainHandlerManager.handlerUI(UIConstantDefault.UI_CONSTANT_HOTEL_DATE, 1, dataBundle);
			
			break;
		case R.id.rl_date_out_layout:
			mModuleID = UIConstantDefault.UI_CONSTANT_HOTEL_DATE;
			dataBundle.putInt("hotelDateType", 1);
			IMainHandlerManager.handlerUI(UIConstantDefault.UI_CONSTANT_HOTEL_DATE, 1, dataBundle);
			
			break;
		case R.id.ibtn_roomtotal_dec:
			roomtotal = et_roomtotal.getText()+"";
			rooms = Integer.parseInt(roomtotal);
			if(rooms>1){
				rooms--;
				et_roomtotal.setText(rooms+"");
			}
			
			break;
		case R.id.ibtn_roomtotal_inc:
			roomtotal = et_roomtotal.getText()+"";
			rooms = Integer.parseInt(roomtotal);
			if(rooms<999){
				rooms++;
				et_roomtotal.setText(rooms+"");
			}
			
			break;
		case R.id.rl_receipt_layout:
//			mModuleID = UIConstantDefault.UI_CONSTANT_HOTEL_DATE;
//			dataBundle.putInt("hotelDateType", 1);
//			IMainHandlerManager.handlerUI(UIConstantDefault.UI_CONSTANT_HOTEL_DATE, 1, dataBundle);
			
			break;
		default:
			break;
		}
		
	}
	
	/**
	 * 房费总额 = 入住天数 * 房间数 * 单价
	 */
	private void showTotalPrice()
	{
		if( tv_totalprice != null ) {
			totalprice = daytotal*rooms*price;
			if(totalprice>0){
				tv_totalprice.setText("￥"+totalprice+"");
			}else{
				totalprice=0;
				tv_totalprice.setText("￥"+totalprice+"");
			}
		}
	}
	
	private boolean checkToPay()
	{
		if(daytotal<=0){
			PromptUtil.showToast(getActivity(), "入住天数异常");
			return false;
		}
		
		if(rooms<=0){
			PromptUtil.showToast(getActivity(), "请选择房间数");
			return false;
		}
		
		if(price<=0){
			PromptUtil.showToast(getActivity(), "单价异常");
			return false;
		}
		
		if(totalprice<=0){
			PromptUtil.showToast(getActivity(), "房费总额异常");
			return false;
		}
		
		String person = et_person.getText()+"";
		if("".equals(person)){
			PromptUtil.showToast(getActivity(), "请填写入住人姓名");
			return false;
		}
		
		String phone = et_phone.getText()+"";
		if("".equals(phone)){
			PromptUtil.showToast(getActivity(), "请填写手机号码");
			return false;
		}
		if(phone.length()<11 || !UserInfoCheck.checkMobilePhone(phone)){
			PromptUtil.showToast(getActivity(), "请填写正确的手机号码");
			return false;
		}
		
		mHotelOrderData = new HotelOrderData();
		if(mHotelOrderData != null && mHotelRoomData != null){
			mHotelOrderData.hotelCode = mHotelRoomData.hotelCode;
			mHotelOrderData.roomCode = mHotelRoomData.code;
			mHotelOrderData.priceCode = mHotelRoomData.priceCode;
			mHotelOrderData.price = mHotelRoomData.price;
			mHotelOrderData.startDate = hotelDate;
			mHotelOrderData.endDate = hotelDateOut;
			mHotelOrderData.roomCount = rooms+"";
			mHotelOrderData.dayCount = daytotal+"";
			mHotelOrderData.phone = phone+"";
			mHotelOrderData.payMoney = totalprice+"";
			mHotelOrderData.names = new String[]{person+""};//需要修改
		}
		
		
		return true;
	}
	
	/**
	 * 计算入住天数
	 * @param s1： 入住日期，格式字符串"yyyy-MM-dd"
	 * @param s2： 离店日期，格式字符串"yyyy-MM-dd"
	 * @return days
	 */
	public int countDays(String s1, String s2){
		int days=0;
		Date d1 = HotelUtils.strToDate(s1);
		Date d2 = HotelUtils.strToDate(s2);
		Calendar c1 = HotelUtils.dateToCalendar(d1);
		Calendar c2 = HotelUtils.dateToCalendar(d2);
		days = HotelUtils.getDaysBetween(c1,c2);
		return days;
	}
	
	private void setEditTextChangedListener(EditText et)
	{
		if(et != null){
			et.addTextChangedListener(new TextWatcher() {
				
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					// TODO Auto-generated method stub
					roomtotal = s.toString();
					if(roomtotal == null){
						return;
					}
					int len = roomtotal.length();
					if(len >= 1 && len<=3) {
						rooms =Integer.parseInt(roomtotal);
						if(rooms >= 1){
							showTotalPrice();
						}else{
							et_roomtotal.setText("1");//数量小于1，设置默认为1
						}
						
					}else if(len ==0) {//手动清除，设置默认为1
						et_roomtotal.setText("1");
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
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	
	@Override
	public void onPause() {
		super.onPause();
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
