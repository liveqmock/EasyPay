package com.inter.trade.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.inter.trade.R;
import com.inter.trade.ui.IndexFunc;
import com.inter.trade.ui.fragment.airticket.util.AirLineUtils;
import com.inter.trade.ui.fragment.airticket.util.ApiAirticketGetOrderHistoryData;
import com.inter.trade.ui.fragment.cridet.data.CridetHistoryData;
import com.inter.trade.ui.fragment.telephone.util.MoblieRechangeRecordData;
import com.inter.trade.util.DateUtil;

import android.content.Context;
import android.content.res.Resources;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 飞机票记录Adapter
 * @author zhichao.huang
 *
 */
public class AirTicketRecordAdapter extends BaseAdapter{
	private Context mContext;
	private LayoutInflater mInflater;
	private Resources mResources;
	private ArrayList<ApiAirticketGetOrderHistoryData> mArrayList;
	
	public AirTicketRecordAdapter(Context context,ArrayList<ApiAirticketGetOrderHistoryData> datas){
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
			convertView = mInflater.inflate(R.layout.airticket_order_list_item, null);//R.layout.record_item
			mHolder = new Holder();
			mHolder.startCity = (TextView)convertView.findViewById(R.id.startCity);
			mHolder.endCity = (TextView)convertView.findViewById(R.id.endCity);
			mHolder.totaPrice = (TextView)convertView.findViewById(R.id.totaPrice);
			mHolder.date = (TextView)convertView.findViewById(R.id.date);
			convertView.setTag(mHolder);
		}else {
			mHolder = (Holder)convertView.getTag();
		}
		ApiAirticketGetOrderHistoryData data = mArrayList.get(arg0);
		
		mHolder.startCity.setText(data.departCity);
		mHolder.endCity.setText(data.arriveCity);
		mHolder.totaPrice.setText("￥"+data.totalPrice);
		if(data.createOrderTime.length() >= 10) {
			mHolder.date.setText(data.createOrderTime.substring(0, 10));
		} else {
			mHolder.date.setText(data.createOrderTime);
		}
		
		return convertView;
	}
	class Holder{
		TextView startCity;
		TextView endCity;
		TextView totaPrice;
		TextView date;
	}
}
