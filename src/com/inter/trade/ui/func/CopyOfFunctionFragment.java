package com.inter.trade.ui.func;

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
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.inter.protocol.ProtocolData;
import com.inter.protocol.ProtocolParser;
import com.inter.protocol.ProtocolRsp;
import com.inter.protocol.util.ProtocolUtil;
import com.inter.trade.R;
import com.inter.trade.data.CommonData;
import com.inter.trade.data.ResponseData;
import com.inter.trade.error.ErrorUtil;
import com.inter.trade.log.Logger;
import com.inter.trade.net.HttpUtil;
import com.inter.trade.ui.CridetActivity;
import com.inter.trade.ui.FunctionActivity;
import com.inter.trade.ui.MainActivity;
import com.inter.trade.ui.fragment.FragmentFactory;
import com.inter.trade.util.Constants;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PreferenceConfig;
import com.inter.trade.util.PromptUtil;
import com.inter.trade.util.VersionUtil;

public class CopyOfFunctionFragment extends Fragment implements OnItemClickListener {

	private ViewPager mPager;
	
	private boolean isExcetue=false;
	private ArrayList<ExtendData> mAdsDatas = new ArrayList<ExtendData>();
	public static ArrayList<ArrayList<FuncData>> mDatas = new ArrayList<ArrayList<FuncData>>();
	private AdTask mAdTask;
	private LinearLayout pointLayout;
	private ArrayList<PointData> mPointDatas = new ArrayList<PointData>();
	private MyFragmentAdapter  myFragmentAdapter;
	public CopyOfFunctionFragment() {

	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Logger.d("FunctionFragment", "onCreate1");
	}
	
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.function_layout, container, false);
		LinearLayout index_view = (LinearLayout) v
				.findViewById(R.id.index_view);
		index_view.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (((MainActivity) getActivity()).getScreen() == 0) {
					((MainActivity) getActivity()).hideMenu();
					return;
				}
			}
		});
		Button back = (Button) v.findViewById(R.id.title_back_btn);
		back.setVisibility(View.GONE);
		Button menu = (Button) v.findViewById(R.id.title_menu_btn);
		menu.setVisibility(View.VISIBLE);
		final RelativeLayout title_layout = (RelativeLayout) v
				.findViewById(R.id.title_layout);
		title_layout.setBackgroundDrawable(getActivity().getResources()
				.getDrawable(R.drawable.title_bg));
		menu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (getActivity() instanceof MainActivity) {

					if (((MainActivity) getActivity()).getScreen() == 0) {
						((MainActivity) getActivity()).hideMenu();
					} else {
						((MainActivity) getActivity()).showLeftMenu();
					}

				}
			}
		});

		mPager = (ViewPager) v.findViewById(R.id.ad_pager);
		mPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				if(mPointDatas != null){
					setSelected(arg0);
				}
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		myFragmentAdapter = new MyFragmentAdapter(getChildFragmentManager());
		pointLayout  = (LinearLayout)v.findViewById(R.id.pointLayout);
		mPager.setAdapter(myFragmentAdapter);
		// mPager.setAdapter(new MyFragmentAdapter(getChildFragmentManager()));
		
		
		
		if (checkConnection(getActivity())) {
			Logger.d("FunctionFragment","excute");
//			Logger.d("FunctionFragment","isExcetue"+PayApp.isExcute);
				mAdTask = new AdTask();
				mAdTask.execute("");
		}else {
			initLocalData();
		}
		
