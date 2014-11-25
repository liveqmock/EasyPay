package com.inter.trade.ui.fragment.order;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.inter.trade.R;
import com.inter.trade.ui.fragment.BaseFragment;
import com.inter.trade.ui.fragment.order.util.OrderData;
import com.inter.trade.util.LoginUtil;

/**
 * 确认订单
 * @author apple
 *
 */

public class OrderConfirmFragment extends BaseFragment{
	private static String bankno;
	private static OrderData mData;
	private static String mCardNo;
	private static String mBankname;
	
	private TextView return_cridet_back;
	private TextView return_cridet_count;
	private TextView return_cridet_cost;
	private TextView return_swip_cost;
	private TextView pay_card_no;
	
	public static OrderConfirmFragment create(OrderData data,String no,String cardNo,String bankname){
		OrderConfirmFragment f = new OrderConfirmFragment();
		bankno = no;
		mData = data;
		mCardNo = cardNo;
		mBankname = bankname;
		return f;
	}
	public OrderConfirmFragment(){
		
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
		View view = inflater.inflate(R.layout.order_confirm_layout, container,false);
		Button next = (Button)view.findViewById(R.id.swip_confirm_btn);
		next.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				FragmentTransaction transaction = getFragmentManager().beginTransaction();
				transaction.replace(R.id.func_container, CardCridetFragment.create(mData, 
						bankno, mCardNo, mBankname));
				transaction.commit();
//				UnionpayUtil.startUnionPlungin(bankno, getActivity());
			}
		});
		initView(view);
		initData();
		return view;
	}
	
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setTitle(getString(R.string.cridet_swip_confirm_title));
	}
	private void initView(View view){
		return_cridet_back = (TextView)view.findViewById(R.id.return_cridet_back);
		return_cridet_count = (TextView)view.findViewById(R.id.return_cridet_count);
		return_cridet_cost = (TextView)view.findViewById(R.id.return_cridet_cost);
		return_swip_cost = (TextView)view.findViewById(R.id.return_swip_cost);
		pay_card_no = (TextView)view.findViewById(R.id.pay_card_no);
	}
	
	private void initData(){
		return_cridet_back.setText(mData.orderno);
		return_cridet_count.setText(mData.ordermoney);
		return_cridet_cost.setText(mData.ordermoney);
		pay_card_no.setText(mCardNo);
	}
	
}
