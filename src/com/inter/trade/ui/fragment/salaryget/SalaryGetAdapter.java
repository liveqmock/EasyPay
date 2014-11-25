/*
 * @Title:  SalaryGetAdapter.java
 * @Copyright:  XXX Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * @Description:  TODO<请描述此文件是做什么的>
 * @author:  MeiYi
 * @data:  2014年10月11日 下午5:21:13
 * @version:  V1.0
 */
package com.inter.trade.ui.fragment.salaryget;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.inter.trade.R;
import com.inter.trade.ui.fragment.salaryget.bean.SalaryGet;

/**
 * 签收列表
 * @author  chenguangchi
 * @data:  2014年10月11日 下午5:21:13
 * @version:  V1.0
 */
public class SalaryGetAdapter extends BaseAdapter {
	private Context context;
	
	private ArrayList<SalaryGet> mList;
	
	private SalaryGetListener listener;
	
	public SalaryGetAdapter(Context context, ArrayList<SalaryGet> mList,SalaryGetListener listener) {
		super();
		this.context = context;
		this.mList = mList;
		this.listener=listener;
	}

	@Override
	public int getCount() {
		if(mList!=null){
			return mList.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		SalaryGet salaryGet = mList.get(position);
		ViewHolder holder=null;
		if(convertView==null){
			holder=new ViewHolder();
			convertView=View.inflate(context, R.layout.item_salaryget_main, null);
			holder.tvMoney=(TextView) convertView.findViewById(R.id.tv_salary);
			holder.btnCommit=(Button) convertView.findViewById(R.id.btn_finish);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		
		holder.btnCommit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
					if(listener!=null){
						 listener.onClickButton(position);
					}
			}
		});
		
		
		if("1".equals(salaryGet.isqianshou)){//1：已签收 0：未签收，可以签收  -1：还未发完工资，还不可以签收
			holder.btnCommit.setEnabled(false);
			holder.btnCommit.setText("已签收");
			holder.tvMoney.setVisibility(View.VISIBLE);
		}else if("0".equals(salaryGet.isqianshou)){
			holder.btnCommit.setEnabled(true);
			holder.btnCommit.setText("签收");
			holder.tvMoney.setVisibility(View.VISIBLE);
		}else if("-1".equals(salaryGet.isqianshou)){
			holder.btnCommit.setEnabled(false);
			holder.btnCommit.setText("当前的月份工资还未发放,暂不可签收");
			holder.tvMoney.setVisibility(View.INVISIBLE);
		}else{
			holder.btnCommit.setEnabled(true);
			holder.btnCommit.setText("签收");
		}
		holder.tvMoney.setText("￥"+salaryGet.wagemoney);
	
		return convertView;
	}
	
	
	private class ViewHolder{
		TextView tvMoney;
		Button btnCommit;
	}
	
	public interface SalaryGetListener{
		public void onClickButton(int position);
	}
}
