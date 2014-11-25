
package com.inter.trade.ui.fragment.salaryget;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupExpandListener;

import com.inter.trade.R;
import com.inter.trade.ui.fragment.salaryget.bean.SalaryGet;
import com.inter.trade.ui.fragment.salaryget.task.GetPayGetListTask;
import com.inter.trade.ui.fragment.salaryget.util.MonthComparator;
import com.inter.trade.ui.fragment.salaryget.util.SalaryGetHistoryAdapter;
import com.inter.trade.ui.fragment.waterelectricgas.data.ResponseStateListener;

/**
 * 工资历史的页面
 * @author  chenguangchi
 * @data:  2014年9月2日 下午4:23:30
 * @version:  V1.0
 */
public class SalaryGetHistoryFragment extends SalaryGetBaseFragment  implements ResponseStateListener{ 
	
	private ExpandableListView listView;
	
	private SalaryGetHistoryAdapter adapter;
	
	
	private ArrayList<SalaryGet> mDatas;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	

	@Override
	public void onResume() {
		super.onResume();
		setTopTitle("签收历史");
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
		View view=inflater.inflate(R.layout.fragment_salaryget_history, null);
		initView(view);
		return view;
	}

	@Override
	protected void onAsyncLoadData() {
		new GetPayGetListTask(getActivity(), this).execute("All","all");
	}

	@Override
	public void onRefreshDatas() {
		
	}

	@Override
	public void onTimeout() {
		
	}



	@Override
	public void onSuccess(Object obj, Class cla) {
		mDatas=(ArrayList<SalaryGet>) obj;
		adapter=new SalaryGetHistoryAdapter(getActivity(), groupList(mDatas));
		listView.setAdapter(adapter);
	}
	
	private ArrayList<ArrayList<SalaryGet>> groupList(ArrayList<SalaryGet> datas){
		ArrayList<ArrayList<SalaryGet>> list=new ArrayList<ArrayList<SalaryGet>>();
		HashMap<String,ArrayList<SalaryGet>> map=new HashMap<String,ArrayList<SalaryGet>>();
		for(SalaryGet salary:datas){
			if(map.containsKey(salary.wagemonth)){
				ArrayList<SalaryGet> arrayList = map.get(salary.wagemonth);
				arrayList.add(salary);
				map.remove(salary.wagemonth);
				map.put(salary.wagemonth, arrayList);
			}else{
				ArrayList<SalaryGet> sList=new ArrayList<SalaryGet>();
				sList.add(salary);
				map.put(salary.wagemonth, sList);
			}
			
		}
		
		for(String month:map.keySet()){
			list.add(map.get(month));
		}
		Collections.sort(list, new MonthComparator());
		return list;
	}
	
}
