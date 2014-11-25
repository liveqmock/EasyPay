package com.inter.trade.ui.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.inter.protocol.ProtocolData;
import com.inter.protocol.ProtocolRsp;
import com.inter.protocol.body.ReadBankListParser;
import com.inter.protocol.util.ProtocolUtil;
import com.inter.trade.R;
import com.inter.trade.adapter.BankListAdapter;
import com.inter.trade.adapter.CridetAdapter;
import com.inter.trade.data.BankData;
import com.inter.trade.data.CommonData;
import com.inter.trade.data.ResponseData;
import com.inter.trade.error.ErrorUtil;
import com.inter.trade.net.HttpUtil;
import com.inter.trade.ui.BankListActivity;
import com.inter.trade.ui.MyListView;
import com.inter.trade.ui.PayApp;
import com.inter.trade.ui.MyListView.OnPullDownListener;
import com.inter.trade.ui.MyListView.OnRefreshListener;
import com.inter.trade.ui.fragment.cridet.CridetRecordFragment.RecordTask;
import com.inter.trade.util.Constants;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PromptUtil;

public class BankListFragment extends BaseFragment implements
		OnItemClickListener {
	private ArrayList<BankData> mBankDatas = new ArrayList<BankData>();
	private int mStartIndex = 0;// 起始索引
	private int mTotalCount = 0;// 订单总条数
	private int mLoadedCount = 0;// 当前加载的总条数
	private boolean isMore = false;

	private MyListView mListView;
	private BankListAdapter mAdapter;
	private BankTask mBankTask;
	private String mType = "0";// 0支持所有银行，1支持手机支付银行
	private Button bank_query_btn;
	private EditText bank_query_edit;
	private static String ACTIVEMOBILESMS = "ACTIVEMOBILESMS";

	private String queryString = "";
	private View tipsview;
	
	/***
	 * 当前显示的页面的第一条item的id
	 */
	private int mShowIndex=0;
	
	public static final String READMODE_TAG = "readmode";
	/**
	 * 作为收款方还是付款方来读取的
	 * 付款方：f（屏蔽付款方禁用的银行）；
	 * 收款方：s（屏蔽收款方禁用的银行）;
	 * 默认为空：读取所有银行
	 */
	public String readmode = null;

	public static BankListFragment create(String type, String readmode) {
		BankListFragment fragment = new BankListFragment();
		Bundle bundle = new Bundle();
		bundle.putString(ACTIVEMOBILESMS, type);
		bundle.putString(READMODE_TAG, readmode);
		fragment.setArguments(bundle);
		return fragment;
	}

	public BankListFragment() {

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//屏蔽输入框一进入页面弹出输入键盘
		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		mType = getArguments() == null ? null : getArguments().getString(
				ACTIVEMOBILESMS);
		
		readmode = getArguments() == null ? null : getArguments().getString(
				READMODE_TAG);

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
		//获取当前的第一条的显示位置
		mShowIndex=mListView.getFirstVisiblePosition();
		if (mLoadedCount < mTotalCount) {
			mStartIndex = mLoadedCount;
			//mStartIndex = 0;
			mBankTask = new BankTask();
			mBankTask.execute("");
		} else {
			mListView.setLastText();
			// PromptUtil.showToast(getActivity(),
			// getString(R.string.last_message));
			// mListView.setProgressGone();
			// mListView.setIsFetchMoreing(false);
		}
	}

	OnRefreshListener onRefreshListener = new OnRefreshListener() {

		@Override
		public void onRefresh() {
			// TODO Auto-generated method stub
			isMore = false;
			mStartIndex = 0;
			mBankTask = new BankTask();
			mBankTask.execute("");
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		LoginUtil.detection(getActivity());
		View view = inflater.inflate(R.layout.bank_list, container, false);
		mListView = (MyListView) view.findViewById(R.id.bank_listview);
		mListView.setOnItemClickListener(this);
		mListView.setonRefreshListener(onRefreshListener);
		tipsview = (View)view.findViewById(R.id.tipsview);

		mListView.setOnPullDownListener(onPullDownListener);
		mListView.setEnableAutoFetchMore(true);
		bank_query_btn = (Button) view.findViewById(R.id.bank_query_btn);
		bank_query_btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				doQuery();
			}
		});
		bank_query_edit = (EditText) view.findViewById(R.id.bank_query_edit);

		setBackVisible();
		setTitle("银行列表");

		mBankTask = new BankTask();
		mBankTask.execute("");

		return view;
	}

	private void doQuery() {
		String tempqueryString = bank_query_edit.getText().toString();
		if (null == tempqueryString || "".equals(tempqueryString)) {
			// PromptUtil.showToast(getActivity(), "请输入查询条件");
			// return ;
			tempqueryString = "";
		}
		this.queryString = tempqueryString;
		mBankTask = new BankTask();
		mBankTask.execute("");
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		if (mBankDatas.size() == (arg2 - 1)) {
			return;
		}
		BankData data = mBankDatas.get(arg2 - 1);
		if (getActivity() instanceof BankListActivity) {
			((BankListActivity) getActivity()).bankname = data.bankname;
			((BankListActivity) getActivity()).bankid = data.bankid;
			((BankListActivity) getActivity()).bankNo = data.bankno;
		}
		getActivity().finish();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (mBankTask != null) {
			mBankTask.cancel(true);
		}
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setTitle("银行列表");
	}

	class BankTask extends AsyncTask<String, Integer, Boolean> {
		private ProtocolRsp mRsp;

		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			// UPLOAD_URL =
			// LoginUtil.mLoginStatus.mUserInfo.mPicData.get(0).uploadurl;
			try {

				List<ProtocolData> mDatas = getRequestDatas(queryString);
				ReadBankListParser authorRegParser = new ReadBankListParser();

				mRsp = HttpUtil.doRequest(authorRegParser, mDatas);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			PromptUtil.dissmiss();
			
			try {
				if (mRsp != null) {
					List<ProtocolData> datas = mRsp.mActions;
					parserResoponse(datas);
					
					if (!ErrorUtil.create().errorDeal(LoginUtil.mLoginStatus,
							getActivity())) {
						PromptUtil.showToast(PayApp.pay, LoginUtil.mLoginStatus.mResponseData.getRetmsg());
						mListView.setAdapter(null);
						mListView.onRefreshComplete();
						return;
					}

					if (mAdapter == null) {
						mAdapter = new BankListAdapter(getActivity(),
								mBankDatas);
					}

					mListView.setAdapter(mAdapter);
					mAdapter.notifyDataSetChanged();

					if (isMore) {//上拉是显示item位置
						mListView.setSelection(mShowIndex+1);
						//mListView.setSelection(mShowIndex);
					}
					isMore = false;
					mListView.setProgressGone();
					mListView.setIsFetchMoreing(false);
				} else {
					PromptUtil.showToast(getActivity(), "服务器繁忙,请稍后再试");
				}
			} catch (Exception e) {
				// TODO: handle exception
				PromptUtil.showToast(PayApp.pay, getString(R.string.net_error));
			}
			mListView.onRefreshComplete();
			if(mBankDatas!=null && mBankDatas.size()>0){
				tipsview.setVisibility(View.VISIBLE);
			}else{
				tipsview.setVisibility(View.GONE);
			}
			// if(mBankDatas.size()<9){
			// mListView.setFooterViewVisiblitiy(View.GONE);
			// }else{
			// mListView.setFooterViewVisiblitiy(View.VISIBLE);
			// }
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			PromptUtil.showDialog(getActivity(), getActivity().getResources()
					.getString(R.string.loading));
		}

	}

	/**
	 * 解析响应体
	 * 
	 * @param params
	 */
	private void parserResoponse(List<ProtocolData> params) {
		ResponseData response = new ResponseData();
		LoginUtil.mLoginStatus.mResponseData = response;
		
		for (ProtocolData data : params) {
			if (data.mKey.equals(ProtocolUtil.msgheader)) {
				ProtocolUtil.parserResponse(response, data);

			} else if (data.mKey.equals(ProtocolUtil.msgbody)) {
				List<ProtocolData> result1 = data.find("/result");
				if (result1 != null) {
					LoginUtil.mLoginStatus.result = result1.get(0).mValue;
				}

				List<ProtocolData> message = data.find("/message");
				if (message != null) {
					LoginUtil.mLoginStatus.message = message.get(0).mValue;
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
				if (aupic != null) {
					if(!isMore){
						mBankDatas.clear();
					}
					for (ProtocolData child : aupic) {
						BankData picData = new BankData();
						if (child.mChildren != null
								&& child.mChildren.size() > 0) {
							Set<String> keys = child.mChildren.keySet();
							for (String key : keys) {
								List<ProtocolData> rs = child.mChildren
										.get(key);
								for (ProtocolData item : rs) {
									if (item.mKey.equals("bankid")) {
										picData.bankid = item.mValue;

									} else if (item.mKey.equals("bankno")) {
										picData.bankno = item.mValue;

									} else if (item.mKey.equals("bankname")) {

										picData.bankname = item.mValue;
									}
								}
							}
						}

						mBankDatas.add(picData);
					}

				}
			}

		}
	}

	/**
	 * 请求修改身份信息
	 * 
	 * @return
	 */
	private List<ProtocolData> getRequestDatas(String where) {
		CommonData data = new CommonData();
		data.putValue("authorid", LoginUtil.mLoginStatus.authorid);
		data.putValue("activemobilesms", mType);
		data.putValue("msgstart", mStartIndex + "");
		data.putValue("msgdisplay", Constants.RECORD_DISPLAY_COUNT + "");
		data.putValue("querywhere", where);
		/**
		 * 作为收款方还是付款方来读取的
		 * 付款方：f（屏蔽付款方禁用的银行）；
		 * 收款方：s（屏蔽收款方禁用的银行）;
		 * 默认为空：读取所有银行
		 */
		data.putValue("readmode", readmode == null || readmode.equals("") ? "" : readmode);
		List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas("ApiAppInfo",
				"readBankList", data);

		return mDatas;
	}
}
