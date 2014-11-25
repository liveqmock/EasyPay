package com.inter.trade.ui.fragment.checking;

import java.util.List;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.IntentCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.inter.protocol.ProtocolData;
import com.inter.protocol.ProtocolParser;
import com.inter.protocol.ProtocolRsp;
import com.inter.protocol.body.AuthorLoginParser;
import com.inter.protocol.util.ProtocolUtil;
import com.inter.trade.R;
import com.inter.trade.data.CommonData;
import com.inter.trade.data.LoginStatus;
import com.inter.trade.data.ResponseData;
import com.inter.trade.log.Logger;
import com.inter.trade.net.HttpUtil;
import com.inter.trade.ui.FunctionActivity;
import com.inter.trade.ui.LoginActivity;
import com.inter.trade.ui.fragment.AboutFragment;
import com.inter.trade.ui.fragment.BaseFragment;
import com.inter.trade.ui.fragment.ForgetPasswordFragment;
import com.inter.trade.ui.fragment.FragmentFactory;
import com.inter.trade.ui.fragment.RigesterFragment;
import com.inter.trade.ui.fragment.RigesterInfoFragment;
import com.inter.trade.ui.fragment.checking.pwdsafety.PwdSafetyReplyActivity;
import com.inter.trade.ui.fragment.checking.util.AuthorPwdModifyParser;
import com.inter.trade.ui.fragment.checking.util.CheckUserStateParser;
import com.inter.trade.ui.fragment.checking.util.RegistParser;
import com.inter.trade.util.Constants;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PhoneInfoUtil;
import com.inter.trade.util.PreferenceConfig;
import com.inter.trade.util.PromptUtil;
import com.inter.trade.util.UserInfoCheck;
import com.unionpay.uppayplugin.demo.UnionpayUtil;

/**
 * 密保回答成功进入的重设密码页面
 * @author zhichao.huang
 *
 */
public class SafetyModifyPwdFragment extends BaseFragment implements OnClickListener{
	private Button loginButton;
	private Button registerButton;
	private LoginStatus mData = new LoginStatus();
	private ProtocolRsp mRsp;
	private EditText login_name_edit;
	private EditText login_pwd_edit;
	private String name = "";
	private String pwd  = "";
	
	private CheckBox remenber_ck;
	private ModifyTask modifyTask;
	
	//用于抖动窗口
	private LinearLayout usepwdLayout, usepwdConfirmLayout;
	
	/**
	 * 用户注册的手机号
	 */
	private TextView userMobileNumber;
	
	/**
	 * 待注册的手机号
	 */
	private String phoneNum;
	
	/**
	 * 用户修改登录密码成功后，
	 * 1.mCleanFlag ==true  默认清除本地信息，调用  cleanNativeUserInfo(); 适用：登录页面的 忘记密码 的 “密保找回密码”
	 * 2.mCleanFlag ==false 不清除本地信息。适用：“手势修改登录密码”，“密保修改登录密码”
	 */
	private static boolean mCleanFlag=true;
	
	private static String REGISTER_PHONE = "REGISTER_PHONE";
	
	public SafetyModifyPwdFragment(){
		
	}
	
	/* 
	 * 密保找回密码
	 */
	public static SafetyModifyPwdFragment createFragment(String userMobileNum){
		mCleanFlag=true;
//		mCleanFlag = PwdSafetyReplyActivity.mCleanFlag;
		SafetyModifyPwdFragment  fragment = new SafetyModifyPwdFragment();
		final Bundle args = new Bundle();
        args.putString(REGISTER_PHONE, userMobileNum);
		fragment.setArguments(args);
		return fragment;
	}
	
	/*
	 * 手势修改登录密码
	 * 密保修改登录密码
	 */
	public static SafetyModifyPwdFragment createFragment(String userMobileNum, boolean cleanFlag){
//		mCleanFlag=false;
		mCleanFlag = cleanFlag;
		SafetyModifyPwdFragment  fragment = new SafetyModifyPwdFragment();
		final Bundle args = new Bundle();
		args.putString(REGISTER_PHONE, userMobileNum);
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitle("重设密码");
		setBackVisible();
		phoneNum = getArguments()==null?"":getArguments().get(REGISTER_PHONE).toString();
	}
	
