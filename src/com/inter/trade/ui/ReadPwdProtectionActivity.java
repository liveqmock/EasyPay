package com.inter.trade.ui;

import java.util.ArrayList;

import com.inter.trade.R;
import com.inter.trade.ui.fragment.ReadPwdProtectionTask;
import com.inter.trade.ui.fragment.agent.AgentMainContentFragment;
import com.inter.trade.ui.fragment.checking.pwdsafety.PwdSafetyReplyCheckFragment;
import com.inter.trade.ui.fragment.checking.util.PwdSafetyValidateUserData;
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
 * 账户管理，修改登录密码，用密保修改登录密码
 */

public class ReadPwdProtectionActivity extends FragmentActivity {
	private ArrayList<PwdSafetyValidateUserData> mList;
	private String phonenumber;
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.func_layout);
		
		phonenumber = ReadPwdProtectionTask.phonenumber;;
		mList=ReadPwdProtectionTask.mList;
		
		pwdSafetyReplyCheckFragment();
	}

	@Override
	public View onCreateView(String name, Context context, AttributeSet attrs) {
		// TODO Auto-generated method stub
		return super.onCreateView(name, context, attrs);
	}

	private void pwdSafetyReplyCheckFragment(){
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.func_container, new PwdSafetyReplyCheckFragment(mList, phonenumber, false));
		transaction.commit();
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
