package com.inter.trade.ui.fragment.wallet;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.inter.trade.R;

public class DetialMonthActivity extends FragmentActivity{
	public static final String MONTH_INDEX = "MONTH_INDEX";
	public static final String MONTH_TYPE_STRING="MONTH_TYPE_STRING";
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.func_layout);
		
		
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		
		
		
		String index = "";
		index = getIntent()==null?"":getIntent().getStringExtra(MONTH_INDEX);
		String type = getIntent().getStringExtra(MONTH_TYPE_STRING);
		fragmentTransaction.add( R.id.func_container,DetialMonthListFragment.newInstance(index,type));
		fragmentTransaction.commit();
	}
	
}
