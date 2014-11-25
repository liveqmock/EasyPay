package com.inter.trade.ui.fragment.airticket;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.inter.trade.AsyncLoadWork;
import com.inter.trade.R;
import com.inter.trade.AsyncLoadWork.AsyncLoadWorkListener;
import com.inter.trade.data.CommonData;
import com.inter.trade.ui.fragment.airticket.adapter.ContactAdapter;
import com.inter.trade.ui.fragment.airticket.adapter.PassengerAdapter;
import com.inter.trade.ui.fragment.airticket.adapter.PassengerAdapter.ListViewButtonListener;
import com.inter.trade.ui.fragment.airticket.util.ApiAirticketDeletePeopleParser;
import com.inter.trade.ui.fragment.airticket.util.PassengerData;
import com.inter.trade.ui.fragment.airticket.util.PassengerListTask;
import com.inter.trade.ui.fragment.waterelectricgas.data.ResponseStateListener;
import com.inter.trade.ui.manager.IBaseFragment;
import com.inter.trade.ui.manager.UIManagerActivity;
import com.inter.trade.ui.manager.core.IMainHandlerManager;
import com.inter.trade.ui.manager.core.UIConstantDefault;
import com.inter.trade.util.PromptUtil;

/**
 * 飞机票 选择联系人 Fragment
 * 
 * @author zhichao.huang
 * 
 */
