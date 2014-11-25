package com.inter.trade.adapter;

import java.util.ArrayList;

import com.inter.trade.R;
import com.inter.trade.ui.IndexFunc;
import com.inter.trade.ui.fragment.buyswipcard.util.BuySwipCardAddressRecordData;
import com.inter.trade.ui.fragment.buyswipcard.util.BuySwipCardRecordData;
import com.inter.trade.ui.fragment.cridet.data.CridetHistoryData;
import com.inter.trade.ui.fragment.telephone.util.MoblieRechangeRecordData;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 历史地址记录Adapter
 * @author zhichao.huang
 *
 */
public class BuySwipCardAddressRecordAdapter extends BaseAdapter{
	private Context mContext;
	private LayoutInflater mInflater;
	private Resources mResources;
	private ArrayList<BuySwipCardAddressRecordData> mArrayList;
	
	public BuySwipCardAddressRecordAdapter(Context context,ArrayList<BuySwipCardAddressRecordData> datas){
		this.mContext = context;
		mInflater = LayoutInflater.from(mContext);
		mResources=context.getResources();
		mArrayList= datas;
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
//		if(convertView == null)
//		{
//			
			int temp = arg0%3;
			
			if(temp == 0) {
				convertView = mInflater.inflate(R.layout.buy_swipcard_address_list_item_layout, null);//R.layout.record_item
			}else if(temp == 1) {
				convertView = mInflater.inflate(R.layout.buy_swipcard_address_list_item_layout2, null);
			}else if(temp == 2) {
				convertView = mInflater.inflate(R.layout.buy_swipcard_address_list_item_layout3, null);
			}/**else{
				convertView = mInflater.inflate(R.layout.buy_swipcard_address_list_item_layout, null);//R.layout.record_item
			}*/
			
			mHolder = new Holder();
			mHolder.receiving_name = (TextView)convertView.findViewById(R.id.receiving_name);
			mHolder.receiving_phone = (TextView)convertView.findViewById(R.id.receiving_phone);
			mHolder.receiving_address = (TextView)convertView.findViewById(R.id.receiving_address);
//			convertView.setTag(mHolder);
//		}else {
//			mHolder = (Holder)convertView.getTag();
//		}
		BuySwipCardAddressRecordData data = mArrayList.get(arg0);
		
		mHolder.receiving_name.setText(data.shman);
		mHolder.receiving_phone.setText(data.shphone);
		mHolder.receiving_address.setText(data.shaddress);
		return convertView;
	}
	class Holder{
		TextView receiving_name;
		TextView receiving_phone;
		TextView receiving_address;
	}
}
