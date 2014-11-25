package com.inter.trade.ui.fragment.transfer2;

import java.util.ArrayList;

import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.inter.protocol.body.BankRecordParser;
import com.inter.trade.AsyncLoadWork;
import com.inter.trade.AsyncLoadWork.AsyncLoadWorkListener;
import com.inter.trade.R;
import com.inter.trade.data.BankRecordData;
import com.inter.trade.data.CommonData;
import com.inter.trade.ui.fragment.airticket.util.ApiAirticketDeletePeopleParser;
import com.inter.trade.ui.fragment.transfer2.util.TransferBankListAdapter;
import com.inter.trade.ui.fragment.transfer2.util.TransferDeleteBankParser;
import com.inter.trade.ui.manager.IBaseFragment;
import com.inter.trade.ui.manager.UIManagerActivity;
import com.inter.trade.ui.manager.core.UIConstantDefault;

/**
 * 转账-历史交易银行记录 Fragment
 * @author zhichao.huang
 *
 */
public class TransferBankListFragment extends IBaseFragment implements OnItemClickListener, AsyncLoadWorkListener{

	private static final String TAG = TransferBankListFragment.class.getName();

	private View rootView;

	private Bundle bundleData = null;

	/**
	 * 可左滑Item的listview
	 */
	private SwipeMenuListView mListView; 

	/**
	 * 请求银行记录的类型,默认超级转账
	 */
	private String paytype = "suptfmg";

	/**
	 * 银行卡列表
	 */
	private ArrayList<BankRecordData> tempList;

	public static TransferBankListFragment newInstance (Bundle data) {
		TransferBankListFragment fragment = new TransferBankListFragment();
		fragment.setArguments(data);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "onCreate");
		Bundle bundle = getArguments();
		if(bundle != null) {
			bundleData = bundle;
			paytype = bundleData.getString("paytype");
		}
	}

	@Override
	protected View onInitView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.i(TAG, "onInitView");
		rootView = inflater.inflate(R.layout.transfer2_banklist, container,false);
		initViews(rootView);
		return rootView;
	}

	@Override
	protected void onAsyncLoadData() {
		if (bundleData != null) {
			BankRecordParser netParser = new BankRecordParser();
			CommonData data = new CommonData();
			data.putValue("paytype", "suptfmg,tfmg");
			new AsyncLoadWork<BankRecordData>(getActivity(), netParser, data, this, false, true,false,false)
			.execute("ApiPayinfo", "readshoucardList");
		}
	}

	@Override
	public void onSuccess(Object protocolDataList, Bundle bundle) {
		if(protocolDataList != null) {
			tempList = (ArrayList<BankRecordData>)protocolDataList;
			mListView.setAdapter(new TransferBankListAdapter(getActivity(),tempList));
		}
	}

	@Override
	public void onFailure(String error) {
	}

	@Override
	public void onRefreshDatas() {
		
		if(getActivity()!= null && ((UIManagerActivity)getActivity()).isRefresh) {
			onAsyncLoadData();
			((UIManagerActivity)getActivity()).isRefresh = false;
		}
	}

	@Override
	public void onTimeout() {
		onAsyncLoadData();
	}

	private void initViews (View rootView) {
		mListView = (SwipeMenuListView)rootView.findViewById(R.id.listview);
		mListView.setOnItemClickListener(this);
		
		LinearLayout emptyView=(LinearLayout) rootView.findViewById(R.id.tv_emptyview);
		mListView.setEmptyView(emptyView);
		
		initSwipeMenuListView();

		//发起新的转账
		rootView.findViewById(R.id.new_transfer).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Bundle data = new Bundle();
				data.putString("paytype", paytype);
				addFragmentToStack(UIConstantDefault.UI_CONSTANT_TRANSFER_SHOUKUAN_CONFIRM, 1, data);
			}
		});
	}

	/**
	 * init SwipeMenuListView
	 */
	private void initSwipeMenuListView() {
		// step 1. create a MenuCreator
		SwipeMenuCreator creator = new SwipeMenuCreator() {

			@Override
			public void create(SwipeMenu menu) {
				// create "delete" item
				SwipeMenuItem deleteItem = new SwipeMenuItem(
						getActivity());
				// set item background
				deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
						0x3F, 0x25)));
				// set item width
				deleteItem.setWidth(dp2px(90));
				// set a icon
				deleteItem.setIcon(R.drawable.ic_delete);
				// add to menu
				menu.addMenuItem(deleteItem);
			}
		};
		
		// set creator
		mListView.setMenuCreator(creator);

		// step 2. listener item click event
		mListView.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public void onMenuItemClick(int position, SwipeMenu menu, int index) {
				if(tempList == null || tempList.size() <= 0) return;
				BankRecordData item = tempList.get(position);
				switch (index) {
				case 0:
					if(tempList.size() == 1) {
						tempList.remove(item);
						mListView.setAdapter(new TransferBankListAdapter(getActivity(),tempList));
					}
					// open
					deleteBankItem(item.shoucardno);
					break;
				}
			}
		});
	}

	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				getResources().getDisplayMetrics());
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if(tempList != null && tempList.size() > 0) {
			BankRecordData bankRecordData = tempList.get(position);
			Bundle data = new Bundle();
			data.putSerializable("bankShoukuanRecordData", bankRecordData);
			data.putString("paytype", paytype);
			addFragmentToStack(UIConstantDefault.UI_CONSTANT_TRANSFER_SHOUKUAN_CONFIRM, 1, data);
		}


	}

	/**
	 * 删除记录
	 * @param shoucardno
	 */
	private void deleteBankItem(String shoucardno) {
		TransferDeleteBankParser authorRegParser = new TransferDeleteBankParser();
		CommonData mData = new CommonData();
		mData.putValue("shoucardno", shoucardno);//收款卡号
		mData.putValue("paytype", "suptfmg,tfmg");//业务类型

		new AsyncLoadWork<String>(getActivity(), authorRegParser, mData, deleteListener, false, true)
		.execute("ApiPayinfo", "delshoucardList");
	}

	private AsyncLoadWorkListener deleteListener = new AsyncLoadWorkListener() {

		@Override
		public void onSuccess(Object protocolDataList, Bundle bundle) {
			onAsyncLoadData();
		}

		@Override
		public void onFailure(String error) {

		}

	};

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}
