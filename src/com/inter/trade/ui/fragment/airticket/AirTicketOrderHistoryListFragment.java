package com.inter.trade.ui.fragment.airticket;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.inter.trade.AsyncLoadWork;
import com.inter.trade.AsyncLoadWork.AsyncLoadWorkListener;
import com.inter.trade.R;
import com.inter.trade.adapter.AirTicketRecordAdapter;
import com.inter.trade.adapter.MobileRecordAdapter;
import com.inter.trade.data.CommonData;
import com.inter.trade.ui.MyListView;
import com.inter.trade.ui.MyListView.OnPullDownListener;
import com.inter.trade.ui.MyListView.OnRefreshListener;
import com.inter.trade.ui.fragment.airticket.AirTicketMainQueryFragment.AirTicketQueryTask;
import com.inter.trade.ui.fragment.airticket.util.AirLineUtils;
import com.inter.trade.ui.fragment.airticket.util.ApiAirticketGetOrderHistoryData;
import com.inter.trade.ui.fragment.airticket.util.ApiAirticketGetOrderHistoryParser;
import com.inter.trade.ui.manager.IBaseFragment;
import com.inter.trade.ui.manager.UIManagerActivity;
import com.inter.trade.ui.manager.core.UIConstantDefault;
import com.inter.trade.util.Constants;

/**
 * 飞机票  历史订单 Fragment
 * @author zhichao.huang
 *
 */
public class AirTicketOrderHistoryListFragment extends IBaseFragment implements OnClickListener,OnItemClickListener, AsyncLoadWorkListener{
	
	private static final String TAG = AirTicketOrderHistoryListFragment.class.getName();
	
	private View rootView;
	
	private Button submitButton;
	
	private Bundle data = null;
	
	/**
	 * 订单号
	 */
	private String orderID;
	
	private TextView orderTextView;
	
	/**
	 * 统一的异步加载
	 */
	private AsyncLoadWork<ApiAirticketGetOrderHistoryData> asyncLoadWork = null;
	
	private MyListView mListView; 
	private boolean isMore=false;
	private int mStartIndex=0;//起始索引
	private int mTotalCount=0;//订单总条数
	private int mLoadedCount=0;//当前加载的总条数
	
	private AirTicketRecordAdapter mAdapter;
	/**
	 * 所有数据
	 */
	private ArrayList<ApiAirticketGetOrderHistoryData> mList = new ArrayList<ApiAirticketGetOrderHistoryData>();

	public static AirTicketOrderHistoryListFragment newInstance (Bundle data) {
		AirTicketOrderHistoryListFragment fragment = new AirTicketOrderHistoryListFragment();
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
	
	private void initViews (View rootView) {
//		submitButton = (Button)rootView.findViewById(R.id.register);
//		
//		submitButton.setOnClickListener(this);
		
//		orderTextView = (TextView)rootView.findViewById(R.id.orderID);
//		orderTextView.setText(data.getString("orderId") != null ? data.getString("orderId") :"无");
		
		mListView = (MyListView) rootView.findViewById(R.id.mm_listview);

		mListView.setOnItemClickListener(this);
		mListView.setonRefreshListener(onRefreshListener);

		mListView.setOnPullDownListener(onPullDownListener);
		mListView.setEnableAutoFetchMore(true);
		
	}
	
	
	
	@Override
	protected View onInitView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.airticket_order_list, container,false);
		initViews(rootView);
		return rootView;
	}

	@Override
	protected void onAsyncLoadData() {
		ApiAirticketGetOrderHistoryParser netParser = new ApiAirticketGetOrderHistoryParser();
		CommonData data = new CommonData();
		data.putValue("msgstart",mStartIndex+"");
		data.putValue("msgdisplay", Constants.RECORD_DISPLAY_COUNT+"");
		asyncLoadWork = new AsyncLoadWork<ApiAirticketGetOrderHistoryData>(getActivity(), netParser, data, this, false, true);
		asyncLoadWork.execute("ApiAirticket", "getOrderHistory");
	}

	@Override
	public void onRefreshDatas() {
		((UIManagerActivity)getActivity()).setTopTitle("历史订单");
		
	}
	
	@Override
	public void onTimeout() {
		// TODO Auto-generated method stub
		
	}
	
	OnPullDownListener onPullDownListener = new OnPullDownListener() {

		@Override
		public void onRefresh() {

		}

		@Override
		public void onMore() {
			isMore=true;
			onAsyncLoadData();
//			loadMore();

		}
	};
	private void loadMore(){
		if(mLoadedCount<mTotalCount){
			mStartIndex = mLoadedCount;
			onAsyncLoadData();

		}else{
			mListView.setLastText();
			//			mListView.setProgressGone();
			//			mListView.setIsFetchMoreing(true);
		}
	}
	
	OnRefreshListener onRefreshListener = new OnRefreshListener() {

		@Override
		public void onRefresh() {
			// TODO Auto-generated method stub
			isMore=false;
			mStartIndex=0;
			onAsyncLoadData();
		}
	};
	
	

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
	

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if(mAdapter == null) return;
		if(mList.size() == (position-1)){
			return;
		}
		ApiAirticketGetOrderHistoryData orderData = (ApiAirticketGetOrderHistoryData)mAdapter.getItem(position-1);
		
		AirTicketOrderHistoryListItemFragment.mMaps.clear();
		
		HashMap<String,String> item1 = new HashMap<String,String>();
		item1.put("出发城市", orderData.departCity);
		AirTicketOrderHistoryListItemFragment.mMaps.add(item1);

		HashMap<String,String> item2 = new HashMap<String,String>();
		item2.put("到达城市", orderData.arriveCity);
		AirTicketOrderHistoryListItemFragment.mMaps.add(item2);
		
		HashMap<String,String> item3 = new HashMap<String,String>();
		item3.put("出发时间", orderData.takeOffTime);
		AirTicketOrderHistoryListItemFragment.mMaps.add(item3);

