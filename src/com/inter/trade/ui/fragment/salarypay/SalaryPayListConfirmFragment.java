package com.inter.trade.ui.fragment.salarypay;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.inter.trade.R;
import com.inter.trade.ui.fragment.salarypay.WarmPromtDialog.ButtonListener;
import com.inter.trade.ui.fragment.salarypay.adapter.EmployerListAdapter;
import com.inter.trade.ui.fragment.salarypay.adapter.EmployerListAdapter.EditButtonListener;
import com.inter.trade.ui.fragment.salarypay.bean.EmployerData;
import com.inter.trade.ui.fragment.salarypay.bean.PayInfo;
import com.inter.trade.ui.fragment.salarypay.bean.SalaryData;
import com.inter.trade.ui.fragment.salarypay.task.BackToFinancialTask;
import com.inter.trade.ui.fragment.salarypay.task.FinancialSubmitTask;
import com.inter.trade.ui.fragment.salarypay.task.GetEmployerListTask;
import com.inter.trade.ui.fragment.salarypay.task.GetSalaryInfoTask;
import com.inter.trade.ui.fragment.salarypay.task.NomoreEditTask;
import com.inter.trade.ui.fragment.waterelectricgas.data.ResponseStateListener;
import com.inter.trade.ui.manager.core.UIConstantDefault;
import com.inter.trade.util.Constants;
import com.inter.trade.util.DateUtil;
import com.inter.trade.util.ListUtils;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PreferenceConfig;
import com.inter.trade.util.PromptUtil;

/**
 * 员工工资清单页面
 * 
 * @author chenguangchi
 * @data: 2014年9月3日 下午3:49:11
 * @version: V1.0
 */
