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

import com.inter.trade.R;
import com.inter.trade.data.BaseData;
import com.inter.trade.data.CommonData;
import com.inter.trade.data.ResultData;
import com.inter.trade.ui.fragment.FragmentFactory;
import com.inter.trade.ui.fragment.coupon.BuySuccessFragment;
import com.inter.trade.ui.fragment.coupon.CouponBuyFragment;
import com.inter.trade.ui.fragment.coupon.ExchangeConfirmFragment;
import com.inter.trade.ui.fragment.coupon.HistoryMainFragment;
import com.inter.trade.ui.fragment.coupon.task.BuySuccessTask;
import com.inter.trade.util.Constants;
import com.inter.trade.util.LoginUtil;

public class CounponActivity extends FragmentActivity{
	public String mBankNo="";
	public ArrayList<HashMap<String, String>> mCommonData = new ArrayList<HashMap<String,String>>();
	private boolean isSuccess = false;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setTheme(R.style.DefaultLightTheme);
		setContentView(R.layout.func_layout);
		
//		LoginUtil.detection(this);
		
		int index = 0;
		index = getIntent()==null?0:getIntent().getIntExtra(FragmentFactory.INDEX_KEY, 0);
		double count = getIntent().getDoubleExtra("count", 0);
		String id = getIntent().getStringExtra("id");
		String shopName = getIntent().getStringExtra("shopname");
		HashMap<String, String> item = new HashMap<String, String>();
		item.put("所属商家", shopName);
		mCommonData.add(item);
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		if(index==0){
			fragmentTransaction.add( R.id.func_container, CouponBuyFragment.create(count,id));
		}else if(index ==1) {
			fragmentTransaction.add( R.id.func_container,new HistoryMainFragment());
		}else{
			fragmentTransaction.add( R.id.func_container,new ExchangeConfirmFragment());
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
			return;
		}
		
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
            isSuccess=true;
            resultData.putValue(ResultData.result, "success");
        } else if (str.equalsIgnoreCase("fail")) {
            msg = "支付失败！";
            resultData.putValue(ResultData.result, "failure");
        } else if (str.equalsIgnoreCase("cancel")) {
            msg = "用户取消了支付";
            resultData.putValue(ResultData.result, "cancel");
        }
        new BuySuccessTask(this, resultData,"ApiCouponInfo","couponSalePay").execute("");
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
	                	 FragmentTransaction transaction = CounponActivity.this.getSupportFragmentManager().beginTransaction();
	 	        		transaction.replace(R.id.func_container,BuySuccessFragment.createFragment(mCommonData));
	 	        		transaction.commit();
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
