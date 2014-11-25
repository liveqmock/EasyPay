package com.inter.trade.ui.fragment.airticket;

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
import com.inter.trade.ui.fragment.airticket.util.PassengerData;
import com.inter.trade.ui.fragment.airticket.util.PassengerInfoUtils;
import com.inter.trade.util.IdcardUtil;
import com.inter.trade.view.popmenucompat.PopupMenuCompat;
import com.inter.trade.view.popmenucompat.PopupMenuCompat.OnMenuItemClickListener;

/**
 * 飞机票 添加乘机人的内容 Fragment
 * 
 * @author zhichao.huang
 * 
 */
public class AirTicketAddPassengerContentFragment extends Fragment implements
		OnClickListener, OnMenuItemClickListener {

	private static final String TAG = AirTicketAddPassengerContentFragment.class
			.getName();

	private View rootView;

	private Bundle data = null;

	private EditText etName;
	private EditText etNO;
	private EditText birthDay; 
	private Button btnChoose;

	private String idType="身份证";// 证件类型
	
	private PassengerData passenger;

	public static AirTicketAddPassengerContentFragment newInstance(Bundle data) {
		AirTicketAddPassengerContentFragment fragment = new AirTicketAddPassengerContentFragment();
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.i(TAG, "onCreateView");
		rootView = inflater.inflate(R.layout.airticket_add_passenger_content,
				container, false);
		initViews(rootView);
		if(data!=null){
			passenger=(PassengerData) data.getSerializable("data");
			if(passenger!=null){
				update(passenger);
			}
		}
		return rootView;
	}

	private void initViews(View rootView) {
		etName = (EditText) rootView.findViewById(R.id.et_name);
		btnChoose = (Button) rootView.findViewById(R.id.btn_choose);
		etNO = (EditText) rootView.findViewById(R.id.et_no);
		birthDay = (EditText) rootView.findViewById(R.id.birthDay);
		
		btnChoose.setOnClickListener(this);
	}


	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btn_choose:// 选择按钮
			PopupMenuCompat popup = PopupMenuCompat.newInstance(getActivity(),
					v);
			popup.inflate(R.menu.tpye_ids);
			popup.setOnMenuItemClickListener(this);
			popup.show();
			break;
		default:
			break;
		}

	}

	// 选中的菜单项
	@Override
	public boolean onMenuItemClick(MenuItem item) {
		idType = item.getTitle() + "";
		btnChoose.setText(idType);

		return false;
	}

	/**
	 * 收集用户填写信息
	 * @return 
	 * @throw
	 * @return Bundle
	 */
	public PassengerData collectInfo() {
		if(passenger==null){
			passenger=new PassengerData();
		}
		passenger.setName(etName.getText()+"");
		passenger.setPassportNo(etNO.getText()+"");
		passenger.setBirthDay(birthDay.getText()+"");
		passenger.setIdtype(PassengerInfoUtils.transferStringToType(idType+""));
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
		birthDay.setText(data.getBirthDay());
		btnChoose.setText(PassengerInfoUtils.transferTypeToString(data.getIdtype()));
	}
	/**
	 * 判断填写区域是否填写
	 * 
	 * @return 返回0代表填写完毕 1代表姓名没有填写 2代表证件号没有填写 3代表身份证格式不对
	 * @throw
	 * @return boolean
	 */
	public int isFill() {
		if (TextUtils.isEmpty(etName.getText())) {
			return 1;
		} else if (TextUtils.isEmpty(etNO.getText())) {
			return 2;
		}else if(idType.equals("身份证")&&!IdcardUtil.validateID(etNO.getText()+"")){
			return 3;
		} else if (TextUtils.isEmpty(birthDay.getText())) {
			return 4;
		}
		return 0;
	}
}
