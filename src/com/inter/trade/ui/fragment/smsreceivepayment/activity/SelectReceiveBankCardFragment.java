package com.inter.trade.ui.fragment.smsreceivepayment.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.inter.trade.R;
import com.inter.trade.ui.creditcard.data.DefaultBankCardData;
import com.inter.trade.ui.creditcard.task.AddBankCardTask2;
import com.inter.trade.ui.creditcard.task.EditBankCardTask;
import com.inter.trade.ui.creditcard.task.GetBankListTask;
import com.inter.trade.ui.creditcard.util.MyBankListAdapter;
import com.inter.trade.ui.fragment.BaseFragment;
import com.inter.trade.ui.fragment.waterelectricgas.data.ResponseStateListener;
import com.inter.trade.util.PromptUtil;

/**
 * 
 * @author xd
 *
 */
public class SelectReceiveBankCardFragment extends BaseFragment implements OnItemClickListener
,ResponseStateListener,OnClickListener{

	private ListView mListView;
	
	private ArrayList<DefaultBankCardData> mDatas;
	
	private GetBankListTask task;
	private DefaultBankCardData defaultBankCardData;
	private int type=1;//1表示信用卡 2 表示储蓄卡
	
	
	public static SelectReceiveBankCardFragment getInstance(Intent intent){
		SelectReceiveBankCardFragment f=new SelectReceiveBankCardFragment();
		Bundle b=new Bundle();
		b.putParcelable("intent", intent);
		f.setArguments(b);
		return f;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		//outState.putSerializable("data", mDatas);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.sms_reveive_banklist, null);
		setTitle("设置默认收款账户");
		setBackVisible();
		initView(view);
		//显示添加按钮
		SelectReceiveBankCardActivity a=(SelectReceiveBankCardActivity)getActivity();
		a.setBtnVisibility(View.VISIBLE);
		
		if(savedInstanceState!=null){
			mDatas=(ArrayList<DefaultBankCardData>) savedInstanceState.getSerializable("data");
		}
		
		Intent intent = getArguments().getParcelable("intent");
		type = intent.getIntExtra("type", 1);
		
		//if(mDatas==null){
			task=new GetBankListTask(getActivity(), this);
			task.execute("");
		/*}else{
			mListView.setAdapter(new MyBankListAdapter(getActivity(),mDatas));
		}*/
		return view;
	}
	
	
	private void initView(View view) {
		mListView=(ListView) view.findViewById(R.id.listview);
		TextView emptyView=(TextView) view.findViewById(R.id.tv_emptyview);
		
		mListView.setOnItemClickListener(this);
		mListView.setEmptyView(emptyView);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.title_right_btn_three://点击添加
			
			break;
		case R.id.title_back_btn://后退按钮
			getActivity().finish();
			break;
		default:
			break;
		}
	}

	@Override
	public void onSuccess(Object obj, Class cla) {
		if(obj==null){
			return;
		}
		
		mDatas=(ArrayList<DefaultBankCardData>) obj;
		if(mDatas!=null){
			//ArrayList<DefaultBankCardData> arrayList = mDatas=getTargetCards(type, mDatas);
			mListView.setAdapter(new MyBankListAdapter(getActivity(),mDatas));
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		defaultBankCardData = mDatas.get(position);
		if(defaultBankCardData != null && bankNameFilter(defaultBankCardData.getBkcardbank())){
			myBankSave();
		}else{
			PromptUtil.showToast(getActivity(), "该银行卡不能作为默认收款卡");
		}
		
//		Intent intent =new Intent();
//		intent.putExtra("bankcard", defaultBankCardData);
//		getActivity().setResult(78, intent);
//		getActivity().finish();
	}
	
	/*
	 * 保存DefaultBankCardData到服务器 task
	 */
	private void myBankSave () {
		if(defaultBankCardData != null){
			//task
			new EditBankCardTask(getActivity(), new ResponseStateListener(){
				
				@Override
				public void onSuccess(Object obj,Class cla){
					/**
					 * 选择某银行卡，然后调接口设置该卡为默认收款卡后，返回短信支付业务页面
					 */
					Intent intent =new Intent();
					intent.putExtra("bankcard", defaultBankCardData);
					getActivity().setResult(78, intent);
					getActivity().finish();
				}
			}
			).execute(defaultBankCardData.getBkcardbankid()+""
			,defaultBankCardData.getBkcardbank()
			,defaultBankCardData.getBkcardno()
			,defaultBankCardData.getBkcardbankman()
			,defaultBankCardData.getBkcardbankphone()
			,defaultBankCardData.getBkcardyxmonth()+""
			,defaultBankCardData.getBkcardyxyear()+""
			,defaultBankCardData.getBkcardcvv()+""
			,defaultBankCardData.getBkcardidcard()+""
			,defaultBankCardData.getBkcardcardtype()
			,defaultBankCardData.getBkcardisdefault()+""
			,defaultBankCardData.getBkcardid()+""
			,defaultBankCardData.getBkcardfudefault()+"", "1");
		}
	}
	
	/**
	 * 默认收款，信用卡过滤六大行
	 * @param bl
	 * @return
	 */
	private boolean bankNameFilter(String bankname){
		String[] sBankArrayList = {"中国工商银行","中国农业银行","中国银行","中国建设银行","中国光大银行","交通银行",};
		boolean flag=true;
		
		for(String s : sBankArrayList) {
			if(s.equals(bankname)) {
				flag=false;
				break;
			}
		}
		
		return flag;
	}
	
	
	/**
	 * 获取某一卡种
	 * 
	 * @return
	 * @throw
	 * @return ArrayList<DefaultBankCardData>
	 */
	private ArrayList<DefaultBankCardData> getTargetCards(int t,ArrayList<DefaultBankCardData> data){
		ArrayList<DefaultBankCardData> mList=new ArrayList<DefaultBankCardData>();
		if(t==1){//信用卡
			for(int i=0;i<data.size();i++){
				if("creditcard".equals(data.get(i).getBkcardcardtype())){
					mList.add(data.get(i));
				}
			}
		}else{//储蓄卡
			for(int i=0;i<data.size();i++){
				if("bankcard".equals(data.get(i).getBkcardcardtype())){
					mList.add(data.get(i));
				}
			}
		}
		
		return mList;
	}
	
}
