package com.inter.trade.ui.fragment.hotel;


import java.lang.reflect.Field;
import java.util.ArrayList;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.inter.trade.AsyncLoadWork;
import com.inter.trade.R;
import com.inter.trade.AsyncLoadWork.AsyncLoadWorkListener;
import com.inter.trade.data.CommonData;
import com.inter.trade.data.MyBankListData;
import com.inter.trade.log.Logger;
import com.inter.trade.ui.HotelSelectPriceActivity;
import com.inter.trade.ui.HotelSelectStarLevelActivity;
import com.inter.trade.ui.fragment.hotel.adapter.HotelListAdapter;
import com.inter.trade.ui.fragment.hotel.adapter.HotelListKeywordAdapter;
import com.inter.trade.ui.fragment.hotel.adapter.HotelListKeywordDetailAdapter;
import com.inter.trade.ui.fragment.hotel.data.HotelGetCityData;
import com.inter.trade.ui.fragment.hotel.data.HotelKeywordData;
import com.inter.trade.ui.fragment.hotel.data.HotelListData;
import com.inter.trade.ui.fragment.hotel.task.HotelKeywordTask;
import com.inter.trade.ui.fragment.hotel.util.HotelKeywordParser;
import com.inter.trade.ui.fragment.hotel.util.HotelListParser;
import com.inter.trade.ui.fragment.hotel.util.HotelUtils;
import com.inter.trade.ui.fragment.transfer.TransferFragment.TransferType;
import com.inter.trade.ui.fragment.waterelectricgas.data.ResponseStateListener;
import com.inter.trade.ui.func.TwoFunctionFragment;
import com.inter.trade.ui.func.childfragment.BankFragment;
import com.inter.trade.ui.func.childfragment.FavourFragment;
import com.inter.trade.ui.func.childfragment.MoreFragment;
import com.inter.trade.ui.manager.IBaseFragment;
import com.inter.trade.ui.manager.UIManagerActivity;
import com.inter.trade.ui.manager.core.IMainHandlerManager;
import com.inter.trade.ui.manager.core.UIConstantDefault;
import com.inter.trade.ui.views.wheelwidget.WheelView;
import com.inter.trade.ui.views.wheelwidget.adapters.ArrayWheelAdapter;

/**
 * 酒店预订 酒店列表
 * @author haifengli
 *
 */
public class HotelListFragment extends IBaseFragment implements OnClickListener, OnCheckedChangeListener,OnItemClickListener{
	
	private static final String TAG = HotelListFragment.class.getName();
	
	private View rootView;
	
	private ListView lv_hotel_list;
	private HotelListAdapter mListAdapter;
	private ArrayList<HotelListData> mHotelListDatas;
	
	private ArrayList<String> mKeywordList;
	private ArrayList<ArrayList<HotelKeywordData>> mKeywordItemList;
	private ArrayList<HotelKeywordData> mKeywordItem;
	private HotelListKeywordAdapter mListKeywordAdapter;
	private HotelListKeywordDetailAdapter mListKeywordDetailAdapter;
	private LinearLayout linearLayoutKeyword;
	private LinearLayout linearLayoutPrice;
	private LinearLayout linearLayoutStar;
	private ListView keywordListview1;
	private ListView keywordListview2;
	private PopupWindow popupWindow;
	
	private String[] mPriceArray;
	private String[] mStarLevelArray;
	
	private Bundle data = null;
	
	private LinearLayout rgNavigation;
	private Button rb_area;
	private Button rb_price;
	private Button rb_star;
	private static int selected = 2;
	
	/*
	 * 酒店预订 当前页面ID
	 */
	private int mModuleID = UIConstantDefault.UI_CONSTANT_HOTEL_LIST;
	
	/*
	 * 酒店预订 日期
	 */
	private String hotelDate = null;
	
	/*
	 * 酒店预订 城市
	 */
	private String hotelCity = null;
	private HotelGetCityData hotelCityData = null;
	
	/*
	 * 酒店预订 价格
	 */
	private String hotelPrice = null;
	
	/*
	 * 酒店预订 星级
	 */
	private String hotelStarLevel = null;
	
	/*
	 * 酒店预订 关键字
	 */
	private String hotelKeyWord = null;
	
	/*
	 * 酒店预订 关键字类别
	 */
	public int hotelKeyType = 0;
	
