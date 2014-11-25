/*
 * @Title:  SalaryGetHistoryAdapter.java
 * @Copyright:  XXX Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * @Description:  TODO<请描述此文件是做什么的>
 * @author:  MeiYi
 * @data:  2014年9月16日 上午8:56:29
 * @version:  V1.0
 */
package com.inter.trade.ui.fragment.salaryget.util;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.inter.trade.R;
import com.inter.trade.ui.fragment.salaryget.bean.SalaryGet;
import com.inter.trade.ui.fragment.salarypay.bean.SalaryData;
import com.inter.trade.util.ListUtils;

/**
 * 获取工资历史
 * @author  chenguangchi
 * @data:  2014年9月16日 上午8:56:29
 * @version:  V1.0
 */
public class SalaryGetHistoryAdapter extends BaseExpandableListAdapter {
	
	private Context context;
	private ArrayList<ArrayList<SalaryGet>> mList;
	
	public SalaryGetHistoryAdapter(Context context, ArrayList<ArrayList<SalaryGet>> mList) {
		super();
		this.context = context;
		this.mList = mList;
	}

	@Override
	public int getGroupCount() {
		if(mList!=null && mList.size()>0){
			return mList.size();
		}
		return 0;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return mList.get(groupPosition).size();
	}

	/**
	 * 重载方法
	 * @param groupPosition
	 * @return
	 */
	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 重载方法
	 * @param groupPosition
	 * @param childPosition
	 * @return
	 */
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 重载方法
	 * @param groupPosition
	 * @return
	 */
	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * 重载方法
	 * @param groupPosition
	 * @param childPosition
	 * @return
	 */
	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * 重载方法
	 * @return
	 */
	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * 重载方法
	 * @param groupPosition
	 * @param isExpanded
	 * @param convertView
	 * @param parent
	 * @return
	 */
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		ViewHolderGroup holder=null;
		ArrayList<SalaryGet> salaryList = mList.get(groupPosition);
		SalaryGet salaryGet=null;
		if(!ListUtils.isEmptyList(salaryList)){
			salaryGet=salaryList.get(0);
		}
		if(convertView==null){
			holder=new ViewHolderGroup();
			convertView=View.inflate(context, R.layout.item_salarypay_history, null);
			holder.tvDate=(TextView) convertView.findViewById(R.id.tv_date);
			holder.ivImage=(ImageView) convertView.findViewById(R.id.iv_image);
			holder.flContent=(RelativeLayout) convertView.findViewById(R.id.fl_content);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolderGroup) convertView.getTag();
		}
		
		if(salaryGet.anmiState==1){
			RotateAnimation anmi=new RotateAnimation(180f, 0f,holder.ivImage.getWidth()/2,holder.ivImage.getHeight()/2);
			anmi.setDuration(400);
			anmi.setFillAfter(true);
			holder.ivImage.startAnimation(anmi);
			if(holder.flContent!=null)
			holder.flContent.setBackgroundColor(context.getResources().getColor(R.color.game_gray));
		}else if(salaryGet.anmiState==2){
			RotateAnimation anmi=new RotateAnimation(0f, 180f,holder.ivImage.getWidth()/2,holder.ivImage.getHeight()/2);
			anmi.setDuration(400);
			anmi.setFillAfter(true);
			holder.ivImage.startAnimation(anmi);
			if(holder.flContent!=null)
			holder.flContent.setBackgroundColor(context.getResources().getColor(R.color.salarypay_bg_blue));
		}else{
			holder.ivImage.clearAnimation();
			if(holder.flContent!=null)
			holder.flContent.setBackgroundColor(context.getResources().getColor(R.color.game_gray));
		}
		if(salaryGet!=null){
			holder.tvDate.setText(salaryGet.wagemonth.replace("-", "年")+"月");
		}
		
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		ArrayList<SalaryGet> salaryList = mList.get(groupPosition);
		SalaryGet salaryGet = salaryList.get(childPosition);
		convertView =View.inflate(context, R.layout.item_salaryget_child, null);
		TextView tvMoney=(TextView) convertView.findViewById(R.id.tv_money);
		TextView tvState=(TextView) convertView.findViewById(R.id.tv_state);
		
		String str = context.getResources().getString(R.string.salaryget_history_list);
		String format = String.format(str, salaryGet.wagemoney);
		tvMoney.setText(format);
		
		tvState.setText(SalaryGetInfoUtils.getState(salaryGet.isqianshou));
		
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public Integer getAnmiState(int position){
		return mList.get(position).get(0).anmiState;
	}
	
	public void setAnmiStateAtPosition(int position,Integer state){
		SalaryGet salaryGet = mList.get(position).get(0);
		salaryGet.anmiState=state;
		mList.get(position).remove(0);
		mList.get(position).add(0, salaryGet);
	}
	
	private class ViewHolderGroup{
		TextView tvDate;
		ImageView ivImage;
		RelativeLayout flContent;
	}
	
}
