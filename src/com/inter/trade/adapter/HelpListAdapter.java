package com.inter.trade.adapter;

import java.util.ArrayList;

import com.inter.trade.R;
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

public class HelpListAdapter extends BaseAdapter{
	private Context mContext;
	private LayoutInflater mInflater;
	private Resources mResources;
	private ArrayList<HelpData> mHelpDatas;
	
	public HelpListAdapter(Context context,ArrayList<HelpData> datas){
		this.mContext = context;
		mInflater = LayoutInflater.from(mContext);
		mResources=context.getResources();
		mHelpDatas = datas;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mHelpDatas.size();
	}
	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return mHelpDatas.get(arg0);
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
			convertView = mInflater.inflate(R.layout.help_list_item, null);
			mHolder = new Holder();
			mHolder.name = (TextView)convertView.findViewById(R.id.name);
			mHolder.date = (TextView)convertView.findViewById(R.id.date);
			mHolder.content = (TextView)convertView.findViewById(R.id.content);
			convertView.setTag(mHolder);
		}else {
			mHolder = (Holder)convertView.getTag();
		}
		HelpData data = mHelpDatas.get(arg0);
		mHolder.name.setText(data.helpname);
		mHolder.date.setText(data.helpdate);
		mHolder.content.setText(data.helpcontent);
		return convertView;
	}
	class Holder{
		TextView name;
		TextView date;
		TextView content;
	}
}
