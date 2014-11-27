package com.inter.trade.ui.fragment.airticket;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.ScrollView;
import android.widget.TextView;

import com.inter.protocol.ProtocolData;
import com.inter.protocol.ProtocolRsp;
import com.inter.protocol.util.ProtocolUtil;
import com.inter.trade.R;
import com.inter.trade.adapter.AirTicketQueryLineSeoAdapter;
import com.inter.trade.adapter.AirTicketQueryLineWangfanAdapter;
import com.inter.trade.data.CommonData;
import com.inter.trade.data.ResponseData;
import com.inter.trade.error.ErrorUtil;
import com.inter.trade.log.Logger;
import com.inter.trade.net.HttpUtil;
import com.inter.trade.ui.MyListView;
import com.inter.trade.ui.MyListView.OnPullDownListener;
import com.inter.trade.ui.MyListView.OnRefreshListener;
import com.inter.trade.ui.fragment.airticket.util.AirLineUtils;
import com.inter.trade.ui.fragment.airticket.util.ApiAirticketGetAirlineData;
import com.inter.trade.ui.fragment.airticket.util.ApiAirticketGetAirlineParser;
import com.inter.trade.ui.manager.IBaseFragment;
import com.inter.trade.ui.manager.UIManagerActivity;
import com.inter.trade.ui.manager.core.UIConstantDefault;
import com.inter.trade.util.DateUtil;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PromptUtil;

/**
 * 飞机票 机票查询(返程) Fragment
 * @author zhichao.huang
 *
 */
public class AirTicketMainQueryFanchengFragment extends IBaseFragment implements OnClickListener,OnItemClickListener{

	private static final String TAG = AirTicketMainQueryFanchengFragment.class.getName();

	//所有航班
	private ArrayList<ApiAirticketGetAirlineData> mList = null;

	//航空公司名（过滤掉同名的航空公司）
	private ArrayList<ApiAirticketGetAirlineData> mAirNameList = null;

	private View rootView;

	/**
	 * 前一天、后一天、日期
	 */
	private TextView before_day, after_day, calendar;

	/**
	 * 类型筛选、公司筛选
	 */
	private Button screen, screen_company;

	private Bundle bundleData = null;

	private MyListView mListView; 

	private int mStartIndex=0;//起始索引
	private int mTotalCount=0;//订单总条数
	private int mLoadedCount=0;//当前加载的总条数
	private boolean isMore=false;

	AirTicketQueryLineWangfanAdapter mAdapter;
	public static final String TYPE_STRING="TYPE_STRING";

	private AirTicketQueryTask airTicketQueryTask;

	/***
	 * 飞机票待查询的日期
	 */
	private String airTicketDate = null;
	
	/**
	 * 去程日期
	 */
	private String startDate= null;

