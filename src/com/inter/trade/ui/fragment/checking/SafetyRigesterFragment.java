package com.inter.trade.ui.fragment.checking;

import java.util.List;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import com.inter.trade.ui.FunctionActivity;
import com.inter.trade.ui.fragment.AboutFragment;
import com.inter.trade.ui.fragment.BaseFragment;
import com.inter.trade.ui.fragment.FragmentFactory;
import com.inter.trade.ui.fragment.RigesterFragment;
import com.inter.trade.ui.fragment.checking.util.RegistParser;
import com.inter.trade.ui.fragment.waterelectricgas.data.ResponseStateListener;
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
public class SafetyRigesterFragment extends BaseFragment implements
		OnClickListener, ResponseStateListener {
	private Button loginButton;
	private Button registerButton;
	private LoginStatus mData = new LoginStatus();
	private ProtocolRsp mRsp;
	private EditText login_name_edit;
	private EditText login_pwd_edit;
	private String name = "";
	private String pwd = "";

	private CheckBox remenber_ck;
	private LoginTask mLoginTask;

	// private CheckUserStateTask checkUserStateTask;

	// 用于抖动窗口
	private LinearLayout usepwdLayout, usepwdConfirmLayout;

	/**
	 * 用户注册的手机号
	 */
	private TextView userMobileNumber;

	/**
	 * 待注册的手机号
	 */
	private String phoneNum;

	private static String REGISTER_PHONE = "REGISTER_PHONE";

	public SafetyRigesterFragment() {

	}

	public static SafetyRigesterFragment createFragment(String userMobileNum) {
		SafetyRigesterFragment fragment = new SafetyRigesterFragment();
		final Bundle args = new Bundle();
		args.putString(REGISTER_PHONE, userMobileNum);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		phoneNum = getArguments() == null ? "" : getArguments().get(
				REGISTER_PHONE).toString();
		// checkUserState ();
	}
	
	

	@Override
	public void onResume() {
		super.onResume();
		setTitle("填写登录密码");
		setBackVisible();
	}

	/**
	 * 设置返回事件
	 */
	protected void setBackVisible() {
		if (getActivity() == null) {
			return;
		}

		View view = getActivity().findViewById(R.id.iv_tilte_line);
		if (view != null) {
			view.setVisibility(View.VISIBLE);
		}

		back = (Button) getActivity().findViewById(R.id.title_back_btn);

		if (back == null) {
			return;
		}
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				FragmentManager manager = getFragmentManager();
				int len = manager.getBackStackEntryCount();
				if (len > 0) {
					manager.popBackStack();
				} else {
					getActivity().finish();
				}
			}
		});

		menu = (Button) getActivity().findViewById(R.id.title_menu_btn);
		menu.setVisibility(View.GONE);
		Button right = (Button) getActivity()
				.findViewById(R.id.title_right_btn);
		right.setVisibility(View.GONE);
	}

	/**
	 * 检查用户状态 该手机是否注册过
	 */
	// public void checkUserState () {
	// checkUserStateTask = new CheckUserStateTask();
	// checkUserStateTask.execute("");
	// }

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
			PromptUtil.showToast(getActivity(), "请输入账户密码");
			runAnimation(usepwdLayout);
			return false;
		}

		// if(!UserInfoCheck.checkMobilePhone(true_name)){
		// PromptUtil.showToast(getActivity(), "手机号码不正确");
		// return false;
		// }
		// name = true_name;
		// LoginUtil.mLoginStatus.login_name=name;
		boolean flag = true;
		String login_pwd = login_pwd_edit.getText().toString();
		if (null == login_pwd || "".equals(login_pwd)) {
			PromptUtil.showToast(getActivity(), "请输入确认密码");
			runAnimation(usepwdConfirmLayout);
			return false;
		}

		if (true_name.length() < 6) {
			PromptUtil.showToast(getActivity(), "账户密码至少输入6-20位数");
			runAnimation(usepwdLayout);
			return false;
		}

		if (!UserInfoCheck.checkPassword(true_name)) {
			PromptUtil.showToast(getActivity(), "您输入的密码中存在非法字符");
			runAnimation(usepwdLayout);
			return false;
		}

		if (login_pwd.length() < 6) {
			PromptUtil.showToast(getActivity(), "确认密码至少输入6-20位数");
			runAnimation(usepwdConfirmLayout);
			return false;
		}

		if (!UserInfoCheck.checkPassword(login_pwd)) {
			PromptUtil.showToast(getActivity(), "您输入的密码中存在非法字符");
			runAnimation(usepwdConfirmLayout);
			return false;
		}

		if (!true_name.equals(login_pwd)) {
			PromptUtil.showToast(getActivity(), "两次输入密码不一致");
			runAnimation(usepwdConfirmLayout);
			return false;
		}

		pwd = login_pwd;
		LoginUtil.mLoginStatus.login_pwd = pwd;
		// if(remenber_ck.isChecked()){
		// PreferenceConfig.instance(getActivity()).putString(Constants.USER_ZHANGHAO,
		// name);
		// }else{
		// PreferenceConfig.instance(getActivity()).putString(Constants.USER_ZHANGHAO,
		// "");
		// }

		if (!remenber_ck.isChecked()) {
			PromptUtil.showToast(getActivity(), "您还没同意通付宝注册协议哦!");
			return false;
		}

		return flag;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (mLoginTask != null) {
			mLoginTask.cancel(true);
		}
		// if(checkUserStateTask!=null){
		// checkUserStateTask.cancel(true);
		// }
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.safety_rigester_layout,
				container, false);
		loginButton = (Button) view.findViewById(R.id.login);
		registerButton = (Button) view.findViewById(R.id.register);
		login_name_edit = (EditText) view.findViewById(R.id.pay_name_edit);
		login_pwd_edit = (EditText) view.findViewById(R.id.check_pwd_edit);
		remenber_ck = (CheckBox) view.findViewById(R.id.remenber_ck);
		remenber_ck.setChecked(true);

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
		loginButton.setOnClickListener(this);
		registerButton.setOnClickListener(this);

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

		view.findViewById(R.id.regist_protocol).setOnClickListener(this);

		userMobileNumber = (TextView) view
				.findViewById(R.id.user_mobile_number);
		userMobileNumber.setText(phoneNum);

		return view;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.login:
			// if(checkInput()){
			// mLoginTask = new LoginTask();
			// mLoginTask.execute("");
			// }
			// 进入换了新手机页面
			Intent intent = new Intent();
			intent.setClass(getActivity(), NewMobileAuthActivity.class);
			getActivity().startActivity(intent);
			break;
		case R.id.register:
			// registerFragment();
			if (checkInput()) {
				// 进入手势密码设置
				// Intent intentlock = new Intent();
				// intentlock.setClass(getActivity(), LockSetupActivity.class);
				// getActivity().startActivity(intentlock);
				mLoginTask = new LoginTask();
				mLoginTask.execute("");
			}
			break;
		case R.id.regist_protocol:
			AboutFragment.mProtocolType = "3";
			Intent protocolIntent = new Intent();
			protocolIntent.setClass(getActivity(), FunctionActivity.class);
			protocolIntent.putExtra(FragmentFactory.INDEX_KEY,
					FragmentFactory.PROTOCOL_LIST_INDEX);
			getActivity().startActivity(protocolIntent);
			break;

		default:
			break;
		}
	}

	private void registerFragment() {
		FragmentTransaction transaction = getActivity()
				.getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.func_container, new RigesterFragment());
		transaction.commit();
	}

	// /**
	// * 检测用户状态，该手机是否注册过
	// * @author zhichao.huang
	// *
	// */
	// class CheckUserStateTask extends AsyncTask<String, Integer, Boolean>{
	//
	// @Override
	// protected Boolean doInBackground(String... params) {
	// // TODO Auto-generated method stub
	// List<ProtocolData> mDatas = getCheckUserRequestDatas();
	// CheckUserStateParser authorRegParser = new CheckUserStateParser();
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
	// // PromptUtil.showToast(getActivity(), LoginUtil.mLoginStatus.message);
	// LoginUtil.isLogin=true;
	// LoginUtil.mLoginStatus.login_name=name;
	// Logger.d("login","登录接口"+LoginUtil.mLoginStatus.mResponseData.getReq_token());
	// ProtocolUtil.printString("LoginFragment",
	// LoginUtil.mLoginStatus.mResponseData.getReq_token());
	//
	// //没有设手势密码，但已经注册过，跳到手势密码设置
	// if(LoginUtil.mLoginStatus.gesturepasswd != null &&
	// LoginUtil.mLoginStatus.gesturepasswd.equals("0")) {
	// //进入手势密码设置
	// Intent intentlock = new Intent();
	// intentlock.setClass(getActivity(), LockSetupActivity.class);
	// getActivity().startActivity(intentlock);
	// }else{
	// //已注册，已存在手势密码，则跳到手势密码登录页面
	// //把手势密码设置到本地，方便对比用户输入手势密码进行校验
	// // SharedPreferences preferences = getActivity().getSharedPreferences(
	// // "lock", getActivity().MODE_PRIVATE);
	// // preferences
	// // .edit()
	// // .putString("lock_key",
	// // LoginUtil.mLoginStatus.gesturepasswd)
	// // .commit();
	//
	// //跳到手势登录
	// Intent intent = new Intent(getActivity(), LockActivity.class);
	// startActivity(intent);
	// }
	//
	// getActivity().finish();
	// // FragmentTransaction fragmentTransaction =
	// getActivity().getSupportFragmentManager().beginTransaction();
	// //
	// fragmentTransaction.replace(R.id.func_container,FragmentFactory.createFragment(FragmentFactory.current_index));
	// // fragmentTransaction.commit();
	//
	// }else {
	// PromptUtil.showToast(getActivity(), "检测完成");
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
	// PromptUtil.showDialog(getActivity(), "正在安全检测...");
	// }
	//
	// }

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
							LoginUtil.mLoginStatus.login_name = phoneNum;

							// 保存数据到PreferenceConfig
							PreferenceConfig.instance(getActivity()).putString(
									Constants.USER_NAME, phoneNum);
							PreferenceConfig.instance(getActivity()).putString(
									Constants.USER_PASSWORD, pwd);
							PreferenceConfig.instance(getActivity()).putString(
									Constants.USER_AUTHORID,
									LoginUtil.mLoginStatus.authorid);

							// //注册新用户，设定为非代理商用户
							// PreferenceConfig.instance(getActivity()).putString(Constants.AGENT_ID,
							// "0");
							//
							// //注册新用户，设定为普通用户
							// PreferenceConfig.instance(getActivity()).putString(Constants.AGENT_TYPE_ID,
							// "0");
							//
							// //注册新用户，设定为未绑定代理商状态
							// PreferenceConfig.instance(getActivity()).putString(Constants.IS_BIND_AGENT,
							// "0");

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

							// 注册新用户，再进入主页面弹出通知公告
							PreferenceConfig.instance(getActivity()).putString(
									Constants.IS_SHOWED_NOTICE, "0");

							// 保存上一次登陆的用户名
							PreferenceConfig.instance(getActivity()).putString(
									Constants.LAST_LOGIN_USERNAME, phoneNum);

							Logger.d(
									"login",
									"登录接口"
											+ LoginUtil.mLoginStatus.mResponseData
													.getReq_token());
							ProtocolUtil.printString("LoginFragment",
									LoginUtil.mLoginStatus.mResponseData
											.getReq_token());

							// if(!("1".equals(LoginUtil.mLoginStatus.relateAgent))){//没有绑定
							// //检查本地是否有代理商代号的配置文件，有就绑定，没有默认绑定020001
							// String
							// agentID=PreferenceConfig.instance(getActivity()).getString(Constants.AGENT_NO_TO_BIND,
							// "020001");
							// new
							// BindAgentTask(getActivity(),SafetyRigesterFragment.this).execute(agentID);
							// }else{
							// 进入手势密码设置
							Intent intentlock = new Intent();
							intentlock.setClass(getActivity(),
									LockSetupActivity.class);
							startActivityForResult(intentlock, 2);
							getActivity().setResult(Constants.ACTIVITY_FINISH);
							// }

							// getActivity().finish();
							// FragmentTransaction fragmentTransaction =
							// getActivity().getSupportFragmentManager().beginTransaction();
							// fragmentTransaction.replace(R.id.func_container,FragmentFactory.createFragment(FragmentFactory.current_index));
							// fragmentTransaction.commit();

						} else {
							LoginUtil.mLoginStatus.cancel();
							getActivity().finish();
							PromptUtil.showToast(getActivity(),
									LoginUtil.mLoginStatus.message);
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
		// data.putValue("aumobile", this.name);
		// data.putValue("aupwd", pwd);
		// data.putValue("auloginmethod", "1");
		// data.putValue("mpmodel", phoneModel);

		String phone = PhoneInfoUtil.getNativePhoneNumber(getActivity());
		data.putValue("phonenumber", phoneNum);// 手机号
		data.putValue("paypasswd", pwd);// 支付密码
		data.putValue("macip", ProtocolUtil.getLocalMacAddress(getActivity()));// 手机mac地址
		List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas(
				"ApiAuthorInfoV2", "register", data);

		return mDatas;
	}

	// /**
	// * 获取检查用户是否注册过的数据定义
	// * @return
	// */
	// private List<ProtocolData> getCheckUserRequestDatas(){
	// CommonData data = new CommonData();
	// // data.putValue("aumobile", this.name);
	// // data.putValue("aupwd", pwd);
	// // data.putValue("auloginmethod", "1");
	// // data.putValue("mpmodel", phoneModel);
	//
	// String phone = PhoneInfoUtil.getNativePhoneNumber(getActivity());
	// data.putValue("phonenumber", phone != null ? phone : "");//手机号
	// data.putValue("paypasswd", pwd != null ? pwd : "");//支付密码
	// data.putValue("accountnumber", "");//绑定的银行卡号
	// data.putValue("macip",
	// ProtocolUtil.getLocalMacAddress(getActivity()));//手机mac地址
	// List<ProtocolData> mDatas =
	// ProtocolUtil.getRequestDatas("ApiAuthorInfoV2", "authorExists", data);
	//
	//
	// return mDatas;
	// }

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

	@Override
	public void onSuccess(Object obj, Class cla) {

		// 进入手势密码设置
		Intent intentlock = new Intent();
		intentlock.setClass(getActivity(), LockSetupActivity.class);
		startActivityForResult(intentlock, 2);
		getActivity().setResult(Constants.ACTIVITY_FINISH);

	}

}
