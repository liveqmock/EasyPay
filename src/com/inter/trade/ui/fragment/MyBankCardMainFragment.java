package com.inter.trade.ui.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.inter.protocol.ProtocolData;
import com.inter.protocol.ProtocolRsp;
import com.inter.protocol.util.ProtocolUtil;
import com.inter.trade.R;
import com.inter.trade.adapter.AgentReplenishAdapter;
import com.inter.trade.adapter.MyBankListAdapter;
import com.inter.trade.data.BaseData;
import com.inter.trade.data.CommonData;
import com.inter.trade.data.MyBankListData;
import com.inter.trade.data.ResponseData;
import com.inter.trade.error.ErrorUtil;
import com.inter.trade.log.Logger;
import com.inter.trade.net.HttpUtil;
import com.inter.trade.parser.MyBankCardListParser;
import com.inter.trade.ui.DetialActivity;
import com.inter.trade.ui.MyListView;
import com.inter.trade.ui.AgentPayActivity;
import com.inter.trade.ui.QMoneyPayRecordActivity;
import com.inter.trade.ui.MyListView.OnPullDownListener;
import com.inter.trade.ui.MyListView.OnRefreshListener;
import com.inter.trade.ui.fragment.BaseFragment;
import com.inter.trade.ui.fragment.agent.AgentApplyFragmentNew;
import com.inter.trade.ui.fragment.agent.util.AgentData;
import com.inter.trade.ui.fragment.agent.util.AgentRecordData;
import com.inter.trade.ui.fragment.agent.util.AgentRecordParser;
import com.inter.trade.ui.fragment.qmoney.util.QMoneyRecordData;
import com.inter.trade.ui.fragment.transfer.RecordDetialFragment;
//import com.inter.trade.ui.fragment.qmoney.util.QMoneyData;
import com.inter.trade.util.BankCardUtil;
import com.inter.trade.util.Constants;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.NumberFormatUtil;
import com.inter.trade.util.PromptUtil;

/**
 * 我的银行卡Fragment
 * @author Lihaifeng
 *
 */
@SuppressLint("ValidFragment")
public class MyBankCardMainFragment extends BaseFragment implements OnClickListener,OnItemClickListener {
	private Button cridet_back_btn;
	private ArrayList<MyBankListData> mList = new ArrayList<MyBankListData>();
	private ListView mListView; 
	private RecordTask mRecordTask;
	
	private EditText agent_replenish_edit;
	private Button agent_replenish_pay_btn;
	
	private int mNumber =0;
	private float mPrice  =0;
	private float mTotalMoney =0;
	
	private AgentRecordData agentData = new AgentRecordData();
	private int mStartIndex=0;//起始索引
	private int mTotalCount=0;//订单总条数
	private int mLoadedCount=0;//当前加载的总条数
	private boolean isMore=false;
	private Bundle bundle;
	MyBankListAdapter mAdapter;
	public static final String TYPE_STRING="TYPE_STRING";
//	private String mType;
	public static MyBankCardMainFragment instance(String type){
		MyBankCardMainFragment fragment = new MyBankCardMainFragment();
		Bundle argsBundle = new Bundle();
		argsBundle.putString(TYPE_STRING, type);
		fragment.setArguments(argsBundle);
		return fragment;
		
	}
	
	public MyBankCardMainFragment() {

	}

	public MyBankCardMainFragment(Bundle b) {
		this.bundle = b;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitle("我的银行卡");
//		mAdapter = new MyBankListAdapter(getActivity(), mList);
		//test
//				MyBankListData mBankCardData =new MyBankListData();
//						mBankCardData.bkcardbank = "中国银行";
//						mBankCardData.bkcardno = "6222023602065051195";
//						mBankCardData.bkcardyxmonth = "08";
//						mBankCardData.bkcardyxyear =  "15";
//						mBankCardData.bkcardcvv = "123";
//						mBankCardData.bkcardbankphone = "13912345678";
//						mBankCardData.bkcardbankman = "牛大力";
//						mBankCardData.bkcardidcard = "440902198510205357";
//						mBankCardData.bkcardisdefault = "1";
//						mBankCardData.bkcardcardtype = "信用卡";
//						mList.add(mBankCardData);
//						
//						MyBankListData mBankCardData2 =new MyBankListData();
//						mBankCardData2.bkcardbank = "中国银行";
//						mBankCardData2.bkcardno = "6222023602065051196";
//						mBankCardData2.bkcardyxmonth = "09";
//						mBankCardData2.bkcardyxyear =  "15";
//						mBankCardData2.bkcardcvv = "456";
//						mBankCardData2.bkcardbankphone = "13912345678";
//						mBankCardData2.bkcardbankman = "杨卯";
//						mBankCardData2.bkcardidcard = "440902198510205358";
//						mBankCardData2.bkcardisdefault = "0";
//						mBankCardData2.bkcardcardtype = "储蓄卡";		
//						mList.add(mBankCardData2);
//						
//						mList.add(mBankCardData2);
//						mList.add(mBankCardData2);
//						mList.add(mBankCardData2);
						//test
		
//		mRecordTask = new RecordTask();
//		mRecordTask.execute("");
		
	}
	
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
//		Log.d("mybanklist", "arg2 int 点击第"+ arg2 +"行" );
		if(mList.size() == 0 || arg2<0){
			return;
		}
		
