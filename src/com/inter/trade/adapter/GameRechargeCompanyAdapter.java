package com.inter.trade.adapter;

import java.util.ArrayList;

import com.inter.trade.R;
import com.inter.trade.ui.fragment.agent.util.AgentRecordData;
import com.inter.trade.ui.fragment.gamerecharge.data.CompanyListData;
import com.inter.trade.ui.fragment.gamerecharge.data.GameListData;
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
 * 游戏充值游戏列表Adapter
 * @author Lihaifeng
 *
 */
//public class WaterElectricGasCitySelectAdapter extends BaseAdapter implements SectionIndexer{
public class GameRechargeCompanyAdapter extends BaseAdapter{
	private Context mContext;
	private LayoutInflater mInflater;
	private Resources mResources;
	private ArrayList<CompanyListData> mArrayList;
//	private ArrayList<WaterElectricGasData> mArrayList;
	private String mSections = "#ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	
	public GameRechargeCompanyAdapter(Context context,ArrayList<CompanyListData> datas){
		this.mContext = context;
		mInflater = LayoutInflater.from(mContext);
		mResources=context.getResources();
		mArrayList= datas;
	}
	
//	public GameRechargeGameAdapter(Context context,ArrayList<WaterElectricGasData> datas){
//		this.mContext = context;
//		mInflater = LayoutInflater.from(mContext);
//		mResources=context.getResources();
//		mArrayList= datas;
//	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mArrayList.size();
	}
	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
//		return mArrayList.get(arg0).cityName;
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
			convertView = mInflater.inflate(R.layout.water_electric_gas_city_select_item, null);
			mHolder = new Holder();
			mHolder.cityName = (TextView)convertView.findViewById(R.id.city_name);
			convertView.setTag(mHolder);
		}else {
			mHolder = (Holder)convertView.getTag();
		}
		CompanyListData game = mArrayList.get(arg0);
		String city=game.getCompanyName();
//		String city = mArrayList.get(arg0).cityName;
		
		if(city == null || city ==""){
			return convertView;
		}
		
		mHolder.cityName.setText(city);
		return convertView;
	}
	class Holder{
		TextView cityName;
	}
	
}
