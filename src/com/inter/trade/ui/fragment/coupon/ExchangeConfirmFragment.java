package com.inter.trade.ui.fragment.coupon;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.AdapterView.OnItemClickListener;

import com.inter.trade.R;
import com.inter.trade.ui.fragment.BaseFragment;

public class ExchangeConfirmFragment extends BaseFragment implements OnClickListener,OnItemClickListener{
	private Button cridet_back_btn;
	
	public ExchangeConfirmFragment()
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
		View view = inflater.inflate(R.layout.exchange_coupon_layout, container,false);
		cridet_back_btn = (Button)view.findViewById(R.id.cridet_back_btn);
		cridet_back_btn.setOnClickListener(this);
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
		transaction.replace(R.id.func_container, new ExchangeSubmitFragment());
		transaction.commit();
	}
}