		MyBankListData mBankCardData = mList.get(arg2);
		
		String type = mBankCardData.bkcardcardtype;
		if(type != null){
			Bundle bundle= new Bundle();
			bundle.putSerializable("data", mBankCardData);
			if("信用卡".equalsIgnoreCase(type)){
				Button title_right_btn_three = (Button) getActivity().findViewById(R.id.title_right_btn_three);
				title_right_btn_three.setVisibility(View.GONE);
				getFragmentManager().beginTransaction().replace(R.id.func_container, MyBankCreditCardEditFragment.create(bundle)).addToBackStack(null).commit();
			}
			else if("储蓄卡".equalsIgnoreCase(type)){
				Button title_right_btn_three = (Button) getActivity().findViewById(R.id.title_right_btn_three);
				title_right_btn_three.setVisibility(View.GONE);
				getFragmentManager().beginTransaction().replace(R.id.func_container, MyBankCardEditFragment.create(bundle)).addToBackStack(null).commit();
			}
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//LoginUtil.detection(getActivity());
		View view = inflater.inflate(R.layout.mybank_main_layout, container, false);
		 mListView = (ListView) view.findViewById(R.id.mm_listview);
		 
		mListView.setOnItemClickListener(this);
//		mListView.setonRefreshListener(onRefreshListener);
//			
//		mListView.setOnPullDownListener(onPullDownListener);
//		mListView.setEnableAutoFetchMore(true);
		
//		mAdapter = new MyBankListAdapter(getActivity(), mList);
//		mListView.setAdapter(mAdapter);
		
		init(view);
		setBackVisible();
		setRightImageButtonVisible();
		
//		setRightVisible(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				getFragmentManager().beginTransaction().replace(R.id.func_container, MyBankAddFragment.create()).addToBackStack(null).commit();
//			}
//		}, "添加");
		
				
		mRecordTask = new RecordTask();
		mRecordTask.execute("");
		return view;
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setTitle("我的银行卡");
	}
	
	public void init(View view){
//		agent_replenish_edit = (EditText)view.findViewById(R.id.agent_replenish_edit);
//		agent_replenish_pay_btn = (Button)view.findViewById(R.id.agent_replenish_pay_btn);
//		agent_replenish_edit.setOnClickListener(this);
//		agent_replenish_pay_btn.setOnClickListener(this);

	}
	
	/**
	 * 设置返回事件
	 */
	protected void setRightImageButtonVisible() {
		if (getActivity() == null) {
			return;
		}
		Button title_right_btn_three = (Button) getActivity().findViewById(R.id.title_right_btn_three);

		if (title_right_btn_three == null) {
			return;
		}
		title_right_btn_three.setVisibility(View.VISIBLE);
		title_right_btn_three.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				 FragmentManager manager = getActivity().getSupportFragmentManager();
//				 int len = manager.getBackStackEntryCount();
//				 if(len>0){
//					 manager.popBackStack();
//				 }else{
//					 getActivity().finish();
//				 }
				Button title_right_btn_three = (Button) getActivity().findViewById(R.id.title_right_btn_three);
				title_right_btn_three.setVisibility(View.GONE);
				getFragmentManager().beginTransaction().replace(R.id.func_container, MyBankAddFragment.create()).addToBackStack(null).commit();
			}
		});
	}
	
	