//		}

		// mPager.setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		// // TODO Auto-generated method stub
		// if(((MainActivity)getActivity()).getScreen() == 0){
		// ((MainActivity)getActivity()).hideMenu();
		// }
		// }
		// });
		return v;
	}
	
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(mAdTask!= null){
			mAdTask.cancel(true);
		}
		
	}
	
	/**
	 * Simple network connection check.
	 * 
	 * @param context
	 */
	private boolean checkConnection(Context context) {
		final ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		final NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		if (networkInfo == null || !networkInfo.isConnectedOrConnecting()) {
			Toast.makeText(context, "net unreachable", Toast.LENGTH_LONG)
					.show();
			return false;
		} else {
			return true;
		}
	}
	private void setSelected(int arg2){
		pointLayout.removeAllViews();
		for(int i=0;i<mPointDatas.size();i++){
			ImageView imageView = new ImageView(getActivity());
			if(i== arg2){
				mPointDatas.get(i).setSelected(true);
				imageView.setBackgroundDrawable(getResources().getDrawable(R.drawable.select_poin));
			}else{
				imageView.setBackgroundDrawable(getResources().getDrawable(R.drawable.unselected));
				mPointDatas.get(i).setSelected(false);
			}
			pointLayout.addView(imageView);
		}
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		if (VersionUtil.isNeedUpdate(getActivity())) {
			VersionUtil.showUpdate(getActivity());
			return;
		}
		if (((MainActivity) getActivity()).getScreen() == 0) {
			((MainActivity) getActivity()).hideMenu();
			return;
		}
		Intent intent = new Intent();
		if (arg2 == 0) {
			if (LoginUtil.isLogin) {
				intent.setClass(getActivity(), CridetActivity.class);
			} else {
				intent.putExtra(FragmentFactory.INDEX_KEY, arg2);
				intent.setClass(getActivity(), FunctionActivity.class);
			}
		} else {
			intent.putExtra(FragmentFactory.INDEX_KEY, arg2);
			intent.setClass(getActivity(), FunctionActivity.class);
		}

		getActivity().startActivity(intent);
	}

	class MyFragmentAdapter extends FragmentPagerAdapter {

		public MyFragmentAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Fragment getItem(int arg0) {
			// TODO Auto-generated method stub
			Logger.d("HttpApi", ""  );
			return IndexItem.create(arg0);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mDatas.size();
		}
	}

	/**
	 * 解析响应体
	 * 
	 * @param params
	 */
	private void parserResoponse(List<ProtocolData> params,boolean islocal) {
		ResponseData response = new ResponseData();
		LoginUtil.mLoginStatus.mResponseData = response;
		mAdsDatas.clear();
		for (ProtocolData data : params) {
			if (data.mKey.equals(ProtocolUtil.msgheader)) {
				ProtocolUtil.parserResponse(response, data);

			} else if (data.mKey.equals(ProtocolUtil.msgbody)) {
				List<ProtocolData> result1 = data.find("/result");
				if (result1 != null) {
					LoginUtil.mLoginStatus.result = result1.get(0).mValue;
				}
				List<ProtocolData> message = data.find("/message");
				if (message != null) {
					LoginUtil.mLoginStatus.message = message.get(0).mValue;
				}
				List<ProtocolData> isupdate = data.find("/isnew");
				String isnew = "";
				if (isupdate != null) {
					isnew = isupdate.get(0).mValue;
				}
				/**
				 * 如果有更新将数据写入本地
				 */
				if(!islocal){
					if(isnew.equals("1")){
						
						ProtocolParser parser = ProtocolParser.instance();
						parser.setParser(new AppResponseParser());
						FuncXmlUtil.writeToXml(FuncXmlUtil.getFuncXml(), parser.aToXml(params));
					}
				}
				
				
				List<ProtocolData> vsn = data.find("/version");
				String version = "";
				if (vsn != null) {
					version = vsn.get(0).mValue;
					PreferenceConfig.instance(getActivity()).putString(Constants.FUNC_ITEM_KEY,
							version);
				}
				

				List<ProtocolData> aupic = data.find("/msgchild");
				if(aupic ==null){
					return;
				}
				for (ProtocolData child : aupic) {
					ExtendData picData = new ExtendData();
					if (child.mChildren != null && child.mChildren.size() > 0) {
						Set<String> keys = child.mChildren.keySet();
						for (String key : keys) {
							List<ProtocolData> rs = child.mChildren.get(key);
							for (ProtocolData item : rs) {
								if (item.mKey.equals("mnuname")) {
									picData.mnuname = item.mValue;

								} else if (item.mKey.equals("mnupic")) {
									picData.mnupic = item.mValue;

								} else if (item.mKey.equals("mnuorder")) {

									picData.mnuorder = item.mValue;
								} else if (item.mKey.equals("mnuurl")) {

									picData.mnuurl = item.mValue;
								}
							}
						}
					}

					mAdsDatas.add(picData);
				}

			}
		}
	}


	class AdTask extends AsyncTask<String, Integer, Boolean> {
		private ProtocolRsp mRsp;

		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			// UPLOAD_URL =
			// LoginUtil.mLoginStatus.mUserInfo.mPicData.get(0).uploadurl;
			try {
				CommonData data = new CommonData();
				data.putValue("paycardkey", "0");
				data.putValue("appversion", 
						PreferenceConfig.instance(getActivity()).getString(Constants.FUNC_ITEM_KEY, ""));
				List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas(
						"ApiAppInfo", "readMenuModule", data);
				ExtendListParser authorRegParser = new ExtendListParser();

				mRsp = HttpUtil.doRequest(authorRegParser, mDatas);
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
			 PromptUtil.dissmiss();
			try {
				mAdsDatas.clear();
				if (mRsp != null) {
					List<ProtocolData> datas = mRsp.mActions;
					parserResoponse(datas,false);

				} 
				//错误处理
				if(!ErrorUtil.create().errorDeal(LoginUtil.mLoginStatus,getActivity())){
					initLocalData();
					return;
				}
				//如果返回数据为0，那么从本地读取
				if(mAdsDatas.size()==0){
					ProtocolRsp rsp= FuncXmlUtil.readXML(FuncXmlUtil.getFuncXml());
					List<ProtocolData> datas=null;
					if(rsp !=null){
						datas = rsp.mActions;
					}
					if(datas!= null){
						parserResoponse(datas,true);
					}
				}
//				
				mDatas = FuncUtil.getFunctions(mAdsDatas);
				pointLayout.removeAllViews();
				mPointDatas.clear();
				for(int i=0;i<mDatas.size();i++)
				{
					PointData data = new PointData();
					ImageView imageView = new ImageView(getActivity());
					mPointDatas.add(data);
					if(i== 0){
						mPointDatas.get(i).setSelected(true);
						imageView.setBackgroundDrawable(getResources().getDrawable(R.drawable.select_poin));
					}else{
						imageView.setBackgroundDrawable(getResources().getDrawable(R.drawable.unselected));
						mPointDatas.get(i).setSelected(false);
					}
					pointLayout.addView(imageView);
				}
//				mPager.setAdapter(new MyFragmentAdapter(
//						getFragmentManager()));
				myFragmentAdapter.notifyDataSetChanged();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				
				// PromptUtil.showToast(getActivity(),getString(R.string.net_error));
			}

		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			 PromptUtil.showDialog(getActivity(), "正在初始化...");
		}

	}
	
	private void initLocalData(){
		ProtocolRsp rsp= FuncXmlUtil.readXML(FuncXmlUtil.getFuncXml());
		List<ProtocolData> datas=null;
		if(rsp !=null){
			datas = rsp.mActions;
		}
		if(datas!= null){
			parserResoponse(datas,true);
		}
		
		mDatas = FuncUtil.getFunctions(mAdsDatas);
		
		for(int i=0;i<mDatas.size();i++)
		{
			PointData data = new PointData();
			ImageView imageView = new ImageView(getActivity());
			mPointDatas.add(data);
			if(i== 0){
				mPointDatas.get(i).setSelected(true);
				imageView.setBackgroundDrawable(getResources().getDrawable(R.drawable.select_poin));
			}else{
				imageView.setBackgroundDrawable(getResources().getDrawable(R.drawable.unselected));
				mPointDatas.get(i).setSelected(false);
			}
			pointLayout.addView(imageView);
		}
//		mPager.setAdapter(myFragmentAdapter);
		myFragmentAdapter.notifyDataSetChanged();
	}
}
