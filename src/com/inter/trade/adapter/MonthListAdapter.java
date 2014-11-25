package com.inter.trade.adapter;

import java.util.ArrayList;

import com.inter.trade.R;
import com.inter.trade.data.AccMonthData;
import com.inter.trade.data.AcctypeData;
import com.inter.trade.data.HelpData;
import com.inter.trade.ui.IndexFunc;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MonthListAdapter extends BaseAdapter{
	private Context mContext;
	private LayoutInflater mInflater;
	private Resources mResources;
	private ArrayList<AccMonthData> mAcctypeDatas;
	
	public MonthListAdapter(Context context,ArrayList<AccMonthData> datas){
		this.mContext = context;
		mInflater = LayoutInflater.from(mContext);
		mResources=context.getResources();
		mAcctypeDatas = datas;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mAcctypeDatas.size();
	}
	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return mAcctypeDatas.get(arg0);
	}
	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}
	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		Holder mHolder = null;
		if(convertView == null)
		{
			convertView = mInflater.inflate(R.layout.acc_month_detial, null);
			mHolder = new Holder();
			mHolder.month = (TextView)convertView.findViewById(R.id.month);
			mHolder.income = (TextView)convertView.findViewById(R.id.income);
			mHolder.outcome = (TextView)convertView.findViewById(R.id.outcome);
			convertView.setTag(mHolder);
		}else {
			mHolder = (Holder)convertView.getTag();
		}
		AccMonthData data = mAcctypeDatas.get(arg0);
		mHolder.month.setText(data.accmonth);
		String temp = "0";
		if(data.accincome!=null){
			temp = data.accincome;
		}
		
		mHolder.income.setText("收入："+temp);
		if(data.accpayout==null){
			temp = "0";
		}else{
			temp = data.accpayout;
		}
		mHolder.outcome.setText("支出："+temp);
		return convertView;
	}
	class Holder{
		TextView month;
		TextView income;
		TextView outcome;
	}
}
