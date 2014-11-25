package com.inter.trade.ui.fragment.checking;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.view.View;

import com.inter.trade.R;
import com.inter.trade.log.Logger;
import com.inter.trade.ui.fragment.LoginFragment;

/**
 * 安全注册登录Activity
 * @author zhichao.huang
 *
 */
public class SafetyRigesterActivity extends FragmentActivity {
	
	public static final String TAG = SafetyRigesterActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		Logger.d(TAG, "onCreate");
		setContentView(R.layout.regist_layout);
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragmentTransaction.add(R.id.func_container, new SafetyRigesterFragment());
		fragmentTransaction.commit();
	}

	@Override
	public View onCreateView(String name, Context context, AttributeSet attrs) {
		// TODO Auto-generated method stub
		return super.onCreateView(name, context, attrs);
	}

}
