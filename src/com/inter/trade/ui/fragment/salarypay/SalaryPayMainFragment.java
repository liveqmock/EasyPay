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

import com.inter.trade.R;
import com.inter.trade.ui.fragment.salarypay.bean.EmployerData;
import com.inter.trade.ui.fragment.salarypay.bean.FinancialStuff;
import com.inter.trade.ui.fragment.salarypay.task.BindFinancialTask;
import com.inter.trade.ui.fragment.salarypay.task.GetEmployerInfoTask;
import com.inter.trade.ui.fragment.salarypay.task.GetFinancialTask;
import com.inter.trade.ui.fragment.waterelectricgas.data.ResponseStateListener;
import com.inter.trade.ui.manager.core.UIConstantDefault;
import com.inter.trade.util.Constants;
import com.inter.trade.util.PreferenceConfig;
import com.inter.trade.util.PromptUtil;
import com.inter.trade.util.UserInfoCheck;

/**
 * 发工资主页
 * 
 * @author chenguangchi
 * @data: 2014年9月2日 下午4:21:58
 * @version: V1.0
 */
public class SalaryPayMainFragment extends SalaryPayBaseFragment implements OnClickListener,ResponseStateListener,TextWatcher{

	private EditText etAccount,etName;

	private Button btnCommit;
	
	private boolean isNormal=true;

	public static SalaryPayMainFragment getInstance(Bundle bundle){
		SalaryPayMainFragment fragment=new SalaryPayMainFragment();
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
		setTopTitle("指定财务");
		//setRightButtonIconOnClickListener(View.VISIBLE, null);
	}

	private void initView(View view) {
		etAccount=(EditText) view.findViewById(R.id.et_account);
		btnCommit=(Button) view.findViewById(R.id.btn_finish);
		etName=(EditText) view.findViewById(R.id.et_name);
		btnCommit.setOnClickListener(this);
		etAccount.addTextChangedListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_finish:
			if(checkInput()){
				String cwmobile = (etAccount.getText()+"").trim();
				new BindFinancialTask(getActivity(), this).execute(cwmobile);	
			}
			break;

		default:
			break;
		}
	}
	
	private boolean checkInput(){
		
		if(TextUtils.isEmpty(etAccount.getText())){
			PromptUtil.showToast(getActivity(), "请输入财务人员通付宝账号");
			return false;
		}else if(!UserInfoCheck.checkMobilePhone(etAccount.getText()+"")){
			PromptUtil.showToast(getActivity(), "请输入正确的手机号码");
			return false;
		}
		
		return true;
	}

	@Override
	protected View onInitView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_salarypay_main, null);
		initView(view);
		initData();
		return view;
	}

	private void initData() {
		Bundle bundle = getArguments();
		if(bundle!=null){
			isNormal=bundle.getBoolean("isNormal");
		}
	}

	@Override
	protected void onAsyncLoadData() {
//		String user_name = PreferenceConfig.instance(getActivity()).getString(Constants.USER_NAME, "");
	new GetFinancialTask(getActivity(), this).execute("");
	}

	@Override
	public void onRefreshDatas() {
		
	}

	@Override
	public void onTimeout() {
		
	}

	@Override
	public void onSuccess(Object obj, Class cla) {
		if(EmployerData.class.equals(cla)){//获取财务人员返回
			EmployerData stuff=(EmployerData) obj;
			
			if(stuff!=null){
				if(stuff.name==null){
					stuff.name="未登记";
				}
				etName.setText(stuff.name);
			}
		}else if(String.class.equals(cla)){//绑定成功
			if(isNormal){
				addFragmentToStack(UIConstantDefault.UI_CONSTANT_SALARYPAY_LISTCONFIRM, 1, null);
			}else{
				removeFragmentToStack();
			}
			PreferenceConfig.instance(getActivity()).putString(Constants.FINACIAL_BIND, "1");
		}else if(FinancialStuff.class.equals(cla)){
			FinancialStuff financial=(FinancialStuff) obj;
			etAccount.setText(financial.cwmobile);
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		if(s.length()==11){
			new GetEmployerInfoTask(getActivity(), this).execute(s+"");
		}
	}
	@Override
	public void afterTextChanged(Editable s) {
	}
}
