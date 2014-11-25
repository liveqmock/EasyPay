package com.inter.trade.adapter;

import java.util.ArrayList;
import java.util.List;

import com.inter.protocol.ProtocolData;
import com.inter.protocol.ProtocolRsp;
import com.inter.protocol.util.ProtocolUtil;
import com.inter.trade.R;
import com.inter.trade.R.color;
import com.inter.trade.R.drawable;
import com.inter.trade.data.CommonData;
import com.inter.trade.data.ResponseData;
import com.inter.trade.error.ErrorUtil;
import com.inter.trade.log.Logger;
import com.inter.trade.net.HttpUtil;
import com.inter.trade.ui.AgentReplenishActivity;
import com.inter.trade.ui.IndexFunc;
import com.inter.trade.ui.fragment.cridet.data.CridetHistoryData;
import com.inter.trade.ui.fragment.agent.AgentReplenishFragment.AgentOrderData;
//import com.inter.trade.ui.fragment.agent.AgentReplenishFragment.OrderStateTask;
import com.inter.trade.ui.fragment.agent.util.AgentRecordParser;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PromptUtil;
import com.inter.trade.ui.fragment.BaseFragment;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 代理商补货订单列表Adapter
 * @author Lihaifeng
 *
 */
public class AgentReplenishAdapter extends BaseAdapter{
	private Context mContext;
	private LayoutInflater mInflater;
	private Resources mResources;
	private ArrayList<AgentOrderData> mArrayList;
	private OrderStateTask mRecordTask;
	private Button mButton;
	private TextView mTextGet;
//	private Holder mHolder = null;
	
	public AgentReplenishAdapter(Context context,ArrayList<AgentOrderData> datas){
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
			convertView = mInflater.inflate(R.layout.agent_replenish_item, null);
			mHolder = new Holder();
			mHolder.agent_replenish_date = (TextView)convertView.findViewById(R.id.agent_replenish_date);
			mHolder.agent_replenish_book = (TextView)convertView.findViewById(R.id.agent_replenish_book);
			mHolder.agent_replenish_send = (TextView)convertView.findViewById(R.id.agent_replenish_send);
			mHolder.agent_replenish_get_tv = (TextView)convertView.findViewById(R.id.agent_replenish_get_tv);
			mHolder.agent_replenish_get = (Button)convertView.findViewById(R.id.agent_replenish_get);
			convertView.setTag(mHolder);
		}else {
			mHolder = (Holder)convertView.getTag();
		}
		AgentOrderData data = mArrayList.get(arg0);
		
//		if(data.imageId != -1 && data.identify!=-1){
		if(data.orderdate == null || data.orderdate =="")
			return convertView;
		
		if(arg0%2 ==0){
			convertView.setBackgroundColor(Color.argb(255, 236, 236, 236));
		}
		else{
			convertView.setBackgroundColor(Color.argb(255, 245, 245, 245));
		}
		
		mHolder.agent_replenish_date.setText(data.orderdate);
		mHolder.agent_replenish_book.setText(data.ordermemo);
		
//		if(data.orderstate == "0" || data.orderstate == "1" ){
//			data.sendState = "0";
//			mHolder.agent_replenish_send.setText("未发货");
//		}else if(data.orderstate == "2"){
//			data.sendState = "1";
//			data.getState  = "0";
//		}else if(data.orderstate == "9"){
//			data.sendState = "1";
//			data.getState  = "1";
//		}
		
//		mHolder.agent_replenish_send.setText(data.orderstate);
//		mHolder.agent_replenish_get.setText(data.orderstate);
		
//		mHolder.agent_replenish_get.setTag(arg0);
		mHolder.agent_replenish_get.setTag(R.id.tag_agent_button, arg0);
		mHolder.agent_replenish_get.setTag(R.id.tag_agent_textview, mHolder.agent_replenish_get_tv);
		mHolder.agent_replenish_get.setOnClickListener(new lvButtonListener(arg0));
//		if(data.sendState =="1" && data.getState =="0"){//已发货，未收货
		if(data.orderstate == "2"){//已发货，未收货
			mHolder.agent_replenish_send.setText("已发");
//			mHolder.agent_replenish_get.setText("收货");
			mHolder.agent_replenish_get.setEnabled(true);
//			mHolder.agent_replenish_get.setTextColor(this.mContext.getResources().getColor(R.color.text_color_white));
//			mHolder.agent_replenish_get.setBackgroundResource(drawable.samll_btn_normal);
			
			mHolder.agent_replenish_get.setVisibility(View.VISIBLE);
			mHolder.agent_replenish_get_tv.setVisibility(View.GONE);
		}
//		else if(data.sendState =="1" && data.getState =="1"){//已发货，已收货
		else if(data.orderstate == "9"){//已发货，已收货
			mHolder.agent_replenish_send.setText("已发");
//			mHolder.agent_replenish_get.setText("已收");
//			mHolder.agent_replenish_get.setEnabled(false);
//			mHolder.agent_replenish_get.setTextColor(this.mContext.getResources().getColor(R.color.text_color_blue));
//			mHolder.agent_replenish_get.setBackgroundDrawable(null);
			
			mHolder.agent_replenish_get.setVisibility(View.GONE);
			mHolder.agent_replenish_get_tv.setVisibility(View.VISIBLE);
		}
		else{
			mHolder.agent_replenish_send.setText("未发货");
//			mHolder.agent_replenish_get.setText("收货");
			mHolder.agent_replenish_get.setEnabled(false);
//			mHolder.agent_replenish_get.setTextColor(this.mContext.getResources().getColor(R.color.text_color_gray));
//			mHolder.agent_replenish_get.setBackgroundResource(drawable.gray_btn);
			
			mHolder.agent_replenish_get.setVisibility(View.VISIBLE);
			mHolder.agent_replenish_get_tv.setVisibility(View.GONE);
		}
		
