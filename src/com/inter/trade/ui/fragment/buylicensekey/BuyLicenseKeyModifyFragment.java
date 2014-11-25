package com.inter.trade.ui.fragment.buylicensekey;


import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
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

import com.inter.protocol.util.ProtocolUtil;
import com.inter.trade.AsyncLoadWork;
import com.inter.trade.R;
import com.inter.trade.AsyncLoadWork.AsyncLoadWorkListener;
import com.inter.trade.data.CommonData;
import com.inter.trade.log.Logger;
import com.inter.trade.ui.BuySwipCardRecordActivity;
import com.inter.trade.ui.fragment.BaseFragment;
import com.inter.trade.ui.fragment.buylicensekey.data.BuyLicenseKeyData;
import com.inter.trade.ui.fragment.buylicensekey.util.BuyLicenseKeyModifyParser;
import com.inter.trade.ui.fragment.buylicensekey.util.BuyLicenseKeyUtils;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PhoneInfoUtil;
import com.inter.trade.util.PromptUtil;

/**
 * (更改绑定的设备，绑定或解绑) Fragment
 * @author haifengli
 *
 */
public class BuyLicenseKeyModifyFragment extends BaseFragment implements OnClickListener{
	
	private static final String TAG = BuyLicenseKeyModifyFragment.class.getName();
	
	private Button btn_modify_device;
	private EditText et_input;
	
	/**
	 * modifyFlag 
	 * 0: 解除绑定设备
	 * 1: 绑定本机设备
	 */
	private int modifyFlag;
	
	private Bundle data = null;
	
	private AsyncLoadWork<BuyLicenseKeyData> asyncModifyLicenseKeyTask = null;
	
	public BuyLicenseKeyModifyFragment()
	{
	}
	
	public static BuyLicenseKeyModifyFragment newInstance (Bundle data) {
		BuyLicenseKeyModifyFragment fragment = new BuyLicenseKeyModifyFragment();
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
			modifyFlag = data.getInt("modifyFlag");
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		LoginUtil.detection(getActivity());
		View view = inflater.inflate(R.layout.buy_licensekey_modify_layout, container,false);
		initView(view);
		
//		setTitle("更改设备绑定");
		
		if(modifyFlag==0){
			setTitle("解除设备绑定");
		}else{
			setTitle("绑定验证");
		}
		
		setBackVisibleForFragment();
		
		return view;
	}
	
