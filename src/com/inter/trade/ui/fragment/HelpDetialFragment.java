package com.inter.trade.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.inter.trade.R;
import com.inter.trade.util.LoginUtil;

/**
 * 我的钱包详情
 * @author apple
 *
 */
public class HelpDetialFragment extends BaseFragment implements OnClickListener{
	private RelativeLayout phone_layout;
	private static String mTitle;
	private static String mDate;
	private static String mContent;
	
	private TextView mNameTextView;
	private TextView mDateTextView;
	private TextView mContentTextView;
	
	public static HelpDetialFragment create(String title,String date,String content){
		mTitle = title;
		mDate = date;
		mContent = content;
		
		return new HelpDetialFragment();
	}
	public HelpDetialFragment()
	{
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitle("交易详情");
		setBackVisible();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		LoginUtil.detection(getActivity());
		View view = inflater.inflate(R.layout.help_detial_item, container, false);
		mNameTextView= (TextView)view.findViewById(R.id.name);
		mDateTextView= (TextView)view.findViewById(R.id.date);
		mContentTextView= (TextView)view.findViewById(R.id.content);
		
		mNameTextView.setText(mTitle);
		mDateTextView.setText(mDate);
		mContentTextView.setText(mContent);
		
		return view;
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setTitle("交易详情");
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		default:
			break;
		}
		
	}
	
}
