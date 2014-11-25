package com.inter.trade.ui.fragment.coupon;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;

import com.inter.protocol.ProtocolData;
import com.inter.protocol.ProtocolRsp;
import com.inter.protocol.util.ProtocolUtil;
import com.inter.trade.R;
import com.inter.trade.data.CommonData;
import com.inter.trade.data.ResponseData;
import com.inter.trade.error.ErrorUtil;
import com.inter.trade.log.Logger;
import com.inter.trade.net.HttpUtil;
import com.inter.trade.ui.MyListView;
import com.inter.trade.ui.MyListView.OnPullDownListener;
import com.inter.trade.ui.MyListView.OnRefreshListener;
import com.inter.trade.ui.RecordDetialActivity;
import com.inter.trade.ui.fragment.BaseFragment;
import com.inter.trade.ui.fragment.coupon.parser.RecordParser;
import com.inter.trade.ui.fragment.coupon.util.BuyAdapter;
import com.inter.trade.ui.fragment.coupon.util.HistoryData;
import com.inter.trade.ui.fragment.cridet.CridetRecordDetialFragment;
import com.inter.trade.util.Constants;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PromptUtil;

public class BuyRecordFragment extends BaseFragment implements OnClickListener,
		OnItemClickListener {
	private Button cridet_back_btn;
	private ArrayList<HistoryData> mList = new ArrayList<HistoryData>();
	private MyListView mListView;
	private RecordTask mRecordTask;

	private int mStartIndex = 0;// 起始索引
	private int mTotalCount = 0;// 订单总条数
	private int mLoadedCount = 0;// 当前加载的总条数
	private boolean isMore = false;

	BuyAdapter mAdapter;

	public BuyRecordFragment() {

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		mRecordTask = new RecordTask();
		mRecordTask.execute("");

	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setTitle("历史记录");
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		if(mList.size() == (arg2-1)){
			return;
		}
		HistoryData data = mList.get(arg2 - 1);
		CridetRecordDetialFragment.mMaps.clear();
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		map.put("交易流水号", data.couponno);
		map.put("金额", data.couponmoney);
		map.put("付款银行", data.couponbank);
		map.put("付款卡号", data.couponcardno);
		map.put("购买时间", data.coupondate);
		map.put("其他描述", data.couponmemo);
		CridetRecordDetialFragment.mMaps.add(map);

		Intent intent = new Intent();
		intent.setClass(getActivity(), RecordDetialActivity.class);
		startActivity(intent);
	}

	OnPullDownListener onPullDownListener = new OnPullDownListener() {

		@Override
		public void onRefresh() {
			// TODO Auto-generated method stub

		}

		@Override
		public void onMore() {
			// TODO Auto-generated method stub
			isMore = true;
			loadMore();

		}
	};

	private void loadMore() {
		if (mLoadedCount < mTotalCount) {
			mStartIndex = mLoadedCount;
			mRecordTask = new RecordTask();
			mRecordTask.execute("");
		} else {
			mListView.setLastText();
//			PromptUtil.showToast(getActivity(), getString(R.string.last_message));
//			mListView.setProgressGone();
//			mListView.setIsFetchMoreing(false);
		}
	}

	OnRefreshListener onRefreshListener = new OnRefreshListener() {

		@Override
		public void onRefresh() {
			// TODO Auto-generated method stub
			isMore = false;
			mStartIndex = 0;
			mRecordTask = new RecordTask();
			mRecordTask.execute("");
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		LoginUtil.detection(getActivity());
		View view = inflater.inflate(R.layout.mm_list, container, false);
		mListView = (MyListView) view.findViewById(R.id.mm_listview);
		mListView.setOnItemClickListener(this);

		mListView.setonRefreshListener(onRefreshListener);

		mListView.setOnPullDownListener(onPullDownListener);
		mListView.setEnableAutoFetchMore(true);
		setBackVisible();
		return view;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (mRecordTask != null) {
			mRecordTask.cancel(true);
		}
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.cridet_back_btn:
			showChuxuka();
			break;

		default:
			break;
		}
	}

	private void showChuxuka() {
		FragmentTransaction transaction = getFragmentManager()
				.beginTransaction();
		transaction.replace(R.id.func_container, new BuySuccessFragment());
		transaction.commit();
	}

	public class RecordTask extends AsyncTask<String, Integer, Boolean> {
		ProtocolRsp mRsp = null;
		FragmentActivity mActivity;
		private String mResultString;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			PromptUtil.showDialog(getActivity(), getActivity().getResources()
					.getString(R.string.loading));
		}

		@Override
		protected Boolean doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			try {
				CommonData data = new CommonData();
				data.putValue("authorid", LoginUtil.mLoginStatus.authorid);
				data.putValue("msgstart", mStartIndex + "");
				data.putValue("msgdisplay", Constants.RECORD_DISPLAY_COUNT+"");
				List<ProtocolData> mDatas = createRequest(data);
				RecordParser versionParser = new RecordParser();
				mRsp = HttpUtil.doRequest(versionParser, mDatas);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				Logger.e(e);
			}

			return null;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			PromptUtil.dissmiss();
			if (mRsp == null) {
				PromptUtil.showToast(mActivity, mActivity.getResources().getString(R.string.net_error));
			} else {
				try {

					if (!isMore) {
						mList.clear();
					}
					List<ProtocolData> mDatas = mRsp.mActions;
					parserResponse(mDatas);

					if (!ErrorUtil.create().errorDeal(LoginUtil.mLoginStatus,
							getActivity())) {
						return;
					}

					mAdapter = new BuyAdapter(getActivity(), mList);
					mListView.setAdapter(mAdapter);
					mAdapter.notifyDataSetChanged();

					if (isMore) {
						mListView.setSelection(mLoadedCount);
					}
					isMore = false;
					mListView.setProgressGone();
					mListView.setIsFetchMoreing(false);

				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				mListView.onRefreshComplete();
			}
		}
	}

	private List<ProtocolData> createRequest(CommonData data) {
		return ProtocolUtil.getRequestDatas("ApiCouponInfo", "couponSalelist",
				data);
	}

	private void parserResponse(List<ProtocolData> mDatas) {
		ResponseData response = new ResponseData();
		LoginUtil.mLoginStatus.mResponseData = response;
		for (ProtocolData data : mDatas) {
			if (data.mKey.equals(ProtocolUtil.msgheader)) {
				ProtocolUtil.parserResponse(response, data);

			} else if (data.mKey.equals(ProtocolUtil.msgbody)) {

				List<ProtocolData> result = data.find("/result");
				if (result != null) {
					LoginUtil.mLoginStatus.result = result.get(0).getmValue();
				}

				List<ProtocolData> message = data.find("/message");
				if (message != null) {

					LoginUtil.mLoginStatus.message = message.get(0).getmValue();

				}

				List<ProtocolData> msgallcount = data.find("/msgallcount");
				if (msgallcount != null) {
					mTotalCount = Integer.parseInt(msgallcount.get(0).mValue
							.trim());
				}
				List<ProtocolData> msgdiscount = data.find("/msgdiscount");
				if (msgdiscount != null) {
					mLoadedCount = Integer.parseInt(msgdiscount.get(0).mValue
							.trim());
				}

				List<ProtocolData> aupic = data.find("/msgchild");
				if (aupic == null) {
					return;
				}
				for (ProtocolData child : aupic) {
					HistoryData picData = new HistoryData();
					if (child.mChildren != null && child.mChildren.size() > 0) {
						Set<String> keys = child.mChildren.keySet();
						for (String key : keys) {
							List<ProtocolData> rs = child.mChildren.get(key);
							for (ProtocolData item : rs) {
								if (item.mKey.equals("couponno")) {
									picData.couponno = item.mValue;

								} else if (item.mKey.equals("couponmoney")) {
									picData.couponmoney = item.mValue;

								} else if (item.mKey.equals("coupondate")) {

									picData.coupondate = item.mValue;

								} else if (item.mKey.equals("paycardid")) {

									picData.paycardid = item.mValue;

								} else if (item.mKey.equals("couponid")) {

									picData.couponid = item.mValue;

								} else if (item.mKey.equals("couponmemo")) {

									picData.couponmemo = item.mValue;
								}else if (item.mKey.equals("couponcardno")) {

									picData.couponcardno = item.mValue;

								} else if (item.mKey.equals("couponbank")) {

									picData.couponbank = item.mValue;
								}
							}
						}
					}

					mList.add(picData);
				}
			}
		}
	}

}
