package com.inter.trade.ui.fragment.order;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.inter.trade.R;
import com.inter.trade.ui.PayActivity;
import com.inter.trade.ui.fragment.FragmentFactory;
import com.inter.trade.ui.fragment.PayFactory;
import com.inter.trade.ui.fragment.order.util.OrderData;

public class OrderWaitAdapter extends BaseAdapter{
	private Activity mContext;
	private LayoutInflater mInflater;
	private Resources mResources;
	private ArrayList<OrderData> mOrderDatas;
	
	public OrderWaitAdapter(Activity context,ArrayList<OrderData> datas){
		this.mContext = context;
		mInflater = LayoutInflater.from(mContext);
		mResources=context.getResources();
		mOrderDatas = datas;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mOrderDatas.size();
	}
	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return mOrderDatas.get(arg0);
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
			convertView = mInflater.inflate(R.layout.order_list_item, null);
			mHolder = new Holder();
			mHolder.order_status_text = (TextView)convertView.findViewById(R.id.order_status_text);
			mHolder.order_no_text = (TextView)convertView.findViewById(R.id.order_no_text);
			mHolder.order_date_text = (TextView)convertView.findViewById(R.id.order_date_text);
			mHolder.order_count_text = (TextView)convertView.findViewById(R.id.order_count_text);
			mHolder.order_goods_count = (TextView)convertView.findViewById(R.id.order_goods_count);
			mHolder.order_btn = (Button)convertView.findViewById(R.id.order_btn);
			
			convertView.setTag(mHolder);
		}else {
			mHolder = (Holder)convertView.getTag();
		}
		final int temp = arg0;
		OrderData data = mOrderDatas.get(arg0);
		mHolder.order_status_text.setText(data.orderstate);
		mHolder.order_no_text.setText(data.orderno);
		mHolder.order_date_text.setText(data.ordertime);
		mHolder.order_count_text.setText("￥"+data.ordermoney);
		mHolder.order_goods_count.setText("共 "+data.orderpronum+" 件商品");
		mHolder.order_btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
					Intent intent = new Intent();
					intent.setClass(mContext, PayActivity.class);
					intent.putExtra(FragmentFactory.INDEX_KEY, PayFactory.PAY_METHOD__INDEX);
					OrderPayFragment.selectedIndex = temp;
					mContext.startActivityForResult(intent, 200);
			}
		});
		return convertView;
	}
	class Holder{
		TextView order_status_text;
		TextView order_no_text;
		TextView order_date_text;
		TextView order_count_text;
		TextView order_goods_count;
		Button order_btn;
	}
}
