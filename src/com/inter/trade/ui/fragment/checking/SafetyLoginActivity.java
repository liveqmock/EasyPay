package com.inter.trade.ui.fragment.checking;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.inter.trade.R;
import com.inter.trade.VersionTask;
import com.inter.trade.log.Logger;
import com.inter.trade.ui.fragment.LoginFragment;
import com.inter.trade.util.Constants;

/**
 * 安全注册登录Activity
 * @author zhichao.huang
 *
 */
public class SafetyLoginActivity extends FragmentActivity {
	
	public static final String TAG = SafetyLoginActivity.class.getSimpleName();
	
	public static boolean isLoadMain = true;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		Logger.d(TAG, "onCreate");
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		if(getIntent() != null)
            isLoadMain = getIntent().getBooleanExtra("isLoadMain", true);
		
		setContentView(R.layout.regist_layout);
		new VersionTask(this, true,false).execute("");//检测新版本
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		if(getIntent()!=null){
			boolean isFirstLogin = getIntent().getBooleanExtra("isfirstlogin", false);
			if(isFirstLogin){
				fragmentTransaction.add(R.id.func_container, SafetyLoginFragment.getInstance(true));
			}else{
				fragmentTransaction.add(R.id.func_container, new SafetyLoginFragment());
			}
		}else{
			fragmentTransaction.add(R.id.func_container, new SafetyLoginFragment());
		}
		fragmentTransaction.commit();
	}

	@Override
	public View onCreateView(String name, Context context, AttributeSet attrs) {
		// TODO Auto-generated method stub
		return super.onCreateView(name, context, attrs);
	}
	
	 @Override
	 protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    	super.onActivityResult(requestCode, resultCode, data);
	    	
	    	if(resultCode == Constants.ACTIVITY_FINISH){
				setResult(Constants.ACTIVITY_FINISH);
				Log.i("Result", "SafetyLoginActivity finish()");
				finish();
			}
	    }

}
