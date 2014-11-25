package com.inter.trade.ui.fragment.buylicensekey;


import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
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
import com.inter.trade.ui.fragment.BaseFragment;
import com.inter.trade.ui.fragment.buylicensekey.util.BuyLicenseKeyUtils;
import com.inter.trade.util.LoginUtil;

/**
 * 我的授权码 详情（查看授权码、也可解绑设备） Fragment
 * @author haifengli
 *
 */
public class BuyLicenseKeyDetailFragment extends BaseFragment implements OnClickListener{
	
	private static final String TAG = BuyLicenseKeyDetailFragment.class.getName();
	
	private Button btn_remove;
	private TextView tv_key;
	private TextView tv_device;
	private int modifyFlag;
	
	private Bundle data = null;
	
	public BuyLicenseKeyDetailFragment()
	{
	}
	
	public static BuyLicenseKeyDetailFragment newInstance (Bundle data) {
		BuyLicenseKeyDetailFragment fragment = new BuyLicenseKeyDetailFragment();
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
		View view = inflater.inflate(R.layout.buy_licensekey_detail_layout, container,false);
		initView(view);
		
		setTitle("我的授权码");
		setBackVisibleForFragment();
		
		return view;
	}
	
	private void initView (View view) {
		btn_remove    = (Button)view.findViewById(R.id.btn_remove);
		tv_key    = (TextView)view.findViewById(R.id.tv_key);
		tv_device    = (TextView)view.findViewById(R.id.tv_device);
		
		btn_remove.setOnClickListener(this);
		tv_key.setText(BuyLicenseKeyMainFragment.licenseKey);
		if(BuyLicenseKeyMainFragment.isBindDevice){
			if(TextUtils.isEmpty(BuyLicenseKeyMainFragment.deviceModel)){
				tv_device.setText(BuyLicenseKeyMainFragment.deviceId+"");
			}else{
				tv_device.setText(BuyLicenseKeyMainFragment.deviceModel+"\n"+BuyLicenseKeyMainFragment.deviceId);
			}
			btn_remove.setText("解除设备绑定");
			modifyFlag=0;
		}else{
			tv_device.setText("未绑定");
			btn_remove.setText("绑定本机");
			modifyFlag=1;
		}
		
	}
	
	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.btn_remove:
			/**
			 * 我的授权码, 解绑设备
			 */
			Bundle bundle = new Bundle();
			bundle.putInt("modifyFlag", modifyFlag);
			BuyLicenseKeyUtils.switchFragment(getFragmentManager().beginTransaction(),
					BuyLicenseKeyModifyFragment.newInstance(bundle), 1);
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
