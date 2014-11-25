package com.inter.trade.ui.fragment.order.query;

import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.inter.trade.R;
import com.inter.trade.ui.fragment.BaseFragment;
import com.inter.trade.util.LoginUtil;

/**
 * 订单号查询界面
 * 
 * @author apple
 * 
 */
public class OrderQureyFragment extends BaseFragment implements OnClickListener{
	
	FragmentActivity mActivity;
	
	
	private ImageView cursor;// 动画图片
	private TextView t1, t2;// 页卡头标
	private int offset = 0;// 动画图片偏移量
	private int bmpW;// 动画图片宽度
	
	private LinearLayout near_month_container;
	private LinearLayout one_month_ago_container;
	private boolean isInitTwo=false;
	public static  final String HSITORY_KEY = "HISTYORY_KEYI";
	private boolean histroy=false;
	private String type = "pay";
	private LinearLayout tab_layout;
	public static OrderQureyFragment create(String isHistory){
		OrderQureyFragment fragment = new OrderQureyFragment();
		Bundle bundle = new Bundle();
		bundle.putString(HSITORY_KEY, isHistory);
		fragment.setArguments(bundle);
		return fragment;
	}
	public OrderQureyFragment() {
		
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mActivity =getActivity();
		LoginUtil.detection(getActivity());
	
		
		setBackVisible();
		
		String params = getArguments().getString(HSITORY_KEY);
		if(null != params &&params.equals("history")){
			histroy = true;
		}else{
			histroy = false;
		}
		if(!histroy){
			setTitle("订单查询");
			type = "all";
		}else {
			setTitle("订单历史");
			type = "pay";
		}
	}
	
	
	private void InitTextView(View view)
	{
		t1 = (TextView) view.findViewById(R.id.opertion_log_tab);
		t2 = (TextView) view.findViewById(R.id.threat_log_tab);
		
		t1.setTextColor(Color.BLACK);
		t1.setTextSize(15);
		t2.setTextColor(Color.GRAY);
		t2.setTextSize(14);

		t1.setOnClickListener(new MyOnClickListener(0));
		t2.setOnClickListener(new MyOnClickListener(1));
		
		tab_layout = (LinearLayout)view.findViewById(R.id.tab_layout);
	}
	
	
	/**
	 * 头标点击监听
	 */
	public class MyOnClickListener implements View.OnClickListener
	{
		private int index = 0;
		int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量
		Animation animation = null;
		public MyOnClickListener(int i)
		{
			index = i;
		}

		@Override
		public void onClick(View v)
		{
			if(v.getId() == R.id.opertion_log_tab){
				TextView tv = (TextView)v;
				tv.setTextColor(Color.BLACK);
				tv.setTextSize(15);
				t2.setTextColor(Color.GRAY);
				t2.setTextSize(14);
				tab_layout.setBackgroundDrawable(getResources().getDrawable(R.drawable.left_button_bg));
			}else{
				TextView tv = (TextView)v;
				tv.setTextColor(Color.BLACK);
				tv.setTextSize(15);
				t1.setTextColor(Color.GRAY);
				t1.setTextSize(14);
				tab_layout.setBackgroundDrawable(getResources().getDrawable(R.drawable.right_button_bg));
			}
			if(index==0){
				
				animation = new TranslateAnimation(one, 0, 0, 0);
			}else{
				animation = new TranslateAnimation(offset, one, 0, 0);
			}
			animation.setFillAfter(true);// True:图片停在动画结束位置
			animation.setDuration(300);
			cursor.startAnimation(animation);
			initPage(index);
		}
	};
	
	private void initPage(int index){
		if(index ==0){
			near_month_container.setVisibility(View.VISIBLE);
			one_month_ago_container.setVisibility(View.GONE);
		}else{
			near_month_container.setVisibility(View.GONE);
			one_month_ago_container.setVisibility(View.VISIBLE);
			if(!isInitTwo){
				initTwo();
				isInitTwo=true;
			}
		}
	}
	
	/**
	 * 初始化动画
	 */
	private void InitImageView(View view)
	{
		cursor = (ImageView) view.findViewById(R.id.cursor);
//		bmpW = SysUtil.getBitmapOptions(R.drawable.log_navigation).outWidth;// 获取图片宽度
		bmpW = cursor.getDrawable().getIntrinsicWidth();
		DisplayMetrics dm = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;// 获取分辨率宽度
		offset = (screenW / 2 - bmpW) / 2;// 计算偏移量
		Matrix matrix = new Matrix();
		matrix.postTranslate(offset, 0);
		cursor.setImageMatrix(matrix);// 设置动画初始位置
	}
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.order_query_layout, container,
				false);
		near_month_container =(LinearLayout) view.findViewById(R.id.near_month_container);
		one_month_ago_container =(LinearLayout) view.findViewById(R.id.one_month_ago_container);
		
		InitImageView(view);
		InitTextView(view);
		
		initOne();
		return view;
	}
	
	private void initOne(){
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.add(R.id.near_month_container, OneMonthFragment.create(type));
		transaction.commit();
	}
	
	private void initTwo(){
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.add(R.id.one_month_ago_container, OneMonthAgoFragment.create(type));
		transaction.commit();
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.order_query_btn:
			break;
		default:
			break;
		}
	}


}
