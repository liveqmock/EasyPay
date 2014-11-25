package com.inter.trade.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.inter.trade.R;
import com.inter.trade.ui.fragment.checking.LockActivity;
import com.inter.trade.ui.fragment.checking.LockSetupActivity;
import com.inter.trade.ui.fragment.checking.SafetyLoginActivity;
import com.inter.trade.util.Constants;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PreferenceConfig;

public class PwdManagerFragment extends BaseFragment implements OnClickListener {
	private RelativeLayout login_pwd_layout;
	private RelativeLayout pay_pwd_layout;

	public PwdManagerFragment() {

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitle(getString(R.string.left_mima));
		
	}

	/**
	 * 设置返回事件
	 */
	protected void setBackVisible() {
		if (getActivity() == null) {
			return;
		}
		back = (Button) getActivity().findViewById(R.id.title_back_btn);

		if (back == null) {
			return;
		}
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
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

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		LoginUtil.detection(getActivity());
		View view = inflater.inflate(R.layout.pwd_manager_layout, container,
				false);
		login_pwd_layout = (RelativeLayout) view
				.findViewById(R.id.login_pwd_layout);
		pay_pwd_layout = (RelativeLayout) view
				.findViewById(R.id.pay_pwd_layout);

		pay_pwd_layout.setOnClickListener(this);
		login_pwd_layout.setOnClickListener(this);
		
		view.findViewById(R.id.rl_gesture_set).setOnClickListener(this);
		view.findViewById(R.id.rl_mibao_set).setOnClickListener(this);
		
		
		return view;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setTitle(getString(R.string.left_mima));
		setBackVisible();
	}
	
	private ReadPwdProtectionTask mReadPwdProtectionTask;
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.pay_pwd_layout:// 手势密码
			// showPwd(1);
			// 进入手势密码设置
			SafetyLoginActivity.isLoadMain = false;
			Intent intentlock = new Intent();
			intentlock.putExtra("mode","set" );//表示从修改页面进入
			intentlock.setClass(getActivity(), LockSetupActivity.class);
			getActivity().startActivityForResult(intentlock, 2);
			break;
		case R.id.login_pwd_layout:// 登录密码
			showPwd(2);
			break;
			
		case R.id.rl_mibao_set://密保问题修改登录密码
			if(mReadPwdProtectionTask != null){
				mReadPwdProtectionTask.cancel(true);
			}
			mReadPwdProtectionTask = new ReadPwdProtectionTask(getActivity(), PreferenceConfig.instance(getActivity()).getString(Constants.USER_NAME, ""));
			mReadPwdProtectionTask.execute("");
			break;
		case R.id.rl_gesture_set://手势密码修改登录密码
			Intent lockIntent = new Intent();
			lockIntent.putExtra("isLoadMain", false);
			lockIntent.putExtra("isGestureModifyPwd", true);
			lockIntent.setClass(getActivity(), LockActivity.class);
//			手势密码输错5次，不能修改登录密码，LockActivity销毁，本Activity也跟着销毁，然后跳到登录页面
//			startActivityForResult(lockIntent, 3);
			getActivity().startActivity(lockIntent);
			break;
		default:
			break;
		}
	}

	private void showPwd(int index) {
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragmentTransaction.replace(R.id.func_container,
				ModifyPwdFragment.createFragment(index));
		fragmentTransaction.addToBackStack("login");
		fragmentTransaction.commit();
	}
}
