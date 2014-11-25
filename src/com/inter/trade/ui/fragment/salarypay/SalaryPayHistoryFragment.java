
package com.inter.trade.ui.fragment.salarypay;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupExpandListener;

import com.inter.trade.R;
import com.inter.trade.ui.fragment.salarypay.adapter.SalaryPayHistoryAdapter;
import com.inter.trade.ui.fragment.salarypay.bean.SalaryData;
import com.inter.trade.ui.fragment.salarypay.task.GetSalaryHistoryTask;
import com.inter.trade.ui.fragment.waterelectricgas.data.ResponseStateListener;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PreferenceConfig;

/**
 * 工资历史的页面
 * @author  chenguangchi
 * @data:  2014年9月2日 下午4:23:30
 * @version:  V1.0
 */
public class SalaryPayHistoryFragment extends SalaryPayBaseFragment  implements ResponseStateListener{ 
	
	private ExpandableListView listView;
	
	private SalaryPayHistoryAdapter adapter;
	
	
	private ArrayList<SalaryData> mDatas;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	

	@Override
	public void onResume() {
		super.onResume();
		setTopTitle("发放历史");
	}



	private void initView(View view) {
		listView=(ExpandableListView) view.findViewById(R.id.lv_history);
		View empty=view.findViewById(R.id.tv_empty);
		listView.setEmptyView(empty);
		
		
		listView.setOnGroupExpandListener(new OnGroupExpandListener() {
			
			@Override
			public void onGroupExpand(int groupPosition) {
				for (int i = 0; i < adapter.getGroupCount(); i++) {
					if (groupPosition != i) {
						listView.collapseGroup(i);
					}
				}
			}
		});
		
		listView.setOnGroupClickListener(new OnGroupClickListener() {
			
			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int position, long id) {
				System.out.println(position);
				Integer anmiState = adapter.getAnmiState(position);
				if(anmiState==0||anmiState==1){
					adapter.setAnmiStateAtPosition(position, 2);
				}else if(anmiState==2){
					adapter.setAnmiStateAtPosition(position, 1);
				}
				for(int i=0;i<adapter.getGroupCount();i++){
					if(i!=position){
						adapter.setAnmiStateAtPosition(i, 0);
					}
				}
				
				adapter.notifyDataSetChanged();
				return false;
			}
		});
		
	}

	@Override
	protected View onInitView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.fragment_salarypay_history, null);
		initView(view);
		return view;
	}

	@Override
	protected void onAsyncLoadData() {
		String bossauthorid = PreferenceConfig.instance(getActivity()).getString("bossAuthorid", LoginUtil.mLoginStatus.authorid);
		new GetSalaryHistoryTask(getActivity(), this).execute("All","all",bossauthorid);
	}

	@Override
	public void onRefreshDatas() {
		
	}

	@Override
	public void onTimeout() {
		
	}



	@Override
	public void onSuccess(Object obj, Class cla) {
		if(SalaryData.class.equals(cla)){
			mDatas=(ArrayList<SalaryData>) obj;
			adapter = new SalaryPayHistoryAdapter(getActivity(),mDatas);
			listView.setAdapter(adapter);
		}
	}
	
}
