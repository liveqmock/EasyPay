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

import com.inter.trade.R;
import com.inter.trade.log.Logger;
import com.inter.trade.ui.AgentQueryWheelDateActivity;
import com.inter.trade.ui.HotelSelectPriceActivity;
import com.inter.trade.ui.HotelSelectStarLevelActivity;
import com.inter.trade.ui.fragment.airticket.util.ApiAirticketGetCityData;
import com.inter.trade.ui.fragment.hotel.data.HotelListData;
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
 * 酒店预订 详情主界面
 * @author haifengli
 *
 */
public class HotelDetailFragment extends IBaseFragment implements OnClickListener, OnCheckedChangeListener{
	
	private static final String TAG = HotelDetailFragment.class.getName();
	
	private View rootView;
	
	private RelativeLayout rl_city_layout;
	private RelativeLayout rl_date_layout;
	private RelativeLayout rl_keyword_layout;
	private RelativeLayout rl_price_layout;
	private RelativeLayout rl_starlevel_layout;
	
	private Button btn_query;
	
	private TextView tv_city     ;
	private TextView tv_date     ;
	private TextView tv_keyword  ;
	private TextView tv_price    ;
	private TextView tv_starlevel;
	
	private TextView tv_hotel_name   ;
	private TextView btn_description ;
	private TextView tv_hotel_star   ;
	private TextView tv_hotel_address;
	private ImageView img_hotel;
	
	
	private String[] mPriceArray;
	private String[] mStarLevelArray;
	
	private Bundle data = null;
	private HotelListData mHotelListData;
	
	private RadioGroup rgNavigation;
	private static int selected = 0;
	
	//酒店预订 当前页面ID
	private int mModuleID = UIConstantDefault.UI_CONSTANT_HOTEL;
	
	//酒店预订 日期
	private String hotelDate = null;
	
	//酒店预订 城市
	private String hotelCity = null;
	private ApiAirticketGetCityData hotelCityData = null;
	
	//酒店预订 价格
	private String hotelPrice = null;
	
	//酒店预订 星级
	private String hotelStarLevel = null;
	
	//酒店预订 关键字
	private String hotelKeyWord = null;
	
	
	public static HotelDetailFragment newInstance (Bundle data) {
		HotelDetailFragment fragment = new HotelDetailFragment();
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
		rootView = inflater.inflate(R.layout.hotel_detail_layout, container,false);
		initViews(rootView);
		return rootView;
	}

	@Override
	protected void onAsyncLoadData() {
		
	}

	@Override
	public void onRefreshDatas() {
		Logger.d(TAG, "onRefreshDatas");
		mModuleID = UIConstantDefault.UI_CONSTANT_HOTEL_DETAIL;
		((UIManagerActivity)getActivity()).setTopTitle("酒店详情");
		if(currentFragment != null) {
			currentFragment.onRefreshDatas();
		}
	}

	private void initViews (View rootView) {
		tv_hotel_name    = (TextView)rootView.findViewById(R.id.tv_hotel_name);
		btn_description  = (TextView)rootView.findViewById(R.id.btn_description);
		tv_hotel_star    = (TextView)rootView.findViewById(R.id.tv_hotel_star);
		tv_hotel_address = (TextView)rootView.findViewById(R.id.tv_hotel_address);
		img_hotel        = (ImageView)rootView.findViewById(R.id.img_hotel);
		
		if( mHotelListData != null) {
			if(mHotelListData.hotelName != null) {
				tv_hotel_name.setText(mHotelListData.hotelName);
			}
			if(mHotelListData.starRate != null) {
				tv_hotel_star.setText(mHotelListData.starRate);
			}
			if(mHotelListData.address != null) {
				tv_hotel_address.setText(mHotelListData.address);
			}
			if(btn_description != null) {
				btn_description.setText("简介");
				btn_description.setOnClickListener(this);
			}
			if(img_hotel != null && mHotelListData.imageUrl != null) {
//				this.imageLoad.displayImage(data.imageUrl, img_hotel,options); 
				FinalBitmap.create(getActivity()).display(img_hotel, mHotelListData.imageUrl);
			}
		}
		
		rgNavigation = (RadioGroup) rootView.findViewById(R.id.rg_navigation);
		rgNavigation.setOnCheckedChangeListener(HotelDetailFragment.this);
		rgNavigation.getChildAt(selected).performClick();
		
	}
	
	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.btn_description:
			Bundle dataBundle = new Bundle();
			dataBundle.putSerializable("hotelDetail", mHotelListData);
			mModuleID = UIConstantDefault.UI_CONSTANT_HOTEL_DESCRIPTION;
			IMainHandlerManager.handlerUI(UIConstantDefault.UI_CONSTANT_HOTEL_DESCRIPTION, 1, dataBundle);
			break;
		default:
			break;
		}
		
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
			 }
			 break;
		}
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

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.rb_house_type:// 房型
			selected = 0;
			if (houseTypeFragment == null) {
				Bundle dataBundle = new Bundle();
				if(mHotelListData != null){
					dataBundle.putSerializable("hotelDetail", mHotelListData);
				}
				houseTypeFragment = HotelHouseTypeFragment.newInstance(dataBundle);
			}
			switchContent(houseTypeFragment);
			break;
		case R.id.rb_comment:// 评论
			selected = 1;
			if (commentFragment == null) {
				commentFragment = HotelCommentFragment.newInstance(null);
			}
			switchContent(commentFragment);
			break;
		case R.id.rb_photo:// 照片
			selected = 2;
			if (photoFragment == null) {
				photoFragment = HotelPhotoFragment.newInstance(null);
			}
			switchContent(photoFragment);
			break;
		default:
			break;
		}
	}
	
	/**
	* 在一个Fragment里，切换多个子Fragment页面
	* 
	* @param from
	* @param to
	*/
	public void switchContent(IBaseFragment to) {
		
		FragmentTransaction transaction = getActivity().getSupportFragmentManager()
				.beginTransaction();
		
		if(currentFragment == null) {
			transaction.add(R.id.detail_container, to).commit();
			currentFragment = to;
			return;
		}
		
		if (currentFragment != to) {

//			to.getLoaderManager().hasRunningLoaders();
			// 先判断是否被add过
			if (!to.isAdded()) {

				// 隐藏当前的fragment，add下一个到Activity中
				transaction.hide(currentFragment).add(R.id.detail_container, to).commit();
			} else {
				// 隐藏当前的fragment，显示下一个
				transaction.hide(currentFragment).show(to).commit();
			}
		}
		currentFragment = to;
	}
	
	/**
	 * 当前Fragment
	 */
	private IBaseFragment currentFragment = null;
	
	private IBaseFragment houseTypeFragment = null;
	private IBaseFragment commentFragment = null;
	private IBaseFragment photoFragment = null;

}
