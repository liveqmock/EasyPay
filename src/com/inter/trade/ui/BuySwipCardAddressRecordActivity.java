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
import com.inter.trade.ui.fragment.FragmentFactory;
import com.inter.trade.ui.fragment.buyswipcard.BuySwipCardAddressRecordFragment;
import com.inter.trade.ui.fragment.buyswipcard.BuySwipCardRecordFragment;
import com.inter.trade.ui.fragment.telephone.TelephonePayRecordFragment;
import com.inter.trade.ui.fragment.transfer.TransferRecordFragment;
import com.inter.trade.ui.fragment.transfer.TransferFragment.TransferType;
import com.inter.trade.util.Constants;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PromptUtil;

/**
 * 购买刷卡器的历史地址
 * @author zhichao.huang
 *
 */
public class BuySwipCardAddressRecordActivity extends FragmentActivity{
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.func_layout);
		
		if(!LoginUtil.isLogin){
			PromptUtil.showLogin(this);
			finish();
			return;
		}
		
//		String type = getIntent().getStringExtra(TransferRecordFragment.TYPE_STRING)==null?
//				TransferType.USUAL_TRANSER:getIntent().getStringExtra(TransferRecordFragment.TYPE_STRING);
		
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		
		Bundle bundle = getIntent().getBundleExtra("product_card");
		if(bundle != null) {
			fragmentTransaction.add( R.id.func_container,new BuySwipCardAddressRecordFragment(bundle));
		}else{
			fragmentTransaction.add( R.id.func_container,new BuySwipCardAddressRecordFragment());
		}
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
		if(resultCode == Constants.ACTIVITY_FINISH){
			setResult(Constants.ACTIVITY_FINISH);
			finish();
		}
	}
	
	
}