public class SalaryPayListConfirmFragment extends SalaryPayBaseFragment
		implements OnClickListener, ResponseStateListener, EditButtonListener,
		ButtonListener {

	private Button btnBack, btnGoBack, btnCommit, btnBefore, btnAfter, btnAdd,btn_submittoboss;

	private TextView tvDate,tvSummary;

	private LinearLayout ll_bottom_btn;//底部双按钮
	
	private ListView mListView;

	private ArrayList<EmployerData> mDatas;
	
	private LinearLayout llBottom;

	private EmployerListAdapter adapter;

	/**
	 * 列表选中的员工
	 */
	private EmployerData employer;

	/**
	 * 当前的月份
	 */
	private String currentDate;
	
	/**
	 * 支付信息
	 */
	private PayInfo payInfo;
	
	/**
	 * bossAuthorid
	 */
	private String bossAuthorid;

	public static SalaryPayListConfirmFragment getInstance(Bundle b){
		SalaryPayListConfirmFragment f=new SalaryPayListConfirmFragment();
		f.setArguments(b);
		return f;
	}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onResume() {
		super.onResume();
		setTopTitle("发放工资");
		setRightButtonIconOnClickListener(View.VISIBLE, null);
	}

	@Override
	protected View onInitView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (container == null) {
			return null;
		}
		View view = inflater.inflate(R.layout.fragment_salarypay_listconfirm,
				null);

		initView(view);
		initData();

		return view;
	}

	private void initView(View view) {

		mListView = (ListView) view.findViewById(R.id.listview);
		View emptyView = view.findViewById(R.id.tv_empty);
		mListView.setEmptyView(emptyView);

		btnBack = (Button) view.findViewById(R.id.btn_back);
		btnGoBack = (Button) view.findViewById(R.id.btn_goback);
		btnCommit = (Button) view.findViewById(R.id.btn_commit);
		btnBefore = (Button) view.findViewById(R.id.before_month);
		btnAfter = (Button) view.findViewById(R.id.after_month);
		tvDate = (TextView) view.findViewById(R.id.calendar);
		btnAdd = (Button) view.findViewById(R.id.btn_employee_add);
		llBottom=(LinearLayout) view.findViewById(R.id.bottom);
		tvSummary=(TextView) view.findViewById(R.id.tv_summary);
		btn_submittoboss=(Button) view.findViewById(R.id.btn_submittoboss);
		ll_bottom_btn=(LinearLayout) view.findViewById(R.id.ll_bottom_btn);

		btnBack.setOnClickListener(this);
		btnGoBack.setOnClickListener(this);
		btnCommit.setOnClickListener(this);
		btnBefore.setOnClickListener(this);
		btnAfter.setOnClickListener(this);
		btnAdd.setOnClickListener(this);
		btn_submittoboss.setOnClickListener(this);
	}

	private void initData() {
		Calendar calendar = Calendar.getInstance();
		Date time = calendar.getTime();
		currentDate = DateUtil.getYYYYMMFormatStr(time);
		tvDate.setText(currentDate);
		
		Bundle b = getArguments();
		if(b!=null){
			bossAuthorid=b.getString("bossauthorid");
		}
		if(bossAuthorid==null){
			bossAuthorid=PreferenceConfig.instance(getActivity()).getString("bossAuthorid", LoginUtil.mLoginStatus.authorid);
		}
		
		if(!bossAuthorid.equals(LoginUtil.mLoginStatus.authorid)){//财务人员不可以支付和不可以指定财务人员
			btnBack.setVisibility(View.GONE);
			ll_bottom_btn.setVisibility(View.GONE);
			btn_submittoboss.setVisibility(View.VISIBLE);
//			RelativeLayout.LayoutParams layoutParams = (android.widget.RelativeLayout.LayoutParams) mListView.getLayoutParams();
//			layoutParams.setMargins(0, 0, 0, 0);
		}
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:// 指定财务
			Bundle b = new Bundle();
			b.putBoolean("isNormal", false);
			addFragmentToStack(UIConstantDefault.UI_CONSTANT_SALARYPAY_MAIN, 1,
					b);
			break;
		case R.id.btn_goback:// 退回财务修订
				new BackToFinancialTask(getActivity(), this)
						.execute(currentDate,payInfo.wagelistid,1+"");
			break;
		case R.id.btn_commit:// 确定并支付
			if (isPay()) {
				
				if(mDatas != null && mDatas.size() > 0 && mDatas.get(0) != null
						&& mDatas.get(0).canEdit != null
						&& !isHasEditItem()){//不可编辑
					gotoPayPage();
				}else{//可编辑
					WarmPromtDialog dialog = new WarmPromtDialog();
					dialog.show(getActivity(), this);
				}
			}
			break;

		case R.id.after_month:// 下个月
			getDate(true);
			updateTheList();
			break;

		case R.id.before_month:// 上个月
			getDate(false);
			updateTheList();
			break;
		case R.id.btn_employee_add:// 添加员工
//			if (mDatas != null && mDatas.size() > 0 && mDatas.get(0) != null
//					&& mDatas.get(0).canEdit != null
//					&& "0".equals(mDatas.get(0).canEdit)) {
//			
//			} else {
				Bundle bun = new Bundle();
				bun.putString("type", "add");
				bun.putString("month", currentDate);
				addFragmentToStack(
						UIConstantDefault.UI_CONSTANT_SALARY_EMPLOYEE_EDIT, 1,
						bun);
//			}
			break;
		case R.id.btn_submittoboss://财务确认提交到boss
			
			if(canSubmit()){
				new FinancialSubmitTask(getActivity(), this).execute(bossAuthorid,mDatas.get(0).wagelistid);
			}
			
			break;
		default:
			break;
		}
	}

	/**
	 * 检查是否需要支付工资
	 * 
	 * @return
	 * @throw
	 * @return boolean
	 */
	private boolean isPay() {
		if(!bossAuthorid.equals(LoginUtil.mLoginStatus.authorid)){//财务人员不可以支付
			PromptUtil.showLongToast(getActivity(), "亲,当前版本暂不支持财务人员支付!");
			return false;
		}
		
		if (mDatas == null || mDatas.size() == 0) {
			PromptUtil.showLongToast(getActivity(), "亲,当前的月份没有需要支付的工资!");
			return false;
		}
		if(payInfo!=null){
			double needpaymoney = Double.parseDouble(payInfo.needpaymoney);
			double allmoney = Double.parseDouble(payInfo.wageallmoney);
			double wagepaymoney = Double.parseDouble(payInfo.wagepaymoney);
			if(allmoney==0){
				PromptUtil.showLongToast(getActivity(), "亲,当前的月份的工资为0元!");
				return false;
			}
			if(needpaymoney<=0){
				PromptUtil.showLongToast(getActivity(), "亲,当前的月份的工资已支付!");
				return false;
			}
		}
		return true;
	}
	
	
	
	private boolean canSubmit(){
		if(isHasZeroItem()){
			
			PromptUtil.showLongToast(getActivity(), "亲,当前的月份的工资包含为0的记录!");
			return false;
		}
		if(isSumZero()){
			PromptUtil.showLongToast(getActivity(), "亲,当前的月份的工资为0元!");
			return false;
		}
			
		return true;
	}

	/**
	 * 更新当前的列表
	 * 
	 * @throw
	 * @return void
	 */
	private void updateTheList() {
		tvDate.setText(currentDate);
		new GetEmployerListTask(getActivity(), this).execute(currentDate,bossAuthorid);
	}

	/**
	 * 
	 * 获取上个月或下个月的日期
	 * 
	 * @param isNext
	 *            表示是否下个月 true 为下个月,false 为上个月
	 * @throw
	 * @return void
	 */
	private void getDate(boolean isNext) {
		Date date = DateUtil.getDateForYYYYMM(currentDate);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		if (isNext) {
			c.add(Calendar.MONTH, +1);
		} else {
			c.add(Calendar.MONTH, -1);
		}
		Date time = c.getTime();
		currentDate = DateUtil.getYYYYMMFormatStr(time);
	}

	@Override
	protected void onAsyncLoadData() {
		new GetEmployerListTask(getActivity(), this).execute(currentDate,bossAuthorid);
	}

	@Override
	public void onRefreshDatas() {
		new GetEmployerListTask(getActivity(), this).execute(currentDate,bossAuthorid);
		
		String finacialbind=PreferenceConfig.instance(getActivity()).getString(Constants.FINACIAL_BIND, "0");
		if("1".equals(finacialbind)){
			btnGoBack.setVisibility(View.VISIBLE);
		}else{
			btnGoBack.setVisibility(View.GONE);
		}
		
	}

	@Override
	public void onTimeout() {

	}

	@Override
	public void onSuccess(Object obj, Class cla) {
		if (EmployerData.class.equals(cla)) {//获取工资列表
			mDatas = (ArrayList<EmployerData>) obj;
		
			
			/*if (mDatas != null && mDatas.size() > 0 && mDatas.get(0) != null
					&& mDatas.get(0).canEdit != null
					&& "0".equals(mDatas.get(0).canEdit)) {
				btnAdd.setEnabled(false);
				
			} else {
				btnAdd.setEnabled(true);
			}*/
			if(isHasEditItem()){
				btnGoBack.setEnabled(true);
			}else{
				btnGoBack.setEnabled(false);
			}
			if(isHasCWEditItem()){
				btn_submittoboss.setEnabled(true);
			}else{
				btn_submittoboss.setEnabled(false);
			}
			
			
			if(!ListUtils.isEmptyList(mDatas)){
				new GetSalaryInfoTask(getActivity(), this,false).execute(currentDate,mDatas.get(0).wagelistid);
			}
		} else if (String.class.equals(cla)) {//退回财务
			//removeFragmentToStack();
			new GetEmployerListTask(getActivity(), this).execute(currentDate,bossAuthorid);
		}else if(PayInfo.class.equals(cla)){//获取支付信息
			payInfo=(PayInfo) obj;
			
			if(!bossAuthorid.equals(LoginUtil.mLoginStatus.authorid)){
				adapter = new EmployerListAdapter(getActivity(), mDatas, this,false);
			}else{
				adapter = new EmployerListAdapter(getActivity(), mDatas, this,true);
			}
			
			mListView.setAdapter(adapter);
			adapter.notifyDataSetChanged();
			String string = getActivity().getResources().getString(R.string.salarypay_summary);
			String format = String.format(string, mDatas.size()+"",getSum()+"");
			tvSummary.setText(format);
			
		}else {
			gotoPayPage();
		}
	}
	
	/**
	 *跳转到支付页面
	 * @throw
	 * @return void
	 */
	private void gotoPayPage() {
		Bundle bundle = new Bundle();
		bundle.putString("date", currentDate);
		bundle.putInt("personnumber", adapter.getCount());
		bundle.putString("wagelistid", mDatas.get(0).wagelistid);
		addFragmentToStack(UIConstantDefault.UI_CONSTANT_SALARYPAY_CONFIRM,
				1, bundle);
	}

	/**
	 * 重载方法
	 */
	@Override
	public void onClickEdit(int position) {
		employer = mDatas.get(position);
		Bundle bun = new Bundle();
		bun.putString("type", "update");
		bun.putSerializable("data", employer);
		addFragmentToStack(UIConstantDefault.UI_CONSTANT_SALARY_EMPLOYEE_EDIT,
				1, bun);
	}

	@Override
	public void onPositive() {
		new NomoreEditTask(getActivity(), this).execute(currentDate,bossAuthorid);
	}
	
	private boolean isHasEditItem(){//判断是否可以编辑
		
		for(EmployerData data :mDatas){
				if("1".equals(data.canEdit)){
					return true;
				}
		}
		return false;
	}
	
	/**
	 * 
	 * 是否包含为0的工资项
	 * @return
	 * @throw
	 * @return boolean
	 */
	private boolean isHasZeroItem(){
		
		for(EmployerData data:mDatas){
			if(Double.parseDouble(data.money)==0){
				 return true;
			}
		}
		return false;
	}
	
	/**
	 * 是否工资总额为0
	 * @return
	 * @throw
	 * @return boolean
	 */
	private boolean isSumZero(){
		double sum=0;
		for(EmployerData data:mDatas){
			sum+=Double.parseDouble(data.money);
		}
		if(sum==0){
			return true;
		}
		return false;
	}
	
	/**
	 * 是否工资总额为0
	 * @return
	 * @throw
	 * @return boolean
	 */
	private Double getSum(){
		double sum=0;
		for(EmployerData data:mDatas){
			sum+=Double.parseDouble(data.money);
		}
		return sum;
	}
	
	
	/**
	 * //判断财务可以编辑 
	 * @return
	 * @throw
	 * @return boolean
	 */
	private boolean isHasCWEditItem(){
		
		for(EmployerData data :mDatas){
				if("1".equals(data.cwcanEdit)){
					return true;
				}
		}
		return false;
	}
}
