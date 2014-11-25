package com.inter.trade.ui.fragment.buylicensekey;


import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.inter.trade.R;
import com.inter.trade.log.Logger;
import com.inter.trade.ui.BuySwipCardRecordActivity;
import com.inter.trade.ui.MainActivity;
import com.inter.trade.ui.fragment.BaseFragment;
import com.inter.trade.ui.fragment.buylicensekey.util.BuyLicenseKeyUtils;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PromptUtil;
import com.inter.trade.util.PromptUtil.PositiveListener;

/**
 * (已有刷卡器的用户可以获取授权码，绑定) Fragment
 * @author haifengli
 *
 */
public class BuyLicenseKeyBindFragment extends BaseFragment implements OnClickListener{
	
	private static final String TAG = BuyLicenseKeyBindFragment.class.getName();
	
	private Button btn_get_key;
	private EditText et_input;
	
	private Bundle data = null;
	
	public BuyLicenseKeyBindFragment()
	{
	}
	
	public static BuyLicenseKeyBindFragment newInstance (Bundle data) {
		BuyLicenseKeyBindFragment fragment = new BuyLicenseKeyBindFragment();
		fragment.setArguments(data);
		return fragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "onCreate");
		Bundle bundle = getArguments();
		if(bundle != null) {
			data = bundle;
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		LoginUtil.detection(getActivity());
		View view = inflater.inflate(R.layout.buy_licensekey_bind_layout, container,false);
		initView(view);
		
		setTitle("我的授权码");
		setBackVisibleForFragment();
		
		return view;
	}
	
	private void initView (View view) {
		btn_get_key    = (Button)view.findViewById(R.id.btn_get_key);
		et_input    = (EditText)view.findViewById(R.id.et_input);
		
		btn_get_key.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.btn_get_key:
			if(checkInput()){
//				BuyLicenseKeyMainFragment.licenseKey="newKey-16101005483";
//				PromptUtil.showToast(getActivity(), "获取到授权码："+BuyLicenseKeyMainFragment.licenseKey);
//				goBack();
				
				PromptUtil.showNoticeDialog("提示", "您的申请已送达，我们将在一个工作日内完成审核。", 
						new PositiveListener() {
							
							@Override
							public void onPositive() {
								// TODO Auto-generated method stub
//								BuyLicenseKeyMainFragment.licenseKey="16101005483";
//								PromptUtil.showToast(getActivity(), "获取到授权码："+BuyLicenseKeyMainFragment.licenseKey);
								goBack();
							}
						}, null, getActivity());
				/**
				 * 网络请求，获取授权码
				 */
//				new Task
			}
			break;
		default:
			break;
		}
		
	}
	
	private boolean checkInput(){
		String cardId = et_input.getText()+"";
		if(TextUtils.isEmpty(cardId)){
			PromptUtil.showToast(getActivity(), "请输入刷卡器编号");
			return false;
		}
		return true;
	}
	
	/**
	 * 更改绑定状态后，返回上一个页面：我的授权码  BuyLicenseKeyMainFragment
	 */
	private void goBack(){
		if(getActivity()==null){
			Logger.d(TAG, "goBack()-->getActivity()==null");
			return;
		}
			
		FragmentManager manager = getActivity().getSupportFragmentManager();
		
		if(manager==null){
			Logger.d(TAG, "goBack()-->getSupportFragmentManager()==null");
			return;
		}
		
		int len = manager.getBackStackEntryCount();
		if(len>0){
			manager.popBackStack();
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	
	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
	
	@Override
	public void onTimeout() {
		// TODO Auto-generated method stub
		
	}
	
}