//	OnPullDownListener onPullDownListener = new OnPullDownListener() {
//		
//		@Override
//		public void onRefresh() {
//			// TODO Auto-generated method stub
//		}
//		
//		@Override
//		public void onMore() {
//			// TODO Auto-generated method stub
//			isMore=true;
//			loadMore();
//		}
//	};
//	private void loadMore(){
//		if(mLoadedCount<mTotalCount){
//			mStartIndex = mLoadedCount;
//			
//			mRecordTask = new RecordTask();
//			mRecordTask.execute("");
//			
//		}else{
//			mListView.setLastText();
//		}
//	}
//	
//	OnRefreshListener onRefreshListener = new OnRefreshListener() {
//		
//		@Override
//		public void onRefresh() {
//			// TODO Auto-generated method stub
//			isMore=false;
//			mStartIndex=0;
//			
//			mRecordTask = new RecordTask();
//			mRecordTask.execute("");
//			
//		}
//	};
	
	public void setVisibleForRightButton(int vis){
		if (getActivity() == null) {
			return;
		}
		Button title_right_btn_three = (Button) getActivity().findViewById(R.id.title_right_btn_three);
	
		if (title_right_btn_three == null) {
			return;
		}
		title_right_btn_three.setVisibility(vis);
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		setVisibleForRightButton(View.GONE);
		if(mRecordTask != null){
			mRecordTask.cancel(true);
		}
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
//		case R.id.agent_replenish_edit:
//			agentReplenishEdit();
//			break;
//		case R.id.agent_replenish_pay_btn:
//			agentReplenishPay();
//			break;

		default:
			break;
		}
	}

	
	public class RecordTask extends AsyncTask<String, Integer, Boolean> {
		ProtocolRsp mRsp = null;
		FragmentActivity mActivity;
		private String mResultString;
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			PromptUtil.showDialog(getActivity(), getActivity().getResources().getString(R.string.loading));
		}

		@Override
		protected Boolean doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			try {
				CommonData data = new CommonData();
				
				data.putValue("msgstart",mStartIndex+"");
				data.putValue("msgdisplay", Constants.RECORD_DISPLAY_COUNT+"");
				List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas("ApiAuthorKuaibkcardInfo", "readKuaibkcardLists",
						data);
				MyBankCardListParser recordParser = new MyBankCardListParser();
				mRsp = HttpUtil.doRequest(recordParser, mDatas);
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
			try {
			if (mRsp == null) {
				PromptUtil.showToast(mActivity, getString(R.string.net_error));
			} else {
				
//				if(!isMore){
					mList.clear();
//				}
				
					List<ProtocolData> mDatas = mRsp.mActions;
					parserResponse(mDatas);
					
					mAdapter = new MyBankListAdapter(getActivity(), mList);
					mListView.setAdapter(mAdapter);
//					mAdapter.notifyDataSetChanged();
					
					if(!ErrorUtil.create().errorDeal(LoginUtil.mLoginStatus,getActivity())){
						return;
					}
					
//					mAdapter = new AgentReplenishAdapter(getActivity(), mList);
//					mListView.setAdapter(mAdapter);
//					mAdapter.notifyDataSetChanged();
					
//					if(isMore){
//						 mListView.setSelection(mLoadedCount);
//					 }
//					 isMore=false;
//					 mListView.setProgressGone();
//					 mListView.setIsFetchMoreing(false);

			}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
//			mListView.onRefreshComplete();
		}
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
					LoginUtil.mLoginStatus.result = result.get(0).mValue;
				}
				
				List<ProtocolData> message = data.find("/message");
				if (message != null) {
					LoginUtil.mLoginStatus.message = result.get(0).mValue;
				}
				
				List<ProtocolData> msgallcount = data.find("/msgallcount");
				if(msgallcount != null){
					mTotalCount = Integer.parseInt(msgallcount.get(0).mValue.trim());
				}
				List<ProtocolData> msgdiscount = data.find("/msgdiscount");
				if(msgdiscount != null){
					mLoadedCount  = Integer.parseInt(msgdiscount.get(0).mValue.trim());
				}
				
				List<ProtocolData> aupic = data.find("/msgchild");
				if(aupic==null){
					return;
				}
				for(ProtocolData child:aupic){
					MyBankListData orderData = new MyBankListData();
					if (child.mChildren != null && child.mChildren.size() > 0) {
						Set<String> keys = child.mChildren.keySet();
						for(String key:keys){
							List<ProtocolData> rs = child.mChildren.get(key);
							for(ProtocolData item: rs){
								if(item.mKey.equals("bkcardid")){
									orderData.bkcardid  = item.mValue;
									
								}else if(item.mKey.equals("bkcardno")){
									orderData.bkcardno  = item.mValue;
									
								}else if(item.mKey.equals("bkcardbankid")){
									orderData.bkcardbankid  = item.mValue;
									
								}else if(item.mKey.equals("bkcardbank")){
									orderData.bkcardbank  = item.mValue;
									
								}else if(item.mKey.equals("bkcardbanklogo")){
									orderData.bkcardbanklogo  = item.mValue;
									
								}else if(item.mKey.equals("banklogo")){
									orderData.banklogo  = item.mValue;
									
								}else if(item.mKey.equals("bkcardbankman")){
									orderData.bkcardbankman  = item.mValue;
									
								}else if(item.mKey.equals("bkcardbankphone")){
									orderData.bkcardbankphone  = item.mValue;
									
								}else if(item.mKey.equals("bkcardyxmonth")){
									orderData.bkcardyxmonth  = item.mValue;
									
								}else if(item.mKey.equals("bkcardyxyear")){
									orderData.bkcardyxyear  = item.mValue;
									
								}else if(item.mKey.equals("bkcardcvv")){
									orderData.bkcardcvv  = item.mValue;
									
								}else if(item.mKey.equals("bkcardidcard")){
									orderData.bkcardidcard  = item.mValue;
									
								}else if(item.mKey.equals("bkcardisdefault")){
									orderData.bkcardisdefault  = item.mValue;
									
								}else if(item.mKey.equals("bkcardfudefault")){
									orderData.bkcardfudefault  = item.mValue;
									
								}else if(item.mKey.equals("bkcardshoudefault")){
									orderData.bkcardshoudefault  = item.mValue;
									
								}else if(item.mKey.equals("bkcardcardtype")){
//									orderData.bkcardcardtype  = item.mValue;
									orderData.bkcardcardtype  = BankCardUtil.getCardType(item.mValue);
								}	
							}
						}
					}
					
					mList.add(orderData);
				}
			}
		}
	}


}
