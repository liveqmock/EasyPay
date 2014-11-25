package com.inter.trade.ui.fragment.coupon.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.inter.trade.R;
import com.inter.trade.ui.fragment.coupon.HTBFragment;
import com.inter.trade.util.Constants;

/**
 * 汇通宝支付Activity
 * @author  chenguangchi
 * @data:  2014年8月25日 下午5:13:12
 * @version:  V1.0
 */
public class HTBActivity extends FragmentActivity{
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.func_layout);
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		Bundle bundle = getIntent().getBundleExtra("data");
		
		fragmentTransaction.add( R.id.func_container,HTBFragment.instance(bundle));
		fragmentTransaction.commit();
	}

	@Override
	public void finish() {
		super.finish();
	}
	
	
}
