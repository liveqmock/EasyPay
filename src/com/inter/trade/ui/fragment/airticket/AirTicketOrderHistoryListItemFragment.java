package com.inter.trade.ui.fragment.airticket;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.inter.trade.R;
import com.inter.trade.ui.fragment.airticket.AirTicketOrderPaySuccessFragment.Item;
import com.inter.trade.ui.manager.IBaseFragment;
import com.inter.trade.ui.manager.UIManagerActivity;

/**
 * 飞机票  历史订单详情 Fragment
 * @author zhichao.huang
 *
 */
public class AirTicketOrderHistoryListItemFragment extends IBaseFragment implements OnClickListener{
	
	private static final String TAG = AirTicketOrderHistoryListItemFragment.class.getName();
	
	private LayoutInflater mInflater;

	private View rootView;

	private Button submitButton;

	private Bundle data = null;

	/**
	 * 订单号
	 */
	private String orderID;

	private TextView orderTextView;

	/**
	 * 订单待显示的数据
	 */
	public static ArrayList<HashMap<String, String>> mMaps = new ArrayList<HashMap<String,String>>();

	public static AirTicketOrderHistoryListItemFragment newInstance (Bundle data) {
		AirTicketOrderHistoryListItemFragment fragment = new AirTicketOrderHistoryListItemFragment();
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
		mInflater= LayoutInflater.from(getActivity());
	}

	private void initViews (View rootView) {
		//		submitButton = (Button)rootView.findViewById(R.id.register);
		//		
		//		submitButton.setOnClickListener(this);


		LinearLayout containerLayout = (LinearLayout)rootView.findViewById(R.id.container);
		containerLayout.removeAllViews();

//		initDatas() ;
		if( mMaps != null && mMaps.size() > 0) {
			for(int i =0;i<mMaps.size();i++){
				Item item = new Item(mMaps.get(i));
				containerLayout.addView(item.mView);
			}
		}
		
		Button see_history = (Button)rootView.findViewById(R.id.see_history);
		see_history.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getActivity().finish();
			}
		});
		see_history.setVisibility(View.GONE);

		//		orderTextView = (TextView)rootView.findViewById(R.id.orderID);
		//		orderTextView.setText(data.getString("orderId") != null ? data.getString("orderId") :"无");

	}
	
	@Override
	public void onTimeout() {
		// TODO Auto-generated method stub
		
	}

//	/**
//	 * 初始化数据
//	 */
//	private void initDatas() {
//
//		if(data != null) {
//			mMaps = new ArrayList<HashMap<String,String>>();
//
//			HashMap<String,String> item0 = new HashMap<String,String>();
//			item0.put("订单编号", data.getString("orderId") != null ? data.getString("orderId") :"无");
//			mMaps.add(item0);
//
//			HashMap<String,String> item1 = new HashMap<String,String>();
//			item1.put("出发城市", data.getString("departCityNameCh") != null ? data.getString("departCityNameCh") :"");
//			mMaps.add(item1);
//
//			HashMap<String,String> item2 = new HashMap<String,String>();
//			item2.put("到达城市", data.getString("arriveCityNameCh") != null ? data.getString("arriveCityNameCh") :"");
//			mMaps.add(item2);
//
//			HashMap<String,String> item3 = new HashMap<String,String>();
//			item3.put("出发时间", data.getString("departTime") != null ? data.getString("departTime") : "");
//			mMaps.add(item3);
//
//			HashMap<String,String> item4 = new HashMap<String,String>();
//			item4.put("到达时间", data.getString("arriveTime") != null ? data.getString("arriveTime") : "");
//			mMaps.add(item4);
//
//			HashMap<String,String> item5 = new HashMap<String,String>();
//			item5.put("所属航班", data.getString("flight") != null ? data.getString("flight") : "");
//			mMaps.add(item5);
//
//			HashMap<String,String> item6 = new HashMap<String,String>();
//			item6.put("订单金额", data.getString("amout") != null ? data.getString("amout") +"元": "");
//			mMaps.add(item6);
//
//		}
//
//	}

	@Override
	protected View onInitView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.airticket_order_success, container,false);
		initViews(rootView);
		return rootView;
	}

	@Override
	protected void onAsyncLoadData() {

	}

	@Override
	public void onRefreshDatas() {
		((UIManagerActivity)getActivity()).setTopTitle("详情");

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

	class Item {
		private View mView;
		public Item(HashMap<String, String> item){
			mView = mInflater.inflate(R.layout.airticket_result_item, null);
			TextView name = (TextView)mView.findViewById(R.id.name);
			TextView content = (TextView)mView.findViewById(R.id.content);
			for(String string : item.keySet()){
				name.setText(string);
				content.setText(item.get(string));
			}
		}
	}
}
