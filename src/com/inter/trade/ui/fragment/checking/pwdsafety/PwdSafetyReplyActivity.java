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
 * 回答密保问题Activity
 * @author zhichao.huang
 *
 */
public class PwdSafetyReplyActivity extends FragmentActivity {
//	public static boolean mCleanFlag=true;
//	public static String phonenumber="";
	public static final String TAG = PwdSafetyReplyActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		Logger.d(TAG, "onCreate");
		setContentView(R.layout.func_layout);
		
//		if(getIntent() != null){
//			mCleanFlag=getIntent().getBooleanExtra(Constants.CLEAN_FLAG, true);
//		}else{
//			mCleanFlag=true;
//		}
//		mCleanFlag=getIntent().getBooleanExtra(Constants.CLEAN_FLAG, true);
//		phonenumber=getIntent().getBooleanExtra(Constants.CLEAN_FLAG, "");
		
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragmentTransaction.add(R.id.func_container, new PwdSafetyReplyFragment());
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
