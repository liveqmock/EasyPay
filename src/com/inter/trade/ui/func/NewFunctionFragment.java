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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
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
import com.inter.trade.ui.MainActivity.DataListener;
import com.inter.trade.ui.fragment.FragmentFactory;
import com.inter.trade.ui.fragment.waterelectricgas.data.ResponseStateListener;
import com.inter.trade.ui.func.data.ModleData;
import com.inter.trade.ui.func.task.ModleTask;
import com.inter.trade.ui.func.util.ModelUtil;
import com.inter.trade.ui.func.util.SimpleAnimatorListener;
import com.inter.trade.util.Constants;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PreferenceConfig;
import com.inter.trade.util.PromptUtil;
import com.inter.trade.util.VersionUtil;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

public class NewFunctionFragment extends Fragment implements OnItemClickListener,
		DataListener, OnClickListener {

	private ViewPager mPager;

	private static ArrayList<ExtendData> mAdsDatas = new ArrayList<ExtendData>();
	public static ArrayList<ArrayList<FuncData>> mDatas = new ArrayList<ArrayList<FuncData>>();
	private LinearLayout pointLayout;
	private ArrayList<PointData> mPointDatas = new ArrayList<PointData>();
	private MyFragmentAdapter myFragmentAdapter;

	private Button btnLeftMenu;
	private ImageButton btnFavour;
	private ImageButton btnShopping;
	private ImageButton btnAirplane;
	private ImageButton btnConvience;
	private ImageButton btnMoney;
	private LinearLayout rightMenu;
	private LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
			LinearLayout.LayoutParams.WRAP_CONTENT,
			LinearLayout.LayoutParams.WRAP_CONTENT);

	private LinearLayout.LayoutParams ll = null;

	public static NewFunctionFragment createFragment(
			ArrayList<ExtendData> extendDatas) {
		NewFunctionFragment fragment = new NewFunctionFragment();
		mAdsDatas = extendDatas;
		return fragment;
	}

	public NewFunctionFragment() {

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Logger.d("FunctionFragment", "onCreate1");
		System.out.println("当前的版本号:"+VersionUtil.getVersionCode(getActivity()));
		new ModleTask(getActivity(), new ResponseStateListener() {
			
			@Override
			public void onSuccess(Object obj, Class cla) {
				if(ModleData.class.equals(cla)){
					ModleData data=(ModleData) obj;
					if("1".equals(data.getIsnew())){//新功能更新
						ModelUtil.getFunctions(data);
					}else{//从本地获取功能菜单
						
					}
				}
			}
		},true).execute("0","16.1");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.function_layout_new, container,
				false);
		LinearLayout index_view = (LinearLayout) v
				.findViewById(R.id.index_view);
		lp.setMargins(10, 0, 0, 0);
		rightMenu = (LinearLayout) v.findViewById(R.id.ll_right_menu);
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
				if (mPointDatas != null) {
					setSelected(arg0);
				}
				for (int i = 0; i < rightMenu.getChildCount(); i++) {
					rightMenu.getChildAt(i).setTag(false);
					ll = (LayoutParams) rightMenu.getChildAt(i).getLayoutParams();
					//ll.setMargins(0, 0, 0, 0);
					rightMenu.getChildAt(i).setLayoutParams(ll);
				}
				
				switch (arg0) {
				case 0:
					moveButton(btnFavour);
					break;
				case 6:
					moveButton(btnShopping);
					break;
				case 4:
					moveButton(btnAirplane);
					break;
				case 2:
					moveButton(btnConvience);
					break;
				case 1:
					moveButton(btnMoney);
					break;

				default:
					break;
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
		pointLayout = (LinearLayout) v.findViewById(R.id.pointLayout);

		// mPager.setAdapter(new MyFragmentAdapter(getChildFragmentManager()));

		// if (checkConnection(getActivity())) {
		// Logger.d("FunctionFragment","excute");
		// // Logger.d("FunctionFragment","isExcetue"+PayApp.isExcute);
		// mAdTask = new AdTask();
		// mAdTask.execute("");
		// }else {
		// initLocalData();
		// }

		// }

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
		notify(new ArrayList<ExtendData>());

		btnLeftMenu = (Button) v.findViewById(R.id.btn_menu);
		btnFavour = (ImageButton) v.findViewById(R.id.btn_favourite);
		//btnShopping = (ImageButton) v.findViewById(R.id.btn_shopping);
		//btnAirplane = (ImageButton) v.findViewById(R.id.btn_airplane);
		btnConvience = (ImageButton) v.findViewById(R.id.btn_convinence);
		btnMoney = (ImageButton) v.findViewById(R.id.btn_money);

		btnFavour.setOnClickListener(this);
		//btnShopping.setOnClickListener(this);
//		btnAirplane.setOnClickListener(this);
		btnConvience.setOnClickListener(this);
		btnMoney.setOnClickListener(this);
		btnLeftMenu.setOnClickListener(this);
		btnFavour.setTag(true);

		return v;
	}
	
	/**
	 * 移动指定的按钮到菜单的第一位 
	 * @throw
	 * @return void
	 */
	private void moveButton(ImageButton btn) {
		btn.setTag(true);
		rightMenu.removeView(btn);
		ll = (LayoutParams) btn.getLayoutParams();
		///ll.setMargins(16, 0, 0, 0);
		btn.setLayoutParams(ll);
		rightMenu.addView(btn, 0);
	}

	/** 标记是否显示菜单栏 */
	private boolean isShowing = true;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_menu:
			if (getActivity() instanceof MainActivity) {

				if (((MainActivity) getActivity()).getScreen() == 0) {
					((MainActivity) getActivity()).hideMenu();
				} else {
					((MainActivity) getActivity()).showLeftMenu();
				}

			}
			break;
		case R.id.btn_favourite:
			changePage(v, 0);
			break;
		/*case R.id.btn_shopping:
			changePage(v, 1);
			break;*/
