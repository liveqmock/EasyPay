package com.inter.trade.ui.fragment.buylicensekey;


import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.inter.trade.AsyncLoadWork;
import com.inter.trade.R;
import com.inter.trade.AsyncLoadWork.AsyncLoadWorkListener;
import com.inter.trade.data.CommonData;
import com.inter.trade.log.Logger;
//import com.inter.trade.ui.BuySwipCardRecordActivity;
import com.inter.trade.ui.fragment.BaseFragment;
import com.inter.trade.ui.fragment.buylicensekey.data.BuyLicenseKeyData;
import com.inter.trade.ui.fragment.buylicensekey.util.BuyLicenseKeyParser;
import com.inter.trade.ui.fragment.buylicensekey.util.BuyLicenseKeyUtils;
//import com.inter.trade.ui.fragment.hotel.adapter.HotelListAdapter;
//import com.inter.trade.ui.fragment.hotel.data.HotelKeywordData;
//import com.inter.trade.ui.fragment.hotel.data.BuyLicenseKeyData;
//import com.inter.trade.ui.fragment.hotel.util.HotelListParser;
//import com.inter.trade.ui.manager.UIManagerActivity;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PhoneInfoUtil;

/**
 * 购买授权码(替换原购买刷卡器模块) 主Fragment
 * @author haifengli
 *
 */
public class BuyLicenseKeyMainFragment extends BaseFragment implements OnClickListener{
	
	private static final String TAG = BuyLicenseKeyMainFragment.class.getName();
	/**
	 * 授权码
	 */
	public static String licenseKey="";
	
	/**
	 * 授权码是否已绑定设备,初始为false,即未绑
	 */
	public static boolean isBindDevice=false;
	
	/**
	 * 设备型号
	 */
	public static String deviceModel="";
	
	/**
	 * 设备ID,即IMEI
	 */
	public static String deviceId="";
	
	/**
	 * 是否从[购买授权码支付成功]页面，点击[我知道了]，返回[购买授权码]首页?
	 * 若true, 则不执行readLicenseKeyTask();
	 * 若false, 则执行readLicenseKeyTask();
	 * 初始化为false, 执行readLicenseKeyTask();
	 */
	public static boolean isComeBackFromPaySuccess=false;
	
	
	private Button btn_buy_licensekey;
	private TextView tv_bind;
	private TextView product_price;
	
	private Bundle data = null;
	private AsyncLoadWork<BuyLicenseKeyData> asyncReadLicenseKeyTask = null;
	private List<BuyLicenseKeyData> mBuyLicenseKeyData = null;
	
	
	public BuyLicenseKeyMainFragment()
	{
	}
	
	public static BuyLicenseKeyMainFragment newInstance (Bundle data) {
		BuyLicenseKeyMainFragment fragment = new BuyLicenseKeyMainFragment();
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
//			mHotelListData = (BuyLicenseKeyData) data.getSerializable("hotelDetail");
		}
		
