package com.inter.trade.ui.fragment.hotel;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.inter.trade.R;
import com.inter.trade.ui.IndexActivity;
import com.inter.trade.ui.fragment.BaseFragment;
import com.inter.trade.ui.fragment.FragmentFactory;
import com.inter.trade.ui.func.FuncMap;
import com.inter.trade.ui.manager.UIManagerActivity;
import com.inter.trade.ui.manager.core.UIConstantDefault;
import com.inter.trade.util.Constants;


/**
 * 酒店预订成功
 * @author haifengli
 *
 */

public class HotelPaySuccessFragment extends BaseFragment{
	private LayoutInflater mInflater;
	private static ArrayList<HashMap<String, String>> mMaps = new ArrayList<HashMap<String,String>>();
	
	public static HotelPaySuccessFragment createFragment(ArrayList<HashMap<String, String>> cData){
		HotelPaySuccessFragment fragment = new HotelPaySuccessFragment();
		mMaps = cData;
		return fragment;
	}
	public HotelPaySuccessFragment(){
		
	}
	@Override
	protected void setBackVisible() {
		// TODO Auto-generated method stub
		if (getActivity() == null) {
			return;
		}
		Button back = (Button) getActivity().findViewById(R.id.title_back_btn);

		if (back == null) {
			return;
		}
		back.setVisibility(View.INVISIBLE);

		menu = (Button) getActivity().findViewById(R.id.title_menu_btn);
		menu.setVisibility(View.GONE);
		Button right = (Button) getActivity()
				.findViewById(R.id.title_right_btn);
		right.setVisibility(View.GONE);
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitle("预订结果");
		setBackVisible();
		mInflater= LayoutInflater.from(getActivity());
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.hotel_book_success, container,false);
//		ListView mListView = (ListView)view.findViewById(R.id.result_listview);
		initResult(view);
		
		InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);  
		imm.toggleSoftInput(0, 0);  		
		return view;
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
				getActivity().setResult(Constants.ACTIVITY_FINISH);
				getActivity().finish();
			}
		});
		
		Button btnAgain=(Button) view.findViewById(R.id.btn_again);
		btnAgain.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(getActivity(),UIManagerActivity.class);
				intent.putExtra(FragmentFactory.INDEX_KEY, FuncMap.HOTEL_INDEX_FUNC);
				intent.putExtra("targetFragment", UIConstantDefault.UI_CONSTANT_HOTEL);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
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
