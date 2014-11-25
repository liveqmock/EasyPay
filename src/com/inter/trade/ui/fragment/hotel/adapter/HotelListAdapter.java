package com.inter.trade.ui.fragment.hotel.adapter;

import java.util.ArrayList;

import net.tsz.afinal.FinalBitmap;

import com.inter.trade.R;
import com.inter.trade.log.Logger;
import com.inter.trade.ui.fragment.hotel.data.HotelListData;
import com.inter.trade.ui.IndexFunc;
import com.inter.trade.ui.creditcard.util.CreditcardInfoUtil;
import com.inter.trade.ui.fragment.cridet.data.CridetHistoryData;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 酒店列表Adapter
 * @author Lihaifeng
 *
 */
public class HotelListAdapter extends BaseAdapter{
	private Context mContext;
	private LayoutInflater mInflater;
	private Resources mResources;
	private ArrayList<HotelListData> mArrayList;
	ImageLoader imageLoad;
	DisplayImageOptions options;
	
	public HotelListAdapter(Context context,ArrayList<HotelListData> datas){
		this.mContext = context;
		mInflater = LayoutInflater.from(mContext);
		mResources=context.getResources();
		mArrayList= datas;
		
//		options = new DisplayImageOptions.Builder()
//		.showImageOnLoading(R.drawable.electric_rate_uesless) // resource or
//												// drawable
//		.showImageForEmptyUri(R.drawable.electric_rate_uesless) // resource or
//													// drawable
//		.showImageOnFail(R.drawable.electric_rate_uesless) // resource or
//												// drawable
//		.resetViewBeforeLoading(false) // default
//		.cacheInMemory(true) // default
//		.cacheOnDisk(false) // default
//		.considerExifParams(false) // default
//		.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
////		.bitmapConfig(Bitmap.Config.ARGB_8888) // default
////		.displayer(new SimpleBitmapDisplayer()) // default
////		.handler(new Handler()) // default
//		.build();
//		this.imageLoad = ImageLoader.getInstance();
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
			convertView = mInflater.inflate(R.layout.hotel_list_item_layout, null);
			mHolder = new Holder();
			mHolder.tv_hotel_name = (TextView)convertView.findViewById(R.id.tv_hotel_name);
			mHolder.tv_hotel_price = (TextView)convertView.findViewById(R.id.tv_hotel_price);
			mHolder.tv_hotel_star = (TextView)convertView.findViewById(R.id.tv_hotel_star);
			mHolder.tv_hotel_score = (TextView)convertView.findViewById(R.id.tv_hotel_score);
			mHolder.tv_hotel_address = (TextView)convertView.findViewById(R.id.tv_hotel_address);
			mHolder.img_hotel = (ImageView)convertView.findViewById(R.id.img_hotel);
			mHolder.img_hotel_wifi = (ImageView)convertView.findViewById(R.id.img_hotel_wifi);
			mHolder.img_hotel_park = (ImageView)convertView.findViewById(R.id.img_hotel_park);
			convertView.setTag(mHolder);
		}else {
			mHolder = (Holder)convertView.getTag();
		}
		HotelListData data = mArrayList.get(arg0);
		
		mHolder.tv_hotel_name.setText(data.getHotelName());
		mHolder.tv_hotel_price.setText("￥"+data.getMinPrice());
		mHolder.tv_hotel_star.setText(data.getHotelStar());
		mHolder.tv_hotel_score.setText(data.getHotelScore()+"分");
		mHolder.tv_hotel_address.setText(data.getHotelAddress());

		if(data != null && data.imageUrl != null){
			Logger.d("HotelListAdapter loading", data.imageUrl+"");
//			this.imageLoad.displayImage(data.imageUrl, mHolder.img_hotel,options); // 通过URL判断图片是否已经下载
			FinalBitmap.create(mContext).display(mHolder.img_hotel, data.imageUrl);
			Logger.d("HotelListAdapter loaded", data.imageUrl+"");
		}
		
//		if("1".equals(data.bkcardisdefault)){
//			mHolder.img_hotel_wifi.setVisibility(View.VISIBLE);
//		}else {
//			mHolder.img_hotel_wifi.setVisibility(View.INVISIBLE);
//		}
//		mHolder.img_hotel.setBackgroundDrawable(mContext.getResources().getDrawable(CreditcardInfoUtil.getDrawableOfBigBank(data.bkcardbanklogo)));
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
