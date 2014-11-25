package com.inter.trade.ui.fragment.agent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.inter.protocol.ProtocolData;
import com.inter.protocol.ProtocolRsp;
import com.inter.protocol.util.ProtocolUtil;
import com.inter.trade.R;
import com.inter.trade.adapter.AgentQueryAdapter;
import com.inter.trade.data.BaseData;
import com.inter.trade.data.CommonData;
import com.inter.trade.data.ResponseData;
import com.inter.trade.error.ErrorUtil;
import com.inter.trade.log.Logger;
import com.inter.trade.net.HttpUtil;
import com.inter.trade.ui.AgentMainContentActivity;
import com.inter.trade.ui.AgentPayActivity;
import com.inter.trade.ui.AgentQueryWheelDateActivity;
import com.inter.trade.ui.AgentReplenishActivity;
import com.inter.trade.ui.DetialActivity;
import com.inter.trade.ui.MyListView;
import com.inter.trade.ui.MyListView.OnPullDownListener;
import com.inter.trade.ui.MyListView.OnRefreshListener;
import com.inter.trade.ui.fragment.BaseFragment;
import com.inter.trade.ui.fragment.agent.util.AgentQueryData;
import com.inter.trade.ui.fragment.agent.util.AgentQueryDetailData;
import com.inter.trade.ui.fragment.agent.util.AgentQueryDetailParser;
import com.inter.trade.ui.fragment.agent.util.AgentQueryParser;
import com.inter.trade.ui.fragment.agent.util.AgentRecordData;
import com.inter.trade.ui.fragment.agent.util.AgentRecordParser;
import com.inter.trade.ui.fragment.agent.util.MyExplandableListView;
import com.inter.trade.ui.fragment.qmoney.util.QMoneyData;
import com.inter.trade.util.Constants;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.NumberFormatUtil;
import com.inter.trade.util.PromptUtil;

/**
 * 代理商查询历史收益Fragment
 * @author Lihaifeng
 *
 */
@SuppressLint("ValidFragment")
public class AgentQueryFragment extends BaseFragment implements OnClickListener,OnItemClickListener {
	private ArrayList<AgentQueryData> mList = new ArrayList<AgentQueryData>();
	private ArrayList<AgentQueryDetailData> mChild = new ArrayList<AgentQueryDetailData>();
	private ArrayList<ArrayList<AgentQueryDetailData>> mChildList = new ArrayList();
	
	private ArrayList<AgentQueryData> mListDate = new ArrayList<AgentQueryData>();
	private ArrayList<AgentQueryData> mListMonth = new ArrayList<AgentQueryData>();
	private ArrayList<AgentQueryData> mListYear = new ArrayList<AgentQueryData>();
	private ArrayList<ArrayList<AgentQueryDetailData>> mChildListDate = new ArrayList();
	private ArrayList<ArrayList<AgentQueryDetailData>> mChildListMonth = new ArrayList();
	private ArrayList<ArrayList<AgentQueryDetailData>> mChildListYear = new ArrayList();
//	private MyListView mListView; 
	private RecordTask mRecordTask;
	private FenrunDedailTask mFenrunDedailTask;
	
	private MyExplandableListView expandable_listview;
	private RelativeLayout query_date_layout;
	private LinearLayout agent_main_mytfb, agent_main_replenish;
	private LinearLayout agent_query_day_layout, agent_query_month_layout, agent_query_year_layout;
	private ImageView agent_query_day_select, agent_query_month_select, agent_query_year_select;
	private TextView agent_main_mytfb_tv, agent_main_replenish_tv;
	private TextView agent_query_date;
	private TextView query_select;
	private TextView totalmoney;
	
	private int mListSize;
	private int mCurrentItem=0;
	private int mYear;
	private int mMonth;
	private int mDay;
	private String mDateType = "day";
	private String mQuerytype = "Date";
	private String mDate ="";
	private String mAppFunId ="";
	private String mTotalfenrun="";
	private String[] mTotalMoneyArray ={"0","0","0"};
	private boolean mIsNewQuery=true;
	private boolean mIsDialogdismiss=false;
	private BaseData mQuery = new BaseData();
	
