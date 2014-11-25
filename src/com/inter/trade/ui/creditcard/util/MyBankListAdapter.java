/*
 * @Title:  MyBankListAdapter.java
 * @Copyright:  XXX Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * @Description:  TODO<请描述此文件是做什么的>
 * @author:  MeiYi
 * @data:  2014年8月8日 上午11:37:54
 * @version:  V1.0
 */
package com.inter.trade.ui.creditcard.util;

import java.util.ArrayList;

import net.tsz.afinal.FinalBitmap;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.inter.trade.R;
import com.inter.trade.ui.creditcard.data.DefaultBankCardData;
import com.inter.trade.util.BankCardUtil;

/**
 * 银行卡列表的适配器
 * @author  ChenGuangChi
 * @data:  2014年8月8日 上午11:37:54
 * @version:  V1.0
 */
public class MyBankListAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<DefaultBankCardData> mdata;

	public MyBankListAdapter(Context context,
			ArrayList<DefaultBankCardData> mdata) {
		super();
		this.context = context;
		this.mdata = mdata;
	}

	@Override
	public int getCount() {
		if(mdata!=null){
			return mdata.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		return mdata.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder holder=null;
		DefaultBankCardData data = mdata.get(position);
		if(data!=null){
			if(convertView==null){
				holder=new ViewHolder();
				convertView = View.inflate(context, R.layout.item_bank_list, null);
				holder.tvBankName=(TextView) convertView.findViewById(R.id.tv_bankname);
				holder.tvUserName=(TextView) convertView.findViewById(R.id.tv_username);
				holder.tvNo=(TextView) convertView.findViewById(R.id.tv_no);
				holder.tvCardType=(TextView) convertView.findViewById(R.id.tv_cardtype);
				holder.ivMark=(ImageView) convertView.findViewById(R.id.iv_bank_mark);
//				holder.ivDefault=(ImageView) convertView.findViewById(R.id.iv_default);
				holder.bankCard_default = (LinearLayout)convertView.findViewById(R.id.bankCard_default);
				holder.bankCard_default_receive = (LinearLayout)convertView.findViewById(R.id.bankCard_default_receive);
				convertView.setTag(holder);
			}else{
				holder=(ViewHolder) convertView.getTag();
			}
			String bankno = data.getBkcardno();
			holder.tvBankName.setText(data.getBkcardbank());
			holder.tvUserName.setText(data.getBkcardbankman());
			holder.tvNo.setText("尾号"+(bankno==null ?"":bankno.substring(bankno.length()-4, bankno.length())));
			holder.tvCardType.setText(BankCardUtil.getCardType(data.getBkcardcardtype()));
			
			/**
			 * 默认支付卡(旧接口，并存兼容)
			 */
			if(data.getBkcardisdefault()!=null){
				if("1".equals(data.getBkcardisdefault())){
					holder.bankCard_default.setVisibility(View.VISIBLE);
				}else{
					holder.bankCard_default.setVisibility(View.GONE);
				}
			}
			
			/**
			 * 默认支付卡
			 */
			if(data.getBkcardfudefault()!=null){
				if("1".equals(data.getBkcardfudefault())){
					holder.bankCard_default.setVisibility(View.VISIBLE);
				}else{
					holder.bankCard_default.setVisibility(View.GONE);
				}
			}
			
			/**
			 * 默认收款卡
			 */
			if(data.getBkcardshoudefault()!=null){
				if("1".equals(data.getBkcardshoudefault())){
					holder.bankCard_default_receive.setVisibility(View.VISIBLE);
				}else{
					holder.bankCard_default_receive.setVisibility(View.GONE);
				}
			}
			
			/**
			 * 先从本地拿银行LOGO，如没有，则去网络拿
			 */
			int drawableId=CreditcardInfoUtil.getDrawableOfBigBank(data.getBkcardbanklogo());
			if(drawableId != R.drawable.bank_big){
				holder.ivMark.setBackgroundDrawable(context.getResources().getDrawable(drawableId));
			}else if(TextUtils.isEmpty(data.getBanklogo())){
				holder.ivMark.setBackgroundDrawable(context.getResources().getDrawable(drawableId));
			}else{
				FinalBitmap.create(context).display(holder.ivMark, data.getBanklogo());
			}
		}
		return convertView;
	}

	class ViewHolder{
		TextView tvBankName;
		TextView tvUserName;
		TextView tvNo;
		TextView tvCardType;
		ImageView ivMark;
//		ImageView ivDefault;
		LinearLayout bankCard_default;
		LinearLayout bankCard_default_receive;
	}
}
