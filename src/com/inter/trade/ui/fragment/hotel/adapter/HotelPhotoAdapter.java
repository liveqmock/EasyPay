package com.inter.trade.ui.fragment.hotel.adapter;

import java.util.ArrayList;

import net.tsz.afinal.FinalBitmap;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.inter.trade.R;
import com.inter.trade.imageframe.ImageFetcher;
import com.inter.trade.log.Logger;
import com.inter.trade.ui.fragment.hotel.data.HotelImageData;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class HotelPhotoAdapter extends BaseAdapter {
	private Context mContext;
	private LayoutInflater mInflater;
	private Resources mResources;
	private ImageFetcher mFetcher;
	private ArrayList<HotelImageData> mFuncDatas;

	ImageLoader imageLoad;
	DisplayImageOptions options;
	LinearLayout gridViewItem;

	public HotelPhotoAdapter(Context context, ImageFetcher fetcher,
			ArrayList<HotelImageData> funcDatas) {
		this.mContext = context;
		mInflater = LayoutInflater.from(mContext);
		mResources = context.getResources();
		mFetcher = fetcher;
		mFuncDatas = funcDatas;
		
	}

	@Override
	public int getCount() {
//		return this.imageUrls.length;
		if(mFuncDatas!=null){
			return mFuncDatas.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		return mFuncDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// 判断这个image是否已经在缓存当中，如果没有就下载
		final ViewHolder holder;
//		if (convertView == null) {
			holder = new ViewHolder();
			gridViewItem = (LinearLayout) mInflater.inflate(
					R.layout.hotel_detail_photo_item_layout, null);
			holder.imageView = (ImageView) gridViewItem.findViewById(R.id.item_icon);
			gridViewItem.setTag(holder);
			convertView = gridViewItem;
//		} else {
//			holder = (ViewHolder) gridViewItem.getTag();
//		}
		
		HotelImageData data = mFuncDatas.get(position);
		
		if(data != null && data.url != null){
			Logger.d("HotelPhotoAdapter loading", data.url+"");
			FinalBitmap.create(mContext).display(holder.imageView, data.url);
			Logger.d("HotelPhotoAdapter loaded", data.url+"");
		}
		
		return convertView;
	}

	static class ViewHolder {
		ImageView imageView;
		ProgressBar progressBar;
	}
}
