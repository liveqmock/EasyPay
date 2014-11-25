package com.inter.trade.ui.fragment.wallet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.inter.trade.R;
import com.inter.trade.ui.fragment.BaseFragment;
import com.inter.trade.ui.fragment.wallet.util.RechargeData;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.NumberFormatUtil;
import com.unionpay.uppayplugin.demo.UnionpayUtil;

/**
 * 确认充值
 * @author apple
 *
 */

public class ConfirmFragment extends BaseFragment{
	public static RechargeData mRechargeData = new RechargeData();
	public static String mBkno;
	private TextView recharge_cridet_no;
	private TextView recharge_phone_no;
	private TextView recharge_count;
	public static ConfirmFragment create(String no){
		mBkno = no;
		return new ConfirmFragment();
	}
	public ConfirmFragment(){
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitle(getString(R.string.cridet_swip_confirm_title));
		setBackVisible();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		LoginUtil.detection(getActivity());
		View view = inflater.inflate(R.layout.recharge_confirm_layout, container,false);
		Button next = (Button)view.findViewById(R.id.swip_confirm_btn);
		recharge_cridet_no = (TextView)view.findViewById(R.id.recharge_cridet_no);
		recharge_phone_no = (TextView)view.findViewById(R.id.recharge_phone_no);
		recharge_count = (TextView)view.findViewById(R.id.recharge_count);
		
		recharge_cridet_no.setText(mRechargeData.getValue(RechargeData.cardno));
		recharge_phone_no.setText(mRechargeData.getValue(RechargeData.cardmobile));
		recharge_count.setText(NumberFormatUtil.format2(mRechargeData.getValue(RechargeData.paymoney))+"元");
		
		next.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showChuxuka();
			}
		});
		
		return view;
	}
	private void showChuxuka(){
		UnionpayUtil.startUnionPlungin(mBkno, getActivity());
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setTitle("充值");
	}
	
}
