package com.inter.trade.ui.fragment.express;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.inter.trade.R;
import com.inter.trade.ui.fragment.BaseFragment;
import com.inter.trade.ui.fragment.express.util.ExpressDetial.InnerStatus;
import com.inter.trade.util.LoginUtil;

/**
 * 订单查询结果
 * @author apple
 *
 */

public class ExpressQueryResultFragment extends BaseFragment{
	private LayoutInflater mInflater;
	public ExpressQueryResultFragment(){
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitle("快递详情");
		setBackVisible();
		LoginUtil.detection(getActivity());
		mInflater= LayoutInflater.from(getActivity());
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.express_result, container,false);
//		ListView mListView = (ListView)view.findViewById(R.id.result_listview);
		initResult(view);
		return view;
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setTitle("快递详情");
	}

	private void initResult(View view){
		try {
			LinearLayout containerLayout = (LinearLayout)view.findViewById(R.id.container);
			ArrayList<InnerStatus> mArrayList = ExpressDetialFragment.mEntity.mDetial.mInnerStatus;
			containerLayout.removeAllViews();
			for(int i =0;i<mArrayList.size();i++){
				Item item = new Item(mArrayList.get(i));
				containerLayout.addView(item.mView);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
	}
	class Item {
		private View mView;
		public Item(InnerStatus sList){
			mView = mInflater.inflate(R.layout.result_item, null);
			TextView name = (TextView)mView.findViewById(R.id.name);
			TextView content = (TextView)mView.findViewById(R.id.content);
				name.setText(sList.time);
				content.setText(sList.context);
		}
	}
}
