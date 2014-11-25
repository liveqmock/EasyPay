package com.inter.trade.ui.fragment.salarypay;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.inter.trade.R;
import com.inter.trade.ResponseMoreStateListener;
import com.inter.trade.ResponseWithTimeoutStateListener;
import com.inter.trade.ui.activity.SalaryPayMainActivity;
import com.inter.trade.ui.creditcard.CommonEasyCreditcardPayActivity;
import com.inter.trade.ui.fragment.salarypay.bean.PayInfo;
import com.inter.trade.ui.fragment.salarypay.task.GetSalaryInfoTask;
import com.inter.trade.ui.fragment.waterelectricgas.data.ResponseStateListener;
import com.inter.trade.ui.manager.core.UIConstantDefault;

/**
 * 工资支付完成页面
 * 
 * @author chenguangchi
 * @data: 2014年9月2日 下午4:28:11
 * @version: V1.0
 */
public class SalaryPaySuccessFragment extends SalaryPayBaseFragment implements
		OnClickListener,ResponseWithTimeoutStateListener{

	private TextView tvPersonNumber, tvMoneyPaid, tvMoneyLeft, tvTotal,
			tvSuccess,tvTotalPay,tv_info,tv_success_one;

	private Button btnHistory, btnGoOn;

	private TableLayout llUnfinish;
	
	/**
	 * 加载中的显示
	 */
	private TextView iLoading;
	
	/**
	 * 内容显示
	 */
	private LinearLayout llPage;
	
	private String personnumber;
	private String wageallmoney;
	private String paidmoney;
	private String wagemonth;
	private String wagelistid;
	
	public static SalaryPaySuccessFragment getInstance(Bundle bundle){
		SalaryPaySuccessFragment fragment=new SalaryPaySuccessFragment();
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
		setTopTitle("支付结果");
	}

	@Override
	protected View onInitView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_salarypay_success, null);
		initView(view);
		initData();
		return view;
	}


	@Override
	protected void setBackButton(View base) {
		 base.findViewById(R.id.back_btn).setVisibility(View.INVISIBLE);
	}

	private void initView(View view) {
		btnHistory = (Button) view.findViewById(R.id.btn_history);
		btnGoOn = (Button) view.findViewById(R.id.btn_goon);

		tvPersonNumber = (TextView) view.findViewById(R.id.tv_person_number);
		tvMoneyPaid = (TextView) view.findViewById(R.id.tv_money_paid);
		tvMoneyLeft = (TextView) view.findViewById(R.id.tv_money_left);
		tvTotal = (TextView) view.findViewById(R.id.tv_total);
		llUnfinish = (TableLayout) view.findViewById(R.id.tl_unfinish);
		tvSuccess = (TextView) view.findViewById(R.id.tv_success);
		tvTotalPay=(TextView) view.findViewById(R.id.tv_total_pay);
		iLoading=(TextView) view.findViewById(R.id.iv_loading);
		llPage=(LinearLayout) view.findViewById(R.id.ll_page);
		tv_info=(TextView) view.findViewById(R.id.tv_info);
		tv_success_one=(TextView) view.findViewById(R.id.tv_success_one);

		btnHistory.setOnClickListener(this);
		btnGoOn.setOnClickListener(this);
	}
	
	private void initData() {
		Bundle bundle = getArguments();
		personnumber = bundle.getString("personnumber");
		wageallmoney = bundle.getString("wageallmoney");
		paidmoney = bundle.getString("paidmoney");
		wagemonth = bundle.getString("wagemonth");
		wagelistid=bundle.getString("wagelistid");
		
		
		new GetSalaryInfoTask(getActivity(), SalaryPaySuccessFragment.this,true).execute(wagemonth,wagelistid);
		/*try {
			Double allmoney=Double.parseDouble(wageallmoney);//需要支付的工资总额
			Double paymoney=Double.parseDouble(paidmoney);//已支付的总额
			if((allmoney-paymoney)>0){//还需继续支付
				hideMessage();
				tvPersonNumber.setText(personnumber+"");
				tvTotal.setText(wageallmoney+"元");
				tvMoneyPaid.setText(paidmoney+"元");
				tvMoneyLeft.setText((allmoney-paymoney)+"元");
			}else{//不需要继续支付
				showMessage();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}*/
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
	/**
	 * 隐藏成功提示 
	 * @throw
	 * @return void
	 */
	private void hideMessage() {
		llUnfinish.setVisibility(View.VISIBLE);
		tvSuccess.setVisibility(View.GONE);
		tv_success_one.setVisibility(View.GONE);
		btnGoOn.setEnabled(true);
	}

	private void showMessage() {
		llUnfinish.setVisibility(View.GONE);
		tvSuccess.setVisibility(View.VISIBLE);
		tv_success_one.setVisibility(View.VISIBLE);
		btnGoOn.setVisibility(View.GONE);
		tv_info.setVisibility(View.GONE);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_goon:
			if(getActivity() instanceof CommonEasyCreditcardPayActivity){
				getActivity().finish();
			}else if(getActivity() instanceof SalaryPayMainActivity){
				getFragmentManager().popBackStack();
			}
			break;
		case R.id.btn_history:
			//addFragmentToStack(UIConstantDefault.UI_CONSTANT_SALARYPAY_HISTORY, 1, null);
//			Intent intent=new Intent();
//			intent.setClass(getActivity(), SalaryPayMainActivity.class);
//			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			intent.putExtra("targetFragment",
//					UIConstantDefault.UI_CONSTANT_SALARYPAY_LISTCONFIRM);
//			startActivity(intent);
			backHomeFragment();
			addFragmentToStack(UIConstantDefault.UI_CONSTANT_SALARYPAY_LISTCONFIRM, 1, null);
			break;

		default:
			break;
		}
	}

	/**
	 * 重载方法
	 * @param obj
	 * @param cla
	 */
	@Override
	public void onSuccess(Object obj, Class cla) {
		PayInfo info=(PayInfo) obj;
		
		Double allmoney=Double.parseDouble(info.needpaymoney);//需要支付的工资总额
		Double paymoney=Double.parseDouble(info.wagepaymoney);//已支付的总额
		Double wageall=Double.parseDouble(info.wageallmoney);//工资总额
		
		if((allmoney)>0){//还需继续支付
			
			hideMessage();
			tvPersonNumber.setText(personnumber+"");
			tvTotal.setText(wageall+"元");
			tvMoneyPaid.setText(paymoney+"元");
			tvMoneyLeft.setText(info.needpaymoney+"元");
			//tvTotalPay.setText(info.needpaymoney+"元");
		}else{//不需要继续支付
			showMessage();
		}
		llPage.setVisibility(View.VISIBLE);
		iLoading.setVisibility(View.INVISIBLE);
	}

	@Override
	public void onFailure(Object obj, Class cla) {//超时或者网络异常的处理
		new GetSalaryInfoTask(getActivity(), SalaryPaySuccessFragment.this,true).execute(wagemonth);
	}

	@Override
	public void onTimeOut(Object obj, Class cla) {
		new GetSalaryInfoTask(getActivity(), SalaryPaySuccessFragment.this,true).execute(wagemonth);
	}
}