	/**
	 * 运行动画
	 * @param view
	 */
	private void runAnimation(View view) {
		Animation shakeAnim = AnimationUtils.loadAnimation(getActivity(),
				R.anim.shake_x);
		view.startAnimation(shakeAnim);
	}
	
	private boolean checkInput()
	{
		String true_name = login_name_edit.getText().toString();
		
		if(null == true_name || "".equals(true_name)){
			PromptUtil.showToast(getActivity(), "请输入新密码");
			runAnimation(usepwdLayout);
			return false;
		}
		
		if(true_name.length() < 6 ){
			PromptUtil.showToast(getActivity(), "账户密码至少输入6-20位数");
			runAnimation(usepwdLayout);
			return false;
		}
		
		if(!UserInfoCheck.checkPassword(true_name)) {
			PromptUtil.showToast(getActivity(), "您输入的密码中存在非法字符");
			runAnimation(usepwdLayout);
			return false;
		}
		
		boolean flag = true;
		String login_pwd = login_pwd_edit.getText().toString();
		if(null == login_pwd || "".equals(login_pwd)){
			PromptUtil.showToast(getActivity(), "请输入确认密码");
			runAnimation(usepwdConfirmLayout);
			return false;
		}
		
		if(login_pwd.length() < 6){
			PromptUtil.showToast(getActivity(), "确认密码至少输入6-20位数");
			runAnimation(usepwdConfirmLayout);
			return false;
		}
		
		if(!UserInfoCheck.checkPassword(login_pwd)) {
			PromptUtil.showToast(getActivity(), "您输入的密码中存在非法字符");
			runAnimation(usepwdConfirmLayout);
			return false;
		}
		
		if(!true_name.equals(login_pwd)){
			PromptUtil.showToast(getActivity(), "两次输入密码不一致");
			runAnimation(usepwdConfirmLayout);
			return false;
		}
		
		pwd = login_pwd;
		
		return flag;
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(modifyTask!=null){
			modifyTask.cancel(true);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.safety_modify_pwd_layout, container, false);
		registerButton = (Button)view.findViewById(R.id.register);
		login_name_edit = (EditText)view.findViewById(R.id.pay_name_edit);
		login_pwd_edit = (EditText)view.findViewById(R.id.check_pwd_edit);
		
		registerButton.setOnClickListener(this);
		
		usepwdLayout = (LinearLayout)view.findViewById(R.id.user_pwd_layout);
		usepwdConfirmLayout = (LinearLayout)view.findViewById(R.id.user_pwd_confirm_layout);
		
		userMobileNumber = (TextView)view.findViewById(R.id.user_mobile_number);
		userMobileNumber.setText(phoneNum);
		
		return view;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.register:
			if(checkInput()){
				modifyTask = new ModifyTask();
				modifyTask.execute("");
			}
			break;
		default:
			break;
		}
	}
	

	
	
	private void cleanNativeUserInfo() {
    	LoginUtil.mLoginStatus.cancel();
    	PreferenceConfig.instance(getActivity()).putString(Constants.USER_NAME, "");
		PreferenceConfig.instance(getActivity()).putString(Constants.USER_PASSWORD, "");
		PreferenceConfig.instance(getActivity()).putString(Constants.USER_AUTHORID, "");
		PreferenceConfig.instance(getActivity()).putString(Constants.USER_GESTURE_PWD, "");
    }
	
	
	
	class ModifyTask extends AsyncTask<String, Integer, Boolean>{
		ProtocolRsp mRsp=null;
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			List<ProtocolData> mDatas = getRequestDatas();
			AuthorPwdModifyParser authorRegParser = new AuthorPwdModifyParser();
			mRsp = HttpUtil.doRequest(authorRegParser, mDatas);
			return null;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			PromptUtil.dissmiss();
			try {
				if(mRsp != null){
					List<ProtocolData> datas = mRsp.mActions;
					parserResoponse(datas);
					//正确
					String retCode = LoginUtil.mLoginStatus.mResponseData.getRettype();
					if (retCode.equals(
							ProtocolUtil.HEADER_SUCCESS)) {
						if (LoginUtil.mLoginStatus.result
								.equals(ProtocolUtil.SUCCESS)) {
							
							PromptUtil.showToast(getActivity(),
									LoginUtil.mLoginStatus.mResponseData.getRetmsg());
							
							if(mCleanFlag){
								cleanNativeUserInfo();//用户修改密码成功后，清除本地信息，转到登录页面
								getActivity().finish();
							}else{
								Intent intent = new Intent();
					    		intent.setClass(getActivity(), SafetyAccountChangeActivity.class);
					    		intent.setFlags(IntentCompat.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
					    		startActivity(intent);
					    		getActivity().finish();
							}
						}else{
							PromptUtil.showToast(getActivity(),
									LoginUtil.mLoginStatus.message);
						}
					}else if(retCode.equals(ProtocolUtil.REAL_ERROR)){
						//操作超时
//						PromptUtil.showRealFail(getActivity());
					}else{
						PromptUtil.showToast(getActivity(),
								LoginUtil.mLoginStatus.mResponseData.getRetmsg());
					}
				}else {
					//异常
					PromptUtil.showToast(getActivity(), getString(R.string.net_error));
				}
			} catch (Exception e) {
				// TODO: handle exception
				Logger.e(e);
				PromptUtil.showToast(getActivity(),getString(R.string.req_error));
			}
			
		}


		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			PromptUtil.showDialog(getActivity(), getActivity().getResources().getString(R.string.loading));
		}
	}
	private List<ProtocolData> getRequestDatas(){
		CommonData data = new CommonData();
		data.putValue("auoldpwd", "");
		data.putValue("aunewpwd", pwd);
		data.putValue("aumoditype", "2");
		data.putValue("reset", "1");//如果为0，将判断用户输入的旧密码是否正确，只有用户输入了正确的旧密码，才会设置新的密码;如果为1，将无视用户输入的旧密码正确与否，直接设置新的密码
		List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas("ApiAuthorInfoV2",
				"authorPwdModify", data);
		return mDatas;
	}
	
