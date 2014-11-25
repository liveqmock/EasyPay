package com.inter.trade.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.inter.trade.R;
import com.inter.trade.data.ArriveData;
import com.inter.trade.ui.fragment.transfer.TransferFragment;
import com.inter.trade.ui.fragment.transfer.util.TransferData;

public class ArriveView {
	public View mView;
	private TextView arrive_date;
	private TextView arrive_remark;
	private RadioButton arrive_radio;
	private LinearLayout remark_layout;
	public boolean isSelected = false;
	public ArriveView(Context context,final ArriveData data,final TransferFragment fragment){
		LayoutInflater mInflater = LayoutInflater.from(context);
		mView = mInflater.inflate(R.layout.arrive_item, null);
		arrive_radio = (RadioButton)mView.findViewById(R.id.arrive_radio);
		arrive_date = (TextView)mView.findViewById(R.id.arrive_date);
		arrive_remark = (TextView)mView.findViewById(R.id.arrive_remark);
		remark_layout = (LinearLayout)mView.findViewById(R.id.remark_layout);
		
		if(null == data.activememo || "".equals(data.activememo)){
			remark_layout.setVisibility(View.GONE);
		}
		
		arrive_date.setText(data.arrivetime);
		arrive_remark.setText(data.activememo);
		if(data.activearriveid!=null && !data.activearriveid.equals("")){
			arrive_radio.setChecked(true);
			CommonActivity.mTransferData.putValue(TransferData.arrivetime, data.arrivetime);
			CommonActivity.mTransferData.putValue(TransferData.arriveid, data.arriveid);
		}else{
			arrive_radio.setChecked(false);
		}
		arrive_radio.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					isSelected=true;
					CommonActivity.mTransferData.putValue(TransferData.arriveid, data.arriveid);
					CommonActivity.mTransferData.putValue(TransferData.arrivetime, data.arrivetime);
					fragment.mfeeData.sunMap.put("arriveid", data.arriveid);
					fragment.initArrive();
				}else{
					isSelected=false;
				}
			}
		});
	}
	public boolean isSelected(){
		return arrive_radio.isChecked();
	}
	public void setChecked(boolean flag)
	{
		arrive_radio.setChecked(flag);
	}
}
