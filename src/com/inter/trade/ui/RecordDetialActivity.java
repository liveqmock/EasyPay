package com.inter.trade.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.view.View;

import com.inter.trade.R;
import com.inter.trade.ui.fragment.cridet.CridetRecordDetialFragment;
import com.inter.trade.util.LoginUtil;

/**
 * 历史纪录的详情
 * @author apple
 *
 */

public class RecordDetialActivity extends FragmentActivity{
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.func_layout);
		
//		LoginUtil.detection(this);
		
		
		FragmentManager fragmentManager = getSupportFragmentManager();
		
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		
			fragmentTransaction.add( R.id.func_container,new CridetRecordDetialFragment());
		fragmentTransaction.commit();
	}

	@Override
	public View onCreateView(String name, Context context, AttributeSet attrs) {
		// TODO Auto-generated method stub
		return super.onCreateView(name, context, attrs);
	}
	
}
