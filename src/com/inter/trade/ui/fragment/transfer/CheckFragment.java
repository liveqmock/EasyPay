package com.inter.trade.ui.fragment.transfer;

import com.inter.trade.R;
import com.inter.trade.ui.CommonActivity;
import com.inter.trade.ui.fragment.BaseFragment;
import com.inter.trade.ui.fragment.transfer.util.TransferData;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.NumberFormatUtil;
import com.unionpay.mpay.widgets.v;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 核对信息界面
 * @author apple
 *
 */
public class CheckFragment extends BaseFragment implements OnClickListener{
	private TextView openBankName;
	private TextView recieve_card;
	private TextView recieverName;
	private TextView transferCount;
	private TextView arriveWay;
	private TextView phoneNumber;
	private TextView transerCost;
	private TextView swipMoney;
	private TextView bakContent;
	
	public CheckFragment()
	{
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitle(getString(R.string.check_title));
		setBackVisible();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		LoginUtil.detection(getActivity());
		View view = inflater.inflate(R.layout.check_transfer_info, container, false);
		Button check_finish = (Button)view.findViewById(R.id.check_finish);
		check_finish.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				FragmentTransaction transaction = getFragmentManager().beginTransaction();
				transaction.replace(R.id.func_container, new TransferSwipFragment());
				transaction.commit();
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
		setTitle(getString(R.string.check_title));
	}

	private void initView(View view)
	{
		openBankName = (TextView)view.findViewById(R.id.openBankName);
		recieve_card = (TextView)view.findViewById(R.id.recieve_card);
		recieverName = (TextView)view.findViewById(R.id.recieverName);
		transferCount = (TextView)view.findViewById(R.id.transferCount);
		arriveWay = (TextView)view.findViewById(R.id.arriveWay);
		phoneNumber = (TextView)view.findViewById(R.id.phoneNumber);
		transerCost = (TextView)view.findViewById(R.id.transerCost);
		swipMoney = (TextView)view.findViewById(R.id.swipMoney);
		bakContent = (TextView)view.findViewById(R.id.bakContent);
	}
	
	private void initData(){
		openBankName.setText(CommonActivity.mTransferData.getValue(TransferData.shoucardbank));
		recieve_card.setText(CommonActivity.mTransferData.getValue(TransferData.shoucardno));
		recieverName.setText(CommonActivity.mTransferData.getValue(TransferData.shoucardman));
		transferCount.setText(NumberFormatUtil.format2(CommonActivity.mTransferData.getValue(TransferData.paymoney))+"元");
//		arriveWay.setText(CommonActivity.mTransferData.getValue(TransferData.arriveid));
		arriveWay.setText(CommonActivity.mTransferData.getValue(TransferData.arrivetime));
		transerCost.setText(NumberFormatUtil.format2(CommonActivity.mTransferData.getValue(TransferData.payfee))+"元");
		float cost = Float.parseFloat(CommonActivity.mTransferData.getValue(TransferData.payfee));
		float pay = Float.parseFloat(CommonActivity.mTransferData.getValue(TransferData.paymoney));
		CommonActivity.mTransferData.putValue(TransferData.money, (cost+pay)+"");
		swipMoney.setText(NumberFormatUtil.format2((cost+pay)+"")+"元");
		bakContent.setText(CommonActivity.mTransferData.getValue(TransferData.shoucardmemo));
		phoneNumber.setText(CommonActivity.mTransferData.getValue(TransferData.shoucardmobile));
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

		default:
			break;
		}
		
	}
	
}
