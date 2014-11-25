package com.inter.trade.ui.fragment.transfer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.inter.trade.R;
import com.inter.trade.data.CommonData;
import com.inter.trade.ui.fragment.BaseFragment;
import com.inter.trade.ui.fragment.FragmentFactory;
import com.inter.trade.ui.fragment.FragmentUtil;
import com.inter.trade.util.LoginUtil;

/**
 * 详情
 * @author apple
 *
 */

public class RecordDetialFragment extends BaseFragment{
	private LayoutInflater mInflater;
	public static ArrayList<HashMap<String, String>> mMaps = new ArrayList<HashMap<String,String>>();
	
	public static RecordDetialFragment createFragment(ArrayList<HashMap<String, String>> cData){
		RecordDetialFragment fragment = new RecordDetialFragment();
		mMaps = cData;
		return fragment;
	}
	public RecordDetialFragment(){
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitle("交易详情");
		setBackVisible();
		mInflater= LayoutInflater.from(getActivity());
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		LoginUtil.detection(getActivity());
		View view = inflater.inflate(R.layout.cridet_list_back_success, container,false);
//		ListView mListView = (ListView)view.findViewById(R.id.result_listview);
		initResult(view);
		return view;
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setTitle("交易详情");
	}
	private void initResult(View view){
		LinearLayout containerLayout = (LinearLayout)view.findViewById(R.id.container);
		containerLayout.removeAllViews();
		for(int i =0;i<mMaps.size();i++){
			Item item = new Item(mMaps.get(i));
			containerLayout.addView(item.mView);
		}
		
		Button see_history = (Button)view.findViewById(R.id.see_history);
		see_history.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getActivity().finish();
			}
		});
	}
	class Item {
		private View mView;
		public Item(HashMap<String, String> item){
			mView = mInflater.inflate(R.layout.result_item, null);
			TextView name = (TextView)mView.findViewById(R.id.name);
			TextView content = (TextView)mView.findViewById(R.id.content);
			for(String string : item.keySet()){
				name.setText(string);
				content.setText(item.get(string));
			}
		}
	}
}
