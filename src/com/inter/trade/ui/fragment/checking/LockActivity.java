package com.inter.trade.ui.fragment.checking;

import java.util.List;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.inter.protocol.ProtocolData;
import com.inter.protocol.ProtocolRsp;
import com.inter.protocol.util.ProtocolUtil;
import com.inter.trade.R;
import com.inter.trade.VersionTask;
import com.inter.trade.data.CommonData;
import com.inter.trade.data.ResponseData;
import com.inter.trade.log.Logger;
import com.inter.trade.net.HttpUtil;
import com.inter.trade.ui.MainActivity;
import com.inter.trade.ui.fragment.checking.LockPatternView.Cell;
import com.inter.trade.ui.fragment.checking.LockPatternView.DisplayMode;
import com.inter.trade.ui.fragment.checking.util.LockPatternUtil;
import com.inter.trade.ui.fragment.checking.util.RegistParser;
import com.inter.trade.util.Constants;
import com.inter.trade.util.LoginTimeoutUtil;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PreferenceConfig;
import com.inter.trade.util.PromptUtil;
import com.inter.trade.view.dialog.ForgetGesturePsdDialog;
import com.inter.trade.view.dialog.ForgetGesturePsdDialog.SmsCodeSubmitListener;

public class LockActivity extends FragmentActivity implements
		LockPatternView.OnPatternListener, OnClickListener
{

	private LockPatternView lockPatternView;

	private TextView tv_account;

	private TextView tv_forget;
	private TextView tv_other;

	private String patternString;

	/**
	 * 手势密码输入次数，默认是5次，输入次数超过5次，跳到系统登录页面，清空当前存储的手势密码
	 */
	private static int gestureInputPwdNum = 5;

	/**
	 * 手势密码输入成功是否新打开主页面
	 */
	public static boolean isLoadMain = true;

	/**
	 * 登录后，密码管理：手势密码输入成功是否修改登录密码
	 */
	public static boolean isGestureModifyPwd = false;

	public LoginTask loginTask;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setTheme(R.style.DefaultLightTheme);
		SharedPreferences preferences = getSharedPreferences("lock",
				MODE_PRIVATE);
		patternString = preferences.getString(Constants.LOCK_KEY, "");
		if (!"".equals(patternString))
		{
			preferences.edit()
					.putString(Constants.LOCK_KEY, Constants.LOCK_KEY_VALUE)
					.commit();// V3.0.0版本之前把登录密码，手势密码保存在本地SharedPreferences，现随机生成一个密码保存至本地，以清除覆盖其他版本的密码
			PreferenceConfig.instance(this).putString(Constants.USER_PASSWORD,
					Constants.LOCK_KEY_VALUE);
		}
		Intent intent = getIntent();
		if (intent != null)
		{
			isLoadMain = intent.getBooleanExtra("isLoadMain", true);
			isGestureModifyPwd = intent.getBooleanExtra("isGestureModifyPwd",
					false);
		}

		setContentView(R.layout.default_lock);

		initLayout();

		if (isGestureModifyPwd)
		{// 手势密码修改登录密码,不显示"忘记密码"
			tv_other.setVisibility(View.INVISIBLE);
			tv_forget.setVisibility(View.INVISIBLE);
		}
		else
		{
			tv_other.setVisibility(View.VISIBLE);
			tv_forget.setVisibility(View.VISIBLE);
		}

		tv_account.setVisibility(View.VISIBLE);
		tv_account.setText(PreferenceConfig.instance(LockActivity.this)
				.getString(Constants.USER_NAME, ""));

		if (gestureInputPwdNum != 5)
		{
			tv_account.setVisibility(View.VISIBLE);
			String inputNumInfo = "密码错误，还可以再试" + gestureInputPwdNum + "次。";
			tv_account.setTextColor(Color.RED);
			tv_account.setText(inputNumInfo);

		}

		// 检测新版本
		new VersionTask(this, true, true).execute("");

	}

	/**
	 * 实例化布局
	 */
	private void initLayout()
	{
		tv_forget = (TextView) findViewById(R.id.tv_forget);
		tv_other = (TextView) findViewById(R.id.tv_other);
		tv_account = (TextView) findViewById(R.id.tv_account);
		lockPatternView = (LockPatternView) findViewById(R.id.lock_pattern);
		lockPatternView.setOnPatternListener(this);
		tv_other.setOnClickListener(this);
		tv_forget.setOnClickListener(this);
	}

	/**
	 * 用户连续输错5次手势密码，清空本地用户数据
	 */
	private void cleanNativeUserInfo()
	{
		LoginUtil.mLoginStatus.cancel();
		PreferenceConfig.instance(this).putString(Constants.USER_NAME, "");
		PreferenceConfig.instance(this).putString(Constants.USER_PASSWORD, "");
		PreferenceConfig.instance(this).putString(Constants.USER_AUTHORID, "");
		PreferenceConfig.instance(this).putString(Constants.USER_GESTURE_PWD,
				"");
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == Constants.ACTIVITY_FINISH)
		{
			setResult(Constants.ACTIVITY_FINISH);
			finish();
		}
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		if (loginTask != null)
		{
			loginTask.cancel(true);
		}
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			// 如果用户按了返回键，说明没有输入手势密码，这时发现是超时了，则清理掉超时状态
			if (LoginTimeoutUtil.get().isTimeout())
			{
				LoginTimeoutUtil.get().cleanTimeoutState();
			}
		}

		return super.onKeyUp(keyCode, event);
	}

	@Override
	public void onPatternStart()
	{
	}

	@Override
	public void onPatternCleared()
	{
	}

	@Override
	public void onPatternCellAdded(List<Cell> pattern)
	{
	}

	@Override
	public void onPatternDetected(List<Cell> pattern)
	{

		if (pattern.size() < LockPatternView.MIN_LOCK_PATTERN_SIZE)
		{
			Toast.makeText(this,
					R.string.lockpattern_recording_incorrect_too_short,
					Toast.LENGTH_SHORT).show();
			clearPattern();
			return;
		}

		patternString = LockPatternUtil.lockPatternToString(pattern);

		PreferenceConfig.instance(this).putString(Constants.REQ_TOKEN, "");// 清空req_token
		// 手势密码输入正确，则异步登录
		loginTask = new LoginTask();
		loginTask.execute("");
	}

	/**
	 * 手势密码输错了，提示用户，并延迟两秒清理当前的输入轨迹
	 */
	private void clearPattern()
	{
		lockPatternView.setDisplayMode(DisplayMode.Wrong);
		new Thread()
		{
			public void run()
			{
				try
				{
					Thread.sleep(500);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
				runOnUiThread(new Runnable()
				{

					@Override
					public void run()
					{
						lockPatternView.clearPattern();
						lockPatternView.enableInput();
					}
				});

			}
		}.start();
	}

	/**
	 * 检测手势密码输入次数
	 */
	private void gestureInputPwdNumInfo()
	{

		if (gestureInputPwdNum == 1)
		{

			// 登录后的密码管理：isGestureModifyPwd==true,手势密码修改登录密码，输错5次,
			// 返回密码管理，而不跳到系统登录页面
			if (!isGestureModifyPwd)
			{
				// 跳到系统登录页面
				Intent intent = new Intent();
				intent.setClass(LockActivity.this, SafetyLoginActivity.class);
				startActivityForResult(intent, 2);

				cleanNativeUserInfo();
			}
			gestureInputPwdNum = 5;
			finish();
			return;
		}

		gestureInputPwdNum--;

		tv_account.setVisibility(View.VISIBLE);
		String inputNumInfo = "密码错误，还可以再试" + gestureInputPwdNum + "次。";
		tv_account.setTextColor(Color.RED);
		tv_account.setText(inputNumInfo);

		Animation shakeAnim = AnimationUtils
				.loadAnimation(this, R.anim.shake_x);
		tv_account.startAnimation(shakeAnim);
	}

	private ProtocolRsp mRsp;

	class LoginTask extends AsyncTask<String, Integer, Boolean>
	{

		@Override
		protected Boolean doInBackground(String... params)
		{
			List<ProtocolData> mDatas = getRequestDatas();
			RegistParser authorRegParser = new RegistParser();
			mRsp = HttpUtil.doRequest(authorRegParser, mDatas);
			return null;
		}

		@Override
		protected void onPostExecute(Boolean result)
		{
			super.onPostExecute(result);
			try
			{
				PromptUtil.dissmiss();
				if (mRsp == null)
				{
					PromptUtil.showToast(LockActivity.this,
							LockActivity.this.getString(R.string.net_error));
					lockPatternView.clearPattern();
					lockPatternView.enableInput();
				}
				else
				{
					try
					{
						List<ProtocolData> mDatas = mRsp.mActions;
						parserResoponse(mDatas);

						if (LoginUtil.mLoginStatus.result
								.equals(ProtocolUtil.SUCCESS))
						{
							LoginUtil.isLogin = true;
							LoginUtil.mLoginStatus.login_name = PreferenceConfig
									.instance(LockActivity.this).getString(
											Constants.USER_NAME, "");
							Logger.d(
									"login",
									"登录接口"
											+ LoginUtil.mLoginStatus.mResponseData
													.getReq_token());
							ProtocolUtil.printString("LoginFragment",
									LoginUtil.mLoginStatus.mResponseData
											.getReq_token());

							// 手势密码登录成功，保存手势密码到本地（用于登录失败，尝试手势密码登录）
							PreferenceConfig.instance(LockActivity.this)
									.putString(Constants.USER_GESTURE_PWD,
											patternString);

							// 保存数据 "是否绑定过代理商" 到PreferenceConfig
							PreferenceConfig.instance(LockActivity.this)
									.putString(
											Constants.IS_BIND_AGENT,
											LoginUtil.mLoginStatus.relateAgent
													+ "");

							// 保存数据 "代理商id" 到PreferenceConfig
							PreferenceConfig
									.instance(LockActivity.this)
									.putString(Constants.AGENT_ID,
											LoginUtil.mLoginStatus.agentid + "");

							// 保存数据 "代理商类型id" 到PreferenceConfig
							PreferenceConfig.instance(LockActivity.this)
									.putString(
											Constants.AGENT_TYPE_ID,
											LoginUtil.mLoginStatus.agenttypeid
													+ "");

							// 保存上一次登陆的用户名
							PreferenceConfig.instance(LockActivity.this)
									.putString(Constants.LAST_LOGIN_USERNAME,
											LoginUtil.mLoginStatus.login_name);

							// 登录成功进入主页面
							if (isLoadMain)
							{
								Intent intent = new Intent();
								intent.setClass(LockActivity.this,
										MainActivity.class);
								startActivity(intent);
							}

							// 手势密码修改登录密码
							if (isGestureModifyPwd)
							{
								Intent intent = new Intent();
								intent.putExtra(
										Constants.USER_NAME,
										PreferenceConfig.instance(
												LockActivity.this).getString(
												Constants.USER_NAME, ""));
								intent.setClass(LockActivity.this,
										GestureModifyPwdActivity.class);
								startActivity(intent);
							}

							gestureInputPwdNum = 5;

							finish();

						}
						else
						{
							gestureInputPwdNumInfo();
							lockPatternView.clearPattern();
							lockPatternView.enableInput();
						}
					}
					catch (Exception e)
					{
						e.printStackTrace();
						PromptUtil.showToast(LockActivity.this,
								getString(R.string.req_error));
						lockPatternView.clearPattern();
						lockPatternView.enableInput();
					}

				}
			}
			catch (Exception e)
			{
			}

		}

		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
			PromptUtil.showDialog(LockActivity.this, "正在登录...");
		}

	}

	private List<ProtocolData> getRequestDatas()
	{
		CommonData data = new CommonData();

		data.putValue(
				"mobile",
				LoginUtil.mLoginStatus.login_name != null ? LoginUtil.mLoginStatus.login_name
						: PreferenceConfig.instance(this).getString(
								Constants.USER_NAME, ""));// 用户手机号
		data.putValue("gesturepasswd", patternString != null ? patternString
				: "");// 手势密码

		List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas(
				"ApiAuthorInfoV2", "login", data);

		return mDatas;
	}

	/**
	 * 解析响应体
	 * 
	 * @param params
	 */
	private void parserResoponse(List<ProtocolData> params)
	{
		ResponseData response = new ResponseData();
		LoginUtil.mLoginStatus.mResponseData = response;
		for (ProtocolData data : params)
		{
			if (data.mKey.equals(ProtocolUtil.msgheader))
			{
				ProtocolUtil.parserResponse(response, data);

			}
			else if (data.mKey.equals(ProtocolUtil.msgbody))
			{
				List<ProtocolData> result1 = data.find("/result");
				if (result1 != null)
				{
					LoginUtil.mLoginStatus.result = result1.get(0).mValue;
				}

				List<ProtocolData> message = data.find("/message");
				if (message != null)
				{
					LoginUtil.mLoginStatus.message = message.get(0).mValue;
				}

				List<ProtocolData> authorid = data.find("/authorid");
				if (authorid != null)
				{
					LoginUtil.mLoginStatus.authorid = authorid.get(0).mValue;
				}

				List<ProtocolData> agentid = data.find("/agentid");
				if (agentid != null)
				{
					LoginUtil.mLoginStatus.agentid = agentid.get(0).mValue;
				}

				/*
				 * 用户是否绑定过代理商，relateAgent，“0”否，“1”是
				 */
				List<ProtocolData> relateAgent = data.find("/relateAgent");
				if (relateAgent != null)
				{
					LoginUtil.mLoginStatus.relateAgent = relateAgent.get(0).mValue;
				}

				/*
				 * 代理商类型，agenttypeid，“0”普通用户，“1”正式代理商， “2”虚拟代理商
				 */
				List<ProtocolData> agenttypeid = data.find("/agenttypeid");
				if (agenttypeid != null)
				{
					LoginUtil.mLoginStatus.agenttypeid = agenttypeid.get(0).mValue;
				}

				List<ProtocolData> ispaypwd = data.find("/ispaypwd");
				if (ispaypwd != null)
				{
					LoginUtil.mLoginStatus.ispaypwd = ispaypwd.get(0).mValue;
				}

				List<ProtocolData> gesturepasswd = data.find("/gesturepasswd");
				if (gesturepasswd != null)
				{
					LoginUtil.mLoginStatus.gesturepasswd = gesturepasswd.get(0).mValue;
				}
			}
		}
	}

	@Override
	public void onClick(View v)
	{
		Intent intent = new Intent();
		switch (v.getId())
		{
		case R.id.tv_other:
			intent.setClass(LockActivity.this, SafetyLoginActivity.class);
			startActivityForResult(intent, 2);
			LoginUtil.mLoginStatus.cancel();
			break;

		case R.id.tv_forget:
			ForgetGesturePsdDialog dialog = new ForgetGesturePsdDialog();
			dialog.show(LockActivity.this, new SmsCodeSubmitListener()
			{

				@Override
				public void onPositive()
				{
					Intent intent = new Intent(LockActivity.this,
							SafetyAccountChangeActivity.class);
					intent.putExtra("isForget", true);// 标记忘记手势密码
					startActivity(intent);
				}
			});
			break;
		}
	}

}
