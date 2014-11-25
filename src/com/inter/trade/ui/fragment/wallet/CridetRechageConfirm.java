package com.inter.trade.ui.fragment.wallet;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.inter.trade.R;
import com.inter.trade.ui.fragment.BaseFragment;
import com.inter.trade.util.LoginUtil;

public class CridetRechageConfirm extends BaseFragment implements OnClickListener{
	private Button cridet_confirm_btn;
	
	
	public CridetRechageConfirm()
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
		View view = inflater.inflate(R.layout.recharge_cridet_confirm_layout, container,false);
		cridet_confirm_btn = (Button)view.findViewById(R.id.cridet_confirm_btn);
		
		cridet_confirm_btn.setOnClickListener(this);
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
		case R.id.cridet_confirm_btn:
			showChuxuka();
			break;

		default:
			break;
		}
	}
	
	private void showChuxuka(){
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.func_container, new CridetRechageFinish());
		transaction.commit();
	}
}
