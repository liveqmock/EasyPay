package com.inter.trade.ui.fragment.checking;

import java.util.List;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.IntentCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.inter.protocol.ProtocolData;
import com.inter.protocol.ProtocolRsp;
import com.inter.protocol.util.ProtocolUtil;
import com.inter.trade.R;
import com.inter.trade.data.CommonData;
import com.inter.trade.data.LoginStatus;
import com.inter.trade.data.ResponseData;
import com.inter.trade.log.Logger;
import com.inter.trade.net.HttpUtil;
import com.inter.trade.ui.MainActivity;
import com.inter.trade.ui.RegistActivity;
//import com.inter.trade.ui.Splash.CheckUserStateTask;
import com.inter.trade.ui.fragment.BaseFragment;
import com.inter.trade.ui.fragment.RigesterFragment;
import com.inter.trade.ui.fragment.checking.pwdsafety.PwdSafetyReplyActivity;
import com.inter.trade.ui.fragment.checking.util.CheckUserStateParser;
import com.inter.trade.ui.fragment.checking.util.RegistParser;
import com.inter.trade.ui.fragment.guide.GuideActivity;
import com.inter.trade.util.Constants;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PhoneInfoUtil;
import com.inter.trade.util.PreferenceConfig;
import com.inter.trade.util.PromptUtil;
import com.inter.trade.util.UserInfoCheck;

/**
 * 安全注册登录页面
 * 
 * @author zhichao.huang
 * 
 */