	private void initView (View view) {
		btn_modify_device    = (Button)view.findViewById(R.id.btn_modify_device);
		et_input    = (EditText)view.findViewById(R.id.et_input);
		
		btn_modify_device.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.btn_modify_device:
			if(checkInput()){
//				testFunc();
				
				/**
				 * 网络请求，更改绑定的设备
				 */
				if(checkModify()){
					modifyLicenseKeyTask();
				}
			}
			break;
		default:
			break;
		}
		
	}
	
	private boolean checkInput(){
		String passwd = et_input.getText()+"";
		if(TextUtils.isEmpty(passwd)){
			PromptUtil.showToast(getActivity(), "请输入登录密码");
			return false;
		}
		return true;
	}
	
	/**
	 * 网络验证（含密码）通过后，前端更新状态
	 */
	private void testFunc(){
		String deviceModel=BuyLicenseKeyMainFragment.deviceModel;
		String deviceId=BuyLicenseKeyMainFragment.deviceId;
		if(modifyFlag==0){
//			if(!TextUtils.isEmpty(deviceModel) && !TextUtils.isEmpty(deviceId)){
			if(!TextUtils.isEmpty(deviceId)){
				BuyLicenseKeyMainFragment.deviceModel = "";
				BuyLicenseKeyMainFragment.deviceId = "";
				BuyLicenseKeyMainFragment.isBindDevice = false;
//				PromptUtil.showToast(getActivity(), "已解除绑定设备:"+deviceId);
				goBack();
			}else{
//				PromptUtil.showToast(getActivity(), "解除绑定设备失败");
			}
		}else if(modifyFlag==1){
//			if(!TextUtils.isEmpty(deviceModel) && !TextUtils.isEmpty(deviceId)){
			if(!TextUtils.isEmpty(deviceId)){
				BuyLicenseKeyMainFragment.deviceModel = deviceModel;
				BuyLicenseKeyMainFragment.deviceId = deviceId;
				BuyLicenseKeyMainFragment.isBindDevice = true;
//				PromptUtil.showToast(getActivity(), "绑定成功！\n"+"设备型号："+deviceModel+"\nIMEI："+deviceId);
				goBack();
			}else{
//				PromptUtil.showToast(getActivity(), "绑定失败！\n"+"设备型号："+deviceModel+"\nIMEI："+deviceId);
			}
		}
	}
	
	/**
	 * 更改绑定状态后，返回上一个页面：我的授权码 详情 BuyLicenseKeyDetailFragment
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
		if (len > 0) {
			manager.popBackStack();
		}
	}
	
	/**
	 * 更改绑定状态后，返回上一个页面：我的授权码 详情 BuyLicenseKeyDetailFragment
	 */
	private boolean checkModify(){
		String paycardid = BuyLicenseKeyMainFragment.licenseKey;
		if(TextUtils.isEmpty(paycardid)){
			PromptUtil.showToast(getActivity(), "授权码为空");
			return false;
		}
		
		/**
		 * modifyFlag ：0 ，解除绑定设备
		 */
		if(modifyFlag==0){
			String deviceModel=BuyLicenseKeyMainFragment.deviceModel;
			String deviceId=BuyLicenseKeyMainFragment.deviceId;
//			if(!TextUtils.isEmpty(deviceModel) && !TextUtils.isEmpty(deviceId)){
			if(!TextUtils.isEmpty(deviceId)){
				return true;
			}else{
				PromptUtil.showToast(getActivity(), "设备IMEI为空");
				return false;
			}
		}
		
		/**
		 * modifyFlag ：1 ，绑定本机设备
		 */
		if(modifyFlag==1){
			String deviceModel = Build.MODEL;
			String deviceId = PhoneInfoUtil.getNativePhoneDeviceId(getActivity());
//			if(!TextUtils.isEmpty(deviceModel) && !TextUtils.isEmpty(deviceId)){
			if(!TextUtils.isEmpty(deviceId)){
				BuyLicenseKeyMainFragment.deviceModel = deviceModel;
				BuyLicenseKeyMainFragment.deviceId = deviceId;
				return true;
			}else{
				PromptUtil.showToast(getActivity(), "获取本机设备IMEI失败！");
				return false;
			}
		}
		
		return false;
	}
	
	
	/**
	 *  绑定、解绑设备Task
	 */
	private void modifyLicenseKeyTask() {
		String funcName = "";
		String func = "";
		
		if(modifyFlag==0){
			funcName = "ApiAuthorInfo";
			func = "paycardLostterminal ";
		}else if(modifyFlag==1){
			funcName = "ApiAuthorInfo";
			func = "paycardbdterminal";
		}
		
		BuyLicenseKeyModifyParser netParser = new BuyLicenseKeyModifyParser();
		
		/**
		 * paycardid （刷卡器ID 复用）授权码
		 */
		String paycardid = BuyLicenseKeyMainFragment.licenseKey;
		
		/**
		 * 设备ID,即IMEI
		 */
		String deviceId  = BuyLicenseKeyMainFragment.deviceId;
		
		/**
		 * 登录密码
		 */
		String aupwd = et_input.getText()+"";
		
		/**
		 * 机型
		 */
		String machineno = BuyLicenseKeyMainFragment.deviceModel;
		
		CommonData requsetData = new CommonData();
		requsetData.putValue("paycardid",paycardid==null ? "": paycardid);
		requsetData.putValue("IMEI",deviceId==null ? "": deviceId);
		requsetData.putValue("aupwd",aupwd==null ? "": aupwd);
		requsetData.putValue("machineno",machineno==null ? "": machineno);
		
		asyncModifyLicenseKeyTask = new AsyncLoadWork<BuyLicenseKeyData>(getActivity(), netParser, requsetData, new AsyncLoadWorkListener() {

			@Override
			public void onSuccess(Object protocolDataList, Bundle bundle) {
				PromptUtil.showToast(getActivity(), LoginUtil.mLoginStatus.message);
				testFunc();
			}

			@Override
			public void onFailure(String error) {
			}
			
		}, false, true);
		
		asyncModifyLicenseKeyTask.execute(funcName, func);
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