	public static AirTicketMainQueryFanchengFragment newInstance (Bundle data) {
		AirTicketMainQueryFanchengFragment fragment = new AirTicketMainQueryFanchengFragment();
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
		}
		mList = new ArrayList<ApiAirticketGetAirlineData>();
		mAirNameList = new ArrayList<ApiAirticketGetAirlineData>();
	}

	@Override
	protected View onInitView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.i(TAG, "onInitView");
		rootView = inflater.inflate(R.layout.airticket_query_list, container,false);
		initViews(rootView);
		return rootView;
	}

	@Override
	protected void onAsyncLoadData() {
		if (bundleData != null) {
			airTicketQueryTask = new AirTicketQueryTask();
			airTicketQueryTask.execute("");
		}
	}

	@Override
	public void onRefreshDatas() {
		String cityTitle = bundleData.getString("arriveCityNameCh") +" - "+ bundleData.getString("departCityNameCh");
		((UIManagerActivity)getActivity()).setTopTitle("返程("+cityTitle+")");
	}
	
	@Override
	public void onTimeout() {
		// TODO Auto-generated method stub
		
	}

	private void initViews (View rootView) {

		rootView.findViewById(R.id.before_day).setOnClickListener(this);
		rootView.findViewById(R.id.after_day).setOnClickListener(this);
		calendar = (TextView)rootView.findViewById(R.id.calendar); calendar.setOnClickListener(this);

		screen = (Button)rootView.findViewById(R.id.screen); screen.setOnClickListener(this);
		screen_company = (Button)rootView.findViewById(R.id.screen_company); screen_company.setOnClickListener(this);


		mListView = (MyListView) rootView.findViewById(R.id.mm_listview);
		mListView.setOnItemClickListener(this);

		mListView.setOnItemClickListener(this);
		mListView.setonRefreshListener(onRefreshListener);

		mListView.setOnPullDownListener(onPullDownListener);
		mListView.setEnableAutoFetchMore(true);

		
		//去程日期
		if(bundleData.getString("departTime") != null && !bundleData.getString("departTime").equals("")) {
			startDate = bundleData.getString("departTime").substring(0, bundleData.getString("departTime").indexOf("T"));
		}
		
		//比较去程日期 和 回程日期
		if(startDate != null && bundleData.getString("returnDate") != null) {
			//去程日期小于等于返程日期则直接按返程日期
			if(DateUtil.compareDate(startDate, bundleData.getString("returnDate"))) {
				airTicketDate = bundleData.getString("returnDate");
			} else{//去程日期大于返程日期，则返程日期 等于去程日期的值
				airTicketDate = startDate;
				
			}
			
			//设置当前查询的返程日期
			calendar.setText(airTicketDate != null ? airTicketDate : "");
			
		} 
		

	}

	OnPullDownListener onPullDownListener = new OnPullDownListener() {

		@Override
		public void onRefresh() {

		}

		@Override
		public void onMore() {
			isMore=true;
			loadMore();

		}
	};
	private void loadMore(){
		if(mLoadedCount<mTotalCount){
			mStartIndex = mLoadedCount;
			airTicketQueryTask = new AirTicketQueryTask();
			airTicketQueryTask.execute("");
		}else{
//			mListView.setLastText();
						mListView.setProgressGone();
			//			mListView.setIsFetchMoreing(true);
		}
	}

	OnRefreshListener onRefreshListener = new OnRefreshListener() {

		@Override
		public void onRefresh() {
			isMore=false;
			mStartIndex=0;
			airTicketQueryTask = new AirTicketQueryTask();
			airTicketQueryTask.execute("");
		}
	};

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if(mAdapter == null || bundleData == null) return;
		if(mAdapter.getCount()==0 || position > mAdapter.getCount()) return;

		ApiAirticketGetAirlineData airlineData = (ApiAirticketGetAirlineData)mAdapter.getItem(position-1);
		//		ApiAirticketGetAirlineData airlineData = mList.get(position-1);
		if(bundleData != null && airlineData != null) {
//			bundleData.putString("departTime", airlineData.takeOffTime != null && !airlineData.takeOffTime.equals("") 
//					? airlineData.takeOffTime : "" );//出发时间
//			bundleData.putString("returnTime", "");//返程时间
//			bundleData.putString("flight", airlineData.flight != null && !airlineData.flight.equals("") 
//					? airlineData.flight : "");//航班号
//
//			bundleData.putString("arriveTime", airlineData.arriveTime != null && !airlineData.arriveTime.equals("") 
//					? airlineData.arriveTime : "" );//到达时间
//
//			bundleData.putString("dPortName", airlineData.dPortName != null && !airlineData.dPortName.equals("") 
//					? airlineData.dPortName : "" );//出发机场
//			bundleData.putString("aPortName", airlineData.aPortName != null && !airlineData.aPortName.equals("") 
//					? airlineData.aPortName : "" );//到达机场
//
//			bundleData.putString("airLineCode", airlineData.airLineCode != null && !airlineData.airLineCode.equals("") 
//					? airlineData.airLineCode : "" );//航空公司代码 （要改为航空公司）
//			bundleData.putString("airLineName", airlineData.airLineName != null && !airlineData.airLineName.equals("") 
//					? airlineData.airLineName : "" );//航空公司
			
			
			//包含去程和返程的航班信息
			ApiAirticketGetAirlineData newAirlineData = (ApiAirticketGetAirlineData)bundleData.getSerializable("AirlineData");
			newAirlineData.airticketFanchengAirlineData = airlineData;
			
			bundleData.putSerializable("AirlineData", newAirlineData);//去程航班信息,如果内字段airticketFanchengAirlineData 不为空，说明存在返程信息
			
		}
		
		//用户选的是往返机票查询
		if(bundleData.getString("searchType").equals("D")) {
			addFragmentToStack(UIConstantDefault.UI_CONSTANT_AIR_TICKET_AIRLINE_DETAIL, 1, bundleData);
			
		} else {
			PromptUtil.showToast(getActivity(), "操作出现异常");
		}

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.before_day://前一天,不能小于去程时间
			if(DateUtil.compareDate(startDate, calendar.getText().toString())) {
				airTicketDate = DateUtil.getSpecifiedDayBefore(calendar.getText().toString());
				calendar.setText(airTicketDate);
				isMore=false;
				onAsyncLoadData();
			}else{
				PromptUtil.showToast(getActivity(), "返程日期不能在去程日期之前");
			}

			break;
		case R.id.after_day://后一天
			airTicketDate = DateUtil.getSpecifiedDayAfter(calendar.getText().toString());
			calendar.setText(airTicketDate);
			isMore=false;
			onAsyncLoadData();
			break;
		case R.id.screen:
			showPopupWindow(0);
			screen.setSelected(true);
			screen_company.setSelected(false);
			break;
		case R.id.screen_company:
			showPopupWindow(1);
			screen.setSelected(false);
			screen_company.setSelected(true);
			break;
		default:
			break;
		}

	}

	private ScrollView scrollView;
	private LinearLayout linearLayout;
	private ListView listView;
	private PopupWindow popupWindow;

	private CheckBox morning_select, afternoon_select, night_select
	, time_start_select, time_end_select ,price_high_low_select, price_low_high_select;

	private AirTicketQueryLineSeoAdapter lineSeo = null;

	//筛选条件
	/**
	 * 时段（如早上（6-12点））；
	 * 选项<-1：用户没选择；0:早上；1：下午；2：晚上>
	 */
	private int timeInterval = -1; 
	/**
	 * 排序（如按价格升序）;
	 * 选项< -1：用户没选择;  0:时间从早到晚；1：时间从晚到早；2：价格从高到低；3：价格从低到高>
	 */
	private int highLow = -1;
	/**
	 * 航空公司名字
	 */
	private String name = "";
	
	private void showPopupWindow(int mark) {
		if(mark == 0) {
			if(scrollView == null) {
				scrollView = (ScrollView) LayoutInflater.from(getActivity()).inflate(
						R.layout.airticket_seo_layout, null);

				//早上(6点-12点)
				morning_select = (CheckBox)scrollView.findViewById(R.id.morning_select);
				morning_select.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						if(mAdapter == null) return;
						
						if(!isChecked)  {
							timeInterval = -1;
							
						} else {
							if(afternoon_select.isChecked()) {
								afternoon_select.setChecked(false);
							}
							if(night_select.isChecked()) {
								night_select.setChecked(false);
							}
							
							timeInterval = 0;
							
						}
						
						mAdapter = new AirTicketQueryLineWangfanAdapter(getActivity(), AirLineUtils.mixSort(mList, timeInterval, highLow, name));
						mListView.setAdapter(mAdapter);
						
					}
				});

				//下午(12点-18点)
				afternoon_select = (CheckBox)scrollView.findViewById(R.id.afternoon_select);
				afternoon_select.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						if(mAdapter == null) return;
						
						if(!isChecked)  {
							timeInterval = -1;
							
						} else {
							if(morning_select.isChecked()) {
								morning_select.setChecked(false);
							}
							if(night_select.isChecked()) {
								night_select.setChecked(false);
							}
							timeInterval = 1;
							
						}
						
						mAdapter = new AirTicketQueryLineWangfanAdapter(getActivity(), AirLineUtils.mixSort(mList, timeInterval, highLow, name));
						mListView.setAdapter(mAdapter);
						
					}
				});

				//晚上(18点-24点)
				night_select = (CheckBox)scrollView.findViewById(R.id.night_select);
				night_select.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						if(mAdapter == null) return;
						
						if(!isChecked)  {
							timeInterval = -1;
							
						} else {
							if(morning_select.isChecked()) {
								morning_select.setChecked(false);
							}
							if(afternoon_select.isChecked()) {
								afternoon_select.setChecked(false);
							}
							timeInterval = 2;
							
						}
						
						mAdapter = new AirTicketQueryLineWangfanAdapter(getActivity(), AirLineUtils.mixSort(mList, timeInterval, highLow, name));
						mListView.setAdapter(mAdapter);
						
					}
				});

				//时间从早到晚
				time_start_select = (CheckBox)scrollView.findViewById(R.id.time_start_select);
				time_start_select.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						if(mAdapter == null) return;
						
						if(!isChecked)  {
							highLow = -1;
							
						} else {
							if(time_end_select.isChecked()) {
								time_end_select.setChecked(false);
							}
							if(price_high_low_select.isChecked()) {
								price_high_low_select.setChecked(false);
							}
							if(price_low_high_select.isChecked()) {
								price_low_high_select.setChecked(false);
							}
							highLow = 0;
						}
						mAdapter = new AirTicketQueryLineWangfanAdapter(getActivity(), AirLineUtils.mixSort(mList, timeInterval, highLow, name));
						mListView.setAdapter(mAdapter);
					}
				});

				//时间从晚到早
				time_end_select = (CheckBox)scrollView.findViewById(R.id.time_end_select);
				time_end_select.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						if(mAdapter == null) return;
						
						
						if(!isChecked)  {
							highLow = -1;
							
						} else {
							if(time_start_select.isChecked()) {
								time_start_select.setChecked(false);
							}
							if(price_high_low_select.isChecked()) {
								price_high_low_select.setChecked(false);
							}
							if(price_low_high_select.isChecked()) {
								price_low_high_select.setChecked(false);
							}
							highLow = 1;
						}
						mAdapter = new AirTicketQueryLineWangfanAdapter(getActivity(), AirLineUtils.mixSort(mList, timeInterval, highLow, name));
						mListView.setAdapter(mAdapter);
					}
				});

				//价格从高到低
				price_high_low_select = (CheckBox)scrollView.findViewById(R.id.price_high_low_select);
				price_high_low_select.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						if(mAdapter == null) return;
						
						
						if(!isChecked)  {
							highLow = -1;
							
						} else {
							if(time_start_select.isChecked()) {
								time_start_select.setChecked(false);
							}
							if(time_end_select.isChecked()) {
								time_end_select.setChecked(false);
							}
							if(price_low_high_select.isChecked()) {
								price_low_high_select.setChecked(false);
							}
							highLow = 2;
						}
						mAdapter = new AirTicketQueryLineWangfanAdapter(getActivity(), AirLineUtils.mixSort(mList, timeInterval, highLow, name));
						mListView.setAdapter(mAdapter);
					}
				});

				//价格从低到高
				price_low_high_select = (CheckBox)scrollView.findViewById(R.id.price_low_high_select);
				price_low_high_select.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						if(mAdapter == null) return;
						
						
						if(!isChecked)  {
							highLow = -1;
							
						} else {
							if(time_start_select.isChecked()) {
								time_start_select.setChecked(false);
							}
							if(time_end_select.isChecked()) {
								time_end_select.setChecked(false);
							}
							if(price_high_low_select.isChecked()) {
								price_high_low_select.setChecked(false);
							}
							highLow = 3;
						}
						mAdapter = new AirTicketQueryLineWangfanAdapter(getActivity(), AirLineUtils.mixSort(mList, timeInterval, highLow, name));
						mListView.setAdapter(mAdapter);
					}
				});
			}

			ViewGroup parent = (ViewGroup)scrollView.getParent();
			if (parent != null) {  
				parent.removeView(scrollView);  
			}   

		} else {
			
			if(linearLayout == null) {
				linearLayout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(
						R.layout.airticket_right_list_seo_layout, null);

				listView = (ListView) linearLayout.findViewById(R.id.air_seo_list);

				if(mList != null && mList.size()>0) {
					lineSeo = new AirTicketQueryLineSeoAdapter(getActivity(), AirLineUtils.toAirLine(mList));
					listView.setAdapter(lineSeo);
					listView.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
								long arg3) {

							if(lineSeo == null ) return;
							
							lineSeo.setCurrentPosition(arg2);
							
							ApiAirticketGetAirlineData data = null;	
							data = (ApiAirticketGetAirlineData)lineSeo.getItem(arg2);
							name = data.airLineName;
							
							mAdapter = new AirTicketQueryLineWangfanAdapter(getActivity(), AirLineUtils.mixSort(mList, timeInterval, highLow, name));
							mListView.setAdapter(mAdapter);
							
							popupWindow.dismiss();
							popupWindow = null;
						}
					});
				}
			}
			
			ViewGroup parent = (ViewGroup)linearLayout.getParent();
			if (parent != null) {  
				parent.removeView(linearLayout);  
			} 
			
		}

		popupWindow = new PopupWindow(getActivity());
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setWidth(getActivity().getWindowManager().getDefaultDisplay().getWidth() / 2);
		popupWindow.setHeight(rootView.getHeight() - screen.getHeight());
		popupWindow.setOutsideTouchable(true);
		popupWindow.setFocusable(true);
		popupWindow.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss() {
				screen.setSelected(false);
				screen_company.setSelected(false);
			}
		});
		if(mark == 0) {
			popupWindow.setContentView(scrollView);
		} else{
			popupWindow.setContentView(linearLayout);
		}
		popupWindow.setAnimationStyle(R.style.popuStyle);
		popupWindow.showAtLocation(rootView, mark == 0 ? Gravity.LEFT
				| Gravity.BOTTOM : Gravity.RIGHT
				| Gravity.BOTTOM, 0, screen.getHeight());//需要指定Gravity，默认情况是center.
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		if(airTicketQueryTask != null) {
			airTicketQueryTask.cancel(true);
		}

	}


	/**
	 * 机票查询
	 * @author zhichao.huang
	 *
	 */
	public class AirTicketQueryTask extends AsyncTask<String, Integer, Boolean> {
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

				data.putValue("departCityCode", bundleData.getString("departCityCode"));
				data.putValue("arriveCityCode", bundleData.getString("arriveCityCode"));
				data.putValue("departDate", startDate != null ? startDate : "");
				data.putValue("returnDate", airTicketDate != null ? airTicketDate : "");
				data.putValue("searchType", bundleData.getString("searchType"));

				List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas("ApiAirticket", "getAirline",
						data);
				ApiAirticketGetAirlineParser recordParser = new ApiAirticketGetAirlineParser();
				mRsp = HttpUtil.doRequest(recordParser, mDatas);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				Logger.e(e);
				mRsp =null;
				return null;
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
					PromptUtil.showToast(getActivity(), getActivity().getString(R.string.net_error));
					mListView.setAdapter(null);
				} else {

					if(!isMore){
						mList.clear();
					}

					List<ProtocolData> mDatas = mRsp.mActions;
					parserResponse(mDatas);

					if(!ErrorUtil.create().errorDeal(LoginUtil.mLoginStatus,getActivity())){
						mListView.setAdapter(null);
						return;
					}
					
					//测试返程
					mList = AirLineUtils.getAirlineDatas(mList, bundleData.getString("departCityCode"), 2);

					mAdapter = new AirTicketQueryLineWangfanAdapter(getActivity(), mList);
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
					LoginUtil.mLoginStatus.message = message.get(0).mValue;
				}

				List<ProtocolData> msgallcount = data.find("/msgallcount");
				if(msgallcount != null){
					//					mTotalCount = Integer.parseInt(msgallcount.get(0).mValue.trim());
				}
				List<ProtocolData> msgdiscount = data.find("/msgdiscount");
				if(msgdiscount != null){
					//					mLoadedCount  = Integer.parseInt(msgdiscount.get(0).mValue.trim());
				}


				List<ProtocolData> aupic = data.find("/msgchild");
				if(aupic==null){
					PromptUtil.showToast(getActivity(), LoginUtil.mLoginStatus.message);
					return;
				}
				for(ProtocolData child:aupic){
					ApiAirticketGetAirlineData recordData = new ApiAirticketGetAirlineData();
					if (child.mChildren != null && child.mChildren.size() > 0) {
						Set<String> keys = child.mChildren.keySet();
						for(String key:keys){
							List<ProtocolData> rs = child.mChildren.get(key);
							for(ProtocolData item: rs){
								if(item.mKey.equals("takeOffTime")){
									recordData.takeOffTime  = item.mValue;

								}else if(item.mKey.equals("arriveTime")){
									recordData.arriveTime  = item.mValue;

								}else if(item.mKey.equals("flight")){

									recordData.flight  = item.mValue;

								}else if(item.mKey.equals("craftType")){

									recordData.craftType  = item.mValue;

								}else if(item.mKey.equals("airLineCode")){

									recordData.airLineCode  = item.mValue;

								}else if(item.mKey.equals("airLineName")){

									recordData.airLineName  = item.mValue;

								}else if(item.mKey.equals("price")){

									recordData.price  = item.mValue;
								}else if(item.mKey.equals("standardPrice")){

									recordData.standardPrice  = item.mValue;
								}
								else if(item.mKey.equals("oilFee")){

									recordData.oilFee  = item.mValue;
								}else if(item.mKey.equals("tax")){

									recordData.tax  = item.mValue;
								}else if(item.mKey.equals("standardPriceForChild")){

									recordData.standardPriceForChild  = item.mValue;
								}

								else if(item.mKey.equals("oilFeeForChild")){

									recordData.oilFeeForChild  = item.mValue;
								}
								else if(item.mKey.equals("taxForChild")){

									recordData.taxForChild  = item.mValue;
								}
								else if(item.mKey.equals("standardPriceForBaby")){

									recordData.standardPriceForBaby  = item.mValue;
								}	
								else if(item.mKey.equals("oilFeeForBaby")){

									recordData.oilFeeForBaby  = item.mValue;
								}
								else if(item.mKey.equals("taxForBaby")){

									recordData.taxForBaby  = item.mValue;
								}
								else if(item.mKey.equals("quantity")){

									recordData.quantity  = item.mValue;
								}
								else if(item.mKey.equals("rePolicy")){

									recordData.rePolicy  = item.mValue;
								}
								else if(item.mKey.equals("dPortName")){

									recordData.dPortName  = item.mValue;
								}
								else if(item.mKey.equals("aPortName")){

									recordData.aPortName  = item.mValue;
								}
								else if(item.mKey.equals("dPortCode")){

									recordData.dPortCode  = item.mValue;
								}
								else if(item.mKey.equals("aPortCode")){

									recordData.aPortCode  = item.mValue;
								}
								else if(item.mKey.equals("dCityCode")){

									recordData.dCityCode  = item.mValue;
								}
							}
						}
					}

					mList.add(recordData);
				}
			}
		}
	}


}
