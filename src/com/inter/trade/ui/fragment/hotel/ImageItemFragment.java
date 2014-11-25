package com.inter.trade.ui.fragment.hotel;


import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
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
public class ImageItemFragment extends IBaseFragment{
	String[] imageUrls = new String[] {"http://Images4.c-ctrip.com/target/hotel/11000/10923/6b863801184f42ac95067837deefb534_100_75.jpg",
			"http://Images4.c-ctrip.com/target/hotel/74000/73429/08346608b3db48d8b83fa047a62632bb_100_75.jpg",
			"http://Images4.c-ctrip.com/target/t1/hotel/73000/72951/5fa9824f445548c79a2c9e511e580b78_100_75.jpg"};
	
	private static final String TAG = ImageItemFragment.class.getName();
	
	private View rootView;
	private View viewPager_layout;
	private ViewPager viewPager;
	private ImageAdapter mPagerAdapter;
	private ImageView imageView; 
	private ImageView[] imageViews; 
	//包裹点点的LinearLayout
	private ViewGroup group;
	private Bundle data = null;
//	private HotelListData mHotelListData;
	
	
	public static ImageItemFragment create(String url) {
		final ImageItemFragment f = new ImageItemFragment();

		final Bundle args = new Bundle();
		args.putString("URL", url);
		f.setArguments(args);
		return f;
	}

	public ImageItemFragment() {

	}
	
	public static ImageItemFragment newInstance (Bundle data) {
		ImageItemFragment fragment = new ImageItemFragment();
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
//			mHotelListData = (HotelListData) data.getSerializable("hotelDetail");
		}
	}
	
	@Override
	protected View onInitView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.hotel_view_page_layout, container,false);
//		rootView = inflater.inflate(R.layout.fragment_hotel_detail, container,false);
//		rootView = inflater.inflate(R.layout.hotel_view_page_item_layout, container,false);
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
	        group = (ViewGroup)view.findViewById(R.id.viewGroup); 
	        
	        //有多少张图就有多少个点点
	        imageViews = new ImageView[imageUrls.length];
	        for(int i =0;i<imageUrls.length;i++){
	            imageView = new ImageView(getActivity());
	            imageView.setLayoutParams(new LayoutParams(20,20));
	            imageView.setPadding(20, 0, 20, 0); 
	            imageViews[i] = imageView;   
	            
	            //默认第一张图显示为选中状态
	            if (i == 0) {  
	                imageViews[i].setBackgroundResource(R.drawable.select_poin);  
	            } else {  
	                imageViews[i].setBackgroundResource(R.drawable.unselected);  
	            }  
	            
	            group.addView(imageViews[i]);  
	        }
	        
	        
	        viewPager_layout = (View) view.findViewById(R.id.viewPager_layout);
			viewPager = (ViewPager) view.findViewById(R.id.viewPager);
			mPagerAdapter = new ImageAdapter(getChildFragmentManager());
			//绑定适配器
	        viewPager.setAdapter(mPagerAdapter);
	        viewPager.setCurrentItem(0);
	        
	        //pageView监听器
	        class GuidePageChangeListener implements OnPageChangeListener{

	            @Override
	            public void onPageScrollStateChanged(int arg0) {
	                // TODO Auto-generated method stub
	                
	            }

	            @Override
	            public void onPageScrolled(int arg0, float arg1, int arg2) {
	                // TODO Auto-generated method stub
	                
	            }

	            @Override
	            //如果切换了，就把当前的点点设置为选中背景，其他设置未选中背景
	            public void onPageSelected(int arg0) {
	                // TODO Auto-generated method stub
	                for(int i=0;i<imageViews.length;i++){
	                    imageViews[arg0].setBackgroundResource(R.drawable.select_poin);
	                     if (arg0 != i) {  
	                            imageViews[i].setBackgroundResource(R.drawable.unselected);  
	                        }  
	                }
	                
	            }
	            
	        }
	        
	        //绑定监听事件
	        viewPager.setOnPageChangeListener(new GuidePageChangeListener());   
	}
	
	public class ImageAdapter extends FragmentPagerAdapter {
		
//		private ArrayList<ArrayList<FuncData>> mList;

		public ImageAdapter(FragmentManager fm) {
			super(fm);
//			this.mList=mList;
		}

		@Override
		public Fragment getItem(int arg0) {
			return ImageIndexItemFragment.create(imageUrls[arg0]);
		}

		@Override
		public int getCount() {
			
			return imageUrls.length;
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


}
