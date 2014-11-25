package com.inter.trade.ui.fragment.order;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.inter.trade.R;
import com.inter.trade.ui.fragment.BaseFragment;
import com.inter.trade.util.Constants;
import com.inter.trade.util.LoginUtil;

/**
 * 订单支付成功
 * @author apple
 *
 */

public class OrderSuccessFragment extends BaseFragment{
	private LayoutInflater mInflater;
	private static ArrayList<HashMap<String, String>> mMaps = new ArrayList<HashMap<String,String>>();
	
	public static OrderSuccessFragment createFragment(ArrayList<HashMap<String, String>> cData){
		OrderSuccessFragment fragment = new OrderSuccessFragment();
		mMaps = cData;
		return fragment;
	}
	public OrderSuccessFragment(){
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitle("支付成功");
		setBackVisible();
		mInflater= LayoutInflater.from(getActivity());
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
				// FragmentManager manager =
				// getActivity().getSupportFragmentManager();
				// int len = manager.getBackStackEntryCount();
				// if(len>0){
				// manager.popBackStack();
				// }else{
				getActivity().setResult(Constants.ACTIVITY_FINISH);
				getActivity().finish();
				// }
			}
		});

		menu = (Button) getActivity().findViewById(R.id.title_menu_btn);
		menu.setVisibility(View.GONE);
		Button right = (Button) getActivity()
				.findViewById(R.id.title_right_btn);
		right.setVisibility(View.GONE);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		LoginUtil.detection(getActivity());
		View view = inflater.inflate(R.layout.order_pay_success, container,false);
//		ListView mListView = (ListView)view.findViewById(R.id.result_listview);
		initResult(view);
		return view;
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setTitle("支付成功");
	}
	private void initResult(View view){
		LinearLayout containerLayout = (LinearLayout)view.findViewById(R.id.container);
		containerLayout.removeAllViews();
		for(int i =0;i<mMaps.size();i++){
			Item item = new Item(mMaps.get(i));
			containerLayout.addView(item.mView);
		}
		
	}
	class Item {
		private View mView;
		public Item(HashMap<String, String> item){
			mView = mInflater.inflate(R.layout.result_item, null);
			TextView name = (TextView)mView.findViewById(R.id.name);
			TextView content = (TextView)mView.findViewById(R.id.content);
			for(String string : item.keySet()){
				name.setText(string);
				content.setText(item.get(string));
			}
		}
	}
}
