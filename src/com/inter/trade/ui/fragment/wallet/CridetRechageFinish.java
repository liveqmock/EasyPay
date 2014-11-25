package com.inter.trade.ui.fragment.wallet;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.inter.trade.R;
import com.inter.trade.ui.fragment.BaseFragment;
import com.inter.trade.util.Constants;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.NumberFormatUtil;

public class CridetRechageFinish extends BaseFragment implements OnClickListener{
	private Button cridet_confirm_btn;
	private TextView card_name;
	private TextView card_no;
	private TextView rechargeCount;
	private TextView serialNumber;
	
	public static String mCardNoString;
	public static String mRechargeString;
	public static String mSerialNumString;
	public static String mCardNameString;
	
	
	private static ArrayList<HashMap<String, String>> mMaps = new ArrayList<HashMap<String,String>>();
	private static String mType;
	public static CridetRechageFinish createFragment(ArrayList<HashMap<String, String>> cData,String type){
		CridetRechageFinish fragment = new CridetRechageFinish();
		mMaps = cData;
		mType = type;
		return fragment;
	}
	
	
	@Override
	protected void setBackVisible() {
		// TODO Auto-generated method stub
		if (getActivity() == null) {
			return;
		}
		Button back = (Button) getActivity().findViewById(R.id.title_back_btn);

		if (back == null) {
			return;
		}
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showChuxuka();
			}
		});

		menu = (Button) getActivity().findViewById(R.id.title_menu_btn);
		menu.setVisibility(View.GONE);
		Button right = (Button) getActivity()
				.findViewById(R.id.title_right_btn);
		right.setVisibility(View.GONE);
	}


	public CridetRechageFinish()
	{
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitle("充值");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		LoginUtil.detection(getActivity());
		View view = inflater.inflate(R.layout.recharge_cridet_finish, container,false);
		cridet_confirm_btn = (Button)view.findViewById(R.id.submit);
		card_name = (TextView)view.findViewById(R.id.card_name);
		
		rechargeCount = (TextView)view.findViewById(R.id.rechargeCount);
		card_no = (TextView)view.findViewById(R.id.card_no);
		serialNumber = (TextView)view.findViewById(R.id.serialNumber);
		
		rechargeCount.setText(NumberFormatUtil.format2(mRechargeString)+"元");
		card_no.setText(mCardNoString);
		serialNumber.setText(mSerialNumString);
		
//		if(mType.equals("0")){
//			card_name.setText("北京银行信用卡");
//		}else{
//			card_name.setText("北京银行储蓄卡");
//		}
		card_name.setText(mCardNameString);
		cridet_confirm_btn.setOnClickListener(this);
		setTitle(getString(R.string.recharge_title));
		setBackVisible();
		return view;
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.submit:
			showChuxuka();
			break;

		default:
			break;
		}
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setTitle("充值");
	}


	private void showChuxuka(){
		getActivity().setResult(Constants.ACTIVITY_FINISH);
		getActivity().finish();
	}
}
