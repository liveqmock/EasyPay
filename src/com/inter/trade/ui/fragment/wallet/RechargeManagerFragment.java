package com.inter.trade.ui.fragment.wallet;

import com.inter.trade.R;
import com.inter.trade.ui.RechargeSelectedActivity;
import com.inter.trade.ui.fragment.BaseFragment;
import com.inter.trade.ui.fragment.FragmentFactory;
import com.inter.trade.ui.fragment.wallet.util.RechargeData;
import com.inter.trade.util.LoginUtil;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

public class RechargeManagerFragment extends BaseFragment implements OnClickListener{
	private RelativeLayout recharge_cridet;
	private RelativeLayout recharge_chuxuka;
	
	
	public RechargeManagerFragment()
	{
			
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		LoginUtil.detection(getActivity());
		View view = inflater.inflate(R.layout.recharge_layout, container,false);
		recharge_chuxuka = (RelativeLayout)view.findViewById(R.id.recharge_chuxuka);
		recharge_cridet = (RelativeLayout)view.findViewById(R.id.recharge_cridet);
		
		recharge_chuxuka.setOnClickListener(this);
		recharge_cridet.setOnClickListener(this);
		
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
		case R.id.recharge_chuxuka:
			showChuxuka();
			break;
		case R.id.recharge_cridet:
			showCridet();
			break;

		default:
			break;
		}
	}
	
	private void showChuxuka(){
//		FragmentTransaction transaction = getFragmentManager().beginTransaction();
//		transaction.replace(R.id.func_container, RechageFragment.createFragment("1"));
//		transaction.commit();
		Intent intent = new Intent();
		intent.putExtra(FragmentFactory.INDEX_KEY, "1");
		intent.setClass(getActivity(), RechargeSelectedActivity.class);
		getActivity().startActivityForResult(intent, 200);
		ConfirmFragment.mRechargeData.sunMap.put(RechargeData.banktype, "depositcard");
	}
	private void showCridet(){
		Intent intent = new Intent();
		intent.putExtra(FragmentFactory.INDEX_KEY, "0");
		intent.setClass(getActivity(), RechargeSelectedActivity.class);
		getActivity().startActivityForResult(intent, 200);
//		FragmentTransaction transaction = getFragmentManager().beginTransaction();
//		transaction.replace(R.id.func_container, RechageFragment.createFragment("0"));
//		transaction.commit();
		ConfirmFragment.mRechargeData.sunMap.put(RechargeData.banktype, "creditcard");
	}
}
