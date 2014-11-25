package com.inter.trade.ui.fragment.order;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.inter.trade.R;
import com.inter.trade.ui.fragment.BaseFragment;
import com.inter.trade.ui.fragment.order.query.OneMonthAgoFragment;
import com.inter.trade.ui.fragment.order.query.OneMonthFragment;
import com.inter.trade.ui.fragment.order.util.OrderData;
import com.inter.trade.ui.fragment.order.util.OrderData.Goods;
import com.inter.trade.ui.fragment.order.util.OrderIndex;
import com.inter.trade.util.LoginUtil;

public class OrderDetial extends BaseFragment{
	private LinearLayout first_layout;
	private LinearLayout second_layout;
	private LinearLayout third_layout;
	private static int mIndex=0;
	private OrderData mOrderData;
	private LayoutInflater mInflater;
	private TextView good_money;
	private TextView good_cost;
	private TextView goodAllmoney;
	
	private static int mWhichType=0;
	
	public static OrderDetial newInstance(int index,int whichType){
		final OrderDetial fragment = new OrderDetial();
		mIndex = index;
		mWhichType = whichType;
		return fragment;
	}
	
	public OrderDetial(){
		
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		if(mWhichType == OrderIndex.ORDER_WAIT_PAY){
			mOrderData = OrderPayFragment.mOrderBean.mOrderDatas.get(mIndex);
		}else if(mWhichType == OrderIndex.ORDER_ONE_MONTH){
			mOrderData = OneMonthFragment.mOrderBean.mOrderDatas.get(mIndex);
		}else if(mWhichType == OrderIndex.ORDER_ONE_MONTH_AGO){
			mOrderData = OneMonthAgoFragment.mOrderBean.mOrderDatas.get(mIndex);
		}
		
		setTitle("订单详情");
		setBackVisible();
		mInflater= LayoutInflater.from(getActivity());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		LoginUtil.detection(getActivity());
		View view = inflater.inflate(R.layout.order_detial_layout, container,false);
		initView(view);
		initFristData();
		initSecData();
		initThird(mOrderData);
		return view;
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setTitle("订单详情");
	}

	private void initView(View view){
		first_layout = (LinearLayout)view.findViewById(R.id.first_layout);
		second_layout =(LinearLayout)view.findViewById(R.id.second_layout);
		third_layout =(LinearLayout)view.findViewById(R.id.third_layout);
		
		good_money = (TextView)view.findViewById(R.id.good_money);
		good_cost = (TextView)view.findViewById(R.id.good_cost);
		goodAllmoney = (TextView)view.findViewById(R.id.goodAllmoney);
	}
	private void initFristData(){
		ArrayList<HashMap<String, String>> temp = initFirst(mOrderData);
		first_layout.removeAllViews();
		for(int i =0;i<temp.size();i++){
			Item item = new Item(temp.get(i));
			first_layout.addView(item.mView);
		}
	}
	private void initSecData(){
		ArrayList<HashMap<String, String>> temp = initSecond(mOrderData);
		second_layout.removeAllViews();
		for(int i =0;i<temp.size();i++){
			Item item = new Item(temp.get(i));
			second_layout.addView(item.mView);
		}
	}
	private ArrayList<HashMap<String, String>> initFirst(OrderData data){	
		ArrayList<HashMap<String, String>> temp = new ArrayList<HashMap<String,String>>();
		
		HashMap<String, String> orderstatus = new HashMap<String, String>();
		orderstatus.put(getString(R.string.order_status), data.orderstate);
		temp.add(orderstatus);
		
		HashMap<String, String> orderNo = new HashMap<String, String>();
		orderNo.put(getString(R.string.order_no), data.orderno);
		temp.add(orderNo);
		
		HashMap<String, String> orderpaytype = new HashMap<String, String>();
		orderpaytype.put(getString(R.string.order_type), data.orderpaytype);
		temp.add(orderpaytype);
		
		HashMap<String, String> ordertime = new HashMap<String, String>();
		ordertime.put(getString(R.string.order_date), data.ordertime);
		temp.add(ordertime);
		return temp;
	}
	
	private ArrayList<HashMap<String, String>> initSecond(OrderData data){	
		ArrayList<HashMap<String, String>> temp = new ArrayList<HashMap<String,String>>();
		
		HashMap<String, String> orderstatus = new HashMap<String, String>();
		orderstatus.put(getString(R.string.order_man), data.shman);
		temp.add(orderstatus);
		
		HashMap<String, String> orderNo = new HashMap<String, String>();
		orderNo.put(getString(R.string.order_company), data.shcmpyname);
		temp.add(orderNo);
		
		HashMap<String, String> orderpaytype = new HashMap<String, String>();
		orderpaytype.put(getString(R.string.zhuangzhang_reciever_phone), "无");
		temp.add(orderpaytype);
		
		HashMap<String, String> ordertime = new HashMap<String, String>();
		ordertime.put(getString(R.string.order_tel), "无");
		temp.add(ordertime);
		
		HashMap<String, String> shaddress = new HashMap<String, String>();
		shaddress.put(getString(R.string.order_address), data.shaddress);
		temp.add(shaddress);
		
		HashMap<String, String> fhstorage = new HashMap<String, String>();
		fhstorage.put(getString(R.string.order_storage), data.fhstorage);
		temp.add(fhstorage);
		
		HashMap<String, String> order_method = new HashMap<String, String>();
		order_method.put(getString(R.string.order_method), data.fhwltype);
		temp.add(order_method);
		
		HashMap<String, String> ordermemo = new HashMap<String, String>();
		ordermemo.put(getString(R.string.order_bak), data.ordermemo);
		temp.add(ordermemo);
		return temp;
	}
	
	private void initThird(OrderData data){	
		third_layout.removeAllViews();
		if(data.mGoods !=null)
		for(Goods good:data.mGoods){
			GoodItem item = new GoodItem(good);
			third_layout.addView(item.mView);
		}
		good_cost.setText(data.fhwlmoney);
		good_money.setText(data.allpromoney);
		goodAllmoney.setText("￥"+data.ordermoney);
	}
	
	class Item {
		public View mView;
		public Item(HashMap<String, String> item){
			mView = mInflater.inflate(R.layout.order_item, null);
			TextView name = (TextView)mView.findViewById(R.id.name);
			TextView content = (TextView)mView.findViewById(R.id.content);
			for(String string : item.keySet()){
				name.setText(string);
				content.setText(item.get(string));
			}
		}
	}
	class GoodItem {
		public View mView;
		public GoodItem(Goods item){
			mView = mInflater.inflate(R.layout.order_good_item, null);
			TextView name = (TextView)mView.findViewById(R.id.goodName);
			TextView content = (TextView)mView.findViewById(R.id.content);
			name.setText(item.proname);
			content.setText(item.proprice+"*"+item.pronum+"="+item.promoney);
		}
	}
}
