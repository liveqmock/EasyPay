package com.inter.trade.ui.fragment.coupon;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.inter.protocol.ProtocolData;
import com.inter.protocol.ProtocolRsp;
import com.inter.protocol.util.ProtocolUtil;
import com.inter.trade.R;
import com.inter.trade.data.CommonData;
import com.inter.trade.data.ResponseData;
import com.inter.trade.error.ErrorUtil;
import com.inter.trade.net.HttpUtil;
import com.inter.trade.ui.CounponActivity;
import com.inter.trade.ui.fragment.BaseFragment;
import com.inter.trade.ui.fragment.FragmentFactory;
import com.inter.trade.ui.fragment.FragmentUtil;
import com.inter.trade.ui.fragment.coupon.activity.CouponListActivity;
import com.inter.trade.ui.fragment.coupon.activity.HTBActivity;
import com.inter.trade.ui.fragment.coupon.parser.CouponListParser;
import com.inter.trade.ui.fragment.coupon.task.GetPayUrlTask;
import com.inter.trade.ui.fragment.coupon.util.ShopData;
import com.inter.trade.ui.fragment.coupon.util.UrlData;
import com.inter.trade.ui.fragment.coupon.util.ShopData.Coupon;
import com.inter.trade.ui.fragment.waterelectricgas.data.ResponseStateListener;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.NumberFormatUtil;
import com.inter.trade.util.PromptUtil;