		/**
		 * 获取是否已有授权码
		 */
		if(!isComeBackFromPaySuccess){
			Logger.d(TAG, "!isComeBackFromPaySuccess");
			readLicenseKeyTask();
		}else{
			Logger.d(TAG, "isComeBackFromPaySuccess");
			isComeBackFromPaySuccess=false;
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		LoginUtil.detection(getActivity());
		View view = inflater.inflate(R.layout.buy_licensekey_layout, container,false);
		initView(view);
		
		setTitle("购买授权码");
		setBackVisibleForFragment();
		
		if(!TextUtils.isEmpty(licenseKey)){
			setRightVisible(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
	//				Intent intent = new Intent();
	//				intent.setClass(getActivity(), BuySwipCardRecordActivity.class);
	//				getActivity().startActivityForResult(intent, 200);
					
					/**
					 * 查看、解绑 我的授权码
					 */
					BuyLicenseKeyUtils.switchFragment(getFragmentManager().beginTransaction(),
							BuyLicenseKeyDetailFragment.newInstance(null), 1);
				}
			}, "我的授权码");
		}
		
		/**
		 * 获取是否已有授权码
		 */
		
		return view;
	}
	
	private void initView (View view) {
		btn_buy_licensekey    = (Button)view.findViewById(R.id.btn_buy_licensekey);
		tv_bind    = (TextView)view.findViewById(R.id.tv_bind);
		
		product_price = (TextView)view.findViewById(R.id.product_price);
		
		//原价 加上删除线
		SpannableString sp = new SpannableString(product_price.getText().toString());  
		sp.setSpan(new StrikethroughSpan(), 0, product_price.getText().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  
		product_price.setText(sp); 
		
		btn_buy_licensekey.setOnClickListener(this);
		tv_bind.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		/**
		 * 购买授权码
		 */
		case R.id.btn_buy_licensekey:
			BuyLicenseKeyUtils.switchFragment(getFragmentManager().beginTransaction(),
					BuyLicenseKeyPayConfirmFragment.newInstance(null), 1);
			break;
			
		/**
		 * 刷卡器获取授权码
		 */
		case R.id.tv_bind:
			BuyLicenseKeyUtils.switchFragment(getFragmentManager().beginTransaction(),
					BuyLicenseKeyBindFragment.newInstance(null), 1);
			break;
		default:
			break;
		}
		
	}
	
	
	/**
	 *  读取授权码Task
	 */
	private void readLicenseKeyTask() {
		/**
		 * 第一次进初始化清空授权码
		 */
		licenseKey="";
		
		BuyLicenseKeyParser netParser = new BuyLicenseKeyParser();
		
		/**
		 * paycardtype  0：(所有) ,1：只读刷卡器  2：只读授权码
		 */
		String paycardtype = "2";
		
		/**
		 * 身份类型 
		 * 商户：author
		 * 代理商：agent
		 * 默认不传当作商户处理
		 */
		String IDtype  = "";
		
		CommonData requsetData = new CommonData();
		requsetData.putValue("paycardtype",paycardtype==null ? "": paycardtype);
		requsetData.putValue("IDtype",IDtype==null ? "": IDtype);
		
		asyncReadLicenseKeyTask = new AsyncLoadWork<BuyLicenseKeyData>(getActivity(), netParser, requsetData, new AsyncLoadWorkListener() {

			@Override
			public void onSuccess(Object protocolDataList, Bundle bundle) {
				mBuyLicenseKeyData = (ArrayList<BuyLicenseKeyData>)protocolDataList;
				
				Logger.d(TAG, "asyncReadLicenseKeyTask-->onSuccess");
				
				if(mBuyLicenseKeyData != null && mBuyLicenseKeyData.size()>0){
					BuyLicenseKeyData data = mBuyLicenseKeyData.get(0);
					if(data != null){
						String licenseKey = data.getLicenseKey();
						String deviceId = data.getPaycardIMEI();
						if(!TextUtils.isEmpty(licenseKey)){
							BuyLicenseKeyMainFragment.this.licenseKey=licenseKey;
						}
						if(!TextUtils.isEmpty(deviceId)){
							BuyLicenseKeyMainFragment.this.deviceId=deviceId;
							BuyLicenseKeyMainFragment.this.isBindDevice=true;
							
							String deviceModel = data.getPaycardmachineno();
							if(!TextUtils.isEmpty(deviceModel)){
								BuyLicenseKeyMainFragment.deviceModel = deviceModel;
							}
						}
					}
				}
				if(!TextUtils.isEmpty(licenseKey)){
					setRightVisible(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							
							/**
							 * 查看、解绑 我的授权码
							 */
							BuyLicenseKeyUtils.switchFragment(getFragmentManager().beginTransaction(),
									BuyLicenseKeyDetailFragment.newInstance(null), 1);
						}
					}, "我的授权码");
				}
			}

			@Override
			public void onFailure(String error) {
			}
			
		}, false, true);
		
		asyncReadLicenseKeyTask.execute("ApiAuthorInfo", "GetpaycardList");
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if(!TextUtils.isEmpty(licenseKey)){
			Button right = (Button) getActivity().findViewById(R.id.title_right_btn);
			if(right != null){
				right.setVisibility(View.VISIBLE);
			}
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
		Button right = (Button) getActivity().findViewById(R.id.title_right_btn);
		if(right != null){
			right.setVisibility(View.GONE);
		}
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
