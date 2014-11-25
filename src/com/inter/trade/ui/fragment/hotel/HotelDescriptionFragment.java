package com.inter.trade.ui.fragment.hotel;


import java.util.ArrayList;

import net.tsz.afinal.FinalBitmap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.inter.trade.AsyncLoadWork;
import com.inter.trade.R;
import com.inter.trade.AsyncLoadWork.AsyncLoadWorkListener;
import com.inter.trade.data.CommonData;
import com.inter.trade.log.Logger;
import com.inter.trade.ui.AgentQueryWheelDateActivity;
import com.inter.trade.ui.HotelSelectPriceActivity;
import com.inter.trade.ui.HotelSelectStarLevelActivity;
import com.inter.trade.ui.fragment.airticket.util.ApiAirticketGetCityData;
import com.inter.trade.ui.fragment.hotel.adapter.HotelPhotoAdapter;
import com.inter.trade.ui.fragment.hotel.data.HotelServiceData;
import com.inter.trade.ui.fragment.hotel.data.HotelListData;
import com.inter.trade.ui.fragment.hotel.util.HotelImageParser;
import com.inter.trade.ui.fragment.hotel.util.HotelServiceParser;
import com.inter.trade.ui.func.FuncData;
import com.inter.trade.ui.func.childfragment.BankFragment;
import com.inter.trade.ui.func.childfragment.FavourFragment;
import com.inter.trade.ui.func.childfragment.MoreFragment;
import com.inter.trade.ui.manager.IBaseFragment;
import com.inter.trade.ui.manager.UIManagerActivity;
import com.inter.trade.ui.manager.core.IMainHandlerManager;
import com.inter.trade.ui.manager.core.UIConstantDefault;
import com.inter.trade.util.PromptUtil;

/**
 * 酒店详情 简介
 * @author haifengli
 *
 */
public class HotelDescriptionFragment extends IBaseFragment{
	
	private static final String TAG = HotelDescriptionFragment.class.getName();
	
	private View rootView;
	
	private TextView tv_hotel_name   ;
	private TextView btn_description ;
	private TextView tv_hotel_star   ;
	private TextView tv_hotel_address;
	private ImageView img_hotel;
	
	private Bundle data = null;
	private HotelListData mHotelListData;
	private ArrayList<HotelServiceData> mHotelServiceData;
	private AsyncLoadWork<HotelServiceData> asyncHotelImageTask = null;
	
	public static HotelDescriptionFragment newInstance (Bundle data) {
		HotelDescriptionFragment fragment = new HotelDescriptionFragment();
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
			mHotelListData = (HotelListData) data.getSerializable("hotelDetail");
		}
	}
	
	@Override
	protected View onInitView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.hotel_detail_description_layout, container,false);
		initViews(rootView);
		return rootView;
	}

	@Override
	protected void onAsyncLoadData() {
		runAsyncTask("", false);
	}

	@Override
	public void onRefreshDatas() {
		Log.i(TAG, "onRefreshDatas");
		((UIManagerActivity)getActivity()).setTopTitle("酒店简介");
//		if(currentFragment != null) {
//			currentFragment.onRefreshDatas();
//		}
	}

	private void initViews (View rootView) {
		tv_hotel_name    = (TextView)rootView.findViewById(R.id.tv_hotel_name);
		btn_description  = (TextView)rootView.findViewById(R.id.btn_description);
		tv_hotel_star    = (TextView)rootView.findViewById(R.id.tv_hotel_star);
		tv_hotel_address = (TextView)rootView.findViewById(R.id.tv_hotel_address);
		img_hotel        = (ImageView)rootView.findViewById(R.id.img_hotel);
		
		if( mHotelListData != null) {
			if(mHotelListData.hotelName != null) {
				tv_hotel_name.setSingleLine(false);
				tv_hotel_name.setText(mHotelListData.hotelName);
			}
			if(mHotelListData.starRate != null) {
				tv_hotel_star.setText(mHotelListData.starRate);
			}
			if(mHotelListData.address != null) {
				tv_hotel_address.setSingleLine(false);
				tv_hotel_address.setText(mHotelListData.address);
			}
			if(btn_description != null) {
				btn_description.setVisibility(View.GONE);
			}
			if(img_hotel != null && mHotelListData.imageUrl != null) {
				FinalBitmap.create(getActivity()).display(img_hotel, mHotelListData.imageUrl);
			}
		}
		
	}
	
	private void runAsyncTask (String cityName , boolean isbackground) {

		HotelServiceParser netParser = new HotelServiceParser();
		String hotelCode=null;
		if( mHotelListData != null && mHotelListData.hotelCode != null) {
			hotelCode = mHotelListData.hotelCode;
		}
		
		CommonData requsetData = new CommonData();
		requsetData.putValue("hotelCode",hotelCode==null ? "": hotelCode);
		
		asyncHotelImageTask = new AsyncLoadWork<HotelServiceData>(getActivity(), netParser, requsetData, new AsyncLoadWorkListener() {

			@Override
			public void onSuccess(Object protocolDataList, Bundle bundle) {
				mHotelServiceData = (ArrayList<HotelServiceData>)protocolDataList;
				if(mHotelServiceData == null || mHotelServiceData.size()==0){
					return;
				}
			}

			@Override
			public void onFailure(String error) {
			}
			
		}, false, true);
		
		asyncHotelImageTask.execute("ApiHotel", "GetHotelService");
	
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

	
	/**
	 * 当前Fragment
	 */
	private IBaseFragment currentFragment = null;
	
}
