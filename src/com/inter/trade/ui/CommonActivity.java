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
import com.inter.trade.data.ResultData;
import com.inter.trade.ui.fragment.coupon.task.BuySuccessTask;
import com.inter.trade.ui.fragment.transfer.CheckFragment;
import com.inter.trade.ui.fragment.transfer.TransferFragment.TransferType;
import com.inter.trade.ui.fragment.transfer.TransferRecordFragment;
import com.inter.trade.ui.fragment.transfer.TransferSuccessFragment;
import com.inter.trade.ui.fragment.transfer.util.TransferData;
import com.inter.trade.util.Constants;
import com.inter.trade.util.LoginUtil;

public class CommonActivity extends FragmentActivity{
	
	public String mBankNo="";
	public ArrayList<HashMap<String, String>> mCommonData = new ArrayList<HashMap<String,String>>();
	public static TransferData mTransferData = new TransferData();
	private boolean isSuccess=false;
	public String mType ="";
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setTheme(R.style.DefaultLightTheme);
		setContentView(R.layout.func_layout);
		
//		LoginUtil.detection(this);
		
		mType = getIntent().getStringExtra(TransferRecordFragment.TYPE_STRING)==null?
				TransferType.USUAL_TRANSER:getIntent().getStringExtra(TransferRecordFragment.TYPE_STRING);
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.add( R.id.func_container, new CheckFragment());
		fragmentTransaction.commit();
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
//		final String str = "success";
        if(null == str){
        		return;
        }
        ResultData resultData = new ResultData();
        resultData.putValue(ResultData.bkntno, mBankNo);
        resultData.putValue("authorid", LoginUtil.mLoginStatus.authorid);
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
        if(TransferType.USUAL_TRANSER.equals(mType)){
        		new BuySuccessTask(this, resultData,"ApiPayinfo","insertTransferMoney").execute("");
        }else {
        	new BuySuccessTask(this, resultData,"ApiPayinfo","insertSupTransferMoney").execute("");
		}
        
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
	                	 FragmentTransaction transaction = CommonActivity.this.getSupportFragmentManager().beginTransaction();
	 	        		transaction.replace(R.id.func_container,TransferSuccessFragment.createFragment(mCommonData));
	 	        		transaction.commit();
                }
//                else{
//                		CommonActivity.this.finish();
//                }
               
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
