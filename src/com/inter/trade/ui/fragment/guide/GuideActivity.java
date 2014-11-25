/*
 * @Title:  GuideActivity.java
 * @Copyright:  XXX Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * @Description:  TODO<请描述此文件是做什么的>
 * @author:  MeiYi
 * @data:  2014年11月6日 下午5:33:59
 * @version:  V1.0
 */
package com.inter.trade.ui.fragment.guide;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.IntentCompat;
import android.view.View;
import android.view.View.OnClickListener;

import com.inter.trade.R;
import com.inter.trade.ui.IndexActivity;
import com.inter.trade.ui.MainActivity;
import com.inter.trade.ui.fragment.FragmentFactory;
import com.inter.trade.ui.func.FuncMap;

/**
 * 引导界面
 * 
 * @author chenguangchi
 * @data: 2014年11月6日 下午5:33:59
 * @version: V1.0
 */
public class GuideActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide);
		findViewById(R.id.btn_try).setOnClickListener(this);
		findViewById(R.id.btn_surfing).setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		Intent intent=new Intent();
		switch (v.getId()) {
		case R.id.btn_try://前往手机充值界面
			intent.putExtra(FragmentFactory.INDEX_KEY, FuncMap.TELEPHONE_INDEX_FUNC_GUIDE);
			intent.setClass(this, IndexActivity.class);
			break;
		case R.id.btn_surfing://前往主页
	            intent.setClass(this, MainActivity.class);
	            intent.setFlags(IntentCompat.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
			break;

		default:
			break;
		}
		startActivity(intent);
		finish();
	}

}
