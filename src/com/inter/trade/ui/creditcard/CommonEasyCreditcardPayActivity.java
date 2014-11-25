/*
 * @Title:  CommonEasyCreditcardPayActivity.java
 * @Copyright:  XXX Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * @Description:  TODO<请描述此文件是做什么的>
 * @author:  MeiYi
 * @data:  2014年7月22日 上午9:05:50
 * @version:  V1.0
 */
package com.inter.trade.ui.creditcard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;

import com.inter.trade.R;
import com.inter.trade.util.Constants;

/**
 * 信用卡支付信息填写页面
 * @author  ChenGuangChi
 * @data:  2014年7月22日 上午9:05:50
 * @version:  V1.0
 */
public class CommonEasyCreditcardPayActivity extends FragmentActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(R.style.DialogStyleLight);
		setContentView(R.layout.func_layout_new);
		getSupportFragmentManager().beginTransaction().add(R.id.func_container, CommonEasyCreditcardPayFragment.newInstance(getIntent())).commit();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		
		if(keyCode == KeyEvent.KEYCODE_BACK){
				finish();
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, arg2);
//		payResult(arg2);
		if(resultCode == Constants.ACTIVITY_FINISH){
			finish();
		}
	}

	@Override
	public void finish() {
		super.finish();
		setResult(Constants.ACTIVITY_FINISH);
	}
	
	
}