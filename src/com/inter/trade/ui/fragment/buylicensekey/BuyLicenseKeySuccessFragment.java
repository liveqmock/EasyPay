package com.inter.trade.ui.fragment.buylicensekey;


import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.inter.protocol.util.ProtocolUtil;
import com.inter.trade.R;
import com.inter.trade.log.Logger;
import com.inter.trade.ui.BuySwipCardRecordActivity;
import com.inter.trade.ui.IndexActivity;
import com.inter.trade.ui.fragment.BaseFragment;
import com.inter.trade.ui.fragment.FragmentFactory;
import com.inter.trade.ui.fragment.buylicensekey.util.BuyLicenseKeyUtils;
import com.inter.trade.ui.func.FuncMap;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PhoneInfoUtil;
import com.inter.trade.util.PromptUtil;

/**
 * 购买授权码 支付成功 结果页面 Fragment
 * @author haifengli
 *
 */
public class BuyLicenseKeySuccessFragment extends BaseFragment implements OnClickListener{
	
	private static final String TAG = BuyLicenseKeySuccessFragment.class.getName();
	
	private Button btn_ok;
	private TextView tv_key;
	private String licenseKey;
	
	private Bundle data = null;
	
	public BuyLicenseKeySuccessFragment()
	{
	}
	
	public static BuyLicenseKeySuccessFragment newInstance (Bundle data) {
		BuyLicenseKeySuccessFragment fragment = new BuyLicenseKeySuccessFragment();
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
			licenseKey=data.getString("licenseKey");
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.buy_licensekey_success_layout, container,false);
		initView(view);
		
		setTitle("支付结果");
		setBackVisibleForFragment();
		
		return view;
	}
	
	private void initView (View view) {
		btn_ok    = (Button)view.findViewById(R.id.btn_ok);
		tv_key    = (TextView)view.findViewById(R.id.tv_key);
		
		btn_ok.setOnClickListener(this);
		
		if(!TextUtils.isEmpty(licenseKey)){
			tv_key.setText("您的授权码为："+licenseKey);
			BuyLicenseKeyMainFragment.licenseKey = licenseKey;
			
			String deviceModel = Build.MODEL;
			String deviceId = PhoneInfoUtil.getNativePhoneDeviceId(getActivity());
			if(!TextUtils.isEmpty(deviceModel) && !TextUtils.isEmpty(deviceId)){
				BuyLicenseKeyMainFragment.deviceModel = deviceModel;
				BuyLicenseKeyMainFragment.deviceId = deviceId;
				BuyLicenseKeyMainFragment.isBindDevice = true;
//				PromptUtil.showToast(getActivity(), "绑定成功！\n"+"设备型号："+deviceModel+"\nIMEI："+deviceId);
			}
		}
		
	}
	
	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.btn_ok:
			/**
			 * 我知道了，返回购买授权码首页
			 */
			BuyLicenseKeyMainFragment.isComeBackFromPaySuccess=true;
			Intent intent=new Intent(getActivity(),IndexActivity.class);
			intent.putExtra(FragmentFactory.INDEX_KEY, FuncMap.BUY_SWIPCARD_INDEX_FUNC);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		default:
			break;
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
