package com.inter.trade.ui.fragment.airticket.address;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;

import com.inter.trade.R;
import com.inter.trade.adapter.AirTicketCityAdapter;
import com.inter.trade.data.CommonData;
import com.inter.trade.ui.fragment.airticket.address.task.AirTicketCityListTask;
import com.inter.trade.ui.fragment.airticket.util.ApiAirticketGetCityData;
import com.inter.trade.ui.fragment.airticket.util.ApiAirticketGetCityParser;
import com.inter.trade.ui.fragment.waterelectricgas.data.ResponseStateListener;
import com.inter.trade.ui.manager.IBaseFragment;
import com.inter.trade.ui.manager.UIManagerActivity;
import com.inter.trade.util.PromptUtil;

/**
 * 飞机票 其他 Fragment
 * @author zhichao.huang
 *
 */
public class AirTicketExpandableListViewCityFragment extends IBaseFragment implements OnClickListener, ResponseStateListener{
	
	private static final String TAG = AirTicketExpandableListViewCityFragment.class.getName();
	
	private View rootView;
	
	private Button airticket_start_city;//出发城市
	private Button airticket_end_city;//到达城市
	
	private Bundle data = null;
	
	private ExpandableListView expandableListView;
	
	private AirTicketCityListTask task;
	
	/**网络获取的数据*/
	private ArrayList<ApiAirticketGetCityData> netData;
	
	/**
	 * 字母列表，如A、B、C……
	 */
	private List<String> groupList = null;
	/**
	 * 点击某个字母展开的列表城市
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
	
	private OnClickListener onRightButtonClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if(airticket_start_city!=null && airticket_start_city.getHint().toString().equals("") 
					&& airticket_end_city!=null && airticket_end_city.getHint().toString().equals("")) {
				PromptUtil.showToast(getActivity(), "信息没有填写完整");
				return;
			}
			if(airticket_start_city.getHint().toString().equals(airticket_end_city.getHint().toString())) {
				PromptUtil.showToast(getActivity(), "出发城市和到达城市不能相同");
				return;
			}
			
			if(((UIManagerActivity)getActivity()).danOrFan == 0) {
				((UIManagerActivity)getActivity()).dancheng_start_data = danchengStartData;
				((UIManagerActivity)getActivity()).dancheng_end_data = danchengEndData;
			} else {
				((UIManagerActivity)getActivity()).wangfan_start_data = wangfanStartData;
				((UIManagerActivity)getActivity()).wangfan_end_data = wangfanEndData;
			}
			
			
			
			removeFragmentToStack();
		}
	};
	
	public static AirTicketExpandableListViewCityFragment newInstance (Bundle data) {
		AirTicketExpandableListViewCityFragment fragment = new AirTicketExpandableListViewCityFragment();
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
		rootView = inflater.inflate(R.layout.airticket_elistview_city_main, container,false);
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
	
	@Override
	public void onPause() {
		super.onPause();
		hideSoftInput(rootView.getWindowToken());
	}
	
	
	/**
	 * 设置顶部栏数据（标题、更多）
	 */
	private void setTitleBar() {
		((UIManagerActivity)getActivity()).setTopTitle("城市选择");
		((UIManagerActivity)getActivity()).setRightButtonOnClickListener("确认", View.VISIBLE, onRightButtonClickListener);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		((UIManagerActivity)getActivity()).setRightButtonOnClickListener("确认", View.GONE, onRightButtonClickListener);
	}
	
	/**
	 * 异步请求
	 * @param firstLetter 城市名拼音首字母,可为空，如A、B、C等。为空时，默认返回热门城市
	 * @param cityName 用户输入搜索的城市名,可为空，支持模糊搜索，如“上海”，“上”等
	 * @param isbackground
	 */
	private void runAsyncTask (String firstLetter, String cityName , boolean isbackground) {
		ApiAirticketGetCityParser myParser = new ApiAirticketGetCityParser();
		CommonData mData = new CommonData();
		mData.putValue("firstLetter", !firstLetter.equals("") ? firstLetter : "" );
		mData.putValue("cityName", !cityName.equals("") ? cityName : "");
		
		task = new AirTicketCityListTask(getActivity(), myParser, mData, isbackground, this);
		task.execute("ApiAirticket","getCity");
	}
	
