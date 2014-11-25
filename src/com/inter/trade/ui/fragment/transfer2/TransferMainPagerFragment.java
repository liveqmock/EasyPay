package com.inter.trade.ui.fragment.transfer2;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.inter.trade.R;
import com.inter.trade.ui.TranserRecordActivity;
import com.inter.trade.ui.fragment.transfer.TransferRecordFragment;
import com.inter.trade.ui.fragment.transfer.TransferFragment.TransferType;
import com.inter.trade.ui.manager.IBaseFragment;
import com.inter.trade.ui.manager.UIManagerActivity;
import com.inter.trade.ui.manager.core.UIConstantDefault;

/**
 * 转账首页
 * @author zhichao.huang
 *
 */
public class TransferMainPagerFragment extends IBaseFragment implements OnClickListener{
	
	private static final String TAG = TransferMainPagerFragment.class.getName();
	
	private View rootView;
	
	/**
	 * 超级转账
	 */
	private TextView superText;
	
	/**
	 * 普通转账
	 */
	private TextView commonText;
	
	/**
	 * 顶部转账标题栏tab_layout
	 */
	private LinearLayout tab_layout;
	
	private Bundle data = null;
	
	/**
	 * 请求银行记录的类型,默认超级转账
	 */
	private String paytype = TransferType.SUPER_TRANSER;
	
	public static TransferMainPagerFragment newInstance (Bundle data) {
		TransferMainPagerFragment fragment = new TransferMainPagerFragment();
		fragment.setArguments(data);
		return fragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "onCreate");
		Bundle bundle = getArguments();
		if(bundle != null) {
			data = bundle;
		}
	}
	
	@Override
	protected View onInitView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.transfer2_main_layout, container,false);
		initViews(rootView);
		return rootView;
	}

	@Override
	protected void onAsyncLoadData() {
		
	}

	@Override
	public void onRefreshDatas() {
		Log.i(TAG, "onRefreshDatas");
		((UIManagerActivity)getActivity()).setTopTitle("转账汇款");
		((UIManagerActivity)getActivity()).setBackButtonHideOrShow(View.VISIBLE);
		
		((UIManagerActivity)getActivity()).setRightButtonOnClickListener("历史记录", View.VISIBLE , new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				addFragmentToStack(UIConstantDefault.UI_CONSTANT_AIR_TICKET_ORDER_HISTORY_LIST, 1, data);
				Intent intent = new Intent();
				intent.setClass(getActivity(), TranserRecordActivity.class);
				intent.putExtra(TransferRecordFragment.TYPE_STRING, paytype);
				getActivity().startActivityForResult(intent, 200);
			}
		});
		if(currentFragment != null) {
			currentFragment.onRefreshDatas();
		}
		
		if(getActivity() != null){
			((UIManagerActivity)getActivity()).tilte_line.setVisibility(View.INVISIBLE);
		}
		
	}

	private void initViews (View rootView) {
		superText = (TextView)rootView.findViewById(R.id.super_tab);
		commonText = (TextView)rootView.findViewById(R.id.common_tab);
		tab_layout = (LinearLayout)rootView.findViewById(R.id.tab_layout);
		
		superText.setOnClickListener(this);
		commonText.setOnClickListener(this);
		
		setSuperSelecte();
	}
	
	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.super_tab://超级转账
			setSuperSelecte();
			break;
		case R.id.common_tab://普通转账
			setCommonSelecte();
			break;
		default:
			break;
		}
		
	}
	
	@Override
	public void onPause() {
		super.onPause();
		((UIManagerActivity)getActivity()).setRightButtonIconOnClickListener(View.GONE ,null);
	}

	@Override
	public void onStop() {
		super.onStop();
	}
	
	@Override
	public void onTimeout() {
		if(currentFragment != null) {
			currentFragment.onTimeout();
		}
	}

	private void setSuperSelecte() {
		paytype = TransferType.SUPER_TRANSER;
		commonText.setSelected(false);
		superText.setSelected(true);
		superText.setTextColor(Color.BLACK);
		superText.setTextSize(15);
		commonText.setTextColor(Color.GRAY);
		commonText.setTextSize(14);
		tab_layout.setBackgroundDrawable(getResources().getDrawable(R.drawable.left_button_bg));
		
		Bundle data = new Bundle();
		data.putString("paytype", /**TYPECLASS.suptfmg*/"suptfmg");
		/**if(superTransferBankListFragment == null)*/ superTransferBankListFragment = TransferBankListFragment.newInstance(data);
		switchContent(superTransferBankListFragment);
	}
	
	private void setCommonSelecte() {
		paytype = TransferType.USUAL_TRANSER;
		commonText.setSelected(true);
		superText.setSelected(false);
		commonText.setTextColor(Color.BLACK);
		commonText.setTextSize(15);
		superText.setTextColor(Color.GRAY);
		superText.setTextSize(14);
		tab_layout.setBackgroundDrawable(getResources().getDrawable(R.drawable.right_button_bg));
		
		Bundle data = new Bundle();
		data.putString("paytype", /**TYPECLASS.tfmg*/"tfmg");
		/**if(commonTransferBankListFragment == null)*/ commonTransferBankListFragment = TransferBankListFragment.newInstance(data);
		switchContent(commonTransferBankListFragment);
	}
	
	/**
	* 在一个Fragment里，切换多个子Fragment页面
	* 
	* @param from
	* @param to
	*/
	public void switchContent(IBaseFragment to) {
		
		FragmentTransaction transaction = getActivity().getSupportFragmentManager()
				.beginTransaction();
		
		if(currentFragment == null) {
			transaction.add(R.id.transfer2_container, to).commit();
			currentFragment = to;
			return;
		}
		
		if (currentFragment != to) {

//			to.getLoaderManager().hasRunningLoaders();
			// 先判断是否被add过
			if (!to.isAdded()) {

				// 隐藏当前的fragment，add下一个到Activity中
				transaction.hide(currentFragment).add(R.id.transfer2_container, to).commit();
			} else {
				// 隐藏当前的fragment，显示下一个
				transaction.hide(currentFragment).show(to).commit();
			}
		}
		currentFragment = to;
	}
	
	/**
	 * 当前Fragment
	 */
	private IBaseFragment currentFragment = null;
	
	private IBaseFragment superTransferBankListFragment = null;
	private IBaseFragment commonTransferBankListFragment = null;

}
