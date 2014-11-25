/*
 * @Title:  PassengerAdapter.java
 * @Copyright:  XXX Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * @Description:  TODO<请描述此文件是做什么的>
 * @author:  MeiYi
 * @data:  2014年7月15日 下午5:57:41
 * @version:  V1.0
 */
package com.inter.trade.ui.fragment.airticket.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.inter.trade.R;
import com.inter.trade.ui.fragment.airticket.util.PassengerData;
import com.inter.trade.ui.fragment.airticket.util.PassengerInfoUtils;

/**
 * 订单中的乘机人适配器
 * 
 * @author ChenGuangChi
 * @data: 2014年7月15日 下午5:57:41
 * @version: V1.0
 */
public class OrderPassengerAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<PassengerData> mList;
	private ListViewButtonListener mListener;
	private ArrayList<PassengerData> sList=new ArrayList<PassengerData>();//選中的集合

	public OrderPassengerAdapter(Context context, ArrayList<PassengerData> mList,
			ListViewButtonListener mListener) {
		super();
		this.context = context;
		this.mList = mList;
		this.mListener = mListener;
	}

	@Override
	public int getCount() {
		if (mList == null) {
			return 0;
		}
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = View.inflate(context,
					R.layout.item_order_passenger_listview, null);
			holder = new ViewHolder();
			holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
			holder.tvType = (TextView) convertView.findViewById(R.id.tv_type);
			holder.tvNo = (TextView) convertView.findViewById(R.id.tv_no);
			holder.btnUpdate = (Button) convertView
					.findViewById(R.id.btn_update);
			holder.cbSelect = (CheckBox) convertView
					.findViewById(R.id.cb_select);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		PassengerData passenger = mList.get(position);
		holder.tvName.setText(passenger.getName());
		holder.tvType.setText(PassengerInfoUtils.transferTypeToString(passenger
				.getIdtype()));
		holder.tvNo.setText(passenger.getPassportNo());
		holder.btnUpdate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mListener != null) {
					mListener.clickAtPosition(position);
				}
			}
		});
		holder.cbSelect
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							sList.add(mList.get(position));
						} else {
							if (sList.contains(mList.get(position))) {
								sList.remove(mList.get(position));
							}
						}
					}
				});
		return convertView;
	}

	private class ViewHolder {
		TextView tvName;
		TextView tvType;
		TextView tvNo;
		Button btnUpdate;
		CheckBox cbSelect;
	}
	/**
	 * 獲取選中的集合 
	 * @return
	 * @throw
	 * @return ArrayList<PassengerData>
	 */
	public ArrayList<PassengerData> getSelectedList(){
		return sList;
	}

	public interface ListViewButtonListener {
		/**
		 * ListView中的按钮的点击监听
		 * 
		 * @param position
		 * @throw
		 * @return void
		 */
		void clickAtPosition(int position);
	}
}
