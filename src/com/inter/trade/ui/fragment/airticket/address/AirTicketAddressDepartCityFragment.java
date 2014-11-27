package com.inter.trade.ui.fragment.airticket.address;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.inter.trade.R;
import com.inter.trade.adapter.GameRechargeGameAdapter;
import com.inter.trade.ui.fragment.BaseFragment;
import com.inter.trade.ui.fragment.airticket.address.task.AirTicketCityListTask;
import com.inter.trade.ui.fragment.airticket.util.ApiAirticketGetCityData;
import com.inter.trade.ui.fragment.waterelectricgas.data.ResponseStateListener;
import com.inter.trade.ui.manager.UIManagerActivity;
import com.inter.trade.view.sortview.CharacterParser;
import com.inter.trade.view.sortview.PinyinComparator;
import com.inter.trade.view.sortview.SideBar;
import com.inter.trade.view.sortview.SideBar.OnTouchingLetterChangedListener;
import com.inter.trade.view.sortview.SortAdapter;
import com.inter.trade.view.sortview.SortModel;

/**
 * 飞机票城市选择 出发城市Fragment
 * @author Lihaifeng
 *
 */
public class AirTicketAddressDepartCityFragment extends BaseFragment implements OnItemClickListener
 	,ResponseStateListener	{
	private ListView mListView;
	/**右侧的a-z*/
	private SideBar sideBar;
	/**显示A-Z的弹窗*/
	private TextView dialog;
	
	private SortAdapter adapter;
	
	/**
	 * 汉字转换成拼音的类
	 */
	private CharacterParser characterParser;
	private List<SortModel> SourceDateList;
	
	/**
	 * 根据拼音来排列ListView里面的数据类
	 */
	private PinyinComparator pinyinComparator;
	
	private GameRechargeGameAdapter mAdapter;
	
	/**网络获取的数据*/
	private ArrayList<ApiAirticketGetCityData> netData;
	
	private AirTicketCityListTask task;
	
	/***标记是否第一次新建*/
	private boolean isFirst=true;
	
	private Bundle data = null;
	
	/**
	 * 城市：判断是出发还是到达城市
	 */
	private String city;
	
	public static AirTicketAddressDepartCityFragment newInstance (Bundle data) {
		AirTicketAddressDepartCityFragment fragment = new AirTicketAddressDepartCityFragment();
		fragment.setArguments(data);
		return fragment;
	}
	
	public AirTicketAddressDepartCityFragment(){}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = getArguments();
		if(bundle != null) {
			data = bundle;
			city = data.getString("city");
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.game_recharge_game_list, container,false);
		initView(view);
		
		if(isFirst){
			task=new AirTicketCityListTask(getActivity(), this);
			task.execute();
			isFirst=false;
		}else {
			if(netData!=null){
				updateCompanyList(netData);
			}
		}
		
		return view;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if(task!=null){
			task.cancel(true);
		}
	}
	
	private void initView(View view){
		mListView = (ListView) view.findViewById(R.id.lv_company);
		sideBar = (SideBar) view.findViewById(R.id.sidrbar);
		dialog = (TextView) view.findViewById(R.id.dialog);
		
	    mListView.setOnItemClickListener(this);
	    
	    characterParser=CharacterParser.getInstance();
	    pinyinComparator = new PinyinComparator();
	    sideBar.setTextView(dialog);
	    
	 	//设置右侧触摸监听
	 	sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {
	 				
	 				@Override
	 				public void onTouchingLetterChanged(String s) {
	 					//该字母首次出现的位置
	 					int position = adapter.getPositionForSection(s.charAt(0));
	 					if(position != -1){
	 						mListView.setSelection(position);
	 					}
	 					
	 				}
	 			});
	}
	
	private void updateCompanyList(ArrayList<ApiAirticketGetCityData> list) {
		ArrayList<String> mArrayList = new ArrayList<String>();
		for(ApiAirticketGetCityData data:list){
			mArrayList.add(data.getCityNameCh());
		}
		SourceDateList = filledData(mArrayList);
	    
	 // 根据a-z进行排序源数据
	 	Collections.sort(SourceDateList, pinyinComparator);
	 	adapter = new SortAdapter(getActivity(), SourceDateList);
	 	mListView.setAdapter(adapter);
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		
		String cityName = SourceDateList.get(position).getName();
		ApiAirticketGetCityData data = null;
		
		for(int i=0;i<netData.size();i++){
			data = netData.get(i);
			if(cityName.equals(data.getCityNameCh())){
				break;
			}
		}
		
		if(((UIManagerActivity)getActivity()).danOrFan == 0) {
			if(city.equals("start")) {
				((UIManagerActivity)getActivity()).dancheng_start_data = data;
			}else if(city.equals("end")) {
				((UIManagerActivity)getActivity()).dancheng_end_data = data;
			}
			
		}else{
			if(city.equals("start")) {
				((UIManagerActivity)getActivity()).dancheng_start_data = data;
			}else if(city.equals("end")) {
				((UIManagerActivity)getActivity()).dancheng_end_data = data;
			}
		}
		
		((UIManagerActivity)getActivity()).removeFragmentToStack();
		
		
//		Bundle bundle=new Bundle();
//		bundle.putSerializable("data", gameListData);
//		
//		Intent intent=new Intent(getActivity(),GameRechargeSelectActivity.class);
//		intent.putExtra("gameSelect", bundle);
//		startActivity(intent);
	}
	
	/**
	 * 为ListView填充数据
	 * @param data
	 * @return
	 */
	private List<SortModel> filledData(ArrayList<String> data){
		List<SortModel> mSortList = new ArrayList<SortModel>();
		
		for(int i=0; i<data.size(); i++){
			SortModel sortModel = new SortModel();
			sortModel.setName(data.get(i));
			String sortString  = null;
			if(data.get(i).equals("重庆")) {//多音字处理
				sortString = "C";
			}else {
				//汉字转换成拼音
				String pinyin = characterParser.getSelling(data.get(i));
				sortString = pinyin.substring(0, 1).toUpperCase();
			}
			
			// 正则表达式，判断首字母是否是英文字母
			if(sortString.matches("[A-Z]")){
				sortModel.setSortLetters(sortString.toUpperCase());
			}else{
				sortModel.setSortLetters("#");
			}
			
			mSortList.add(sortModel);
		}
		return mSortList;
	}

	@Override
	public void onSuccess(Object obj, Class cla) {
		netData=(ArrayList<ApiAirticketGetCityData>) obj;
		if(netData!=null){
			updateCompanyList(netData);
		}
	}
}
