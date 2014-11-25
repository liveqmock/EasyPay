package com.inter.trade.adapter;

import java.util.ArrayList;

import com.inter.trade.R;
import com.inter.trade.ui.IndexFunc;
import com.inter.trade.ui.fragment.airticket.util.ApiAirticketGetAirlineData;
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
 * 航班 舱位选择Adapter
 * @author zhichao.huang
 *
 */
public class AirTicketQueryLineCangWeiAdapter extends BaseAdapter{
	private Context mContext;
	private LayoutInflater mInflater;
	private Resources mResources;
	private ArrayList<ApiAirticketGetAirlineData> mArrayList;
	
	public AirTicketQueryLineCangWeiAdapter(Context context,ArrayList<ApiAirticketGetAirlineData> datas){
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
			convertView = mInflater.inflate(R.layout.airticket_cangwei_query_item, null);//R.layout.record_item
			mHolder = new Holder();
			mHolder.aclass = (TextView)convertView.findViewById(R.id.aclass);
			mHolder.quantity = (TextView)convertView.findViewById(R.id.quantity);
//			mHolder.start_time = (TextView)convertView.findViewById(R.id.start_time);
//			mHolder.end_time = (TextView)convertView.findViewById(R.id.end_time);
			mHolder.price = (TextView)convertView.findViewById(R.id.price);
//			mHolder.discount = (TextView)convertView.findViewById(R.id.discount);
//			mHolder.air_type = (TextView)convertView.findViewById(R.id.air_type);
			
			convertView.setTag(mHolder);
		}else {
			mHolder = (Holder)convertView.getTag();
		}
		ApiAirticketGetAirlineData data = mArrayList.get(arg0);
//		String startTime = null, endTime = null;
//		if(data.takeOffTime != null && !data.takeOffTime.equals("")) {
//			startTime = data.takeOffTime.substring(data.takeOffTime.indexOf("T")+1, data.takeOffTime.lastIndexOf(":"));
//		}
//		
//		if(data.arriveTime != null && !data.arriveTime.equals("")) {
//			endTime = data.arriveTime.substring(data.arriveTime.indexOf("T")+1, data.arriveTime.lastIndexOf(":"));
//		}
//		String mClass = "";
//		if(data.aClass.equals("Y")) {
//			mClass = "经济舱";
//		}else if(data.aClass.equals("C")) {
//			mClass = "公务舱";
//		}else if(data.aClass.equals("F")) {
//			mClass = "头等舱";
//		}else{
//			mClass = data.aClass;
//		}
		
		mHolder.aclass.setText(data.aClass);
		mHolder.quantity.setText(data.quantity);
//		mHolder.end_airport.setText(data.aPortName);
//		mHolder.start_time.setText(startTime);
//		mHolder.end_time.setText(endTime);
		mHolder.price.setText("￥"+data.price);
//		mHolder.discount.setText(data.craftType);
//		mHolder.air_type.setText(data.flight);
		return convertView;
	}
	class Holder{
		TextView aclass;//舱位等级
		TextView quantity;//票数
//		TextView start_time;//出发时间
//		TextView end_time;//到达时间
		TextView price;//价格
//		TextView discount;//折扣
//		TextView air_type;//航班类型
	}
}
