package com.inter.trade.ui.fragment.airticket;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
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
import android.widget.DatePicker;
import android.widget.EditText;

import com.inter.trade.R;
import com.inter.trade.ui.AgentQueryWheelDateActivity;
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
		birthDay.setOnClickListener(this);
		
		btnChoose.setOnClickListener(this);
	}

	
	private void selectDate()
	{
		Intent intent = new Intent();
		intent.setClass(getActivity(), AgentQueryWheelDateActivity.class);
		
		Bundle bundle = new Bundle();
		bundle.putString("dateType", "");
		intent.putExtra("wheelDate", bundle);
		startActivityForResult(intent, 4);
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
		case R.id.birthDay:// 选择日期
//			selectDate();
//			showDateDialog ();
			break;
		default:
			break;
		}

	}
	
	
	
	 /**
     * 创建日期及时间选择对话框
     */
    protected void showDateDialog () {
    	Calendar calendar = Calendar.getInstance();
    	DatePickerDialog dialog = new DatePickerDialog(getActivity(), 
                dateListener, 
                calendar.get(Calendar.YEAR), 
                calendar.get(Calendar.MONTH), 
                calendar.get(Calendar.DAY_OF_MONTH)); 
    	
    	dialog.show();
    }
	
	
    DatePickerDialog.OnDateSetListener dateListener =  
    	    new DatePickerDialog.OnDateSetListener() { 
    	        @Override 
    	        public void onDateSet(DatePicker datePicker,  
    	                int year, int month, int dayOfMonth) { 
    	             //Calendar月份是从0开始,所以month要加1 
    	        	birthDay.setText(year + "-" +  
    	                    (month+1) + "-" + dayOfMonth); 
    	        } 
    	    }; 
	
	
	
	
	
	
	
	
	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
//		switch (requestCode) { 
////		 case 3:
//		 case 4:
			 if(resultCode == Activity.RESULT_OK )
			 {
				 int mYear = data.getIntExtra("year", 0);
				 int mMonth = data.getIntExtra("month", 0);
				 int mDay = data.getIntExtra("day", 0);
//				 SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
//				 agent_query_date.setText(mYear+"年"+mMonth+"月"+mDay);
				 Log.i(TAG, ""+mYear+""+mMonth+""+mDay);
				 birthDay.setText(""+mYear+""+mMonth+""+mDay);
			 }
//			 break;
//		}
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
