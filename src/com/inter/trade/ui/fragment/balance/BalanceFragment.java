package com.inter.trade.ui.fragment.balance;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
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

public class BalanceFragment extends BaseFragment{
	
	public BalanceFragment(){
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitle("余额查询");
		setBackVisible();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.balance_layout, container,false);
		Button next = (Button)view.findViewById(R.id.btn_balance);
		next.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				FragmentTransaction transaction = getFragmentManager().beginTransaction();
				transaction.replace(R.id.func_container, new BalanceSuccessFragment());
				transaction.commit();
			}
		});
		return view;
	}
	
}