	private int mStartIndex=0;//起始索引
	private int mTotalCount=0;//订单总条数
	private int mLoadedCount=0;//当前加载的总条数
	private boolean isMore=false;
	private Bundle bundle;
	AgentQueryAdapter mAdapter;
	public static final String TYPE_STRING="TYPE_STRING";
//	private String mType;
	public static AgentQueryFragment instance(String type){
		AgentQueryFragment fragment = new AgentQueryFragment();
		Bundle argsBundle = new Bundle();
		argsBundle.putString(TYPE_STRING, type);
		fragment.setArguments(argsBundle);
		return fragment;
		
	}
	
	public AgentQueryFragment() {

	}

	public AgentQueryFragment(Bundle b) {
		this.bundle = b;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitle("查询历史收益");
		
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
		String[] date = df.format(new Date()).split("-");
		if(date.length==3){
			mYear  = Integer.parseInt(date[0]);
			mMonth = Integer.parseInt(date[1]);
			mDay   = Integer.parseInt(date[2]);
		}
//		mQuery.putValue(mDateType, "new");
		checkToQuery();
	}
	
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
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
		View view = inflater.inflate(R.layout.agent_client_list_layout, container, false);
//		mListView = (MyListView) view.findViewById(R.id.mm_listview);
//		 
//		mListView.setOnItemClickListener(this);
//		mListView.setonRefreshListener(onRefreshListener);
//			
//		mListView.setOnPullDownListener(onPullDownListener);
//		mListView.setEnableAutoFetchMore(true);
		setBackVisible();
		initView(view);
		return view;
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setTitle("查询历史收益");
	}
	
	public void initView(View view){
		agent_query_day_layout = (LinearLayout)view.findViewById(R.id.agent_query_day_layout);
		agent_query_month_layout = (LinearLayout)view.findViewById(R.id.agent_query_month_layout);
		agent_query_year_layout = (LinearLayout)view.findViewById(R.id.agent_query_year_layout);
		
		agent_query_day_select = (ImageView)view.findViewById(R.id.agent_query_day_select);
		agent_query_month_select = (ImageView)view.findViewById(R.id.agent_query_month_select);
		agent_query_year_select = (ImageView)view.findViewById(R.id.agent_query_year_select);
		
		totalmoney = (TextView)view.findViewById(R.id.totalmoney);
		
		query_select = (TextView)view.findViewById(R.id.query_select);
		query_select.setText("请选择查询时间");
		
		agent_query_date = (TextView)view.findViewById(R.id.agent_query_date);
//		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
//		agent_query_date.setText(df.format(new Date()));
		
		showDate();
		
		query_date_layout = (RelativeLayout)view.findViewById(R.id.query_date_layout);
		query_date_layout.setOnClickListener(this);
		
		agent_query_day_layout.setOnClickListener(this);
		agent_query_month_layout.setOnClickListener(this);
		agent_query_year_layout.setOnClickListener(this);
		agent_query_day_layout.setSelected(true);
		agent_query_month_layout.setSelected(false);
		agent_query_year_layout.setSelected(false);
		
		agent_query_day_select.setVisibility(View.VISIBLE);
		agent_query_month_select.setVisibility(View.INVISIBLE);
		agent_query_year_select.setVisibility(View.INVISIBLE);
		
		expandable_listview = (MyExplandableListView)view.findViewById(R.id.expandable_listview);
		mAdapter = new AgentQueryAdapter(getActivity(), mList, mChildList);
		expandable_listview.setAdapter(mAdapter);
//		mAdapter.notifyDataSetChanged();
		
		//暂时测试用，必须去掉	
//		AgentQueryData tempData3 = new AgentQueryData();
//		tempData3.appfunid="2";
//		tempData3.appfunname="销售收益";
//		tempData3.allfenrun="800.00";
//		mList.add(tempData3);
//		
//		AgentQueryData tempData4 = new AgentQueryData();
//		tempData4.appfunid="2";
//		tempData4.appfunname="娱乐类收益";
//		tempData4.allfenrun="700.00";
//		mList.add(tempData4);
//		
//		ArrayList<AgentQueryData> tempList_null = new ArrayList<AgentQueryData>();
//		mChildList.add(tempList_null);
//		
//		ArrayList<AgentQueryData> tempList = new ArrayList<AgentQueryData>();
//		AgentQueryData tempData = new AgentQueryData();
//		tempData.appfunid="1";
//		tempData.appfunname="手机充值";
//		tempData.allfenrun="210.00";
//		tempList.add(tempData);
//		
//		AgentQueryData tempData2 = new AgentQueryData();
//		tempData2.appfunid="2";
//		tempData2.appfunname="游戏充值";
//		tempData2.allfenrun="490.00";
//		tempList.add(tempData2);
//		mChildList.add(tempList);
//		
//		mTotalfenrun = "1500";
//		
//		showData();
		//暂时测试用，必须去掉
		
//暂时测试用，必须去掉
//		for(int n=0; n<25; n++){
//			AgentRecordData recordData = new AgentRecordData();
//			if(n%2==0){
//				recordData.sex  = "0";
//				recordData.name  = "土豪妹";
//				recordData.date  = "2014/05/15";
//				recordData.money  = "300";
//			}else{	
//				recordData.sex  = "1";
//				recordData.name  = "土豪哥";
//				recordData.date  = "2014/05/16";
//				recordData.money  = "400";
//			}
//			mList.add(recordData);
//		}
//		
//		mAdapter = new AgentQueryAdapter(getActivity(), mList);
//		mListView.setAdapter(mAdapter);
//		mAdapter.notifyDataSetChanged();
//		
//		if(isMore){
//			 mListView.setSelection(mLoadedCount);
//		 }
//		 isMore=false;
//		 mListView.setProgressGone();
//		 mListView.setIsFetchMoreing(false);
//		 
//		 mListView.onRefreshComplete();
//暂时测试用，必须去掉end
		 
	}
	