//		case R.id.btn_airplane:
//			changePage(v, 1);
//			break;
		case R.id.btn_convinence:
			changePage(v, 2);
			break;
		case R.id.btn_money:
			changePage(v, 1);
			break;

		default:
			break;
		}
	}

	private void changePage(final View view, int page) {
		int sdkInt = android.os.Build.VERSION.SDK_INT;
		System.out.println("sdk:"+sdkInt);
		Boolean flag = (Boolean) view.getTag();
		AnimatorListener listener = new SimpleAnimatorListener() {
			@Override
			public void onAnimationEnd(Animator arg0) {
				view.setEnabled(true);
			}
		};
		if (flag == null || !flag) {// 不是第一个按钮
			if(sdkInt>=14){
				startAnimator(null, 1);
				isShowing = false;
			}
			// TODO 将图标移动到第一位
			rightMenu.removeView(view);
			rightMenu.addView(view, 0);
			for (int i = 0; i < rightMenu.getChildCount(); i++) {
				rightMenu.getChildAt(i).setTag(false);
				ll = (LayoutParams) rightMenu.getChildAt(i).getLayoutParams();
				ll.setMargins(0, 0, 0, 0);
				rightMenu.getChildAt(i).setLayoutParams(ll);
			}
			view.setTag(true);
			ll = (LayoutParams) view.getLayoutParams();
			//ll.setMargins(16, 0, 0, 0);
			view.setLayoutParams(ll);
		} else if(sdkInt>=14){// 第一个按钮
			view.setEnabled(false);
			if (isShowing) {
				startAnimator(listener, 1);
				isShowing = false;
				
			} else {
				startAnimator(listener,-1);
				isShowing = true;
			}
		}

		mPager.setCurrentItem(page, true);
	}
	
	/**
	 * 启动平移动画
	 * @param listener
	 * @throw
	 * @return void
	 */
	private void startAnimator(AnimatorListener listener,int direction) {
		ViewPropertyAnimator.animate(rightMenu).translationXBy(direction*rightMenu.getWidth() * 3 / 4).setListener(listener).setDuration(500).start();;
	}

	@Override
	public void notify(ArrayList<ExtendData> extendDatas) {
		// TODO Auto-generated method stub
		mAdsDatas.clear();
		mAdsDatas = extendDatas;
		initLocalData();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

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

	private void setSelected(int arg2) {
		pointLayout.removeAllViews();
		for (int i = 0; i < mPointDatas.size(); i++) {
			ImageView imageView = new ImageView(getActivity());
			if (i == arg2) {
				mPointDatas.get(i).setSelected(true);
				// imageView.setBackgroundDrawable(getResources().getDrawable(R.drawable.select_poin));
				imageView.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.point_selected));
			} else {
				// imageView.setBackgroundDrawable(getResources().getDrawable(R.drawable.unselected));
				imageView.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.point_unselected));
				mPointDatas.get(i).setSelected(false);
			}
			if (i != 0) {
				imageView.setLayoutParams(lp);
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
			Logger.d("HttpApi", "");
			return IndexItem.create(arg0);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mDatas.size();
		}
	}

	private void initResult() {
		// mDatas = FuncUtil.getFunctions(mAdsDatas);
		mDatas = FuncUtil.getFunctions();
		pointLayout.removeAllViews();
		mPointDatas.clear();
		for (int i = 0; i < mDatas.size(); i++) {
			PointData data = new PointData();
			ImageView imageView = new ImageView(getActivity());
			mPointDatas.add(data);
			if (i == 0) {
				mPointDatas.get(i).setSelected(true);
				// imageView.setBackgroundDrawable(getResources().getDrawable(R.drawable.select_poin));
				imageView.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.point_selected));
			} else {
				// imageView.setBackgroundDrawable(getResources().getDrawable(R.drawable.unselected));
				imageView.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.point_unselected));
				mPointDatas.get(i).setSelected(false);
				// imageView.setPadding(10, 0, 0, 0);
				imageView.setLayoutParams(lp);
			}
			pointLayout.addView(imageView);
		}
		// mPager.setAdapter(new MyFragmentAdapter(
		// getFragmentManager()));
		myFragmentAdapter.notifyDataSetChanged();
	}

	/**
	 * 解析响应体
	 * 
	 * @param params
	 */
	private void parserResoponse(List<ProtocolData> params, boolean islocal) {
		ResponseData response = new ResponseData();
		// LoginUtil.mLoginStatus.mResponseData = response;
		mAdsDatas.clear();
		for (ProtocolData data : params) {
			if (data.mKey.equals(ProtocolUtil.msgheader)) {
				// ProtocolUtil.parserResponse(response, data);

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
				if (!islocal) {
					if (isnew.equals("1")) {

						ProtocolParser parser = ProtocolParser.instance();
						parser.setParser(new AppResponseParser());
						FuncXmlUtil.writeToXml(FuncXmlUtil.getFuncXml(),
								parser.aToXml(params));
					}
				}

				List<ProtocolData> vsn = data.find("/version");
				String version = "";
				if (vsn != null) {
					version = vsn.get(0).mValue;
					PreferenceConfig.instance(getActivity()).putString(
							Constants.FUNC_ITEM_KEY, version);
				}

				List<ProtocolData> aupic = data.find("/msgchild");
				if (aupic == null) {
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
								} else if (item.mKey.equals("mnuid")) {

									picData.mnuid = item.mValue;
								}
							}
						}
					}

					mAdsDatas.add(picData);
				}

			}
		}
	}

	private void initLocalData() {
		Logger.d("parser", "read from xml");
		// Toast.makeText(getActivity(), "initLocalData",
		// Toast.LENGTH_LONG).show();
		ProtocolRsp rsp = FuncXmlUtil.readXML(FuncXmlUtil.getFuncXml());
		List<ProtocolData> datas = null;
		if (rsp != null) {
			datas = rsp.mActions;
		}
		if (datas != null) {
			parserResoponse(datas, true);
		}
		mDatas.clear();
		mPointDatas.clear();
		// mDatas =
		// FuncUtil.getFunctions(mAdsDatas);//一开始运行代码，获取首页默认功能栏数据，mAdsDatas !=
		// null, mAdsDatas 里面没有子元素
		mDatas = FuncUtil.getFunctions(getActivity());
		pointLayout.removeAllViews();
		for (int i = 0; i < mDatas.size(); i++) {
			PointData data = new PointData();
			ImageView imageView = new ImageView(getActivity());
			mPointDatas.add(data);
			if (i == 0) {
				mPointDatas.get(i).setSelected(true);
				// imageView.setBackgroundDrawable(getResources().getDrawable(R.drawable.select_poin));
				imageView.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.point_selected));
			} else {
				// imageView.setBackgroundDrawable(getResources().getDrawable(R.drawable.unselected));
				imageView.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.point_unselected));
				mPointDatas.get(i).setSelected(false);
				imageView.setLayoutParams(lp);
			}
			pointLayout.addView(imageView);
		}
		mPager.setAdapter(myFragmentAdapter);
		myFragmentAdapter.notifyDataSetChanged();
	}

}
