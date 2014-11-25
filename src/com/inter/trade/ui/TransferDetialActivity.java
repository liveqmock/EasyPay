package com.inter.trade.ui;

import com.inter.trade.R;
import com.inter.trade.ui.fragment.FragmentFactory;
import com.inter.trade.ui.fragment.HelpDetialFragment;
import com.inter.trade.util.LoginUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.view.View;

public class TransferDetialActivity extends FragmentActivity{
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setTheme(R.style.DefaultLightTheme);
		setContentView(R.layout.func_layout);
		
//		LoginUtil.detection(this);
		Intent intent = getIntent();
		String date = intent.getStringExtra("date");
		String content = intent.getStringExtra("content");
		String title = intent.getStringExtra("title");
		
		
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.add( R.id.func_container,HelpDetialFragment.create(title, date, content));
		fragmentTransaction.commit();
	}

	@Override
	public View onCreateView(String name, Context context, AttributeSet attrs) {
		// TODO Auto-generated method stub
		return super.onCreateView(name, context, attrs);
	}
	
}
