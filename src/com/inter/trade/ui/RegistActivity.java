package com.inter.trade.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.view.View;

import com.inter.trade.R;
import com.inter.trade.log.Logger;
import com.inter.trade.ui.fragment.RigesterFragment;
import com.inter.trade.util.Constants;

public class RegistActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		Logger.d("RegistActivity", "onCreate");
		setTheme(R.style.DefaultLightTheme);
		setContentView(R.layout.func_layout);
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragmentTransaction.add(R.id.func_container, new RigesterFragment());
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
		
		if(resultCode == Constants.ACTIVITY_FINISH) {
			setResult(Constants.ACTIVITY_FINISH);
			finish();
		}
		
	}

}
