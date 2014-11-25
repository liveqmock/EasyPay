package com.inter.trade.ui.func;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
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
import com.inter.trade.util.SerializeUtils;
import com.inter.trade.util.VersionUtil;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

public class FunctionFragment extends Fragment implements
		DataListener, OnClickListener,OnPageChangeListener {

	private ViewPager mPager;

	private static ArrayList<ExtendData> mAdsDatas = new ArrayList<ExtendData>();
	public static ArrayList<ArrayList<FuncData>> mDatas;
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
	
	private boolean isLocal=true;//記錄是否採用本地菜單
	
	private Context context;
	
	private ModleTask task;

	public static FunctionFragment createFragment(
			ArrayList<ExtendData> extendDatas) {
		FunctionFragment fragment = new FunctionFragment();
		mAdsDatas = extendDatas;
		return fragment;
	}

	public FunctionFragment() {

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Logger.d("FunctionFragment", "onCreate1");
		System.out.println("当前的版本号:"
				+ VersionUtil.getVersionCode(getActivity()));
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
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
		mPager = (ViewPager) v.findViewById(R.id.ad_pager);
		mPager.setOnPageChangeListener(this);
		
		myFragmentAdapter = new MyFragmentAdapter(getChildFragmentManager());
		pointLayout = (LinearLayout) v.findViewById(R.id.pointLayout);

		btnLeftMenu = (Button) v.findViewById(R.id.btn_menu);
		btnFavour = (ImageButton) v.findViewById(R.id.btn_favourite);
		// btnShopping = (ImageButton) v.findViewById(R.id.btn_shopping);
		// btnAirplane = (ImageButton) v.findViewById(R.id.btn_airplane);
		btnConvience = (ImageButton) v.findViewById(R.id.btn_convinence);
		btnMoney = (ImageButton) v.findViewById(R.id.btn_money);

		btnFavour.setOnClickListener(this);
		// btnShopping.setOnClickListener(this);
		// btnAirplane.setOnClickListener(this);
		btnConvience.setOnClickListener(this);
		btnMoney.setOnClickListener(this);
		btnLeftMenu.setOnClickListener(this);
		btnFavour.setTag(true);
		btnFavour.setSelected(true);

		
		if(savedInstanceState!=null && mDatas==null){
			mDatas=(ArrayList<ArrayList<FuncData>>) savedInstanceState.getSerializable("data");
			mPointDatas=(ArrayList<PointData>) savedInstanceState.getSerializable("point");
			updatePage();
		}
		return v;
	}
	
	

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		context=activity;
	}

	private String path="";
	private boolean isFlag=true;
	
	@Override
	public void onStart() {
		super.onStart();
		path = context.getFilesDir()+File.separator+Constants.OBJECTFILE;
		
		if(!isLocal){//採用網絡菜單
			//先从本地读取
			mDatas=(ArrayList<ArrayList<FuncData>>) SerializeUtils.deserialization(path);
			if(mDatas!=null){
				isFlag=false;
				updatePage();
				System.out.println("由本地获取的数据");
			}
			task=new ModleTask(context, new ResponseStateListener() {

					@Override
					public void onSuccess(Object obj, Class cla) {
						ModleData data=(ModleData)obj;
						if ("1".equals(data.getIsnew())) {// 新功能更新

							mDatas = ModelUtil
									.getFunctions(data);// 进行逻辑排序等操作
							
							SerializeUtils.serialization(path, mDatas);//将对象保存到本地
							if(isFlag){
								updatePage();
								System.out.println("由网络直接获取的数据");
							}
						}
						//mDatas=(ArrayList<ArrayList<FuncData>>) obj;
						System.out.println("获取的菜单的大小:" + mDatas.size());
						
					}
				},isFlag);
			task.execute("0", "16.1");
		}else{//採用本地菜單
			mDatas = FuncUtil.getFunctions(context);
			updatePage();
		}
		initPoint();
	}
	
	

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable("data", mDatas);
		outState.putSerializable("point", mPointDatas);
	}

	/**
	 * 移动指定的按钮到菜单的第一位
	 * 
	 * @throw
	 * @return void
	 */
	private void moveButton(ImageButton btn) {
		//刪除第一個按鈕，并把按鈕添加到最後的位置
		View view = rightMenu.getChildAt(0);
		rightMenu.removeViewAt(0);
		rightMenu.addView(view, rightMenu.getChildCount());
		//將選中的按鈕添加到第一位
		btn.setTag(true);
		rightMenu.removeView(btn);
		ll = (LayoutParams) btn.getLayoutParams();
		// ll.setMargins(24, 0, 0, 0);
		btn.setLayoutParams(ll);
		rightMenu.addView(btn, 0);
		//把第一個設置為選中，顯示顏色
		for(int i=0;i<rightMenu.getChildCount();i++){
			rightMenu.getChildAt(i).setSelected(false);
		}
		rightMenu.getChildAt(0).setSelected(true);
		if (isShowing) {
			for (int i = 0; i < rightMenu.getChildCount(); i++) {
				rightMenu.getChildAt(i).setEnabled(true);
			}
		} else {
			// 将其他按钮设置成不可用
			for (int i = 0; i < rightMenu.getChildCount(); i++) {
				rightMenu.getChildAt(i).setEnabled(false);
			}
			btn.setEnabled(true);
		}
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
		/*
		 * case R.id.btn_shopping: changePage(v, 1); break;
		 */
		// case R.id.btn_airplane:
		// changePage(v, 1);
		// break;
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
		System.out.println("sdk:" + sdkInt);
		Boolean flag = (Boolean) view.getTag();
		AnimatorListener listener = new SimpleAnimatorListener() {
			@Override
			public void onAnimationEnd(Animator arg0) {
				view.setEnabled(true);
			}
		};
		if (flag == null || !flag) {// 不是第一个按钮
			if (sdkInt >= 14) {
				startAnimator(null, 1);
				isShowing = false;
				// 将其他按钮设置成不可用
				for (int i = 0; i < rightMenu.getChildCount(); i++) {
					rightMenu.getChildAt(i).setEnabled(false);
				}
				view.setEnabled(true);
			}
			
			//刪除第一個按鈕，并把按鈕添加到最後的位置
			View v = rightMenu.getChildAt(0);
			rightMenu.removeViewAt(0);
			rightMenu.addView(v, rightMenu.getChildCount());
			
			
			
			// TODO 将图标移动到第一位
			rightMenu.removeView(view);
			rightMenu.addView(view, 0);
			for (int i = 0; i < rightMenu.getChildCount(); i++) {
				rightMenu.getChildAt(i).setTag(false);
				ll = (LayoutParams) rightMenu.getChildAt(i).getLayoutParams();
				// ll.setMargins(0, 0, 0, 0);
				rightMenu.getChildAt(i).setLayoutParams(ll);
			}
			view.setTag(true);
			ll = (LayoutParams) view.getLayoutParams();
			// ll.setMargins(24, 0, 0, 0);
			view.setLayoutParams(ll);
			
			//把第一個設置為選中，顯示顏色
			for(int i=0;i<rightMenu.getChildCount();i++){
				rightMenu.getChildAt(i).setSelected(false);
			}
			rightMenu.getChildAt(0).setSelected(true);
		} else if (sdkInt >= 14) {// 第一个按钮
			view.setEnabled(false);
			if (isShowing) {
				startAnimator(listener, 1);
				isShowing = false;
				// 将其他按钮设置成不可用
				for (int i = 0; i < rightMenu.getChildCount(); i++) {
					rightMenu.getChildAt(i).setEnabled(false);
				}
			} else {
				startAnimator(listener, -1);
				isShowing = true;
				// 将所有的按钮恢复成可用
				for (int i = 0; i < rightMenu.getChildCount(); i++) {
					rightMenu.getChildAt(i).setEnabled(true);
				}
			}
		}

		mPager.setCurrentItem(page, true);
	}

	/**
	 * 启动平移动画
	 * 
	 * @param listener
	 * @throw
	 * @return void
	 */
	private void startAnimator(AnimatorListener listener, int direction) {
		ViewPropertyAnimator.animate(rightMenu)
				.translationXBy(direction * rightMenu.getWidth() * 2 / 3)
				.setListener(listener).setDuration(250).start();
		;
	}

	@Override
	public void notify(ArrayList<ExtendData> extendDatas) {
		
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if(task!=null&&!task.isCancelled()){
			task.cancel(true);
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

	class MyFragmentAdapter extends FragmentPagerAdapter {

		public MyFragmentAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int arg0) {
			Logger.d("HttpApi", "");
			return IndexItem.create(arg0);
		}

		@Override
		public int getCount() {
			return mDatas.size();
		}
	}
	
	private void updatePage() {
		mPager.setAdapter(myFragmentAdapter);
		myFragmentAdapter.notifyDataSetChanged();
	}

	private void initPoint() {
		mPointDatas.clear();
		pointLayout.removeAllViews();
		for (int i = 0; i < 3; i++) {
			PointData data = new PointData();
			ImageView imageView = new ImageView(context);
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
	}
	@Override
	public void onPageScrollStateChanged(int arg0) {
		
	}
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		
	}
	@Override
	public void onPageSelected(int arg0) {
		if (mPointDatas != null) {
			setSelected(arg0);
		}
		for (int i = 0; i < rightMenu.getChildCount(); i++) {
			rightMenu.getChildAt(i).setTag(false);
			ll = (LayoutParams) rightMenu.getChildAt(i)
					.getLayoutParams();
			// ll.setMargins(0, 0, 0, 0);
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

}
