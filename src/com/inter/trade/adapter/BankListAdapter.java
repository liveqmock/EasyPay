package com.inter.trade.adapter;

import java.util.ArrayList;

import com.inter.trade.R;
import com.inter.trade.data.BankData;
import com.inter.trade.data.HelpData;
import com.inter.trade.ui.IndexFunc;
import com.inter.trade.ui.fragment.coupon.util.ShopData.Coupon;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class BankListAdapter extends BaseAdapter{
	private Context mContext;
	private LayoutInflater mInflater;
	private Resources mResources;
	private ArrayList<BankData> mBankDatas;
	
	public BankListAdapter(Context context,ArrayList<BankData> datas){
		this.mContext = context;
		mInflater = LayoutInflater.from(mContext);
		mResources=context.getResources();
		mBankDatas = datas;
	}
	
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mBankDatas.size();
	}
	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return mBankDatas.get(arg0);
	}
	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}
	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		Holder mHolder =null;
		if(convertView == null){
			mHolder = new Holder();
			convertView = mInflater.inflate(R.layout.bank_item, null);
			mHolder.name = (TextView)convertView.findViewById(R.id.bankname);
			convertView.setTag(mHolder);
		}else{
			mHolder = (Holder)convertView.getTag();
		}
		mHolder.name.setText(mBankDatas.get(arg0).bankname);
		return convertView;
	}
	class Holder{
		TextView name;
	}
}
