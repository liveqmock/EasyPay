package com.inter.trade.ui.fragment.smsreceivepayment.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;

import com.inter.trade.R;
import com.inter.trade.ui.fragment.MyBankAddFragment;
import com.inter.trade.ui.fragment.MyBankCardMainFragment;
import com.inter.trade.util.Constants;

/**
 * 设置默认收款银行卡Activity
 * @author Lihaifeng
 *
 */
public class ReceiveBankCardActivity extends FragmentActivity{
//	public static String mBankNo="";
//	public static ArrayList<HashMap<String, String>> mCommonData = new ArrayList<HashMap<String,String>>();
//	public static QMoneyData moblieRechangeData = new QMoneyData();
	private boolean isSuccess = false;
	
	/**
	 * 0：选择已有账户
	 * 1：新增收款账户
	 */
	private String pageId = "";
	
	/**
	 * 启动当前Activity方式
	 * @param context
	 * @param data1
	 * @param data2
	 */
	public static void startMyActivity(Context context, String data1, String data2) {  
        Intent intent = new Intent(context, ReceiveBankCardActivity.class);  
        intent.putExtra("param1", data1);  
        intent.putExtra("param2", data2);  
        context.startActivity(intent);  
    }  
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setTheme(R.style.DialogStyleLight);
		setContentView(R.layout.func_layout);
		
//		LoginUtil.detection(this);
		
		pageId = getIntent().getStringExtra("param1");
		if(TextUtils.isEmpty(pageId)){
			return;
		}
		
		Fragment fm = null;
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		if(pageId.equals("0")){
			fm = new MyBankCardMainFragment();
		}else if(pageId.equals("1")){
			fm = new MyBankAddFragment();
		}
		if(fm != null){
			transaction.replace(R.id.func_container, fm);
			transaction.commit();
		}
	}

	@Override
	public View onCreateView(String name, Context context, AttributeSet attrs) {
		// TODO Auto-generated method stub
		return super.onCreateView(name, context, attrs);
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		
		if(keyCode == KeyEvent.KEYCODE_BACK){
			if(isSuccess){
				setResult(Constants.ACTIVITY_FINISH);
				finish();
			}
		}
		return super.onKeyDown(keyCode, event);
	}
}
