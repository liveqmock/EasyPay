package com.inter.trade.ui;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.inter.protocol.ProtocolData;
import com.inter.protocol.ProtocolRsp;
import com.inter.protocol.body.BankRecordParser;
import com.inter.protocol.util.ProtocolUtil;
import com.inter.trade.R;
import com.inter.trade.data.BankRecordData;
import com.inter.trade.data.CommonData;
import com.inter.trade.data.ResponseData;
import com.inter.trade.error.ErrorUtil;
import com.inter.trade.net.HttpUtil;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PromptUtil;
import com.inter.trade.util.UserInfoCheck;

public class BankRecordListActivity extends BaseActivity implements OnItemClickListener{
	private ListView bank_listview;
	private ArrayList<BankRecordData> bankRecordDatas = new ArrayList<BankRecordData>();
	private String mType="";
	public static final String TYPE_KEY_STRING="TYPE_KEY_STRING";
	public static final String BABK_ITEM_VALUE_STRING="BABK_ITEM_VALUE_STRING";
	private BankRecordTask mBankTask;
	private LayoutInflater mInflater;
	public  BankRecordData mSelecteData=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bankrecordlist_layout);
		if(!LoginUtil.isLogin){
			PromptUtil.showLogin(this);
			finish();
			return;
		}
		bank_listview = (ListView)findViewById(R.id.bank_listview);
		mType= getIntent().getStringExtra(TYPE_KEY_STRING);
		if(null == mType|| "".equals(mType)){
			mType= TYPECLASS.creditcard;
		}
		setBackVisible();
		setTitle(getMyTitle());
		mInflater = LayoutInflater.from(this);
		mBankTask = new BankRecordTask();
		mBankTask.execute();
		
	}
	@Override
	public void finish() {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.putExtra(BABK_ITEM_VALUE_STRING, mSelecteData);
		setResult(2,intent);
		super.finish();
		
	}
	
	
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		mSelecteData = bankRecordDatas.get(arg2);
		finish();
		
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(mBankTask!=null){
			mBankTask.cancel(true);
		}
	}



	class BankRecordTask extends AsyncTask<String, Integer, Boolean>{
		private ProtocolRsp mRsp;
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
//			UPLOAD_URL = LoginUtil.mLoginStatus.mUserInfo.mPicData.get(0).uploadurl;
			try {
				
					List<ProtocolData> mDatas = getRequestDatas();
					BankRecordParser authorRegParser = new BankRecordParser();
					
					mRsp = HttpUtil.doRequest(authorRegParser, mDatas);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			PromptUtil.dissmiss();
			try {
				if(mRsp != null){
					List<ProtocolData> datas = mRsp.mActions;
					parserResoponse(datas);
					
					if(!ErrorUtil.create().dealError(LoginUtil.mLoginStatus,BankRecordListActivity.this)){
						return;
					}
					RecordAdapter recordAdapter = new RecordAdapter();
					bank_listview.setAdapter(recordAdapter);
					bank_listview.setOnItemClickListener(BankRecordListActivity.this);
//					mAdapter = new BankListAdapter(BankRecordListActivity.this, mBankDatas);
//					mListView.setAdapter(mAdapter);
				}else {
					PromptUtil.showToast(BankRecordListActivity.this,"服务器繁忙,请稍后再试");
				}
			} catch (Exception e) {
				// TODO: handle exception
				PromptUtil.showToast(PayApp.pay,getString(R.string.net_error));
			}
			
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			PromptUtil.showDialog(BankRecordListActivity.this,BankRecordListActivity.this.getResources().getString(R.string.loading));
		}
		
	}
	
	/**
	 * 解析响应体
	 * @param params
	 */
	private void parserResoponse(List<ProtocolData> params){
		ResponseData response = new ResponseData();
		LoginUtil.mLoginStatus.mResponseData = response;
		bankRecordDatas.clear();
		for(ProtocolData data :params){
			if(data.mKey.equals(ProtocolUtil.msgheader)){
				ProtocolUtil.parserResponse(response, data);
				
			}else if(data.mKey.equals(ProtocolUtil.msgbody)){
				List<ProtocolData> result1 = data.find("/result");
				if(result1 != null){
					LoginUtil.mLoginStatus.result = result1.get(0).mValue;
				}
				List<ProtocolData> message = data.find("/message");
				if(message != null){
					LoginUtil.mLoginStatus.message = message.get(0).mValue;
				}
				
				
				List<ProtocolData> aupic = data.find("/msgchild");
				if(aupic!=null)
				for(ProtocolData child:aupic){
					BankRecordData picData = new BankRecordData();
					if (child.mChildren != null && child.mChildren.size() > 0) {
						Set<String> keys = child.mChildren.keySet();
						for(String key:keys){
							List<ProtocolData> rs = child.mChildren.get(key);
							for(ProtocolData item: rs){
								if(item.mKey.equals("shoucardid")){
									picData.shoucardid  = item.mValue;
									
								}else if(item.mKey.equals("shoucardno")){
									picData.shoucardno  = item.mValue;
									
								}else if(item.mKey.equals("shoucardbank")){
									
									picData.shoucardbank  = item.mValue;
								}else if(item.mKey.equals("shoucardman")){
									
									picData.shoucardman  = item.mValue;
								}else if(item.mKey.equals("shoucardmobile")){
									
									picData.shoucardmobile  = item.mValue;
								}else if(item.mKey.equals("paytype")){
									
									picData.paytype  = item.mValue;
								}else if(item.mKey.equals("bankid")){
									
									picData.bankid  = item.mValue;
								}
							}
						}
					}
					
					bankRecordDatas.add(picData);
				}
				
			}
		}
	}
	
	/**
	 * 请求修改身份信息
	 * @return
	 */
	private List<ProtocolData> getRequestDatas(){
		CommonData data = new CommonData();
//		data.putValue("authorid", LoginUtil.mLoginStatus.authorid);
		data.putValue("paytype", mType);
		
		List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas("ApiPayinfo",
				"readshoucardList", data);
		
		return mDatas;
	}
	
	private String getMyTitle(){
		if(mType.equals(TYPECLASS.creditcard)){
			return "历史账户";
		}else if(mType.equals(TYPECLASS.repay)){
			return getResources().getString(R.string.cridet_title);
		}else if(mType.equals(TYPECLASS.order)){
			return getResources().getString(R.string.order_title);
			
		}else if(mType.equals(TYPECLASS.tfmg)){
			return getResources().getString(R.string.zhuangzhang_title);
		}else if(mType.equals(TYPECLASS.suptfmg)){
			return getResources().getString(R.string.suptfmg_title);
		}
		return "";
	}
	public static  class TYPECLASS{
		public static String creditcard="creditcard";//信用卡还款
		public static String repay="repay";//还贷款
		public static String order="order";//订单付款
		public static String tfmg="tfmg";//转账汇款
		public static String suptfmg="suptfmg";//超级转账
	}
	
	class RecordAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return bankRecordDatas.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return bankRecordDatas.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder=null;
			if(convertView==null){
				convertView = mInflater.inflate(R.layout.bank_record_item, null);
				holder = new ViewHolder();
				holder.mbankcardno = (TextView)convertView.findViewById(R.id.mbankcardno);
				holder.mrecordname = (TextView)convertView.findViewById(R.id.mrecordname);
				holder.mrecordmanname = (TextView)convertView.findViewById(R.id.mrecordmanname);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder)convertView.getTag();
			}
			
			BankRecordData mData = bankRecordDatas.get(position);
			if(null ==mData.shoucardno || "null".equals(mData.shoucardno)){
				return convertView;
			}
			
			//校验收款卡号
			if(!UserInfoCheck.checkBankCard(mData.shoucardno.toString())) {
				holder.mbankcardno.setText("卡号有误");
			} else {
				if(mData.shoucardno.length() >= 12) {
					StringBuffer noBuffer = new StringBuffer();
					noBuffer.append(mData.shoucardno.substring(0, 6));
					noBuffer.append("***");
					noBuffer.append(mData.shoucardno.substring(mData.shoucardno.length()-4, mData.shoucardno.length()));
					holder.mbankcardno.setText(noBuffer.toString());
				}
				/**if(UserInfoCheck.checkBankCard(mData.shoucardno.toString())) {
					StringBuffer noBuffer = new StringBuffer();
					noBuffer.append(mData.shoucardno.substring(0, 6));
					noBuffer.append("***");
					noBuffer.append(mData.shoucardno.substring(mData.shoucardno.length()-4, mData.shoucardno.length()));
					holder.mbankcardno.setText(noBuffer.toString());
				}*/ else {
//					mData.shoucardno = "";//收款卡号设为空
					holder.mbankcardno.setText("");
					
				}
			}
			
//			holder.mbankcardno.setText(mData.shoucardno);
			holder.mrecordmanname.setText(mData.shoucardman);
			holder.mrecordname.setText(mData.shoucardbank);
			
			return convertView;
		}
		
		class ViewHolder{
			public TextView mbankcardno;
			public TextView mrecordname;
			public TextView mrecordmanname;
		}
	}
}
