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
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 航班Adapter,往返（包括去程，回程）
 * @author zhichao.huang
 *
 */
public class AirTicketQueryLineWangfanAdapter extends BaseAdapter{
	private Context mContext;
	private LayoutInflater mInflater;
	private Resources mResources;
	private ArrayList<ApiAirticketGetAirlineData> mArrayList;
	
	public AirTicketQueryLineWangfanAdapter(Context context,ArrayList<ApiAirticketGetAirlineData> datas){
		this.mContext = context;
		mInflater = LayoutInflater.from(mContext);
		mResources=context.getResources();
		mArrayList= datas;
	}
	
	public ArrayList<ApiAirticketGetAirlineData> getDatas() {
		return mArrayList;
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
			convertView = mInflater.inflate(R.layout.airticket_query_wangfan_item, null);//R.layout.record_item
			mHolder = new Holder();
//			convertView.findViewById(R.id.air_query_layout);
			//去程
			mHolder.qucheng_mark = (TextView)convertView.findViewById(R.id.qucheng_mark);
			mHolder.start_airport = (TextView)convertView.findViewById(R.id.start_airport);
			mHolder.end_airport = (TextView)convertView.findViewById(R.id.end_airport);
			mHolder.start_time = (TextView)convertView.findViewById(R.id.start_time);
			mHolder.end_time = (TextView)convertView.findViewById(R.id.end_time);
			mHolder.price = (TextView)convertView.findViewById(R.id.price);
			mHolder.discount = (TextView)convertView.findViewById(R.id.discount);
			mHolder.air_type = (TextView)convertView.findViewById(R.id.air_type);
			mHolder.airline_name = (TextView)convertView.findViewById(R.id.airline_name);
			
			//返程
			mHolder.huicheng_mark = (TextView)convertView.findViewById(R.id.huicheng_mark);
			mHolder.huicheng_layout = (LinearLayout)convertView.findViewById(R.id.huicheng_layout);
			mHolder.fan_start_airport = (TextView)convertView.findViewById(R.id.fan_start_airport);
			mHolder.fan_end_airport = (TextView)convertView.findViewById(R.id.fan_end_airport);
			mHolder.fan_start_time = (TextView)convertView.findViewById(R.id.fan_start_time);
			mHolder.fan_end_time = (TextView)convertView.findViewById(R.id.fan_end_time);
			mHolder.fan_price = (TextView)convertView.findViewById(R.id.fan_price);
			mHolder.fan_discount = (TextView)convertView.findViewById(R.id.fan_discount);
			mHolder.fan_air_type = (TextView)convertView.findViewById(R.id.fan_air_type);
			mHolder.fan_airline_name = (TextView)convertView.findViewById(R.id.fan_airline_name);
			
			convertView.setTag(mHolder);
		}else {
			mHolder = (Holder)convertView.getTag();
		}
		ApiAirticketGetAirlineData data = mArrayList.get(arg0);
		//-----------------------------去程数据-----------------------------------------
		String startTime = null, endTime = null;
		if(data.takeOffTime != null && !data.takeOffTime.equals("")) {
			startTime = data.takeOffTime.substring(data.takeOffTime.indexOf("T")+1, data.takeOffTime.lastIndexOf(":"));
		}
		
		if(data.arriveTime != null && !data.arriveTime.equals("")) {
			endTime = data.arriveTime.substring(data.arriveTime.indexOf("T")+1, data.arriveTime.lastIndexOf(":"));
		}
		
		mHolder.start_airport.setText(data.dPortName);
		mHolder.end_airport.setText(data.aPortName);
		mHolder.start_time.setText(startTime);
		mHolder.end_time.setText(endTime);
		mHolder.price.setText("￥"+data.price);
		mHolder.airline_name.setText(data.airLineName);
		mHolder.air_type.setText(data.flight);
		
		//-----------------------------返程数据-----------------------------------------
//		ApiAirticketGetAirlineData fanData = null;
//		if(data.airticketFanchengAirlineData != null) {
//			mHolder.huicheng_layout.setVisibility(View.VISIBLE);
//			fanData = data.airticketFanchengAirlineData;
//			
//			String startTime2 = null, endTime2 = null;
//			if(fanData.takeOffTime != null && !fanData.takeOffTime.equals("")) {
//				startTime2 = fanData.takeOffTime.substring(fanData.takeOffTime.indexOf("T")+1, fanData.takeOffTime.lastIndexOf(":"));
//			}
//			
//			if(fanData.arriveTime != null && !fanData.arriveTime.equals("")) {
//				endTime2 = fanData.arriveTime.substring(fanData.arriveTime.indexOf("T")+1, fanData.arriveTime.lastIndexOf(":"));
//			}
//			
//			mHolder.fan_start_airport.setText(fanData.dPortName);
//			mHolder.fan_end_airport.setText(fanData.aPortName);
//			mHolder.fan_start_time.setText(startTime2);
//			mHolder.fan_end_time.setText(endTime2);
//			mHolder.fan_price.setText("￥"+fanData.price);
//			mHolder.fan_airline_name.setText(fanData.airLineName);
//			mHolder.fan_air_type.setText(fanData.flight);
//			
//			
//		} else {
//			mHolder.huicheng_layout.setVisibility(View.GONE);
//			mHolder.qucheng_mark.setVisibility(View.GONE);
//		}
		
		return convertView;
	}
	class Holder{
		//去程
		TextView qucheng_mark;//去程文字标识
		TextView start_airport;//出发机场
		TextView end_airport;//到达机场
		TextView start_time;//出发时间
		TextView end_time;//到达时间
		TextView price;//价格
		TextView discount;//折扣
		TextView air_type;//航班类型
		TextView airline_name;//航空公司
		
		//返程
		TextView huicheng_mark;//回程文字标识
		LinearLayout huicheng_layout;//回程layout
		TextView fan_start_airport;//出发机场
		TextView fan_end_airport;//到达机场
		TextView fan_start_time;//出发时间
		TextView fan_end_time;//到达时间
		TextView fan_price;//价格
		TextView fan_discount;//折扣
		TextView fan_air_type;//航班类型
		TextView fan_airline_name;//航空公司
	}
}
