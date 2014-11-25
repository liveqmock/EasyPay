package com.inter.trade.ui.fragment.airticket;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.inter.trade.R;
import com.inter.trade.ui.fragment.airticket.util.AddPassengerTask;
import com.inter.trade.ui.fragment.airticket.util.PassengerData;
import com.inter.trade.ui.fragment.airticket.util.PassengerInfoUtils;
import com.inter.trade.ui.fragment.waterelectricgas.data.ResponseStateListener;
import com.inter.trade.ui.manager.IBaseFragment;
import com.inter.trade.ui.manager.UIManagerActivity;
import com.inter.trade.util.IdcardUtil;
import com.inter.trade.util.PromptUtil;
import com.inter.trade.util.UserInfoCheck;
import com.inter.trade.view.popmenucompat.PopupMenuCompat;
import com.inter.trade.view.popmenucompat.PopupMenuCompat.OnMenuItemClickListener;

/**
 * 飞机票 添加联系人Fragment
 * 
 * @author zhichao.huang
 * 
 */
public class AirTicketAddContactFragment extends IBaseFragment implements
		OnClickListener,ResponseStateListener{

	private static final String TAG = AirTicketAddContactFragment.class
			.getName();

	private View rootView;

	private Bundle data = null;

	private EditText etName;//名字
	private EditText etNO;//手机号


	public static AirTicketAddContactFragment newInstance(Bundle data) {
		AirTicketAddContactFragment fragment = new AirTicketAddContactFragment();
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
		rootView = inflater.inflate(R.layout.airticket_add_contact,
				container, false);
		initViews(rootView);
		return rootView;
	}
	

	@Override
	public void onResume() {
		super.onResume();
		setTitleBar();
	}
	
	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		((UIManagerActivity) getActivity()).setRightButtonOnClickListener("确认",
				View.GONE, this);
	}

	private void initViews(View rootView) {
		etName = (EditText) rootView.findViewById(R.id.et_name);
		etNO = (EditText) rootView.findViewById(R.id.et_no);
	}
	
	
	/**
	 * 设置顶部栏数据（标题、更多）
	 */
	private void setTitleBar() {
		((UIManagerActivity) getActivity()).setTopTitle("添加联系人");
		((UIManagerActivity) getActivity()).setRightButtonOnClickListener("确认",
				View.VISIBLE, this);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.title_right_btn:// 确定按钮
			if(checkInfo()){//通过验证，提交到服务器
				PassengerData passenger = collectInfo();
				new AddPassengerTask(getActivity(), this).execute(
						passenger.getName(),
						"",
						"",
						passenger.getPhoneNumber(),
						"",
						"");
				
			}
			break;
		default:
			break;
		}

	}
	
	/**
	 * 检查输入 
	 * @throw
	 * @return void
	 */
	private boolean checkInfo() {
		if(TextUtils.isEmpty(etName.getText()+"")){
			PromptUtil.showToast(getActivity(), "亲,请您输入姓名！");
			return false;
		}else if(TextUtils.isEmpty(etNO.getText()+"")){
			PromptUtil.showToast(getActivity(), "亲,请您输入手机号码！");
			return false;
		}else if(!isMobileNum(etNO.getText()+"")){
			PromptUtil.showToast(getActivity(), "亲,请您输入正确的手机号码！");
			return false;
		}
		return true;
		
	}
	
	/**
	 * 验证手机号码 
	 * @param mobile
	 * @return
	 * @throw
	 * @return boolean
	 */
	private boolean isMobileNum(String mobile) {
		return UserInfoCheck.checkMobilePhone(mobile);
//		 Pattern p = Pattern
//	                .compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
//	        Matcher m = p.matcher(mobile);
//	        return m.matches();
	}

	/**
	 * 收集用户填写信息
	 * @return 
	 * @throw
	 * @return Bundle
	 */
	public PassengerData collectInfo() {
		PassengerData passenger=new PassengerData();
		passenger.setName(etName.getText()+"");
		passenger.setPhoneNumber(etNO.getText()+"");
		return passenger;
	}
	/**
	 * 更新用户信息 
	 * @throw
	 * @return void
	 */
	private void update(PassengerData data){
		etName.setText(data.getName());
		etNO.setText(data.getPassportNo());
	}

	@Override
	protected void onAsyncLoadData() {
		// TODO Auto-generated method stub
		
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
		removeFragmentToStack();//退棧
	}
}
