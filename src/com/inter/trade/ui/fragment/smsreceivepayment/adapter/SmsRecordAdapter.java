package com.inter.trade.ui.fragment.smsreceivepayment.adapter;

import java.util.ArrayList;

import com.inter.trade.R;
import com.inter.trade.ui.IndexFunc;
import com.inter.trade.ui.fragment.cridet.data.CridetHistoryData;
import com.inter.trade.ui.fragment.smsreceivepayment.util.SmsRecordData;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 短信收款历史记录Adapter
 * @author Lihaifeng
 *
 */
public class SmsRecordAdapter extends BaseAdapter{
	private Context mContext;
	private LayoutInflater mInflater;
	private Resources mResources;
	private ArrayList<SmsRecordData> mArrayList;
	
	public SmsRecordAdapter(Context context,ArrayList<SmsRecordData> datas){
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
			convertView = mInflater.inflate(R.layout.sms_record_item_layout, null);
			mHolder = new Holder();
			mHolder.phone = (TextView)convertView.findViewById(R.id.phone);
			mHolder.money = (TextView)convertView.findViewById(R.id.money);
			mHolder.date = (TextView)convertView.findViewById(R.id.date);
			mHolder.status = (TextView)convertView.findViewById(R.id.status);
			convertView.setTag(mHolder);
		}else {
			mHolder = (Holder)convertView.getTag();
		}
		SmsRecordData data = mArrayList.get(arg0);
		
		mHolder.phone.setText(data.phone);
		mHolder.money.setText("+"+data.money);
		mHolder.date.setText(data.date);
		mHolder.status.setText(data.status);
		return convertView;
	}
	class Holder{
		TextView phone;
		TextView money;
		TextView date;
		TextView status;
	}
}
