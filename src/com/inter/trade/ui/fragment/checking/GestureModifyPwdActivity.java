package com.inter.trade.ui.fragment.checking;

import com.inter.trade.R;
import com.inter.trade.ui.fragment.agent.AgentMainContentFragment;
import com.inter.trade.util.Constants;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;

/*
 * Author: Lihaifeng
 * 账户管理，修改登录密码，用手势密码修改登录密码
 */

public class GestureModifyPwdActivity extends FragmentActivity {
	private String phonenumber;
	
	/**
	 * 进入修改密码页面
	 */
	private void safetyModifyPwdFragment(){
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.func_container, SafetyModifyPwdFragment.createFragment(phonenumber, false));
		transaction.commit();
	}
	
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.func_layout);
		
		phonenumber = getIntent().getStringExtra(Constants.USER_NAME);
		
		safetyModifyPwdFragment();
	}

	@Override
	public View onCreateView(String name, Context context, AttributeSet attrs) {
		// TODO Auto-generated method stub
		return super.onCreateView(name, context, attrs);
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		
//		if(keyCode == KeyEvent.KEYCODE_BACK){
//				setResult(Constants.ACTIVITY_FINISH);
//				finish();
//		}
		return super.onKeyDown(keyCode, event);
	}
}
