package com.inter.trade.ui;

import com.inter.trade.R;
import com.inter.trade.ui.func.MainFragment;
import com.inter.trade.ui.views.AnimTabsView;
import com.inter.trade.util.PromptUtil;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;

/**
 * 新版主activity
 * @author zhichao.huang
 *
 */
public class TFBMainActivity extends FragmentActivity {
	
	private AnimTabsView mTabsView;
	
	private ViewPager viewpage;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		setContentView(R.layout.default_main_layout);
		
		mTabsView = (AnimTabsView)findViewById(R.id.publiclisten_tab);
		mTabsView.addItem("收款");
		mTabsView.addItem("查询");
		mTabsView.addItem("支付");
		//			mTabsView.addItem("DJ节目");

		mTabsView.setOnAnimTabsItemViewChangeListener(new AnimTabsView.IAnimTabsItemViewChangeListener() {
			@Override
			public void onChange(AnimTabsView tabsView, final int oldPosition, int currentPosition) {
				PromptUtil.showToast(getApplicationContext(), "oldPosition:"+oldPosition);
				Log.i("demo currentPosition:", currentPosition+"");
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						viewpage.setCurrentItem(oldPosition);
					}
				});
				
			}
		});
		
		viewpage = (ViewPager)findViewById(R.id.viewpage);
		
		MyPagerAdapter adpter =new MyPagerAdapter(getSupportFragmentManager());
		viewpage.setAdapter(adpter);
		viewpage.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				if(mTabsView != null){
					mTabsView.selecteItem(arg0);
				}
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		//default_new_home_pay.xml 新的支付layout布局
		
	}
	
	public class MyPagerAdapter extends FragmentPagerAdapter {

		private final String[] TITLES = { "收款", "查询", "支付" };

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}
		
		@Override
		public CharSequence getPageTitle(int position) {
			return TITLES[position];
		}

		@Override
		public int getCount() {
			return TITLES.length;
		}

		@Override
		public Fragment getItem(int position) {
			//加载fragment页
			Fragment fragment=null;
			switch(position){
			case 0:
				fragment = new MainFragment();
				break;
			case 1:
				fragment = new MainFragment();
				break;
			case 2:
				fragment = new MainFragment();
				break;
			
			}
			return fragment;
		}

		
		/* FragmentPagerAdapter不用每次都实例化fragments,
		 * 不要使用switch。可以把fragment放在一个列表中，然后再从列表中返回。
		 * 
		 * List<Fragment> fragments;

			public Fragment getItem(int pos) {
			  return fragments.get(pos);
			}
			
			public void addFragment(Fragment f) {
			  fragments.add(f);
			}
		 */
	}
}
