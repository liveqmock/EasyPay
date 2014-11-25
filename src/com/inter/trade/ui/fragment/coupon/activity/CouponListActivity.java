package com.inter.trade.ui.fragment.coupon.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.inter.trade.R;
import com.inter.trade.ui.fragment.coupon.CouponListFragment;
import com.inter.trade.util.LoginUtil;

public class CouponListActivity extends FragmentActivity{
	public String money=null;
	public String id = null;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.func_layout);
//		LoginUtil.detection(this);
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.add( R.id.func_container,new CouponListFragment());
		fragmentTransaction.commit();
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		
		Intent result = new Intent();
		result.putExtra("money", money);
		result.putExtra("id", id);
		setResult(1, result);
		
		super.finish();
		
	}
	
	
}
