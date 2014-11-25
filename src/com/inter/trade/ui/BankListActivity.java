package com.inter.trade.ui;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.inter.trade.R;
import com.inter.trade.ui.fragment.BankListFragment;
import com.inter.trade.ui.fragment.FragmentFactory;

public class BankListActivity extends FragmentActivity{
	public static final String BANK_NAME="BANK_NAME";
	public static final String BANK_ID="BANK_ID";
	public static final String BANK_NO="BANK_NO";
	public String bankname="";
	public String bankid = "";
	public String bankNo ="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.func_layout);
//		LoginUtil.detection(this);
		
		int index = 0;
		index = getIntent()==null?0:getIntent().getIntExtra(FragmentFactory.INDEX_KEY, 0);
		String type = getIntent().getStringExtra("type");
		if(type==null || "".equals(type)){
			type = "0";
		}
		
		/**
		 * 作为收款方还是付款方来读取的
		 * 付款方：f（屏蔽付款方禁用的银行）；
		 * 收款方：s（屏蔽收款方禁用的银行）;
		 * 默认为空：读取所有银行
		 */
		String readmode = "";
		if(getIntent() != null) {
			readmode = getIntent().getStringExtra(BankListFragment.READMODE_TAG);
		}
		
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.add( R.id.func_container,BankListFragment.create(type, readmode == null  ? "" : readmode));
		fragmentTransaction.commit();
	}
	@Override
	public void finish() {
		// TODO Auto-generated method stub
		Intent  intent = new Intent();
		intent.putExtra(BANK_ID, bankid);
		intent.putExtra(BANK_NAME, bankname);
		intent.putExtra(BANK_NO, bankNo);
		setResult(1, intent);
		super.finish();
		
	}
	
}
