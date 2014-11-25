package com.inter.trade.ui.fragment.coupon;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.inter.trade.R;
import com.inter.trade.ui.fragment.BaseFragment;

public class HistoryMainFragment extends BaseFragment implements OnClickListener
{

	private ViewPager mPager;// 页卡内容
	private ImageView cursor;// 动画图片
	private TextView t1, t2;// 页卡头标
	private int offset = 0;// 动画图片偏移量
	private int currIndex = 0;// 当前页卡编号
	private int bmpW;// 动画图片宽度
	private View mView;
	
	private ArrayList<Fragment> fragmentsList;
	public static final String IMAGE_CACHE_DIR = "image";
	
	
	public HistoryMainFragment(){
		
	}
	/**
	 * 初始化头标
	 */
	private void InitTextView()
	{
		t1 = (TextView) mView.findViewById(R.id.opertion_log_tab);
		t2 = (TextView) mView.findViewById(R.id.threat_log_tab);
		
		t1.setTextColor(Color.BLACK);
		t1.setTextSize(15);
		t2.setTextColor(Color.GRAY);
		t2.setTextSize(14);

		t1.setOnClickListener(new MyOnClickListener(0));
		t2.setOnClickListener(new MyOnClickListener(1));
		
	}
	
	/**
	 * 初始化ViewPager
	 */
	private void InitViewPager()
	{
		mPager = (ViewPager) mView.findViewById(R.id.vPager);
		fragmentsList = new ArrayList<Fragment>();
		fragmentsList.add(new BuyRecordFragment());
		fragmentsList.add(new BuyRecordFragment());
		mPager.setAdapter(new MyFragmentPagerAdapter(getFragmentManager(), fragmentsList));
		mPager.setCurrentItem(0);
		mPager.setOnPageChangeListener(new MyOnPageChangeListener());
	}

	
	/**
	 * 初始化动画
	 */
	private void InitImageView()
	{
		cursor = (ImageView) mView.findViewById(R.id.cursor);
//		bmpW = SysUtil.getBitmapOptions(R.drawable.log_navigation).outWidth;// 获取图片宽度
		bmpW = cursor.getDrawable().getIntrinsicWidth();
		DisplayMetrics dm = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;// 获取分辨率宽度
		offset = (screenW / 2 - bmpW) / 2;// 计算偏移量
		Matrix matrix = new Matrix();
		matrix.postTranslate(offset, 0);
		cursor.setImageMatrix(matrix);// 设置动画初始位置
	}

	/**
	 * 头标点击监听
	 */
	public class MyOnClickListener implements View.OnClickListener
	{
		private int index = 0;

		public MyOnClickListener(int i)
		{
			index = i;
		}

		@Override
		public void onClick(View v)
		{
			if(v.getId() == R.id.opertion_log_tab){
				TextView tv = (TextView)v;
				tv.setTextColor(Color.BLACK);
				tv.setTextSize(15);
				t2.setTextColor(Color.GRAY);
				t2.setTextSize(14);
			}else{
				TextView tv = (TextView)v;
				tv.setTextColor(Color.BLACK);
				tv.setTextSize(15);
				t1.setTextColor(Color.GRAY);
				t1.setTextSize(14);
			}
			mPager.setCurrentItem(index);
		}
	};

	/**
	 * ViewPager适配器
	 */
	public class MyPagerAdapter extends PagerAdapter
	{
		public List<View> mListViews;

		public MyPagerAdapter(List<View> mListViews)
		{
			this.mListViews = mListViews;
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2)
		{
			((ViewPager) arg0).removeView(mListViews.get(arg1));
		}

		@Override
		public void finishUpdate(View arg0)
		{
		}

		@Override
		public int getCount()
		{
			return mListViews.size();
		}

		@Override
		public Object instantiateItem(View arg0, int arg1)
		{
			((ViewPager) arg0).addView(mListViews.get(arg1), 0);
			return mListViews.get(arg1);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1)
		{
			return arg0 == (arg1);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1)
		{
		}

		@Override
		public Parcelable saveState()
		{
			return null;
		}

		@Override
		public void startUpdate(View arg0)
		{
		}
	}

	/**
	 * 页卡切换监听
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener
	{

		int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量

		// int two = one * 2;// 页卡1 -> 页卡3 偏移量

		@Override
		public void onPageSelected(int arg0)
		{
			Animation animation = null;
			switch (arg0)
			{
			case 0:
				t1.setTextColor(Color.BLACK);
				t1.setTextSize(15);
				t2.setTextColor(Color.GRAY);
				t2.setTextSize(14);
				// 点击统计
				if (currIndex == 1)
				{
					animation = new TranslateAnimation(one, 0, 0, 0);
				}
				break;
			case 1:
				t2.setTextColor(Color.BLACK);
				t2.setTextSize(15);
				t1.setTextColor(Color.GRAY);
				t1.setTextSize(14);
				// 点击统计
				if(sendMM){
					sendMM=false;
				}
				if (currIndex == 0)
				{
					animation = new TranslateAnimation(offset, one, 0, 0);
				}
				break;
			}

			currIndex = arg0;
			animation.setFillAfter(true);// True:图片停在动画结束位置
			animation.setDuration(300);
			cursor.startAnimation(animation);
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2)
		{
		}

		@Override
		public void onPageScrollStateChanged(int arg0)
		{
		}
	}

	boolean sendMM= true;

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitle("税换记录");
		setRightVisible(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		}, "历史记录");
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mView = inflater.inflate(R.layout.coupon_history_layout, container, false);
		InitImageView();
		InitTextView();
		InitViewPager();
		return mView;
	}


	@Override
	public void onClick(View v)
	{

	}




}
