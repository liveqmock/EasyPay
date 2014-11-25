package com.inter.trade.ui.fragment.express.util;

import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.inter.trade.R;
import com.inter.trade.ui.IndexFunc;

public class EpressGridAdapter extends BaseAdapter{
	private Context mContext;
	private LayoutInflater mInflater;
	private Resources mResources;
	private List<ExpressData> mArrayList;
	
	public EpressGridAdapter(Context context,List<ExpressData>  datas){
		this.mContext = context;
		mInflater = LayoutInflater.from(mContext);
				mResources=context.getResources();
				;
		mArrayList = datas;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mArrayList.size();
	}
	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return mArrayList.get(arg0);
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
			convertView = mInflater.inflate(R.layout.grid_item, null);
			mHolder = new Holder();
			mHolder.name = (TextView)convertView.findViewById(R.id.item_name);
			mHolder.icon = (ImageView)convertView.findViewById(R.id.item_icon);
			convertView.setTag(mHolder);
		}else {
			mHolder = (Holder)convertView.getTag();
		}
		ExpressData data = mArrayList.get(arg0);
		mHolder.name.setText(data.comname);
		mHolder.icon.setBackgroundDrawable(mResources.getDrawable(ExpressInfoUtils.getExpressDrawable(data.comname)));
		return convertView;
	}
	class Holder{
		TextView name;
		ImageView icon;
	}
}
