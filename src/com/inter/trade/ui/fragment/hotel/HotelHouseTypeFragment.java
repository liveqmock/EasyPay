package com.inter.trade.ui.fragment.hotel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.R.integer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;

import com.inter.trade.AsyncLoadWork;
import com.inter.trade.R;
import com.inter.trade.AsyncLoadWork.AsyncLoadWorkListener;
import com.inter.trade.adapter.AirTicketCityAdapter;
import com.inter.trade.data.CommonData;
import com.inter.trade.log.Logger;
import com.inter.trade.ui.fragment.airticket.address.task.AirTicketCityListTask;
import com.inter.trade.ui.fragment.airticket.util.ApiAirticketGetCityData;
import com.inter.trade.ui.fragment.airticket.util.ApiAirticketGetCityParser;
import com.inter.trade.ui.fragment.hotel.adapter.HotelHouseTypeAdapter;
import com.inter.trade.ui.fragment.hotel.adapter.HotelListAdapter;
import com.inter.trade.ui.fragment.hotel.data.HotelKeywordData;
import com.inter.trade.ui.fragment.hotel.data.HotelListData;
import com.inter.trade.ui.fragment.hotel.data.HotelRoomData;
import com.inter.trade.ui.fragment.hotel.data.HotelServiceData;
import com.inter.trade.ui.fragment.hotel.util.HotelRoomParser;
import com.inter.trade.ui.fragment.hotel.util.HotelServiceParser;
import com.inter.trade.ui.fragment.waterelectricgas.data.ResponseStateListener;
import com.inter.trade.ui.manager.IBaseFragment;
import com.inter.trade.ui.manager.UIManagerActivity;
import com.inter.trade.ui.manager.core.IMainHandlerManager;
import com.inter.trade.ui.manager.core.UIConstantDefault;
import com.inter.trade.util.PromptUtil;

/**
 * 酒店详情 房型  Fragment
 * @author haifengli
 *
 */
public class HotelHouseTypeFragment extends IBaseFragment implements OnClickListener, ResponseStateListener,OnItemClickListener{
	
	private static final String TAG = HotelHouseTypeFragment.class.getName();
	
	private View rootView;
	
	private Bundle data = null;
	private HotelListData mHotelListData;
	private ListView lv_hotel_list;
	private HotelHouseTypeAdapter mListAdapter;
	private ArrayList<HotelRoomData> mHotelRoomDatas;
	
	private ExpandableListView expandableListView;
	
	private AirTicketCityListTask task;
	
	/**网络获取的数据*/
	private ArrayList<ApiAirticketGetCityData> netData;
	
	/**
	 * 字母列表，如A、B、C……
	 */
	private List<String> groupList = null;
	/**
	 * 点击某个字母展开的列表关键字
	 */
	private ArrayList<ArrayList<ApiAirticketGetCityData>> childList = null;
	
	private String groupIndex = "";
	
	private AirTicketCityAdapter airTicketCityAdapter ;
	
	/**
	 * ExpandableListView 的 headView
	 */
	private View headView;
	private EditText search_city;
	
	/**
	 * 记录是否是搜索
	 */
	private boolean isSearch = false;
	
	private AsyncLoadWork<HotelRoomData> asyncHotelHouseTypeTask = null;
	private ArrayList<HotelServiceData> mHotelServiceData;
	private AsyncLoadWork<HotelServiceData> asyncHotelServiceTask = null;
	
