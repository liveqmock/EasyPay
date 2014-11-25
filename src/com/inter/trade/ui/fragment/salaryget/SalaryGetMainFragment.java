package com.inter.trade.ui.fragment.salaryget;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.inter.trade.R;
import com.inter.trade.ResponseMoreStateListener;
import com.inter.trade.ui.BankCardActivity;
import com.inter.trade.ui.fragment.salaryget.SalaryGetAdapter.SalaryGetListener;
import com.inter.trade.ui.fragment.salaryget.bean.SalaryGet;
import com.inter.trade.ui.fragment.salaryget.task.GetPayGetListTask;
import com.inter.trade.ui.fragment.salaryget.task.PayGetTask;
import com.inter.trade.ui.fragment.salaryget.util.BindBandTipsDialog;
import com.inter.trade.ui.fragment.salaryget.util.BindBandTipsDialog.BindBankTipListener;
import com.inter.trade.ui.fragment.salaryget.util.BindSuccessTipsDialog;
import com.inter.trade.ui.fragment.salaryget.util.BindSuccessTipsDialog.SalaryGetSuccessListener;
import com.inter.trade.util.DateUtil;

/**
 * 签收工资主页
 * 
 * @author chenguangchi
 * @data: 2014年9月2日 下午4:21:58
 * @version: V1.0
 */
public class SalaryGetMainFragment extends SalaryGetBaseFragment implements OnClickListener,ResponseMoreStateListener 
		,BindBankTipListener,SalaryGetSuccessListener,SalaryGetListener{


	private Button btnAfter,btnBefore;
	private TextView tvDate,tvText,tv_empty;
	
	private View line;
	
	private String currentDate;
	
	private GetPayGetListTask getTask;
	
	private SalaryGet salaryGet;
	
	private ListView mListView;
	
	private SalaryGetAdapter adapter;
	
	private ArrayList<SalaryGet> list;//数据
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		setTopTitle("工资签收");
		setRightButtonIconOnClickListener(View.VISIBLE, null);
	}

	private void initView(View view) {
		btnAfter=(Button) view.findViewById(R.id.after_month);
		btnBefore=(Button) view.findViewById(R.id.before_month);
		mListView=(ListView) view.findViewById(R.id.listview);
		tvText=(TextView) view.findViewById(R.id.tv_text);
		line=view.findViewById(R.id.line);
		tv_empty=(TextView) view.findViewById(R.id.tv_empty);
		
		btnAfter.setOnClickListener(this);
		btnBefore.setOnClickListener(this);
		
		tvDate = (TextView) view.findViewById(R.id.calendar);
		mListView.setEmptyView(tv_empty);
	}
	
	private void initData() {
		Calendar calendar=Calendar.getInstance();
		Date time = calendar.getTime();
		currentDate= DateUtil.getYYYYMMFormatStr(time);
		tvDate.setText(currentDate);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_finish:
			
			break;
		case R.id.after_month:
			getDate(true);
			tvDate.setText(currentDate);
			getCurrentMonthSalary();
			break;
		case R.id.before_month:
			getDate(false);
			tvDate.setText(currentDate);
			getCurrentMonthSalary();
			break;

		default:
			break;
		}
	}
	
	/**
	 * 
	 * 获取上个月或下个月的日期
	 * @param isNext 表示是否下个月 true 为下个月,false 为上个月
	 * @throw
	 * @return void
	 */
	private void getDate(boolean isNext) {
		Date date = DateUtil.getDateForYYYYMM(currentDate);
		Calendar c=Calendar.getInstance();
		c.setTime(date);
		if(isNext){
			c.add(Calendar.MONTH, +1);
		}else{
			c.add(Calendar.MONTH, -1);
		}
		Date time = c.getTime();
		currentDate=DateUtil.getYYYYMMFormatStr(time);
	}

	
	@Override
	protected View onInitView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_salaryget_main, null);
		initView(view);
		initData();
		return view;
	}
	

	@Override
	public void onStop() {
		super.onStop();
		if(getTask!=null && getTask.isCancelled()){
			getTask.cancel(true);
		}
	}

	@Override
	protected void onAsyncLoadData() {
		getCurrentMonthSalary();
	}
	
	private void getCurrentMonthSalary() {
		getTask=new GetPayGetListTask(getActivity(), this);
		getTask.execute("month",currentDate);
	}

	@Override
	public void onRefreshDatas() {
		
	}

	@Override
	public void onTimeout() {
		
	}

	@Override
	public void onSuccess(Object obj, Class cla) {
		if(String.class.equals(cla)){
			new BindSuccessTipsDialog().show(getActivity(),this);
		}else if(SalaryGet.class.equals(cla)){
			list=(ArrayList<SalaryGet>) obj;
			adapter=new SalaryGetAdapter(getActivity(), list,this);
			mListView.setAdapter(adapter);
			if(list!=null && list.size()>0){
				tvText.setVisibility(View.VISIBLE);
				line.setVisibility(View.VISIBLE);
			}else{
				tvText.setVisibility(View.INVISIBLE);
				line.setVisibility(View.INVISIBLE);
			}
		}
	}

	@Override
	public void onPositive() {
		startActivity(new Intent(getActivity(), BankCardActivity.class));
	}

	/**
	 * 重载方法
	 */
	@Override
	public void onPositiveButton() {
		getCurrentMonthSalary();
	}

	@Override
	public void onFailure(Object obj, Class cla) {
		String msg=(String) obj;
		if(msg!=null && msg.contains("未补充")){
			BindBandTipsDialog dialog=BindBandTipsDialog.getInstance(msg);
			dialog.show(getActivity(), this);
		}
	}

	@Override
	public void onClickButton(int position) {//点击签收按钮
		salaryGet=list.get(position);
		if(salaryGet!=null){
			new PayGetTask(getActivity(), this).execute(currentDate,salaryGet.wageid);
		}
	}
}