		return convertView;
	}
	public final class Holder{
		TextView agent_replenish_date;
		TextView agent_replenish_book;
		TextView agent_replenish_send;
		TextView agent_replenish_get_tv;
		Button agent_replenish_get;
	}
	
	class lvButtonListener implements OnClickListener {
        private int position;

        lvButtonListener(int pos) {
            position = pos;
        }
        
        @Override
        public void onClick(View v) {
        	int p=(Integer) v.getTag(R.id.tag_agent_button);
        	mTextGet=(TextView) v.getTag(R.id.tag_agent_textview);
        	mButton=(Button)v;
    		if(mButton==null || mTextGet==null)
    			return;
        	AgentOrderData data = mArrayList.get(p);
//        	if(data.sendState =="1" && data.getState =="0"){
        	if(data.orderstate == "2"){
        		
        		mRecordTask = new OrderStateTask(data);
        		mRecordTask.execute("");
        		
//        		data.getState ="1";
        		
//        		mButton.setBackgroundDrawable(null);
//        		mButton.setText("已收");
//        		mButton.setEnabled(false);
//        		mButton.setTextColor(color.text_color_blue);
        		
    		}
//    		else{
//    			mButton.setText("错误");
//    		}
        }
    }
	
	public class OrderStateTask extends AsyncTask<String, Integer, Boolean> {
		ProtocolRsp mRsp = null;
		FragmentActivity mActivity;
		private String mResultString;
//		private String mOrderid;
		private AgentOrderData mData;
		
		public OrderStateTask(AgentOrderData data) {
			// TODO Auto-generated constructor stub
			mData=data;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			PromptUtil.showDialog(mContext, mContext.getResources().getString(R.string.loading));
		}

		@Override
		protected Boolean doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			try {
				CommonData data = new CommonData();
				
				data.putValue("orderid",mData.orderid+"");
				List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas("ApiAgentInfo", "agentorderstaterq",
						data);
				AgentRecordParser recordParser = new AgentRecordParser();
				mRsp = HttpUtil.doRequest(recordParser, mDatas);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				Logger.e(e);
			}
			return null;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			PromptUtil.dissmiss();
			try {
			if (mRsp == null) {
				PromptUtil.showToast(mActivity, mActivity.getString(R.string.net_error));
			} else {
				
					List<ProtocolData> mDatas = mRsp.mActions;
					parserResponseOrder(mDatas);
//					if(!ErrorUtil.create().errorDeal(LoginUtil.mLoginStatus, AgentReplenishActivity.class)){
//						return;
//					}
					
					if(LoginUtil.mLoginStatus.result.equals(ProtocolUtil.SUCCESS)){
//						mButton.setBackgroundDrawable(null);
//		        		mButton.setText("已收");
//		        		mButton.setEnabled(false);
//		        		mButton.setTextColor(mContext.getResources().getColor(R.color.text_color_blue));
		        		mData.orderstate = "9";
		        		
		        		mButton.setVisibility(View.GONE);
		        		mTextGet.setVisibility(View.VISIBLE);
					}

			}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}

	private void parserResponseOrder(List<ProtocolData> mDatas) {
		ResponseData response = new ResponseData();
		LoginUtil.mLoginStatus.mResponseData = response;
		for (ProtocolData data : mDatas) {
			if (data.mKey.equals(ProtocolUtil.msgheader)) {
				ProtocolUtil.parserResponse(response, data);

			} else if (data.mKey.equals(ProtocolUtil.msgbody)) {

				List<ProtocolData> result = data.find("/result");
				if (result != null) {
					LoginUtil.mLoginStatus.result = result.get(0).mValue;
				}
				
				List<ProtocolData> message = data.find("/message");
				if (message != null) {
					LoginUtil.mLoginStatus.message = result.get(0).mValue;
				}
				
			}
		}
	}
	
}
