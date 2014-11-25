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
import android.view.View;

import com.inter.trade.R;
import com.inter.trade.data.ResultData;
import com.inter.trade.ui.fragment.coupon.task.BuySuccessTask;
import com.inter.trade.ui.fragment.cridet.CridetCardFragment;
import com.inter.trade.ui.fragment.cridet.CridetSuccessFragment;
import com.inter.trade.ui.fragment.cridet.SwipCardFragment;
import com.inter.trade.ui.fragment.cridet.SwipCardFragmentFuLinMen;
import com.inter.trade.util.Constants;

public class CridetPayActivity extends FragmentActivity{
	public String mBankNo="";
	public ArrayList<HashMap<String, String>> mCommonData = new ArrayList<HashMap<String,String>>();
	public String paytype;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setTheme(R.style.DialogStyleLight);
		setContentView(R.layout.func_layout);
		
//		LoginUtil.detection(this);
		paytype = getIntent().getStringExtra("paytype");
		
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		
		if(paytype.equals(CridetCardFragment.PAYTYPE_FULINMEN)) {//付临门通道
			fragmentTransaction.add( R.id.func_container, new SwipCardFragmentFuLinMen());
		} else {
			fragmentTransaction.add( R.id.func_container, new SwipCardFragment());
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
//		payResult(arg2);
		if(resultCode == Constants.ACTIVITY_FINISH){
			setResult(Constants.ACTIVITY_FINISH);
			finish();
		}
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
            resultData.putValue(ResultData.result, "success");
        } else if (str.equalsIgnoreCase("fail")) {
            msg = "支付失败！";
            resultData.putValue(ResultData.result, "failure");
        } else if (str.equalsIgnoreCase("cancel")) {
            msg = "用户取消了支付";
            resultData.putValue(ResultData.result, "cancel");
        }
        new BuySuccessTask(this, resultData,"ApiPayinfo","insertcreditCardMoney").execute("");
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
	                	 FragmentTransaction transaction = CridetPayActivity.this.getSupportFragmentManager().beginTransaction();
	 	        		transaction.replace(R.id.func_container,CridetSuccessFragment.createFragment(mCommonData));
	 	        		transaction.commit();
                }else{
                	setResult(Constants.ACTIVITY_FINISH);
                		CridetPayActivity.this.finish();
                }
               
            }
        });
        builder.create().show();
	}
	
}
