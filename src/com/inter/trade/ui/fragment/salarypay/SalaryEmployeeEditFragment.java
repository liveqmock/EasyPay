package com.inter.trade.ui.fragment.salarypay;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.inter.trade.R;
import com.inter.trade.ui.fragment.salarypay.bean.EmployerData;
import com.inter.trade.ui.fragment.salarypay.task.AddEmployerInfoTask;
import com.inter.trade.ui.fragment.salarypay.task.DeleteEmployerInfoTask;
import com.inter.trade.ui.fragment.salarypay.task.EditEmployerInfoTask;
import com.inter.trade.ui.fragment.salarypay.task.GetEmployerInfoTask;
import com.inter.trade.ui.fragment.waterelectricgas.data.ResponseStateListener;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PreferenceConfig;
import com.inter.trade.util.PromptUtil;
import com.inter.trade.util.UserInfoCheck;

/**
 * 工资员工信息的添加和编辑
 * 
 * @author chenguangchi
 * @data: 2014年9月2日 下午4:21:58
 * @version: V1.0
 */
public class SalaryEmployeeEditFragment extends SalaryPayBaseFragment implements OnClickListener,ResponseStateListener
		,TextWatcher{
	
	private EditText et_phone,et_salary,et_name;
	private Button btn_delete,btn_save;
	private TextView tv_registermark,tv_haveCard,tv_cardno;
	
	private EmployerData employer;
	
	private String currentMonth;
	
	private String type;
	
	private String bossauthorid;

	public static SalaryEmployeeEditFragment getInstance(Bundle bundle){
		SalaryEmployeeEditFragment fragment=new SalaryEmployeeEditFragment();
		fragment.setArguments(bundle);
		return fragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		setTopTitle("登记工资");
	}

	private void initView(View view) {
		
		et_phone=(EditText) view.findViewById(R.id.et_phone);
		et_salary=(EditText) view.findViewById(R.id.et_salary);
		et_name=(EditText) view.findViewById(R.id.et_name);
		btn_delete=(Button) view.findViewById(R.id.btn_delete);
		btn_save=(Button) view.findViewById(R.id.btn_save);
		tv_registermark=(TextView) view.findViewById(R.id.tv_registermark);
		tv_haveCard=(TextView) view.findViewById(R.id.tv_haveCard);
		tv_cardno=(TextView) view.findViewById(R.id.tv_cardno);
		
		btn_delete.setOnClickListener(this);
		btn_save.setOnClickListener(this);
		et_phone.addTextChangedListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_delete://删除数据
			new DeleteEmployerInfoTask(getActivity(), this).execute(employer.id,bossauthorid);
			break;
		case R.id.btn_save://保存记录
			if(checkInfo()){
				if("add".equals(type)){
					new AddEmployerInfoTask(getActivity(), this).execute(employer);
				}else{
					new EditEmployerInfoTask(getActivity(), this).execute(employer);
				}
			}
			break;
		default:
			break;
		}
	}
	
	
	public boolean checkInfo(){
		
		if(TextUtils.isEmpty(et_phone.getText()+"")){
			PromptUtil.showToast(getActivity(), "请填写员工手机号码!");
			return false;
		}
		if(!UserInfoCheck.checkMobilePhone(et_phone.getText()+"")){
			PromptUtil.showToast(getActivity(), "请填写正确的手机号!");
			return false;
		}
		if(TextUtils.isEmpty(et_salary.getText()+"")){
			PromptUtil.showToast(getActivity(), "请填写员工的工资!");
			return false;
		}
		if(TextUtils.isEmpty(et_name.getText()+"")){
			PromptUtil.showToast(getActivity(), "请填写员工的姓名!");
			return false;
		}
		
		if(employer==null){
			employer=new EmployerData();
		}
		employer.month=currentMonth;
		employer.phone=et_phone.getText()+"";
		employer.money=et_salary.getText()+"";
		employer.name=et_name.getText()+"";
		employer.bossauthorid=bossauthorid;
		return true;
	}
	

	@Override
	protected View onInitView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_salary_employee_edit, null);
		initView(view);
		initData();
		return view;
	}

	private void initData() {
		
		bossauthorid = PreferenceConfig.instance(getActivity()).getString("bossAuthorid", LoginUtil.mLoginStatus.authorid);
		
		Bundle bun = getArguments();
		if(bun!=null){
			type = bun.getString("type");
			if("add".equals(type)){
				currentMonth =bun.getString("month");
				btn_delete.setVisibility(View.GONE);
			}else if("update".equals(type)){
				btn_delete.setVisibility(View.VISIBLE);
				employer=(EmployerData) bun.getSerializable("data");
				et_name.setText(employer.name);
				et_phone.setText(employer.phone);
				et_phone.setEnabled(false);
				et_salary.setText(employer.money);
				
				tv_registermark.setText(employer.hasRegister);
				if(TextUtils.isEmpty(employer.bankAccount)){
					tv_haveCard.setText("未登记");
					tv_cardno.setText("");
				}else{
					tv_haveCard.setText("已登记");
					tv_cardno.setText(employer.bankAccount);
				}
			}
		}
	}

	@Override
	protected void onAsyncLoadData() {
	}

	@Override
	public void onRefreshDatas() {
		
	}

	@Override
	public void onTimeout() {
		
	}

	@Override
	public void onSuccess(Object obj, Class cla) {
		if(EmployerData.class.equals(cla)){
			EmployerData employer=(EmployerData) obj;
			if(employer.name!=null){
				et_name.setText(employer.name);
			}else{
				et_name.setText("");
			}
			tv_registermark.setText(employer.hasRegister);
			if(TextUtils.isEmpty(employer.bankAccount)){
				tv_haveCard.setText("未登记");
				tv_cardno.setText("");
			}else{
				tv_haveCard.setText("已登记");
				tv_cardno.setText(employer.bankAccount);
			}
	  }else if(String.class.equals(cla)){
		  removeFragmentToStack();
	  }else{
		  removeFragmentToStack();
	  }
	}


	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		
		if(s.length()==11 && "add".equals(type)){
			new GetEmployerInfoTask(getActivity(), this).execute(s+"");
		}
	}
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		
	}
	@Override
	public void afterTextChanged(Editable s) {
		
	}
}
