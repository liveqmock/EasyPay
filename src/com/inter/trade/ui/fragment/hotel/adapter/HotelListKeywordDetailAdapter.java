package com.inter.trade.ui.fragment.hotel.adapter;

import java.util.ArrayList;

import com.inter.trade.R;
import com.inter.trade.ui.IndexFunc;
import com.inter.trade.ui.creditcard.util.CreditcardInfoUtil;
import com.inter.trade.ui.fragment.cridet.data.CridetHistoryData;
import com.inter.trade.ui.fragment.hotel.data.HotelKeywordData;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 酒店列表， 区域等关键字筛选Adapter
 * @author Lihaifeng
 *
 */
public class HotelListKeywordDetailAdapter extends BaseAdapter{
	private Context mContext;
	private LayoutInflater mInflater;
	private Resources mResources;
	private ArrayList<ArrayList<HotelKeywordData>> mArrayLists;
	private ArrayList<HotelKeywordData> mArrayList;
	
	public HotelListKeywordDetailAdapter(Context context,ArrayList<ArrayList<HotelKeywordData>> datas, int index){
		this.mContext = context;
		mInflater = LayoutInflater.from(mContext);
		mResources=context.getResources();
		mArrayLists=datas;
		selectItem=index;
		if(selectItem < mArrayLists.size()){
			mArrayList= mArrayLists.get(selectItem);
		}
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mArrayList==null? 0: mArrayList.size();
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
			convertView = mInflater.inflate(R.layout.hotel_list_keyword_detail_item_layout, null);
			mHolder = new Holder();
			mHolder.keyword_name = (TextView)convertView.findViewById(R.id.keyword_name);
			convertView.setTag(mHolder);
		}else {
			mHolder = (Holder)convertView.getTag();
		}
		
		String data = mArrayList.get(arg0).getCityNameCh();
		
		mHolder.keyword_name.setText(data);
		return convertView;
	}
	class Holder{
		TextView keyword_name;
	}
	
	public  void setSelectItem(int selectItem) {  
        this.selectItem = selectItem;  
        if(selectItem < mArrayLists.size()){
			mArrayList= mArrayLists.get(selectItem);
		}
   }  
   private int  selectItem=-1;
}
