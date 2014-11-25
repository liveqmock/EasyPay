package com.inter.trade.adapter;

import java.util.ArrayList;

import com.inter.trade.R;
import com.inter.trade.data.AcctypeData;
import com.inter.trade.data.HelpData;
import com.inter.trade.ui.IndexFunc;
import com.inter.trade.util.NumberFormatUtil;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class WalletListAdapter extends BaseAdapter{
	private Context mContext;
	private LayoutInflater mInflater;
	private Resources mResources;
	private ArrayList<AcctypeData> mAcctypeDatas;
	
	public WalletListAdapter(Context context,ArrayList<AcctypeData> datas){
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
			convertView = mInflater.inflate(R.layout.wallet_item, null);
			mHolder = new Holder();
			mHolder.name = (TextView)convertView.findViewById(R.id.name);
			mHolder.count = (TextView)convertView.findViewById(R.id.count);
			convertView.setTag(mHolder);
		}else {
			mHolder = (Holder)convertView.getTag();
		}
		AcctypeData data = mAcctypeDatas.get(arg0);
		mHolder.name.setText(data.acctypename);
		mHolder.count.setText(NumberFormatUtil.format2(data.accmoney)+" 元");
		return convertView;
	}
	class Holder{
		TextView name;
		TextView count;
	}
}
