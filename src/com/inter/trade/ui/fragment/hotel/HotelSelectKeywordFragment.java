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
import com.inter.trade.ui.fragment.hotel.adapter.HotelKeywordAdapter;
import com.inter.trade.ui.fragment.hotel.data.HotelGetCityData;
import com.inter.trade.ui.fragment.hotel.data.HotelKeywordData;
import com.inter.trade.ui.fragment.hotel.task.HotelKeywordTask;
import com.inter.trade.ui.fragment.hotel.util.HotelKeywordParser;
import com.inter.trade.ui.fragment.waterelectricgas.data.ResponseStateListener;
import com.inter.trade.ui.manager.IBaseFragment;
import com.inter.trade.ui.manager.UIManagerActivity;
import com.inter.trade.util.PromptUtil;

/**
 * 酒店预订 选择关键字 Fragment
 * @author haifengli
 *
 */
public class HotelSelectKeywordFragment extends IBaseFragment implements OnClickListener, ResponseStateListener{
	
	private static final String TAG = HotelSelectKeywordFragment.class.getName();
	
	private View rootView;
	
	private Bundle data = null;
	
	private ExpandableListView expandableListView;
	
	private HotelKeywordTask task;
	
	/**网络获取的数据*/
	private ArrayList<HotelKeywordData> netData;
	
	/**
	 * 字母列表，如A、B、C……
	 */
	private List<String> groupList = null;
	/**
	 * 点击某个字母展开的列表关键字
	 */
	private ArrayList<ArrayList<HotelKeywordData>> childList = null;
	
	private String groupIndex = "";
	
	private HotelKeywordAdapter hotelKeywordAdapter ;
	
	/**
	 * ExpandableListView 的 headView
	 */
	private View headView;
	private EditText search_city;
	
	/**
	 * 记录是否是搜索
	 */
	private boolean isSearch = false;
	
	public static HotelSelectKeywordFragment newInstance (Bundle data) {
		HotelSelectKeywordFragment fragment = new HotelSelectKeywordFragment();
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
		rootView = inflater.inflate(R.layout.hotel_elistview_keyword, container,false);
		initViews(rootView);
		return rootView;
	}
	
	@Override
	protected void onAsyncLoadData() {
		initData();
//		runAsyncTask (groupIndex,"", false);
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
		((UIManagerActivity)getActivity()).setTopTitle("关键字");
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}
	
	/**
	 * 异步请求
	 * @param firstLetter 城市名拼音首字母,可为空，如A、B、C等。为空时，默认返回热门城市
	 * @param cityName 用户输入搜索的城市名,可为空，支持模糊搜索，如“上海”，“上”等
	 * @param isbackground
	 */
	private void runAsyncTask (String keyword, int groupPosition , boolean isbackground) {
		HotelKeywordParser myParser = new HotelKeywordParser();
		CommonData mData = new CommonData();
		
		HotelGetCityData hotelCityData = ((UIManagerActivity)getActivity()).hotelCityData;
		if( hotelCityData != null ) {
			String hotelCityId = hotelCityData.getCityId();
			mData.putValue("cityId", hotelCityId != null ? hotelCityId : "" );
		}
		String funcStr="";
		switch(groupPosition){
			case 0:
				funcStr="GetBusinessZone";
				break;
			case 1:
				funcStr="GetDistrict";
				break;
			case 2:
				funcStr="GetHotelBrand";
				break;
			case 3:
				funcStr="GetHotelTheme";
				break;
		}
		task = new HotelKeywordTask(getActivity(), myParser, mData, isbackground, this);
		task.execute("ApiHotel",funcStr);
	}
	
	/**
	 * 初始化数据
	 */
	private void initData() {
//		groupList = Arrays.asList(new String[]{"热门城市","搜索结果","A","B","C","D","E","F","G"
//				,"H","I","J","K","L","M","N","O","P","Q","R","S","T","U"
//				,"V","W","X","Y","Z"});
		
		groupList = Arrays.asList(new String[]{"商业区","行政区", "品牌","主题"});
		
		childList = new ArrayList<ArrayList<HotelKeywordData>>();
		
		
		for(int i = 0; i < groupList.size(); i ++) {
			ArrayList<HotelKeywordData> cityDatas = new ArrayList<HotelKeywordData>();
			HotelKeywordData cityData = null;
			cityData = new HotelKeywordData();
			cityData.cityNameCh = "";
			cityData.cityCode = "";
			cityData.cityId = groupList.get(i);
			cityDatas.add(cityData);
			
			childList.add(cityDatas);
		}
		
		hotelKeywordAdapter = new HotelKeywordAdapter(getActivity(), groupList, childList);
		expandableListView.setAdapter(hotelKeywordAdapter);
	}
	
