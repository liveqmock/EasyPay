package com.inter.trade.adapter;

import java.util.ArrayList;

import com.inter.trade.R;
import com.inter.trade.ui.IndexFunc;
import com.inter.trade.ui.fragment.airticket.util.AirticketIconsUtils;
import com.inter.trade.ui.fragment.airticket.util.ApiAirticketGetAirlineData;
import com.inter.trade.ui.fragment.cridet.data.CridetHistoryData;
import com.inter.trade.ui.fragment.telephone.util.MoblieRechangeRecordData;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 通过航空公司检索对应的航班Adapter
 * @author zhichao.huang
 *
 */
public class AirTicketQueryLineSeoAdapter extends BaseAdapter{
	private Context mContext;
	private LayoutInflater mInflater;
	private Resources mResources;
	private ArrayList<ApiAirticketGetAirlineData> mArrayList;
	
	/**
	 * 当前选中位置
	 */
	private int currentPosition = -1;
	
	public AirTicketQueryLineSeoAdapter(Context context,ArrayList<ApiAirticketGetAirlineData> datas){
		this.mContext = context;
		mInflater = LayoutInflater.from(mContext);
		mResources=context.getResources();
		mArrayList= datas;
	}
	
	/**
	 * 
	 * 设置当前选中位置
	 */
	public void setCurrentPosition (int position) {
		currentPosition = position;
	}
	
	/**
	 * 获取当前选中位置
	 * @return
	 */
	public int getCurrentPosition () {
		return currentPosition;
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
			convertView = mInflater.inflate(R.layout.airticket_list_seo_item, null);//R.layout.record_item
			mHolder = new Holder();
			mHolder.air_name = (TextView)convertView.findViewById(R.id.air_name);
			mHolder.airlineIcon = (ImageView)convertView.findViewById(R.id.airline_icon);
			
			convertView.setTag(mHolder);
		}else {
			mHolder = (Holder)convertView.getTag();
		}
		ApiAirticketGetAirlineData data = mArrayList.get(arg0);
		
		
		mHolder.air_name.setText(data.airLineName);
		if(AirticketIconsUtils.getAirlineDrawable(data.airLineName) != 0) {
			mHolder.airlineIcon.setBackgroundResource(AirticketIconsUtils.getAirlineDrawable(data.airLineName));
		}else {
			mHolder.airlineIcon.setBackgroundColor(Color.parseColor("#00000000"));
		}
		
		if(arg0 == currentPosition) {
			convertView.setBackgroundColor(Color.parseColor("#4CFFFFFF"));
		} else {
			convertView.setBackgroundColor(Color.parseColor("#00000000"));
		}
		
		return convertView;
	}
	class Holder{
		TextView air_name;//航空公司名字
		ImageView airlineIcon;//航空公司图标
	}
}
