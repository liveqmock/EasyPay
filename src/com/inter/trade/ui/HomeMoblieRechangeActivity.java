package com.inter.trade.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.view.View;

import com.inter.trade.R;
import com.inter.trade.VersionTask;
import com.inter.trade.log.Logger;
import com.inter.trade.ui.fragment.telephone.HomeTelephonePayMainFragment;
import com.inter.trade.util.LoginUtil;

/**
 * 首页的手机充值页面Activity
 * @author zhichao.huang
 *
 */
public class HomeMoblieRechangeActivity extends FragmentActivity {
	
	public static final String TAG = HomeMoblieRechangeActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		Logger.d(TAG, "onCreate");
		setContentView(R.layout.func_layout);
		new VersionTask(HomeMoblieRechangeActivity.this, true,false).execute("");
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragmentTransaction.add(R.id.func_container, new HomeTelephonePayMainFragment());
		fragmentTransaction.commit();
	}

	@Override
	public View onCreateView(String name, Context context, AttributeSet attrs) {
		// TODO Auto-generated method stub
		return super.onCreateView(name, context, attrs);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		LoginUtil.mLoginStatus.cancel();
		LoginUtil.isLogin = false;
	}

}
