package com.inter.trade.ui.fragment.hotel;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.inter.trade.R;
import com.inter.trade.ui.fragment.hotel.util.ru.truba.touchgallery.GalleryWidget.BasePagerAdapter.OnItemChangeListener;
import com.inter.trade.ui.fragment.hotel.util.ru.truba.touchgallery.GalleryWidget.GalleryViewPager;
import com.inter.trade.ui.fragment.hotel.util.ru.truba.touchgallery.GalleryWidget.UrlPagerAdapter;
import com.inter.trade.ui.manager.IBaseFragment;
import com.inter.trade.ui.manager.UIManagerActivity;

/**
 * 酒店预订 图片滑动展示
 * @author haifengli
 *
 */
public class HotelTouchGalleryFragment extends IBaseFragment{
	private int currentItem;
	private String[] imageUrls;
//	String[] imageUrls = new String[] {
//			"http://Images4.c-ctrip.com/target/hotel/1000/83/478636c39701436184a932fce14021c5_550_412.jpg",
//			"http://Images4.c-ctrip.com/target/hotel/1000/83/dfb82114e73d41a3a97252f68e72bc5b_550_412.jpg",
//			"http://Images4.c-ctrip.com/target/hotel/1000/83/64dbb685d53c4126ab9ac97f13de60a7_550_412.jpg",
//			
//			"http://Images4.c-ctrip.com/target/hotel/11000/10923/6b863801184f42ac95067837deefb534_100_75.jpg",
//			"http://Images4.c-ctrip.com/target/hotel/74000/73429/08346608b3db48d8b83fa047a62632bb_100_75.jpg",
//			"http://Images4.c-ctrip.com/target/t1/hotel/73000/72951/5fa9824f445548c79a2c9e511e580b78_100_75.jpg",
//			
//			"http://Images4.c-ctrip.com/target/t1/hotel/1000/83/2edab06e25d94ad9b0a770101199bb33_550_412.jpg",
//			
//			"http://cs407831.userapi.com/v407831207/18f6/jBaVZFDhXRA.jpg",
//            "http://cs407831.userapi.com/v4078f31207/18fe/4Tz8av5Hlvo.jpg",
//            "http://cs407831.userapi.com/v407831207/1906/oxoP6URjFtA.jpg",
//            "http://cs407831.userapi.com/v407831207/190e/2Sz9A774hUc.jpg",
//            "http://cs407831.userapi.com/v407831207/1916/Ua52RjnKqjk.jpg",
//            "http://cs407831.userapi.com/v407831207/191e/QEQE83Ok0lQ.jpg"
//			};
	
	private static final String TAG = HotelTouchGalleryFragment.class.getName();
	
	private View rootView;
	private Bundle data = null;
	private GalleryViewPager mViewPager;
//	private HotelListData mHotelListData;
	
	
	public static HotelTouchGalleryFragment create(String[] urls, int position) {
		final HotelTouchGalleryFragment f = new HotelTouchGalleryFragment();

		final Bundle args = new Bundle();
		args.putStringArray("URLS", urls);
		args.putInt("currentItem", position);
		f.setArguments(args);
		return f;
	}

	public HotelTouchGalleryFragment() {

	}
	
	public static HotelTouchGalleryFragment newInstance (Bundle data) {
		HotelTouchGalleryFragment fragment = new HotelTouchGalleryFragment();
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
			imageUrls=data.getStringArray("URLS");
			currentItem=data.getInt("currentItem");
		}
	}
	
	@Override
	protected View onInitView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.hotel_touch_gallery_layout, container,false);
		initViews(rootView);
		return rootView;
	}

	@Override
	protected void onAsyncLoadData() {
		
	}

	@Override
	public void onRefreshDatas() {
		setTitleBar();
	}

	/**
	 * 设置顶部栏数据（标题、更多）
	 */
	private void setTitleBar() {
		((UIManagerActivity)getActivity()).setTopTitle("酒店照片");
	}
	
	private void initViews (View view) {
		if(imageUrls == null || imageUrls.length==0){
			return;
		}
		
		List<String> items = new ArrayList<String>();
        Collections.addAll(items, imageUrls);

        UrlPagerAdapter pagerAdapter = new UrlPagerAdapter(getActivity(), items);
        pagerAdapter.setOnItemChangeListener(new OnItemChangeListener()
		{
			@Override
			public void onItemChange(int currentPosition)
			{
				Toast.makeText(getActivity(), "Current item is " + currentPosition, Toast.LENGTH_SHORT).show();
			}
		});
        
        mViewPager = (GalleryViewPager)view.findViewById(R.id.viewer);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setCurrentItem(currentItem);
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
