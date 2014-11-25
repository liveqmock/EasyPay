package com.inter.trade.ui;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.inter.trade.R;
import com.inter.trade.adapter.GameRechargeGameAdapter;
import com.inter.trade.ui.fragment.gamerecharge.GameRechargeCompanyFragment;
import com.inter.trade.ui.fragment.gamerecharge.GameRechargeGameFragment;

/**
 * 游戏充值主activity
 * 
 * @author Lihaifeng
 * 
 */
public class GameRechargeMainActivity extends FragmentActivity implements
		OnCheckedChangeListener,OnClickListener {

	/** 记录选中的页面 */
	private int selected = 0;

	private RadioGroup rgNavigation;
	
	private List<Fragment> mList;
	
	private FragmentManager fm ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_gamecharge_main);
		initView();
		initFragment();

	}

	private void initFragment() {
		fm = getSupportFragmentManager();
		mList=new ArrayList<Fragment>();
		GameRechargeGameFragment gg=new GameRechargeGameFragment();
		GameRechargeCompanyFragment gc=new GameRechargeCompanyFragment();
		mList.add(gg);
		mList.add(gc);
		
	}

	private void initView() {
		rgNavigation = (RadioGroup) findViewById(R.id.rg_navigation);
		rgNavigation.setOnCheckedChangeListener(this);
		rgNavigation.getChildAt(selected).performClick();
		
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
		
		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		
		if(keyCode == KeyEvent.KEYCODE_BACK){
				finish();
			}
		return super.onKeyDown(keyCode, event);
	}

}
