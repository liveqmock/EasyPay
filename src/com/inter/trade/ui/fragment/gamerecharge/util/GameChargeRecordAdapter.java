package com.inter.trade.ui.fragment.gamerecharge.util;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.inter.trade.R;
import com.inter.trade.ui.fragment.gamerecharge.data.GameRecordListData;

public class GameChargeRecordAdapter extends BaseExpandableListAdapter {
	
	private Context context;
	
	private ArrayList<GameRecordListData> mList;
	
	public GameChargeRecordAdapter(Context context,ArrayList<GameRecordListData> mList) {
		super();
		this.context = context;
		this.mList=mList;
	}

	@Override
	public int getGroupCount() {
		return mList.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return 1;
	}

	@Override
	public Object getGroup(int groupPosition) {
		return null;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return null;
	}

	@Override
	public long getGroupId(int groupPosition) {
		return 0;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return 0;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
			GameRecordListData data = mList.get(groupPosition);
			ViewHolderGroup group=null;
			
			if(convertView==null){
				group=new ViewHolderGroup();
				convertView=View.inflate(context, R.layout.item_group_game_charge_record, null);
				group.tvGameName=(TextView) convertView.findViewById(R.id.tv_gamename);
				group.tvTotalCount=(TextView) convertView.findViewById(R.id.tv_game_price);
				group.tvDate=(TextView) convertView.findViewById(R.id.tv_game_date);
				convertView.setTag(group);
			}else{
				group=(ViewHolderGroup) convertView.getTag();
			}
			
			group.tvGameName.setText(data.getGameName());
			group.tvTotalCount.setText(data.getTotalPrice()+"元");
			String completeTime = data.getCompleteTime();
			completeTime=completeTime.substring(0, 10).replace("-", "/");
			group.tvDate.setText(completeTime);
		
		
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		GameRecordListData data = mList.get(groupPosition);
		ViewHolderChild child=new ViewHolderChild();
		
		View view=View.inflate(context, R.layout.item_child_game_charge_record, null);
		
		child.tvArea=(TextView) view.findViewById(R.id.tv_game_area);
		child.tvAccount=(TextView) view.findViewById(R.id.tv_game_account);
		child.tvQuantity=(TextView) view.findViewById(R.id.tv_game_quantity);
		child.tvPrice=(TextView) view.findViewById(R.id.tv_game_price);
		child.tvNO=(TextView) view.findViewById(R.id.tv_game_no);
		
		child.tvArea.setText(data.getArea());
		child.tvAccount.setText(data.getAccount());
		child.tvQuantity.setText(data.getQuantity());
		child.tvPrice.setText(data.getPrice());
		child.tvNO.setText(data.getAccount());
		
		return view;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}
	private class ViewHolderGroup{
		TextView tvGameName;
		TextView tvTotalCount;
		TextView tvDate;
	}
	private class ViewHolderChild{
		TextView tvArea;
		TextView tvAccount;
		TextView tvQuantity;
		TextView tvPrice;
		TextView tvNO;
	}
}
