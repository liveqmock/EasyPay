package com.inter.trade.ui.fragment.checking;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.inter.trade.R;
import com.inter.trade.ui.RegistActivity;
import com.inter.trade.ui.fragment.BaseFragment;
import com.inter.trade.ui.fragment.RigesterFragment;
import com.inter.trade.util.Constants;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PreferenceConfig;

/**
 * 安全注册登录页面
 * @author zhichao.huang
 *
 */
public class SafetyMainFragment extends BaseFragment implements OnClickListener{
	
	private Button btnLogin,btnRegister;
	
	public SafetyMainFragment(){
		
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
		View view = inflater.inflate(R.layout.fragment_safety_main, container, false);
		btnLogin=(Button) view.findViewById(R.id.btn_login);
		btnRegister=(Button) view.findViewById(R.id.btn_register);
		btnLogin.setOnClickListener(this);
		btnRegister.setOnClickListener(this);
		
		return view;
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_login://登陆
			
			//进入系统登录页面
			Intent intentRegist = new Intent();
			intentRegist.putExtra("isfirstlogin", true);//标记第一次登陆
			intentRegist.setClass(getActivity(), SafetyLoginActivity.class);
			startActivityForResult(intentRegist, 56);
			break;
		case R.id.btn_register://注册
			LoginUtil.mLoginStatus.cancel();//清空本地的登陆状态
			//进入注册页面
			Intent intent = new Intent();
    		intent.setClass(getActivity(), RegistActivity.class);
    		getActivity().startActivityForResult(intent, 3);
			break;
		default:
			break;
		}
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
}	
	