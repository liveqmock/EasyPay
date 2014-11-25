package com.inter.trade.ui.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.inter.protocol.ProtocolData;
import com.inter.protocol.ProtocolParser;
import com.inter.protocol.ProtocolRsp;
import com.inter.protocol.body.ReadIndexAdListParser;
import com.inter.protocol.util.ProtocolUtil;
import com.inter.trade.R;
import com.inter.trade.adapter.IndexFuncAdapter;
import com.inter.trade.data.AdsData;
import com.inter.trade.data.ResponseData;
import com.inter.trade.imageframe.ImageCache.ImageCacheParams;
import com.inter.trade.imageframe.ImageFetcher;
import com.inter.trade.log.Logger;
import com.inter.trade.net.HttpUtil;
import com.inter.trade.ui.CridetActivity;
import com.inter.trade.ui.FunctionActivity;
import com.inter.trade.ui.IndexFunc;
import com.inter.trade.ui.MainActivity;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.VersionUtil;

public class IndexFragment extends Fragment implements OnItemClickListener{
	
	private GridView mGridView;
	
	private ViewPager mPager;
	
	public static  ImageFetcher mImageFetcher;
	private int mImageThumbSize;
	public static final String IMAGE_CACHE_DIR = "image";
	private ArrayList<AdsData> mAdsDatas = new ArrayList<AdsData>();

	private int mAdallcount;
	
	public IndexFragment(){
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		initFetcher();
		View v=  inflater.inflate(R.layout.index_layout, container, false);
		LinearLayout index_view = (LinearLayout)v.findViewById(R.id.index_view);
		index_view.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(((MainActivity)getActivity()).getScreen() == 0){
					((MainActivity)getActivity()).hideMenu();
					return ;
				}
			}
		});
		Button back = (Button)v.findViewById(R.id.title_back_btn);
		back.setVisibility(View.GONE);
		Button menu = (Button)v.findViewById(R.id.title_menu_btn);
		menu.setVisibility(View.VISIBLE);
		final RelativeLayout title_layout= (RelativeLayout)v.findViewById(R.id.title_layout);
		title_layout.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.title_bg));
		menu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(getActivity() instanceof MainActivity){
					
					if(((MainActivity)getActivity()).getScreen() == 0){
						((MainActivity)getActivity()).hideMenu();
					}else {
						((MainActivity)getActivity()).showLeftMenu();
					}
					
				}
			}
		});
