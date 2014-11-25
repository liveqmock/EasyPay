package com.inter.trade.ui.fragment.hotel.adapter;

import java.util.ArrayList;

import net.tsz.afinal.FinalBitmap;

import com.inter.trade.R;
import com.inter.trade.log.Logger;
import com.inter.trade.ui.fragment.hotel.data.HotelRoomData;
import com.inter.trade.ui.IndexFunc;
import com.inter.trade.ui.creditcard.util.CreditcardInfoUtil;
import com.inter.trade.ui.fragment.cridet.data.CridetHistoryData;
import com.inter.trade.ui.manager.core.IMainHandlerManager;
import com.inter.trade.ui.manager.core.UIConstantDefault;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 酒店详情 房型Adapter
 * @author Lihaifeng
 *
 */
public class HotelHouseTypeAdapter extends BaseAdapter{
	private Context mContext;
	private LayoutInflater mInflater;
	private Resources mResources;
	private ArrayList<HotelRoomData> mArrayList;
	
	public HotelHouseTypeAdapter(Context context,ArrayList<HotelRoomData> datas){
		this.mContext = context;
		mInflater = LayoutInflater.from(mContext);
		mResources=context.getResources();
		mArrayList= datas;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return (mArrayList==null)? 0: mArrayList.size();
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
			convertView = mInflater.inflate(R.layout.hotel_detail_house_type_item_layout, null);
			mHolder = new Holder();
			mHolder.room_name = (TextView)convertView.findViewById(R.id.room_name);
			mHolder.room_bedSize = (TextView)convertView.findViewById(R.id.room_bedSize);
			mHolder.room_price = (TextView)convertView.findViewById(R.id.room_price);
			mHolder.room_book = (Button)convertView.findViewById(R.id.room_book);
			mHolder.img_hotel = (ImageView)convertView.findViewById(R.id.img_hotel);
			convertView.setTag(mHolder);
		}else {
			mHolder = (Holder)convertView.getTag();
		}
		final HotelRoomData data = mArrayList.get(arg0);
		
		mHolder.room_name.setText(data.name);
		mHolder.room_bedSize.setText(data.bedSize);
		mHolder.room_price.setText(data.price);
		
		mHolder.room_book.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Bundle dataBundle = new Bundle();
				dataBundle.putSerializable("hotelOrder", data);
				IMainHandlerManager.handlerUI(UIConstantDefault.UI_CONSTANT_HOTEL_ORDER, 1, dataBundle);
			}
		});
		
		if(data != null && data.imageUrls != null){
			Logger.d("HotelHouseTypeAdapter loading", data.imageUrls[0]+"");
			FinalBitmap.create(mContext).display(mHolder.img_hotel, data.imageUrls[0]);
			Logger.d("HotelHouseTypeAdapter loaded", data.imageUrls[0]+"");
		}

		return convertView;
	}
	class Holder{
		TextView room_name;
		TextView room_bedSize;
		TextView room_price;
		Button room_book;
		ImageView img_hotel;
	}
}