	private void initViews (View rootView) {
		expandableListView = (ExpandableListView)rootView.findViewById(R.id.expandablelist);
		expandableListView.setOnGroupClickListener(new OnGroupClickListener() {
			
			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				if( hotelKeywordAdapter == null ) return false;
				
//				HotelKeywordData cityData = (HotelKeywordData)hotelKeywordAdapter.getChild(groupPosition, 0);
//				if(cityData != null && cityData.cityNameCh == null) {
//					PromptUtil.showToast(getActivity(), "暂未查找到对应的关键字");
//					return false;
//				}
				
				ArrayList<HotelKeywordData> cityDataList = childList.get(groupPosition);
				if(cityDataList == null || (cityDataList != null && cityDataList.size()==0)) {
					PromptUtil.showToast(getActivity(), "暂未查找到对应的关键字");
					return false;
				}
				
				HotelKeywordData cityData = cityDataList.get(0);
				
				if(cityData==null || cityData.cityId ==null || cityData.cityNameCh ==null){
					return false;
				}
				
				String cityId = cityData.cityId;
				if(cityData.cityNameCh.equals("") && !cityId.equals("")) {
					groupIndex = cityId;
					runAsyncTask (groupIndex,groupPosition, false);
				}
				
//				String keyword = (String) hotelKeywordAdapter.getGroup(groupPosition);
//				if(keyword != null && !keyword.equals("")) {
//					groupIndex = keyword;
//					runAsyncTask (groupIndex,groupPosition, false);
//				}
				
				return false;
			}
		});
		
		expandableListView.setOnChildClickListener(new OnChildClickListener() {
			
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				if( hotelKeywordAdapter == null ) return false;
				
				HotelKeywordData cityData = (HotelKeywordData)hotelKeywordAdapter.getChild(groupPosition, childPosition);
				if(cityData != null && cityData.getCityId() != null && !cityData.getCityId().equals("")) {
					((UIManagerActivity)getActivity()).hotelKeyWord = cityData;
					((UIManagerActivity)getActivity()).hotelKeyType = groupPosition;
					removeFragmentToStack();
				}
				
				return false;
			}
		});
		
//		search_city = (EditText)rootView.findViewById(R.id.search_city);
//		search_city.addTextChangedListener(new TextWatcher() {
//			
//			@Override
//			public void onTextChanged(CharSequence s, int start, int before, int count) {
//				if(s.toString().length() >= 1) {//自动执行搜索
////					isSearch = true;
//					groupIndex = "搜索结果";
//					char firstLetter = s.toString().charAt(0);
//					if((firstLetter>='a' && firstLetter<='z')
//						|| (firstLetter>='A' && firstLetter<='Z')){
//						if(firstLetter>='a' && firstLetter<='z'){
//							firstLetter-='a'-'A';
//						}
//						runAsyncTask(firstLetter+"", "", true);
//					}else{
//						runAsyncTask("", s.toString(), true);
//					}
//				}
//			}
//			
//			@Override
//			public void beforeTextChanged(CharSequence s, int start, int count,
//					int after) {
//				
//			}
//			
//			@Override
//			public void afterTextChanged(Editable s) {
//				
//			}
//		});
		
	}
	
	
	@Override
	public void onClick(View v) {
	}
	
	private void updateList(ArrayList<HotelKeywordData> list) {
		int groupPos = -1;
		for(int i = 0; i < groupList.size(); i ++) {
			
			if(groupIndex.equals("") || groupList.get(i).equals(groupIndex)) {
				childList.set(i, list);
				groupPos = i;
				break;
			}
		}
		
//		hotelKeywordAdapter = new HotelKeywordAdapter(getActivity(), groupList, childList);
//		expandableListView.setAdapter(hotelKeywordAdapter);
		hotelKeywordAdapter.notifyDataSetChanged();
		
		if(!groupIndex.equals("")) {
			expandableListView.setSelectedGroup(groupPos);
		}
		expandableListView.expandGroup(groupPos);
		
		ArrayList<HotelKeywordData> cityDataList=null;
		if(groupPos != -1 ){
			cityDataList = childList.get(groupPos);
		}
		if(groupPos == -1 || cityDataList == null || (cityDataList != null && cityDataList.size()==0)) {
			PromptUtil.showToast(getActivity(), "暂未查找到对应的关键字");
		}
	}
	
	@Override
	public void onSuccess(Object obj, Class cla) {
		netData=(ArrayList<HotelKeywordData>) obj;
		if(netData!=null){
			updateList(netData);
		}
	}
	
}
