package com.inter.trade.ui.fragment.agent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import android.annotation.SuppressLint;
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
import android.widget.EditText;
import android.widget.ListView;

import com.inter.protocol.ProtocolData;
import com.inter.protocol.ProtocolRsp;
import com.inter.protocol.util.ProtocolUtil;
import com.inter.trade.R;
import com.inter.trade.adapter.AgentReplenishAdapter;
import com.inter.trade.data.BaseData;
import com.inter.trade.data.CommonData;
import com.inter.trade.data.ResponseData;
import com.inter.trade.error.ErrorUtil;
import com.inter.trade.log.Logger;
import com.inter.trade.net.HttpUtil;
import com.inter.trade.ui.DetialActivity;
import com.inter.trade.ui.MyListView;
import com.inter.trade.ui.AgentPayActivity;
import com.inter.trade.ui.MyListView.OnPullDownListener;
import com.inter.trade.ui.MyListView.OnRefreshListener;
import com.inter.trade.ui.fragment.BaseFragment;
import com.inter.trade.ui.fragment.agent.util.AgentData;
import com.inter.trade.ui.fragment.agent.util.AgentRecordData;
import com.inter.trade.ui.fragment.agent.util.AgentRecordParser;
//import com.inter.trade.ui.fragment.qmoney.util.QMoneyData;
import com.inter.trade.util.Constants;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.NumberFormatUtil;
import com.inter.trade.util.PromptUtil;

/**
 * 代理商补货Fragment
 * @author Lihaifeng
 *
 */
@SuppressLint("ValidFragment")
public class AgentReplenishFragment extends BaseFragment implements OnClickListener,OnItemClickListener {
	private Button cridet_back_btn;
	private ArrayList<AgentOrderData> mList = new ArrayList<AgentOrderData>();
	private MyListView mListView; 
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
	AgentReplenishAdapter mAdapter;
	public static final String TYPE_STRING="TYPE_STRING";
//	private String mType;
	public static AgentReplenishFragment instance(String type){
		AgentReplenishFragment fragment = new AgentReplenishFragment();
		Bundle argsBundle = new Bundle();
		argsBundle.putString(TYPE_STRING, type);
		fragment.setArguments(argsBundle);
		return fragment;
		
	}
	
	public AgentReplenishFragment() {

	}

	public AgentReplenishFragment(Bundle b) {
		this.bundle = b;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitle("刷卡器补货");
		
		mRecordTask = new RecordTask();
		mRecordTask.execute("");
		
	}
	
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
//		PromptUtil.showToast(getActivity(), "position="+arg2 + ", rowId="+arg3);
//		log("agent list: position="+arg2 + ", rowId="+arg3);
		
//		if(mList.size() == (arg2-1)){
//			return;
//		}
//		AgentRecordData data = mList.get(arg2-1);
//		RecordDetialFragment.mMaps.clear();
//		
//		HashMap<String, String> shoukuanbank = new HashMap<String, String>();
//		shoukuanbank.put("使用金额",
//				data.money+"元"
//				);
//		RecordDetialFragment.mMaps.add(shoukuanbank);
//		
//		
//		HashMap<String, String> item4 = new HashMap<String, String>();
//		item4.put("订单日期", data.date);
//		RecordDetialFragment.mMaps.add(item4);
//		
//		
//		Intent intent = new Intent();
//		intent.putExtra("type", "record");
//		intent.setClass(getActivity(), DetialActivity.class);
//		startActivity(intent);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//LoginUtil.detection(getActivity());
		View view = inflater.inflate(R.layout.agent_replenish_layout, container, false);
		 mListView = (MyListView) view.findViewById(R.id.mm_listview);
		 
		mListView.setOnItemClickListener(this);
		mListView.setonRefreshListener(onRefreshListener);
			
