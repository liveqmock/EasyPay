package com.inter.trade.ui.fragment.wallet;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.inter.trade.R;

public class DetialXiangqingActivity extends FragmentActivity{
	public static final String MONTH_INDEX = "MONTH_INDEX";
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.func_layout);
		String acctypeid = getIntent().getStringExtra("acctypeid");
		String accmonth = getIntent().getStringExtra("accmonth");
		String accincome = getIntent().getStringExtra("accincome");
		if(null == accincome || "null".equals(accincome)){
			accincome="0";
		}
		String accpayout = getIntent().getStringExtra("accpayout");
		if(null == accpayout || "null".equals(accpayout)){
			accpayout="0";
		}
		String acctypename = getIntent().getStringExtra("acctypename");
//		FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//		transaction.replace(R.id.func_container, DetialMonthFragment.newInstance(
//				acctypeid,
//				monthDatas.get(arg2).accmonth,
//				monthDatas.get(arg2).accincome,
//				monthDatas.get(arg2).accpayout
//				)
//				);
//		transaction.commit();
		
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		
		
		
		fragmentTransaction.add( R.id.func_container,DetialMonthFragment.newInstance(acctypeid, accmonth, accincome, accpayout,acctypename));
		fragmentTransaction.commit();
	}
	
}