//		menu.setOnLongClickListener(new View.OnLongClickListener() {
//			
//			@Override
//			public boolean onLongClick(View v) {
//				// TODO Auto-generated method stub
//				title_layout.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.title_bg));
//				return false;
//			}
//		});
		
		mGridView = (GridView)v.findViewById(R.id.func_grid);
		mGridView.setAdapter(new IndexFuncAdapter(getActivity()));
		mGridView.setOnItemClickListener(this);
		
		mPager = (ViewPager)v.findViewById(R.id.ad_pager);
		mPager.setAdapter(new MyFragmentAdapter(getFragmentManager()));
		if(checkConnection(getActivity())){
//			new AdTask().execute("");
		}
		
		mPager.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(((MainActivity)getActivity()).getScreen() == 0){
					((MainActivity)getActivity()).hideMenu();
				}
			}
		});
		return v;
	}
	
	   /**
	    * Simple network connection check.
	    *
	    * @param context
	    */
	    private boolean checkConnection(Context context) {
	        final ConnectivityManager cm =
	                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	        final NetworkInfo networkInfo = cm.getActiveNetworkInfo();
	        if (networkInfo == null || !networkInfo.isConnectedOrConnecting()) {
	            Toast.makeText(context, "net unreachable", Toast.LENGTH_LONG).show();
	            return false;
	        }else{
	        		return true;
	        }
	    }
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		if(VersionUtil.isNeedUpdate(getActivity())){
			VersionUtil.showUpdate(getActivity());
			return;
		}
		if(((MainActivity)getActivity()).getScreen() == 0){
			((MainActivity)getActivity()).hideMenu();
			return ;
		}
		Intent intent = new Intent();
		if(arg2 == 0){
			if(LoginUtil.isLogin){
				intent.setClass(getActivity(), CridetActivity.class);
			}else {
				intent.putExtra(FragmentFactory.INDEX_KEY, arg2);
				intent.setClass(getActivity(), FunctionActivity.class);
			}
		}else{
			intent.putExtra(FragmentFactory.INDEX_KEY, arg2);
			intent.setClass(getActivity(), FunctionActivity.class);
		}
		
		getActivity().startActivity(intent);
	}
	private void initFetcher(){
		 final DisplayMetrics displayMetrics = new DisplayMetrics();
	        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
	        final int width = displayMetrics.widthPixels;
		ImageCacheParams cacheParams = new ImageCacheParams(getActivity(), IMAGE_CACHE_DIR);
		cacheParams.setMemCacheSizePercent(0.25f); // Set memory cache to 25% of app memory
		mImageThumbSize = getResources().getDimensionPixelSize(R.dimen.ad_pager_height);
		mImageFetcher = new ImageFetcher(getActivity(), width,mImageThumbSize);
		mImageFetcher.setLoadingImage(R.drawable.ad_unload);
        mImageFetcher.addImageCache(getActivity().getSupportFragmentManager(), cacheParams);
	}
	
	public ImageFetcher getFetcher(){
		return mImageFetcher;
	}
	class MyFragmentAdapter extends FragmentPagerAdapter{

		public MyFragmentAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Fragment getItem(int arg0) {
			// TODO Auto-generated method stub
			if(mAdsDatas.size()==0){
				return AdDetialFragment.newInstanceLocal(IndexFunc.adsId[arg0],IndexFragment.this);
			}
			return AdDetialFragment.newInstance(mAdsDatas.get(arg0).adpicurl,IndexFragment.this);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if(mAdsDatas.size()==0){
				return IndexFunc.adsId.length;
			}
			return mAdsDatas.size();
		}
	}
	
	
	/**
	 * 解析响应体
	 * @param params
	 */
	private void parserResoponse(List<ProtocolData> params){
		ResponseData response = new ResponseData();
		LoginUtil.mLoginStatus.mResponseData = response;
		mAdsDatas.clear();
		for(ProtocolData data :params){
			if(data.mKey.equals(ProtocolUtil.msgheader)){
				ProtocolUtil.parserResponse(response, data);
				
			}else if(data.mKey.equals(ProtocolUtil.msgbody)){
				List<ProtocolData> result1 = data.find("/result");
				if(result1 != null){
					LoginUtil.mLoginStatus.result = result1.get(0).mValue;
				}
				List<ProtocolData> message = data.find("/message");
				if(message != null){
					LoginUtil.mLoginStatus.message = message.get(0).mValue;
				}
				
				List<ProtocolData> msgallcount = data.find("/msgallcount");
				if(msgallcount != null){
					mAdallcount = Integer.parseInt(msgallcount.get(0).mValue.trim());
				}
				List<ProtocolData> aupic = data.find("/msgchild");
				for(ProtocolData child:aupic){
					AdsData picData = new AdsData();
					if (child.mChildren != null && child.mChildren.size() > 0) {
						Set<String> keys = child.mChildren.keySet();
						for(String key:keys){
							List<ProtocolData> rs = child.mChildren.get(key);
							for(ProtocolData item: rs){
								if(item.mKey.equals("adno")){
									picData.adno  = item.mValue;
									
								}else if(item.mKey.equals("adpicurl")){
									picData.adpicurl  = item.mValue;
									
								}else if(item.mKey.equals("adtitle")){
									
									picData.adtitle  = item.mValue;
								}else if(item.mKey.equals("adlinkurl")){
									
									picData.adlinkurl  = item.mValue;
								}
							}
						}
					}
					
					mAdsDatas.add(picData);
				}
				
			}
		}
	}
	
	/**
	 * 请求修改身份信息
	 * @return
	 */
	private List<ProtocolData> getRequestDatas(){
		List<ProtocolData> mDatas = new ArrayList<ProtocolData>();
		
		ProtocolData headerData = ProtocolUtil.headerData();
		ProtocolData chinalData= new ProtocolData(ProtocolUtil.channelinfo, null);
		ProtocolData name= new ProtocolData(ProtocolUtil.api_name, "ApiAppInfo");
		
		ProtocolData name_func= new ProtocolData(ProtocolUtil.api_name_func, "readIndexAdList");
		
		
		chinalData.addChild(name);
		chinalData.addChild(name_func);
		headerData.addChild(chinalData);
		
		
		ProtocolData bodyData = new ProtocolData(ProtocolUtil.msgbody, null);
		ProtocolData msgadtype = new ProtocolData("msgadtype", "1");
		
		
		bodyData.addChild(msgadtype);
		
		mDatas.add(headerData);
		mDatas.add(bodyData);
		
		return mDatas;
	}
	
	class AdTask extends AsyncTask<String, Integer, Boolean>{
		private ProtocolRsp mRsp;
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
//			UPLOAD_URL = LoginUtil.mLoginStatus.mUserInfo.mPicData.get(0).uploadurl;
			try {
				
					List<ProtocolData> mDatas = getRequestDatas();
					ReadIndexAdListParser authorRegParser = new ReadIndexAdListParser();
					ProtocolParser.instance().setParser(authorRegParser);
					String content = ProtocolParser.instance().aToXml(mDatas);
					
					Logger.d("HttpApi", "request\n"+content);
					mRsp = HttpUtil.getRequest(content, authorRegParser);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
//			PromptUtil.dissmiss();
			try {
				if(mRsp != null){
					List<ProtocolData> datas = mRsp.mActions;
					parserResoponse(datas);
					mPager.setAdapter(new MyFragmentAdapter(getFragmentManager()));
//					if(mAdsDatas.size()==0){
////						PromptUtil.showToast(getActivity(), getString(R.string.net_error));
//					}else{
//						mPager.setAdapter(new MyFragmentAdapter(getChildFragmentManager()));
//					}
				
				}else {
//					PromptUtil.showToast(getActivity(), getString(R.string.net_error));
				}
			} catch (Exception e) {
				// TODO: handle exception
//				PromptUtil.showToast(getActivity(),getString(R.string.net_error));
			}
			
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
//			PromptUtil.showDialog(getActivity(), "");
		}
		
	}
}
