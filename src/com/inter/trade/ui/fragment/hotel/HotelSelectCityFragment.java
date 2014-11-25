package com.inter.trade.ui.fragment.hotel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;

import com.inter.trade.R;
import com.inter.trade.data.CommonData;
import com.inter.trade.ui.fragment.hotel.adapter.HotelCityAdapter;
import com.inter.trade.ui.fragment.hotel.data.HotelGetCityData;
import com.inter.trade.ui.fragment.hotel.task.HotelSelectCityTask;
import com.inter.trade.ui.fragment.hotel.util.HotelGetCityParser;
import com.inter.trade.ui.fragment.waterelectricgas.data.ResponseStateListener;
import com.inter.trade.ui.manager.IBaseFragment;
import com.inter.trade.ui.manager.UIManagerActivity;
import com.inter.trade.util.PromptUtil;

/**
 * 酒店预订 选择城市 Fragment
 * @author zhichao.huang
 *
 */
public class HotelSelectCityFragment extends IBaseFragment implements OnClickListener, ResponseStateListener{
	
	private static final String TAG = HotelSelectCityFragment.class.getName();
	
	private View rootView;
	
	private Button airticket_start_city;//出发城市
	private Button airticket_end_city;//到达城市
	
	private Bundle data = null;
	
	private ExpandableListView expandableListView;
	
	private HotelSelectCityTask task;
	
	/**网络获取的数据*/
	private ArrayList<HotelGetCityData> netData;
	
	/**
	 * 字母列表，如A、B、C……
	 */
	private List<String> groupList = null;
	/**
	 * 点击某个字母展开的列表城市
	 */
	private ArrayList<ArrayList<HotelGetCityData>> childList = null;
	
	private String groupIndex = "";
	
	private HotelCityAdapter hotelCityAdapter ;
	
	/**
	 * ExpandableListView 的 headView
	 */
	private View headView;
	private EditText search_city;
	
	/**
	 * 记录是否是搜索
	 */
	private boolean isSearch = false;
	
	public static HotelSelectCityFragment newInstance (Bundle data) {
		HotelSelectCityFragment fragment = new HotelSelectCityFragment();
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
	
	@Override
	protected View onInitView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.hotel_elistview_city, container,false);
		initViews(rootView);
		return rootView;
	}
	
	@Override
	protected void onAsyncLoadData() {
		initData();
		runAsyncTask (groupIndex,"", false);
	}

	@Override
	public void onRefreshDatas() {
		setTitleBar();
		
	}
	
	@Override
	public void onTimeout() {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * 设置顶部栏数据（标题、更多）
	 */
	private void setTitleBar() {
		((UIManagerActivity)getActivity()).setTopTitle("城市选择");
//		((UIManagerActivity)getActivity()).setRightButtonOnClickListener("确认", View.VISIBLE, onRightButtonClickListener);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
//		((UIManagerActivity)getActivity()).setRightButtonOnClickListener("确认", View.GONE, onRightButtonClickListener);
	}
	
	/**
	 * 异步请求
	 * @param firstLetter 城市名拼音首字母,可为空，如A、B、C等。为空时，默认返回热门城市
	 * @param cityName 用户输入搜索的城市名,可为空，支持模糊搜索，如“上海”，“上”等
	 * @param isbackground
	 */
	private void runAsyncTask (String firstLetter, String cityName , boolean isbackground) {
		HotelGetCityParser myParser = new HotelGetCityParser();
		CommonData mData = new CommonData();
		mData.putValue("firstLetter", !firstLetter.equals("") ? firstLetter : "" );
		mData.putValue("cityName", !cityName.equals("") ? cityName : "");
		
		task = new HotelSelectCityTask(getActivity(), myParser, mData, isbackground, this);
		task.execute("ApiHotel","GetCity");
	}
	
	/**
	 * 初始化数据
	 */
	private void initData() {
		groupList = Arrays.asList(new String[]{"热门城市","搜索结果","A","B","C","D","E","F","G"
				,"H","I","J","K","L","M","N","O","P","Q","R","S","T","U"
				,"V","W","X","Y","Z"});
		
		childList = new ArrayList<ArrayList<HotelGetCityData>>();
		
		
		for(int i = 0; i < groupList.size(); i ++) {
			ArrayList<HotelGetCityData> cityDatas = new ArrayList<HotelGetCityData>();
			HotelGetCityData cityData = null;
			cityData = new HotelGetCityData();
			cityData.cityNameCh = "";
			cityData.cityCode = "";
			cityData.cityId = groupList.get(i);//这里初始化设为abc..开头字母
			cityDatas.add(cityData);
			
			childList.add(cityDatas);
		}
		
		
	}
	
	private void initViews (View rootView) {
		
//		airticket_start_city = (Button)rootView.findViewById(R.id.airticket_start_city);
//		airticket_end_city = (Button)rootView.findViewById(R.id.airticket_end_city);
//		
//		airticket_start_city.setOnClickListener(this);
//		airticket_end_city.setOnClickListener(this);
//		
//		((ViewGroup)airticket_start_city.getParent()).setSelected(true);
		
		expandableListView = (ExpandableListView)rootView.findViewById(R.id.expandablelist);
		expandableListView.setOnGroupClickListener(new OnGroupClickListener() {
			
			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				if( hotelCityAdapter == null || childList.size()==0) return false;
				
				ArrayList<HotelGetCityData> cityDataList = childList.get(groupPosition);
				if(cityDataList == null || (cityDataList != null && cityDataList.size()==0)) {
					PromptUtil.showToast(getActivity(), "暂未查找到对应的城市");
					return false;
				}
				
				HotelGetCityData cityData = cityDataList.get(0);
				
				if(cityData==null || cityData.cityId ==null || cityData.cityNameCh ==null){
					return false;
				}
				
				String cityId = cityData.cityId;
				if(cityData.cityNameCh.equals("") && !cityId.equals("")) {
					groupIndex = cityId;
					runAsyncTask (groupIndex,"", false);
				}
				
				return false;
			}
		});
		
		expandableListView.setOnChildClickListener(new OnChildClickListener() {
			
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				if( hotelCityAdapter == null ) return false;
				
				HotelGetCityData cityData = (HotelGetCityData)hotelCityAdapter.getChild(groupPosition, childPosition);
				if(cityData.cityNameCh != null && !cityData.equals("")) {
					((UIManagerActivity)getActivity()).hotelCityData = cityData;
					removeFragmentToStack();
					
				}
				
				return false;
			}
		});
		