	/////////////////////
	private AlertDialog dlg;
	private TextView cancel_tv;
	private TextView ok_tv;
	private Button star_nolimit;
	private Button star_kuaijie;
	private Button star_level2;
	private Button star_level3;
	private Button star_level4;
	private Button star_level5;
	
	private int mStarLevel=0;
	private int mStarLevelOld=mStarLevel;
	private String strarLevelArrays[];
	////////////////////
	private WheelView price;
	Bundle bundle;
	private int mPrice;
	private String priceArrays[]={"价格由高到低", "价格由低到高"};
	private AsyncLoadWork<HotelListData> asyncHotelListTask = null;
	
	private HotelKeywordTask keywordTask;
	/**网络获取的数据*/
	private ArrayList<HotelKeywordData> netData;
	private int groupIndex;
	
	public static HotelListFragment newInstance (Bundle data) {
		HotelListFragment fragment = new HotelListFragment();
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
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("selected", selected);
		System.out.println("hotellist fragment保存了数据" + selected);
	}
	
	@Override
	protected View onInitView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.hotel_list_layout, container,false);
		initViews(rootView);
		if (savedInstanceState != null) {
			selected = savedInstanceState.getInt("selected");
			System.out.println("hotellist fragment获取了数据" + selected);
		}
		return rootView;
	}

	@Override
	protected void onAsyncLoadData() {
		Logger.d(TAG, "onAsyncLoadData");
		if(mListAdapter == null){
			HotelListTask();
		}
	}

	@Override
	public void onRefreshDatas() {
		Logger.d(TAG, "onRefreshDatas");
		mModuleID = UIConstantDefault.UI_CONSTANT_HOTEL_LIST;
		((UIManagerActivity)getActivity()).setTopTitle("酒店列表");
//		if(mListAdapter != null){
//			mListAdapter.notifyDataSetChanged();
//		}
		
//		if(mListAdapter == null){
//			HotelListTask();
//		}
	}

	private void initViews (View rootView) {
		Logger.d("hotellist initViews", "start");
		ArrayList<HotelListData> mHotelListDatas = new ArrayList<HotelListData>();
		
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
//		mHotelListDatas=mListDatas;
		
		rgNavigation = (LinearLayout) rootView.findViewById(R.id.rg_navigation);
		rb_area = (Button) rootView.findViewById(R.id.rb_area);
		rb_price = (Button) rootView.findViewById(R.id.rb_price);
		rb_star = (Button) rootView.findViewById(R.id.rb_star);
		rb_area.setOnClickListener(this);
		rb_price.setOnClickListener(this);
		rb_star.setOnClickListener(this);
		rb_area.setSelected(false);
		rb_price.setSelected(false);
		rb_star.setSelected(true);
		selected = 2;
		
//		rgNavigation.setOnCheckedChangeListener(HotelListFragment.this);
//		rgNavigation.setSelected(true);
//		rgNavigation.getChildAt(selected).performClick();
		
		lv_hotel_list = (ListView)rootView.findViewById(R.id.lv_hotel_list);
//		mListAdapter = new HotelListAdapter(getActivity(), mHotelListDatas);
//		lv_hotel_list.setAdapter(mListAdapter);
		lv_hotel_list.setOnItemClickListener(this);
		Logger.d("hotellist initViews", "end");
	}
	
	/**
	 *  获取酒店列表Task
	 */
	private void HotelListTask() {
		HotelListParser netParser = new HotelListParser();
		
		hotelCityData = ((UIManagerActivity)getActivity()).hotelCityData;
		String cityId="";
		if( hotelCityData != null ) {
			cityId = hotelCityData.getCityId();
		}
		String price = ((UIManagerActivity)getActivity()).priceId+"";
		String star  = ((UIManagerActivity)getActivity()).starId+"";
		
		CommonData requsetData = new CommonData();
		requsetData.putValue("cityId",cityId==null ? "": cityId);
		requsetData.putValue("priceRange",price==null ? "": price);
		requsetData.putValue("starRate",star==null ? "": star);
//		requsetData.putValue("start",start);
//		requsetData.putValue("count",count);
		
		String keyTypeId = "";
		int keytype = ((UIManagerActivity)getActivity()).hotelKeyType;
		switch(keytype){
			case 0:
				keyTypeId = "zoneId";
				break;
			case 1:
				keyTypeId = "districtId";
				break;
			case 2:
				keyTypeId = "brandId";
				break;
			case 3:
				keyTypeId = "themeId";
				break;
			case 4:
				keyTypeId = "locationId";
				break;
		}
		
		HotelKeywordData KeyWord = ((UIManagerActivity)getActivity()).hotelKeyWord;
		if(!keyTypeId.equals("") && KeyWord != null){
			requsetData.putValue(keyTypeId, KeyWord.getCityId()==null ? "": KeyWord.getCityId());
		}
		
		asyncHotelListTask = new AsyncLoadWork<HotelListData>(getActivity(), netParser, requsetData, new AsyncLoadWorkListener() {

			@Override
			public void onSuccess(Object protocolDataList, Bundle bundle) {
					mHotelListDatas = (ArrayList<HotelListData>)protocolDataList;
					mListAdapter = new HotelListAdapter(getActivity(), mHotelListDatas);
					lv_hotel_list.setAdapter(mListAdapter);
//					mListAdapter.notifyDataSetChanged();
			}

			@Override
			public void onFailure(String error) {
			}
			
		}, false, true);
		
		asyncHotelListTask.execute("ApiHotel", "GetHotelList");
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id){
		if(mHotelListDatas.size() == 0 || position<0){
			return;
		}
		
		HotelListData data = mHotelListDatas.get(position);
		
		String name = data.getHotelName();
		if(name != null){
			Bundle dataBundle = new Bundle();
			dataBundle.putSerializable("hotelDetail", data);
			IMainHandlerManager.handlerUI(UIConstantDefault.UI_CONSTANT_HOTEL_DETAIL, 1, dataBundle);
		}
	}
	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rb_area:// 区域
			rb_area.setSelected(true);
			rb_price.setSelected(false);
			rb_star.setSelected(false);
			selected = 0;
			showPopupWindow(selected);
			break;
		case R.id.rb_price:// 价格
			rb_area.setSelected(false);
			rb_price.setSelected(true);
			rb_star.setSelected(false);
			selected = 1;
			showPopupWindow(selected);
			break;
		case R.id.rb_star:// 星级
			rb_area.setSelected(false);
			rb_price.setSelected(false);
			rb_star.setSelected(true);
			selected = 2;
			showPopupWindow(selected);
			break;
		default:
			break;
		}
		
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.rb_area:// 区域
			selected = 0;
