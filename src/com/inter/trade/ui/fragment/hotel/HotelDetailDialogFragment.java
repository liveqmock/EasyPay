package com.inter.trade.ui.fragment.hotel;

import java.util.ArrayList;
import java.util.List;

import android.R.anim;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.inter.trade.R;
import com.inter.trade.ui.fragment.hotel.data.HotelListData;
import com.inter.trade.ui.fragment.hotel.data.HotelRoomData;
import com.inter.trade.ui.fragment.hotel.data.HotelServiceData;
import com.inter.trade.ui.func.FuncData;
import com.inter.trade.ui.func.childfragment.ChildIndexItem;
import com.inter.trade.util.PromptUtil;
import com.inter.trade.view.slideplayview.AbSlidingPlayView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class HotelDetailDialogFragment extends DialogFragment implements OnClickListener{
	
//	private AbSlidingPlayView playView;
//	private ImageView playView;
	
	private View viewPager_layout;
	private ViewPager viewPager;
//	private ArrayList<View> pageview;
	private Button btnBook;
	private Bundle data = null;
	private ArrayList<HotelServiceData> mHotelServiceData;
	private HotelRoomData mHotelRoomDatas;
	String[] imageUrls = new String[]{};
	DisplayImageOptions options;
	private ImageView imageView; 
	private ImageView[] imageViews; 
	//包裹点点的LinearLayout
	private ViewGroup group;
	private ImageAdapter mPagerAdapter;
	private View view1, view2, view3;//需要滑动的页卡  
	private List<View> viewList;//把需要滑动的页卡添加到这个list中
	
	public static HotelDetailDialogFragment newInstance (Bundle data) {
		HotelDetailDialogFragment fragment = new HotelDetailDialogFragment();
		fragment.setArguments(data);
		return fragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog);
		
		Bundle bundle = getArguments();
		if(bundle != null) {
			data = bundle;
			mHotelServiceData = (ArrayList<HotelServiceData>) data.getSerializable("hotelService");
			mHotelRoomDatas = (HotelRoomData) data.getSerializable("hotelDetail");
			imageUrls=(mHotelRoomDatas.imageUrls==null?imageUrls:mHotelRoomDatas.imageUrls);
		}
		
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.hotel_dialog_image, null);
		initView(view);
		
		
		
		return view;
	}


	private void initView(View view) {
        group = (ViewGroup)view.findViewById(R.id.viewGroup); 
        
        //有多少张图就有多少个点点,若只有一个点则不用显示
        if(imageUrls.length>1){
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
        if(imageUrls.length>1){
        	viewPager.setOnPageChangeListener(new GuidePageChangeListener());
        }
		
//		playView=(ImageView) view.findViewById(R.id.slidingplay);
		btnBook=(Button) view.findViewById(R.id.btn_book);
		btnBook.setOnClickListener(this);
		
		
	}

	public class ImageAdapter extends FragmentPagerAdapter {
		
//		private ArrayList<ArrayList<FuncData>> mList;

		public ImageAdapter(FragmentManager fm) {
			super(fm);
//			this.mList=mList;
		}

		@Override
		public Fragment getItem(int arg0) {
			return ImageIndexItem2Fragment.create(imageUrls[arg0]);
		}

		@Override
		public int getCount() {
			
			return imageUrls.length;
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_book://预订按钮
			PromptUtil.showToast(getActivity(), "预订测试");
			viewPager_layout.setVisibility(viewPager_layout.getVisibility()== View.VISIBLE?View.GONE:View.VISIBLE);
			break;

		default:
			break;
		}
	}

		
}
