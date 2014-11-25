package com.inter.trade.ui;

import com.inter.trade.R;
import com.inter.trade.ui.fragment.FragmentFactory;
import com.inter.trade.ui.func.FuncWorker;
import com.inter.trade.util.Constants;
import com.inter.trade.util.LoginUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.view.View;

public class IndexActivity extends FragmentActivity{
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.func_layout);
		
//		LoginUtil.detection(this);
		
		
		int index = 0;
		index = getIntent()==null?0:getIntent().getIntExtra(FragmentFactory.INDEX_KEY, 0);
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.add( R.id.func_container,new FuncWorker().createFragment(index,null));
		fragmentTransaction.commit();
	}

	@Override
	public View onCreateView(String name, Context context, AttributeSet attrs) {
		// TODO Auto-generated method stub
		return super.onCreateView(name, context, attrs);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, arg2);
		if(resultCode==Constants.ACTIVITY_FINISH){
			finish();
		}
	}
	
}
