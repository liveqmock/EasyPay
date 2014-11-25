package com.inter.trade.ui.fragment.checking;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.WindowManager;

import com.inter.trade.R;
import com.inter.trade.log.Logger;
import com.inter.trade.util.Constants;

/**
 * 用户切换页面
 * @author zhichao.huang
 *
 */
public class SafetyAccountChangeOnceActivity extends FragmentActivity {
	
	public static final String TAG = SafetyAccountChangeOnceActivity.class.getSimpleName();
	
	public static boolean isLoadMain = true;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		Logger.d(TAG, "onCreate");
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		
		setContentView(R.layout.regist_layout);
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragmentTransaction.add(R.id.func_container, new SafetyAccountChangeFragment());
		fragmentTransaction.commit();
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
