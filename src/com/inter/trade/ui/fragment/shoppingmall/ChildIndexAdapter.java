package com.inter.trade.ui.fragment.shoppingmall;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.inter.trade.R;
import com.inter.trade.imageframe.ImageFetcher;
import com.inter.trade.ui.func.FuncData;

public class ChildIndexAdapter extends BaseAdapter {
	private Context mContext;
	private LayoutInflater mInflater;
	private Resources mResources;
	private ImageFetcher mFetcher;
	private ArrayList<FuncData> mFuncDatas;

	public ChildIndexAdapter(Context context, ImageFetcher fetcher,
			ArrayList<FuncData> funcDatas) {
		this.mContext = context;
		mInflater = LayoutInflater.from(mContext);
		mResources = context.getResources();
		mFetcher = fetcher;
		mFuncDatas = funcDatas;
	}

	@Override
	public int getCount() {
		if(mFuncDatas!=null){
			return mFuncDatas.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return mFuncDatas.get(arg0);
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
		// if(convertView == null)
		// {
		// convertView = mInflater.inflate(R.layout.grid_item, null);
		convertView = mInflater.inflate(R.layout.grid_item_new_three, null);
		mHolder = new Holder();
		mHolder.name = (TextView) convertView.findViewById(R.id.item_name);
		mHolder.icon = (ImageView) convertView.findViewById(R.id.item_icon);
		convertView.setTag(mHolder);
		// }else {
		// mHolder = (Holder)convertView.getTag();
		// }
		FuncData data = mFuncDatas.get(arg0);
		mHolder.name.setText(data.name);
		if (data.imageId != -1 && data.identify != -1) {
			mHolder.icon.setBackgroundDrawable(mResources
					.getDrawable(data.imageId));
		} else {
//			mFetcher.loadImage(data.url, mHolder.icon);
		}
		return convertView;
	}

	class Holder {
		TextView name;
		ImageView icon;
	}
}
