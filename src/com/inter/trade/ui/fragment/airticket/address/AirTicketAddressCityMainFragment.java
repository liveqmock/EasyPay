package com.inter.trade.ui.fragment.airticket.address;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.inter.trade.R;
import com.inter.trade.ui.GameRechargeRecordActivity;
import com.inter.trade.ui.fragment.BaseFragment;

/**
 * 飞机票城市选择主Fragment
 * @author Lihaifeng
 *
 */
public class AirTicketAddressCityMainFragment extends BaseFragment implements
OnCheckedChangeListener,OnClickListener{
	
	/** 记录选中的页面 */
	private int selected = 0;

	private RadioGroup rgNavigation;
	
	private List<Fragment> mList;
	
	private FragmentManager fm ;
	
	private Bundle data = null;
	
	/**
	 * 城市：判断是出发还是到达城市
	 */
	private String city;

	public static AirTicketAddressCityMainFragment newInstance (Bundle data) {
		AirTicketAddressCityMainFragment fragment = new AirTicketAddressCityMainFragment();
		fragment.setArguments(data);
		return fragment;
	}
	
	public AirTicketAddressCityMainFragment()
	{
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
//		setRightVisible(this,R.drawable.icon_list);
		Bundle bundle = getArguments();
		if(bundle != null) {
			data = bundle;
			city = data.getString("city");
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if(rgNavigation!=null){
			rgNavigation.getChildAt(selected).performClick();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//LoginUtil.detection(getActivity());
		
		View view = inflater.inflate(R.layout.airticket_city_main, container,false);
		initView(view);
		initFragment();
		setTitle("城市选择");
		
//		setBackVisible();
		
		return view;
	}
	
	private void initFragment() {
		fm = getChildFragmentManager();
		mList=new ArrayList<Fragment>();
		AirTicketAddressDepartCityFragment gg= AirTicketAddressDepartCityFragment.newInstance(data);
		AirTicketAddressDepartCityFragment gc= AirTicketAddressDepartCityFragment.newInstance(data);
		mList.add(gc);
		mList.add(gg);
		
	}

	private void initView(View view) {
		rgNavigation = (RadioGroup) view.findViewById(R.id.rg_navigation);
		rgNavigation.setOnCheckedChangeListener(this);
		
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.rb_game_list://按出发
			selected=0;
			break;
		case R.id.rb_company_list://按到达
			selected=1;
			break;
		default:
			break;
		}
		if(mList!=null){
			FragmentTransaction ft = fm.beginTransaction();
			ft.replace(R.id.fl_container, mList.get(selected));
			ft.commit();
		}
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_right_btn_two:
			Intent intent =new Intent(getActivity(),GameRechargeRecordActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}
		
	}
	
}
