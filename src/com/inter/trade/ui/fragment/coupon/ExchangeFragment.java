package com.inter.trade.ui.fragment.coupon;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.inter.trade.R;
import com.inter.trade.ui.CounponActivity;
import com.inter.trade.ui.IndexFunc;
import com.inter.trade.ui.fragment.BaseFragment;
import com.inter.trade.ui.fragment.FragmentFactory;

public class ExchangeFragment extends BaseFragment implements OnClickListener,OnItemClickListener{
	
	
	public ExchangeFragment()
	{
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.mm_list, container,false);
		ListView mListView = (ListView)view.findViewById(R.id.mm_listview);
		mListView.setOnItemClickListener(this);
		mListView.setAdapter(new MyAdapter(getActivity()));
		return view;
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.cridet_back_btn:
			showChuxuka();
			break;

		default:
			break;
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.putExtra(FragmentFactory.INDEX_KEY, 3);
		intent.putExtra("coupon_index", arg2-1);
		intent.setClass(getActivity(), CounponActivity.class);
		getActivity().startActivity(intent);
		
	}

	private void showChuxuka(){
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.func_container, new BuySuccessFragment());
		transaction.commit();
	}
	class MyAdapter extends BaseAdapter{
		private Context mContext;
		private LayoutInflater mInflater;
		private Resources mResources;
		
		public MyAdapter(Context context){
			this.mContext = context;
			mInflater = LayoutInflater.from(mContext);
					mResources=context.getResources();
					;
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return IndexFunc.funcs_image.length;
		}
		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return IndexFunc.funcs_image[arg0];
		}
		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}
		@Override
		public View getView(int arg0, View convertView, ViewGroup arg2) {
			// TODO Auto-generated method stub
			Holder mHolder = null;
			if(convertView == null)
			{
				convertView = mInflater.inflate(R.layout.exchange_item, null);
				mHolder = new Holder();
				mHolder.name = (TextView)convertView.findViewById(R.id.name);
				mHolder.date = (TextView)convertView.findViewById(R.id.date);
				mHolder.count = (TextView)convertView.findViewById(R.id.count);
				convertView.setTag(mHolder);
			}else {
				mHolder = (Holder)convertView.getTag();
			}
			mHolder.name.setText(IndexFunc.funcs_name[arg0]);
			return convertView;
		}
		class Holder{
			TextView name;
			TextView date;
			TextView count;
		}
	}
}
