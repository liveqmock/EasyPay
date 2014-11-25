package com.inter.trade.ui;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;

import com.inter.trade.R;
import com.inter.trade.ui.fragment.MyBankCardMainFragment;
import com.inter.trade.ui.fragment.agent.AgentApplyFragmentNew;
import com.inter.trade.ui.fragment.agent.AgentQueryFragment;
import com.inter.trade.ui.fragment.qmoney.util.QMoneyData;
import com.inter.trade.util.Constants;

/**
 * （新版）我的银行卡Activity
 * @author Lihaifeng
 *
 */
public class MyBankCardActivity extends FragmentActivity{
//	public static String mBankNo="";
//	public static ArrayList<HashMap<String, String>> mCommonData = new ArrayList<HashMap<String,String>>();
//	public static QMoneyData moblieRechangeData = new QMoneyData();
	private boolean isSuccess = false;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setTheme(R.style.DialogStyleLight);
		setContentView(R.layout.func_layout);
		
//		LoginUtil.detection(this);
		
//		Bundle bundle = getIntent().getBundleExtra("AgentMain");
		
		FragmentManager fragmentManager = getSupportFragmentManager();
		
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		
		transaction.replace(R.id.func_container, new MyBankCardMainFragment());
		
		transaction.commit();
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
		
		if(keyCode == KeyEvent.KEYCODE_BACK){
			if(isSuccess){
				setResult(Constants.ACTIVITY_FINISH);
				finish();
			}
		}
		return super.onKeyDown(keyCode, event);
	}
}
