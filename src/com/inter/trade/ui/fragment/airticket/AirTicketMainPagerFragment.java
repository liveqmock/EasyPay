package com.inter.trade.ui.fragment.airticket;


import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.inter.trade.R;
import com.inter.trade.ui.fragment.airticket.util.ApiAirticketGetCityData;
import com.inter.trade.ui.manager.IBaseFragment;
import com.inter.trade.ui.manager.UIManagerActivity;
import com.inter.trade.ui.manager.core.UIConstantDefault;
import com.inter.trade.util.DateUtil;

/**
 * 飞机票首页
 * @author zhichao.huang
 *
 */
public class AirTicketMainPagerFragment extends IBaseFragment implements OnClickListener{
	
	private static final String TAG = AirTicketMainPagerFragment.class.getName();
	
	private View rootView;
	
	/**
	 * 单程按钮
	 */
	private Button danchengButton;
	
	/**
	 * 往返按钮
	 */
	private Button wangfanButton;
	
//	private LinearLayout danchengLayout;
//	
//	private LinearLayout wangfanLayout;
	
	private ImageView air_left_triangle, air_right_triangle;
	
	private Bundle data = null;
	
	public static AirTicketMainPagerFragment newInstance (Bundle data) {
		AirTicketMainPagerFragment fragment = new AirTicketMainPagerFragment();
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
		rootView = inflater.inflate(R.layout.airticket_main_layout, container,false);
		initViews(rootView);
		return rootView;
	}

	@Override
	protected void onAsyncLoadData() {
		
	}

	@Override
	public void onRefreshDatas() {
		Log.i(TAG, "onRefreshDatas");
		((UIManagerActivity)getActivity()).setTopTitle("机票查询");
		((UIManagerActivity)getActivity()).setRightButtonIconOnClickListener(View.VISIBLE , new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				addFragmentToStack(UIConstantDefault.UI_CONSTANT_AIR_TICKET_ORDER_HISTORY_LIST, 1, data);
			}
		});
		if(currentFragment != null) {
			currentFragment.onRefreshDatas();
		}
		
		if(getActivity() != null){
			((UIManagerActivity)getActivity()).tilte_line.setVisibility(View.INVISIBLE);
		}
		
	}

	private void initViews (View rootView) {
		danchengButton = (Button)rootView.findViewById(R.id.dancheng);
		wangfanButton = (Button)rootView.findViewById(R.id.wangfan);
		air_left_triangle = (ImageView)rootView.findViewById(R.id.air_left_triangle);
		air_right_triangle = (ImageView)rootView.findViewById(R.id.air_right_triangle);
		
		danchengButton.setOnClickListener(this);
		wangfanButton.setOnClickListener(this);
		
		initCityData();
		
		setDanchengSelecte();
		if(danFragment == null) danFragment = AirTicketMainDanchengFragment.newInstance(null);
		switchFragment(danFragment);
	}
	
	/**
	 * 初始化城市数据
	 */
	private void initCityData() {
		ApiAirticketGetCityData airticketStartCityData = new ApiAirticketGetCityData();
		airticketStartCityData.setCityNameCh("北京");
		airticketStartCityData.setCityCode("BJS");
		airticketStartCityData.setCityId("1");
		((UIManagerActivity)getActivity()).dancheng_start_data = airticketStartCityData;
		
		ApiAirticketGetCityData airticketEndCityData = new ApiAirticketGetCityData();
		airticketEndCityData.setCityNameCh("上海");
		airticketEndCityData.setCityCode("SHA");
		airticketEndCityData.setCityId("2");
		((UIManagerActivity)getActivity()).dancheng_end_data = airticketEndCityData;
		
		String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		((UIManagerActivity)getActivity()).airTicketDate = DateUtil.getSpecifiedDayAfter(dateStr);
		((UIManagerActivity)getActivity()).airTicketStartDate = DateUtil.getSpecifiedDayAfter(dateStr);
		((UIManagerActivity)getActivity()).airTicketFanDate = DateUtil.getSpecified2DayAfter(dateStr);
	}
	
	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.dancheng:
			setDanchengSelecte();
			if(danFragment == null) danFragment = AirTicketMainDanchengFragment.newInstance(null);
			switchFragment(danFragment);
			break;
		case R.id.wangfan:
			setWangfanSelecte();
			if(wangFragment == null ) wangFragment = AirTicketMainWangfanFragment.newInstance(null);
			switchFragment(wangFragment);
			break;
		default:
			break;
		}
		
	}
	
	@Override
	public void onPause() {
		super.onPause();
		((UIManagerActivity)getActivity()).setRightButtonIconOnClickListener(View.GONE ,null);
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

	private void setDanchengSelecte() {
		danchengButton.setSelected(true);
		wangfanButton.setSelected(false);
		air_left_triangle.setVisibility(View.VISIBLE);
		air_right_triangle.setVisibility(View.INVISIBLE);
	}
	
	private void setWangfanSelecte() {
		danchengButton.setSelected(false);
		wangfanButton.setSelected(true);
		air_left_triangle.setVisibility(View.INVISIBLE);
		air_right_triangle.setVisibility(View.VISIBLE);
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
			transaction.add(R.id.airticket_dancheng_container, to).commit();
			currentFragment = to;
			return;
		}
		
		if (currentFragment != to) {

//			to.getLoaderManager().hasRunningLoaders();
			// 先判断是否被add过
			if (!to.isAdded()) {

				// 隐藏当前的fragment，add下一个到Activity中
				transaction.hide(currentFragment).add(R.id.airticket_dancheng_container, to).commit();
			} else {
				// 隐藏当前的fragment，显示下一个
				transaction.hide(currentFragment).show(to).commit();
			}
		}
		currentFragment = to;
	}
	
	private void switchFragment(IBaseFragment targetFragment){
		
		if(targetFragment == currentFragment) {
			return;
		}
		
		FragmentTransaction ft = getActivity().getSupportFragmentManager()
				.beginTransaction();
    	ft.replace(R.id.airticket_dancheng_container, targetFragment);
    	ft.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)/**.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)*/;
    	
    	ft.commit();
    	
    	currentFragment = targetFragment;
    }
	
	/**
	 * 当前Fragment
	 */
	private IBaseFragment currentFragment = null;
	
	private IBaseFragment danFragment = null;
	private IBaseFragment wangFragment = null;

}