	public static HotelHouseTypeFragment newInstance (Bundle data) {
		HotelHouseTypeFragment fragment = new HotelHouseTypeFragment();
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
			mHotelListData = (HotelListData) data.getSerializable("hotelDetail");
		}
	}
	
	@Override
	protected View onInitView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Logger.d(TAG, "onInitView");
		rootView = inflater.inflate(R.layout.hotel_detail_house_type_layout, container,false);
		initViews(rootView);
		return rootView;
	}
	
	@Override
	protected void onAsyncLoadData() {
		Logger.d(TAG, "onAsyncLoadData");
//		initData();
		if(mListAdapter == null){
			runAsyncTask (groupIndex,"", false);
		}
	}

	@Override
	public void onRefreshDatas() {
		Logger.d(TAG, "onRefreshDatas");
//		setTitleBar();
//		if(mListAdapter != null){
//			mListAdapter.notifyDataSetChanged();
//		}
		
//		if(mListAdapter == null){
//			runAsyncTask (groupIndex,"", false);
//		}
	}
	
	@Override
	public void onTimeout() {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * 设置顶部栏数据（标题、更多）
	 */
	private void setTitleBar() {
		((UIManagerActivity)getActivity()).setTopTitle("房型");
	}

	@Override
	public void onDestroyView() {
		Logger.d(TAG, "onDestroyView");
		super.onDestroyView();
	}
	
	/**
	 * 异步请求
	 * @param firstLetter 城市名拼音首字母,可为空，如A、B、C等。为空时，默认返回热门城市
	 * @param cityName 用户输入搜索的城市名,可为空，支持模糊搜索，如“上海”，“上”等
	 * @param isbackground
	 */
	private void runAsyncTask (String firstLetter, String cityName , boolean isbackground) {

		HotelRoomParser netParser = new HotelRoomParser();
		String hotelCode=null;
		if( mHotelListData != null && mHotelListData.hotelCode != null) {
			hotelCode = mHotelListData.hotelCode;
		}
		
		CommonData requsetData = new CommonData();
		requsetData.putValue("hotelCode",hotelCode==null ? "": hotelCode);
		
		asyncHotelHouseTypeTask = new AsyncLoadWork<HotelRoomData>(getActivity(), netParser, requsetData, new AsyncLoadWorkListener() {

			@Override
			public void onSuccess(Object protocolDataList, Bundle bundle) {
					mHotelRoomDatas = (ArrayList<HotelRoomData>)protocolDataList;
					
					mListAdapter = new HotelHouseTypeAdapter(getActivity(), mHotelRoomDatas);
					lv_hotel_list.setAdapter(mListAdapter);
//					mListAdapter.notifyDataSetChanged();
			}

			@Override
			public void onFailure(String error) {
			}
			
		}, false, true);
		
		asyncHotelHouseTypeTask.execute("ApiHotel", "GetGuestRoom");
	
	}
	
	private void runAsyncServiceTask (String roomCode, final int position, boolean isbackground) {
		if( roomCode == null || "".equals(roomCode) ){
			return;
		}
		
		HotelServiceParser netParser = new HotelServiceParser();
		CommonData requsetData = new CommonData();
		requsetData.putValue("roomCode", roomCode);
		
		asyncHotelServiceTask = new AsyncLoadWork<HotelServiceData>(getActivity(), netParser, requsetData, new AsyncLoadWorkListener() {
			
			@Override
			public void onSuccess(Object protocolDataList, Bundle bundle) {
				mHotelServiceData = (ArrayList<HotelServiceData>)protocolDataList;
				if(mHotelRoomDatas==null && (mHotelServiceData == null || mHotelServiceData.size()==0)){
					return;
				}
				
				Bundle dataBundle = new Bundle();
				dataBundle.putSerializable("hotelService", mHotelServiceData);
				dataBundle.putSerializable("hotelDetail", mHotelRoomDatas.get(position));
				HotelDetailDialogFragment.newInstance(dataBundle).show(getFragmentManager(), "dialog");
			}
			
			@Override
			public void onFailure(String error) {
				if(mHotelRoomDatas==null && (mHotelServiceData == null || mHotelServiceData.size()==0)){
					return;
				}
				
				Bundle dataBundle = new Bundle();
				dataBundle.putSerializable("hotelService", mHotelServiceData);
				dataBundle.putSerializable("hotelDetail", mHotelRoomDatas.get(position));
				HotelDetailDialogFragment.newInstance(dataBundle).show(getFragmentManager(), "dialog");
				
//				switchFragment(ImageItemFragment.create(""), 1);
				
			}
			
		}, false, false);
		
		asyncHotelServiceTask.execute("ApiHotel", "GetRoomAmenity");
		
	}
	
	 /**
     * 切换Fragment
     * @param targetFragment 目标Fragment
     * @param isAddToBackStack 是否要添加到堆栈；=1 ：添加到堆栈；否则不添加到堆栈
     */
    private void switchFragment(Fragment targetFragment, int isAddToBackStack){
    	FragmentTransaction ft= getFragmentManager().beginTransaction();
    	ft.replace(R.id.func_container, targetFragment);
    	ft.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)/**.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)*/;
    	if(isAddToBackStack == 1) {
    		ft.addToBackStack(null);
    	}
    	ft.commit();
    }
	
	
	/**
	 * 初始化数据
	 */
	private void initData() {
		groupList = Arrays.asList(new String[]{"热门城市","搜索结果","A","B","C","D","E","F","G"
				,"H","I","J","K","L","M","N","O","P","Q","R","S","T","U"
				,"V","W","X","Y","Z"});
		
		childList = new ArrayList<ArrayList<ApiAirticketGetCityData>>();
		
		
		for(int i = 0; i < groupList.size(); i ++) {
			ArrayList<ApiAirticketGetCityData> cityDatas = new ArrayList<ApiAirticketGetCityData>();
			ApiAirticketGetCityData cityData = null;
			cityData = new ApiAirticketGetCityData();
			cityData.cityNameCh = "";
			cityData.cityCode = "";
			cityData.cityId = groupList.get(i);//这里初始化设为abc..开头字母
			cityDatas.add(cityData);
			
			childList.add(cityDatas);
		}
	}
	
	private void initViews (View rootView) {
		ArrayList<HotelRoomData> mHotelRoomDatas = new ArrayList<HotelRoomData>();
//		ArrayList<HotelListData> mListDatas = new ArrayList<HotelListData>();
//		for(int n=0; n<20; n++){
//			if(n%2==0){
//				HotelListData mListData = new HotelListData();
//				mListData.setHotelName("广州白云宾馆");
//				mListData.setMinPrice("480");
//				mListData.setHotelStar("星星星");
//				mListData.setHotelScore("4.2");
//				mListData.setHotelAddress("越秀区环市东路368号");
//				mListData.setHotelWifi("1");
//				mListData.setHotelPark("1");
//				mListDatas.add(mListData);
//			}else{
//				HotelListData mListData = new HotelListData();
//				mListData.setHotelName("广州丽晶大酒店");
//				mListData.setMinPrice("580");
//				mListData.setHotelStar("星星星星星");
//				mListData.setHotelScore("4.6");
//				mListData.setHotelAddress("越秀区环市东路16号");
//				mListData.setHotelWifi("1");
//				mListData.setHotelPark("1");
//				mListDatas.add(mListData);
//			}
//		}
//		mHotelRoomDatas=mListDatas;
		
		lv_hotel_list = (ListView)rootView.findViewById(R.id.lv_hotel_list);
//		mListAdapter = new HotelHouseTypeAdapter(getActivity(), mHotelRoomDatas);
//		lv_hotel_list.setAdapter(mListAdapter);
		lv_hotel_list.setOnItemClickListener(this);
		
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id){
		if(mHotelRoomDatas.size() == 0 || position<0){
			return;
		}
		
		HotelRoomData data = mHotelRoomDatas.get(position);
		
		String roomCode = data.code;
		if(roomCode != null){
			Bundle dataBundle = new Bundle();
			dataBundle.putSerializable("hotelDetail", data);
//			IMainHandlerManager.handlerUI(UIConstantDefault.UI_CONSTANT_HOTEL_DETAIL, 1, dataBundle);
			
			runAsyncServiceTask(roomCode, position, false);
		}
	}
	
	@Override
	public void onClick(View v) {
	}
	
	private void updateList(ArrayList<ApiAirticketGetCityData> list) {
		int groupPos = 0;
		for(int i = 0; i < groupList.size(); i ++) {
			
			if(groupIndex.equals("") || groupList.get(i).equals(groupIndex)) {
				childList.set(i, list);
				groupPos = i;
				break;
			}
		}
		
		airTicketCityAdapter = new AirTicketCityAdapter(getActivity(), groupList, childList);
		expandableListView.setAdapter(airTicketCityAdapter);
		
		if(!groupIndex.equals("")) {
			expandableListView.setSelectedGroup(groupPos);
		}
		expandableListView.expandGroup(groupPos);
	}
	
	@Override
	public void onSuccess(Object obj, Class cla) {
		netData=(ArrayList<ApiAirticketGetCityData>) obj;
		if(netData!=null){
			updateList(netData);
		}
	}
	
}
