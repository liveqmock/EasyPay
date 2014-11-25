package com.inter.trade.ui.fragment.agent.util;

import com.inter.trade.R;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 自定义ExplandableListView
* Title: MyExplandableListView
* Description:
* Company: iceTree
* @author    zoeice
* @date       2012-7-9
 */
public class MyExplandableListView extends ExpandableListView{ 
//			implements OnChildClickListener,OnGroupClickListener{
	
//	ExpandInfoAdapter adapter;
	private Context context_;
	private LayoutInflater mInflater;
	
//	String[] str_group_items_ = {"销售收益", "娱乐类收益", "便民类收益"};
//	String[] str_group_items_2= {"800.00", "428.00", "4280.00"};
//	String[][] str_child_items_ = {{}, {"手机充值", "Q币充值", "游戏充值"}, {"水电煤充值"}};
//	String[][] str_child_items_2 = {{}, {"200.00", "200.00", "28.00"}, {"4280.00"}};
	
	public MyExplandableListView(Context context) {
		super(context);
		init(context);
		
	}
	
	
    
	public MyExplandableListView(Context context, AttributeSet attrs) {
//		this(context);
		super(context, attrs);
		init(context);
		// TODO Auto-generated constructor stub
	}

	private void init(Context context) {
		context_ = context;
		/* 隐藏默认箭头显示 */
		this.setGroupIndicator(null);
		/* 隐藏垂直滚动条 */
		this.setVerticalScrollBarEnabled(false);
		
		/* 监听child，group点击事件 */
//		this.setOnChildClickListener(this);
//		this.setOnGroupClickListener(this);
		
		setCacheColorHint(Color.TRANSPARENT);
		setDividerHeight(0);
		setChildrenDrawnWithCacheEnabled(false);
		setGroupIndicator(null);
		
		/*隐藏选择的黄色高亮*/
		ColorDrawable drawable_tranparent_ = new ColorDrawable(Color.TRANSPARENT);
		setSelector(drawable_tranparent_);
		
		/*设置adapter*/
//		mInflater = LayoutInflater.from(context_);
//		adapter = new ExpandInfoAdapter();
//		setAdapter(adapter);
		
	}

//	public class ExpandInfoAdapter extends BaseExpandableListAdapter {
//	    	LinearLayout mGroupLayout;
//    	
//    	//++++++++++++++++++++++++++++++++++++++++++++
//    	// child's stub
//    	
//		@Override
//		public Object getChild(int groupPosition, int childPosition) {
//			// TODO Auto-generated method stub
//			return str_child_items_[groupPosition][childPosition];
//		}
//
//		@Override
//		public long getChildId(int groupPosition, int childPosition) {
//			// TODO Auto-generated method stub
//			return childPosition;
//		}
//		
//		@Override
//		public int getChildrenCount(int groupPosition) {
//			// TODO Auto-generated method stub
//			return str_child_items_[groupPosition].length;
//		}
//		
//		@Override
//		public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
//				ViewGroup parent) {
//			// TODO Auto-generated method stub
////			TextView txt_child;
////			if(null == convertView){
////				convertView = LayoutInflater.from(context_).inflate(R.layout.child_item, null);  
////			}
////			/*判断是否是最后一项，最后一项设计特殊的背景*/
////			if(isLastChild){
////				convertView.setBackgroundResource(R.drawable.child_end);
////			} else {
////				convertView.setBackgroundResource(R.drawable.child);
////			}
//			
//			Holder mHolder = null;
////			mInflater = LayoutInflater.from(context_);
//			if(convertView == null)
//			{
//				convertView = mInflater.inflate(R.layout.agent_expandable_listview_child_item, null);
//				mHolder = new Holder();
//				mHolder.title = (TextView)convertView.findViewById(R.id.childTitle);
//				mHolder.money = (TextView)convertView.findViewById(R.id.childMoney);
//				convertView.setTag(mHolder);
//			}else {
//				mHolder = (Holder)convertView.getTag();
//			}
////			AgentRecordData data = mArrayList.get(arg0);
////			mHolder.title.setText(data.name);
////			mHolder.money.setText(data.date);
//			mHolder.title.setText(str_child_items_[groupPosition][childPosition]);
//			mHolder.money.setText("￥"+str_child_items_2[groupPosition][childPosition]);
//			
////			txt_child = (TextView)convertView.findViewById(R.id.id_child_txt);
////			txt_child.setText(str_child_items_[groupPosition][childPosition]);
//
//			return convertView;
//		}
//				
//		//++++++++++++++++++++++++++++++++++++++++++++
//    	// group's stub
//		
//		@Override
//		public Object getGroup(int groupPosition) {
//			// TODO Auto-generated method stub
//			return str_group_items_[groupPosition];
//		}
//
//		@Override
//		public int getGroupCount() {
//			// TODO Auto-generated method stub
//			return str_group_items_.length;
//		}
//
//		@Override
//		public long getGroupId(int groupPosition) {
//			// TODO Auto-generated method stub
//			return groupPosition;
//		}
//
//		@Override
//		public View getGroupView(int groupPosition, boolean isExpanded,  View convertView, ViewGroup parent) {
//			// TODO Auto-generated method stub
////			TextView txt_group;
////			if(null == convertView){
////				convertView = LayoutInflater.from(context_).inflate(R.layout.agent_expandable_listview_group_item, null);  
////			}
//			/*判断是否group张开，来分别设置背景图*/
////			if(isExpanded){
////				convertView.setBackgroundResource(R.drawable.group_e);
////			}else{
////				convertView.setBackgroundResource(R.drawable.group);
////			}
//			
//			Holder mHolder = null;
////			mInflater = LayoutInflater.from(context_);
//			if(convertView == null)
//			{
//				convertView = mInflater.inflate(R.layout.agent_expandable_listview_group_item, null);
//				mHolder = new Holder();
//				mHolder.title = (TextView)convertView.findViewById(R.id.groupTitle);
//				mHolder.money = (TextView)convertView.findViewById(R.id.groupMoney);
//				convertView.setTag(mHolder);
//			}else {
//				mHolder = (Holder)convertView.getTag();
//			}
////			AgentRecordData data = mArrayList.get(arg0);
////			mHolder.title.setText(data.name);
////			mHolder.money.setText(data.date);
//			mHolder.title.setText(str_group_items_[groupPosition]);
//			mHolder.money.setText("￥"+str_group_items_2[groupPosition]);
//			
//			ImageView mgroupimage=(ImageView)convertView.findViewById(R.id.groupIndicator);
//			if(str_child_items_[groupPosition].length >0){
////				ImageView mgroupimage=(ImageView)convertView.findViewById(R.id.groupIndicator);
//	            if(isExpanded){
//	                mgroupimage.setBackgroundResource(R.drawable.detailbtn);
//	            }else{
//	            	mgroupimage.setBackgroundResource(R.drawable.detailbtn2);
//	            }
//			}else{
//				mgroupimage.setVisibility(View.INVISIBLE);
//			}
//			
////            mgroupimage.setImageBitmap(mla);
////            if(!isExpanded){
////            	mgroupimage.setImageBitmap(mshou);
////            }
//			
////			txt_group = (TextView)convertView.findViewById(R.id.groupTitle);
////			txt_group.setText(str_group_items_[groupPosition]);
////			txt_group = (TextView)convertView.findViewById(R.id.groupMoney);
////			txt_group.setText(str_group_items_[groupPosition]);
//			
//			return convertView;
//		}
//		
//		class Holder{
//			TextView title;
//			TextView money;
//		}
//		
//	        @Override
//		public boolean isChildSelectable(int arg0, int arg1) {
//			// TODO Auto-generated method stub
//			return true;
//		}
//		
//		@Override
//		public boolean hasStableIds() {
//			// TODO Auto-generated method stub
//			return false;
//		}
//
//	}
	
//	@Override
//	public boolean onChildClick(ExpandableListView parent, View v,
//			int groupPosition, int childPosition, long id) {
//		// TODO Auto-generated method stub
//		Toast.makeText(getContext(), "你点击了第" + (groupPosition + 1) + "组的第" + (childPosition + 1) + "条！", 2000).show();
//		return false;
//	}
//	
//	@Override
//	public boolean onGroupClick(ExpandableListView parent, View v,
//			int groupPosition, long id) {
//		// TODO Auto-generated method stub
//		return false;
//	}

}