public class SafetyLoginFragment extends BaseFragment implements
		OnClickListener {
	// 登录失败，尝试手势密码登录
	private LinearLayout login_gesture_layout;
	private Button login_gesture_button;

	private Button loginButton, btnBack;
	private TextView registerButton;
	private LoginStatus mData = new LoginStatus();
	private ProtocolRsp mRsp;
	private EditText login_name_edit;
	private EditText login_pwd_edit;
	private String name = "";
	private String pwd = "";

	private CheckBox remenber_ck;
	private LoginTask mLoginTask;

	// 用于抖动窗口
	private LinearLayout usepwdLayout, usepwdConfirmLayout;

	/**
	 * 手机唯一标识
	 */
	private String mobileDeviceId;

	public static SafetyLoginFragment safetyLoginFragment;

	public static SafetyLoginFragment getInstance(boolean isFirstLogins) {
		SafetyLoginFragment f = new SafetyLoginFragment();
		Bundle b = new Bundle();
		b.putBoolean("isfirstlogin", isFirstLogins);
		f.setArguments(b);
		return f;
	}

	public SafetyLoginFragment() {

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitle("安全登录");
		// setBackVisible();
		safetyLoginFragment = this;
	}

	/**
	 * 运行动画
	 * 
	 * @param view
	 */
	private void runAnimation(View view) {
		Animation shakeAnim = AnimationUtils.loadAnimation(getActivity(),
				R.anim.shake_x);
		view.startAnimation(shakeAnim);
	}

	private boolean checkInput() {
		String true_name = login_name_edit.getText().toString();

		if (null == true_name || "".equals(true_name)) {
			PromptUtil.showToast(getActivity(), "请输入手机号码");
			runAnimation(usepwdLayout);
			return false;
		}

		if (!UserInfoCheck.checkMobilePhone(true_name)) {
			PromptUtil.showToast(getActivity(), "手机号码不正确");
			runAnimation(usepwdLayout);
			return false;
		}
		name = true_name;
		LoginUtil.mLoginStatus.login_name = name;
		boolean flag = true;

		String login_pwd = login_pwd_edit.getText().toString();
		if (null == login_pwd || "".equals(login_pwd)) {
			PromptUtil.showToast(getActivity(), "请输入登录密码");
			runAnimation(usepwdConfirmLayout);
			return false;
		}
		//
		if (login_pwd.length() < 6 || login_pwd.length() > 20) {
			PromptUtil.showToast(getActivity(), "登录密码至少输入6-20位数");
			runAnimation(usepwdConfirmLayout);
			return false;
		}

		if (!UserInfoCheck.checkPassword(login_pwd)) {
			PromptUtil.showToast(getActivity(), "您输入的密码中存在非法字符");
			runAnimation(usepwdConfirmLayout);
			return false;
		}
		pwd = login_pwd;
		LoginUtil.mLoginStatus.login_pwd = pwd;
		// if(login_pwd.length() < 6){
		// PromptUtil.showToast(getActivity(), "确认密码至少输入6位数");
		// runAnimation(usepwdConfirmLayout);
		// return false;
		// }
		//
		// if(!true_name.equals(login_pwd)){
		// PromptUtil.showToast(getActivity(), "两次输入密码不一致");
		// runAnimation(usepwdConfirmLayout);
		// return false;
		// }

		// pwd = login_pwd;
		// LoginUtil.mLoginStatus.login_pwd=pwd;
		// if(remenber_ck.isChecked()){
		// PreferenceConfig.instance(getActivity()).putString(Constants.USER_ZHANGHAO,
		// name);
		// }else{
		// PreferenceConfig.instance(getActivity()).putString(Constants.USER_ZHANGHAO,
		// "");
		// }

		// if(!remenber_ck.isChecked()){
		// PromptUtil.showToast(getActivity(), "您还没同意通付宝注册协议哦!");
		// return false;
		// }

		return flag;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (mLoginTask != null) {
			mLoginTask.cancel(true);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.safety_login_layout1, container,
				false);
		login_gesture_layout = (LinearLayout) view
				.findViewById(R.id.login_gesture_layout);
		login_gesture_button = (Button) view
				.findViewById(R.id.login_gesture_button);
		loginButton = (Button) view.findViewById(R.id.login);
		registerButton = (TextView) view.findViewById(R.id.register);
		login_name_edit = (EditText) view.findViewById(R.id.pay_name_edit);
		login_pwd_edit = (EditText) view.findViewById(R.id.check_pwd_edit);
		remenber_ck = (CheckBox) view.findViewById(R.id.remenber_ck);
		btnBack = (Button) view.findViewById(R.id.title_back_btn);
		view.findViewById(R.id.title_name).setVisibility(View.INVISIBLE);

		btnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getActivity().finish();
			}
		});

		remenber_ck.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {

				}
			}
		});
		// String zhanghao =
		// PreferenceConfig.instance(getActivity()).getString(Constants.USER_ZHANGHAO,
		// "");
		// if(!zhanghao.equals("")){
		// login_name_edit.setText(zhanghao);
		// remenber_ck.setChecked(true);
		// }

		view.findViewById(R.id.forgetpwd).setOnClickListener(this);

		loginButton.setOnClickListener(this);
		registerButton.setOnClickListener(this);
		login_gesture_button.setOnClickListener(this);

		// TextView find_pwd = (TextView)view.findViewById(R.id.find_pwd);
		// find_pwd.setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		// // TODO Auto-generated method stub
		// FragmentTransaction transaction =
		// getActivity().getSupportFragmentManager().beginTransaction();
		// transaction.replace(R.id.func_container, new
		// ForgetPasswordFragment());
		// transaction.commit();
		// }
		// });

		usepwdLayout = (LinearLayout) view.findViewById(R.id.user_pwd_layout);
		usepwdConfirmLayout = (LinearLayout) view
				.findViewById(R.id.user_pwd_confirm_layout);

