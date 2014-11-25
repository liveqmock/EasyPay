package com.inter.trade.ui.fragment.transfer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.inter.trade.R;
import com.inter.trade.ui.CommonActivity;
import com.inter.trade.ui.fragment.BaseFragment;
import com.inter.trade.ui.fragment.returndaikuan.util.DaikuanData;
import com.inter.trade.util.LoginUtil;
import com.unionpay.uppayplugin.demo.UnionpayUtil;

/**
 * 确认转账
 * @author apple
 *
 */

public class TransferConfirmFragment extends BaseFragment{
	
	
	private static String bankno;
	private TextView return_cridet_back;
	private TextView return_cridet_count;
	private TextView return_cridet_cost;
	private TextView return_swip_cost;
	private TextView pay_card_no;
	
	public static TransferConfirmFragment create(String no){
		TransferConfirmFragment f = new TransferConfirmFragment();
		bankno = no;
		return f;
	}
	public TransferConfirmFragment(){
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitle("确认转账");
		setBackVisible();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		LoginUtil.detection(getActivity());
		View view = inflater.inflate(R.layout.daikuan_confirm_layout, container,false);
		Button next = (Button)view.findViewById(R.id.swip_confirm_btn);
		next.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//				FragmentTransaction transaction = getFragmentManager().beginTransaction();
//				transaction.replace(R.id.func_container, new SubmitFragment());
//				transaction.commit();
				UnionpayUtil.startUnionPlungin(bankno, getActivity());
			}
		});
		initView(view);
		initData();
		return view;
	}
	private void initView(View view){
		return_cridet_back = (TextView)view.findViewById(R.id.return_cridet_back);
		return_cridet_count = (TextView)view.findViewById(R.id.return_cridet_count);
		return_cridet_cost = (TextView)view.findViewById(R.id.return_cridet_cost);
		return_swip_cost = (TextView)view.findViewById(R.id.return_swip_cost);
		pay_card_no = (TextView)view.findViewById(R.id.pay_card_no);
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setTitle("确认转账");
	}
	private void initData(){
		try {
				float cost = Float.parseFloat(CommonActivity.mTransferData.getValue(
						DaikuanData.payfee));
				float money = Float.parseFloat(CommonActivity.mTransferData.getValue(
						DaikuanData.money));
				return_swip_cost.setText((money)+"元");
			return_cridet_back.setText(CommonActivity.mTransferData.getValue(
					DaikuanData.shoucardbank));
			return_cridet_count.setText(CommonActivity.mTransferData.getValue(
					DaikuanData.paymoney));
			return_cridet_cost.setText(CommonActivity.mTransferData.getValue(
					DaikuanData.payfee));
			
			pay_card_no.setText(CommonActivity.mTransferData.getValue(
					DaikuanData.fucardno));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}
	
}
