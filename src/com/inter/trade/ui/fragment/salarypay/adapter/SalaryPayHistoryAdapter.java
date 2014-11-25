package com.inter.trade.ui.fragment.salarypay.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.text.TextUtils;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.inter.trade.R;
import com.inter.trade.ui.fragment.salarypay.bean.SalaryData;
import com.inter.trade.ui.fragment.salarypay.bean.Stuff;

/**
 * 发工资历史记录 
 * @author  chenguangchi
 * @data:  2014年9月4日 下午4:24:12
 * @version:  V1.0
 */
public class SalaryPayHistoryAdapter extends BaseExpandableListAdapter {
	
	
	private Context context;
	
	private ArrayList<SalaryData> mDatas;
	
	
	public SalaryPayHistoryAdapter(Context context,ArrayList<SalaryData> mDatas) {
		super();
		this.context = context;
		this.mDatas=mDatas; 
	}

	@Override
	public int getGroupCount() {
		
		if(mDatas!=null){
			return mDatas.size();
		}
		return 0;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		
		if(mDatas.get(groupPosition).stuffList!=null){
			return mDatas.get(groupPosition).stuffList.size()+2;
		}
		return 0; 
	}

	@Override
	public Object getGroup(int groupPosition) {
		return mDatas.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return mDatas.get(groupPosition).stuffList.get(childPosition);
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
		
		ViewHolderGroup holder=null;
		SalaryData salaryData = mDatas.get(groupPosition);
		
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
		
		if(salaryData.anmiState==1){
			RotateAnimation anmi=new RotateAnimation(180f, 0f,holder.ivImage.getWidth()/2,holder.ivImage.getHeight()/2);
			anmi.setDuration(400);
			anmi.setFillAfter(true);
			holder.ivImage.startAnimation(anmi);
			if(holder.flContent!=null)
			holder.flContent.setBackgroundColor(context.getResources().getColor(R.color.game_gray));
		}else if(salaryData.anmiState==2){
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
		if(salaryData!=null){
			holder.tvDate.setText(salaryData.wagemonth.replace("-", "年")+"月");
		}
		
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		ViewHolderChild holder=null;
		Stuff stuff=null;
		
		SalaryData salaryData = mDatas.get(groupPosition);
		if(childPosition>=1){
			convertView=View.inflate(context, R.layout.item_salarypay_child_two, null);
			holder=new ViewHolderChild();
			holder.tvPhone=(TextView) convertView.findViewById(R.id.tv_phone);
			holder.tvName=(TextView) convertView.findViewById(R.id.tv_name);
			holder.tvMoney=(TextView) convertView.findViewById(R.id.tv_money);
			convertView.setTag(holder);
		}else if(childPosition==0){
			convertView=View.inflate(context, R.layout.item_salarypay_history_child, null);
			holder=new ViewHolderChild();
			holder.tvTip=(TextView) convertView.findViewById(R.id.tv_tip);
		}else{
			holder=(ViewHolderChild) convertView.getTag();
			if(holder.tvMoney==null){
				convertView=View.inflate(context, R.layout.item_salarypay_employer, null);
				holder=new ViewHolderChild();
				holder.tvPhone=(TextView) convertView.findViewById(R.id.tv_phone);
				holder.tvName=(TextView) convertView.findViewById(R.id.tv_name);
				holder.tvMoney=(TextView) convertView.findViewById(R.id.tv_money);
				convertView.setTag(holder);
			}
		}
		if(childPosition==1){
			holder.tvPhone.setText("手机号");
			holder.tvName.setText("姓名");
			holder.tvMoney.setText("本月工资");
		}else if(childPosition==0){
			String str = context.getResources().getString(R.string.salarypay_history_list);
			String formatStr = String.format(str, salaryData.wagestanum,salaryData.wageallmoney);
			holder.tvTip.setText(formatStr);
		}else{
			if(salaryData!=null){
				ArrayList<Stuff> stuffList = salaryData.stuffList;
				if(stuffList!=null && stuffList.size()>0){
					stuff = stuffList.get(childPosition-2);
				}
			}
			if(stuff!=null){
				holder.tvPhone.setText(stuff.mobile.substring(0, 3)+"***");
				if(TextUtils.isEmpty(stuff.staname)){
					stuff.staname="匿名";
				}
				holder.tvName.setText(stuff.staname);
				holder.tvMoney.setText("￥"+stuff.wagemoney);
			}
		}
		return convertView;
	}
	public Integer getAnmiState(int position){
		return mDatas.get(position).anmiState;
	}
	
	public void setAnmiStateAtPosition(int position,Integer state){
		SalaryData salaryData = mDatas.get(position);
		salaryData.anmiState=state;
		mDatas.set(position, salaryData);
	}
	
	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}
	private class ViewHolderGroup{
		TextView tvDate;
		ImageView ivImage;
		RelativeLayout flContent;
	}
	private class ViewHolderChild{
		TextView tvPhone;
		TextView tvName;
		TextView tvMoney;
		TextView tvTip;
	}
}
