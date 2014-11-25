package com.inter.trade.ui.fragment.balance;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.inter.trade.R;
import com.inter.trade.ui.fragment.BaseFragment;

/**
 * 余额查询
 * @author apple
 *
 */

public class BalanceSuccessFragment extends BaseFragment{
	
	public BalanceSuccessFragment(){
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitle("查询结果");
		setBackVisible();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.balance_success, container,false);
		Button next = (Button)view.findViewById(R.id.btn_balance);
		return view;
	}
	
}