//	OnPullDownListener onPullDownListener = new OnPullDownListener() {
//		
//		@Override
//		public void onRefresh() {
//			// TODO Auto-generated method stub
//			
//		}
//		
//		@Override
//		public void onMore() {
//			// TODO Auto-generated method stub
//			isMore=true;
//			loadMore();
//			
//		}
//	};
//	private void loadMore(){
//		if(mLoadedCount<mTotalCount){
//			mStartIndex = mLoadedCount;
//			
//			mRecordTask = new RecordTask();
//			mRecordTask.execute("");
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
//		}
//	};
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(mRecordTask != null){
			mRecordTask.cancel(true);
		}
		if(mFenrunDedailTask != null){
			mFenrunDedailTask.cancel(true);
		}
	}

//	long time=0L;
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		//1秒内，禁止双击两次提交
//		long currentTime=System.currentTimeMillis();
//		if(currentTime-time<1000){
//			return;
//		}
//		time=currentTime;
				
		switch (arg0.getId()) {
		case R.id.agent_query_day_layout://按日查收益
			mDateType = "day";
			showDate();
			query_select.setText("请选择查询时间");
			agent_query_day_layout.setSelected(true);
			agent_query_month_layout.setSelected(false);
			agent_query_year_layout.setSelected(false);
			
			agent_query_day_select.setVisibility(View.VISIBLE);
			agent_query_month_select.setVisibility(View.INVISIBLE);
			agent_query_year_select.setVisibility(View.INVISIBLE);
			
			checkToQuery();
			break;
		case R.id.agent_query_month_layout://按月查收益
			mDateType = "month";
			showDate();
			query_select.setText("请选择查询时间");
			agent_query_day_layout.setSelected(false);
			agent_query_month_layout.setSelected(true);
			agent_query_year_layout.setSelected(false);
			
			agent_query_day_select.setVisibility(View.INVISIBLE);
			agent_query_month_select.setVisibility(View.VISIBLE);
			agent_query_year_select.setVisibility(View.INVISIBLE);
			
			checkToQuery();
			break;
		case R.id.agent_query_year_layout://按年查收益
			mDateType = "year";
			showDate();
			query_select.setText("请选择查询时间");
			agent_query_day_layout.setSelected(false);
			agent_query_month_layout.setSelected(false);
			agent_query_year_layout.setSelected(true);
			
			agent_query_day_select.setVisibility(View.INVISIBLE);
			agent_query_month_select.setVisibility(View.INVISIBLE);
			agent_query_year_select.setVisibility(View.VISIBLE);
			
			checkToQuery();
			break;
		case R.id.query_date_layout://选择年月日
			selectDate();
			break;
//		case R.id.agent_main_replenish://代理商登录后的主页面之补货
//		case R.id.agent_main_replenish_img:
//		case R.id.agent_main_replenish_tv:
//			invokeAgentReplenish();
//			break;
//		case R.id.agent_main_mytfb://代理商登录后的主页面之我的通付宝，点击回到APP主功能菜单
//		case R.id.agent_main_mytfb_img:
//		case R.id.agent_main_mytfb_tv:
//			AgentToMainTFB();
//			break;
		default:
			break;
		}
	}

	private void invokeAgentReplenish()
	{
		Intent intent = new Intent();
		intent.setClass(getActivity(), AgentReplenishActivity.class);
		startActivityForResult(intent, 3);
	}
	private void selectDate()
	{
		Intent intent = new Intent();
		intent.setClass(getActivity(), AgentQueryWheelDateActivity.class);
		
		Bundle bundle = new Bundle();
		bundle.putString("dateType", ""+mDateType);
		intent.putExtra("wheelDate", bundle);
		startActivityForResult(intent, 4);
	}
	
	private void AgentToMainTFB()
	{
		getActivity().setResult(Constants.ACTIVITY_FINISH_TO_MENU);
		getActivity().finish();
	}
	
	private void updateData(){
		totalmoney.setText("￥"+NumberFormatUtil.format2(mTotalfenrun));
		mAdapter.notifyDataSetChanged();
	}
	
	private void showDetailData(){
		updateData();
		if(mCurrentItem < mListSize){
			mAppFunId=mList.get(mCurrentItem).appfunid;
			if(mCurrentItem == mListSize-1){
				mIsDialogdismiss = true;
			}
			mFenrunDedailTask = new FenrunDedailTask();
			mFenrunDedailTask.execute("");
		}else{
			mListSize=0;
			mCurrentItem=0;
			mIsDialogdismiss = false;
		}
	}
	
	
	private void checkToQuery(){
		String month="";
		String day="";
		if((mMonth+"").length()==1){
			month="0"+mMonth;
		}else{
			month=mMonth+"";
		}
		if((mDay+"").length()==1){
			day="0"+mDay;
		}else{
			day=mDay+"";
		}
		
//		String mQuerytype ="";
		if(mDateType.equals("day")){
			mQuerytype = "Date";
			mDate = mYear+"-"+month+"-"+day;
		 }else if(mDateType.equals("month")){
			 mQuerytype = "month";
			mDate = mYear+"-"+month;
		 }else if(mDateType.equals("year")){
			 mQuerytype = "year";
			 mDate = mYear+"";
		 }
		
		String date = mQuery.getValue(mDateType)+"";
		if(date.equalsIgnoreCase(mDate)){
			mIsNewQuery = false;
			
			ArrayList<AgentQueryData> mTempList = new ArrayList<AgentQueryData>();
			ArrayList<ArrayList<AgentQueryDetailData>> mTempChildList = new ArrayList();
			
			if(mDateType.equals("day")){
				mTempList=mListDate;
				mTotalfenrun=mTotalMoneyArray[0];
				
				mTempChildList=mChildListDate;
			}else if(mDateType.equals("month")){
				mTempList=mListMonth;
				mTotalfenrun=mTotalMoneyArray[1];
				
				mTempChildList=mChildListMonth;
			}else if(mDateType.equals("year")){
				mTempList=mListYear;
				mTotalfenrun=mTotalMoneyArray[2];
				
				mTempChildList=mChildListYear;
			}
			
			int max;
			
			max=mTempList.size();
			mList.clear();
			for(int n=0; n<max; n++){
				mList.add(mTempList.get(n));
			}
			
			max=mTempChildList.size();
			mChildList.clear();
			for(int n=0; n<max; n++){
				mChildList.add(mTempChildList.get(n));
			}
			
			updateData();
		}else{
			mIsNewQuery = true;
		}
		
//		mIsNewQuery = true;
		if(mIsNewQuery){
			mListSize=0;
			mCurrentItem=0;
			mIsDialogdismiss = false;
			mRecordTask = new RecordTask();
			mRecordTask.execute("");
		}
	}
	
	private void showDate(){
		String month="";
		String day="";
		if((mMonth+"").length()==1){
			month="0"+mMonth;
		}else{
			month=mMonth+"";
		}
		if((mDay+"").length()==1){
			day="0"+mDay;
		}else{
			day=mDay+"";
		}
		
		 if(mDateType.equals("day")){
			 agent_query_date.setText(mYear+"年"+month+"月"+day+"日"); 
		 }else if(mDateType.equals("month")){
			 agent_query_date.setText(mYear+"年"+month+"月");
		 }else if(mDateType.equals("year")){
			 agent_query_date.setText(mYear+"年");
		 }
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) { 
//		 case 3:
		 case 4:
			 if(resultCode == Activity.RESULT_OK )
			 {
				 mYear = data.getIntExtra("year", 0);
				 mMonth = data.getIntExtra("month", 0);
				 mDay = data.getIntExtra("day", 0);
//				 SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
//				 agent_query_date.setText(mYear+"年"+mMonth+"月"+mDay);
				 
				 showDate();
				 
				 checkToQuery();
			 }
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
				
				data.putValue("querytype", mQuerytype);
				data.putValue("querywhere", mDate);
				List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas("ApiAgentInfo", "payagentfenrunlist",
						data);
				AgentQueryParser recordParser = new AgentQueryParser();
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
			if(mIsDialogdismiss){
				PromptUtil.dissmiss();
			}
			try {
			if (mRsp == null) {
				PromptUtil.dissmiss();
				PromptUtil.showToast(mActivity, getString(R.string.net_error));
			} else {
				
				mChildList.clear();
				mList.clear();
				mTotalfenrun="0.00";
					List<ProtocolData> mDatas = mRsp.mActions;
					parserResponse(mDatas);
					
					mListSize = mList.size();
					int max=mList.size();
					if(mDateType.equals("day")){
						mListDate.clear();
						for(int n=0; n<max; n++){
							mListDate.add(mList.get(n));
						}
						mTotalMoneyArray[0]=mTotalfenrun;
						
						mChildListDate.clear();
					}else if(mDateType.equals("month")){
						mListMonth.clear();
						for(int n=0; n<max; n++){
							mListMonth.add(mList.get(n));
						}
						mTotalMoneyArray[1]=mTotalfenrun;
						
						mChildListMonth.clear();
					}else if(mDateType.equals("year")){
						mListYear.clear();
						for(int n=0; n<max; n++){
							mListYear.add(mList.get(n));
						}
						mTotalMoneyArray[2]=mTotalfenrun;
						
						mChildListYear.clear();
					}
					mQuery.putValue(mDateType, mDate);
					mIsNewQuery = false;
					
					if(mListSize==0){
						PromptUtil.dissmiss();
					}
					
					showDetailData();
					if(!ErrorUtil.create().dealError(LoginUtil.mLoginStatus,getActivity())){
						return;
					}
			}
			} catch (Exception e) {
				// TODO: handle exception
				PromptUtil.dissmiss();
				e.printStackTrace();
			}
			
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
				
				List<ProtocolData> totalfenrun = data.find("/totalfenrun");
				if (totalfenrun != null) {
					mTotalfenrun = totalfenrun.get(0).mValue;
				}

			
			
				List<ProtocolData> aupic = data.find("/msgchild");
				if(aupic==null){
					return;
				}
				for(ProtocolData child:aupic){
					AgentQueryData recordData = new AgentQueryData();
					if (child.mChildren != null && child.mChildren.size() > 0) {
						Set<String> keys = child.mChildren.keySet();
						for(String key:keys){
							List<ProtocolData> rs = child.mChildren.get(key);
							for(ProtocolData item: rs){
								if(item.mKey.equals("appfunid")){
									recordData.appfunid  = item.mValue;
									
								}else if(item.mKey.equals("appfunname")){
									recordData.appfunname  = item.mValue;
								}else if(item.mKey.equals("allfenrun")){
									recordData.allfenrun  = item.mValue;
								}	
							}
						}
					}
					
					mList.add(recordData);
				}
			}
		}
	}
	public class FenrunDedailTask extends AsyncTask<String, Integer, Boolean> {
		ProtocolRsp mRsp = null;
		FragmentActivity mActivity;
		private String mResultString;
		
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
//			if(mIsDialogdismiss){
//				PromptUtil.showDialog(getActivity(), getActivity().getResources().getString(R.string.loading));
//			}
		}
		
		@Override
		protected Boolean doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			try {
				CommonData data = new CommonData();
				
				data.putValue("querytype", mQuerytype);
				data.putValue("querywhere", mDate);
				data.putValue("appfunid", mAppFunId);
				List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas("ApiAgentInfo", "payagentfenrunlistdetail",
						data);
				AgentQueryDetailParser recordParser = new AgentQueryDetailParser();
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
			if(mIsDialogdismiss){
				PromptUtil.dissmiss();
			}
			try {
				if (mRsp == null) {
					PromptUtil.dissmiss();
					PromptUtil.showToast(mActivity, getString(R.string.net_error));
				} else {
					
//				if(!isMore){
//					mList.clear();
//				}
					mChild.clear();
					List<ProtocolData> mDatas = mRsp.mActions;
					parserResponseFenrunDedail(mDatas);
					
					ArrayList<AgentQueryDetailData> mTempChild = new ArrayList<AgentQueryDetailData>();
					int size=mChild.size();
					for(int n=0; n<size; n++){
						mTempChild.add(mChild.get(n));
					}
					if(mDateType.equals("day")){
						mChildListDate.add(mTempChild);
						
					}else if(mDateType.equals("month")){
						mChildListMonth.add(mTempChild);
						
					}else if(mDateType.equals("year")){
						mChildListYear.add(mTempChild);
						
					}
					
					mChildList.add(mTempChild);
					mCurrentItem++;
					showDetailData();
					if(!ErrorUtil.create().errorDeal(LoginUtil.mLoginStatus,getActivity())){
						return;
					}
					
				}
			} catch (Exception e) {
				// TODO: handle exception
				PromptUtil.dissmiss();
				e.printStackTrace();
			}
			
		}
	}
	
	private void parserResponseFenrunDedail(List<ProtocolData> mDatas) {
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
				
//				List<ProtocolData> alllfenrun = data.find("/alllfenrun");
//				if (alllfenrun != null) {
//					alllfenrun = alllfenrun.get(0).mValue;
//				}
//				List<ProtocolData> appfunid = data.find("/appfunid");
//				if (appfunid != null) {
//					appfunid = appfunid.get(0).mValue;
//				}
				
				List<ProtocolData> aupic = data.find("/msgchild");
				if(aupic==null){
					return;
				}
				for(ProtocolData child:aupic){
					AgentQueryDetailData recordData = new AgentQueryDetailData();
					if (child.mChildren != null && child.mChildren.size() > 0) {
						Set<String> keys = child.mChildren.keySet();
						for(String key:keys){
							List<ProtocolData> rs = child.mChildren.get(key);
							for(ProtocolData item: rs){
								if(item.mKey.equals("appmenuid")){
									recordData.appmenuid  = item.mValue;
									
								}else if(item.mKey.equals("appfunname")){
									recordData.appfunname  = item.mValue;
								}else if(item.mKey.equals("fenrun")){
									recordData.fenrun  = item.mValue;
								}	
							}
						}
					}
					
					mChild.add(recordData);
				}
			}
		}
	}

}
