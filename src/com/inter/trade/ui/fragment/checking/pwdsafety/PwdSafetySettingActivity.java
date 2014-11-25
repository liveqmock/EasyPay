package com.inter.trade.ui.fragment.checking.pwdsafety;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.inter.trade.R;
import com.inter.trade.log.Logger;
import com.inter.trade.ui.fragment.LoginFragment;
import com.inter.trade.util.Constants;

/**
 * 设置密保问题Activity
 * @author zhichao.huang
 *
 */
public class PwdSafetySettingActivity extends FragmentActivity {
	
	public static final String TAG = PwdSafetySettingActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		Logger.d(TAG, "onCreate");
		setContentView(R.layout.func_layout);
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragmentTransaction.add(R.id.func_container, new PwdSafetySettingFragment());
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
