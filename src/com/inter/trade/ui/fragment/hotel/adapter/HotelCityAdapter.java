package com.inter.trade.ui.fragment.hotel.adapter;

import java.util.ArrayList;
import java.util.List;

import com.inter.trade.R;
import com.inter.trade.ui.fragment.hotel.data.HotelGetCityData;
//import com.inter.trade.ui.fragment.agent.util.MyExplandableListView.ExpandInfoAdapter.Holder;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 酒店预订 城市选择Adapter
 * @author Lihaifeng
 *
 */
public class HotelCityAdapter extends BaseExpandableListAdapter{
	private Context mContext;
	private LayoutInflater mInflater;
	private Resources mResources;
	private List<String> mGroupList;
	private ArrayList<ArrayList<HotelGetCityData>> mChildList;

	LinearLayout mGroupLayout;
	
//	String[] str_group_items_ = {"销售收益", "娱乐类收益", "便民类收益"};
//	String[] str_group_items_2= {"800.00", "428.00", "4280.00"};
//	String[][] str_child_items_ = {{}, {"手机充值", "Q币充值", "游戏充值"}, {"水电煤充值"}};
//	String[][] str_child_items_2 = {{}, {"200.00", "200.00", "28.00"}, {"4280.00"}};
	
	
	public HotelCityAdapter(Context context, List<String> groupList, ArrayList<ArrayList<HotelGetCityData>> childList){
		this.mContext = context;
		mInflater = LayoutInflater.from(mContext);
		mResources=context.getResources();
		mGroupList = groupList;
		mChildList = childList;
	}
//++++++++++++++++++++++++++++++++++++++++++++
// child's stub

@Override
public Object getChild(int groupPosition, int childPosition) {
	// TODO Auto-generated method stub
//	return str_child_items_[groupPosition][childPosition];
	return mChildList.get(groupPosition).get(childPosition);
}

@Override
public long getChildId(int groupPosition, int childPosition) {
	// TODO Auto-generated method stub
	return childPosition;
}

@Override
public int getChildrenCount(int groupPosition) {
	// TODO Auto-generated method stub
//	return str_child_items_[groupPosition].length;
	return mChildList.size()>0 ? mChildList.get(groupPosition).size() : 0;
}

@Override
public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
		ViewGroup parent) {
	// TODO Auto-generated method stub
//	TextView txt_child;
//	if(null == convertView){
//		convertView = LayoutInflater.from(context_).inflate(R.layout.child_item, null);  
//	}
//	/*判断是否是最后一项，最后一项设计特殊的背景*/
//	if(isLastChild){
//		convertView.setBackgroundResource(R.drawable.child_end);
//	} else {
//		convertView.setBackgroundResource(R.drawable.child);
//	}
	
	Holder mHolder = null;
//	mInflater = LayoutInflater.from(context_);
	if(convertView == null)
	{
		convertView = mInflater.inflate(R.layout.airticket_expandable_listview_child_item, null);
		mHolder = new Holder();
		mHolder.title = (TextView)convertView.findViewById(R.id.childTitle);
		mHolder.money = (TextView)convertView.findViewById(R.id.childMoney);
		convertView.setTag(mHolder);
	}else {
		mHolder = (Holder)convertView.getTag();
	}
//	AgentRecordData data = mArrayList.get(arg0);
//	mHolder.title.setText(data.name);
//	mHolder.money.setText(data.date);
	
//	mHolder.title.setText(str_child_items_[groupPosition][childPosition]);
//	mHolder.money.setText("￥"+str_child_items_2[groupPosition][childPosition]);
	mHolder.title.setText(mChildList.get(groupPosition).get(childPosition).cityNameCh);
	mHolder.money.setText(mChildList.get(groupPosition).get(childPosition).cityCode);
	
//	txt_child = (TextView)convertView.findViewById(R.id.id_child_txt);
//	txt_child.setText(str_child_items_[groupPosition][childPosition]);

	return convertView;
}
		
//++++++++++++++++++++++++++++++++++++++++++++
// group's stub

@Override
public Object getGroup(int groupPosition) {
	// TODO Auto-generated method stub
//	return str_group_items_[groupPosition];
	return mGroupList.get(groupPosition);
}

@Override
public int getGroupCount() {
	// TODO Auto-generated method stub
//	return str_group_items_.length;
	return mGroupList.size();
}

@Override
public long getGroupId(int groupPosition) {
	// TODO Auto-generated method stub
	return groupPosition;
}

@Override
public View getGroupView(int groupPosition, boolean isExpanded,  View convertView, ViewGroup parent) {
	// TODO Auto-generated method stub
//	TextView txt_group;
//	if(null == convertView){
//		convertView = LayoutInflater.from(context_).inflate(R.layout.agent_expandable_listview_group_item, null);  
//	}
	/*判断是否group张开，来分别设置背景图*/
//	if(isExpanded){
//		convertView.setBackgroundResource(R.drawable.group_e);
//	}else{
//		convertView.setBackgroundResource(R.drawable.group);
//	}
	
	Holder mHolder = null;
//	mInflater = LayoutInflater.from(context_);
	if(convertView == null)
	{
		convertView = mInflater.inflate(R.layout.airticket_expandable_listview_group_item, null);
		mHolder = new Holder();
		mHolder.title = (TextView)convertView.findViewById(R.id.groupTitle);
		mHolder.money = (TextView)convertView.findViewById(R.id.groupMoney);
		convertView.setTag(mHolder);
	}else {
		mHolder = (Holder)convertView.getTag();
	}
//	AgentRecordData data = mArrayList.get(arg0);
//	mHolder.title.setText(data.name);
//	mHolder.money.setText(data.date);
	
//	mHolder.title.setText(str_group_items_[groupPosition]);
//	mHolder.money.setText("￥"+str_group_items_2[groupPosition]);
	mHolder.title.setText(mGroupList.get(groupPosition));
//	mHolder.money.setText("￥"+mGroupList.get(groupPosition).allfenrun);
	
//	ImageView mgroupimage=(ImageView)convertView.findViewById(R.id.groupIndicator);
////	if(str_child_items_[groupPosition].length >0){
//	if(mChildList.size()>0 && mChildList.get(groupPosition).size() >0){
//		mgroupimage.setVisibility(View.VISIBLE);
//        if(isExpanded){
//            mgroupimage.setBackgroundResource(R.drawable.detailbtn);
//        }else{
//        	mgroupimage.setBackgroundResource(R.drawable.detailbtn2);
//        }
//	}else{
//		mgroupimage.setVisibility(View.INVISIBLE);
//	}
	
//    mgroupimage.setImageBitmap(mla);
//    if(!isExpanded){
//    	mgroupimage.setImageBitmap(mshou);
//    }
	
//	txt_group = (TextView)convertView.findViewById(R.id.groupTitle);
//	txt_group.setText(str_group_items_[groupPosition]);
//	txt_group = (TextView)convertView.findViewById(R.id.groupMoney);
//	txt_group.setText(str_group_items_[groupPosition]);
	
	return convertView;
}

class Holder{
	TextView title;
	TextView money;
}

    @Override
public boolean isChildSelectable(int arg0, int arg1) {
	// TODO Auto-generated method stub
	return true;
}

@Override
public boolean hasStableIds() {
	// TODO Auto-generated method stub
	return false;
}

}
