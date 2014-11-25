package com.inter.trade.ui.fragment.airticket;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.inter.trade.R;
import com.inter.trade.ui.fragment.BaseFragment;
import com.inter.trade.ui.manager.UIManagerActivity;
import com.inter.trade.ui.manager.core.IMainHandlerManager;
import com.inter.trade.ui.manager.core.UIConstantDefault;

/**
 * 飞机票 其他 Fragment
 * @author zhichao.huang
 *
 */
public class AirTicketMainOtherFragment extends BaseFragment implements OnClickListener{
	
	private static final String TAG = AirTicketMainOtherFragment.class.getName();
	
	private View rootView;
	
	private Button submitButton;
	
	private Bundle data = null;

//	public AirTicketMainOtherFragment (Bundle bundleData) {
//		data = bundleData;
//	}
	
	public static AirTicketMainOtherFragment newInstance (Bundle data) {
		AirTicketMainOtherFragment fragment = new AirTicketMainOtherFragment();
		fragment.setArguments(data);
		return fragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "onCreate");
		Bundle bundle = getArguments();
		if(bundle != null) {
			data = bundle;
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.i(TAG, "onCreateView");
		((UIManagerActivity)getActivity()).setTopTitle("飞机票其它页");
		rootView = inflater.inflate(R.layout.safety_rigester_layout, container,false);
		initViews(rootView);
		return rootView;
	}
	
	private void initViews (View rootView) {
		submitButton = (Button)rootView.findViewById(R.id.register);
		
		
		submitButton.setOnClickListener(this);
		
	}
	
	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.register:
//			((UIManagerActivity)getActivity()).testData = "15812345678";
//			((UIManagerActivity)getActivity()).removeFragmentToStack();
			break;

		default:
			break;
		}
		
	}

}
