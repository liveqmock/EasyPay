package com.inter.trade.adapter;

import java.util.ArrayList;

import com.inter.trade.R;
import com.inter.trade.ui.fragment.agent.util.AgentRecordData;
import com.inter.trade.ui.views.StringMatcher;
//import com.inter.trade.ui.fragment.waterelectricgas.WaterElectricGasCitySelectFragment;
import com.inter.trade.ui.fragment.waterelectricgas.WaterElectricGasMainFragment;
import com.inter.trade.ui.fragment.waterelectricgas.util.WaterElectricGasData;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

/**
 * 水电煤缴费城市选择Adapter
 * @author Lihaifeng
 *
 */
//public class WaterElectricGasCitySelectAdapter extends BaseAdapter implements SectionIndexer{
public class WaterElectricGasCitySelectAdapter extends BaseAdapter implements SectionIndexer{
	private Context mContext;
	private LayoutInflater mInflater;
	private Resources mResources;
//	private ArrayList<String> mArrayList;
	private ArrayList<WaterElectricGasData> mArrayList;
	private String mSections = "#ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	
//	public WaterElectricGasCitySelectAdapter(Context context,ArrayList<String> datas){
//		this.mContext = context;
//		mInflater = LayoutInflater.from(mContext);
//		mResources=context.getResources();
//		mArrayList= datas;
//	}
	public WaterElectricGasCitySelectAdapter(Context context,ArrayList<WaterElectricGasData> datas){
		this.mContext = context;
		mInflater = LayoutInflater.from(mContext);
		mResources=context.getResources();
		mArrayList= datas;
//		mArrayList= datas;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mArrayList.size();
	}
	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return mArrayList.get(arg0).cityName;
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
			convertView = mInflater.inflate(R.layout.water_electric_gas_city_select_item, null);
			mHolder = new Holder();
			mHolder.cityName = (TextView)convertView.findViewById(R.id.city_name);
			convertView.setTag(mHolder);
		}else {
			mHolder = (Holder)convertView.getTag();
		}
//		String city = mArrayList.get(arg0);
		String city = mArrayList.get(arg0).cityName;
		
		if(city == null || city ==""){
			return convertView;
		}
		
		if(city.charAt(0) > 'Z'){//listview里的城市拼音字母A~Z不响应，只接收城市的名称
			//城市名字"@color/gray"灰色
			mHolder.cityName.setTextColor(Color.argb(255, 128, 128, 128));
			
			//城市名字背景白色
			convertView.setBackgroundColor(Color.argb(255, 255, 255, 255));
		}else{
			//拼音字母A~Z"@color/yellow"黄色
			mHolder.cityName.setTextColor(Color.argb(255, 254, 175, 0));
			
			//拼音字母A~Z的背景灰色
			convertView.setBackgroundColor(Color.argb(255, 236, 236, 236));
		}
		
		mHolder.cityName.setText(city);
		return convertView;
	}
	class Holder{
		TextView cityName;
	}
	
	@Override
	public boolean isEnabled(int position) {
        // TODO Auto-generated method stub
		
//		String city = mArrayList.get(position);
		String city = mArrayList.get(position).cityName;
		
		if(city == null || city ==""){
			return false;
		}
		
		if(city.charAt(0) <= 'Z'){//listview里的城市拼音字母A~Z不响应，只接收城市的名称
			return false;
		}
        return super.isEnabled(position);
	}
	
	@Override
	public int getPositionForSection(int section) {
		// If there is no item for current section, previous section will be selected
		for (int i = section; i >= 0; i--) {
			for (int j = 0; j < getCount(); j++) {
				if (i == 0) {
					// For numeric section
					for (int k = 0; k <= 9; k++) {
						if (StringMatcher.match(String.valueOf(mArrayList.get(j).cityNameTag.charAt(0)), String.valueOf(k)))
							return j;
					}
				} else {
					if (StringMatcher.match(String.valueOf(mArrayList.get(j).cityNameTag.charAt(0)), String.valueOf(mSections.charAt(i))))
						return j;
				}
			}
		}
		return 0;
	}

	@Override
	public int getSectionForPosition(int position) {
		return 0;
	}

	@Override
	public Object[] getSections() {
		String[] sections = new String[mSections.length()];
		for (int i = 0; i < mSections.length(); i++)
			sections[i] = String.valueOf(mSections.charAt(i));
		return sections;
	}
}
