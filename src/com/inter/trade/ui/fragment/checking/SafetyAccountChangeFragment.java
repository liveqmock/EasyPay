package com.inter.trade.ui.fragment.checking;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.IntentCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.inter.trade.R;
import com.inter.trade.ui.MainActivity;
import com.inter.trade.ui.RegistActivity;
import com.inter.trade.ui.fragment.BaseFragment;
import com.inter.trade.ui.fragment.checking.bean.LoginInfo;
import com.inter.trade.ui.fragment.checking.pwdsafety.PwdSafetyReplyActivity;
import com.inter.trade.ui.fragment.checking.task.LoginTask;
import com.inter.trade.ui.fragment.waterelectricgas.data.ResponseStateListener;
import com.inter.trade.util.Constants;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PhoneInfoUtil;
import com.inter.trade.util.PreferenceConfig;
import com.inter.trade.util.PromptUtil;
import com.inter.trade.util.UserInfoCheck;

/**
 * 
 * @author zhichao.huang
 *
 */
public class SafetyAccountChangeFragment extends BaseFragment implements OnClickListener,ResponseStateListener{
	
	private Button btnLogin,btnRegister;
	
	private TextView tv_forgetpwd,tv_login_username,tv_account_change;
	
	private EditText   check_pwd_edit;
	
	private boolean isForget;
	
	/**
	 * 
	 * @param isForget 是否从忘记手势密码进入
	 * 	 * @return
	 * @throw
	 * @return SafetyAccountChangeFragment
	 */
	public static  SafetyAccountChangeFragment getInstance(boolean isForget){
		
		SafetyAccountChangeFragment f=new SafetyAccountChangeFragment();
		Bundle b=new Bundle();
		b.putBoolean("isForget", isForget);
		f.setArguments(b);
		return f;
	}
	
	
	public SafetyAccountChangeFragment(){
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_safety_account_change, container, false);
		btnLogin=(Button) view.findViewById(R.id.btn_login);
		btnRegister=(Button) view.findViewById(R.id.btn_register);
		tv_forgetpwd=(TextView) view.findViewById(R.id.tv_forgetpwd);
		tv_login_username=(TextView) view.findViewById(R.id.tv_login_username);
		tv_account_change=(TextView) view.findViewById(R.id.tv_account_change);
		check_pwd_edit=(EditText) view.findViewById(R.id.check_pwd_edit);
		
		
		btnLogin.setOnClickListener(this);
		btnRegister.setOnClickListener(this);
		tv_account_change.setOnClickListener(this);
		tv_forgetpwd.setOnClickListener(this);
		
		initData();
		
		return view;
	}
	
	private void initData() {
		Bundle b = getArguments();
		if(b!=null){
			isForget=b.getBoolean("isForget");
		}
		
		
		String lastUser = PreferenceConfig.instance(getActivity()).getString(Constants.LAST_LOGIN_USERNAME, "");
		if(lastUser!=null && lastUser.length()>=11){
			tv_login_username.setText(lastUser.subSequence(0, 3)+" "+lastUser.substring(3,7)+" "+lastUser.substring(7, 11));
		}else{
			String telnumber = null;
			telnumber = PhoneInfoUtil.getNativePhoneNumber(getActivity());
			tv_login_username.setText(telnumber.subSequence(0, 3)+" "+telnumber.substring(3,7)+" "+telnumber.substring(7, 11));
		}
}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_login://登陆
			
			if(checkInput()){
				PreferenceConfig.instance(getActivity()).putString(Constants.REQ_TOKEN, "");//清空req_token
				LoginTask loginTask = new LoginTask(getActivity(),this);
				loginTask.execute("");
			}
			break;
		case R.id.btn_register://注册
			LoginUtil.mLoginStatus.cancel();//清空本地的登陆状态
			//进入注册页面
			Intent intent = new Intent();
    		intent.setClass(getActivity(), RegistActivity.class);
    		getActivity().startActivityForResult(intent, 3);
			
			break;
		case R.id.tv_forgetpwd://忘记密码
			Intent pwdIntent = new Intent();
			pwdIntent.putExtra(Constants.CLEAN_FLAG, true);
//			pwdIntent.setClass(getActivity(), ForgetPasswordActivity.class);
			pwdIntent.setClass(getActivity(), PwdSafetyReplyActivity.class);
    		getActivity().startActivity(pwdIntent);
			
			break;
		case R.id.tv_account_change://切换账号
			//进入系统登录页面
			Intent intentRegist = new Intent();
			intentRegist.setClass(getActivity(), SafetyLoginActivity.class);
			startActivity(intentRegist);
			
			break;
		default:
			break;
		}
	}
	
	
	private boolean checkInput()
	{
		String username =tv_login_username.getText()+"";
		LoginUtil.mLoginStatus.login_name=username.replace(" ", "");
		boolean flag = true;
		
		String login_pwd = check_pwd_edit.getText().toString();
		if(null == login_pwd || "".equals(login_pwd)){
			PromptUtil.showToast(getActivity(), "请输入登录密码");
			return false;
		}
//		
		if(login_pwd.length() < 6 || login_pwd.length() > 20){
			PromptUtil.showToast(getActivity(), "登录密码至少输入6-20位数");
			return false;
		}
		
		if(!UserInfoCheck.checkPassword(login_pwd)) {
			PromptUtil.showToast(getActivity(), "您输入的密码中存在非法字符");
			return false;
		}
		LoginUtil.mLoginStatus.login_pwd=login_pwd;
		
		return flag;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if(resultCode == Constants.ACTIVITY_FINISH){
			getActivity().setResult(Constants.ACTIVITY_FINISH);
			Log.i("Result", "SafetyLoginActivity fragement finish()");
			getActivity().finish();
		}
		
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 重载方法
	 * @param obj
	 * @param cla
	 */
	@Override
	public void onSuccess(Object obj, Class cla) {
		//进入手势密码设置
		LoginInfo info=(LoginInfo) obj;
		if(isForget){//从忘记手势密码页面进入
			Intent intentlock = new Intent();
			intentlock.setClass(getActivity(), LockSetupActivity.class);
			getActivity().startActivityForResult(intentlock, 2);
			
			getActivity().finish();
		}else{
			if(info!=null && "1".equals(info.gesturepasswd)){
				PreferenceConfig.instance(getActivity()).putString(Constants.USER_GESTURE_PWD, "x58abfghfghgf");
				Intent intent = new Intent();
	    		intent.setClass(getActivity(), MainActivity.class);
	    		intent.setFlags(IntentCompat.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
	    		startActivity(intent);
	    		getActivity().finish();
			}else{
				
				PreferenceConfig.instance(getActivity()).putString(Constants.USER_GESTURE_PWD, "");//
				Intent intentlock = new Intent();
				intentlock.setClass(getActivity(), LockSetupActivity.class);
				getActivity().startActivityForResult(intentlock, 2);
				
				getActivity().finish();
			}
		}
	}
}	
	