//		headView = LayoutInflater.from(getActivity()).inflate(R.layout.search_layout, expandableListView, false);
//		search_city = (EditText)headView.findViewById(R.id.search_city);
		search_city = (EditText)rootView.findViewById(R.id.search_city);
		search_city.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(s.toString().length() >= 1) {//自动执行搜索
//					isSearch = true;
					groupIndex = "搜索结果";
					char firstLetter = s.toString().charAt(0);
					if((firstLetter>='a' && firstLetter<='z')
						|| (firstLetter>='A' && firstLetter<='Z')){
						if(firstLetter>='a' && firstLetter<='z'){
							firstLetter-='a'-'A';
						}
						runAsyncTask(firstLetter+"", "", true);
					}else{
						runAsyncTask("", s.toString(), true);
					}
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				
			}
		});
		
//		expandableListView.addHeaderView(headView);
	}
	
	
	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.airticket_start_city:
//			airticket_start_city.setSelected(true);
//			airticket_end_city.setSelected(false);
			((ViewGroup)airticket_start_city.getParent()).setSelected(true);
			((ViewGroup)airticket_end_city.getParent()).setSelected(false);
//			((UIManagerActivity)getActivity()).testData = "15812345678";
//			((UIManagerActivity)getActivity()).removeFragmentToStack();
			break;
			
		case R.id.airticket_end_city:
//			airticket_start_city.setSelected(false);
//			airticket_end_city.setSelected(true);
			((ViewGroup)airticket_start_city.getParent()).setSelected(false);
			((ViewGroup)airticket_end_city.getParent()).setSelected(true);
//			((UIManagerActivity)getActivity()).testData = "15812345678";
//			((UIManagerActivity)getActivity()).removeFragmentToStack();
			break;

		default:
			break;
		}
		
	}
	
	private void updateList(ArrayList<HotelGetCityData> list) {
		int groupPos = -1;
//		if(isSearch) {
//			groupList.add(0, "搜索结果");
//			childList.add(0,list);
//			
////			childList.set(0, list);
////			groupList.set(0, "搜索结果");
////			childList.set(0, list);
//		}else{
			for(int i = 0; i < groupList.size(); i ++) {
				
				if(groupIndex.equals("") || groupList.get(i).equals(groupIndex)) {
					childList.set(i, list);
					groupPos = i;
					break;
				}
			}
//		}
		
		hotelCityAdapter = new HotelCityAdapter(getActivity(), groupList, childList);
		expandableListView.setAdapter(hotelCityAdapter);
		
		if(!groupIndex.equals("")) {
			expandableListView.setSelectedGroup(groupPos);
		}
		expandableListView.expandGroup(groupPos);
//		isSearch = false;
		
		ArrayList<HotelGetCityData> cityDataList=null;
		if(groupPos != -1 ){
			cityDataList = childList.get(groupPos);
		}
		if(groupPos == -1 || cityDataList == null || (cityDataList != null && cityDataList.size()==0)) {
			PromptUtil.showToast(getActivity(), "暂未查找到对应的城市");
		}
		
	}
	
	@Override
	public void onSuccess(Object obj, Class cla) {
		netData=(ArrayList<HotelGetCityData>) obj;
		if(netData!=null){
			updateList(netData);
		}
	}
	

}
