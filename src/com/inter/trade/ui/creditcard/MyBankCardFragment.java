package com.inter.trade.ui.creditcard;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
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
import com.inter.trade.ui.creditcard.task.GetBankListTask;
import com.inter.trade.ui.creditcard.util.MyBankListAdapter;
import com.inter.trade.ui.fragment.BaseFragment;
import com.inter.trade.ui.fragment.waterelectricgas.data.ResponseStateListener;

/**
 * 
 * @author xd
 *
 */
public class MyBankCardFragment extends BaseFragment implements OnItemClickListener
,ResponseStateListener,OnClickListener{

	private ListView mListView;
	
	private ArrayList<DefaultBankCardData> mDatas;
	
	private GetBankListTask task;
	
	private int type=1;//1表示信用卡 2 表示储蓄卡
	
	
	public static MyBankCardFragment getInstance(Intent intent){
		MyBankCardFragment f=new MyBankCardFragment();
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
		View view=inflater.inflate(R.layout.activity_my_banklist, null);
		setTitle("我的银行卡");
		setBackVisible();
		initView(view);
		//显示添加按钮
		MyBankCardActivity a=(MyBankCardActivity)getActivity();
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
		DefaultBankCardData defaultBankCardData = mDatas.get(position);
		Intent intent =new Intent();
		intent.putExtra("bankcard", defaultBankCardData);
		getActivity().setResult(78, intent);
		getActivity().finish();
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
