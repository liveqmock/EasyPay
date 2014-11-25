package com.inter.trade.ui;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;

import com.inter.trade.R;
import com.inter.trade.data.ResultData;
import com.inter.trade.ui.fragment.buyswipcard.BuySwipCardPayConfirmFragment;
import com.inter.trade.ui.fragment.buyswipcard.BuySwipCardPaySuccessFragment;
import com.inter.trade.ui.fragment.buyswipcard.util.BuySwipCardOrderData;
import com.inter.trade.ui.fragment.coupon.task.BuySuccessTask;
import com.inter.trade.util.Constants;

/**
 * 购买刷卡器，刷卡页面Activity
 * @author Administrator
 *
 */
public class BuySwipcardPayActivity extends FragmentActivity{
	public static String mBankNo="";
	public static ArrayList<HashMap<String, String>> mCommonData = new ArrayList<HashMap<String,String>>();
	public static BuySwipCardOrderData buySwipCardOrderData = new BuySwipCardOrderData();
	private boolean isSuccess = false;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		//禁止EditText自动弹出输入法
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		setContentView(R.layout.func_layout);
		
//		LoginUtil.detection(this);
		
		Bundle bundle = getIntent().getBundleExtra("product_card");
		
		FragmentManager fragmentManager = getSupportFragmentManager();
		
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		if(bundle != null){
			transaction.replace(R.id.func_container, new BuySwipCardPayConfirmFragment(bundle));
		}else{
			transaction.replace(R.id.func_container, new BuySwipCardPayConfirmFragment());
		}
		transaction.commit();
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
		payResult(arg2);
	}
	private void payResult(Intent data){
		/*
         * 支付控件返回字符串:success、fail、cancel
         *      分别代表支付成功，支付失败，支付取消
         */
		 if (data == null) {
	            return;
	     }
		String msg ="";
        final String str = data.getExtras().getString("pay_result");
        if(null == str){
        		return;
        }
        ResultData resultData = new ResultData();
        resultData.putValue(ResultData.bkntno, mBankNo);
        if (str.equalsIgnoreCase("success")) {
            msg = "支付成功！";
            isSuccess = true;
            resultData.putValue(ResultData.result, "success");
        } else if (str.equalsIgnoreCase("fail")) {
            msg = "支付失败！";
            resultData.putValue(ResultData.result, "failure");
        } else if (str.equalsIgnoreCase("cancel")) {
            msg = "用户取消了支付";
            resultData.putValue(ResultData.result, "cancel");
        }
        new BuySuccessTask(this, resultData,"ApiBuyOderInfo","orderPayrqStatus").execute("");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("支付结果通知");
        builder.setMessage(msg);
        builder.setInverseBackgroundForced(true);
        builder.setCancelable(false);
        //builder.setCustomTitle();
        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (str.equalsIgnoreCase("success")) {
	                	 FragmentTransaction transaction = BuySwipcardPayActivity.this.getSupportFragmentManager().beginTransaction();
	 	        		transaction.replace(R.id.func_container,BuySwipCardPaySuccessFragment.createFragment(mCommonData));
	 	        		transaction.commit();
                }else{
                		finish();
                }
               
            }
        });
        builder.create().show();
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
