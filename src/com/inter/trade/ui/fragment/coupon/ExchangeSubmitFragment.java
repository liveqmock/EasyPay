package com.inter.trade.ui.fragment.coupon;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.inter.trade.R;
import com.inter.trade.ui.fragment.BaseFragment;

public class ExchangeSubmitFragment extends BaseFragment implements OnClickListener,OnItemClickListener{
	
	
	public ExchangeSubmitFragment()
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
		View view = inflater.inflate(R.layout.exchange_confirm_layout, container,false);
		setBackVisible();
		setTitle("兑换优惠券");
		return view;
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.cridet_back_btn:
			showChuxuka();
			break;

		default:
			break;
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}

	private void showChuxuka(){
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.func_container, new BuySuccessFragment());
		transaction.commit();
	}
}
