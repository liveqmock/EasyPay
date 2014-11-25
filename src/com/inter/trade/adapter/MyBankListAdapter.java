package com.inter.trade.adapter;

import java.util.ArrayList;

import net.tsz.afinal.FinalBitmap;

import com.inter.trade.R;
import com.inter.trade.data.MyBankListData;
import com.inter.trade.ui.IndexFunc;
import com.inter.trade.ui.creditcard.util.CreditcardInfoUtil;
import com.inter.trade.ui.fragment.cridet.data.CridetHistoryData;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Q币充值记录Adapter
 * @author Lihaifeng
 *
 */
public class MyBankListAdapter extends BaseAdapter{
	private Context mContext;
	private LayoutInflater mInflater;
	private Resources mResources;
	private ArrayList<MyBankListData> mArrayList;
	
	public MyBankListAdapter(Context context,ArrayList<MyBankListData> datas){
		this.mContext = context;
		mInflater = LayoutInflater.from(mContext);
		mResources=context.getResources();
		mArrayList= datas;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mArrayList.size();
	}
	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return mArrayList.get(arg0);
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
			convertView = mInflater.inflate(R.layout.mybank_list_layout, null);
			mHolder = new Holder();
			mHolder.bank_name = (TextView)convertView.findViewById(R.id.bank_name);
			mHolder.person_name = (TextView)convertView.findViewById(R.id.person_name);
			mHolder.bankCard_id4 = (TextView)convertView.findViewById(R.id.bankCard_id4);
			mHolder.bankCard_type = (TextView)convertView.findViewById(R.id.bankCard_type);
			mHolder.img_bank_logo = (ImageView)convertView.findViewById(R.id.img_bank_logo);
//			mHolder.bankCard_default = (ImageView)convertView.findViewById(R.id.bankCard_default);
			mHolder.bankCard_default = (LinearLayout)convertView.findViewById(R.id.bankCard_default);
			mHolder.bankCard_default_receive = (LinearLayout)convertView.findViewById(R.id.bankCard_default_receive);
			convertView.setTag(mHolder);
		}else {
			mHolder = (Holder)convertView.getTag();
		}
		MyBankListData data = mArrayList.get(arg0);
		
		mHolder.bank_name.setText(data.bkcardbank);
		mHolder.person_name.setText(data.bkcardbankman);
		
		String no = data.bkcardno;
		if(no != null && (no.length()-4) >0){
			no=no.substring(no.length()-4);
			mHolder.bankCard_id4.setText("尾号"+no);
		}
		mHolder.bankCard_type.setText(data.bkcardcardtype);

		/**
		 * 默认支付卡(旧接口，并存兼容)
		 */
		if(data.bkcardisdefault!=null){
			if("1".equals(data.bkcardisdefault)){
				mHolder.bankCard_default.setVisibility(View.VISIBLE);
			}else {
				mHolder.bankCard_default.setVisibility(View.GONE);
			}
		}
		
		/**
		 * 默认支付卡
		 */
		if(data.bkcardfudefault!=null){
			if("1".equals(data.bkcardfudefault)){
				mHolder.bankCard_default.setVisibility(View.VISIBLE);
			}else {
				mHolder.bankCard_default.setVisibility(View.GONE);
			}
		}
		
		/**
		 * 默认收款卡
		 */
		if(data.bkcardshoudefault!=null){
			if("1".equals(data.bkcardshoudefault)){
				mHolder.bankCard_default_receive.setVisibility(View.VISIBLE);
			}else {
				mHolder.bankCard_default_receive.setVisibility(View.GONE);
			}
		}
		
//		mHolder.img_bank_logo.setBackgroundDrawable(mContext.getResources().getDrawable(CreditcardInfoUtil.getDrawableOfBigBank(data.bkcardbanklogo)));
		/**
		 * 先从本地拿银行LOGO，如没有，则去网络拿
		 */
		int drawableId=CreditcardInfoUtil.getDrawableOfBigBank(data.bkcardbanklogo);
		if(drawableId != R.drawable.bank_big){
			mHolder.img_bank_logo.setBackgroundDrawable(mContext.getResources().getDrawable(drawableId));
		}else if(TextUtils.isEmpty(data.banklogo)){
			mHolder.img_bank_logo.setBackgroundDrawable(mContext.getResources().getDrawable(drawableId));	
		}else{
			FinalBitmap.create(mContext).display(mHolder.img_bank_logo, data.banklogo);
		}
		return convertView;
	}
	class Holder{
		TextView bank_name;
		TextView person_name;
		TextView bankCard_id4;
		TextView bankCard_type;
		ImageView img_bank_logo;
//		ImageView bankCard_default;
		LinearLayout bankCard_default;
		LinearLayout bankCard_default_receive;
	}
}