//			hotelFilterDialogArea();
			showPopupWindow(selected);
			break;
		case R.id.rb_price:// 价格
			selected = 1;
//			hotelFilterDialog();
			showPopupWindow(selected);
			break;
		case R.id.rb_star:// 星级
			selected = 2;
//			hotelFilterDialog();
			showPopupWindow(selected);
			break;
		default:
			break;
		}
	}
	
	private void showPopupWindow(int mark) {
		
	    if(mark==0){
	    	if(linearLayoutKeyword == null) {
	    		linearLayoutKeyword = (LinearLayout) LayoutInflater.from(getActivity()).inflate(
						R.layout.hotel_list_keyword_layout, null);
				keywordFilterInitView(linearLayoutKeyword);
			}
	    }
	    else if(mark==1){
	    	if(linearLayoutPrice == null) {
	    		linearLayoutPrice = (LinearLayout) LayoutInflater.from(getActivity()).inflate(
	    				R.layout.hotel_list_price_layout, null);
	    		priceFilterInitView(linearLayoutPrice);
	    	}
	    }
		else if(mark==2) {
			if(linearLayoutStar == null) {
				linearLayoutStar = (LinearLayout) LayoutInflater.from(getActivity()).inflate(
						R.layout.hotel_list_starlevel_layout, null);
				starFilterInitView(linearLayoutStar);
			}
		}
		
		popupWindow = new PopupWindow(getActivity());
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setWidth(getActivity().getWindowManager().getDefaultDisplay().getWidth());
		popupWindow.setHeight((rootView.getHeight() - rgNavigation.getHeight()));
		popupWindow.setOutsideTouchable(true);
		popupWindow.setFocusable(true);
//		popupWindow.setOnDismissListener(new OnDismissListener() {
//			
//			@Override
//			public void onDismiss() {
//				screen.setSelected(false);
//				screen_company.setSelected(false);
//			}
//		});
		if(mark == 0) {
			popupWindow.setContentView(linearLayoutKeyword);
		} else if(mark == 1) {
			popupWindow.setContentView(linearLayoutPrice);
		} else if(mark == 2) {
			popupWindow.setContentView(linearLayoutStar);
		}
//		popupWindow.setAnimationStyle(R.style.popuStyle);
		popupWindow.showAtLocation(rootView, Gravity.RIGHT
				| Gravity.BOTTOM, 0, rgNavigation.getHeight());//需要指定Gravity，默认情况是center.
	}
	
	public void keywordFilterInitView(View view){
		keywordListview1 = (ListView) view.findViewById(R.id.keywordListview1);
		keywordListview2 = (ListView) view.findViewById(R.id.keywordListview2);
		
		mKeywordList = new ArrayList<String>();
		mKeywordList.add("商业区");
		mKeywordList.add("火车站");
		mKeywordList.add("机场");
		mKeywordList.add("行政区");
		mKeywordList.add("市中心");//4
//		mKeywordList.add("地铁线");//4
		mKeywordList.add("景点");
		keywordListview1.setAdapter(mListKeywordAdapter=new HotelListKeywordAdapter(getActivity(), mKeywordList));
		mListKeywordAdapter.setSelectItem(0);
		
		mKeywordItemList = new ArrayList<ArrayList<HotelKeywordData>>();
		for(int i=0, max=mKeywordList.size(); i<max; i++){
			ArrayList<HotelKeywordData> keywordItemList = new ArrayList<HotelKeywordData>();
			HotelKeywordData data = new HotelKeywordData();
			data.cityNameCh="";
			data.cityId="";
			keywordItemList.add(data);
			mKeywordItemList.add(keywordItemList);
		}
		groupIndex=0;
		keywordListview2.setAdapter(mListKeywordDetailAdapter=new HotelListKeywordDetailAdapter(getActivity(), mKeywordItemList, groupIndex));
		runAsyncTask (groupIndex, false);
		
		keywordListview1.setOnItemClickListener(new OnItemClickListener(){
			@Override	
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {  
	            
				mListKeywordAdapter.setSelectItem(arg2);
				mListKeywordAdapter.notifyDataSetInvalidated();
				
				int max=mKeywordItemList.size();
				if(max>0 && arg2<max && mKeywordItemList.get(arg2).size()>0){
					groupIndex=arg2;
					String item = mKeywordItemList.get(arg2).get(0).getCityNameCh();
					if(item != null && item.equals("")){
						runAsyncTask (groupIndex, false);
					}else if(item != null){
						if(mListKeywordDetailAdapter != null){
							mListKeywordDetailAdapter.setSelectItem(groupIndex);
							mListKeywordDetailAdapter.notifyDataSetChanged();
						}
					}
				}
	        } 
		});
		
		keywordListview2.setOnItemClickListener(new OnItemClickListener(){
			@Override	
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {  
				int max=mKeywordItemList.size();
				if(max>0 && groupIndex<max){
					ArrayList<HotelKeywordData> dataList = mKeywordItemList.get(groupIndex);
					int size=dataList.size();
					if(size>0 && arg2<size){
						HotelKeywordData cityData = dataList.get(arg2);
						if(cityData != null && cityData.getCityId() != null && !cityData.getCityId().equals("")) {
							
							((UIManagerActivity)getActivity()).hotelKeyWord = cityData;
							((UIManagerActivity)getActivity()).hotelKeyType = hotelKeyType;
							
							dismiss();
							HotelListTask();
						}
					}
				}
			} 
		});
		
	}
	
	private void runAsyncTask (int groupPosition, boolean isbackground) {
		HotelKeywordParser myParser = new HotelKeywordParser();
		CommonData mData = new CommonData();
		
		HotelGetCityData hotelCityData = ((UIManagerActivity)getActivity()).hotelCityData;
		if( hotelCityData != null ) {
			String hotelCityId = hotelCityData.getCityId();
			mData.putValue("cityId", hotelCityId != null ? hotelCityId : "" );
		}
		String funcStr="";
		String locationType ="";
		switch(groupPosition){
			case 0:
				hotelKeyType=0;
				funcStr="GetBusinessZone";//商业区
				break;
			case 3:
				hotelKeyType=1;
				funcStr="GetDistrict";//行政区
				break;
			case 1:
			case 2:
			case 4:
			case 5:
				hotelKeyType=4;
				funcStr="GetCityLocation";//地标，不同地标参数，不同地标
				locationType = transferType(groupPosition);
				mData.putValue("locationType", locationType);
				break;
		}
		
		/*
		 * 该关键字，接口暂未实现
		 */
		if("-1" .equals(locationType)){
			if(mListKeywordDetailAdapter != null){
				mListKeywordDetailAdapter.setSelectItem(groupIndex);
				mListKeywordDetailAdapter.notifyDataSetChanged();
			}
			return;
		}
		
		keywordTask = new HotelKeywordTask(getActivity(), myParser, mData, isbackground, new ResponseStateListener(){
			@Override
			public void onSuccess(Object obj, Class cla) {
				netData=(ArrayList<HotelKeywordData>) obj;
				if(netData!=null){
					updateList(netData);
				}
			}
		});
		keywordTask.execute("ApiHotel",funcStr);
	}
	
	/*
	 * 根据文档接口，将keywordListview1 元素的index ID，转换成 接口需要的locationType 地标参数
	 */
	private String transferType(int type){
		int locationType=0;
		switch(type){
		case 1:
			locationType=2;
			break;
		case 2:
			locationType=3;
			break;
		case 4:
			locationType=4;
			break;
		case 5:
			locationType=7;
			break;
		}
		return locationType+"";
	}
	
	private void updateList(ArrayList<HotelKeywordData> list) {
		int groupPos = 0;
		for(int i=0, max=mKeywordList.size(); i<max; i++){
			
			if(groupIndex==i) {
				mKeywordItemList.set(i, list);
				groupPos = i;
				break;
			}
		}
		
		if(mListKeywordDetailAdapter != null){
			mListKeywordDetailAdapter.setSelectItem(groupIndex);
			mListKeywordDetailAdapter.notifyDataSetChanged();
		}
		
	}
	
	public void priceFilterInitView(View view){
		cancel_tv = (TextView) view.findViewById(R.id.cancel_tv);
		ok_tv = (TextView) view.findViewById(R.id.ok_tv);
		
		OnClickListener priceClickListener = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch (v.getId()) {
				case R.id.cancel_tv://取消
					dismiss();
					break;
				case R.id.ok_tv://确定
					getPrice();
					break;
				}
			}
		};
		cancel_tv.setOnClickListener(priceClickListener);
		ok_tv.setOnClickListener(priceClickListener);
		
		price = (WheelView) view.findViewById(R.id.price);

		price.setWheelBackground(R.drawable.wheel_bg_white);// 设置背景的颜色
		price.setShadowColor(0xFFFFFF, 0xFFFFFF, 0xFFFFFF);// 设置上下边缘的颜色值
