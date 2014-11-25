package com.inter.trade.ui.fragment.airticket;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.inter.trade.R;
import com.inter.trade.ui.MyListView;
import com.inter.trade.ui.fragment.BaseFragment;
import com.inter.trade.ui.manager.UIManagerActivity;
import com.inter.trade.ui.manager.core.IMainHandlerManager;
import com.inter.trade.ui.manager.core.UIConstantDefault;

/**
 * 飞机票主Fragment
 * @author zhichao.huang
 *
 */
public class AirTicketMainFragment extends BaseFragment implements OnClickListener{
	
	private static final String TAG = AirTicketMainFragment.class.getName();
	
	private View rootView;
	
	/**
	 * 单程按钮
	 */
	private Button danchengButton;
	
	/**
	 * 往返按钮
	 */
	private Button wangfanButton;
	
	private EditText editText;
	
	private Bundle data = null;
	
	private boolean isSelecte = true;
	
	
	public static AirTicketMainFragment newInstance (Bundle data) {
		AirTicketMainFragment fragment = new AirTicketMainFragment();
		fragment.setArguments(data);
		return fragment;
	}
	
//	public AirTicketMainFragment (Bundle bundleData) {
//		data = bundleData;
//	}
	
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
		((UIManagerActivity)getActivity()).setTopTitle("机票查询");
		
		rootView = inflater.inflate(R.layout.airticket_main_layout, container,false);
		initViews(rootView);
		
		if(((UIManagerActivity)getActivity()).danOrFan == 0) {
			setDanchengSelecte();
			switchDanchengFragment();
		}else{
			setWangfanSelecte();
			switchWangfanFragment();
		}
		
		
		return rootView;
	}
	
	
	
	@Override
	public void onResume() {
		Log.i(TAG, "onResume");
		//模拟后一个页面返回前一个页面 填充数据
//		if( test != null && editText != null) {
//			editText.setText(test);
//		}
		super.onResume();
	}

	private void initViews (View rootView) {
		danchengButton = (Button)rootView.findViewById(R.id.dancheng);
		wangfanButton = (Button)rootView.findViewById(R.id.wangfan);
		editText = (EditText)rootView.findViewById(R.id.pay_name_edit);
		
		danchengButton.setOnClickListener(this);
		wangfanButton.setOnClickListener(this);
		
	}
	
	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.dancheng:
			setDanchengSelecte();
			switchDanchengFragment();
			break;
		case R.id.wangfan:
			setWangfanSelecte();
			switchWangfanFragment();
			break;
		default:
			break;
		}
		
	}
	
	private void setDanchengSelecte() {
		danchengButton.setSelected(true);
		wangfanButton.setSelected(false);
	}
	
	private void setWangfanSelecte() {
		danchengButton.setSelected(false);
		wangfanButton.setSelected(true);
	}
	
	private void switchDanchengFragment(){
		FragmentTransaction ft= getActivity().getSupportFragmentManager().beginTransaction();
		ft.replace(R.id.airticket_dancheng_container, AirTicketMainDanchengFragment.newInstance(null));
		ft.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)/**.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)*/;
		ft.commit();
	}
	
	private void switchWangfanFragment(){
		FragmentTransaction ft= getActivity().getSupportFragmentManager().beginTransaction();
		ft.replace(R.id.airticket_dancheng_container, AirTicketMainWangfanFragment.newInstance(null));
		ft.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)/**.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)*/;
		ft.commit();
	}

}
