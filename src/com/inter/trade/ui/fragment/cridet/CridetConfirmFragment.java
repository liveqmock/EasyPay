package com.inter.trade.ui.fragment.cridet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.inter.trade.R;
import com.inter.trade.ui.fragment.BaseFragment;
import com.inter.trade.ui.fragment.cridet.data.JournalData;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.NumberFormatUtil;
import com.unionpay.uppayplugin.demo.UnionpayUtil;

/**
 * 确认还款
 * @author apple
 *
 */

public class CridetConfirmFragment extends BaseFragment{
	private static String cost;
	private static String allmoney;
	private static String bankno;
	
	private TextView return_cridet_back;
	private TextView return_cridet_count;
	private TextView return_cridet_cost;
	private TextView return_swip_cost;
	private TextView pay_card_no;
	
	public static CridetConfirmFragment create(String costString,String allString,String no){
		CridetConfirmFragment f = new CridetConfirmFragment();
		cost = costString;
		allmoney = allString;
		bankno = no;
		return f;
	}
	public CridetConfirmFragment(){
		
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
		View view = inflater.inflate(R.layout.cridet_confirm_layout, container,false);
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
	
	private void initData(){
		return_cridet_back.setText(CridetCardFragment.mJournalData.getValue(
				JournalData.shoucardno));
		return_cridet_count.setText(NumberFormatUtil.format2(CridetCardFragment.mJournalData.getValue(
				JournalData.paymoney))+"元");
		if(null == cost || "".equals(cost)){
			cost="0元";
		}
		return_cridet_cost.setText(NumberFormatUtil.format2(cost)+"元");
		if(allmoney==null || allmoney.equals("")){
			allmoney=CridetCardFragment.mJournalData.getValue(
					JournalData.paymoney);
		}
		return_swip_cost.setText(NumberFormatUtil.format2(allmoney)+"元");
		pay_card_no.setText(CridetCardFragment.mJournalData.getValue(
				JournalData.fucardno));
	}
	
}
