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
import android.widget.GridView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;

import com.inter.trade.AsyncLoadWork;
import com.inter.trade.R;
import com.inter.trade.AsyncLoadWork.AsyncLoadWorkListener;
import com.inter.trade.adapter.AirTicketCityAdapter;
import com.inter.trade.data.CommonData;
import com.inter.trade.imageframe.ImageFetcher;
import com.inter.trade.log.Logger;
import com.inter.trade.ui.fragment.airticket.address.task.AirTicketCityListTask;
import com.inter.trade.ui.fragment.airticket.util.ApiAirticketGetCityData;
import com.inter.trade.ui.fragment.airticket.util.ApiAirticketGetCityParser;
import com.inter.trade.ui.fragment.hotel.adapter.HotelHouseTypeAdapter;
import com.inter.trade.ui.fragment.hotel.adapter.HotelListAdapter;
import com.inter.trade.ui.fragment.hotel.adapter.HotelPhotoAdapter;
import com.inter.trade.ui.fragment.hotel.data.HotelImageData;
import com.inter.trade.ui.fragment.hotel.data.HotelListData;
import com.inter.trade.ui.fragment.hotel.data.HotelRoomData;
import com.inter.trade.ui.fragment.hotel.util.HotelImageParser;
import com.inter.trade.ui.fragment.hotel.util.HotelRoomParser;
import com.inter.trade.ui.fragment.waterelectricgas.data.ResponseStateListener;
import com.inter.trade.ui.func.FuncData;
import com.inter.trade.ui.func.childfragment.ChildIndexAdapter;
import com.inter.trade.ui.func.util.MoreFunUtil;
import com.inter.trade.ui.manager.IBaseFragment;
import com.inter.trade.ui.manager.UIManagerActivity;
import com.inter.trade.ui.manager.core.IMainHandlerManager;
import com.inter.trade.ui.manager.core.UIConstantDefault;
import com.inter.trade.util.PromptUtil;

/**
 * 酒店详情 图片  Fragment
 * @author haifengli
 *
 */
public class HotelPhotoFragment extends IBaseFragment implements OnClickListener,OnItemClickListener{
	
	private static final String TAG = HotelPhotoFragment.class.getName();
	
	private View rootView;
	private GridView gridView;
	private Bundle data = null;
	private HotelListData mHotelListData;
	private ListView lv_hotel_list;
	private HotelPhotoAdapter mListAdapter;
//	private ArrayList<HotelListData> mHotelListDatas;
	private ArrayList<HotelImageData> mHotelListDatas;
	private AsyncLoadWork<HotelImageData> asyncHotelImageTask = null;
	
	public static HotelPhotoFragment newInstance (Bundle data) {
		HotelPhotoFragment fragment = new HotelPhotoFragment();
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
		rootView = inflater.inflate(R.layout.hotel_detail_photo_layout, container,false);
		initViews(rootView);
		return rootView;
	}
	
	@Override
	protected void onAsyncLoadData() {
//		initData();
		runAsyncTask ("", false);
	}

	@Override
	public void onRefreshDatas() {
//		setTitleBar();
//		if(mListAdapter != null){
//			mListAdapter.notifyDataSetChanged();
//		}
		
		if(mListAdapter == null){
			runAsyncTask ("", false);
		}
	}
	
	@Override
	public void onTimeout() {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * 设置顶部栏数据（标题、更多）
	 */
	private void setTitleBar() {
		((UIManagerActivity)getActivity()).setTopTitle("图片");
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}
	
	
	/**
	 * 初始化数据
	 */
	private void initData() {
	}
	
	private void initViews (View rootView) {
		
		gridView = (GridView) rootView.findViewById(R.id.func_grid);
//		gridView.setAdapter(mListAdapter=new HotelPhotoAdapter(getActivity(), null,mHotelListDatas=getTestData()));
		gridView.setOnItemClickListener(this);
		
	}
	
	/**
	 * 获取酒店照片
	 * @param firstLetter 城市名拼音首字母,可为空，如A、B、C等。为空时，默认返回热门城市
	 * @param cityName 用户输入搜索的城市名,可为空，支持模糊搜索，如“上海”，“上”等
	 * @param isbackground
	 */
	private void runAsyncTask (String cityName , boolean isbackground) {

		HotelImageParser netParser = new HotelImageParser();
		String hotelCode=null;
		if( mHotelListData != null && mHotelListData.hotelCode != null) {
			hotelCode = mHotelListData.hotelCode;
		}
		
		CommonData requsetData = new CommonData();
		requsetData.putValue("hotelCode",hotelCode==null ? "": hotelCode);
		
		asyncHotelImageTask = new AsyncLoadWork<HotelImageData>(getActivity(), netParser, requsetData, new AsyncLoadWorkListener() {

			@Override
			public void onSuccess(Object protocolDataList, Bundle bundle) {
				mHotelListDatas = (ArrayList<HotelImageData>)protocolDataList;
				if(mHotelListDatas == null || mHotelListDatas.size()==0){
					return;
				}
				Logger.d(TAG, "total photo:"+mHotelListDatas.size());
				gridView.setAdapter(mListAdapter=new HotelPhotoAdapter(getActivity(), null,mHotelListDatas));
			}

			@Override
			public void onFailure(String error) {
			}
			
		}, false, true);
		
		asyncHotelImageTask.execute("ApiHotel", "GetHotelImage");
	
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id){
		Logger.d("HotelPhoto itemclick", "position="+position+", mHotelListDatas.size()="+mHotelListDatas.size());
//		PromptUtil.showToast(getActivity(), "HotelPhoto itemclick, position="+position+", mHotelListDatas.size()="+mHotelListDatas.size());
		
		if(mHotelListDatas.size() == 0 || position<0){
			return;
		}
		
		HotelImageData data = mHotelListDatas.get(position);
		
		String url = data.url;
		if(url != null){
			String[] imageUrls;
			List<String> imageList = new ArrayList<String>();
			int urlSize=mHotelListDatas.size();
			for(int i=0; i<urlSize; i++){
				HotelImageData data2 = mHotelListDatas.get(i);
				String url2 = data2.url;
				if(url2 != null){
					imageList.add(url2);
				}
			}
			if(imageList != null && imageList.size()>0){
				imageUrls=imageList.toArray(new String[imageList.size()]);
				switchFragment(HotelTouchGalleryFragment.create(imageUrls, position), 1);
			}
			
		}
		
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
	
	@Override
	public void onClick(View v) {
	}
	
public static ArrayList<HotelImageData> getTestData(){
		
		ArrayList<HotelImageData> fList=new ArrayList<HotelImageData>();
		
		for(int i=0; i<20; i++){
			HotelImageData data = new HotelImageData();
			fList.add(data);
		}
		
		return fList;
	}
	
}
