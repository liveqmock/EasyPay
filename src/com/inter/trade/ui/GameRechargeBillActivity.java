package com.inter.trade.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.gesture.Gesture;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.inter.trade.R;
import com.inter.trade.ui.fragment.agent.AgentPaySuccessFragment;
import com.inter.trade.ui.fragment.gamerecharge.GameRechargeBillFragment;
import com.inter.trade.ui.fragment.gamerecharge.data.BillData;
import com.inter.trade.ui.fragment.gamerecharge.dialog.SimpleMessageDialog;
import com.inter.trade.ui.fragment.gamerecharge.parser.PayBackParser;
import com.inter.trade.ui.fragment.gamerecharge.task.PayBackTask;
import com.inter.trade.ui.fragment.waterelectricgas.data.ResponseStateListener;
import com.inter.trade.util.Constants;
import com.inter.trade.view.styleddialog.SimpleDialogFragment;

/**
 * 游戏充值账单Activity
 * @author Lihaifeng
 *
 */
public class GameRechargeBillActivity extends FragmentActivity implements ResponseStateListener{
	private boolean isSuccess = false;
	private	Bundle bundle;
	private GameRechargeBillFragment fragment;
	private PayBackTask task;
	private BillData billData;
	private static final String TAG="GameRechargeBillActivity";
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setTheme(R.style.DefaultLightTheme);
		setContentView(R.layout.func_layout);
		
		if(arg0!=null){
			 bundle = arg0.getBundle("data");
		}else{
			 bundle = getIntent().getBundleExtra("data");
		}
		
		FragmentManager fragmentManager = getSupportFragmentManager();
		
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		
		if(bundle != null){
			fragment=new GameRechargeBillFragment(bundle);
		}else{
			fragment=new GameRechargeBillFragment();
		}
		transaction.replace(R.id.func_container, fragment);
		transaction.commit();
	}

	@Override
	public View onCreateView(String name, Context context, AttributeSet attrs) {
		return super.onCreateView(name, context, attrs);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putBundle("data", bundle);
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent data) {
		if (data == null) {
			Log.i(TAG, "data is null");
			return;
		}
		String str = data.getExtras().getString("pay_result");
		if (null == str) {
			Log.i(TAG, "str is null");
			return;
		}
		
		if (str.equalsIgnoreCase("success")) {// 支付成功！
			System.out.println("支付成功！");
			String bankid = data.getStringExtra(BankListActivity.BANK_ID);
			String bankname = data.getStringExtra(BankListActivity.BANK_NAME);
			String bankno = data.getStringExtra(BankListActivity.BANK_NO);
			billData = fragment.getBillData();
			if(billData!=null){
				task=new PayBackTask(this,this);
				task.execute(billData.getBkntno());
				//task.execute("201407071810240097802");
			}

		} else if (str.equalsIgnoreCase("fail")) {// 支付失败！
			//SimpleDialogFragment.createBuilder(this, getSupportFragmentManager()).setTitle("支付结果通知").setMessage("支付失败！").show();
			 AlertDialog.Builder builder = new AlertDialog.Builder(this);
		        builder.setTitle("支付结果通知");
		        builder.setMessage("支付失败");
		        builder.setInverseBackgroundForced(true);
		        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
		            @Override
		            public void onClick(DialogInterface dialog, int which) {
		                dialog.dismiss();
		                GameRechargeBillActivity.this.finish();
		            }
		        });
		        builder.create().show();
		} else if (str.equalsIgnoreCase("cancel")) {// 用户取消了支付
			System.out.println("用户取消了支付");
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
		        builder.setTitle("支付结果通知");
		        builder.setMessage("您取消了支付！");
		        builder.setInverseBackgroundForced(true);
		        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
		            @Override
		            public void onClick(DialogInterface dialog, int which) {
		                dialog.dismiss();
		                GameRechargeBillActivity.this.finish();
		            }
		        });
		        builder.create().show();
			//SimpleMessageDialog.show(getSupportFragmentManager(), "温馨提示", "您取消了支付！");
		}
	}
	
	private void paySuccess() {
		Bundle b=new Bundle();
		b.putString("gamename",bundle.getString("gamename"));
		b.putString("bankno",fragment.getBankNo());
		b.putString("money",billData.getTotalPrice());
		
		Intent intent = new Intent(this,
				GameRechargeSuccessActivity.class);
		intent.putExtra("data", b);
		startActivity(intent);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		if(keyCode == KeyEvent.KEYCODE_BACK){
			if(isSuccess){
				setResult(Constants.ACTIVITY_FINISH);
				finish();
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onSuccess(Object obj, Class cla) {
		paySuccess();
	}
}
