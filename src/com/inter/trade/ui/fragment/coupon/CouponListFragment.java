package com.inter.trade.ui.fragment.coupon;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.inter.protocol.ProtocolData;
import com.inter.protocol.ProtocolParser;
import com.inter.protocol.ProtocolRsp;
import com.inter.protocol.body.ReadBankListParser;
import com.inter.protocol.util.ProtocolUtil;
import com.inter.trade.R;
import com.inter.trade.adapter.BankListAdapter;
import com.inter.trade.adapter.CouponListAdapter;
import com.inter.trade.data.BankData;
import com.inter.trade.data.ResponseData;
import com.inter.trade.log.Logger;
import com.inter.trade.net.HttpUtil;
import com.inter.trade.ui.BankListActivity;
import com.inter.trade.ui.fragment.BaseFragment;
import com.inter.trade.ui.fragment.coupon.activity.CouponListActivity;
import com.inter.trade.ui.fragment.coupon.util.ShopData;
import com.inter.trade.ui.fragment.coupon.util.ShopData.Coupon;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PromptUtil;

public class CouponListFragment extends BaseFragment implements
		OnItemClickListener {

	private ListView mListView;
	private BankListAdapter mAdapter;

	public static CouponListFragment instance(ArrayList<Coupon> temp) {
		CouponListFragment fragment = new CouponListFragment();
		return fragment;
	}

	public CouponListFragment() {

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
		View view = inflater.inflate(R.layout.bank_list, container, false);
		mListView = (ListView) view.findViewById(R.id.bank_listview);
		mListView.setOnItemClickListener(this);
		setBackVisible();
		setTitle("抵用券金额列表");
		mListView.setAdapter(new CouponListAdapter(getActivity(),
				CouponFirstFragment.mShopData.mCoupons));

		return view;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		Coupon data = CouponFirstFragment.mShopData.mCoupons.get(arg2 - 1);
		if (getActivity() instanceof CouponListActivity) {
			((CouponListActivity) getActivity()).money = data.couponmoney;
			((CouponListActivity) getActivity()).id = data.couponid;
			// ((BankListActivity)getActivity()).bankid = data.bankid;
		}
		getActivity().finish();
	}

}
