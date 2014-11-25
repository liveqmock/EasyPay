package com.inter.trade.ui.fragment.salarypay;

import java.util.ArrayList;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.inter.trade.R;
import com.inter.trade.data.BankData;
import com.inter.trade.db.DBHelper;
import com.inter.trade.db.SupportBank;
import com.inter.trade.ui.creditcard.task.BankTask;
import com.inter.trade.ui.fragment.salarypay.bean.EmployerData;
import com.inter.trade.ui.fragment.salarypay.bean.FinancialStuff;
import com.inter.trade.ui.fragment.salarypay.task.GetBossAuthoridTask;
import com.inter.trade.ui.fragment.salarypay.task.GetFinancialTask;
import com.inter.trade.ui.fragment.waterelectricgas.data.ResponseStateListener;
import com.inter.trade.ui.manager.core.UIConstantDefault;
import com.inter.trade.util.Constants;
import com.inter.trade.util.ListUtils;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PreferenceConfig;

/**
 * 工资主页
 * 
 * @author chenguangchi
 * @data: 2014年9月2日 下午4:21:58
 * @version: V1.0
 */
public class SalaryMainFragment extends SalaryPayBaseFragment implements OnClickListener,ResponseStateListener {

	private Button btnBoss,btnEmployee;
	
	/**
	 * bossAuthorid
	 */
	private String bossAuthorid;

	public static SalaryMainFragment getInstance(Bundle bundle){
		SalaryMainFragment fragment=new SalaryMainFragment();
		fragment.setArguments(bundle);
		return fragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		PreferenceConfig.instance(getActivity()).putString(Constants.FINACIAL_BIND, "0");
	}
	
	@Override
	public void onResume() {
		super.onResume();
		setTopTitle("工资收发");
	}

	private void initView(View view) {
		
		btnBoss=(Button) view.findViewById(R.id.btn_boss);
		btnEmployee=(Button) view.findViewById(R.id.btn_employee);
		
		btnBoss.setOnClickListener(this);
		btnEmployee.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_boss://老板
			if(TextUtils.isEmpty(bossAuthorid)){
				bossAuthorid=LoginUtil.mLoginStatus.authorid;
				PreferenceConfig.instance(getActivity()).putString("bossAuthorid", bossAuthorid);
			}
			Bundle b=new Bundle();
			b.putString("bossauthorid", bossAuthorid);
			addFragmentToStack(UIConstantDefault.UI_CONSTANT_SALARYPAY_LISTCONFIRM, 1, b);
			break;
		case R.id.btn_employee://员工
			addFragmentToStack(UIConstantDefault.UI_CONSTANT_SALARYGET_MAIN, 1, null);
			break;
		default:
			break;
		}
	}
	

	@Override
	protected View onInitView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_salary_main, null);
		initView(view);
		initData();
		return view;
	}

	private void initData() {
	}

	@Override
	protected void onAsyncLoadData() {
		//new BankTask(getActivity(), this,true).execute("");
		new GetBossAuthoridTask(getActivity(), this).execute();
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
		if(BankData.class.equals(cla)){//获取易宝支付银行列表
			ArrayList<BankData> list=(ArrayList<BankData>) obj;
			ArrayList<SupportBank> bankList=new ArrayList<SupportBank>();
			if(!ListUtils.isEmptyList(list)){
				for(BankData data:list){
					SupportBank bank=new SupportBank(null, data.bankid, data.bankno, data.bankname);
					bankList.add(bank);
				}
				DBHelper helper=DBHelper.getInstance(getActivity());
				helper.insertBanks(bankList);
			}
		}else if(String.class.equals(cla)){//获取BossAuthorid
			bossAuthorid=(String) obj;
			
			PreferenceConfig.instance(getActivity()).putString("bossAuthorid", bossAuthorid);
		}else if(FinancialStuff.class.equals(cla)){//获取财务人员返回
			FinancialStuff stuff=(FinancialStuff) obj;
			if(stuff.cwmobile!=null && stuff.cwmobile.length()>=11){
				PreferenceConfig.instance(getActivity()).putString(Constants.FINACIAL_BIND, "1");
			}
		}
		
	}
	
	private DBHelper helper;
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if(helper!=null){
			helper.closeDB();
		}
	}
}