//	/**
//	 * 解析响应体
//	 * @param params
//	 */
//	private void parserResoponse(List<ProtocolData> params){
//		ResponseData response = new ResponseData();
//		LoginUtil.mLoginStatus.mResponseData = response;
//		for(ProtocolData data :params){
//			if(data.mKey.equals(ProtocolUtil.msgheader)){
//				ProtocolUtil.parserResponse(response, data);
//				
//			}else if(data.mKey.equals(ProtocolUtil.msgbody)){
//				List<ProtocolData> result1 = data.find("/result");
//				if(result1 != null){
//					LoginUtil.mLoginStatus.result = result1.get(0).mValue;
//				}
//				
//				
//				List<ProtocolData> message = data.find("/message");
//				if(message != null){
//					LoginUtil.mLoginStatus.message = message.get(0).mValue;
//				}
//			}
//		}
//	}
	
	
	/**
	 * 解析响应体
	 * @param params
	 */
	private void parserResoponse(List<ProtocolData> params){
		ResponseData response = new ResponseData();
		LoginUtil.mLoginStatus.mResponseData = response;
		for(ProtocolData data :params){
			if(data.mKey.equals(ProtocolUtil.msgheader)){
				ProtocolUtil.parserResponse(response, data);
				
			}else if(data.mKey.equals(ProtocolUtil.msgbody)){
				List<ProtocolData> result1 = data.find("/result");
				if(result1 != null){
					LoginUtil.mLoginStatus.result = result1.get(0).mValue;
				}
				
				
				List<ProtocolData> message = data.find("/message");
				if(message != null){
					LoginUtil.mLoginStatus.message = message.get(0).mValue;
				}
				
				List<ProtocolData> authorid = data.find("/authorid");
				if(authorid != null){
					LoginUtil.mLoginStatus.authorid = authorid.get(0).mValue;
				}
				
				List<ProtocolData> ispaypwd = data.find("/ispaypwd");
				if(ispaypwd != null){
					LoginUtil.mLoginStatus.ispaypwd = ispaypwd.get(0).mValue;
				}
				
				List<ProtocolData> gesturepasswd = data.find("/gesturepasswd");
				if(gesturepasswd != null){
					LoginUtil.mLoginStatus.gesturepasswd = gesturepasswd.get(0).mValue;
				}
			}
		}
	}

}
