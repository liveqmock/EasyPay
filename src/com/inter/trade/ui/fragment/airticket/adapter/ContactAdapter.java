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
import android.widget.LinearLayout;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.inter.trade.R;
import com.inter.trade.ui.fragment.airticket.adapter.PassengerAdapter.ListViewButtonListener;
import com.inter.trade.ui.fragment.airticket.util.PassengerData;
import com.inter.trade.ui.fragment.airticket.util.PassengerInfoUtils;

/**
 *  联系人适配器
 * @author  ChenGuangChi
 * @data:  2014年7月15日 下午5:57:41
 * @version:  V1.0
 */
public class ContactAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<PassengerData> mList;
	private ArrayList<PassengerData> sList=new ArrayList<PassengerData>();//選中的集合
	private ListViewButtonListener mListener;
	public ContactAdapter(Context context,ArrayList<PassengerData> mList) {
		super();
		this.context = context;
		this.mList=mList;
	}
	
	public ContactAdapter(Context context,ArrayList<PassengerData> mList,
			ListViewButtonListener mListener) {
		super();
		this.context = context;
		this.mList = mList;
		this.mListener = mListener;
	}

	@Override
	public int getCount() {
		if(mList==null){
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
		ViewHolder holder=null;
	   if(convertView==null){
		   convertView=View.inflate(context, R.layout.item_contact_listview, null);
		   holder=new ViewHolder();
		   holder.passengerLayout = (LinearLayout) convertView.findViewById(R.id.passenger_layout);
		   holder.tvName=(TextView) convertView.findViewById(R.id.tv_name);
		   holder.tvPhone=(TextView) convertView.findViewById(R.id.tv_phone);
		   holder.cbSelect=(CheckBox) convertView.findViewById(R.id.cb_select);
		   holder.btnDelete = (Button) convertView
					.findViewById(R.id.btn_delete);
		   convertView.setTag(holder);
	   }else{
		   holder=(ViewHolder) convertView.getTag();
	   }
		PassengerData passenger = mList.get(position);
		holder.tvName.setText(passenger.getName());
		holder.tvPhone.setText(passenger.getPhoneNumber());
		holder.passengerLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				notifyDataSetChanged();
				if (!sList.contains(mList.get(position))){
					sList.add(mList.get(position));
				} else {
					sList.remove(mList.get(position));
				}
			}
		});
		holder.cbSelect.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					if (!sList.contains(mList.get(position))){
						sList.add(mList.get(position));
					}
				}else{
					if(sList.contains(mList.get(position))){
						sList.remove(mList.get(position));
					}
				}
			}
		});
		holder.btnDelete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mListener != null) {
					mListener.clickAtPosition(position);
				}
			}
		});
		
		if(passenger.isCheck()) {
			holder.cbSelect.setChecked(true);
		}
		
		if(sList.contains(mList.get(position))) {
			holder.cbSelect.setChecked(true);
		}else{
			holder.cbSelect.setChecked(false);
		}
		return convertView;
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
	
	private class ViewHolder{
		LinearLayout passengerLayout;
		TextView tvName;
		TextView tvPhone;
		CheckBox cbSelect;
		Button btnDelete;
	}
}
