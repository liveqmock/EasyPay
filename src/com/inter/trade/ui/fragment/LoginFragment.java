package com.inter.trade.ui.fragment;

import java.util.List;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
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
import com.inter.trade.ui.LoginActivity;
import com.inter.trade.ui.TFBMainActivity;
import com.inter.trade.ui.fragment.checking.LockActivity;
import com.inter.trade.ui.fragment.checking.LockSetupActivity;
import com.inter.trade.ui.fragment.checking.SafetyLoginActivity;
import com.inter.trade.ui.fragment.checking.SafetyRigesterActivity;
import com.inter.trade.util.Constants;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PreferenceConfig;
import com.inter.trade.util.PromptUtil;
import com.inter.trade.util.UserInfoCheck;
import com.unionpay.uppayplugin.demo.UnionpayUtil;

public class LoginFragment extends BaseFragment implements OnClickListener{
	private Button loginButton;
	private Button registerButton;
	private LoginStatus mData = new LoginStatus();
	private ProtocolRsp mRsp;
	private EditText login_name_edit;
	private EditText login_pwd_edit;
	private String name = "";
	private String pwd  = "";
	
	private CheckBox remenber_ck;
	private LoginTask mLoginTask;
	public LoginFragment(){
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitle("登录");
		setBackVisible();
	}
	
	private boolean checkInput()
	{
		String true_name = login_name_edit.getText().toString();
		
		if(null == true_name || "".equals(true_name)){
			PromptUtil.showToast(getActivity(), "请输入姓名");
			return false;
		}
		if(!UserInfoCheck.checkMobilePhone(true_name)){
			PromptUtil.showToast(getActivity(), "手机号码不正确");
			return false;
		}
		name = true_name;
		LoginUtil.mLoginStatus.login_name=name;
		boolean flag = true;
		String login_pwd = login_pwd_edit.getText().toString();
		if(null == login_pwd || "".equals(login_pwd)){
			PromptUtil.showToast(getActivity(), "请输入密码");
			return false;
		}
		pwd = login_pwd;
		LoginUtil.mLoginStatus.login_pwd=pwd;
		if(remenber_ck.isChecked()){
			PreferenceConfig.instance(getActivity()).putString(Constants.USER_ZHANGHAO, name);
		}else{
			PreferenceConfig.instance(getActivity()).putString(Constants.USER_ZHANGHAO, "");
		}
		
		
		return flag;
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(mLoginTask!=null){
			mLoginTask.cancel(true);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.login_layout, container, false);
		loginButton = (Button)view.findViewById(R.id.login);
		registerButton = (Button)view.findViewById(R.id.register);
		login_name_edit = (EditText)view.findViewById(R.id.login_name_edit);
		login_pwd_edit = (EditText)view.findViewById(R.id.login_pwd_edit);
		remenber_ck = (CheckBox)view.findViewById(R.id.remenber_ck);
		
		remenber_ck.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					
				}
			}
		});
		String zhanghao = PreferenceConfig.instance(getActivity()).getString(Constants.USER_ZHANGHAO, "");
		if(!zhanghao.equals("")){
			login_name_edit.setText(zhanghao);
			remenber_ck.setChecked(true);
		}
		loginButton.setOnClickListener(this);
		registerButton.setOnClickListener(this);
		
		TextView find_pwd = (TextView)view.findViewById(R.id.find_pwd);
		find_pwd.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
				transaction.replace(R.id.func_container, new ForgetPasswordFragment());
				transaction.commit();
			}
		});
		
		//以下代码为测试阶段，安全登录入口 （包括手势密码登录等）
		((TextView)view.findViewById(R.id.safety_login)).setOnClickListener(this);
		
		return view;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.login:
			if(checkInput()){
				mLoginTask = new LoginTask();
				mLoginTask.execute("");
			}
			break;
		case R.id.register:
			registerFragment();
			break;
			
		case R.id.safety_login:
			Intent intent = new Intent();
    		intent.setClass(getActivity(), SafetyRigesterActivity.class);
//    		intent.setClass(getActivity(), SafetyLoginActivity.class);
//			intent.setClass(getActivity(), TFBMainActivity.class);
//			intent.setClass(getActivity(), LockSetupActivity.class);
    		getActivity().startActivity(intent);
			break;

		default:
			break;
		}
	}
	
	private void registerFragment(){
		FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.func_container, new RigesterFragment());
		transaction.commit();
	}
	
	
	class LoginTask extends AsyncTask<String, Integer, Boolean>{

		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			List<ProtocolData> mDatas = getRequestDatas();
			AuthorLoginParser authorRegParser = new AuthorLoginParser();
			mRsp = HttpUtil.doRequest(authorRegParser, mDatas);
			return null;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			
			super.onPostExecute(result);
			try {
				PromptUtil.dissmiss();
				if(mRsp == null){
					PromptUtil.showToast(getActivity(), getActivity().getString(R.string.net_error));
				}else{
					try {
						List<ProtocolData> mDatas = mRsp.mActions;
						parserResoponse(mDatas);
						
						if(LoginUtil.mLoginStatus.result.equals(ProtocolUtil.SUCCESS)){
							PromptUtil.showToast(getActivity(), LoginUtil.mLoginStatus.message);
							LoginUtil.isLogin=true;
							LoginUtil.mLoginStatus.login_name=name;
							Logger.d("login","登录接口"+LoginUtil.mLoginStatus.mResponseData.getReq_token());
							ProtocolUtil.printString("LoginFragment",
									 LoginUtil.mLoginStatus.mResponseData.getReq_token());
							getActivity().finish();
//							FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
//							fragmentTransaction.replace(R.id.func_container,FragmentFactory.createFragment(FragmentFactory.current_index));
//							fragmentTransaction.commit();
							
						}else {
							PromptUtil.showToast(getActivity(), LoginUtil.mLoginStatus.message);
						}
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
						PromptUtil.showToast(getActivity(),getString(R.string.req_error));
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
	
	
	private List<ProtocolData> getRequestDatas(){
		CommonData data = new CommonData();
		data.putValue("aumobile", this.name);
		data.putValue("aupwd", pwd);
		data.putValue("auloginmethod", "1");
		data.putValue("mpmodel", phoneModel);
		List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas("ApiAuthorInfo", "checkAuthorLogin", data);
		
		
		return mDatas;
	}
	
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
			}
		}
	}
	
}
