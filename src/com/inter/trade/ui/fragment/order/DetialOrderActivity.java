package com.inter.trade.ui.fragment.order;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.inter.trade.R;
import com.inter.trade.ui.fragment.order.util.OrderIndex;
import com.inter.trade.util.LoginUtil;

public class DetialOrderActivity extends FragmentActivity{
	public static final String ORDER_INDEX = "ORDER_INDEX";
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.func_layout);
		
//		LoginUtil.detection(this);
		
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		
		
		
		int index = 0;
		
		index = getIntent().getIntExtra(ORDER_INDEX, 0);
		int whichType = getIntent().getIntExtra(OrderIndex.ORDER_KEY_STRING, 0);
		fragmentTransaction.add( R.id.func_container,OrderDetial.newInstance(index,whichType));
		fragmentTransaction.commit();
	}
	
}
