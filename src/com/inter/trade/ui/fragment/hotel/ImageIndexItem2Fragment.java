package com.inter.trade.ui.fragment.hotel;


import java.util.ArrayList;

import net.tsz.afinal.FinalBitmap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
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
import com.inter.trade.ui.AgentQueryWheelDateActivity;
import com.inter.trade.ui.HotelSelectPriceActivity;
import com.inter.trade.ui.HotelSelectStarLevelActivity;
import com.inter.trade.ui.fragment.airticket.util.ApiAirticketGetCityData;
import com.inter.trade.ui.fragment.hotel.HotelDetailDialogFragment.ImageAdapter;
import com.inter.trade.ui.fragment.hotel.data.HotelListData;
import com.inter.trade.ui.func.FuncData;
import com.inter.trade.ui.func.childfragment.BankFragment;
import com.inter.trade.ui.func.childfragment.ChildIndexItem;
import com.inter.trade.ui.func.childfragment.FavourFragment;
import com.inter.trade.ui.func.childfragment.MoreFragment;
import com.inter.trade.ui.manager.IBaseFragment;
import com.inter.trade.ui.manager.UIManagerActivity;
import com.inter.trade.ui.manager.core.IMainHandlerManager;
import com.inter.trade.ui.manager.core.UIConstantDefault;
import com.inter.trade.util.PromptUtil;

/**
 * 酒店预订 图片滑动展示
 * @author haifengli
 *
 */
public class ImageIndexItem2Fragment extends IBaseFragment{
	
	private static final String TAG = ImageIndexItem2Fragment.class.getName();
	
	private View rootView;
	private ImageView img;
	private String url;
//	private ViewPager viewPager;
//	private ImageAdapter mPagerAdapter;
	
	private Bundle data = null;
//	private HotelListData mHotelListData;
	
	
	public static ImageIndexItem2Fragment create(String url) {
		final ImageIndexItem2Fragment f = new ImageIndexItem2Fragment();

		final Bundle args = new Bundle();
		args.putString("URL", url);
		f.setArguments(args);
		return f;
	}

	public ImageIndexItem2Fragment() {

	}
	
	public static ImageIndexItem2Fragment newInstance (Bundle data) {
		ImageIndexItem2Fragment fragment = new ImageIndexItem2Fragment();
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
			url=data.getString("URL");
		}
	}
	
	@Override
	protected View onInitView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
//		rootView = inflater.inflate(R.layout.fragment_hotel_detail, container,false);
		rootView = inflater.inflate(R.layout.hotel_view_page_item2_layout, container,false);
		initViews(rootView);
		return rootView;
	}

	@Override
	protected void onAsyncLoadData() {
		if(img != null && url != null){
			FinalBitmap.create(getActivity()).display(img, url);
		}
	}

	@Override
	public void onRefreshDatas() {
		Log.i(TAG, "onRefreshDatas");
	}

	private void initViews (View view) {
		img = (ImageView) view.findViewById(R.id.image);
//			viewPager = (ViewPager) view.findViewById(R.id.viewPager);
//			mPagerAdapter = new ImageAdapter(getChildFragmentManager());
//			//绑定适配器
//	        viewPager.setAdapter(mPagerAdapter);
//	        viewPager.setCurrentItem(0);
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