//		HashMap<String,String> item4 = new HashMap<String,String>();
//		item4.put("到达时间", data.getString("arriveTime") != null ? data.getString("arriveTime") : "");
//		AirTicketOrderHistoryListItemFragment.mMaps.add(item4);
		
		HashMap<String,String> item5 = new HashMap<String,String>();
		item5.put("所属航班", orderData.flight);
		AirTicketOrderHistoryListItemFragment.mMaps.add(item5);

		HashMap<String,String> item6 = new HashMap<String,String>();
		item6.put("订单金额", orderData.totalPrice +"元");
		AirTicketOrderHistoryListItemFragment.mMaps.add(item6);
		
		HashMap<String,String> item0 = new HashMap<String,String>();
		item0.put("订单时间", orderData.createOrderTime);
		AirTicketOrderHistoryListItemFragment.mMaps.add(item0);
		
		HashMap<String,String> item7 = new HashMap<String,String>();
		item7.put("订单状态", AirLineUtils.getOrderState(orderData.orderProcess));
		AirTicketOrderHistoryListItemFragment.mMaps.add(item7);
		
		addFragmentToStack(UIConstantDefault.UI_CONSTANT_AIR_TICKET_ORDER_HISTORY_LISTITEM, 1, data);
		
	}

	@Override
	public void onSuccess(Object protocolDataList, Bundle bundle) {
//		if(bundle != null) {
//			mTotalCount = bundle.getInt("mTotalCount");//订单总条数
//			mLoadedCount = bundle.getInt("mLoadedCount");//当前加载的总条数
//		}
		
		if(!isMore){
			mList.clear();
		}
		ArrayList<ApiAirticketGetOrderHistoryData> tempList = (ArrayList<ApiAirticketGetOrderHistoryData>)protocolDataList;
		
		mList.addAll(tempList);
		
		mAdapter = new AirTicketRecordAdapter(getActivity(), mList);
		mListView.setAdapter(mAdapter);
		mAdapter.notifyDataSetChanged();
		
		if(isMore){
//			 mListView.setSelection(mLoadedCount);
			mListView.setSelection(mList.size() - tempList.size());
		 }
		 isMore=false;
		 mListView.setProgressGone();
		 mListView.setIsFetchMoreing(false);
		 mListView.onRefreshComplete();
		 
		 mStartIndex = mList.size();
	}

	@Override
	public void onFailure(String error) {
		mListView.setProgressGone();
		mListView.setIsFetchMoreing(false);
		mListView.onRefreshComplete();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if(asyncLoadWork != null){
			asyncLoadWork.cancel(true);
		}
	}

}
