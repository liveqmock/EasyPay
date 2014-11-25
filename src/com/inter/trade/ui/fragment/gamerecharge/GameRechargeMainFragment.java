package com.inter.trade.ui.fragment.gamerecharge;

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
 * 游戏充值主Fragment
 * @author Lihaifeng
 *
 */
public class GameRechargeMainFragment extends BaseFragment implements
OnCheckedChangeListener,OnClickListener{
	
	/** 记录选中的页面 */
	private int selected = 0;

	private RadioGroup rgNavigation;
	
	private List<Fragment> mList;
	
	private FragmentManager fm ;

	
	public GameRechargeMainFragment()
	{
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setRightVisible(this,R.drawable.icon_list);
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
		
		View view = inflater.inflate(R.layout.activity_gamecharge_main, container,false);
		initView(view);
		initFragment();
		setTitle("游戏充值");
		
//		setBackVisible();
		
		return view;
	}
	
	private void initFragment() {
		fm = getChildFragmentManager();
		mList=new ArrayList<Fragment>();
		GameRechargeGameFragment gg=new GameRechargeGameFragment();
		GameRechargeCompanyFragment gc=new GameRechargeCompanyFragment();
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
		case R.id.rb_game_list://按游戏
			selected=0;
			break;
		case R.id.rb_company_list://按公司
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
