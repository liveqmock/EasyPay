/*
 * @Title:  CommonEasyCreditcardPayActivity.java
 * @Copyright:  XXX Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * @Description:  TODO<请描述此文件是做什么的>
 * @author:  MeiYi
 * @data:  2014年7月22日 上午9:05:50
 * @version:  V1.0
 */
package com.inter.trade.ui.creditcard;

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
 * 银行卡列表
 * @author  ChenGuangChi
 * @data:  2014年7月22日 上午9:05:50
 * @version:  V1.0
 */
public class MyBankCardActivity extends BaseFragmentActivity implements OnClickListener{
	
	private Button btn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(R.style.DialogStyleLight);
		setContentView(R.layout.func_layout);
		btn = (Button)findViewById(R.id.title_right_btn_three);
		btn.setOnClickListener(this);
		btn.setVisibility(View.VISIBLE);
		getSupportFragmentManager().beginTransaction().add(R.id.func_container, MyBankCardFragment.getInstance(getIntent())).commit();
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.title_right_btn_three://点击添加
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction().replace(R.id.func_container, MyBankAddFragment.create());
			ft.addToBackStack(null); 
			ft.commit();
			btn.setVisibility(View.INVISIBLE);
			break;
		default:
			break;
		}
	}
	
	/**
	 * 设置按钮的显示
	 * @param i
	 */
	public void setBtnVisibility(int i){
		btn.setVisibility(i);
	}
}