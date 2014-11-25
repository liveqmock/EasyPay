package com.inter.trade.ui.fragment.airticket;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.inter.trade.R;
import com.inter.trade.ui.fragment.airticket.util.AddPassengerTask;
import com.inter.trade.ui.fragment.airticket.util.PassengerData;
import com.inter.trade.ui.fragment.airticket.util.PassengerInfoUtils;
import com.inter.trade.ui.fragment.waterelectricgas.data.ResponseStateListener;
import com.inter.trade.ui.manager.IBaseFragment;
import com.inter.trade.ui.manager.UIManagerActivity;
import com.inter.trade.util.PromptUtil;

/**
 * 飞机票 添加乘机人 Fragment
 * 
 * @author zhichao.huang
 * 
 */
public class AirTicketAddPassengerFragment extends IBaseFragment implements
		OnClickListener, OnCheckedChangeListener,ResponseStateListener {

	private static final String TAG = AirTicketAddPassengerFragment.class
			.getName();

	private FragmentManager fm;

	private ArrayList<Fragment> fList;

	private int selected = 0;

	private View rootView;

	private Bundle data = null;
	
	private PassengerData passenger;

	private RadioGroup rgNavigation;
	
	private AddPassengerTask task;

	public static AirTicketAddPassengerFragment newInstance(Bundle data) {
		AirTicketAddPassengerFragment fragment = new AirTicketAddPassengerFragment();
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
			passenger =(PassengerData) data.getSerializable("data");
		}
		
	}

	@Override
	protected View onInitView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.airticket_add_passenger,
				container, false);
		initChildFragment();
		initViews(rootView);
		setTitleBar();
		updatePage();
		return rootView;
	}

	/**
	 * 设置顶部栏数据（标题、更多）
	 */
	private void setTitleBar() {
		if(passenger!=null){
			((UIManagerActivity) getActivity()).setTopTitle("编辑乘机人");
		}else{
			((UIManagerActivity) getActivity()).setTopTitle("添加乘机人");
		}
		((UIManagerActivity) getActivity()).setRightButtonOnClickListener("确认",
				View.VISIBLE, this);
	}
	
	/**
	 * 刷新页面
	 * @throw
	 * @return void
	 */
	private void updatePage(){
		if(passenger!=null){
			String ageRange = passenger.getAgeRange();
			if("ADU".equals(ageRange)){//ADU（成人），CHI（儿童）， BAB（婴儿）
				selected=0;
			}else if("CHI".equals(ageRange)){
				selected=1;
			}else if("BAB".equals(ageRange)){
				selected=2;
			}
			rgNavigation.getChildAt(selected).performClick();
		}else{
			rgNavigation.getChildAt(0).performClick();
		}
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		((UIManagerActivity) getActivity()).setRightButtonOnClickListener("确认",
				View.GONE, this);
	}

	private void initChildFragment() {
		fm = getChildFragmentManager();
		fList = new ArrayList<Fragment>();
		Bundle b=new Bundle();
		if(passenger!=null){
			passenger.setUpdate(true);
		}
		b.putSerializable("data", passenger);
		fList.add(AirTicketAddPassengerContentFragment.newInstance(b));
		fList.add(AirTicketAddPassengerContentFragment.newInstance(b));
		fList.add(AirTicketAddPassengerContentFragment.newInstance(b));
		FragmentTransaction ft = fm.beginTransaction();
		ft.add(R.id.fl_content, fList.get(0));
		ft.commit();
	}

	private void initViews(View rootView) {
		rgNavigation = (RadioGroup) rootView.findViewById(R.id.rg_navigation);
		rgNavigation.setOnCheckedChangeListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_right_btn:// 确定按钮
			AirTicketAddPassengerContentFragment d = (AirTicketAddPassengerContentFragment) (fList
					.get(selected));
			switch (d.isFill()) {// 返回 0代表填写完毕 1代表姓名没有填写 2代表证件号没有填写
			case 0:
				PassengerData p =d.collectInfo();//p为增加或者修改后的乘机人信息
				p.setAgeRange(PassengerInfoUtils.transfetIntToAgeRange(selected));
				//PromptUtil.showToast(getActivity(), "name__"+p.getName()+"++++++idtype:_------"+p.getIdtype()+"_____"+p.getAgeRange());
				
				// TODO 将填写的信息返回到调用的Fragment
				if(p.isUpdate()){//编辑
					p.setUpdate(false);
					
				}else{//增加
					//乘机人类型
					String passengerType = (selected+1) +"";
					task=new AddPassengerTask(getActivity(), this);
					task.execute(p.getName(),
								p.getIdtype(),
								p.getPassportNo(),
								"",
								passengerType,
								p.getBirthDay());
				}
				break;
			case 1:
				PromptUtil.showToast(getActivity(), "亲,请填写乘机人姓名！");
				break;
			case 2:
				PromptUtil.showToast(getActivity(), "亲,请填写乘机人证件号！");
				break;
			case 3:
				PromptUtil.showToast(getActivity(), "亲,请填写有效的身份证号！");
				break;
			case 4:
				PromptUtil.showToast(getActivity(), "亲,请填写出生日期！");
				break;
			default:
				break;
			}
			
			break;

		default:
			break;
		}

	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.rb_adult:// 选择成人
			selected = 0;
			break;
		case R.id.rb_child:// 选择小孩
			selected = 1;
			break;
		case R.id.rb_baby:// 选择婴儿
			selected = 2;
			break;
		default:
			break;
		}
		FragmentTransaction ft = fm.beginTransaction();
		ft.replace(R.id.fl_content, fList.get(selected));
		ft.commit();
	}

	@Override
	protected void onAsyncLoadData() {

	}

	@Override
	public void onRefreshDatas() {

	}
	
	@Override
	public void onTimeout() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSuccess(Object obj, Class cla) {
		PromptUtil.showToast(getActivity(), (String)obj);
		removeFragmentToStack();// 将fragment退出栈
	}

}
