package com.inter.trade.ui.fragment.hotel.adapter;

import java.util.ArrayList;

import com.inter.trade.R;
import com.inter.trade.ui.IndexFunc;
import com.inter.trade.ui.creditcard.util.CreditcardInfoUtil;
import com.inter.trade.ui.fragment.cridet.data.CridetHistoryData;

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
 * 酒店列表， 区域等关键字筛选Adapter
 * @author Lihaifeng
 *
 */
public class HotelListKeywordAdapter extends BaseAdapter{
	private Context mContext;
	private LayoutInflater mInflater;
	private Resources mResources;
	private ArrayList<String> mArrayList;
	
	public HotelListKeywordAdapter(Context context,ArrayList<String> datas){
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
			convertView = mInflater.inflate(R.layout.hotel_list_keyword_item_layout, null);
			mHolder = new Holder();
			mHolder.keyword_name = (TextView)convertView.findViewById(R.id.keyword_name);
			convertView.setTag(mHolder);
		}else {
			mHolder = (Holder)convertView.getTag();
		}
		String data = mArrayList.get(arg0);
		
		mHolder.keyword_name.setText(data);
		
		if (arg0 == selectItem) {  
            convertView.setBackgroundColor(0xFFADDFF9);//半透明
        }   
        else {  
            convertView.setBackgroundColor(0xff7FCDF3);
        } 
		return convertView;
	}
	class Holder{
		TextView keyword_name;
	}
	
	public  void setSelectItem(int selectItem) {  
         this.selectItem = selectItem;  
    }  
    private int  selectItem=-1;  
}