public class AirTicketSelectContactFragment extends IBaseFragment implements
		OnClickListener,ListViewButtonListener,ResponseStateListener {

	private static final String TAG = AirTicketSelectContactFragment.class
			.getName();


	private View rootView;

	private Bundle data = null;
	
	private Button btnChoose;
	private ListView lvPassenger;
	
	private ArrayList<PassengerData> passengerList;
	
	private PassengerListTask task;
	
	private ArrayList<PassengerData> datas;
	
	private ContactAdapter adapter;
	
	/**
	 * 删除联系人
	 */
	private AsyncLoadWork<String> asyncDeleteLoadWork = null;

	public static AirTicketSelectContactFragment newInstance(Bundle data) {
		AirTicketSelectContactFragment fragment = new AirTicketSelectContactFragment();
		fragment.setArguments(data);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "onCreate");
		Bundle bundle = getArguments();
		if (bundle != null) {
			data = bundle;
		}
	}

	@Override
	protected View onInitView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.airticket_select_contact,
				container, false);
		initViews(rootView);
		
		
		return rootView;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		setTitleBar();
		
		task=new PassengerListTask(getActivity(), this);
		task.execute("2");//读取乘机人
	}

	/**
	 * 设置顶部栏数据（标题、更多）
	 */
	private void setTitleBar() {
		((UIManagerActivity) getActivity()).setTopTitle("选择联系人");
		((UIManagerActivity) getActivity()).setRightButtonOnClickListener("确认",
				View.VISIBLE, this);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		((UIManagerActivity) getActivity()).setRightButtonOnClickListener("确认",
				View.GONE, this);
	}

	@Override
	public void onTimeout() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if(task!=null&&!task.isCancelled()){
			task.cancel(true);
		}
	}

	private void initViews(View rootView) {
		btnChoose=(Button) rootView.findViewById(R.id.btn_choose);
		btnChoose.setOnClickListener(this);
		
		lvPassenger=(ListView)rootView.findViewById(R.id.lv_passenger);
		
		passengerList=new ArrayList<PassengerData>();
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_right_btn:// 确定按钮
			if(adapter!=null){
				ArrayList<PassengerData> selectedList = adapter.getSelectedList();//選中的聯繫人集合
				if(selectedList.size()==0){
					PromptUtil.showToast(getActivity(), "亲,请您选中至少一个联系人!");
				}else{
//					if(((UIManagerActivity)getActivity()).selectedLinkmanList != null) {
//						//检测当前选中的联系人，在订单页面是否选择过
//						if(checkDatas(selectedList)) {
//							PromptUtil.showToast(getActivity(), "您不能选择同样的联系人!");
//							break;
//						}
//						((UIManagerActivity)getActivity()).selectedLinkmanList.addAll(selectedList);
//					}else{
//						((UIManagerActivity)getActivity()).selectedLinkmanList = selectedList;
//					}
						if(selectedList.size()==1){
							((UIManagerActivity)getActivity()).selectedLinkmanList = selectedList;
							removeFragmentToStack();//退棧
						} else {
							PromptUtil.showToast(getActivity(), "亲,只能选择一个联系人!");
						}
					//TODO 返回联系人到订单界面
					
				}
			}
			
			break;
		case R.id.btn_choose://添加聯繫人
			IMainHandlerManager.handlerUI(UIConstantDefault.UI_CONSTANT_AIR_TICKET_ADD_CONTACT, 1, null);//跳转到添加乘机人页面
			default:
				break;
			}
	}


	@Override
	protected void onAsyncLoadData() {
	}

	@Override
	public void onRefreshDatas() {

	}
	
	@Override
	public void clickAtPosition(final int position) {
		if(datas == null || datas.get(position) == null ) {
			return;
		}
		
		new AlertDialog.Builder(getActivity()).setTitle("删除联系人").setMessage("确定要删除："+datas.get(position).getName()+"-"+datas.get(position).getPhoneNumber()+"?")
		.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				//删除联系人
				ApiAirticketDeletePeopleParser authorRegParser = new ApiAirticketDeletePeopleParser();
				CommonData mData = new CommonData();
				mData.putValue("id", datas.get(position).getId() != null ? datas.get(position).getId() : "");
				mData.putValue("type", "2");//删除信息类型，1代表删除乘机人；2代表删除联系人

				asyncDeleteLoadWork = new AsyncLoadWork<String>(getActivity(), authorRegParser, mData, deleteListener, false, true);
				asyncDeleteLoadWork.execute("ApiAirticket", "deletePassenger");
			}
		})
		.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		}).show();
	}
	
	private AsyncLoadWorkListener deleteListener = new AsyncLoadWorkListener() {

		@Override
		public void onSuccess(Object protocolDataList, Bundle bundle) {
			onResume();
		}

		@Override
		public void onFailure(String error) {
			
		}
		
	};

	@Override
	public void onSuccess(Object obj, Class cla) {
		datas=(ArrayList<PassengerData>) obj;
		System.out.println("乘机人的"+datas.size());
		adapter=new ContactAdapter(getActivity(),filterDatas(datas), this);
		lvPassenger.setAdapter(adapter);
		
		//删除联系人，当删除用户已经选择的乘机人时，替换为最新的
		ArrayList<PassengerData> selectedList = adapter.getSelectedList();
		((UIManagerActivity)getActivity()).selectedLinkmanList = selectedList;
	}
	
	
	/**
	 * 过滤数据,检测当前选中的联系人，在订单页面是否选择过
	 */
	private ArrayList<PassengerData> filterDatas(ArrayList<PassengerData> passengerDatas) {
		ArrayList<PassengerData> checkDatas = null;
		checkDatas = ((UIManagerActivity)getActivity()).selectedLinkmanList;
		
		if(checkDatas != null) {
			
			for(int i = 0; i < passengerDatas.size(); i ++) {
				
				PassengerData passengerData= passengerDatas.get(i);
				
				for(int j = 0; j < checkDatas.size(); j++) {
					
					if(checkDatas.get(j).getPhoneNumber().equals(passengerData.getPhoneNumber())
							&& checkDatas.get(j).getName().equals(passengerData.getName())) {
						passengerData.setCheck(true);
					}
					
				}
				
			}
			
		}
		return passengerDatas;
	}
	
//	/**
//	 * 检测当前选中的联系人，在订单页面是否选择过
//	 */
//	private boolean checkDatas(ArrayList<PassengerData> passengerDatas) {
//		ArrayList<PassengerData> checkDatas = null;
//		checkDatas = ((UIManagerActivity)getActivity()).selectedLinkmanList;
//		
//		if(checkDatas != null) {
//			
//			for(int i = 0; i < passengerDatas.size(); i ++) {
//				
//				PassengerData passengerData= passengerDatas.get(i);
//				
//				for(int j = 0; j < checkDatas.size(); j++) {
//					
//					if(checkDatas.get(j).getPhoneNumber().equals(passengerData.getPhoneNumber())){
//						return true;
//					}
//				}
//			}
//		}
//		return false;
//	}

}
