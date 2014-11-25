package com.inter.trade.adapter;

import java.util.ArrayList;

import com.inter.trade.R;
import com.inter.trade.ui.IndexFunc;
import com.inter.trade.ui.fragment.cridet.data.CridetHistoryData;
import com.inter.trade.ui.fragment.waterelectricgas.util.WaterElectricGasRecordData;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 水电煤历史记录Adapter
 * @author Lihaifeng
 *
 */
public class WaterElectricGasRecordAdapter extends BaseAdapter{
	private Context mContext;
	private LayoutInflater mInflater;
	private Resources mResources;
	private ArrayList<WaterElectricGasRecordData> mArrayList;
	
	public WaterElectricGasRecordAdapter(Context context,ArrayList<WaterElectricGasRecordData> datas){
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
			convertView = mInflater.inflate(R.layout.water_electric_gas_record_item, null);
			mHolder = new Holder();
			mHolder.billmoney = (TextView)convertView.findViewById(R.id.billmoney);
			mHolder.paymoney = (TextView)convertView.findViewById(R.id.paymoney);
			mHolder.date = (TextView)convertView.findViewById(R.id.date);
			convertView.setTag(mHolder);
		}else {
			mHolder = (Holder)convertView.getTag();
		}
		WaterElectricGasRecordData data = mArrayList.get(arg0);
		
		mHolder.billmoney.setText(data.factNumber+"元");
		mHolder.paymoney.setText(data.payNumber+"元");
		mHolder.date.setText(data.completeTime);
		return convertView;
	}
	class Holder{
		TextView billmoney;
		TextView paymoney;
		TextView date;
	}
}