public class CouponFirstFragment extends BaseFragment implements
		OnClickListener, OnCheckedChangeListener,ResponseStateListener{
	private Button cridet_confirm_btn;
	public static ShopData mShopData = null;
	private TextView shop_text;
	private RelativeLayout select_coupon;
	private TextView money_tv;
	private EditText coupon_count;
	private ImageView reduce;
	private ImageView plus;
	private int mCount = 0;
	private double mPrice = 0;
	private String mId;
	private CouponTask mCouponTask;

	/** 支付方式的选择 */
	private RadioGroup rgPay;

	/****
	 * 记录是否选择默认支付方式
	 */
	private boolean isDefault = true;
	
	private int maxValue=5000;

	public CouponFirstFragment() {
		mCouponTask = new CouponTask();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		LoginUtil.detection(getActivity());
		mCouponTask.execute("");
		setTitle("商户收款");
		setRightVisible(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				FragmentUtil.startFuncActivity(getActivity(),
						FragmentFactory.BUY_RECORD_INDEX);
			}
		}, "历史记录");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.coupon_first_layout, container,
				false);
		cridet_confirm_btn = (Button) view.findViewById(R.id.cridet_back_btn);
		select_coupon = (RelativeLayout) view.findViewById(R.id.select_coupon);
		money_tv = (TextView) view.findViewById(R.id.money_tv);
		coupon_count = (EditText) view.findViewById(R.id.coupon_count);
		reduce = (ImageView) view.findViewById(R.id.reduce);
		plus = (ImageView) view.findViewById(R.id.plus);

		rgPay = (RadioGroup) view.findViewById(R.id.rg_pay);
		rgPay.setOnCheckedChangeListener(this);
		rgPay.getChildAt(0).performClick();

		// coupon_count.setSelection(1);
		cridet_confirm_btn.setOnClickListener(this);
		reduce.setOnClickListener(this);
		plus.setOnClickListener(this);
		// select_coupon.setOnClickListener(this);//禁止选择额度
		shop_text = (TextView) view.findViewById(R.id.shop_text);
		mPrice = 1;// 抵用劵额度默认设为1元
		mId = 59 + "";

		return view;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setTitle("商户收款");
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.cridet_back_btn:
			if (mShopData == null) {
				PromptUtil.showToast(getActivity(), getActivity().getResources().getString(R.string.net_error));
				return;
			}
			if (mShopData != null && mShopData.isshop != null
					&& mShopData.isshop.equals("1")) {
				if (coupon_count.getText().toString().equals("")
						|| coupon_count.getText().toString() == null) {
					PromptUtil.showToast(getActivity(), "请输入收款金额");
					return;
				}
				mCount = Integer.parseInt(coupon_count.getText().toString());
				if (mPrice == 0) {
					PromptUtil.showToast(getActivity(), "请输入收款金额");
					return;
				} else if (mCount == 0) {
					PromptUtil.showToast(getActivity(), "收款金额不能为0");
					return;
				} else if (mCount > maxValue) {
					PromptUtil.showToast(getActivity(), "收款金额不能超过"+maxValue);
					return;
				}
				
				if(isDefault){//默认支付
					showChuxuka();
				}else{//汇通宝
					new GetPayUrlTask(getActivity(),this).execute(mId,mCount+"");
//					Intent intent =new Intent(getActivity(),HTBActivity.class);
//					Bundle b=new Bundle();
//					b.putString("money", mCount+"");
//					b.putString("url", "http://www.baidu.com");
//					b.putString("orderid", "ssss");
//					intent.putExtra("data", b);
//					startActivity(intent);
				}
			} else {

				PromptUtil.showToast(getActivity(), "没有权限购买，请与商家联系");
			}

			break;
		case R.id.select_coupon:
			if (mShopData != null && mShopData.isshop != null
					&& mShopData.isshop.equals("1")) {
				showlist();
			}
			break;
		case R.id.reduce:
			reduce();
			break;
		case R.id.plus:
			plus();
			break;

		default:
			break;
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (mCouponTask != null) {
			mCouponTask.cancel(true);
		}
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		if (mCouponTask != null) {
			mCouponTask.cancel(true);
		}
	}

	private void plus() {
		mCount = Integer.parseInt(coupon_count.getText().toString());
		mCount++;
		if (mCount > 5000) {
			mCount = 5000;
		}
		coupon_count.setText(mCount + "");
		coupon_count.setSelection((mCount + "").length());

	}

	private void reduce() {
		mCount = Integer.parseInt(coupon_count.getText().toString());
		mCount--;
		if (mCount < 0) {
			mCount = 0;
		}
		coupon_count.setText(mCount + "");
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (data == null) {
			return;
		}
		String money = data.getStringExtra("money");
		mId = data.getStringExtra("id");
		if (null != money && !"".equals(money)) {
			mPrice = Double.parseDouble(money);
			money_tv.setText(money);
		}
	}

	/**
	 * 确认付款
	 */
	private void showChuxuka() {
		Intent intent = new Intent();
		intent.setClass(getActivity(), CounponActivity.class);
		intent.putExtra("count", compute());
		intent.putExtra("id", mId);
		intent.putExtra("shopname", mShopData.shopname);
		getActivity().startActivityForResult(intent, 200);
	}

	private double compute() {
		String temp = NumberFormatUtil.round(String.valueOf(mCount * mPrice),
				2, BigDecimal.ROUND_HALF_UP);
		return Double.parseDouble(temp);
	}

	private void showlist() {
		// FragmentManager manager = getActivity().getSupportFragmentManager();
		// FragmentTransaction transaction = manager.beginTransaction();
		// transaction.add(R.id.func_container,
		// CouponListFragment.instance(mShopData.mCoupons));
		// transaction.addToBackStack(null);
		// transaction.commit();
		Intent intent = new Intent();
		intent.setClass(getActivity(), CouponListActivity.class);
		startActivityForResult(intent, 1);
	}

	class CouponTask extends AsyncTask<String, Integer, Boolean> {
		ProtocolRsp mRsp;

		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				CommonData data = new CommonData();
				data.putValue("authorid", LoginUtil.mLoginStatus.authorid);
				List<ProtocolData> mDatas = ProtocolUtil.getRequestDatas(
						"ApiCouponInfo", "readcouponinfo", data);
				CouponListParser authorRegParser = new CouponListParser();
				mRsp = HttpUtil.doRequest(authorRegParser, mDatas);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				mRsp = null;
			}

			return null;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			PromptUtil.dissmiss();
			if (mRsp == null) {
				PromptUtil.showToast(getActivity(),
						getString(R.string.net_error));
			} else {
				try {
					List<ProtocolData> mDatas = mRsp.mActions;
					parserResoponse(mDatas);

					if (!ErrorUtil.create().errorDeal(LoginUtil.mLoginStatus,
							getActivity())) {

						return;
					}
					System.out.print("");

					if (LoginUtil.mLoginStatus.result
							.equals(ProtocolUtil.SUCCESS)) {
						if (mShopData.isshop.equals("1")) {
							initResult();
						} else {
							PromptUtil.showToast(getActivity(),
									LoginUtil.mLoginStatus.message);
						}
					} else {
						PromptUtil.showToast(getActivity(),
								LoginUtil.mLoginStatus.message);
					}
				} catch (Exception e) {
					// TODO: handle exception
					PromptUtil.showToast(getActivity(),
							getString(R.string.req_error));
				}

			}

		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			PromptUtil.showDialog(getActivity(), getActivity().getResources()
					.getString(R.string.loading));
		}

	}

	private void initResult() {
		shop_text.setText(mShopData.shopname);
	}

	/**
	 * 解析响应体
	 * 
	 * @param params
	 */
	private void parserResoponse(List<ProtocolData> params) {
		ResponseData response = new ResponseData();
		LoginUtil.mLoginStatus.mResponseData = response;
		for (ProtocolData data : params) {
			if (data.mKey.equals(ProtocolUtil.msgheader)) {
				ProtocolUtil.parserResponse(response, data);
			} else if (data.mKey.equals(ProtocolUtil.msgbody)) {

				mShopData = new ShopData();
				List<ProtocolData> result1 = data.find("/result");
				if (result1 != null) {
					LoginUtil.mLoginStatus.result = result1.get(0).mValue;
				}

				List<ProtocolData> message = data.find("/message");
				if (message != null) {
					LoginUtil.mLoginStatus.message = message.get(0).mValue;
				}

				List<ProtocolData> shopname = data.find("/shopname");
				if (shopname != null) {
					mShopData.shopname = shopname.get(0).mValue;
				}

				List<ProtocolData> isshop = data.find("/isshop");
				if (isshop != null) {
					mShopData.isshop = isshop.get(0).mValue;
				}

				List<ProtocolData> aupic = data.find("/msgchild");
				for (ProtocolData child : aupic) {
					Coupon picData = new ShopData.Coupon();
					if (child.mChildren != null && child.mChildren.size() > 0) {
						Set<String> keys = child.mChildren.keySet();
						for (String key : keys) {
							List<ProtocolData> rs = child.mChildren.get(key);
							for (ProtocolData item : rs) {
								if (item.mKey.equals("couponid")) {
									picData.couponid = item.mValue;

								} else if (item.mKey.equals("couponmoney")) {
									picData.couponmoney = item.mValue;
								} else if (item.mKey.equals("couponlimitnum")) {
									picData.couponlimitnum = item.mValue;
								}
							}
						}
					}

					mShopData.mCoupons.add(picData);
				}
			}
		}
		for(Coupon c:mShopData.mCoupons){
			if("59".equals(c.couponid)){
				maxValue=Integer.parseInt(c.couponlimitnum);
				break;
			}
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.rb_default:// 默认支付
			isDefault=true;
			break;
		case R.id.rb_huitongbao://汇通宝
			isDefault=false;
			break;

		default:
			break;
		}
	}

	@Override
	public void onSuccess(Object obj, Class cla) {
		UrlData url=(UrlData) obj;
		if(url!=null){
			Intent intent =new Intent(getActivity(),HTBActivity.class);
			Bundle b=new Bundle();
			b.putString("money", mCount+"");
			b.putString("url", url.getUrl());
			b.putString("orderid", url.getOrderNo());
			intent.putExtra("data", b);
			startActivityForResult(intent, 20);
		}
	}
}