	private String selecteCity = "";//选中的城市
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
		
		
		if(((UIManagerActivity)getActivity()).danOrFan == 0) {
			danchengStartData = ((UIManagerActivity)getActivity()).dancheng_start_data ;
			danchengEndData = ((UIManagerActivity)getActivity()).dancheng_end_data ;
			
			if(data != null) {
				selecteCity = data.getString("city");
				if(selecteCity.equals("end")) {
					selecteEndTity ();
				} else {
					selecteStartTity ();
				}
			}
			
			if(danchengStartData != null) {
				airticket_start_city.setText(danchengStartData.cityNameCh);
				airticket_start_city.setHint(danchengStartData.cityNameCh);
			}
			
			if(danchengEndData != null) {
				airticket_end_city.setText(danchengEndData.cityNameCh);
				airticket_end_city.setHint(danchengEndData.cityNameCh);
			}
			
			
		} else {
			wangfanStartData = ((UIManagerActivity)getActivity()).wangfan_start_data;
			wangfanEndData = ((UIManagerActivity)getActivity()).wangfan_end_data;
			
			if(data != null) {
				selecteCity = data.getString("city");
				if(selecteCity.equals("end")) {
					selecteEndTity ();
				} else {
					selecteStartTity ();
				}
			}
			
			if(wangfanStartData != null) {
				airticket_start_city.setText(wangfanStartData.cityNameCh);
				airticket_start_city.setHint(wangfanStartData.cityNameCh);
			}
			
			if(wangfanEndData != null) {
				airticket_end_city.setText(wangfanEndData.cityNameCh);
				airticket_end_city.setHint(wangfanEndData.cityNameCh);
			}
		}
		
		
		
	}
	
	/**
	 * 单程出发城市数据
	 */
	private ApiAirticketGetCityData danchengStartData;//
	/**
	 * 单程到达城市数据
	 */
	private ApiAirticketGetCityData danchengEndData;
	
	/**
	 * 往返出发城市数据
	 */
	private ApiAirticketGetCityData wangfanStartData;
	/**
	 * 往返到达城市数据
	 */
	private ApiAirticketGetCityData wangfanEndData;
	
	/**
	 * 选中出发城市
	 */
	private void selecteStartTity () {
		((ViewGroup)airticket_start_city.getParent()).setSelected(true);
		((ViewGroup)airticket_end_city.getParent()).setSelected(false);
	}
	
	/**
	 * 选中到达城市
	 */
	private void selecteEndTity () {
		((ViewGroup)airticket_start_city.getParent()).setSelected(false);
		((ViewGroup)airticket_end_city.getParent()).setSelected(true);
	}
	
	private void initViews (View rootView) {
		
		airticket_start_city = (Button)rootView.findViewById(R.id.airticket_start_city);
		airticket_end_city = (Button)rootView.findViewById(R.id.airticket_end_city);
		
		airticket_start_city.setOnClickListener(this);
		airticket_end_city.setOnClickListener(this);
		
//		airticket_start_city.setSelected(true);
		((ViewGroup)airticket_start_city.getParent()).setSelected(true);
		
		expandableListView = (ExpandableListView)rootView.findViewById(R.id.expandablelist);
		expandableListView.setOnGroupClickListener(new OnGroupClickListener() {
			
			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				if( airTicketCityAdapter == null || childList == null) return false;
				
				ArrayList<ApiAirticketGetCityData> groupCityData =  childList.get(groupPosition);
				if(groupCityData == null || groupCityData.size() <=0) {
					return false;
				}
				
				ApiAirticketGetCityData cityData = (ApiAirticketGetCityData)airTicketCityAdapter.getChild(groupPosition, 0);
				if(cityData != null && cityData.cityNameCh == null) {
//					PromptUtil.showToast(getActivity(), "暂未查找到对应的城市");
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
				if( airTicketCityAdapter == null ) return false;
				
				ApiAirticketGetCityData cityData = (ApiAirticketGetCityData)airTicketCityAdapter.getChild(groupPosition, childPosition);
				if(cityData.cityNameCh != null && !cityData.equals("")) {
					if(airticket_start_city.isSelected()) {
						airticket_start_city.setText(cityData.cityNameCh);
						airticket_start_city.setHint(cityData.cityNameCh);
//						airticket_start_city.setSelected(false);
//						airticket_end_city.setSelected(true);
						
						selecteEndTity ();
						
						if(((UIManagerActivity)getActivity()).danOrFan == 0) {
//							((UIManagerActivity)getActivity()).dancheng_start_data = cityData;
							danchengStartData = cityData;
						}else{
//							((UIManagerActivity)getActivity()).wangfan_start_data = cityData;
							wangfanStartData = cityData;
						}
						
					}else{
						airticket_end_city.setText(cityData.cityNameCh);
						airticket_end_city.setHint(cityData.cityNameCh);
						
						if(((UIManagerActivity)getActivity()).danOrFan == 0){
//							((UIManagerActivity)getActivity()).dancheng_end_data = cityData;
							danchengEndData = cityData;
							
						}else{
//							((UIManagerActivity)getActivity()).wangfan_end_data = cityData;
							wangfanEndData = cityData;
						}
					}
					
//					resultData(cityData);
				}
				
				return false;
			}
		});
		
		headView = LayoutInflater.from(getActivity()).inflate(R.layout.search_layout, expandableListView, false);
		search_city = (EditText)headView.findViewById(R.id.search_city);
		search_city.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(s.toString().length() >= 1) {//自动执行搜索
					isSearch = true;
					groupIndex = "搜索结果";
					runAsyncTask("", s.toString(), true);
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
		
		expandableListView.addHeaderView(headView);
	}
	
//	/**
//	 * 结果回调数据
//	 * @param cityData
//	 */
//	private void resultData (ApiAirticketGetCityData cityData) {
//		if(((UIManagerActivity)getActivity()).danOrFan == 0) {
//			if(city.equals("start")) {
//				((UIManagerActivity)getActivity()).dancheng_start_data = cityData;
//			}else if(city.equals("end")) {
//				((UIManagerActivity)getActivity()).dancheng_end_data = cityData;
//			}
//			
//		}else{
//			if(city.equals("start")) {
//				((UIManagerActivity)getActivity()).wangfan_start_data = cityData;
//			}else if(city.equals("end")) {
//				((UIManagerActivity)getActivity()).wangfan_end_data = cityData;
//			}
//		}
//	}
	
	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.airticket_start_city:
			selecteStartTity();

			break;
			
		case R.id.airticket_end_city:
			selecteEndTity ();
			break;

		default:
			break;
		}
		
	}
	
	private void updateList(ArrayList<ApiAirticketGetCityData> list) {
		int groupPos = 0;
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
		
		
		
		airTicketCityAdapter = new AirTicketCityAdapter(getActivity(), groupList, childList);
		expandableListView.setAdapter(airTicketCityAdapter);
		
		if(!groupIndex.equals("")) {
			if(isSearch) {
				isSearch = false;
			} else {
				expandableListView.setSelectedGroup(groupPos);
			}
			
//			expandableListView.setSelectedChild(groupPos, 0, true);
		}
		
		expandableListView.expandGroup(groupPos);
//		isSearch = false;
	}
	
	@Override
	public void onSuccess(Object obj, Class cla) {
		netData=(ArrayList<ApiAirticketGetCityData>) obj;
		if(netData!=null){
			updateList(netData);
		}
		
	}
	
	/**
	 * 隐藏键盘
	 * @param token
	 */
	private void hideSoftInput(IBinder token)  
   	{  
   	    if (token != null)  
   	    {  
   	        InputMethodManager im = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);  
   	        im.hideSoftInputFromWindow(token,  
   	                InputMethodManager.HIDE_NOT_ALWAYS);  
   	    }  
   	} 

}
