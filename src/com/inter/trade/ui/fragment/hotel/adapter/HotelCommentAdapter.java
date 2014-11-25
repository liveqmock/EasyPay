package com.inter.trade.ui.fragment.hotel.adapter;

import java.util.ArrayList;

import com.inter.trade.R;
import com.inter.trade.ui.fragment.hotel.data.HotelListData;
import com.inter.trade.ui.IndexFunc;
import com.inter.trade.ui.creditcard.util.CreditcardInfoUtil;
import com.inter.trade.ui.fragment.cridet.data.CridetHistoryData;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 酒店详情 评论Adapter
 * @author Lihaifeng
 *
 */
public class HotelCommentAdapter extends BaseAdapter{
	private Context mContext;
	private LayoutInflater mInflater;
	private Resources mResources;
	private ArrayList<HotelListData> mArrayList;
	
	public HotelCommentAdapter(Context context,ArrayList<HotelListData> datas){
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
			convertView = mInflater.inflate(R.layout.hotel_detail_comment_item_layout, null);
			mHolder = new Holder();
//			mHolder.tv_hotel_name = (TextView)convertView.findViewById(R.id.tv_hotel_name);
//			mHolder.tv_hotel_price = (TextView)convertView.findViewById(R.id.tv_hotel_price);
//			mHolder.tv_hotel_star = (TextView)convertView.findViewById(R.id.tv_hotel_star);
//			mHolder.tv_hotel_score = (TextView)convertView.findViewById(R.id.tv_hotel_score);
//			mHolder.tv_hotel_address = (TextView)convertView.findViewById(R.id.tv_hotel_address);
//			mHolder.img_hotel = (ImageView)convertView.findViewById(R.id.img_hotel);
//			mHolder.img_hotel_wifi = (ImageView)convertView.findViewById(R.id.img_hotel_wifi);
//			mHolder.img_hotel_park = (ImageView)convertView.findViewById(R.id.img_hotel_park);
			convertView.setTag(mHolder);
		}else {
			mHolder = (Holder)convertView.getTag();
		}
		HotelListData data = mArrayList.get(arg0);
		
//		mHolder.tv_hotel_name.setText(data.getHotelName());
//		mHolder.tv_hotel_price.setText("￥"+data.getMinPrice());
//		mHolder.tv_hotel_star.setText(data.getHotelStar());
//		mHolder.tv_hotel_score.setText(data.getHotelScore()+"分");
//		mHolder.tv_hotel_address.setText(data.getHotelAddress());

		return convertView;
	}
	class Holder{
		TextView tv_hotel_name;
		TextView tv_hotel_price;
		TextView tv_hotel_star;
		TextView tv_hotel_score;
		TextView tv_hotel_address;
		ImageView img_hotel;
		ImageView img_hotel_wifi;
		ImageView img_hotel_park;
	}
}
