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
import com.inter.trade.AsyncLoadWork.AsyncLoadWorkListener;
import com.inter.trade.R;
import com.inter.trade.data.CommonData;
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
 * 飞机票 选择乘机人 Fragment
 * 
 * @author zhichao.huang
 * 
 */
public class AirTicketSelectPassengerFragment extends IBaseFragment implements
		OnClickListener,ListViewButtonListener,ResponseStateListener {

	private static final String TAG = AirTicketSelectPassengerFragment.class
			.getName();

	private FragmentManager fm;

	private int selected = 0;

	private View rootView;

	private Bundle data = null;
	
	private Button btnChoose;
	private ListView lvPassenger;
	
	private ArrayList<PassengerData> passengerList;
	
	private PassengerListTask task;
	
	private ArrayList<PassengerData> datas;
	
	private PassengerAdapter adapter;
	
	/**
	 * 删除联系人
	 */
	private AsyncLoadWork<String> asyncDeleteLoadWork = null;

	public static AirTicketSelectPassengerFragment newInstance(Bundle data) {
		AirTicketSelectPassengerFragment fragment = new AirTicketSelectPassengerFragment();
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
		rootView = inflater.inflate(R.layout.airticket_select_passenger,
				container, false);
		initViews(rootView);
		
		
		return rootView;
	}
	
	@Override
	public void onTimeout() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		setTitleBar();
		
		task=new PassengerListTask(getActivity(), this);
		task.execute("1");//读取乘机人
	}

	/**
	 * 设置顶部栏数据（标题、更多）
	 */
	private void setTitleBar() {
		((UIManagerActivity) getActivity()).setTopTitle("选择乘机人");
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
	/*	PassengerData data=new PassengerData();
		data.setIdtype("1");
		data.setName("里嬉戏");
		data.setPassportNo("346132151354641315746813");
		data.setAgeRange("ADU");
		data.setUpdate(true);
		passengerList.add(data);*/
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_right_btn:// 确定按钮
			if(adapter!=null){
				ArrayList<PassengerData> selectedList = adapter.getSelectedList();
				if(selectedList.size()==0){
					PromptUtil.showToast(getActivity(), "亲,请选择至少一个乘机人!");
				}else{
//					if(((UIManagerActivity)getActivity()).selectedPassengerList != null) {
//						
//						//检测当前选中的联系人，在订单页面是否选择过
//						if(checkDatas(selectedList)) {
//							PromptUtil.showToast(getActivity(), "您不能选择同样的乘机人!");
//							break;
//						}
//						
//						((UIManagerActivity)getActivity()).selectedPassengerList.addAll(selectedList);
//					}else{
						((UIManagerActivity)getActivity()).selectedPassengerList = selectedList;
//					}
					removeFragmentToStack();//退栈
				}
			}
				
			break;
		case R.id.btn_choose://添加乘机人
			IMainHandlerManager.handlerUI(UIConstantDefault.UI_CONSTANT_AIR_TICKET_ADD_PASSENGER, 1, null);//跳转到添加乘机人页面
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
		//PromptUtil.showToast(getActivity(), position+"");
//		Bundle b=new Bundle();
//		b.putSerializable("data", datas.get(position));
//		IMainHandlerManager.handlerUI(UIConstantDefault.UI_CONSTANT_AIR_TICKET_ADD_PASSENGER, 1, b);//跳转到添加乘机人页面
		
		if(datas == null || datas.get(position) == null ) {
			return;
		}
		
		new AlertDialog.Builder(getActivity()).setTitle("删除乘机人").setMessage("确定要删除："+datas.get(position).getName()+"-"+datas.get(position).getPassportNo()+"?")
		.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				//删除联系人
				ApiAirticketDeletePeopleParser authorRegParser = new ApiAirticketDeletePeopleParser();
				CommonData mData = new CommonData();
				mData.putValue("id", datas.get(position).getId() != null ? datas.get(position).getId() : "");
				mData.putValue("type", "1");//删除信息类型，1代表删除乘机人；2代表删除联系人

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
		adapter=new PassengerAdapter(getActivity(),filterDatas(datas)/**datas*/,this);
		lvPassenger.setAdapter(adapter);
		
		//删除联系人，当删除用户已经选择的乘机人时，替换为最新的
		ArrayList<PassengerData> selectedList = adapter.getSelectedList();
		((UIManagerActivity)getActivity()).selectedPassengerList = selectedList;
	}
	
	/**
	 * 过滤数据,检测当前选中的联系人，在订单页面是否选择过
	 */
	private ArrayList<PassengerData> filterDatas(ArrayList<PassengerData> passengerDatas) {
		ArrayList<PassengerData> checkDatas = null;
		checkDatas = ((UIManagerActivity)getActivity()).selectedPassengerList;
		
		if(checkDatas != null) {
			
			for(int i = 0; i < passengerDatas.size(); i ++) {
				
				PassengerData passengerData= passengerDatas.get(i);
				
				for(int j = 0; j < checkDatas.size(); j++) {
					
					if(checkDatas.get(j).getPassportNo().equals(passengerData.getPassportNo())){
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
//		checkDatas = ((UIManagerActivity)getActivity()).selectedPassengerList;
//		
//		if(checkDatas != null) {
//			
//			for(int i = 0; i < passengerDatas.size(); i ++) {
//				
//				PassengerData passengerData= passengerDatas.get(i);
//				
//				for(int j = 0; j < checkDatas.size(); j++) {
//					
//					if(checkDatas.get(j).getPassportNo().equals(passengerData.getPassportNo())){
//						return true;
//					}
//				}
//			}
//		}
//		return false;
//	}

}
