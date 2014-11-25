/**
 * 
 */
package com.inter.trade.ui;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;

import com.inter.trade.R;
import com.inter.trade.db.DaoMaster;
import com.inter.trade.db.DaoMaster.DevOpenHelper;
import com.inter.trade.ui.fragment.checking.LockActivity;
import com.inter.trade.ui.fragment.checking.SafetyAccountChangeActivity;
import com.inter.trade.ui.fragment.checking.SafetyLoginActivity;
import com.inter.trade.util.Constants;
import com.inter.trade.util.PreferenceConfig;
import com.inter.trade.util.ShortCutUtils;

/**
 * @author LiGuohui
 * @since 2012-11-19 下午9:24:01
 * @version 1.0.0
 */
public class Splash extends FragmentActivity implements Callback
{
	private Handler mHandler;
	public static final int TO_MAIN = 0;
	private Resources resources;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_safety_main);
		resources = getResources();
		String appName = resources.getString(R.string.app_name);
		if (!ShortCutUtils.hasShortcut(this, appName))
		{
			ShortCutUtils
					.creatShortCut(this, appName, R.drawable.easy_pay_logo);// 创建快捷方式
		}

		mHandler = new Handler(this);

		new Timer().schedule(new TimerTask()
		{

			@Override
			public void run()
			{
				Message msg = mHandler.obtainMessage(TO_MAIN);
				mHandler.sendMessage(msg);
			}
		}, 2000);
	}

	@Override
	public boolean handleMessage(Message msg)
	{
		if (msg.what == TO_MAIN)
		{
			createDB();
			checkNativeUserInfo();
		}
		return false;
	}

	/**
	 * 建立数据库等操作
	 * 
	 * @throw
	 * @return void
	 */
	private void createDB()
	{
		DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "notes-db",
				null);
		PayApp.setHelper(helper);
	}

	/**
	 * 检查本地用户状态
	 */
	private void checkNativeUserInfo()
	{
		String user_name = PreferenceConfig.instance(this).getString(
				Constants.USER_NAME, "");
		String user_gesture_pwd = PreferenceConfig.instance(this).getString(
				Constants.USER_GESTURE_PWD, "");
		// 进入手势密码登录页面
		Intent intent = new Intent();
		if (!"".equals(user_name) && !"".equals(user_gesture_pwd))
		{
			intent.setClass(Splash.this, LockActivity.class);
		}
		else if (!"".equals(user_name))
		{
			// 进入系统登录页面
			intent.setClass(Splash.this, SafetyAccountChangeActivity.class);
		}
		else
		{
			// 进入系统登录页面
			intent.putExtra("isfirstlogin", true);
			intent.setClass(Splash.this, SafetyLoginActivity.class);
		}
		startActivity(intent);
		finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