//		Bundle b = getArguments();
//		if (b != null) {
//			if (b.getBoolean("isfirstlogin")) {
//				String user_name = PreferenceConfig.instance(getActivity())
//						.getString(Constants.USER_NAME, "");
//				if (user_name != null && user_name.length() >= 11) {
//					login_name_edit.setText(user_name);
//				} else {
//					String telnumber = null;
//					telnumber = PhoneInfoUtil
//							.getNativePhoneNumber(getActivity());
//					login_name_edit.setText(telnumber);
//				}
//			} else {
//
//			}
//
//		}

		String telnumber = null;
		telnumber = PhoneInfoUtil
				.getNativePhoneNumber(getActivity());
		login_name_edit.setText(telnumber);
		
		//应用启动的时候，隐藏title
		Bundle b = getArguments();
		if (b != null) {
			if (b.getBoolean("isfirstlogin")) {
				view.findViewById(R.id.title_layout).setVisibility(View.GONE);
			} 
		}
		
		
		return view;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.register:
			// if(checkInput()){
			// mLoginTask = new LoginTask();
			// mLoginTask.execute("");
			// }
			cleanNativeUserInfo();
			// 进入注册页面
			Intent intent = new Intent();
			intent.setClass(getActivity(), RegistActivity.class);
			getActivity().startActivityForResult(intent, 3);
			break;
		case R.id.login:
			// registerFragment();
			if (checkInput()) {
				// PreferenceConfig.instance(getActivity()).putString(Constants.REQ_TOKEN,
				// "");//清空req_token
				LoginTask loginTask = new LoginTask();
				loginTask.execute("");

				// //进入手势密码设置
				// Intent intentlock = new Intent();
				// intentlock.setClass(getActivity(), LockSetupActivity.class);
				// getActivity().startActivity(intentlock);
			}
			break;

		case R.id.forgetpwd:
			Intent pwdIntent = new Intent();
			pwdIntent.putExtra(Constants.CLEAN_FLAG, true);
			// pwdIntent.setClass(getActivity(), ForgetPasswordActivity.class);
			pwdIntent.setClass(getActivity(), PwdSafetyReplyActivity.class);
			getActivity().startActivity(pwdIntent);
			break;

		// 登录失败，尝试手势密码登录
		case R.id.login_gesture_button:
			PreferenceConfig.instance(getActivity()).putString(
					Constants.USER_NAME, name);
			PreferenceConfig.instance(getActivity()).putString(
					Constants.LAST_LOGIN_USERNAME, name);
			Intent lockIntent = new Intent();
			lockIntent.setClass(getActivity(), LockActivity.class);
			// lockIntent.putExtra("isLoadMain", false);
			// lockIntent.putExtra("isGestureModifyPwd", true);
			getActivity().startActivity(lockIntent);
			getActivity().finish();
			break;

		default:
			break;
		}
	}

	/**
	 * 清理本地存储的用户信息
	 */
	private void cleanNativeUserInfo() {
		LoginUtil.mLoginStatus.cancel();
		// PreferenceConfig.instance(getActivity()).putString(Constants.USER_NAME,
		// "");
		// PreferenceConfig.instance(getActivity()).putString(Constants.USER_PASSWORD,
		// "");
		// PreferenceConfig.instance(getActivity()).putString(Constants.USER_AUTHORID,
		// "");
		// PreferenceConfig.instance(getActivity()).putString(Constants.USER_GESTURE_PWD,
		// "");
	}

	private void registerFragment() {
		FragmentTransaction transaction = getActivity()
				.getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.func_container, new RigesterFragment());
		transaction.commit();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == Constants.ACTIVITY_FINISH) {
			getActivity().setResult(Constants.ACTIVITY_FINISH);
			Log.i("Result", "SafetyLoginActivity fragement finish()");
			getActivity().finish();
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	class LoginTask extends AsyncTask<String, Integer, Boolean> {

		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			List<ProtocolData> mDatas = getRequestDatas();
			RegistParser authorRegParser = new RegistParser();
			mRsp = HttpUtil.doRequest(authorRegParser, mDatas);
			return null;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub

			super.onPostExecute(result);
			try {
				PromptUtil.dissmiss();
				if (mRsp == null) {
					PromptUtil.showToast(getActivity(), getActivity()
							.getString(R.string.net_error));
				} else {
					try {
						List<ProtocolData> mDatas = mRsp.mActions;
						parserResoponse(mDatas);

						if (LoginUtil.mLoginStatus.result
								.equals(ProtocolUtil.SUCCESS)) {
							PromptUtil.showToast(getActivity(),
									LoginUtil.mLoginStatus.message);
							LoginUtil.isLogin = true;
							LoginUtil.mLoginStatus.login_name = name;

							// 保存数据到PreferenceConfig
							PreferenceConfig.instance(getActivity()).putString(
									Constants.USER_NAME, name);
							PreferenceConfig.instance(getActivity()).putString(
									Constants.USER_PASSWORD, pwd);
							PreferenceConfig.instance(getActivity()).putString(
									Constants.USER_AUTHORID,
									LoginUtil.mLoginStatus.authorid);

							// 保存数据 "是否绑定过代理商" 到PreferenceConfig
							PreferenceConfig.instance(getActivity()).putString(
									Constants.IS_BIND_AGENT,
									LoginUtil.mLoginStatus.relateAgent + "");

							// 保存数据 "代理商id" 到PreferenceConfig
							PreferenceConfig.instance(getActivity()).putString(
									Constants.AGENT_ID,
									LoginUtil.mLoginStatus.agentid + "");

							// 保存数据 "代理商类型id" 到PreferenceConfig
							PreferenceConfig.instance(getActivity()).putString(
									Constants.AGENT_TYPE_ID,
									LoginUtil.mLoginStatus.agenttypeid + "");

							// 保存上一次登陆的用户名
							PreferenceConfig.instance(getActivity()).putString(
									Constants.LAST_LOGIN_USERNAME, name);

							Logger.d(
									"login",
									"登录接口"
											+ LoginUtil.mLoginStatus.mResponseData
													.getReq_token());
							ProtocolUtil.printString("LoginFragment",
									LoginUtil.mLoginStatus.mResponseData
											.getReq_token());

							MainActivity.switchLogin = true;

							if (PreferenceConfig.instance(getActivity())
									.getBoolean(
											Constants.IS_FIRST_USE_IN_PHONE,
											true)) {// 当前为第一次启动
								PreferenceConfig
										.instance(getActivity())
										.putBoolean(
												Constants.IS_FIRST_USE_IN_PHONE,
												false);
								Intent intent =new Intent(getActivity(),GuideActivity.class);
								startActivity(intent);

							} else {

								// 没有设手势密码，但已经注册过，跳到手势密码设置
								if (LoginUtil.mLoginStatus.gesturepasswd != null
										&& LoginUtil.mLoginStatus.gesturepasswd
												.equals("1")) {
									PreferenceConfig.instance(getActivity())
											.putString(
													Constants.USER_GESTURE_PWD,
													"x58abfghfghgf");
									// 已注册，已存在手势密码,跳到首页
									Intent intent = new Intent();
									intent.setClass(getActivity(),
											MainActivity.class);
									intent.setFlags(IntentCompat.FLAG_ACTIVITY_CLEAR_TASK
											| Intent.FLAG_ACTIVITY_NEW_TASK);
									startActivity(intent);
									getActivity().finish();

								} else {
									// 已注册，已存在手势密码，则跳到手势密码登录页面
									// 把手势密码设置到本地，方便对比用户输入手势密码进行校验
									// SharedPreferences preferences =
									// getActivity().getSharedPreferences(
									// "lock", getActivity().MODE_PRIVATE);
									// preferences
									// .edit()
									// .putString("lock_key",
									// LoginUtil.mLoginStatus.gesturepasswd)
									// .commit();
									PreferenceConfig.instance(getActivity())
											.putString(
													Constants.USER_GESTURE_PWD,
													"");
									// 进入手势密码设置
									Intent intentlock = new Intent();
									intentlock.setClass(getActivity(),
											LockSetupActivity.class);
									getActivity().startActivityForResult(
											intentlock, 2);

									getActivity().finish();

								}
							}

							/*
							 * //进入手势密码设置 Intent intentlock = new Intent();
							 * intentlock.setClass(getActivity(),
							 * LockSetupActivity.class);
							 * getActivity().startActivityForResult(intentlock,
							 * 2);
							 * 
							 * getActivity().finish();
							 */

							// new CheckUserStateTask().execute("");

							// getActivity().setResult(Constants.ACTIVITY_FINISH);

							// FragmentTransaction fragmentTransaction =
							// getActivity().getSupportFragmentManager().beginTransaction();
							// fragmentTransaction.replace(R.id.func_container,FragmentFactory.createFragment(FragmentFactory.current_index));
							// fragmentTransaction.commit();

						} else {
							PromptUtil.showToast(getActivity(),
									LoginUtil.mLoginStatus.message);
							login_gesture_layout.setVisibility(View.VISIBLE);
							// login_gesture_button.setOnClickListener(SafetyLoginFragment.this);
						}
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
						PromptUtil.showToast(getActivity(),
								getString(R.string.req_error));
					}

				}
			} catch (Exception e) {
				// TODO: handle exception
			}

		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			PromptUtil.showDialog(getActivity(), "正在登录...");
		}

	}

	private List<ProtocolData> getRequestDatas() {
		CommonData data = new CommonData();

		data.putValue("mobile", LoginUtil.mLoginStatus.login_name);// 用户手机号
		data.putValue("gesturepasswd", "");// 手势密码
		data.putValue("paypasswd", LoginUtil.mLoginStatus.login_pwd);// 登录密码

		List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas(
				"ApiAuthorInfoV2", "login", data);

		return mDatas;
	}

	/**
	 * 解析响应体
	 * 
	 * @param params
	 */
	private void parserResoponse(List<ProtocolData> params) {
		ResponseData response = new ResponseData();
		LoginUtil.mLoginStatus.mResponseData = response;
		for (ProtocolData data : params) {
			if (data.mKey.equals(ProtocolUtil.msgheader)) {
				ProtocolUtil.parserResponse(response, data);

			} else if (data.mKey.equals(ProtocolUtil.msgbody)) {
				List<ProtocolData> result1 = data.find("/result");
				if (result1 != null) {
					LoginUtil.mLoginStatus.result = result1.get(0).mValue;
				}

				List<ProtocolData> message = data.find("/message");
				if (message != null) {
					LoginUtil.mLoginStatus.message = message.get(0).mValue;
				}

				List<ProtocolData> authorid = data.find("/authorid");
				if (authorid != null) {
					LoginUtil.mLoginStatus.authorid = authorid.get(0).mValue;
				}

				List<ProtocolData> agentid = data.find("/agentid");
				if (agentid != null) {
					LoginUtil.mLoginStatus.agentid = agentid.get(0).mValue;
				}

				/*
				 * 用户是否绑定过代理商，relateAgent，“0”否，“1”是
				 */
				List<ProtocolData> relateAgent = data.find("/relateAgent");
				if (relateAgent != null) {
					LoginUtil.mLoginStatus.relateAgent = relateAgent.get(0).mValue;
				}

				/*
				 * 代理商类型，agenttypeid，“0”普通用户，“1”正式代理商， “2”虚拟代理商
				 */
				List<ProtocolData> agenttypeid = data.find("/agenttypeid");
				if (agenttypeid != null) {
					LoginUtil.mLoginStatus.agenttypeid = agenttypeid.get(0).mValue;
				}

				List<ProtocolData> ispaypwd = data.find("/ispaypwd");
				if (ispaypwd != null) {
					LoginUtil.mLoginStatus.ispaypwd = ispaypwd.get(0).mValue;
				}

				List<ProtocolData> gesturepasswd = data.find("/gesturepasswd");
				if (gesturepasswd != null) {
					LoginUtil.mLoginStatus.gesturepasswd = gesturepasswd.get(0).mValue;
				}
			}
		}
	}

	/**
	 * 检测该用户是否设置过手势密码，如没设置则进入到手势密码设置，有设置则直接进入主页面
	 * 
	 * @author zhichao.huang
	 * 
	 */
	class CheckUserStateTask extends AsyncTask<String, Integer, Boolean> {

		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			List<ProtocolData> mDatas = getCheckUserRequestDatas();
			CheckUserStateParser authorRegParser = new CheckUserStateParser();
			mRsp = HttpUtil.doRequest(authorRegParser, mDatas);
			return null;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub

			super.onPostExecute(result);
			try {
				PromptUtil.dissmiss();
				if (mRsp == null) {
					PromptUtil.showToast(getActivity(), getActivity()
							.getString(R.string.net_error));
					// Intent intentRegist = new Intent();
					// intentRegist.setClass(Splash.this,
					// SafetyLoginActivity.class);
					// startActivity(intentRegist);
				} else {
					try {
						List<ProtocolData> mDatas = mRsp.mActions;
						parserUserStateResoponse(mDatas);

						if (LoginUtil.mLoginStatus.result
								.equals(ProtocolUtil.SUCCESS)) {
							// PromptUtil.showToast(getActivity(),
							// LoginUtil.mLoginStatus.message);
							LoginUtil.isLogin = true;
							// LoginUtil.mLoginStatus.login_name=name;
							Logger.d(
									"login",
									"登录接口"
											+ LoginUtil.mLoginStatus.mResponseData
													.getReq_token());
							ProtocolUtil.printString("LoginFragment",
									LoginUtil.mLoginStatus.mResponseData
											.getReq_token());

							// 没有设手势密码，但已经注册过，跳到手势密码设置
							if (LoginUtil.mLoginStatus.gesturepasswd != null
									&& LoginUtil.mLoginStatus.gesturepasswd
											.equals("1")) {

								// 已注册，已存在手势密码,跳到首页
								Intent intent = new Intent();
								intent.setClass(getActivity(),
										MainActivity.class);
								intent.setFlags(IntentCompat.FLAG_ACTIVITY_CLEAR_TASK
										| Intent.FLAG_ACTIVITY_NEW_TASK);
								startActivity(intent);
								getActivity().finish();

							} else {
								// 已注册，已存在手势密码，则跳到手势密码登录页面
								// 把手势密码设置到本地，方便对比用户输入手势密码进行校验
								// SharedPreferences preferences =
								// getActivity().getSharedPreferences(
								// "lock", getActivity().MODE_PRIVATE);
								// preferences
								// .edit()
								// .putString("lock_key",
								// LoginUtil.mLoginStatus.gesturepasswd)
								// .commit();
								PreferenceConfig.instance(getActivity())
										.putString(Constants.USER_GESTURE_PWD,
												"");
								// 进入手势密码设置
								Intent intentlock = new Intent();
								intentlock.setClass(getActivity(),
										LockSetupActivity.class);
								getActivity().startActivityForResult(
										intentlock, 2);

								getActivity().finish();

							}

							// MainActivity.this.finish();
							// FragmentTransaction fragmentTransaction =
							// getActivity().getSupportFragmentManager().beginTransaction();
							// fragmentTransaction.replace(R.id.func_container,FragmentFactory.createFragment(FragmentFactory.current_index));
							// fragmentTransaction.commit();

						} else {
							// PromptUtil.showToast(MainActivity.this, "检测完成");
							PreferenceConfig.instance(getActivity()).putString(
									Constants.USER_GESTURE_PWD, "");
							// 进入手势密码设置
							Intent intentlock = new Intent();
							intentlock.setClass(getActivity(),
									LockSetupActivity.class);
							getActivity().startActivityForResult(intentlock, 2);

							getActivity().finish();

						}
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}

				}
			} catch (Exception e) {
				// TODO: handle exception
			}

		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			PromptUtil.showDialog(getActivity(), "正在安全检测...");
		}

	}

	/**
	 * 获取检查用户是否注册过的数据定义
	 * 
	 * @return
	 */
	private List<ProtocolData> getCheckUserRequestDatas() {
		CommonData data = new CommonData();
		data.putValue("phonenumber", name != null ? name : "");// 手机号
		data.putValue("paypasswd", "");// 支付密码
		data.putValue("accountnumber", "");// 绑定的银行卡号
		data.putValue("macip", "");// 手机mac地址
		List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas(
				"ApiAuthorInfoV2", "authorExists", data);

		return mDatas;
	}

	/**
	 * 解析响应体
	 * 
	 * @param params
	 */
	private void parserUserStateResoponse(List<ProtocolData> params) {
		ResponseData response = new ResponseData();
		LoginUtil.mLoginStatus.mResponseData = response;
		for (ProtocolData data : params) {
			if (data.mKey.equals(ProtocolUtil.msgheader)) {
				ProtocolUtil.parserResponse(response, data);

			} else if (data.mKey.equals(ProtocolUtil.msgbody)) {
				List<ProtocolData> result1 = data.find("/result");
				if (result1 != null) {
					LoginUtil.mLoginStatus.result = result1.get(0).mValue;
				}

				List<ProtocolData> message = data.find("/message");
				if (message != null) {
					LoginUtil.mLoginStatus.message = message.get(0).mValue;
				}

				List<ProtocolData> authorid = data.find("/authorid");
				if (authorid != null) {
					LoginUtil.mLoginStatus.authorid = authorid.get(0).mValue;
				}

				List<ProtocolData> agentid = data.find("/agentid");
				if (agentid != null) {
					LoginUtil.mLoginStatus.agentid = agentid.get(0).mValue;
				}

				List<ProtocolData> ispaypwd = data.find("/ispaypwd");
				if (ispaypwd != null) {
					LoginUtil.mLoginStatus.ispaypwd = ispaypwd.get(0).mValue;
				}

				List<ProtocolData> gesturepasswd = data.find("/gesturepasswd");
				if (gesturepasswd != null) {
					LoginUtil.mLoginStatus.gesturepasswd = gesturepasswd.get(0).mValue;
				}
			}
		}
	}

	// class LoginTask extends AsyncTask<String, Integer, Boolean>{
	//
	// @Override
	// protected Boolean doInBackground(String... params) {
	// // TODO Auto-generated method stub
	// List<ProtocolData> mDatas = getRequestDatas();
	// AuthorLoginParser authorRegParser = new AuthorLoginParser();
	// mRsp = HttpUtil.doRequest(authorRegParser, mDatas);
	// return null;
	// }
	//
	// @Override
	// protected void onPostExecute(Boolean result) {
	// // TODO Auto-generated method stub
	//
	// super.onPostExecute(result);
	// try {
	// PromptUtil.dissmiss();
	// if(mRsp == null){
	// PromptUtil.showToast(getActivity(),
	// getActivity().getString(R.string.net_error));
	// }else{
	// try {
	// List<ProtocolData> mDatas = mRsp.mActions;
	// parserResoponse(mDatas);
	//
	// if(LoginUtil.mLoginStatus.result.equals(ProtocolUtil.SUCCESS)){
	// PromptUtil.showToast(getActivity(), LoginUtil.mLoginStatus.message);
	// LoginUtil.isLogin=true;
	// LoginUtil.mLoginStatus.login_name=name;
	// Logger.d("login","登录接口"+LoginUtil.mLoginStatus.mResponseData.getReq_token());
	// ProtocolUtil.printString("LoginFragment",
	// LoginUtil.mLoginStatus.mResponseData.getReq_token());
	// getActivity().finish();
	// // FragmentTransaction fragmentTransaction =
	// getActivity().getSupportFragmentManager().beginTransaction();
	// //
	// fragmentTransaction.replace(R.id.func_container,FragmentFactory.createFragment(FragmentFactory.current_index));
	// // fragmentTransaction.commit();
	//
	// }else {
	// PromptUtil.showToast(getActivity(), LoginUtil.mLoginStatus.message);
	// }
	// } catch (Exception e) {
	// // TODO: handle exception
	// e.printStackTrace();
	// PromptUtil.showToast(getActivity(),getString(R.string.req_error));
	// }
	//
	// }
	// } catch (Exception e) {
	// // TODO: handle exception
	// }
	//
	//
	// }
	//
	// @Override
	// protected void onPreExecute() {
	// // TODO Auto-generated method stub
	// super.onPreExecute();
	// PromptUtil.showDialog(getActivity(), "正在登录...");
	// }
	//
	// }
	//
	//
	// private List<ProtocolData> getRequestDatas(){
	// CommonData data = new CommonData();
	// data.putValue("aumobile", this.name);
	// data.putValue("aupwd", pwd);
	// data.putValue("auloginmethod", "1");
	// data.putValue("mpmodel", phoneModel);
	// List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas("ApiAuthorInfo",
	// "checkAuthorLogin", data);
	//
	//
	// return mDatas;
	// }
	//
	// /**
	// * 解析响应体
	// * @param params
	// */
	// private void parserResoponse(List<ProtocolData> params){
	// ResponseData response = new ResponseData();
	// LoginUtil.mLoginStatus.mResponseData = response;
	// for(ProtocolData data :params){
	// if(data.mKey.equals(ProtocolUtil.msgheader)){
	// ProtocolUtil.parserResponse(response, data);
	//
	// }else if(data.mKey.equals(ProtocolUtil.msgbody)){
	// List<ProtocolData> result1 = data.find("/result");
	// if(result1 != null){
	// LoginUtil.mLoginStatus.result = result1.get(0).mValue;
	// }
	//
	//
	// List<ProtocolData> message = data.find("/message");
	// if(message != null){
	// LoginUtil.mLoginStatus.message = message.get(0).mValue;
	// }
	//
	// List<ProtocolData> authorid = data.find("/authorid");
	// if(authorid != null){
	// LoginUtil.mLoginStatus.authorid = authorid.get(0).mValue;
	// }
	//
	// List<ProtocolData> ispaypwd = data.find("/ispaypwd");
	// if(ispaypwd != null){
	// LoginUtil.mLoginStatus.ispaypwd = ispaypwd.get(0).mValue;
	// }
	// }
	// }
	// }

}
