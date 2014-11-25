package com.inter.trade.ui.fragment.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ToggleButton;

import com.inter.trade.R;
import com.inter.trade.ui.RechargeActivity;
import com.inter.trade.ui.fragment.BaseFragment;
import com.inter.trade.ui.fragment.FragmentFactory;
import com.inter.trade.ui.fragment.wallet.util.RechargeData;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PromptUtil;

public class RechageFragment extends BaseFragment implements OnClickListener{
	private Button cridet_back_btn;
	private static String RECHAREG_TYPE="RECHAREG_TYPE";
	private String mType ="";
	private EditText count_edit;
	private String mCount ="0";
	private ToggleButton smsToggle;
	
	public RechageFragment()
	{
		
	}
	
	public static RechageFragment createFragment(String type){
		RechageFragment fragment = new RechageFragment();
		final Bundle args = new Bundle();
        args.putString(RECHAREG_TYPE, type);
        fragment.setArguments(args);
        return fragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mType = getArguments() != null ? getArguments().getString(RECHAREG_TYPE) : null;
		ConfirmFragment.mRechargeData.putValue(RechargeData.sendsms, "1");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		LoginUtil.detection(getActivity());
		View view = inflater.inflate(R.layout.recharge_count, container,false);
		cridet_back_btn = (Button)view.findViewById(R.id.cridet_back_btn);
		count_edit = (EditText)view.findViewById(R.id.count_edit);
		smsToggle = (ToggleButton)view.findViewById(R.id.smsToggle);
		smsToggle.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					ConfirmFragment.mRechargeData.putValue(RechargeData.sendsms, "1");
				}else{
					ConfirmFragment.mRechargeData.putValue(RechargeData.sendsms, "0");
				}
			}
		});
		
		cridet_back_btn.setOnClickListener(this);
		setTitle(getString(R.string.recharge_title));
		setBackVisible();
		return view;
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setTitle(getString(R.string.recharge_title));
		
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.cridet_back_btn:
			if(checkInput()){
				showChuxuka();
			}
			break;

		default:
			break;
		}
	}
	
	private boolean checkInput(){
		String temp = count_edit.getText().toString();
		if(null == temp || "".equals(temp)){
			PromptUtil.showToast(getActivity(), "请输入充值金额");
			return false;
		}
		mCount = temp;
		CridetRechageFinish.mRechargeString = temp;
		return true;
	}
	
	private void showChuxuka(){
//		FragmentTransaction transaction = getFragmentManager().beginTransaction();
//		if(mType.equals("0")){
//			transaction.replace(R.id.func_container, CridetRechageFragment.create(mCount));
//		}else{
//			transaction.replace(R.id.func_container, new DepositRechageFragment());
//		}
//		transaction.commit();
		Intent intent = new Intent();
		if(mType.equals("0")){
			
			intent.putExtra(FragmentFactory.INDEX_KEY, FragmentFactory.CridetRechageFragment_INDEX);
		}else {
			intent.putExtra(FragmentFactory.INDEX_KEY, FragmentFactory.DepositRechageFragment_INDEX);
		}
		intent.setClass(getActivity(), RechargeActivity.class);
		getActivity().startActivityForResult(intent, 0);
		ConfirmFragment.mRechargeData.putValue(RechargeData.paymoney, mCount);
		ConfirmFragment.mRechargeData.putValue("authorid", LoginUtil.mLoginStatus.authorid);
	}
}
