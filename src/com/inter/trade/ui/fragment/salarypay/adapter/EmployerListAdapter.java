package com.inter.trade.ui.fragment.salarypay.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.inter.trade.R;
import com.inter.trade.ui.fragment.salarypay.bean.EmployerData;

/**
 * 员工工资清单
 * 
 * @author chenguangchi
 * @data: 2014年9月3日 下午4:33:07
 * @version: V1.0
 */
public class EmployerListAdapter extends BaseAdapter {

	private Context context;

	private ArrayList<EmployerData> stuffList;
	
	private EditButtonListener listener;
	
	/**
	 * 是否老板
	 */
	private boolean isBoss;

	public EmployerListAdapter(Context context, ArrayList<EmployerData> mDatas,EditButtonListener listener,boolean isBoss) {
		super();
		this.context = context;
		this.listener=listener;
		this.stuffList=mDatas;
		this.isBoss=isBoss;
	}

	@Override
	public int getCount() {
		if (stuffList != null) {
			return stuffList.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		return stuffList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = View.inflate(context,
					R.layout.item_salarypay_employer, null);
			holder = new ViewHolder();
			holder.tvPhone = (TextView) convertView.findViewById(R.id.tv_phone);
			holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
			holder.tvMoney = (TextView) convertView.findViewById(R.id.tv_money);
			holder.tvNO = (TextView) convertView.findViewById(R.id.tv_id);
			holder.tvRegister = (TextView) convertView.findViewById(R.id.tv_register);
			holder.tvBankCard = (TextView) convertView.findViewById(R.id.tv_cardno);
			holder.btnEdit = (Button) convertView.findViewById(R.id.tv_edit);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		
		holder.btnEdit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(listener!=null){
					listener.onClickEdit(position);
				}
					
			}
		});
		EmployerData employerData = stuffList.get(position);
		holder.tvNO.setText(position+1+"");
		if (employerData.money != null) {
			holder.tvMoney.setText("￥"+employerData.money);
		}
		if (employerData.name != null) {
			holder.tvName.setText(employerData.name);
		}else{
			holder.tvName.setText("匿名");
		}
		if (employerData.phone != null && employerData.phone.length()>=11) {
			holder.tvPhone.setText(employerData.phone.substring(0, 3)+"****"+employerData.phone.substring(employerData.phone.length()-4,employerData.phone.length()));
		}
		if(employerData.hasRegister!=null){
			holder.tvRegister.setText(employerData.hasRegister);
		}
		if(employerData.bankAccount!=null && employerData.bankAccount.length()>0){
			holder.tvBankCard.setText(employerData.bankAccount.substring(0, 4)+"********"+employerData.bankAccount.substring(employerData.bankAccount.length()-4, employerData.bankAccount.length()));
		}else{
			holder.tvBankCard.setText("未绑定银行卡");
		}
		if(isBoss){
			if("0".equals(employerData.canEdit)){//老板的编辑
				holder.btnEdit.setEnabled(false);
			}else{
				holder.btnEdit.setEnabled(true);
			}
		}else{
			if("0".equals(employerData.canEdit)){//财务的编辑
				holder.btnEdit.setEnabled(false);
			}else{
				if("0".equals(employerData.cwcanEdit)){//
					holder.btnEdit.setEnabled(false);
				}else{
					holder.btnEdit.setEnabled(true);
				}
			}
		}
		
		
		return convertView;
	}

	private class ViewHolder {
		TextView tvNO;
		TextView tvPhone;
		TextView tvName;
		TextView tvMoney;
		TextView tvBankCard;
		TextView tvRegister;
		Button btnEdit;
	}
	
	public interface EditButtonListener{
		/**
		 * 点击编辑按钮的时候回执行此方法 
		 * @throw
		 * @return void
		 */
		public void onClickEdit(int position);
	}
}
