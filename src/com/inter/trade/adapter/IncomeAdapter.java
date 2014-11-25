package com.inter.trade.adapter;

import java.util.ArrayList;

import com.inter.trade.R;
import com.inter.trade.data.MonthDetialData;
import com.inter.trade.ui.IndexFunc;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class IncomeAdapter extends BaseAdapter{
	private Context mContext;
	private LayoutInflater mInflater;
	private Resources mResources;
	private ArrayList<MonthDetialData> monthDetialDatas;
	
	public IncomeAdapter(Context context,ArrayList<MonthDetialData> temp ){
		this.mContext = context;
		mInflater = LayoutInflater.from(mContext);
				mResources=context.getResources();
				;
				monthDetialDatas = temp;		
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return monthDetialDatas.size();
	}
	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return monthDetialDatas.get(arg0);
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
			convertView = mInflater.inflate(R.layout.income_item, null);
			mHolder = new Holder();
			mHolder.name = (TextView)convertView.findViewById(R.id.name);
			mHolder.status = (TextView)convertView.findViewById(R.id.status);
			mHolder.date = (TextView)convertView.findViewById(R.id.date);
			mHolder.count = (TextView)convertView.findViewById(R.id.money);
			convertView.setTag(mHolder);
		}else {
			mHolder = (Holder)convertView.getTag();
		}
		
		MonthDetialData  data = monthDetialDatas.get(arg0);
		mHolder.name.setText(data.accgpaymode);
		mHolder.date.setText(data.accglistdate);
		mHolder.count.setText(data.accglistmoney);
		mHolder.status.setText(data.accgstate);
		return convertView;
	}
	class Holder{
		TextView name;
		TextView date;
		TextView count;
		TextView status;
	}
}