		mListView.setOnPullDownListener(onPullDownListener);
		mListView.setEnableAutoFetchMore(true);
		setBackVisible();
		init(view);
		return view;
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setTitle("刷卡器补货");
	}
	
	public void init(View view){
		agent_replenish_edit = (EditText)view.findViewById(R.id.agent_replenish_edit);
		agent_replenish_pay_btn = (Button)view.findViewById(R.id.agent_replenish_pay_btn);
		agent_replenish_edit.setOnClickListener(this);
		agent_replenish_pay_btn.setOnClickListener(this);

//暂时测试用，必须去掉		
//		for(int n=1; n<32; n++){
//			AgentRecordData recordData = new AgentRecordData();
//			if(n%2!=0){
//				recordData.sex  = n+"/5";
//				recordData.name  = "自订刷卡器20台";
//				recordData.date  = "未发货";
//				recordData.money  = "收货";
//				recordData.sendState  = "0";
//				recordData.getState  = "0";
//			}else{	
//				recordData.sex  = n+"/5";
//				recordData.name  = "系统订刷卡器20台";
//				recordData.date  = "已发";
//				recordData.money  = "收货";
//				recordData.sendState  = "1";
//				recordData.getState  = "0";
//			}
//			mList.add(recordData);
//		}
//		mAdapter = new AgentReplenishAdapter(getActivity(), mList);
//		mListView.setAdapter(mAdapter);
//		mAdapter.notifyDataSetChanged();
//		
//		if(isMore){
//			 mListView.setSelection(mLoadedCount);
//		 }
//		 isMore=false;
//		 mListView.setProgressGone();
//		 mListView.setIsFetchMoreing(false);
//		 mListView.onRefreshComplete();
//暂时测试用，必须去掉
		 
	}
	
	OnPullDownListener onPullDownListener = new OnPullDownListener() {
		
		@Override
		public void onRefresh() {
			// TODO Auto-generated method stub
		}
		
		@Override
		public void onMore() {
			// TODO Auto-generated method stub
			isMore=true;
			loadMore();
		}
	};
	private void loadMore(){
		if(mLoadedCount<mTotalCount){
			mStartIndex = mLoadedCount;
			
			mRecordTask = new RecordTask();
			mRecordTask.execute("");
			
		}else{
			mListView.setLastText();
		}
	}
	
	OnRefreshListener onRefreshListener = new OnRefreshListener() {
		
		@Override
		public void onRefresh() {
			// TODO Auto-generated method stub
			isMore=false;
			mStartIndex=0;
			
			mRecordTask = new RecordTask();
			mRecordTask.execute("");
			
		}
	};
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(mRecordTask != null){
			mRecordTask.cancel(true);
		}
	}

	long time=0L;
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		//1秒内，禁止双击两次提交
		long currentTime=System.currentTimeMillis();
		if(currentTime-time<1000){
			return;
		}
		time=currentTime;
		
		switch (arg0.getId()) {
//		case R.id.agent_replenish_edit:
//			agentReplenishEdit();
//			break;
		case R.id.agent_replenish_pay_btn:
			agentReplenishPay();
			break;

		default:
			break;
		}
	}

	//检查接收输入的补货台数
	private void agentReplenishEdit() {
		//检查接收输入的补货台数
	}
	
	//补货用通付宝支付
	private void agentReplenishPay() {
		//先检查输入框，判断再执行以下 
		String number = "";
		number = number + agent_replenish_edit.getText().toString();
		if("".equals(number)){
			PromptUtil.showToast(getActivity(), "请输入补货台数");
			return;
		}
		mNumber = Integer.parseInt(number);
		if(mNumber <=0){
			PromptUtil.showToast(getActivity(), "补货台数输入不正确");
			return;
		}
		
		if(mNumber > agentData.limitmaxnum && agentData.limitmaxnum>0){
			PromptUtil.showToast(getActivity(), "补货台数最大值为"+agentData.limitmaxnum);
			return;
		}
		if(mNumber < agentData.limitminnum && agentData.limitminnum>0){
			PromptUtil.showToast(getActivity(), "补货台数最小值为"+agentData.limitminnum);
			return;
		}
		
		mPrice = agentData.nowprice;
		
//		mPrice=480;//测试，必须删除
		mTotalMoney=(float)mNumber*mPrice;
		
		if(mTotalMoney <= 0){
			PromptUtil.showToast(getActivity(), "支付总金额异常");
			return;
		}
		
		Intent intent = new Intent();
		intent.setClass(getActivity(), AgentPayActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("orderprodureid", agentData.produreid);
		bundle.putString("produrename", agentData.produrename);
		bundle.putString("ordernum", mNumber+"");
		bundle.putString("orderprice", mPrice+"");
		bundle.putString("ordermoney", NumberFormatUtil.format2(mTotalMoney+""));
		intent.putExtra("AgentPay", bundle);
		startActivityForResult(intent, 3);
	}

	public class AgentOrderData extends BaseData{
		/**
		 * 订单id
		 */
		public  String orderid;
		
		/**
		 * 订单日期
		 */
		public  String orderdate;
		
		/**
		 * 订单描述
		 */
		public  String ordermemo;
		
		/**
		 * 订单状态
		 */
		public  String orderstate;
		
		/**
		 * 发货状态，0未发，1已发
		 */
		public  String sendState;
		
		/**
		 * 收货状态，0未收，1已收
		 */
		public  String getState;
		
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
				List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas("ApiAgentInfo", "readagentorder",
						data);
				AgentRecordParser recordParser = new AgentRecordParser();
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
				
				if(!isMore){
					mList.clear();
				}
				
					List<ProtocolData> mDatas = mRsp.mActions;
					parserResponse(mDatas);
					
					//以下测试
//					agentData.produreid = "10";
//					agentData.nowprice = 480;
//					agentData.produrename = "通付宝刷卡器";
//					agentData.limitmaxnum = 100;
//					agentData.limitminnum = 20;
					//以上测试
					
					if(!ErrorUtil.create().errorDeal(LoginUtil.mLoginStatus,getActivity())){
						return;
					}
					
					mAdapter = new AgentReplenishAdapter(getActivity(), mList);
					mListView.setAdapter(mAdapter);
					mAdapter.notifyDataSetChanged();
					
					if(isMore){
						 mListView.setSelection(mLoadedCount);
					 }
					 isMore=false;
					 mListView.setProgressGone();
					 mListView.setIsFetchMoreing(false);

			}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			mListView.onRefreshComplete();
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
				
				List<ProtocolData> limitmaxnum = data.find("/limitmaxnum");
				if(limitmaxnum != null){
					agentData.limitmaxnum  = Integer.parseInt(limitmaxnum.get(0).mValue.trim());
				}
				
				List<ProtocolData> nowprice = data.find("/nowprice");
				if(nowprice != null){
//					agentData.nowprice  = Integer.parseInt(nowprice.get(0).mValue.trim());
					agentData.nowprice  = Float.parseFloat(nowprice.get(0).mValue.trim());
				}
				
				List<ProtocolData> limitminnum = data.find("/limitminnum");
				if(limitminnum != null){
					agentData.limitminnum  = Integer.parseInt(limitminnum.get(0).mValue.trim());
				}
				
				List<ProtocolData> produreid = data.find("/produreid");
				if(produreid != null){
					agentData.produreid  =produreid.get(0).mValue.trim();
				}
				
				List<ProtocolData> produrename = data.find("/produrename");
				if(produrename != null){
					agentData.produrename = produrename.get(0).mValue.trim();
				}
				
				int test =0;//测试
				
				List<ProtocolData> aupic = data.find("/msgchild");
				if(aupic==null){
					return;
				}
				for(ProtocolData child:aupic){
					AgentOrderData orderData = new AgentOrderData();
					if (child.mChildren != null && child.mChildren.size() > 0) {
						Set<String> keys = child.mChildren.keySet();
						for(String key:keys){
							List<ProtocolData> rs = child.mChildren.get(key);
							for(ProtocolData item: rs){
								if(item.mKey.equals("orderid")){
									orderData.orderid  = item.mValue;
									
								}else if(item.mKey.equals("orderdate")){
									orderData.orderdate  = item.mValue;
									
								}else if(item.mKey.equals("ordermemo")){
									orderData.ordermemo  = item.mValue;
									
								}else if(item.mKey.equals("orderstate")){
									orderData.orderstate  = item.mValue;
									
//									if(test++%2==0){
//										orderData.orderstate  = "1";//测试
//									}else{
//										orderData.orderstate  = "2";//测试
//									}
									
								}	
							}
						}
					}
					
					mList.add(orderData);
				}
			}
		}
	}

	public class OrderStateTask extends AsyncTask<String, Integer, Boolean> {
		ProtocolRsp mRsp = null;
		FragmentActivity mActivity;
		private String mResultString;
		private String mOrderid;
		
		public OrderStateTask(String orderid) {
			// TODO Auto-generated constructor stub
			mOrderid=orderid;
		}

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
				
				data.putValue("orderid",mOrderid+"");
				List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas("ApiAgentInfo", "agentorderstaterq",
						data);
				AgentRecordParser recordParser = new AgentRecordParser();
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
				
					mList.clear();
				
					List<ProtocolData> mDatas = mRsp.mActions;
					parserResponseOrder(mDatas);
					

			}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}

	private void parserResponseOrder(List<ProtocolData> mDatas) {
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
				
			}
		}
	}
	
}
