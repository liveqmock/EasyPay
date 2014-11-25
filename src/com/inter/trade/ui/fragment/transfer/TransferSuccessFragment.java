package com.inter.trade.ui.fragment.transfer;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.inter.trade.R;
import com.inter.trade.ui.fragment.BaseFragment;
import com.inter.trade.ui.manager.UIManagerActivity;
import com.inter.trade.util.Constants;
import com.inter.trade.util.LoginUtil;

/**
 * 还款成功
 * @author apple
 *
 */

public class TransferSuccessFragment extends BaseFragment{
	private LayoutInflater mInflater;
	private static ArrayList<HashMap<String, String>> mMaps = new ArrayList<HashMap<String,String>>();
	
	public static TransferSuccessFragment createFragment(ArrayList<HashMap<String, String>> cData){
		TransferSuccessFragment fragment = new TransferSuccessFragment();
		mMaps = cData;
		return fragment;
	}
	public TransferSuccessFragment(){
		
	}
	@Override
	protected void setBackVisible() {
		// TODO Auto-generated method stub
		if (getActivity() == null) {
			return;
		}
		
//		if(getActivity() instanceof UIManagerActivity) {
//			((UIManagerActivity)getActivity()).setBackButtonOnClickListener(new View.OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					getActivity().setResult(Constants.ACTIVITY_FINISH);
//					getActivity().finish();
//				}
//			});
//		}
		
//		Button back = (Button) getActivity().findViewById(R.id.title_back_btn);
//
//		if (back == null) {
//			return;
//		}
//		back.setVisibility(View.VISIBLE);
//		back.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				getActivity().setResult(Constants.ACTIVITY_FINISH);
//				getActivity().finish();
//			}
//		});
//
//		menu = (Button) getActivity().findViewById(R.id.title_menu_btn);
//		menu.setVisibility(View.GONE);
//		Button right = (Button) getActivity()
//				.findViewById(R.id.title_right_btn);
//		right.setVisibility(View.GONE);
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setBackVisible();
		mInflater= LayoutInflater.from(getActivity());
	}
	
	
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setTitle("转账结果");
		if(getActivity() instanceof UIManagerActivity){
			((UIManagerActivity)getActivity()).setBackButtonHideOrShow(View.INVISIBLE);
		}
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		LoginUtil.detection(getActivity());
		View view = inflater.inflate(R.layout.cridet_back_success, container,false);
//		ListView mListView = (ListView)view.findViewById(R.id.result_listview);
		initResult(view);
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
		
		//再次交易
		Button btnAgain=(Button) view.findViewById(R.id.btn_again);
		btnAgain.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				isAgain = true;
//				Intent intent=new Intent(getActivity(),IndexActivity.class);
//				intent.putExtra(FragmentFactory.INDEX_KEY, FuncMap.TELEPHONE_INDEX_FUNC);
//				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//				startActivity(intent);
				if(getActivity() != null && getActivity() instanceof UIManagerActivity) {
					((UIManagerActivity)getActivity()).isRefresh = true;
					((UIManagerActivity)getActivity()).backHomeFragment();
				}
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
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if(!isAgain) {
			if(getActivity() != null){
				getActivity().finish();
			}
		}
	}
	
	/**
	 * 是否是再次交易
	 */
	private boolean isAgain = false;
}
