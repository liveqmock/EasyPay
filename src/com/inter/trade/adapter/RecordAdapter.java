package com.inter.trade.adapter;

import com.inter.trade.R;
import com.inter.trade.ui.IndexFunc;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class RecordAdapter extends BaseAdapter{
	private Context mContext;
	private LayoutInflater mInflater;
	private Resources mResources;
	
	public RecordAdapter(Context context){
		this.mContext = context;
		mInflater = LayoutInflater.from(mContext);
				mResources=context.getResources();
				;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return IndexFunc.funcs_image.length;
	}
	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return IndexFunc.funcs_image[arg0];
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
			convertView = mInflater.inflate(R.layout.record_item, null);
			mHolder = new Holder();
			mHolder.name = (TextView)convertView.findViewById(R.id.name);
			mHolder.status = (TextView)convertView.findViewById(R.id.status);
			mHolder.date = (TextView)convertView.findViewById(R.id.date);
			mHolder.count = (TextView)convertView.findViewById(R.id.money);
			convertView.setTag(mHolder);
		}else {
			mHolder = (Holder)convertView.getTag();
		}
		mHolder.name.setText(IndexFunc.funcs_name[arg0]);
		return convertView;
	}
	class Holder{
		TextView name;
		TextView date;
		TextView count;
		TextView status;
	}
}
