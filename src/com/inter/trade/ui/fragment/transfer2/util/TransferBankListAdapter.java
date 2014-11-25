/*
 * @Title:  MyBankListAdapter.java
 * @Copyright:  XXX Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * @Description:  TODO<请描述此文件是做什么的>
 * @author:  MeiYi
 * @data:  2014年8月8日 上午11:37:54
 * @version:  V1.0
 */
package com.inter.trade.ui.fragment.transfer2.util;

import java.util.ArrayList;

import net.tsz.afinal.FinalBitmap;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.inter.trade.R;
import com.inter.trade.data.BankRecordData;
import com.inter.trade.ui.creditcard.data.DefaultBankCardData;
import com.inter.trade.ui.creditcard.util.CreditcardInfoUtil;
import com.inter.trade.util.BankCardUtil;

/**
 * 银行卡列表的适配器
 * @author  ChenGuangChi
 * @data:  2014年8月8日 上午11:37:54
 * @version:  V1.0
 */
public class TransferBankListAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<BankRecordData> mdata;

	public TransferBankListAdapter(Context context,
			ArrayList<BankRecordData> mdata) {
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
		BankRecordData data = mdata.get(position);
		if(data!=null){
			if(convertView==null){
				holder=new ViewHolder();
				convertView = View.inflate(context, R.layout.transfer2_item_bank_list, null);
				holder.tvBankName=(TextView) convertView.findViewById(R.id.tv_bankname);
				holder.tvUserName=(TextView) convertView.findViewById(R.id.tv_username);
				holder.tvNo=(TextView) convertView.findViewById(R.id.tv_no);
				holder.tvCardType=(TextView) convertView.findViewById(R.id.tv_cardtype);
				holder.ivMark=(ImageView) convertView.findViewById(R.id.iv_bank_mark);
				holder.ivDefault=(ImageView) convertView.findViewById(R.id.iv_default);
				convertView.setTag(holder);
			}else{
				holder=(ViewHolder) convertView.getTag();
			}
			String bankno = data.shoucardno;
			holder.tvBankName.setText(data.shoucardbank);
			holder.tvUserName.setText(data.shoucardman);
			if(bankno!= null && bankno.length() >= 12) {
				holder.tvNo.setText("尾号"+(bankno==null ?"":bankno.substring(bankno.length()-4, bankno.length())));
			} else{
				holder.tvNo.setText("卡号有误");
			}
			
//			holder.tvCardType.setText(BankCardUtil.getCardType(data.getBkcardcardtype()));
//			if("1".equals(data.getBkcardisdefault())){
//				holder.ivDefault.setVisibility(View.VISIBLE);
//			}else{
//				holder.ivDefault.setVisibility(View.INVISIBLE);
//			}
//			holder.ivMark.setBackgroundDrawable(context.getResources().getDrawable(CreditcardInfoUtil.getDrawableOfBigBank(data.getBkcardbanklogo())));
			
			if(data.bkcardbanklogo != null && !data.bkcardbanklogo.equals("")) {
				String bkcardbanklogoURL = data.bkcardbanklogo;
				if(data.bkcardbanklogo.startsWith("http")) {
					bkcardbanklogoURL = data.bkcardbanklogo;
				} else{
					bkcardbanklogoURL = "http://"+data.bkcardbanklogo;
				}
				
				FinalBitmap.create(context).display(holder.ivMark, /**"http://images.csdn.net/20140926/QQ0140925164507.jpg"*/bkcardbanklogoURL);
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
		ImageView ivDefault;
	}
}
