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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.ScrollView;

import com.inter.protocol.ProtocolData;
import com.inter.protocol.ProtocolRsp;
import com.inter.protocol.util.ProtocolUtil;
import com.inter.trade.R;
import com.inter.trade.adapter.AirTicketQueryLineCangWeiAdapter;
import com.inter.trade.data.CommonData;
import com.inter.trade.data.ResponseData;
import com.inter.trade.error.ErrorUtil;
import com.inter.trade.log.Logger;
import com.inter.trade.net.HttpUtil;
import com.inter.trade.ui.MyListView;
import com.inter.trade.ui.MyListView.OnPullDownListener;
import com.inter.trade.ui.MyListView.OnRefreshListener;
import com.inter.trade.ui.fragment.airticket.util.ApiAirticketGetAirlineData;
import com.inter.trade.ui.fragment.airticket.util.ApiAirticketGetAirlineParser;
import com.inter.trade.ui.manager.IBaseFragment;
import com.inter.trade.ui.manager.UIManagerActivity;
import com.inter.trade.ui.manager.core.IMainHandlerManager;
import com.inter.trade.ui.manager.core.UIConstantDefault;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PromptUtil;

/**
 * 飞机票 查询 航班详情Fragment
 * @author zhichao.huang
 *
 */
public class AirTicketMainQueryAirlineDetailFragment extends IBaseFragment 
implements OnClickListener,OnItemClickListener, OnCheckedChangeListener{

	private static final String TAG = AirTicketMainQueryAirlineDetailFragment.class.getName();

	private ArrayList<ApiAirticketGetAirlineData> mList = new ArrayList<ApiAirticketGetAirlineData>();

	private View rootView;

	private Bundle bundleData = null;

	private MyListView mListView; 

	private int mStartIndex=0;//起始索引
	private int mTotalCount=0;//订单总条数
	private int mLoadedCount=0;//当前加载的总条数
	private boolean isMore=false;

	AirTicketQueryLineCangWeiAdapter mAdapter;
	public static final String TYPE_STRING="TYPE_STRING";

	private AirTicketQueryTask airTicketQueryTask;

	/**
	 * 选项卡（去程，返程）
	 */
	private RadioGroup qu_wang_class_navigation;
	
	/**
	 * 去程按钮，返程按钮
	 */
	private RadioButton quRadioButton, fanRadioButton;

	/**
	 * 判断 单程/返程
	 */
	private	String searchType ;

	/**
	 * 飞机票航班信息，（用于判断去程-返程的关键之一）
	 */
	private ApiAirticketGetAirlineData airticketGetAirlineData;

	/**
	 * 返程数据（用于判断去程-返程的关键之一）
	 */
	private ApiAirticketGetAirlineData airticketFanchengAirlineData;
	
	/**
	 * 去程数据（用于传给下个页面）
	 */
	private ApiAirticketGetAirlineData quchengAirlineData;
	
	/**
	 * 返程数据（用于传给下个页面）
	 */
	private ApiAirticketGetAirlineData fanchengAirlineData;

	/**
	 * 标识顶部导航栏当前点的是去程还是返程；0：去程；1:返程
	 */
	private int selected = 0;

	private OnClickListener onRightButtonClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			
			if(quRadioButton.getText().toString().equals("去程")) {
				PromptUtil.showToast(getActivity(), "请选择去程的舱位等级");
				return;
			}
			if(fanRadioButton.getText().toString().equals("返程")) {
				PromptUtil.showToast(getActivity(), "请选择返程的舱位等级");
				return;
			}
			
			if(quchengAirlineData != null && fanchengAirlineData != null) {
				
				if(bundleData != null && quchengAirlineData != null && fanchengAirlineData != null) {
					bundleData.putSerializable("AirlineDetail", quchengAirlineData);//去程数据
					bundleData.putSerializable("AirlineDetailFan", fanchengAirlineData);//返程数据
				}
				addFragmentToStack(UIConstantDefault.UI_CONSTANT_AIR_TICKET_ORDER_FORM_WANGFAN, 1, bundleData);
				
			} else {
				PromptUtil.showToast(getActivity(), "数据不完整，请选择");
			}
			
		}
	};

	public static AirTicketMainQueryAirlineDetailFragment newInstance (Bundle data) {
		AirTicketMainQueryAirlineDetailFragment fragment = new AirTicketMainQueryAirlineDetailFragment();
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
	}

	@Override
	protected View onInitView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.airticket_query_airline_detail_list, container,false);
		initViews(rootView);
		return rootView;
	}

	@Override
	protected void onAsyncLoadData() {
		if (bundleData != null) {
			searchType =  bundleData.getString("searchType") != null ? bundleData.getString("searchType") : "";
			if (searchType.equals("S")) {//单程
				qu_wang_class_navigation.setVisibility(View.GONE);

				airTicketQueryTask = new AirTicketQueryTask();
				airTicketQueryTask.execute("");
			} else {
				((UIManagerActivity)getActivity()).setRightButtonOnClickListener("确认", View.VISIBLE, onRightButtonClickListener);
			}

		}
	}

	@Override
	public void onRefreshDatas() {
		((UIManagerActivity)getActivity()).setTopTitle("舱位选择");
		if(searchType.equals("D")) {
			((UIManagerActivity)getActivity()).setRightButtonOnClickListener("确认", View.VISIBLE, onRightButtonClickListener);
		}
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		((UIManagerActivity)getActivity()).setRightButtonOnClickListener("确认", View.GONE, onRightButtonClickListener);
	}

	private void initViews (View rootView) {

		qu_wang_class_navigation = (RadioGroup) rootView.findViewById(R.id.qu_wang_class_navigation);
		qu_wang_class_navigation.setOnCheckedChangeListener(this);
		
		quRadioButton = (RadioButton)qu_wang_class_navigation.findViewById(R.id.qu_class_navigation);
		fanRadioButton = (RadioButton)qu_wang_class_navigation.findViewById(R.id.wang_class_navigation);


		mListView = (MyListView) rootView.findViewById(R.id.mm_listview);
		mListView.setOnItemClickListener(this);

		mListView.setOnItemClickListener(this);
		mListView.setonRefreshListener(onRefreshListener);

		mListView.setOnPullDownListener(onPullDownListener);
		mListView.setEnableAutoFetchMore(true);

//		rootView.findViewById(R.id.explain).setOnClickListener(this);
		rootView.findViewById(R.id.explain_button).setOnClickListener(this);
		
		airticketGetAirlineData = (ApiAirticketGetAirlineData)bundleData.getSerializable("AirlineData");

		if(airticketGetAirlineData != null) {
			airticketFanchengAirlineData = airticketGetAirlineData.airticketFanchengAirlineData;
			if(airticketFanchengAirlineData != null) {//存在返程数据
				//判断 单程/返程
				searchType =  bundleData.getString("searchType") != null ? bundleData.getString("searchType") : "";
				if (searchType.equals("S")) {//单程
					qu_wang_class_navigation.setVisibility(View.GONE);

				} else {//返程
					qu_wang_class_navigation.setVisibility(View.VISIBLE);
					qu_wang_class_navigation.getChildAt(0).performClick();

				}

			} else {
				qu_wang_class_navigation.setVisibility(View.GONE);
			}
		}

	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {

		switch (checkedId) {
		case R.id.qu_class_navigation:// 选择去程
			selected = 0;
			isMore=false;
			mStartIndex=0;
			airTicketQueryTask = new AirTicketQueryTask();
			airTicketQueryTask.execute("");
			break;
		case R.id.wang_class_navigation:// 选择返程
			selected = 1;
			isMore=false;
			mStartIndex=0;
			airTicketQueryTask = new AirTicketQueryTask();
			airTicketQueryTask.execute("");
			break;
		default:
			break;
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
	
	@Override
	public void onTimeout() {
		// TODO Auto-generated method stub
		
	}
	
	private void loadMore(){
		if(mLoadedCount<mTotalCount){
			mStartIndex = mLoadedCount;
			airTicketQueryTask = new AirTicketQueryTask();
			airTicketQueryTask.execute("");
		}else{
			mListView.setLastText();
			//			mListView.setProgressGone();
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
		if(mAdapter == null ) return;
		if(mList== null || mList.size()==0 || position > mList.size()) return;

		ApiAirticketGetAirlineData airlineData = mList.get(position-1);

		if(airticketFanchengAirlineData != null) {//说明存在返程数据，处理如下(点“确定按钮”导航栏中的去程、返程中要有数据便提交)
			
			if(selected == 0) {//当前选的是单程选项卡
				
				quRadioButton.setText("去程-"+airlineData.aClass);
				
				quchengAirlineData = airlineData;
				
			} else { //当前选的是返程选项卡
				fanRadioButton.setText("返程-"+airlineData.aClass);
				fanchengAirlineData = airlineData;
			}
			
		} else {

			if(bundleData != null && airlineData != null) {
				bundleData.putSerializable("AirlineDetail", airlineData);
			}
			addFragmentToStack(UIConstantDefault.UI_CONSTANT_AIR_TICKET_ORDER_FORM, 1, bundleData);
		}

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.explain_button:
			showPopupWindow();
			break;
		default:
			break;
		}
	}

	private ScrollView scrollView;
	private PopupWindow popupWindow;

	private void showPopupWindow() {
		scrollView = (ScrollView) LayoutInflater.from(getActivity()).inflate(
				R.layout.airticket_buy_explain_layout, null);

		popupWindow = new PopupWindow(getActivity());
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setWidth(getActivity().getWindowManager().getDefaultDisplay().getWidth());
		popupWindow.setHeight(getActivity().getWindowManager().getDefaultDisplay().getHeight());
		popupWindow.setOutsideTouchable(true);
		popupWindow.setFocusable(true);
		popupWindow.setContentView(scrollView);

		popupWindow.setAnimationStyle(R.style.popuStyle);
		popupWindow.showAtLocation(rootView, Gravity.LEFT
				| Gravity.BOTTOM , 0, 0);//需要指定Gravity，默认情况是center.
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

				if(selected == 0) {//去程
					data.putValue("departCityCode", bundleData.getString("departCityCode"));
					data.putValue("arriveCityCode", bundleData.getString("arriveCityCode"));
					//				data.putValue("departDate",  bundleData.getString("departDate"));
					//				data.putValue("returnDate", bundleData.getString("returnDate"));
					data.putValue("departTime", bundleData.getString("departTime"));
					data.putValue("returnTime", bundleData.getString("returnTime"));
					data.putValue("searchType", bundleData.getString("searchType"));
					data.putValue("flight", bundleData.getString("flight"));
				} else {//返程
					data = new CommonData();

					data.putValue("departCityCode", bundleData.getString("arriveCityCode"));
					data.putValue("arriveCityCode", bundleData.getString("departCityCode"));
					//				data.putValue("departDate",  bundleData.getString("departDate"));
					//				data.putValue("returnDate", bundleData.getString("returnDate"));

					data.putValue("returnTime", bundleData.getString("returnTime"));
					data.putValue("searchType", bundleData.getString("searchType"));

					if(airticketFanchengAirlineData != null) {
						data.putValue("departTime", airticketFanchengAirlineData.takeOffTime);
						data.putValue("flight", airticketFanchengAirlineData.flight);
					}
				}
				List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas("ApiAirticket", "getAirlineDetail",
						data);
				ApiAirticketGetAirlineParser recordParser = new ApiAirticketGetAirlineParser();
				mRsp = HttpUtil.doRequest(recordParser, mDatas);
			} catch (Exception e) {
				e.printStackTrace();
				Logger.e(e);
			}

			return null;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			PromptUtil.dissmiss();
			try {
				if (mRsp == null) {
					PromptUtil.showToast(mActivity, getActivity().getString(R.string.net_error));
				} else {

					if(!isMore){
						mList.clear();
					}

					List<ProtocolData> mDatas = mRsp.mActions;
					parserResponse(mDatas);

					if(!ErrorUtil.create().errorDeal(LoginUtil.mLoginStatus,getActivity())){
						return;
					}

					mAdapter = new AirTicketQueryLineCangWeiAdapter(getActivity(), mList);
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
								else if(item.mKey.equals("rerNote")){

									recordData.rerNote  = item.mValue;
								}
								else if(item.mKey.equals("endNote")){

									recordData.endNote  = item.mValue;
								}
								else if(item.mKey.equals("refNote")){

									recordData.refNote  = item.mValue;
								}
								else if(item.mKey.equals("dPortName")){

									recordData.dPortName  = item.mValue;
								}
								else if(item.mKey.equals("aPortName")){

									recordData.aPortName  = item.mValue;
								}
								else if(item.mKey.equals("class")){

									recordData.aClass  = item.mValue;
								}
								else if(item.mKey.equals("id")){

									recordData.id  = item.mValue;
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
