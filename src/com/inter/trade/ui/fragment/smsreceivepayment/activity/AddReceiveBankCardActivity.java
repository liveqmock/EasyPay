/*
 * @Title:  CommonEasyCreditcardPayActivity.java
 * @Copyright:  XXX Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * @Description:  TODO<请描述此文件是做什么的>
 * @author:  MeiYi
 * @data:  2014年7月22日 上午9:05:50
 * @version:  V1.0
 */
package com.inter.trade.ui.fragment.smsreceivepayment.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;

import com.inter.trade.R;
import com.inter.trade.qrsacncode.BaseFragmentActivity;
import com.inter.trade.ui.fragment.MyBankAddFragment;

/**
 * 新增默认收款账户
 * @author  hfli
 * @data:  2014年7月22日 上午9:05:50
 * @version:  V1.0
 */
public class AddReceiveBankCardActivity extends BaseFragmentActivity {
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(R.style.DialogStyleLight);
		setContentView(R.layout.func_layout);
		getSupportFragmentManager().beginTransaction().add(R.id.func_container, AddReceiveBankCardFragment.create()).commit();
	}

}