package com.inter.trade.adapter;

import java.util.ArrayList;

import com.inter.trade.R;
import com.inter.trade.ui.IndexFunc;
import com.inter.trade.ui.fragment.cridet.data.CridetHistoryData;
import com.inter.trade.ui.fragment.qmoney.util.QMoneyRecordData;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Q币充值记录Adapter
 * @author Lihaifeng
 *
 */
public class QMoneyRecordAdapter extends BaseAdapter{
	private Context mContext;
	private LayoutInflater mInflater;
	private Resources mResources;
	private ArrayList<QMoneyRecordData> mArrayList;
	
	public QMoneyRecordAdapter(Context context,ArrayList<QMoneyRecordData> datas){
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
		if(convertView == null)
		{
			convertView = mInflater.inflate(R.layout.qmoney_rechange_record_item, null);//R.layout.record_item
			mHolder = new Holder();
			mHolder.phoneId = (TextView)convertView.findViewById(R.id.phoneId);
			mHolder.rechmoney = (TextView)convertView.findViewById(R.id.rechmoney);
			mHolder.rechpaymoney = (TextView)convertView.findViewById(R.id.rechpaymoney);
			mHolder.date = (TextView)convertView.findViewById(R.id.date);
			convertView.setTag(mHolder);
		}else {
			mHolder = (Holder)convertView.getTag();
		}
		QMoneyRecordData data = mArrayList.get(arg0);
		
		mHolder.phoneId.setText(data.rechaqq);
		mHolder.rechmoney.setText(data.rechamoney+"元");
		mHolder.rechpaymoney.setText(data.rechapaymoney+"元");
		mHolder.date.setText(data.rechadatetime);
		return convertView;
	}
	class Holder{
		TextView phoneId;
		TextView rechmoney;
		TextView rechpaymoney;
		TextView date;
	}
}