//		price.setCyclic(true);// 设置是否循环
		
		
//		bundle = getIntent().getBundleExtra("hotelPrice");
//		if(bundle != null){
//			priceArrays = bundle.getStringArray("priceArray");
//		}
		
		if(priceArrays != null){
//			//默认选择中间项
//			int curPrice = (int)(priceArrays.length/2);
			
			//默认选择首项
			int curPrice = 0;
			price.setViewAdapter(new DateArrayAdapter(getActivity(), priceArrays, curPrice));
			price.setCurrentItem(curPrice);
		}
	}
	
	public void getPrice(){
		if(popupWindow != null){
			dismiss();
			mPrice = price.getCurrentItem();
			if(priceArrays != null && mPrice<priceArrays.length){
				HotelListSortPrice(mPrice);
			}
		}
	}
	
	public void HotelListSortPrice(int sortFlag ){
		if(sortFlag==0){//由高到低
			mHotelListDatas = HotelUtils.priceHightToLow(mHotelListDatas);
			if(mListAdapter != null){
				mListAdapter.notifyDataSetChanged();
			}
		}else if(sortFlag==1){//由低到高
			mHotelListDatas = HotelUtils.priceLowToHight(mHotelListDatas);
			if(mListAdapter != null){
				mListAdapter.notifyDataSetChanged();
			}
		}

	}
	
	public void starFilterInitView(View view){
		cancel_tv = (TextView) view.findViewById(R.id.cancel_tv);
		ok_tv = (TextView) view.findViewById(R.id.ok_tv);
		
		star_nolimit = (Button) view.findViewById(R.id.star_nolimit);
		star_kuaijie = (Button) view.findViewById(R.id.star_kuaijie);
		star_level2 = (Button) view.findViewById(R.id.star_level2);
		star_level3 = (Button) view.findViewById(R.id.star_level3);
		star_level4 = (Button) view.findViewById(R.id.star_level4);
		star_level5 = (Button) view.findViewById(R.id.star_level5);
		
		OnClickListener starClickListener = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch (v.getId()) {
				case R.id.cancel_tv://取消
					dismiss();
					break;
				case R.id.ok_tv://确定
					getStarLevel();
					break;
				case R.id.star_nolimit://不限
					mStarLevel=0;
					star_nolimit.setSelected(true);
					selectStarLevel();
					break;
				case R.id.star_kuaijie://快捷连锁
					mStarLevel=1;
					star_kuaijie.setSelected(true);
					selectStarLevel();
					break;
				case R.id.star_level2://二星级以下
					mStarLevel=2;
					star_level2.setSelected(true);
					selectStarLevel();
					break;
				case R.id.star_level3://三星级
					mStarLevel=3;
					star_level3.setSelected(true);
					selectStarLevel();
					break;
				case R.id.star_level4://四星级
					mStarLevel=4;
					star_level4.setSelected(true);
					selectStarLevel();
					break;
				case R.id.star_level5://五星级
					mStarLevel=5;
					star_level5.setSelected(true);
					selectStarLevel();
					break;
				}
			}
		};
		
		cancel_tv.setOnClickListener(starClickListener);
		ok_tv.setOnClickListener(starClickListener);
		star_nolimit.setOnClickListener(starClickListener);
		star_kuaijie.setOnClickListener(starClickListener);
		star_level2.setOnClickListener(starClickListener);
		star_level3.setOnClickListener(starClickListener);
		star_level4.setOnClickListener(starClickListener);
		star_level5.setOnClickListener(starClickListener);

		mStarLevel=0;
		mStarLevelOld=mStarLevel;
		star_nolimit.setSelected(true);
		star_kuaijie.setSelected(false);
		star_level2.setSelected(false);
		star_level3.setSelected(false);
		star_level4.setSelected(false);
		star_level5.setSelected(false);
		
		
		mStarLevelArray = new String[] {"不限", "快捷连锁", "二星级以下", "三星级/舒适", "四星级/高档", "五星级/豪华"};
	}
	
	public void dismiss(){
		if(popupWindow != null){
			popupWindow.dismiss();
			popupWindow = null;
		}
	}
	
	public void getStarLevel(){
		if(popupWindow != null){
			if(strarLevelArrays != null && mStarLevel<strarLevelArrays.length){
				((UIManagerActivity)getActivity()).hotelStarLevel=strarLevelArrays[mStarLevel];
				((UIManagerActivity)getActivity()).starId=mStarLevel;
			}
			dismiss();
			HotelListTask();
		}
	}
	
	public void selectStarLevel(){
		if(mStarLevelOld != mStarLevel){
			switch (mStarLevelOld) {
			case 0://不限
				star_nolimit.setSelected(false);
				break;
			case 1://快捷连锁
				star_kuaijie.setSelected(false);
				break;
			case 2://二星级以下
				star_level2.setSelected(false);
				break;
			case 3://三星级
				star_level3.setSelected(false);
				break;
			case 4://四星级
				star_level4.setSelected(false);
				break;
			case 5://五星级
				star_level5.setSelected(false);
				break;
			}
			mStarLevelOld = mStarLevel;
		}
	}
	
	/**
	 * Adapter for string based wheel. Highlights the current value.
	 */
	private class DateArrayAdapter extends ArrayWheelAdapter<String> {
		// Index of current item
		int currentItem;
		// Index of item to be highlighted
		int currentValue;

		/**
		 * Constructor
		 */
		public DateArrayAdapter(Context context, String[] items, int current) {
			super(context, items);
			this.currentValue = current;
			setTextSize(16);
		}

		@Override
		protected void configureTextView(TextView view) {
			super.configureTextView(view);
//			if (currentItem == currentValue) {
//				view.setTextColor(0xFF0000F0);
//			}
			view.setTypeface(Typeface.SANS_SERIF);
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			currentItem = index;
			return super.getItem(index, cachedView, parent);
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
//		if(mModuleID != UIConstantDefault.UI_CONSTANT_HOTEL_STAR_LEVEL 
//			&& mModuleID != UIConstantDefault.UI_CONSTANT_HOTEL_PRICE){
//			((UIManagerActivity)getActivity()).setRightButtonIconOnClickListener(View.GONE ,null);
//		}
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
	
	@Override
	public void onTimeout() {
		// TODO Auto-generated method stub
		
	}

	
	/**
	 * 当前Fragment
	 */
	private IBaseFragment currentFragment = null;
	
//	private IBaseFragment danFragment = null;
//	private IBaseFragment wangFragment = null;

}
