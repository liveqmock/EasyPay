package com.inter.trade.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.inter.trade.R;
import com.inter.trade.data.ResultData;
import com.inter.trade.ui.fragment.airticket.AirTicketMainPagerFragment;
import com.inter.trade.ui.fragment.salaryget.SalaryGetHistoryFragment;
import com.inter.trade.ui.fragment.salaryget.SalaryGetMainFragment;
import com.inter.trade.ui.fragment.salarypay.task.DepositPayBackTask;
import com.inter.trade.ui.fragment.waterelectricgas.data.ResponseStateListener;
import com.inter.trade.ui.manager.core.IMainHandler;
import com.inter.trade.ui.manager.core.IMainHandlerManager;
import com.inter.trade.ui.manager.core.IMainHandlerProcess;
import com.inter.trade.ui.manager.core.UIConstantDefault;
import com.inter.trade.util.PromptUtil;

/**
 * UI Activity 管理类
 * 
 * @author zhichao.huang
 * 
 */
public class SalaryGetMainActivity extends FragmentActivity implements
		IMainHandlerProcess,ResponseStateListener {

	public static final String TAG = SalaryGetMainActivity.class
			.getSimpleName();

	private IMainHandler mainHandler;
	

	/**
	 * 默认Fragment
	 */
	public Fragment fragment;


	/**
	 * 目标Fragment的targetID
	 */
	public int targetID;

	/**
	 * 银联交易流水号
	 */
	public static String mBankNo = "";
	
	/**
	 * 发工资人数
	 */
	public static String personNumber;
	/**
	 * 发工资的总额
	 */
	public static String wageallmoney;
	/**
	 * 已支付的总额
	 */
	public static String paidmoney; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "onCreate");
		setTheme(R.style.DefaultLightTheme);
		mBankNo = "";

		mainHandler = new IMainHandler(this);
		IMainHandlerManager.regist(mainHandler);

		initViews();

		if (savedInstanceState != null) {
			Log.i(TAG, "onSaveInstanceState get");
			targetID = savedInstanceState.getInt("targetID");
		} else {
			targetID = getIntent().getIntExtra("targetFragment", 0);
			loadFirstFragment(switchUI(targetID, null));
			// switchFragment(switchUI(targetID, null), 1);
		}

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Log.i(TAG, "onSaveInstanceState put");
		outState.putInt("targetID", targetID);

	}

	/**
	 * 初始化
	 */
	private void initViews() {


		getSupportFragmentManager().addOnBackStackChangedListener(
				new OnBackStackChangedListener() {

					@Override
					public void onBackStackChanged() {
						Log.i(TAG,
								"onBackStackChanged() - getBackStackEntryCount():"
										+ getSupportFragmentManager()
												.getBackStackEntryCount());
						// if(getSupportFragmentManager().getBackStackEntryCount()
						// == 0) {
						// finish();
						// }
					}
				});
	}

	@Override
	public void handlerUI(int uiarg, int isAddToStack, Bundle data) {
		switchFragment(switchUI(uiarg, data), isAddToStack);
	}

	/**
	 * 切换Fragment
	 * 
	 * @param uitag
	 */
	private Fragment switchUI(int uitag, Bundle data) {
		switch (uitag) {

		case UIConstantDefault.UI_CONSTANT_SALARYGET_MAIN:

			fragment = new SalaryGetMainFragment();

			break;

		case UIConstantDefault.UI_CONSTANT_SALARYGET_HISTORY:

			fragment = new SalaryGetHistoryFragment();

			break;

		default:
			break;
		}
		return fragment;
	}

	/**
	 * 装载第一个fragment
	 * 
	 * @param targetFragment
	 */
	private void loadFirstFragment(Fragment targetFragment) {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		// ft.addToBackStack(null);
		ft.add(android.R.id.content, targetFragment);
		ft.commit();

	}

	/**
	 * 切换Fragment
	 * 
	 * @param targetFragment
	 *            目标Fragment
	 * @param isAddToBackStack
	 *            是否要添加到堆栈；=1 ：添加到堆栈；否则不添加到堆栈
	 */
	private void switchFragment(Fragment targetFragment, int isAddToBackStack) {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		if (isAddToBackStack == 1) {
			ft.addToBackStack(null);
		}
		ft.replace(android.R.id.content, targetFragment);
		ft.commit();
	}

	/**
	 * 从Stack移除Fragment
	 */
	public void removeFragmentToStack() {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				getSupportFragmentManager().popBackStack();
				// fragmentManager.executePendingTransactions();
				if (getSupportFragmentManager().getBackStackEntryCount() == 0 /**
				 * 
				 * || getSupportFragmentManager().getBackStackEntryCount() == 1
				 */
				) {
					finish();
				} else {
				}
			}
		});

	}

	/**
	 * 返回首页
	 */
	public void backHomeFragment() {
		getSupportFragmentManager().popBackStack(null,
				FragmentManager.POP_BACK_STACK_INCLUSIVE);
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);
		payResult(arg2);
	}

	private void payResult(Intent data) {
		/*
		 * 支付控件返回字符串:success、fail、cancel 分别代表支付成功，支付失败，支付取消
		 */
		if (data == null) {
			return;
		}
		String msg = "";
		final String str = data.getExtras().getString("pay_result");
		if (null == str) {
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
			PromptUtil.showNoticeDialog("温馨提示",msg,this);
		} else if (str.equalsIgnoreCase("cancel")) {
			msg = "用户取消了支付";
			resultData.putValue(ResultData.result, "cancel");
			PromptUtil.showNoticeDialog("温馨提示",msg,this);
		}
		new DepositPayBackTask(this,this).execute(mBankNo);
	}

	/**
	 * 用户在软键盘出现的时候，点击非EditText任一处则隐藏软键盘 点击空白隐藏软键盘
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {

			// 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
			View v = getCurrentFocus();

			if (isShouldHideInput(v, ev)) {
				hideSoftInput(v.getWindowToken());
			}
		}
		return super.dispatchTouchEvent(ev);
	}

	/**
	 * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏
	 * 
	 * @param v
	 * @param event
	 * @return
	 */
	private boolean isShouldHideInput(View v, MotionEvent event) {
		if (v != null && (v instanceof EditText)) {
			int[] l = { 0, 0 };
			v.getLocationInWindow(l);
			int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
					+ v.getWidth();
			if (event.getX() > left && event.getX() < right
					&& event.getY() > top && event.getY() < bottom) {
				// 点击EditText的事件，忽略它。
				return false;
			} else {
				return true;
			}
		}
		// 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
		return false;
	}

	/**
	 * 多种隐藏软件盘方法的其中一种
	 * 
	 * @param token
	 */
	private void hideSoftInput(IBinder token) {
		if (token != null) {
			InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			im.hideSoftInputFromWindow(token,
					InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		IMainHandlerManager.onDestroy();
	}

	@Override
	public void onSuccess(Object obj, Class cla) {
		Bundle bundle=new Bundle();
		bundle.putString("personnumber",personNumber);
		bundle.putString("wageallmoney",wageallmoney);
		bundle.putString("paidmoney",paidmoney);
		handlerUI(UIConstantDefault.UI_CONSTANT_SALARYPAY_SUCCESS, 1, null);
	}

}
